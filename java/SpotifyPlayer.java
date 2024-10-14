package com.example.deezheart.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.deezheart.R;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

public class SpotifyPlayer extends AppCompatActivity {

    private SpotifyAppRemote mSpotifyAppRemote;

    private ImageButton mPause;
    private ImageButton mPreviousTrack;
    private ImageButton mNextTrack;
    private TextView mNameSong;
    private TextView mMomentChanson;
    private SeekBar mSlider;
    private long mMoment;

    private static final String CLIENT_ID = "f4583493c2e94ea1902a550d79ef8451";
    private static final String REDIRECT_URI = "com.example.deezheart://callback";
    private static final String TAG = "Spotify" + SpotifyConnect.class.getSimpleName();
    private static final int REQUEST_CODE = 1337;
    public static String AUTH_TOKEN = "AUTH_TOKEN";


    // Connexion avec Spotify App Remote
    @Override
    protected void onStart() {
        super.onStart();

        //if (!AUTH_TOKEN.equals("AUTH_TOKEN")) {
        openLoginWindow();
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        // Now you can start interacting with App Remote
                        connected();
                    }

                    public void onFailure(Throwable throwable) {
                        Log.e("MyActivity", throwable.getMessage(), throwable);
                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
        //}
    }

    private void openLoginWindow() {

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"user-read-private", "streaming", "user-top-read", "user-read-recently-played", "user-library-read"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            final AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, data);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:

                    AUTH_TOKEN = response.getAccessToken();
                    //mSeekBarValue.setText(response.getAccessToken());

                    break;

                // Auth flow returned an error
                case ERROR:
                    Log.e(TAG, "Auth error: " + response.getError());
                    break;

                // Most likely auth flow was cancelled
                default:
                    Log.d(TAG, "Auth result: " + response.getType());
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
        //((TextView) findViewById(R.id.nameUser)).setText("Disconnect");
    }

    private void connected() {
        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        ((TextView) findViewById(R.id.nameSong)).setText(track.name);
                        ((TextView) findViewById(R.id.infoComplementaires)).setText(track.artist.name);

                        valeurMinute = new Long((track.duration / 60000)).toString();

                        ((TextView) findViewById(R.id.momentChanson)).setText(new Long(track.duration).toString());

                    }
                    else
                    {
                        ((TextView) findViewById(R.id.nameSong)).setText("- - - - - - - - - - -");
                        ((TextView) findViewById(R.id.infoComplementaires)).setText("- - -");
                    }
                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_player);
        mPause = findViewById((R.id.pause_track));
        mPreviousTrack = findViewById((R.id.previoustrack));
        mNextTrack = findViewById((R.id.next_track));
        mNameSong = findViewById(R.id.nameSong);
        mMomentChanson = findViewById(R.id.momentChanson);
        getSupportActionBar().hide();

        mSlider = findViewById(R.id.slider);

//.url("https://api.spotify.com/v1/me/player?fields=items(duration_ms)")
        // Récupérer la durée de la musique actuelle


        mSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                Integer mValeurMinute;
                Integer mValeurSeconde;
                mValeurMinute = progressChangedValue;
                mValeurSeconde = progressChangedValue % 60;
                mMomentChanson.setText(mValeurMinute.toString() + ":" + mValeurSeconde.toString());
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Integer mValeurMilli;
                mValeurMilli = mSlider.getProgress();
                mSpotifyAppRemote.getPlayerApi().seekTo(mValeurMilli);
            }
        });



        mPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSpotifyAppRemote.getPlayerApi().pause();
            }
        });

        mPreviousTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSpotifyAppRemote.getPlayerApi().skipPrevious();
            }
        });

        mNextTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSpotifyAppRemote.getPlayerApi().skipNext();
            }
        });
    }
}
