package com.gregtam.fbdfdetect.constants;

public class FacebookConstants
{
	// USER - publicly available
	public static final String KEY_ID = "id";
	public static final String KEY_FIRST_NAME = "first_name";
	public static final String KEY_LAST_NAME = "last_name";
	public static final String KEY_NAME = "name";

	public static final String KEY_THIRD_PARTY_ID = "third_party_id";
	public static final String KEY_LAST_UPDATE = "updated_time";

	public static final String KEY_DATA = "data";

	public static final String KEY_CLIENT_ID = "client_id";
	public static final String KEY_DISPLAY = "display";
	public static final String KEY_REDIRECT = "redirect_uri";
	public static final String KEY_SCOPE = "scope";
	public static final String KEY_CLIENT_SECRET = "client_secret";
	public static final String KEY_CODE = "code";
	public static final String KEY_ACCESS_TOKEN = "access_token";
	public static final String KEY_VERIFIED = "verified";
	public static final String KEY_EXPIRES = "expires";
	public static final String KEY_PICTURE = "picture";

	// require email permission
	public static final String KEY_EMAIL = "email";

	// FACEBOOK ACTIONS
	public static final String FB_BASE_URL = "https://graph.facebook.com";
	public static final String FB_AUTHORIZE = "oauth/authorize";
	public static final String FB_ACCESS = "oauth/access_token";
	public static final String FB_FRIENDS = "me/friends";
	public static final String FB_ME = "me";
	public static final String FB_STATIC_FRIEND_URL = "http://www.facebook.com/profile.php";
	public static final String FB_SECURE_STATIC_FRIEND_URL = "https://www.facebook.com/profile.php";
	public static final String FB_FIELDS = "fields";

	// user to store on request
	public static final String MY_USER = "MYUSER";

	public static final String POST_SIGNED_REQUEST = "signed_request";

	// authentication and authorization keys
	public static final String AUTH_OBJECT = "auth_object";
	public static final String AUTH_ERROR_REASON = "error_reason";
	public static final String AUTH_ERROR = "error";
	public static final String AUTH_ERROR_DESCRIPTION = "error_description";

	public static final String AUTH_CODE = "code";
	public static final String AUTH_TYPE = "type";
	public static final String AUTH_MESSAGE = "message";

	// available actions
	public static final String ACTION_LOGIN = "/login.jsp";
	public static final String ACTION_REDIR_DASHBOARD = "/fb/view";
	public static final String ACTION_DASHBOARD = "/dashboard.jsp";
	public static final String ACTION_GENERAL_ERROR = "/error.jsp";
	public static final String ACTION_APP_ERROR = "/errorApp.jsp";
	public static final String ACTION_PRIVACY = "/privacy.jsp";
	public static final String ACTION_RAW = "/raw.jsp";
}
