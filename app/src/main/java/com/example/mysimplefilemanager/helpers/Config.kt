package com.example.mysimplefilemanager.helpers

import android.content.Context
import com.example.common.extensions.getInternalStoragePath
import com.example.common.helpers.BaseConfig
import com.example.common.helpers.VIEW_TYPE_LIST
import java.io.File

class Config(context: Context) : BaseConfig(context) {
    companion object {
        fun newInstance(context: Context) = Config(context)
    }

    var homeFolder: String
        get(): String {
            var path = prefs.getString(HOME_FOLDER, "")!!
            if (path.isEmpty() || !File(path).isDirectory) {
                path = context.getInternalStoragePath()
                homeFolder = path
            }
            return path
        }
        set(homeFolder) = prefs.edit().putString(HOME_FOLDER, homeFolder).apply()

    var showHidden: Boolean
        get() = prefs.getBoolean(SHOW_HIDDEN, false)
        set(show) = prefs.edit().putBoolean(SHOW_HIDDEN, show).apply()

    var temporarilyShowHidden: Boolean
        get() = prefs.getBoolean(TEMPORARILY_SHOW_HIDDEN, false)
        set(temporarilyShowHidden) = prefs.edit().putBoolean(TEMPORARILY_SHOW_HIDDEN, temporarilyShowHidden).apply()

    var shouldShowHidden = showHidden || temporarilyShowHidden

    var viewType: Int
        get() = prefs.getInt(VIEW_TYPE, VIEW_TYPE_LIST)
        set(viewTypeFiles) = prefs.edit().putInt(VIEW_TYPE, viewTypeFiles).apply()

    fun getFolderViewType(path: String) = prefs.getInt(VIEW_TYPE_PREFIX + path.toLowerCase(), viewType)
}