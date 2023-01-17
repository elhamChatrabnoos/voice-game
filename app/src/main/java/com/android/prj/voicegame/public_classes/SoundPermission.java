package com.android.prj.voicegame.public_classes;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

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
