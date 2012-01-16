package com.gregtam.fbdfdetect.model;

public class FbdfRequest
{
	public static enum ReqAction
	{
		LOGIN, DASHBOARD, ACTIVITY, UPDATE, INVALID, REDIR_DASHBOARD, PRIVACY, RE_AUTH;
	}

	private ReqAction action;
	private FbdfUser fbUser;

	// paging
	private long start;
	private long limit;

	// metrics
	private long startTime;
	private long endTime;

	private String url;

	public FbdfRequest()
	{
		// set the start time of when i get the request
		this.startTime = System.currentTimeMillis();
		this.action = ReqAction.INVALID;
		this.url = "";
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public ReqAction getAction()
	{
		return action;
	}

	public long getStart()
	{
		return start;
	}

	public long getLimit()
	{
		return limit;
	}

	public long getStartTime()
	{
		return startTime;
	}

	public long getEndTime()
	{
		return endTime;
	}

	public FbdfUser getFbUser()
	{
		return fbUser;
	}

	public void setFbUser(FbdfUser fbUser)
	{
		this.fbUser = fbUser;
	}

	public void setAction(ReqAction action)
	{
		this.action = action;
	}

	public void setStart(long start)
	{
		this.start = start;
	}

	public void setLimit(long limit)
	{
		this.limit = limit;
	}

	public void setStartTime(long startTime)
	{
		this.startTime = startTime;
	}

	public void setEndTime(long endTime)
	{
		this.endTime = endTime;
	}

}
