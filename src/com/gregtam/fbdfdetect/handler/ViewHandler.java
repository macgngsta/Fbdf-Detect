package com.gregtam.fbdfdetect.handler;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.gregtam.fbdfdetect.constants.FacebookConstants;
import com.gregtam.fbdfdetect.constants.FrameworkConstants;
import com.gregtam.fbdfdetect.model.ActivityItem;
import com.gregtam.fbdfdetect.model.FbdfRequest;
import com.gregtam.fbdfdetect.model.FbdfResponse;
import com.gregtam.fbdfdetect.model.FbdfUser;

public class ViewHandler
{
	private static final Logger log = Logger.getLogger(ViewHandler.class
			.getName());

	private FbdfResponse response;

	public ViewHandler(FbdfResponse response)
	{
		this.response = response;
	}

	public String printProfile()
	{
		StringBuilder sb = new StringBuilder();
		FbdfRequest request = this.response.getPreviousRequest();
		if (request != null)
		{
			if (this.response.getStatus().equals(
					FbdfResponse.ResultStatus.SUCCESS))
			{

				FbdfUser user = request.getFbUser();

				if (user != null)
				{

					// start row
					sb.append("<tr>");

					// start pic
					sb.append("<td rowspan='3' id='profilePicture'>");
					sb.append(printProfileUrl(user));
					sb.append("</td>");

					// name
					sb.append("<td><h4>");
					sb.append(user.getFullName());
					sb.append("</h4></td>");

					// end row
					sb.append("</tr>");

					// start row
					sb.append("<tr>");

					// friend #
					sb.append("<td><p class='info'><span class='bold'>friends: </span> ");
					sb.append(printFriendStats(user));
					sb.append("</p></td>");

					// end row
					sb.append("</tr>");

					// start row
					sb.append("<tr>");

					// last update
					sb.append("<td><p class='info'><span class='bold'>last update: </span>");
					sb.append(printDate(user.getLastModify()));
					sb.append("</p></td>");

					// end row
					sb.append("</tr>");

				}
			}
			else if (this.response.getStatus().equals(
					FbdfResponse.ResultStatus.ERROR)
					|| this.response.getStatus().equals(
							FbdfResponse.ResultStatus.SERVICE_ERROR))
			{
				sb.append("<tr>");

				sb.append("<td><p>System is temporarily unavailable, please try again later.</p></td>");
				// end row
				sb.append("</tr>");
			}
			else
			{
				sb.append("<tr>");

				sb.append("<td><p>We could not retrieve your information. Please re-authorize this application. Thanks</p></td>");
				// end row
				sb.append("</tr>");
			}

		}
		return sb.toString();
	}

	public String printActivity()
	{
		StringBuilder sb = new StringBuilder();

		if (this.response.getStatus().equals(FbdfResponse.ResultStatus.SUCCESS))
		{

			List<ActivityItem> activity = this.response.getActivity();

			if (activity != null)
			{
				if (activity.isEmpty())
				{
					// row
					sb.append("<tr>");
					sb.append("<td colspan='5'>processing data...</td>");
					sb.append("</tr>");
				}
				else
				{
					for (ActivityItem act : activity)
					{
						// row
						sb.append("<tr>");

						// icon
						sb.append("<td>");
						sb.append(printActionTypeIcon(act));
						sb.append("</td>");

						// date
						sb.append("<td>");
						sb.append(printDate(act.getModifyTime()));
						sb.append("</td>");

						// id
						sb.append("<td>");
						sb.append(getFacebookURL(act));
						sb.append("</td>");

						// name
						sb.append("<td>");
						sb.append(getFacebookName(act));
						sb.append("</td>");

						// description
						sb.append("<td>");
						sb.append(printActionTypeDescription(act));
						sb.append("</td>");

						// end row
						sb.append("</tr>");
					}
				}
			}
		}
		else
		{
			// do nothing - dont show any data
			sb.append("<tr>");

			// end row
			sb.append("<td colspan='4'><span id='initialLoad'></span></td>");

			// end row
			sb.append("</tr>");
		}

		return sb.toString();
	}

	public String getBenchmarks()
	{
		StringBuilder sb = new StringBuilder();

		if (this.response != null)
		{
			FbdfRequest req = this.response.getPreviousRequest();
			if (req != null)
			{
				long total = req.getEndTime() - req.getStartTime();
				if (total > 0)
				{
					sb.append(total);
					sb.append(" ms");
				}
			}
		}

		return sb.toString();
	}

	public String printActivityHeader()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("<thead><th></th><th>date</th><th>id</th><th>name</th><th>description</th></thead>");

		return sb.toString();
	}

	private String printActionTypeIcon(ActivityItem act)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<img src='");

		switch (act.getType())
		{
		case ActivityItem.ADD:
			sb.append("/static/gfxs/user_add.png");
			break;
		case ActivityItem.DELETE:
			sb.append("/static/gfxs/user_delete.png");
			break;
		case ActivityItem.UPDATE:
			sb.append("/static/gfxs/user_edit.png");
			break;
		case ActivityItem.INITIAL_LOAD:
			sb.append("/static/gfxs/info.png");
			break;
		case ActivityItem.INVALID:
		default:
			sb.append("/static/gfxs/user.png");
		}

		sb.append("'/>");
		return sb.toString();
	}

	private String printActionTypeDescription(ActivityItem act)
	{
		StringBuilder sb = new StringBuilder();
		switch (act.getType())
		{
		case ActivityItem.ADD:
			sb.append("friend added");
			break;
		case ActivityItem.DELETE:
			sb.append("friend deleted");
			break;
		case ActivityItem.UPDATE:
			sb.append("updated name");
			break;
		case ActivityItem.INITIAL_LOAD:
			sb.append(act.getDescription());
			break;
		case ActivityItem.INVALID:
		default:
			sb.append("invalid activity");
		}

		return sb.toString();
	}

	private String printDate(Date date)
	{
		StringBuilder sb = new StringBuilder();

		if (date != null)
		{
			Format formatter = new SimpleDateFormat("HH:mm:ss MMM dd yyyy");
			String fDate = formatter.format(date);

			sb.append(fDate);
		}

		return sb.toString();
	}

	private String getFacebookName(ActivityItem act)
	{
		StringBuilder sb = new StringBuilder();

		if (act != null)
		{
			if (act.getFriendId() > 0)
			{
				sb.append(act.getDescription());
			}
			else
			{
				sb.append("-");
			}

		}
		return sb.toString();
	}

	private String getFacebookURL(ActivityItem act)
	{
		StringBuilder sb = new StringBuilder();

		if (act != null)
		{
			if (act.getFriendId() > 0)
			{
				sb.append("<a href='");
				sb.append(FacebookConstants.FB_STATIC_FRIEND_URL);
				sb.append(FrameworkConstants.URL_QUERY);
				sb.append(FacebookConstants.KEY_ID);
				sb.append(FrameworkConstants.URL_EQUALS);
				sb.append(act.getFriendId());
				sb.append("'>");
				sb.append(act.getFriendId());
				sb.append("</a>");
			}
			else
			{
				sb.append("-");
			}
		}

		return sb.toString();
	}

	private String printProfileUrl(FbdfUser user)
	{
		StringBuilder sb = new StringBuilder();

		if (user != null)
		{
			String pUrl = user.getPictureURL();

			if (pUrl != null && !pUrl.isEmpty())
			{
				sb.append("<img src='");
				sb.append(pUrl);
				sb.append("'/>");
			}
		}

		return sb.toString();
	}

	private String printFriendStats(FbdfUser user)
	{
		StringBuilder sb = new StringBuilder();

		if (user != null)
		{
			int change = user.getFriendNumber()
					- user.getPreviousFriendNumber();

			sb.append(user.getFriendNumber());

			if (change > 0)
			{
				sb.append("<span class='up'> (+");
				sb.append(change);
				sb.append(")</span>");
			}
			else if (change < 0)
			{
				sb.append("<span class='down'> (");
				sb.append(change);
				sb.append(")</span>");
			}
			else
			{
				// no number here
				sb.append("(no change)");
			}
		}

		return sb.toString();
	}
}
