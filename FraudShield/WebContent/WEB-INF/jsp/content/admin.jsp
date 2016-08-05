<script type="text/javascript">


	var contextPath = $("#contextPath").val();

	$(document).ready(function() {
		$.mask.definitions['~']='[+-]';
		$("#phone").mask("(9?99) 999-9999");
	});
	
	
	function searchTsoUser(){
		//validate search criteria
		var username = document.getElementById('username').value;
		
		if ($.trim(username) == '')
		{
			var message ='Please provide a search criteria.';
			appConfirmDialog(message,"Search Error" );
			return;
		}		
		
		var myForm = $('#tsoUserSearchForm').serialize();
		var url = contextPath+"/servlet/admin/dosearch";

		setTimeout(function(){ // this gives the UI time to update before AJAX locks it up
			$.ajax({
				type: "POST",
				url: url,
				data: myForm,
				cache: false,
				async: false, // to set local variable
				success: function(data) {
					$('#bodyId').css('cursor','default');
					$('#cmdSearch').css('cursor','default');
					$("#tsoUserDetails").html(data);
				},
				error: function(xhr, textStatus, errorThrown) {
					$('#bodyId').css('cursor','default');
					$('#cmdSearch').css('cursor','default');
				}
			});
		},500);
	}
	
	function changeAccount(lock, userId){
		var url = contextPath+"/servlet/admin/doChangeAccount?userId=" + userId + "&lock=" + lock;
		
		setTimeout(function(){ 
			$.ajax({
				type: "POST",
				url: url,
				cache: false,
				async: false, // to set local variable
				success: function(data) {
					$('#bodyId').css('cursor','default');
					$('#cmdSearch').css('cursor','default');
					$("#tsoUserDetails").html(data);
				},
				error: function(xhr, textStatus, errorThrown) {
					$('#bodyId').css('cursor','default');
					$('#cmdSearch').css('cursor','default');
				}
			});
		},500);
	}
	
		
	function addNewUser(){
	
		$("#username1").val('');
		$("#password1").val('');
		$("#password2").val('');
		$("#firstName").val('');
		$("#lastName").val('');
		$("#email").val('');
		$("#phone").val('');
		$("#role").val('');
		
		 $("#userDetailDialog").dialog({
         	title: "Add New User",
             autoOpen: false,
             draggable: "true",
             modal: true,
             height: 480,
             width: 480
         }); 
		$("#userDetailDialog").dialog('open');
	}
	
	function saveTsoUser(){
		
		var userName = $("#username1").val();
		var pass1 = $("#password1").val();
		var pass2 = $("#password2").val();
		var firstname = $("#firstName").val();
		var lastname = $("#lastName").val();
		var email = $("#email").val();
		var phone = $("#phone").val();
		
		var msg = '';
		if(userName==''){
			msg = msg + "User name is required<br>"; 
		}
		if(pass1=='' || pass2 == ''){
			msg = msg + "Password is required<br>"; 
		}else{
			if(pass1!= pass2){
				msg = msg + "Password missmatch<br>"; 
				$("#password1").val('');
				$("#password2").val('');
			}
		}
		
		if(firstname == ''){
			msg = msg + 'First name is required' + "<br>";
		}
		if(lastname == ''){
			msg = msg + 'Last name is required' + "<br>";
		}
		
		if(phone == ''){
			msg = msg + 'Valid phone number is required' + "<br>";
		}
		
		if (email == '' || !validateEmail(email)) {
			msg = msg + 'Valid email address is required' + "<br>";
		}
		
		
		if(msg!=''){
			appConfirmDialog(msg,"Error");
			return false;
		}
		
		//Save
		var myForm = $('#tsoUserDetailForm').serialize();
		var url = contextPath+"/servlet/admin/adduser";

		setTimeout(function(){ // this gives the UI time to update before AJAX locks it up
			$.ajax({
				type: "POST",
				url: url,
				data: myForm,
				cache: false,
				async: false, // to set local variable
				success: function(data) {
					appConfirmDialog('New user created',"Message");
					closeDialog();
				},
				error: function(xhr, textStatus, errorThrown) {
					appConfirmDialog('Failed to add new user',"Error");
				}
			});
		},500);
		
	}
	
	
	function closeDialog(){
		$('#userDetailDialog').dialog('close');
	}
	

</script>

<div class="container">
	<div class="panel panel-default"  style="background: #eff8fe; background-size: coverd; margin: 15px;">
		<div class="panel-heading">
			<h3 class="panel-title">Search TSO User</h3>
		</div>
		<div class="panel-body">
			<form id="tsoUserSearchForm">
				<table style="width:50%">
					<tr>
						<td style="floating:right">User Name:</td>
						<td><input type="text" style="width: 180px;" maxlength="120"
							class="form-control" name="username" id="username" value=""
							placeholder="username" /></td>
					</tr>
				</table>
				<div class="center" align="center">
					<input type="button" name="cmdSearch" class="btn btn-large btn-primary" style="width:100px" id="cmdSearch" value="Search" onclick="javascript:searchTsoUser();" />
				</div>
				<br/><br/>
			</form>
				<div class="panel panel-default">
				
					<div class="panel-heading">
						<span>TSO Users</span>
						<div class="actionLinks">
							<span style="display:inline-block;font-size:9">
								<span class="glyphicon glyphicon-cloud-upload"></span>
					    		<a href="#" onclick="javascript:addNewUser();">Add New User</a>
							</span>					
						</div>
					</div>
					<div class="panel-body" id="tsoUserDetails"></div>
				</div>
	
				</div>		
	</div>
</div>


<div id="userDetailDialog" style="display:none;">
	<div class="panel panel-default">
	
		<div class="panel-body">
			<form id="tsoUserDetailForm">
				<input type="hidden" name="personId" id="personId"/>
				<table>
					<tr>
						<td>User Name:<span class="glyphicon glyphicon-star"></span></td>
						<td><input type="text" style="width: 180px;" maxlength="120"
							class="form-control" name="username1" id="username1" value=""
							placeholder="user name" /></td>
					</tr>
						<tr>
						<td>Password:<span class="glyphicon glyphicon-star"></span></td>
						<td><input type="password" style="width: 180px;" maxlength="120"
							class="form-control" name="password1" id="password1" value=""
							placeholder="password" /></td>
					</tr>
					<tr>
						<td>Re-type Password:<span class="glyphicon glyphicon-star"></span></td>
						<td><input type="password" style="width: 180px;" maxlength="120"
							class="form-control" name="password2" id="password2" value=""
							placeholder="password" /></td>
					</tr>
					<tr>
						<td>First Name:<span class="glyphicon glyphicon-star"></span></td>
						<td><input type="text" style="width: 180px;" maxlength="120"
							class="form-control" name="firstName" id="firstName" value=""
							placeholder="first name" /></td>
					</tr>
					<tr>
						<td>Last Name:<span class="glyphicon glyphicon-star"></span></td>
						<td><input type="text" style="width: 180px;" maxlength="120"
							class="form-control" name="lastName" id="lastName" value=""
							placeholder="last name" /></td>
					</tr>
						<tr>
						<td>Email :<span class="glyphicon glyphicon-star"></span></td>
						<td><input type="text" style="width: 180px;" maxlength="120"
							class="form-control" name="email" id="email" value=""
							placeholder="email" /></td>
					</tr>
						<tr>
						<td>Contact Phone:<span class="glyphicon glyphicon-star"></span></td>
						<td><input type="text" style="width: 180px;" maxlength="120"
							class="form-control" name="phone" id="phone" value=""
							placeholder="phone number" /></td>
					</tr>
			
					<tr>
						<td>User Role:</td>
						<td>
							<select class="form-control" name="role" id="role" style="width: 100px;">
								<option value="TSO">TSO User</option>
								<option value="CCTSO">CCTSO User</option>
								<option value="ADMIN">TSO Administrator</option>							
							</select>
						</td>
					</tr>
					
				</table>
				<div class="center" align="center">
					<input type="button" name="cmdAdd" class="btn btn-large btn-primary" style="width:100px"  id="cmdAdd" value="Save" onclick="javascript:saveTsoUser();" />
					<input type="button" name="cmdClose" class="btn btn-large btn-primary" style="width:100px"  id="cmdClose" value=" Close " onclick="javascript:closeDialog();" />
				</div>
			</form>
		</div>
	</div>


</div>