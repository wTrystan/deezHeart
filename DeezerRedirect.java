package com.example.deezheart.controller;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.player.AlbumPlayer;
import com.deezer.sdk.player.PlayerWrapper;
import com.deezer.sdk.player.event.PlayerState;
import com.example.deezheart.R;


public class DeezerRedirect extends AppCompatActivity {

    private PlayerWrapper mPlayer;
    private DeezerConnect mDeezerConnect;
    private Application mAlbumPlayercreate;
    private AlbumPlayer mAlbumPlayer;
    private TextView mAffichage;
    private static final String applicationID = "426042";
/////



    @Override
    protected void onDestroy() {
        doDestroyPlayer();
        super.onDestroy();
    }

    /**
     * Will destroy player. Subclasses can override this hook.
     */
    protected void doDestroyPlayer() {

        if (mPlayer == null) {
            // No player, ignore
            return;
        }

        if (mPlayer.getPlayerState() == PlayerState.RELEASED) {
            // already released, ignore
            return;
        }

        // first, stop the player if it is not
        if (mPlayer.getPlayerState() != PlayerState.STOPPED) {
            mPlayer.stop();
        }

        // then release it
        mPlayer.release();
    }

    /////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deezer_redirect);

        mDeezerConnect = new DeezerConnect(this, applicationID);
        //DeezerRequest request = DeezerRequestFactory.requestCurrentUser();
        mAlbumPlayercreate = this.getApplication();

        //Affiche le nom de l'utilsateur lorsqu'il se connecte
        ((TextView) findViewById(R.id.affichage)).setText("Bienvenue à toi, ");










        // create the player
       //try {
       //     AlbumPlayer mAlbumPlayer = new AlbumPlayer(mAlbumPlayercreate, mDeezerConnect, new WifiAndMobileNetworkStateChecker());
       // } catch (TooManyPlayersExceptions tooManyPlayersExceptions) {
        //    tooManyPlayersExceptions.printStackTrace();
       // } catch (DeezerError deezerError) {
       //     deezerError.printStackTrace();
       // }


// start playing music
        long albumId = 89142;
       //doDestroyPlayer();
        //mAlbumPlayer.playAlbum(albumId);

// ...

// to make sure the player is stopped (for instance when the activity is closed)
        //albumPlayer.stop();
        //albumPlayer.release();

        // Permet d'afficher un ptit pop up sympa
        Context context = getApplicationContext();
        CharSequence text = "Connexion effectuée!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();


        //if (mPlayer.getPlayerState() == PlayerState.PLAYING) {
            //mPlayer.pause();
        //
        //
        // } else {
            //mPlayer.play();
        }


    }