package com.ternaryop.utils.intent;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.ternaryop.utils.dialog.app.AppPickerDialog;
import com.ternaryop.utils.dialog.app.AppPickerUtils;


/**
 * Created by dave on 11/05/14.
 * Helper class to create intents
 */
public class IntentUtils {
    public static Intent getAppIntentFromMimeType(Activity activity, Uri uri, ComponentName componentName, String mimeType) {
        if (componentName != null) {
            Intent intent = createViewIntent(activity, uri, componentName, mimeType);

            // check intent existence
            List<ResolveInfo> list = activity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (list != null && list.size() > 0) {
                return intent;
            }
        }
        return null;
    }

    public static Intent createViewIntent(Activity activity, Uri uriFile, ComponentName componentName, String mimeType) {
//        return ShareCompat.IntentBuilder.from(activity)
//                .setStream(uriFile) // uri from FileProvider
//                .setType(mimeType)
//                .getIntent()
//                .setAction(Intent.ACTION_VIEW) //Change if needed
//                .setDataAndType(uriFile, mimeType)
//                .addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT
//                        | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP
//                        | Intent.FLAG_RECEIVER_FOREGROUND
//                        | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uriFile, mimeType);
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT
                | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP
                | Intent.FLAG_RECEIVER_FOREGROUND
                | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setComponent(componentName);
        return intent;
    }

    public static void startViewActivity(@NonNull final Activity activity,
                                         @NonNull final Uri uri,
                                         @NonNull final String mimeType,
                                         boolean useDefaultViewer) {
        String path = uri.getPath();
        if (useDefaultViewer) {
            ComponentName componentName = AppPickerUtils.getDefaultViewerComponentName(activity, path, mimeType);
            Intent intent = getAppIntentFromMimeType(activity, uri, componentName, mimeType);

            // use default saved viewer
            if (intent != null) {
                activity.startActivity(intent);
                return;
            }
        }

        final AppPickerDialog appPickerDialog = new AppPickerDialog(activity, path, mimeType);
        appPickerDialog.setOpenAppClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityInfo activityInfo = appPickerDialog.getSelectedApp().activityInfo;
                ComponentName componentName1 = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);
                activity.startActivity(createViewIntent(activity, uri, componentName1, mimeType));
            }
        });
        appPickerDialog.show();
    }

    public static List<ResolveInfo> getActivitiesFromMimeType(final PackageManager manager, final String mimeType) {
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

}
