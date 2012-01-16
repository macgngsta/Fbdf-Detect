<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%><%@ page
	import="com.gregtam.fbdfdetect.model.*"%><%@ page
	import="com.gregtam.fbdfdetect.services.*"%><%@ page
	import="com.gregtam.fbdfdetect.helper.*"%><%@ page
	import="com.gregtam.fbdfdetect.constants.*"%><%@ page import="java.io.*"
%><%

String authError = "Application Error";
String authMsg = "Something happened. I can't explain it.";

//pull the authObject from request
AuthObject auth = (AuthObject)request.getAttribute(FacebookConstants.AUTH_OBJECT);
if(auth!=null)
{
	authError = auth.getErrorType();
	authMsg = auth.getErrorMessage();
}

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
<h3><%=authError %></h3>
<br />
<p><span class='bold'><%=authError %></span> <br /><br/>
Something happened. This is what the system thinks happened:<br/><br/>
<code><%=authMsg %></code></p>

<p>
<%
		StringBuilder sb = new StringBuilder();
		
		sb.append("<a class='button awesome medium blue' href='");
		sb.append(FacebookHelper.getAppAuthentication());
		sb.append("'>login &raquo;</a>");
		out.print(sb.toString());
%>
</p>
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