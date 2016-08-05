var GetParameter = function() {
    var map = new Array();
    var tgs = document.getElementsByTagName('script');
    if (tgs.length <= 0) { return null; }
    var src = tgs.item(tgs.length - 1).src;
    var pos = src.indexOf('?');
    if (-1 == pos) { return null; }
    var paras = src.substring(pos + 1);
    paras = paras.split('&');
    for (var n = 0; n < paras.length; n++) {
        _ParseParameter(map, paras[n]);
    }
    return map;
};

var _ParseParameter = function(map, para) {
    var pos = para.indexOf('=');
    var key = para.substring(0, pos);
    var value = para.substring(pos + 1);
    map[key] = value;
};

var paras = GetParameter();
var paraValue = -1;
if (paras != null)
 paraValue = paras['Type'];


document.write('<div id="divmenu"><div id="menu"><ul><li style="float:left; width:30px; height:40px; line-height:40px; color:#FFF;"></li>');
if (paraValue == 0)
    document.write('<li class="D_menu_item_select" style="width:240px"><div class="menubar_split"></div><a class="nohref" href="webscan.jsp">Document Scanning & Upload</a></li>');
else
    document.write('<li class="D_menu_item" style="width:240px"><div class="menubar_split"></div><a class="nohref" href="webscan.jsp">Document Scanning & Upload</a></li>');

if(paraValue == 1)
    document.write('<li class="D_menu_item_select" style="width:240px"><div class="menubar_split" ></div><a class="nohref" href="webcam.jsp">Webcam Image Capture & Upload</a></li>');
else
    document.write('<li class="D_menu_item" style="width:240px"><div class="menubar_split" ></div><a class="nohref" href="webcam.jsp">Webcam Image Capture & Upload</a></li>');



