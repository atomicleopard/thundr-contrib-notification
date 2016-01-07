package com.atomicleopard.thundr.notification;

import java.util.List;
import java.util.Map;

public interface NotificationTargetRepository<N extends NotificationTarget> {
	public N store(String username, String targetId, NotificationType type, String...categories);
	public N store(String username, String targetId, NotificationType type, List<String> categories);

	public List<N> list(String username);

	public List<N> list(String username, String category);

	public List<N> list(String username, NotificationType type);

	public List<N> list(String username, NotificationType type, String category);

	public Map<String, List<N>> list(List<String> usernames);

	public Map<String, List<N>> list(List<String> usernames, String category);

	public Map<String, List<N>> list(List<String> usernames, NotificationType type);

	public Map<String, List<N>> list(List<String> usernames, NotificationType type, String category);

	public void clearAllFor(String username);

	public void clear(String... targetId);

	public void clear(List<String> targetId);

}
