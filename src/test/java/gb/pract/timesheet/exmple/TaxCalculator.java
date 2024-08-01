package gb.pract.timesheet.exmple;

public class TaxCalculator {

    /**
     * В данном случае мы напишем тест для этого класса
     * НО
     * важно, мы понимаем, что этот класс не должен зависеть от реализации TaxResolver'а
     * то есть, нам надо его подменить как-то - замокать
     *
     */

    private final TaxResolver resolver;

    public TaxCalculator(TaxResolver resolver) {
        this.resolver = resolver;
    }

    public double getPriceWithTax(double price){
        double tax = resolver.getCurrentTax();
        return price * (1 + tax);
    }
}
