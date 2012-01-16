package com.gregtam.fbdfdetect.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class FriendAssociation
{
	private static final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	public void setKey(Key key)
	{
		this.key = key;
	}

	@Persistent
	private Long fbId;

	@Persistent
	private Long friendId;

	public FriendAssociation()
	{
	}

	public FriendAssociation(Long fbId, Long friendId)
	{
		super();
		this.fbId = fbId;
		this.friendId = friendId;
	}

	public Key getKey()
	{
		return key;
	}

	public Long getFbId()
	{
		return fbId;
	}

	public Long getFriendId()
	{
		return friendId;
	}

	public void setFbId(Long fbId)
	{
		this.fbId = fbId;
	}

	public void setFriendId(Long friendId)
	{
		this.friendId = friendId;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fbId == null) ? 0 : fbId.hashCode());
		result = prime * result
				+ ((friendId == null) ? 0 : friendId.hashCode());
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
		FriendAssociation other = (FriendAssociation) obj;
		if (fbId == null)
		{
			if (other.fbId != null)
				return false;
		}
		else if (!fbId.equals(other.fbId))
			return false;
		if (friendId == null)
		{
			if (other.friendId != null)
				return false;
		}
		else if (!friendId.equals(other.friendId))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "FriendAssociation [key=" + key + ", fbId=" + fbId
				+ ", friendId=" + friendId + "]";
	}

}
