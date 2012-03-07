/*
 * ObjectDataCellProvider.java
 * 
 */
package tdo;

import tdo.service.TableServices;

/**
 *      ����� �������� ��������� ������ ������ �������, �����������������
 * ������������ Java Beans.
 * 
 *      ��������� ������������� java bean �� ��� ����� ����� � �������, � ��� 
 * ��������� �����, ������ � ������� �������������� � �������������� 
 * �������������� �������.
 * 
 *  <p>��������� ����� ��������� ��������� <code>tdo.dataCellCollection</code>,
 * �� �� ����� �������������� ���������, �������� � {@link tdo.DefaultDataRow}.
 * 
 *  <p>������ ���, ������������  {@link tdo.Table} �������� ����������� ���������
 * <code>DataCellCollection</code>.
 * 
 * <p> �� ���������, ����������� ������� ������������ ������� ������� � 
 * ��������� ������� ������� ������ � ��������� �����:
 * <ol>
 *   <li>���� ��� ������� � �������� <code><i>columnIndex</i></code>
 *       ��������� ������� getKind() == DataColumn.CALC_KIND, �� ��� ��� ���
 *       ��������������� ������ � ��������� �����. 
 *   </li>
 *   <li> ���� ��� ������� � �������� <code><i>columnIndex</i></code>
 *       ��������� ������� getKind() == DataColumn.DATA_KIND, �� �� 
 *       ������������� ��������� ������ ������, �������, � ���� �������,
 *       ������������� ���� ��� �������� ������� ������.
 *   </li>
 * </ol>
 * 
 * <p>����� �������, ����������� ������� �� ����� ���� ��������� ������.
 * 
 * <p>��������� ����� ������� � �������� <code>tdo.Table</code>. ��� ����� 
 * ����������� ����� ������������� �������� 
 * {@link tdo.service.TableServices#getCellServices()).
 * 
 * <p>
 */
public class ObjectDataCellCollection implements DataCellCollection {

    /**
     * �������� �������, ��� ������� ��������� ��������� ������.
     */
    protected TableServices context;
    /**
     * ��������� �������� �������� - ������ �� ������ ������, ��� �������� 
     * ������� ��������� �����.
     */
    private Object rowObject;
    /**
     * ��������� �������� �������� - ����� ������� ������.
     */
    private Class objectClass;
//    private ObjectRowSupport objectRowSupport;
    /**
     * 
     */
    private ObjectValueAccessor objectRowSupport;

    /**
     * ������� ����� ��������� ��� ��������� ��������� �������. 
     * ���������� ���������������� ���������� ������������� ������� ��������,
     * ���� ���� ����������� �������� �������� <code>objectClass</code> �
     * <code>objectRowSupport</code>. 
     * 
     * @param context �������� �������
     * @see 
     */
    public ObjectDataCellCollection(TableServices context) {
        this.context = context;
        rowObject = null;
        objectClass = null;
        objectRowSupport = null;
    }

    /**
     * ������� ����� ��������� ��� ��������� ��������� ������� � ���������
     * �������, ������������ ��������� <code>tdo.ObjectValueAccessor</code>.
     * ���c������� ��������� ��������� ��������� �����, ��� �������� java
     * ��� ������������� ������������, ��������� ������� ��� ����������������
     * ������ ������, ����������� ������������ ������� ��������� 
     * <code>ObjectValueAccessor</code> � �� ������� ������������� ������
     * <code>ObjectRowSupport</code>. 
     * <p>����������� ������ ��� ������������ � ����������� <code>JUnit</code>.
     * @param context �������� �������
     * @param va 
     * @see tdo.ObjectValueAccessor
     * @see tdo.ObjectRowSupport
     * @see tdo.ObjectDataRowSupport     
     */
    public ObjectDataCellCollection(TableServices context, ObjectValueAccessor va) {
        this(context);
        objectRowSupport = va;
    }

    /**
     * ���������� ������, ��� �������� ������� ������ ���������.
     * @return java bean ������ ������
     * @see #setObject(java.lang.Object)
     */
    @Override
    public Object getObject() {
        return this.rowObject;
    }

    /**
     * ���������� �����, ������� �������� ����� ���� ������������
     * ��� �������� ���������.
     * @return ����� java bean ������� ������
     * @see #setObjectClass(java.lang.Class) 
     */
    public Class getObjectClass() {
        return this.objectClass;
    }

    /**
     * ������������� ������, ��� �������� ����� ����������� ���������.
     * @param  rowObject ����� �������� �������
     * @see #getObject
     */
    public void setObject(Object rowObject) {
        this.rowObject = rowObject;
    }

    /**
     * ������������� �����, ������� �������� ����� ���� ������������
     * ��� �������� ���������.
     * 
     * @param  objectClass ����� �������� ������ ��������
     * @see #setObjectClass(java.lang.Class) 
     */
    public void setObjectClass(Class objectClass) {
        this.objectClass = objectClass;
    }

    /**
     * ������������� ����� �������� �������, ���������� ���������� 
     * �������������� ��� java bean �������, ������������ ������������
     * {@link tdo.Table}.
     * 
     * @param objectRowSupport ����� �������� ������� ���������
     */
    protected void setObjectRowSupport(ObjectRowSupport objectRowSupport) {
        this.objectRowSupport = objectRowSupport;
    }

    /**
     * ���������� �������� �������� bean ������� �� ��������� �����.
     * @param pName ��� ��������
     * @return �������� �������� �������
     * @see #getFieldValue
     * @see #setPropertyValue
     */
    protected Object getPropertyValue(String pName) {
        return this.objectRowSupport.getValue(pName, this.rowObject);
    }

    /**
     * ���������� �������� ���� bean ������� �� ��������� �����.
     * @param pName ��� ����
     * @return �������� �������� �������
     */
    protected Object getFieldValue(String fName) {
        return this.objectRowSupport.getValue(fName, this.rowObject);
    }

    /**
     * ������������� ����� �������� ���� bean ������� ��� ��������� �����.
     * @param fName ��� ����
     * @param value ����� �������� ��� ����
     */
    protected void setFieldValue(String fName, Object value) {
        this.objectRowSupport.setValue(fName, this.rowObject, value);
    }

    /**
     * ������������� ����� �������� �������� bean ������� ��� ��������� �����.
     * @param pName ��� ����
     * @param value ����� �������� ��� ��������
     */
    protected void setPropertyValue(String pName, Object value) {
        this.objectRowSupport.setValue(pName, this.rowObject, value);
    }

    /**
     * ���������� �������� �������� ��� ���� ������� �� ������� �������.
     * ������ ������� - ��� ������, ��� ����������
     * {@link tdo.DataColumnCollection}.
     * @param columnIndex ������ ������� � ��������� ������� �������
     * @return �������� �������� ��� ���� �������
     * @see #getValue(String) 
     * @see #setValue(Object,int) 
     */
    @Override
    public Object getValue(int columnIndex) {
        Object result = null;
        String pname = context.getCellServices().getPropertyName(columnIndex);
        String fname = context.getCellServices().getFieldName(columnIndex);

        //DataColumn column = context.getColumnServices().columns(columnIndex);
        if (pname != null) {
            result = getPropertyValue(pname);
        } else {
            if (fname != null) {
                result = getFieldValue(fname);
            }
        }
        return result;
    }

    /**
     * ���������� �������� �������� ��� ���� ������� �� ����� �������.
     * ��� ������� - ��� ���, ��� ����������
     * {@link tdo.DataColumn}.
     * @param columnName ��� ������� � ��������� ������� �������
     * @return �������� �������� ��� ���� �������
     * @see #getValue(int) 
     * @see #setValue(Object,String) 
     */
    @Override
    public Object getValue(String columnName) {
        return getValue(context.getCellServices().getColumnIndex(columnName));
    }

    /**
     * ������������� ����� �������� �������� ��� ���� ������� �� ������� �������.
     * ������ ������� - ��� ������, ��� ����������
     * {@link tdo.DataColumnCollection}.
     * @param value ����� �������� �������� ��� ����
     * @param columnIndex ������ ������� � ��������� ������� �������
     * @see #getValue(int) 
     */
    @Override
    public Object setValue(Object value, int columnIndex) {
        int mapped = context.getCellServices().getCellIndex(columnIndex);
        if (mapped == -1) {
            return null;
        }
        Object oldValue = this.getValue(columnIndex);
        String pname = context.getCellServices().getPropertyName(columnIndex);
        String fname = context.getCellServices().getFieldName(columnIndex);

        if (pname != null) {
            setPropertyValue(pname, value);
        } else {
            if (fname != null) {
                setFieldValue(fname, value);
            }
        }
        return oldValue;
    }

    /**
     * ������������� ����� �������� �������� ��� ���� ������� �� ������� �������.
     * ������ ������� - ��� ������, ��� ����������
     * {@link tdo.DataColumnCollection}.
     * @param value ����� �������� �������� ��� ����
     * @param columnIndex ������ ������� � ��������� ������� �������
     * @see #getValue(int) 
     * @see #setValue(Object,int) 
     */
    protected void setValue1(Object value, int columnIndex) {
        int mapped = context.getCellServices().getCellIndex(columnIndex);
        if (mapped == -1) {
            return;
        }
        String pname = context.getCellServices().getPropertyName(columnIndex);
        String fname = context.getCellServices().getFieldName(columnIndex);

        if (pname != null) {
            setPropertyValue(pname, value);
        } else {
            if (fname != null) {
                setFieldValue(fname, value);
            }
        }
    }

    /**
     * ������������� ����� �������� �������� ��� ���� ������� �� ����� �������.
     * ��� ������� - ��� ���, ��� ����������
     * {@link tdo.DataColumn}.
     * @param value ����� �������� �������� ��� ����
     * @param columnName ��� ������� � ��������� ������� �������
     * @see #getValue(int) 
     * @see #getValue(String) 
     * @see #setValue(Object,int) 
     */
    @Override
    public Object setValue(Object value, String columnName) {
        return setValue(value, context.getCellServices().getColumnIndex(columnName));
    }

    /**
     * �������� ���������� ����� �������� ��������� � ������ ������� ���������.
     * @param source ���������-�������� ����� ��� �����������.
     */
    @Override
    public void copyCells(DataCellCollection source) {
        for (int i = 0; i < context.getCellServices().getColumnCount(); i++) {
            if (context.getCellServices().isDataKind(i)) {
            if ( ! context.getCellServices().copyCell(i,source.getValue(i),getValue(i)) )
                this.setValue(source.getValue(i), i);
            }
        }
    }

    /**
     * �������� ��������� ��������� ������� �������� �������� � ��������� �����.
     * i-� ������� ������� ������������ �� ����� i-�� �������� ������ �����. 
     * ��������� ������ �� ����������.
     * @param source ������-�������� ������� �����
     */
    @Override
    public void copyCells(Object[] source) {
/*        for (int i = 0; i < source.length; i++) {
            this.setValue1(source[i], i);
        }
 */
        for (int i = 0; i < source.length; i++) {
            int columnIndex = context.getCellServices().columnIndexByCell(i);
            if ( ! context.getCellServices().copyCell(columnIndex, source[i], getValue(i)))
                 this.setValue1(source[i], i);
        }
        
    }

    /**
     * �� ���������� ������� ��������
     * @param columnIndex
     */
    @Override
    public void columnAdded(int columnIndex) {
    }

    /**
     * �� ���������� ������� ��������
     * @param columnIndex
     */
    @Override
    public void columnRemoved(int columnIndex) {
    }

    /**
    /**
     * �� ���������� ������� ��������
     * @param columnIndex
     * @param oldCellIndex
     * @param newCellIndex
     */
    @Override
    public void columnMoved(int columnIndex, int oldCellIndex, int newCellIndex) {
    }
    /**
     * ���������� ���������� ��������� ���������.
     * @return �����: <i>���������� ����� + ���������� �������</i>
     */
    public int size() {
        return this.objectRowSupport.size();
    }

}
