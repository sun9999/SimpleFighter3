package hk.edu.vtc.simplefighter3;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import hk.edu.vtc.simplefighter3.view.GameMenuView;
import hk.edu.vtc.simplefighter3.view.GameView;
import hk.edu.vtc.simplefighter3.view.InstructionView;
import hk.edu.vtc.simplefighter3.view.RankView;

public class GameActivity extends Activity {
    DisplayMetrics dm;
    GameView game;
    GameMenuView menu;
    InstructionView inst;
    RankView rankview;
    Button saveButton;
    EditText nameText;

    SQLiteHelper helper;
    SQLiteDatabase db;
    InputMethodManager imm;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dm  = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        menu = new GameMenuView(this);
        setContentView(menu);

        helper = new SQLiteHelper(this, "db1", null, 1);

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public void onRestart() {
        super.onRestart();
        if (game!=null) {
            setContentView(game);
            game.running =true;
//            game.startThread();
        }
    }

    public void onPause() {
        super.onPause();
        if (game != null) {
//            game.stopThread();
        }
    }

    public void startGame() {
        if (game == null) {
            game = new GameView(this);
        } else {
            if (game.gameState == GameView.GameState.GAME_OVER) {
                game = new GameView(this);
            } else if (game.gameState == GameView.GameState.WIN) {
                game = new GameView(this);
            }
//            game.startThread(); //continue game
        }
        setContentView(game);
    }

    public void showSaveScore() {
        setContentView(R.layout.input_name);
        nameText = (EditText) findViewById(R.id.yourname);

        saveButton = (Button) findViewById(R.id.button1);
        saveButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (!nameText.getText().equals("")) {
                    imm.hideSoftInputFromWindow(nameText.getWindowToken(), 0);
                    saveGameRank( nameText.getText().toString(), game.score );
                }
                showGameMenu();
            }
        });

    }

    public void showRankView() {
        if (rankview ==null) {
            rankview =  new RankView(this);
        }
        setContentView(rankview);
    }

    public void showGameMenu() {
        setContentView(menu);
    }

    public void showInstruction() {
        if (inst==null) {
            inst =  new InstructionView(this);
        }
        setContentView(inst);
    }

    public DisplayMetrics getDisplayMetrics()
    {
        return dm;
    }

    public int getWidth() {
        return dm.widthPixels;
    }

    public int getHeight() {
        return dm.heightPixels;
    }

    public float getDensity() {
        return dm.density;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (game!=null) {
//            return game.onKeyDown(keyCode);
//        }
        return true;
    }


    public void saveGameRank(String name, int score) {

        db = helper.getWritableDatabase();
        // db.execSQL("insert into game_rank(name,score) values('Peter Kwok',100)");
        // or
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("score", score);
        db.insert("game_rank", "name", values);
        values.clear();
        db.close();
        Log.v("MyActivity", "Save Game Rank success!");

    }
    public SQLiteDatabase getWritableDatabase() {
        return helper.getWritableDatabase();
    }

    public SQLiteDatabase getReadableDatabase() {
        return helper.getReadableDatabase();
    }

    // READ file Method
    public String getDatafromFile(int thefile) {
        int lineNo = 1;
        String theLine = "";
        String txtString = "";
        BufferedReader br;
        Resources res = getResources();
        try  {
            InputStream is = res.openRawResource(thefile);
            br = new BufferedReader(new InputStreamReader(is));
            theLine = br.readLine();
            while (theLine!=null) {
                Log.v("MyActivity", String.valueOf(lineNo));
                Log.v("MyActivity", "TheLine=" + theLine);
                lineNo ++;
                txtString += theLine + "\n";
                theLine =  br.readLine();
            }
			/*@Read the Whole documents
				byte[] b = new byte[10000];
				int totalLength = is.read(b);
				txtString = new String(b, "UTF-8");
				System.out.println(totalLength);
			*/
        } catch (Exception e) {
            Log.v("MyActivity", "read file error!!" +  e.getMessage());
        }
        return txtString;
    }

}