/*
 * DartaRowViewProvider.java
 */
package tdo;

import tdo.service.TableServices;

/**
 *
 * @author Valera
 */
public class DataRowViewProvider extends DefaultDataRowProvider {

    public DataRowViewProvider(TableServices context) {
        super(context);
    }

    /**
     * ������� � ���������� ��������� ����.
     * ����� ��� ����� ��� <code>{@link tdo.DataRowView}.
     * @return ����� ��������� ���� ��� <code>TableView</code>
     */
    @Override
    public DataRow createRow() {
        DataRow r = getContext().getDataRowViewServices().createViewOwnerRow();
        DefaultDataRowView row =
                new DefaultDataRowView(getContext(), r.getContext(), r);

        DefaultRowViewCellCollection dvs = new DefaultRowViewCellCollection(row.getViewOwnerRow().getCellCollection());
        row.setCellCollection(dvs);
        return row;
    }

    /**
     * ������� � ���������� ��������� �� ������ ������� �������� � ���������
     * �������� �������.
     * 
     * ����� ��� ����� ��� <code>{@link tdo.DataRowView}.
     * @param values ������ �������� ������� ����
     * @param start ��������� �������� ������� �������
     * @param end �������� �������� ������� �������
     * @return ����� ��������� ���� ��� <code>TableView</code>
     */
/*    @Override
    public DataRow createRow(Object[] values, int start, int end) {

        //Table odt = getContext().getCoreServices().getViewOwner();
        DataRow oRow = getContext().getDataRowViewServices().createViewOwnerRow(values, start, end);
        DefaultDataRowView drv = new DefaultDataRowView(getContext(), oRow.getContext(), oRow);
        DataRowViewCellProvider dvs = new DataRowViewCellProvider(oRow);
        drv.setDataCellProvider(dvs);
        drv.setDataRowProvider(this);
        
        return drv;

    }
*/
    public DataRowView createRowView() {
        return (DataRowView) createRow();
    }

    /**
     * ��� ��������� "��������" ���� ��������� � ������������ ��� ����� 
     * ���-�������������.
     * <i>���������</i> ������ ����-�������������, (�������� <code>state</code> ���� 
     * {@link tdo.RowState} ) ����� ������ <i>���������</i> ���������
     * ���������� ����.
     * 
     * @param viewOwnerRow ���, ��� �������� ��������� �������������
     * @return ����� ��������� ����-�������������
     * @see tdo.RowState#copyFrom(tdo.RowState) 
     */
    public DataRowView createRowView(DataRow viewOwnerRow) {
        if (viewOwnerRow == null) {
            return null;
        }
        DefaultDataRowView drv = new DefaultDataRowView(getContext(), viewOwnerRow.getContext(), viewOwnerRow);
        //DefaultRowViewCellCollection dvs = new DefaultRowViewCellCollection(viewOwnerRow);
        DefaultRowViewCellCollection dvs = new DefaultRowViewCellCollection(drv.getViewOwnerRow().getCellCollection());        
        drv.setCellCollection(dvs);
        //drv.setDataRowProvider(this);

        drv.getState().copyFrom(viewOwnerRow.getState());

        return drv;

    }

    /**
     * ������� � ���������� ��� ��������� ������������� ���� ����� 
     * ������������� ����.
     * 
     * �� ����, ����������� �������������� ����������� ��� �������-���������
     * �������������. ��� ���� ��������� �����, ������� ���, ���� ���������� 
     * ������ ���� �������-���������. ��������� ����� ���-������������� ��� 
     * ��������� ���� �����, ������� ���� ��������� ������. <p>
     * ��������� ����� � ������� ����, � ��� ����� � �������� <code>state</code>
     * �� ����������.
     * 
     * @param rowView ������������� ����
     * @return ����� ���-������������� 
     */
    @Override
    public DataRow createRowCopy(DataRow rowView) {
        if (rowView == null) {
            return null;
        }
        DataRow ownerRow = ((DataRowView) rowView).getViewOwnerRow();
        DataRow newOwnerRow = ownerRow.createCopy();

        return createRowView(newOwnerRow);
    }
}//class DataRowViewProvider
