<%@ taglib  uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
	String sid = request.getParameter("sid");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Document Scan</title>
    <meta http-equiv="Content-type" content="text/html;charset=UTF-8" />
    <meta http-equiv="Content-Language" content="en-us"/>
    <meta http-equiv="X-UA-Compatible" content="requiresActiveX=true" />
    <meta name="viewport" content="width=device-width, maximum-scale=1.0" />
    <link href="Styles/style.css" type="text/css" rel="stylesheet" />
    <script type="text/javascript">
        window.history.forward();
        function noBack() {
        	window.history.forward();
        }
	</script>
</head>

<body onpageshow="if (event.persisted) noBack();" onunload="">

<div id="container" class="body_Broad_width" style="margin:0 auto;">

<div class="DWTHeader">
    <!-- header.aspx is used to initiate the head of the sample page. Not necessary!-->
    <!-- #include file=includes/PageHead.aspx -->
    <!-- script src="Scripts/PageMenu.js?Type=0"></script-->
</div>

<div id="DWTcontainer" class="body_Broad_width" style="background-color:#ffffff; height:800px; border:0;">
<div id="dwtcontrolContainer" style="height:605px;"></div>
<div id="DWTNonInstallContainerID" style="width:580px;display:none""></div>
<div id="DWTemessageContainer" style="margin-left:50px;width:580px;display:none"></div>

<div id="ScanWrapper" style="margin-top:-600px;width:350px;">
<div id="divScanner" class="divinput">
    <ul class="PCollapse">
        <li>
        <div class="divType"><div class="mark_arrow expanded"></div>Custom Scan</div>
            <div id="div_ScanImage" class="divTableStyle">
                <ul id="ulScaneImageHIDE" >
                    <li style="padding-left: 15px;">
                        <label for="source">Select Source:</label>
                        <select size="1" id="source" style="position:relative;width: 220px;" onchange="source_onchange()">
                            <option value = ""></option>
                        </select></li>
                         <li style="display:none;" id="pNoScanner">
                            <a href="javascript: void(0)" class="ShowtblLoadImage" style="font-size: 11px; background-color:#f0f0f0; position:relative" id="aNoScanner"><b>What if I don't have a scanner/webcam connected?</b>
                        </a></li>
                        <li id="divProductDetail"></li>
                    <li style="text-align:center;">
                        <input id="btnScan" class="bigbutton" style="color:#C0C0C0;" disabled="disabled" type="button" value="Scan" onclick ="acquireImage();"/></li>
                </ul>
            </div>
        </li>
        <li>
        <div class="divType" style="display: none;"><div class="mark_arrow collapsed"></div>Load the Sample Images</div>
        <div id="div_SampleImage" style="display: none;" class="divTableStyle">
            <ul>
                <li><b>Samples:</b></li>
                <li style="text-align: center;">
                    <table style="border-spacing: 2px; width: 100%;">
                        <tr>
                           <td style="width: 33%">
                                <input name="SampleImage3" type="image" src="Images/icon_associate3.png" style="width: 50px;
                                    height: 50px" onclick="loadSampleImage(3);" onmouseover="Over_Out_DemoImage(this,'Images/icon_associate3.png');"
                                    onmouseout="Over_Out_DemoImage(this,'Images/icon_associate3.png');" />
                            </td>
                            <td style="width: 33%">
                                <input name="SampleImage2" type="image" src="Images/icon_associate2.png" style="width: 50px;
                                    height: 50px" onclick="loadSampleImage(2);" onmouseover="Over_Out_DemoImage(this,'Images/icon_associate2.png');"
                                    onmouseout="Over_Out_DemoImage(this,'Images/icon_associate2.png');" />
                            </td>
                             <td style="width: 33%">
                                <input name="SampleImage1" type="image" src="Images/icon_associate1.png" style="width: 50px;
                                    height: 50px" onclick="loadSampleImage(1);" onmouseover="Over_Out_DemoImage(this,'Images/icon_associate1.png');"
                                    onmouseout="Over_Out_DemoImage(this,'Images/icon_associate1.png');" />
                            </td>
                        </tr>
                        <tr>
                           <td>
                                B&W Image
                            </td>
                            <td>
                                Gray Image
                            </td>
                             <td>
                                Color Image
                            </td>

                        </tr>
                    </table>
                </li>
            </ul>
        </div>
    </li>
    <li>
        <div class="divType" style="display:none;"><div class="mark_arrow collapsed"></div>Load a Local Image</div>
        <div id="div_LoadLocalImage" style="display: none" class="divTableStyle">
            <ul>
                <li style="text-align: center; height:35px; padding-top:8px;">
                    <input type="button" value="Load Image" style="width: 130px; height:30px; font-size:medium;" onclick="return btnLoad_onclick()" />
                </li>
            </ul>
        </div>
    </li>

    </ul>

</div>

<div id="divBlank" style="height:20px">
<ul>
    <li></li>
</ul>
</div>

<div id="tblLoadImage" style="visibility:hidden;height:80px">
<ul>
    <li><b>You can:</b><a href="javascript: void(0)" style="text-decoration:none; padding-left:200px" class="ClosetblLoadImage">X</a></li>
</ul>
<div id="notformac1" style="background-color:#f0f0f0; padding:5px;">
<ul>
    <li><img alt="arrow" src="Images/arrow.gif" width="9" height="12"/><b>Install a Virtual Scanner:</b></li>
    <li style="text-align:center;"><a id="samplesource32bit" href="http://www.dynamsoft.com/demo/DWT/Sources/twainds.win32.installer.2.1.3.msi">32-bit Sample Source</a>
        <a id="samplesource64bit" style="display:none;" href="http://www.dynamsoft.com/demo/DWT/Sources/twainds.win64.installer.2.1.3.msi">64-bit Sample Source</a>
        from <a href="http://www.twain.org">TWG</a></li>
</ul>
</div>
</div>

<div id ="divEdit" class="divinput" style="position:relative">
<ul>
    <li><img alt="arrow" src="Images/arrow.gif" width="9" height="12"/><b>Edit Image</b></li>
    <li style="padding-left:9px;"><img src="Images/ShowEditor.png" title= "Show Image Editor" alt="Show Image Editor" id="btnEditor" onclick="btnShowImageEditor_onclick()"/>
    <img src="Images/RotateLeft.png" title="Rotate Left" alt="Rotate Left" id="btnRotateL"  onclick="btnRotateLeft_onclick()"/>
    <img src="Images/RotateRight.png" title="Rotate Right" alt="Rotate Right" id="btnRotateR"  onclick="btnRotateRight_onclick()"/>
    <img src="Images/Rotate180.png" alt="Rotate 180" title="Rotate 180" onclick="btnRotate180_onclick()" />
    <img src="Images/Mirror.png" title="Mirror" alt="Mirror" id="btnMirror"  onclick="btnMirror_onclick()"/>
    <img src="Images/Flip.png" title="Flip" alt="Flip" id="btnFlip" onclick="btnFlip_onclick()"/>
    <img src="Images/ChangeSize.png" title="Change Image Size" alt="Change Image Size" id="btnChangeImageSize" onclick="btnChangeImageSize_onclick();"/>
    <img src="Images/Crop.png" title="Crop" alt="Crop" id="btnCrop" onclick="btnCrop_onclick();"/>
    </li>
</ul>

</div>


<div id="divUpload" class="divinput" style="position:relative">
<ul>
    <li><img alt="arrow" src="Images/arrow.gif" width="9" height="12"/><b>Upload Image</b></li>
     <li style="padding-left:9px;;display:none">
        <label for="txt_fileName">File Name: <input type="text" size="20" id="txt_fileName" /></label></li>

    <li style="padding-left:9px;display:none;">
        <label for="txt_fileName">File Name: <input type="text" size="20" id="txt_fileName" /></label></li>
    <li style="padding-left:9px;display:none">
	    <label for="imgTypejpeg2">
		    <input type="radio" value="jpg" name="ImageType" id="imgTypejpeg2" onclick ="rd_onclick();"/>JPEG</label>
	    <label for="imgTypetiff2">
		    <input type="radio" value="tif" name="ImageType" id="imgTypetiff2" onclick ="rdTIFF_onclick();"/>TIFF</label>
	    <label for="imgTypepng2">
		    <input type="radio" value="png" name="ImageType" id="imgTypepng2" onclick ="rd_onclick();"/>PNG</label>
	    <label for="imgTypepdf2">
		    <input type="radio" value="pdf" name="ImageType" id="imgTypepdf2" onclick ="rdPDF_onclick();"/>PDF</label></li>
    <li style="padding-left:9px;;display:none">
        <label for="MultiPageTIFF"><input type="checkbox" id="MultiPageTIFF"/>Multi-Page TIFF</label>
        <label for="MultiPagePDF"><input type="checkbox" id="MultiPagePDF"/>Multi-Page PDF </label></li>


    <li style="text-align: left">
			Document Type :<br/>
			<select id="idDocType" onchange="showDescription();" style="width: 275px">
				<c:forEach var="entry" items="${applicationScope.identityDocTypes}">
					<option value="${entry.key}"><c:out value="${entry.value}"/></option>
				</c:forEach>
			</select>
			<br/>
			<br/>
			Expiration Date:<br/>
			<input type="text" id="idDocExpiration" name="idDocExpiration" value="" placeholder="Expiration Date" size="20" maxlength="10" /> (mm/dd/yyyy)
			<br/>
			<br/>
			<div id="docDescDiv" style="display:none;">
				Description:
				<input type="text" id="idDocOtherDesc" name="idDocOtherDesc" value="" size="40" maxlength="500" />
			</div>
			<input type="hidden" id="sessionID" value="<%=sid%>"/>
			<br/>
		</li>
		<li style="text-align: center">
		<input id="btnUpload" class="bigbutton" style="color:#6070F0;"  type="button" value="Upload" onclick ="upload();"/>
	</li>

        <!--input id="btnUpload" type="button" value="Upload Image" onclick ="btnUpload_onclick()"/></li-->
</ul>
</div>

<div class="divinput" >
<ul>
 <li><img alt="arrow" src="Images/arrow.gif" width="9" height="12"/><b>Complete</b></li>
	<li style="text-align: center">
		<input id="btnDone" class="bigbutton" style="color:#6070F0;" type="button" value="Close"
			onclick ="window.close();"/>
	</li>
</ul>
</div>


<div id="divSave" class="divinput" style="position:relative;display:none">
<ul>
    <li><img alt="arrow" src="Images/arrow.gif" width="9" height="12"/><b>Save Image</b></li>
    <li style="padding-left:15px;">
        <label for="txt_fileNameforSave">File Name: <input type="text" size="20" id="txt_fileNameforSave"/></label></li>
    <li style="padding-left:12px;">
        <label for="imgTypebmp">
            <input type="radio" value="bmp" name="imgType_save" id="imgTypebmp" onclick ="rdsave_onclick();"/>BMP</label>
	    <label for="imgTypejpeg">
		    <input type="radio" value="jpg" name="imgType_save" id="imgTypejpeg" onclick ="rdsave_onclick();"/>JPEG</label>
	    <label for="imgTypetiff">
		    <input type="radio" value="tif" name="imgType_save" id="imgTypetiff" onclick ="rdTIFFsave_onclick();"/>TIFF</label>
	    <label for="imgTypepng">
		    <input type="radio" value="png" name="imgType_save" id="imgTypepng" onclick ="rdsave_onclick();"/>PNG</label>
	    <label for="imgTypepdf">
		    <input type="radio" value="pdf" name="imgType_save" id="imgTypepdf" onclick ="rdPDFsave_onclick();"/>PDF</label></li>
    <li style="padding-left:12px;">
        <label for="MultiPageTIFF_save"><input type="checkbox" id="MultiPageTIFF_save"/>Multi-Page TIFF</label>
        <label for="MultiPagePDF_save"><input type="checkbox" id="MultiPagePDF_save"/>Multi-Page PDF </label></li>
    <li style="text-align: center">
        <input id="btnSave" type="button" value="Save Image" onclick ="btnSave_onclick()"/></li>
</ul>
</div>



</div>

</div>

 <div class="DWTTail">
    <!-- #include file=includes/PageTail.aspx -->
</div>



</div>

<div id="ImgSizeEditor" style="visibility:hidden; text-align:left;">
<ul>
    <li><label for="img_height"><b>New Height :</b>
        <input type="text" id="img_height" style="width:50%;" size="10"/>pixel</label></li>
    <li><label for="img_width"><b>New Width :</b>&nbsp;
        <input type="text" id="img_width" style="width:50%;" size="10"/>pixel</label></li>
    <li>Interpolation method:
        <select size="1" id="InterpolationMethod"><option value = ""></option></select></li>
    <li style="text-align:center;">
        <input type="button" value="   OK   " id="btnChangeImageSizeOK" onclick ="btnChangeImageSizeOK_onclick();"/>
        <input type="button" value=" Cancel " id="btnCancelChange" onclick ="btnCancelChange_onclick();"/></li>
</ul>
</div>
 <div class="narrow-screen">
        <div class="tips-header"><a href="http://www.dynamsoft.com/Products/WebTWAIN_Overview.aspx"><img src="images/DWT icon logo.png" border="0" /></a><h1>Online Demo</h1></div>
        <div class="tips-desktop"><img src="images/sc-desktop-only.png" border="0" alt="" width="280"/></div>
        <p class="tips">Sorry! <br /> Please visit this page in your desktop web browsers. Thanks! </p>
</div>
<script type="text/javascript" language="javascript" src="Resources/dynamsoft.webtwain.initiate.js?t=150418"></script>
<script type="text/javascript" language="javascript" src="Resources/dynamsoft.webtwain.config.js"></script>
<script type="text/javascript" language="javascript" src="Scripts/online_demo_operation.js"></script>
<script type="text/javascript" language="javascript" src="Scripts/online_demo_initpage.js"></script>
<script type="text/javascript" language="javascript" src="Scripts/jquery.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.maskedinput-1.3.min.js"></script>   
<script src="${pageContext.request.contextPath}/js/moment.min.js"></script>   

<script type="text/javascript">
    $("ul.PCollapse li>div").click(function() {
        if ($(this).next().css("display") == "none") {
            $(".divType").next().hide("normal");
            $(".divType").children(".mark_arrow").removeClass("expanded");
            $(".divType").children(".mark_arrow").addClass("collapsed");
            $(this).next().show("normal");
            $(this).children(".mark_arrow").removeClass("collapsed");
            $(this).children(".mark_arrow").addClass("expanded");
        }
    });
</script>
<script type="text/javascript" language="javascript">
    // Assign the page onload fucntion.
    $(function() {
        pageonload();
    });

    $('#DWTcontainer').hover(function() {
        $(document).bind('mousewheel DOMMouseScroll', function(event) {
            stopWheel(event);
        });
    }, function() {
        $(document).unbind('mousewheel DOMMouseScroll');
    });



		var parentRefreshed = false;

		$(document).ready(function() {
			$.mask.definitions['~']='[+-]';		
			$("#idDocExpiration").mask("99/99/9999");
		});

		function upload() {
			if (!checkIfImagesInBuffer()) {
				alert("Please scan a document to upload");
		        return;
		    }

			if ($("#idDocType").val() == '')
			{
				alert("Please select document type");
				return;
			}
			
			var exDate = $("#idDocExpiration").val();
			if (exDate == '')
			{
				alert("Please provide document expiration date");
				return;
			}
			else
			{			 
				var birthday = moment(exDate, ["MM/DD/YYYY"], true);
				if (!birthday.isValid()) {
					alert("Invalid document expiration date");
					return;
				} else {
					var now = moment();
					if(birthday.isBefore(now)) {
						alert("Document already expired?");
						return;
					}
				}			
			}
			btnUpload_onclick();
		}

		function showDescription() {
			if ($("#idDocType").val() == 'OTHER') {
				$("#docDescDiv").css("display", "inline");
			}
			else{
				$("#idDocOtherDesc").val("");
				$("#docDescDiv").css("display", "none");
			}
		}

		function finishUpload(){
			parentRefreshed = true;
			window.opener.listDocs();
			window.close();
		}

		function handleUnload(){
			if (!parentRefreshed){
				finishUpload();
			}
		}


</script>
</body>
</html>

