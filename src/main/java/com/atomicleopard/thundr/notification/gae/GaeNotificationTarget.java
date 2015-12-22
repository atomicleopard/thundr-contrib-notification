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
