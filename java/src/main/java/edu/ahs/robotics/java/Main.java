package edu.ahs.robotics.java;

public class Main {
    public static void main(String[] args){

        Point a = new Point(-9, -9);
        Point b = new Point(-8, -7);
        Point a2 = new Point(-6, -4);
        Point b2 = new Point(0, 0);

        Point current = new Point(-7,-8);

        Point[] ab = new Point[4];
        ab[0] = a;
        ab[1] = b;
        ab[2] = a2;
        ab[3] = b2;

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
        System.out.println(dummy.totalDistance());

        dummy.printTargetPoint(current, .5);

    }
}