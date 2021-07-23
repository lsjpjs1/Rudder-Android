package com.rudder.util

import android.content.Context
import android.os.Build
import java.util.Locale

class LocaleUtil {

    fun getSystemLocale(context: Context):Locale{
        val systemLocale: Locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales.get(0);
        } else {
            context.resources.configuration.locale
        }
        return systemLocale
    }
    }
