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

import static org.junit.Assert.assertEquals;

public class ModelControllerTest {

    @Test
    public void addingNewPeriodShouldIncreaseNumberOfPeriodsByOne() throws SQLException {
        // given
        MainViewController mainViewController = new MainViewController();
        ModelController mc = new ModelController(mainViewController);

        Period period = new Period(new LocalDate());

        int expectedAfterAdd = mc.getTotalNumberOfPeriods() + 1;

        // when

        mc.addNewPeriod(period);


        // then
        assertEquals(expectedAfterAdd, mc.getTotalNumberOfPeriods());


        // clean up after the testing
    }


}
