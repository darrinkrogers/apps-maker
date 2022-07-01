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
function saveTableSelections(){
	var bootAppSelections = 0;
	for (x = 0; x < document.getElementsByTagName('input').length; x++) {
	  if (document.getElementsByTagName('input').item(x).type == 'checkbox') {
		 if(document.getElementsByTagName('input').item(x).checked &&
		    document.getElementsByTagName('input').item(x).name.startsWith("selCrudBootApp|")){
			 bootAppSelections++;
		 }
	  }
	}

	if(bootAppSelections < 1){
		alert("You must select at least one table for CRUD Spring Boot application generation");
	}else{
		document.getElementById('frmTableInfo').submit();
	}
}
</script>
</head>

<body>
<form method="POST" id="frmTableInfo" action="savetableselections">
	<div id="divChooseTables">
	<table style="width: 100%">
		<tr style="line-height: 14px">
			<td colspan="2" style="background-color: #0D004D; padding: 10px"><span style="font-weight: bold; font-size: 20px; color: white">Generate CRUD Applications</span></td>
		</tr>
		<tr style="line-height: 8px" align="center">
			<td colspan="2" style="background-color: silver; padding: 10px"><span style="font-weight: bold; font-size: 18px;">&nbsp;&nbsp;&nbsp;Step 2 - Select Tables</span></td>
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
				<div id="divTablesSelection" style="overflow-y: scroll; height:400px;width: 760px">
					<table style="width: 740px; border-collapse:collapse; border:1px solid black;">
						<tr>
							<td style="border-collapse:collapse; border:1px solid; background-color: silver"><span style="font-weight: bold; font-size: 11pt">Table Name</span></td>
							<td style="border-collapse:collapse; border:1px solid; background-color: silver"><span style="font-size: 11pt">CRUD Boot App?</span></td>
							<td style="border-collapse:collapse; border:1px solid; background-color: silver"><span style="font-size: 11pt">Component in Angular App?</span></td>
						</tr>
						
						<c:forEach var="tableMetaData" items="${tableMetaDataList}"> 
							<tr>
								<td style="border-collapse:collapse; border:1px solid"><span>${tableMetaData.tableName}</span></td>
								<td style="border-collapse:collapse; border:1px solid"><input type="checkbox" name="selCrudBootApp|${tableMetaData.tableName}"></td>
								<td style="border-collapse:collapse; border:1px solid"><input type="checkbox" name="selFrontEndApp|${tableMetaData.tableName}"></td>
							</tr>								
						</c:forEach>
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
			<td colspan="2" align="center"><input style="width: 250px" type="button" id="pbSubmitTableInfo" value="Submit" onclick="javascript: saveTableSelections()"></td>
		</tr>
	</table>
	</div>
</form>
</body>

</html>