package com.smups;

import java.util.ArrayList;
import java.util.List;

public class ImmutableCanvas {
    
    //Immutable fields
    public final int rows;
    public final int cols;
    private final byte[][] canvas_data;
    private final List<Byte> colours;

    public ImmutableCanvas(Canvas canvas){
        this.rows = canvas.rows;
        this.cols = canvas.cols;
        this.canvas_data = canvas.get_canvas_data().clone();
        this.colours = new ArrayList<Byte>(canvas.get_colours());
    }

    public Canvas copy() {return new Canvas(this);}

    public byte[][] copy_canvas_data() {return this.canvas_data.clone();}

    public List<Byte> copy_colours() {
        return new ArrayList<Byte>(this.colours);
    }
}