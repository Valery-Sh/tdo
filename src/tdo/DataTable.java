/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdo;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import tdo.event.ActiveRowEvent;
import tdo.event.RowEditingListener;
import tdo.event.TableEvent;
import tdo.event.TableEvent.TableEventCause;
import tdo.impl.Joiner.Plan;
import tdo.impl.TableViewLinker;
import tdo.impl.TreeBuilder;
import tdo.impl.Validator;
import tdo.service.DefaultTableServices;
import tdo.service.TableServices;

/**
 *
 * @author valery
 */
public class DataTable extends TableBase implements Externalizable {

    protected List<LinkedDataTable> linkedTables;
    protected Validator linkedTableValidator;
    protected Class beanClass;

    public DataTable() {
        super();
        setRowEditingHandler(new RowEditingHandler(this));
        if (!(this instanceof TableView)) {

            setTableLinker(new TableViewLinker());
            getTableLinker().addRowEditingListener(this.getRowEditingHandler());
        }
        beanClass = null;

    }

    public <T> DataTable(Class<T> beanClass) {
        this(beanClass, false);
    }

    public <T> DataTable(Class<T> beanClass, boolean includeFields) {
        init();
        this.beanClass = beanClass;

        this.defaultObjectRowSupport = new ObjectDataRowSupport(getContext(), beanClass, null, includeFields); //new 06.06
        this.saveState(beanClass, includeFields);
        setRowEditingHandler(new RowEditingHandler(this));

        if (!(this instanceof TableView)) {
            //setTableLinker(new TableViewLinker());
            setTableLinker(new TableViewLinker());
            getTableLinker().addRowEditingListener(this.getRowEditingHandler());
        }


    }

    @Override
    public boolean processRowEditing(int kind, DataRow row, int columnIndex) {
        boolean r = columnIndex < 0 && kind == DataRow.ENDEDIT_BEFORE;
        if (r) {
            if (this.linkedTables == null || this.linkedTables.isEmpty()) {
                return true;
            }
            if (!isValidationEnabled()) {
                return true;
            }
            r = this.linkedTableValidator.validate(row);
            if (!r) {
                //Validator v = ((LinkedTableValidator)linkedTableValidator).getChildValidator();
                Validator v = ((LinkedTableValidator) linkedTableValidator);
                int vi = ((LinkedTableValidator) linkedTableValidator).getChildValidatorIndex();
                getValidationManager().notifyListeners(v, -1, row, true); //throws ValidateException
            }
        }
        return super.processRowEditing(kind, row, columnIndex);
    }
    /*
    @Override
    protected void forceValidate(DataRow row) {
    if ( this.linkedTables == null || this.linkedTables.isEmpty() )
    return;
    if (! isValidationEnabled() ) {
    return;
    }

    if (getValidationManager() != null)
    getValidationManager().validate(row, true);
    }
     */

    @Override
    protected void notifyExternal(int kind, DataRow row, int columnInex) {
        if ((!isLoading()) && (kind == DataRow.ATTACH || kind == DataRow.ATTACHNEW)) {
            objectListInsert(row);
        }
        if (this.editObserver != null) {
            switch (kind) {
                case DataRow.ATTACH:
                    editObserver.attach(row);
                    break;
                case DataRow.ATTACHNEW:
                    editObserver.attachNew(row);
                    break;
                case DataRow.BEGINEDIT:
                    editObserver.beginEdit(row);
                    break;
                case DataRow.CANCELEDIT_RESET_ROW_VERSIONS:
                    editObserver.cancelEditResetRowVersions(row);
                    break;
                case DataRow.CANCELEDIT_INSERTING:
                    editObserver.cancelEditInserting(row);
                    break;
                case DataRow.CANCELEDIT:
                    editObserver.cancelEdit(row);
                    break;
                case DataRow.ENDEDIT_BEFORE:
                    editObserver.endEditBefore(row);
                    break;
                case DataRow.ENDEDIT_INSERTING:
                    editObserver.endEditInserting(row);
                    break;
                case DataRow.ENDEDIT_RESET_ROW_VERSIONS:
                    editObserver.endEditResetRowVersions(row);
                    break;
                case DataRow.ENDEDIT:
                    editObserver.endEdit(row);
                    break;
                case DataRow.DELETE:
                    editObserver.delete(row);
                    break;

            } //switch
        }
    }
    public void setEditObserver(EditObserver observer) {
        this.editObserver = observer;
    }
    @Override
    protected void notifyLinked(ActiveRowEvent e) {
        if (this.linkedTables == null || e.getNewValue() < 0) {
            return;
        }

        for (LinkedDataTable linked : linkedTables) {
            linked.parentActiveRowChanged(e);
        }
    }

    protected void objectListInsert(DataRow row) {
        if (this.getObjectList() == null) {
            return;
        }
        this.getObjectList().add(row.getObject());
    }

    /*    @Override
    protected void initTable() {
    if (!(this instanceof TableView)) {
    //setTableLinker(new TableViewLinker());
    setTableLinker(new TableViewLinker());
    getTableLinker().addRowEditingListener(this.getRowEditingHandler());
    }

    }
     */
    private void saveState(Class beanClass, boolean includeFields) {
        this.beanClass = beanClass;
        this.includeFields = includeFields;
    }

    public DataTable select(String columns, String whereFilter, String groupBy, int totals, boolean includeDetails, boolean groupMode) {
        return (DataTable) super.selectBase(columns, whereFilter, groupBy, totals, includeDetails, groupMode);
    }

    public DataTable select(String columns, String whereFilter, String groupBy) {
        return this.select(columns, whereFilter, groupBy, TreeBuilder.TOTALS_NONE, false, TreeBuilder.BEFORE);
    }

    public DataTable select(String columns, String whereFilter) {
        return this.select(columns, whereFilter, null, TreeBuilder.TOTALS_NONE, false, TreeBuilder.BEFORE);
    }

    public DataTable select(String columns) {
        return this.select(columns, null, null, TreeBuilder.TOTALS_NONE, false, TreeBuilder.BEFORE);
    }

    public DataTable join(Table joinTable, String columns, String onExpr, String whereExpr, Plan plan) {
        return (DataTable) joinBase(joinTable, columns, onExpr, whereExpr, plan);
    }

    public DataTable join(Table joinTable, String columns, String onExpr) {
        return (DataTable) joinBase(joinTable, columns, onExpr, null, null);
    }

    public DataTable join(Table joinTable, String columns, String onExpr, String whereExpr) {
        return (DataTable) joinBase(joinTable, columns, onExpr, whereExpr, null);
    }
    protected ObjectRowSupport defaultObjectRowSupport;
    protected boolean includeFields;

    public ObjectRowSupport getDefaultObjectRowSupport() {
        return this.defaultObjectRowSupport;
    }

    /**
     *
     */
    @Override
    public void reset() {
        TableViewLinker linker = this.getTableLinker();
        super.reset();
        List<RowEditingListener> ls = linker.getListeners();
        if (ls == null) {
            return;
        }
        for (int i = 0; i < ls.size(); i++) {
            ((RowEditingHandler) ls.get(i)).getTable().reset();
        }
    }

    public void deleteView(DataView v) {
        v.reset();
    }

    @Override
    public Table createTable() {
        return new DataTable();
    }

    public <T> List<T> getObjectList(Class<T> clazz, String columnName) {
        List<T> ls = new LinkedList<T>();
        return ls;
    }

    @Override
    protected List getObjectList() {
        if (this.objectList == null && this.beanClass != null) {
            objectList = new LinkedList();
        }
        return objectList;
    }

    public LinkedDataTable createLinkedTable(Class beanClass, String listColumn, String masterIdColumn) {
        if (linkedTables == null) {
            linkedTables = new ArrayList<LinkedDataTable>();
            linkedTableValidator = new LinkedTableValidator(this, linkedTables);
        //this.addRowValidator(linkedTableValidator, "LinkedTableValidator (ERROR). ");
        }

        LinkedDataTable lt = new LinkedDataTable(this, listColumn, beanClass, masterIdColumn);

        linkedTables.add(lt);
        if (this.activeRow >= 0) {
            List l = (List) getRow(activeRow).getValue(listColumn);
            if (l == null) {
                l = this.getObjectList(beanClass, listColumn);
            }
            lt.populate(l);
        }

        return lt;
    }

    protected Table prepareView() {
        DataView dv = new DataView(this);
        TableServices c = new DefaultTableServices(dv);
        dv.setRowProvider(new DataRowViewProvider(c));
        dv.setTableLinker(getTableLinker());
        getTableLinker().addRowEditingListener(dv.getRowEditingHandler());
        return dv;
    }

    public DataView createView() {
        return this.createView("");
    }

    public DataView createView(String whereFilter, String orderBy) {
        DataTable dt = (DataTable) this.createTableViewSource(whereFilter, orderBy);
        DataView dv = (DataView) prepareView();

        importColumns(dv, dt.getColumns());

        //dv.getColumns().

        dv.setViewOwner(this);
        dv.setLoading(true);
        for (int i = 0; i < dt.getRowCount(); i++) {
            DataRowView drv = dv.createRowView(dt.getRow(i));
            dv.addRow(drv);
        }

        dv.internalMoveTop();
        dv.setLoading(false);

        return dv;
    }

    public DataView createView(String whereFilter) {
        return this.createView(whereFilter, null);
    }
    /*
    public DataTable createChangesView() {
    return (DataTable) this.createBaseChangesView();
    }
     */

    @Override
    public void populate(Object[] schema, Object[][] cells) {
        super.populate(schema, cells);
    }

    @Override
    public void populate(ResultSet resultSet) throws SQLException {
        super.populate(resultSet);
    }

    public <T> void populate(List<T> rowList, Class<T> beanClass) {
        //DataRowProvider rowProvider = this.getRowProvider();
        //DataRowCollection rm = this.rows;
        if (rowList == null) {
            throw new IllegalArgumentException("Parameter 'rowList' cannot be null");
        }

        this.setLoading(true);

        Class stopBeanClass = beanClass.getClass().getSuperclass();
        this.clear();

        if (beanClass.equals(this.beanClass) && this.includeFields == false) {

//        if (this.getRows() != null && rowProvider.getClass().equals(ObjectRowProvider.class) && beanClass.equals(((ObjectRowProvider) rm).getObjectClass()) && stopBeanClass.equals(((ObjectRowProvider) rm).getStopObjectClass()) && ((ObjectRowProvider) rowProvider).getIncludeFields() == false) {
            this.populate(rowList);
            return;
        }
        //createColumns( dataTable, resultSet);
        int capacity = 100;
        if (!rowList.isEmpty()) {
            capacity = rowList.size();
        }
        DataRowCollection drc = new DefaultDataRowCollection(capacity);
        ((DefaultDataRowCollection) drc).setContext(getContext());
        this.createInternalView(drc);
        this.defaultObjectRowSupport = new ObjectDataRowSupport(getContext(), beanClass, null, false); //new 06.06
        saveState(beanClass, false);
        //rowProvider = new ObjectRowProvider(getContext(), beanClass, false);
        this.populate(rowList);
        this.setLoading(false);

    }

    public <T> void populate(List<T> rowList) {
        if (rowList == null) {
            throw new IllegalArgumentException("Parameter 'rowList' cannot be null");
        }
        if (this.beanClass == null) {
            throw new IllegalArgumentException("Unknown list items's class");
        }

        this.setLoading(true);
        DataRow oRow;
        DataRowCollection orm = this.view.getTableRows();

        this.objectList = rowList;
        for (T obj : rowList) {
            oRow = createRow(obj);
            this.addRow(oRow);
        }

        this.fireDataModel(new TableEvent(this, TableEvent.ALL_ROWS, TableEvent.ALL_COLUMNS, TableEventCause.schema));
        if (getRowCount() != 0) {
            this.internalMoveTop();
        }
        this.setLoading(false);
    }

    /*    public int findByRow(DataRow row, DataRowCollection rc) {
    int rowIndex = -1;
    DataRow r = row;

    if (row instanceof DataRowView) {
    r = ((DataRowView) row).getViewOwnerRow();
    }
    return rc.indexOf(row);

    }
     */
    @Override
    public int find(DataRow row) {
        int rowIndex = -1;
        DataRow r;//My 06.03.2012 = row;

        if (row instanceof DataRowView) {
            r = ((DataRowView) row).getViewOwnerRow();
            for (int i = 0; i < getRowCount(); i++) {
                if (((DataRowView) getRow(i)).getViewOwnerRow() == r) {
                    rowIndex = i;
                    break;
                }

            }
        } else {
            rowIndex = view.find(row);
        }
        return rowIndex;
    }

    //********** RowEditingEvent treating *************************
    /**
     * This method is called when row.endEdit() method was invoked and
     * the row was in {@link RowStateImpl#INSERTING}} state and its new state
     * has become {@link tdo.RowStateImpl#INSERTED}
     *
     * @param row the source of a <@link tdo.event#RowEditingEvent}
     */
    @Override
    protected void endInserting(DataRow row) {
        int rowIndex;
        if (row instanceof DataRowView) {
            DataRowView drv = (DataRowView) row;
            DataRow dr = drv.getViewOwnerRow();
            //dr.setTable(viewOwner);
            rowIndex = getView().addRow(dr);
        } else {
            rowIndex = getRowCount() - 1;
        }
        fireRowInserted(this, rowIndex, row);
    }

    @Override
    protected void endSetValue(DataRow row, int columnIndex) {
        //updateViewListRowCellValueChanged(row, columnIndex);
        DataRow r = row;
        int rowIndex;
        if (row instanceof DataRowView) {
            rowIndex = find(((DataRowView) row).getViewOwnerRow());
        } else {
            rowIndex = findInternal(row);
        }
        fireRowCellValueChanged(rowIndex, columnIndex);

    }

    @Override
    protected void cancelEditing(DataRow row) {
        //updateViewListRowCellValueChanged(row, columnIndex);
        DataRow r = row;
        int rowIndex;
        if (row instanceof DataRowView) {
            rowIndex = find(((DataRowView) row).getViewOwnerRow());
        } else {
            rowIndex = findInternal(row);
        }
        fireRowCellValueChanged(rowIndex, -1);

    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
} //class DataTable