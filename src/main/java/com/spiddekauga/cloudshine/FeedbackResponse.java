package com.spiddekauga.cloudshine;

/**
 * Feedback response
 */
public class FeedbackResponse {
private States state;

public FeedbackResponse(States state) {
	this.state = state;
}

public FeedbackResponse() {
}

public States getState() {
	return state;
}

public void setState(States state) {
	this.state = state;
}

public enum States {
	/** Successfully sent the feedback */
	SUCCESS,
	/** Failed to sent feedback due to incompleted Feedback */
	FAILED_FEEDBACK_INCOMPLETE,
	/** Some server error */
	FAILED_SERVER_ERROR
}
}
