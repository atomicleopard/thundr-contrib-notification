package com.atomicleopard.thundr.notification.gae;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.atomicleopard.expressive.ETransformer;
import com.atomicleopard.expressive.Expressive;
import com.atomicleopard.thundr.notification.NotificationTargetRepository;
import com.atomicleopard.thundr.notification.NotificationType;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;

public class GaeNotificationTargetRepository implements NotificationTargetRepository<GaeNotificationTarget> {

	@Override
	public GaeNotificationTarget store(String username, String clientId, NotificationType type, String... categories) {
		return store(username, clientId, type, categories.length == 0 ? null : Arrays.asList(categories));
	}

	@Override
	public GaeNotificationTarget store(String username, String clientId, NotificationType type, List<String> categories) {
		GaeNotificationTarget notificationTarget = new GaeNotificationTarget(clientId, username, type, categories);
		ofy().save().entity(notificationTarget).now();
		return notificationTarget;
	}

	@Override
	public List<GaeNotificationTarget> list(String username) {
		return listInternal(username, null, null);
	}

	@Override
	public List<GaeNotificationTarget> list(String username, String category) {
		return listInternal(username, null, category);
	}

	@Override
	public List<GaeNotificationTarget> list(String username, NotificationType type) {
		return listInternal(username, type, null);
	}

	@Override
	public List<GaeNotificationTarget> list(String username, NotificationType type, String category) {
		return listInternal(username, type, category);
	}

	@Override
	public Map<String, List<GaeNotificationTarget>> list(List<String> usernames) {
		List<GaeNotificationTarget> results = listInternal(usernames, null, null);
		return ToUsernameLookup.from(results);
	}

	@Override
	public Map<String, List<GaeNotificationTarget>> list(List<String> usernames, String category) {
		List<GaeNotificationTarget> results = listInternal(usernames, null, category);
		return ToUsernameLookup.from(results);
	}

	@Override
	public Map<String, List<GaeNotificationTarget>> list(List<String> usernames, NotificationType type) {
		List<GaeNotificationTarget> results = listInternal(usernames, type, null);
		return ToUsernameLookup.from(results);
	}

	@Override
	public Map<String, List<GaeNotificationTarget>> list(List<String> usernames, NotificationType type, String category) {
		List<GaeNotificationTarget> results = listInternal(usernames, type, category);
		return ToUsernameLookup.from(results);
	}

	public List<GaeNotificationTarget> listInternal(String username, NotificationType type, String category) {
		Query<GaeNotificationTarget> filter = load().filter("username", username);
		return filterTypeAndCategory(type, category, filter).list();
	}

	protected List<GaeNotificationTarget> listInternal(List<String> usernames, NotificationType type, String category) {
		Query<GaeNotificationTarget> filter = load().filter("username in", usernames);
		return filterTypeAndCategory(type, category, filter).list();
	}

	protected Query<GaeNotificationTarget> filterTypeAndCategory(NotificationType type, String category, Query<GaeNotificationTarget> filter) {
		if (type != null) {
			filter = filter.filter("type", type);
		}
		if (category != null) {
			filter = filter.filter("categories", category);
		}
		return filter;
	}

	protected LoadType<GaeNotificationTarget> load() {
		return ofy().load().type(GaeNotificationTarget.class);
	}

	@Override
	public void clearAllFor(String username) {
		List<GaeNotificationTarget> all = load().filter("username", username).list();
		ofy().delete().entities(all).now();
	}

	@Override
	public void clear(String... targetIds) {
		clear(Arrays.asList(targetIds));
	}

	@Override
	public void clear(List<String> targetIds) {
		ofy().delete().type(GaeNotificationTarget.class).ids(targetIds).now();
	}

	private static final ETransformer<Collection<GaeNotificationTarget>, Map<String, List<GaeNotificationTarget>>> ToUsernameLookup = Expressive.Transformers.toBeanLookup("username",
			GaeNotificationTarget.class);
}
