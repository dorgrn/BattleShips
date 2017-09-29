package registration.utils;



import registration.users.UserManager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class ServletUtils {

    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";

    public static UserManager getUserManager(ServletContext servletContext) {
	if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
	    servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
	}
	return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }
//
//    public static int getIntParameter(HttpServletRequest request, String name) {
//	String value = request.getParameter(name);
//	if (value != null) {
//	    try {
//		return Integer.parseInt(value);
//	    } catch (NumberFormatException numberFormatException) {
//	    }
//	}
//	return INT_PARAMETER_ERROR;
//    }
}
