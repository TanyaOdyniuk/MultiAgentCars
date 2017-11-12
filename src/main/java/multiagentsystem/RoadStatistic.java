package multiagentsystem;

import java.util.ArrayList;
import java.util.List;

public class RoadStatistic {
    private List<Integer> time;

    public RoadStatistic() {
        this.time = new ArrayList<Integer>(10);
    }

    public List<Integer> getTime() {
        return time;
    }
    public void refreshTime(Integer ntime){
        time.subList(1, 10).clear();
        time.add(ntime);
    }
}
