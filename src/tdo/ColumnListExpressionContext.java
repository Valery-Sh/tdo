/*
 * ColumnListExpressionContext.java
 */
package tdo;

import java.sql.ResultSetMetaData;
import tdo.expr.CompoundExpression.CommaListExpression;
import tdo.expr.FunctionExpression.FunctionConvertExpression;
import tdo.expr.*;
import tdo.impl.TreeBuilder.SortColumnInfo;
import tdo.service.TableServices;

/**
 * Расширяет функциональность класса {@link tdo.DefaultExpressionContext} для использования
 * его при обработке выражений типа {@link tdo.expr.CompoundExpression.CommaListExpression} представляющих
 * список колонок, например, подобных <code>SQL SELECT</code>. <p>
 */
public class ColumnListExpressionContext extends DefaultExpressionContext {

    private CommaListExpression expression;
    private Table targetTable;
    protected SortColumnInfo[] sortColumnInfo;
    /**
     * Для каждого элемента списка CommaListExpression содержит сгенерированное имя,
     * которое будет использоваться при создании колонок таблицы <code>targetTable</code>.
     */
    protected ItemName[] itemNames;
    /**
     * Для каждого элемента списка CommaListExpression содержит или его элемент или,
     * если элемент имеет тип ASExpression его первый операнд.
     */
    protected IOperand[] items;
    /**
     * Содержит список функций агрегатов из списка <code>CommaListExpression</code>.
     */
    protected FunctionAggregateExpression[] aggFunctions;

    public ColumnListExpressionContext(String expr) {
        super(expr);
    }
    public Table getTable() {
        return this.getTableServices().getCoreServices().getTable();
    }
    public Table getTable(String alias) {
        TableServices srv = this.getTableServices(alias);
        return srv.getCoreServices().getTable();
    }

    public FunctionAggregateExpression[] getAggFunctions() {
        return this.aggFunctions;
    }

    public Object getValue(DataRow row) {
        return getExpression().getValue(row);
    }

    public Object getValue(DataRow[] rows) {
        return getExpression().getValue(rows);
    }

    public CommaListExpression getExpression() {
        if (this.expression == null) {
            IOperand o = this.createExpression();
            if (!(o instanceof CommaListExpression)) {
                CommaListExpression cle = new CommaListExpression();
                cle.add(o);
                this.expression = cle;
            } else {
                this.expression = (CommaListExpression) o;
            }
            createItems();
        }
        return this.expression;
    }

    public Table getTargetTable() {
        return this.targetTable;
    }

    public void setTargetTable(Table targetTable) {
        this.targetTable = targetTable;
    }

    public SortColumnInfo[] getSortColumnInfo() {
        return this.sortColumnInfo;
    }

    public void setSortColumnInfo(SortColumnInfo[] sortColumnInfo) {
        this.sortColumnInfo = sortColumnInfo;
    }

    public Class getColumnType(FunctionAggregateExpression func) {
        int n = -1;
        for (int i = 0; i < items.length; i++) {
            if (items[i] == func) {
                n = i;
                break;
            }
        }
        return n == -1 ? null : getTargetTable().getColumns().get(n).getType();
    }

    private boolean checkDublicate(String newName) {
        for (int i = 0; i < itemNames.length; i++) {
          //My 06.03.2012 if (itemNames[i].equals(newName)) {
            if (itemNames[i].targetName.equals(newName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Создает {@link #items}, {@link #itemNames}, {@link #aggFunctions}.
     */
    protected void createItems() {

        int postfix = 0; // postfix for auto created column names
        int fcount = 0; // agg function count
        if (this.expression == null || this.expression.isEmpty()) {
            return;
        }
        items = new IOperand[expression.size()];
        itemNames = new ItemName[expression.size()];
        for (int i = 0; i < itemNames.length; i++) {
            itemNames[i] = new ItemName();
        }
        for (int i = 0; i < expression.size(); i++) {

            IOperand op = expression.get(i);

            String columnName = null;

            if (op instanceof ASExpression) {
                columnName = ((ASExpression) op).getName().toUpperCase();
                op = ((ASExpression) op).getOp1();
            }

            items[i] = op;

            if (op instanceof IdentifierOperand) {
                //Имеем дело с именем колонки из source => можем просто клонировать ее
                String nm = ((IdentifierOperand) op).getName().toUpperCase();
                String tnm = nm;
                String a = ((IdentifierOperand) op).getAlias();
                if (a != null) {
                    a = a.toUpperCase();
                    tnm = a + "_" + nm;
                }
                itemNames[i].targetName = columnName == null ? tnm : columnName;
                itemNames[i].sourceName = nm;
                itemNames[i].sourceAlias = a;
                continue;
            }
            if (op instanceof FunctionAggregateExpression) {
                fcount++;
                // Имеем дело с агрегатной функцией над полями из source
                // Если параметр функции типа IdentifierOperand => можем просто клонировать ее
                FunctionAggregateExpression f = (FunctionAggregateExpression) op;
                if (f.getOp1() instanceof IdentifierOperand) {
                    itemNames[i].sourceName = ((IdentifierOperand) f.getOp1()).getName().toUpperCase();
                    
                    String a = ((IdentifierOperand) f.getOp1()).getAlias();
                    if (a != null) {
                        a = a.toUpperCase();
                    }
                    itemNames[i].sourceAlias = a;
                    if (columnName == null) {
                        String nm = ((IdentifierOperand) f.getOp1()).getName().toUpperCase();
                        if (!checkDublicate(nm)) {
                            itemNames[i].targetName = nm.toUpperCase();
                        } else {
                            itemNames[i].targetName = (nm + "_" + (postfix++)).toUpperCase();
                        }
                    } else {
                        itemNames[i].targetName = columnName;
                    }
                } else {
                    itemNames[i].targetName = (columnName == null ? "COL_" + (postfix++) : columnName).toUpperCase();
                }
                f.setColumnName(itemNames[i].targetName);
                continue;
            }
            itemNames[i].targetName = (columnName == null ? "COL_" + (postfix++) : columnName).toUpperCase();
        } //for
        int j = 0;
        aggFunctions = new FunctionAggregateExpression[fcount];
        for (int i = 0; i < items.length; i++) {
            if (items[i] instanceof FunctionAggregateExpression) {
                this.aggFunctions[j++] = (FunctionAggregateExpression) items[i];
            }
        }
    }

    /**
     * Создает коллекцию колонок для заданной таблицы <code>target</code>.
     * Использеутся для обработки списка колонок при различных <code>select</code>
     * операциях над <code>source</code> таблицей.
     * @throws NullPointerException when source or target DataTable is null
     */
    public void createTargetColumns() {
        if (getTargetTable() == null || getTable() == null) {
            throw new NullPointerException("ColumnListExpressionContext.createTargetColumns: " + "source DataTable and target DataTable cannot be null");
        }
        DataColumnCollection columns = getTargetTable().getColumns();
        Table source;// My 06.03.2012 = getTable();
        this.getExpression();
        // int postfix = 0; // postfix for auto created column names
        for (int i = 0; i < items.length; i++) {

            IOperand op = items[i];

            String columnName = itemNames[i].targetName;
            DataColumn dc;//My 06.03.2012 = null;

            if (op instanceof IdentifierOperand) {
                // Имеем дело с именем колонки из source => можем просто клонировать ее
                source = itemNames[i].sourceAlias == null ? getTable() : getTable(itemNames[i].sourceAlias);
                dc = (DataColumn) source.getColumns().get(itemNames[i].sourceName).clone();
                dc.setName(columnName);
                columns.add(dc);
            } else if (op instanceof FunctionAggregateExpression) {
                // Имеем дело с агрегатной функцией над полями из source
                // Если параметр функции типа IdentifierOperand => можем просто клонировать ее
                FunctionAggregateExpression f = (FunctionAggregateExpression) op;
                if (f.getOp1() instanceof IdentifierOperand) {
                    source = itemNames[i].sourceAlias == null ? getTable() : getTable(itemNames[i].sourceAlias);
                    dc = (DataColumn) source.getColumns().get(itemNames[i].sourceName).clone();
                    dc.setName(columnName);
                    columns.add(dc);
                } else {
                    Class clazz = f.getType();
                    dc = columns.add(clazz, columnName);

                    if (f.getOp1() instanceof FunctionConvertExpression) {
                        defineConvertType((IExpression) f.getOp1(), dc);
                    }
                }
            } else if (op instanceof FunctionConvertExpression) {
                dc = columns.add(String.class, columnName);
                defineConvertType((IExpression) op, dc);

            } else {
                Class clazz = op.getType();
                columns.add(columns.createColumn(clazz, columnName));
                dc = columns.get(i);
            }

            dc.setReadOnly(false);
            dc.setNullable(ResultSetMetaData.columnNullable);
        } //for
    }

    protected void defineConvertType(IExpression expr, DataColumn column) {
        FunctionConvertExpression fce = (FunctionConvertExpression) expr;
        column.setScale(fce.getScale());
        //column.setSize(fce.getSize());
        column.setType(fce.getType());

    /*        if ( expr instanceof BigDecimalExpression ) {
    
    } else if ( expr instanceof DoubleExpression ) {
    }
     */
    }
    protected Object[] currentValues;

    /**
     * Возвращает вычисленные значения колонок исходной таблицы для ряда
     * таблицы результатов {@link #targetTable}. <p>
     * i-й элемент результирующего массива соответствует i-й колонке
     * таблицы {@link #targetTable}.
     * @param row
     * @param rowIndex
     * @return массив значений для ячеек ряда
     */
    /*   public Object[] getRowValues(int rowIndex) {
    Object[] r = new Object[items.length];
    if ( currentValues == null )
    currentValues = new Object[items.length];
    for ( int i=0; i < items.length; i++ ) {
    r[i] = items[i].getValue(rowIndex);
    if ( items[i] instanceof FunctionAggregateExpression )
    currentValues[i] = ((FunctionAggregateExpression)items[i]).getAggValue();
    else {
    if ( items[i] instanceof IdentifierOperand && ! isKey(i) ) {
    currentValues[i] = getTargetTable().getColumn(i).createBlankObject();
    } else {
    currentValues[i] = r[i];
    }
    }
    }
    return r;
    }
     */
    public Object[] getRowValues(DataRow row) {
        Object[] r = new Object[items.length];
        if (currentValues == null) {
            currentValues = new Object[items.length];
        }
        for (int i = 0; i < items.length; i++) {
            r[i] = items[i].getValue(row);
            if (items[i] instanceof FunctionAggregateExpression) {
                currentValues[i] = ((FunctionAggregateExpression) items[i]).getAggValue();
            } else {
                if (items[i] instanceof IdentifierOperand && !isKey(i)) {
                    currentValues[i] = getTargetTable().getColumns().get(i).createBlankObject();
                } else {
                    currentValues[i] = r[i];
                }
            }
        }

        return r;
    }

    public Object[] getRowValues(DataRow[] rows) {
        Object[] r = new Object[items.length];
        if (currentValues == null) {
            currentValues = new Object[items.length];
        }
        for (int i = 0; i < items.length; i++) {
            r[i] = items[i].getValue(rows);
            if (items[i] instanceof FunctionAggregateExpression) {
                currentValues[i] = ((FunctionAggregateExpression) items[i]).getAggValue();
            } else {
                if (items[i] instanceof IdentifierOperand && !isKey(i)) {
                    currentValues[i] = getTargetTable().getColumns().get(i).createBlankObject();
                } else {
                    currentValues[i] = r[i];
                }
            }
        }

        return r;
    }

    /**
     * Возвращает вычисленные значения колонок исходной таблицы для ряда
     * таблицы результатов {@link #targetTable}. <p>
     * i-й элемент результирующего массива соответствует i-й колонке
     * таблицы {@link #targetTable}.
     * @return массив значений для ячеек ряда
     */
    public Object[] getAggRowValues() {
        return this.currentValues;
    }

    public void reset(DataRow row) {
        for (int i = 0; i < this.aggFunctions.length; i++) {
            aggFunctions[i].reset(row);
        }
    }
    public void updateInserted(DataRow row) {
        Object[] r = new Object[items.length];
        if (currentValues == null) {
            return;
        }
        for (int i = 0; i < items.length; i++) {
            if (items[i] instanceof FunctionAggregateExpression) {
                ((FunctionAggregateExpression) items[i]).updateInserted(row);
            }
        }

    }

    public void init(DataRow row) {
        for (int i = 0; i < this.aggFunctions.length; i++) {
            aggFunctions[i].init(row);
        }
    }

    /*   public Object[] getAggRowValues(int rowIndex) {
    Object[] r = new Object[items.length];
    for ( int i=0; i < items.length; i++ ) {
    if ( items[i] instanceof FunctionAggregateExpression )
    r[i] = ((FunctionAggregateExpression)items[i]).getAggValue(rowIndex);
    else {
    if ( items[i] instanceof IdentifierOperand && ! isKey(i) ) {
    r[i] = getTargetTable().getColumn(i).createBlankObject();
    } else {
    r[i] = items[i].getValue(rowIndex);
    }
    }
    }
    return r;
    }
     */
    /**
     * Возвращает вычисленные значения колонок исходной таблицы для ряда
     * таблицы результатов {@link #targetTable}. <p>
     * i-й элемент результирующего массива соответствует i-й колонке
     * таблицы {@link #targetTable}.
     * @param targetColumnIndex
     * @param rowIndex
     * @return массив значений для ячеек ряда
     */
    /*   public Object[] peekAggRowValues(int rowIndex) {
    Object[] r = new Object[items.length];
    for ( int i=0; i < items.length; i++ ) {
    if ( items[i] instanceof FunctionAggregateExpression )
    r[i] = ((FunctionAggregateExpression)items[i]).getAggValue();
    else {
    if ( items[i] instanceof IdentifierOperand && ! isKey(i) ) {
    r[i] = getTargetTable().getColumn(i).createBlankObject();
    } else {
    r[i] = items[i].getValue(rowIndex);
    }
    }
    }
    return r;
    }
     */
    public boolean isKey(int targetColumnIndex) {
        boolean r = false;
        //String t = targetTable.getColumnName(targetColumnIndex);
        String t = itemNames[targetColumnIndex].sourceName;
        if (t == null || this.sortColumnInfo == null) {
            return false;
        }
        for (int i = 0; i < this.sortColumnInfo.length; i++) {

            if (sortColumnInfo[i].columnName.equals(t)) {
                r = true;
                break;
            }
        }
        return r;
    }

    public static class ItemName {

        public String sourceName;
        public String sourceAlias;
        public String targetName;

        public ItemName() {
            this(null, null, null);
        }

        public ItemName(String sName, String tName) {
            this(sName, tName, null);
        }

        public ItemName(String sName, String tName, String sAlias) {
            this.sourceName = sName;
            targetName = tName;
            sourceAlias = sAlias;
        }
    }
} //class ColumnListExpressionContext