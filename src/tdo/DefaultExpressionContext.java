package tdo;

import java.util.*;
import tdo.expr.*;
import tdo.service.TableServices;
import tdo.tools.scanner.ExpressionException;
//import tdo.tools.scanner.ExpressionException;

/**
 * Реализация контекста выражений, используемых с объектами 
 * <code>tdo.DataTable</code>. 
 * <p>
 * Предоставляет методы для создания, обработки, проверки и выполнения выражений,
 * опрерандами которых могут быть литералы, имена колонок таблицы, параметры и 
 * функции.
 * <p>
 *  <p>Метод createExpression() служит для создания объекта типа 
 *  {@link tdo.expr.IExpression}. Общий сценарий обработки выражений может быть
 *  следующим:
 * <ul>
 *   <li>Приложение создает экземпляр класса и назначает ему строковое значение 
 *       выражения.
 *   </li>
 *   <li>Приложение применяет метод <code>createExpression()</code> к экземпляру
 *       контекста. Метод компилирует выражение и возвращает результат, как 
 *       объект типа  <code>IExpression</code>  
 *   </li>
 *   <li>Для получения значения выражения приложение выполняет один из методов
 *       <code>getValue</code> полученного выражения.
 *   </li>
 * </ul>
 * <p>Класс может быть использован без назначения ему таблиц 
 * <code>tdo.Table</code>, например:
 * 
 *  <p><b>Пример 1.</b> 
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
 * <p>При работе с таблицами <code>tdo.Table</code> может обрабатываться одна
 * или несколько таблиц. Чтобы указать используемую таблицу, необходимо выполнить
 * метод {@link #addTable(tdo.Table)} или {@link #addTable(String,tdo.Table)}.
 * 
 *  <p><b>Пример 2.</b> 
 * <code>
 *   <pre>
 *      DataTable dt = new DataTable();
 *      dt.getColumns().add(String.class,"firstName");
 *      dt.getColumns().add(Integer.class,"age");
 *      dt.getColumns().add(Double.class,"salary");
 *      dt.addRow(new Object[] {"Tom", 30, 2000});
 *      dt.addRow(new Object[] {"Bill", 25, 3200});
 *      DefaultExpressionContext context = new DefaultExpressionContext("salary * 12");
 *      context.addTable(dt);
 * 
 *      IExpression expr = context.createExpression();
 *      System.out.println("Пример 2.1 результат: " + expr.getValue(dt.getRow(0)));
 *      System.out.println("Пример 2.2 результат: " + expr.getValue(dt.getRow(1)));
 * 
 *   </pre>
 * </code>
 * Результат на консоли:
 * 
 * <pre>
 * Пример 2.1 результат: 24000.0
 * Пример 2.2 результат: 38400.0
 * </pre>
 * 
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
 * <p>Выражение может содержать идентификаторы с предшествующими <i>алиасами</i>.
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
 * <p>Контекст позволяет использовать алиасы, предоставляя методы, содержащие
 * значение алиаса в качестве параметров.
 * <p>Рассмотрим следующий пример:
 *  Есть две таблицы:
 * <ol>
 *   <li>Таблица, содержащая сведения о сотрудниках и их зарплате в долларах;</li>
 *   <li>Таблица, содержащая сведения о курсах валют различных стран по
 *       отношению к доллару.</li>
 * </ol>
 *  Требуется выбрать произвольный ряд из первой таблицы и любой ряд из второй
 * и посчитать зарплату сотрудника в соответствующей валюте.
 * <code><pre> 
 *     // ***** СОТРУДНИКИ *****
 *     DataTable dt = new DataTable();
 *     dt.getColumns().add(String.class,"Name");
 *     dt.getColumns().add(Integer.class,"age");
 *     dt.getColumns().add(Double.class,"salary");
 *     dt.addRow(new Object[] {"Tom", 30, 2000});
 *     dt.addRow(new Object[] {"Bill", 25, 3200});
 * 
 *     // ***** КУРСЫ ВАЛЮТ *****
 *     DataTable dt1 = new DataTable();
 *     dt1.getColumns().add(String.class,"Country");
 *     dt1.getColumns().add(Double.class,"DollarRate");
 *     dt1.addRow(new Object[] {"JPN", 1000});
 *     dt1.addRow(new Object[] {"RUS", 23.54});
 * 
 *     DefaultExpressionContext context = new DefaultExpressionContext(
 *             "c.salary * r.dollarRate");
 *     // Добавляем в контекст таблицу сотрудников с алиасом 'c'. 
 *     context.addTable("c",dt);
 *     // Добавляем в контекст таблицу курсов валют с алиасом 'r'. 
 *     context.addTable("r",dt1);
 *
 *     IExpression expr = context.createExpression();
 *     // Чтобы посчитать выражение для двух таблиц, необходимо выполнить для 
 *     // выражения expr метод с параметром, являющимся массивом рядов
 *     DataRow[] rows = new DataRow[2];
 *     rows[0] = dt.getRow(0);  // Выбираем 0-й ряд из табл. сотрудников
 *     rows[1] = dt1.getRow(0); // Выбираем 0-й ряд из табл. курсов валют
 *     
 *     System.out.println("Пример 3.1 результат: " + expr.getValue(rows) );
 *     
 *     rows[0] = dt.getRow(0); // Выбираем 0-й ряд из табл. сотрудников
 *     rows[1] = dt1.getRow(1);// Выбираем 1-й ряд из табл. курсов валют
 * 
 *     System.out.println("Пример 3.2 результат: " + expr.getValue(rows) );
 * </pre></code>
 * На консоди:
 * <pre> 
 * Пример 3.1 результат: 2000000.0
 * Пример 3.2 результат: 47080.0
 * </pre>
 * 
 * <p>Класс может быть создан или переведен в моду тестирования. Для этого 
 * служит свойство <code>testMode</code>. Если значение свойства установлено в 
 * <code>true</code> то методы <code>getIdentifierType</code> и 
 * <code>getParameterType</code> не выбрасывают исключения, а 
 * возвращают <code>null</code> значение, тем самым позволяя обрабатывать 
 * выражения без проверки типов колонок. Разумеется, если метод 
 * <code>getTable</code> возвращает <code>null</code>, то вычислять значения
 * выражения не возможно. Класс предоставляет методы доступа к свойству: 
 * {@link #isTestMode() } и {@link #setTestMode(boolean) }. Кроме того, 
 * предоставляются конструкторы для создания контекста в заданной моде.
 *  
 * 
 */
public class DefaultExpressionContext implements ExpressionContext {
    protected NullValue nullValue = new NullValue();
    private boolean testMode;
    //private Table table;
//    private Hashtable tables;
    private Hashtable params;
    private Map<String,TableServices> tableServices;

    /**
     * Строка исходного выражения
     */
    private String expressionString;
    /**
     * Список ошибок, содержащий элементы {@link ErrorItem}.
     */
    private Vector errorList;
    /**
     * Если для добавления таблицы к контексту использован метод 
     * {@link #addTable(tdo.Table) } без явного указания алиаса, то будет 
     * использовано значение "__DA__"
     */
    protected String defaultAlias = "__DA__";
    private Vector columnOperands;
    private Vector parameterOperands;

    /**
     * Создает экземпляр класса со значением строкового выражения
     * равным <code>null</code>.
     */
    public DefaultExpressionContext() {
        this(null);
    }

    /**
     * Создает экземпляр класса со значением строкового выражения
     * равным <code>null</code> и переводит его в режим тестирования.
     * @param testMode true - включает моду тестирования
     */
    public DefaultExpressionContext(boolean testMode) {
        this(null);
        this.testMode = testMode;
    }

    /**
     * Создает экземпляр класса для заданного строкового выражения.
     * @param expr устанавливаемое значение свойства {@link #expressionString}
     */
    public DefaultExpressionContext(String expr) {
        this.expressionString = expr;
        errorList = new Vector(2);
        columnOperands = new Vector(5);
        parameterOperands = new Vector(5);

        //tables = new Hashtable(2);
        tableServices = new HashMap<String,TableServices>(2);
        params = new Hashtable(5);
        this.testMode = false;
    }

    /**
     * Создает экземпляр класса для заданного строкового выражения и переводит
     * его в режим тестирования.
     * @param expr устанавливаемое значение свойства {@link #expressionString}
     * @param testMode 
     */
    public DefaultExpressionContext(String expr, boolean testMode) {
        this.expressionString = expr;
        errorList = new Vector(2);
        columnOperands = new Vector(5);
        parameterOperands = new Vector(5);

        //tables = new Hashtable(2);
        tableServices = new HashMap<String,TableServices>(2);
        params = new Hashtable(5);
        this.testMode = testMode;
    }

    /**
     * Находит в коллекции таблиц контекста и возвращает таблицу с алиасом 
     * равным "__DA__".
     * 
     * Если коллекция не содержит такой таблицы 
     * то возвращается <code>null</code>.
     * 
     * @return объект Table или <code>null</code>
     * @throws NullPointerException если таблица с алиасом равным значению по 
     *   умолчанию ("__DA__") не найдена и отключен режим тестирования.
     * 
     * @see #isTestMode() 
     * @see #setTestMode
     */
/*    public Table getTable() {
        return getTable(this.defaultAlias);
    }
*/
    public TableServices getTableServices() {
        return getTableServices(this.defaultAlias);
    }
    /**
    /**
     * Находит и возвращает таблицу с заданным алиасом равным.
     * 
     * Если коллекция не содержит такой таблицы то возвращается <code>null</code>.
     * 
     * @param alias алиас искомой таблицы
     * @return объект Table или <code>null</code>
     * @throws NullPointerException если для заданного алиаса таблица не найдена
     *    и отключен режим тестирования.
     * @see #isTestMode() 
     * @see #setTestMode
     */
/*    public Table getTable(String alias) {
        String a = alias == null ? defaultAlias : alias.toUpperCase();
        return (Table) tables.get(a);
    }
*/
    /**
     * Находит и возвращает таблицу с заданным алиасом равным.
     *
     * Если коллекция не содержит такой таблицы то возвращается <code>null</code>.
     *
     * @param alias алиас искомой таблицы
     * @return объект Table или <code>null</code>
     * @throws NullPointerException если для заданного алиаса таблица не найдена
     *    и отключен режим тестирования.
     * @see #isTestMode()
     * @see #setTestMode
     */
    public TableServices getTableServices(String alias) {
        String a = alias == null ? defaultAlias : alias.toUpperCase();
        return tableServices.get(a);
    }

    /**
     * Возвращает тип колонки для заданного имени колонки и алиаса таблицы.
     * 
     * Если коллекция не содержит заданной алиасом таблицы и режим тестирования 
     * отключен, то выбрасывается исключение. <p>
     * 
     * Если коллекция не содержит заданной алиасом таблицы и режим тестирования 
     * включен, то возвращается <code>null</code> значение. <p>
     * 
     * @param columnName имя колонки, соответствующее свойству <code>name</code>
     *      объекта <cpde>tdo.DataColumn</code>.
     * @param alias алиас таблицы, к которой принадлежит колонка
     * @return тип колонки
     * @see tdo.DataColumn
     * @see #getParameterType(java.lang.String) 
     */
    @Override
    public Class getIdentifierType(String columnName, String alias) {
        TableServices srv = getTableServices(alias);
        if (srv == null && !isTestMode()) {
            throw new NullPointerException("A table with default alias='" + alias + "' is not found");
        } else if (srv == null) {
            return null;
        }
        return srv.getFilterServices().getColumnType(columnName);

    }

/*    public Class getIdentifierType(String columnName, String alias) {
        Table dt = getTable(alias);
        if (dt == null && !isTestMode()) {
            throw new NullPointerException("A table with default alias='" + alias + "' is not found");
        } else if (dt == null) {
            return null;
        }
        return dt.getColumns().get(columnName).getType();
        
    }
*/
    /**
     * Возвращает ряд таблицы по заданному индексу.
     * @param rowIndex индекс ряда
     * @return объект типа <code>tdo.DataRow</code>.
     * @see tdo.NamedValues
     * @see tdo.DataRow
     */
    public NamedValues getRow(int rowIndex) {
        return this.getTableServices().getFilterServices().getRow(rowIndex);
    }

    /**
     * Для заданного ряда и алиаса таблицы по умолчанию возврашает его индекс 
     * в таблице.
     *
     * Метод введен специально для реализации операторов-функций, позволяющих,
     * например, возвращать индекс ряда.
     * 
     * @param row ряд, для которого определяется индекс в таблице
     * @return индекс искомого ряда или значение равное -1, если ряд не найден.
     */
    public int getRowIndex(NamedValues row) {
        //return this.getTable().find((DataRow) row);
        return ((DataRow) row).getIndex();
    }

    /**
     * Для заданного ряда и алиаса таблицы возврашает индекс ряда в таблице.
     * Метод введен специально для реализации операторов-функций, позволяющих,
     * например, возвращать индекс ряда.
     *
     * @param row ряд, для которого определяется индекс в таблице
     * @param alias алиас таблицы, в которой находится ряд
     * @return индекс искомого ряда или значение равное -1, если ряд не найден.
     */
    public int getRowIndex(NamedValues row, String alias) {
        //return this.getTable(alias).find((DataRow) row);
        return ((DataRow) row).getIndex();
    }

    /**
     * Возвращает ряд таблицы, заданной алиасом и находящийся в заданном массиве.
     * Просматривается массив заданных рядов и отыскивается ряд, принадлежащий 
     * таблице с заданным алиасом. Если значение параметра <code>alias</code>
     * равно <code>null</code>, то для поиска используется алиас по умолчанию.
     * Если таблица не найдена, то выбрасывается исключение.
     * 
     * @param rows массивов рядов, содержащий искомый ряд.
     * @param alias алиас таблицы, ряд которой определяется.
     * @return искомый ряд или <code>null</code>, если ряд не найден
     * @throws NullPointerException если не найдена таблица с заданным алиасом,
     *   или с алиасом по умолчанию.
     */
    @Override
/*    public NamedValues getNamedValues(NamedValues[] rows, String alias) {
        //String a = alias == null ? null : alias.toUpperCase();
        Table dt = (alias == null) ? getTable() : getTable(alias);
        if (dt == null) {
            dt = getTable();
        }
        if (dt == null) {
            throw new NullPointerException("A table with alias=" + alias + " is not found");
        }
        NamedValues r = null;
        for (int i = 0; i < rows.length; i++) {
            if (((DataRow) rows[i]).getContext().getCoreServices().getTable() == dt) {
                r = rows[i];
                break;
            }
        }
        return r;
    }
*/
    public NamedValues getNamedValues(NamedValues[] rows, String alias) {
        //String a = alias == null ? null : alias.toUpperCase();
        TableServices srv = (alias == null) ? getTableServices() : getTableServices(alias);
        if (srv == null) {
            srv = getTableServices();
        }
        if (srv == null) {
            throw new NullPointerException("A table with alias=" + alias + " is not found");
        }
        NamedValues r = null;
        for (int i = 0; i < rows.length; i++) {
            if (((DataRow) rows[i]).getContext() == srv) {
                r = rows[i];
                break;
            }
        }
        return r;
    }

    /**
     * Возвращает значение для заданного имени параметра.
     *  <p>Если имя параметра равно <code>"PI"</code>, то возвращается значение 
     * константы <code>java.math.PI</code>.
     *  <p>Если имя параметра равно <code>"E"</code>, то возвращается значение 
     * константы <code>java.math.E</code>.
     * 
     * @param row ряд таблицы. Не используется
     * @param paramName имя параметра (бе предшествующего двоеточия). Значение 
     *   может быть задано в любом регистре
     * @return Значение параметра
     */
    @Override
    public Object getParameterValue(NamedValues row, String paramName) {
        return getParamValueCheckConst(row, paramName);
    }

    private Object getParamValueCheckConst(NamedValues row, String paramName) {
        if (paramName.equals("PI")) {
            return Math.PI;
        }
        if (paramName.equals("E")) {
            return Math.E;
        }
        Object pv = params.get(paramName.toUpperCase());
        if ( pv instanceof NullValue )
            return null;
        else
            return pv;
    }

    private Object getParamValueCheckConst(NamedValues[] row, String paramName) {
        if (paramName.equals("PI")) {
            return Math.PI;
        }
        if (paramName.equals("E")) {
            return Math.E;
        }
        Object pv = params.get(paramName.toUpperCase());
        if ( pv instanceof NullValue )
            return null;
        else
            return pv;

    }

    /**
     * Возвращает значение для заданного имени параметра.
     *  <p>Если имя параметра равно <code>"PI"</code>, то возвращается значение 
     * константы <code>java.math.PI</code>.
     *  <p>Если имя параметра равно <code>"E"</code>, то возвращается значение 
     * константы <code>java.math.E</code>.
     * 
     * @param rows массив рядов таблицы. Не используется
     * @param paramName имя параметра (без предшествующего двоеточия). Значение 
     *   может быть задано в любом регистре
     * @return Значение параметра
     */
    @Override
    public Object getParameterValue(NamedValues[] rows, String paramName) {
        return getParamValueCheckConst(rows, paramName);
    }

    /**
     * Возвращает тип значения параметра по его имени.
     * 
     * Если параметр с заданным именем не существует и режим тестирования 
     * отключен, то выбрасывается исключение. <p>
     * 
     * Если параметр с заданным именем не существует и режим тестирования 
     * включен, то возвращается <code>null</code>. <p>

     * @param paramName имя параметра (без предшествующего двоеточия)
     * @return тип значения параметра 
     */
    @Override
    public Class getParameterType(String paramName) {
        Object pv = params.get(paramName.toUpperCase());
        if (pv == null && !isTestMode()) {
            throw new NullPointerException("Parameter " + paramName + " is not found");
        } else if (pv == null) {
            return null;
        } else {
            return pv.getClass();
        }

    }

    /**
     * Добавляет параметр с заданным именем  и значением к контексту.
     * @param paramName имя параметра (без предшествующего двоеточия). Может быть
     *    задан в любом регистре
     * @param value значение нового параметра
     */
    @Override
    public void setParameter(String paramName, Object value) {
        if ( value == null )
            params.put(paramName.toUpperCase(), nullValue);
        else
            params.put(paramName.toUpperCase(), value);
    }

    /**
     * Удаляет параметр с заданным именем  из контекста.
     * @param paramName имя параметра (без предшествующего двоеточия). Может быть
     *    задан в любом регистре
     */
    @Override
    public void removeParameter(String paramName) {
        params.remove(paramName.toUpperCase());
    }

    /**
     * Добавляет в коллекцию таблиц контекста таблицу с заданным 
     * алиасом.
     * 
     * @param alias алиас добавляемой таблицы. Параметр не чувсвителен к регистру.
     *    Если равен <code>null</code>, то выбрасывается исключение
     * @param table добавляемая таблица
     * @throws IllegalArgumentException если параметр алиас равен 
     *   <code>null</code> или содержит пустое значение или только пробелы.
     */
/*    public void addTable1(String alias, Table table) {
        if (alias == null || alias.trim().length() == 0) {
            throw new IllegalArgumentException("Parameter 'alias' must have a value");
        }
        this.tables.put(alias.toUpperCase(), table);
    }
*/
    /**
     * Добавляет в коллекцию таблиц контекста таблицу с заданным
     * алиасом.
     *
     * @param alias алиас добавляемой таблицы. Параметр не чувсвителен к регистру.
     *    Если равен <code>null</code>, то выбрасывается исключение
     * @param table добавляемая таблица
     * @throws IllegalArgumentException если параметр алиас равен
     *   <code>null</code> или содержит пустое значение или только пробелы.
     */
    public void addTableServices(String alias, TableServices tableServices) {
        if (alias == null || alias.trim().length() == 0) {
            throw new IllegalArgumentException("Parameter 'alias' must have a value");
        }
        this.tableServices.put(alias.toUpperCase(), tableServices);
    }

    /**
     * Добавляет в коллекцию таблиц контекста заданную таблицу с 
     * алиасом по умолчанию "__DA__".
     * 
     * @param table добавляемая таблица
     * @see tdo.Table
     * @see #defaultAlias
     */
/*    public void addTable(Table table) {
        this.addTable(defaultAlias, table);
    }
*/
    /**
     * Добавляет в коллекцию таблиц контекста заданную таблицу с
     * алиасом по умолчанию "__DA__".
     *
     * @param table добавляемая таблица
     * @see tdo.Table
     * @see #defaultAlias
     */
    public void addTableServices(TableServices tableServices) {
        this.addTableServices(defaultAlias, tableServices);
    }

    /**
     * @return Возвращает исходное выражение в строковом формате
     */
    @Override
    public String getExpressionString() {
        return this.expressionString;
    }

    /**
     * Устанавливает новое значение выражения в строковом формате. <p>
     * @param expr новое значение выражения в строковом формате
     */
    @Override
    public void setExpressionString(String expr) {
        this.expressionString = expr;
    }

    /**
     * Добавляет елемент с информацией об обнаруженной в процессе обработки
     * выражения ошибке к списку ошибок.
     * @param code код ошибки - значение одной из констант, определенных
     *   интерфейсом {@link tdo.expr.ExpressionContext}
     * @param expr выражение, при обработки которого обнаружена ошибка
     * @see #addError(int, tdo.expr.IExpression, java.lang.String) 
     * @see tdo.expr.ErrorItem
     */
    @Override
    public void addError(int code, IExpression expr) {
        this.errorList.addElement(new tdo.expr.ErrorItem(code, expr));
    }

    /**
     * Добавляет елемент с информацией об обнаруженной в процессе обработки
     * выражения ошибке к списку ошибок.
     * @param code код ошибки - значение одной из констант, определенных
     *   интерфейсом {@link tdo.expr.ExpressionContext}
     * @param expr выражение, при обработки которого обнаружена ошибка
     * @param msg сообщение, содержащее дополнительную информацию об ошибке
     * @see #addError(int, tdo.expr.IExpression) 
     * @see tdo.expr.ErrorItem
     */
    @Override
    public void addError(int code, IExpression expr, String msg) {
        this.errorList.addElement(new ErrorItem(code, expr, msg));
    }

    /**
     * Возвращает коллекцию элементов, содержащих информацию об ошибках
     * обработки выражения.  
     * @return коллекция элементов, описывающих ошибки
     */
    @Override
    public Vector getErrorList() {
        return this.errorList;
    }

    /**
     * Выводит на системную консоль информацию об ошибках, если они обнаружены 
     * при обработке выражения.
     * Используется список, содержащий элементы {@link tdo.expr.ErrorItem}.
     */
    @Override
    public void printErrors() {
        System.out.println("*** Error List ***");
        for (int i = 0; i < errorList.size(); i++) {
            ErrorItem er = (ErrorItem) errorList.elementAt(i);
            System.out.println("code=" + er.code);
            System.out.println("---" + (er.expr == null ? "parser" : er.expr.toString()));
            System.out.println("*** " + (er.getMessage()));
        }
    }

    /**
     * Выводит на системную консоль информацию об ошибках из заданного списка, 
     * если они обнаружены. 
     * при обработке выражения.
     * @see tdo.expr.ErrorItem
     */
    public static void printErrors(Vector errorList) {
        System.out.println("*** Error List ***");
        for (int i = 0; i < errorList.size(); i++) {
            ErrorItem er = (ErrorItem) errorList.elementAt(i);
            System.out.println("code=" + er.code);
            System.out.println("---" + (er.expr == null ? "parser" : er.expr.toString()));
            System.out.println("*** " + (er.getMessage()));
        }
    }

    /**
     * Возвращает коллекцию объектов, представляющих идентификаторы выражения.
     * В эту коллекцию идентификаторы-параметры не входят.
     * @return коллекция объектов, типа {@link tdo.expr.IdentifierOperand}. 
     * @see #getParameterOperands() 
     */
    @Override
    public Vector getIdentifierOperands() {
        return this.columnOperands;
    }

    /**
     * Возвращает коллекцию объектов, представляющих идентификаторы-параметры 
     * выражения.
     * Параметры выражения это идентификаторы с предшествующим символом ':' - 
     * двоеточие.
     * @return коллекция объектов, типа {@link tdo.expr.ParameterOperand}. 
     * @see #getIdentifierOperands() 
     */
    @Override
    public Vector getParameterOperands() {
        return this.parameterOperands;
    }

    /**
     * Создает и возвращает результат компиляции строкового выражения.
     * @return результат обработки исходного выражения в виде IExpression объекта.
     */
    @Override
    public IExpression createExpression() {
        errorList.clear();
        columnOperands.clear();
        parameterOperands.clear();

        ExprParser exprparser = new ExprParser(this, expressionString);
        try {
            exprparser.parse();
        } catch (ExpressionException e) {
            this.addError(EXPRPARSER, new ErrorExpression(this, e));
            throw e;
        }
        Vector rpn = exprparser.getRPN();
        Stack stack = new Stack();

        for (int i = 0; i < rpn.size(); i++) {
            IToken t = (IToken) rpn.elementAt(i);
            if (t instanceof IOperand) {
                stack.push(t);
                if (t instanceof IdentifierOperand) {
                    columnOperands.addElement(t);
                } else if (t instanceof ParameterOperand) {
                    parameterOperands.addElement(t);
                }
            } else {
                IOperator oper = (IOperator) t;
                if (oper.isUnary()) {
                    if (oper instanceof FunctionOperator) {
                        //str = " " + t.toExprString() + " (" + (String)stack.pop() +")";
                        IOperand op1 = (IOperand) stack.pop();
                        stack.push(oper.createExpression(this, op1));
                        continue;
                    } else {
                        IOperand op = (IOperand) stack.pop();
                        stack.push(oper.createExpression(this, op));
                    }
                } else {
                    if (stack.isEmpty()) {
                        this.addError(STRUCTURE, new ErrorExpression(this, oper, null, null));
                        throw new EmptyStackException();
                    }
                    IOperand op2 = (IOperand) stack.pop();
                    if (stack.isEmpty()) {
                        this.addError(STRUCTURE, new ErrorExpression(this, oper, null, op2));
                        throw new EmptyStackException();
                    }

                    IOperand op1 = (IOperand) stack.pop();
                    stack.push(oper.createExpression(this, op1, op2));
                }
            }
        }

        return (IExpression) stack.pop();
    }

    public void printExpr(IExpression e) {
        String s = e.getClass().getName();
        int l = s.lastIndexOf('$');
        if (l >= 0) {
            l++;
        } else {
            l = s.lastIndexOf('.') + 1;
        }
        s = s.substring(l);
        if ( e instanceof AbstractExpression ) {
            System.out.println(s + " : operator='" + ((AbstractExpression)e).getOperator() + "'" );
        } else if ( e instanceof IdentifierOperand ) {
            IdentifierOperand ei = (IdentifierOperand)e;
            s += " : identifier=";
            if ( ei.getAlias() != null ) {
                s += "'" + ei.getAlias() + ".";
            } else {
                s += "'";
            }
            System.out.println(s + ((IdentifierOperand)e).getName() + "'" );
        } else if ( e instanceof ParameterOperand ) {
            System.out.println(s + " : parameter='" + ((ParameterOperand)e).getName() + "'" );
        } else if ( e instanceof LiteralOperand ) {
            System.out.println(s + " : value='" + ((LiteralOperand)e).getValue() + "'" );
        } else {
            System.out.println(s );
        }

    }

    public static Vector test(String expression, TableServices tableServices) {
        Vector el = new Vector(1);
        Object result = null;
        DefaultExpressionContext c = new DefaultExpressionContext(expression);
        IExpression exp = c.createExpression();
        if (tableServices != null) {
            c.addTableServices(tableServices);
        }

        try {
            if (tableServices != null) {
                result = exp.getValue(tableServices.getFilterServices().getRow(0));
            } else {
                result = exp.getValue();
            }
        } catch (Exception e) {
            c.addError(TESTING, exp, e.getMessage());
        }

        if (c.getErrorList().isEmpty()) {
            c.getErrorList().addElement(result);
        }
        return c.getErrorList();
    }

    /**
     * Позволяет определить находится ли объект в режиме тестирования.
     * @return true означает, что контект находится в режиме тестирования. 
     *   false - в противном случае.
     * @see #setTestMode(boolean) 
     */
    public boolean isTestMode() {
        return this.testMode;
    }

    /**
     * Включает или отключает режим тестирования.
     * @param testMode значение true означает, что контект переводится в режим
     *    тестирования. false - режим тестирования отключается.
     * @see #isTestMode() 
     */
    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

    protected static class NullValue {

    }
}//class DefaultExpressionContext
