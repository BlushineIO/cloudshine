package io.blushine.cloudshine;

import com.google.apphosting.api.ApiProxy;

import java.io.UnsupportedEncodingException;

import javax.mail.internet.InternetAddress;

/**
 * Common server variables
 */
public class ServerConfig {
public static final String APP_NAME = ApiProxy.getCurrentEnvironment().getAppId().substring(2);
public static final String ADMIN_EMAIL = "matteus@blushine.io";
public static final String APP_EMAIL = "noreply@" + APP_NAME + ".appspotmail.com";
public static final InternetAddress APP_EMAIL_ADDRESS;
public static final InternetAddress ADMIN_EMAIL_ADDRESS;

static {
	InternetAddress adminAddress = null;
	InternetAddress appAddress = null;
	try {
		adminAddress = new InternetAddress(ADMIN_EMAIL, "Matteus Magnusson");
		appAddress = new InternetAddress(APP_EMAIL, "Server");
	} catch (UnsupportedEncodingException e) {
		e.printStackTrace();
	}
	ADMIN_EMAIL_ADDRESS = adminAddress;
	APP_EMAIL_ADDRESS = appAddress;
}
}
