package com.example.akhil.e_ayush.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.akhil.e_ayush.R;

public class BodyPartActivity extends AppCompatActivity {

    ImageView body;
    String injuredPart="face";
    private Body[] b;

    class Body{
        String part;
        double upperLeftx,upperLefty,upperRightx,upperRighty,lowerLeftx,lowerLefty,lowerRightx,lowerRighty;

        public Body(String part,double upperLeftx,double upperLefty,double upperRightx,double upperRighty,double lowerLeftx,
                    double lowerLefty,double lowerRightx,double lowerRighty){
            this.part=part;
            this.upperLeftx=upperLeftx;
            this.upperLefty=upperLefty;
            this.upperRightx=upperRightx;
            this.upperRighty=upperRighty;
            this.lowerLeftx=lowerLeftx;
            this.lowerLefty=lowerLefty;
            this.lowerRightx=lowerRightx;
            this.lowerRighty=lowerRighty;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_part);

        findIds();
        b=new Body[7];
        b[0]=new Body("face",415,0,663,0,415,207,
                663,207);
        b[1]=new Body("right hand",0,240,400,240,0,440,
                400,440);
        b[2]=new Body("left hand",680,240,1079,240,680,440,
                1079,440);
        b[3]=new Body("chest",400,207,680,207,400,440,
                680,440);
        b[4]=new Body("stomach",400,440,680,440,400,670,
                680,670);
        b[5]=new Body("right leg",300,675,515,675,300,1600,
                515,1600);
        b[6]=new Body("left leg",520,675,800,675,520,1600,
                800,1600);
    }

    private void findIds() {
        body=findViewById(R.id.body_img_id);

        body.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int[] viewCoords = new int[2];
                body.getLocationOnScreen(viewCoords);

                int touchX = (int) motionEvent.getX();
                int touchY = (int) motionEvent.getY();

                double imageX = touchX - viewCoords[0]; // viewCoords[0] is the X coordinate
                double imageY = touchY - viewCoords[1]; // viewCoords[1] is the y coordinate
                double dis=0;
                for(int i=0;i<7;i++){
                    if(imageX>b[i].upperLeftx && imageX<b[i].upperRightx && imageY>b[i].upperLefty && imageY<b[i].lowerRighty){
                        injuredPart=b[i].part;
                        break;
                    }
                    for(int j=0;j<4;j++){
                        double x=0,y=0;
                        if(j==0){
                            x=b[i].upperLeftx;
                            y=b[i].upperLefty;
                        }
                        else if(j==1){
                            x=b[i].upperRightx;
                            y=b[i].upperRighty;
                        }
                        else if(j==2){
                            x=b[i].lowerLeftx;
                            y=b[i].lowerLefty;
                        }
                        else if(j==3){
                            x=b[i].lowerRightx;
                            y=b[i].lowerRighty;
                        }
                        double d=Math.sqrt(Math.pow((y-imageY),2)+Math.pow((x-imageX),2));
                        if(i==0 && j==0) {
                            dis = d;
                            injuredPart=b[i].part;
                        }
                        else if(d<dis){
                            dis=d;
                            injuredPart=b[i].part;
                        }
                    }
                }
                Log.e("deb",injuredPart+"\n"+imageX+"\n"+imageY);
                return true;
            }
        });
    }
}
