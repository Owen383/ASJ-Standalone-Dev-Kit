package edu.ahs.robotics.java;

import org.junit.Test;

import static org.junit.Assert.*;

public class PathTest {

    @Test
    public void testDuplicatesRemoved() {
        // Make some points
        Point[] points1 = new Point[] {new Point(0,0), new Point(3,4), new Point(3,4), new Point(5,5)};
        Path path1 = new Path(points1);
        Path.WayPoint[] points2 = path1.returnWayPoints();

        boolean foo = true;

        for(int i = 0; i<points2.length; i++){
            for(int j = i + 1; j<points2.length; j++){
                if(points2[i]==points2[j]){
                    foo = false;
                }else{foo = true;}
            }
        }
        assertTrue(foo);
    }

}