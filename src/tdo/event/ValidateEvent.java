/*
 * ValidateEvent.java
 *
 * Created on 15 ������� 2006 �., 15:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.event;

import java.util.EventObject;
import tdo.DataRow;
import tdo.Table;
import tdo.impl.ValidationManager;
import tdo.impl.Validator;
import tdo.service.TableServices;

/**
 *  ������������� ���������� �� ������ ��������� ���� ��� �������.
 * <p>������� ������������, ����� � ������� {@link tdo.Table} ���� ���� �� ����
 * ������������������ ���������, ������� ��������� ������ � ������ ���� ���
 * �������.
 * @see tdo.impl.Validator
 */
public class ValidateEvent extends EventObject{
    
    //private int rowIndex;

    private String message;
  //  private String cause;
    private DataRow row;
    private String columnName;

    public String getColumnName() {
        return columnName;
    }
    private Validator validator;
    private Object value;
    /**
     * ������� ��������� ������ ��� �������� ��������� �������, ����, �������
     * �������, ����������, ���������� ������ � ��������.
     * <p>���� ����������� ������ ������������ ��� ��������� �������.
     * @param source �������� �������, ������� ������ �������� ������ ����
     *   {@link tdo.impl.ValidationManager}
     * @param row ���, ��� �������� �������� ���������� ������
     * @param columnIndex ������ �������, ��� ���������� ��� �������, ����������
     *      ������
     * @param validator ���������, ������� ���������� �������� �� ���������
     * @param value ����� �������� ��� ������� � ��������
     *      <code>columnIndex</code>
     */
    public ValidateEvent(ValidationManager source, DataRow row, String columnName, Validator validator, Object value) {
        super(source);
        this.row = row;
        this.columnName = columnName;
        this.validator = validator;
        this.value = value;
    }
    /**
     *
     * ������� ��������� ������ ��� �������� ��������� �������, ����, �������
     * �������, ����������, ���������� ������ � ��������.
     *
     * <p>���� ����������� ������ ������������ ��� ��������� ����.
     * @param source �������� �������, ������� ������ �������� ������ ����
     *   {@link tdo.impl.ValidationManager}
     * @param row ���, ��� �������� �������� ���������� ������
     * @param validator ���������, ������� ���������� �������� �� ���������
     */
    public ValidateEvent(ValidationManager source, DataRow row, Validator validator) {
        super(source);
        this.row = row;
        this.validator = validator;
        this.columnName = null;
    }
    /**
     * ���������� �������, ��� ��������� ������� ���������� ������.
     * @return �������, ��� ��������� ������� ���������� ������.
     */
    public TableServices getTableServices() {
        return ((ValidationManager)getSource()).getTableServices();
    }

    /**
     * ���������� ������, ������������ ��������� ���������.
     * <p>������ <code>validate</code> ������� ���������� ���������� ������������
     * ������ ���� {@link tdo.impl.ValidationManager} , ������� �������������
     * �������� ��� ����������� �����������, ����������� �������, �������
     * ��������� ��������� � ���������� ���������� �������, ���� ��� ����������.
     * @return ������, ������������ ��������� ���������.
     */
    public ValidationManager getValidationManager() {
        return (ValidationManager)getSource();
    }
    /**
     * ���������� ������ ������� �������, ��� ��������� ������� ���������� ������.
     * @return ������ �������
     */
/*    public int getColumnIndex() {
        if ( columnName == null )
            return -1;
        return this.getTable().getColumns().find(columnName);
    }
 */
    /**
     * ���������� ���������, ������������ ������.
     * @return ���������, ������������ ������
     */
    public Validator getValidator() {
        return this.validator;
    }
    /**
     * ���������� ��������, ��� ��������� �������� ��� ������� ���������� ������.
     * @return ��������, ��������� ������ ��� ���������
     */
    public Object getValue() {
        return this.value;
    }
    /**
     * ���������� ���, ��� ��������� �������� ���������� ������.
     * @return ��������� ���
     */
    public DataRow getRow() {
        return this.row;
    }
    /**
     *  ���������� ��������������� ���������, ����������� ������� ������.
     * <p>������ ����������� �������� �������� ���� <code>java.lang.String</code>,
     * ���������� ���������, ������� ������������ ��� �������� �������. � ������
     * ������ ����� ��������� ����� ������� ����������� ������������������
     * ��������, ������� ���������� ��������� ������� ��� ������ ������� ������.
     *
     * @return ���� ����� �������� ������� ���������� �����
     * {@link #setMessage(java.lang.String) },  �� ������������ �������������. �
     *  ��������� ������, ������������ ��������������� ������ ���������
     *  ����������
     *
     * @see tdo.impl.RowValidator#getMessage()
     * @see tdo.impl.ColumnValidator#getMessage()
     */
    public String getMessage() {
        String s;//My 06.03.2012 = null;
        if ( this.message != null ) {
            return this.message;
        }
        s = validator.getMessage();
        if ( s == null )
            return s;
        return columnName != null ?
            getValidationManager().formatMessage(row, validator, columnName) :
            getValidationManager().formatMessage(row, validator);

    }
    /**
     * ������������� �������� ���������, ����������� ������.
     * ���� ��� �������� �� ����� <code>null</code>, �� ��� ������������ ���
     * ���������� {@link #getMessage() }.
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
