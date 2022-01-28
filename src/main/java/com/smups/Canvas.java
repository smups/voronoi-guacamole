package com.smups;

import java.util.ArrayList;
import java.util.List;

import com.smups.exceptions.CanvasSizeMisMatchException;
import com.smups.exceptions.PointOutsideCanvasException;

public class Canvas {
    
    public final int rows;
    public final int cols;

    //Contains coloured pixels
    private byte[][] canvas_data;
    private List<Byte> colours = new ArrayList<Byte>();

    public Canvas(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public static Canvas new_empty_canvas(int rows, int cols) {
        Canvas cv = new Canvas(rows, cols);
        try {cv.set_canvas_data(new byte[rows][cols]);}
        catch (Exception e) {} //This can litterally never throw an error
        cv.colours.add((byte) 0); //Add nothing as a byte
        return cv;
    }

    public byte[][] get_canvas_data() { return this.canvas_data; }
    public List<Byte> get_colours() { return this.colours; }

    public void set_canvas_data(byte[][] canvas_data) throws CanvasSizeMisMatchException {
        if (this.rows != canvas_data.length ||
            this.cols != canvas_data[0].length) {
            //Canvasses were not the same size!
            throw new CanvasSizeMisMatchException(this,
                canvas_data.length,
                canvas_data[0].length
            );
        }
        this.canvas_data = canvas_data;
    }

    public void set(Point p) throws PointOutsideCanvasException {
        if (p.x < 0 || p.x >= rows) throw new PointOutsideCanvasException();
        if (p.y < 0 || p.y >= cols) throw new PointOutsideCanvasException();
        //Ok we in range -> set colour on canvas and add it to the known colour list
        this.canvas_data[(int) p.x][(int) p.y] = p.colour;
        if (!this.colours.contains(p.colour)) this.colours.add(p.colour);
    }

    public byte[] get_canvas_data_1d() {
        byte[] res = new byte[this.cols*this.rows];

        for (int i = 0; i < this.rows; ++i) {
            for (int j = 0; j < this.cols; ++j) {
                int k = i*this.cols + j;
                res[k] = this.canvas_data[i][j];
            }
        }

        return res;
    }
}