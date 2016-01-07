package com.atomicleopard.thundr.notification.gae;

import org.junit.Rule;
import org.junit.Test;

import com.threewks.thundr.gae.SetupAppengine;
import com.threewks.thundr.gae.objectify.SetupObjectify;

public class GaeNotificationTargetRepositoryTest {
	@Rule
	public SetupAppengine setupAppengine = new SetupAppengine();
	@Rule
	public SetupObjectify setupObjectify = new SetupObjectify(GaeNotificationTarget.class);

	private GaeNotificationTargetRepository repository = new GaeNotificationTargetRepository();

	@Test
	public void shouldMapToUsernames() {
		repository.list("a", "b");
	}
}
