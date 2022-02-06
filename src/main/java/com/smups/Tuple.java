package com.smups;

import java.io.Serializable;

import com.smups.drawings.Metric;

public class Tuple implements Serializable{

    public final double x;
    public final double y;

    public Tuple(double x, double y) {this.x = x; this.y = y;}
    public Tuple(Tuple t) {this.x = t.x; this.y = t.y;}

    private transient static final Metric cartesian_metric = new Metric() {
        @Override
        public double distance(Tuple p1, Tuple p2) {
            return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
        }
    };

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tuple)) return false;
        Tuple that = (Tuple) o;
        return (Math.round(this.x) == Math.round(that.x)) && 
               (Math.round(this.y) == Math.round(that.y));
    }

    @Override
    public String toString() {
        return String.format("T(%f,%f)", x,y);
    }
    
    //Elementry mathematics:

    //Single tuple functions
    public double size(Metric m) {return m.distance(this, this);}
    public double size() {return size(Tuple.cartesian_metric);}

    //2-Tuple functions
    public Tuple plus(Tuple t) {return new Tuple(this.x + t.x, this.y + t.y);}
    public Tuple min(Tuple t) {return new Tuple(this.x - t.x, this.y - t.y);}
    public double dot(Tuple t, Metric m) {return m.distance(this, t);}
    public double dot(Tuple t) {return dot(t, Tuple.cartesian_metric);}
    public double angle(Tuple t, Metric m) {
        return Math.acos(dot(t, m) / t.size(m) * this.size(m) );
    }
    public double angle(Tuple t) {return angle(t, Tuple.cartesian_metric);}

}