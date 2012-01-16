package com.gregtam.fbdfdetect.dao;

import java.util.List;

import com.gregtam.fbdfdetect.model.ActivityItem;

public interface IActivityDAO
{
	void addActivity(ActivityItem activity);

	void removeActivity(ActivityItem activity);

	void removeAllActivity(Long fbid);

	void updateActivity(ActivityItem activity);

	List<ActivityItem> listActivity(long start, long limit, boolean flush);

	List<ActivityItem> listActivityforUser(Long fid, long start, long limit,
			boolean flush);
}
