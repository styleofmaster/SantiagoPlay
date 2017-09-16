package com.santiago.zm.santiagoplay.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.santiago.zm.santiagoplay.R;
import com.santiago.zm.santiagoplay.utils.ContentImageGetter;

/**
 * Created by Santiago on 2017/9/2.
 */

public class HistoryContentActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_his_content);
        Handler handler = new Handler();
        TextView tv = findViewById(R.id.content);
        String content = getIntent().getStringExtra("content");
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());//添加页面滚动
        tv.setText(Html.fromHtml(content,new ContentImageGetter(handler,tv),null));
//        tv.setText(content);
    }

}
