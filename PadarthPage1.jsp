<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Padarth</title>

        <style>
        @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;700&display=swap');
        	h4 {
        		color: white;
        		font-size: 30px;
        		font-family: poppins;
        	}
        	        	
        	#main {
				width: 100%;
				height: 100%;
				border: #ccc 1px solid;
				position: relative;
        		top: 70%;        	
        	}
        	
        	body {
        		background-color: #E3F1E2;
			    color: grey;
        		font-size: 25px;
        		font-family: poppins;    
			}
			
			#head1 {
			    cursor: default;
			    position: relative;
			    top : -22%;
			    left : 0%;
			    font-size: 40px;
			    font-family: poppins;
			    letter-spacing: 1px;
			    color :rgb(119, 123, 118);
			}
			
			img { 
			    position: relative;
			    top: -18%;
			    left:34%;
			    width: 30%; 
			    height: 100%;
			    border-radius: 10px;
			    max-height: 300px;
			    max-width: 400px; 
			}
			#leaf{
				position: relative;
			    height:100px;
			    width:100px;
			    top:-4%;
			    left:11%;
			}
			.list {
			    width:500px;
			    height:auto;
			   
			    backdrop-filter: blur(60px);
			    border-radius: 5px;
			    border: 1px #fbf7f7 soild;
			    padding-left: -20px;
			}
			
			
			@media screen and (max-width : 768px) {
			
				#head1 {
					font-size: 35px;
					top: -19%;
				}
				
				#leaf{
				    height:100px;
				    width:100px;
				    top:-4%;
				    left: 32%;
				    border: 0px;
				}
				
				img { 
			    width: 80%;
			    left: 10%;
			    top: -15%; 
				}
				
			}
        	
        </style>

        </head>
        <body>
        
        	<img src="https://raw.githubusercontent.com/KetanB6/Portfolio/43507ea313b4970e7c3f8a06a38962a4988e8b19/leaf.png" width="10px" id="leaf">
            <h2 id = "head1">Padarth</h2>
            
		    <% String imageData = (String) session.getAttribute("imageData"); %>
		    
		    <!-- Ingredient image -->
		    <% if(imageData != null && !imageData.isEmpty()) {%>
		    	<img src = "<%= imageData %>"> 
		    <% } else { %>
		    	<script>
		    		alert("Image upload error!");
		    	</script>    	
		    <% } %>
		   
		    <% String[] name = (String[]) session.getAttribute("name"); %>
		    <% String[] description = (String[]) session.getAttribute("description"); %>
		    <% String[] rating = (String[]) session.getAttribute("rating"); %>
		    
		    <% for(int i = 0; i < name.length; i++){ %>	
		    <br>	    	
		    		<div class = "list">
		    	 	<h2 id="heading"><%= i+1 %>. <%= name[i] %> </h2>
		    	 	 <%= description[i] %> <br>
		    		 <% if(rating[i].equals("safe")){ %>
		    		 <h3>Risk Level: <font color = "green">*Safe*</font></h3>
		    		 <% } else if(rating[i].equals("moderate")) {%>
		    		 <h3>Risk Level: <font color = "orange">*Moderate*</font></h3>
		    		 <% } else {%>
		    		 <h3>Risk Level: <font color = "red">*Harmful*</font></h3>
		    		 <% } %>
		    		 
		    		 </div>
		    <% } %>	    
        </body>
                
</html>