package com.ndominkiewicz.dfa.model.geometry;

public class NodeBounds {
    private final double size;
    private double layoutX;
    private double layoutY;

    public NodeBounds(double size, double x, double y) {
        this.size = size;
        this.layoutX = x;
        this.layoutY = y;
    }

    public void setX(double x) {
        this.layoutX = x;
    }

    public void setY(double y) {
        this.layoutY = y;
    }

    public double getStartX() {
        return layoutX;
    }

    public double getEndX() {
        return layoutX + size;
    }

    public double getStartY() {
        return layoutY;
    }

    public double getEndY() {
        return layoutY + size;
    }

    public double getCenterX() {
        return layoutX + (size / 2);
    }

    public double getCenterY() {
        return layoutY + (size / 2);
    }

    public double getSize() {
        return size;
    }
}
