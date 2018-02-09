package sudo.example.org.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Created by cr7 on 4/1/18.
 */

public class BaseActivity extends Activity {
    /**
     * this is menu for music and other settings which is accessible from the main
     * as well as in game screen
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, Prefs.class));
                return true;

        }
        return false;
    }

    /**
     *end of the menu button
     */

    //plays the thug life tune when game start (either newly created or resumed)
    @Override
    public void onResume(){
        super.onResume();
        Music.start(this,R.raw.t__g_life);
    }
    //this is for pausing the music when user changes the activity
    @Override
    public void onPause(){
        super.onPause();
        Music.stop(this);
    }



}
