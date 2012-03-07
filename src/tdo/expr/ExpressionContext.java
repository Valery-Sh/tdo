/*
 * ExpressionContext.java
 */
package tdo.expr;

import java.util.Vector;
import tdo.NamedValues;

/**
 * Класс обеспечивает среду для обработки выражений. <p>
 * Необходимость такого класса диктуется следующими обстоятельствами:
 * <ol>
 *   <li>Предоставляет значения и типы именованных идентификаторов.
 *       Это, например, позволяет добавить параметры к операндам.
 *   </li>
 *   <li>
 *     Организовать централизованную обработку ошибок
 *   </li>
 *   <li>
 *     Обеспечивает независимость от таких класссов, как <code>tdo.Table,
 *     tdo.DataRow</code>  и т.д. Это обстоятельство можно использовать, для 
 *     создания произвольного контекста, например, при тестировании с помощью 
 *     JUnit Framework. 
 *   </li>
 *  </ol>
 *  <p>Интерфейс содержит метод createExpression() для создания объекта типа 
 *  {@link tdo.expr.IExpression}. Общий сценарий обработки выражений может быть
 *  следующим:
 * <ul>
 *   <li>Приложение создает экземпляр класса, реализующего интерфейс и назначает
 *       ему строковое значение выражения.
 *   </li>
 *   <li>Приложение применяет метод <code>createExpression()</code> к экземпляру
 *       контекста. Метод компилирует выражение и возвращает результат, как 
 *       объект типа  <code>IExpression</code>  
 *   </li>
 *   <li>Для получения значения выражения приложение выполняет один из методов
 *       <code>getValue</code> полученного выражения.
 *   </li>
 * </ul>
 *  <p><b>Пример 1.</b> Для примера используем реализацию интерфейса
 * {@link tdo.DefaultExpressionContext}.
 * <code>
 *   <pre>
 *      ExpressionContext context = new DefaultExpressionContext("(2/3 + 7)*8");
 *      IExpression expr = context.createExpression();
 *      System.out.println("Пример 1 результат: " + expr.getValue());
 * 
 *   </pre>
 * </code>
 * Результат на консоли:
 * 
 * <pre>Пример 1 результат: 61.333333333333336</pre>
 * 
 *  <p><b>Пример 2.</b> 
 * <code>
 *   <pre>
 *      ExpressionContext context = new DefaultExpressionContext("NumberFormat( (2/3 + 7)*8),'###.## ");
 *      IExpression expr = context.createExpression();
 *      System.out.println("Пример 2 результат: " + expr.getValue());
 * 
 *   </pre>
 * </code>
 * Результат на консоли:
 * 
 * <pre>Пример 2 результат: 61.33</pre>

 * <p> Контекст выражения предоставляет объектам, компилирующим и обрабатывающим
 *   выражения методы, позволяющие определить java-тип значений 
 *   операндов-идентификаторов. Так, для определения типа данных, 
 *   представленных идентификатором может быть использован метод:
 *      <pre> <code>
 * 	Class getIdentifierType(String idName, String alias);
 *      </code></pre>
 *	Информация о типе позволяет правильно строить выражение и на ранней 
 *   стадии выявлять ошибки. На разных этапах обработки исходного выражения 
 *   могуг возникать ошибки, информация о которых накапливается объектом 
 *   контекста с использованием метода <code>addError</code>. После окончания 
 *   обработки, приложение может установить были ли ошибки и проанализировать 
 *   их причину, получив список объектов типа 
 *   {@link tdo.expr.ErrorItem} методом {@link #getErrorList() }.
 * <p>Контекст позволяет использовать в выражении идентификаторы-параметры.
 * 
 *  <p><b>Пример 3.</b> 
 * <code>
 *   <pre>
 * 
 *      ExpressionContext context = new DefaultExpressionContext("NumberFormat( (:divisible/:divisor + 7)*8,'###.00') ");
 *      context.setParameter("divisible",2);
 *      context.setParameter("divisor",3);
 *      IExpression expr = context.createExpression();
 *      System.out.println("Пример 3.1 результат: " + expr.getValue());
 * 
 *      context.setParameter("divisible",7);
 *      context.setParameter("divisor",2);
 *      System.out.println("Пример 3.2 результат: " + expr.getValue());
 * 
 *   </pre>
 * </code>
 * Результат на консоли:
 * 
 * <pre>Пример 3.1 результат: 61.33</pre>
 * <pre>Пример 3.2 результат: 84.00</pre>
 * 
 * <p>Выражение может содержать идентификаторы с предшествующими <p>алиасами</p>.
 * Например, при обработке данных из нескольких таблиц, для однозначной 
 * идентификации имени колонки это имя имеет формат:
 *   <code>
 *    <pre>
 *          <алиас.<имя-колонки>
 *   </pre>
 *   </code>
 * например, " a.firstName = 'Bill' and b.Department = 'other' ". В этом 
 * выражении именам 'firstName' и 'Department' предшествуют алиасы <b>a</b> и
 * <b>b</b> соответственно.
 * <p>контекст позволяет использовать алиасы, предоставляя методы, содержащие
 * значение алиаса в качестве параметров.
 *
 */
public interface ExpressionContext {

    /**
     * Означает недопустимый операндах для операции сравнения
     */
    static final int OPERANDTYPE = 100;
    /**
     * Означает недопустимый операндах для арифметической операции.
     */
    static final int MATH_OPERANDTYPE = 120;
    /**
     * Ошибка парсинга классом ExprParser.
     */
    static final int EXPRPARSER = 140;
    /**
     * Ошибка "неизвестная функция".
     */
    static final int UNKNOWNFUNCTION = 160;
    /**
     * Ошибка в структуре.
     */
    static final int STRUCTURE = 170;
    /**
     * Ошибка арифметической операции.
     */
    static final int MATHEXPRESSION = 180;
    /**
     * Ошибка операции сравнения.
     */
    static final int COMPAREEXPRESSION = 190;
    /**
     * Ошибка выражения функции.
     */
    static final int FUNCTIONEXPRESSION = 200;
    /**
     * Ошибка тестирования.
     */
    static final int TESTING = 210;

    /**
     * Добавляет информацию об ошибке к коллекции ошибок.
     * 
     * @param code значение одной из статических констант, определенных в
     *    интерфейсе.
     * @param expr элемент выражения, при обработке которого произошла ошибка.
     * @see #addError(int, tdo.expr.IExpression, java.lang.String) 
     * @see #getErrorList() 
     * @see tdo.expr.ErrorItem
     */
    void addError(int code, IExpression expr);

    /**
     * Добавляет информацию об ошибке к коллекции ошибок.
     * 
     * @param code значение одной из статических констант, определенных в
     *    интерфейсе.
     * @param expr элемент выражения, при обработке которого произошла ошибка.
     * @param msg текстовое сообщение с дополгнительной информацией
     * @see #addError(int, tdo.expr.IExpression) 
     * @see #getErrorList() 
     * @see tdo.expr.ErrorItem
     */
    void addError(int code, IExpression expr, String msg);

    /**
     * Создает и возвращает объект типа <code>IExpression</code> по 
     * определенному строковому выражению.
     * 
     * @return результат обработки исходного выражения.
     * @see tdo.expr.IExpression
     * @see #getExpressionString() 
     */
    IExpression createExpression();
    /**
     * Возвращает коллекцию объектов, представляющих идентификаторы выражения.
     * В эту коллекцию идентификаторы-параметры не входят.
     * @return коллекция объектов, типа {@link tdo.expr.IdentifierOperand}. 
     * @see #getParameterOperands() 
     */
    Vector getIdentifierOperands();

    /**
     * Возвращает класс идентификатора по заданным имени и алиасу.
     * @param idName 
     * @param alias 
     * @return объект Table или <code>null</code>
     */
    Class getIdentifierType(String idName, String alias);

    Vector getErrorList();

    /**
     * @return Возвращает исходное выражение в строковом формате
     */
    String getExpressionString();
    /**
     * Возвращает коллекцию объектов, представляющих идентификаторы-параметры 
     * выражения.
     * Параметры выражения это идентификаторы с предшествующим символом ':' - 
     * двоеточие.
     * @return коллекция объектов, типа {@link tdo.expr.ParameterOperand}. 
     * @see #getIdentifierOperands() 
     */
    Vector getParameterOperands();
    /**
     * Возвращает класс идентификатора-параметра по заданному имени.
     * @param paramName имя параметра. Может быть указано в любом регистре и не
     * содержит предшествующий символ-двоеточие.
     * @return объект <code>java.lang.Class</code>
     */
    Class getParameterType(String paramName);

    /**
     * Возвращает значение соответствующее заданному имени параметра.
     * @param nvalues 
     * @param paramName имя параметра. Может быть указано в любом регистре и не
     * содержит предшествующий символ-двоеточие.
     * @return значение параметра
     */
    Object getParameterValue(NamedValues nvalues, String paramName);
    /**
     * Возвращает значение соответствующее заданному имени параметра.
     * @param nvalues массив, например, объектов тира <code>tdo.DataRow</code>
     * @param paramName имя параметра. Может быть указано в любом регистре и не
     * содержит предшествующий символ-двоеточие.
     * @return значение параметра
     */
    Object getParameterValue(NamedValues[] nvalues, String paramName);
    /**
     * Возвращает значение соответствующее заданному имени параметра.
     * @param nvalues массив, например, объектов тира <code>tdo.DataRow</code> 
     *   из которого по заданному алиасу выбирается один и возвращается как 
     *   результат метода
     * @param alias алиас идентификатора. Может быть указано в любом регистре.
     * @return коллекцию содержащую значения, доступные по имени
     */
    NamedValues getNamedValues(NamedValues[] nvalues, String alias);
    /**
     * Удаляет параметр и его значение из внутренней коллекции параметров
     * @param paramName имя удаляемого параметра. Не содержит предшествующий 
     * символ-двоеточие.
     */
    void removeParameter(String paramName);

    /**
     * Устанавливает новое значение выражения в строковом формате. <p>
     * @param expr новое значение выражения в строковом формате
     */
    void setExpressionString(String expr);

    /**
     * Добавляет параметр и соответствующее значение к внутренней коллекции 
     * параметров.
     * 
     * @param paramName имя добавляемого параметра. Не содержит предшествующий 
     * символ-двоеточие.
     * @param value значение параметра
     */
    void setParameter(String paramName, Object value);

    /**
     * Печатает информацию об ошибках, возникших при обработке выражения.
     */
    void printErrors();
} //interface ExpressionContext
