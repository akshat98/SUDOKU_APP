package sudo.example.org.myapplication;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by cr7 on 19/12/17.
 */

public class Prefs extends PreferenceActivity {

    private static final String OPT_MUSIC="music";
    private static final String OPT_HINTS="hints";
    private static final boolean OPT_MUSIC_DEf=true;
    private static final boolean OPT_HINTS_DEf=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        Music.start(this,R.raw.t__g_life);

    }

    // get the current value of the menu option
    public static boolean getMusic(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_MUSIC,OPT_MUSIC_DEf);

    }
    // get the current value of the hints option
    public static boolean getHints(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_HINTS,OPT_HINTS_DEf);

    }
/*
    // some logic goes above, when you want to reset value and update EditTextPreference value
    // For convenience, I am going to wrap two different task in different methods
    private void resetPreferenceValue() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor prefEditor = sharedPref.edit(); // Get preference in editor mode
        prefEditor.putString("your_edit_text_pref_key", "DEFAULT-VALUE"); // set your default value here (could be empty as well)
        prefEditor.commit(); // finally save changes

        // Now we have updated shared preference value, but in activity it still hold the old value
        this.resetElementValue();
    }

    private void resetElementValue() {
        // First get reference to edit-text view elements
        EditTextPreference myPrefText = (EditTextPreference) super.findPreference("your_edit_text_pref_key");

        // Now, manually update it's value to default/empty
        myPrefText.setText("DEFAULT-VALUE"); // Now, if you click on the item, you'll see the value you've just set here
    }
}
*/
}
