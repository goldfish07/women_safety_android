package com.example.womensafety.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HelpLine {
    String name;
    String number;
    String icon;

    public HelpLine(String icon, String name, String number) {
        this.icon=icon;
        this.name = name;
        this.number = number;
    }

    public Drawable getIcon(Context context) {
        return bitmapToDrawable(context, stringToBitmap(icon));
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    /**
     * @return bitmap (from given string)
     */
    @Nullable
    public static Bitmap stringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @NonNull
    public static Drawable bitmapToDrawable(@NonNull Context context, Bitmap bitmap) {
        return new BitmapDrawable(context.getResources(), bitmap);
    }

}