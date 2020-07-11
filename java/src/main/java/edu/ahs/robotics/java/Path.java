package edu.ahs.robotics.java;

import java.util.ArrayList;

public class Path {

    public static class WayPoint {
        public Point point;
        private double deltaXFromPrevious;
        private double deltaYFromPrevious;
        private double distanceFromPrevious;
        private double angleFromPrevious;

        private WayPoint(Point point, double deltaXFromPrevious, double deltaYFromPrevious, double distanceFromPrevious) {
            this.point = point;
            this.deltaXFromPrevious = deltaXFromPrevious;
            this.deltaYFromPrevious = deltaYFromPrevious;
            this.distanceFromPrevious = distanceFromPrevious;
            if(deltaXFromPrevious!=0){
                angleFromPrevious = Math.atan2(deltaYFromPrevious, deltaXFromPrevious);
            }else{
                angleFromPrevious = Math.PI/2;
            }
        }

        public double getDistanceFromPrevious() {
            return distanceFromPrevious;
        }
        public String toString() {
            return "Point{" + "x=" + point.getX() + ", y=" + point.getY() + ", distance from previous = " + distanceFromPrevious + "}";
        }
        public double getAngleFromPrevious(){
            return angleFromPrevious;
        }
        public double getX(){
            return point.getX();
        }
        public double getY(){
            return point.getY();
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
            distanceFromPrevious = Math.sqrt(deltaXFromPrevious * deltaXFromPrevious + deltaYFromPrevious * deltaYFromPrevious);

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

    public void printTargetPoint(Point current, double targetDistance){
        System.out.println(targetPoint(current, targetDistance));
    }

    /**
     * @return a point at the supplied look-ahead distance along the path from the supplied current position
     * Note that the point will usually be interpolated between the points that originally defined the Path
     */
    public Path.WayPoint targetPoint(Point current, double targetDistance) {
        int i = -1; int j = 0;
        double distance;
        double distancePassed = targetDistance;
        double popY;
        double popX;
        double popDistance;
        double distanceX;
        double distanceY;
        double finalDeltaX;
        double finalDeltaY;

        WayPoint a = new WayPoint(new Point(0,0),0,0,0);
        WayPoint c = new WayPoint(new Point(0,0),0,0,0);
        Point targetPoint;

        while(i<wayPoints.size()-1){
            i++;
            a = wayPoints.get(i);
            System.out.println(a.componentAlongPath(current)+" "+i);
            if(a.componentAlongPath(current) > 0){
               break;
            }
        }

        //trig stuff
        distance = a.componentAlongPath(current);
        popY = a.componentAlongPath(current) * Math.sin(a.getAngleFromPrevious());
        popX = a.componentAlongPath(current) * Math.cos(a.getAngleFromPrevious());
        current.distanceTraveled();
        popDistance = Math.sqrt(popX*popX+popY*popY);
        WayPoint shadow = new WayPoint(new Point (a.getX()-popX,a.getY()-popY), a.deltaXFromPrevious-popX,
                a.deltaYFromPrevious-popY, a.getDistanceFromPrevious() - popDistance);

        if(distance < targetDistance){
            distancePassed = distancePassed-distance;
        }

        //wraparound
        while(distance < targetDistance){
            i++; j++;
            c = wayPoints.get(i);
            distance = distance + c.getDistanceFromPrevious();
            distancePassed = distancePassed - c.getDistanceFromPrevious();
        }

        if(j==0){
            double ratio = a.getDistanceFromPrevious()/distancePassed;
            distanceX = a.deltaXFromPrevious / ratio;
            distanceY = a.deltaYFromPrevious / ratio;
            distanceX = (a.deltaXFromPrevious - shadow.deltaXFromPrevious) - distanceX;
            distanceY = (a.deltaYFromPrevious - shadow.deltaYFromPrevious) - distanceY;
            Point target = new Point(a.getX() - distanceX,a.getY() - distanceY);
            targetPoint = target;
        }

        else{
            double ratio = c.getDistanceFromPrevious()/distancePassed;
            distanceX = c.deltaXFromPrevious / ratio;
            distanceY = c.deltaYFromPrevious / ratio;
            distanceX = c.deltaXFromPrevious - distanceX;
            distanceY = c.deltaYFromPrevious - distanceY;
            Point target = new Point(c.getX() - distanceX,c.getY() - distanceY);
            targetPoint = target;
        }

        finalDeltaX = targetPoint.getX() - current.getX();
        finalDeltaY = targetPoint.getY() - current.getY();

        return new WayPoint(targetPoint, finalDeltaX, finalDeltaY, Math.sqrt(finalDeltaX * finalDeltaX + finalDeltaY * finalDeltaY));

    }

}
