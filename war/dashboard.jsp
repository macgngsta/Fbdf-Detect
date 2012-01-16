<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	%><%@ page import="com.gregtam.fbdfdetect.model.*" 
	%><%@ page import="com.gregtam.fbdfdetect.handler.*" 
	%><%@ page import="com.gregtam.fbdfdetect.constants.*" 
	%><%@ page import="java.io.*" 
	%><%

FbdfResponse fbResponse = (FbdfResponse)request.getAttribute(FrameworkConstants.FBDF_RESPONSE);

ViewHandler myView = null;

if(response!=null)
{
	myView = new ViewHandler(fbResponse);
}

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Facebook DeFriender Detector</title>
<meta name="description" content="Detect when friends de-friend you.">
<link type="text/css" rel="stylesheet" href="/static/css/fbdf.css">

<link rel="shortcut icon" type="image/x-icon" href="/static/gfxs/favicon.ico">

<script type="text/javascript" src="/static/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="/static/js/jquery.tablesorter.min.js"></script>
<script type="text/javascript" src="/static/js/fbdf.js"></script>

</head>
<body>

<!-- navigation header -->
<div id='header'>
<table cellpadding='0' cellspacing='0'>
	<tr>
		<td><a href='#'><img class='logo' src='/static/gfxs/logo.png' /></a></td>
		<td class='right'>
		<a id='updateButton' class='button awesome medium green' href='#'>update</a>
		</td>
	</tr>
</table>
<br/>
<p><div id="resultStatus"></div></p>
</div>

<!-- container -->
<div id='container'>

<div id='profile'>
<h3>Profile</h3>
<br />
<table cellpadding='0' cellspacing='0'>
<%
	out.print(myView.printProfile());
%>
</table>
</div>

<div id='activity'>
<h3>Activity History</h3>
<br />
<table cellpadding='0' cellspacing='0' id='activityTable'
	class='tablesorter'>
<%
	//print the header
	out.print(myView.printActivityHeader());
%>
	<tbody>
<%
	//print the activity
	out.print(myView.printActivity());
%>
	<div id='dynamicActivity'></div>
	</tbody>
</table>
</div>
</div>

<div id='footer'>
<table>
	<tr>
		<td>&copy; 2010 <a href='http://www.gregtam.com'>GregTam</a>. All
		Rights Reserved</td>
		<td class='right'>
		<%
			out.print(myView.getBenchmarks());
		%>
		</td>
	</tr>
</table>
</div>

</body>
</html>