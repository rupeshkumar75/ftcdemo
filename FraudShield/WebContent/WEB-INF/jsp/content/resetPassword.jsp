
<div class="container" align="center">
<div class="panel panel-default"  style="margin-top:100px;width:40%;">
	<div class="panel-heading">
		<h3 class="panel-title">Reset Password</h3>
	</div>
	<div class="panel-body">
		<div align="left">
		Please type in your new password. The password must be 8 to 16 characters long, and must have at least 1 number 
		and 1 special character (!@#$%^&*).
		</div>
		<br/>
		<br/>
		<div id="inputPanel" style="display:block">
		<form method="post" id="requestForm" action="${pageContext.request.contextPath}/servlet/home/checkAccount">
		<input type="hidden" name="resetCode" value="${resetCode}"/>
		<table>
			<tr>
				<td>Password : </td>
				<td><input type="password" class="form-control" id="pass1" name="pass1"></td>
			</tr>
			<tr>
				<td>Re-enter Password :</td>
				<td><input type="password" class="form-control" id="pass2" name="pass2"></td>
			</tr>
		</table>	
		<div class="center" align="center" >
			
				<button class="btn btn-large btn-primary" type="button" onclick="submitForm();">Submit</button>
				&nbsp;&nbsp;
				<button class="btn btn-large btn-primary" type="button" onclick="goLogin();">Cancel</button>
		</div>
		</form>
		</div>
	</div>
</div>
</div>


<script>

	var regularExpression = /^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,16}$/;
	
	function submitForm() {
		var pass1 = $("#pass1").val();
		var pass2 = $("#pass2").val();
		
		if(pass1 != pass2) {
			appConfirmDialog('Password and re-enter password don\'t match');
			return ;
		}
		
		if(!regularExpression.test(pass1)) {
			appConfirmDialog('Password is too weak. password should contain atleast one number and one special character.');
			return;
		}
		
		var url = "${pageContext.request.contextPath}/servlet/home/changePassword";
		var myForm = $('#requestForm').serialize();
		
		setTimeout(function(){ // this gives the UI time to update before AJAX locks it up
			$.ajax({
				type: "POST",
				url: url,
				dataType: "json",
				data: myForm,
				cache: false,
				async: false, // to set local variable
				success: function(data) {					
					if(data.status == 'OK') {					
						//message, title, focusElmtIdOnClose, onConfirmFunction, onCancelFunction
						appConfirmDialog('Password is changed successfully', 'Confirm', null, goLogin, null);
						$("#pass1").val('');
						$("#pass2").val('');					
					} else {
						appConfirmDialog("Cannot reset your password.");
					}				
				},
				error: function(xhr, textStatus, errorThrown) {
					appConfirmDialog('Error to reset your password.');
				}
			});
		},500);			
	}
	
	function goLogin() {
		document.location = "${pageContext.request.contextPath}/servlet/home/start";
	}

</script>