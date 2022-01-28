package com.smups;

public class ImmutableCanvas {
    
    //Immutable fields
    public final int rows;
    public final int cols;
    private final byte[][] canvas_data;

    public ImmutableCanvas(Canvas canvas){
        this.rows = canvas.rows;
        this.cols = canvas.cols;

        //Make a blank canvas
        this.canvas_data = canvas.get_canvas_data();
    }

    public byte[][] get_canvas_data() {
        return this.canvas_data;
    }
}