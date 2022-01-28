package com.smups;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import java.io.File;
import java.io.FileOutputStream;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.smups.drawings.RangeFunction;
import com.smups.drawings.RangedVoronoiDrawing;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    //System temp dir
    private static String tmp_dir = System.getProperty("java.io.tmpdir");

    @Test
    public void fill_test_buffer() throws Exception{
        Random rnd = new Random();
        int smallest = 1024;
        int biggest = 8192;

        Canvas cv = Canvas.new_empty_canvas(
            smallest + rnd.nextInt(biggest),
            smallest + rnd.nextInt(biggest)
        );

        //Points to draw in the canvas
        List<Point> vecs = new ArrayList<Point>();

        int n_points = rnd.nextInt(24); //at most 24 points
        for (int j = 0; j <= n_points; ++j) {
            //Make n_points within the canvas bounds
            Point p = new Point(
                cv.rows * rnd.nextDouble(), //x
                cv.cols * rnd.nextDouble(), //y
                (byte) (rnd.nextInt(255) - 127) //colour
            );
            System.out.println(p.colour);
            vecs.add(p);
        }

        //Our drawing function
        RangeFunction rf = new RangeFunction() {
            @Override
            public double distance(Point p1, Point p2) {
                return (p1.x - p2.x)*(p1.x - p2.x) + (p1.y - p2.y)*(p1.y - p2.y);
            }
        };

        //Draw on the canvas
        Canvas result = new RangedVoronoiDrawing(rf, vecs, cv, 0.2).draw();

        //Turn the canvas into an actual image
        BufferedImage img = new BufferedImage(result.rows, result.cols, BufferedImage.TYPE_INT_ARGB);

        //Map the byte colours into random actual colours
        HashMap<Byte, Integer> color_hashmap = new HashMap<Byte, Integer>();
        int alpha = 265;
        for (byte colour : result.get_colours()){
            int r = (int)(Math.random()*256);
            int g = (int)(Math.random()*256);
            int b = (int)(Math.random()*256);
            int p = (alpha << 24) | (r << 16) | (g << 8) | b;

            color_hashmap.put(colour, p);
            System.out.printf("%d - %d\n", colour, p);
        }
        //Fill the image
        for (int y = 0; y < result.cols; y++) {
            for (int x = 0; x < result.rows; x++) {
                img.setRGB(x, y, color_hashmap.get(Byte.valueOf(result.get_canvas_data()[x][y])));
            }
        }

        //aaand write the image
        try {
            File f = new File(tmp_dir + "image_test.png");
            ImageIO.write(img, "png", f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}