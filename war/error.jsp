<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%><%@ page import="java.io.*"
%><%
int errorCode = pageContext.getErrorData().getStatusCode();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Facebook DeFriender Detector</title>
<meta name="description" content="Detect when friends de-friend you.">

<link rel="shortcut icon" type="image/x-icon" href="/static/gfxs/favicon.ico">

<link type="text/css" rel="stylesheet" href="/static/css/fbdf.css">
</head>

<body>

<div id='header'>
<table cellpadding='0' cellspacing='0'>
	<tr>
		<td><a href='#'><img class='logo' src='/static/gfxs/logo.png' /></a></td>
	</tr>
</table>
</div>

<div id='container'>

<div id='errorDiv'>
<h3><%=errorCode %> Error</h3>
<br />
<p><span class='bold'><%=errorCode %> Error</span> <br /><br/>
What did you do? You must have broke the application.</p>
</div>

</div>

<div id='footer'>
<table>
	<tr>
		<td>&copy; 2010 <a href='http://www.gregtam.com'>GregTam</a>. All
		Rights Reserved</td>
	</tr>
</table>
</div>
<body>
</html>