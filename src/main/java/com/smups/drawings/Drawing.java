package com.smups.drawings;

import com.smups.Canvas;

public abstract class Drawing {
    
    //Canvas that should be used for drawing (by children)
    protected final Canvas cv;

    public Drawing(Canvas cv) {this.cv = cv;}

    //Drawing function to be overridden by children
    public abstract Canvas draw() throws Exception;

}