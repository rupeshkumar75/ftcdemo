  <style type="text/css">
      body {       
        padding-bottom: 40px;
        background:url('${pageContext.request.contextPath}/images/fraud_shield_logo.png') no-repeat fixed center;
      }

      .form-signin {
      	
        max-width: 300px;
        padding: 19px 29px 29px;
        margin: 0 auto 20px;
        background-color: #fff;
        border: 1px solid #e5e5e5;
        -webkit-border-radius: 5px;
           -moz-border-radius: 5px;
                border-radius: 5px;
        -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
           -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
                box-shadow: 0 1px 2px rgba(0,0,0,.05);
      }
      .form-signin .form-signin-heading,
      .form-signin .checkbox {
        margin-bottom: 10px;
      }
      .form-signin input[type="text"],
      .form-signin input[type="password"] {
       	font-size: 14px;
        height: auto;
        margin-bottom: 15px;
        padding: 7px 9px;
      }

   </style>
   <p>&nbsp;</p>
   <%
   	String errorMessage = (String)request.getAttribute("error");
   %>
   
   <form class="form-signin" action="${pageContext.request.contextPath}/servlet/home/login" method="post">
   		<%
   		if (errorMessage != null)
   	   {
   		%><font color="red"><%= errorMessage %></font>
   		<%
   	   }
   		%>
        <h3 class="form-signin-heading">Please Log in</h3>
        <input type="text" class="input-block-level" name="username" placeholder="Username"><br/>
        <input type="password" class="input-block-level" name="password" placeholder="Password"><br/>      
        <button class="btn btn-large btn-primary" type="submit">Log in</button>
       
    </form>