package gb.pract.aspect.recover;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Recover {

    /**
     * Представляет собой список классов-классов, которые обрабатывать не надо
     * @return список классов в виде массива
     */
    Class<?>[] noRecovered() default {};

    /**
     * Параметр, определяющий, будут ли указанные в аннотации классы замещать те,
     * что указаны в настойках в месте использования.
     * <p>
     * Если параметр true, то будут проверяться только те классы, которые указаны непосредственно
     * в месте объявления аннотации.<p>
     * По умолчанию параметр выключен, т.е. списки будут объединяться
     * @return true - чтобы не учитывать список классов в конфигурации
     * <p> false - чтобы объединить оба списка
     */
    boolean replaceDefaultList() default false;
}
