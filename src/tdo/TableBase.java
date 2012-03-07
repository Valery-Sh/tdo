/*
 * BUG LIST
 *   24.05.2008 После создания таблици и добавления ряда addRow(DataRow)
 *              не установлена позиция активной записи
 *              STATUS fixed 24.05.2008
 *   24.05.2008 Когда используется PositionManager и к нему добавляетя
 *              JTextField, то изменения JTextField не проводятся в Table.
 *              STATUS fixed 24.05.2008
 * END BUG LIST
 * TODO List
 * 1. Реализовать метод setFilter, который выполняет сначала отмену фильтров,
 *    а затем установку нового.
 *
 */
package tdo;


import java.sql.ResultSet;
import java.sql.SQLException;
import tdo.event.*;
import tdo.event.TableEvent.TableEventCause;
import tdo.expr.IExpression;
import tdo.expr.RowEvaluator;
import tdo.expr.StartsWithEvaluator;
import tdo.impl.Joiner.Plan;
import tdo.impl.*;

public class TableBase extends SimpleTable {

    /**
     * Контекст выражения, используемый для вычисляемых колонок.
     */
    protected DefaultExpressionContext calcContext; // for calculate method
    /**
     * Схема таблицы.
     */
    /**
     *
     */
    protected Filterer deletedRowFilterer;
//    protected int deletePolicy;
    ColumnValueChangedEvent columnValueChangedEvent = new ColumnValueChangedEvent(this);
    ColumnValueChangedListener updateCalculatedColumnsListener;
    private RowEditingHandler rowEditingHandler;
    private RowEditingEvent rowEditingEvent;

    private TableViewLinker tableViewLinker;

    public TableBase() {
        super();
        setRowEditingHandler(new RowEditingHandler(this));
        if (!(this instanceof TableView)) {
            setTableLinker(new TableViewLinker());
            getTableLinker().addRowEditingListener(this.getRowEditingHandler());
        }

    }

    @Override
    protected InternalView createInternalView() {
        view = new DefaultInternalView(this);
        return view;
        //safeView = dv;
    }
    @Override
    protected InternalView createInternalView(DataRowCollection rc) {
        view = new DefaultInternalView(this,rc);
        return view;
        //safeView = dv;
    }

    /**
     * &nbsp; &nbsp; &nbsp; &nbsp;Проводит инициализацию значений полей и
     * свойств. <p>
     * <ol>
     *    <li>Создает объект внутреннего представления таблицы типа
     *    {@link tdo.impl.InternalViewImpl}
     *    </li>
     *    <li>
     *      Устанавливает значение свойства {@link #deletedHidden} в
     *      <code>false</code>
     *    </li>
     *
     * </ol>
     * Используется {@link tdo.DataTable} и {@link tdo.DataView )
     */
    protected void initTable() {
        createInternalView();
        deletedHidden = false;
        this.updatePolicy = Table.ALLOW_UPDATE_ALL_ROWS;
        loading = false;

        setTableName("Table1");

        rowEditingHandler = new RowEditingHandler(this);
        tableViewLinker = null;
        rowEditingEvent = new RowEditingEvent(this);
    }

    @Override
    public void reset() {
        view.getTableRows().clear();
        columns.clear();
        createInternalView();
        loading = false;
        tableViewLinker.removeRowEditingListener(rowEditingHandler);

        rowEditingHandler = new RowEditingHandler(this);
        tableViewLinker = null;
        rowEditingEvent = new RowEditingEvent(this);

        deletedRowFilterer = null;
        clearActiveRowListeners();
        clearTableListeners();

        deletedHidden = false;
            clearPendingListeners();

        if (validationManager != null) {
            validationManager.clear();
        }

        fireDataModelStructure();

    }
    private void fireDataModelStructure() {
        TableEvent te = new TableEvent(this, TableEvent.ALL_ROWS,TableEvent.ALL_COLUMNS, TableEventCause.schema);
        this.fireDataModel(te);
    }

    public RowEditingHandler getRowEditingHandler() {
        return this.rowEditingHandler;
    }
    public void setRowEditingHandler(RowEditingHandler rowEditingHandler) {
        this.rowEditingHandler = rowEditingHandler;
    }

    public TableViewLinker getTableLinker() {
        return this.tableViewLinker;
    }

    public void setTableLinker(TableViewLinker linker) {
        this.tableViewLinker = linker;
    }


    @Override
    public DataRow createRowCopy(DataRow row) {
        return getRowProvider().createRowCopy(row);
    }

    protected void setView(InternalView view) {
        this.view = view;
    }

    protected void importColumns(Table to, DataColumnCollection from) {
        for (int i = 0; i < from.getCount(); i++) {
            to.getColumns().add((DataColumn) from.get(i).clone());
        }
    }

/*    protected abstract void fireDataModelStructure(Object source); //HEADER_ROW

    protected abstract void fireDataModelAllRows(Object source);

    protected abstract void fireDataModelUpdateRow(Object source, int rowIndex);

    protected abstract void fireDataModelUpdateColumn(Object source, int rowIndex, int columnIndex);

    protected abstract void fireDataModelDeleteRow(Object source, int rowIndex);

    protected abstract void fireDataModelInsertRow(Object source, int rowIndex);

    protected abstract DataColumnCollection createColumns();

    @Override
    public abstract Table createTable();

    //public abstract Table createView();
//    public abstract DataRow createRowCopy(DataRow original);
    @Override
    public abstract DataRow createRow();
*/
    /**
     * Возвращает индекс заданного ряда в в заданной коллекции.
     * Переопределяется реализациями DataTable и DataView.
     * @param whereFilter
     * @param orderBy
     * @return целое число большее или равное 0, если поиск успешен и -1, в
     *  противном случае.
     *
     */
    //public abstract int find(DataRow row, DataRowCollection rc);

    //protected abstract DataRow createRow(Object[] values, int start, int end);
    //protected abstract DataRowProvider getRowProvider();

    //public abstract TableServices getContext();

    //protected abstract void updateCellIndexes();

    /*    protected void addTableView(Table view) {
    if (view instanceof TableView && view == this) {
    return;
    }
    if (tableViewList == null) {
    this.tableViewList = new Vector(2);
    }
    tableViewList.addElement(view);
    }

    protected void removeTableView(Table view) {
    if (tableViewList == null) {
    return;
    }
    tableViewList.remove(view);
    }
     */
    protected Table createTableViewSource(String whereFilter, String orderBy) {
        TableBase dt = (TableBase) createTable();
        importColumns(dt, this.columns);
        DataRowCollection rc = this.getView().getCurrentRows().createShared(this.getContext());
        dt.createInternalView(rc);
       
        //dt.setRows(this.getView().getRows().createShared(this.getContext())); // rows from internal view

        //dt.view = dt.createInternalView();
        if (orderBy != null && orderBy.trim().length() != 0) {
            dt.sort(orderBy);
        }
        if (whereFilter != null && whereFilter.trim().length() != 0) {

            dt.filter(whereFilter);
        }
        dt.filter(new InsertingFilterer(dt.getContext()));

        DataRowCollection rm = dt.view.getCurrentRows();
        dt.cancelSort();
        dt.cancelFilters();
        dt.createInternalView(rm);

        return dt;
    }

    @Override
    public Table createTable() {
        return new TableBase();
    }

/*    public int getColumnCount() {
        return this.getColumns().getCount();
    }
*/
    public int getColumnCount(int kind) {
        return this.getColumns().getCount(kind);
    }

/*    public String getColumnName(int columnIndex) {
        return this.getColumns().get(columnIndex).getName();
    //return "c" + Integer.toString(columnIndex);
    }
*/
/*    public int indexOfColumn(String columnName) {
        return getColumns().find(columnName);
    }
*/

    public Object getValue(int rowIndex, int columnIndex) {

        return getRow(rowIndex).getValue(columnIndex);
    }

    public Object getValue(int rowIndex, String columnName) {
        return getRow(rowIndex).getValue(columnName);
    }

    @Override
    public Object calculateColumnValue(int rowIndex, int columnIndex) {
        Table dt = null;
        DataColumn dc = this.getColumns().get(columnIndex);
        ColumnExpressionContext expr = dc.getColumnExpression();
        if (expr != null) {
            if (expr.getTableServices() == null) {
                expr.addTableServices(this.getContext());
            }
            //dt = expr.getDataTable();
            return expr.getValue(getRow(rowIndex));
        }


        Object result;// My 06.03.2012 = this.getColumns().get(columnIndex).createBlankObject();
        result = fireCalculateColumnValue(this, getRow(rowIndex), rowIndex, columnIndex);
        if (result == null) {
            return this.getColumns().get(columnIndex).createBlankObject();
        }
        return result;
    }

    @Override
    public Object calculateColumnValue(DataRow row, int columnIndex) {

        Table dt = null;
        DataColumn dc = this.getColumns().get(columnIndex);
        ColumnExpressionContext expr = dc.getColumnExpression();
        if (expr != null) {
            if (expr.getTableServices() == null) {
                expr.addTableServices(this.getContext());
            }
            return expr.getValue(row);
        }
        Object result;//My 06.03.2012 = this.getColumns().get(columnIndex).createBlankObject();
        result = fireCalculateColumnValue(this, row, -1, columnIndex);
        if (result == null) {
            return this.getColumns().get(columnIndex).createBlankObject();
        }
        return result;
    }


    public void setValue(Object aValue, int rowIndex, String columnName) {
        setValue(aValue, rowIndex, this.getColumns().find(columnName));
    }

    public void setValue(Object aValue, int rowIndex, int columnIndex) {
        DataRow row = getRow(rowIndex);
        if (rowIndex == this.activeRow) {
            row.beginEdit();
        }
        row.setValue(aValue, columnIndex);
    }

    public void addColumnValueChangedListener(ColumnValueChangedListener l) {
        this.updateCalculatedColumnsListener = l;
    }

    public void removeColumnValueChangedListener(ColumnValueChangedListener l) {
        this.updateCalculatedColumnsListener = null;
    }

    protected Object fireCalculateColumnValue(Object source, DataRow row, int rowIndex, int columnIndex) {

        if (updateCalculatedColumnsListener == null) {
            return null;
        }

        DataColumn column = this.getColumns().get(columnIndex);
        columnValueChangedEvent.setSource(source);

        columnValueChangedEvent.setColumn(column);
        columnValueChangedEvent.setColumnIndex(columnIndex);
        columnValueChangedEvent.setRowIndex(rowIndex);
        columnValueChangedEvent.setRow(row);

        return updateCalculatedColumnsListener.calculateColumnValues(columnValueChangedEvent);
    }

    /////////////////////////////////////

    protected int internalMoveTop() {
        if (this.getRowCount() == 0) {
            return internalMoveTo(-1);
        } else {
            return internalMoveTo(0);
        }
    }
/*    protected int internalMoveTop() {
        if (this.getRowCount() == 0) {
            return internalMoveTo(-1);
        } else {
            return internalMoveTo(0);
        }
    }
*/
    protected int internalMoveTo(int newIndex) {
        int old = this.activeRow;
        this.activeRow = newIndex;
        fireActiveRowChange(old, newIndex);
        return newIndex;
    }

/*
    public int moveTo(int newIndex) throws ValidateException {
        if (newIndex == getActiveRowIndex()) {
            return newIndex;
        }

        if (newIndex >= this.getRowCount()) {
            newIndex = this.getRowCount() - 1;
        } else {
            if (newIndex < 0) {
                newIndex = 0;
            }
        }

        setActiveRowIndex(newIndex);
        return newIndex;
    }

*/


/*
    protected DataRowCollection getRows() {
        return this.rows;
    }
*/

/*
    public DataRow getLastRow() {
        return getRowCount() == 0 ? null : this.view.getRow(getRowCount() - 1);
    }
*/
    /**
     *
     * @param row
     * @return
     */
/*    @Override
    public int find(DataRow row) {
        return this.view.find(row);
    }
*/

/*    @Override
    public void fireRowEditing(int kind, DataRow row) {
        RowEditingEvent e = new RowEditingEvent(this, kind, row, -1);
        getTableLinker().fireRowEditing(e);

    }
*/
    @Override
    public boolean processRowEditing(int kind, DataRow row, int columnIndex) {
        RowEditingEvent e = new RowEditingEvent(this, kind, row, columnIndex);
        getTableLinker().fireRowEditing(e);
        return true;
    }


    public void createColumns(String[] columnName, Object[] values) {
        //DataColumnCollection columns = this.getColumns();

        for (int i = 0; i < values.length; i++) {
            getColumns().add(values[i].getClass(), columnName[i]);
        }
    }

    public void createColumns(String[] columnName, Class[] classes) {
        //DataColumnCollection columns = this.getColumns();

        for (int i = 0; i < classes.length; i++) {
            getColumns().add(classes[i], columnName[i]);
        }
    }

    private DataColumn createColumn(Object value) {
        //DataColumnCollection columns = this.getColumns();
        return getColumns().add(value.getClass());
    }

    private DataColumn createColumn(String columnName, Object value) {
        //DataColumnCollection columns = this.getColumns();
        return getColumns().add(value.getClass(), columnName);
    }

    /**
     * Создает для заданных массивов имен колонок и значений новый ряд
     * и добавляет его в коллекцию рядов таблицы.
     *
     * Обычно метод применяется для вновь созданной таблицы, не имеющей ни
     * одного ряда и у которой еще не определена схема колонок, т.е метод
     * {@link #getColumnCount()} возвращает 0. В этом случае схема колонок
     * создается на основе типов элементов массива автоматически.
     * <p>Метод может быть применен к таблице, уже имеющей один или более рядов
     * и/или с готовой схемой колонок. В таком случае, схема колонок не
     * создается и не изменяется. Следует осторожно применять метод к таблице
     * с готовой схемой, с тем, чтобы элементы массива по типам соответствовали
     * колонкам.
     * <p>Создается новый ряд и его ячейки заполняются данными из массива.
     * При этом, значение нулевого элемента массива соответствует
     * нулевой колонке и <code><i>i-го</i></code> соответствует i-ой
     * колонке.
     * <p>Массив имен колонок <code>names</code> не должен быть null и иметь не
     *  нулевую длину и его размер должен быть меньше или равен размеру массива
     * значений.
     * <p>Массив значений <code>values</code> не должен быть null
     * <p>Длина массива имен колонок <code>names</code> должна быть менше или
     * равно длине массива значений.
     *
     * @param names
     * @param values
     * @return
     * @throws IllegalArgumentException если один из параметров равен null
     *    или длина массива имен больше длины массива значений или длина
     *    массива имен равна 0
     *
     */
    public DataRow addRow(String[] names, Object[] values) {
        if (editProhibited) {
            return null;
        }
        if (names == null || values == null || names.length > values.length ||
                names.length == 0) {
            throw new IllegalArgumentException("Illegal parameter valuue(s). " +
                    " Must be " +
                    "( names != null && values != null && names.length <= values.length && names.length !=0");
        }
        if (getColumns().getCount() == 0) {
            for (int i = 0; i < names.length; i++) {
                DataColumn dc = createColumn(values[i]);
                dc.setName(names[i]);
            }
        }
        return this.addRow(values);
    }

/*    public int getViewKind(DataRowCollection store) {
        return view.getKind(store);
    }
*/
    @Override
    public DataColumnCollection getColumns() {
        return columns;
    }

    public DataColumn getColumn(int columnIndex) {
        return columns.get(columnIndex);
    }

    public DataColumn getColumn(String columnName) {
        return columns.get(columnName);
    }

/*
    public int locate(String columnName, Object value) {
        RowEvaluator p = new StartsWithEvaluator(this, columnName, value);

        Finder f = new Finder(this);
        f.setPredicate(p);
        int r = f.first();
        return r;
    }

    public int locate(String columnName, Object value, boolean caseInsensitive) {
        StartsWithEvaluator p = new StartsWithEvaluator(this, columnName, value);
        p.setCaseSensitive(caseInsensitive);
        Finder f = new Finder(this);
        f.setPredicate(p);
        int r = f.first();

        return r;
    }
 */ 
    protected int findInternal(DataRow row) {
        int rowIndex = getActiveRowIndex();
        DataRow aRow = rowIndex < 0 ? null : getRow(rowIndex);
        if (aRow == null || aRow != row) {
            rowIndex = find(row);
        }
        return rowIndex;
    }

/*    protected int findNoCase(String columnName, String value, int startFrom) {
        int r = -1;
        if (getRowCount() == 0) {
            return -1;
        }
        if (startFrom + 1 >= getRowCount()) {
            return -1;
        }
        int columnIndex = this.columns.find(columnName);
        if (columnIndex < 0) {
            return r;
        }
        for (int i = startFrom + 1; i < getRowCount(); i++) {
            Object o = this.getValue(i, columnIndex);
            if (o == null && value == null) {
                r = i;
                break;
            }
            if (o == null || value == null) {
                continue;
            }
            String s = o.toString().toUpperCase();
            if (s.equals(value.toUpperCase())) {
                r = i;
                break;
            }
        } //for

        return r;
    }

    protected Finder getFinder(String columnList, Object[] values, boolean caseSensitive) {
        int r = -1;
        if (columnList == null || values == null || columnList.length() == 0 || values.length == 0) {
            return null;
        }
        String s = Strings.compressSpaces(columnList.toUpperCase());
        String[] names = Strings.split(s);
        if (names.length != values.length) {
            return null;
        }
        PAND c = new PAND();
        for (int i = 0; i < names.length; i++) {
            FinderEqualsEvaluator p = new FinderEqualsEvaluator(this, names[i], values[i]);
            p.setCaseSensitive(caseSensitive);
            c.add(p);
        }
        Finder f = new Finder(this);
        f.setPredicate(c);
        return f;
    }

    protected int findCase(String columnList, Object[] values, boolean caseSensitive) {
        Finder f = getFinder(columnList, values, caseSensitive);
        if (f == null) {
            return -1;
        }
        int r = f.first();
        return r;
    }

    protected int findNextCase(int startFrom, String columnList, Object[] values, boolean caseSensitive) {
        Finder f = getFinder(columnList, values, caseSensitive);
        if (f == null) {
            return -1;
        }
        int r = f.nextAfter(startFrom);

        return r;
    }
*/
    /**
     * Возвращает индекс найденного ряда. <p>
     * Для строковых колонок при сравнении регистр игнорируется как для значений
     * в ячейках таблицы, так и для строковых значений элементов из <code>values</code>.
     * @param columnName
     * @param value
     * @return
     */
  /*  public int find(String columnName, Object value) {
        RowEvaluator p = new FinderEqualsEvaluator(this, columnName, value);
        Finder f = new Finder(this);
        f.setPredicate(p);
        int r = f.first();
        return r;
    }

    public int find(String columnName, Object value, boolean caseInsensitive) {
        if (value == null || (!(value instanceof String))) {
            return find(columnName, value);
        }
        if (caseInsensitive) {
            return findNoCase(columnName, (String) value, -1);
        } else {
            return find(columnName, value);
        }
    }

    public int findNext(int currentRow, String columnName, Object value) {
        RowEvaluator p = new FinderEqualsEvaluator(this, columnName, value);
        Finder f = new Finder(this);
        f.setPredicate(p);
        int r = f.nextAfter(currentRow);
        return r;
    }

    public int findNext(int currentRow, String columnName, Object value, boolean caseInsensitive) {
        if (value == null || (!(value instanceof String))) {
            return findNext(currentRow, columnName, value);
        }
        if (caseInsensitive) {
            return findNoCase(columnName, (String) value, currentRow);
        } else {
            return findNext(currentRow, columnName, value);
        }
    }

    public int find(String columnList, Object[] values) {
        return findCase(columnList, values, true);
    }

    public int find(String columnList, Object[] values, boolean caseInsensitive) {
        if (caseInsensitive) {
            return findCase(columnList, values, false);
        } else {
            return findCase(columnList, values, true);
        }
    }

    public int findNext(int currentRow, String columnList, Object[] values) {
        int r = -1;
        r = findNextCase(currentRow, columnList, values, true);
        return r;
    }

    public int findNext(int currentRow, String columnList, Object[] values, boolean caseInsensitive) {
        int r = -1;
        if (caseInsensitive) {
            r = findNextCase(currentRow, columnList, values, false);
        } else {
            r = findNextCase(currentRow, columnList, values, true);
        }
        return r;
    }

    public int findLast(String columnList, Object[] values) {
        int r = -1;
        Finder f = getFinder(columnList, values, true);
        if (f == null) {
            return -1;
        }
        r = f.last();
        return r;
    }

    public int findLast(String columnList, Object[] values, boolean caseInsensitive) {
        int r = -1;

        Finder f = null;
        if (caseInsensitive) {
            f = getFinder(columnList, values, false);
        } else {
            f = getFinder(columnList, values, true);
        }
        if (f == null) {
            return -1;
        }
        r = f.last();
        return r;
    }
*/

    public Filterer prepareFilter(String expression) {
        if (expression == null || expression.length() == 0) {
            return null;
        }
        Filterer filterer = new DefaultFilterer(this.getContext(),expression);
        return filterer;
    }

    public Filterer filter(String expression) {
        if (expression == null || expression.length() == 0) {
            return null;
        }
        Filterer filterer = new DefaultFilterer(this.getContext(),expression);
        
        filter(filterer);
        return filterer;
    }

    public void filter(Filterer newfilterer) {
        if (newfilterer == null) {
            return;
        }
        //((FilterView)this.view).filter(newfilterer);
        if ( this.view.getViewManager().filter(newfilterer) ) {
            internalMoveTop();
            TableEvent te = new TableEvent(this, TableEvent.ALL_ROWS,TableEvent.ALL_COLUMNS, TableEventCause.schema);
            fireDataModel(te);
        }
    }

    public Filterer filter(RowEvaluator predicate) {
        if (predicate == null) {
            return null;
        }
        Filterer filterer = new DefaultFilterer(this.getContext());
        filterer.setPredicate(predicate);
        filter(filterer);
        return filterer;
    }

    public void refresh() {
    }

    public Finder getFinder(String expression) {
        if (expression == null || expression.length() == 0) {
            return null;
        }
        Finder finder = new Finder(this);
        finder.setExpression(expression);

        return finder;
    }

    public RowLocator getLocator(String columnName) {
        DataColumn dc = this.getColumns().get(columnName);
        RowEvaluator p = new StartsWithEvaluator(this, columnName, null);
        RowLocator f = new RowLocator(this.getContext());
        f.setPredicate(p);
        return f;
    }

    public Sorter sort(String columnList) {
        return this.sort(columnList, Sorter.ASCENDING);
    }

    public Sorter sort(String columnList, boolean direction) {
        if (columnList == null || columnList.length() == 0) {
            return null;
        }
        Sorter sorter = new DefaultSorter(this.getContext());
        sorter.setColumnList(columnList);
        sorter.setSortDirection(direction);
        this.view.getViewManager().sort(sorter);
        internalMoveTop();
     //   fireDataModelAllRows(this);
        this.fireDataModelStructure();
        return sorter;
    }

    public String getSortColumnList() {
        if ( view.getViewManager().getCurrentSorter() == null) {
            return null;
        }
        return view.getViewManager().getCurrentSorter().getColumnList();
    }

    public boolean getSortDirection() {
        return view.getViewManager().getCurrentSorter().getSortDirection();
    }


    public void reverse(int first, int last) {
        if (getRowCount() < 2) {
            return;
        }
        int start = first;
        int end = last;
        //int n = getRowCount() / 2;
        int n = (last - first + 1) / 2 + first;
        for (int i = first; i < n; i++) {
            DataRow r1 = getRow(i);
            view.setRow(getRow(end), start);
            view.setRow(r1, end);
            start++;
            end--;
        }
    }

    public boolean isSorted() {
        return this.view.getViewManager().getCurrentSorter() == null ? false : true;
    }

    public boolean isFiltered() {
        Filterer[] f = this.view.getViewManager().getFilterers();
        boolean b = ( f == null || f.length == 0 ) ? false : true;
        return b;
    }

    public void cancelSort(Sorter sorter) {
        this.view.getViewManager().cancelSort(sorter);
        internalMoveTop();
    //    fireDataModelAllRows(this);
        this.fireDataModelStructure();
    }

    public void cancelSort() {
        this.view.getViewManager().cancelSort();
        internalMoveTop();
      //  fireDataModelAllRows(this);
        this.fireDataModelStructure();
    }

    public void cancelFilter(Filterer filterer) {
        this.view.getViewManager().cancelFilter(filterer);
        internalMoveTop();
        this.fireDataModelStructure();
        //fireDataModelAllRows(this);
    }

    public void cancelFilters() {
        this.view.getViewManager().cancelFilters();
        internalMoveTop();
        //fireDataModelAllRows(this);
        this.fireDataModelStructure();
    }

    public Filterer[] getFilterers() {
        return  this.view.getViewManager().getFilterers();
    }



    public Relation setRelation(Table parentTable, String keyList, String parentKeyList) {
        Relation relation = new DefaultRelation(this, parentTable,keyList,parentKeyList);
        if ( this.view.getViewManager().setRelation(relation) ) {
            TableEvent te = new TableEvent(this, TableEvent.ALL_ROWS,TableEvent.ALL_COLUMNS, TableEventCause.schema);
            fireDataModel(te);
        }
        return relation;
    }
    public void stopRelation() {
        this.view.getViewManager().setRelation(null);
        TableEvent te = new TableEvent(this, TableEvent.ALL_ROWS,TableEvent.ALL_COLUMNS, TableEventCause.schema);
        fireDataModel(te);
    }
    @Override
    public boolean isDeletedHidden() {
        return this.deletedHidden;
    }

    public void setDeletedHidden(boolean value) {
        boolean oldValue = this.deletedHidden;
        if (value == deletedHidden) {
            return;
        }
        deletedHidden = value;
        if (oldValue) {
            restoreDeleted();
        } else {
            hideDeleted();
        }
        this.internalMoveTop();
        this.firePropertyChange("deletedHidden", oldValue, value );
    }

    protected void hideDeleted() {
        deletedRowFilterer = new DeletedFilterer(this.getContext());
        this.filter(deletedRowFilterer);
    }

    protected void restoreDeleted() {
        this.cancelFilter(deletedRowFilterer);
    }
    protected void fireDataModelAllRows(Object source) {
        fireTable(new TableEvent(this, TableEvent.ALL_ROWS,TableEvent.ALL_COLUMNS, TableEventCause.update));
    }
    public DataRow createRow(Object obj) {
        return this.rowProvider.createRow(obj);
    }

    // *********** Aggregates *********************

    public TreeTable createTreeTable(String columns, String groupColumns, int totals, boolean includeDetails, boolean groupMode) {
        String oldColumns = null;
        boolean oldDir = true;
        if (groupColumns != null) {
            if (view.getViewManager().getCurrentSorter() != null) {
                oldColumns = view.getViewManager().getCurrentSorter().getColumnList();
                oldDir = view.getViewManager().getCurrentSorter().getSortDirection();
            }
            cancelSort();
            sort(groupColumns);
        }
        TreeBuilder tbuilder = new TreeBuilder(this);
        tbuilder.setColumnList(columns);
        tbuilder.setSortColumnList(view.getViewManager().getCurrentSorter().getColumnList());
        tbuilder.setSortDirection(view.getViewManager().getCurrentSorter().getSortDirection());
        tbuilder.setIncludeDetailRows(includeDetails);
        tbuilder.setTotalsPosition(totals);
        tbuilder.setGroupMode(groupMode);

        Table table = tbuilder.createTargetTreeTable();

        tbuilder.execute();

        if (oldColumns != null) {
            sort(oldColumns, oldDir);
        }

        return (TreeTable)table;
    }
    /**
     * Создает и возвращает новую тавлицу, с заданной схемой колонок и сгруппированными
     * по полям из <code>groupyColumns</code> рядами, вычисленными
     * на основании рядов исходной таблицы, удовлетворяющих заданному критерию.<p>
     *
     * В отличие от метода {@link #select}, список <code>groupColumns</code> рассматривается как
     * множество списков "group by", например, если groupColumns = "a1,a2,a3,a4", то группировка
     * рядов призводится для последовательностей:
     * <ol>
     *   <li>a1,a2,a3,a4</li>
     *   <li>a1,a2,a3</li>
     *   <li>a1,a2</li>
     *   <li>a1</li>
     * </ol>
     *
     * Таким образом, в результирующую таблицу попадает агрегированный ряд при  изменении
     * каждой из указанных выше последовательностей.<p>
     *
     * Если список колонок содержит хотя бы одну агрегатную функцию, то результ будет содержать
     * один ряд для каждой группы. <p>
     *
     * Если список колонок НЕ содержит агрегатных функций, то результ будет содержать
     * один ряд для каждой группы, при этом, заполненными в результирующей таблице будут только колонки,
     * соответствующие колонкам из списка <code>groupColumns</code>, а также колонки, созданные
     * с использованием выражений.
     *
     *
     * @param columns список элементов, каждый из которых - это имя исходной колонки или выражение.
     *    После имени колонки или выражения может следовать модификатор " AS " с последующим <i>новым</i>
     *    именем колонки. Выражение может являться агрегатной функцией.
     * @param groupColumns список колонок группирования
     * @return новая таблица с заданной схемой колонок и рядами, вычисленными
     *         на основании рядов исходной таблицы, удовлетворяющих заданному критерию и сгруппированными
     *         по значениям из <code>groupColumns</code>.
     */
    public TreeTable createTreeTable(String columns, String groupColumns) {
        return createTreeTable(columns, groupColumns, TreeBuilder.TOTALS_BOTH, false, TreeBuilder.BEFORE);
    }

    /**
     * Создает и возвращает новую тавлицу, с заданной схемой колонок и сгруппированными
     * по полям из <code>groupyColumns</code> рядами, вычисленными
     * на основании рядов исходной таблицы, удовлетворяющих заданному критерию.<p>
     *
     * В отличие от метода {@link #select}, список <code>groupColumns</code> рассматривается как
     * множество списков "group by", например, если groupColumns = "a1,a2,a3,a4", то группировка
     * рядов призводится для последовательностей:
     * <ol>
     *   <li>a1,a2,a3,a4</li>
     *   <li>a1,a2,a3</li>
     *   <li>a1,a2</li>
     *   <li>a1</li>
     * </ol>
     *
     * Таким образом, в результирующую таблицу попадает агрегированный ряд при  изменении
     * каждой из указанных выше последовательностей.<p>
     *
     * Если список колонок содержит хотя бы одну агрегатную функцию, то результ будет содержать
     * один ряд для каждой группы. <p>
     *
     * Если список колонок НЕ содержит агрегатных функций, то результ будет содержать
     * один ряд для каждой группы, при этом, заполненными в результирующей таблице будут только колонки,
     * соответствующие колонкам из списка <code>groupColumns</code>, а также колонки, созданные
     * с использованием выражений.
     *
     *
     * @param columns список элементов, каждый из которых - это имя исходной колонки или выражение.
     *    После имени колонки или выражения может следовать модификатор " AS " с последующим <i>новым</i>
     *    именем колонки. Выражение может являться агрегатной функцией.
     * @param groupColumns список колонок группирования
     * @param includeDetails <code>true</code> если в результирующую таблицу выводятся детальные ряды.
     *  <code>false</code> - в противном случае
     * @return новая таблица с заданной схемой колонок и рядами, вычисленными
     *         на основании рядов исходной таблицы, удовлетворяющих заданному критерию и сгруппированными
     *         по значениям из <code>groupColumns</code>.
     */
    public TreeTable createTreeTable(String columns, String groupColumns, boolean includeDetails) {
        return createTreeTable(columns, groupColumns, TreeBuilder.TOTALS_BOTH, includeDetails, TreeBuilder.BEFORE);
    }

    /**
     * Создает и возвращает новую тавлицу, с заданной схемой колонок и сгруппированными
     * по полям из <code>groupyColumns</code> рядами, вычисленными
     * на основании рядов исходной таблицы, удовлетворяющих заданному критерию.<p>
     *
     * В отличие от метода {@link #select}, список <code>groupColumns</code> рассматривается как
     * множество списков "group by", например, если groupColumns = "a1,a2,a3,a4", то группировка
     * рядов призводится для последовательностей:
     * <ol>
     *   <li>a1,a2,a3,a4</li>
     *   <li>a1,a2,a3</li>
     *   <li>a1,a2</li>
     *   <li>a1</li>
     * </ol>
     *
     * Таким образом, в результирующую таблицу попадает агрегированный ряд при  изменении
     * каждой из указанных выше последовательностей.<p>
     *
     * Если список колонок содержит хотя бы одну агрегатную функцию, то результ будет содержать
     * один ряд для каждой группы. <p>
     *
     * Если список колонок НЕ содержит агрегатных функций, то результ будет содержать
     * один ряд для каждой группы, при этом, заполненными в результирующей таблице будут только колонки,
     * соответствующие колонкам из списка <code>groupColumns</code>, а также колонки, созданные
     * с использованием выражений.
     *
     *
     * @param columns список элементов, каждый из которых - это имя исходной колонки или выражение.
     *    После имени колонки или выражения может следовать модификатор " AS " с последующим <i>новым</i>
     *    именем колонки. Выражение может являться агрегатной функцией.
     * @param groupColumns список колонок группирования
     * @param includeDetails <code>true</code> если в результирующую таблицу выводятся детальные ряды.
     *  <code>false</code> - в противном случае
     * @param groupMode принимает булевое значение {@link TreeBuilder#AFTER} или  {@link TreeBuilder#BEFORE}.
     *    В первом случае ( значение по умолчанию для перегруженных методов не содержащих такого параметра)
     *    в результирующую таблицу детальные ряды попадают прежде агрегатных, во втором вначале выводится
     *    агрегатный ряд, затем соответствующие ему детальные ряды.
     * @return новая таблица с заданной схемой колонок и рядами, вычисленными
     *         на основании рядов исходной таблицы, удовлетворяющих заданному критерию и сгруппированными
     *         по значениям из <code>groupColumns</code>.
     */
    public TreeTable createTreeTable(String columns, String groupColumns, boolean includeDetails, boolean groupMode) {
        return createTreeTable(columns, groupColumns, TreeBuilder.TOTALS_BOTH, includeDetails, groupMode);
    }

    public TreeTable createTreeTable(String columns, String groupColumns, int totals) {
        return createTreeTable(columns, groupColumns, totals, false, TreeBuilder.BEFORE);
    }

    public TreeTable createTreeTable(String columns, String groupColumns, int totals, boolean includeDetails) {
        return createTreeTable(columns, groupColumns, totals, includeDetails, TreeBuilder.BEFORE);
    }


    /**
     * Создает новую таблицу и заполняет ее рядами данной таблицы, удовлетворяющими
     * заданному критерию и, если необходимо, сгруппированными по заданному списку полей. <p>
     * При использовании метода необходимо принимать во внимание следующие особенности:
     * <OL>
     *   <LI>
     *      Список колонок <code>columns</code> не содержит ни одной агрегатной функции. В этом случае
     *      возможны два варианта:
     *      <UL>
     *          <LI>Параметр <code>groupByColumns</code> равен <code>null</code>. В этом случае,
     *              группирования рядов не производится. Результирующая таблица содержит ряды,
     *              с колонками, согласно <code>columns</code>, удовлетворяющие условию фильтра
     *              <code>whereFilter</code>.
     *          </LI>
     *          <LI>Параметр <code>groupByColumns</code> содержит список колонок. В этом случае,
     *              группирования рядов не производится. Результирующая таблица содержит ряды,
     *              удовлетворяющие условию фильтра. Колонка рядов результата, содержат значение,
     *              только, если эта колонка является колонкой из списка <code>groupByColumns</code>
     *              или колонка определена в списке <code>columns</code> как выражение.
     *              <code>whereFilter</code>.
     *          </LI>
     *      </UL>
     *
     *   </LI>
     *   <LI>
     *      Список колонок <code>columns</code> содержит хотя бы  одну агрегатную функцию. В этом случае
     *      возможны два варианта:
     *      <UL>
     *          <LI>Параметр <code>groupByColumns</code> равен <code>null</code>. В этом случае,
     *              результирующая таблица содержит только один ряд ( или 0 рядов), как результат
     *              группирования всех рядов исходной таблицы удовлетворяющих условию фильтра
     *              <code>whereFilter</code>.
     *          </LI>
     *          <LI>Параметр <code>groupByColumns</code> содержит список колонок.
     *              Результирующая таблица содержит ряды,
     *              удовлетворяющие условию фильтра <code>whereFilter</code>, сгруппированные
     *              по полям списка <code>groupByColumns</code>.
     *          </LI>
     *      </UL>
     *
     *   </LI>
     * </OL>
     * @param columns список выражений, каждое из которых описывает колонку новой таблицы
     * @param whereFilter булевое выражение, представляющее фильтр рядов для исходной таблицы
     * @param groupByColumns список колонок исходной таблицы, используемый при группировании ее рядов
     * @param totals
     * @param includeDetails
     * @param groupMode
     * @return
     */
    protected Table selectBase(String columns, String whereFilter, String groupByColumns, int totals, boolean includeDetails, boolean groupMode) {
        //BaseDataTable source = screateBaseTableView(whereFilter, groupByColumns); //2-й параметр - поля сортировки для метода
        Table source = (Table) this.createTableViewSource(whereFilter, groupByColumns);
        //source.open();
        Selector s = new Selector(source);
        s.setSortColumnList(groupByColumns);
        String columnList = "";
        if (columns == null || columns.equals("*")) {
            DataColumnCollection sc = this.getColumns();
            String c = "";
            for (int i = 0; i < sc.getCount(); i++) {
                columnList += (c + sc.get(i).getName());
                c = ",";
            }
        } else {
            columnList = columns;
        }

        s.setColumnList(columnList);
        Table table = s.createTargetTable();

        s.setIncludeDetailRows(includeDetails);
        s.setTotalsPosition(totals);
        s.setGroupMode(groupMode);

        if ((!s.hasAggFunction()) && groupByColumns == null) {
            s.setIncludeDetailRows(true);
        }
        s.execute();

        return table;
    } //select()


    public Table joinBase(Table joinTable, String columns, String onExpr, String whereExpr, Plan plan) {
        Joiner s = new Joiner(this, joinTable, onExpr, whereExpr, plan);
        String columnList = "";
        if (columns == null) {
            DataColumnCollection sc = this.getColumns();
            String c = "";
            for (int i = 0; i < sc.getCount(); i++) {
                columnList += (c + sc.get(i).getName());
                c = ",";
            }
        } else {
            columnList = columns;
        }
        s.setColumnList(columnList);
        Table table = s.createTargetTable(joinTable);
        s.execute();

        return table;
    } //select()

    public Object[][] toObjectArray() {
        Object[][] a = new Object[getRowCount()][getColumns().getCount()];
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumns().getCount(); j++) {
                a[i][j] = getValue(i, j);
            }
        }
        return a;
    }

    /**
     * !!! Ряд строится вместе с calculated колонками
     * @return
     */
    /*    protected Object[][] getDataArray() {
    int ar = this.getActiveRowIndex();
    if (ar >= 0) {
    try {
    RowState rs = this.getRow(ar).getState();
    if (rs.isEditing()) {
    getRow(ar).endEdit();
    }
    } catch (Exception ex) {
    return null;
    }
    }

    RowState[] rsa = null;
    int h = 0;
    if (!(rsa == null || rsa.length == 0)) {
    for (int i = 0; i < rsa.length; i++) {
    if (rsa[i].isDeleted() || rsa[i].isUpdated()) {
    h++;
    }
    }
    }
    h += getRows().getCount();

    int w = getColumnCount() + 4;
    int r = -1;
    Object[][] a = new Object[h][w];
    int start = w - getColumnCount();
    //int columnCount = getColumns().getCount(DataColumn.DATA_KIND);
    for (int i = 0; i < getRows().getCount(); i++) {
    r++;

    // заносим ячейки ряда
    for (int j = start; j < getColumnCount(); j++) {
    a[r][j] = getRows().getValue(i, j - start);
    }
    // формируем RowState
    DataRow row = getRows().get(i);
    RowState rs = row.getState();
    if (!rs.isLoaded()) {
    //                rs = new RowState();
    //                rs.setEditingState(RowState.NONE); // editingState
    //                rs.setOriginState(RowState.NONE); // originState
    //rs.setRowIndex(i); //rowIndex
    //rs.setOrder(0); //order
    }
    a[r][0] = rs.getEditingState();
    //a[r][1] = rs.getOriginState();
    //a[r][2] = rs.getRowIndex();
    //a[r][3] = rs.getOrder();
    // Для UPDATED ( !!! но не для MANMADE) заносим также
    // следующей строкой оригинальный ряд
    if (rs.getEditingState() != RowState.LOADED && rs.getEditingState() != RowState.MANMADE) {

    DataRow orow = rs.getOriginalRow();
    if (orow != null) {
    r++;
    for (int j = start; j < getColumnCount(); j++) {
    a[r][j] = orow.getValue(j - start);
    }
    }
    }
    }
    // Формируем строки, соответствующие удаленным рядам
    //        RowState[] rsa = getRows().getChanges();
    if (rsa == null || rsa.length == 0) {
    return a;
    }
    for (int i = 0; i < rsa.length; i++) {
    if (rsa[i].getEditingState() == Integer.valueOf(RowState.DELETED)) {
    r++;
    a[r][0] = rsa[i].getEditingState();
    //  a[r][1] = rsa[i].getOriginState();
    //a[r][2] = rsa[i].getRowIndex();
    //a[r][3] = rsa[i].getOrder();
    DataRow orow = rsa[i].getOriginalRow();
    for (int j = start; j < getColumnCount(); j++) {
    a[r][j] = orow.getValue(j - start);
    }
    }
    }
    return a;
    }
     */
    protected void populate(Object[] schema, Object[][] cells) {
        DataColumnCollection dcs = this.getColumns();
        dcs.clear();
        for (int i = 0; i < schema.length; i++) {
            dcs.add((DataColumn) schema[i]);
        }
        for (int i = 0; i < cells.length; i++) {
            this.addRow(cells[i]);
        }
    }

    protected void populate(ResultSet resultSet) throws SQLException {
        //DataAdapter adapter = new DataAdapter();
        //adapter.populate(this, resultSet);
        ResultSetHelper.populate(this, resultSet);
    } //populate()

    /**
     * Вычисляет заданное выражение для заданного ряда.
     *
     * Удобный метод, который предназначен для пользователя.
     *
     * @param row ряд, для которого производятся вычисления. Если в выражении
     *   нет оперендов-колонок, то row параметр может быть любым, в том числе и
     *   <code>null</code>.
     * @param expr вычисляемое выражение
     * @return вычисленное значение выражения
     */
    public Object calculate(DataRow row, String expr) {
        if (this.calcContext == null) {
            this.calcContext = new DefaultExpressionContext();
            this.calcContext.addTableServices(this.getContext());
        }
        this.calcContext.setExpressionString(expr);
        IExpression e = this.calcContext.createExpression();
        return e.getValue(row);
    }

    public Object calculate(String paramName, Object param, String expr) {
        if (this.calcContext == null) {
            this.calcContext = new DefaultExpressionContext();
            this.calcContext.addTableServices(this.getContext());
        }
        this.calcContext.setParameter(paramName, param);
        this.calcContext.setExpressionString(expr);
        IExpression e = this.calcContext.createExpression();
        return e.getValue();
    }


    public static class RowEditingHandler implements RowEditingListener {

        private Table table;

        public RowEditingHandler(Table table) {
            this.table = table;
        }

        @Override
        public void processRowEditing(RowEditingEvent e) {
            Table t = (Table)e.getSource();
            if (e.getKind() == DataRow.CANCELEDIT_INSERTING && table != t) {
                return;
            }
            if (e.getColumnIndex() < 0) {
                ((SimpleTable)table).dispatchRowEditing(e.getKind(), e.getRow());
            } else {
                ((SimpleTable)table).dispatchRowEditing(e.getKind(), e.getRow(), e.getColumnIndex());
            }
        }

        public Table getTable() {
            return this.table;
        }
    } //class RowEditingHandler
} //class BaseDataTable