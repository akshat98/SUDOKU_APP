package sudo.example.org.myapplication;

import android.util.Log;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class OpeningActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View continueButton = findViewById(R.id.continue_button);
        continueButton.setOnClickListener(this);

        View newButton = findViewById(R.id.new_button);
        newButton.setOnClickListener(this);

        View aboutButton = findViewById(R.id.about_button);
        aboutButton.setOnClickListener(this);

        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
    }
    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.continue_button:
                startGame(Game.DIFFICULTY_CONTINUE);
                break;
            case R.id.new_button:
                openNewGameDialog();
                break;
            case R.id.about_button:
                Intent i =new Intent(this,About.class);
                startActivity(i);
                break;
            case R.id.exit_button:
                finish();
                break;

        }
    }




    //for new game classes needed are

    private static final String Tag="Sudoku";

    private void openNewGameDialog(){
        new AlertDialog.Builder(this)
            .setTitle(R.string.new_game_title)
            .setItems(R.array.difficulty,
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialogInterface,int i){
                        startGame(i);
                    }
                })
                .show();
    }

    private void startGame(int i){
        Log.d(Tag,"clicked on"+i);
        Intent intent=new Intent(this,Game.class);
        intent.putExtra(Game.KEY_DIFFICULTY,i);
        startActivity(intent);

    }

}
