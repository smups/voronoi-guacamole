package com.smups;

public class Point extends Tuple {

    public final byte colour;

    public Point(double x, double y, byte colour) {
        super(x, y);
        this.colour = colour;
    }
    public Point(Tuple t, byte colour) {
        super(t);
        this.colour = colour;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point)) return false;
        return ((Tuple) this).equals((Tuple) o) && ((Point) o).colour == this.colour;
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
        return String.format("P{%f, %f} col:#%x", this.x, this.y, this.colour);
    }
}