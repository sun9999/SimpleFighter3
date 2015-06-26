package hk.edu.vtc.simplefighter3.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.Vector;

import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.TiledLayer;

import hk.edu.vtc.simplefighter3.GameActivity;
import hk.edu.vtc.simplefighter3.R;

public class InstructionView extends View {

	GameActivity activity;

	Bitmap gameLogo;
	LayerManager lm;
	TiledLayer background;
	Paint p;
	
	public InstructionView(Context context) {
		super(context);
		this.activity = (GameActivity)context;
		gameLogo = BitmapFactory.decodeResource(getResources(), R.drawable.game_logo);
		p = new Paint();
	}

	public void onDraw(Canvas c) {
		c.drawColor(0xffffffff);
		Paint p2 = new Paint();
		p2.setAlpha(50);
		c.drawBitmap(gameLogo, 0, -0, p2);
		p.setTextSize(50 * activity.getDensity());
		c.drawText("Introduction", 10, 150, p);
		p.setTextSize(20 * activity.getDensity());
//		FontMetrics fm = p.getFontMetrics();
//		Typeface tf = p.getTypeface();
//		int fontHeight = (int)Math.ceil(fm.descent-fm.top) +2;
		String message = activity.getDatafromFile(R.raw.inst);
		//String messages[] = message.split("\n");
		String messages[] = StringFormat( message, activity.getWidth()-20, 30);
		for (int i=0; i<messages.length; i++) {
			c.drawText(messages[i], 10, 300 + 30 *(i), p);			
		}
	}
	
	public String[] StringFormat(String text, int maxWidth, int fontSize) {
		String[] result = null ; 	
		Vector<String> tempR = new Vector<String>(); 	
		int lines = 0 ; 
		int len = text.length(); 
	
		int index0 = 0 ; 
		int index1 = 0 ; 
	
		boolean wrap; 
	
		while ( true ) {
			int widthes = 0 ; 
		
			wrap = false ; 
		
			for (index0 = index1; index1 < len; index1++) {
				if (text.charAt(index1) == '\n' ) {
					index1++; 
					wrap = true ;
					break ;
				} 
	
				widthes = fontSize + widthes; 
	
				if (widthes > maxWidth) {
					break ;
				} 
	
			} 
	
			lines++; 
	
			if (wrap) {
				tempR.addElement(text.substring(index0, index1 - 1 ));
			} else {
				tempR.addElement(text.substring(index0, index1));
			} 
	
			if (index1 >= len) {
				break ; 		
			}
		} 
	
		result = new String[lines]; 
		tempR.copyInto(result); 
	
		return result;
	} 	

	public boolean onTouchEvent(MotionEvent e) {
		activity.showGameMenu();
		return true;
	}
}
