<div class="container" style="margin-top:100px;width:40%;">

<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">Request Password Reset</h3>
	</div>
	<div class="panel-body">
		To reset your password, Please enter your email address.
		<p>&nbsp;</p>
		<div id="inputPanel">
				<input type="text" class="form-control" id="email" name="email">
				<br/>
				<div class="center" align="center" >
				<button class="btn btn-large btn-primary" type="button" onclick="submitForm();">Submit</button>
				<button class="btn btn-large btn-primary" type="button" onclick="closePage();">Close</button>
				</div>
		</div>
	</div>
</div>

</div>

<script>

	function submitForm() {
		var email = $("#email").val();
		if(!validateEmail(email)) {
			appConfirmDialog("Please enter a valid email address.");
			return false;
		}
		
		var url = "${pageContext.request.contextPath}/servlet/home/checkAccount?email=" + email;

		setTimeout(function(){ // this gives the UI time to update before AJAX locks it up
			$.ajax({
				type: "GET",
				url: url,
				dataType: "json",
				cache: false,
				async: false, // to set local variable
				success: function(data) {					
					if(data.status == 'OK') {					
						appConfirmDialog('You account was retrieved, Please check your email for instruction');
					} else {
						if(data.code == 'MAIL_ERROR') {
							appConfirmDialog("Cannot send notification email at this moment, please try later.");
						} else if (data.code == 'USER_NOT_FOUND') {
							appConfirmDialog("Cannot find user account associated with this email.");
						} else {
							appConfirmDialog("Cannot reset password at this moment,  please try later.");
						}
					}				
				},
				error: function(xhr, textStatus, errorThrown) {
					appConfirmDialog('Error to reset password');
				}
			});
		},500);					
	}
	
	function closePage (){
		document.location = "${pageContext.request.contextPath}/login.jsp";
		
	}
</script>