/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdo;

/*
 * DataView.java
 *
 */
/**
 *
 * @author Valera
 */
public class DataView extends TableBase implements TableView {

    private Table viewOwner;

    public DataView(DataTable table) {
        /*        editProhibited = false;

        columnsHandler = new DataColumnCollectionHandler();
        this.setColumns(new DefaultDataColumnCollection());

        this.rows = new DefaultDataRowCollection();

        this.setContext(new DefaultTableContext(this));

        ((DefaultDataRowCollection)this.rows).setContext(getContext());
         */
        init();
        this.setRowProvider(new DataRowViewProvider(getContext()));
    //initTable();

    }

    @Override
    public void reset() {
        super.reset();
        setViewOwner(null);

    }

    /**
     *
     * @param row
     * @return
     */
    //@Override
    @Override
    public DataRowView createRowView(DataRow row) {
        DataRowView drv = ((DataRowViewProvider) getRowProvider()).createRowView(row);
        return drv;
    }

    @Override
    public Table getViewOwner() {
        return this.viewOwner;
    }

    /**
     *
     * @param table
     */
    @Override
    public void setViewOwner(Table table) {
        this.viewOwner = table;
    }

    /**
     * ѕереопределен с целью обеспечить доступ в пакете tdo.data.
     * @return
     */
    /*   protected DataRowCollection getRows() {
    return super.getRows();
    }
     */
    //@Override
    @Override
    protected DataColumnCollection createColumns() {
        return new DefaultDataColumnCollection();
    }

    @Override
    public boolean isEditProhibited() {
        return super.isEditProhibited();
    }

    @Override
    public Table createTable() {
        return new DataTable();
    }

    //@Override
    public <T> int addObject(T obj) {
        return this.addRow(createRow(obj));
    }

    @Override
    public int find(DataRow row) {
        int rowIndex = -1;
        DataRowCollection rc = view.getCurrentRows();
        if (row instanceof DataRowView) {
            rowIndex = view.find(row);
        } else {
            for (int i = 0; i < getRowCount(); i++) {
                if ( ((DataRowView) getRow(i)).getViewOwnerRow() == row) {
                    rowIndex = i;
                    break;
                }
            }
        }

        return rowIndex;

    }

    /*
    public void writeExternal(ObjectOutput out) throws IOException {
    //   Object[] dcs = new Object[] { getColumns().getColumn("Name"),getColumns().getColumn("SortCode")};
    int ar = this.getActiveRowIndex();
    if (ar >= 0) {
    try {
    RowState rs = this.getRow(ar).getState();
    if (rs.isEditing()) {
    getRow(ar).endEdit();
    }
    } catch (Exception ex) {
    return;
    }
    }
    Object[] dcs = new Object[getColumnCount()];
    for (int i = 0; i < getColumnCount(); i++) {
    dcs[i] = getColumns().get(i).clone();
    }
    out.writeObject(dcs);
    //TODO out.writeObject(getDataArray());

    Sorter sorter = this.getView().getSorter();
    String list = null;
    boolean dir = false;
    if (sorter != null) {
    list = sorter.getColumnList();
    dir = sorter.getSortDirection();
    }
    out.writeObject(list);
    out.writeBoolean(dir);

    Filterer filterer = this.getView().getFilterer();
    String expr = null;
    if (filterer != null) {
    expr = filterer.getExpression();
    }
    out.writeObject(expr);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    Object[] adcs = (Object[]) in.readObject(); // columns
    DataColumnCollection dcs = this.getColumns();
    dcs.clear();
    for (int i = 0; i < adcs.length; i++) {
    dcs.add((DataColumn) adcs[i]);
    }

    Object[][] a = (Object[][]) in.readObject(); // rows and rowStates
    if (a == null || a.length == 0) {
    return;
    }
    int editingState = 0;
    int start = 4;
    int end = a[0].length;

    int i = 0;
    RowState rs;
    setLoading(true);
    while ( i < a.length ) {
    editingState = ((Integer)a[i][0]).intValue();
    if ( editingState == RowState.UNCHANGED ) {
    addRow( a[i],start, end);
    i++;
    System.out.println("new i = " + i);
    continue;
    }
    if ( editingState == RowState.INSERTED ) {
    DataRow row = addRow( a[i],start, end);
    rs = new RowState(row);
    rs.setEditingState( ((Integer)a[i][0]).intValue());
    //                rs.setOriginState( ((Integer)a[i][1]).intValue());
    rs.setRowIndex( ((Integer)a[i][2]).intValue());
    rs.setOrder( ((Integer)a[i][3]).intValue());
    i++;
    //DataRow orow = getRows().createRow( a[i],start, end);
    rs.setOriginalRow(row);
    // TODO getRows().addRowState(row, rs);
    continue;
    }
    if ( editingState == RowState.DELETED ) {
    DataRow row = getRows().createRow( a[i],start, end);
    rs = new RowState(row);
    rs.setEditingState( ((Integer)a[i][0]).intValue());
    //              rs.setOriginState( ((Integer)a[i][1]).intValue());
    rs.setRowIndex( ((Integer)a[i][2]).intValue());
    rs.setOrder( ((Integer)a[i][3]).intValue());
    rs.setOriginalRow(row);
    // TODO getRows().addRowState(row, rs);
    i++;
    continue;
    }
    if ( editingState == RowState.UPDATED ) {
    DataRow row = getRows().createRow( a[i],start, end);
    rs = new RowState(row);
    rs.setEditingState( ((Integer)a[i][0]).intValue());
    //                rs.setOriginState( ((Integer)a[i][1]).intValue());
    rs.setRowIndex( ((Integer)a[i][2]).intValue());
    rs.setOrder( ((Integer)a[i][3]).intValue());
    DataRow orow = getRows().createRow( a[i+1],start, end);
    rs.setOriginalRow(orow);
    //TODO getRows().addRow(row, rs);
    i += 2;
    continue;
    }
    }//while
    setLoading(false);

    String sortList = (String) in.readObject();
    boolean direction = in.readBoolean();
    if (sortList != null && sortList.trim().length() > 0) {
    sort(sortList, direction);
    }
    String filterExpr = (String) in.readObject();
    if (filterExpr != null && filterExpr.trim().length() > 0) {
    this.filter(filterExpr);
    }
    }
    /*
    public DataRow createRow() {
    return this.getRowProvider().createRow();
    }

    public DataRow createRow(Object obj) {
    DataRow row = null;
    if (this.getRowProvider() instanceof ObjectRowProvider) {
    row = ((ObjectRowProvider) this.getRowProvider()).createRow(obj);
    } else {
    throw new IllegalArgumentException("This table doesn't contain ObjectRowProvider");
    }
    return row;
    }

    public DataRow createRow(Object[] values, int start, int end) {
    DataRow row = null;
    row = this.getRowProvider().createRow(values, start, end);
    return row;
    }

    public DataRow createRowCopy(DataRow row) {
    return getRowProvider().createRowCopy(row);
    }

    public Table createTreeTable() {
    return new TreeDataTable();
    }
     */
///////////////////////////////////////
    @Override
    protected void endInserting(DataRow row) {
        int rowIndex;
        if (!(row instanceof DataRowView)) {
            DataRowView drv = createRowView(row);
            rowIndex = getView().addRow(drv);
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
            rowIndex = findInternal(row);
        } else {
            rowIndex = find(row);
        }
        fireRowCellValueChanged(rowIndex, columnIndex);
    }

    @Override
    protected void cancelEditing(DataRow row) {
        if (isLoading()) {
            return;
        }
        DataRow r = row;
        int rowIndex;
        if (row instanceof DataRowView) {
            rowIndex = findInternal(row);
        } else {
            rowIndex = find(row);
        }
        fireRowCellValueChanged(rowIndex, -1);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}