package com.example.mysimplefilemanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.common.activities.BaseSimpleActivity
import com.example.common.extensions.getTextSize
import com.example.common.extensions.toast
import com.example.common.helpers.VIEW_TYPE_GRID
import com.example.common.helpers.VIEW_TYPE_LIST
import com.example.common.helpers.ensureBackgroundThread
import com.example.common.models.FileDirItem
import com.example.common.views.Breadcrumbs
import com.example.mysimplefilemanager.R
import com.example.mysimplefilemanager.dialogs.CreateNewItemDialog
import com.example.mysimplefilemanager.extensions.config
import com.example.mysimplefilemanager.helpers.RootHelpers
import com.example.mysimplefilemanager.models.ListItem
import kotlinx.android.synthetic.main.fragment_items.view.*

class ItemsFragment : Fragment(), Breadcrumbs.BreadcrumbsListener {

    lateinit var mView: View

    var currentPath = ""
    private var showHidden = false

    private var currentViewType = VIEW_TYPE_LIST

    private var storedItems = ArrayList<ListItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_items, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView.apply {
            items_swipe_refresh.setOnRefreshListener { refreshItems() }
            items_fab.setOnClickListener{ createNewItem() }
            bread_crumbs.listener = this@ItemsFragment
        }
    }

    private fun refreshItems() {
        openPath(currentPath)
    }

    private fun openPath(path: String, forceRefresh: Boolean = false) {
        var realPath = path.trimEnd('/')
        if (realPath.isEmpty()) {
            realPath = "/"
        }

        currentPath = realPath
        showHidden = context!!.config.shouldShowHidden
        getItems(currentPath) { originalPath, listItems ->
            if (currentPath != originalPath || !isAdded) {
                return@getItems
            }

            FileDirItem.sorting = context!!.config.getFolderSorting(currentPath)
            listItems.sort()
            activity?.runOnUiThread {
                activity?.invalidateOptionsMenu()
                addItems(listItems, forceRefresh)
                if (context != null && currentViewType != context!!.config.getFolderViewType(currentPath)) {
                    setupLayoutManager()
                }
            }
        }
    }

    private fun createNewItem() {
        CreateNewItemDialog(activity as BaseSimpleActivity, currentPath) {
            if (it) {
                refreshItems()
            } else {
                activity?.toast(R.string.unknown_error_occurred)
            }
        }
    }

    override fun breadcrumbClicked(id: Int) {

    }

    private fun setupLayoutManager() {
        if (context!!.config.getFolderViewType(currentPath) == VIEW_TYPE_GRID) {
            currentViewType = VIEW_TYPE_GRID
            setupGridLayoutManager()
        } else {
            currentViewType = VIEW_TYPE_LIST
            setupListLayoutManager()
        }

        mView.rcv_items.adapter = null
        initZoomListener()
        addItems(storedItems, true)
    }

    private fun addItems(items: ArrayList<ListItem>, forceRefresh: Boolean) {
        mView.apply {
            activity?.runOnUiThread {
                items_swipe_refresh?.isRefreshing = false
                bread_crumbs.setBreadcrumb(currentPath)
                if (!forceRefresh && items.hashCode() == storedItems.hashCode()) {
                    return@runOnUiThread
                }

                storedItems = items
                if (rcv_items.adapter == null) {
                    bread_crumbs.updateFontSize(context!!.getTextSize())
                }


            }
        }
    }

    private fun initZoomListener() {
        TODO("Not yet implemented")
    }

    private fun setupListLayoutManager() {
        TODO("Not yet implemented")
    }

    private fun setupGridLayoutManager() {
        TODO("Not yet implemented")
    }

    private fun getItems(path: String, callback: (originalPath: String, items: ArrayList<ListItem>) -> Unit) {
        ensureBackgroundThread {
            if (activity?.isDestroyed == false && activity?.isFinishing == false) {
                RootHelpers(activity!!).getFiles(path, callback)
            }
        }
    }
}