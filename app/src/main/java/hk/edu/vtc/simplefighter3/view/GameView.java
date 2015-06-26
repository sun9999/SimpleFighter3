package hk.edu.vtc.simplefighter3.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;

import hk.edu.vtc.simplefighter3.GameActivity;
import hk.edu.vtc.simplefighter3.R;
import hk.edu.vtc.simplefighter3.model.Character;

public class GameView extends View implements Runnable {

	public enum GameState {
		WIN, PAUSE, GAME_OVER;
	}

	GameActivity activity;
	short TICK_INTERVAL = 10; //50ms
	public boolean running = false;
	public GameState gameState;
	public int score = 0;

	private LayerManager layerManager;
	private Character playerCharacter;
	private Character computerCharacter;
	private Sprite background;

	private Sprite tmp;

	Thread thread;

	public GameView(Context context) {
		super(context);
		this.activity = (GameActivity)context;

		layerManager = new LayerManager();

		background = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.background2));
		playerCharacter = new Character(BitmapFactory.decodeResource(getResources(), R.drawable.yuri), getResources().openRawResource(+R.drawable.yuri_map), activity);
		playerCharacter.setIsFlip(true);
		playerCharacter.setCharPrefix("Yuri Sakazaki_");
		playerCharacter.setCharPostfix(".png");
		playerCharacter.addAnimateFrame("idle", 97, 103);
		playerCharacter.addAnimateFrame("punch", 181, 184);
		playerCharacter.addAnimateFrame("run", 167, 176);

		playerCharacter.addAnimationPipeLine("idle", 200);
		playerCharacter.addAnimationPipeLine("punch", 1000);
		playerCharacter.addAnimationPipeLine("idle", 200);
		playerCharacter.addAnimationPipeLine("run", 0);
		playerCharacter.addAnimationPipeLine("idle", 0);

		playerCharacter.setStartPositionX(0);

		playerCharacter.setRunning(true);


//		computerCharacter = new Character(BitmapFactory.decodeResource(getResources(), R.drawable.ken), getResources().openRawResource(+R.drawable.ken_map), activity);
//		computerCharacter.setCharPrefix("Ken Masters_");
//		computerCharacter.setCharPostfix(".png");
//		computerCharacter.addAnimateFrame("idle", 65, 71);
//		computerCharacter.addAnimateFrame("punch", 427, 439);
//		computerCharacter.addAnimateFrame("superkick", 441, 458);

//		computerCharacter.addAnimationPipeLine("idle", 200);
//		computerCharacter.addAnimationPipeLine("punch", 1000);
//		computerCharacter.addAnimationPipeLine("idle", 200);
//		computerCharacter.addAnimationPipeLine("superkick", 0);
//		computerCharacter.addAnimationPipeLine("idle", 0);

//		computerCharacter.setStartPositionX(1000);
//
//		computerCharacter.setRunning(true);


		layerManager.append(playerCharacter);
//		layerManager.append(computerCharacter);
		layerManager.append(background);

		layerManager.setViewWindow(0, 0, activity.getWidth(), activity.getHeight());

		running = true;
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
//		playerCharacter.setGameView(this);

		playerCharacter.startThread();
//		computerCharacter.startThread();

		while (running) {
			postInvalidate();
			try {
				Thread.sleep(TICK_INTERVAL);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		layerManager.paint(canvas, 0, 0);
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		activity.showGameMenu();
		return true;
	}
}

