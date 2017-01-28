/*
 * This file is a part of thundr-contrib-notification, a software library from Atomic Leopard.
 *
 * Copyright (C) 2017 Atomic Leopard, <nick@atomicleopard.com.au>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
