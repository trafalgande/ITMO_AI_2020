package pepe.lmao.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode

public class City implements Comparable<City> {
    public String name;
    @EqualsAndHashCode.Exclude
    public City parent;
    @EqualsAndHashCode.Exclude
    public Integer distance;
    @EqualsAndHashCode.Exclude
    public Boolean visited;

    public City(City parent, String name, Integer distance) {
        this.parent = parent;
        this.name = name;
        this.distance = distance;
        this.visited = false;
    }

    public void forget() {
        this.visited = false;
    }


    @Override
    public int compareTo(City o) {
        int thisDistance = this.distance;
        int otherDistance = o.distance;
        int delta = thisDistance - otherDistance;
        return Integer.compare(delta, 0);
    }
}
