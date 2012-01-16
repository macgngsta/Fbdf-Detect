package com.gregtam.fbdfdetect;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gregtam.fbdfdetect.constants.FrameworkConstants;
import com.gregtam.fbdfdetect.handler.RequestHandler;
import com.gregtam.fbdfdetect.model.FbdfRequest;
import com.gregtam.fbdfdetect.model.FbdfResponse;

@SuppressWarnings("serial")
public class FbdfdetectServlet extends HttpServlet
{
	private static final Logger log = Logger.getLogger(FbdfdetectServlet.class
			.getName());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		processParameters(req, resp);

	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		doPost(req, resp);
	}

	private void processParameters(HttpServletRequest httpRequest,
			HttpServletResponse resp)
	{

		FbdfRequest fbRequest = (FbdfRequest) httpRequest
				.getAttribute(FrameworkConstants.FBDF_REQUEST);

		RequestHandler myHandler = new RequestHandler();
		FbdfResponse fbResponse = myHandler.processRequest(fbRequest);
		httpRequest.setAttribute(FrameworkConstants.FBDF_RESPONSE, fbResponse);

		// we only need a redirect if coming straight from a login
		if (fbResponse.isNeedRedirect())
		{
			redirectTo(resp, fbResponse.getAction());
		}
		else
		{
			forwardTo(httpRequest, resp, fbResponse.getAction());
		}

	}

	private void forwardTo(HttpServletRequest req, HttpServletResponse resp,
			String page)
	{
		try
		{
			log.info("attempting fwd to " + page);
			RequestDispatcher dispatcher = getServletContext()
					.getRequestDispatcher(page);
			dispatcher.forward(req, resp);
		}
		catch (ServletException e)
		{
			log.info("unable to fwd to " + page + e);
			e.printStackTrace();
			throw new RuntimeException("unable to fwd");
		}
		catch (IOException e)
		{
			log.info("unable to fwd to " + page + e);
			e.printStackTrace();
			throw new RuntimeException("unable to fwd");
		}
	}

	private void redirectTo(HttpServletResponse resp, String page)
	{
		try
		{
			resp.sendRedirect(page);
		}
		catch (IOException e)
		{
			log.info("unable to redirect to " + page + e);
			e.printStackTrace();
			throw new RuntimeException("unable to redirect");
		}
	}
}
