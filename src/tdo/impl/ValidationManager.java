/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tdo.DataRow;
import tdo.event.ValidateEvent;
import tdo.event.ValidateListener;
import tdo.expr.ExpressionContext;
import tdo.expr.RowExpressionContext;
import tdo.service.TableServices;

/**
 * ��������� ������������ � ����������� ����������� ���� � �������.
 * <p>������� ���� <code>tdo.Table</code> ��������� �������������� �������������� �����
 * ����������� � �������� ���� tdo.impl.Validator. ��� �������� ����� ��������,
 * � ����� �� ���������� ������������ ����� ValidationManager.
 * ���������� ����� ��� ����������� ����������� � ��������� ����
 * <code>java.util.List</code>. ���������� ��� ��������� ��������������� �������
 * ����� ����� <code>getRowValidators()</code>. ������ ������������������
 * ��������  ����������:
 * <code><pre>
 * ValidationManager vm  =  table.getValidationManager();
 * List<Validator> ls  = v m.getRowValidators();
 * </pre></code>
 * <p>��� ��������� ����� ���� ������������, ��������, ��� ������� ���������
 * ������.
 * <p>���������� ������� ��� ����������� ����������� � ��������� ����
 * <code><pre>java.util.Map<String,List<Validator>></pre></code>.
 * ���������� ��� ��������� ��������������� ������� ����� �����
 * <code>getColumnValidators()</code>.
 * ������ ������������������ ��������  ����������:
 * <code><pre>
 * ValidationManager vm  =  table.getValidationManager();
 * Map<String,List<Validator>>   map =  vm.getColumnValidators();
 * List<Validator>  cls  = map.get(column-name);
 * </pre>
 * </code>
 *
 * <p>����� � �������� ��������� ������ ���� �������, ��������� ���������
 * ��������� ����, �� ����������� ���� �� ������������� �������:
 * {@link #validate(DataRow) } ���  {@link #validate(DataRow, boolean) }
 * ���������. ��� ������, ��������������� ��������� �� ������ ����������� ����,
 * ������� � 0-�� �������� ��������� � ������ ���� Validator � ����������� ���
 * ����� <code>validate</code>. ���� ��� ���� ����������� ������� ���������
 * <code>true</code>, �� ���������, ��� ��� ������� ������ �������� � ������ ��
 * ����������. ����, ���� �� ��� ������ �� �����������, ��� �����
 * <code>validate</code> ������ <code>false</code>, �� ���������, ��� ���
 * �������� ��������� ������.
 * <p>����� � �������� ��������� ������� ���� �������, ��������� ���������
 * ��������� �������, �� ����������� �����:
 * {@link #validate(tdo.DataRow, java.lang.String, java.lang.Object) }
 * ���������. �����, ��������� ��� �������� ������� ��� ����, ��������� ������
 * ����������� �������, �, �����,  ������� � 0-�� �������� ���������������
 * ��������� ��� ����� <code>validate</code>. ���� ��� ���� ����������� ������
 * ������� ��������� <code>true</code>, �� ���������, ��� ������� ������� ������
 * �������� � ������ �� ����������. ����, ���� �� ��� ������ �� �����������, ���
 * ����� <code>validate</code> ������ <code>false</code>, �� ���������, ���
 * ������� �������� ��������� ������.
 * <h2>����������� �����������</h2>
 * ����������� ���������� ���� ������������ ���������� ��������� �����������
 * ������ �� �������:
 * <ol><li>void addRowValidator(Validator validator,String msg)</li>
 * <li>Validator addRowValidator(String expression,String msg)</li>
 * ���� ��� ���� �������������� ��������� �����������, �� ��� ������� �� ���
 * ����� ���� �������� ����� �� ����������� ���� �������.
 * <p><i>������</i> ����� ������ �������� �������� ������, �������� ��� ���������
 * ����������� �����, ����������� ��������� {@link  tdo.impl.Validator} .
 * <p><i>������</i> ����� ������ ����� ������, �.�. ��������� ������ �������
 * ��������� ��������� ���������� ��� ������� ������� �������, ����������,
 * ��������� � �����������.
 * <p>��� �������� ���������� ���� ������������ �����:
 * {@link #removeRowValidator(tdo.impl.Validator) }
 * ����������� ���������� ������� ������������ ���������� ��������� �����������
 * ������ �� �������:
 * <ol>
 * <li>void addColumnValidator(String columnName, Validator validator,String msg)
 * </li>
 * <li>Validator addColumnValidator(String columnName, String expression,String msg)
 * </li>
 * </ol>
 * <p>���� ��� ������� �������������� ��������� �����������, �� ��� ������� ��
 * ��� ����� ���� �������� ����� �� ����������� ���� �������.
 * <p><i>������</i> ����� ������ �������� �������� ������, �������� ��� ���������
 * ����������� �����, ����������� ��������� <code>Validator</code>.
 * <p><i>������</i> ����� ������ ����� ������, �.�. ��������� ������ �������
 * ��������� ��������� ���������� ��� ������� ������� �������, ����������,
 * ��������� � �����������.
 * <p>��� �������� ���������� ������� �� ��������� ����� ������� ������������ �����:
 * {@link #removeColumnValidator(java.lang.String, tdo.impl.Validator)
 *
 * <h2>��������� ������� ���������</h2>
 *
 *  ����� ��������� ������� ��� ���� ������������ ������ ���������, �� ����������
 * ������� ���� {@ link tdo.event.ValidateEvent} � ��������� ���
 * ������������������ ����������� ����� �������. ���������� �������, ������
 * ������������� ��������� {@link tdo.event.ValidateListener}. �������
 * ��������� �������������� ������� <code>ValidationManager</code> ����� ��
 * �������:
 * <ol>
 * <li>void addRowValidateListener(ValidateListener l) </li>
 * <li>void addColumnValidateListener(ValidateListener l)</li>
 * <p>��� �������� ����������� ������������ ���� �� ������� ��������������:
 * <ol>
 * <li>void removeRowValidateListener(ValidateListener l)</li>
 * <li>void removeColumnValidateListener(ValidateListener l)</li>
 * </ol>
 */
public class ValidationManager {
    protected TableServices tableServices;
    //protected Table table;
    /**
     * ������ ������������������ ����������� ����.
     */
    protected List<Validator> rowValidators;
    /**
     * ������������������ ���������� �������.<p>
     * ������� ����� ������� ������������� ���� � <code>map</code>-������� ���
     * ��������-��������� �������� ������ �����������
     */
    protected Map<String,List<Validator>> columnValidators;
    /**
     * ������ ������������������ ������������ ������� <code>ValidateEvent</code>
     * ������������� ������������ ����.
     */
    protected List<ValidateListener> validateList;
    /**
     * ������ ������������������ ������������ ������� <code>ValidateEvent</code>
     * ������������� ������������ �������.
     */
    protected List<ValidateListener> columnValidateList;
    /**
     * ������� ��������� ������ ��� �������� �������.
     * @param services ������� �������, ��� ������� ��������� ������
     */
    public ValidationManager(TableServices services) {
        //this.table = table;
        this.tableServices = services;
    }
    /**
     * ���������� ������ ���� ����������� ����.
     * @return ������ ����������� ����
     */
    public List<Validator> getRowValidators() {
        return this.rowValidators;
    }
    /**
     * ���������� map-������� ���� ����������� ��� ���� �������.
     * @return map-������� ����������� �������
     */
    public Map<String,List<Validator>> getColumnValidators() {
        return this.columnValidators;
    }
    /**
     * ���������� ������� �������, ��� ������� ����������� ���������.
     * @return ������� ����������� �������
     */
    public TableServices getTableServices() {
        return this.tableServices;
    }
    /**
     * ������� ���������� � ����������� �������. <p>
     * ������� ��� ���������� ����, ��� ���������� ���� �������. ����� ���������
     * ����������� ������� <code>ValidateEvent</code> ��� ��� ����, ��� �
     * ��� �������.
     */
    public void clear() {
        this.rowValidators.clear();
        this.columnValidators.clear();
        this.validateList.clear();
        this.columnValidateList.clear();
    }
    //////////////////////////////////////////////////////////////////////////
    //   COLUMN VALIDATORS
    //////////////////////////////////////////////////////////////////////////
    /**
     * ������� � ��������� � map-������� ����������� ����� ������ ���
     * �������� ����� �������, ����������� ��������� � ��������� �� ������.
     *
     * @param columnName ��� �������, ������� ����������� �����������
     * @param expression ���������� ��������� ��� �������� ���������� 
     *   ������ �������� �������
     * @param msg ��������� �� ������. ���� ����� �������� <code>null</code>,
     *    �� ����������� ��������� ��������� ���� ����������� ���������.
     * @return <code>null</code>, ���� <code>expression</code> �����
     *   <code>null</code> ��� ��� ����� ����� 0.
     *
     * @see #addColumnValidateListener(tdo.event.ValidateListener)
     * @see #addColumnValidator(java.lang.String,tdo.impl.Validator,java.lang.String)
     */
    public Validator addColumnValidator(String columnName, String expression,String msg) {
        if (expression == null || expression.trim().length() == 0) {
            return null;
        }
        ColumnValidator cv = new ColumnValidator(this.tableServices, columnName);
        cv.setExpression(expression);
        if ( msg != null )
            cv.setMessage(msg);
        if (columnValidators == null) {
            columnValidators = new HashMap<String,List<Validator>>(5);
        }
        List<Validator> vl = columnValidators.get(columnName.toUpperCase());
        if ( vl == null ) {
            vl = new ArrayList<Validator>(3);
            columnValidators.put(columnName.toUpperCase(), vl);

        }
        vl.add(cv);

        return cv;
    }
    /**
     * ������� � ��������� � map-������� ����������� ����� ������ ���
     * �������� ����� �������, ����������� ��������� � ��������� �� ������.
     *
     * @param columnName ��� �������, ������� ����������� �����������
     * @param validator ��������� ������ �������� �������
     * @param msg ��������� �� ������. ���� ����� �������� <code>null</code>,
     *    �� ����������� ��������� ��������� ���� ����������� ���������.
     *
     * @see #addColumnValidateListener(tdo.event.ValidateListener)
     * @see #addColumnValidator(java.lang.String,java.lang.String,java.lang.String)
     */
    public void addColumnValidator(String columnName, Validator validator,String msg) {
        if (validator == null) {
            return;
        }
        if (columnValidators == null) {
            columnValidators = new HashMap<String,List<Validator>>(5);
        }
        if ( msg != null ) {
            validator.setMessage(msg);
        }
        List<Validator> vl = columnValidators.get(columnName.toUpperCase());
        if ( vl == null ) {
            vl = new ArrayList<Validator>(3);
            columnValidators.put(columnName.toUpperCase(), vl);

        }
        vl.add(validator);

    }
    /**
     * ������� �������� ��������� ������� �� ������, ���������������� ��������
     * ����� �������.
     *
     * @param columnName �������, ��� ������� ��������� ���������
     * @param validator ��������� ���������
     */
    public void removeColumnValidator(String columnName, Validator validator) {
        if (columnValidators == null || columnValidators.isEmpty()) {
            return;
        }
        List<Validator> vl = columnValidators.get(columnName);
        if ( vl == null || vl.isEmpty() )
            return;
        vl.remove(validator);

    }
   /**
     * ������� ��� ���������� �������, ��������������� ��������
     * ����� �������.
     *
     * @param columnName �������, ��� ������� ��������� ����������
     */
    public void removeColumnValidators(String columnName) {
        if (columnValidators == null || columnValidators.isEmpty()) {
            return;
        }
        columnValidators.remove(columnName);

    }

    //////////////////////////////////////////////////////////////////////////
    //   ROW VALIDATORS
    //////////////////////////////////////////////////////////////////////////

    /**
     * ������� � ��������� � ������ ����������� ����� ������ ���
     * ��������� ����������� ��������� � ��������� ��������� �� ������.
     *
     * @param expression ���������� ��������� ��� �������� ���������� �����
     *   �������
     * @param msg ��������� �� ������. ���� ����� �������� <code>null</code>,
     *    �� ����������� ��������� ��������� ���� ����������� ���������.
     * @return <code>null</code>, ���� <code>expression</code> �����
     *   <code>null</code> ��� ��� ����� ����� 0.
     *
     * @see #addRowValidateListener(tdo.event.ValidateListener)
     * @see #addRowValidator(tdo.impl.Validator,java.lang.String)
     */
    public Validator addRowValidator(String expression,String msg) {
        if (expression == null || expression.trim().length() == 0) {
            return null;
        }
        RowValidator rv = new RowValidator(this.tableServices);
        rv.setExpression(expression);
        if ( msg != null )
            rv.setMessage(msg);
        if (rowValidators == null) {
            rowValidators = new ArrayList<Validator>(5);
        }
        rowValidators.add(rv);
        return rv;
    }
    /**
     * ������� � ��������� � ������ ����������� ����� ������ ���
     * ��������� ���������� � ��������� ��������� �� ������.
     * @param validator ����������� ���������
     * @param msg ��������� �� ������. ���� ����� �������� <code>null</code>,
     *    �� ��������� <code>validator</code>��������� ���� �����������
     *    ���������. � ��������� ������, <code>msg</code> �������� �����������
     *    ��������� ����������.
     * @see #addRowValidateListener(tdo.event.ValidateListener)
     * @see #addRowValidator(java.lang.String, java.lang.String)
     */
    public void addRowValidator(Validator validator,String msg) {
        if (validator == null) {
            return;
        }
        if (rowValidators == null) {
            rowValidators = new ArrayList<Validator>(5);
        }
        if ( msg != null ) {
            validator.setMessage(msg);
        }
        rowValidators.add(validator);
    }
    /**
     * ������� �������� ��������� �� ������ ������������������ �����������.
     *
     * @param validator ��������� ���������
     */
    public void removeRowValidator(Validator validator) {
        if (rowValidators == null || rowValidators.isEmpty()) {
            return;
        }
        rowValidators.remove(validator);
    }

    /**
     * ��������� ��������� ��������� ���� ���������� ��� ��� ���������� � ������
     * ����������� ������. <p>
     *
     * ��������������� ��������� ���������� �� ������ ����������� ����, �������
     * � ���������� � �������� 0. ������ ���������, ������������ ������ ���������
     * ���������. ��� ����, ���� �������� <code>throwException</code> �����
     * <code>true</code> ������������� ����������
     * <code>tdo.impl.ValidateException</code>.
     * @param row ����������� ���
     * @param throwException ��������� ������� �� ��� ������ �����������
     *   ����������. �������� <code>true</code> ��������, ��� ����� ���������
     *   ���������� {@link tdo.impl.ValidateException} ��� ������, ������������
     *   ���� �� ����� �����������
     * @return <code>true</code> ��� ���������� ����������� �������, ��
     * ��������� ������. <code>false</code> - � ��������� ������ (��������
     *   ������, ���� �������� <code>throwException</code> ����� <code>false</code>.
     */
    public boolean validate(DataRow row, boolean throwException) {

        if (rowValidators == null || rowValidators.isEmpty()) {
            return true;
        }
        boolean b = true;
        int vindex = -1;
        Validator v = null;
        for (int i = 0; i < rowValidators.size(); i++) {
            v = rowValidators.get(i);
            if (!v.validate(row)) {
                b = false;
                vindex = i;
                break;
            }
        }
        //table.editProhibited = true;

        if (validateList != null && validateList.size() > 0 &&  vindex >= 0 ) {
            ValidateEvent e = new ValidateEvent(this, row, v );
            for (int i = 0; i < validateList.size(); i++) {
                validateList.get(i).processError(e);
            }
        }

        //editProhibited = false;
        if ( vindex >= 0 && throwException) {
            throw new ValidateException("Row validate ERROR", row, vindex);
        }
        return vindex < 0 ? true : false;

    }

    public void notifyListeners(Validator v, int validatorIndex, DataRow row, boolean throwException) {
        if (validateList != null && validateList.size() > 0  ) {
            ValidateEvent e = new ValidateEvent(this, row, v );
            for (int i = 0; i < validateList.size(); i++) {
                validateList.get(i).processError(e);
            }
        }

        //editProhibited = false;
        if ( throwException  ) {
            throw new ValidateException("Row validate ERROR", row, validatorIndex);
        }

    }
    
    /**
     * ��������� ��������� ��������� ����.
     * <p>���������� ���������� ������ {@link #validate(tdo.DataRow, boolean) ���
     * �������� ������� ��������� ������ <code>false</code>.
     * <p>
     * ��������������� ��������� ���������� �� ������ ����������� ����, �������
     * � ���������� � �������� 0. ������ ���������, ������������ ������ ���������
     * ���������.
     * @param row ����������� ���
     * @return <code>true</code> ��� ���������� ����������� �������, ��
     * ��������� ������. <code>false</code> - � ��������� ������
     */
    public boolean validate(DataRow row) {
        return this.validate(row, false);
    }
    
    /**
     * ��������� ��������� ��������� ���� ���������� ��� ��� ���������� � ������
     * ����������� ������. <p>
     *
     * ��������������� ��������� ���������� �� ������ ����������� ����, �������
     * � ���������� � �������� 0. ������ ���������, ������������ ������ ���������
     * ���������. ��� ����, ���� �������� <code>throwException</code> �����
     * <code>true</code> ������������� ����������
     * <code>tdo.impl.ValidateException</code>.
     * @param row ����������� ���
     * @param throwException ��������� ������� �� ��� ������ �����������
     *   ����������. �������� <code>true</code> ��������, ��� ����� ���������
     *   ���������� {@link tdo.impl.ValidateException} ��� ������, ������������
     *   ���� �� ����� �����������
     * @return <code>true</code> ��� ���������� ����������� �������, ��
     * ��������� ������. <code>false</code> - � ��������� ������ (��������
     *   ������, ���� �������� <code>throwException</code> ����� <code>false</code>.
     */
    public Validator validateSilent(DataRow row) {

        if (rowValidators == null || rowValidators.isEmpty()) {
            return null;
        }
        Validator result = null;
        Validator v;//isEmpty() = null;
        for (int i = 0; i < rowValidators.size(); i++) {
            v = rowValidators.get(i);
            if (! v.validate(row)) {
                result = v;
                break;
            }
        }
        return result;

    }
    
    /**
     * ��������� ��������� ��������� ���� ��� �������� ������� � ��� ��������
     * ����� ��������������� �������� �������.
     * <p>����� ����������� �� ����������, ���� �� ���������������� �� ������
     * ���������� ��� �������� ������� ��� ��� ������� ����� <code>null</code>.
     * <p>
     * ��������������� ��������� ���������� �� ������ ����������� �������, �������
     * � ���������� � �������� 0. ������ ���������, ������������ ������ ���������
     * ���������.
     * @param row ����������� ���
     * @param columnName
     * @param value
     * @return <code>true</code> ��� ���������� ����������� �������, ��
     * ��������� ������. <code>false</code> - � ��������� ������
     */
    public boolean validate(DataRow row, String columnName, Object value) {

        int vindex = -1;

        if (columnValidators == null || columnValidators.isEmpty() )
            return true;

        //String columnName = table.getColumns().get(columnIndex).getName();

        if ( columnName == null )
            return true;

        List<Validator> vl = columnValidators.get(columnName.toUpperCase().trim());
        if ( vl == null || vl.isEmpty() )
            return true;

        for (int i = 0; i < vl.size(); i++) {
            if ( ! vl.get(i).validate(row,value) ) {
                vindex = i;
                break;
            }
        }

        //editProhibited = true;

        if (columnValidateList != null && columnValidateList.size() > 0 &&  vindex >= 0 ) {
            ValidateEvent e = new ValidateEvent(this, row, columnName, vl.get(vindex), value);
            for (int i = 0; i < columnValidateList.size(); i++) {
                columnValidateList.get(i).processError(e);
            }
        }

        //editProhibited = false;

        return vindex < 0 ? true : false;
    }


    /**
     * ������������ ���������� ������� ��������  ������.<p>
     * @param l - ��� <code>ValidateListener</code>.
     */
    public void addRowValidateListener(ValidateListener l) {
        if (validateList == null) {
            validateList = new ArrayList<ValidateListener>();
        }
        validateList.add(l);
    }

    /**
     * ������� ���������� ������� ����������� ���� .<p>
     * @param l - ��� <code>ValidateListener</code>.
     */
    public void removeRowValidateListener(ValidateListener l) {
        if (validateList == null) {
            return;
        }
        validateList.remove(l);
    }

    /**
     * ������������ ���������� ������� ����������� �������.<p>
     * @param l - ��� <code>ValidateListener</code>.
     */
    public void addColumnValidateListener(ValidateListener l) {
        if (columnValidateList == null) {
            columnValidateList = new ArrayList<ValidateListener>();
        }
        columnValidateList.add(l);
    }

    /**
     * ������� ���������� ������� ����������� �������.<p>
     * @param l - ��� <code>ValidateListener</code>.
     */
    public void removeColumnValidateListener(ValidateListener l) {
        if (columnValidateList == null) {
            return;
        }
        columnValidateList.remove(l);
    }

    /**
     * ����������� ������ ������, ���������� ������� ������������ ����� 
     * ��������� �� ������ ��������� ���� � ����� �������� ���������� 
     * ������������������ ��������:
     * <ul>
     * <li>#i - ������ ���������� � ������ �����������</li>
     * <li>#ri - ������ ���� � �������</li>
     * <li>#e - ��������� ���������, ���� ������</li>
     * </ul>
     * @param row
     * @param v
     * @return
     */
    public String formatMessage(DataRow row, Validator v ) {
        String m = v.getMessage();
        if ( m.contains("#i") )
            m = m.replaceAll("#i" , "" + this.rowValidators.indexOf(v));
        if ( m.contains("#ri" ) )
            m = m.replaceAll("#ri" , "" + (row == null ? -1 : row.getIndex()) );
        if ( m.contains("#t") )
            m = m.replaceAll("#t" , this.tableServices.getFilterServices().getTableName());
        if ( m.contains("#e") ) {

            ExpressionContext ec = v.getExpressionContext();
            String r = "";
            if ( ec != null )
              r = ec.getExpressionString();
            r = r == null ? "" : r;
            m = m.replaceAll("#e" , r);
        }

        return m;
    }
    /**
     * ����������� ������ ������, ���������� ������� ������������ ����� 
     * ��������� �� ������ ��������� ���� � ����� �������� ���������� 
     * ������������������ ��������:
     * <ul>
     * <li>#i - ������ ���������� � ������ �����������</li>
     * <li>#ri - ������ ���� � �������</li>
     * <li>#t - ��� �������</li>
     * <li>#e - ��������� ���������, ���� ������</li>
     * <li>#cn - ��� �������</li>
     * <li>#v - ����� (���������) �������� �������</li>
     * </ul>
     * @param row
     * @param v
     * @param columnIndex
     * @param columnName
     * @return
     */
    public String formatMessage(DataRow row, Validator v, String columnName ) {
        String m = v.getMessage();
        if ( m.contains("#i") ) {
            //String cn = table.getColumns().get(columnIndex).getName();
            m = m.replaceAll("#i" , "" + this.columnValidators.get(columnName.toUpperCase().trim()).indexOf(v));
        }
        if ( m.contains("#ri" ) )
            m = m.replaceAll("#ri" , "" + (row == null ? -1 : row.getIndex()) );
        if ( m.contains("#t") )
            m = m.replaceAll("#t" , this.tableServices.getFilterServices().getTableName());
        if ( m.contains("#e") ) {
            ExpressionContext ec = ((ColumnValidator)v).getExpressionContext();
            String r = "";
            if ( ec != null )
                r = ec.getExpressionString();
            r = r == null ? "" : r;
            m = m.replaceAll("#e" , r);
        }

        if ( m.contains("#�n") )
            m = columnName != null ? "" : m.replaceAll("#cn" , columnName );

        if ( m.contains("#v") ) {
            RowExpressionContext ec = (RowExpressionContext) v.getExpressionContext();
            Object o = "";
            if ( ec != null )
                o = ec.getParameterValue(row, "value");
            String r = ( o == null ? "" : o.toString());
            m = m.replaceAll("#v" , r);
        }

        return m;

    }

}//class ValidationManager
