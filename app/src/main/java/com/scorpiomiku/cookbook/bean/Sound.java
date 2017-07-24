package com.scorpiomiku.cookbook.bean;

/**
 * Created by Administrator on 2017/7/22.
 */

public class Sound {

    private String mAssetPath;
    private String mName;
    private Integer mSoundId;


    public Sound(String assetPath) {
        mAssetPath = assetPath;
        String[] components = assetPath.split("/");
        String filename = components[components.length - 1];
        mName = filename.replace(".wav", "");
    }

    public void setSoundId(Integer soundId) {
        mSoundId = soundId;
    }

    public Integer getSoundId() {
        return mSoundId;
    }

    public String getAssetPath() {
        return mAssetPath;
    }

    public String getName() {
        return mName;
    }

}
