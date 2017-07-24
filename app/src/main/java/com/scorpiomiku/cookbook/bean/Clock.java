package com.scorpiomiku.cookbook.bean;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/24.
 */

public class Clock {
    private static final String TAG = "Clock";
    private static final String SOUNDS_FOLDER = "clock_sounds";
    private AssetManager mAssetManager;
    private List<Sound> mSounds = new ArrayList<>();
    private static final int MAX_SOUNDS = 5;
    private SoundPool mSoundPool;


    public List<Sound> getSounds() {
        return mSounds;
    }

    public Clock(Context context) {
        mAssetManager = context.getAssets();

        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        loidSounds();
    }

    private void loidSounds() {
        String[] soundNames;
        try {
            soundNames = mAssetManager.list(SOUNDS_FOLDER);
            Log.d(TAG, "loidSounds: found" + soundNames.length);
        } catch (IOException e) {
            Log.d(TAG, "cant load assets", e);
            return;
        }
        for (String filename : soundNames) {
            String assetPath = SOUNDS_FOLDER + "/" + filename;
            Sound sound = new Sound(assetPath);
            try {
                load(sound);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mSounds.add(sound);
        }
    }

    private void load(Sound sound) throws IOException {
        AssetFileDescriptor afd = mAssetManager.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(afd, 1);
        sound.setSoundId(soundId);
    }

    public void play(Sound sound) {
        Integer soundId = sound.getSoundId();
        if (soundId == null) {
            return;
        }
        mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }
}
