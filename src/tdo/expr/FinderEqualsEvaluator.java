/*
 * FinderEqualsEvaluator.java
 * 
 * Created on 12.06.2007, 23:00:16
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.expr;

import tdo.DataRow;
import tdo.Table;

/**
 *
 * @author Valera
 */
public class FinderEqualsEvaluator extends StartsWithEvaluator {

    public FinderEqualsEvaluator() {
    }
    public FinderEqualsEvaluator(Table table, String columnName, Object value) {
        super(table, columnName, value);
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
     * <code>java.lang.String#equals(value)</code> � ������������ ��������
     * ��������������� ��� ��������� ������.
     *
     * <p>���� �������� �������� <code>caseSensitive</code> ����� <code>false</code>,
     * �� � �������� ������� � �������� <code>value</code> ������������� �
     * ������� ������� ����� ����������� ������ <code>equals</code>.
     *
     * @param row ����������� ���
     * @return ��������� ������
     */

    @Override
    public boolean evaluate(DataRow row) {
        Object columnValue;
        columnValue = row.getValue(columnName);    
        
        if ( value == null && columnValue == null ) {
            return true;
        }
        if ( value == null || columnValue == null ) {
            return false;
        }
        String str = value.toString();
        String astr = columnValue.toString();
        
        if ( ! this.caseSensitive ) {
            str = str.toUpperCase();
            astr = astr.toUpperCase();
        } 
        if ( astr.equals(str) )
            return true;
        return false;

    }
    
}//class FinderEqualsEvaluator
