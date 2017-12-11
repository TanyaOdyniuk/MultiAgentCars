package multiagentsystem;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CityMap implements Serializable {
    protected Set<Road> roads;

    public CityMap() {
        roads = new HashSet<Road>();
    }
    public Set<Road> getRoads() {
        return roads;
    }
    public RoadStatistic getCurrentRoadStatistics(Road road) {
        RoadStatistic result = null;
        Iterator<Road> roadIterator = roads.iterator();
        while (roadIterator.hasNext()) {
            Road roadFromCityMap = roadIterator.next();
            if (roadFromCityMap.equals(road)) {
                result = roadFromCityMap.getStatistic();
                break;
            }
        }
        return result;
    }


}
