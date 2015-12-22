package com.atomicleopard.thundr.notification;

public class Notification {
	private String message;
	//private int badgeIncrement = 1;
	
	public Notification(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	/*
	public int getBadgeIncrement() {
		return badgeIncrement;
	}
	*/

}
