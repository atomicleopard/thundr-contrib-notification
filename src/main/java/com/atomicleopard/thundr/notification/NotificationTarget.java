package com.atomicleopard.thundr.notification;

import java.util.List;

import com.atomicleopard.expressive.EList;
import com.atomicleopard.expressive.ETransformer;
import com.atomicleopard.expressive.Expressive;

public interface NotificationTarget {

	public String getId();

	public String getUsername();

	public NotificationType getType();

	public List<String> getCategories();

	public boolean isInCategory(String category);
}
