/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package com.spiddekauga.cloudshine;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.spiddekauga.utils.Strings;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static com.spiddekauga.cloudshine.ServerConfig.ADMIN_EMAIL_ADDRESS;
import static com.spiddekauga.cloudshine.ServerConfig.APP_EMAIL;

@Api(
		name = "feedbackApi",
		version = "v1",
		namespace = @ApiNamespace(
				ownerDomain = "cloudshine.spiddekauga.com",
				ownerName = "Spiddekauga"
		)
)
public class FeedbackEndpoint {

@ApiMethod(name = "sendFeedback", httpMethod = "POST")
public FeedbackResponse sendFeedback(Feedback feedback) {
	if (feedback.isValid()) {
		try {
			Message message = new Message();
			message.build(feedback);
			message.send();
			return new FeedbackResponse(FeedbackResponse.States.SUCCESS);
		} catch (RuntimeException e) {
			return new FeedbackResponse(FeedbackResponse.States.FAILED_SERVER_ERROR);
		}
	} else {
		return new FeedbackResponse(FeedbackResponse.States.FAILED_FEEDBACK_INCOMPLETE);
	}
}

private static final class Message {
	private static final Logger mLogger = Logger.getLogger(Message.class.getName());
	private String mTitle = "";
	private InternetAddress[] mReplyTo = new InternetAddress[1];
	private InternetAddress mFrom = null;
	private String mMessage = "";
	private Date mDate;

	private void build(Feedback feedback) {
		buildAddresses(feedback);
		buildTitle(feedback);
		buildMessage(feedback);
		mDate = feedback.getDate();
	}

	private void buildAddresses(Feedback feedback) {
		String replyToEmail = feedback.getEmail();
		String fromEmail = ServerConfig.APP_EMAIL;

		String name = feedback.getName();
		if (name == null || name.isEmpty()) {
			name = "Anonymous";
		}

		try {
			if (replyToEmail != null) {
				mReplyTo[0] = new InternetAddress(replyToEmail, name);
			}
			mFrom = new InternetAddress(fromEmail, name);
		} catch (UnsupportedEncodingException e) {
			mLogger.warning("Invalid email address: " + replyToEmail + "\n" + Strings.exceptionToString(e));
			throw new RuntimeException(e);
		}
	}

	private void buildTitle(Feedback feedback) {
		// Title
		String title = feedback.getTitle();
		if (title != null) {
			mTitle += title;
		}

		// App name
		if (mTitle.length() > 0) {
			mTitle += " ";
		}
		mTitle += "#" + feedback.getAppName() + " ";

		// FeedbackEndpoint type
		if (feedback.isBugReport()) {
			if (feedback.hasException()) {
				mTitle += "#bug_exn";
			} else {
				mTitle += "#bug";
			}
		} else {
			mTitle += "#feedback";
		}
	}

	private void buildMessage(Feedback feedback) {
		// Message
		String message = feedback.getMessageHtml();
		if (message != null && !message.isEmpty()) {
			mMessage += message + "<br />\n<br />\n";
		}

		// Application information
		mMessage += "<strong>Application Information</strong><br />\n" +
				feedback.getAppName() + "-" + feedback.getAppVersion() + "<br />\n" +
				feedback.getDeviceInfoHtml();

		// Exception
		if (feedback.hasException()) {
			mMessage += "<br />\n<br />\n" +
					"<strong>Exception</strong><br />\n" +
					feedback.getExceptionHtml();
		}
	}

	void send() {
		Properties properties = new Properties();
		Session session = Session.getDefaultInstance(properties);
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(mFrom);
			if (mReplyTo[0] != null) {
				message.setReplyTo(mReplyTo);
			}
			message.addRecipient(javax.mail.Message.RecipientType.TO, ADMIN_EMAIL_ADDRESS);
			mLogger.info("APP Email: " + APP_EMAIL);
			message.setSubject(mTitle);
			message.setContent(mMessage, "text/html");
			message.setSentDate(mDate);
			Transport.send(message);
		} catch (MessagingException e) {
			mLogger.warning("Couldn't send message\n" + Strings.exceptionToString(e));
			throw new RuntimeException(e);
		}
	}
}
}
