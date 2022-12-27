package com.android.prj.voicegame.publicClasses;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.prj.voicegame.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class SoundPermission {

    private Context context;
    private Activity activity;
    private static final int PERMISSION_REQUEST_CODE = 100;
    public static boolean permissionGranted = false;

    public SoundPermission(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }

    public void getPermission(){
        Dexter.withContext(context).withPermissions( Manifest.permission.RECORD_AUDIO
                        ,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener( new MultiplePermissionsListener() {

                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if(multiplePermissionsReport.isAnyPermissionPermanentlyDenied()){
                            Toast.makeText( context, R.string.give_perms, Toast.LENGTH_SHORT ).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                } ).onSameThread().check();
    }
////

}
