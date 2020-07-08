package edu.ahs.robotics.java;

public class LineSegment {

    private Point point1;
    private Point point2;

    private double deltaX;
    private double deltaY;
    private double sub;

    public LineSegment(Point point1, Point point2) {
        this.point1 = point1;
        this.point2 = point2;
        deltaX = point1.getX() - point2.getX();
        deltaY = point1.getY() - point2.getY();
    }

    

    public Point[] subDivide(int subSegments){
        sub = subSegments;

        Point [] pointsArray = new Point [subSegments-1];
        
        for(int i = 0; i < subSegments - 1; i++) {
            pointsArray[i] = new Point(point1.getX() - deltaX/sub * (i + 1), point1.getY() - deltaY/sub * (i + 1));
        }
        
        return pointsArray;
    }

    public Point midPoint(){
        return new Point(point1.getX()+deltaX/2,point1.getY()+deltaY/2);
    }

    public double length(){
        return (Math.sqrt(deltaX*deltaX+deltaY*deltaY));
    }


}
