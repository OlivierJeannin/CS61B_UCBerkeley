package ngrams;

import java.util.*;

/**
 * An object for mapping a year number (e.g. 1996) to numerical data. Provides
 * utility methods useful for data analysis.
 *
 * @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {

    /** If it helps speed up your code, you can assume year arguments to your NGramMap
     * are between 1400 and 2100. We've stored these values as the constants
     * MIN_YEAR and MAX_YEAR here. */
    public static final int MIN_YEAR = 1400;
    public static final int MAX_YEAR = 2100;

    /**
     * Constructs a new empty TimeSeries.
     */
    public TimeSeries() {
        super();
    }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     * inclusive of both end points.
     */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        super();
        for (int year: ts.keySet()) {
            if (year >= startYear && year <= endYear) {
                put(year, ts.get(year));
            }
        }
    }

    /**
     *  Returns all years for this time series in ascending order.
     */
    public List<Integer> years() {
        return new ArrayList<>(keySet());
    }

    /**
     *  Returns all data for this time series. Must correspond to the
     *  order of years().
     */
    public List<Double> data() {
        List<Double> list = new ArrayList<>();
        for (int year : years()) {
            list.add(get(year));
        }
        return list;
    }

    /**
     * Returns the year-wise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     *
     * If both TimeSeries don't contain any years, return an empty TimeSeries.
     * If one TimeSeries contains a year that the other one doesn't, the returned TimeSeries
     * should store the value from the TimeSeries that contains that year.
     */
    public TimeSeries plus(TimeSeries ts) {
        List<Integer> years1 = this.years();
        List<Double> data1 = this.data();
        List<Integer> years2 = ts.years();
        List<Double> data2 = ts.data();

        TimeSeries ret = new TimeSeries();
        int i1 = 0;
        int i2 = 0;
        while (i1 < years1.size() && i2 < years2.size()) {  // both lists have remaining elements
            int year1 = years1.get(i1);
            int year2 = years2.get(i2);
            double d1 = data1.get(i1);
            double d2 = data2.get(i2);
            if (year1 < year2) {
                ret.put(year1, d1);
                i1++;
            } else if (year1 > year2) {
                ret.put(year2, d2);
                i2++;
            } else {
                ret.put(year1, d1 + d2);
                i1++;
                i2++;
            }
        }
        while (i1 < years1.size()) {  // list1 still has remaining elements
            ret.put(years1.get(i1), data1.get(i1));
            i1++;
        }
        while (i2 < years2.size()) {  // list2 still has remaining elements
            ret.put(years2.get(i2), data2.get(i2));
            i2++;
        }
        return ret;
    }

    /**
     * Returns the quotient of the value for each year this TimeSeries divided by the
     * value for the same year in TS. Should return a new TimeSeries (does not modify this
     * TimeSeries).
     *
     * If TS is missing a year that exists in this TimeSeries, throw an
     * IllegalArgumentException.
     * If TS has a year that is not in this TimeSeries, ignore it.
     */
    public TimeSeries dividedBy(TimeSeries ts) {
        Set<Integer> years1 = this.keySet();
        Set<Integer> years2 = ts.keySet();

        Set<Integer> intersection = new TreeSet<>(years1);
        intersection.retainAll(years2);

        if (!intersection.equals(years1)) {
            throw new IllegalArgumentException();
        }

        TimeSeries ret = new TimeSeries();
        for (Integer year: intersection) {
            ret.put(year, this.get(year) / ts.get(year));
        }
        return ret;
    }
}
