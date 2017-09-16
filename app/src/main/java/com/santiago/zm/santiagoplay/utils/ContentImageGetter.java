package com.santiago.zm.santiagoplay.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.Handler;
import android.text.Html;
import android.widget.TextView;

/**
 * Created by Santiago on 2017/9/6.
 */

public class ContentImageGetter implements Html.ImageGetter {

    Handler handler;
    TextView textView;

    public ContentImageGetter(Handler handler,TextView tv){
        this.handler = handler;
        this.textView = tv;
    }

    @Override
    public Drawable getDrawable(final String s) {
        final LevelListDrawable drawable = new LevelListDrawable();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (s.startsWith("http")&&(s.endsWith("jpg")||s.endsWith("jpeg")||s.endsWith("png"))) {
                    Bitmap bm = HttpUtil.getHttpBitmap(s);
                    BitmapDrawable bd = new BitmapDrawable(bm);
                    drawable.addLevel(1, 1, bd);
                    drawable.setBounds(0, 0, bm.getWidth(), bm.getHeight());
                    drawable.setLevel(1);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.invalidate();
                            textView.setText(textView.getText());
                        }
                    });

                }
            }
        }).start();
        return drawable;
    }
}
