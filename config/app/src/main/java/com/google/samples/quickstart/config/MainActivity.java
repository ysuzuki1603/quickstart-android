/*
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * For more information on setting up and running this sample code, see
 * https://firebase.google.com/docs/remote-config/android
 */

package com.google.samples.quickstart.config;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    RemoteConfigRepository mRemoteConfigRepository;
    private TextView mWelcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRemoteConfigRepository = new RemoteConfigRepository();
        mWelcomeTextView = (TextView) findViewById(R.id.welcomeTextView);
        mWelcomeTextView.setText(mRemoteConfigRepository.getLoadingText());
        Button fetchButton = (Button) findViewById(R.id.fetchButton);
        fetchButton.setOnClickListener(v -> fetchWelcome());
        fetchWelcome();
    }

    /**
     * Fetch a welcome message from the Remote Config service, and then activate it.
     */
    private void fetchWelcome() {
        mRemoteConfigRepository.fetchValue(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::displayWelcomeMessage, error -> showToast(error.getMessage()));
    }

    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Display a welcome message in all caps if welcome_message_caps is set to true. Otherwise,
     * display a welcome message as fetched from welcome_message.
     */
    private void displayWelcomeMessage(Welcome welcome) {
        mWelcomeTextView.setAllCaps(welcome.isCaps());
        mWelcomeTextView.setText(welcome.getMessage());
    }
}
