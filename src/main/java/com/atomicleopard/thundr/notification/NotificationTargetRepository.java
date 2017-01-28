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
