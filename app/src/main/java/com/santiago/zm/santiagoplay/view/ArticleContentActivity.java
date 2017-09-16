package com.santiago.zm.santiagoplay.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.santiago.zm.santiagoplay.R;
import com.santiago.zm.santiagoplay.bean.Article;
import com.santiago.zm.santiagoplay.utils.ContentImageGetter;


/**
 * Created by Santiago on 2017/9/6.
 */

public class ArticleContentActivity extends Activity {

    private Article article;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_content);
        article = (Article) getIntent().getSerializableExtra("content");

        initView();
    }

    private void initView() {
        Handler handler = new Handler();

        TextView title = findViewById(R.id.art_content_title);
        TextView date = findViewById(R.id.art_content_date);
        TextView author = findViewById(R.id.art_content_author);
        TextView summary = findViewById(R.id.art_content_summary);
        TextView content = findViewById(R.id.art_content_text);
        ImageView icon = findViewById(R.id.art_content_icon);

        title.setText(article.title);
        date.setText(article.time);
        author.setText(article.weixinaccount);
        String sum = article.weixinsummary;
        if (TextUtils.isEmpty(sum)){
            summary.setVisibility(View.GONE);
        }else {
            summary.setText(sum);
        }
        content.setText(Html.fromHtml(article.content,new ContentImageGetter(handler,content),null));
//        content.setText(article.content);
        Glide.with(this).load(article.pic).into(icon);

    }
}
