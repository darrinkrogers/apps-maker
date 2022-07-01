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
function uploadSchemaFile(){
	if(document.getElementById("fileToUpload").files.length == 0)){
		alert("Please select a schema script file to upload");
	}else{
		document.forms[0].submit();
	}
}
</script>
</head>

<body>
<form method="POST" id="frmUploadSchema" action="uploadschema" enctype="multipart/form-data">
	<table style="width: 100%">
		<tr style="line-height: 14px">
			<td colspan="2" style="background-color: #0D004D; padding: 10px"><span style="font-weight: bold; font-size: 20px; color: white">Generate CRUD Applications</span></td>
		</tr>
		<tr style="line-height: 8px" align="center">
			<td colspan="2" style="background-color: silver; padding: 10px"><span style="font-weight: bold; font-size: 18px;">&nbsp;&nbsp;&nbsp;Step 1 - Upload Schema Creation Script</span></td>
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

		<tr>
			<td align="right" width="50%" style="padding: 10px"><span>DBMS Name/Type</span></td>
			<td align="left" width="50%" style="padding: 10px">
				<select name="dbmsName">
					<option value="mysql">MySQL</option>
				</select>
			</td>
		</tr>
		<tr>
			<td align="right" width="50%" style="padding: 10px"><span>Schema Script</span></td>
			<td align="left" width="50%" style="padding: 10px">
				<input type="file" name="fileToUpload" id="fileToUpload" required>
			</td>
		</tr>
		<tr align="center">
			<td style="padding: 10px" colspan="2"><br><span><b>NOTE</b>: The uploaded script must only contain CREATE TABLE statements</span></td>
		</tr>
		<tr>
			<td style="padding: 10px" colspan="2"><hr></td>
		</tr>
		<tr>
			<td align="center"style="padding: 10px" colspan="2"><button value="Upload Script" id="btnUploadScript" name="Upload File" onclick="uploadSchemaFile()">Upload Script</button></td>
		</tr>
	</table>
	
</form>	
</body>
</html>