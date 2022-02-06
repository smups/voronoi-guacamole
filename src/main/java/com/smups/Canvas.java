package com.smups;

import java.io.File;
import java.io.Serializable;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.smups.exceptions.CanvasSizeMisMatchException;
import com.smups.exceptions.PointOutsideCanvasException;

public class Canvas implements Serializable{
    static final long serialVersionUID = 1L;

    public static final byte BLANK = 0;
    
    public final int rows;
    public final int cols;

    //Contains coloured pixels
    private byte[][] canvas_data;
    private List<Byte> colours = new ArrayList<Byte>();

    public Canvas(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public Canvas(ImmutableCanvas cv) {
        this.rows = cv.rows;
        this.cols = cv.cols;
        this.canvas_data = cv.copy_canvas_data();
        this.colours = cv.copy_colours();
    }

    public static Canvas new_empty_canvas(int rows, int cols) {
        Canvas cv = new Canvas(rows, cols);
        try {cv.set_canvas_data(new byte[rows][cols]);}
        catch (Exception e) {} //This can litterally never throw an error
        cv.colours.add((byte) BLANK); //Add nothing as a byte
        return cv;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Canvas)) return false;
        Canvas that = (Canvas) o;
        return (this.rows == that.rows) && (this.cols == that.cols) &&
            this.colours.equals(that.colours) &&
            Arrays.deepEquals(this.get_canvas_data(), that.get_canvas_data());
    }

    @Override
    public String toString() {
        return String.format("cv:(%dx%d) with %d colours",
            this.rows,
            this.cols,
            this.colours.size()
        );
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
    
    public void set_colours(List<Byte> cols) {this.colours = cols;}

    public void set(Point p) throws PointOutsideCanvasException {
        if (p.x < 0 || p.x >= rows) throw new PointOutsideCanvasException();
        if (p.y < 0 || p.y >= cols) throw new PointOutsideCanvasException();
        //Ok we in range -> set colour on canvas and add it to the known colour list
        this.canvas_data[(int) Math.round(p.x)][(int) Math.round(p.y)] = p.colour;
        if (!this.colours.contains(p.colour)) this.colours.add(p.colour);
    }

    public byte get(Tuple p) throws PointOutsideCanvasException {
        if (p.x < 0 || p.x >= rows) throw new PointOutsideCanvasException();
        if (p.y < 0 || p.y >= cols) throw new PointOutsideCanvasException();
        //Ok we in range 
        return this.canvas_data[(int) Math.round(p.x)][(int) Math.round(p.y)];
    }

    public void save_as_png(File f) throws Exception{

        // (1) Make a new image
        BufferedImage img = new BufferedImage(
            this.rows,
            this.cols,
            BufferedImage.TYPE_INT_RGB
        );

        // (2) Map the byte colours into random actual colours
        HashMap<Byte, Integer> color_hashmap = new HashMap<Byte, Integer>();
        color_hashmap.put((byte) BLANK, (256 << 16) | (256 << 8) | 256); //black background

        for (byte colour : this.colours){
            int r = (int)(Math.random()*256);
            int g = (int)(Math.random()*256);
            int b = (int)(Math.random()*256);
            int p = (r << 16) | (g << 8) | b;

            if (!color_hashmap.containsKey(colour)) color_hashmap.put(colour, p);
        }

        // (3) Fill the image
        for (int y = 0; y < this.cols; y++) {
            for (int x = 0; x < this.rows; x++) {
                img.setRGB(x, y, color_hashmap.get(
                    Byte.valueOf(this.canvas_data[x][y]))
                );
            }
        }

        // (4) Write the image to the file
        System.out.printf("Output file at: %s\n", f.getPath());
        ImageIO.write(img, "png", f);
    }

}