package edu.ahs.robotics.java;

public class Point {

    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Point{" + "x=" + x + ", y=" + y + '}';
    }

    public double distanceFromOrigin(){
        double distance = Math.sqrt(x*x+y*y);
        return distance;
    }

    public void distanceTraveled(){
        System.out.println("cheeve man");
    }

    public String getQuadrant(){
        if(x > 0 && y > 0){
            return "Quadrant 1";
        }else if(x < 0 && y > 0){
            return "Quadrant 2";
        }else if(x < 0 && y < 0){
            return "Quadrant 3";
        }else if(x > 0 && y < 0){
            return "Quadrant 4";
        }else if(x == 0 && y == 0){
            return "Origin";
        }else{
            return "Axis";
        }
    }

}
