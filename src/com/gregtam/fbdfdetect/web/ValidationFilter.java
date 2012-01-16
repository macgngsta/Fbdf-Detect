package com.gregtam.fbdfdetect.web;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.gregtam.fbdfdetect.constants.FrameworkConstants;
import com.gregtam.fbdfdetect.helper.DataManager;
import com.gregtam.fbdfdetect.helper.EnvManager;
import com.gregtam.fbdfdetect.helper.FbdfRequestBuilder;
import com.gregtam.fbdfdetect.helper.IOUtil;
import com.gregtam.fbdfdetect.model.FbdfRequest;

public class ValidationFilter implements Filter
{

	private static final Logger log = Logger.getLogger(ValidationFilter.class
			.getName());

	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException
	{
		log.info("validation filter...");

		HttpServletRequest httpRequest = (HttpServletRequest) request;

		// create the request
		FbdfRequestBuilder myBuilder = new FbdfRequestBuilder(request);
		FbdfRequest fbRequest = myBuilder.processHttp();

		checkDebug(httpRequest, fbRequest);

		// reset the session
		httpRequest.setAttribute(FrameworkConstants.FBDF_REQUEST, fbRequest);

		chain.doFilter(request, response);
		// dont need to run on the way back
		return;

	}

	private void checkDebug(HttpServletRequest request, FbdfRequest req)
	{
		String serverKey = createKey();

		// y n
		String debug = (String) request
				.getParameter(FrameworkConstants.POST_DEBUG);
		// key
		String securityKey = (String) request
				.getParameter(FrameworkConstants.POST_SECURITY_KEY);
		// y n
		String reset = (String) request
				.getParameter(FrameworkConstants.POST_RESET);
		// local prod
		String env = (String) request.getParameter(FrameworkConstants.POST_ENV);
		// y n
		String cache = (String) request
				.getParameter(FrameworkConstants.POST_CACHE);
		// fbid
		String deleteUser = (String) request
				.getParameter(FrameworkConstants.POST_DELETE_USER);
		// y n
		String deleteAll = (String) request
				.getParameter(FrameworkConstants.POST_DELETE_ALL);

		String deleteSchema = (String) request
				.getParameter(FrameworkConstants.POST_DELETE_SCHEMA);

		if (IOUtil.validate(debug))
		{
			if (IOUtil.validate(securityKey) && IOUtil.validate(serverKey))
			{
				// validated
				if (securityKey.equals(serverKey))
				{
					// first check reset
					if (IOUtil.parseValue(reset))
					{
						EnvManager.getInstance().reset();
					}
					else
					{
						// validate environment
						if (IOUtil.validate(env))
						{
							EnvManager.getInstance().addProperty(
									FrameworkConstants.POST_ENV, env);
						}

						if (IOUtil.validate(cache))
						{
							EnvManager.getInstance().addProperty(
									FrameworkConstants.POST_CACHE, cache);
						}

						DataManager dm = new DataManager();

						if (IOUtil.validate(deleteAll)
								&& IOUtil.validate(deleteSchema))
						{
							if (IOUtil.parseValue(deleteAll))
							{
								boolean isAll = false;

								// run through all schemas
								if (deleteSchema
										.equalsIgnoreCase(FrameworkConstants.POST_SCHEMA_ALL))
								{
									isAll = true;
								}

								if (deleteSchema
										.equalsIgnoreCase(FrameworkConstants.POST_SCHEMA_ACTIVITY)
										|| isAll)
								{
									dm.removeAllActivities();
								}

								if (deleteSchema
										.equalsIgnoreCase(FrameworkConstants.POST_SCHEMA_ASSOC)
										|| isAll)
								{
									dm.removeAllAssociations();
								}

								if (deleteSchema
										.equalsIgnoreCase(FrameworkConstants.POST_SCHEMA_FRIEND)
										|| isAll)
								{
									dm.removeAllFriends();
								}

								if (deleteSchema
										.equalsIgnoreCase(FrameworkConstants.POST_SCHEMA_USER)
										|| isAll)
								{
									dm.removeAllUsers();
								}

							}

						}

						if (IOUtil.validate(deleteUser))
						{
							// delete everything about the user
							long fid = Long.parseLong(deleteUser);
							dm.removeAllActivitesByUser(fid);
							dm.removeAllAssociationsByUser(fid);
							dm.removeUser(fid);
						}
					}
				}
			}
		}
	}

	private String createKey()
	{
		Date currentTime = new Date();
		Format formatter = new SimpleDateFormat("HHMMddyyyy");
		String fDate = formatter.format(currentTime);

		log.info("date: " + fDate);

		return fDate;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException
	{
		// TODO Auto-generated method stub

	}

}
