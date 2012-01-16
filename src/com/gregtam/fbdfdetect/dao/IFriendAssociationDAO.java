package com.gregtam.fbdfdetect.dao;

import java.util.List;

import com.gregtam.fbdfdetect.model.FriendAssociation;

public interface IFriendAssociationDAO
{
	void addAssociation(FriendAssociation association);

	void removeAssociation(FriendAssociation association);

	void removeAllAssociations(Long fbid);

	void updateAssociation(FriendAssociation association);

	List<FriendAssociation> listAssociations(boolean flush);

	List<FriendAssociation> listAssociationsForUser(Long fid, boolean flush);
}
