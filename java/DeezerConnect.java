package com.example.deezheart.controller;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.deezer.sdk.model.Permissions;
import com.deezer.sdk.network.connect.SessionStore;
import com.deezer.sdk.network.connect.event.DialogListener;
import com.example.deezheart.R;

public class DeezerConnect extends AppCompatActivity {

    private Button mChooseTracks;
    private Button mPlaylistCalme;
    private Button mPlaylistSport;

    private Integer mBPMValueMIN;
    private Integer mBPMValueMAX;
    private SeekBar mSeekBar;
    private TextView mSeekBarValue;
    private Integer valueSeekBar;

    private String applicationID = "426042";

    /** DeezerConnect object used for auhtentification or request. */
    protected com.deezer.sdk.network.connect.DeezerConnect mDeezerConnect = null;

    /** Sample app Deezer appId. */
    public static final String SAMPLE_APP_ID = "123565";



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //
        setContentView(R.layout.activity_deezer_connect);
        mChooseTracks = findViewById(R.id.spotify_tracks);
        mSeekBar = findViewById(R.id.seekBar);
        mSeekBarValue = findViewById(R.id.SeekBarValue);
        mPlaylistCalme = findViewById(R.id.BPMcalme);
        mPlaylistSport = findViewById(R.id.BPMsport);
        getSupportActionBar().hide();

        // Configuration du SeekBar
        this.mSeekBar.setMax(180);
        this.mSeekBar.setMin(70);
        this.mSeekBar.setProgress(110);

        this.mSeekBarValue.setText("BPM : " + mSeekBar.getProgress());

        this.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            // When Progress value changed.
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
                mSeekBarValue.setText("BPM : " + progressValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Authentification sur Deezer
        final com.deezer.sdk.network.connect.DeezerConnect deezerConnect = new com.deezer.sdk.network.connect.DeezerConnect(this, applicationID);

        // Vérification si la personne n'est pas déjà identifié
        final SessionStore sessionStore = new SessionStore();

        //final String test = deezerConnect.getCurrentUser().toString();

        if (sessionStore.restore(deezerConnect, this)) {
            Toast.makeText(this, "Tu es déja connecté", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Pas encore connecté", Toast.LENGTH_LONG).show();
        }

        // The set of Deezer Permissions needed by the app
        final String[] permissions = new String[] {
                Permissions.BASIC_ACCESS,
                Permissions.MANAGE_LIBRARY,
                Permissions.DELETE_LIBRARY,
                Permissions.LISTENING_HISTORY,
                Permissions.OFFLINE_ACCESS};

// The listener for authentication events
        final DialogListener listener = new DialogListener() {
            @Override
            public void onComplete(final Bundle values) {
                // store the current authentication info
                Toast.makeText(DeezerConnect.this, "oui", Toast.LENGTH_LONG).show();
                //sessionStore.save(deezerConnect, GameActivity.this);

                // Launch the Home activity
                //Intent intent = new Intent(GameActivity.this, MainActivity.class);
                //startActivity(intent);
            }

            @Override
            public void onException(final Exception exception) {
                Toast.makeText(DeezerConnect.this, "Deezer error",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(DeezerConnect.this, "Annulation de la connexion",
                        Toast.LENGTH_LONG).show();
            }
        };

        mChooseTracks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Fonction globale

                mSeekBar = findViewById(R.id.seekBar);
                valueSeekBar = mSeekBar.getProgress();

                String mBPMValueMINString = valueSeekBar.toString();
                mBPMValueMIN = Integer.parseInt(mBPMValueMINString) - 10;
                mBPMValueMAX = mBPMValueMIN + 20;

                // Accès à la page d'attente

                deezerConnect.authorize(DeezerConnect.this, permissions, listener);


                //Intent accesWaiting = new Intent(DeezerConnect.this, ChooseType.class);
                //accesWaiting.putExtra("BPMmin", mBPMValueMIN);
                //accesWaiting.putExtra("BPMmax", mBPMValueMAX);
                //startActivity(accesWaiting);
            }
        });

        mPlaylistCalme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Fonction globale

                mSeekBar = findViewById(R.id.seekBar);
                valueSeekBar = mSeekBar.getProgress();

                String mBPMValueMINString = valueSeekBar.toString();

                mSeekBarValue.setText("Playlist calme");

                mBPMValueMIN = 30;
                mBPMValueMAX = 80;

                //Toast.makeText(DeezerConnect.this, "Le choix des chansons se fera entre "+Math.round(mBPMValueMIN)+ " et " +Math.round(mBPMValueMAX), Toast.LENGTH_LONG).show();

                Intent accesWaiting = new Intent(DeezerConnect.this, ChooseType.class);
                accesWaiting.putExtra("BPMmin", mBPMValueMIN);
                accesWaiting.putExtra("BPMmax", mBPMValueMAX);
                startActivity(accesWaiting);
            }
        });

        mPlaylistSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Fonction globale

                mSeekBar = findViewById(R.id.seekBar);
                valueSeekBar = mSeekBar.getProgress();

                String mBPMValueMINString = valueSeekBar.toString();

                mSeekBarValue.setText("Playlist sportive");

                mBPMValueMIN = 140;
                mBPMValueMAX = 200;

                //Toast.makeText(DeezerConnect.this, "Le choix des chansons se fera entre "+Math.round(mBPMValueMIN)+ " et " +Math.round(mBPMValueMAX), Toast.LENGTH_LONG).show();

                Intent accesWaiting = new Intent(DeezerConnect.this, ChooseType.class);
                accesWaiting.putExtra("BPMmin", mBPMValueMIN);
                accesWaiting.putExtra("BPMmax", mBPMValueMAX);
                startActivity(accesWaiting);
            }
        });

    }

}
