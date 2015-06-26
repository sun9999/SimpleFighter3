package hk.edu.vtc.simplefighter3.view;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.TiledLayer;

import hk.edu.vtc.simplefighter3.GameActivity;
import hk.edu.vtc.simplefighter3.R;

public class RankView extends View {
	
	GameActivity activity;

	Bitmap gameLogo;
	LayerManager lm;
	TiledLayer background;
	Paint p;
	
	int xColumn0 = 15;
	int xColumn1 = 50;
	int xColumn2 = 300;
	int rowHeight =  25;
	
	public RankView(Context context) {
		super(context);
		this.activity = (GameActivity)context;
		gameLogo = BitmapFactory.decodeResource(getResources(), R.drawable.game_logo);
		p = new Paint();
 
		xColumn1 	= (int)(xColumn1 * activity.getDensity());
		xColumn2 	= (int)(xColumn2 * activity.getDensity());
		rowHeight 	= (int)(rowHeight * activity.getDensity());
	}

	public void onDraw(Canvas c) {
		c.drawColor(0xffffffff);
		Paint p2 = new Paint();
		p2.setAlpha(50);
		c.drawBitmap(gameLogo, 0, -0, p2);
//		p.setTextSize(50);
		p.setTextSize(50 * activity.getDensity());
//		p.setColor(0xffff0000);
		p.setAntiAlias(true);
//		c.drawText("Ranking", activity.getWidth()/2, 200*activity.getDensity(), p);
		c.drawText("Ranking", 10, 150, p);
		
		drawRank(c); 	
	}
	
	public void drawRank(Canvas c) {
		float yPosition = 230 * activity.getDensity();
		SQLiteDatabase db = activity.getReadableDatabase();
		int rankCount =1;
		
		p.setColor(0xff000000);
//		p.setTextSize(30);		
		
		Cursor cursor = db.query("game_rank", null, null, null, null, null, "score desc", "10");
		int nameIndex = cursor.getColumnIndex("name");	
		int score = cursor.getColumnIndex("score");	
		for(cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()){
			p.setTextAlign(Paint.Align.LEFT);			
			c.drawText( String.valueOf(rankCount),
					(float)xColumn0, (float)yPosition,  p);
			c.drawText( cursor.getString(nameIndex),
					(float)xColumn1, (float)yPosition,  p);
			p.setTextAlign(Paint.Align.RIGHT);
			c.drawText( String.valueOf(cursor.getInt(score)),
					(float)xColumn2, (float)yPosition, p);
			
			yPosition += rowHeight;
			rankCount ++;
		}
		cursor.close();		
		db.close();			
	}
	
	public boolean onTouchEvent(MotionEvent e) {
		activity.showGameMenu();
		return true;
	}
}
