/*
 * LiteralOperand.java
 */

package tdo.expr;

import tdo.NamedValues;

/**
 * Класс предназначен для представления объектов, являющихся операндами
 * выражений и таких, что значение операнда хранится в в нем самом.<p>
 * Парсер исходного выражения распознает литеральные операнды типов:
 * <code>Boolean,Float,Double,Integer,Long,String</code>. <p>
 * Когда в исходном выражении встречается литерал, например, число или строка
 * знаков, то компилятор выражения создает для него объект данного класса, а
 * значение сохраняет во внутреннем поле <code>Object value</code>. Тип операнда,
 * возвращаемый методом getType определяется  значением операнда. <p>
 * Для создания литерального операнда применяется один из двух конструкторов:
 * первый принимает один параметр <code><i>value</i></code>. Если значение
 * параметра (а следовательго значение операнда)  равно <code>null</code>, то
 * тип операнда также <ode>null</code>, т.е. метод getType возвратит для такого
 * операнда <code>null</code>. 	Второй конструктор применяется для явного
 * задания типа. Тип устанавливается равным  значению параметра
 * <i><code>clazz</code></i>, независимо от параметра <i><code>value</code></i>.
 * Значение value параметра сохраняется во внутреннем поле  объекта класса.
 * Все методы getValue класса возвращают значение из внутреннего поля
 * <code>value</code>, установленной конструктором при создании объекта.
 *
 */
public class LiteralOperand implements IExpression {
    /**
     * For print 
     */
    public int lexType;
    
    private Object value;
    private Class  type;
    private ExpressionContext context;
    
/*    public LiteralOperand() {
        this(null);
    }
 */
    /**
     * Создает новый экземпляр класса для заданного значения.
     * Устанавливает значение свойства <code>type</code>, используя
     * <code>value.getClass()</code> вызов. Однако, если значение параметра
     * <code>value</code> равно <code>null</code>, то и значение свойства
     * <code>type</code> также устанавливается в <code>null</code>.
     *
     * @param value значение литерала, для которого создается объект
     */
    public LiteralOperand(Object value) {
        //this(Object.class,value);
        //this.type = value == null ? Object.class : value.getClass();
        this.type = value == null ? null : value.getClass();        
        this.value = value;
    }
    /**
     * Создает новый экземпляр класса для заданного класса и значения.
     * Устанавливает значение свойства <code>type</code>, используя параметр
     * <code><i>clazz</i></code>.
     *
     * @param clazz устанавливаемый тип операнда
     * @param value значение литерала, для которого создается объект
     */
    public LiteralOperand(Class clazz,Object value) {
        this.type = clazz;
        this.value = value;
    }

/*    
    public Object getValue(int rowIndex) {
        return value;
    }
 */
    /**
     * Возвращает значение операнда.
     * Поскольку значение операнда, хранится в экземпляре литерального операнда,
     * то параметр не имеет значения и может быть любым, в том числе и
     * <code>null</code>
     *
     * @param row коллекция именованных элементов
     * @return значение литерального операнда
     * @see #getValue(tdo.NamedValues[])
     * @see #getValue()
     */
    @Override
    public Object getValue(NamedValues row) {
        return value;
    }
    /**
     * Возвращает значение операнда.
     * Поскольку значение операнда, хранится в экземпляре литерального операнда,
     * то параметр не имеет значения и может быть любым, в том числе и
     * <code>null</code>
     *
     * @param row массив, элементами которого являются коллекции именованных
     *        элементов
     * @return значение литерального операнда
     * @see #getValue(tdo.NamedValues)
     * @see #getValue()
     */

    @Override
    public Object getValue(NamedValues[] row) {
        return value;
    }
    /**
     * Возвращает значение операнда.
     * Поскольку значение операнда, хранится в экземпляре литерального операнда,
     * то параметр не имеет значения и может быть любым, в том числе и
     * <code>null</code>
     *
     * @param row массив, элементами которого являются коллекции именованных
     *        элементов
     * @return значение литерального операнда
     * @see #getValue(tdo.NamedValues)
     * @see #getValue(tdo.NamedValues[])
     */
    @Override
    public Object getValue() {
        return value;
    }
    /**
     * Устанавливает новое значение операнда.
     *
     * @param value новое значение
     *
     * @see #getValue(tdo.NamedValues[])
     * @see #getValue()
     * @see #getValue(tdo.NamedValues[])
     */
    
    public void setValue(Object value) {
        this.value = value;
    }
    /**
     * Возвращает тип значений обрабатываемый объектом, представляющим
     * литеральный операнд.
     * @return тип значений
     */
    @Override
    public Class getType() {
        return this.type;
    }
    /**
     * Возвращает приоритет операнда в выражении.
     * Значение >= 0 имеет смысл только для операторов. Здесь всегда -1.
     * @return значение равное -1.
     */
    @Override
    public int getPriority() {
        return -1;
    }
    
    @Override
    public String toString() {
        String s = "";
        if ( value != null && value instanceof String  ) {
            s += "'" + value + "'";
        } else {
            s = "" + value;
        }
        return s;
    }

    
    
    /**
     * Устанавливает контекст, в котором обрабатывается операнд.
     * @param context ссылка на контекст
     */
    @Override
    public void setContext(ExpressionContext context) {
        this.context = context;
    }
}
