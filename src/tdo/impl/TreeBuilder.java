package tdo.impl;

/*
 * Aggregator.java
 *
 * Created on 21 Ноябрь 2006 г., 10:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import tdo.*;
import tdo.util.Strings;

/**
 * Предоставляет возможность создания нового объекта типа {@link Table}, содержащего
 * агрегированные данные исходного объекта типа <code>Table</code>. <p>
 *
 * Как правило программист не будет явно создавать экземляр класса. Вместо этого удобнее
 * использовать один из перегруженных методов {@link Table#createAggTable}.<p>
 * <b>Пример использования</b><p>
 *
 *  Создадим тавлицу с данными о сотрудниках
 *   <code>
 *   <pre>
 *
 *  1.     DataTable empl = new DataTable();
 *  2.     empl.addRow( new String[] { ""Branch","Department","LastName","Salary" },
 *                    new Object[] { "South","Accounts","Smith",2000 } );
 *  3.     empl.addRow(  new Object[] {"South", "Accounts","Fisher",3500 } );
 *  4.     empl.addRow(  new Object[] {"North", "IT","Gates",15000 } );
 *  5.     empl.addRow(  new Object[] {"North", "IT","Wood",12000 } );
 *  6.     empl.addRow(  new Object[] { "North", "Accounts","Norton",2500 } );
 *   </pre>
 *   </code>
 *  Таблица будет содержать следующие ряды:
 *  <code>
 *  <pre>
 *              Branch    Department     LastName    Salary
 *          -----------------------------------------------------
 *  0.          South      Accounts      Smith         2000
 *  1.          South      Accounts      Fisher        3500
 *  2.          North      IT            Gates         15000
 *  3.          North      IT            Wood          12000
 *  4.          North      Accounts      Norton        2500
 *
 *  </pre>
 *  </code>
 *  Теперь, отсортируем тавлицу по колонкам Department и LastName:
 *  <code>
 *  <pre>
 * 7.     empl.setSort("Branch,Department");
 *  </pre>
 *  </code>
 *
 *  При использовании класса <code>Aggregator</code> исходная таблица <b>должна</b>
 *  быть отсортирована. <p>
 *  Используя класс <code>Aggregator</code> выполним:
 *  <code>
 *  <pre>
 *
 *  8.     Aggregator agg = new Aggregator(empl);
 *  9.     DataTable aggTable = agg.createTable();
 *  10.    agg.setColumnList("Branch,Department,LastName, SUM(salary) as TotalSalary");
 *  11.    agg.execute();
 *
 *   </pre>
 *   </code>
 *  Строка 8 нашего кода создает экземпляр объекта типа <code>Aggregator</code>.
 *  Обратим внимание, что конструктор класса принимает параметр типа
 *  <code>DataTable</code>, которым в нашем случае является исходная таблица
 *   <code>empl</code>.
 *  Порядок строк  9 и 10 значения не имеет. Основная логика сосредоточена в
 *  методе {@link #execute }. Метод сканирует исходную таблицу ряд за рядом и
 *  добавляет агрегатные ( групповые ) ряды к результирующей таблице. Групповые
 *  ряды создаются при изменении значений колонкок, входящих в список полей
 *  сортировки. В неашем примере "Branch,Department". <p>
 *
 *  Новая таблица, ссылка на которую находится в переменной <code>aggTable</code>
 *  приобретает вид:
 *  <code>
 *  <pre>
 *             Branch           Department      LastName      TotalSalary
 *          ------------------------------------------------------------
 *   0.                                                         35000
 *   1.         North                                           29500
 *   2.         North            Accounts                       2500
 *   3.         North            IT                             27000
 *   4.         South                                           5500
 *   5.         South            Accounts                       4500
 *   6.                                                         35000
 *
 *  </pre>
 *  </code>
 *  Видим, что результирующая таблица содержит на 2 ряда больше исходной.
 *  Ряды с индексами 0 и 6 ( самый первый и самый последний ) являются
 *  "итоговыми"  или "totals" рядами. У этих рядов заполнены только ячейки,
 *  соответствующие "агрегатным" колонкам, т.е. колонкам которые в списке
 *  метода {@link #setColumnList } определяются через использование
 *  агрегатных функций. В нашем случае - это  колонка с именем Salary,
 *  для которой мы использовали агрегатную функцию SUM(salary).
 *  Наличием первого, последнего или обоих итоговых рядов
 *  управляет свойство <code>totalsPosition</code>.
 *  Его setter-метод {@link #setTotalsPosition} может принимать как параметр
 *  одно из значений:
 *  {@link #TOTALS_TOP}, {@link #TOTALS_BOTTOM}, {@link #TOTALS_BOTH}. <p>
 *  Ряд с индексом 1 - это агрегатный ряд уровня 1, т.е. соответствует самой
 *  первой колонке списка полей, использованных при сортировке исходной таблицы.
 *  В нашем примере - это колонка с именем <code>Branch</code>.
 *  Этот ряд содержит в колонке TotalSalary сумму значений колонок Salarу для
 *  рядов в колонке Branch которых, содержится значение "North". Все сказанное
 *  касается и ряда 4. <p>
 *  Ряды с индексами 2 и 3  - это агрегатные ряды уровня 2. и так далее. <p>
 *  Обратим внимание, что колонка <code>LastName</code> во всех записях пустая.
 *  Это потому, что мы указали ее в списке колонок результирующей таблицы, но она
 *  не входит в список колонок сортировки и к ней не применена никакая агрегатная
 *  функция. Можно указать <code>Aggregator</code> создать таблицу, состоящую не
 *  только из агрегатных рядов, но и из "детальных" рядов исходной таблицы. Для этого
 *  модифицируем наш код, как показано ниже (новая строка выделена):
 * <code>
 * <pre>
 *  8.     Aggregator agg = new Aggregator(empl);
 *  9.     DataTable aggTable = agg.createTable();
 *  10.    agg.setColumnList("Branch,Department,LastName, SUM(salary) as TotalSalary");
 *  11     <b>agg.setIncludeDetailRows(true);</b>
 *  12.    agg.execute();
 * </pre>
 * </code>
 *
 *  Получим следующую тавлицу, содержащую как агрегированные ряды, так и детальные.
 *  <code>
 *  <pre>
 *             Branch           Department      LastName      TotalSalary
 *          ------------------------------------------------------------
 *   0.                                                         35000
 *   1. <b>     North                                           29500 </b>
 *   2.      North            Accounts                       2500
 *   3. <i>     North            Accounts       Norton          2500 </i>
 *   4.      North            IT                             27000
 *   5. <i>     North            IT             Wood            12000 </i>
 *   6. <i>     North            IT             Gates           15000 </i>
 *   7. <b>     South                                           5500  </b>
 *   8.      South            Accounts                       5500
 *   9.  <i>    South            Accounts       Smith           2000 </i>
 *   10. <i>    South            Accounts       Fisher          3500 </i>
 *   11.                                                         35000
 *
 *  </pre>
 *  </code>
 *
 *  До сих пор мы рассматривали создание агрегированной таблице в режиме, когда агрегированные
 *  ряды появляются <i>прежде</i>, чем детальные, а точнее вначале следует агрегатный ряд,
 *  соответствующий первой колонке из списка сортировки, затем ряд, соответствующий второй колонке
 *  и .т.д. Часто требуется изменить этот порядок. Для этого воспользуемся методом
 *  {@link setGroupMode } со значением параметра равным  {@link AFTER} ( по умолчанию, свойство
 *  <code>groupMode</code> равно {@link AFTER}.
 *  <code>
 *  <pre>
 *  8.     Aggregator agg = new Aggregator(empl);
 *  9.     DataTable aggTable = agg.createTable();
 *  10.    agg.setColumnList("Branch,Department,LastName, SUM(salary) as TotalSalary");
 *  11     agg.setIncludeDetailRows(true);
 *  12.    agg.setGroupMode(Aggregator.AFTER);
 *  13.    agg.execute();
 *  </pre>
 *  </code>
 *
 *  <code>
 *  <pre>
 *             Branch           Department      LastName      TotalSalary
 *          ------------------------------------------------------------
 *  0.                                                  		35000
 *  1. <i>      North           Accounts        Norton          2500 </i>
 *  2.       North           Accounts                        2500
 *  3. <i>      North           IT              Wood            12000 </i>
 *  4. <i>      North           IT              Gates           15000 </i>
 *  5.       North           IT                              27000
 *  6. <b>      North                                           29500 </b>
 *  7. <i>      South           Accounts        Smith           2000  </i>
 *  8. <i>      South           Accounts        Fisher          3500  </i>
 *  9.       South           Accounts                        5500
 *  10. <b>     South                                           5500  </b>
 *  11.                                                         35000
 *  </pre>
 *  </code>
 *
 *
 * @author valery
 */
public class TreeBuilder {

    protected GroupPart[] groupParts;
    /**
     * Константа, для установки значения свойства <code>groupMode</code> таким образом,
     * что агрегатные ряды следуют прежде детальных.
     * @see #AFTER
     * @see #getGroupMode
     * @see #setGroupMode
     */
    public static final boolean BEFORE = false;
    /**
     * Константа, для установки значения свойства <code>groupMode</code> таким образом,
     * что агрегатные ряды следуют после детальных.
     * @see #BEFORE
     * @see #getGroupMode
     * @see #setGroupMode
     */
    public static final boolean AFTER = true;
    /**
     * Когда значение свойства <code>totalsPosition</code> равно значению этого поля,
     * итоговый ряд агрегированной таблице всегда размещается вверху, т.е. индекс итогового ряда
     * равен 0.
     * @see #setTotalsPosition
     * @see #TOTALS_BOTTOM
     * @see #TOTALS_BOTH
     * @see #TOTALS_NONE
     *
     */
    public static final int TOTALS_TOP = 1;
    /**
     * Когда значение свойства <code>totalsPosition</code> равно значению этого поля,
     * итоговый ряд агрегированной таблице всегда размещается внизу, т.е. индекс итогового ряда
     * равен <i>(количество-рядов - 1).</i>
     * @see #setTotalsPosition
     * @see #TOTALS_TOP
     * @see #TOTALS_BOTH
     * @see #TOTALS_NONE
     *
     */
    public static final int TOTALS_BOTTOM = 2;
    /**
     * Когда значение свойства <code>totalsPosition</code> равно значению этого поля,
     * итоговый ряд агрегированной таблице  размещается как в позиции, когда. индекс итогового ряда
     * равен 0, так и в позиции <i>(количество-рядов - 1).</i>
     * @see #setTotalsPosition
     * @see #TOTALS_TOP
     * @see #TOTALS_BOTTOM
     * @see #TOTALS_NONE
     *
     */
    public static final int TOTALS_BOTH = 3;
    /**
     * Когда значение свойства <code>totalsPosition</code> равно значению этого поля,
     * итоговые ряды агрегированной таблице  не выводятся.
     *
     * @see #setTotalsPosition
     * @see #TOTALS_TOP
     * @see #TOTALS_BOTTOM
     * @see #TOTALS_BOTH
     *
     */
    public static final int TOTALS_NONE = -1;
    /**
     * Тавлица, служащая как исходная, подлежащая агрегированию. Значение назначается
     * конструктором класса через параметр.
     *
     * @see #Aggregator(Table)
     */
    protected Table sourceTable;
    /**
     * Таблица, экземпляр которой создается методом {@link #createTable} - для результата агрегирования.
     */
    protected Table targetTable; // this datatable is used as a result dataTable.
    /**
     * Строка знаков, содержащая разделенный запятой список колонок и (или) агрегатных функций.<p>
     * Значение строки устанавливается методом {@link #setColumnList}. Там же описывается синтаксис
     * списка.
     */
    private String columnList;
    private String sortColumnList;
    private boolean sortDirection;
    /**
     * Строка знаков, содержащая разделенный запятой список колонок и (или) агрегатных функций.<p>
     * Значение строки устанавливается методом {@link #setColumnList}. Там же описывается синтаксис
     * списка.
     */
    private ColumnListExpressionContext columnExpressionContext;
    /**
     * Хранит значение, управляющее порядком появления агрегатных рядов в результирующей
     * таблице. <p>
     * Допусимыми значениями являются {@link #BEFORE} - по умолчанию и {@link #AFTER }.
     * Устанавливается методом {@link #setGroupMode }.
     */
    private boolean groupMode = BEFORE;
    /**
     * Хранит значение, управляющее наличием и размещением итоговых агрегатных рядов в
     * результирующей  таблице. <p>
     * Допусимыми значениями являются {@link #TOTALS_BOTH} - по умолчанию, и {@link #TOTALS_TOP } и
     * {@link #TOTALS_BOTTOM}.
     * Устанавливается методом {@link #setTotalsPosition }
     */
    private int totalsPosition = TOTALS_BOTH;
    /**
     * Хранит значение, указывающее включаются результирующую  таблицу детальные записи исходной или нет. <p>
     * Значение <code>false</code> - значение по умолчаню: детальные ряды не включены.
     *  <code>true</code> - в противном случае.
     * Устанавливается методом {@link #setIncludeDetailRows}
     */
    private boolean includeDetailRows;
    /**
     * Содержит массив имен колонок результирующей (агрегатной) таблицы, каждая из которых
     * создана применением агрегатной функции. Все имена заданы в верхнем регистре.
     * Например, если {@link #columnList}, содержит элемент <code><b>MIN(birthDay) AS HerBirhDay</b></code>,
     * то в массиве будет представлен элемент "HERBIRTHDAY". <p>
     * Здесь устанавливается вызовом метода <code>getColumns</code> класса {@link AggContainer}.
     * Предназначен для использования внутри класса и (или) его наследников.
     *
     * @see AggContainer#getColumns
     */
    protected String[] aggColumns;
    protected SortColumnInfo[] sortColumnInfo;
    /**
     * Количество уровней аггрегирования (группирования).<p>
     * Вычисляется как сумма количества колонок сортировки исходной таблицы,
     * минус количество колонок с модификатором <code>EXCLUDE</code> плюс 1 для
     * итогов.
     * @see exclCount
     * @see Sorter
     */
   //protected int levelCount;

    /**
     * Создает новый экземпляр класса для исходной таблицы, определяемой параметромю <p>
     * @param sourceTable исходная таблица.
     */
    public TreeBuilder(Table sourceTable) {
        totalsPosition = TOTALS_BOTH;
        this.sourceTable = sourceTable;
        //this.levelCount = -1;
    }
/*    public int getLevelCount() {
        if ( this.levelCount == -1 ) {

        }
    }
 */
    /**
     * Создает экземпляр типа {@link Table}, используемый как результирующая
     * таблица агрегированных данных.
     * @return новый объект Table.
     */
    public Table createTargetTable() {
        targetTable = sourceTable.createTable();
        createColumnListExpressionContext();
        return targetTable;
    }

    public Table createTargetTreeTable() {
        //25.07 targetTable = sourceTable.createTreeTable();
        targetTable = new TreeDataTable();
        createColumnListExpressionContext();
        return targetTable;
    }

    protected SortColumnInfo[] createSortColumnInfo() {
        
        if (sortColumnList == null) {
            return null;
        }
        sortColumnInfo = this.createSortColumnInfo(sortColumnList);
        for (int i = 0; i < sortColumnInfo.length; i++) {
            sortColumnInfo[i].columnIndex = sourceTable.getColumns().find(sortColumnInfo[i].columnName);
        }
        return sortColumnInfo;
    }

    protected void createColumnListExpressionContext() {
        columnExpressionContext = new ColumnListExpressionContext(columnList);
        // -- установим в контекст выражения sourceTable для правильного
        // -- определения типов выражений
        columnExpressionContext.addTableServices(sourceTable.getContext());
        columnExpressionContext.setTargetTable(targetTable);
        columnExpressionContext.createTargetColumns();
        sortColumnInfo = this.createSortColumnInfo();
        columnExpressionContext.setSortColumnInfo(sortColumnInfo);
    }

    protected void createColumnListExpressionContext(Table aTable, String alias) {
        columnExpressionContext = new ColumnListExpressionContext(columnList);
        columnExpressionContext.addTableServices(alias, aTable.getContext());
        // -- установим в контекст выражения sourceTable для правильного
        // -- определения типов выражений
        columnExpressionContext.addTableServices(sourceTable.getContext());
        columnExpressionContext.setTargetTable(targetTable);
        columnExpressionContext.createTargetColumns();
        sortColumnInfo = this.createSortColumnInfo();
        columnExpressionContext.setSortColumnInfo(sortColumnInfo);
    }

    public ColumnListExpressionContext getContext() {
        return this.columnExpressionContext;
    }

    /**
     * Устанавливает список выбираемых из исходной таблицы колонок. <p>
     * Строка columnList - это список элементов, разделенных запятой, каждый из которых
     * представляет собой одно из:
     * <UL>
     *   <li><i>имя-колонки</i>;
     *   <li>MIN(<i>имя-колонки</i>) [ AS <i>имя-колонки-1</i> ];</li>
     *   <li>MAX(<i>имя-колонки</i>) [ AS <i>имя-колонки-1</i> ];</li>
     *   <li>SUM(<i>имя-колонки</i>) [ AS <i>имя-колонки-1</i> ];</li>
     *   <li>COUNT(*) [ AS <i>имя-колонки-1</i> ];              </li>
     *   <li>LEVEL(*) [ AS <i>имя-колонки-1</i> ];              </li>
     *   <li><i>имя-custom-функции</i>(*) [ AS <i>имя-колонки-1</i> ];               </li>
     *   <li><i>имя-custom-функции</i>(<i>имя-колонки</i>) [ AS <i>имя-колонки-1</i> ]</li>
     * </UL>
     * Каждый из элементов может быть задан в любом регистре. Допускается любое
     * количество пробелов, разделяющих как элементы, перед и после имен колонок
     * и перед и после ключевого слова AS. Например,
     *  "field1   AS f1,  miN(      field2)"
     * @param columnList
     */
    public void setColumnList(String columnList) {
        this.columnList = columnList.toUpperCase();
    }

    public void setSortColumnList(String sortColumns) {
        this.sortColumnList = sortColumns;
    }

    /**
     * Устанавливает значение направления сортировки исходной таблицы: по
     * убыванию или по возрастанию.<p>
     *
     * @param sortDirection принимает значение ISorter#ASCENDING или
     *      ISorter#DESCENDING.
     */
    public void setSortDirection(boolean sortDirection) {
        this.sortDirection = sortDirection;
    }

    /**
     * @return true если детальные записи исходной таблицы дабавляются к
     * результирующей (агрегатной) таблице. false - в противном случае.
     * @see #setIncludeDetailRows
     */
    public boolean getIncludeDetailRows() {
        return this.includeDetailRows;
    }

    /**
     * @param includeDetailRows если равен true - устанавливается режим, когда
     *      детальные ряды исходной таблицы добавляются к агрегатной таблице.
     *      false указывает, что детальные ряды не добавляются.
     * @see #getIncludeDetailRows
     */
    public void setIncludeDetailRows(boolean includeDetailRows) {
        this.includeDetailRows = includeDetailRows;
    }

    /**
     * Возвращает значение, определяющее выводятся  или нет итоговые ряды в
     * результирующую таблицу и, если да, то их позиция. <p>
     *
     *  @return значение одного из полей {@link TOTALS_NONE}, {@link TOTALS_TOP},
     *      {@link TOTALS_BOTTOM},{@link TOTALS_BOTH}
     */
    public int getTotalsPosition() {
        return this.totalsPosition;
    }

    /**
     * Устанавливает порядок вывода итоговых рядов в агрегатную таблицу. <p>
     * @param  totalsPosition  значение одного из полей {@link TOTALS_NONE},
     *     {@link TOTALS_TOP}, {@link TOTALS_BOTTOM},{@link TOTALS_BOTH}
     */
    public void setTotalsPosition(int totalsPosition) {
        this.totalsPosition = totalsPosition;
    }

    /**
     * Возвращает значение, определяющее порядок вывода агрегатных рядов.
     * @return {@link BEFORE} - агрегатные ряды выводятся перед детальными;
     *         {@link AFTER} - агрегатные ряды выводятся после детальных.
     */
    public boolean getGroupMode() {
        return this.groupMode;
    }

    /**
     * Устанавливает значение, определяющее порядок вывода агрегатных рядов.
     * Это же значение метод устанавливает в {@link #aggContainer} .
     * @param groupMode
     *
     */
    public void setGroupMode(boolean groupMode) {
        this.groupMode = groupMode;
    }

    /**
     * Подготавливает объект класса для выполнения метода {@link #execute}.
     * вызов метода в начале execute() метода, позволяет повторно вызывать
     * его (execute) , без необходимости создавать новый объект Aggregator.
     */
/*    protected void prepare() {
    }
     */
    /**
     * Используя объект типа ISorter исходной таблицы определяет имена полей
     * сортировки. По именам колонок определяет индексы и создает их массив
     * в переменной {@link #sorterKeys}.
     * @param sortColumnList
     * @return
     */
    protected SortColumnInfo[] createSortColumnInfo(String sortColumnList) {
        String cList = Strings.compressSpaces(sortColumnList).trim();
        String[] s = Strings.split(cList.toUpperCase());
        SortColumnInfo[] sortInfo = new SortColumnInfo[s.length];

        for (int i = 0; i < sortInfo.length; i++) {
            sortInfo[i] = new SortColumnInfo();
        }
        for (int i = 0; i < s.length; i++) {
            sortInfo[i].sortDirection = Sorter.ASCENDING;
            sortInfo[i].caseSensitive = true;
            sortInfo[i].excluded = false;

            String[] s1 = Strings.split(s[i], " ");

            sortInfo[i].columnName = s1[0];

            for (int j = 0; j < s1.length; j++) {
                if (s1[j].toUpperCase().equals("ASC")) {
                    continue;
                }
                if (s1[j].toUpperCase().equals("DESC")) {
                    sortInfo[i].sortDirection = Sorter.DESCENDING;
                    continue;
                }

                if (s1[j].toUpperCase().equals("CASEINS") || s1[j].toUpperCase().equals("CI")) {
                    sortInfo[i].caseSensitive = false;
                    continue;
                }
                if (s1[j].toUpperCase().equals("EX") || s1[j].toUpperCase().equals("EXCLUDE")) {
                    sortInfo[i].excluded = true;
                    continue;
                }
            }
        }
        return sortInfo;
    }

    protected void createGroupParts() {
        groupParts = new GroupPart[sortColumnInfo.length];
        int id = groupParts.length - 1;
        for (int i = 0; i < groupParts.length; i++) {
            SortColumnInfo[] keys = new SortColumnInfo[i + 1];
            System.arraycopy(sortColumnInfo, 0, keys, 0, keys.length);
            groupParts[i] = new GroupPart(keys);
            //groupParts[i].id = id;
            id--;
        }
    }

    /**
     * Выполняет работу по созданию результирующей агрегатной таблицы. <p>
     * Проводит подготовку, вызывая метод {@link prepare}.
     * Используя методы {@link AggContainer#hasMoreData} и
     * {@link AggContainer#nextData} создает результирующую агрегатную тавлицу.
     * @param mode
     */
    protected void executeSingle(boolean mode) {
        int start = 0;
        int end = sourceTable.getRowCount();
        int step = 1;

        if (mode == BEFORE) {
            start = sourceTable.getRowCount() - 1;
            end = 0;
            step = -1;
        }

        //this.setIncludeDetailRows(false);
        Object[] values;//My 06.03.2012 = null;
        Object[] totals = null;
        if (totalsPosition == TOTALS_TOP || totalsPosition == TOTALS_BOTH) {
            targetTable.addRow();
            getLastRow().getState().setDepth(0);
            //targetTable.getLastRow().getState().getAttribbutes().put("depth", 0);
        }
        for (int rowIndex = start; mode == AFTER ? rowIndex < end : rowIndex >= 0; rowIndex = mode == AFTER ? ++rowIndex : --rowIndex) {
            DataRow row = this.columnExpressionContext.getTable().getRow(rowIndex);
            //totals = this.columnExpressionContext.getRowValues(rowIndex);
            if (rowIndex == start) {
                columnExpressionContext.init(row);
            }
            totals = this.columnExpressionContext.getRowValues(row);
            if (this.includeDetailRows) {
                targetTable.addRow(totals);
            }
        } //for
        values = columnExpressionContext.getAggRowValues();
        targetTable.addRow(values);

        createTotalRows(totals);
        if (mode == BEFORE) {
            reverse();
        }
    }
    public DataRow getLastRow() {
        return targetTable.getRowCount() == 0 ? null : targetTable.getRow(targetTable.getRowCount() - 1);
    }

    /**
     * Выполняет работу по созданию результирующей агрегатной таблицы. <p>
     * Проводит подготовку, вызывая метод {@link prepare}.
     * Используя методы {@link AggContainer#hasMoreData} и
     * {@link AggContainer#nextData} создает результирующую агрегатную тавлицу.
     */
    public void execute() {
        if (sortColumnInfo == null && hasAggFunction()) {
            executeSingle(groupMode);
        } else {
            execute(groupMode);
        }
        createTreeNodeInfo();
    }

    public boolean hasAggFunction() {
        return columnExpressionContext.getAggFunctions() != null && columnExpressionContext.getAggFunctions().length > 0;
    }

    /**
     * Выполняет работу по созданию результирующей агрегатной таблицы. <p>
     * Проводит подготовку, вызывая метод {@link prepare}.
     * Используя методы {@link AggContainer#hasMoreData} и
     * {@link AggContainer#nextData} создает результирующую агрегатную тавлицу.
     * @param mode
     */
    protected void execute(boolean mode) {
        //this.levelCount = ((TreeTable)targetTable).getTreeNodeInfo().getLevelCount();
        int start = 0;
        int end = sourceTable.getRowCount();
        int step = 1;

        if (mode == BEFORE) {
            start = sourceTable.getRowCount() - 1;
            end = 0;
            step = -1;
        }

        createGroupParts();
        int levelCount = groupParts == null ? 0 : groupParts.length;        
        Object[] values;//My 06.03.2012 = null;
        Object[] totals = null;
        if (totalsPosition == TOTALS_TOP || totalsPosition == TOTALS_BOTH) {
            DataRow newRow =  targetTable.addRow();
            newRow.getState().setDepth(0);
            columnExpressionContext.updateInserted(newRow);
        }
        for (int rowIndex = start; mode == AFTER ? rowIndex < end : rowIndex >= 0; rowIndex = mode == AFTER ? ++rowIndex : --rowIndex) {
            DataRow row = this.columnExpressionContext.getTable().getRow(rowIndex);
            //totals = this.columnExpressionContext.getRowValues(rowIndex);
            if (rowIndex == start) {
                columnExpressionContext.init(row);
            }
            totals = this.columnExpressionContext.getRowValues(row);
            boolean detailAdded = false;
            if (groupParts != null) {
                for (int i = groupParts.length - 1; i >= 0; i--) {

                    if (groupParts[i].isGroupChanged(rowIndex, mode)) {
                        values = groupParts[i].getAggRowValues();
                        DataRow newRow = targetTable.addRow(values);
                        newRow.getState().setDepth(i + 1);
                        //int a = targetTable.getLastRow().getState().getDepth();
                        groupParts[i].reset(row);
                        columnExpressionContext.updateInserted(newRow);
                    } else {
                        if (i == groupParts.length - 1 && this.includeDetailRows) {
                            DataRow newRow = targetTable.addRow(totals);
                            newRow.getState().setDepth(levelCount+1);
//System.out.println("levelCount=" + levelCount +"; DDD=" + newRow.getState().getDepth());
                            detailAdded = true;
                            columnExpressionContext.updateInserted(newRow);
                        }
                    }
                    
                    //values = groupParts[i].getRowValues(rowIndex);
                } //for
            }
            if ((!detailAdded) && this.includeDetailRows) {
                DataRow newRow = targetTable.addRow(totals);
                newRow.getState().setDepth(levelCount+1);
                columnExpressionContext.updateInserted(getLastRow());
            }
        } //for
        if (groupParts != null) {
            for (int i = groupParts.length - 1; i >= 0; i--) {
                values = groupParts[i].getAggRowValues();
                DataRow newRow = targetTable.addRow(values);
                newRow.getState().setDepth(i + 1);
                columnExpressionContext.updateInserted(getLastRow());
                groupParts[i].reset(null);
            } //for
        }
        createTotalRows(totals);
        if (mode == BEFORE) {
            reverse();
        }
    }

    protected void createTotalRows(Object[] totals) {
        DataRow row;
        if (totalsPosition == TOTALS_TOP || totalsPosition == TOTALS_BOTH) {
            row = targetTable.getRow(0);
            for (int i = 0; i < targetTable.getColumns().getCount(); i++) {
                totals = this.columnExpressionContext.getAggRowValues();
                if (!columnExpressionContext.isKey(i)) {
                    row.setValue(totals[i], i);
                }
            }
            columnExpressionContext.updateInserted(row);
        }
        if (totalsPosition == TOTALS_BOTTOM || totalsPosition == TOTALS_BOTH) {
            row = targetTable.addRow();
            //row = targetTable.getNamedValues(targetTable.addRow());
            getLastRow().getState().setDepth(0);

            for (int i = 0; i < targetTable.getColumns().getCount(); i++) {
                totals = this.columnExpressionContext.getAggRowValues();
                if (!columnExpressionContext.isKey(i)) {
                    row.setValue(totals[i], i);
                }
            }
            this.columnExpressionContext.updateInserted(row);
        }
    }

    public void reverse() {
        if (targetTable.getRowCount() < 2) {
            return;
        }
        int start = 0;
        int end = targetTable.getRowCount() - 1;

        if (totalsPosition == TOTALS_TOP) {
            start = 1;
        }

        if (totalsPosition == TOTALS_BOTTOM) {
            end = targetTable.getRowCount() - 2;
        }

        if (totalsPosition == TOTALS_BOTH) {
            start = 1;
            end = targetTable.getRowCount() - 2;
        }

        reverse(targetTable, start, end);
    }

    public void reverse(Table table, int first, int last) {
        if (table.getRowCount() < 2) {
            return;
        }
        int start = first;
        int end = last;
        //int n = getRowCount() / 2;
        int n = (last - first + 1) / 2 + first;
        for (int i = first; i < n; i++) {
            DataRow r1 = table.getRow(i);
            table.setRow(table.getRow(end), start);
            table.setRow(r1, end);
            start++;
            end--;
        }
    }

/////////////////////////////////////////////////////////////////////////////
    /**
     * Аккумулирует информацию о модификаторах сортировки каждой колонки. <p>
     *
     */
    public static class SortColumnInfo {

        public boolean sortDirection = Sorter.ASCENDING;
        public boolean caseSensitive = true;
        public String columnName = null;
        public int columnIndex = -1;
        public boolean excluded = false;
        public int levelCount;
    } //class SortColumnInfo

    public class GroupPart {

        // public int id;
        protected ColumnListExpressionContext partExpressionContext;
        protected int lastKey;
        //protected CommaListExpression commaListExpression;
        protected SortColumnInfo[] keys;
        protected Object[] keyValues;

        /**
         * Если список полей сортировки есть a0,a1,..,an то lastKey может быть 0,1,..., n.
         * Таким образом, класс имеет дело с частью полей сортировки.
         * @param keys
         *
         */
        public GroupPart(SortColumnInfo[] keys) {

            this.keys = keys;
            keyValues = new Object[keys.length];
            for (int i = 0; i < keyValues.length; i++) {
                keyValues[i] = null;
            }
            partExpressionContext = new ColumnListExpressionContext(columnList);
            partExpressionContext.getExpression();
            // -- установим в контекст выражения sourceTable для правильного
            // -- определения типов выражений
            partExpressionContext.addTableServices(sourceTable.getContext());
            partExpressionContext.setTargetTable(targetTable);
            partExpressionContext.setSortColumnInfo(this.keys);
        }

        public void updateInserted(DataRow row) {
            partExpressionContext.updateInserted(row);
        }

        public boolean isGroupChanged(int rowIndex, boolean mode) {
            boolean r = false;
            int start;
            int end;
            if (mode == AFTER) {
                start = 0;
                end = sourceTable.getRowCount();
            } else {
                start = sourceTable.getRowCount() - 1;
                end = -1;
            }

            if (rowIndex == start) {
                for (int i = 0; i < keyValues.length; i++) {
                    keyValues[i] = sourceTable.getRow(rowIndex).getValue(keys[i].columnIndex);
                } //for
                return r;
            }

            if (rowIndex == end) {
                return true;
            }

            for (int i = 0; i < keyValues.length; i++) {
                Object o = sourceTable.getRow(rowIndex).getValue(keys[i].columnIndex);
                Object ko = keyValues[i];
                if (o == null || ko == null) {
                    if (o != null || ko != null) {
                        r = true;
                        break;
                    }
                }

                if (!o.equals(ko)) {
                    r = true;
                    break;
                }
            } //for
            // save if nessasary current key values
            if (r) {
                for (int i = 0; i < keyValues.length; i++) {
                    keyValues[i] = sourceTable.getRow(rowIndex).getValue(keys[i].columnIndex);
                } //for
            }

            return r;
        }

        public boolean isGroupChanged(int rowIndex) {
            return isGroupChanged(rowIndex, TreeBuilder.AFTER);
        }

        public Object[] getAggRowValues() {
            return this.partExpressionContext.getAggRowValues();
        }

        public Object[] getRowValues(int rowIndex) {
            DataRow row = this.partExpressionContext.getTable().getRow(rowIndex);
            return this.partExpressionContext.getRowValues(row);
        }

        public void reset(DataRow row) {
            partExpressionContext.reset(row);
        }
    } //class GroupPart

    /**
     * Создает и возвращает объект, предоставляющий информацию об условиях,
     * при которых создана агрегатная таблица (treeTable).<p>
     * Эта информация может понадобиться, например, при специальной сортировке
     * ( сортировки по узлам ) таблицы. <p>
     * Для созданного объекта устанавливаются значения свойств:
     * <ul>
     *   <li>includeDetailsRows</li>
     *   <li>groupMode</li>
     *   <li>totalsPosition</li>
     *   <li>sortDirection</li>
     *   <li>levelCount</li>
     * </ul>
     *
     * После создания и установки свойств ссылка на объект заносится в
     * свойство {@link Table#treeNodeInfo} результирующей таблицы
     * {@link #targetTable}.
     *
     * Метод предназначен для внутреннего использования.
     *
     * @return созданный объект
     */
    protected TreeNodeInfo createTreeNodeInfo() {
        TreeNodeInfo state = new TreeNodeInfo(targetTable);
        state.setIncludeDetailRows(includeDetailRows);
        state.setGroupMode(groupMode);
        state.setTotalsPosition(totalsPosition);
        state.setSortDirection(sortDirection);
        int r = 0;
        if ( sortColumnInfo != null ) {
            r = sortColumnInfo.length;
            for (int i = 0; i < sortColumnInfo.length; i++) {
                if (sortColumnInfo[i].excluded) {
                    r--;
                }
            }
        } 
        state.setLevelCount(r);
        //this.levelCount = r;
        ((TreeTable) targetTable).setTreeNodeInfo(state);
        return state;
    }

/**
     * Описывает состояние созданной агрегатной таблицы.<p>
     *
     * Созданная агрегатная таблица имеет тип Table и не содержит каких
     * либо сведений о том каким образом таблица создавалась, какие колонки и
     * агрегатный функции использованы. Иногда это не имеет значения, но чаще
     * всего требуется иметь возможность, например, зная индекс ряда, определить
     * его уровень группирования или при каком значении <code>groupMode</code>
     * таблица создана. Как правило имея для анализа только ряд, т.е. объект
     * типа DataRow это невозможно установить и приложение,  ипользующее
     * агрегированную таблицу должно было бы позаботиться о сохранении состояния
     * в котором таблица создавалась. Поэтому в классе {@link Table} введено
     * свойство <code>aggState</code> типа AggState, значение которого
     * устанавливается методом {@link Aggregator#execute}. Сразу после окончательного
     * создания агрегатной таблицы в методе {@link #execute} создается объект
     * типа <code>AggState</code>.
     *
     * @see Aggregator#createState
     * @see BaseTable#getAggState
     */
    public static class TreeNodeInfo {

        private Table table; //target Table
        private DataRowCollection rows; // dataSore for table
        private boolean includeDetailRows;
        private int totalsPosition;
        private int levelCount;
        private boolean sortDirection;
        private boolean groupMode;
        private int[] levelIndice;
        private int[] rowIndice;

        protected TreeNodeInfo() {
        }

        public TreeNodeInfo(Table table) {
            this.table = table;
        }
        /**
         * Устанавливается перед аггрегатной сортировкой во <code>ViewManager</code>.
         * @param rows
         */
        public void setRows(DataRowCollection rows) {
            this.rows = rows;
        }

        /**
         * @return true если детальные записи исходной таблицы дабавлены к
         * результирующей (агрегатной) таблице. false - в противном случае.
         * @see #setIncludeDetailRows
         */
        public boolean getIncludeDetailRows() {
            return this.includeDetailRows;
        }

        /**
         * @param includeDetailRows если равен true - устанавливается режим, когда
         *      детальные ряды исходной таблицы добавлены к агрегатной таблице.
         *      false указывает, что детальные ряды не добавлены.
         * @see #getIncludeDetailRows
         */
        public void setIncludeDetailRows(boolean includeDetailRows) {
            this.includeDetailRows = includeDetailRows;
        }

        /**
         * Возвращает значение, определяющее выведены  или нет итоговые ряды в
         * результирующую таблицу и, если да, то их позиция. <p>
         *
         *  @return значение одного из полей {@link Aggregator#TOTALS_NONE},
         *      {@link Aggregator#TOTALS_TOP},
         *      {@link Aggregator#TOTALS_BOTTOM},{@link Aggregator#TOTALS_BOTH}
         */
        public int getTotalsPosition() {
            return this.totalsPosition;
        }

        /**
         * Устанавливает порядок выведенных итоговых рядов в агрегатную таблицу. <p>
         * @param  totalsPosition  значение одного из полей
         *      {@link Aggregator#TOTALS_TOP},
         *      {@link Aggregator#TOTALS_BOTTOM},{@link Aggregator#TOTALS_BOTH}
         */
        public void setTotalsPosition(int totalsPosition) {
            this.totalsPosition = totalsPosition;
        }

        /**
         * Свойство {@link #levelCount} определяет количество уровней
         * группирования, которое обычно равно количеству колонок
         * в списке колонок, по которому производилась сортировка
         * исходной таблицы, прежде, чем была создана результирующая.
         * Это значение изменяется применением модификатора <i>EXCLUDE</code>
         * к колонке сортировки. Например, при сортировке применяется список:
         * <code>
         *  <pre>
         *      "State,City EXCLUDE,Department"
         *  </pre>
         * </code>
         * При сортировке используются все три колонки, но при создании
         * <code>treeTable</code> агрегатные ряды выводятся в результирующую
         * таблицу только для двух: City и Department. Таким образом,
         * значение свойства <code>levelCount</code> равно 2.
         *
         *
         * @return Количество уровней аггрегирования.
         */
        public int getLevelCount() {
            return levelCount;
        }

        /**
         * Устанавливается количество уровней группирования. <p>
         * Метод предназначен для внутреннего использования.
         * @param levelCount
         */
        public void setLevelCount(int levelCount) {
            this.levelCount = levelCount;
        }

        /**
         * Возвращает значение направления сортировки исходной таблицы: по
         * убыванию или по возрастанию.<p>
         *
         * @return ISorter#ASCENDING или ISorter#DESCENDING.
         */
        public boolean getSortDirection() {
            return this.sortDirection;
        }

        /**
         * Устанавливает значение направления сортировки исходной таблицы: по
         * убыванию или по возрастанию.<p>
         *
         * @param sortDirection принимает значение ISorter#ASCENDING или
         *      ISorter#DESCENDING.
         */
        public void setSortDirection(boolean sortDirection) {
            this.sortDirection = sortDirection;
        }

        /**
         * Возвращает значение, определяющее порядок вывода агрегатных рядов.
         * @return {@link BEFORE} - агрегатные ряды выводятся перед детальными;
         *         {@link AFTER} - агрегатные ряды выводятся после детальных.
         */
        public boolean getGroupMode() {
            return this.groupMode;
        }
        /**
         * Устанавливает значение, определяющее порядок вывода агрегатных рядов.
         *
         * @param groupMode принимает одно из двух значений {@link BEFORE} -
         *   агрегатные ряды выводятся перед детальными;
         *   {@link AFTER} - агрегатные ряды выводятся после детальных.
         */
        public void setGroupMode(boolean groupMode) {
            this.groupMode = groupMode;
        }

        /**
         * Возвращает ряд агрегатной таблицы по его индексу. <p>
         * Метод не зависит от какого-либо фильтра или сортировки, возможно
         * примененного к агрегатной таблице, т.е. обращается напрямую
         * к dataStore агрегатной таблицы.
         * Используется, например, при сортировке агрегатной (tree)
         * таблицы.
         * @param rowIndex индекс возвращаемого ряда.
         * @return ряд таблицы типа DataRow.
         */
        public DataRow getRow(int rowIndex) {
//            DataRow r = null;
//                r = rows.get(rowIndex);
            return this.rows.get(rowIndex);
        }

        /**
         * Возвращает номер уровня группирования для ряда агрегатной таблицы
         * с индексом заданным параметром. <p>
         * Для итоговых строк возвращаемый результат равен 0; для детальных,
         * если присутствуют, рядов, значение уровня на 1 больше значения
         * {@link #getLevelCount}.
         *
         * @param rowIndex индекс ряда, для которого определяется уровень
         *      группирования.
         * @return номер уровня. Значение >= 0.
         */
        public int getLevel(int rowIndex) {
            DataRow row = getRow(rowIndex);
            int d = row.getState().getDepth();
            if ( isDetail(row) )
                d = levelCount+1;
            return d;
        }

        public int getLevel(DataRow row) {
            int d = row.getState().getDepth();
            if ( this.isDetail(row) ) {
                d = this.levelCount+1;
            }

            return d;
        }

        public boolean isDetail( DataRow row ) {
            boolean b = false;
            int d = row.getState().getDepth();
            if ( d == levelCount+1 ) {
                if ( this.includeDetailRows && this.table.getRowCount() > 0 ) {
                    if ( totalsPosition == TOTALS_TOP || totalsPosition == TOTALS_BOTH )
                       b = row != table.getRow(0) ;
                    else if ( totalsPosition == TOTALS_BOTTOM || totalsPosition == TOTALS_BOTH )
                       b = row != table.getRow(table.getRowCount()-1);
                }
            }

            return b;
        }
        /**
         * Создает два массива <code>rowIndice</code> и
         * <code>levelIndice</code>. <p>
         * Изначально массив <code>rowIndice</code> - это массив целых чисел.
         * Элемент i массива содержит значение i - индекс ряда. Тогда, если,
         * например применяем сортировку рядов таблицы и вместе с собственно
         * объектами <code>DataRow</code> сортируем <code>rowIndice</code>.
         * На любом этапе сортировки зная текущий индекс какого либо ряда, мы
         * соответственно знаем текущий индекс элемента <code>rowIndice</code>
         * и тогда нам становится известным индекс ряда в ИСХОДНОМ состоянии.<p>
         *
         * У другого массива levelIndice также элементами явлются целые числа.
         * Если, например, при сортировке, необходимо, зная индекс ряда,
         * определить индекс ряда являющийся агрегатным и находящийся на
         * предыдущем уровне, т.е. на 1 меньше, то выражение levelIndice[rowIndex]
         * является искомым. Если ряд rowIndex является итоговым агрегатным
         * рядом, то levelIndice[rowIndex] == 0. Если ряд rowIndex является
         * агрегатным уровня 1, то levelIndice[rowIndex] == -1.
         */
        public void createIndice() {
            //int levelCount = getLevelCount();
            if (rows == null || rows.getCount() == 0) {
                return;
            }
            levelIndice = new int[this.rows.getCount()];
            rowIndice = new int[this.rows.getCount()];

            int first = (groupMode == TreeBuilder.BEFORE) ? -1 : rows.getCount();
            int last = (groupMode == TreeBuilder.BEFORE) ? rows.getCount() - 1 : 0;
            int step = (groupMode == TreeBuilder.BEFORE) ? 1 : -1;

            int[] priorIndex = new int[levelCount + 1]; //Последний для детальных
            for (int i = 0; i < priorIndex.length; i++) {
                priorIndex[i] = -1;
            }
            //for ( int i=first; i <= last; i += step ) {
            int i = first;
            while (i != last) {
                i += step;
                int level = getLevel(i);
                if (level != 0) {
                    //Игнорируем итоговые
                    if (level == 1) {
                        levelIndice[i] = -1;
                    } else {
                        levelIndice[i] = priorIndex[level - 1];
                    }
                } else {
                    levelIndice[i] = -1;
                }
                rowIndice[i] = i;
                if (level <= levelCount ) // detail row
                    priorIndex[level] = i;
            }


        }

        public int[] getLevelIndice() {
            return levelIndice;
        }

        public int[] getRowIndice() {
            return rowIndice;
        }
    } //class GroupState
} //class