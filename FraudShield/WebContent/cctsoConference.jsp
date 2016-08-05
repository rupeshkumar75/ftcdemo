<%
	String requestCode = request.getParameter("requestCode"); 
	String chatRoomNumber = request.getParameter("roomNumber"); 
	String signalServer = "http://webrtc.tsosecurelogin.com:8080";
	//String signalServer = "http://52.24.23.75:8090";
	//String signalServer = "http://192.168.1.200:9090";
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> <!--skip-->
        <title>EasyRTC Demo: Simple Video+Audio</title>
        <link rel="stylesheet" type="text/css" href="<%=signalServer%>/easyrtc/easyrtc.css" />
    	<script>
        	var chatRoomNumber = '<%=chatRoomNumber%>';
        </script>
        <!--hide-->
        <link rel="stylesheet" type="text/css" href="<%=signalServer%>/demos/css/landing.css" />
        <link rel="stylesheet" type="text/css" href="<%=signalServer%>/demos/css/demo_audio_video_simple.css" />
        <!-- Prettify Code -->
        <script type="text/javascript" src="<%=signalServer%>/demos/js/prettify/prettify.js"></script>
        <script type="text/javascript" src="<%=signalServer%>/demos/js/prettify/loadAndFilter.js"></script>
        <script type="text/javascript" src="<%=signalServer%>/demos/js/prettify/jquery.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=signalServer%>/demos/js/prettify/prettify.css" />

        <!--show-->
        <!-- Assumes global locations for socket.io.js and easyrtc.js -->
        <script src="<%=signalServer%>/socket.io/socket.io.js"></script>
        <script type="text/javascript" src="<%=signalServer%>/easyrtc/easyrtc.js"></script>
        <script type="text/javascript" src="<%=signalServer%>/demos/js/demo_fraudshield_cctso.js"></script>
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet"/>
        <style>    
      		#snapImage {
			 
			    width:400px;
			    float:left;
			    margin-left:10px;
			    background-color: black;
			}

			
			#compareImage {		  
			    width:400px;
			    margin-left:10px;
			    background-color: black;				
			}
      	</style>
    </head>
    <body onload="connect();">
   
		<div id="videos">
        	<video autoplay="autoplay" class="easyrtcMirror" id="selfVideo" muted="muted" volume="0" ></video>
            <div style="position:relative;float:left;">
           		<video autoplay="autoplay" id="callerVideo"></video>
            </div>
            <div id="otherClients" style="display:none"></div>
				<canvas id="snapshot" style="display:none"></canvas>
            </div>
            <hr/>
            <div align="center">
            	<button type="button" class="btn btn-large btn-primary" name="checkFace" onclick="snap();">Facial Comparison</button>
                <button type="button" class="btn btn-large btn-primary" name="hangup" onclick="hangup();">Hangup</button>
                <span style="width:200px;align:right" id="similarity"></span>
            </div>
            <hr/>
            <div id="compareContainer" style="display:none">                             					
					     <img id="compareImage"/>		
				  	     <img id="snapImage"/>
            </div>
            <div id="divWaiting" style="display:none" align="center">                             					
				<img alt="" src="${pageContext.request.contextPath}/images/ajax_loader_blue_128.gif">	
            </div>
    </body>
</html>

<script>
		// Get handles on the video and canvas elements
		var video = document.getElementById('callerVideo');
		var canvas = document.getElementById('snapshot');
		
		// Get a handle on the 2d context of the canvas element
		var context = canvas.getContext('2d');
		var w, h, ratio;

		// Add a listener to wait for the 'loadedmetadata' state so the video's dimensions can be read
		video.addEventListener('loadedmetadata', function() {
			// Calculate the ratio of the video's width to height
			ratio = video.videoWidth / video.videoHeight;
			// Define the required width as 100 pixels smaller than the actual video's width
			w = video.videoWidth - 100;
			// Calculate the height based on the video's width and the ratio
			h = parseInt(w / ratio, 10);
			// Set the canvas width and height to the values just calculated
			canvas.width = w;
			canvas.height = h;
		}, false);

		// Takes a snapshot of the video
		function snap() {

			// Define some vars required later
			// Define the size of the rectangle that will be filled (basically the entire element)
			context.fillRect(0, 0, w, h);
			// Grab the image from the video
			context.drawImage(video, 0, 0, w, h);

			//Send to server for compare
			var dataURL = canvas.toDataURL();
			
			document.getElementById("snapImage").setAttribute('src', dataURL);
			document.getElementById("divWaiting").style.display='block';
			
			//parent.hello(dataURL);
			var url = "${pageContext.request.contextPath}/servlet/renew/faceCompare?requestCode=" + '<%=requestCode%>';
			setTimeout(function(){
				$.ajax({
					type: "POST",
					url: url,
					data: {
					     imgBase64: dataURL
					},
					dataType: "json",
					cache: false,
					async: false,
					success: function(data) {
						document.getElementById("divWaiting").style.display='none';
						var simm = "Similarity : " + data.status + "%";
						$("#similarity").html(simm);
						document.getElementById('compareImage').setAttribute('src', 'data:image/png;base64,' + data.orgFace);
						document.getElementById('snapImage').setAttribute('src', 'data:image/png;base64,' + data.newFace);
						document.getElementById('compareContainer').style.display = "block";
					},
					error: function(xhr, textStatus, errorThrown) {
						document.getElementById("divWaiting").style.display='none';
						alert('Error to retrieve data!');
					}
					});
			}, 1000);
		}
		
		function hangup() {
			easyrtc.hangupAll();
			window.close();
		}
</script>

