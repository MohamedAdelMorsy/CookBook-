package com.scorpiomiku.cookbook.tensorflow;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.scorpiomiku.cookbook.classifierresultactivity.ClassifierResultActivity;
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
import java.util.List;

/**
 * Created by ScorpioMiku on 2018/7/8.
 */

public class Classifier {
    private String TAG = "Classifier";
    private HashMap<String, String> options = new HashMap<String, String>();
    private Context mContext;
    private String result = " ";

    public Classifier(Context context) {
        mContext = context;
        YouDaoApplication.init(mContext, "0bc38eee2baaf2f8");
        options.put("top_num", "3");
    }

    public String classifierSingle(byte[] data) {
        Face_test face_test = new Face_test();
        JSONObject res = face_test.plantDetect(data);
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
                ClassifierResultActivity.mPictureResult = translate.getTranslations().get(0);
                if (ClassifierResultActivity.mPictureResult.equals(" ")) {

                }
                Log.d(TAG, "onResult: " + ClassifierResultActivity.mPictureResult);
            }

            @Override
            public void onResult(List<Translate> list, List<String> list1, List<TranslateErrorCode> list2, String s) {

            }
        });
        return str;
    }

    public void classifierMore(byte[] data) {
        Face_test face_test = new Face_test();
        JSONObject res = face_test.plantDetect(data);
        try {
            result = res.getJSONArray("objects").getJSONObject(0).getString("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onResult: " + result);

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
            result = translate.getTranslations().get(0);
            MoreCameraActivity.moreResults.add(result);
            Log.d(TAG, "onResult: " + result);
        }

        @Override
        public void onResult(List<Translate> list, List<String> list1, List<TranslateErrorCode> list2, String s) {

        }
    }
}

