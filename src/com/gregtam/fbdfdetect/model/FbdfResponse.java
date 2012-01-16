package com.gregtam.fbdfdetect.model;

import java.util.List;

public class FbdfResponse
{
	public static enum ResultStatus
	{
		SUCCESS, ERROR, SERVICE_ERROR, INVALID;
	}

	private ResultStatus status;
	private FbdfRequest previousRequest;
	private FbdfUser user;
	private List<ActivityItem> activity;
	private String action;
	private boolean needRedirect;

	public FbdfResponse()
	{
		this.status = ResultStatus.INVALID;
		this.needRedirect = false;
	}

	public boolean isNeedRedirect()
	{
		return needRedirect;
	}

	public void setNeedRedirect(boolean needRedirect)
	{
		this.needRedirect = needRedirect;
	}

	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}

	public ResultStatus getStatus()
	{
		return status;
	}

	public FbdfRequest getPreviousRequest()
	{
		return previousRequest;
	}

	public FbdfUser getUser()
	{
		return user;
	}

	public List<ActivityItem> getActivity()
	{
		return activity;
	}

	public void setStatus(ResultStatus status)
	{
		this.status = status;
	}

	public void setPreviousRequest(FbdfRequest previousRequest)
	{
		this.previousRequest = previousRequest;
	}

	public void setUser(FbdfUser user)
	{
		this.user = user;
	}

	public void setActivity(List<ActivityItem> activity)
	{
		this.activity = activity;
	}

	@Override
	public String toString()
	{
		return "FbdfResponse [status=" + status + ", previousRequest="
				+ previousRequest + ", user=" + user + ", activity=" + activity
				+ "]";
	}
}
