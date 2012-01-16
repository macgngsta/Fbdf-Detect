package com.gregtam.fbdfdetect.model;

import java.io.Serializable;

public class SessionObject implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int INVALID_STATE = -1;
	public static final int VALID_STATE = 0;
	public static final int REAUTH_STATE = 1;
	public static final int LOGIN_STATE = 2;
	// new user
	public static final int NEW_STATE = 3;

	public static final long INVALID_USER = -1;

	private long fbId;
	private int state;
	private String accessCode;
	private String expires;
	private String authCode;

	public SessionObject()
	{
		this.state = INVALID_STATE;
		this.fbId = INVALID_USER;
		this.accessCode = "";
		this.expires = "";
		this.authCode = "";
	}

	public String getAuthCode()
	{
		return authCode;
	}

	public void setAuthCode(String authCode)
	{
		this.authCode = authCode;
	}

	public String getAccessCode()
	{
		return accessCode;
	}

	public String getExpires()
	{
		return expires;
	}

	public void setAccessCode(String accessCode)
	{
		this.accessCode = accessCode;
	}

	public void setExpires(String expires)
	{
		this.expires = expires;
	}

	public long getFbId()
	{
		return fbId;
	}

	public void setFbId(long fbId)
	{
		this.fbId = fbId;
	}

	public int getState()
	{
		return state;
	}

	public void setState(int state)
	{
		this.state = state;
	}

	public void resetAccess()
	{
		this.accessCode = "";
		this.expires = "";
	}
}
