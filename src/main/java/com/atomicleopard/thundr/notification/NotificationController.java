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
