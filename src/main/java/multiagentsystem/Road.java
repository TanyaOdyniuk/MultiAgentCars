package multiagentsystem;

import java.io.Serializable;
import java.util.Objects;

public class Road implements Serializable{
    private Integer startPoint;
    private Integer endPoint;
    private RoadStatistic statistic;

    public Road() {
    }

    public Road(Integer startPoint, Integer endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.statistic  = new RoadStatistic();
    }

    public Integer getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Integer startPoint) {
        this.startPoint = startPoint;
    }

    public Integer getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Integer endPoint) {
        this.endPoint = endPoint;
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
        return Objects.equals(startPoint, road.startPoint) &&
                Objects.equals(endPoint, road.endPoint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPoint, endPoint);
    }
}
