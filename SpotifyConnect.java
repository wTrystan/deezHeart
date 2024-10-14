package com.example.deezheart.controller;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.deezheart.R;
import com.sdsmdg.harjot.crollerTest.Croller;

public class SpotifyConnect extends AppCompatActivity {

    private ImageButton mChooseTracks;
    private ImageButton mPlaylistCalme;
    private ImageButton mPlaylistSport;
    private ImageButton mSoiree;
    private Integer mBPMValueMIN;
    private Integer mBPMValueMAX;
    private TextView mSeekBarValue;
    private Integer valueSeekBar;
    private Integer mValeurCalme;
    private Integer mValeurSport;
    private Integer mValeurSoiree;
    private Croller mScroll;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Récupération des élements de la page
        setContentView(R.layout.activity_spotify_connect);
        mChooseTracks = findViewById(R.id.spotify_tracks);
        mSeekBarValue = findViewById(R.id.SeekBarValue);
        mPlaylistCalme = findViewById(R.id.bpmcalme);
        mPlaylistSport = findViewById(R.id.bpmsport);
        mSoiree = findViewById(R.id.soiree);
        getSupportActionBar().hide();

        //Initialisation des boutons
        mValeurCalme = 0;
        mValeurSport=0;
        mValeurSoiree=0;
        mChooseTracks.setImageResource(R.drawable.boutoncoeurblanc);

        // Configuration du SeekBar
        Croller croller = (Croller) findViewById(R.id.croller);
        croller.setIsContinuous(false);
        croller.setMin(70);
        croller.setMax(200);
        croller.setProgress(110);
        croller.setStartOffset(25);
        croller.setLabelSize(0);
        croller.setSweepAngle(300);
        mSeekBarValue.setText("BPM : " + croller.getProgress());

        mScroll = findViewById(R.id.croller);
        mScroll.setOnProgressChangedListener(new Croller.onProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress) {
                mSeekBarValue.setText("BPM : " + progress);
                mValeurSport=0;
                mValeurSoiree=0;
                mValeurCalme=0;
                mPlaylistSport.setImageResource(R.drawable.sportblanc);
                mSoiree.setImageResource(R.drawable.partyblanc);
                mPlaylistCalme.setImageResource(R.drawable.zenblanc);
                mSeekBarValue.setVisibility(View.VISIBLE);
            }
        });


        mChooseTracks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Fonction globale

                mChooseTracks.setImageResource(R.drawable.boutoncoeurvert);

                valueSeekBar = mScroll.getProgress();
                mBPMValueMIN = 0;
                mBPMValueMAX = 0;

                // Cas pour Playlist Calme

                // Accès à la page d'attente
                if (mValeurCalme==1){
                Intent accescalme = new Intent(SpotifyConnect.this, WaitingScreen.class);
                accescalme.putExtra("Methode", 1);
                startActivity(accescalme);}

                // Cas pour Playlist Sport
                if (mValeurSport==1){
                Intent accessport = new Intent(SpotifyConnect.this, WaitingScreen.class);
                accessport.putExtra("Methode", 2);
                startActivity(accessport);}

                // Cas pour Playlist Soirée
                if (mValeurSoiree==1){
                Intent accessoiree = new Intent(SpotifyConnect.this, WaitingScreen.class);
                accessoiree.putExtra("Methode", 3);
                startActivity(accessoiree);}

                // Cas pour CHoix du BPM
                if (mValeurCalme==0 && mValeurSoiree==0 && mValeurSport==0){
                String mBPMValueMINString = valueSeekBar.toString();
                mBPMValueMIN = Integer.parseInt(mBPMValueMINString) - 10;
                mBPMValueMAX = mBPMValueMIN + 20;

                // Accès à la page d'attente
                Intent accesWaiting = new Intent(SpotifyConnect.this, WaitingScreen.class);
                accesWaiting.putExtra("BPMmin", mBPMValueMIN);
                accesWaiting.putExtra("BPMmax", mBPMValueMAX);
                accesWaiting.putExtra("Methode", 0);
                startActivity(accesWaiting);}
            }
        });

        mPlaylistCalme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Fonction globale

                if (mValeurCalme==0){
                    mValeurCalme=1;
                    mPlaylistCalme.setImageResource(R.drawable.zenvert);
                    mSeekBarValue.setVisibility(View.INVISIBLE);
                    // Retour aux couleurs normales
                    mValeurSport=0;
                    mValeurSoiree=0;
                    mPlaylistSport.setImageResource(R.drawable.sportblanc);
                    mSoiree.setImageResource(R.drawable.partyblanc);
                }
                else {
                    mValeurCalme = 0;
                    mPlaylistCalme.setImageResource(R.drawable.zenblanc);
                }
            }
        });

        mPlaylistSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Fonction globale

                if (mValeurSport==0){
                    mValeurSport=1;
                    mPlaylistSport.setImageResource(R.drawable.sportvert);
                    mSeekBarValue.setVisibility(View.INVISIBLE);
                    // Retour aux couleurs normales
                    mValeurCalme=0;
                    mValeurSoiree=0;
                    mPlaylistCalme.setImageResource(R.drawable.zenblanc);
                    mSoiree.setImageResource(R.drawable.partyblanc);
                }
                else {
                    mValeurSport = 0;
                    mPlaylistSport.setImageResource(R.drawable.sportblanc);
                }
            }
        });

        mSoiree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Fonction globale
                if (mValeurSoiree==0){
                    mValeurSoiree=1;
                    mSoiree.setImageResource(R.drawable.partyvert);
                    mSeekBarValue.setVisibility(View.INVISIBLE);
                    // Retour aux couleurs normales
                    mValeurSport=0;
                    mValeurCalme=0;
                    mPlaylistSport.setImageResource(R.drawable.sportblanc);
                    mPlaylistCalme.setImageResource(R.drawable.zenblanc);
                }
                else {
                    mValeurSoiree = 0;
                    mSoiree.setImageResource(R.drawable.partyblanc);
                }
            }
        });

    }

}

