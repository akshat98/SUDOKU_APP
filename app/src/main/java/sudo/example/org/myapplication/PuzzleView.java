
package sudo.example.org.myapplication;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;

import static android.content.Context.VIBRATOR_SERVICE;
import static android.graphics.Paint.ANTI_ALIAS_FLAG;


/**
 * Created by cr7 on 22/12/17.
 */

public class PuzzleView extends View{
    private Context mcontext;
    private static final String TAG="Sudoku";
    private final Game game;

    private float width; //width of one tile
    private float height; //height of one tile
    private int selX;  //X index of selection
    private int selY; //Y index of selection
    private final Rect selRect=new Rect();

    /**
     * defining the Paint Objects
     */
    private Paint background;
    private Paint dark;
    private Paint hilite;
    private Paint lite;
    private Paint foreground;
    private Paint initialforeground;
    private Paint selected;
    private Paint hint;
    private int[] hintclrary=new int[3];
    private Rect r=new Rect();

    //to determine whether the the call to onDraw is first or not it is used in
    //determining the font color that distinguish default values from the
    //entered values
    private  int flag;
    //CONSTRUCTOR
    public PuzzleView(Context context){
        super(context);
        setPaintObjects();
        this.game = (Game) context;
        this.mcontext=context;
        setFocusable(true);
        setFocusableInTouchMode(true);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        width=w /9f;
        height=h /9f;
        getRect(selX,selY,selRect);
        Log.d(TAG,"onSizeChanged width"+width+" height "+height);
        super.onSizeChanged(w, h, oldw, oldh);
    }


    public void getRect(int x,int y,Rect rect){
        rect.set((int) (x*width),(int) (y*height),(int) (x*width+width),(int) (y*height+height));
    }





    public void setPaintObjects(){

        background = new Paint();
        background.setColor(getResources().getColor(R.color.puzzle_background));

        dark = new Paint();
        dark.setColor(getResources().getColor(R.color.puzzle_dark));
        dark.setStrokeWidth(2);

        hilite = new Paint();
        hilite.setColor(getResources().getColor(R.color.puzzle_hilite));
        hilite.setStrokeWidth(2);


        lite = new Paint();
        lite.setColor(getResources().getColor(R.color.puzzle_lite));
        lite.setStrokeWidth(2);


        foreground = new Paint(ANTI_ALIAS_FLAG);
        foreground.setColor(getResources().getColor(R.color.puzzle_foreground));


        initialforeground = new Paint(ANTI_ALIAS_FLAG);
        initialforeground.setColor(getResources().getColor(R.color.colorPrimaryDark));

        selected = new Paint();
        selected.setColor(getResources().getColor(R.color.puzzle_selected));

        // hint have array of colors
        hint=new Paint();
        hintclrary[0] =getResources().getColor(R.color.puzzle_hint_0);
        hintclrary[1]=     getResources().getColor(R.color.puzzle_hint_1);
        hintclrary[2]= getResources().getColor(R.color.puzzle_hint_2);

}



    // drawing the board

    protected void onDraw(Canvas canvas){
        // draw the background...
        canvas.drawRect(0,0,getWidth(),getHeight(),background);

        //DRAW THE MINOR GRID LINES
        for(int i=0 ; i<9 ;i++){
            canvas.drawLine(0,i*height,getWidth(),i*height,lite);
            canvas.drawLine(0,i*height+2,getWidth(),i*height+1,hilite);
            canvas.drawLine(i*width,0,i*width,getHeight(),lite);
            canvas.drawLine(i*width+2,0,i*width+2,getHeight(),hilite);
        }
        //DRAW MAJOR GRID LINES
        for(int i=0 ; i<9;i++){
            if(i%3 == 0) {
                canvas.drawLine(0, i * height, getWidth(), i * height, dark);
                canvas.drawLine(0, i * height + 2, getWidth(), i * height + 2, hilite);
                canvas.drawLine(i * width, 0, i * width, getHeight(), dark);
                canvas.drawLine(i * width + 2, 0, i * width + 2, getHeight(), hilite);
            }
        }
        //DRAWING THE NUMBERS



        foreground.setStyle(Paint.Style.FILL);
        foreground.setTextAlign(Paint.Align.CENTER);
        foreground.setTextSize(height*0.75f);
        foreground.setTextScaleX(width/height);

        initialforeground.setStyle(Paint.Style.FILL);
        initialforeground.setTextAlign(Paint.Align.CENTER);
        initialforeground.setTextSize(height*0.75f);
        initialforeground.setTextScaleX(width/height);

        // DRAW THE NUMBER IN CENTER OF TILE
        Paint.FontMetrics fm= foreground.getFontMetrics();
        //Centring in x : use alignment (and X at midpoint)
        float x=width/2;
        // Centring in y : use alignment (and Y at midpoint)
        float y=height/2 - (fm.ascent+fm.descent)/2;

        //Filling the tiles with generated numbers by getTileString method



        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(game.checkDefaultValue(i,j))
                    canvas.drawText(this.game.getTileString(i, j), i * width + x, j * height + y, foreground);
                else
                    canvas.drawText(this.game.getTileString(i, j), i * width + x, j * height + y, initialforeground);

            }
        }

    //Checks for the hints in the preference class    
        if (Prefs.getHints(getContext())) {
            // Draw the hints ...
            // Pick a hint color based on #moves left
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    int movesleft = 9 - game.getUsedTiles(i, j).length;
                    if (movesleft < hintclrary.length) {
                        getRect(i, j, r);
                        hint.setColor(hintclrary[movesleft]);
                        canvas.drawRect(r, hint);
                    }
                }
            }
        }
        // draw the selection...
        Log.d(TAG,"select Rect"+selRect);
        canvas.drawRect(selRect,selected);


    }

    /**
     *
     * @param keyCode which number is pressed on a D-Pad
     * @param event  figures out which key is pressed key code matches with the event
     *               when the selected rectangle is validated
     *               and user input D-Pad center button
     *               show the keypad to input value to that block
     * @return if unknown key is pressed then pass the event to the super class of onKeyDown
     * @return else do the validation according to the key code and return true
     */
    @Override
    public boolean onKeyDown(int keyCode , KeyEvent event) {
        Log.d(TAG, "onKeyDown : keycode =" + keyCode + ", events= " + event);
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                select(selX, selY - 1);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                select(selX, selY + 1);
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                select(selX - 1, selY);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                select(selX + 1, selY);
                break;
            case KeyEvent.KEYCODE_0:
            case KeyEvent.KEYCODE_SPACE:
                setSelectedTile(0);
                break;
            case KeyEvent.KEYCODE_1:
                setSelectedTile(1);
                break;
            case KeyEvent.KEYCODE_2:
                setSelectedTile(2);
                break;
            case KeyEvent.KEYCODE_3:
                setSelectedTile(3);
                break;
            case KeyEvent.KEYCODE_4:
                setSelectedTile(4);
                break;
            case KeyEvent.KEYCODE_5:
                setSelectedTile(5);
                break;
            case KeyEvent.KEYCODE_6:
                setSelectedTile(6);
                break;
            case KeyEvent.KEYCODE_7:
                setSelectedTile(7);
                break;
            case KeyEvent.KEYCODE_8:
                setSelectedTile(8);
                break;
            case KeyEvent.KEYCODE_9:
                setSelectedTile(9);
                break;
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                game.showKeypadOrError(selX, selY);
                break;
            default:
                return super.onKeyDown(keyCode, event);

        }
        return true;
    }

    /**
     *
     * @param x knows which horizontal block is selected
     * @param y knows which vertical block is selected
     *          that will land upto a single cell of the grid and thus
     *          we could validate the selection and form new rectangle
     */
    private void select(int x, int y){
       invalidate(selRect);
        selX=Math.min(Math.max(x, 0),8);
        selY=Math.min(Math.max(y, 0),8);
        getRect(selX,selY,selRect);
        invalidate(selRect);
    }

    /**
     *validate the selcted rectangle and shows a keypad to enter integer into the selected block
     * @param event captures a motion event (touch)
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction()!=MotionEvent.ACTION_DOWN)
            return super.onTouchEvent(event);
        /**
         * get the x,y co-ordinate where a motion event was generated (touch)
         * then divison of x by width and y by height will give the cell number of the grid ( 0 indexed)
         * call the select method to validate
         * then shows the keypad to input value to that block/cell
         */
        select((int) (event.getX()/width),(int)(event.getY()/height));

        game.showKeypadOrError(selX, selY);
        Log.d(TAG,"onTouchEvent :x "+selX+" ,y "+selY);

        return true;
    }


    public void setSelectedTile(int tile){
        if(game.setTileIfValid(selX,selY,tile)){
            invalidate();

            //may change hints
        }
        else {
            // Number is not valid for this tile
            Log.d(TAG,"setSelectedTile :invalid "+tile);
            // Vibrate for 500 milliseconds
            Vibrator v= (Vibrator) mcontext.getSystemService(VIBRATOR_SERVICE);
            v.vibrate(200);
            startAnimation(AnimationUtils.loadAnimation(game,R.anim.shake));
        }
    }




}

