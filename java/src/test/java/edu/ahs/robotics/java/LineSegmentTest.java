package edu.ahs.robotics.java;

import org.junit.Test;

import static org.junit.Assert.*;

public class LineSegmentTest {

    @Test
    public void subDivide() {
        Point[] pointsExpected = new Point[0];
        pointsExpected[0] = new Point(1,1);
        pointsExpected[0] = new Point(2,2);

        LineSegment lineSegment = new LineSegment(new Point(0,0), new Point(3,3));
        Point[] pointsActual = lineSegment.subDivide(3);

        for(int i = 0; i < pointsActual.length; i++){
            assertEquals(pointsExpected[i].getX(), pointsActual[i].getX(), .000001);
            assertEquals(pointsExpected[i].getY(), pointsActual[i].getY(), .000001);
        }
    }
}