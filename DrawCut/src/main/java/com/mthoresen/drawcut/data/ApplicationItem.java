package com.mthoresen.drawcut.data;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class ApplicationItem {

    private static PackageManager pm;

    private String appName;
    private Drawable icon;
    private String packageName;

    public ApplicationItem(Context context, ApplicationInfo ai) {
        if (pm == null) {
            pm = context.getPackageManager();
        }
        appName = pm.getApplicationLabel(ai).toString();
        icon = pm.getApplicationIcon(ai);
        packageName = ai.packageName;
    }


    public String getAppName() {
        return appName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public static ApplicationItem createFromPackageName(Context context, String packageName) {
        if (pm == null) {
            pm = context.getPackageManager();
        }
        ApplicationInfo ainfo;
        try {
            ainfo = pm.getApplicationInfo(packageName, 0);
            return new ApplicationItem(context, ainfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
