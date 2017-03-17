package com.google.samples.quickstart.config;

import android.app.Activity;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import io.reactivex.Single;

/**
 * Created by yasuhirosuzuki on 2017/03/16.
 *
 * See https://github.com/firebase/quickstart-android/blob/master/config/app/src/main/java/com/google/samples/quickstart/config/MainActivity.java
 */

public class RemoteConfigRepository {

    // Remote Config keys
    private static final String WELCOME_MESSAGE_KEY = "welcome_message";
    private static final String WELCOME_MESSAGE_CAPS_KEY = "welcome_message_caps";
    private static final String LOADING_PHRASE_CONFIG_KEY = "loading_phrase";

    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    public RemoteConfigRepository() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        if (BuildConfig.DEBUG) {
            enableDevMode();
        }
        // Set default Remote Config parameter values. An app uses the in-app default values, and
        // when you need to adjust those defaults, you set an updated value for only the values you
        // want to change in the Firebase console. See Best Practices in the README for more
        // information.
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
    }

    private void enableDevMode() {
        // Create a Remote Config Setting to enable developer mode, which you can use to increase
        // the number of fetches available per hour during development. See Best Practices in the
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
    }

    public Single<Welcome> fetchValue(Activity activity){
        // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
        // retrieve values from the service.
        long cacheExpiration = isDeveloperModeEnabled()? 0 : 3600; // 1 hour in seconds.
        return Single.create(singleEmitter ->
                // cacheExpirationSeconds is set to cacheExpiration here, indicating the next fetch request
                // will use fetch data from the Remote Config service, rather than cached parameter values,
                // if cached parameter values are more than cacheExpiration seconds old.
                // See Best Practices in the README for more information.
                mFirebaseRemoteConfig.fetch(cacheExpiration).addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        // After config data is successfully fetched, it must be activated before newly fetched
                        // values are returned.
                        mFirebaseRemoteConfig.activateFetched();
                        String message = mFirebaseRemoteConfig.getString(WELCOME_MESSAGE_KEY);
                        boolean caps = mFirebaseRemoteConfig.getBoolean(WELCOME_MESSAGE_CAPS_KEY);
                        singleEmitter.onSuccess(new Welcome(message, caps));
                    } else {
                        singleEmitter.onError(new Throwable("Fetch Failed"));
                    }
                }));
    }

    public String getLoadingText() {
        return mFirebaseRemoteConfig.getString(LOADING_PHRASE_CONFIG_KEY);
    }

    private boolean isDeveloperModeEnabled() {
        return mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled();
    }

}
