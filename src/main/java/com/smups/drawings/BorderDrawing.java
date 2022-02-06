package com.smups.drawings;

import java.util.HashSet;

import com.smups.Canvas;
import com.smups.Point;
import com.smups.Tuple;
import com.smups.exceptions.PointOutsideCanvasException;

public class BorderDrawing {

    private HashSet<Tuple> boundary_set = new HashSet<Tuple>();

    public Canvas draw(Canvas source, Canvas target, Point start) throws Exception {
        final Tuple start_tpl = new Tuple(start);
        final byte target_col = start.colour;
        register_point(start, target);

        Tuple step = left_step(new Tuple(0, 1));
        Tuple next = start.plus(step);

        byte next_col = Canvas.BLANK;

        while(!start_tpl.equals(next)) {       
            
            try {next_col = source.get(next);}
            catch (PointOutsideCanvasException e) {
                next_col = Canvas.BLANK;
            } 

            //Check if the pixel is at the edge
            if (next_col != target_col || boundary_set.contains(next)) {
                next = next.min(step); //go back
                step = right_step(step); //go right
            } else {
                register_point(new Point(next, target_col), target);
                step = left_step(step); //continue left
            }

            next = next.plus(step);
        }
        
        return target;
    }

    private void register_point(Point point, Canvas cv) throws Exception{
        boundary_set.add((Tuple) point);
        cv.set(point);
    }    

    //Helper functions
    private Tuple left_step(Tuple p) {return new Tuple(p.y, -p.x);}
    private Tuple right_step(Tuple p) {return new Tuple(-p.y, p.x);}

}