package com.atomicleopard.thundr.notification.gae;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.atomicleopard.expressive.ETransformer;
import com.atomicleopard.expressive.Expressive;
import com.atomicleopard.thundr.notification.NotificationTarget;
import com.atomicleopard.thundr.notification.NotificationTargetRepository;
import com.atomicleopard.thundr.notification.NotificationType;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;

public class GaeNotificationTargetRepository implements NotificationTargetRepository {

	@Override
	public NotificationTarget store(String username, String clientId, NotificationType type, String... categories) {
		return store(username, clientId, type, categories.length == 0 ? null : Arrays.asList(categories));
	}

	@Override
	public NotificationTarget store(String username, String clientId, NotificationType type, List<String> categories) {
		NotificationTarget notificationTarget = new GaeNotificationTarget(clientId, username, type, categories);
		ofy().save().entity(notificationTarget).now();
		return notificationTarget;
	}

	@Override
	public List<NotificationTarget> list(String username) {
		return listInternal(username, null, null);
	}

	@Override
	public List<NotificationTarget> list(String username, String category) {
		return listInternal(username, null, category);
	}

	@Override
	public List<NotificationTarget> list(String username, NotificationType type) {
		return listInternal(username, type, null);
	}

	@Override
	public List<NotificationTarget> list(String username, NotificationType type, String category) {
		return listInternal(username, type, category);
	}

	@Override
	public Map<String, List<NotificationTarget>> list(List<String> usernames) {
		List<NotificationTarget> results = listInternal(usernames, null, null);
		return ToUsernameLookup.from(results);
	}

	@Override
	public Map<String, List<NotificationTarget>> list(List<String> usernames, String category) {
		List<NotificationTarget> results = listInternal(usernames, null, category);
		return ToUsernameLookup.from(results);
	}

	@Override
	public Map<String, List<NotificationTarget>> list(List<String> usernames, NotificationType type) {
		List<NotificationTarget> results = listInternal(usernames, type, null);
		return ToUsernameLookup.from(results);
	}

	@Override
	public Map<String, List<NotificationTarget>> list(List<String> usernames, NotificationType type, String category) {
		List<NotificationTarget> results = listInternal(usernames, type, category);
		return ToUsernameLookup.from(results);
	}

	public List<NotificationTarget> listInternal(String username, NotificationType type, String category) {
		Query<NotificationTarget> filter = load().filter("username", username);
		return filterTypeAndCategory(type, category, filter).list();
	}

	protected List<NotificationTarget> listInternal(List<String> usernames, NotificationType type, String category) {
		Query<NotificationTarget> filter = load().filter("username in", usernames);
		return filterTypeAndCategory(type, category, filter).list();
	}

	protected Query<NotificationTarget> filterTypeAndCategory(NotificationType type, String category, Query<NotificationTarget> filter) {
		if (type != null) {
			filter = filter.filter("type", type);
		}
		if (category != null) {
			filter = filter.filter("categories", category);
		}
		return filter;
	}

	protected LoadType<NotificationTarget> load() {
		return ofy().load().type(NotificationTarget.class);
	}

	@Override
	public void clearAllFor(String username) {
		List<NotificationTarget> all = load().filter("username", username).list();
		ofy().delete().entities(all).now();
	}

	@Override
	public void clear(String... targetIds) {
		clear(Arrays.asList(targetIds));
	}

	@Override
	public void clear(List<String> targetIds) {
		ofy().delete().type(NotificationTarget.class).ids(targetIds).now();
	}

	private static final ETransformer<Collection<NotificationTarget>, Map<String, List<NotificationTarget>>> ToUsernameLookup = Expressive.Transformers.toBeanLookup("username",
			NotificationTarget.class);
}
