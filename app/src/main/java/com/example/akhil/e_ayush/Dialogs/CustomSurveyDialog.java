package com.example.akhil.e_ayush.Dialogs;

import android.app.Dialog;
import android.content.Context;
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

public class CustomSurveyDialog extends Dialog implements View.OnClickListener {

    private Observer observer;
    private Context context;
    private Button b1,b2,b3,b4;
    private TextView t1;

    public CustomSurveyDialog(@NonNull Context context, Observer observer) {
        super(context);
        this.context = context;
        this.observer = observer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.survey_view);

        b1=findViewById(R.id.canBreathe);
        b2=findViewById(R.id.cantBreathe);
        b3=findViewById(R.id.canMove);
        b4=findViewById(R.id.cantMove);

        t1=findViewById(R.id.moveToNext);

        t1.setOnClickListener(this);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(context, "Notified", Toast.LENGTH_SHORT).show();
        observer.response();
        dismiss();
    }

    public interface Observer {
        public void response();
    }
}
