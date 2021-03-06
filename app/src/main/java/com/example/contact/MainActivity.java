package com.example.contact;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView tv;
    ImageView iv;
    int drawable, rand = 0, sound = 0, flag = 0, temporary;
    String name = "", temp = "";
    MediaPlayer mp;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        iv = findViewById(R.id.iv);
        tv = findViewById(R.id.textView);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    if(mp == null){
                        mp = MediaPlayer.create(getApplicationContext(), sound);
                        fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_media_pause));
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                stopPlayer();
                                fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_media_play));
                            }
                        });
                        temporary = sound;
                        mp.start();
                    }else{
                        if(temporary == sound && flag == 0) {
                            flag = 1;
                            fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_media_play));
                            mp.pause();
                        }else if(temporary == sound && flag == 1){
                            flag = 0;
                            fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_media_pause));
                            mp.start();
                        }else if(temporary != sound){
                            fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_media_play));
                            stopPlayer();
                        }
                        fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_media_play));
                    }
                }catch (Resources.NotFoundException e){
                    Snackbar.make(view, "Please Choose sound", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void randNum(){
        Random r = new Random();
        rand = r.nextInt(5);
        choosePicture(rand);
    }

    public void choosePicture(int i){
        switch (i){
            case 0 :
                drawable = R.drawable.avatar_1;
                break;
            case 1:
                drawable = R.drawable.avatar_2;
                break;
            case 2:
                drawable = R.drawable.avatar_3;
                break;
            case 3:
                drawable = R.drawable.avatar_4;
                break;
            case 4:
                drawable = R.drawable.avatar_5;
                break;
        }
        iv.setImageResource(drawable);
    }

    public void changeContact(View view) {
        Intent intent = new Intent(getApplicationContext(), ChangeContact.class);
        startActivityForResult(intent,1);
    }

    public void changeSound(View view) {
        if(!tv.getText().equals("")) {
            Intent intent = new Intent(getApplicationContext(), ChooseSound.class);
            startActivityForResult(intent, 2);
        }else
            Toast.makeText(this, "First choose contact", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            if (resultCode == RESULT_OK) {
                temp = name;
                name = data.getStringExtra("nameSet");

                if(!temp.equals(name)){
                    tv.setText(name);
                    randNum();
                }

            }else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "I'm back!", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode == 2){
            if(resultCode == RESULT_OK){
                sound = data.getIntExtra("sound", 0);
            }else if(resultCode == RESULT_CANCELED){
                Toast.makeText(getApplicationContext(), "I'm back!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void stopPlayer(){
        if(mp != null){
            mp.release();
            mp = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }
}
