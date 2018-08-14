package com.apkglobal.scanmusic2;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer=new MediaPlayer();
    ListView listView;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    private static final int MY_PERMISSION_REQUEST =1;
    int songTitle;
    int songArtist;
    int songLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.list);
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSION_REQUEST);
        }
        else
        {
            doStuff();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)==
                            PackageManager.PERMISSION_GRANTED)
                    {
                        Toast.makeText(this, "Permission Granted    ", Toast.LENGTH_SHORT).show();
                        doStuff();
                    }
                }
                else
                {
                    Toast.makeText(this, "Permissiion not granted", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;

            }
        }


    }

    private void doStuff()
    {
        arrayList=new ArrayList<>();

        ContentResolver contentResolver=getContentResolver();
        Uri songUri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor=contentResolver.query(songUri,null,null,null,null);

        if(songCursor!=null&&songCursor.moveToFirst())
        {
            songTitle=songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            songArtist=songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            songLocation=songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do
            {
                String currentTitle=songCursor.getString(songTitle);
                String currentArtist=songCursor.getString(songArtist);
                String currentLocation=songCursor.getString(songLocation);
                arrayList.add(currentTitle+","+currentLocation);
            }while (songCursor.moveToNext());

        }

        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mediaPlayer.isPlaying()==true)
                {
                    mediaPlayer.pause();
                    mediaPlayer.stop();
                }

                AdapterView.OnItemClickListener listener;
                String path=arrayList.get(position);
                StringTokenizer stringTokenizer=new StringTokenizer(path,",");
                String first=stringTokenizer.nextToken();
                String second=stringTokenizer.nextToken();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                Uri uri=Uri.parse(second);

                mediaPlayer=MediaPlayer.create(MainActivity.this,uri);
                mediaPlayer.start();



                Toast.makeText(MainActivity.this, "Now Playing", Toast.LENGTH_SHORT).show();

                /*
                mediaPlayer=new MediaPlayer();
                String path="sdcard/Dil Tutda.mp3";
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                Uri uri=Uri.parse(path);
                try
                {
                    mediaPlayer.setDataSource(getApplicationContext(),uri);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    Toast.makeText(getApplicationContext(), "Now Playing", Toast.LENGTH_LONG).show();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                */

            }
        });

    }


}
