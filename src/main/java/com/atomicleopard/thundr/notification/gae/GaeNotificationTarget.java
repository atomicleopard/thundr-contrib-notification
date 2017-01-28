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

import java.util.List;

import com.atomicleopard.expressive.EList;
import com.atomicleopard.expressive.ETransformer;
import com.atomicleopard.expressive.Expressive;
import com.atomicleopard.thundr.notification.NotificationTarget;
import com.atomicleopard.thundr.notification.NotificationType;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Cache
@Entity
public class GaeNotificationTarget implements NotificationTarget{
	@Id
	public String id;
	@Index
	public NotificationType type;
	@Index
	public String username;

	/**
	 * You can have finer grained control over the notifications a particular device/notification target
	 * receives by specifying the categories notifcations are received for
	 */
	@Index
	public List<String> categories;

	public GaeNotificationTarget() {

	}

	public GaeNotificationTarget(String id, String username, NotificationType type, List<String> categories) {
		super();
		this.id = id;
		this.username = username;
		this.type = type;
		this.categories = categories;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public NotificationType getType() {
		return type;
	}

	@Override
	public List<String> getCategories() {
		return categories;
	}

	@Override
	public boolean isInCategory(String category) {
		return categories.contains(category);
	}

	public static final ETransformer<GaeNotificationTarget, String> ToId = Expressive.Transformers.toProperty("id", GaeNotificationTarget.class);
	public static final ETransformer<Iterable<GaeNotificationTarget>, EList<String>> ToIds = Expressive.Transformers.transformAllUsing(ToId);
}
