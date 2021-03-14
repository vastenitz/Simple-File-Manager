package com.example.mysimplefilemanager.extensions

import android.content.Context
import com.example.mysimplefilemanager.helpers.Config

val Context.config: Config get() = Config.newInstance(applicationContext)