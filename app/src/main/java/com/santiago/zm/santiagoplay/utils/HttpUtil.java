package com.santiago.zm.santiagoplay.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.Gson;
import com.santiago.zm.santiagoplay.bean.Article;
import com.santiago.zm.santiagoplay.bean.HistoryResult;
import com.santiago.zm.santiagoplay.bean.RequestDate;
import com.santiago.zm.santiagoplay.constant.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Santiago on 2017/9/2.
 */

public class HttpUtil {

    private static final String TAG = "HttpUtil";

    public static List<HistoryResult> getHistory(RequestDate reqDate) {
        List<HistoryResult> results = new ArrayList<>();
        String url = Constant.HISTORY_URL + "?appkey=" + Constant.JISU_APPKEY
                + "&month=" + reqDate.month
                + "&day=" + reqDate.day;
        Log.d(TAG, "getHistory: " + url);
        String response = null;
        try {
            Response res = get(url);
            if (res==null)
                return results;
            response = get(url).body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response != null) {
            try {
                JSONObject obj = new JSONObject(response);
                String result = obj.getString("result");
                JSONArray items = new JSONArray(result);
                for (int i = 0; i < items.length(); i++) {
                    String item = items.getString(i);
                    Gson gson = new Gson();
                    HistoryResult history = gson.fromJson(item, HistoryResult.class);
                    results.add(history);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    public static List<Article> getArticles(String url){
        List<Article> articles = new ArrayList<>();
        try {
            Response res = get(url);
            if (res==null)
                return articles;
            String response = get(url).body().string();
            JSONObject obj = new JSONObject(response);
            JSONObject result = obj.getJSONObject("result");
            JSONArray array = result.getJSONArray("list");
            for (int i = 0;i<array.length();i++){
                Gson gson = new Gson();
                Article article = gson.fromJson(array.getString(i),Article.class);
                articles.add(article);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return articles;

    }

    public static Response get(String url) {
        Response response = null;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static byte[] getBytes(String url){
        byte[] bytes = null;
        try {
            bytes = get(url).body().bytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static Bitmap getHttpBitmap(String url){
        byte[] bytes = new byte[0];
        try {
            Response res = get(url);
            if (res == null){
                return null;
            }
            bytes = res.body().bytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        return bitmap;
    }
}
