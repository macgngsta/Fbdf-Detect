package com.gregtam.fbdfdetect.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ActivityItem
{
	private static final long serialVersionUID = 1L;

	public static final int INVALID = -1;
	public static final int ADD = 1;
	public static final int DELETE = 2;
	public static final int UPDATE = 3;
	public static final int DEACTIVATE = 4;
	public static final int INITIAL_LOAD = 5;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private Long userId;

	@Persistent
	private Long friendId;

	@Persistent
	private Date modifyTime;

	@Persistent
	private int type;

	@Persistent
	private String description;

	public ActivityItem()
	{
		this.type = INVALID;
	}

	public ActivityItem(Long userId, Long friendId, int t, String description)
	{
		super();
		this.userId = userId;
		this.friendId = friendId;
		this.type = t;
		this.modifyTime = new Date();
		this.description = description;
	}

	public Key getKey()
	{
		return key;
	}

	public Long getFriendId()
	{
		return friendId;
	}

	public Long getUserId()
	{
		return userId;
	}

	public Date getModifyTime()
	{
		return modifyTime;
	}

	public int getType()
	{
		return type;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public void setFriendId(Long friendId)
	{
		this.friendId = friendId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public void setModifyTime(Date modifyTime)
	{
		this.modifyTime = modifyTime;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((friendId == null) ? 0 : friendId.hashCode());
		result = prime * result
				+ ((modifyTime == null) ? 0 : modifyTime.hashCode());
		result = prime * result + type;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		ActivityItem other = (ActivityItem) obj;
		if (description == null)
		{
			if (other.description != null)
				return false;
		}
		else if (!description.equals(other.description))
			return false;
		if (friendId == null)
		{
			if (other.friendId != null)
				return false;
		}
		else if (!friendId.equals(other.friendId))
			return false;
		if (modifyTime == null)
		{
			if (other.modifyTime != null)
				return false;
		}
		else if (!modifyTime.equals(other.modifyTime))
			return false;
		if (type != other.type)
			return false;
		if (userId == null)
		{
			if (other.userId != null)
				return false;
		}
		else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "ActivityItem [key=" + key + ", userId=" + userId
				+ ", friendId=" + friendId + ", modifyTime=" + modifyTime
				+ ", type=" + type + ", description=" + description + "]";
	}

}
