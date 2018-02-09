package sudo.example.org.myapplication;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by cr7 on 4/1/18.
 */

public class Music {

    private static MediaPlayer mp = null;

    public static void start(Context context,int resource){
       stop(context);

        //check for the preferences for the music
        if (Prefs.getMusic(context)) {
            mp = MediaPlayer.create(context, resource);
            mp.setLooping(true);
            mp.start();
        }
    }

    public static void stop(Context context){
        if(mp!=null){
            mp.stop();
            mp.release();
            mp = null;
        }
    }
}
