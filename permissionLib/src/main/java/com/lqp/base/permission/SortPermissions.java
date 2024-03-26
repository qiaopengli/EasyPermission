package com.lqp.base.permission;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SortPermissions {
    private final List<String> deniedList = new ArrayList<>();
    private final List<String> grantedList = new ArrayList<>();
    private final List<String> allPermissions = new ArrayList<>();

    public SortPermissions(@NonNull Context context, String... permissions) {
        if (permissions != null) {
            allPermissions.addAll(Arrays.asList(permissions));
            sort(context);
        }
    }

    public SortPermissions sort(@NonNull Context context) {
        grantedList.clear();
        deniedList.clear();
        for (String permission : allPermissions) {
            if (EasyPermissionUtil.hasPermissions(context, permission)) {
                grantedList.add(permission);
            } else {
                deniedList.add(permission);
            }
        }
        return this;
    }

    @NonNull
    public List<String> getAllPermissions() {
        return allPermissions;
    }

    @NonNull
    public List<String> getDeniedList() {
        return deniedList;
    }

    @NonNull
    public List<String> getGrantedList() {
        return grantedList;
    }

    public boolean isGranted(){
        return deniedList.isEmpty();
    }
}
