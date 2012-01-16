package com.gregtam.fbdfdetect.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class FriendItem
{
	private static final long serialVersionUID = 1L;

	@Persistent
	private String fullName;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long fbId;

	@Persistent
	private Date lastModify;

	public FriendItem(Long id, String name)
	{
		this.fbId = id;
		this.fullName = name;
		this.lastModify = new Date();
	}

	public Long getFbId()
	{
		return fbId;
	}

	public void setFbId(Long fbId)
	{
		this.fbId = fbId;
	}

	public String getFullName()
	{
		return fullName;
	}

	public Date getLastModify()
	{
		return lastModify;
	}

	public void setLastModify(Date lastModify)
	{
		this.lastModify = lastModify;
	}

	public void setFullName(String name)
	{
		this.fullName = name;
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
		FriendItem other = (FriendItem) obj;
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
		return "FriendObject [name=" + fullName + ", id=" + fbId + "]";
	}
}
