package com.example.fitcontroluser.Utilidades;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.GregorianCalendar;

public class Utilidades {


    public static final int MESSAGE_READ = 0;

   public static Bitmap cropToSquare(Bitmap bitmap){
       int width = bitmap.getWidth();
       int height = bitmap.getHeight();
       int newWidth = (height > width) ? width : height;
       int newHeight = (height > width)? height - ( height - width) : height;
       int cropW = (width - height) / 2; cropW = (cropW < 0)? 0: cropW;
       int cropH = (height - width) / 2; cropH = (cropH < 0)? 0: cropH;
       Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);
       return cropImg;
   }
    public static Bitmap getBitmapFromUri(Uri uri, Context context) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                context.getContentResolver().openFileDescriptor ( uri , "r" );
        FileDescriptor fileDescriptor = parcelFileDescriptor . getFileDescriptor ();
        Bitmap image = BitmapFactory. decodeFileDescriptor ( fileDescriptor );
        parcelFileDescriptor . close ();
        return image ;
    }
    public static int numeroDeDiasMes(String mes, int anio) {

        int numeroDias = -1;
        switch (mes.toLowerCase().trim()) {
            case "january":
            case "march":
            case "may":
            case "july":
            case "august":
            case "october":
            case "december":
                numeroDias = 31;
                break;
            case "april":
            case "june":
            case "september":
            case "november":
                numeroDias = 30;
                break;
            case "february":

                if (esBisiesto(anio)) {
                    numeroDias = 29;
                } else {
                    numeroDias = 28;
                }
                break;

        }

        return numeroDias;
    }

    public static boolean esBisiesto(int anio) {

        GregorianCalendar calendar = new GregorianCalendar();
        boolean esBisiesto = false;
        if (calendar.isLeapYear(anio)) {
            esBisiesto = true;
        }
        return esBisiesto;

    }
}
