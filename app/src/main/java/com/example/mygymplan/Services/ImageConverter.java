package com.example.mygymplan.Services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ImageConverter {

    // ---------------------------------------------------------------------------------------------------

    public String ConvertToString(Bitmap image) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytesImageEncoded = byteArrayOutputStream.toByteArray();

        return android.util.Base64.encodeToString(bytesImageEncoded, android.util.Base64.DEFAULT);
    }

    // ---------------------------------------------------------------------------------------------------

    public Bitmap ConvertToBitmap(String imageString) {

        byte[] bytesImageDecoded = Base64.decode(imageString, Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytesImageDecoded);

        return BitmapFactory.decodeStream(byteArrayInputStream);
    }
}
