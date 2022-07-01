<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>CRUD Applications Generator</title>

<style type="text/css">
	h1,h2,span {
		font-family: Arial;
	}
</style>

<script>
function generateApps(){
	if(document.getElementById('basePackageName').value.trim() == ""){
		alert("You must enter a Base Package Name");
		document.getElementById('basePackageName').focus();
	}else if(document.getElementById('javaVersion').value.trim() == ""){
		alert("You must enter a Java Version");
		document.getElementById('javaVersion').focus();
	}else if(isNaN(document.getElementById('javaVersion').value) || 
			document.getElementById('javaVersion').value < 5 ||
			document.getElementById('javaVersion').value > 14)
		alert("Java Version must be numeric and between 5 and 14");
		document.getElementById('javaVersion').focus();
	}else{
		document.getElementById('frmGenOptions').submit();
		document.getElementById("divEnterOptions").style.display = "none";
		document.getElementById("divGenerationComplete").style.display = "block";
	}
}
</script>
</head>

<body>
<form method="POST" id="frmGenOptions" action="savegenoptions">
	<div id="divEnterOptions">
	<table style="width: 100%">
		<tr style="line-height: 14px">
			<td colspan="2" style="background-color: #0D004D; padding: 10px"><span style="font-weight: bold; font-size: 20px; color: white">Generate CRUD Applications</span></td>
		</tr>
		<tr style="line-height: 8px" align="center">
			<td colspan="2" style="background-color: silver; padding: 10px"><span style="font-weight: bold; font-size: 18px;">&nbsp;&nbsp;&nbsp;Step 3 - Choose Options</span></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>

		<c:if test="${not empty errMsgList}">
			<tr>
				<td colspan="2">
				<c:forEach var="errMsg" items="${errMsgList}">
	    			<li><span style="color: red">${errMsg}</span></li><br>
				</c:forEach>				
				</td>
			</tr>
		</c:if>

		<tr align="center">
			<td colspan="2">
				<div id="divChooseOptions" style="height:500px;width: 760px">
					<table style="width: 750px">
						<tr>
							<td><span style="font-size: 11pt">Base Package Name</span></td>
							<td><input type="text" name="basePackageName" id="basePackageName" size="30" value="" /></td>
						</tr>
						<tr>
							<td><span style="font-size: 11pt">Java Version</span></td>
							<td><input type="text" name="javaVersion" id="javaVersion" size="30" value="" /></td>
						</tr>
					</table>
				</div>
			</td>
		</tr>

		<tr>
			<td colspan="2"><hr></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="2" align="center"><input style="width: 250px" type="button" id="pbSubmitOptionsInfo" value="Submit" onclick="generateApps()"></td>
		</tr>
	</table>
	</div>
	<div id="divGenerationComplete" style="display: none">
	<table style="width: 100%">
		<tr style="line-height: 14px">
			<td colspan="2" style="background-color: #0D004D; padding: 10px"><span style="font-weight: bold; font-size: 20px; color: white">CRUD Application Generator</span></td>
		</tr>
		<tr style="line-height: 8px" align="center">
			<td colspan="2" style="background-color: silver; padding: 10px"><span style="font-weight: bold; font-size: 18px;">&nbsp;&nbsp;&nbsp;Generation Complete</span></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>

		<tr align="center">
			<td colspan="2"><span>Your applications/projects have been generated and are contained in the downloaded apps.zip file. Customize as needed.<br><br>
			 <b>Note</b>: The spring data source properties in each project's application.properties file default to generated values and should be changed accordingly.</span></td>
		</tr>
		<tr>
			<td colspan="2"><hr></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="2" align="center"><a href="getschemafile"><span style="font-size: 14px;">Generate More Applications</span></a></td>
		</tr>
	</table>
	</div>			
</form>
</body>

</html>