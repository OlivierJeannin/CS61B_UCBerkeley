import ngrams.TimeSeries;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

/** Unit Tests for the TimeSeries class.
 *  @author Josh Hug
 */
public class TimeSeriesTest {
    @Test
    public void testFromSpec() {
        TimeSeries catPopulation = new TimeSeries();
        catPopulation.put(1991, 0.0);
        catPopulation.put(1992, 100.0);
        catPopulation.put(1994, 200.0);

        TimeSeries dogPopulation = new TimeSeries();
        dogPopulation.put(1994, 400.0);
        dogPopulation.put(1995, 500.0);

        TimeSeries totalPopulation = catPopulation.plus(dogPopulation);
        // expected: 1991: 0,
        //           1992: 100
        //           1994: 600
        //           1995: 500

        List<Integer> expectedYears = new ArrayList<>();
        expectedYears.add(1991);
        expectedYears.add(1992);
        expectedYears.add(1994);
        expectedYears.add(1995);

        assertThat(totalPopulation.years()).isEqualTo(expectedYears);

        List<Double> expectedTotal = new ArrayList<>();
        expectedTotal.add(0.0);
        expectedTotal.add(100.0);
        expectedTotal.add(600.0);
        expectedTotal.add(500.0);

        for (int i = 0; i < expectedTotal.size(); i += 1) {
            assertThat(totalPopulation.data().get(i)).isWithin(1E-10).of(expectedTotal.get(i));
        }
    }

    @Test
    public void testEmptyBasic() {
        TimeSeries catPopulation = new TimeSeries();
        TimeSeries dogPopulation = new TimeSeries();

        assertThat(catPopulation.years()).isEmpty();
        assertThat(catPopulation.data()).isEmpty();

        TimeSeries totalPopulation = catPopulation.plus(dogPopulation);

        assertThat(totalPopulation.years()).isEmpty();
        assertThat(totalPopulation.data()).isEmpty();

        // I added this test to dividedBy()
        TimeSeries quotientPopulation = catPopulation.dividedBy(dogPopulation);

        assertThat(quotientPopulation.years()).isEmpty();
        assertThat(quotientPopulation.data()).isEmpty();
    }

    @Test
    public void testCopyConstructor() {
        TimeSeries ts1 = new TimeSeries();
        // add new mappings in random order
        ts1.put(2000, 1.1);
        ts1.put(1998, 0.03);
        ts1.put(2001, 3.14);
        ts1.put(2002, 9.98);
        ts1.put(1999, 2.7);

        TimeSeries ts2 = new TimeSeries(ts1, 1999, 2001);
        assertThat(ts2).containsExactly(1999, 2.7, 2000, 1.1, 2001, 3.14).inOrder();
    }

    @Test
    public void testYearsAndData() {
        TimeSeries ts = new TimeSeries();
        assertThat(ts.years()).isEmpty();

        ts.put(2001, 3.14);
        ts.put(2000, 1.1);
        ts.put(1999, 2.7);
        ts.put(2002, 9.98);
        ts.put(1998, 0.03);
        assertThat(ts.years()).containsExactly(1998, 1999, 2000, 2001, 2002).inOrder();
        assertThat(ts.data()).containsExactly(0.03, 2.7, 1.1, 3.14, 9.98).inOrder();
    }

    @Test
    public void testPlus() {
        TimeSeries ts1 = new TimeSeries();
        ts1.put(2000, 0.5);
        ts1.put(2004, 4.9);
        ts1.put(2006, 2.2);
        ts1.put(2001, 2.2);

        TimeSeries ts2 = new TimeSeries();
        ts2.put(2006, 4.4);
        ts2.put(2004, 0.7);
        ts2.put(2002, 2.0);
        ts2.put(2007, 7.0);
        ts2.put(2008, 8.8);

        TimeSeries total = ts1.plus(ts2);
        // expected: 2000: 0.5
        //           2001: 2.2
        //           2002: 2
        //           2004: 5.6
        //           2006: 6.6
        //           2007: 7.0
        //           2008: 8.8

        List<Integer> expectedYears = new ArrayList<>();
        expectedYears.add(2000);
        expectedYears.add(2001);
        expectedYears.add(2002);
        expectedYears.add(2004);
        expectedYears.add(2006);
        expectedYears.add(2007);
        expectedYears.add(2008);
        assertThat(total.years()).isEqualTo(expectedYears);

        List<Double> expectedTotal = new ArrayList<>();
        expectedTotal.add(0.5);
        expectedTotal.add(2.2);
        expectedTotal.add(2.0);
        expectedTotal.add(5.6);
        expectedTotal.add(6.6);
        expectedTotal.add(7.0);
        expectedTotal.add(8.8);
        for (int i = 0; i < expectedTotal.size(); i++) {
            assertThat(total.data().get(i)).isWithin(1E-10).of(expectedTotal.get(i));
        }
    }

    @Test
    public void testDividedBy() {
        TimeSeries ts1 = new TimeSeries();
        ts1.put(2000, 0.5);
        ts1.put(2004, 4.9);
        ts1.put(2006, 2.2);
        ts1.put(2001, 2.2);

        TimeSeries ts2 = new TimeSeries();
        ts2.put(2006, 4.4);
        ts2.put(2001, 0.8);
        ts2.put(2004, 0.7);
        ts2.put(2002, 2.0);
        ts2.put(2007, 7.0);
        ts2.put(2000, 0.5);

        TimeSeries quotient = ts1.dividedBy(ts2);
        // expected: 2000: 1
        //           2001: 2.75
        //           2004: 7
        //           2006: 0.5

        List<Integer> expectedYears = new ArrayList<>();
        expectedYears.add(2000);
        expectedYears.add(2001);
        expectedYears.add(2004);
        expectedYears.add(2006);
        assertThat(quotient.years()).isEqualTo(expectedYears);

        List<Double> expectedTotal = new ArrayList<>();
        expectedTotal.add(1.0);
        expectedTotal.add(2.75);
        expectedTotal.add(7.0);
        expectedTotal.add(0.5);
        for (int i = 0; i < expectedTotal.size(); i++) {
            assertThat(quotient.data().get(i)).isWithin(1E-10).of(expectedTotal.get(i));
        }
    }
}
