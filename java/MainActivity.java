package com.example.deezheart.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.deezer.sdk.network.connect.DeezerConnect;
import com.example.deezheart.R;


public class MainActivity extends AppCompatActivity {

    private Button mPlayButton;
    private Button mActivityFonctionnalite;
    private Button mSpotify;
    private Button mDeezer;
    private ImageButton mImageSpotify;
    private ImageButton mImageDeezer;

    /** DeezerConnect object used for auhtentification or request. */
    protected DeezerConnect mDeezerConnect = null;

    /** Sample app Deezer appId. */
    public static final String SAMPLE_APP_ID = "123565";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        mImageSpotify = findViewById(R.id.ImageSpotify);
        //mImageDeezer = findViewById(R.id.ImageDeezer);

        // Permet d'accéder à Spotify
        mImageSpotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SpotifyActivity = new Intent(MainActivity.this, SpotifyConnect.class);
                startActivity(SpotifyActivity);
            }
        });

        // Permet d'accéder à Deezer
        //mImageDeezer.setOnClickListener(new View.OnClickListener() {
           // @Override
           // public void onClick(View view) {
                //Intent DeezerActivity = new Intent(MainActivity.this, com.example.deezheart.controller.DeezerConnect.class);
                //startActivity(DeezerActivity);
          //  }
        //});
    }
}

