package com.smups;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.File;

import com.smups.drawings.Metric;
import com.smups.drawings.RangedVoronoiDrawing;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

@TestMethodOrder(OrderAnnotation.class)
public class AppTest 
{
    //System temp dir
    private static String tmp_dir = System.getProperty("java.io.tmpdir");

    //Shared drawing
    private ImmutableCanvas drawing;

    /**
     * This test creates a canvas of a random size and then fills the canvas
     * with a voronoi drawing drawn from at most 24 random points. The resulting
     * canvas is saved a in a class field and also turned into a png image.
     * 
     * This png image is saved in the OS's tmp folder.
     * 
     * @throws Exception
     */
    @Test
    @Order(1)
    public void draw_voronoi_diagram() throws Exception{
        //Make a random drawing (and save it for test #2)

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

        // (4) Draw on the canvas and save an immutable copy
        Canvas result = new RangedVoronoiDrawing(metric, vecs, cv, 0.00001).draw();
        this.drawing = new ImmutableCanvas(result);

        // (5) save the image
        try {
            File f = new File(tmp_dir + "/image_test.png");
            if (!f.exists()) f.createNewFile();
            result.save_as_png(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(2)
    public void draw_borders() throws Exception {
        // (1) Make a copy of the result from test #1 (may thrown nullptr ex)
        Canvas cv = new Canvas(this.drawing);

    }
}