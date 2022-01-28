package com.smups.drawings;

import java.util.List;

import com.smups.Canvas;
import com.smups.Point;

public class RangedVoronoiDrawing {

    //Rangefunction that will be used to calculate the ranged voronoi drawing
    private final RangeFunction rf;
    
    //Points from which ranged voronoi diagram should be drawn
    private final List<Point> vecs;

    //Score threshold below which a pixel should be assigned to belong to no-one
    private final double threshold;

    //Canvas on which to draw
    private Canvas cv;

    public RangedVoronoiDrawing(RangeFunction rf, List<Point> vecs,
        Canvas canvas, double threshold){
        this.rf = rf;
        this.vecs = vecs;
        this.cv = canvas;
        this.threshold = threshold;
    }

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
                    score = rf.distance(pos, p);
                    if (score > highscore) {
                        cv.set(p);
                        highscore = score;
                    }
                }
            }
        }
        return this.cv;
    }

}