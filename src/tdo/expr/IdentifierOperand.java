/*
 * IdentifierOperand.java
 */
package tdo.expr;



import tdo.NamedValues;
import tdo.tools.scanner.ExpressionException;

/**
 * Обеспечивает функциональность операндов, представляемых в выражениях
 * как идентификатор. <p>
 * В выражениях идентификатор имеет формат: <br>
 * <b>[</b><i>alias.</i><b>]</b><i>имя</i>
 * <p>
 *  <i>Алиас</i> используется, если имена операндов-идентификаторов
 * принадлежат разным пространствам имен. Например, операция объединения
 * двух таблиц типа <code>tdo.Table</code> требует указания к какой из
 * двух таблиц принадлежит <i>имя колонки</i> таблицы, для чего используется
 * алиас.
 * <p> Алиас - это идентификатор, т.е. последовательность латинских
 * букв, цифр и знака подчеркивания. Первым символом должна быть буква или знак
 * подчеркивания.
 * <p>Дополнительная информация и примеры  использования алиасов приведены
 * в {@link tdo.DefaultExpressionContext}.
 * 
 */
public class IdentifierOperand extends NamedOperand{
    /**
     * Хранит значение алиаса.
     */
    protected String alias;
    //protected int columnIndex;
    /**
     * Создает экземпляр класса со значение имени равным <code>null</code> и
     * значением алиаса равным <code>null</code>.
     */
    public IdentifierOperand() {
        this(null,(String)null);
    }
    /**
     * Создает экземпляр класса с заданным именем и значением алиаса равным
     * <code>null</code>.
     */
    public IdentifierOperand(String columnName) {
        this(columnName,(String)null);
    }
    /**
     * Создает экземпляр класса с заданным именем, значением алиаса равным
     * <code>null</code> и заданным контекстом выражения.
     */
    public IdentifierOperand(String columnName,ExpressionContext context) {
        this(columnName,(String)null);
        this.setContext(context);
    }
    /**
     * Создает экземпляр класса с заданным именем и заданным значением алиаса.
     */
    public IdentifierOperand(String columnName, String alias) {
        //super(columnName,alias);
        super(columnName);
        this.alias = alias;
        //columnIndex = -1;
    }
    /**
     * Возвращает значение выражения.
     * Делегирует выполнение методу {@link #getValue(NamedValues)} со значением
     * параметра равным <code>null</code>.
     * @return значение выражения
     */
    @Override
    public Object getValue() {
        NamedValues row = null;
        return this.getValue(row);
    }
    /**
     * Возвращает значение выражения для заданной коллекции именнованных значений.
     *
     * @param values коллекция именованных значений.
     * @return значение выражения
     * @throws ExpressionException если коллекция <code>values</code> не
     *  содержит элемента с именем, полученным {@link #getName() }
     * @see #getValue(tdo.NamedValues[])
     * @see #getValue()
     */
    @Override
    public Object getValue(NamedValues values) {
        Object value = null;
        try {
            value = values.getValue(getName());
        } catch(Exception e) {
            throw new ExpressionException("IdentifierOperand.getValue(NamedValues) : identifier {" + getName() + "} doesn't exist" );
        }
        return value;
    }    
    /**
     * Возвращает значение выражения для заданного массива коллекций
     * именнованных значений.
     *
     * @param values массив коллекций именованных значений.
     * @return значение выражения
     * @throws ExpressionException если коллекция <code>values</code> не
     *  содержит элемента с именем, полученным {@link #getName() }
     * @see #getValue(tdo.NamedValues)
     * @see #getValue()
     */
    @Override
    public Object getValue(NamedValues[] values) {
        NamedValues r = context.getNamedValues(values,getAlias());
        Object value = null;
        try {
            value = r.getValue(getName());            
        } catch(Exception e) {
            throw new ExpressionException("IdentifierOperand.getValue(NamedValues[]) : identifier {" + getName() + "} doesn't exist" );
        }
        return value;
    }
    /**
     * Возвращает класс значений, соответствующих идентификатору.
     * Класс значений метод получает вызовом метода
     * <code>tdo.expr.ExpressionContext.getIdentifierType</code>,
     * передавая ему имя и алиас в качестве параметров. Если метод контекста
     * вернет значение <code>null</code>, то возвращается
     * <code>IdentifierOperand.class</code>, такое возможно для контекста,
     * являющегося объектом типа <code>tdo.DefaultExpressionContext</code>,
     * находящегося в моде тестирования.
     * @return <code>IdentifierOperand.class</code> или класс значений,
     *   соответствующих идентификатору.
     * @see tdo.expr.ExpressionContext#getIdentifierType(java.lang.String, java.lang.String)
     * @see tdo.DefaultExpressionContext
     */
    @Override
    public Class getType() {
       Class c = context.getIdentifierType(getName(),getAlias() );
       return c == null ? IdentifierOperand.class : c;
    }
    /**
     * @return значение -1, поскольку значение большее или равное нулю,
     *     определено для операторов, но не для операндов
     */
    @Override
    public int getPriority() {
        return -1;
    }
    /**
     * Возвращает алиас идентификатора.
     * @return алиас идентификатора
     * @see #setAlias(java.lang.String)
     */
    public String getAlias() {
        return alias;
    }
    /**
     * Устанавливает значение алиаса идентификатора.
     * @see #getAlias()
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }
    /**
     * Возвращает строку знаков, представляющую идентификатор.
     * @return значение <code>getName()</code>, если значение
     * алиаса равно <code>null</code>, или
     *  <code>getAlias() + "." + getName() в противном случае
     */
    @Override
    public String toString() {
        String s = "";
        if ( alias != null )
            s = alias + ".";
        s += this.getName();
        return s;
    }

}// class IdentifierOperand
