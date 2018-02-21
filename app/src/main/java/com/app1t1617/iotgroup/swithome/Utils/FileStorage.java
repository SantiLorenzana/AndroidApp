package com.app1t1617.iotgroup.swithome.Utils;

import android.os.Environment;

import java.io.File;

import static com.app1t1617.iotgroup.swithome.Login.MainActivity.PACKAGE_NAME;

/**
 * Created by chema.dev on 20/2/18.
 */

public class FileStorage {

    /** Create a File for saving an image or video */
    public static File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + PACKAGE_NAME
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        File mediaFile;
        String mImageName="profileImage.jpeg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }
}


