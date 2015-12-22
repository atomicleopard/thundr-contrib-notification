package com.atomicleopard.thundr.notification;

public enum ApnsEnvironment {
	None,
	Sandbox,
	Production;

	@Override
	public String toString() {
		return name();
	}

	public static ApnsEnvironment from(String apnsEnvironment) {
		for (ApnsEnvironment e : values()) {
			if (e.name().equalsIgnoreCase(apnsEnvironment)) {
				return e;
			}
		}
		return null;
	}
}
