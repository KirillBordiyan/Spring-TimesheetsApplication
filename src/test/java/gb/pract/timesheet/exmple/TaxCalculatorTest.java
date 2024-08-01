package gb.pract.timesheet.exmple;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //через рефлекшн внедрение
class TaxCalculatorTest {


    @Mock
    TaxResolver mock;
    /**
     * Вот этот тест для метода класса TaxCalculator
     * учитываем, что он не знает о реализации резолвера, мы его мокаем
     *
     * Второй варик создать мок - вынести такие "классы-подмены" как переменные класса
     * и добавить ЗДЕСЬ аннотацию @ExtendWith(MockitoExtension.class)
     *
     */

    @Test
    void testGetPriceWithTax(){
//        TaxResolver mock = Mockito.mock(TaxResolver.class); //мы сделали другой экземпляр резолвера через мок
//        when(mock.getCurrentTax()).thenReturn(0.2);
        //или
        doReturn(0.2).when(mock).getCurrentTax(); //так получше, если мокается void метод, у вар-та выше мы не смогли бы вызвать что-то там, где вернется void
        //когда у МОКА вызовется метод getCurrentTax() - этот метод вернет 0.2
        // это делаем, чтобы тест был контролируемым;
        // а что если поменяется в оригинале? а не важно, у нас тут заготовка своя


        TaxCalculator taxCalculator = new TaxCalculator(mock);
        assertEquals(120.0, taxCalculator.getPriceWithTax(100.0));

        // чтобы убедится, что метод X у мока mock был точно вызван
        verify(mock).getCurrentTax();


    }

}