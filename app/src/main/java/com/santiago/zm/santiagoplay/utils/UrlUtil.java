package com.santiago.zm.santiagoplay.utils;

import com.santiago.zm.santiagoplay.constant.Constant;

/**
 * Created by Santiago on 2017/9/7.
 */

public class UrlUtil {

    public static String getArticleUrl(int id, int pageNum) {

        return Constant.WECHAT_ARTICLE_URL + "?channelid=" + id
                + "&start=" + (pageNum * 10) + "&num=10&appkey="
                + Constant.JISU_APPKEY;
    }
}
