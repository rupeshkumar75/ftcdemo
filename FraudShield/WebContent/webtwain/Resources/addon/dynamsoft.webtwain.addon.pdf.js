/*!
* Dynamsoft JavaScript Library
* Product: Dynamsoft Web Twain
* Web Site: http://www.dynamsoft.com
*
* Copyright 2015, Dynamsoft Corporation 
* Author: Dynamsoft R&D Department
*
* Version: 11.1
*
* Module: htm5
* final js: build\addon\dynamsoft.webtwain.addon.pdf.js
*/
;Dynamsoft.PdfVersion="9, 3, 0, 212";(function(a){if(!a.product.bChromeEdition){return}var b=function(d){var e=a.html5.Funs,c;c={PDF:{Download:function(k,n,g){var j=d,l;Dynamsoft.Lib.cancelFrome=2;var i=function(){if(Dynamsoft.Lib.isFunction(n)){n()}return true},o=function(){if(Dynamsoft.Lib.isFunction(g)){g(j.ErrorCode,j.ErrorString)}return false};var f=j._innerFun("GetAddOnVersion",e.makeParams("pdf"));if(f==Dynamsoft.PdfVersion){i();return true}if(!k||k==""){Dynamsoft.Lib.Errors.Pdf_InvalidRemoteFilename(j);o();return false}if(e.isServerInvalid(j)){o();return false}l="get";Dynamsoft.Lib.showProgress(j,"Download",true);var m=function(q){var r=(q.total===0)?100:Math.round(q.loaded*100/q.total),s=[q.loaded," / ",q.total].join("");j._OnPercentDone([0,r,"","http"])},h=true;j._OnPercentDone([0,-1,"HTTP Downloading...","http"]);if(!Dynamsoft.Lib.isFunction(n)){h=false}e.loadHttpBlob(j,l,k,h,function(p){j._OnPercentDone([0,-1,"Loading..."]);var q=100;j.__LoadImageFromBytes(p,q,"",h,i,o)},function(){Dynamsoft.Lib.closeProgress("Download");o()},m)},ConvertToImage:function(j,n,h,k){var l=e.replaceLocalFilename(j);var g="ConvertPDFToImage";var i=function(f){e.hideMask(g);if(h){h()}return true},o=function(f){e.hideMask(g);if(k){k()}return false};e.showMask(g);d._innerSend(g,e.makeParams(l,n),true,i,o);return true}}};d.__addon=d.__addon||{};a.mix(d.__addon,c)};a.DynamicLoadAddonFuns.push(b)})(Dynamsoft.Lib);(function(a){if(!a.product.bPluginEdition&&!a.product.bActiveXEdition){return}var b=function(e){var d=false,c,f;if(e.getSWebTwain()&&e.getSWebTwain().Addon){d=true}if(!d){return false}f=e.getSWebTwain();c={PDF:{Download:function(k,g,i){var j=f.GetAddOnVersion("pdf");if(j==Dynamsoft.PdfVersion){if(Dynamsoft.Lib.isFunction(g)){g()}return true}var h=f.DownloadAddon(k);return a.wrapperRet(e,h,g,i)},ConvertToImage:function(h,k,g,j){var i=f.ConvertPDFToImage(h,k);return a.wrapperRet(e,i,g,j)}}};e.Addon=e.Addon||{};a.mix(e.Addon,c)};a.DynamicLoadAddonFuns.push(b)})(Dynamsoft.Lib);
