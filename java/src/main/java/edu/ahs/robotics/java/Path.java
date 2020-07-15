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
            }else if(deltaYFromPrevious > 0){
                angleFromPrevious = Math.PI/2;
            }else{
                angleFromPrevious = 3*Math.PI/2;
            }
        }

        public double getDistanceFromPrevious() {
            return distanceFromPrevious;
        }
        public String toString() {
            return "Point{" + "(" + point.getX() + ", " + point.getY() + "), distance from previous = " + distanceFromPrevious + "}";
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
    int nextWaypointIndex = 0;
    WayPoint nextWaypoint = new WayPoint(new Point(0,0),0,0,0);
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
    public Path.WayPoint targetPoint(Point robotPosition, double targetDistance) {

        while(nextWaypointIndex < wayPoints.size()){
            nextWaypoint = wayPoints.get(nextWaypointIndex);
            System.out.println(nextWaypoint.componentAlongPath(robotPosition)+" "+ nextWaypointIndex);
            if(nextWaypoint.componentAlongPath(robotPosition) > 0){
               break;
            }
            nextWaypointIndex++;
        }

        //trig stuff
        double distance = nextWaypoint.componentAlongPath(robotPosition);
        double dYcomponent = nextWaypoint.componentAlongPath(robotPosition) * Math.sin(nextWaypoint.getAngleFromPrevious());
        double dXcomponent = nextWaypoint.componentAlongPath(robotPosition) * Math.cos(nextWaypoint.getAngleFromPrevious());
        robotPosition.distanceTraveled();
        WayPoint shadow = new WayPoint(new Point (nextWaypoint.getX()-dXcomponent, nextWaypoint.getY()-dYcomponent), nextWaypoint.deltaXFromPrevious-dXcomponent,
                nextWaypoint.deltaYFromPrevious-dYcomponent, nextWaypoint.getDistanceFromPrevious() - distance);

        double distancePassed = targetDistance;
        if(distance < targetDistance){
            distancePassed = distancePassed - distance;
        }

        int j = 0;
        WayPoint waypointAfterTarget = new WayPoint(new Point(0,0),0,0,0);
        //wraparound
        while(distance < targetDistance){
            nextWaypointIndex++; j++;
            waypointAfterTarget = wayPoints.get(nextWaypointIndex);
            distance = distance + waypointAfterTarget.getDistanceFromPrevious();
            if(distance < targetDistance){
                distancePassed = distancePassed - waypointAfterTarget.getDistanceFromPrevious();
            }

        }

        Point targetPoint;

        if(j==0){
            double ratio = nextWaypoint.getDistanceFromPrevious()/distancePassed;
            double distanceX = nextWaypoint.deltaXFromPrevious / ratio;
            double distanceY = nextWaypoint.deltaYFromPrevious / ratio;
            distanceX = (nextWaypoint.deltaXFromPrevious - shadow.deltaXFromPrevious) - distanceX;
            distanceY = (nextWaypoint.deltaYFromPrevious - shadow.deltaYFromPrevious) - distanceY;
            Point target = new Point(nextWaypoint.getX() - distanceX, nextWaypoint.getY() - distanceY);
            targetPoint = target;
        }

        else{
            double ratio = waypointAfterTarget.getDistanceFromPrevious()/distancePassed;
            double distanceX = waypointAfterTarget.deltaXFromPrevious / ratio;
            double distanceY = waypointAfterTarget.deltaYFromPrevious / ratio;
            distanceX = waypointAfterTarget.deltaXFromPrevious - distanceX;
            distanceY = waypointAfterTarget.deltaYFromPrevious - distanceY;
            System.out.println(waypointAfterTarget);
            Point target = new Point(waypointAfterTarget.getX() - distanceX,waypointAfterTarget.getY() - distanceY);
            targetPoint = target;
        }

        double finalDeltaX = targetPoint.getX() - robotPosition.getX();
        double finalDeltaY = targetPoint.getY() - robotPosition.getY();

        return new WayPoint(targetPoint, finalDeltaX, finalDeltaY, Math.sqrt(finalDeltaX * finalDeltaX + finalDeltaY * finalDeltaY));

    }

}
