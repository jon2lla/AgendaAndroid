package com.example.agenda.modelo;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agenda.R;

public class ReproductorSonidos extends AppCompatActivity {
    private SoundPool sp;
    private int sonidoPapelera;

    public ReproductorSonidos(Context c){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes audioAt = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            sp = new SoundPool.Builder()
                    .setMaxStreams(6)
                    .setAudioAttributes(audioAt)
                    .build();
        }
        else{
            sp = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        }
        sonidoPapelera = sp.load(c, R.raw.can_open_2, 1);
    }

    public void playSound(){
        sp.play(sonidoPapelera, 5, 5, 0, 0,1);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        sp.release();
        sp = null;
    }
}
