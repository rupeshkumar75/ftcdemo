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

        <!--hide-->
        <link rel="stylesheet" type="text/css" href="<%=signalServer%>/demos/css/landing.css" />
        <link rel="stylesheet" type="text/css" href="<%=signalServer%>/demos/css/demo_audio_video_simple.css" />
        <script>
        	var chatRoomNumber = '<%=chatRoomNumber%>';
        </script>
        <!-- Prettify Code -->
        <script type="text/javascript" src="<%=signalServer%>/demos/js/prettify/prettify.js"></script>
        <script type="text/javascript" src="<%=signalServer%>/demos/js/prettify/loadAndFilter.js"></script>
        <script type="text/javascript" src="<%=signalServer%>/demos/js/prettify/jquery.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=signalServer%>/demos/js/prettify/prettify.css" />

        <!--show-->
        <!-- Assumes global locations for socket.io.js and easyrtc.js -->
        <script src="<%=signalServer%>/socket.io/socket.io.js"></script>
        <script type="text/javascript" src="<%=signalServer%>/easyrtc/easyrtc.js"></script>
        <script type="text/javascript" src="<%=signalServer%>/demos/js/demo_fraudshield.js"></script>
		<style>    
      		body {       
        		padding-bottom: 40px;
       			 background:url('/images/fraud_shield_logo.png') no-repeat fixed center;
      		}
      	</style>
    </head>
    <body onload="connect();"> 
                    <div id="videos">
                        <video autoplay="autoplay" class="easyrtcMirror" id="selfVideo" muted="muted" volume="0" ></video>
                        <div style="position:relative;float:left;">
                        <video autoplay="autoplay" id="callerVideo"></video>
                        </div>
                        <!-- each caller video needs to be in it's own div so it's close button can be positioned correctly -->
                    </div>
                    <div id="connectControls" style="display:none">
						<div id="iam">Not yet connected...</div>
					    	<br />
					        <strong>Connected users:</strong>
					        <div id="otherClients"></div>
                    </div>
    </body>
</html>
