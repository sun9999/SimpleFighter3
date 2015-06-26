package hk.edu.vtc.simplefighter3.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.TiledLayer;

import hk.edu.vtc.simplefighter3.GameActivity;
import hk.edu.vtc.simplefighter3.R;
import hk.edu.vtc.simplefighter3.TouchButton;

public class GameMenuView extends View implements Runnable {
    final int TICK_INTERVAL = 200;
    GameActivity activity;
    TouchButton startButton, quitButton, rankButton, instructionButton, settingButton, aboutButton;
    boolean running = false;
    LayerManager layerManager;

    int tileWidth = 58;
    int tileHeight = 42;
    int MAP_CYCLE	= 20;
    int mapCycleCount = 0;
    int viewWindowTop = 0;
    int viewWindowLeft = 0;

    public Thread thread = null;

    public GameMenuView(Context context) {
        super(context);
        this.activity = (GameActivity) context;

        TiledLayer background = createBackground();

        int yPosition = 100;
        startButton =  new TouchButton(getResources(), activity.getDensity(), R.drawable.start_button);
        startButton.setAlignCenter( yPosition, activity.getDisplayMetrics());
        yPosition += 50;

        instructionButton =  new TouchButton(getResources(), activity.getDensity(), R.drawable.instruction_button);
        instructionButton.setAlignCenter( yPosition, activity.getDisplayMetrics());
        yPosition += 50;

        rankButton =  new TouchButton(getResources(), activity.getDensity(), R.drawable.rank_button);
        rankButton.setAlignCenter( yPosition, activity.getDisplayMetrics());
        yPosition += 50;

        quitButton =  new TouchButton(getResources(), activity.getDensity(), R.drawable.quit_button);
        quitButton.setAlignCenter(yPosition, activity.getDisplayMetrics());

        layerManager = new LayerManager();
        layerManager.append(background);
        layerManager.setViewWindow(viewWindowLeft, viewWindowTop, activity.getWidth(), activity.getHeight());

        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public TiledLayer createBackground() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.bg);

        int mapWidth =(int) metrics.widthPixels / img.getWidth() + 2;
        int mapHeight=(int) metrics.heightPixels / img.getHeight() + 2;

        Log.v("GameMenuView", String.valueOf(mapWidth));
        Log.v("GameMenuView", String.valueOf(mapHeight));
        Log.v("GameMenuView", String.valueOf(activity.getDensity()));

        TiledLayer background = new TiledLayer( mapWidth,  mapHeight, img,
                (int)(tileWidth* activity.getDensity()),
                (int)(tileHeight* activity.getDensity()));
        for (int i=0; i< mapWidth*mapHeight; i++) {
            int column = i % mapWidth;
            int row = (i / mapWidth );
            background.setCell(column, row, 1);
        }

        return background;
    }

    @Override
    public void onDraw(Canvas c) {
        Paint paint = new Paint();
        c.drawColor(0xffffffff);
        layerManager.paint(c, 0, 0);

        Bitmap gameLogo = BitmapFactory.decodeResource(getResources(), R.drawable.game_logo);
        c.drawBitmap(gameLogo, (c.getWidth()-gameLogo.getWidth())/2, 5, paint);
        startButton.draw(paint, c);
        instructionButton.draw(paint, c);
        rankButton.draw(paint, c);
        quitButton.draw(paint, c);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent e ) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x		= (int)event.getX();
        int y		= (int)event.getY();

        switch(action) {
            case MotionEvent.ACTION_DOWN: case MotionEvent.ACTION_MOVE:
                if (startButton.clicked(x,y)) {
                    activity.startGame();
                } else if (instructionButton.clicked(x,y)) {
                    activity.showInstruction();
                } else if (quitButton.clicked(x,y)) {
                    activity.finish();
                } else if (rankButton.clicked(x, y)) {
                    activity.showRankView();
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    public void updateBackground() {
        mapCycleCount ++;
        if (mapCycleCount == MAP_CYCLE) {
            mapCycleCount = 0;
            viewWindowTop = 0; viewWindowLeft=0;
        } else {
            viewWindowLeft	= Math.round(mapCycleCount * tileWidth / MAP_CYCLE) ;
            viewWindowTop 	= Math.round(mapCycleCount * tileHeight / MAP_CYCLE);
        }
        layerManager.setViewWindow(viewWindowLeft, viewWindowTop, activity.getWidth(), activity.getHeight());

    }

    public void run() {
        while(running) {
            updateBackground();
            try {
                postInvalidate();
                Thread.sleep(TICK_INTERVAL);
            } catch(Exception e) {
                Log.e("MyActivity", e.getMessage());
            }
        }
    }
}
