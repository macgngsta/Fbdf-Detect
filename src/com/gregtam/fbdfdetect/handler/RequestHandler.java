package com.gregtam.fbdfdetect.handler;

import java.util.Date;
import java.util.logging.Logger;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.gregtam.fbdfdetect.constants.FacebookConstants;
import com.gregtam.fbdfdetect.constants.FrameworkConstants;
import com.gregtam.fbdfdetect.helper.FriendListManager;
import com.gregtam.fbdfdetect.model.FbdfRequest;
import com.gregtam.fbdfdetect.model.FbdfResponse;
import com.gregtam.fbdfdetect.model.FbdfUser;

public class RequestHandler
{
	private static final Logger log = Logger.getLogger(RequestHandler.class
			.getName());

	private static final long TIME_BETWEEN_CALLS = 20000;

	public RequestHandler()
	{

	}

	public FbdfResponse processRequest(FbdfRequest request)
	{

		FbdfResponse myResponse = new FbdfResponse();

		if (request != null)
		{
			myResponse.setUser(request.getFbUser());

			FbdfUser myUser = request.getFbUser();

			if (request.getAction().equals(FbdfRequest.ReqAction.DASHBOARD))
			{
				log.info("dashboard action");

				if (myUser != null)
				{
					FriendListManager flManager = new FriendListManager(
							myUser.getFbId());

					myResponse.setActivity(flManager.getAcitivty());

					myResponse.setAction(FacebookConstants.ACTION_DASHBOARD);
					myResponse.setStatus(FbdfResponse.ResultStatus.SUCCESS);
				}
				else
				{
					log.info("invalid access");
					myResponse.setAction(FacebookConstants.ACTION_APP_ERROR);
					myResponse
							.setStatus(FbdfResponse.ResultStatus.SERVICE_ERROR);
				}
			}
			else if (request.getAction().equals(
					FbdfRequest.ReqAction.REDIR_DASHBOARD))
			{
				log.info("redirect dashboard action");

				if (myUser != null)
				{
					FriendListManager flManager = new FriendListManager(
							myUser.getFbId());

					myResponse.setNeedRedirect(true);
					myResponse.setActivity(flManager.getAcitivty());

					myResponse
							.setAction(FacebookConstants.ACTION_REDIR_DASHBOARD);
					myResponse.setStatus(FbdfResponse.ResultStatus.SUCCESS);

				}
				else
				{
					log.info("invalid access");
					myResponse.setAction(FacebookConstants.ACTION_APP_ERROR);
					myResponse
							.setStatus(FbdfResponse.ResultStatus.SERVICE_ERROR);
				}
			}
			else if (request.getAction().equals(FbdfRequest.ReqAction.RE_AUTH))
			{
				log.info("reauth action");

				myResponse.setNeedRedirect(true);
				myResponse.setAction(request.getUrl());
			}
			else if (request.getAction().equals(FbdfRequest.ReqAction.UPDATE))
			{
				log.info("update action");

				if (myUser != null)
				{
					if (allowProcess(myUser))
					{
						createProcessTask(myUser);
					}

					myResponse.setAction(FacebookConstants.ACTION_RAW);
					myResponse.setStatus(FbdfResponse.ResultStatus.SUCCESS);
				}
				else
				{
					log.info("invalid access");
					myResponse.setAction(FacebookConstants.ACTION_APP_ERROR);
					myResponse
							.setStatus(FbdfResponse.ResultStatus.SERVICE_ERROR);
				}
			}
			else if (request.getAction().equals(FbdfRequest.ReqAction.ACTIVITY))
			{
				log.info("activity action");
				if (myUser != null)
				{
					FriendListManager flManager = new FriendListManager(
							myUser.getFbId());
					// get activity
					myResponse.setActivity(flManager.getActivity(
							request.getStart(), request.getLimit()));

					// TODO: create json object

					myResponse.setAction(FacebookConstants.ACTION_RAW);
					myResponse.setStatus(FbdfResponse.ResultStatus.SUCCESS);
				}
				else
				{
					log.info("invalid access");
					myResponse.setAction(FacebookConstants.ACTION_APP_ERROR);
					myResponse
							.setStatus(FbdfResponse.ResultStatus.SERVICE_ERROR);
				}

			}
			else if (request.getAction().equals(FbdfRequest.ReqAction.LOGIN))
			{
				log.info("login action");

				myResponse.setAction(FacebookConstants.ACTION_LOGIN);
			}
			else if (request.getAction().equals(FbdfRequest.ReqAction.PRIVACY))
			{
				log.info("privacy page");

				myResponse.setAction(FacebookConstants.ACTION_PRIVACY);
			}
			else
			{
				myResponse.setAction(FacebookConstants.ACTION_APP_ERROR);
			}

			// set end time
			request.setEndTime(System.currentTimeMillis());
		}
		else
		{
			myResponse.setAction(FacebookConstants.ACTION_GENERAL_ERROR);
		}

		myResponse.setPreviousRequest(request);

		return myResponse;

	}

	private void createProcessTask(FbdfUser myUser)
	{
		// creat a queue
		Queue queue = QueueFactory
				.getQueue(FrameworkConstants.QUEUE_PROCESS_FRIENDS);

		// add the url
		queue.add(TaskOptions.Builder.withUrl(FrameworkConstants.QUEUE_PF_URL)
				.param(FrameworkConstants.QUEUE_PF_PARAM_FBID,
						myUser.getFbId().toString()));

		log.info("added process friends to tasks.");
	}

	private boolean allowProcess(FbdfUser myUser)
	{
		Date lastModify = myUser.getLastModify();
		long lModify = lastModify.getTime();

		Date currentDate = new Date();
		long lCurrent = currentDate.getTime();

		if (lModify + TIME_BETWEEN_CALLS < lCurrent)
		{
			log.info("last modify is within bounds");
			return true;
		}

		return false;
	}
}
