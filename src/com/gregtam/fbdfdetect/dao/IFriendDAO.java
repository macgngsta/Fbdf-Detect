package com.gregtam.fbdfdetect.dao;

import java.util.List;

import com.gregtam.fbdfdetect.model.FriendItem;

public interface IFriendDAO
{
	void addFriend(FriendItem friend);

	void removeFriend(FriendItem friend);

	void removeAllFriends(Long fbid);

	void updateFriend(FriendItem friend);

	List<FriendItem> listFriends(boolean flush);

	FriendItem getFriendById(long fbid);
}
