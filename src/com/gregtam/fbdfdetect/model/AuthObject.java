package com.gregtam.fbdfdetect.model;

import java.util.Date;

public class AuthObject
{
	public static enum Status
	{
		SUCCESS, ERROR, INVALID;
	}

	private String errorType;
	private String errorMessage;
	private Status status;
	private String expires;
	private String accessCode;
	private String authCode;
	private Long fbid;
	private Date oAuthCreation;

	public AuthObject()
	{
		this.status = Status.INVALID;
		this.errorType = "";
		this.errorMessage = "";
		this.expires = "";
		this.accessCode = "";
		this.authCode = "";
		this.oAuthCreation = new Date();
	}

	public Date getoAuthCreation()
	{
		return oAuthCreation;
	}

	public void setoAuthCreation(Date oAuthCreation)
	{
		this.oAuthCreation = oAuthCreation;
	}

	public String getAuthCode()
	{
		return authCode;
	}

	public void setAuthCode(String authCode)
	{
		this.authCode = authCode;
	}

	public Long getFbid()
	{
		return fbid;
	}

	public void setFbid(Long fbid)
	{
		this.fbid = fbid;
	}

	public String getErrorType()
	{
		return errorType;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public Status getStatus()
	{
		return status;
	}

	public String getExpires()
	{
		return expires;
	}

	public String getAccessCode()
	{
		return accessCode;
	}

	public void setErrorType(String errorType)
	{
		this.errorType = errorType;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public void setStatus(Status status)
	{
		this.status = status;
	}

	public void setExpires(String expires)
	{
		this.expires = expires;
	}

	public void setAccessCode(String accessCode)
	{
		this.accessCode = accessCode;
	}

	@Override
	public String toString()
	{
		return "AuthObject [errorType=" + errorType + ", errorMessage="
				+ errorMessage + ", status=" + status + ", expires=" + expires
				+ ", accessCode=" + accessCode + ", authCode=" + authCode
				+ ", fbid=" + fbid + "]";
	}

}
