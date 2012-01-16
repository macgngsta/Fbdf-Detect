<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    %><%@ page import="com.gregtam.fbdfdetect.model.*" 
	%><%@ page import="com.gregtam.fbdfdetect.services.*" 
	%><%@ page import="com.gregtam.fbdfdetect.helper.*" 
	%><%@ page import="java.io.*" %>
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
			<td><a href='#'><img class='logo'src='/static/gfxs/logo.png'/></a></td>
		</tr>
	</table>
</div>

<div id='container'>

<div id='welcome'>
	<h3>Welcome</h3><br/>
	<p><span class='bold'>Welcome to Facebook De-Friender Detector.</span>
	<br/>The way this project came to be was that I got sick of seeing my Facebook friend number change and not know what was going on, whether it was someone de-activating their Facebook account or someone de-friending me.</p>
	<p><span class='bold'>So how does this work?</span>
	<br/>The application requests basic access to your Facebook data, it then pulls your friend list and stores it into a database. Every subsequent launch of this application will diff the current list with the one stored in the database. Initially, the first time you run this application, you will not have any changes because we use this first run as the baseline. After we have a baseline, we can determine the subsequent changes.</p>
	<p><span class='bold'>This is cool, but I want [insert feature]</span>
	<br/>This is just the first iteration of the application and once I get the kinks out of the way, I'll start implementing other enhancements. If you are as excited as I am for this application, then leave your ideas and comments in the discussion section.</p>
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
			<td>&copy; 2010 <a href='http://www.gregtam.com'>GregTam</a>. All Rights Reserved</td>
		</tr>
	</table>
</div>
<body>
</html>