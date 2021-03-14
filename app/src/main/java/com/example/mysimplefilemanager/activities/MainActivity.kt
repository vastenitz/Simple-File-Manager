package com.example.mysimplefilemanager.activities

import android.content.Intent
import android.os.Bundle
import com.example.common.activities.BaseSimpleActivity
import com.example.common.extensions.getRealPathFromURI
import com.example.common.extensions.internalStoragePath
import com.example.common.extensions.toast
import com.example.common.helpers.PERMISSION_WRITE_STORAGE
import com.example.mysimplefilemanager.R
import com.example.mysimplefilemanager.extensions.config
import com.example.mysimplefilemanager.extensions.tryOpenPathIntent
import java.io.File

class MainActivity : BaseSimpleActivity() {
    override fun getAppIconIDs() = arrayListOf(R.mipmap.ic_app_launcher)

    override fun getAppLauncherName() = getString(R.string.app_name)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handlePermission(PERMISSION_WRITE_STORAGE) {
            if (it) {
                initFileManager()
            } else {
                toast(R.string.no_storage_permissions)
                finish()
            }
        }
    }

    private fun initFileManager() {
        if (intent.action == Intent.ACTION_VIEW && intent.data != null) {
            val data = intent.data
            if (data?.scheme == "file") {
                openPath(data.path!!)
            } else {
                val path = getRealPathFromURI(data!!)
                if (path != null) {
                    openPath(path)
                } else {
                    openPath(config.homeFolder)
                }
            }

            if (!File(data.path!!).isDirectory) {
                tryOpenPathIntent(data.path!!, false)
            }
        } else {
            openPath(config.homeFolder)
        }
    }

    private fun openPath(path: String, forceRefresh: Boolean = false) {
        var newPath = path
        val file = File(path)
        if (file.exists() &&!file.isDirectory) {
            newPath = file.parent
        } else if (!file.exists()) {
            newPath = internalStoragePath
        }
    }
}