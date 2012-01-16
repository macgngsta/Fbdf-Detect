package com.gregtam.fbdfdetect.constants;

public class FrameworkConstants
{
	public static final String KEY_SESSION_USER = "SESSION_USER";

	public static final String ACTION_VIEW_DASHBOARD = "viewDashboard";
	public static final String ACTION_GET_ACTIVITY = "getActivity";
	public static final String ACTION_LOGIN = "login";

	public static final String FBDF_REQUEST = "fbdf_request";
	public static final String FBDF_RESPONSE = "fbdf_response";

	public static final String CONTEXT_AUTHENTICATION = "/auth";
	public static final String CONTEXT_VIEW = "/view";

	public static final String CONTEXT_UPDATE = "/update";
	public static final String CONTEXT_RAW = "/raw";
	public static final String CONTEXT_PRIVACY = "/privacy";

	// URL related
	public static final String URL_DELIMITER = "/";
	public static final String URL_CONCAT = "&";
	public static final String URL_PLUS = "+";
	public static final String URL_EQUALS = "=";
	public static final String URL_QUERY = "?";
	public static final String URL_COMMA = ",";

	public static final String SESSION_OBJECT = "session_object";

	public static final String POST_DEBUG = "debug";
	public static final String POST_SECURITY_KEY = "skey";
	public static final String POST_RESET = "reset";
	public static final String POST_ENV = "env";
	public static final String POST_CACHE = "cache";
	public static final String POST_DELETE_USER = "deluser";

	public static final String POST_START = "start";
	public static final String POST_LIMIT = "limit";

	public static final String POST_DELETE_SCHEMA = "schema";
	public static final String POST_DELETE_ALL = "delall";

	public static final String POST_SCHEMA_USER = "user";
	public static final String POST_SCHEMA_ACTIVITY = "activity";
	public static final String POST_SCHEMA_ASSOC = "assoc";
	public static final String POST_SCHEMA_FRIEND = "friend";
	public static final String POST_SCHEMA_ALL = "all";

	public static final String QUEUE_PROCESS_FRIENDS = "processFriends-queue";
	public static final String QUEUE_PF_URL = "/tasks/processFriends";
	public static final String QUEUE_PF_PARAM_FBID = "fbid";
	public static final String QUEUE_PF_PARAM_NEWUSER = "initial";
}
