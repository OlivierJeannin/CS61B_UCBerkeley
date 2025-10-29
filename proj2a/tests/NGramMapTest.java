import ngrams.NGramMap;
import ngrams.TimeSeries;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static utils.Utils.*;
import static com.google.common.truth.Truth.assertThat;

/** Unit Tests for the NGramMap class.
 *  @author Josh Hug
 */
public class NGramMapTest {
    @Test
    public void testCountHistory() {
        NGramMap ngm = new NGramMap(SHORT_WORDS_FILE, TOTAL_COUNTS_FILE);
        List<Integer> expectedYears = new ArrayList<>();
        expectedYears.add(2005);
        expectedYears.add(2006);
        expectedYears.add(2007);
        expectedYears.add(2008);

        List<Double> expectedCounts = new ArrayList<>();
        expectedCounts.add(646179.0);
        expectedCounts.add(677820.0);
        expectedCounts.add(697645.0);
        expectedCounts.add(795265.0);

        TimeSeries request2005to2008 = ngm.countHistory("request");
        assertThat(request2005to2008.years()).isEqualTo(expectedYears);

        for (int i = 0; i < expectedCounts.size(); i += 1) {
            assertThat(request2005to2008.data().get(i)).isWithin(1E-10).of(expectedCounts.get(i));
        }

        expectedYears = new ArrayList<>();
        expectedYears.add(2006);
        expectedYears.add(2007);
        expectedCounts = new ArrayList<>();
        expectedCounts.add(677820.0);
        expectedCounts.add(697645.0);

        TimeSeries request2006to2007 = ngm.countHistory("request", 2006, 2007);

        assertThat(request2006to2007.years()).isEqualTo(expectedYears);

        for (int i = 0; i < expectedCounts.size(); i += 1) {
            assertThat(request2006to2007.data().get(i)).isWithin(1E-10).of(expectedCounts.get(i));
        }
    }

    @Test
    public void testCountHistoryEmpty() {
        NGramMap ngm = new NGramMap(SHORT_WORDS_FILE, TOTAL_COUNTS_FILE);

        TimeSeries empty1 = ngm.countHistory("empty");
        assertThat(empty1).isNotNull();
        assertThat(empty1).isEmpty();

        TimeSeries empty2 = ngm.countHistory("airport", 2005, 2006);
        assertThat(empty2).isNotNull();
        assertThat(empty2).isEmpty();
    }

    @Test
    public void testTotalCountHistory() {
        NGramMap ngm = new NGramMap(SHORT_WORDS_FILE, TOTAL_COUNTS_FILE);
        TimeSeries totalCounts = ngm.totalCountHistory();
        assertThat(totalCounts.get(2007)).isWithin(1E-10).of(28307904288.0);
    }

    @Test
    public void testWeightHistory() {
        NGramMap ngm = new NGramMap(SHORT_WORDS_FILE, TOTAL_COUNTS_FILE);

        TimeSeries wandered2005to2008 = ngm.weightHistory("wandered");

        List<Integer> expectedYears = new ArrayList<>();
        expectedYears.add(2005);
        expectedYears.add(2006);
        expectedYears.add(2007);
        expectedYears.add(2008);
        assertThat(wandered2005to2008.years()).isEqualTo(expectedYears);

        List<Double> expectedFrequencies = new ArrayList<>();
        expectedFrequencies.add(83769.0 / 26609986084.0);
        expectedFrequencies.add(87688.0 / 27695491774.0);
        expectedFrequencies.add(108634.0 / 28307904288.0);
        expectedFrequencies.add(171015.0 / 28752030034.0);
        for (int i = 0; i < expectedYears.size(); i++) {
            assertThat(wandered2005to2008.data().get(i)).isWithin(1E-10).of(expectedFrequencies.get(i));
        }

        TimeSeries wandered2005to2006 = ngm.weightHistory("wandered", 2005, 2006);

        expectedYears = new ArrayList<>();
        expectedYears.add(2005);
        expectedYears.add(2006);
        assertThat(wandered2005to2006.years()).isEqualTo(expectedYears);

        expectedFrequencies = new ArrayList<>();
        expectedFrequencies.add(83769.0 / 26609986084.0);
        expectedFrequencies.add(87688.0 / 27695491774.0);
        for (int i = 0; i < expectedYears.size(); i++) {
            assertThat(wandered2005to2006.data().get(i)).isWithin(1E-10).of(expectedFrequencies.get(i));
        }
    }

    @Test
    public void testWeightHistoryEmpty() {
        NGramMap ngm = new NGramMap(SHORT_WORDS_FILE, TOTAL_COUNTS_FILE);

        TimeSeries empty1 = ngm.weightHistory("aoeu");
        assertThat(empty1).isNotNull();
        assertThat(empty1).isEmpty();

        TimeSeries empty2 = ngm.weightHistory("request", 1800, 1899);
        assertThat(empty2).isNotNull();
        assertThat(empty2).isEmpty();
    }

    @Test
    public void testSummedWeightHistory() {
        List<String> wordList = new ArrayList<>();
        wordList.add("airport");
        wordList.add("request");

        NGramMap ngm = new NGramMap(SHORT_WORDS_FILE, TOTAL_COUNTS_FILE);

        TimeSeries airportHistory = ngm.countHistory("airport");
        // there is no data for word "airport" in 2006
        assertThat(airportHistory.get(2008)).isWithin(1E-10).of(173294.0);

        TimeSeries requestHistory = ngm.countHistory("request");
        assertThat(requestHistory.get(2006)).isWithin(1E-10).of(677820.0);
        assertThat(requestHistory.get(2008)).isWithin(1E-10).of(795265.0);

        TimeSeries totalCounts = ngm.totalCountHistory();
        assertThat(totalCounts.get(2008)).isWithin(1E-10).of(28752030034.0);

        TimeSeries airportPlusRequestWeight = ngm.summedWeightHistory(wordList);
        assertThat(airportPlusRequestWeight.get(2008)).isWithin(1E-10).of((173294.0 + 795265.0) / 28752030034.0);

        TimeSeries airportPlusRequestWeight2006to2008 = ngm.summedWeightHistory(wordList, 2006, 2008);
        assertThat(airportPlusRequestWeight2006to2008.get(2006)).isWithin(1E-10).of(677820.0 / 27695491774.0);
    }

    @Test
    public void testSummedWeightHistoryEmpty() {
        NGramMap ngm = new NGramMap(SHORT_WORDS_FILE, TOTAL_COUNTS_FILE);

        List<String> noValidWordList = new ArrayList<>();
        noValidWordList.add("aoeu");
        noValidWordList.add("empty");

        TimeSeries noValidWord = ngm.summedWeightHistory(noValidWordList);
        assertThat(noValidWord).isNotNull();
        assertThat(noValidWord).isEmpty();

        List<String> noValidCountList = new ArrayList<>();
        noValidWordList.add("airport");
        noValidWordList.add("empty");

        TimeSeries noValidCount = ngm.summedWeightHistory(noValidCountList, 2005, 2006);
        assertThat(noValidCount).isNotNull();
        assertThat(noValidCount).isEmpty();
    }

    @Test
    public void testOnShortFile() {
        // creates an NGramMap from a large dataset
        NGramMap ngm = new NGramMap(SHORTER_WORDS_FILE,
                TOTAL_COUNTS_FILE);

        // returns the count of the number of occurrences of economically per year between 2000 and 2010.
        TimeSeries econCount = ngm.countHistory("economically", 2000, 2010);
        assertThat(econCount.get(2000)).isWithin(1E-10).of(294258.0);
        assertThat(econCount.get(2010)).isWithin(1E-10).of(222744.0);

        TimeSeries totalCounts = ngm.totalCountHistory();
        assertThat(totalCounts.get(1999)).isWithin(1E-10).of(22668397698.0);

        // returns the relative weight of the word academic in each year between 1999 and 2010.
        TimeSeries academicWeight = ngm.weightHistory("academic", 1999, 2010);
        assertThat(academicWeight.get(1999)).isWithin(1E-7).of(969087.0 / 22668397698.0);
    }
    @Test
    public void testOnLargeFile() {
        // creates an NGramMap from a large dataset
        NGramMap ngm = new NGramMap(TOP_14337_WORDS_FILE,
                TOTAL_COUNTS_FILE);

        // returns the count of the number of occurrences of fish per year between 1850 and 1933.
        TimeSeries fishCount = ngm.countHistory("fish", 1850, 1933);
        assertThat(fishCount.get(1865)).isWithin(1E-10).of(136497.0);
        assertThat(fishCount.get(1922)).isWithin(1E-10).of(444924.0);

        TimeSeries totalCounts = ngm.totalCountHistory();
        assertThat(totalCounts.get(1865)).isWithin(1E-10).of(2563919231.0);

        // returns the relative weight of the word fish in each year between 1850 and 1933.
        TimeSeries fishWeight = ngm.weightHistory("fish", 1850, 1933);
        assertThat(fishWeight.get(1865)).isWithin(1E-7).of(136497.0/2563919231.0);

        TimeSeries dogCount = ngm.countHistory("dog", 1850, 1876);
        assertThat(dogCount.get(1865)).isWithin(1E-10).of(75819.0);

        List<String> fishAndDog = new ArrayList<>();
        fishAndDog.add("fish");
        fishAndDog.add("dog");
        TimeSeries fishPlusDogWeight = ngm.summedWeightHistory(fishAndDog, 1865, 1866);

        double expectedFishPlusDogWeight1865 = (136497.0 + 75819.0) / 2563919231.0;
        assertThat(fishPlusDogWeight.get(1865)).isWithin(1E-10).of(expectedFishPlusDogWeight1865);
    }

    @Test
    public void testOnVeryLargeFile() {
        // Uses the same test code as in testOnLargeFile(),
        // except for using TOP_49887_WORDS_FILE as data file.

        NGramMap ngm = new NGramMap(TOP_49887_WORDS_FILE, TOTAL_COUNTS_FILE);

        // returns the count of the number of occurrences of fish per year between 1850 and 1933.
        TimeSeries fishCount = ngm.countHistory("fish", 1850, 1933);
        assertThat(fishCount.get(1865)).isWithin(1E-10).of(136497.0);
        assertThat(fishCount.get(1922)).isWithin(1E-10).of(444924.0);

        TimeSeries totalCounts = ngm.totalCountHistory();
        assertThat(totalCounts.get(1865)).isWithin(1E-10).of(2563919231.0);

        // returns the relative weight of the word fish in each year between 1850 and 1933.
        TimeSeries fishWeight = ngm.weightHistory("fish", 1850, 1933);
        assertThat(fishWeight.get(1865)).isWithin(1E-7).of(136497.0/2563919231.0);

        TimeSeries dogCount = ngm.countHistory("dog", 1850, 1876);
        assertThat(dogCount.get(1865)).isWithin(1E-10).of(75819.0);

        List<String> fishAndDog = new ArrayList<>();
        fishAndDog.add("fish");
        fishAndDog.add("dog");
        TimeSeries fishPlusDogWeight = ngm.summedWeightHistory(fishAndDog, 1865, 1866);

        double expectedFishPlusDogWeight1865 = (136497.0 + 75819.0) / 2563919231.0;
        assertThat(fishPlusDogWeight.get(1865)).isWithin(1E-10).of(expectedFishPlusDogWeight1865);
    }
}