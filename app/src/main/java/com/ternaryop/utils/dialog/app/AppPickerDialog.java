package com.ternaryop.utils.dialog.app;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ternaryop.utils.R;

/**
 * Created by dave on 01/05/14.
 * Helper dialog to select app from installed
 */
public class AppPickerDialog extends Dialog implements AdapterView.OnItemClickListener, View.OnClickListener {
    private LayoutInflater inflater = null;
    private final ResolveInfoAdapter resolveInfoAdapter;
    private PackageManager manager;
    private OnClickListener dialogClickListener;
    private ResolveInfo selectedApp;
    private final String fullPath;
    private final String mimeType;
    private final RadioButton defaultViewerRadio;
    private final RadioButton defaultViewerPerFileRadio;

    public AppPickerDialog(Context context, String fullPath, String mimeType) {
        super(context);
        this.fullPath = fullPath;
        this.mimeType = mimeType;
        setContentView(R.layout.dialog_app_picker);

        setTitle(context.getString(R.string.open_with_title));
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        manager = getContext().getPackageManager();

        findViewById(R.id.cancelButton).setOnClickListener(this);
        defaultViewerRadio = (RadioButton) findViewById(R.id.default_viewer);
        defaultViewerPerFileRadio = (RadioButton) findViewById(R.id.default_viewer_per_file);

        if (mimeType.startsWith("*/")) {
            defaultViewerRadio.setVisibility(View.GONE);
        } else {
            String[] type = mimeType.split("/");
            if (type.length == 2) {
                String label = context.getResources().getString(R.string.default_viewer_specific_mime_type_label, type[1]);
                defaultViewerRadio.setText(label);
            }
        }

        defaultViewerPerFileRadio.setChecked(AppPickerUtils.hasDefaultViewer(context, fullPath));

        resolveInfoAdapter = new ResolveInfoAdapter(context);
        resolveInfoAdapter.addAll(getActivitiesFromMimeType(mimeType));
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(resolveInfoAdapter);
        list.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.cancelButton) {
            dismiss();
        }
    }

    private void onOpenApp() {
        dismiss();

        if (selectedApp != null) {
            if (defaultViewerRadio.isChecked()) {
                // remove default path (if any)
                AppPickerUtils.resetDefaultViewer(getContext(), fullPath);
                String[] type = mimeType.split("/");
                if (type.length == 2 && !type[0].equals("*") && !type[1].equals("*")) {
                    ActivityInfo activity = selectedApp.activityInfo;
                    AppPickerUtils.setDefaultViewer(getContext(), mimeType,
                            activity.applicationInfo.packageName, activity.name);
                }
            } else if (defaultViewerPerFileRadio.isChecked()) {
                ActivityInfo activity = selectedApp.activityInfo;
                AppPickerUtils.setDefaultViewer(getContext(), fullPath,
                        activity.applicationInfo.packageName, activity.name);
            }
        }
        if (dialogClickListener != null) {
            dialogClickListener.onClick(this, BUTTON_POSITIVE);
        }
    }

    public void setOpenAppClickListener(OnClickListener dialogClickListener) {
        this.dialogClickListener = dialogClickListener;
    }
    
    private List<ResolveInfo> getActivitiesFromMimeType(String mimeType) {
        Intent intent = new Intent(Intent.ACTION_VIEW, null);
        intent.setType(mimeType);
        List<ResolveInfo> resolveInfos = manager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        Collections.sort(resolveInfos, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo lhs, ResolveInfo rhs) {
                return lhs.loadLabel(manager).toString().compareTo(rhs.loadLabel(manager).toString());
            }
        });
        return resolveInfos;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectedApp = resolveInfoAdapter.getItem(position);
        onOpenApp();
    }

    public ResolveInfo getSelectedApp() {
        return selectedApp;
    }

    class ResolveInfoAdapter extends ArrayAdapter<ResolveInfo> {

        public ResolveInfoAdapter(Context context) {
            super(context, 0);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_app_picker_row_image_text, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // https://android.googlesource.com/platform/packages/apps/Launcher3/+/master/src/com/android/launcher3/ThirdPartyWallpaperPickerListAdapter.java
            final ResolveInfo resolveInfo = getItem(position);
            manager = getContext().getPackageManager();
            holder.text.setText(resolveInfo.loadLabel(manager));
            holder.image.setImageDrawable(resolveInfo.loadIcon(manager));

            return convertView;
        }

        private class ViewHolder {
            final TextView text;
            final ImageView image;

            public ViewHolder(View vi) {
                text = (TextView)vi.findViewById(R.id.text1);
                image = (ImageView)vi.findViewById(R.id.image1);
            }
        }
    }
}
