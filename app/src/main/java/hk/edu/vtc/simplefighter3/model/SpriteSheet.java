package hk.edu.vtc.simplefighter3.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.microedition.lcdui.game.Sprite;

import hk.edu.vtc.simplefighter3.util.SpriteSheetReader;


public class SpriteSheet extends Sprite {

    public static final int SACLE_FACTOR=7; //because the images are too small, need to scale them bigger

    private Map<String, Frame> frames = null;
    private List<Bitmap> frameSequence = null;
    private int currentFrameIndex = -1;
    private boolean isFlip = false;

    public boolean isFlip() {
        return isFlip;
    }

    public void setIsFlip(boolean isFlip) {
        this.isFlip = isFlip;
    }

    public int getCurrentFrameIndex() {
        return currentFrameIndex;
    }

    public void setCurrentFrameIndex(int currentFrameIndex) {
        this.currentFrameIndex = currentFrameIndex;
    }

    public void clearAllFrameSequence() {
        this.getFrameSequence().clear();
        this.currentFrameIndex =-1;
    }

    public Bitmap getCurrentFrame() {
        if (currentFrameIndex<0) {return null;}
        if (currentFrameIndex>getFrameSequence().size()-1) {return null;}
        return getFrameSequence().get(currentFrameIndex);
    }

    @Override
    public void prevFrame() {
        if (currentFrameIndex <= 0) {
            currentFrameIndex = getFrameSequence().size() - 1;
        } else {
            currentFrameIndex--;
        }
    }

    @Override
    public void nextFrame() {
        if (getFrameSequence().size()==0) {return;}
        currentFrameIndex = (currentFrameIndex + 1) % getFrameSequence().size();
    }

    public SpriteSheet(Bitmap image) {
        super(image);
    }

    public SpriteSheet(Bitmap image, InputStream in) {
        super(image);
        try {
            frames = SpriteSheetReader.getFrames(in);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Map<String, Frame> getFrames() {
        synchronized (this) {
            if (frames==null) {
                frames = new HashMap<>();
            }
        }
        return frames;
    }

    public void setFrameSequence(List<Bitmap> frameSequence) {
        this.frameSequence = frameSequence;
    }

    public List<Bitmap> getFrameSequence() {
        synchronized (this) {
            if (frameSequence==null) {
                frameSequence = new ArrayList<>();
            }
        }
        return frameSequence;
    }

    public void addFrameSequence(String frameSequence) {
        Frame frame = this.getFrames().get(frameSequence);
        if (frame!=null) {
            int height = frame.getHeight();
            int weight = frame.getWeight();

            Matrix matrix = new Matrix();
            matrix.reset();
            if (frame.isRotated()) {
                height = frame.getWeight();
                weight = frame.getHeight();
                matrix.postRotate(270);
                matrix.preScale(SACLE_FACTOR, isFlip? -SACLE_FACTOR : SACLE_FACTOR); //use 1 or -1 if scaling is not required
            } else {
                matrix.preScale(isFlip? -SACLE_FACTOR : SACLE_FACTOR, SACLE_FACTOR); //use 1 or -1 if scaling is not required
            }

            getFrameSequence().add(Bitmap.createBitmap(getBitmap(), frame.getX(), frame.getY(), weight, height, matrix, false));
            matrix=null;
        }
    }

    @Override
    public int getFrameSequenceLength() {
        return getFrameSequence().size();
    }

    @Override
    public void paint(Canvas canvas) {
        if (canvas == null) {
            throw new NullPointerException();
        }

        if (isVisible()) {
            Bitmap frame = getCurrentFrame();
            if (frame!=null) {
                Rect rect = new Rect();
                rect.left = 0;
                rect.right = frame.getWidth();
                rect.top = 0;
                rect.bottom = frame.getHeight();

                Rect rect_dst = new Rect();
                rect_dst.left = getX();
                rect_dst.right = getX() + frame.getWidth();
                rect_dst.top = getY();
                rect_dst.bottom = getY() + frame.getHeight();

                canvas.drawBitmap(frame, rect, rect_dst, null);
                rect=null;
                rect_dst = null;
            }
            frame=null;
        }
    }

//    private long duration=-1;
//    private long startTimeMillis;
//    private long endTimeMillis;

    @Override
    public String toString() {
        return "SpriteSheet{" +
                "currentFrameIndex=" + currentFrameIndex +
                ", frames=" + frames +
                ", frameSequence=" + frameSequence +
                '}';
    }

    //    public void animate() {
//        Thread thread = new Thread(this);
//        thread.start();
//    }
//
//    @Override
//    public void run() {
//        long startTime = System.currentTimeMillis();
//
//        do {
//            nextFrame();
//        } while (duration==-1 || System.currentTimeMillis()<startTime+duration);
//    }
}
