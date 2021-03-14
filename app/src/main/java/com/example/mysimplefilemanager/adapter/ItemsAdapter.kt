package com.example.mysimplefilemanager.adapter

import android.graphics.drawable.Drawable
import android.view.Menu
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.common.activities.BaseSimpleActivity
import com.example.common.adapters.MyRecyclerViewAdapter
import com.example.common.extensions.getColoredDrawableWithColor
import com.example.common.extensions.getTimeFormat
import com.example.common.helpers.getFilePlaceholderDrawables
import com.example.common.helpers.isOreoPlus
import com.example.common.models.FileDirItem
import com.example.common.views.MyRecyclerView
import com.example.mysimplefilemanager.R
import com.example.mysimplefilemanager.extensions.config
import com.example.mysimplefilemanager.models.ListItem
import java.util.ArrayList
import java.util.HashMap

class ItemsAdapter (activity: BaseSimpleActivity, var listItems: MutableList<ListItem>, recyclerView: MyRecyclerView,
                    val swipeRefreshLayout: SwipeRefreshLayout, itemClick: (Any) -> Unit) :
            MyRecyclerViewAdapter(activity, recyclerView, itemClick){

    private lateinit var fileDrawable: Drawable
    private lateinit var folderDrawable: Drawable
    private var fileDrawables = HashMap<String, Drawable>()

    private var dateFormat = ""
    private var timeFormat = ""

    init {
        initDrawables()
        dateFormat = activity.config.dateFormat
        timeFormat = activity.getTimeFormat()
    }

    override fun getActionMenuId() = R.menu.cab

    override fun prepareActionMode(menu: Menu) {
        menu.apply {
            findItem(R.id.cab_decompress).isVisible = getSelectedFileDirItems().map { it.path }.any { it.endsWith(".zip", true)}
            findItem(R.id.cab_confirm_selection).isVisible = true
            findItem(R.id.cab_copy_path).isVisible = isOneItemSelected()
            findItem(R.id.cab_open_with).isVisible = isOneFileSelected()
            findItem(R.id.cab_open_as).isVisible = isOneFileSelected()
            findItem(R.id.cab_set_as).isVisible = isOneFileSelected()
            findItem(R.id.cab_create_shortcut).isVisible = isOreoPlus() && isOneItemSelected()

            checkHideBtnVisibility(this)
        }
    }

    private fun checkHideBtnVisibility(menu: Menu) {
        var hiddenCnt = 0
        var unhiddenCnt = 0
        getSelectedFileDirItems().map { it.name }.forEach {
            if (it.startsWith(".")) {
                hiddenCnt++
            } else {
                unhiddenCnt++
            }
        }

        menu.findItem(R.id.cab_hide).isVisible = unhiddenCnt > 0
        menu.findItem(R.id.cab_unhide).isVisible = hiddenCnt > 0
    }

    override fun actionItemPressed(id: Int) {
        if(selectedKeys.isEmpty()) {
            return
        }

        when(id) {
            R.id.cab_confirm_selection -> confirmSelection()
            R.id.cab_rename -> displayRenameDialog()
            R.id.cab_properties -> showProperties()
            R.id.cab_share -> shareFiles()
            R.id.cab_hide -> toggleFileVisibility(true)
            R.id.cab_unhide -> toggleFileVisibility(false)
            R.id.cab_create_shortcut -> createShortcut()
            R.id.cab_copy_path -> copyPath()
            R.id.cab_set_as -> setAs()
            R.id.cab_open_with -> openWith()
            R.id.cab_open_as -> openAs()
            R.id.cab_copy_to -> copyMoveTo(true)
            R.id.cab_move_to -> tryMoveFiles()
            R.id.cab_compress -> compressSelection()
            R.id.cab_decompress -> decompressSelection()
            R.id.cab_select_all -> selectAll()
            R.id.cab_delete -> askConfirmDelete()
        }
    }

    private fun openAs() {
        
    }

    private fun copyMoveTo(b: Boolean) {

    }

    private fun tryMoveFiles() {

    }

    private fun compressSelection() {

    }

    private fun decompressSelection() {

    }

    private fun askConfirmDelete() {

    }

    private fun openWith() {

    }

    private fun setAs() {

    }

    private fun copyPath() {

    }

    private fun createShortcut() {

    }

    private fun toggleFileVisibility(b: Boolean) {

    }

    private fun shareFiles() {

    }

    private fun showProperties() {

    }

    private fun displayRenameDialog() {

    }

    private fun confirmSelection() {

    }

    override fun getSelectableItemCount() = listItems.filter { !it.isSectionTitle }.size

    override fun getIsItemSelectable(position: Int) = !listItems[position].isSectionTitle

    override fun getItemSelectionKey(position: Int) = listItems.getOrNull(position)?.path?.hashCode()

    override fun getItemKeyPosition(key: Int): Int {
        TODO("Not yet implemented")
    }

    override fun onActionModeCreated() {
        TODO("Not yet implemented")
    }

    override fun onActionModeDestroyed() {
        TODO("Not yet implemented")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    private fun isOneFileSelected() = isOneItemSelected() && getItemWithKey(selectedKeys.first())?.isDirectory == false

    private fun getItemWithKey(key: Int): FileDirItem? = listItems.firstOrNull { it.path.hashCode() == key }

    private fun getSelectedFileDirItems() = listItems.filter { selectedKeys.contains(it.path.hashCode()) } as ArrayList<FileDirItem>

    private fun initDrawables() {
        folderDrawable = resources.getColoredDrawableWithColor(R.drawable.ic_folder_vector, textColor)
        folderDrawable.alpha = 180
        fileDrawable = resources.getDrawable(R.drawable.ic_file_generic)
        fileDrawables = getFilePlaceholderDrawables(activity)
    }

}