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

import static com.atomicleopard.expressive.Expressive.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.atomicleopard.expressive.Expressive;
import com.threewks.thundr.exception.BaseException;
import com.threewks.thundr.logger.Logger;
import com.threewks.thundr.user.User;

import apns.ApnsConnection;
import apns.ApnsConnectionFactory;
import apns.CannotOpenConnectionException;
import apns.CannotUseConnectionException;
import apns.FailedDeviceToken;
import apns.FeedbackService;
import apns.PayloadException;
import apns.PushNotification;
import apns.PushNotificationService;

public class NotificationService {
	protected NotificationTargetRepository<NotificationTarget> notificationTokenStore;
	protected PushNotificationService apnsService;
	protected ApnsConnectionFactory apnsConnectionFactory;
	protected ApnsConnection apnsConnection;
	protected FeedbackService feedbackService;

	public NotificationService(NotificationTargetRepository<NotificationTarget> notificationTokenStore) {
		super();
		this.notificationTokenStore = notificationTokenStore;
	}

	public void setApnsService(PushNotificationService apnsService) {
		this.apnsService = apnsService;
	}

	public void setApnsConnectionFactory(ApnsConnectionFactory apnsConnectionFactory) {
		this.apnsConnectionFactory = apnsConnectionFactory;
		this.apnsConnection = createNewConnection();
	}

	protected ApnsConnection createNewConnection() {
		try {
			return this.apnsConnectionFactory.openPushConnection();
		} catch (CannotOpenConnectionException e) {
			throw new BaseException(e);
		}
	}

	public void setFeedbackService(FeedbackService feedbackService) {
		this.feedbackService = feedbackService;
	}

	public int send(Notification notification, String category, String... usernames) {
		return send(notification, category, Arrays.asList(usernames));
	}

	public int send(Notification notification, String category, List<String> usernames) {
		List<NotificationTarget> targets = listNotificationTargets(usernames, category);
		Map<NotificationType, List<NotificationTarget>> targetsByType = groupByType(targets);
		List<NotificationTarget> apns = targetsByType.get(NotificationType.APNS);
		List<NotificationTarget> gcm = targetsByType.get(NotificationType.GCM);
		int count = 0;
		count += sendApns(notification, apns);
		count += sendGcm(notification, gcm);
		return count;
	}

	private int sendGcm(Notification notification, List<NotificationTarget> gcm) {
		return 0;
	}

	private int sendApns(Notification notification, List<NotificationTarget> apns) {
		List<String> deviceTokens = toIds(apns);
		if (isNotEmpty(deviceTokens)) {
			// @formatter:off
			PushNotification pn = new PushNotification()
										  .setAlert(notification.getMessage())
										  //.setBadge(notification.getBadgeIncrement())
										  .setDeviceTokens(deviceTokens);
			// @formatter:on
			sendApnsWithRety(pn, false);

		}
		return deviceTokens.size();
	}

	protected void sendApnsWithRety(PushNotification pn, boolean retrying) {
		try {
			apnsService.send(pn, getConnection());
		} catch (CannotUseConnectionException e) {
			if (!retrying) {
				this.apnsConnection = createNewConnection();
				sendApnsWithRety(pn, true);
			} else {
				Logger.error("Failed to send APNS push notification, have already retried: %s", e.getMessage());
			}
		} catch (PayloadException e) {
			Logger.error("Failed to send APNS push notification - payload error: %s", e.getMessage());
		}
	}

	public int removeInvalidApnsTokens() {
		if (feedbackService == null) {
			return 0;
		}

		try {
			List<FailedDeviceToken> failedTokens = feedbackService.read(getConnection());

			List<String> deviceTokens = new ArrayList<>();
			for (FailedDeviceToken failedToken : failedTokens) {
				deviceTokens.add(failedToken.getDeviceToken());
			}
			notificationTokenStore.clear(deviceTokens);
			return deviceTokens.size();
		} catch (CannotUseConnectionException e) {
			return 0;
		}
	}

	protected ApnsConnection getConnection() {
		return apnsConnection;
	}

	public NotificationTarget createNotificationTarget(User user, String clientId, NotificationType type, List<String> categories) {
		return notificationTokenStore.store(user.getUsername(), clientId, type, categories);
	}

	public List<NotificationTarget> listNotificationTargets(List<String> usernames, String category) {
		Map<String, List<NotificationTarget>> all = notificationTokenStore.list(usernames, category);
		return Expressive.flatten(all.values());
	}

	public List<NotificationTarget> listNotificationTargets(String user) {
		return notificationTokenStore.list(user);
	}

	public void removeNotificationTarget(String clientId) {
		notificationTokenStore.clear(clientId);
	}

	public static List<String> toIds(List<NotificationTarget> apns) {
		List<String> result = new ArrayList<>(apns.size());
		for (NotificationTarget t : apns) {
			result.add(t.getId());
		}
		return result;
	}

	private static Map<NotificationType, List<NotificationTarget>> groupByType(List<NotificationTarget> targets) {
		List<NotificationTarget> apns = new ArrayList<>();
		List<NotificationTarget> gcm = new ArrayList<>();
		for (NotificationTarget n : targets) {
			if (n.getType() == NotificationType.APNS) {
				apns.add(n);
			}
			if (n.getType() == NotificationType.GCM) {
				gcm.add(n);
			}
		}
		return map(NotificationType.APNS, apns, NotificationType.GCM, gcm);
	}
}
