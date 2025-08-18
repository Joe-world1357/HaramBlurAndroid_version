package com.haramblur.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for HaramBlur app
 * Annotated with @HiltAndroidApp to enable Hilt dependency injection
 */
@HiltAndroidApp
class HaramBlurApplication : Application()