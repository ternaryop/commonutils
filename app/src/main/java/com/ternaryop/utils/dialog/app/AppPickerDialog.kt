package com.ternaryop.utils.dialog.app

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.RadioButton
import android.widget.TextView
import com.ternaryop.utils.R

/**
 * Created by dave on 01/05/14.
 * Helper dialog to select app from installed
 */
class AppPickerDialog(context: Context, private val fullPath: String, private val mimeType: String) :
    Dialog(context), AdapterView.OnItemClickListener, View.OnClickListener {
    private var inflater: LayoutInflater? = null
    private val resolveInfoAdapter: ResolveInfoAdapter
    private var manager: PackageManager? = null
    private var dialogClickListener: DialogInterface.OnClickListener? = null
    var selectedApp: ResolveInfo? = null
        private set
    private val defaultViewerRadio: RadioButton
    private val defaultViewerPerFileRadio: RadioButton

    init {
        setContentView(R.layout.dialog_app_picker)

        setTitle(context.getString(R.string.open_with_title))
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        manager = getContext().packageManager

        findViewById<View>(R.id.cancelButton).setOnClickListener(this)
        defaultViewerRadio = findViewById<View>(R.id.default_viewer) as RadioButton
        defaultViewerPerFileRadio = findViewById<View>(R.id.default_viewer_per_file) as RadioButton

        if (mimeType.startsWith("*/")) {
            defaultViewerRadio.visibility = View.GONE
        } else {
            val type = mimeType.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (type.size == 2) {
                val label = context.resources.getString(R.string.default_viewer_specific_mime_type_label, type[1])
                defaultViewerRadio.text = label
            }
        }

        defaultViewerPerFileRadio.isChecked = AppPickerUtils.hasDefaultViewer(context, fullPath)

        resolveInfoAdapter = ResolveInfoAdapter(context)
        resolveInfoAdapter.addAll(getActivitiesFromMimeType(mimeType))
        val list = findViewById<View>(R.id.list) as ListView
        list.adapter = resolveInfoAdapter
        list.onItemClickListener = this
    }

    override fun onClick(v: View) {
        val i = v.id
        if (i == R.id.cancelButton) {
            dismiss()
        }
    }

    private fun onOpenApp() {
        dismiss()

        selectedApp?.also { app ->
            if (defaultViewerRadio.isChecked) {
                // remove default path (if any)
                AppPickerUtils.resetDefaultViewer(context, fullPath)
                val type = mimeType.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (type.size == 2 && type[0] != "*" && type[1] != "*") {
                    val activity = app.activityInfo
                    AppPickerUtils.setDefaultViewer(
                        context, mimeType,
                        activity.applicationInfo.packageName, activity.name
                    )
                }
            } else if (defaultViewerPerFileRadio.isChecked) {
                val activity = app.activityInfo
                AppPickerUtils.setDefaultViewer(
                    context, fullPath,
                    activity.applicationInfo.packageName, activity.name
                )
            }
        }
        dialogClickListener?.onClick(this, DialogInterface.BUTTON_POSITIVE)
    }

    fun setOpenAppClickListener(dialogClickListener: DialogInterface.OnClickListener) {
        this.dialogClickListener = dialogClickListener
    }

    private fun getActivitiesFromMimeType(mimeType: String): List<ResolveInfo> {
        val manager = manager ?: return emptyList()
        val intent = Intent(Intent.ACTION_VIEW, null)
        intent.type = mimeType
        val resolveInfos = manager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        resolveInfos.sortWith { lhs, rhs ->
            lhs.loadLabel(manager).toString().compareTo(rhs.loadLabel(manager).toString())
        }
        return resolveInfos
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        selectedApp = resolveInfoAdapter.getItem(position)
        onOpenApp()
    }

    internal inner class ResolveInfoAdapter(context: Context) : ArrayAdapter<ResolveInfo>(context, 0) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val inflatedView: View
            val holder: ViewHolder

            if (convertView == null) {
                inflatedView = inflater!!.inflate(R.layout.list_app_picker_row_image_text, parent, false)
                holder = ViewHolder(inflatedView)
                inflatedView.tag = holder
            } else {
                inflatedView = convertView
                holder = inflatedView.tag as ViewHolder
            }
            // https://android.googlesource.com/platform/packages/apps/Launcher3/+/master/src/com/android/launcher3/ThirdPartyWallpaperPickerListAdapter.java
            val resolveInfo = getItem(position)
            manager = context.packageManager
            holder.text.text = resolveInfo!!.loadLabel(manager)
            holder.image.setImageDrawable(resolveInfo.loadIcon(manager))

            return inflatedView
        }

        private inner class ViewHolder(vi: View) {
            val text = vi.findViewById<View>(R.id.text1) as TextView
            val image = vi.findViewById<View>(R.id.image1) as ImageView
        }
    }
}
