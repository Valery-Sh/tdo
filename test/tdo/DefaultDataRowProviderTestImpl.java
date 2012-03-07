/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo;

import tdo.DefaultDataRowTest.DataRowTableContext;
import tdo.service.*;

/**
 *
 * @author valery
 */
public class DefaultDataRowProviderTestImpl implements tdo.DataRowProvider {

    private DataRowTableContext context;
      
    public DefaultDataRowProviderTestImpl(TableServices context )
    {    
        this.context = (DataRowTableContext)context;
    }
    
    public TableServices getContext() {
        return this.context;
    }
    
    @Override
    public DataRow createRow() {
        DataCellCollection cc = new DefaultDataCellCollectionStub(context,
                context.columnCount,context.calcColumns,context.columnNameMap);
        DefaultDataRow row = new DefaultDataRow(context,cc);
        
        return row;
    }

    /**
     * ������� � ���������� ����� ��� � ������� ����������� ����� ��������� ����.
     * 
     * ��������� ����� � ������� ����, � ��� ����� � �������� <code>state</code>
     * �� ����������.
     * 
     * @param row ���, ������ �������� ����������
     * @return ����� ���-����� ���������
     * @see #createRow
     */
    
    @Override
    public DataRow createRowCopy(DataRow row) {
        if (row == null) {
            return null;
        }
        DefaultDataRow newRow = (DefaultDataRow) createRow();
        //02.10.2007 
        newRow.copyFrom(row);
        return newRow;

    }

    @Override
    public DataRow createRow(Object bean) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}//class DefaultDataRowProviderTestImpl
