
var checkPlugin=function(name){if(navigator.plugins){var check=new RegExp(name,'i');for(var x=0;x<navigator.plugins.length;x++){if(check.test(navigator.plugins[x].name)){return navigator.plugins[x];}}}}
var extractVersion=function(plugin,app){if(plugin.version){return plugin.version;}
else{if(app=="acrobat"||app=="sw"){var tokens=plugin.description.split(" ");return tokens[tokens.length-1].replace(/"/g,'');}
else if(app=="flash"){var tokens=plugin.description.split(" ");return tokens[tokens.length-2]+" "+tokens[tokens.length-1];}
else if(app=="qt"||app=="real"||app=="wm"){return"";}
return plugin.description;}}
var checkControl=function(name){if(window.ActiveXObject){try{return new ActiveXObject(name);}
catch(e){}}};var fixReaderVersion=function(ver){var vers=ver.split(/[,=]/);return vers[1];}
var detect=function(){var control=null,plugin=null;control=checkControl('AcroPDF.PDF');if(control){application("Adobe Reader",fixReaderVersion(control.GetVersions()));}
else{control=checkControl('PDF.PdfCtrl');if(control)
application("Adobe Reader",fixReaderVersion(control.GetVersions()));}
plugin=checkPlugin('Adobe Acrobat');if(plugin)
application("Adobe Reader",extractVersion(plugin,"acrobat"));control=checkControl('ShockwaveFlash.ShockwaveFlash');if(control)
application("Adobe Flash",control.GetVariable('$version').substring(4).replace(/,/g,'.'));plugin=checkPlugin('shockwave flash');if(plugin)
application("Adobe Flash",extractVersion(plugin,"flash"));control=checkControl('SWCtl.SWCtl');if(control)
application("Adobe Shockwave",control.ShockwaveVersion(''));plugin=checkPlugin('shockwave for director');if(plugin)
application("Adobe Shockwave",extractVersion(plugin,"sw"));control=checkControl('AgControl.AgControl');if(control){var sv,s_vers=['5.1.20913.019','5.1.20125.013','5.1.10411.011','5.0.61118.11','5.0.60818.0','5.0.60401.0','4.0.60531.015','4.0.603310.019','4.0.60129.014','4.0.51204.015','4.0.50917.028','4.0.50524.0003','4.0.50401.0015','3.0.40624','2.0.31005','2.0.30923','2.0.30523','2.0.30226','1.0.20816'];for(var x=0;x<s_vers.length;x++){if(control.isVersionSupported(s_vers[x])){application("MS Silverlight",s_vers[x]);break;}}}
plugin=checkPlugin('Silverlight Plug-In');if(plugin)
application("MS Silverlight",extractVersion(plugin,"description"));var realPlayerControls=['rmocx.RealPlayer G2 Control','rmocx.RealPlayer G2 Control.1','RealPlayer.RealPlayer(tm) ActiveX Control (32-bit)','RealVideo.RealVideo(tm) ActiveX Control (32-bit)','RealPlayer'];for(var x=0;x<realPlayerControls.length;x++){control=checkControl(realPlayerControls[x]);if(control){application("Real Player",control.GetVersionInfo());break;}}
plugin=checkPlugin("realone player");if(plugin)
application("RealOne Player",extractVersion(plugin,"real"));plugin=checkPlugin("realplayer");if(plugin)
application("Real Player",extractVersion(plugin,"real"));plugin=checkPlugin("realjukebox");if(plugin)
application("Real Jukebox",extractVersion(plugin,"real"));control=checkControl('QuickTime.QuickTime');if(control)
application("Apple QuickTime","");plugin=checkPlugin("quicktime");if(plugin)
application("Apple QuickTime",extractVersion(plugin,"qt"));control=checkControl('WMPlayer.OCX');if(control)
application("Windows Media Player",control.versionInfo);plugin=checkPlugin("Microsoft.*?DRM");if(plugin)
application("Windows Media Player",extractVersion(plugin,"wmp"));var java_list=deployJava.getJREs();if(java_list.length>0){for(var x=0;x<java_list.length;x++){application("Java",java_list[x]);}}
else{try{var t=document.getElementById("checkip");var v=t.getJavaVersion();application("Java",v);}
catch(e){}}
if(typeof(compatability)!="undefined"&&typeof(compatability.id)!="undefined"&&typeof(compatability.getComponentVersion)!="undefined"){var version=undefined;var available=compatability.isComponentInstalled("{89820200-ECBD-11CF-8B85-00AA005B4383}","ComponentID");if(available){version=compatability.getComponentVersion("{89820200-ECBD-11CF-8B85-00AA005B4383}","ComponentID");if(version)
application("Internet Explorer",version.replace(/,/g,'.'));}}
try{application("JScript",ScriptEngineMajorVersion()+"."+ScriptEngineMinorVersion()+"."+ScriptEngineBuildVersion());}
catch(e){}
plugin=checkPlugin("Microsoft Office .*");if(plugin)
application(plugin.name,extractVersion(plugin,"off"));var a=null,b=null,c=null,d=null,e=null;e=checkControl("SharePoint.OpenDocuments.5");a=checkControl("SharePoint.OpenDocuments.4");b=checkControl("SharePoint.OpenDocuments.3");c=checkControl("SharePoint.OpenDocuments.2");d=checkControl("SharePoint.OpenDocuments.1");if(a!=null&&b!=null&&c!=null&&d!=null&&e!=null){application("MS Office 2013","");}
else if(a!=null&&b!=null&&c!=null&&d!=null&&e==null){application("MS Office 2010","");}
else if(a==null&&b!=null&&c!=null&&d!=null&&e==null){application("MS Office 2007","");}
else if(a==null&&b==null&&c!=null&&d!=null&&e==null){application("MS Office 2003","");}
else if(a==null&&b==null&&c==null&&d!=null&&e==null){application("MS Office XP","");}
intip=internalAddress();if(intip=="unknown")
decloak();}
function decloak(){var found=false;var RTCPeerConnection=window.webkitRTCPeerConnection||window.mozRTCPeerConnection;if(!RTCPeerConnection)
return;function setip(newip){if(intip!="unknown"||newip=='0.0.0.0'||newip=='127.0.0.1')
return;found=true;intip=newip;}
function extract(sdp){var temp=sdp.split('\r\n');for(var x=0;x<temp.length;x++){var blah=temp[x].split(' ');if(blah[7]==='host')
setip(blah[4]);}}
var conn=new RTCPeerConnection({iceServers:[]});if(window.mozRTCPeerConnection){conn.createDataChannel('',{reliable:false});}
conn.onicecandidate=function(event){if(event.candidate)
extract(event.candidate.candidate);};conn.createOffer(function(desc){extract(desc.sdp);conn.setLocalDescription(desc);},function(){});}
var internalAddress=function(){if(deployJava.getBrowser()!="MSIE"){try{var socket=new java.net.Socket();socket.bind(new java.net.InetSocketAddress('0.0.0.0',0));socket.connect(new java.net.InetSocketAddress(document.domain,(!document.location.port)?80:document.location.port));address=socket.getLocalAddress().getHostAddress();return address;}
catch(e){}}
try{var tryit=document.getElementById("checkip");return tryit.getMyIPAddress();}
catch(e){}
return"unknown";}
var intip="unknown";var applications=[];var application=function(name,version){applications.push(name+"\t"+version);}