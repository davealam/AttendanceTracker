package Application;

import java.util.Comparator;

public class FallOffDateComparator implements Comparator<Points> {
    @Override
    public int compare(Points points, Points morePoints) {
        if (points.getReceivedDate().isAfter(morePoints.getReceivedDate())) {
            return +1;
        } if(points.getReceivedDate().isBefore(morePoints.getReceivedDate())) {
            return -1;
        } else {
            return 0;
        }
    }
}
