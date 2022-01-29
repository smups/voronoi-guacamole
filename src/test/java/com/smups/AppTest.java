package com.smups;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import java.io.File;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.smups.drawings.Metric;
import com.smups.drawings.RangedVoronoiDrawing;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    //System temp dir
    private static String tmp_dir = System.getProperty("java.io.tmpdir");

    /**
     * This test creates a canvas of a random size and then fills the canvas
     * with a voronoi drawing drawn from at most 24 random points. 
     * 
     * The resulting image is saved in the OS's tmp folder.
     * 
     * @throws Exception
     */
    @Test
    public void fill_test_buffer() throws Exception{

        // (1) Create a randomly sized canvas
        Random rnd = new Random();
        int smallest = 1024;//1024;
        int biggest = 4*smallest;

        Canvas cv = Canvas.new_empty_canvas(
            smallest + rnd.nextInt(biggest),
            smallest + rnd.nextInt(biggest)
        );

        // (2) Generate points to draw in the canvas
        List<Point> vecs = new ArrayList<Point>();

        int n_points = rnd.nextInt(124); //at most 124 points
        for (int j = 0; j <= n_points; ++j) {
            //Make n_points within the canvas bounds
            Point p = new Point(
                cv.rows * rnd.nextDouble(), //x
                cv.cols * rnd.nextDouble(), //y
                (byte) (rnd.nextInt(255) - 127) //colour
            );
            vecs.add(p);
        }

        // (3) Drawing requires defining a rangefunction. We use a simple square
        Metric metric = new Metric() {
            @Override
            public double distance(Point p1, Point p2) {
                double r2 = (p1.x - p2.x)*(p1.x - p2.x) + (p1.y - p2.y)*(p1.y - p2.y);
                return 1/r2;
            }
        };

        // (4) Draw on the canvas
        Canvas result = new RangedVoronoiDrawing(metric, vecs, cv, 0.00001).draw();

        /*
        for (byte[] r : result.get_canvas_data()) {
            System.out.println(Arrays.toString(r));
        }
        */

        // (5) Turn the canvas into an actual image
        BufferedImage img = new BufferedImage(
            result.rows,
            result.cols,
            BufferedImage.TYPE_INT_RGB
        );

        //Map the byte colours into random actual colours
        HashMap<Byte, Integer> color_hashmap = new HashMap<Byte, Integer>();

        color_hashmap.put((byte) 0, (256 << 16) | (256 << 8) | 256); //black background

        for (byte colour : result.get_colours()){
            int r = (int)(Math.random()*256);
            int g = (int)(Math.random()*256);
            int b = (int)(Math.random()*256);
            int p = (r << 16) | (g << 8) | b;

            if (!color_hashmap.containsKey(colour)) color_hashmap.put(colour, p);
        }

        //Fill the image
        for (int y = 0; y < result.cols; y++) {
            for (int x = 0; x < result.rows; x++) {
                img.setRGB(x, y, color_hashmap.get(
                    Byte.valueOf(result.get_canvas_data()[x][y]))
                );
            }
        }

        //aaand write the image
        try {
            File f = new File(tmp_dir + "/image_test.png");
            System.out.printf("Output file at: %s\n", f.getPath());
            if (!f.exists()) f.createNewFile();
            ImageIO.write(img, "png", f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}