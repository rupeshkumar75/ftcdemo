<script type="text/javascript">
	var contextPath;
	
	$(document).ready(function() {
		contextPath = $("#contextPath").val();
	});
	
	function back(){
		var url = contextPath + "/servlet/home/start";
		document.location=url;
	}
</script>

<div class="container" style="margin-top:100px;width:50%;">
	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="panel-title">Application Error</h3>
		</div>
		<div class="panel-body">
			<p>There was an error in the system.  Please contact your system administrator.</p>
			
			<p>${errorMsg}</p>
			<br/>
			<div class="center" align="center" >
				<input type="button"  name="cmdCancel"  class="btn btn-large btn-primary"  id="cmdCancel" value="Back" onclick="javascript:back();" />
			</div>
		</div>
	</div>
</div>