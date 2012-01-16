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
	out.print("SUCCESS");
}

%>