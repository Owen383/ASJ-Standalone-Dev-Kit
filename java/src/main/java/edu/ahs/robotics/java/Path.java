package edu.ahs.robotics.java;

import java.util.ArrayList;

public class Path {

    public static class WayPoint {
        public Point point;
        private double deltaXFromPrevious;
        private double deltaYFromPrevious;
        private double distanceFromPrevious;

        private WayPoint(Point point, double deltaXFromPrevious, double deltaYFromPrevious, double distanceFromPrevious) {
            this.point = point;
            this.deltaXFromPrevious = deltaXFromPrevious;
            this.deltaYFromPrevious = deltaYFromPrevious;
            this.distanceFromPrevious = distanceFromPrevious;
        }

        public double getDistanceFromPrevious() {
            return distanceFromPrevious;
        }

        public String toString() {
            return "Point{" + "x=" + point.getX() + ", y=" + point.getY() + ", distance from previous = " + distanceFromPrevious + "}";
        }
        /**
         * Calculates the projection of the vector Vcurrent leading from the supplied current
         * point to this WayPoint onto the vector Vpath leading from the previous point on the path
         * to this WayPoint.  If the return value is positive, it means that the WayPoint is
         * farther along the path from the current point.  If the return value is negative, it means
         * that the WayPoint is before the current point (earlier on the path).
         *The magnitude of the value tells the
         * distance along the path.  The value is computed as the dot product between Vcurrent and
         * Vpath, normalized by the length of vPath
         * @param current The source point to compare to the WayPoint
         */
        private double componentAlongPath(Point current) {
            double deltaXFromCurrent = point.getX() - current.getX();
            double deltaYFromCurrent = point.getY() - current.getY();

            double dp = deltaXFromCurrent * deltaXFromPrevious + deltaYFromCurrent * deltaYFromPrevious;
            return dp / distanceFromPrevious;
        }
    }

    private ArrayList<WayPoint> wayPoints;
    double totalDistance = 0;
    /**
     * @param rawPoints Array of X,Y points.  Consecutive duplicate points are discarded
     *                  A path must have at least 2 non-identical points
     * @throws IllegalArgumentException for paths with fewer than 2 non-duplicate points.
     */

    public Path(Point[] rawPoints){

        double deltaXFromPrevious;
        double deltaYFromPrevious;
        double distanceFromPrevious;

        wayPoints = new ArrayList<>();
        Point j = rawPoints[0];
        wayPoints.add(new WayPoint(rawPoints[0], 0, 0, 0));

        for(int i = 1; i<rawPoints.length; i++){

            deltaXFromPrevious = rawPoints[i].getX() - j.getX();
            deltaYFromPrevious = rawPoints[i].getY() - j.getY();
            distanceFromPrevious = Math.sqrt(deltaXFromPrevious*deltaXFromPrevious + deltaYFromPrevious*deltaYFromPrevious);

            if(rawPoints[i].getX() != j.getX() || rawPoints[i].getY() != j.getY()) {
                wayPoints.add(new WayPoint(rawPoints[i], deltaXFromPrevious, deltaYFromPrevious, distanceFromPrevious));
                totalDistance = totalDistance + distanceFromPrevious;
            }
            j = rawPoints[i];
        }

    }

    public void printWayPoints(){
        for(WayPoint a:wayPoints){
            System.out.println(a);
        }
    }

    public double totalDistance() {
        return totalDistance;
    }

    /**
     * @return a point at the supplied look-ahead distance along the path from the supplied current position
     * Note that the point will usually be interpolated between the points that originally defined the Path
     */
    public Path.WayPoint targetPoint(Point current, double lookAheadDistance) {
        boolean b = true;
        int i = 0;
        double distance = 0;

        while(b && i<wayPoints.size()){
            i++;
            WayPoint a = wayPoints.get(i-1);
            WayPoint c = wayPoints.get(i);
            if (a.componentAlongPath(current) > 0) {
               b = false;
            }
        }
        while(distance < lookAheadDistance){
            WayPoint c = wayPoints.get(i);
            distance = distance + c.getDistanceFromPrevious();
            i++;
        }
        //a and c are our two brackets


        return new WayPoint(new Point(0,0), 0.0, 0.0, 0.0);

    }

}
