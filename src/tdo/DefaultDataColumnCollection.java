/*
 * DefaultDataColumnCollection.java
 *
 */
package tdo;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static tdo.DataColumn.CALC_KIND;
import tdo.event.DataColumnCollectionEvent;
import tdo.event.DataColumnCollectionListener;

/**
 * ��������� ��������� <code>DataColumnCollection</code>.
 * ������������ ��������� ������ ��� ���������  {@link tdo.DataColumn}
 * � ���� ������ ���� <code>java.util.ArrayList</code>. ��� ����� �������� � 
 * ������������ ������� � ������ ������� ����������� �������������� 
 * <code>java.util.Map</code> ���������, ����������� �� ����� ������� 
 * �������� ������ � �������� <code>DataColumn</code>. <p>
 * �������� ��������� ����� ��������� ������ ���� ������� � �������� ������.<p>
 * ������ ������ ��������� ��������� ������� � ����� ���������, ��������� 
 * ������� � �������� �������, ������� ������� �� ��������� � ���������� �������
 * ������ ���������. <p> 
 * ����� �������� ����� �������, ������� �� ��������� ����������������, �� 
 * �������� �������� �������� ��� ������������� � ���� ��� IDE. ��������, ������
 * ������������� ������� <code>add(...)</code> ���� ����� {@link #addInteger},
 * � ����� ������ �������� ������ ��� �������� ����� ������������ �����. <p>
 * ���� ����� ��������� ������ <code>createColumn</code>, ��� ���������������
 * �����������, ��, �� ����� ����, ���������� ������� ������������ �������,
 * ����������� ��������� {@link tdo.ColumnProvider}. ��� ���������, ��-������ 
 * ����� ���������� ������������ ������������, � ��-������ ������ �����
 * ����������� �� ����� �������, �������� ����� ����� �����������. ���� ���
 * �������� ���������� ������ ������������ ����������� ��� ����������, ��
 * �� ��������� ����� ����������� ����� {@link tdo.DefaultColumnProvider}, 
 * ������� ������������ �������� �������, ��������������� �������-���������, 
 * ����� ��� <code>java.lang.Integer</code>, � .�.�., ������� 
 * <code>java.lang.String, java.util.Date,java.sql.Time, java.sql.Timestamp,
 * java.math.BigDecimal</code>. 
 * <code>DefaultColumnProvider</code> ����� ������������ mapping ����� ������,
 * ������������� � ������ <code>java.util.Types</code> � �������� Java. <p>
 * ����� <code>DefaultDataColumnCollection</code> ������������� ����� ����������� 
 * � ���������� ���� {@link tdo.ColumnProvider}, ����������� ������� ������ 
 * ���� �������.<p>
 * ���������� ��������, ��� ����� �� ����� �����-���� ����� ������ �� ���� 
 * <code>Table, DataRow, DataRowCollection</code> � ������. ������, �� ���������
 * ���������������� ����������� ������� 
 * {@link tdo.event.DataColumnCollectionListener} � ���������� ��� �������
 * ��������� � ��������� �������, �������� ���� ������������������ ���������� 
 * ���� {@link tdo.event.DataColumnCollectionListener}.<p>
 * 
 */
public class DefaultDataColumnCollection implements DataColumnCollection, Serializable {

    private List<DataColumnCollectionListener> dataColumnsListenerList = new ArrayList<DataColumnCollectionListener>();
    private PropertyChangeSupport changeSupport;
    /**
     * ���������� ��������� ��������� ���� {@link tdo.DataColumn}.
     */
    private List<DataColumn> columns;
    /**
     * ������ ���� <i>����/��������</i>, ��� � �������� <i>�����</i> ������������
     * ��� ������� � ������� ��������, � <i>��������</i> - ������ �� ������ ����
     * {@link tdo.DataColumn}. ������������ ��� �������� ������� � ��������.
     * �������������� ������� ��� ��������� ������� � �������� ������� � 
     * ���������.
     */
    private Map<String, DataColumn> columnMap;
    private PropertyChangeHandler propertyChangeHandler;
    /**
     * ������� ��� ���������� ������ ��������� �������.
     */
    private ColumnProvider columnProvider;

    /**
     * ������� ����� ��������� ������. 
     * ������� ����� ������ ���� {@link tdo.DefaultColumnProvider} ��������� ���
     * � �������� �������� ���������� �������. 
     * @see #DefaultDataColumnCollection(ColumnProvider)
     * @see #DefaultDataColumnCollection(DataColumn[])
     * @see #DefaultDataColumnCollection(DataColumn[],ColumnProvider)
     */
    public DefaultDataColumnCollection() {
        this(new DefaultColumnProvider());
    }

    /**
     * ������� ����� ��������� ������. 
     * ������� ����� ������ ���� {@link tdo.DefaultColumnProvider} � ��������� ���
     * � �������� �������� ���������� �������. ��������� �������, ��������
     * ����������-�������� � ���������.
     * @param columnArr ������ �������, ����������� ��� �������� ����������
     *    ������ � ���������.
     * @see #DefaultDataColumnCollection(ColumnProvider)
     * @see #DefaultDataColumnCollection()
     * @see #DefaultDataColumnCollection(DataColumn[],ColumnProvider)
     */
    public DefaultDataColumnCollection(DataColumn[] columnArr) {
        this(columnArr, new DefaultColumnProvider());
    }

    /**
     * ������� ����� ��������� ������. 
     * ������� ����� ������ ���� {@link tdo.DefaultColumnProvider} � ��������� ���
     * � �������� �������� ���������� �������. ��������� �������, ��������
     * ����������-�������� � ���������.
     * @param cp ������� ��������� �������, ����������� ������������ �������.
     * @see #DefaultDataColumnCollection(DataColumn[])
     * @see #DefaultDataColumnCollection()
     * @see #DefaultDataColumnCollection(DataColumn[],ColumnProvider)
     */
    public DefaultDataColumnCollection(ColumnProvider cp) {
        this.columnProvider = cp;
        initColumns();
        this.propertyChangeHandler = new PropertyChangeHandler();
    }

    /**
     * ������� ����� ��������� ������. 
     * ������� ����� ������ ���� {@link tdo.DefaultColumnProvider} � ��������� ���
     * � �������� �������� ���������� �������. ��������� �������, ��������
     * ����������-�������� � ���������.
     * @param columnArr ������ �������, ����������� ��� �������� ����������
     *    ������ � ���������.
     * @param cp ������� ��������� �������, ����������� ������������ �������.
     * @see #DefaultDataColumnCollection(DataColumn[])
     * @see #DefaultDataColumnCollection()
     * @see #DefaultDataColumnCollection(ColumnProvider)
     */
    public DefaultDataColumnCollection(DataColumn[] columnArr, ColumnProvider cp) {
        this.columnProvider = cp;
        initColumns();
        this.propertyChangeHandler = new PropertyChangeHandler();
        for (int i = 0; i < columnArr.length; i++) {
            add(columnArr[i]);
        }
    }

    /**
     * ���������� ���������� �������, �������� ������� ����������, �������������
     * ����������.
     * 
     * @param columnKind ��������� ���� �� ��������:
     *  <ul>
     *      <li><i>DataColumn.DATA_KIND</i> - ������� ������� ������;</li>
     *      <li><i>DataColumn.CALC_KIND - ����������� �������;</i></li>
     *      <li><i>DataColumn.LOOKUP_KIND - � ������ ������ �� ������������;</i></li>
     *      <li><i>DataColumn.ALL_KIND - ��� �������.</i></li>
     * </ul>
     * @return
     */
    @Override
    public int getCount(int columnKind) {

        if (columnKind == DataColumn.ALL_KIND) {
            return this.getCount();
        }
        int result = 0;

        for (DataColumn dc : this.columns) {
            if (dc.getKind() == columnKind) {
                result++;
            }
        }
        return result;
    }

    /**
     * ������� � ���������� ����� ��������� <code>DataColumn</code> ��������� 
     * ���� � � �������� ������.
     * ����� ���������� �������� ������� ������������ ������ ��������
     * ���������� �������.
     * @param type ��� ������, ������������� ��������
     * @param columnName ��� ����� �������, ������� ����� ���� ��������
     *   ������� ������ {@link tdo.DataColumn#getName}.
     * @return ����� ��������� �������
     */
    @Override
    public DataColumn createColumn(Class type, String columnName) {
        return this.columnProvider.createColumn(type, columnName);
    }

    /**
     * ������� � ���������� ����� ��������� <code>DataColumn</code> ��������� 
     * sql-���� � � �������� ������.
     * ����� ���������� �������� ������� ������������ ������ ��������
     * ���������� �������.
     * @param sqlType ��� ������, ������������� ��������, ������� ���������
     *    ���� �� ��������, ������������ ������� <code>java.sql.Types</code>.
     * @param columnName ��� ����� �������, ������� ����� ���� ��������
     *   ������� ������ {@link tdo.DataColumn#getName}.
     * @return ����� ��������� �������
     */
    @Override
    public DataColumn createColumn(int sqlType, String columnName) {
        return this.columnProvider.createColumn(sqlType, columnName);
    }

    /**
     * ���������� �������� �����������(��������) �������� � ���������.
     * ��������� ��������� �������:
     * <ul>
     *   <li>������� ���������� ��� ��������� ��� ������� 
     *      <i>(� ������� ��������)</i>, ���� �������� ��� ����� 
     *       <code>null</code>
     *   </li>
     *   <li>��������� �������� ��� �� ������������ � ��������� �, ���� 
     *       ��������� ��������, �� ������������ ���������� ���� 
     *      <code>IllegalArgumentException</code>.
     *   </li>
     *   <li>������� ����� ��������� ������� ������� ������ 
     *      {@link createColumn(int,String)} � ���������� ��� � ����� ������.
     *   </li>
     * </ul>
     * @param sqlType sql-��� ����������� ���� �� ��������, ������������
     *      java.sql.Types
     * @param columnName ��� ������� ��� <code>null</code>
     * @return ��������� ������� �������
     */
    protected DataColumn prepareAddColumn(int sqlType, String columnName) {

        DataColumn column = null;
        String pcolumnName = columnName;
        if (columnName == null) {
            pcolumnName = this.produceColumnName().toUpperCase();
        }
        if (find(pcolumnName.toUpperCase()) >= 0) //pcolumnName = this.produceColumnName().toUpperCase();            
        {
            throw new IllegalArgumentException("Dublicate columnName '" + columnName.toUpperCase() + "'");
        }
        column = createColumn(sqlType, pcolumnName.toUpperCase());
        return column;
    }

    /**
     * ���������� �������� �����������(��������) �������� � ���������.
     * ��������� ��������� �������:
     * <ul>
     *   <li>������� ���������� ��� ��������� ��� ������� 
     *      <i>(� ������� ��������)</i>, ���� �������� ��� ����� 
     *       <code>null</code>
     *   </li>
     *   <li>��������� �������� ��� �� ������������ � ��������� �, ���� 
     *       ��������� ��������, �� ������������ ���������� ���� 
     *      <code>IllegalArgumentException</code>.
     *   </li>
     *   <li>������� ����� ��������� ������� ������� ������ 
     *      {@link createColumn(Class,String)} � ���������� ��� � ����� ������.
     *   </li>
     * </ul>
     * @param type ����� ��������, ��������������� �������
     * @param columnName ��� ������� ��� <code>null</code>
     * @return ��������� ������� �������
     * @throws IllegalArgumentException ���� �������� ��� ������� ��� �����
     *     ���� �� ������� ���������
     */
    protected DataColumn prepareAddColumn(Class type, String columnName) {

        DataColumn column = null;
        String pcolumnName = columnName;
        if (columnName == null) {
            pcolumnName = this.produceColumnName().toUpperCase();
        }
        if (find(pcolumnName.toUpperCase()) >= 0) {
            throw new IllegalArgumentException("Dublicate columnName '" + columnName.toUpperCase() + "'");
        }

        column = createColumn(type, pcolumnName.toUpperCase());
        return column;
    }

    /**
     * ������� ������� ��������� sql-���� � ���������� �� 
     * � ����� ��������� �������. <p>
     * ���������� ���������� ������ {@link #add(int,String)}, ��������� 
     * <code>null</code> ��������� ������� ���������
     * @param sqlType �����, ����������� ���� �� �������� ��������,
     *     ������������ � ������ <code>java.sql.Types</code>.
     * @return ����� ��������� �������
     * @see #add(int,String)
     */
    @Override
    public DataColumn add(int sqlType) {
        //String columnName = this.produceColumnName().toUpperCase();
        return add(sqlType, null);
    }

    /**
     * ������� ������� ��������� sql-���� � � �������� ������  � ���������� �� 
     * � ����� ��������� �������. 
     * 
     * ���� ���������� ������� � ����� �� ������, ��� �������� ������� ���������,
     * �� ������������� ���������� IllegalArgumentException.
     * 
     * @param sqlType �����, ����������� ���� �� �������� ��������,
     *     ������������ � ������ <code>java.sql.Types</code>.
     * @param columnName ��� ������� ��� <code>null</code>
     *
     * @return ����� ��������� �������
     * 
     * @throws IllegalArgumentException ���� �������� ��� ������� ��� �����
     *     ���� �� ������� ���������
     */
    @Override
    public DataColumn add(int sqlType, String columnName) {
        DataColumn column = prepareAddColumn(sqlType, columnName);
        appendColumn(column);
        return column;
    }

    /**
     * ������� ������� � ��������� sql-�����, ������, ������������ � 
     * <i>���������� ��������</i>  � ���������� �� � ����� ��������� �������. 
     * 
     * ���� ���������� ������� � ����� �� ������, ��� �������� ������� ���������,
     * �� ������������� ���������� IllegalArgumentException.
     * 
     * @param sqlType �����, ����������� ���� �� �������� ��������,
     *     ������������ � ������ <code>java.sql.Types</code>.
     * @param columnName ��� ������� ��� <code>null</code>
     *
     * @param precision 
     * @return ����� ��������� �������
     * 
     * @throws IllegalArgumentException ���� �������� ��� ������� ��� �����
     *     ���� �� ������� ���������
     */
    //@Override
/*    public DataColumn add(int sqlType, String columnName, int precision,
            int scale) {
        DataColumn column = prepareAddColumn(sqlType, columnName);
        column.setScale(scale);
        column.setSize(precision);
        appendColumn(column);
        return column;
    }
*/
    /**
     * ��������� ������������ ��������� ������� � ����� ��������� �������. 
     * 
     * ���� ���������� ������� � ����� �� ������, ��� �������� ������� ���������,
     * �� ������������� ���������� IllegalArgumentException.
     * 
     * @param column ����������� �������
     * 
     * @throws IllegalArgumentException ���� �������� ��� ������� ��� �����
     *     ���� �� ������� ���������
     */
    @Override
    public void add(DataColumn column) {

        if (column.getName() == null) {
            column.setName(this.produceColumnName().toUpperCase());
        } else if (find(column.getName()) >= 0) {
            throw new IllegalArgumentException("Dublicate columnName '" + column.getName().toUpperCase() + "'");
        }


        appendColumn(column);
    }

    /**
     * ������� ������� ��������� ���� � ���������� �� � ����� ��������� �������. <p>
     * ���������� ���������� ������ {@link #add(Class,String)}, ��������� 
     * <code>null</code> ��������� ������� ���������
     * @param type java ��� ������, ������������� ����� ��������
     * @return ����� ��������� �������
     * @see #add(int)
     * @see #add(Class,String)
     */
    @Override
    public DataColumn add(Class type) {
        return add(type, null);
    }

    /**
     * ������� ������� ��������� ���� � � �������� ������ � 
     * ���������� �� � ����� ��������� �������. 
     * 
     * ���� ���������� ������� � ����� �� ������, ��� �������� ������� ���������,
     * �� ������������� ���������� IllegalArgumentException.
     * 
     * @param type java-��� ������, ������������� ��������
     * @param columnName ��� ������� ��� <code>null</code>
     *
     * @return ����� ��������� �������
     * 
     * @throws IllegalArgumentException ���� �������� ��� ������� ��� �����
     *     ���� �� ������� ���������
     */
    @Override
    public DataColumn add(Class type, String columnName) {
        DataColumn column = prepareAddColumn(type, columnName);
        appendColumn(column);
        return column;

    }

    /**
     * ������� ����������� ������� ��������� ���� � � �������� ������ � 
     * ���������� �� � ����� ��������� �������. 
     * 
     * ������� �������� �����������, ���� �� ����� 
     * {@link tdo.DataColumn#getKind()} ���������� �������� 
     * <code>DataColumn.CALC_KIND}</code>. 
     * 
     * ���� ���������� ������� � ����� �� ������, ��� �������� ������� ���������,
     * �� ������������� ���������� IllegalArgumentException.
     * 
     * @param type java-��� ������, ������������� ��������
     * @param columnName ��� ������� ��� <code>null</code>
     *
     * @return ����� ��������� �������
     * 
     * @throws IllegalArgumentException ���� �������� ��� ������� ��� �����
     *     ���� �� ������� ���������
     */
    @Override
    public DataColumn addCalculated(Class type, String columnName) {
        //int sqlType = getSqlType(type);
        DataColumn column = prepareAddColumn(type, columnName);
        column.setKind(DataColumn.CALC_KIND);
        appendColumn(column);
        return column;

    }

    /**
     * ������� ����������� ������� ��������� sql-���� � � �������� ������ 
     * � ���������� �� � ����� ��������� �������. 
     * 
     * ������� �������� �����������, ���� �� ����� 
     * {@link tdo.DataColumn#getKind()} ���������� �������� 
     * <code>DataColumn.CALC_KIND}</code>. 
     * 
     * ���� ���������� ������� � ����� �� ������, ��� �������� ������� ���������,
     * �� ������������� ���������� IllegalArgumentException.
     * 
     * @param sqlType sql-��� ������, ������������� ��������
     * @param columnName ��� ������� ��� <code>null</code>
     *
     * @return ����� ��������� �������
     * 
     * @throws IllegalArgumentException ���� �������� ��� ������� ��� �����
     *     ���� �� ������� ���������
     */
/*    @Override
    public DataColumn addCalculated(int sqlType, String columnName) {
        DataColumn column = prepareAddColumn(sqlType, columnName);
        column.setKind(DataColumn.CALC_KIND);
        appendColumn(column);
        return column;

    }
*/
    /**
     * ������� <i>�����������</i> ������� � ��������� sql-�����, 
     * ������, ������������ �  <i>scale</i>  � ���������� �� � ����� ���������
     * �������. 
     * ������� �������� �����������, ���� �� ����� 
     * {@link tdo.DataColumn#getKind()} ���������� �������� 
     * <code>DataColumn.CALC_KIND}</code>. 
     * 
     * ���� ���������� ������� � ����� �� ������, ��� �������� ������� ���������,
     * �� ������������� ���������� IllegalArgumentException.
     * 
     * @param sqlType �����, ����������� ���� �� �������� ��������,
     *     ������������ � ������ <code>java.sql.Types</code>.
     * @param columnName ��� ������� ��� <code>null</code>
     *
     * @param precision ����������� ������
     * @param scale 
     * @return ����� ��������� �������
     * 
     * @throws IllegalArgumentException ���� �������� ��� ������� ��� �����
     *     ���� �� ������� ���������
     */
    //@Override
/*    public DataColumn addCalculated(int sqlType, String columnName, int precision, int scale) {
        DataColumn column = prepareAddColumn(sqlType, columnName);
        column.setKind(DataColumn.CALC_KIND);
        column.setPrecision(precision);
        column.setScale(scale);
        appendColumn(column);
        return column;
    }
*/
    /**
     * 
     * @param sqlType
     * @param columnName
     * @return
     */
    //@Override
/*    public DataColumn addLookup(int sqlType, String columnName) {
        DataColumn column = prepareAddColumn(sqlType, columnName);
        column.setKind(DataColumn.LOOKUP_KIND);
        appendColumn(column);
        return column;
    }
*/
    //@Override
/*    public DataColumn addLookup(int sqlType, String columnName, int precision, int scale) {
        DataColumn column = prepareAddColumn(sqlType, columnName);
        if (column == null) {
            return null;
        }
        column.setKind(DataColumn.LOOKUP_KIND);
        column.setPrecision(precision);
        column.setScale(scale);
        appendColumn(column);
        return column;

    }
*/
    @Override
    public DataColumn addLookup(Class type, String columnName) {
        //int sqlType = getSqlType(type);
        DataColumn column = prepareAddColumn(type, columnName);
        column.setKind(DataColumn.LOOKUP_KIND);
        appendColumn(column);
        return column;
    }

    /**
     * ������� ������� ����  <code>java.lang.Integer</code> c
     * �������� ������  � ���������� �� � ����� ��������� �������. 
     * 
     * ����� �� ��������� ���������������� � ������������ ��� �������� �
     * �������������. <p>
     * 
     * ���� ���������� ������� � ����� �� ������, ���������,
     * �� ������������� ���������� IllegalArgumentException.
     * 
     * @param columnName ��� ������� ��� <code>null</code>
     *
     * @return ����� ��������� �������
     * 
     * @throws IllegalArgumentException ���� �������� ��� ������� ��� �����
     *     ���� �� ������� ���������
     */
    //@Override
/*    public DataColumn addInteger(String columnName) {
        DataColumn column = prepareAddColumn(java.sql.Types.INTEGER, columnName);
        appendColumn(column);
        return column;
    }
*/
    /**
     * ������� ������� ����  <code>java.math.BigDecimal</code> c
     * �������� ������  � ���������� �� � ����� ��������� �������. 
     * 
     * ����� �� ��������� ���������������� � ������������ ��� �������� �
     * �������������. <p>
     * 
     * ���� ���������� ������� � ����� �� ������, ���������,
     * �� ������������� ���������� IllegalArgumentException.
     * 
     * @param columnName ��� ������� ��� <code>null</code>
     *
     * @return ����� ��������� �������
     * 
     * @throws IllegalArgumentException ���� �������� ��� ������� ��� �����
     *     ���� �� ������� ���������
     */
    //@Override
/*    public DataColumn addDecimal(String columnName) {
        DataColumn column = prepareAddColumn(java.sql.Types.DECIMAL, columnName);
        appendColumn(column);
        return column;
    }
*/
    /**
     * ������� ������� ����  <code>java.math.BigDecimal</code> 
     * �������� ������, ������������ � ���������� �������� � ���������� �� � 
     * ����� ��������� �������. 
     * 
     * ����� �� ��������� ���������������� � ������������ ��� �������� �
     * �������������. <p>
     * 
     * ���� ���������� ������� � ����� �� ������, ���������,
     * �� ������������� ���������� IllegalArgumentException.
     * 
     * @param columnName ��� ������� ��� <code>null</code>
     *
     * @param precision ����������� ���������� ������
     * @param scale ���������� �������
     * @return ����� ��������� �������
     * 
     * @throws IllegalArgumentException ���� �������� ��� ������� ��� �����
     *     ���� �� ������� ���������
     */
    //@Override
/*    public DataColumn addDecimal(String columnName, int precision, int scale) {
        DataColumn column = prepareAddColumn(java.sql.Types.DECIMAL, columnName);
        column.setPrecision(precision);
        column.setScale(scale);
        appendColumn(column);
        return column;
    }
*/
    /**
     * ������� ������� ����  <code>java.lang.Double</code> 
     * c �������� ������  � ���������� �� � ����� ��������� �������. 
     * 
     * ����� �� ��������� ���������������� � ������������ ��� �������� �
     * �������������. <p>
     * 
     * ���� ���������� ������� � ����� �� ������, ���������,
     * �� ������������� ���������� IllegalArgumentException.
     * 
     * @param columnName ��� ������� ��� <code>null</code>
     *
     * @return ����� ��������� �������
     * 
     * @throws IllegalArgumentException ���� �������� ��� ������� ��� �����
     *     ���� �� ������� ���������
     */
    //@Override
/*    public DataColumn addDouble(String columnName) {
        DataColumn column = prepareAddColumn(java.sql.Types.DOUBLE, columnName);
        appendColumn(column);
        return column;
    }
*/
    /**
     * ������� ������� ����  <code>java.lang.String</code>  c
     * �������� ������ � ���������� �� � ����� ��������� �������. 
     * 
     * ����� �� ��������� ���������������� � ������������ ��� �������� �
     * �������������. <p>
     * 
     * ���� ���������� ������� � ����� �� ������, ���������,
     * �� ������������� ���������� IllegalArgumentException.
     * 
     * @param columnName ��� ������� ��� <code>null</code>
     *
     * @return ����� ��������� �������
     * 
     * @throws IllegalArgumentException ���� �������� ��� ������� ��� �����
     *     ���� �� ������� ���������
     */
    //@Override
/*    public DataColumn addString(String columnName) {
        DataColumn column = prepareAddColumn(java.sql.Types.VARCHAR, columnName);
        appendColumn(column);
        return column;
    }
*/
    /**
     * ������� ����������� ������� ��������� ���� � �������� ������  �
     * ��������� �� � �������� ������� ��������� �������. 
     * 
     * ������� �������� �����������, ���� �� ����� 
     * {@link tdo.DataColumn#getKind()} ���������� �������� 
     * <code>DataColumn.CALC_KIND}</code>. 
     * 
     * ���� ���������� ������� � ����� �� ������, ��� �������� ������� ���������,
     * �� ������������� ���������� IllegalArgumentException.
     * 
     * @param position ������� �������.
     * @param type java-��� ������, ������������� ��������
     * @param columnName ��� ������� ��� <code>null</code>
     *
     * @return ����� ��������� �������
     * 
     * @throws IllegalArgumentException ���� �������� ��� ������� ��� �����
     *     ���� �� ������� ���������
     * @throws IndexOutOfBoundsException ��� ������� ������� <i>position</i>
     * ��������� ���� �� �������:
     * <ul>
     *    <li><i>position</i> ������ ������ 0</li>
     *    <li><i>position</i> ������ ������ �������� ������� ���������</li>
     * </ul>
     */
    @Override
    public DataColumn insertCalculated(int position, Class type, String columnName) {

        DataColumn column = prepareAddColumn(type, columnName);
        column.setKind(DataColumn.CALC_KIND);

        if (position < 0 || position > getCount()) {
            throw new IndexOutOfBoundsException("Insert position out of bounds ( insert(" + position + ") )");
        }
        if (column.getName() == null) {
            column.setName(this.produceColumnName().toUpperCase());
        } else if (find(column.getName()) >= 0) {
            throw new IllegalArgumentException("Dublicate columnName '" + column.getName().toUpperCase() + "'");
        }
        columns.add(position, column);
        columnMap.put(column.getName().toUpperCase(), column);
        //  updateIndexes();
        column.addPropertyChangeListener(propertyChangeHandler);

        fireDataColumns(new DataColumnCollectionEvent(this, column, DataColumnCollectionEvent.COLUMN_ADDED));

        return column;
    }

    /**
     * �������  ������� ��������� ���� � �������� ������  �
     * ��������� �� � �������� ������� ��������� �������. 
     * 
     * ���� ���������� ������� � ����� �� ������, ��� �������� ������� ���������,
     * �� ������������� ���������� IllegalArgumentException.
     * 
     * @param position ������� �������.
     * @param type java-��� ������, ������������� ��������
     * @param columnName ��� ������� ��� <code>null</code>
     *
     * @return ����� ��������� �������
     * 
     * @throws IllegalArgumentException ���� �������� ��� ������� ��� �����
     *     ���� �� ������� ���������
     * @throws IndexOutOfBoundsException ��� ������� ������� <i>position</i>
     * ��������� ���� �� �������:
     * <ul>
     *    <li><i>position</i> ������ ������ 0</li>
     *    <li><i>position</i> ������ ������ �������� ������� ���������</li>
     * </ul>
     */
    @Override
    public DataColumn insert(int position, Class type, String columnName) {
        DataColumn column = prepareAddColumn(type, columnName);
        insert(position, column);
        return column;
    }
   /**
     * ������������ ������� ��������� � �������� ������� ��������� �������. 
     * 
     * ���� ���������� ������� � ����� �� ������, ��� � �������, �������� 
     * ����������, �� ������������� ���������� 
     * <code>IllegalArgumentException</code>.
     * 
     * @param position ������� �������.
     * @param column ����������� �������
     * @throws IllegalArgumentException ���� �������� ��� ������� ��� �����
     *     ���� �� ������� ���������
     * @throws IndexOutOfBoundsException ��� ������� ������� <i>position</i>
     * ��������� ���� �� �������:
     * <ul>
     *    <li><i>position</i> ������ ������ 0</li>
     *    <li><i>position</i> ������ ������ �������� ������� ���������</li>
     * </ul>
     */
    public void insert(int position, DataColumn column) {
        if (position < 0 || position > getCount()) {
            throw new IndexOutOfBoundsException("Insert position out of bounds ( insert(" + position + ") )");
        }
        if (column == null) {
            throw new NullPointerException("'column' argument cannot be null. ( insert(" + position + ") )");
        }
        if (column.getName() == null) {
            column.setName(this.produceColumnName().toUpperCase());
        } else if (find(column.getName()) >= 0) {
            throw new IllegalArgumentException("Dublicate columnName '" + column.getName().toUpperCase() + "'");
        }

        columns.add(position, column);
        columnMap.put(column.getName().toUpperCase(), column);
        //    updateIndexes();
        column.addPropertyChangeListener(propertyChangeHandler);

        fireDataColumns(new DataColumnCollectionEvent(this, column, DataColumnCollectionEvent.COLUMN_ADDED));

    }

    public void renameColumn(DataColumn column, String newName) {
        if (column == null || newName == null) {
            throw new NullPointerException("'newName' argument cannot be null. ( renameColumn())");
        }
        String nm = column.getName().toUpperCase();
        column.setName(newName);
        columnMap.remove(nm);
        columnMap.put(column.getName().toUpperCase(), column);
    }

    protected void appendColumn(DataColumn column) {
        //    int oldIndex = column.getIndex();
        int oldIndex = indexOf(column);
        columns.add(column);

        columnMap.put(column.getName().toUpperCase(), column);
        //      updateIndexes();
        column.addPropertyChangeListener(propertyChangeHandler);

        fireDataColumns(new DataColumnCollectionEvent(this, column, DataColumnCollectionEvent.COLUMN_ADDED));
    }
    /**
     * ���������� ������� � ��������� �������� ������ ��������� � ������� �����
     * �������� ��������
     *
     * ������� ���������������� ���: ����������� ������� � �������� 
     * <code>columnIndex</code> �� ������� <i>�����</I> �������� � �������� 
     * <code>newIndex</code>.  �� ����� �������, ����
     * <i>columnIndex == newIndex</i>,  �� ������������ �� ����� ������ � �� 
     * ����������. ���� newIndex ������ columnIndex �� 1, �� ����� ������������ 
     * �� ����� ������. ������ ������, ����� 
     * <i>newIndex == ����� ������� ���������</i>. � ����� ������
     * ������� �������������� <i>��</i> ��������� ���������.
     * @param columnIndex �������� ������� �������.
     * @param newIndex newIndex ����� ������� �������. ������ ���� ������ ��� 
     *                  ����� 0 � ������ ��� ����� ������� ��������� �������.
     * @see #set
     */
    @Override
    public void move(int columnIndex, int newIndex) {

        if (newIndex < 0 || newIndex > this.getCount()) {
            return;
        }
        int newIndexSafe = newIndex;

        if (newIndex - columnIndex == 1) {
            return;
        } else if (columnIndex < newIndex) {
            newIndex--;
        }

        HashMap<String, String> emap = new HashMap<String, String>();
        String s;//My 06.03.2012 = null;
        for (DataColumn dc : this.columns) {
            if (dc.getKind() == CALC_KIND && dc.getExpression() != null) {
                emap.put(dc.getName().toUpperCase(), dc.getExpression());
                dc.setExpression(null);
            }
        }

        DataColumn column = columns.remove(columnIndex);
        columns.add(newIndex, column);
        // We must notify Table
        fireDataColumns(new DataColumnCollectionEvent(this, column, newIndex, DataColumnCollectionEvent.COLUMN_MOVED));
        
        for (DataColumn dc : this.columns) {
            if (dc.getKind() == CALC_KIND) {
                s = emap.get(dc.getName().toUpperCase());
                dc.setExpression(s);
            }
        }
        //My 06.03.2012emap = null;
    }

    /**
     * ������� ������� � �������� �������� �� ���������.
     * <b>����������:</b> �� ������� ��������� ���� ����� � ����� 
     * <code>foreach</code>, ��������� � �������� �������� �����, �, ��� �������
     * ����� ����������� �������� � ������ �������� ��������, � ���������
     * ������������� ��������� <code>DataColumn.cellIndex</code>.
     * @param columnIndex ������ ��������� �������
     * @throws IndexOutOfBoundsException ���� ������� ������� ������������� ���
     *    ������ ��� ����� ����� �������
     */
    @Override
    public void remove(int columnIndex) {
        if (columnIndex < 0 || columnIndex >= this.getCount()) {
            throw new IndexOutOfBoundsException(" remove(" + columnIndex +
                    ") out of bounds");
        }
        DataColumn column = this.get(columnIndex);

        column.removePropertyChangeListener(propertyChangeHandler);

        this.columns.remove(columnIndex);
        this.columnMap.remove(column.getName());

        int kind = column.getKind();
//        this.updateIndexes();
        fireDataColumns(new DataColumnCollectionEvent(this, column, columnIndex, DataColumnCollectionEvent.COLUMN_REMOVED));
    }
    /**
     * ������� �������� �������  �� ���������.
     * <b>����������:</b> �� ������� ��������� ���� ����� � ����� 
     * <code>foreach</code>, ��������� � �������� �������� �����, �, ��� �������
     * ����� ����������� �������� � ������ �������� ��������, � ���������
     * ������������� ��������� <code>DataColumn.cellIndex</code>.<p>
     * 
     * @param column ��������� �������
     * @throws NullPointerException ���� <code>column</code> ����� <code>null</code>
     * @throws IndexOutOfBoundsException ���� ��������� �� �������� ��������� 
     * �������. �.�. �����, � �������� �����, ���������� ���������� ������ 
     * {@link #remove(int)}
     * @see #remove(java.lang.String) 
     * @see #remove(int) 
     */
    @Override
    public int remove(DataColumn column) {
        if (column == null )
            throw new NullPointerException("'column' argument cannot be null");
        return remove(column.getName());
    }

    /**
     * ������� ������� � �������� ������ �� ���������.
     * <b>����������:</b> �� ������� ��������� ���� ����� � ����� 
     * <code>foreach</code>, ��������� � �������� �������� �����, �, ��� �������
     * ����� ����������� �������� � ������ �������� ��������, � ���������
     * ������������� ��������� <code>DataColumn.cellIndex</code>.
     * @param columnName ��� ��������� �������
     * @return ������ ��������� �������
     * @throws IndexOutOfBoundsException ���� ��������� �� �������� ��������� 
     * �������. �.�. �����, � �������� �����, ���������� ���������� ������ 
     * {@link #remove(int)}
     * @see #remove(tdo.DataColumn) 
     * @see #remove(int) 
     */
    @Override
    public int remove(String columnName) {
        int columnIndex = this.find(columnName);
        remove(columnIndex);
        return columnIndex;
    }

    /**
     * ��������� ����� � ��������� ������� ���� <code>DataColumn</code> � 
     * �������� ������.<p>
     * ����� �������������� �� ����� �������. ������ ������������, �.�.
     * �� ����������� ������ � "�������/������" ��������.<p>
     *
     * @param columnName ��������� �������� ����� �������, ��� ���������� �
     *   ������ <code>DataColumn</code> ��������� <code>name</code>.
     * @return ��� <code>int</code> - ���� ������� � �������� ������
     *         �������, �� ������������ �� ������ � ���������
     *         ���� ����� �� ��������� �������, �� ������������ �������� -1
     *         (����� 1).
     */
    @Override
    public int find(String columnName) {
        int result = 0;
        boolean found = false;
        DataColumn dc;//My 06.03.2012 = null;

        for (int i = 0; i < columns.size(); i++) {
            dc = columns.get(i);
            if (dc.getName().equalsIgnoreCase(columnName)) {
                return i;
            }
            ++result;
        }//for

        return -1;
    }

    /**
     * ���������� ������ �������� ������� � ���������.
     * @param column �������, ������ ������� ������������
     * @return ������ ��������� ������� ��� -1, ���� ����� �� ������
     */
    @Override
    public int indexOf(DataColumn column) {
        return this.columns.indexOf(column);
    }

    /**
     * ������ ���������� ��� ����� ���� �������, ������������ � ���������.
     * ��������������� ����, ����� ������� ����� ��������
     * <code>COL_</code>. ��������������, ��� �� ��������� �������
     * ���������, ��������� �� ���������� ���� - ��������. ������������ �����,
     * ������� ������������ �������� �� ���� ����������. � ���� ����������� 1 �
     * ���������� ��������� ������������ ��� �������� ������ �����.
     *
     * @return ������ ������ - ����������� ��� ����.
     */
    protected String produceColumnName() {
        String template = "COL_";
        String s;
        int p = -1;
        DataColumn dc;//My 06.03.2012 = null;
        for (int i = 0; i < columns.size(); i++) {
            dc = columns.get(i);
            s = dc.getName().toUpperCase();
            if (s.startsWith(template)) {
                p = Math.max(Integer.parseInt(s.substring(template.length())), p);
            }
        }//for

        return template + String.valueOf(++p);
    }

    /**
     * ������� ��� �������� ���������. <p>
     */
    @Override
    public void clear() {
        columns.clear();
        columnMap.clear();
    }

    /**
     * �������������, �������� �� ��������� ������� ���� �� ���� �������.<p>
     * @return ��� <code>boolean</code> <i>true</i> - ���� ��������� �� ��������
     *    �� ������ ��������. <i>false</i> - � ��������� ������.
     */
    @Override
    public boolean isEmpty() {
        return columns.isEmpty();
    }

    /**
     * ���������� ��� �������� ������ ���������, � ����� ���������, 
     * �������� ����������. <p>
     * ���������, �������� ���������� �� ��������� ���� ������������ � ���
     * ��������� �������� ���������. ��� ������� ����������� ��������
     * ������������ ����� ������ �� ��� �� ����� ������� �������� ���������, �.�.
     * ����� ������ �� ���������.
     * @param columns  �������������� ���������.
     */
    @Override
    public void copyTo(DataColumnCollection columns) {

        DataColumn dc;//My 06.03.2012 = null;
        for (int i = 0; i < this.columns.size(); i++) {
            dc = this.columns.get(i);
            columns.add(dc);
        }//for

    }
/*
    public Map<String, DataColumn> getColumnMap() {
        return this.columnMap;
    }
*/
    /**
     * ���������� �������, ���� ��� ������ � ���������, �� ��������� ������� 
     * �������.
     * @param columnIndex ������ �������.
     * @return ������� �� ��������� �������. 
     * @throws IndexOutOfBoundsException - ���� ������ ��� ������� ��������� �������
     *     (index < 0 || index >= getCount())
     */
    @Override
    public DataColumn get(int columnIndex) {
        return columns.get(columnIndex);
    }
    /**
     * ���������� �������, ���� ��� ������ � ���������, �� ��������� �����. 
     * @param columnName ��� �������.
     * @return ������� �� ��������� ����� ��� <code>null</code>, ���� ����� 
     *    ������� �� �������. 
     */ 
    @Override
    public DataColumn get(String columnName) {
        return columnMap.get(columnName.toUpperCase());
    }

    /**
     * ���������� ������� ������ ��������� ����� �������, ��� �� ����� ������
     * ���������� ������ ���������.
     * <ul>
     *   <li>���� �������� ������� ���������� � ��������� � �� ������ ��������� � 
     *       ����� ��������, �� ���������� ������ �����������.
     *   </li>
     *   <li>���� ��������� ������� <code><pre>
     *          index < 0 || index > getCount()</pre></code>
     *       �� ������������� ����������.
     *   </li>
     *   <li>���� ������� � ��������� �� ����������, �� ��� �����������
     *       � �������� �������.
     *   </li>
     * </ul>
     * <p>
     * ������� �������� �� �������� �  ���������� ���������� ������� ������ 
     * � ������ {@link #move}
     * @param index ����� ������ �������
     * @param column ������������ �������
     * @throws IndexOutOfBoundsException ���� ��������� 
     * <code><pre> index < 0 || index > getCount()</pre></code>,
     */
    @Override
    public void set(int index, DataColumn column) {

        int oldIndex = indexOf(column);
        if (oldIndex == index) {
            return;
        }
        if (index < 0 || index > getCount()) {
            throw new IndexOutOfBoundsException("Invalid constraint 'new index < 0 or  > column count'");
        }

        if (oldIndex < 0) {
            this.insert(index, column);
        } else if (index > oldIndex) {
            move(oldIndex, index + 1);
        } else {
            move(oldIndex, index);
        }

    }

    /**
     * ���������� ���������� ��������� � ��������� �������.<p>
     *
     * @return ���������� ������� � ���������.
     */
    @Override
    public int getCount() {
        return columns.size();
    }

    /**
     * ��������� ���������� ������� �������� � ��������� � ������ ������������
     * ���� <code>tdo.DataColumnCollectionListener</code>.
     * ������� <code>tdo.DataColumnCollectionEvent</code> ������������ � 
     * �������� ����������, �������, �������� � ����������� �������.
     * 
     * @param l ����������� ����������
     * @see tdo.event.DataColumnCollectionListener
     * @see tdo.event.DataColumnCollectionEvent
     * @see #removeDataColumnColectionListener
     * @see #fireDataColumns
     */
    @Override
    public void addDataColumnCollectionListener(DataColumnCollectionListener l) {
        dataColumnsListenerList.add(l);
    }
    /**
     * ������� ���������� ������� �������� � ��������� �� ������ ������������
     * ���� <code>tdo.DataColumnCollectionListener</code>.
     * 
     * @param l ��������� ����������
     * @see tdo.event.DataColumnCollectionListener
     * @see tdo.event.DataColumnCollectionEvent
     * @see #addDataColumnColectionListener
     * @see #fireDataColumns
     */
    @Override
    public void removeDataColumnColectionListener(DataColumnCollectionListener l) {
        dataColumnsListenerList.remove(l);
    }

    /**
     * ��������� ��� ������������������ ����������� ������� 
     * <code>tdo.DataColumnCollectionEvent</code>.
     * ������� <code>tdo.DataColumnCollectionEvent</code> ������������ � 
     * �������� ����������, �������, �������� � ����������� �������.
     * 
     * @param e ��������� �������
     * @see tdo.event.DataColumnCollectionListener
     * @see tdo.event.DataColumnCollectionEvent
     * @see #removeDataColumnColectionListener
     */
    protected void fireDataColumns(DataColumnCollectionEvent e) {
        if (dataColumnsListenerList.isEmpty()) {
            return;
        }
        for (DataColumnCollectionListener l : new ArrayList<DataColumnCollectionListener>(this.dataColumnsListenerList)) {
            l.columnsChanged(e);
        }
    }
    /**
     * ���������� ������� <code>PropertyChangeEvent</code>.
     * ����� ������� ����������� � ���������, �� ����������� ����������
     * ������� ��������� ������� �������.<p> 
     * ����� ������� ��������� �� ���������, ����������
     * ������� ��������� ������� ������� ���������.
     */
    public class PropertyChangeHandler implements PropertyChangeListener, Serializable {

        public PropertyChangeHandler() {
        }

        @Override
        public void propertyChange(PropertyChangeEvent e) {
            if (e.getPropertyName().equals("columnName")) {
                if (e.getOldValue() != null) {
                    columnMap.remove(((String) e.getOldValue()).toUpperCase());
                }
                if (e.getNewValue() != null) {
                    columnMap.put(((String) e.getNewValue()).toUpperCase(), (DataColumn) e.getSource());
                }
            }
        }
    }//class PropertyChangeHandler
/*
    protected void setColumnMap(Map<String, DataColumn> columnMap) {
        this.columnMap = columnMap;
    }
*/
    protected void initColumns() {
        this.columns = new ArrayList<DataColumn>(10);
        this.columnMap = new HashMap<String, DataColumn>();
    }
    /**
     * ������� � ���������� ����� ��������� ��������� �������, ���������
     * ����������� ��� ���������� {@link tdo.DefaultDataColumnCollection} ��� 
     * ��������������� {@link tdo.DataColumnCollection}.
     * 
     * @return ����� ��������� ���������
     */
    @Override
    public DataColumnCollection create() {
        return new DefaultDataColumnCollection();
    }

    /**
     * ��� ����� ������������. ����� ��������� ����� ���.
     * @return
     */
    @Override
    public boolean isValid() {
        boolean r = true;
        /*        for (int i = 0; i < columns.size(); i++) {
        DataColumn dc = columns.get(i);
        if (indexOf(dc) != i) {
        r = false;
        break;
        }
        }
        
        int n = 0;
        for (int i = 0; i < this.getCount(); i++) {
        DataColumn dc = get(i);
        if (dc.getKind() == DataColumn.DATA_KIND) {
        
        if (dc.getCellIndex() != n) {
        return false;
        }
        n++;
        } else {
        if (dc.getCellIndex() != -1) {
        return false;
        }
        }
        }
         */
        return r;
    }
}//class DefaultDataColumnCollection
