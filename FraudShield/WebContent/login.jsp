<%
	String errorMsg = (String)request.getParameter("errorMsg");
%>
<!DOCTYPE html>
	<!--[if IE 8]>
		<html xmlns="http://www.w3.org/1999/xhtml" class="ie8" lang="en-US">
	<![endif]-->
	<!--[if !(IE 8) ]><!-->
		<html xmlns="http://www.w3.org/1999/xhtml" lang="en-US">
	<!--<![endif]-->
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>TSO Secure Login &rsaquo; Log In</title>
	<link rel='stylesheet' id='buttons-css'  href='css/site/buttons.min.css' type='text/css' media='all' />
	<link rel='stylesheet' id='open-sans-css'  href='css/site/open-scan.css' type='text/css' media='all' />
	<link rel='stylesheet' id='dashicons-css'  href='css/site/dashicons.min.css' type='text/css' media='all' />
	<link rel='stylesheet' id='login-css'  href='css/site/login.min.css?ver=4.3.1' type='text/css' media='all' />


<style type="text/css">
    .login #login h1 a {
        background-image: url('images/Blu-FS-logo-slogan.png');        
    }
</style>


<style type="text/css">

.login label,
#login_error, .login .message,
h1.custom-messege ,
.login #backtoblog a, .login #nav a {
color: ;

}

h1.custom-messege {
	padding-bottom: 10px;
}

.login form {
    background: #eff8fe ;
    background-size: coverd;

}

.login-action-login{
    background: #d2ecfb;
}

h1.custom_mesg_style {
	padding-bottom: 10px;
}

html {
	background:transparent;
}

    /*Only For Mozila Firefox*/

    @-moz-document url-prefix() {

        #login {
        width: 320px;
        padding: 4% 0px 0px;
        margin: auto;
    }
}


</style>

<meta name='robots' content='noindex,follow' />
	</head>
	<body class="login login-action-login wp-core-ui  locale-en-us">
	<div id="login">
		
		<img src="images/flash.jpg" width="300" height="200" border="0"/>
<% if(errorMsg != null) { %>
	<div id="errorMsg" style=" color: red;"><%=errorMsg%></div>
<%} %>
<form name="loginform" id="loginform" action="${pageContext.request.contextPath}/servlet/home/login" method="post" autocomplete="off">
	<p>
		<label for="user_login">Username<br />
		<input type="text" name="username" id="username" class="input" value="" size="20" /></label>
	</p>
	<p>
		<label for="user_pass">Password<br />
		<input type="password" name="password" id="password" class="input" value="" size="20" /></label>
	</p>
		<!-- p class="forgetmenot"><label for="rememberme"><input name="rememberme" type="checkbox" id="rememberme" value="forever"  /> Remember Me</label></p-->
	<p class="submit">
		<input type="submit" name="wp-submit" id="wp-submit" class="button button-primary button-large" value="Log In" />
		<input type="hidden" name="redirect_to" value="https://tsosecurelogin.com/wp-admin/" />
		<input type="hidden" name="testcookie" value="1" />
	</p>
</form>

<p id="nav">
	<a href="${pageContext.request.contextPath}/servlet/home/lost" title="Password Lost and Found">Lost your password?</a>
</p>

<script type="text/javascript">
function wp_attempt_focus(){
	setTimeout( function(){ try{
	d = document.getElementById('user_login');
	d.focus();
	d.select();
	} catch(e){}
	}, 200);
}

wp_attempt_focus();

document.getElementById('username').value = '';
document.getElementById('password').value = '';

if(typeof wpOnload=='function') wpOnload();
</script>

	<p id="backtoblog"><a href="home.jsp" title="Are you lost?">&larr; Back to TSO Secure Login</a></p>

	</div>
		<div class="clear"></div>
	</body>
	</html>
