package com.example.akhil.e_ayush.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akhil.e_ayush.R;

/**
 * Created by Akhil on 22-03-2018.
 */

public class LocDialog extends Dialog implements View.OnClickListener {

    private final Context context;
    private final LocObserver observer;
    private TextView button,siren;
    int i=1;
    boolean isPlaying=false;
    private MediaPlayer mPlayer2;

    public LocDialog(@NonNull Context context, LocObserver observer) {
        super(context);
        this.context = context;
        this.observer = observer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sec_survey);

        button=findViewById(R.id.move);
        siren=findViewById(R.id.siren);

        Toast.makeText(context, "Family Contacts and near by doctors informed", Toast.LENGTH_SHORT).show();

        siren.setOnClickListener(this);
        button.setOnClickListener(this);

        media();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.move:
                Toast.makeText(context, "Info updated", Toast.LENGTH_SHORT).show();
                break;
            case R.id.siren:
                media();
                break;
        }
//        MediaPlayer mPlayer2;
//        mPlayer2= MediaPlayer.create(this, R.raw.bg_music_wav);
//        mPlayer2.start();
////        observer.locCallback();
//        dismiss();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(isPlaying){
            mPlayer2.stop();
        }
    }

    private void media() {
        if(!isPlaying){
            mPlayer2= MediaPlayer.create(context, R.raw.buzz);
            mPlayer2.setLooping(true);
            mPlayer2.start();
            siren.setText("Off Siren");
            isPlaying=true;
        }
        else if(isPlaying){
            mPlayer2.stop();
            isPlaying=false;
            siren.setText("On Siren");
        }
    }

    public interface LocObserver{
        public void locCallback();
    }
}
