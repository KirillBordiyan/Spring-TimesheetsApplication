package gb.pract.timesheet.exmple;


import gb.pract.timesheet.model.Timesheet;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CalcTest {

    @Test
    public void testSum(){
        Calc calc = new Calc();
        int actual = calc.sum(2,3); //то, что должен сделать метод
        int exp = 5; //заранее заготовленный ответ

        assertEquals(exp, actual);
        //27 минута
    }


    @Test
    public void createTimesheet(){
        Timesheet ts = new Timesheet();
        ts.setMinutes(100);
        ts.setProjectId(1L);
        ts.setEmployeeId(1L);


        Timesheet ts2 = new Timesheet();
        ts2.setId(1L);
        ts.setMinutes(100);
        ts.setProjectId(1L);
        ts.setEmployeeId(1L);
        ts.setCreatedAt(LocalDate.now());

        assertTrue(Objects.equals(ts2.toString(), ts.toString()));
    }

}
