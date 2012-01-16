package com.gregtam.fbdfdetect.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class FbdfUser
{

	private static final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long fbId;

	@Persistent
	private String accessCode;

	@Persistent
	private String fullName;

	@Persistent
	private String email;

	@Persistent
	private Date lastModify;

	@Persistent
	private Date createToken;

	@Persistent
	private String authCode;

	@Persistent
	private long expires;

	@Persistent
	private boolean verified;

	@Persistent
	private String pictureURL;

	@Persistent
	private int friendNumber;

	@Persistent
	private int previousFriendNumber;

	public FbdfUser()
	{

	}

	public FbdfUser(Long fbId, String accessCode, String name, String email,
			long expires, boolean verified, String pictureURL, String authCode)
	{
		super();
		this.fbId = fbId;
		this.accessCode = accessCode;
		this.fullName = name;
		this.email = email;
		this.expires = expires;
		this.verified = verified;
		this.lastModify = new Date();
		this.pictureURL = pictureURL;
		this.createToken = new Date();
		this.authCode = authCode;
	}

	public String getAuthCode()
	{
		return authCode;
	}

	public void setAuthCode(String authCode)
	{
		this.authCode = authCode;
	}

	public Long getFbId()
	{
		return fbId;
	}

	public void setFbId(Long fbId)
	{
		this.fbId = fbId;
	}

	public String getAccessCode()
	{
		return accessCode;
	}

	public String getFullName()
	{
		return fullName;
	}

	public String getEmail()
	{
		return email;
	}

	public Date getLastModify()
	{
		return lastModify;
	}

	public Date getCreateToken()
	{
		return createToken;
	}

	public boolean isVerified()
	{
		return verified;
	}

	public String getPictureURL()
	{
		return pictureURL;
	}

	public int getFriendNumber()
	{
		return friendNumber;
	}

	public int getPreviousFriendNumber()
	{
		return previousFriendNumber;
	}

	public void setFriendNumber(int friendNumber)
	{
		this.friendNumber = friendNumber;
	}

	public void setPreviousFriendNumber(int previousFriendNumber)
	{
		this.previousFriendNumber = previousFriendNumber;
	}

	public void setPictureURL(String pictureURL)
	{
		this.pictureURL = pictureURL;
	}

	public void setAccessCode(String accessCode)
	{
		this.accessCode = accessCode;
	}

	public void setFullName(String name)
	{
		this.fullName = name;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public void setLastModify(Date lastModify)
	{
		this.lastModify = lastModify;
	}

	public void setCreateToken(Date token)
	{
		this.createToken = token;
	}

	public void setVerified(boolean verified)
	{
		this.verified = verified;
	}

	public long getExpires()
	{
		return expires;
	}

	public void setExpires(long expires)
	{
		this.expires = expires;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fbId == null) ? 0 : fbId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FbdfUser other = (FbdfUser) obj;
		if (fbId == null)
		{
			if (other.fbId != null)
				return false;
		}
		else if (!fbId.equals(other.fbId))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "FbdfUser [fbId=" + fbId + ", accessCode=" + accessCode
				+ ", fullName=" + fullName + ", email=" + email
				+ ", lastModify=" + lastModify + ", createToken=" + createToken
				+ ", authCode=" + authCode + ", expires=" + expires
				+ ", verified=" + verified + ", pictureURL=" + pictureURL
				+ ", friendNumber=" + friendNumber + ", previousFriendNumber="
				+ previousFriendNumber + "]";
	}

}
