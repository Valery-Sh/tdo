/*
 * Validator.java
 *
 */

package tdo.impl;


import tdo.DataRow;
import tdo.expr.ExpressionContext;

/**
 * ���������� ���������������� �������, ��������������� ��� ����������
 * ��������� ���� ��� ������� �������.
 * <p>
 * ����� � ���������� validate(DataRow,Object) ���������� �c��������
 * �������������� ������ validate(DataRow).
 */
public interface Validator {
    /**
     * ���������� �������� ���������, ���� ��� ������������ ��� �������
     * ���������� ���� ��� �������.
     * @return �������� ��������� ��� <code>null</code>
     */
    ExpressionContext getExpressionContext();
    /**
     * ��������� ��������� ����.
     *
     * @param row ����������� �� ���������� ���
     * @return <code>true</code> ���� ������ ���� ������� �������� ���������.
     *   <code>false</code> � ��������� ������
     * @see #validate(tdo.DataRow, java.lang.Object)
     */
    boolean validate(DataRow row);
    /**
     * ��������� ��������� ���� ��� ��������� ��������.
     * <p>������ ���� ����� ������������ ��� ����������� �������.
     * @param row ����������� �� ���������� ���
     * @param value ����� �������� �������, ��� �������� ���������� ���������
     * @return <code>true</code> ���� ������ ���� ������� �������� ���������.
     *   <code>false</code> � ��������� ������
     * @see #validate(tdo.DataRow)
     */
    boolean validate(DataRow row, Object value);
    /**
     * ���������� ������ ������, ���������� ������� ��������� ������ � �� �������
     * @return ���������� �� ������
     */
    String getMessage();
    /**
     * ������������� �������� ������ ������, ���������� ������� ���������
     * ������ � �� �������.
     * @param message ����� ���������� ���������
     */
    void setMessage(String message);
}//interface Validator
