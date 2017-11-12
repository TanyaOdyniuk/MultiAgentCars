package multiagentsystem;

public class Road {
    private int startPoint;
    private int endPoint;
    private int distance;
    private RoadStatistic statistic;

    public Road(int startPoint, int endPoint, int distance) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.distance = distance;
        this.statistic  = new RoadStatistic();
    }

    public int getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(int startPoint) {
        this.startPoint = startPoint;
    }

    public int getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(int endPoint) {
        this.endPoint = endPoint;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public RoadStatistic getStatistic() {
        return statistic;
    }
    public void refreshStatistic(Integer time){
        statistic.refreshTime(time);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Road road = (Road) o;

        if (startPoint != road.startPoint) return false;
        if (endPoint != road.endPoint) return false;
        return distance == road.distance;
    }

    @Override
    public int hashCode() {
        int result = startPoint;
        result = 31 * result + endPoint;
        result = 31 * result + distance;
        return result;
    }
}
