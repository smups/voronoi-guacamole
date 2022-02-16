package com.smups;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.smups.drawings.BorderDrawing;
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
    private static ImmutableCanvas drawing;

    @Test
    @Order(1)
    /**
     * This test creates a canvas of a random size and then fills the canvas
     * with a voronoi drawing drawn from at most 24 random points. The resulting
     * canvas is saved a in a class field and also turned into a png image.
     * 
     * This png image is saved in the OS's tmp folder.
     * 
     * @throws Exception
     */    
    public void draw_voronoi_diagram() throws Exception{
        //Make a random drawing (and save it for test #2)

        // (1) Create a randomly sized canvas
        Random rnd = new Random();
        int smallest = 1024;
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
                (byte) j //colour
            );
            vecs.add(p);
        }

        // (3) Drawing requires defining a rangefunction. We use a simple square
        Metric metric = new Metric() {
            @Override
            public double distance(Tuple p1, Tuple p2) {
                double r2 = (p1.x - p2.x)*(p1.x - p2.x) + (p1.y - p2.y)*(p1.y - p2.y);
                return 1/r2;
            }
        };

        // (4) Draw on the canvas and save an immutable copy
        Canvas result = new RangedVoronoiDrawing(metric, vecs, cv, rnd.nextDouble()/(cv.rows*cv.cols)).draw();
        this.drawing = new ImmutableCanvas(result);

        // (5) save the image
        File f = new File(tmp_dir + "/voronoiG-1.png");
        if (!f.exists()) f.createNewFile();
        result.save_as_png(f);
    }

    @Test
    @Order(2)
    /**
     * This test writes a Canvas to a file and reads it back into memory. The
     * test will pass if original canvas equals the canvas that went through
     * a read-write cycle.
     * 
     * @throws Exception
     */
    public void io() throws Exception {
        // (1) Make a copy of the result from test #1 (may thrown nullptr ex)
        Canvas cv1 = new Canvas(this.drawing);

        // (2) Write the copy to some tmp file
        File f = new File(tmp_dir + "voronoiG-2.sr");
        FileOutputStream f_out = new FileOutputStream(f);
        ObjectOutputStream o_out = new ObjectOutputStream(f_out);
        o_out.writeObject(cv1);
        o_out.flush();
        o_out.close();
        f_out.close();

        // (3) Now read it again
        FileInputStream f_in = new FileInputStream(f);
        ObjectInputStream o_in = new ObjectInputStream(f_in);
        Canvas cv2 = (Canvas) o_in.readObject();
        o_in.close();
        f_in.close();

        // (4) Compare the two
        System.out.println(cv1.toString());
        System.out.println(cv2.toString());
        assert(cv1.equals(cv2));
    }

    @Test
    @Order(3)
    /**
     * This test blah blah 
     * 
     * @throws Exception
     */
    public void draw_borders() throws Exception {
        // (1) Make a copy of the result from test #1 (may thrown nullptr ex)
        Canvas source = new Canvas(this.drawing);
        Canvas target = Canvas.new_empty_canvas(source.rows, source.cols);
        target.set_colours(source.get_colours()); //same colours

        // (2) Loop through the all points in the canvas starting in the top right
        HashSet<Byte> known_cols = new HashSet<Byte>();
        known_cols.add(Canvas.BLANK);

        int colours_handled = 0;
        
        for (int x = 0; x < source.rows; x++) {
            for (int y = 0; y < source.cols; y++) {
                if (!known_cols.contains(source.get_canvas_data()[x][y])) {
                    byte colour = source.get_canvas_data()[x][y];
                    colours_handled++;

                    System.out.printf("Working on colour 0x%x (%d/%d)%n",
                        colour,
                        colours_handled,
                        source.get_colours().size() - 1
                    );

                    known_cols.add(colour);
                    new BorderDrawing().draw(source, target, new Point(x, y, colour));
                }
            }
        }

        // (3) Save the img
        File f = new File(tmp_dir + "/voronoiG-3.png");
        if (!f.exists()) f.createNewFile();
        target.save_as_png(f);
    }
}