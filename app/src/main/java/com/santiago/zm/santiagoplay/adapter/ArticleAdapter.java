package com.santiago.zm.santiagoplay.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.santiago.zm.santiagoplay.R;
import com.santiago.zm.santiagoplay.bean.Article;
import com.santiago.zm.santiagoplay.bridge.OnRecyclerItemClickListener;
import com.santiago.zm.santiagoplay.utils.HttpUtil;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by Santiago on 2017/9/4.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.MyViewHolder> {

    private List<Article> articles;
    private Handler handler;
    private OnRecyclerItemClickListener listener;
    private View headView,footView;
    private final static int TYPE_NOMAL = 0;
    private final static int TYPE_HEAD = 1;
    private final static int TYPE_FOOT = 2;

    public void setItemListener(OnRecyclerItemClickListener listener){
        this.listener = listener;
    }

    private static final String TAG = "ArticleAdapter";
    Fragment mFragment;

    public ArticleAdapter(List<Article> item, Fragment fragment) {
        articles = item;
        handler = new Handler(Looper.getMainLooper());
        mFragment = fragment;
    }

    public void updateList(List<Article> list){
        articles = list;
        notifyDataSetChanged();
    }

    public void setHeadView(View head){
        headView = head;
        notifyItemInserted(0);
    }

    public void setFootView(View foot){
        footView = foot;
        notifyItemInserted(getItemCount()-1);
    }

    public void removeHeadView(){
        notifyItemMoved(0,0);
    }

    public void removeFootView(){
        notifyItemMoved(articles.size()-1,articles.size()-1);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (headView!=null&&viewType==TYPE_HEAD){
            return new MyViewHolder(headView);
        }
        if (footView!=null&&viewType==TYPE_FOOT){
            return new MyViewHolder(footView);
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        if (headView==null&&footView==null){
            return TYPE_NOMAL;
        }
        if (headView!=null){
            return TYPE_HEAD;
        }
        if (footView!=null){
            return TYPE_FOOT;
        }
        return super.getItemViewType(position);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        int type = getItemViewType(position);
        if (type == TYPE_NOMAL){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(view,position);
                }
            });
            final Article article = articles.get(position);
            holder.title.setText(article.title);
            Glide.with(mFragment).load(article.pic).into(holder.icon);
        }


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final Bitmap bitmap = getIcon(article.pic, holder.icon);
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        holder.icon.setImageBitmap(bitmap);
//                    }
//                });
//            }
//        }).start();


    }

    private Bitmap getIcon(String pic, ImageView img) {
        Bitmap bm = HttpUtil.getHttpBitmap(pic);
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        int scale = getScale(imgWidth, imgHeight, options);
        Log.d(TAG, "getIcon mid: "+scale);
        options.inSampleSize = scale;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeByteArray(bytes,0,bytes.length,options);
        return bm;
    }

    private int getScale(int width, int height, BitmapFactory.Options options) {
        int bmwidth = options.outWidth;
        int bmHeight = options.outHeight;
        float widScale = bmwidth * 1.0f / width;
        float heiScale = bmHeight * 0.1f / height;
        float scale = widScale > heiScale ? widScale : heiScale;
        return (int) scale;
    }

    @Override
    public int getItemCount() {
        if (headView==null&&footView==null){
            return articles.size();
        }
        if (headView!=null&&footView!=null){
            return articles.size()+2;
        }
        return articles.size()+1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            if (itemView!=headView&&itemView!=footView){
                icon = itemView.findViewById(R.id.article_item_img);
                title = itemView.findViewById(R.id.article_item_title);
            }

        }
    }
}
