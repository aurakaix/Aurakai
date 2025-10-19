﻿package dev.aurakai.auraframefx.ui.settings

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HiltTestActivity : AppCompatActivity() {
    @Inject
    lateinit var greetingProvider: GreetingProvider

    /**
     * Called when the activity is created; logs a debug message with the greeting provided by the injected GreetingProvider.
     *
     * Requires Hilt to have injected `greetingProvider` before this is called — accessing it otherwise will cause a runtime failure.
     *
     * @param savedInstanceState Standard Android saved instance state passed to `super.onCreate`.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("HiltTestActivity", greetingProvider.getGreeting())
    }
}
