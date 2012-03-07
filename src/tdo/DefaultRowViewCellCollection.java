/*
 * DataRowViewCellProvider.java
 * 
 */

package tdo;



/**
 *
 * @author Valera
 */
public class DefaultRowViewCellCollection implements DataCellCollection {
    /**
     * ������ �� ��������� ����� ��� ���� ������� ���������.
     * ��������������� ������������� ������.
     */
    protected DataCellCollection cells;
    //protected DataRow row;
    
/*    public DefaultRowViewCellCollection(DataRow viewOwnerRow) {
        cells = ((DefaultDataRow)viewOwnerRow).getCellCollection();
        this.row = viewOwnerRow;
    }
*/    
    public DefaultRowViewCellCollection(DataCellCollection viewOwnerCells) {
        //cells = ((DataRowView)viewRow).getViewOwnerRow().getCellCollection();;
        cells = viewOwnerCells;
        //cells = ((DefaultDataRow)viewRow).getCellCollection();
        //this.row = viewRow;
    }
    
    @Override
    public Object getValue(int columnIndex) {
       // DefaultDataRow arow = (DefaultDataRow)((DataRowView)row).getViewOwnerRow();
       // return arow.getCellCollection().getValue(columnIndex);
         return cells.getValue(columnIndex);
    }
    
    @Override
    public Object setValue(Object value, int columnIndex) {
//        DefaultDataRow arow = (DefaultDataRow)((DataRowView)row).getViewOwnerRow();
//        return arow.getCellCollection().setValue(value, columnIndex);
         return cells.setValue(value,columnIndex);
        
    }
    
    @Override
    public Object getValue(String columnName) {
        //DefaultDataRow arow = (DefaultDataRow)((DataRowView)row).getViewOwnerRow();
        //return arow.getCellCollection().getValue(columnName);
        return cells.getValue(columnName);
    }

    @Override
    public Object setValue(Object value, String columnName) {
        //DefaultDataRow arow = (DefaultDataRow)((DataRowView)row).getViewOwnerRow();
        //return arow.getCellCollection().setValue(value, columnName);
        return cells.setValue(value, columnName);
    }

    /**
     * �������� ���������� ����� ��������� ���� � ������ �������� ����.
     * ���������, ������������ ��������� <code>state</code>, �
     * ����� ���� � �������� �������� ���� �� ����������.<p>
     * �� ����� ���� �������� <code>source</code> ���������� ������ ���� 
     * {@link tdo.DataRowView}. �������, �� ���� ����������� ��� 
     * �������-��������� ������������� � ���������� ��� ������, ��������������
     * � ��� �������-��������� �������� <code>DataRowView</code>.
     *  
     * @param source ���-�������� ����� ��� �����������
     */
    @Override
    public void copyCells(DataCellCollection source) {
        //DefaultDataRow arow = (DefaultDataRow)((DataRowView)source).getViewOwnerRow();
        //cells.copyCells(arow);
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void copyCells(Object[] source) {
        cells.copyCells(source);
    }

    @Override
    public void columnAdded(int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    public void columnMoved(int columnIndex, int oldCellIndex, int newCellIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void columnRemoved(int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getObject() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
}//class DataRowViewCellProvider
