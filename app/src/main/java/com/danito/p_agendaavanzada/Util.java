package com.danito.p_agendaavanzada;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.widget.ImageView;

public class Util {
    public enum Layout {GRID, LINEAR}

    public static Bitmap bitmapFromUri(Uri uri, Context context) {
        ImageView imageViewTemp = new ImageView(context);
        imageViewTemp.setImageURI(uri);
        BitmapDrawable d = (BitmapDrawable) imageViewTemp.getDrawable();
        return d.getBitmap();
    }
}
