/*
 * ParameterOperand.java
 */

package tdo.expr;

import tdo.NamedValues;
/**
 * Обеспечивает функциональность операндов, представляемых в выражениях
 * как параметры. <p>
 * В выражениях идентификатор имеет формат: <br>
 * <b><i>:имя</i></b>
 * <p>Дополнительная информация и примеры  использования параметров приведены
 * в {@link tdo.DefaultExpressionContext}.
 *
 */
public class ParameterOperand extends NamedOperand{
    /**
     * Создает экземпляр класса со значением имени параметра равным
     * <code>null</code>.
     */
    public ParameterOperand() {
        this(null);
    }
    /**
     * Создает экземпляр класса с заданным значение имени параметра.
     * @param paramName имя параметра, без предшествующего символа
     * двоеточия.
     */
    public ParameterOperand(String paramName) {
        super(paramName);
    }
    /**
     * Возвращает значение выражения.
     * Делегирует выполнение методу {@link #getValue(NamedValues)} со значением
     * параметра равным <code>null</code>.
     * @return значение выражения
     */
    @Override
    public Object getValue() {
        NamedValues v = null;
        return this.getValue(v);
    }
    /**
     * Возвращает значение соответствующее параметру для заданной коллекции
     * именованных значений.<p>
     * Делегирует исполнение методу контекста выражения
     * {@link tdo.expr.ExpressionContext#getParameterValue(tdo.NamedValues, java.lang.String) }
     * @param values коллекция именованных значений, например, объект
     *   типа <code>tdo.DataRow</code>
     * @return значение выражения
     * @see tdo.DataRow
     * @see #getValue(tdo.NamedValues[])
     */
    @Override
    public Object getValue(NamedValues values) {
        return context.getParameterValue(values, getName());
    }
    /**
     * Возвращает значение соответствующее параметру для заданного массива
     * коллекций именованных значений.<p>
     * Делегирует исполнение методу контекста выражения
     * {@link tdo.expr.ExpressionContext#getParameterValue(tdo.NamedValues, java.lang.String) }
     * @param values массив коллекций именованных значений, например, массив объектов
     *   типа <code>tdo.DataRow</code>
     * @return значение выражения
     * @see tdo.DataRow
     * @see #getValue(tdo.NamedValues)
     */
    @Override
    public Object getValue(NamedValues[] values) {
        return context.getParameterValue(values, getName());
    }
    /**
     * Возвращает класс значений, соответствующих параметру.
     * Класс значений метод получает вызовом метода
     * <code>tdo.expr.ExpressionContext.getParameterType</code>,
     * передавая ему имя параметра. Если метод контекста
     * вернет значение <code>null</code>, то возвращается
     * <code>ParameterOperand.class</code>, такое возможно для контекста,
     * являющегося объектом типа <code>tdo.DefaultExpressionContext</code>,
     * находящегося в моде тестирования.
     * @return <code>ParameterOperand.class</code> или класс значений,
     *   соответствующих параметру.
     * @see tdo.expr.ExpressionContext#getParameterType(java.lang.String)
     * @see tdo.DefaultExpressionContext
     */
    @Override
    public Class getType() {
       Class c = context.getParameterType(getName());
       return c == null ? ParameterOperand.class : c;
    }
    /**
     * Возвращает строку знаков, представляющую параметр.
     * @return значение ":" + <code>getName()</code>
     */
    @Override
    public String toString() {
        return ":" + getName();
    }
    /**
     * @return значение -1, поскольку значение большее или равное нулю,
     *     определено для операторов, но не для операндов
     */
    @Override
    public int getPriority() {
        return -1;
    }

}//class ParameterOperand
