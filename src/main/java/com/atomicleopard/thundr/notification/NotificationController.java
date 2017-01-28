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
package com.atomicleopard.thundr.notification;

import static com.atomicleopard.expressive.Expressive.map;

import java.util.List;

import com.threewks.thundr.logger.Logger;
import com.threewks.thundr.user.User;
import com.threewks.thundr.user.controller.Authenticated;
import com.threewks.thundr.view.json.JsonView;

public class NotificationController {
	protected NotificationService notificationService;

	public NotificationController(NotificationService notificationService) {
		super();
		this.notificationService = notificationService;
	}

	@Authenticated
	public JsonView registerApns(User user, String clientId, List<String> categories) {
		NotificationTarget result = notificationService.createNotificationTarget(user, clientId, NotificationType.APNS, categories);
		Logger.info("Registered %s for user %s for Apns notifications", result.getId(), user.getUsername());
		return new JsonView(map("accepted", result.getId()));
	}

	public JsonView registerGcm(User user, String clientId, List<String> categories) {
		NotificationTarget result = notificationService.createNotificationTarget(user, clientId, NotificationType.GCM, categories);
		Logger.info("Registered %s for user %s for Gcm notifications", result.getId(), user.getUsername());
		return new JsonView(map("accepted", result.getId()));
	}

	public JsonView removeInvalidApnsDevices() {
		int removed = notificationService.removeInvalidApnsTokens();
		return new JsonView(removed);
	}
}
