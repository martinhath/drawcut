package net.hath.drawcut.util;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.Comparator;

public class ApplicationInfoComparator implements Comparator<ApplicationInfo> {

    private static PackageManager pm;

    public ApplicationInfoComparator(PackageManager pm){
        if(ApplicationInfoComparator.pm == null){
            ApplicationInfoComparator.pm = pm;
        }
    }

    @Override
    public int compare(ApplicationInfo ai1, ApplicationInfo ai2) {
        String name1 = pm.getApplicationLabel(ai1).toString();
        String name2 = pm.getApplicationLabel(ai2).toString();
        return name1.compareTo(name2);
    }
}
