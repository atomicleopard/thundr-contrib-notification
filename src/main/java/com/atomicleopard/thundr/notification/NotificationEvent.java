package com.atomicleopard.thundr.notification;

public class NotificationEvent {
	protected String type;
	protected Object content;

	public NotificationEvent(String type, Object content) {
		super();
		this.type = type;
		this.content = content;
	}

}
