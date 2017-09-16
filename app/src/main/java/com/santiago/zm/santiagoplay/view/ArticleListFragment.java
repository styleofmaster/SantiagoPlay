package com.santiago.zm.santiagoplay.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.santiago.zm.santiagoplay.R;
import com.santiago.zm.santiagoplay.adapter.ArticleAdapter;
import com.santiago.zm.santiagoplay.bean.Article;
import com.santiago.zm.santiagoplay.bridge.OnRecyclerItemClickListener;
import com.santiago.zm.santiagoplay.utils.HttpUtil;
import com.santiago.zm.santiagoplay.utils.UrlUtil;

import java.util.List;

/**
 * Created by Santiago on 2017/9/4.
 */

public abstract class ArticleListFragment extends Fragment {

    private List<Article> loadArticleList(int pageNum){
        return HttpUtil.getArticles(UrlUtil.getArticleUrl(getArticleId(),pageNum));
    }

    public abstract int getArticleId();

    ProgressBar progressBar;
    RecyclerView rv;
    Handler handler;
    int pageNum,scrollState;
    ArticleAdapter adapter;
    List<Article> currentList;
    TextView load;
    boolean isLoading;
    View foot;

    private void addList(){
        List<Article> newList = loadArticleList(pageNum);
        if (newList!=null&&newList.size()>0){
            for (int i=0;i<newList.size();i++){
                currentList.add(newList.get(i));
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article,container,false);
        initView(view);
        foot = LayoutInflater.from(getActivity()).inflate(R.layout.foot_recycler,null);
        return view;
    }

     private void initView(View view){
         handler = new Handler();
         load = view.findViewById(R.id.article_up_load);
         progressBar = view.findViewById(R.id.article_progress);
         rv = view.findViewById(R.id.article_recyler);
         final LinearLayoutManager manager = new LinearLayoutManager(getActivity());
         rv.setLayoutManager(manager);

         new Thread(new Runnable() {
             @Override
             public void run() {
                 currentList = loadArticleList(pageNum);
                 adapter = new ArticleAdapter(currentList,ArticleListFragment.this);
                 handler.post(new Runnable() {
                     @Override
                     public void run() {
                         adapter.setItemListener(new OnRecyclerItemClickListener() {
                             @Override
                             public void onItemClick(View view, int position) {
                                 Intent intent = new Intent(getActivity(),ArticleContentActivity.class);
                                 intent.putExtra("content",currentList.get(position));
                                 startActivity(intent);
                             }
                         });
                         progressBar.setVisibility(View.GONE);
                         rv.setAdapter(adapter);
                     }
                 });
             }
         }).start();

         rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
             @Override
             public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                 super.onScrollStateChanged(recyclerView, newState);
                 scrollState = newState;
             }

             @Override
             public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                 super.onScrolled(recyclerView, dx, dy);
                 int lastVisitDex = manager.findLastVisibleItemPosition();
                 int totleCount = manager.getItemCount();
                 if (!isLoading&&lastVisitDex==(totleCount-1)&&
                         scrollState==RecyclerView.SCROLL_STATE_DRAGGING){
                     isLoading = true;
//                     load.setVisibility(View.VISIBLE);
                     adapter.setFootView(foot);
                     pageNum++;
                     new Thread(new Runnable() {
                         @Override
                         public void run() {
                             addList();
                             handler.post(new Runnable() {
                                 @Override
                                 public void run() {
                                     isLoading = false;
//                                     adapter.removeFootView();
                                     adapter.updateList(currentList);
//                                     load.setVisibility(View.GONE);
                                 }
                             });
                         }
                     }).start();


                 }
             }
         });






     }
}
