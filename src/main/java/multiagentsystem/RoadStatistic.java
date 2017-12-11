package multiagentsystem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoadStatistic implements Serializable {
    private List<Integer> time;
    private final int STATISTICS_CAPACITY = 5;

    public RoadStatistic() {
        this.time = new ArrayList<>(STATISTICS_CAPACITY);
    }

    public Double getAverageTime() {
        Double resalt = 0D;
        for (Integer t:time) {
            resalt += t;
        }
        return (time.size() != 0) ? resalt / time.size() : 0D;
    }

    public void refreshTime(Integer ntime){
        if(time.size() == STATISTICS_CAPACITY){
            time.subList(1, STATISTICS_CAPACITY).clear();
        }
        time.add(ntime);
    }
}
