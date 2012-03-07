/*
 * StartsWithEvaluator.java
 * 
 */
package tdo.expr;

import tdo.DataRow;
import tdo.Table;

/**
 *
 */
public class StartsWithEvaluator implements RowEvaluator {

    protected Table table;

    protected String columnName;
    protected int columnIndex;
    protected Object value;
    protected boolean caseSensitive;

    protected StartsWithEvaluator() {
    }

    public StartsWithEvaluator(Table table, String columnName, Object value) {
        this.table = table;
        this.columnName = columnName;
        this.value = value;
        this.columnIndex = -1;
    }

    /**
     * ��������� ���������� ��������� ����, ��������� ����������� ��������
     * ������� <code>columnName, value, caseSensitive</code>.
     * <p>��� ������� � �������� ����������� ������������� ������. ����� ����,
     * �������� ������� <code>caseSensitive, value</code> ����� ���� �����������
     * � �������������� setter-�������.
     * <p>��� ��������� ���� ����������� �������� ������� �� �� �����.
     * <p>���� �������� ������� � <code>value</code> ��� ����� <code>null</code>,
     * �� ������������ <code>true</code>.
     * <p>���� ������ �������� ������� ��� ������  <code>value</code> �����
     * <code>null</code>, �� ������������ <code>false</code>.
     *
     * <p>���� �������� ������� � <code>value</code> �� ����� <code>null</code>, ��
     * � ����������� ��������, ��� ������ ������ ����������� �����
     * <code>java.lang.String#startsWith(value)</code> � ������������ ��������
     * ��������������� ��� ��������� ������.
     *
     * <p>���� �������� �������� <code>caseSensitive</code> ����� <code>false</code>,
     * �� � �������� ������� � �������� <code>value</code> ������������� �
     * ������� ������� ����� ����������� ������ <code>startsWith</code>.
     *
     * @param row ����������� ���
     * @return ��������� ������
     */
    @Override
    public boolean evaluate(DataRow row) {
        Object columnValue;


        if (columnIndex < 0) {
            columnIndex = table.getColumns().find(columnName);
        }
        columnValue = row.getValue(columnIndex);

        if (value == null && columnValue == null) {
            return true;
        }
        if (value == null || columnValue == null) {
            return false;
        }
        String str = value.toString();
        String astr = columnValue.toString();

        if (!this.caseSensitive) {
            str = str.toUpperCase();
            astr = astr.toUpperCase();

        }
        if (astr.startsWith(str)) {
            return true;
        }
        return false;

    }
    /**
     * ������������� ��������, ������������ ��� ��������� �� ��������� �������
     * ����.
     * @param value ��������������� ��������
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * ������������� ���������, �������� �� �������� ������ �������������� �
     * ��������.<p>
     * ���� ��������� ��������� ����� <code>true</code>, �� ��������
     * ������������� � �������� � �� ������� ��������������� ��������� ��������,
     * ����������� � ���. false - � ��������� ������.
     *
     * @param caseSensitive
     */
    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }
}//class

