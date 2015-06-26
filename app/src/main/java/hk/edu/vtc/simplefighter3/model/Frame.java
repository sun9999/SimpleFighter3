package hk.edu.vtc.simplefighter3.model;

public class Frame {
    private String filename;
    private int x;
    private int y;
    private int weight;
    private int height;
    private boolean isRotated;
    private double pivotX;
    private double pivotY;

    @Override
    public String toString() {
        return "Frame{" +
                "filename='" + filename + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", weight=" + weight +
                ", height=" + height +
                ", isRotated=" + isRotated +
                ", pivotX=" + pivotX +
                ", pivotY=" + pivotY +
                "}\n";
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Frame() {
        super();

    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isRotated() {
        return isRotated;
    }

    public void setIsRotated(boolean isRotated) {
        this.isRotated = isRotated;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public double getPivotX() {
        return pivotX;
    }

    public void setPivotX(double pivotX) {
        this.pivotX = pivotX;
    }

    public double getPivotY() {
        return pivotY;
    }

    public void setPivotY(double pivotY) {
        this.pivotY = pivotY;
    }
}
