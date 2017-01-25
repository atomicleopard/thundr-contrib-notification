package com.atomicleopard.thundr.notification;

import com.threewks.thundr.configuration.ConfigurationException;
import com.threewks.thundr.injection.BaseModule;
import com.threewks.thundr.injection.UpdatableInjectionContext;
import com.threewks.thundr.logger.Logger;
import com.threewks.thundr.route.Router;

import apns.ApnsConnectionFactory;
import apns.DefaultApnsConnectionFactory;
import apns.DefaultFeedbackService;
import apns.DefaultPushNotificationService;
import apns.FeedbackService;
import apns.PushNotificationService;
import apns.keystore.ClassPathResourceKeyStoreProvider;
import apns.keystore.KeyStoreProvider;
import apns.keystore.KeyStoreType;

public class NotificationModule extends BaseModule {
	public static final String PropertyApnsEnvironment = "notification.apns.environment";
	public static final String PropertyApnsKey = "notification.apns.keyfile";
	public static final String PropertyApnsPassword = "notification.apns.password";

	@Override
	public void configure(UpdatableInjectionContext injectionContext) {
		super.configure(injectionContext);
		configureApns(injectionContext);

		injectionContext.inject(NotificationService.class).as(NotificationService.class);
	}

	protected void configureApns(UpdatableInjectionContext injectionContext) {
		String apnsEnvironment = injectionContext.get(String.class, PropertyApnsEnvironment);
		ApnsEnvironment environment = ApnsEnvironment.from(apnsEnvironment);
		if (environment == null) {
			throw new ConfigurationException("Failed to start %s - required property '%s' is missing. You must specify which environment to target for apns, one of [%s, %s, %s]",
					this.getClass().getSimpleName(), PropertyApnsEnvironment, ApnsEnvironment.None, ApnsEnvironment.Sandbox, ApnsEnvironment.Production);
		}

		if (environment == ApnsEnvironment.None) {
			Logger.info(
					"%s: Notifications will not be sent to Apple Push Notification Service - to change this specify a target environment property in your applications.properties or similar as '%s=%s|%s'",
					this.getClass().getSimpleName(), PropertyApnsEnvironment, ApnsEnvironment.Sandbox, ApnsEnvironment.Production);
		} else {
			String apnsKeyfile = injectionContext.get(String.class, PropertyApnsKey);
			String apnsPassword = injectionContext.get(String.class, PropertyApnsPassword);
			if (apnsKeyfile == null) {
				throw new ConfigurationException("Failed to start %s - no keyfile was supplied. Specify a property in your applications.properties or similar as '%s=path/keyfile.p12'",
						this.getClass().getSimpleName(), PropertyApnsKey);
			}
			if (apnsPassword == null) {
				throw new ConfigurationException("Failed to start %s - no certificate password was supplied. Specify a property in your applications.properties or similar as '%s=password'",
						this.getClass().getSimpleName(), PropertyApnsPassword);
			}
			injectionContext.inject(new DefaultPushNotificationService()).as(PushNotificationService.class);
			injectionContext.inject(apnsConnectionFactory(apnsKeyfile, apnsPassword, environment)).as(ApnsConnectionFactory.class);
			injectionContext.inject(DefaultFeedbackService.class).as(FeedbackService.class);
		}
	}

	private ApnsConnectionFactory apnsConnectionFactory(String apnsKeyfile, String apnsPassword, ApnsEnvironment environment) {
		try {
			KeyStoreProvider ksp = new ClassPathResourceKeyStoreProvider(apnsKeyfile, KeyStoreType.PKCS12, apnsPassword.toCharArray());
			if (environment == ApnsEnvironment.Production) {
				return DefaultApnsConnectionFactory.Builder.get().setProductionKeyStoreProvider(ksp).build();
			} else {
				return DefaultApnsConnectionFactory.Builder.get().setSandboxKeyStoreProvider(ksp).build();
			}
		} catch (Exception e) {
			throw new ConfigurationException(e, "%s - failed creating Apple Push Notification Service connection factory - check the specified keyfile '%s' is correct and valid",
					this.getClass().getSimpleName(), apnsKeyfile);
		}
	}

	@Override
	public void start(UpdatableInjectionContext injectionContext) {
		super.start(injectionContext);
		Router router = injectionContext.get(Router.class);
		router.post("/notification/register/apns", NotificationController.class, "registerApns");
		router.post("/notification/register/gcm", NotificationController.class, "registerGcm");
		router.post("/admin/notification/remove-apns", NotificationController.class, "removeInvalidApnsDevices");
	}

}
