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
</head>

<body>
<form method="POST" id="frmDBInfo" action="savedbconnectInfo">
	<table style="width: 100%">
		<tr style="line-height: 14px">
			<td colspan="2" style="background-color: #0D004D; padding: 10px"><span style="font-weight: bold; font-size: 20px; color: white">Generate CRUD Applications</span></td>
		</tr>
		<tr style="line-height: 8px" align="center">
			<td colspan="2" style="background-color: silver; padding: 10px"><span style="font-weight: bold; font-size: 18px;">&nbsp;&nbsp;&nbsp;Step 1 - Identify Source Database</span></td>
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
			<td align="right" width="50%" style="padding: 10px"><span>User/Schema</span></td>
			<td align="left" width="50%" style="padding: 10px"><input type="text" name="userSchema" size="20" maxlength="20" value="${param.userSchema}" /></td>
		</tr>
		<tr>
			<td align="right" width="50%" style="padding: 10px"><span>Password</span></td>
			<td align="left" width="50%" style="padding: 10px"><input type="password" name="password" size="40" maxlength="40" value="${param.password}" /></td>
		</tr>
		<tr>
			<td align="right" width="50%" style="padding: 10px"><span>Server Name (or IP)</span></td>
			<td align="left" width="50%" style="padding: 10px"><input type="text" name="serverOrIP" size="40" maxlength="40" value="${param.serverOrIP}" /></td>
		</tr>
		<tr>
			<td align="right" width="50%" style="padding: 10px"><span>Server Listen Port</span></td>
			<td align="left" width="50%" style="padding: 10px"><input type="text" name="serverListenPort" size="40" maxlength="40" value="${param.serverListenPort}" /></td>
		</tr>
		<tr>
			<td align="right" width="50%" style="padding: 10px"><span>DB Name</span></td>
			<td align="left" width="50%" style="padding: 10px"><input type="text" name="dbName" size="40" maxlength="40" value="${param.dbName}" /></td>
		</tr>
		<tr>
			<td colspan="2"><hr></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="2" align="center"><input style="width: 250px" type="submit" id="pbSubmitDbInfo" value="Submit"></td>
		</tr>
	</table>
</form>	
</body>
</html>