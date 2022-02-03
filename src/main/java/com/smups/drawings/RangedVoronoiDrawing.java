package com.smups.drawings;

import java.util.List;

import com.smups.Canvas;
import com.smups.Point;

public class RangedVoronoiDrawing extends Drawing{

    //Metric that will be used to calculate the ranged voronoi drawing
    private final Metric g; //refrence to general relativity?
    
    //Points from which ranged voronoi diagram should be drawn
    private final List<Point> vecs;

    //Score threshold below which a pixel should be assigned to belong to no-one
    private final double threshold;

    public RangedVoronoiDrawing(Metric g, List<Point> vecs,
        Canvas canvas, double threshold){
        super(canvas);
        this.g = g;
        this.vecs = vecs;
        this.threshold = threshold;
    }

    @Override
    public Canvas draw() throws Exception {
        for (int x = 0; x < cv.rows; ++x) {
            for (int z = 0; z < cv.cols; ++z) {
                //Current position
                Point pos = new Point(x, z, (byte) 0);

                //Initial value of the highscore determines the range
                double highscore = this.threshold;
                double score = 0.0;

                for (Point p: this.vecs) {
                    //If a point has a higher score than the highscore,
                    //the point now owns this pixel
                    score = g.distance(pos, p);
                    if (score > highscore) {
                        pos = pos.change_colour(p.colour);
                        cv.set(pos);
                        highscore = score;
                        /*System.out.printf("Added px(%d,%d) to point %s",
                            x, z, p.toString()
                        );*/
                    }
                }
            }
        }
        return this.cv;
    }

}