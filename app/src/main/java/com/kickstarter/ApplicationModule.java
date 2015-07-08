package com.kickstarter;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;

import com.kickstarter.libs.Build;
import com.kickstarter.libs.ConfigLoader;
import com.kickstarter.libs.CurrentUser;
import com.kickstarter.libs.Font;
import com.kickstarter.libs.ForApplication;
import com.kickstarter.libs.Money;
import com.kickstarter.libs.preferences.StringPreference;
import com.kickstarter.libs.qualifiers.AccessTokenPreference;
import com.kickstarter.libs.qualifiers.UserPreference;
import com.kickstarter.services.ApiClient;
import com.kickstarter.services.KickstarterClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
  private final Application application;

  public ApplicationModule(final Application application) {
    this.application = application;
  }

  @Provides
  @Singleton
  @AccessTokenPreference
  StringPreference provideAccessTokenPreference(final SharedPreferences sharedPreferences) {
    return new StringPreference(sharedPreferences, "access_token");
  }

  @Provides
  @Singleton
  Application provideApplication() {
    return application;
  }

  @Provides
  @Singleton
  ApiClient provideApiClient(final Build build, final String clientId, final CurrentUser currentUser) {
    return new ApiClient(build, clientId, currentUser);
  }

  @Provides
  @Singleton
  @ForApplication
  Context provideApplicationContext() {
    return application;
  }

  @Provides
  @Singleton
  AssetManager provideAssetManager() {
    return application.getAssets();
  }

  @Provides
  @Singleton
  Build provideBuild(final PackageInfo packageInfo) {
    return new Build(packageInfo);
  }

  @Provides
  @Singleton
  String provideClientId() {
    // TODO: Switch based on environment
    return "***REMOVED***";
  }


  @Provides
  @Singleton
  ConfigLoader provideConfigLoader(final AssetManager assetManager) {
    return new ConfigLoader(assetManager);
  }

  @Provides
  @Singleton
  CurrentUser provideCurrentUser(@UserPreference StringPreference userPreference,
    @AccessTokenPreference StringPreference accessTokenPreference) {
    return new CurrentUser(userPreference, accessTokenPreference);
  }

  @Provides
  @Singleton
  Font provideFont(final AssetManager assetManager) {
    return new Font(assetManager);
  }

  @Provides
  @Singleton
  KickstarterClient provideKickstarterClient(final Build build) {
    return new KickstarterClient(build);
  }

  @Provides
  @Singleton
  Money provideMoney(final ConfigLoader configLoader) {
    return new Money(configLoader);
  }

  @Provides
  @Singleton
  PackageInfo providePackageInfo(final Application application) {
    try {
      return application.getPackageManager().getPackageInfo(application.getPackageName(), 0);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  String providePackageName(final Application application) {
    return application.getPackageName();
  }

  @Provides
  @Singleton
  SharedPreferences provideSharedPreferences() {
    return PreferenceManager.getDefaultSharedPreferences(application);
  }

  @Provides
  @Singleton
  @UserPreference
  StringPreference provideUserPreference(final SharedPreferences sharedPreferences) {
    return new StringPreference(sharedPreferences, "user");
  }
}