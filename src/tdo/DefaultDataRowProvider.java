package tdo;

import java.io.Serializable;
import tdo.service.TableServices;
/**
 * ���������� ���������� <code>tdo.DataRowProvider</code> ��������� ���������
 * ������� �����, � ����� <code>tdo.DefaultDataRow</code>.
 * <p> ������ ������ ������ ����������� � �������� ��������� �������
 * {@link tdo.service.TableServices}.
 * <p>������� ���� <code>tdo.DefaultDataRow</code> ������� ���������� �� 
 * ���������� {@link tdo.DataCellCollection}, ��� �������� ������� ������ ������
 * ���������� ������� 
 * {@link tdo.service.TableServices#getCellCollectionServices}. ��� ���������
 * ������������ ���� � ��� �� ����� <code>DefaultDataRow</code> � ���� � 
 * ��� �� ��������� ����� ��� ��������� ������ ��� � ������������ ����� ������,
 * ��� � ������� java bean ��������.
 * @see tdo.DataRow
 * @see tdo.ObjectDataCellCollection
 * @see tdo.DataRowViewProvider 
 * @see tdo.DataRowView
 */
public class DefaultDataRowProvider implements DataRowProvider,Serializable{
    /**
     * �������� �������, ��������������� ����� �����������
     */
    private TableServices context;
    /**
     * ������� ����� ��������� ������ ��� ���������� ��������� �������.
     * @param context �������� �������
     */
    public DefaultDataRowProvider(TableServices context) {
        this.context = context;
    }
    /**
     * @return �������� �������
     */
    public TableServices getContext() {
        return context;
    }
    
    /**
     * ������� � ���������� ����� ��������� ���� ��� ��������� java bean �������.
     * Runtime ��� ������������� ������� - <code>tdo.DefaultDataRow</code>.
     * <p>����� ����������� ������� 
     * {@link tdo.service.TableServices#getCellCollectionServices } ��� ��������
     * ���������� <code>tdo.DataCellCollection</code> �� ��������� ������� �
     * ��������� ��� ����� ���������� ����. 
     * 
     * @param obj ������ java bean - ������ ����.
     * @return ����� ��������� ������ <code>tdo.DefaultDataRow</code>.
     * @see #createRow()
     * @see tdo.DataRow
     * @see tdo.DefaultDataRow
     * @see tdo.DataCellCollection
     */
    @Override
    public  DataRow createRow(Object obj) {
        DataCellCollection dcc = context.getCellCollectionServices().createCellCollection(obj);
        DefaultDataRow row = new DefaultDataRow(context,dcc);
        return row;
    }
    /**
     * ������� � ���������� "������ ���" ��� ������ ���� <code>DataRow</code>.
     * <p>����� ����������� ������� 
     * {@link tdo.service.TableServices#getCellCollectionServices } ��� ��������
     * ���������� <code>tdo.DataCellCollection</code> � ��������� ��� ����� 
     * ���������� ����. 
     * 
     * @return ����� ���, ��� �������� <code>tdo.DefaultDataRow</code>
     * @see #createRow(Object)
     * @see tdo.DataRow
     * @see tdo.DefaultDataRow
     * @see tdo.DataCellCollection
     */
    @Override
    public  DataRow createRow() {
        DataCellCollection dcc = context.getCellCollectionServices().createCellCollection();
        DefaultDataRow row = new DefaultDataRow(context,dcc);        
        return row;
    }
    /**
     * ������� � ���������� ����� ��� � ������� ����������� ����� ��������� ����.
     * 
     * ��������� ����� � ������� ����, � ��� ����� � �������� <code>state</code>
     * �� ����������.
     * 
     * @param row ���, ������ �������� ����������
     * @return ����� ���-����� ���������, ��� �������� 
     *    <code>tdo.DefaultDataRow</code> 
     * 
     * @see #createRow()
     * @see #createRow(Object)
     */
    @Override
    public DataRow createRowCopy(DataRow row) {
        if ( row == null )
            return null; 
        DataCellCollection dcc = context.getCellCollectionServices().createCellCollection();
        DefaultDataRow newRow = new DefaultDataRow(context,dcc);        
        
        newRow.copyFrom(row);
        return newRow;
    }
}//class DefaultDataRowProvider
