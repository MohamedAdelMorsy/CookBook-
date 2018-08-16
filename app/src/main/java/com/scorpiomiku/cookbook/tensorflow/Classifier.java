package com.scorpiomiku.cookbook.tensorflow;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.scorpiomiku.cookbook.classifierresultactivity.ClassifierResultActivity;
import com.scorpiomiku.cookbook.takephoto.CameraActivity;
import com.scorpiomiku.cookbook.takephoto.MoreCameraActivity;
import com.youdao.sdk.app.Language;
import com.youdao.sdk.app.LanguageUtils;
import com.youdao.sdk.app.YouDaoApplication;
import com.youdao.sdk.ydonlinetranslate.Translator;
import com.youdao.sdk.ydtranslate.Translate;
import com.youdao.sdk.ydtranslate.TranslateErrorCode;
import com.youdao.sdk.ydtranslate.TranslateListener;
import com.youdao.sdk.ydtranslate.TranslateParameters;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ScorpioMiku on 2018/7/8.
 */

public class Classifier {
    private Set<String> resultsSet = new HashSet<String>();
//    public static String mPictureResult = "";

    private String TAG = "Classifier";
    private HashMap<String, String> options = new HashMap<String, String>();
    private Context mContext;
    private String result = " ";

    public Classifier(Context context) {
        mContext = context;
        YouDaoApplication.init(mContext, "0bc38eee2baaf2f8");
        options.put("top_num", "3");
        resultsSet.add("萝卜");
        resultsSet.add("红萝卜");
        resultsSet.add("胡萝卜");
        resultsSet.add("菠菜");
        resultsSet.add("茄子");
        resultsSet.add("菜花");
        resultsSet.add("花菜");
        resultsSet.add("黄瓜");
        resultsSet.add("姜");
        resultsSet.add("生姜");
        resultsSet.add("大葱");
        resultsSet.add("肘子");
        resultsSet.add("苹果");
        resultsSet.add("西兰花");
        resultsSet.add("韭菜");
        resultsSet.add("葱");
        resultsSet.add("狗贴耳");
        resultsSet.add("侧耳根");
        resultsSet.add("豆角");
        resultsSet.add("贡菜");
        resultsSet.add("白菜");
        resultsSet.add("油菜");
        resultsSet.add("空心菜");
        resultsSet.add("番茄");
        resultsSet.add("辣椒");
        resultsSet.add("土豆");
        resultsSet.add("南瓜");
//        resultsSet.add("西葫芦");
        resultsSet.add("冬瓜");
        resultsSet.add("大蒜");
        resultsSet.add("蒜");
        resultsSet.add("芹菜");
        resultsSet.add("香菜");
        resultsSet.add("蘑菇");
        resultsSet.add("凤尾菇");
    }

    public void classifierSingle(byte[] data) {

        Face_test face_test = new Face_test();
        final JSONObject res = face_test.plantDetect(data);
        String str = "";
        try {
            str = res.getJSONArray("objects").getJSONObject(0).getString("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onResult: " + str);

        Language langFrom = LanguageUtils.getLangByName("英文");
        Language langTo = LanguageUtils.getLangByName("中文");
        TranslateParameters tps = new TranslateParameters.Builder()
                .source("识菜帮")
                .from(langFrom).to(langTo).build();
        Translator translator = Translator.getInstance(tps);
        translator.lookup(str, "5a6d7b852ba9ea34", new TranslateListener() {
            @Override
            public void onError(TranslateErrorCode translateErrorCode, String s) {
                Log.e(TAG, "onError: " + translateErrorCode.toString() + ";" + s);
            }

            @Override
            public void onResult(Translate translate, String s, String s1) {
                result = " ";
                result = translate.getTranslations().get(0);
                if (resultsSet.contains(result)) {
                    result = result;
                } else if (result.equals("西葫芦")) {
                    result = "黄瓜";
                } else {
                    result = "";
                }
//                ClassifierResultActivity.mPictureResult = result;
                CameraActivity.mPictureResult = result;
                Log.d(TAG, "onResult final: " + CameraActivity.mPictureResult);
            }

            @Override
            public void onResult(List<Translate> list, List<String> list1, List<TranslateErrorCode> list2, String s) {

            }
        });
    }

    public void classifierMore(byte[] data) {
        Face_test face_test = new Face_test();
        result = " ";
        JSONObject res = face_test.plantDetect(data);
        try {
            result = res.getJSONArray("objects").getJSONObject(0).getString("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onResult more: " + result);

        Language langFrom = LanguageUtils.getLangByName("英文");
        Language langTo = LanguageUtils.getLangByName("中文");
        TranslateParameters tps = new TranslateParameters.Builder()
                .source("识菜帮")
                .from(langFrom).to(langTo).build();
        Translator translator = Translator.getInstance(tps);
        MyTranslateListener translateListener = new MyTranslateListener();
        translator.lookup(result, "5a6d7b852ba9ea34", translateListener);

    }

    public class MyTranslateListener implements TranslateListener {
        @Override
        public void onError(TranslateErrorCode translateErrorCode, String s) {
            Log.e(TAG, "onError: " + translateErrorCode.toString() + ";" + s);
        }

        @Override
        public void onResult(Translate translate, String s, String s1) {
            result = " ";
            result = translate.getTranslations().get(0);
            if (result.equals("西葫芦")) {
                result = "黄瓜";
            }
            if (resultsSet.contains(result)) {
                MoreCameraActivity.moreResults.add(result);
            } else {
//                Toast.makeText(mContext, "请正确拍摄", Toast.LENGTH_SHORT).show();

            }
            Log.d(TAG, "onResult more: " + result);
        }

        @Override
        public void onResult(List<Translate> list, List<String> list1, List<TranslateErrorCode> list2, String s) {

        }
    }
}

