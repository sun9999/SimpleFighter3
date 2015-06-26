package hk.edu.vtc.simplefighter3;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;

public class TouchButton {
	Bitmap img;
	private int x;
	private int y;
	
	int buttonState ;
	final int UNCLICKED = 0;
	final int CLICKED = 1;
	
	final int CLICK_DURATION =3;
	int clickCountDown =0;
	
	float density;
	
	final int ALIGN_RIGHT = 0;
	final int ALIGN_LEFT = 0;
	final int ALIGN_CENTER = 0;
	final int ALIGN_TOP = 0;
	final int ALIGN_BOTTOM = 0;
	
	public TouchButton(Resources res, float density, int drawable, int x, int y) {
		this.img = BitmapFactory.decodeResource(res, drawable);
		if (img ==null) {
			Log.v("MyActivity", "NULL image " + drawable);
		}
		this.density =density;
		this.x = (int)(x * density);
		this.y = (int)(y * density);	
	}

	public TouchButton(Resources res, float density, int drawable, int x, int y, int align) {
		this.img = BitmapFactory.decodeResource(res, drawable);
		if (img ==null) {
			Log.v("MyActivity", "NULL image " + drawable);
		}
		this.density =density;
		this.x = (int)(x * density);
		this.y = (int)(y * density);	
	}	
	public TouchButton(Resources res, float density, int drawable) {
		this.img = BitmapFactory.decodeResource(res, drawable);
		if (img ==null) {
			Log.v("MyActivity", "NULL image " + drawable);
		}
		this.density =density;
 	
	}
	
	public TouchButton(Bitmap img, int x, int y) {
		this.img = img;
		this.x = x ;
		this.y =y; 
	}
	
	public void draw(Paint p, Canvas c) {
		if ( buttonState == CLICKED) {
			p.setAlpha( p.getAlpha() + 60);
			c.drawBitmap(img, x, y, p);
			p.setAlpha( p.getAlpha() - 60);
			clickCountDown ++;
			if (clickCountDown ==CLICK_DURATION) {
				buttonState = UNCLICKED;
			}
		} else if (buttonState == UNCLICKED){
			//c.drawText("HELOO",2, 33, p);
			if (img ==null) {
				//Log.v("MyActivity", "Img is null");
			} else {
				c.drawBitmap(img, x, y, p);
			}
		}
	}
	
	public void setAlignCenter(int y, DisplayMetrics dm) {
		this.x =  (dm.widthPixels - img.getWidth())/2;	
		this.y =  (int)(y * dm.density); 
	}
	
	public void setPositionFromLeft(int x, int y) {
		this.x = (int)(x*density);
		this.y = (int)(y*density);
	}
	public void setPositionFromRight(int x, int y, DisplayMetrics dm) {
		this.x = dm.widthPixels - (int)(x*density);
		this.y = dm.heightPixels -(int)(y*density);		
	}	
	public void setX(int x) {
		this.x =x;
	}
	public void setY(int y) {
		this.y =y;
	}	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getWidth() {
		return img.getWidth();
	}
	public int getHeight() {
		return img.getHeight();
	}
    public boolean clicked(int clickX, int clickY) 
    {
    	if ( (clickX > x ) && (clickX <= (x + img.getWidth()) ) ) {
            if ( (clickY > y ) && (clickY <= (y + img.getHeight()) ) ) {
            	buttonState = CLICKED;
            	clickCountDown = 0;            	
            	return true;            	
            }                
        } 
        return false;
    }
}
