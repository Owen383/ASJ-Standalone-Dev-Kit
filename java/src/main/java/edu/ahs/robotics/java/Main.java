package edu.ahs.robotics.java;

public class Main {
    public static void main(String[] args){

        Point a = new Point(0, 0);
        Point b = new Point(3, 6);
        Point[] ab = new Point[2];
        ab[0] = a;
        ab[1] = b;

        LineSegment c = new LineSegment(a,b);

        System.out.println(a.getX());
        System.out.println(a.getY());
        System.out.println(a);

        System.out.println("Distance from Origin = "+a.distanceFromOrigin() + "\n");

        System.out.println("Point a is in " + a.getQuadrant() + "\n");
        System.out.println("Point b is in " + b.getQuadrant() + "\n");

        Point [] v = c.subDivide(3);
        for(int i = 0; i < v.length; i++){
            System.out.println(v[i]);
        }
        System.out.println(c.midPoint());

        System.out.println(c.length());

        Path dummy = new Path(ab);

        dummy.printWayPoints();
    }

}