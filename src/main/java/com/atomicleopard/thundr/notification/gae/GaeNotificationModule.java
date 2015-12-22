package com.atomicleopard.thundr.notification.gae;

import com.atomicleopard.thundr.notification.NotificationModule;
import com.atomicleopard.thundr.notification.NotificationTargetRepository;
import com.googlecode.objectify.ObjectifyService;
import com.threewks.thundr.injection.BaseModule;
import com.threewks.thundr.injection.UpdatableInjectionContext;
import com.threewks.thundr.module.DependencyRegistry;

public class GaeNotificationModule extends BaseModule {

	@Override
	public void requires(DependencyRegistry dependencyRegistry) {
		super.requires(dependencyRegistry);
		dependencyRegistry.addDependency(NotificationModule.class);
	}

	@Override
	public void configure(UpdatableInjectionContext injectionContext) {
		super.configure(injectionContext);
		injectionContext.inject(GaeNotificationTargetRepository.class).as(NotificationTargetRepository.class);
	}

	@Override
	public void start(UpdatableInjectionContext injectionContext) {
		super.start(injectionContext);
		ObjectifyService.register(GaeNotificationTarget.class);
	}

}
