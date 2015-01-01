package com.ternaryop.utils.intent;

import java.io.File;
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

import com.ternaryop.utils.dialog.app.AppPickerDialog;


/**
 * Created by dave on 11/05/14.
 * Helper class to create intents
 */
public class IntentUtils {
    public static Intent getAppIntentFromMimeType(PackageManager packageManager, String path, ComponentName componentName, String mimeType) {
        if (componentName != null) {
            Intent intent = createViewIntent(path, componentName, mimeType);

            // check intent existence
            List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (list != null && list.size() > 0) {
                return intent;
            }
        }
        return null;
    }

    public static Intent createViewIntent(String path, ComponentName componentName, String mimeType) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(path)), mimeType);
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT
                | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP
                | Intent.FLAG_RECEIVER_FOREGROUND);
        intent.setComponent(componentName);
        return intent;
    }

    public static void startViewActivity(final Activity activity,
                                         final String path,
                                         final ComponentName componentName,
                                         final String mimeType,
                                         boolean useDefaultViewer) {
        if (useDefaultViewer) {
            Intent intent = getAppIntentFromMimeType(activity.getPackageManager(), path, componentName, mimeType);

            // use default saved viewer
            if (intent != null) {
                activity.startActivity(intent);
                return;
            }
        }

        final AppPickerDialog appPickerDialog = new AppPickerDialog(activity, mimeType);
        appPickerDialog.setOpenAppClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityInfo activityInfo = appPickerDialog.getSelectedApp().activityInfo;
                ComponentName componentName1 = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);
                activity.startActivity(createViewIntent(path, componentName1, mimeType));
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
