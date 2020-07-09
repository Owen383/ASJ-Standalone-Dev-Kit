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
        private WayPoint(Point point) {
            this.point = point;
        }
        public String toString() {
            return "Point{" + "x=" + point.getX() + ", y=" + point.getY() + '}';
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

    /**
     * @param rawPoints Array of X,Y points.  Consecutive duplicate points are discarded
     *                  A path must have at least 2 non-identical points
     * @throws IllegalArgumentException for paths with fewer than 2 non-duplicate points.
     */

    private ArrayList<WayPoint> wayPoints;
    Point j = new Point(0, 0);
    int counter = 0;



    public Path(Point[] rawPoints){
        wayPoints = new ArrayList<>();
        for(Point i:rawPoints){
            if(i==j) {
                wayPoints.add(new WayPoint(i));
                counter++;
            }else if(i.getX() == 0 && i.getY() == 0){
                wayPoints.add(new WayPoint(i));
                counter++;
            }
                Point j = i;

        }

    }
    WayPoint[] points = new WayPoint[counter];

    public WayPoint[] returnWayPoints(){
        for(int i = 0; i<counter; i++){
            points[i] = wayPoints.get(i);
        }
        return points;
    }

    public void printWayPoints(){
        for(WayPoint a:wayPoints){
            System.out.println(a);
        }
    }

    public double totalDistance() {
        return 0.0;
    }





    /**
     * @return a point at the supplied look-ahead distance along the path from the supplied current position
     * Note that the point will usually be interpolated between the points that originally defined the Path
     */
    public Path.WayPoint targetPoint(Point current, double lookAheadDistance) {
        return new WayPoint(new Point(0,0), 0.0, 0.0, 0.0);

    }

}
