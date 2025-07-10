package MyPadarthServerPack;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

//for text extraction
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

//for api integration
import okhttp3.*;
import org.json.JSONObject;
import org.json.JSONArray;


@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    String iUrl;   
    String ingredients;
    String manual;
    private static final String API_key= "XXXXXXXXXXXXXXXXXXXXXXXXXXX";
	private static final String API_url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_key;
	
    public MyServlet() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		extractor();
        request.getSession().setAttribute("ingredients", ingredients);
		manual = request.getParameter("manual_search");
		
		if(manual != null) {
			ingredients = manual;
			request.getSession().setAttribute("imageData", "https://static.vecteezy.com/system/resources/previews/021/249/669/non_2x/food-safety-icons-safe-food-badge-seal-tag-label-sticker-emblem-with-grunge-effect-png.png");
		}
		
		String details = Gemini(ingredients); //api response
		
		
//		//parsing json response
		JSONObject json_object = new JSONObject(details);
		JSONArray candidates = json_object.getJSONArray("candidates");
		JSONObject content = candidates.getJSONObject(0).getJSONObject("content");
		JSONArray parts = content.getJSONArray("parts");
		String textResponse = parts.getJSONObject(0).getString("text");
		
//		remove extra json formating
		textResponse = textResponse.replaceAll("```json", "").replaceAll("```", "").trim();
	
//		//converting string to json
		JSONArray ingredientsArray = new JSONArray(textResponse);
     
//        //Extracting required field
        String[] name = new String[ingredientsArray.length()];
        String[] description = new String[ingredientsArray.length()];
        String[] rating = new String[ingredientsArray.length()];
        
        for(int i = 0; i < ingredientsArray.length(); i++) {
        	JSONObject ingredientData = ingredientsArray.getJSONObject(i);
        	name[i] = ingredientData.getString("name");
        	description[i] = ingredientData.getString("description");
        	rating[i] = ingredientData.getString("rating");
        }
        
     
        
        request.getSession().setAttribute("name", name);
        request.getSession().setAttribute("description", description);
        request.getSession().setAttribute("rating", rating);
		
		 		
        RequestDispatcher dispatcher = request.getRequestDispatcher("PadarthPage1.jsp");
        dispatcher.forward(request, response);
	}
	
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get the image data from the request		)
        String imageData = request.getParameter("data");
        manual = request.getParameter("manual_search");
        
        request.setAttribute("manual", manual);
        iUrl = imageData;
       
        request.getSession().setAttribute("imageData", imageData); //(key, value) for dynamic scope to use all over program 
        
        
        response.sendRedirect("MyServlet");
        
	}
	
	//converting base64 format into File format
	public void extractor() {
		// Remove Base64 prefix if present
	    if (iUrl.contains(",")) {
	        iUrl = iUrl.split(",")[1];
	    }
		String base64Image = iUrl;
		
		byte[] imageBytes = Base64.getDecoder().decode(base64Image);
		
		//creating temporary image file
		File imageFile = new File("temp_image.png");
		try {
			FileOutputStream fos = new FileOutputStream(imageFile);
			fos.write(imageBytes); //writing into imageFile using bytes
			fos.close();
		} catch(Exception e) {
			e.getMessage();
		}
		
		Tesseract tesseract = new Tesseract();
		//path where trained language data present 
		tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
		tesseract.setLanguage("eng");
		try {
			String extractedText = tesseract.doOCR(imageFile);
			extractedText = extractedText.replaceAll("[^a-zA-Z0-9.,\\s]", "").trim();
			if (!extractedText.isEmpty()) {
	            ingredients = extractedText;
	        }
			System.out.println("Ingredients : " + ingredients);
		} catch(TesseractException e) {
			e.getStackTrace();
		}		
	}
	
	public String Gemini(String ingredients) throws IOException {
		//creating a client to make http request
		OkHttpClient client = new OkHttpClient();
		
		String prompt = "Analyze the '"+ ingredients + "' and return a JSON array of recognized ingredients. "
			    + "Each ingredient should be an object with 'name', 'description', and 'rating'. "
				+ "rating should be safe, moderate or harmful"
			    + "Must ignore non-ingredient term "
			    + "Ensure the response is a valid JSON array with NO additional formatting (no Markdown or code blocks).";
		
		//JSON request body - input format for gemini - creating json object
		JSONObject jsonRequest = new JSONObject();
		jsonRequest.put("contents", new JSONObject().put("parts", new JSONObject().put("text", prompt)));
		
		//Create request body - convert json object to request body
		RequestBody body = RequestBody.create(jsonRequest.toString(), MediaType.get("application/json"));
		
		//building http request
		Request request = new Request.Builder().url(API_url).post(body).build();
		
		//Sending api req. and taking response
		try (Response response = client.newCall(request).execute()) {
			if(!response.isSuccessful()) throw new IOException("Unexpected response!");
			String details = response.body().string();
//			System.out.println("Ingredients : " + details);
			return details;
		}
			}
}
