package model;

import main.model.Period;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by san on 23/07/15.
 *
 */

public class PeriodTest {


    private Period randomPeriod = new Period();

    @Test
    public void nextPeriodShouldBeRecurrenceDaysAhead() {
        int recDays = randomPeriod.getRecurrenceDays();

        Period next = randomPeriod.createNextPeriod();

        String msg = "The distance between the two consecutive periods should be the number of the recurrence days of the first period";
        assertEquals(msg, recDays, Days.daysBetween(randomPeriod.getStartDate(), next.getStartDate()).getDays());
    }

    @Test
    public void endDateShouldBeAfterStartDate() {
        LocalDate start, end;

        start = randomPeriod.getStartDate();
        end = randomPeriod.getEndDate();

        String message = "Check start and end dates assigned in the Period\'s constructor";
        assertTrue(message, start.isBefore(end));

        Period next = randomPeriod.createNextPeriod();
        start = next.getStartDate();
        end = next.getEndDate();

        message = "Check start and end dates assigned during the creation of the next Period object";
        assertTrue(message, start.isBefore(end));

    }




}
