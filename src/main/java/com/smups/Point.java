package com.smups;

public class Point {
    public final double x;
    public final double y;
    public final byte colour;

    public Point(double x, double y, byte colour) {
        this.x = x;
        this.y = y;
        this.colour = colour;
    }

    public Point change_colour(byte new_colour){
        return new Point(
            this.x,
            this.y,
            new_colour
        );
    }

    @Override
    public String toString() {
        return String.format("P{%f, %f} col:#%x%n", this.x, this.y, this.colour);
    }
}