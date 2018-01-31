package io.blushine.cloudshine;

import java.util.Date;

/**
 * The feedback to send
 */
public class Feedback {
private Date date = new Date();
private String name;
private String email;
private String exception;
private String title;
private String message;
private String appName;
private String appVersion;
private String deviceInfo;
private boolean bugReport;
private boolean syncing = false;

public Feedback() {
}

public boolean isSyncing() {
	return syncing;
}

public void setSyncing(boolean syncing) {
	this.syncing = syncing;
}

public Date getDate() {
	return date;
}

public void setDate(Date date) {
	this.date = date;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}

public String getException() {
	return exception;
}

public void setException(String exception) {
	this.exception = exception;
}

public String getTitle() {
	return title;
}

public void setTitle(String title) {
	this.title = title;
}

public String getMessage() {
	return message;
}

public void setMessage(String message) {
	this.message = message;
}

public String getAppName() {
	return appName;
}

public void setAppName(String appName) {
	this.appName = appName;
}

public String getAppVersion() {
	return appVersion;
}

public void setAppVersion(String appVersion) {
	this.appVersion = appVersion;
}

public String getDeviceInfo() {
	return deviceInfo;
}

public void setDeviceInfo(String deviceInfo) {
	this.deviceInfo = deviceInfo;
}

public boolean isBugReport() {
	return bugReport;
}

public void setBugReport(boolean bugReport) {
	this.bugReport = bugReport;
}

public boolean hasException() {
	return exception != null && !exception.isEmpty();
}

public String getExceptionHtml() {
	return insertHtmlLineBreaks(exception);
}

/**
 * Convert regular line breaks to HTML line breaks. If the string already contain HTML line breaks
 * nothing it just returns the regular string
 * @param string the string to convert
 * @return string with HTML line breaks
 */
private String insertHtmlLineBreaks(String string) {
	if (string != null) {
		if (!string.contains("<br")) {
			return string.replace("\n", "<br />\n");
		}
		// Already containing HTML linebreaks
		else {
			return string;
		}
	} else {
		return null;
	}
}

public String getMessageHtml() {
	return insertHtmlLineBreaks(message);
}

public String getDeviceInfoHtml() {
	return insertHtmlLineBreaks(deviceInfo);
}

/**
 * Checks is the parameters are valid to create a feedback
 */
public boolean isValid() {
	boolean minimumOk = date != null &&
			appName != null && appName.length() > 0 &&
			appVersion != null && appVersion.length() > 0 &&
			deviceInfo != null && deviceInfo.length() > 0;

	if (!minimumOk) {
		return false;
	}

	// For all feedback that isn't an automatic exception there must be a title and a message
	if (!bugReport || exception == null || exception.length() == 0) {
		return title != null && title.length() > 0 && message != null && message.length() > 0;
	} else {
		return true;
	}
}
}
