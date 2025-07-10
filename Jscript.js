//<!--1. Script for scan ingredient button -->

                const uplod = document.getElementById('uplod');//sub upload button
                const scanBtn = document.getElementById('scan');
                const captureBtn = document.getElementById('capture');
                const webcam = document.getElementById('webcam');
                const canvas = document.getElementById('canvas');
                const capturedImage = document.getElementById('captured-image');
                const retake = document.getElementById('retake');
                const up = document.getElementById('up');//main upload button
                const search = document.getElementById('search');
                const backBtn = document.getElementById('back');
				const falseSubmit = document.getElementById('falseSubmit');
				const bowl = document.getElementById('bowl');
                var imgUrl;         
                let stream = null;
        
                // Open webcam
                async function startWebcam() {
                    try {
                    stream = await navigator.mediaDevices.getUserMedia({
                        video: { facingMode: "environment" }  
                    });
                    webcam.srcObject = stream;
                    webcam.style.display = 'block';
                    captureBtn.style.display = 'inline';
                    scanBtn.style.display = 'none';
                    up.style.visibility = 'hidden';
                    search.style.visibility = 'hidden';
                    backBtn.style.visibility = 'visible';
                } catch (error) {
                    console.error("Error accessing webcam:", error);
                    alert("Camera access denied or unavailable.");
                }
                }       

                scanBtn.addEventListener('click', startWebcam);

                // Capture image
                captureBtn.addEventListener('click', () => {
                    const context = canvas.getContext('2d');
                    canvas.width = webcam.videoWidth;
                    canvas.height = webcam.videoHeight;
                    context.drawImage(webcam, 0, 0, canvas.width, canvas.height);
        
                    capturedImage.src = canvas.toDataURL('image/png');
                    capturedImage.style.display = 'none';
					webcam.style.display = 'none';
					
					bowl.src = capturedImage.src;
                    
                    retake.style.visibility = 'visible';
                    uplod.style.visibility = 'visible';
                    // Stop the webcam stream
                    stream.getTracks().forEach(track => track.stop());
                    captureBtn.style.visibility = "hidden";
                    
                });

                // Retake Image (Restart Camera)
                retake.addEventListener('click', () => {
                    // Clear captured image
                    capturedImage.src = "";
                    capturedImage.style.display = 'none';

                    // Hide retake & upload buttons
                    captureBtn.style.visibility = "visible";

                    // Restart webcam
                    startWebcam();
                });

                uplod.addEventListener('click', () => {
                    // Clear captured image on click retake
                    imgUrl = capturedImage.src;
                    connectionAPI();
                });
				
                function connectionAPI(){
                	fetch("MyServlet", {
            			method: "POST",
            			headers: {"content-type": "application/x-www-form-urlencoded"},
            			body: "data=" + encodeURIComponent(imgUrl)
            		})
                    .then(response => response.text())
                    .then(data => console.log(data))
                    .catch(error => console.error(error));
                }
			
        
				
				
				
           

            //<!--2. Script for upload image button-->
           
            	function hide() {
            		//hide buttons
                    scanBtn.style.visibility = 'hidden';
                    search.style.visibility = 'hidden';
					up.style.visibility = 'hidden';
                    backBtn.style.visibility = 'visible';
					falseSubmit.style.visibility = 'visible';
            	}
            
                //function to hide buttons when user click on main upload image button
				document.addEventListener("DOMContentLoaded", function () {
					
					
                	const iFile = document.getElementById('file');
                	const iForm = document.getElementById('imageFile');
					
                	                	
                	//Convert data into base64 format                	
                	iFile.addEventListener("change", function(event) {
                		const ImageFile = iFile.files[0];
                		if(ImageFile){
                			const reader = new FileReader();
                			reader.onload = function(e) {
                				imgUrl = e.target.result;
								//send image to backend								
                				connectionAPI();
								bowl.src= imgUrl;
								iForm.submit();
                				
                			}
                			reader.readAsDataURL(ImageFile);
                		}
                	})
					});
				
                                    
                	

					
					
					
            //3. Script for search ingredient button
          
                const panel = document.getElementById('panel');
                function pop_panel(){
                    panel.style.visibility='visible';
                    scanBtn.style.visibility='hidden';
                    up.style.visibility='hidden';
                    search.style.visibility='hidden';
                    backBtn.style.visibility = 'visible';
                }
				
				
				

          