package model;

/**
 * Created by san on 23/07/15.
 */


import main.MainViewController;
import main.model.ModelController;
import main.model.Period;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ModelControllerTest {


    private ModelController createModelController() throws SQLException {
        MainViewController mainViewController = new MainViewController();
        return new ModelController(mainViewController);
    }


    @Test
    public void addingNewPeriodShouldIncreaseNumberOfPeriodsByOne() throws SQLException {
        // given
        ModelController mc = createModelController();

        Period latestPeriod = mc.getNLastPeriods(1).get(0);

        Period period = latestPeriod.createNextPeriod();

        int expectedAfterAdd = mc.getTotalNumberOfPeriods() + 1;

        // when
        mc.addNewPeriod(period);

        // then
        assertEquals(expectedAfterAdd, mc.getTotalNumberOfPeriods());


        // clean up after the testing
        mc.removePeriod(period);
    }

    @Test
    public void removePeriodThatIsNotInDb() throws SQLException {

        ModelController mc  = createModelController();
        Period period = new Period(new LocalDate());

        int nBeforeDel = mc.getTotalNumberOfPeriods();

        // should not throw any exceptions
        mc.removePeriod(period);

        // Number of periods should not change
        assertEquals(nBeforeDel, mc.getTotalNumberOfPeriods());
    }

    @Test
    public void allPeriodsInDbShouldBeUnique() throws SQLException {
        ModelController mc = createModelController();
        List<Period> periods = mc.getAllPeriods();

        // since periods is already sorted we, can just test that there is no neighbours
        // with the same properties
        String msgStartDate = "Found periods with duplicate start dates in the db.";
        String msgEndDate = "Found periods with duplicate end dates in the db.";

        for (int i = 0; i < periods.size() - 1; i++) {
            assertNotEquals(msgStartDate, periods.get(i).getStartDate(), periods.get(i + 1).getStartDate());
            assertNotEquals(msgEndDate, periods.get(i).getEndDate(), periods.get(i + 1).getEndDate());

        }
    }

    @Test
    public void shouldDeleteAndRestorePeriods() throws SQLException {

        ModelController mc = createModelController();
        List<Period> periods = mc.getAllPeriods();

        mc.removePeriods(periods);

        String message = "When all periods are removed the total number of periods should be 0";
        assertEquals(message, 0, mc.getTotalNumberOfPeriods());

        for (Period p: periods) {
            p.setDbRow(-1);
            mc.addNewPeriod(p);
        }
        mc.save();


        message = "When all periods are added, the number of periods should be equal to the number of additions";
        assertEquals(message, periods.size(), mc.getTotalNumberOfPeriods());
    }


    @Test
    public void duplicatePeriodsShouldNotBeAdded() throws SQLException {
        ModelController mc = createModelController();

        Period p = mc.getNLastPeriods(1).get(0);

        int nPeriods = mc.getTotalNumberOfPeriods();

        // the clone, which is not connected to the database
        p = new Period(p);
        mc.addNewPeriod(p);

        mc.save();

        assertEquals(nPeriods, mc.getTotalNumberOfPeriods());

    }

}
