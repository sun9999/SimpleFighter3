package hk.edu.vtc.simplefighter3.model;


import android.graphics.Bitmap;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hk.edu.vtc.simplefighter3.GameActivity;

public class Character extends SpriteSheet implements Runnable {

    public GameActivity gameActivity;

    public int startPositionX;

    public int getStartPositionX() {
        return startPositionX;
    }

    public void setStartPositionX(int startPositionX) {
        this.startPositionX = startPositionX;
    }

    public GameActivity getGameActivity() {
        return gameActivity;
    }

    public void setGameActivity(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }



    public boolean running = false;
    short TICK_INTERVAL = 80; //50ms

    private String charPrefix;
    private String charPostfix;

    private List<Action> animationPipeline = new ArrayList<>();

    public void addAnimationPipeLine(String actionName, long animationTime) {
        this.getAnimationPipeline().add(new Action(actionName, animationTime));
    }

    public class Action {
        private String actionName;
        private long animationTime; //in ms, if smaller or equal to zero, it means animate forever

        public Action(String actionName, long animationTime) {
            this.actionName = actionName;
            this.animationTime = animationTime;
        }

        public String getActionName() {
            return actionName;
        }

        public void setActionName(String actionName) {
            this.actionName = actionName;
        }

        public long getAnimationTime() {
            return animationTime;
        }

        public void setAnimationTime(long animationTime) {
            this.animationTime = animationTime;
        }
    }

    public List<Action> getAnimationPipeline() {
        return animationPipeline;
    }

    public void setAnimationPipeline(List<Action> animationPipeline) {
        this.animationPipeline = animationPipeline;
    }

    private Map<String, FrameRange> animateFrames = new HashMap<>();

    public void addAnimateFrame(String actionName, int startIndex, int endIndex) {
        getAnimateFrames().put(actionName, new FrameRange(startIndex, endIndex));
    }

    public Map<String, FrameRange> getAnimateFrames() {
        return animateFrames;
    }

    public void setAnimateFrames(Map<String, FrameRange> animateFrames) {
        this.animateFrames = animateFrames;
    }

    public String getCharPostfix() {
        return charPostfix;
    }

    public void setCharPostfix(String charPostfix) {
        this.charPostfix = charPostfix;
    }

    public String getCharPrefix() {
        return charPrefix;
    }

    public void setCharPrefix(String charPrefix) {
        this.charPrefix = charPrefix;
    }

    public class FrameRange {
        private int startFrameIndex;
        private int endFrameIndex;

        public FrameRange(int startFrameIndex, int endFrameIndex) {
            this.endFrameIndex = endFrameIndex;
            this.startFrameIndex = startFrameIndex;
        }

        public int getEndFrameIndex() {
            return endFrameIndex;
        }

        public void setEndFrameIndex(int endFrameIndex) {
            this.endFrameIndex = endFrameIndex;
        }

        public int getStartFrameIndex() {
            return startFrameIndex;
        }

        public void setStartFrameIndex(int startFrameIndex) {
            this.startFrameIndex = startFrameIndex;
        }
    }

    public void processFrames(String animateAction) {
        if (animateAction==null) {return;}

        FrameRange frameRange = getAnimateFrames().get(animateAction);
        if (frameRange==null) {return;}

//        this.clearAllFrameSequence();

        for (int i=frameRange.getStartFrameIndex(); i<=frameRange.getEndFrameIndex(); i++) {
            addFrameSequence(getCharPrefix() + i + getCharPostfix());
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public Character(Bitmap image) {
        super(image);
    }

    public Character(Bitmap image, InputStream in) {
        super(image, in);
    }

    public Character(Bitmap image, InputStream in, GameActivity gameActivity) {
        super(image, in);
        this.gameActivity = gameActivity;
    }

    public void startThread() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        for (Action action: this.getAnimationPipeline()) {
            if (action.getActionName()==null) {continue;}
            processFrames(action.getActionName());
        }

        for (Action action: this.getAnimationPipeline()) {
            if (action.getActionName()==null) {continue;}

            long startTime = System.currentTimeMillis();
            do {
                this.nextFrame();
                setPosition(startPositionX, getGameActivity().getHeight()-getCurrentFrame().getHeight());
                try {
                    Thread.sleep(TICK_INTERVAL);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } while (action.getAnimationTime()<=0 || System.currentTimeMillis()<startTime+action.getAnimationTime());
        }

//        while (running) {
//            this.nextFrame();
//            try {
//                Thread.sleep(TICK_INTERVAL);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
    }
}
