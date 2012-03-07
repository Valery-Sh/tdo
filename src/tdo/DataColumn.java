package tdo;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSetMetaData;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import tdo.util.Strings;

/**
 * The <code>DataColumn</code> object specifies the schema for a single column in a DataTable.<p>
 * The class is defined as an abstract class and serves as base class for all implementations
 * specific for a given type.<p> 
 * ���� �������� ������ ���������������� �������, ����������� DataColumn. 
 * ��� ������ ����������� ��� ���������� � �������������� ��� ��������� 
 * ������ � ��������, ��������������� �������� ����� � �������� ��� ������ <p>
 * <UL>
 *   <LI>{@link DataColumn.PDBBigIntColumn}</LI>
 *   <LI>{@link DataColumn.PDBBitColumn}</LI>
 *   <LI>{@link DataColumn.PDBDateColumn}</LI>
 *   <LI>{@link DataColumn.PDBDecimalColumn}</LI>
 *   <LI>{@link DataColumn.PDBDoubleColumn}</LI>
 *   <LI>{@link DataColumn.PDBIntegerColumn}</LI>
 *   <LI>{@link DataColumn.PDBJavaObjectColumn}</LI>
 *   <LI>{@link DataColumn.PDBRealColumn}</LI>
 *   <LI>{@link DataColumn.PDBSmallIntColumn}</LI>
 *   <LI>{@link DataColumn.PDBStringColumn}</LI>
 *   <LI>{@link DataColumn.PDBTimeColumn}</LI>
 *   <LI>{@link DataColumn.PDBTimestampColumn}</LI>
 *   <LI>{@link DataColumn.PDBTinyIntColumn}</LI>
 * </UL>
 * 
 * ����� �������� ��������� �������, ����������� �� ������� ������� � �����������
 * ������ <code>java.sql.ResultSetMetaData</code> �, ��� �������, �� ������������
 * �����������, �� ����� ���� ���������, ��������, ��� �������� ������ ��� ������.<p>
 * ������ ������������ �� ����� ������������ ������������ ������������� �������.
 * ������ ��� ������� {@link tdo.DataColumnCollection} ��������������� ����� 
 * ������� ������ <code>addXXX</code>.
 * 
 * 
 * @version 1.0
 */
public abstract class DataColumn implements Cloneable, tdo.expr.Comparable, Serializable {

    /**
     * ������������ ��� ��������� �/��� ������� � �������� �������� 
     * <b>{@link #kind}</b>. ���������, ��� ������� ��������� �������� ������.
     * @see #getKind
     * @see #setKind
     */
    public static final int DATA_KIND = 10;
    /**
     * ������������ ��� ��������� �/��� ������� � �������� �������� 
     * <b>{@link #kind}</b>. ���������, ��� ������� ��������� ����������� ������.
     * @see #getKind
     * @see #setKind
     */
    public static final int CALC_KIND = 11;
    /**
     * ������������ ��� ��������� �/��� ������� � �������� �������� 
     * <b>{@link #kind}</b>. ���������, ��� ������� ��������� ����������� 
     * lookup-������. � ������ ������ �� ������������.
     * @see #getKind
     * @see #setKind
     */
    public static final int LOOKUP_KIND = 12;
    /**
     * ������������ ��� ���������� ������� �� ���� ��������.
     * @see tdo.DataColumnCollection#getCount(int)
     * @see tdo.DataColumnCollection#setCount(int,int)
     */
    public static final int ALL_KIND = 14;
    /**
     * ������������ ��� ���������� ������� ������ � ����������� � lookup-��������.
     * @see tdo.DataColumnCollection#getCount(int)
     * @see tdo.DataColumnCollection#setCount(int,int)
     */
    public static final int CALC_AND_LOOKUP = 15;
    private int kind = DATA_KIND;
    private String name = null;
    private int sqlType = java.sql.Types.NULL;
    private Class type;
    private boolean readOnly = false;
    private int size = 10;
    private int precision = 10;
    private int scale = 2;
    private int nullable = ResultSetMetaData.columnNullable;
    private boolean autoIncrement = false;
    private boolean caseSensitive;
    private boolean currency = false;
    private boolean signed;
    private boolean searchable;
    private String label;
    private String tableName; // as in sql db table

    private String schemaName; // as in sql db table

    private int cellIndex;
    /**
     * ������ �������� �������� POJO, ����� � �������� ���� ������� ���������
     * java bean.
     */
    private String propertyName = null;
    private String fieldName = null;
    private String expression;
    private ColumnExpressionContext columnExpression;
    /**
     * ������  � ��������� {@link DataColumnCollection}.<p>
     * ���� ������� ��������� � ���������, �� ��������� ������ ����������
     * ��������� �������� ������� �������� ��� �������, ���������� ���
     * �������� ������� �� ���������.<p>
     * �������� �� ��������� ����� -1 � ��������������� ��� ���������� �������
     * � DataColumnCollection.
     * @see #getIndex
     * @see #setIndex
     * @see tdo.DataColumnCollection
     */
    private int index = -1;
    private PropertyChangeSupport changeSupport;
    private Object defaultValue;

    /**
     * getter-����� �������� <code>name</code>.
     * @return ��������� �������� <b>�����</b> ������� - �������� <code>name</code>.
     * @see #setName
     */
    public String getName() {
        return this.name;
    }

    /**
     * ������������� ��������� �������� <b>�����</b> ������� - �������� <code>name</code>.
     * ����� ���������� �������� ��������� ������������� � ������� �������.
     * ���������� ������� <code>PropertyChangeEvent</code>. <p>
     * �������� �������� ������ �������� ��������� �������� �������� 
     * <code>label</code>. �������� ��������� <code>name</code> ����� 
     * ������������ ����� ������ ��������� �� ���� ������, ����������� �������.
     * ������ ����� �� ������� - �������� �������� <code>name</code>. 
     * ������ ����� ����� ������� - �������� �������� <code>label</code>. <p>
     * ���� �������� ��������� � ���������������� ������� �� ���� ������, �� �������� 
     * <code>label</code> ��������������� ���������� �� ������ �������� ��������.<p>
     * ���� �������� ��������� �������� ������ ��� �������, �.�. ������� �� 
     * ����� �����, �� ����������� ������� �������� �������� <code>label</code>.
     * ���� ����� <code>null</code>, �� �������� �������� <code>label</code> 
     * ��������������� ������ �������� ��������� (�� ��������) <code>name</code>.
     * 
     * @param name �������� ����� �������. ������ ���� ������� �� <code>null</code>.
     * @see #getName
     * @see #firePropertyChange
     * @see #getLabel
     * @see #setLabel
     * @throws IllegalArgumentException , ����� �������� ��������� ����� 
     * <code>null</code> ��� ����� ������ ����� 0 ��� ������ �������� � ��������
     * ��������� �������� �������.
     */
    public void setName(String name) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("Column name cannot be null and length must be  > 0 ");
        }
        String pname = name.trim();
        if (pname.startsWith(",")) {
            throw new IllegalArgumentException("First letter cannot be comma");
        }
        String oldValue = this.name;
        String[] nmlb = Strings.split(pname);
        this.name = nmlb[0].toUpperCase();
        if (nmlb.length > 1) {
            this.label = nmlb[1];
        }
        this.firePropertyChange("name", oldValue, this.name);
        if (this.label == null) {
            this.setLabel(name);
        }

    }

    /**
     * ���������� ��������� �������� ���������, ������������� ��� �����������
     * �������. <p> 
     * @see DataColumnCollection#addCalculated(java.lang.Class, java.lang.String) 
     * @see DataColumnCollection#addCalculated(int, java.lang.String) ) 
     * @see DataColumnCollection#addCalculated(int, java.lang.String, int, int)  
     * 
     * @return  ��������� �������� ���������, ������������� ��� �����������
     * �������. 
     */
    public String getExpression() {
        return this.expression;
    }

    /**
     * ������������� �������� ���������, ������������� ��� �����������
     * �������. <p> 
     * <b>����������.</b> ������ ��������� �������� �������� 
     * <code>expression</code>, ����� ������� ��������� ������ 
     * {@link tdo.ColumnExpressionContext} � ������������ �������� �������� 
     * {@link #columnExpression}. 
     * @param expr ����� �������� �������� <code>expression</code>. ���� ����� 
     * <code>null</code> ��� ����� ������ ��� ��������� � �������� �������� �����
     * 0, �� <code>expression</code> � <code>columnExpressiont</code> 
     * ��������������� � <code>null</code>.
     * 
     * @see #getColumnExpression
     * @see DataColumnCollection#addCalculated(java.lang.Class, java.lang.String) 
     * @see DataColumnCollection#addCalculated(int, java.lang.String) ) 
     * @see DataColumnCollection#addCalculated(int, java.lang.String, int, int)  
     * @see tdo.ColumnExpressionContext
     */
    public void setExpression(String expr) {
        this.expression = expr;
        if (expr != null && expr.trim().length() > 0) {
            columnExpression = new ColumnExpressionContext(expr);
        } else {
            this.expression = null;
            columnExpression = null;
        }
    }

    /**
     * ���������� �������� ��������� ��������� �������.<p>
     * @return �������� �������� columnExpression
     * @see #setExpression
     */
    public ColumnExpressionContext getColumnExpression() {
        return this.columnExpression;
    }

    // Returns the type of the values for this DataColumn as a Java Class.
    /**
     * ���������� ��� ��� ��������, ������������ �������� ��� Java Class.
     * 
     * @return ��� ��� ��������, ������������ �������� ��� Java Class.
     */
    public Class getType() {
        return type;
    }

    /**
     * ������������� ����� �������� �������� <code>type</code> - ��� ��������
     * ��� ������, ����������� �������� ��� Jacva Class. <p> 
     * ����������� ������� ������ = <code>protected</code>. ������������, ���� 
     * ������ �� �� ����������� ������ ������, ������������ 
     * <code>DataColumn</code>, �� ����� ������������ �����. �������� ��������
     * ������������ ������� - ����������� <code>DataColumn</code>.
     * 
     * @param type ����� �������� ���� ��������, ������������ ��������
     */
    protected void setType(Class type) {
        this.type = type;
    }

    /**
     * ���������� sql-��� ��������, ����������� ��������, ��� ����������
     * ����������� ������ <code>java.sql.Types</code>.<p>
     * �������� �������� <code>sqlType</code> ��������������� ����� �� ���������
     * ��������:
     * <ol>
     *   <li>����������� ��������� <code>new</code> � ������������ ��� ����������
     *       ��� ������ �� ���������������� �������-����������� 
     *       <code>DataColumn</code>:
     *       <UL>
     *          <LI>{@link DataColumn.PDBBigIntColumn}. �������� �������� 
     *              sqlType ��������������� ������ 
     *              <code>java.sql.Types.BIGINT</code>.
     *              �������� �������� <code>type</code> ��������������� ������ 
     *              java.lang.Long</code>.
     *          </LI>
     *          <LI>{@link DataColumn.PDBBitColumn}. �������� �������� 
     *              sqlType ��������������� ������ 
     *              <code>java.sql.Types.BIT</code>.
     *              �������� �������� <code>type</code> ��������������� ������ 
     *              java.lang.Boolean</code>.
     *          </LI>
     *          <LI>{@link DataColumn.PDBDateColumn}. �������� �������� 
     *              sqlType ��������������� ������ 
     *              <code>java.sql.Types.DATE</code>.
     *              �������� �������� <code>type</code> ��������������� ������ 
     *              java.util.Date</code>.
     *          </LI>
     *          <LI>{@link DataColumn.PDBDecimalColumn}. �������� �������� 
     *              sqlType ��������������� ������ 
     *              <code>java.sql.Types.DECIMAL</code>.
     *              �������� �������� <code>type</code> ��������������� ������ 
     *              java.math.BigDecimal</code>.
     *          </LI>
     *          <LI>{@link DataColumn.PDBDoubleColumn}. �������� �������� 
     *              sqlType ��������������� ������ 
     *              <code>java.sql.Types.DOUBLE</code>.
     *              �������� �������� <code>type</code> ��������������� ������ 
     *              java.lang.Double</code>.
     *          </LI>
     *          <LI>{@link DataColumn.PDBIntegerColumn}. �������� �������� 
     *              sqlType ��������������� ������ 
     *              <code>java.sql.Types.INTEGER</code>.
     *              �������� �������� <code>type</code> ��������������� ������ 
     *              java.lang.Integer</code>.
     *          </LI>
     *          <LI>{@link DataColumn.PDBJavaObjectColumn}. �������� �������� 
     *              sqlType ��������������� ������ 
     *              <code>java.sql.Types.OTHER</code>.
     *              �������� �������� <code>type</code> ��������������� ������ 
     *              java.lang.Object</code>.
     *          </LI>
     *          <LI>{@link DataColumn.PDBRealColumn}. �������� �������� 
     *              sqlType ��������������� ������ 
     *              <code>java.sql.Types.REAL</code>.
     *              �������� �������� <code>type</code> ��������������� ������ 
     *              java.lang.Float</code>.
     *          </LI>
     *          <LI>{@link DataColumn.PDBSmallIntColumn}. �������� �������� 
     *              sqlType ��������������� ������ 
     *              <code>java.sql.Types.SMALLINT</code>.
     *              �������� �������� <code>type</code> ��������������� ������ 
     *              java.lang.Short</code>.
     *          </LI>
     *          <LI>{@link DataColumn.PDBStringColumn}. �������� �������� 
     *              sqlType ��������������� ������ 
     *              <code>java.sql.Types.CHAR</code>.
     *              �������� �������� <code>type</code> ��������������� ������ 
     *              java.lang.String</code>.
     *          </LI>
     *          <LI>{@link DataColumn.PDBTimeColumn}. �������� �������� 
     *              sqlType ��������������� ������ 
     *              <code>java.sql.Types.TIME</code>.
     *              �������� �������� <code>type</code> ��������������� ������ 
     *              java.sql.Time</code>.
     *          </LI>
     *          <LI>{@link DataColumn.PDBTimestampColumn}. �������� �������� 
     *              sqlType ��������������� ������ 
     *              <code>java.sql.Types.TIMESTAMP</code>.
     *              �������� �������� <code>type</code> ��������������� ������ 
     *              java.lang.Timestamp</code>.
     *          </LI>
     *          <LI>{@link DataColumn.PDBTinyIntColumn}. �������� �������� 
     *              sqlType ��������������� ������ 
     *              <code>java.sql.Types.TINYINT</code>.
     *              �������� �������� <code>type</code> ��������������� ������ 
     *              java.lang.Byte</code>.
     *          </LI>
    
     *       </UL>
     *   </li>  
     *   <li>�������������� ������ �� ������������� ������� <b>addXXX</b> ������ 
     *       {@link tdo.DataColumnCollection}, ������ ���������� �������� 
     *       �������� <code>sqlType</code>:
     *          <ul>
     *              <li>{@link tdo.DataColumnCollection#add(int)}</li>
     *              <li>{@link tdo.DataColumnCollection#add(int,String,int,int)};
     *              </li>
     *              <li>{@link tdo.DataColumnCollection#addCalculated(int,String)};
     *              </li>
     *              <li>{@link tdo.DataColumnCollection#addCalculated(int,String,int,int)};
     *              </li>
     *          </ul> 
     *          � ���� ������, ��������� ��������, ��������������� 
     *          <code>sqlType</code> ���������� ���������� �����-��������� 
     *          <code>DataColumn</code> � ��������� ��� ���������, � ������:
     *          <ul>
     *              <li>��� sqlType ������� <code>java.sql.Types.INTEGER</code>
     *                   ������������� ����� {@link PDBIntegerColumn}
     *              </li>               
     *              <li>��� sqlType ������� <code>java.sql.Types.BIGINT</code>
     *                   ������������� ����� {@link PDBBigIntColumn}
     *              </li>               
     *              <li>��� sqlType ������� <code>java.sql.Types.SMALLINT</code>
     *                   ������������� ����� {@link PDBSmallIntColumn}
     *              </li>               
     *              <li>��� sqlType ������� <code>java.sql.Types.TINYINT</code>
     *                   ������������� ����� {@link PDBTinyIntColumn}
     *              </li>               
     *              <li>��� sqlType ������� <code>java.sql.Types.REAL</code>
     *                   ������������� ����� {@link PDBRealColumn}
     *              </li>               
     *              <li>��� sqlType ������� <code>java.sql.Types.FLOAT</code>
     *                   ������������� ����� {@link PDBDoubleColumn}
     *              </li>               
     *              <li>��� sqlType ������� <code>java.sql.Types.DOUBLE</code>
     *                   ������������� ����� {@link PDBDoubleColumn}
     *              </li>               
     *              <li>��� sqlType ������� <code>java.sql.Types.VARCHAR</code>
     *                  ������������� ����� {@link PDBStringColumn} � 
     *                  ��������������� ��� ��� ���������� �������� ��������
     *                  <code>sqlType</code> ������ <code>java.sql.Types.CHAR</code>
     *              </li>               
     *              <li>��� sqlType ������� <code>java.sql.Types.CHAR</code>
     *                  ������������� ����� {@link PDBStringColumn} � 
     *                  ��������������� ��� ��� ���������� �������� ��������
     *                  <code>sqlType</code> ������ <code>java.sql.Types.CHAR</code>
     *              </li>               
     *              <li>��� sqlType ������� <code>java.sql.Types.LONGVARCHAR</code>
     *                  ������������� ����� {@link PDBStringColumn} � 
     *                  ��������������� ��� ��� ���������� �������� ��������
     *                  <code>sqlType</code> ������ <code>java.sql.Types.LONGVARCHAR</code>
     *              </li>               
     *              <li>��� sqlType ������� <code>java.sql.Types.NUMERIC</code>
     *                  ������������� ����� {@link PDBDecimalColumn} � 
     *                  ��������������� ��� ��� ���������� �������� ��������
     *                  <code>sqlType</code> ������ <code>java.sql.Types.NUMERIC</code>
     *              </li>               
     *              <li>��� sqlType ������� <code>java.sql.Types.DECIMAL</code>
     *                  ������������� ����� {@link PDBDecimalColumn} � 
     *                  ��������������� ��� ��� ���������� �������� ��������
     *                  <code>sqlType</code> ������ <code>java.sql.Types.DECIMAL</code>
     *              </li>               
     *              <li>��� sqlType ������� <code>java.sql.Types.TIMESTAMPL</code>
     *                  ������������� ����� {@link PDBTimestampColumn} � 
     *                  ��������������� ��� ��� ���������� �������� ��������
     *                  <code>sqlType</code> ������ <code>java.sql.Types.TIMESTAMPO</code>
     *              </li>               
     *              <li>��� sqlType ������� <code>java.sql.Types.TIME</code>
     *                  ������������� ����� {@link PDBTimeColumn} � 
     *                  ��������������� ��� ��� ���������� �������� ��������
     *                  <code>sqlType</code> ������ <code>java.sql.Types.Time</code>
     *              </li>               
     *              <li>��� sqlType ������� <code>java.sql.Types.DATE</code>
     *                  ������������� ����� {@link PDBDateColumn} � 
     *                  ��������������� ��� ��� ���������� �������� ��������
     *                  <code>sqlType</code> ������ <code>java.sql.Types.DATE</code>
     *              </li>               
     *              <li>��� sqlType ������� <code>java.sql.Types.BIT</code>
     *                  ������������� ����� {@link PDBBitColumn} � 
     *                  ��������������� ��� ��� ���������� �������� ��������
     *                  <code>sqlType</code> ������ <code>java.sql.Types.BIT</code>
     *              </li>               
     *              <li>��� sqlType ������� <code>java.sql.Types.OTHER</code>
     *                  ������������� ����� {@link PDBJavaObjectColumn} � 
     *                  ��������������� ��� ��� ���������� �������� ��������
     *                  <code>sqlType</code> ������ <code>java.sql.Types.OTHER</code>
     *              </li>               
     *          </ul>
     *   </li>
     *   <li>�������������� ������ {@link tdo.impl.AbstractTable#populate(java.sql.ResultSet)}.
     *       � ���� ������ <code>sqlType</code> ������ ������� ����������� �� ����������
     *       ����� ������ <code>ResultSetMetaData.getColumnType</code>. 
     *       ���������� �������� ����������� ���������� ����������� ������ 2.
     *   </li>
    
     * </ol>
     * @return ����� �������� ��� ���������� �������� <code>java.sql.Types</code>. 
     */
    public int getSqlType() {
        return this.sqlType;
    }

    /**
     * ������������� �������� �������� <code>sqlType</code> ������ �������� ���������.
     * 
     * @param sqlType ����� �������� sql-����, ��� ���������� ������� 
     *        <code>java.sql.Type</code>.
     * @see #getSqlType() 
     */
    protected void setSqlType(int sqlType) {
        this.sqlType = sqlType;
    }

    /**
     * ���������� ��������, ������������� ����� �� �������� ������, ����������� ��������.
     * @return <code>false</code>, ���� ������, ������� ��������� ������� �����������
     *  �������������. <code>true</code> - � ��������� ������.
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * ������������� �������� �����, ������������� ����������� ��� ��� 
     * �������������� ������, ��������������� �������.
     * �� ��������� ��� �������, �������� <code> kind</code> ������� ����� 
     * {@link DataColumn#CALC_KIND} �������� �������� ������ ���� <code>true</code>.
     * ����� �� ���������� ������� �������� ��� ����� �������.
     * ��� �������, �������� <code>kind</code> ������� ����� 
     * {@link DataColumn#DATA_KIND} �������� �������� �� ��������� �����������
     * � <code>false</code>, �� ����� ���� �������� �� <code>true</code>.
     * 
     * @param readOnly ������ <code>true</code> ��������� ��������������. 
     *      �������� ������ <code>false</code> ��������� ��������������.
     * @see #isReadOnly
     * @see #setKind
     */
    public void setReadOnly(boolean readOnly) {
        if (this.getKind() == CALC_KIND) {
            return;
        }
        this.readOnly = readOnly;
    }

    /**
     * ���������� ������ ������, ��������������� �������. 
     * <p>�������� ����� �������� ������ ����� �������� �������� 
     * <code>precision</code> � ������ ���������� ��� ��������� ����������.<p>
     * ����� ������� ��������� �� ������ <code>ResultSetMetaData</code>, �� 
     * �������� ��� ��������������� ������ 
     * <code>ResultSetMeteData.getPrecision</code>. <p>
     * ����� ������� ��������� ������������� ������ �� ����������������
     * �������-����������� <code>DataColumn</code>, �� �������� ������� 
     * �� ������ ��������� �������:
     * <UL>
     *   <LI>{@link DataColumn.PDBBigIntColumn}     : (20,0)</LI>
     *   <LI>{@link DataColumn.PDBBitColumn}        : (?)</LI>
     *   <LI>{@link DataColumn.PDBDateColumn}       : (?)</LI>
     *   <LI>{@link DataColumn.PDBDecimalColumn}    : (15,2)</LI>
     *   <LI>{@link DataColumn.PDBDoubleColumn}     : (15,-1)</LI>
     *   <LI>{@link DataColumn.PDBIntegerColumn}    : (10,0)</LI>
     *   <LI>{@link DataColumn.PDBJavaObjectColumn} : (?) </LI>
     *   <LI>{@link DataColumn.PDBRealColumn}       : (15,2)</LI>
     *   <LI>{@link DataColumn.PDBSmallIntColumn}   : (6,0)</LI>
     *   <LI>{@link DataColumn.PDBStringColumn}     : (?)</LI>
     *   <LI>{@link DataColumn.PDBTimeColumn}       : (?)</LI>
     *   <LI>{@link DataColumn.PDBTimestampColumn}  : (?)</LI>
     *   <LI>{@link DataColumn.PDBTinyIntColumn}    : (4,0)</LI>
     * </UL>
     * <b>����������:</b> ���, ��� ������� (?) �� ����� ���� ����� ������������
     * (10,2). <p>
     * ����� ��������, ��� �������� �������� <code>size</code> (� ����� ��������
     * <code>scale</code> � <code>precision</code> ) ����� � ������ 
     * TDO Framework �� ������������.  ��������� ����������� � ���������� ����� 
     * ���� DDL-�������� ��� ����� ������.
     * 
     * Get the column's specified column size. For numeric data, 
     * this is the maximum precision. For character data, this is the length 
     * in characters. For datetime datatypes, this is the length in characters
     * of the String representation (assuming the maximum allowed precision 
     * of the fractional seconds component). For binary data, this is the length
     * in bytes. For the ROWID datatype, this is the length in bytes. 
     * 0 is returned for data types where the column size is not applicable.
     * 
     * @return ������ ������, ��������������� �������. 
     * @see #getPrecision
     * @see #setPrecision
     * @see #getScale
     * @see #setScale
     */
    public int getSize() {
        return this.size;
    }

    /**
     * ������������� ������ ������, ��������������� �������. <p>
     * �������� �������� <code>precision</code> ��������������� ������ ��������
     * <code>size</code>.
     * 
     * @param size ����� ��������
     * @see #getSize
     * @see #getPrecision
     * @see #setPrecision
     * @see #getScale
     * @see #setScale
     */
    public void setSize(int size) {
        int old = this.size;
        this.precision = size;
        this.size = size;
    }

    /**
     * ���������� ������ ������, ��������������� �������. 
     * <p>�������� ����� �������� ������ ����� �������� �������� 
     * <code>size</code> � ������ ���������� ��� ��������� ����������.<p>
     * @return ������ ������, ��������������� �������. 
     * @see #getSize
     * @see #setPrecision
     * @see #getScale
     * @see #setScale
     */
    public int getPrecision() {
        return this.precision;
    }

    /**
     * ������������� ������ ������, ��������������� �������. <p>
     * �������� �������� <code>size</code> ��������������� ������ ��������
     * <code>precision</code>.
     * 
     * @param precision ����� �������� ������� ������, �������������� ��������.
     * @see #getSize
     * @see #setSize
     * @see #getPrecision
     * @see #getScale
     * @see #setScale
     */
    public void setPrecision(int precision) {
        int old = this.precision;
        this.size = precision;
        this.precision = precision;
    }

    /**
     * ���������� �������� <i>scale</i> ��� ������, �������������� ��������.<p>
     * ��������������, ��� �������� <code>scale</code> ������������ ��� ������ 
     * ���� <code>java.math.BigDecimal</code>.
     * 
     * @return <code>scale</code>
     * @see #setScale 
     * @see #getPrecision
     * @see #getSize
     * @see java.math.BigDecimal
     */
    public int getScale() {
        return this.scale;
    }

    /**
     * ������������� ����� �������� <i>scale</i> ��� ������, �������������� ��������.<p>
     * ��������������, ��� �������� <code>scale</code> ������������ ��� ������ 
     * ���� <code>java.math.BigDecimal</code>.
     * @param scale
     * @see #getScale 
     * @see #getPrecision
     * @see #getSize
     * @see java.math.BigDecimal
     */
    public void setScale(int scale) {
        int old = this.scale;
        this.scale = scale;
    }

    /**
     * ���������� ��������, �����������, �������� �� ������� �������������
     * ����������. <p>
     * �������� <code>autoIncrement</code> ������������� ������������ ��������
     * ������ <code>java.sql.ResultSetMeteData</code>. ������ �����������, �����
     * <code>DataColumn</code> ������������ � ����� 
     * <code>java.sql.ResutSet</code>. 
     * <p>� ������� TDO Framework ����� �� ������������. ��������� ���������� 
     * - ������������� SQL DDL �������� ������� ���� ������.
     * 
     * @return autoIncrement
     */
    public boolean isAutoIncrement() {
        return this.autoIncrement;
    }

    /**
     * ������������� ����� ��������, �����������, �������� �� ������� 
     * ������������� ����������. <p>
     * �������� <code>autoIncrement</code> ������������� ������������ ��������
     * ������ <code>java.sql.ResultSetMeteData</code>. ������ �����������, �����
     * <code>DataColumn</code> ������������ � ����� � 
     * <code>java.sql.ResutSet</code>. 
     * <p>� ������� TDO Framework ����� �� ������������. ��������� ���������� 
     * - ������������� SQL DDL �������� ������� ���� ������.
     * 
     * @param value ����� ��������. <code>true</code> ������� ��������������. 
     *          <code>false</code> - � ��������� ������
     */
    public void setAutoIncrement(boolean value) {
        this.autoIncrement = value;
    }

    /**
     * ���������� ��������, �����������, �������� �� ������ ����������� ��������
     * ��������������� � ��������<p>
     * �������� <code>caseSensitive</code> ������������� ������������ ��������
     * ������ <code>java.sql.ResultSetMeteData</code>. ������ �����������, �����
     * <code>DataColumn</code> ������������ � ����� � �������������� 
     * <code>java.sql.ResutSet</code>. 
     * <p>� ������� TDO Framework ����� �� ������������. ��������� ���������� 
     * - ������������� SQL DDL �������� ������� ���� ������.
     * 
     * @return caseSensitive
     */
    public boolean isCaseSensitive() {
        return this.caseSensitive;
    }

    /**
     * ������������� ����� ��������, �����������, �������� �� ������ �����������
     * �������� ��������������� � ��������<p>
     * �������� <code>caseSensitive</code> ������������� ������������ ��������
     * ������ <code>java.sql.ResultSetMeteData</code>. ������ �����������, �����
     * <code>DataColumn</code> ������������ � ����� � 
     * <code>java.sql.ResutSet</code>. 
     * <p>� ������� TDO Framework ����� �� ������������. ��������� ���������� 
     * - ������������� SQL DDL �������� ������� ���� ������.
     * 
     * @param  value . <code>true</code> - ������ ������������� � ��������. 
     * <code>false</code> - � ��������� ������.
     */
    public void setCaseSensitive(boolean value) {
        this.caseSensitive = value;
    }

    /**
     * ���������� ��������, �����������, ������������ �� ������ ����������� ��������
     * �������� ��������<p>
     * �������� <code>currency</code> ������������� ������������ ��������
     * ������ <code>java.sql.ResultSetMeteData</code>. ������ �����������, �����
     * <code>DataColumn</code> ������������ � ����� � 
     * <code>java.sql.ResutSet</code>. 
     * <p>� ������� TDO Framework ����� �� ������������. ��������� ���������� 
     * - ������������� SQL DDL �������� ������� ���� ������.
     * 
     * @return currency
     */
    public boolean isCurrency() {
        return this.currency;
    }

    /**
     * ������������� ����� ��������, �����������, ������������ �� ������ 
     * ����������� �������� �������� ��������<p>
     * �������� <code>currency</code> ������������� ������������ ��������
     * ������ <code>java.sql.ResultSetMeteData</code>. ������ �����������, �����
     * <code>DataColumn</code> ������������ � ����� � 
     * <code>java.sql.ResutSet</code>. 
     * <p>� ������� TDO Framework ����� �� ������������. ��������� ���������� 
     * - ������������� SQL DDL �������� ������� ���� ������.
     * 
     * @param value
     */
    public void setCurrency(boolean value) {
        this.currency = value;
    }

    /**
     * ���������� ��������, �����������, ������������ �� ������ ����������� ��������
     * �������� ������ �� ������<p>
     * �������� <code>signed</code> ������������� ������������ ��������
     * ������ <code>java.sql.ResultSetMeteData</code>. ������ �����������, �����
     * <code>DataColumn</code> ������������ � ����� � 
     * <code>java.sql.ResutSet</code>. 
     * <p>� ������� TDO Framework ����� �� ������������. ��������� ���������� 
     * - ������������� SQL DDL �������� ������� ���� ������.
     * 
     * @return signed
     */
    public boolean isSigned() {
        return this.signed;
    }

    /**
     * ������������� ����� ��������, �����������, ������������ �� ������ 
     * ����������� �������� �������� ������ �� ������<p>
     * �������� <code>signed</code> ������������� ������������ ��������
     * ������ <code>java.sql.ResultSetMeteData</code>. ������ �����������, �����
     * <code>DataColumn</code> ������������ � ����� � 
     * <code>java.sql.ResutSet</code>. 
     * <p>� ������� TDO Framework ����� �� ������������. ��������� ���������� 
     * - ������������� SQL DDL �������� ������� ���� ������.
     * 
     * @param value - ����� �������� �������� <code>signed</code>.
     */
    public void setSigned(boolean value) {
        this.signed = value;
    }

    /**
     * ���������� ��������, �����������, ����� �� ������������ ������� 
     * � ����������� <code>where</code>.<p>
     * �������� <code>searchable</code> ������������� ������������ ��������
     * ������ <code>java.sql.ResultSetMeteData</code>. ������ �����������, �����
     * <code>DataColumn</code> ������������ � ����� � 
     * <code>java.sql.ResutSet</code>. 
     * <p>� ������� TDO Framework ����� �� ������������. ��������� ���������� 
     * - ������������� SQL DDL �������� ������� ���� ������.
     * 
     * @return searchable
     */
    public boolean isSearchable() {
        return this.searchable;
    }

    /**
     * ������������� ����� ��������, �����������, ����� �� ������������ ������� 
     * � ����������� <code>where</code>.<p>
     * �������� <code>searchable</code> ������������� ������������ ��������
     * ������ <code>java.sql.ResultSetMeteData</code>. ������ �����������, �����
     * <code>DataColumn</code> ������������ � ����� � 
     * <code>java.sql.ResutSet</code>. 
     * <p>� ������� TDO Framework ����� �� ������������. ��������� ���������� 
     * - ������������� SQL DDL �������� ������� ���� ������.
     * 
     * @param value
     */
    public void setSearchable(boolean value) {
        this.searchable = value;
    }

    /**
     * ���������� ��������, ������� ����� �������������� ��� ���������
     * ��� ������ � ����������� �� ������. <p>
     * 
     * @return �������� ��������
     * @see #setLabel
     * @see #getName
     * @see #setName
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * ������������� ����� ��������, ������� ����� �������������� ��� ���������
     * ��� ������ � ����������� �� ������. <p>
     * �������� <code>label</code> ������������� �������� 
     * <code>columnLabel</code> ������ <code>java.sql.ResultSetMeteData</code>.
     * 
     * <p>���������� ������� <code>PropertyChangeEvent</code> � ������, ����
     * ����� �������� �� ����� ��������.
     * 
     * @param label - ����� �������� ��������
     * @see #getLabel
     * @see #getName
     * @see #setName
    
     */
    public void setLabel(String label) {
        if (this.label != null && this.label.equals(label)) {
            return;
        }
        if (this.label == null && label == null) {
            return;
        }
        String oldLabel = this.label;
        this.label = label;

        firePropertyChange("label", oldLabel, this.label);
    }

    /**
     * ���������� ������ �������  � ��������� {@link DataColumnCollection},
     * ���� ������� ��� ���������� � ���������.
     * ��������� <code>DataColumnCollection</code> ������ ����������
     * ��������� �������� �������� <code>index</code> ��� �������, ���������� ���
     * �������� ������� �� ���������.
     *
     * @return  - ������ ������� � ����������
     * @see #setIndex
     * @see AbstractDataColumns
     */
/*    public int getIndex() {
        return this.index;
    }
*/
    /**
     * ������������� ����� �������� ������� �������  � ��������� {@link DataColumnCollection},
     * ���� ������� ��� ���������� � ���������.
     * ��������� <code>DataColumnCollection</code> ������ ����������
     * ��������� �������� �������� <code>index</code> ��� �������, ���������� ���
     * �������� ������� �� ���������. <p>
     * ��������� ������ �������� �������� <code>index</code> ������������, ������,
     * ���� ����� �������� �� ��������� � �������. ��� ���� ������������ �������
     * <code>PropertyChangeEvent</code>. ���������� 
     * <code>DataColumnCollection<code><p> ������ ���������� ���������� 
     * ������������� ��������, ����������� ������� �������� ��������.
     * @param index ����� �������� ������� ������� � ���������
     * @see #getIndex
     * @see tdo.DataColumnCollection
     */
/*    public void setIndex(int index) {
        if (index == this.index) {
            return;
        }
        int oldIndex = this.index;
        this.index = index;
        this.firePropertyChange("index", oldIndex, index);
    }
*/
    /**
     * ���������� �������� ������� ������ ��������� ������ ����.<p>
     * <code>DataColumn</code> ������������ ��������� c {@link tdo.Table},
     * {@link tdo.DataRow} � �.�.. �������� <code>cellIndex</code> ��������� 
     * �� ������ ��������� �������� <code>DataRow</code>. �����, ������ ������,
     * ������ ��� ���� �������� ��� private, protected ��� package. ����������
     * ��� public ������� � �������� ������������.
     * @return cellIndex
     */
    public int getCellIndex() {
        return this.cellIndex;
    }

    /**
     * ������������� ����� �������� ������� ������ ��������� ������ ����.<p>
     * <code>DataColumn</code> ������������ ��������� c {@link tdo.Table},
     * {@link tdo.DataRow} � �.�.. �������� <code>cellIndex</code> ��������� 
     * �� ������ ��������� �������� <code>DataRow</code>. �����, ������ ������,
     * ������ ��� ���� �������� ��� private, protected ��� package. ����������
     * ��� public ������� � �������� ������������. ����� ������������
     * ��� ����������� ������������� � �� ������� ������������ ��� �
     * ����������.
     * @param cellIndex 
     */
    protected void setCellIndex(int cellIndex) {
        this.cellIndex = cellIndex;
    }

    /**
     * ���������� ��������, ������������ ����� �� ������, ����������� ��������
     * ��������� �������� <code>null</code>.
     * �������� <code>nullable</code> ������������� ������������ ��������
     * ������ <code>java.sql.ResultSetMeteData</code>. ������ �����������, �����
     * <code>DataColumn</code> ������������ � ����� � 
     * <code>java.sql.ResutSet</code>. � ����������� �� ����������, �������� 
     * �������� ����� ������ �� ���������, ������������ ������� 
     * {@link #createBlankObject() }. ��� �������� ����� �� ������ �����������
     * �������� ��������� <code>null</code> �������� ������������� ������������
     * ����� {@link #isNullable}.
     * 
     * @return ����� ��������
     * @see #isNullable
     * @see #createBlankObject
     */
    public int getNullable() {
        return this.nullable;
    }

    /**
     * ������������� ����� ��������, ������������ ����� �� ������, ����������� 
     * �������� ��������� �������� <code>null</code>.
     * �������� <code>nullable</code> ������������� ������������ ��������
     * ������ <code>java.sql.ResultSetMeteData</code>. ������ �����������, �����
     * <code>DataColumn</code> ������������ � ����� � 
     * <code>java.sql.ResutSet</code>. � ����������� �� ����������, �������� 
     * �������� ����� ������ �� ���������, ������������ ������� 
     * {@link #createBlankObject() }. ��� �������� ����� �� ������ �����������
     * �������� ��������� <code>null</code> �������� ������������� ������������
     * ����� {@link #isNullable}.
     * 
     * @param nullable ����� ��������
     * @see #isNullable
     * @see #createBlankObject
     */
    public void setNullable(int nullable) {
        this.nullable = nullable;
    }

    /**
     * ���������� ��������, ������������ ����� �� ������, ����������� ��������
     * ��������� �������� <code>null</code>.
     * �������� <code>nullable</code> ������������� ������������ ��������
     * ������ <code>java.sql.ResultSetMeteData</code>. ������ �����������, �����
     * <code>DataColumn</code> ������������ � ����� � 
     * <code>java.sql.ResutSet</code>. � ����������� �� ����������, �������� 
     * �������� ����� ������ �� ���������, ������������ ������� 
     * {@link #createBlankObject() }. ����� <code>ResulSetMetaData</code>
     * ���������� ��������� ��������, ����������� ������� <code>nullable</code>
     * � ����� ������ ��������� ���������� ����������� ����. � ����� ������ 
     * tdo.DataColumn ������� ��������� <code>nullable</code>, ���� ��������
     * �������� ��������� ���� �� ���� �������� : 
     * <ul>
     *   <li>ResultSetMetaData.columnNullable</li>
     *   <li>ResultSetMetaData.columnNullableUnknown</li>
     * </ul>
     * 
     * @return true , ���� ������� ����� ��������� <code>null</code> ��������.
     *  false � ��������� ������.
     * @see #isNullable
     * @see #createBlankObject
     */
    public boolean isNullable() {
        return (nullable == ResultSetMetaData.columnNullable) || (nullable == ResultSetMetaData.columnNullableUnknown);
    }

    /**
     * @return ���������� �������� �������� POJO, ���������������� �������. <p>
     * @see #setProperyName
     */
    public String getPropertyName() {
        return this.propertyName;
    }

    /**
     * ������������� �������� �������� POJO, ���������������� �������. <p>
     * @param propertyName
     * @see #getProperyName
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * @return ���������� �������� �������� POJO, ���������������� �������. <p>
     * @see #setProperyName
     */
    public String getFieldName() {
        return this.fieldName;
    }

    /**
     * ������������� �������� �������� POJO, ���������������� �������. <p>
     * @param fieldName
     * @see #getProperyName
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * ���������� ����� ��������� �������, ������������� ��� �������� 
     * �� ��������� ��� ������, ��������� � ��������.
     * � ������ DataColumn ����� ���������� ����� ���������
     * <code>java.lang.Object</code>. ������-���������� ��� ������� 
     * �������������� �����.
     * 
     * @return �������� �� ���������
     * @see #getDefaultValue
     * @see #setDefaultValue
     * @see #blankValueInstance
     */
    public Object blankValueInstance() {
        return new Object();
    }

    /**
     * ���������� �������� �� ��������� ��� ������, ��������� � ��������.
     * �������� <code>defaultValue</code> ������������ ��� ����������
     * ���������� ������� ������, ����� ���� �� ���������� ��������.
     * ����� ������������ ��� <code>protected</code>, ����� ��������
     * ��� ��������� ��� <code>mutable</code> ��������. �������������
     * ������������ ����� {@link #defualtValueInstance}, ������� ������
     * ���������� �� �������� �������� <code>defaultValue</code>, �
     * ����� ���������.
     * 
     * @return �������� �� ���������
     * @see #setDefaultValue
     * @see #blankValueInstance
     */
    protected Object getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * ������������� ����� �������� �� ��������� ��� ������, ��������� � ��������.
     * �������� <code>defaultValue</code> ������������ ��� ����������
     * ���������� ������� ������, ����� ���� �� ���������� ��������.
     * 
     * @see #getDefaultValue
     * @see #blankValueInstance
     * @throws IllegalArgumentException ����� ����� �������� ����� 
     *          <code>null</code>.
     */
    public void setDefaultValue(Object defaultValue) {
        if (defaultValue == null) {
            throw new IllegalArgumentException("Column defaultValue cannnot be null");
        }
        this.defaultValue = defaultValue;
    }

    /**
     * ���������� �������� ����� ������� ���� ������.<p>
     * �������� <code>tableName</code> ������������� ������������ ��������
     * ������ <code>java.sql.ResultSetMeteData</code>. ������ �����������, �����
     * <code>DataColumn</code> ������������ � ����� � 
     * <code>java.sql.ResutSet</code>. 
     * <p>� ������� TDO Framework ����� �� ������������. ��������� ���������� 
     * - ������������� SQL DDL �������� ������� ���� ������.
     * 
     * @return ��� ������� ���� ������
     */
    public String getTableName() {
        return this.tableName;
    }

    /**
     * ������������� ����� �������� ����� ������� ���� ������.<p>
     * �������� <code>tableName</code> ������������� ������������ ��������
     * ������ <code>java.sql.ResultSetMeteData</code>. ������ �����������, �����
     * <code>DataColumn</code> ������������ � ����� � 
     * <code>java.sql.ResutSet</code>. 
     * <p>� ������� TDO Framework ����� �� ������������. ��������� ���������� 
     * - ������������� SQL DDL �������� ������� ���� ������.
     * 
     * @param tableName ��� ������� ���� ������
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * ���������� �������� ����� ����� ������� ���� ������.<p>
     * �������� <code>schemaName</code> ������������� ������������ ��������
     * ������ <code>java.sql.ResultSetMeteData</code>. ������ �����������, �����
     * <code>DataColumn</code> ������������ � ����� � 
     * <code>java.sql.ResutSet</code>. 
     * <p>� ������� TDO Framework ����� �� ������������. ��������� ���������� 
     * - ������������� SQL DDL �������� ������� ���� ������.
     * 
     * @return ��� ����� ������� ���� ������
     */
    public String getSchemaName() {
        return this.schemaName;
    }

    /**
     * ������������� ����� �������� ����� ����� ������� ���� ������.<p>
     * �������� <code>schemaName</code> ������������� ������������ ��������
     * ������ <code>java.sql.ResultSetMeteData</code>. ������ �����������, �����
     * <code>DataColumn</code> ������������ � ����� � 
     * <code>java.sql.ResutSet</code>. 
     * <p>� ������� TDO Framework ����� �� ������������. ��������� ���������� 
     * - ������������� SQL DDL �������� ������� ���� ������.
     * 
     * @param schemaName ����� ��� ����� ������� ���� ������
     */
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    /**
     * ���������� ��������, ������������ ������ ���������� �������� ������, 
     * ��������� � ��������.<p>
     * ������ ������ ������������ ��� �������: <i>������� ������</i> �
     * <i>����������� ������</i>. ����� ����������� ������ <i>������� ������</i>,
     * �� �������� �������� ����� {@link #DATA_KIND}. ����� ����������� ������ 
     * <i>����������� ������</i>, �� �������� �������� ����� {@link #CALC_KIND}.
     * 
     * @return kind
     */
    public int getKind() {
        return this.kind;
    }

    /**
     * ���������� ��������, ������������ ������ ���������� �������� ������, 
     * ��������� � ��������<p>
     * ������ ������ ������������ ��� �������: <i>������� ������</i> �
     * <i>����������� ������</i>. ����� ����������� ������ <i>������� ������</i>,
     * �� �������� �������� ����� {@link #DATA_KIND}. ����� ����������� ������ 
     * <i>����������� ������</i>, �� �������� �������� ����� {@link #CALC_KIND}.
     * <p>��������� ������, ��������������� ����������� ������� �� ����� 
     * ������������ �����������, �� ��� ��������� ������ �������� ������ 
     * <code>CALC_KIND</code> ������������� ��������������� �������� ��������
     * <code>readOnly</code> � <code>true</code>.<p>
     * ��������� ������ �������� �������� <code>kind</code> ������������, ������,
     * ���� ����� �������� �� ��������� � �������. ��� ���� ������������ �������
     * <code>PropertyChangeEvent</code>.<p>
     * <b>����������.</b>��� ������� ���������� ����� ��������, �������� �� ������
     * �� <ul> <li>DATA_KIND</li><li>CALC_KIND</li></ul> ����� ���������� 
     * ���������� � �����������. ����� ��������� ����� ���� �������� � 
     * ����������� �������.
     * 
     * @param kind ����� �������� ������� ����������
     * @see #getKind
     */
    public void setKind(int kind) {
        if (this.kind == kind) {
            return;
        }
        if (kind != CALC_KIND && kind != DATA_KIND) {
            return;
        }
        int oldKind = this.kind;
        this.kind = kind;
        if (kind == CALC_KIND) {
            this.setReadOnly(true);
        }
        firePropertyChange("kind", oldKind, this.kind);
    }

    /**
     * ���������� ����� ��������� ���� Object ��� null. <p>
     * ������ ���������� ������, ��� �������, �������������� ���� ����� � 
     * ��������� ����� ���������� �������� ��� ������������� ������. ��������,
     * ���� ������� ������������� ��� ��������� ������ ���� java.lang.Integer,
     * �� ������� ��������� � ����������, ��������, <code> new Integer(0)</code>.
     * 
     * @return null - ���� ����� isNullable() ���������� true;
     *         ���������� �������� �������� defaultValue � ��������� ������.
     */
    public Object createBlankObject() {
        if (isNullable()) {
            return null;
        }
        return blankValueInstance();
    }
    public void assign(DataColumn source ) {
            this.name = source.name;
            this.kind = source.kind;
            this.scale = source.scale;
            this.size = source.size;
            this.precision = source.precision;
            this.sqlType = source.sqlType;
            this.cellIndex = source.cellIndex;
            this.type = source.type;
            this.readOnly = source.readOnly;
            this.nullable = source.nullable;
            this.autoIncrement = source.autoIncrement;
            this.caseSensitive = source.caseSensitive;
            this.currency = source.currency;
            this.signed = source.signed;
            this.searchable = source.searchable;
            this.label = source.label;
            this.tableName = source.tableName;
            this.schemaName = source.schemaName; // as in sql db table

            this.cellIndex = source.cellIndex;

            this.propertyName = source.propertyName;
            this.fieldName = source.fieldName;
            this.index = source.index;

            this.expression = source.expression;
        
    }
    /**
     * ������� ����� ������, ���������� ������ �������.
     * ��������� ��������� {@link java.lang.Cloneable}. ������-���������� ������
     * �������������� ����� ��� ��������� �������������� ������������.
     * @return ���� �������� �������
     */
    @Override
    public synchronized Object clone() {
        try {
            DataColumn obj = (DataColumn) super.clone();
            obj.name = this.name;
            obj.kind = this.kind;
            obj.scale = this.scale;
            obj.size = this.size;
            obj.precision = this.precision;
            obj.sqlType = this.sqlType;
            obj.changeSupport = null;
            obj.cellIndex = this.cellIndex;
            obj.type = this.type;
            obj.readOnly = this.readOnly;
            obj.nullable = this.nullable;
            obj.autoIncrement = this.autoIncrement;
            obj.caseSensitive = this.caseSensitive;
            obj.currency = this.currency;
            obj.signed = this.signed;
            obj.searchable = this.searchable;
            obj.label = this.label;
            obj.tableName = this.tableName;
            obj.schemaName = this.schemaName; // as in sql db table

            obj.cellIndex = this.cellIndex;

            obj.propertyName = this.propertyName;
            obj.fieldName = this.fieldName;
            obj.index = this.index;

            obj.setExpression(this.expression);

            return obj;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    /**
     * ���������� ������, ��������������� ���� �������, ��������� ������, 
     * �������� ����������. <p>
     *
     * @param obj ������ ��� ������� ����������� �����
     * @return ����� ������ ��� ������ �������� ����������, ���� �� ���������
     *    �������������� �����.
     */
    public Object toColumnType(Object obj) {
        return obj;
    }
    
    public boolean copy(Object source, Object target) {
        return false;
    }
    
    /**
     * ���������� ��� �������� �������, ������, ��� <code>null</code> ������
     * ������ ��������� ������� ��������, ���� ������ ������ ������ �� ����� 
     * <code>null</code>. ���� ��� ������� ����� <code>null</code>, �� ��� 
     * ��������� �������.
     * ����� ������������ ��� ���������� ����������, ���������� � ������
     * ��������� �������� TDO.
     * @param obj ������ ������������ ������
     * @param anotherObj ������ ������������ ������
     * @return 0 - ���� ������� ��������� �������. 1 - ���� �������� �������
     * ������� ������ ������ �������� �������. -1  - ���� �������� �������
     * ������� ������ ������ �������� �������. 
     * @see #compareObjects(Object,Object,boolean)
     */
    @Override
    public int compareObjects(Object obj, Object anotherObj) {
        return this.compareObjects(obj, anotherObj, true);
    }

    /**
     * ���������� ��� �������� �������. ��� ���������������� <code>null</code>
     * �������� ������� ������� �� ��������� �������� ���������.
     * � ����� ������, ���� ��� ������� ����� <code>null</code>, �� ��� 
     * ��������� �������.
     * ����� ������� ������ ����������� ������� �� ������ <code>null</code> � 
     * ������ ������ <code>obj.toString</code>, <code>anotherObj.toString</code>
     * � �������� ��������� ����� ������� <code>compareTo</code>. ������, 
     * ����������� ������, ������ �������������� ����� ��� ���������� ������
     * ������� ����������� ����. <p>
     * ����� ������������ ��� ���������� ����������, ���������� � ������
     * ��������� �������� TDO.
     * @param obj ������ ������������ ������
     * @param anotherObj ������ ������������ ������
     * @param nullMin ���� ����� <code>true</code> �� <code>null</code> ��������
     *   ��������� �����������. false - ��������, ��� <code>null</code> ��������
     *   ����������� ��� ������������. 
     * @return 0 - ���� �������  �����. 1 - ���� �������� �������
     * ������� ������ ������ �������� �������. -1  - ���� �������� �������
     * ������� ������ ������ �������� �������. 
     * 
     * @see #compareObjects(Object,Object)
     */
    @Override
    public int compareObjects(Object obj, Object anotherObj, boolean nullMin) {
        int nullRes = nullMin ? -1 : 1;
        if (obj == null && anotherObj == null) {
            return 0;
        }
        if (obj == null) {
            return nullRes;
        }
        if (anotherObj == null) {
            return -nullRes;
        }
        return compareNotNull(obj, anotherObj);
    }

    /**
     * ������� �����, ��������� ��� ����������� ������������� � �������
     * {@link #compareObjects(Object,Object,boolean)}.
     * ����� compareObjects ���������� ��� ����������� ��������, ����� ����
     * ��� ��� ��������� ����� �������� <code>null</code>. ���� ��� �������� 
     * ������� �� <code>null</code>, �� ���������� ������ �����. ������,
     * ����������� ������, ����� ������ �������������� ������ �����, ����������
     * �������� ������� �� ���� ������.
     * @param o1 ������ ������������ �������
     * @param o2 ������ ������������ �������
     * @return 0 - ���� ������� ��������� �������. 1 - ���� �������� �������
     * ������� ������ ������ �������� �������. -1  - ���� �������� �������
     * ������� ������ ������ �������� �������. 
     */
    protected int compareNotNull(Object o1, Object o2) {
        String s1 = o1.toString();
        String s2 = o2.toString();
        return s1.compareTo(s2);

    }

    /**
     * ���������� ������� PropertyChangeEvent, ����� �������� ����� ��� �� 
     * ���������� java.lang.String, boolean, int.
     * @param propertyName ��� ���������� ��������
     * @param oldValue �������� �������� ����� ����������� ��� ������ ��������.
     * @param newValue ����� �������� ��������.
     */
    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (changeSupport != null) {
            changeSupport.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    /**
     * ���������� ������� PropertyChangeEvent, ����� �������� ����� ��� int.
     * @param propertyName ��� ���������� ��������
     * @param oldValue �������� �������� ����� ����������� ��� ������ ��������.
     * @param newValue ����� �������� ��������.
     */
    public void firePropertyChange(String propertyName, int oldValue, int newValue) {
        if (changeSupport != null) {
            changeSupport.firePropertyChange(propertyName, oldValue, newValue);

        }
    }

    /**
     * ���������� ������� PropertyChangeEvent, ����� �������� ����� ��� boolean.
     * @param propertyName ��� ���������� ��������
     * @param oldValue �������� �������� ����� ����������� ��� ������ ��������.
     * @param newValue ����� �������� ��������.
     */
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        if (changeSupport != null) {
            changeSupport.firePropertyChange(propertyName, oldValue, newValue);

        }
    }

    /**
     * ���������� ������� PropertyChangeEvent, ����� �������� ����� ��� String.
     * @param propertyName ��� ���������� ��������
     * @param oldValue �������� �������� ����� ����������� ��� ������ ��������.
     * @param newValue ����� �������� ��������.
     */
    public void firePropertyChange(String propertyName, String oldValue, String newValue) {
        if (changeSupport != null) {
            changeSupport.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    /**
     * ��������� ��������� ��������� ������� <code>PropertyChangeEvent</code> � 
     * ���������� ���������.
     * @param listener ����������� ���������� �������
     */
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        if (changeSupport == null) {
            changeSupport = new PropertyChangeSupport(this);
        }
        changeSupport.addPropertyChangeListener(listener);
    }

    /**
     * ������� ��������� ��������� ������� <code>PropertyChangeEvent</code> �� 
     * ���������� ���������.
     * @param listener ��������� ���������� �������
     */
    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        if (changeSupport != null) {
            changeSupport.removePropertyChangeListener(listener);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Internal classes implementing class tdo.DataColumn
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public static abstract class PDBNumberColumn extends DataColumn implements Cloneable, Serializable {

        @Override
        public Object clone() {
            PDBNumberColumn obj = (PDBNumberColumn) super.clone();
            return obj;
        }
    } //PDBNumericColumn


    /**
     * ����� ��������� ����������� ����� {@link tdo.DataColumn} ��� �������������
     * � ������� ���� <code>java.lang.Long</code>. 
     * Sql-��� BIGINT, ������������ ������� <code>java.sql.Types</code> �������������
     * ���� <code>java.lang.Long</code>.
     */
    public static class PDBBigIntColumn extends PDBNumberColumn implements Cloneable, Serializable {

        /**
         * �������������� �������� ����� � �������, ����������� ��� ������.
         * <code><pre>
         *   setSize(20);
         *   setScale(0);
         *   setType(Long.class);
         *   setSqlType(java.sql.Types.BIGINT);            
         *   setDefaultValue(new Long(0));
         * </pre></code>
         */
        public PDBBigIntColumn() {

            this.setSize(20);
            this.setScale(0);
            this.setType(Long.class);
            this.setSqlType(java.sql.Types.BIGINT);
            this.setDefaultValue(new Long(0));
        }

        /**
         * @return ���������� �������� ���� ������ ������ <code>java.lang.Long</code>.
         */
        @Override
        public Class getType() {
            return Long.class;
        }

        /**
         * @inherited
         */
        @Override
        public synchronized Object clone() {
            PDBBigIntColumn obj = (PDBBigIntColumn) super.clone();
            return obj;
        }

        /**
         * ���������� ����� ��������� �������, ������������� ��� �������� 
         * �� ��������� ��� ������, ��������� � ��������.
         * 
         * @return �������� �� ���������. ���� ����� <code>getDefaultValue</code>
         *    ���������� <code>null</code>, �� ������������ <code>null</code>.
         *    � ��������� ������� ������������ ����� ��������� ���� 
         *   <code>java.lang.Long</code>, ����� <code>longValue</code> ��������
         *   ����� �������� <code>getDefaultValue().longValue()</code>.
         */
        @Override
        public Object blankValueInstance() {
            Object obj = getDefaultValue();
            if (obj == null) {
                return null;
            }
            Long d = (Long) obj;
            return new Long(d.longValue());
        }

        /**
         * ����������� �������� ��������� � ��� <code>java.lang.Long</code>.
         * @param value ������������� ��������
         * @return �������� ���� <code>java.lang.Long</code>.          
         * ���� <i>value</i> ����� <code>null</code>, �� ���������� 
         * <code>null</code>.<br>
         * ���� <i>value</i> �������� ����� <code>Long</code>, �� ������������ 
         * ��� ��������������.<br>
         * ���� <i>value</i> �������� ����� <code>Number</code>, �� ��������� � 
         * ������������  ��������� ���� <code>Long</code>.<br>
         * ���� <i>value</i> ��� ������� �� ���������, �� ������������ �������
         * ��������� <code>new Long(value.toString()</code>. ��� ���� ����� ����
         * ��������� ���������� <code>NumberFormatException</code>.
         * @throws NumberFormatException
         */
        @Override
        public Object toColumnType(Object value) {

            if (value instanceof Long) {
                return value;
            }

            if (value == null) {
                return null;
            }

            Object r; // = null;

            if (value instanceof Number) {
                r = Long.valueOf(((Number) value).longValue());
            } else {
                r = new Long(value.toString()); //may be NumberFormatException

            }
            return r;
        }

        @Override
        protected int compareNotNull(Object o1, Object o2) {
            return ((Long) o1).compareTo((Long) o2);
        }
    } //class PDBBigIntColumn


    /**
     * ����� ��������� ����������� ����� {@link tdo.DataColumn} ��� �������������
     * � ������� ���� <code>java.lang.Double</code>. 
     * Sql-��� DOUBLE, ������������ ������� <code>java.sql.Types</code> �������������
     * ���� <code>java.lang.Double</code>.
     */
    public static class PDBDoubleColumn extends PDBNumberColumn implements Cloneable, Serializable {

        /**
         * �������������� �������� ����� � �������, ����������� ��� ������.
         * <code><pre>
         *   setSize(15);
         *   setScale(-1);
         *   setType(Double.class);
         *   setSqlType(java.sql.Types.DOUBLE);            
         *   setDefaultValue(new Double(0));
         * </pre></code>
         */
        public PDBDoubleColumn() {
            this.setSize(15);
            this.setScale(-1);
            this.setType(Double.class);
            this.setSqlType(java.sql.Types.DOUBLE);
            this.setDefaultValue(new Double(0));
        }

        /**
         * @return ���������� �������� ���� ������ ������ <code>java.lang.Double</code>.
         */
        @Override
        public Class getType() {
            return Double.class;
        }

        /**
         * @inherited
         */
        @Override
        public synchronized Object clone() {
            PDBDoubleColumn obj = (PDBDoubleColumn) super.clone();
            return obj;
        }

        @Override
        protected int compareNotNull(Object o1, Object o2) {
            return ((Double) o1).compareTo((Double) o2);
        }

        /**
         * ���������� ����� ��������� �������, ������������� ��� �������� 
         * �� ��������� ��� ������, ��������� � ��������.
         * 
         * @return �������� �� ���������. ���� ����� <code>getDefaultValue</code>
         *    ���������� <code>null</code>, �� ������������ <code>null</code>.
         *    � ��������� ������� ������������ ����� ��������� ���� 
         *   <code>java.lang.Double</code>, ����� <code>doubleValue</code> ��������
         *   ����� �������� <code>getDefaultValue().doubleValue()</code>.
         */
        @Override
        public Object blankValueInstance() {
            Object obj = getDefaultValue();
            if (obj == null) {
                return null;
            }
            Double d = (Double) obj;
            return new Double(d.doubleValue());
        }

        /**
         * ����������� �������� ��������� � ��� <code>java.lang.Double</code>.
         * @param value ������������� ��������
         * @return �������� ���� <code>java.lang.Double</code>.          
         * ���� <i>value</i> ����� <code>null</code>, �� ���������� 
         * <code>null</code>.<br>
         * ���� <i>value</i> �������� ����� <code>Double</code>, �� ������������ 
         * ��� ��������������.<br>
         * ���� <i>value</i> �������� ����� <code>Number</code>, �� ��������� � 
         * ������������  ��������� ���� <code>Double</code>.<br>
         * ���� <i>value</i> ��� ������� �� ���������, �� ������������ �������
         * ��������� <code>new Double(value.toString()</code>. ��� ���� ����� ����
         * ��������� ���������� <code>NumberFormatException</code>.
         * @throws NumberFormatException
         */
        @Override
        public Object toColumnType(Object value) {
            Object r = null;

            if (value instanceof Double) {
                r = value;
            } else if (value instanceof Number) {
                r = new Double(((Number) value).doubleValue());
            } else if (value != null) {
                r = Double.valueOf(value.toString()); //may be NumberFormatException

            }
            return r;
        }
    }

    /**
     * ����� ��������� ����������� ����� {@link tdo.DataColumn} ��� �������������
     * � ������� ���� <code>java.math.BigDecoimal</code>. 
     * Sql-��� DECIMAL, ������������ ������� <code>java.sql.Types</code> �������������
     * ���� <code>java.math.BigDecimal</code>.
     */
    public static class PDBDecimalColumn extends PDBNumberColumn implements Cloneable, Serializable {

        public static final BigDecimal ZEROVALUE = new BigDecimal(0);

        /**
         * �������������� �������� ����� � �������, ����������� ��� ������.
         * <code><pre>
         *   setSize(15);
         *   setScale(2);
         *   setType(BigDecimal.class);
         *   setSqlType(java.sql.Types.DECIMAL);            
         *   this.setDefaultValue(new BigDecimal(0));
         *   this.setDefaultValue(((BigDecimal) getDefaultValue()).setScale(getScale(), BigDecimal.ROUND_HALF_UP));
         * </pre></code>
         */
        public PDBDecimalColumn() {
            this.setSize(15);
            this.setScale(2);
            this.setType(BigDecimal.class);
            this.setSqlType(java.sql.Types.DECIMAL);
            this.setDefaultValue(new BigDecimal(0));
            this.setDefaultValue(((BigDecimal) blankValueInstance()).setScale(getScale(), BigDecimal.ROUND_HALF_UP));
        }

        /**
         * @return ���������� �������� ���� ������ ������ <code>java.math.BigDecimal</code>.
         */
        @Override
        public Class getType() {
            return BigDecimal.class;
        }

        /**
         * @inherited
         */
        @Override
        public synchronized Object clone() {
            PDBDecimalColumn obj = (PDBDecimalColumn) super.clone();
            return obj;
        }

        @Override
        protected int compareNotNull(Object o1, Object o2) {
            return ((BigDecimal) o1).compareTo((BigDecimal) o2);
        }

        /**
         * ���������� ����� ��������� �������, ������������� ��� �������� 
         * �� ��������� ��� ������, ��������� � ��������.
         * 
         * @return �������� �� ���������. ���� ����� <code>getDefaultValue</code>
         *    ���������� <code>null</code>, �� ������������ <code>null</code>.
         *    � ��������� ������� ������������ ����� ��������� ���� 
         *   <code>java.math.BigDecimal</code>. 
         */
        @Override
        public Object blankValueInstance() {
            Object obj = getDefaultValue();
            if (obj == null) {
                return null;
            }
            BigDecimal bd;
            if (obj instanceof Double) {
                Double d = (Double) obj;
                return new Double(d.doubleValue());
            }
            bd = (BigDecimal) obj;
            return new BigDecimal(bd.unscaledValue(), bd.scale());
        }

        /**
         * ����������� �������� ��������� � ��� <code>java.math.BigDecimal</code>.
         * @param value ������������� ��������
         * @return �������� ���� <code>java.math.BigDecimal</code>.          
         * <ol>
         * <li>���� <i>value</i> ����� <code>null</code>, �� ���������� 
         * <code>null</code>.</li>
         * <li>���� <i>value</i> �������� ����� <code>java.math.BigDecimal</code>, 
         * �� ������������  <i>value</i> ��� �����-���� ��������.</li>
         * <li>���� <i>value</i> �������� ����� <code>BigInteger</code>, �� ��������� � 
         * ������������  ��������� ���� <code>java.math.BigDecimal</code> 
         * ����������� ������������ � ���������� 
         * <code>BigDecimal(BigInteger)</code>.</li>
         * <li>���� <i>value</i> �������� ����� <code>Number</code>, �� ��������� � 
         * ������������  ��������� ���� <code>java.math.BigDecimal</code>
         * ����������� ������������ � ���������� 
         * <code>BigDecimal(double)</code>.</li>
         * <li>���� <i>value</i> ��� ������� �� ���������, �� ������������ �������
         * ��������� <code>new BigDecimal(value.toString()</code>. ��� ���� ����� ����
         * ��������� ���������� <code>NumberFormatException</code>.</li>
         * ���� ��������� (����� ������� 1) � 2) ) ������� �� <code>null</code>,
         * �� ��� ������������� ���������� ���������������:
         * <code>setScale( getScale(), BigDecimal.ROUND_HALF_UP);</code>
         * @throws NumberFormatException
         */
        @Override
        public Object toColumnType(Object value) {
            if (value == null) {
                return null;
            }
            Object r = null;

            if (value instanceof BigDecimal) {
                r = value;
            } else if (value instanceof BigInteger) {
                r = new BigDecimal((BigInteger) value);
            } else if (value instanceof Number) {
                r = new BigDecimal(((Number) value).doubleValue());
            } else if (value != null) {
                r = new BigDecimal(value.toString()); //may be NumberFormatException

            }
            if (r != null) {
                r = ((BigDecimal) r).setScale(getScale(), BigDecimal.ROUND_HALF_UP);
            }
            return r;
        }
    } // class PDBDecimalColumn


    /**
     * ����� ��������� ����������� ����� {@link tdo.DataColumn} ��� �������������
     * � ������� ���� <code>java.lang.Integer</code>. 
     * Sql-��� INTEGER, ������������ ������� <code>java.sql.Types</code> �������������
     * ���� <code>java.lang.Integer</code>.
     */
    public static class PDBIntegerColumn extends PDBNumberColumn implements Cloneable, Serializable {

        /**
         * �������������� �������� ����� � �������, ����������� ��� ������.
         * <code><pre>
         *   setSize(10);
         *   setScale(0);
         *   setType(Integer.class);
         *   setSqlType(java.sql.Types.INTEGER);            
         *   this.setDefaultValue(new Integer(0));
         * </pre></code>
         */
        public PDBIntegerColumn() {
            this.setSqlType(java.sql.Types.INTEGER);
            this.setType(Integer.class);
            this.setDefaultValue(new Integer(0));

            this.setSize(10);
            this.setScale(0);
        }

        /**
         * @return ���������� �������� ���� ������ ������ <code>java.lang.Integer</code>.
         */
        @Override
        public Class getType() {
            return Integer.class;
        }

        /**
         * 
         * @inherited
         */
        @Override
        public synchronized Object clone() {
            PDBIntegerColumn obj = (PDBIntegerColumn) super.clone();
            return obj;
        }

        @Override
        protected int compareNotNull(Object o1, Object o2) {
            return ((Integer) o1).compareTo((Integer) o2);
        }

        /**
         * ���������� ����� ��������� �������, ������������� ��� �������� 
         * �� ��������� ��� ������, ��������� � ��������.
         * 
         * @return �������� �� ���������. ���� ����� <code>getDefaultValue</code>
         *    ���������� <code>null</code>, �� ������������ <code>null</code>.
         *    � ��������� ������� ������������ ����� ��������� ���� 
         *   <code>java.lang.Integer</code>, ����� <code>intValue</code> ��������
         *   ����� �������� <code>getDefaultValue().intValue()</code>.
         */
        @Override
        public Object blankValueInstance() {
            Object obj = getDefaultValue();

            if (obj == null) {
                return null;
            }
            Integer d = (Integer) obj;
            return new Integer(d.intValue());
        }

        /**
         * ����������� �������� ��������� � ��� <code>java.lang.Integer</code>.
         * @param value ������������� ��������
         * @return �������� ���� <code>java.lang.Integer</code>.          
         * ���� <i>value</i> ����� <code>null</code>, �� ���������� 
         * <code>null</code>.<br>
         * ���� <i>value</i> �������� ����� <code>Integer</code>, �� ������������ 
         * ��� ��������������.<br>
         * ���� <i>value</i> �������� ����� <code>Number</code>, �� ��������� � 
         * ������������  ��������� ���� <code>Integer</code>.<br>
         * ���� <i>value</i> ��� ������� �� ���������, �� ������������ �������
         * ��������� <code>new Integer(value.toString()</code>. ��� ���� ����� ����
         * ��������� ���������� <code>NumberFormatException</code>.
         * @throws NumberFormatException
         */
        @Override
        public Object toColumnType(Object value) {
            Object r = null;

            if (value instanceof Integer) {
                r = value;
            } else if (value instanceof Number) {
                r = new Integer(((Number) value).intValue());
            } else if (value != null) {
                r = Integer.valueOf(value.toString()); //may be NumberFormatException

            }
            return r;
        }
    } //PDBIntegerColumn


    /**
     * ����� ��������� ����������� ����� {@link tdo.DataColumn} ��� �������������
     * � ������� ���� <code>java.lang.Float</code>. 
     * Sql-��� REAL, ������������ ������� <code>java.sql.Types</code> �������������
     * ���� <code>java.lang.Float</code>.
     */
    public static class PDBRealColumn extends PDBNumberColumn implements Cloneable, Serializable {

        /**
         * �������������� �������� ����� � �������, ����������� ��� ������.
         * <code><pre>
         *   setSize(15);
         *   setScale(-1);
         *   setType(Float.class);
         *   setSqlType(java.sql.Types.REAL);            
         *   this.setDefaultValue(new Float(0));
         * </pre></code>
         */
        public PDBRealColumn() {
            this.setSize(15);
            this.setScale(-1);
            this.setType(Float.class);
            this.setSqlType(java.sql.Types.REAL);
            this.setDefaultValue(new Float(0));
        }

        /**
         * @return ���������� �������� ���� ������ ������ <code>java.lang.Float</code>.
         */
        @Override
        public Class getType() {
            return Float.class;
        }

        /**
         * @inherited
         */
        @Override
        public synchronized Object clone() {
            PDBRealColumn obj = (PDBRealColumn) super.clone();
            return obj;
        }

        @Override
        protected int compareNotNull(Object o1, Object o2) {
            return ((Float) o1).compareTo((Float) o2);
        }

        /**
         * ���������� ����� ��������� �������, ������������� ��� �������� 
         * �� ��������� ��� ������, ��������� � ��������.
         * 
         * @return �������� �� ���������. ���� ����� <code>getDefaultValue</code>
         *    ���������� <code>null</code>, �� ������������ <code>null</code>.
         *    � ��������� ������� ������������ ����� ��������� ���� 
         *   <code>java.lang.Float</code>, ����� <code>floatValue</code> ��������
         *   ����� �������� <code>getDefaultValue().floatValue()</code>.
         */
        @Override
        public Object blankValueInstance() {
            Object obj = getDefaultValue();
            if (obj == null) {
                return null;
            }
            Float d = (Float) obj;
            return new Float(d.floatValue());
        }

        /**
         * ����������� �������� ��������� � ��� <code>java.lang.Float</code>.
         * @param value ������������� ��������
         * @return �������� ���� <code>java.lang.Float</code>.          
         * ���� <i>value</i> ����� <code>null</code>, �� ���������� 
         * <code>null</code>.<br>
         * ���� <i>value</i> �������� ����� <code>Float</code>, �� ������������ 
         * ��� ��������������.<br>
         * ���� <i>value</i> �������� ����� <code>Number</code>, �� ��������� � 
         * ������������  ��������� ���� <code>Float</code>.<br>
         * ���� <i>value</i> ��� ������� �� ���������, �� ������������ �������
         * ��������� <code>new Float(value.toString()</code>. ��� ���� ����� ����
         * ��������� ���������� <code>NumberFormatException</code>.
         * @throws NumberFormatException
         */
        @Override
        public Object toColumnType(Object value) {
            Object r = null;

            if (value instanceof Float) {
                r = value;
            } else if (value instanceof Number) {
                r = new Float(((Number) value).floatValue());
            } else if (value != null) {
                r = Float.valueOf(value.toString()); //may be NumberFormatException

            }
            return r;
        }
    }

    /**
     * ����� ��������� ����������� ����� {@link tdo.DataColumn} ��� �������������
     * � ������� ���� <code>java.lang.Short</code>. 
     * Sql-��� REAL, ������������ ������� <code>java.sql.Types</code> �������������
     * ���� <code>java.lang.Short</code>.
     */
    public static class PDBSmallIntColumn extends PDBNumberColumn implements Cloneable, Serializable {

        /**
         * �������������� �������� ����� � �������, ����������� ��� ������.
         * <code><pre>
         *   setSize(6);
         *   setScale(0);
         *   setType(Short.class);
         *   setSqlType(java.sql.Types.SMALLINT);            
         *   this.setDefaultValue(new Short("0"));
         * </pre></code>
         */
        public PDBSmallIntColumn() {
            this.setSize(6);
            this.setScale(0);
            this.setType(Short.class);
            this.setSqlType(java.sql.Types.SMALLINT);
            this.setDefaultValue(new Short("0"));
        }

        /**
         * @return ���������� �������� ���� ������ ������ <code>java.lang.Short</code>.
         */
        @Override
        public Class getType() {
            return Short.class;
        }

        /**
         * @inherited
         */
        @Override
        public synchronized Object clone() {
            PDBSmallIntColumn obj = (PDBSmallIntColumn) super.clone();
            return obj;
        }

        @Override
        protected int compareNotNull(Object o1, Object o2) {
            return ((Short) o1).compareTo((Short) o2);
        }

        /**
         * ���������� ����� ��������� �������, ������������� ��� �������� 
         * �� ��������� ��� ������, ��������� � ��������.
         * 
         * @return �������� �� ���������. ���� ����� <code>getDefaultValue</code>
         *    ���������� <code>null</code>, �� ������������ <code>null</code>.
         *    � ��������� ������� ������������ ����� ��������� ���� 
         *   <code>java.lang.Short</code>, ����� <code>shortValue</code> ��������
         *   ����� �������� <code>getDefaultValue().shortValue()</code>.
         */
        @Override
        public Object blankValueInstance() {
            Object obj = getDefaultValue();

            if (obj == null) {
                return null;
            }
            Short d = (Short) obj;
            return new Short(d.shortValue());
        }

        /**
         * ����������� �������� ��������� � ��� <code>java.lang.Short</code>.
         * @param value ������������� ��������
         * @return �������� ���� <code>java.lang.Short</code>.          
         * ���� <i>value</i> ����� <code>null</code>, �� ���������� 
         * <code>null</code>.<br>
         * ���� <i>value</i> �������� ����� <code>Short</code>, �� ������������ 
         * ��� ��������������.<br>
         * ���� <i>value</i> �������� ����� <code>Number</code>, �� ��������� � 
         * ������������  ��������� ���� <code>Short</code>.<br>
         * ���� <i>value</i> ��� ������� �� ���������, �� ������������ �������
         * ��������� <code>new Short(value.toString()</code>. ��� ���� ����� ����
         * ��������� ���������� <code>NumberFormatException</code>.
         * @throws NumberFormatException
         */
        @Override
        public Object toColumnType(Object value) {
            Object r = null;

            if (value instanceof Short) {
                r = value;
            } else if (value instanceof Number) {
                r = new Short(((Number) value).shortValue());
            } else if (value != null) {
                r = Short.valueOf(value.toString()); //may be NumberFormatException

            }
            return r;
        }
    }

    /**
     * ����� ��������� ����������� ����� {@link tdo.DataColumn} ��� �������������
     * � ������� ���� <code>java.lang.Byte</code>. 
     * Sql-��� TINYINT, ������������ ������� <code>java.sql.Types</code> �������������
     * ���� <code>java.lang.Byte</code>.
     */
    public static class PDBTinyIntColumn extends PDBNumberColumn implements Cloneable, Serializable {

        /**
         * �������������� �������� ����� � �������, ����������� ��� ������.
         * <code><pre>
         *   setSize(4);
         *   setScale(0);
         *   setType(Byte.class);
         *   setSqlType(java.sql.Types.TINYINT);            
         *   this.setDefaultValue(new Byte("0"));
         * </pre></code>
         */
        public PDBTinyIntColumn() {

            this.setSize(4);
            this.setScale(0);
            this.setType(Byte.class);
            this.setSqlType(java.sql.Types.TINYINT);

            this.setDefaultValue(new Byte("0"));
        }

        /**
         * @return ���������� �������� ���� ������ ������ <code>java.lang.Byte</code>.
         */
        @Override
        public Class getType() {
            return Byte.class;
        }

        /**
         * @inherited
         */
        @Override
        public synchronized Object clone() {
            PDBTinyIntColumn obj = (PDBTinyIntColumn) super.clone();
            return obj;
        }

        @Override
        protected int compareNotNull(Object o1, Object o2) {
            return ((Byte) o1).compareTo((Byte) o2);
        }

        /**
         * ���������� ����� ��������� �������, ������������� ��� �������� 
         * �� ��������� ��� ������, ��������� � ��������.
         * 
         * @return �������� �� ���������. ���� ����� <code>getDefaultValue</code>
         *    ���������� <code>null</code>, �� ������������ <code>null</code>.
         *    � ��������� ������� ������������ ����� ��������� ���� 
         *   <code>java.lang.Byte</code>, ����� <code>byteValue</code> ��������
         *   ����� �������� <code>getDefaultValue().byteValue()</code>.
         */
        @Override
        public Object blankValueInstance() {
            Object obj = getDefaultValue();

            if (obj == null) {
                return null;
            }
            Byte d = (Byte) obj;
            return new Byte(d.byteValue());
        }

        /**
         * ����������� �������� ��������� � ��� <code>java.lang.Byte</code>.
         * @param value ������������� ��������
         * @return �������� ���� <code>java.lang.Byte</code>.          
         * ���� <i>value</i> ����� <code>null</code>, �� ���������� 
         * <code>null</code>.<br>
         * ���� <i>value</i> �������� ����� <code>Byte</code>, �� ������������ 
         * ��� ��������������.<br>
         * ���� <i>value</i> �������� ����� <code>Number</code>, �� ��������� � 
         * ������������  ��������� ���� <code>Byte</code>.<br>
         * ���� <i>value</i> ��� ������� �� ���������, �� ������������ �������
         * ��������� <code>new Byte(value.toString()</code>. ��� ���� ����� ����
         * ��������� ���������� <code>NumberFormatException</code>.
         * @throws NumberFormatException
         */
        @Override
        public Object toColumnType(Object value) {
            Object r;//My 06.03.2012 = null;
            if (value == null) {
                return null;
            }
            if (value instanceof Byte) {
                return value;
            }
            if (value instanceof Number) {
                r = new Byte(((Number) value).byteValue());
            } else {
                r = Byte.valueOf(value.toString()); //may be NumberFormatException

            }
            return r;
        }
    }

    /**
     * ����� ��������� ����������� ����� {@link tdo.DataColumn} ��� �������������
     * � ������� ���� <code>java.lang.Boolean</code>. 
     * Sql-��� BIT, ������������ ������� <code>java.sql.Types</code> �������������
     * ���� <code>java.lang.Boolean</code>.
     */
    public static class PDBBitColumn extends DataColumn implements Cloneable, Serializable {

        /**
         * �������������� �������� ����� � �������, ����������� ��� ������.
         * <code><pre>
         *   setType(Boolean.class);
         *   setSqlType(java.sql.Types.BIT);            
         *   this.setDefaultValue(new Boolean("0"));
         * </pre></code>
         */
        public PDBBitColumn() {
            this.setType(Boolean.class);
            this.setSqlType(java.sql.Types.BIT);
            this.setDefaultValue(new Boolean(false));
        }

        /**
         * @return ���������� �������� ���� ������ ������ <code>java.lang.Boolean</code>.
         */
        @Override
        public Class getType() {
            return Boolean.class;
        }

        /**
         * @inherited
         */
        @Override
        public synchronized Object clone() {
            PDBBitColumn obj = (PDBBitColumn) super.clone();

            return obj;
        }

        /**
         * ����������� �������� ��������� � ��� <code>java.lang.Boolean</code>.
         * @param value ������������� ��������
         * @return �������� ���� <code>java.lang.Boolean</code>.          
         * ���� <i>value</i> ����� <code>null</code>, �� ���������� 
         * <code>null</code>.<br>
         * ���� <i>value</i> �������� ����� <code>Boolean</code>, �� ������������ 
         * ��� ��������������.<br>
         * ���� <i>value</i> ��� ������� �� ���������, �� 
         * ����������� <code>Boolean.valueOf(value.toString())</code>.
         */
        @Override
        public Object toColumnType(Object value) {
            if (value instanceof Boolean) {
                return value;
            }
            if (value == null) {
                return null;
            }
            return Boolean.valueOf(value.toString());
        }

        @Override
        protected int compareNotNull(Object o1, Object o2) {
            return ((Boolean) o1).compareTo((Boolean) o2);
        }

        /**
         * ���������� ����� ��������� �������, ������������� ��� �������� 
         * �� ��������� ��� ������, ��������� � ��������.
         * 
         * @return �������� �� ���������. ���� ����� <code>getDefaultValue</code>
         *    ���������� <code>null</code>, �� ������������ <code>null</code>.
         *    � ��������� ������� ������������ ����� ��������� ���� 
         *   <code>java.lang.Boolean</code>, ����� <code>booleanValue</code> ��������
         *   ����� �������� <code>getDefaultValue().booleanValue()</code>.
         */
        @Override
        public Object blankValueInstance() {
            Object obj = getDefaultValue();
            if (obj == null) {
                return null;
            }
            return new Boolean(((Boolean) obj).booleanValue());
        }
    }

    /**
     * ����� ��������� ����������� ����� {@link tdo.DataColumn} ��� �������������
     * � ������� ���� <code>java.util.Date</code>. 
     * Sql-��� DATE, ������������ ������� <code>java.sql.Types</code> �������������
     * ���� <code>java.util.Date</code>.
     */
    public static class PDBDateColumn extends DataColumn implements Cloneable, Serializable {

        /**
         * �������������� �������� ����� � �������, ����������� ��� ������.
         * <code><pre>
         *   setType(java.util.Date.class);
         *   setSqlType(java.sql.Types.DATE);            
         *   this.setDefaultValue(new java.util.Date(0));
         * </pre></code>
         */
        public PDBDateColumn() {

            this.setType(java.util.Date.class);
            this.setSqlType(java.sql.Types.DATE);
            this.setDefaultValue(new java.util.Date(0));
        }

        /**
         * @return ���������� �������� ���� ������ ������ <code>java.util.Date</code>.
         */
        @Override
        public Class getType() {
            return java.util.Date.class;
        }

        /**
         * @inherited
         */
        @Override
        public synchronized Object clone() {
            PDBDateColumn obj = (PDBDateColumn) super.clone();

            return obj;
        }

        @Override
        protected int compareNotNull(Object o1, Object o2) {
            java.util.Date d1 = (java.util.Date) o1;
            java.util.Date d2 = (java.util.Date) o2;

            int result = 0;
            if (d1.equals(d2)) {
                result = 0;
            }
            if (d1.before(d2)) {
                result = -1;
            }
            if (d1.after(d2)) {
                result = 1;
            }
            return result;

        }

        /**
         * ���������� ����� ��������� �������, ������������� ��� �������� 
         * �� ��������� ��� ������, ��������� � ��������.
         * 
         * @return �������� �� ���������. ���� ����� <code>getDefaultValue</code>
         *    ���������� <code>null</code>, �� ������������ <code>null</code>.
         *    � ��������� ������� ������������ ����� ��������� ���� 
         *   <code>java.util.Date</code>, ����� <code>getTime()</code> ��������
         *   ����� �������� <code>getDefaultValue().getTime()</code>.
         */
        @Override
        public Object blankValueInstance() {
            Object obj = getDefaultValue();
            if (obj == null) {
                return null;
            }
            return new Date(((Date) obj).getTime());
        }

        /**
         * ����������� �������� ��������� � ��� <code>java.util.Date</code>.
         * @param value ������������� ��������
         * @return �������� ���� <code>java.util.Date</code>.          
         * ���� <i>value</i> ����� <code>null</code>, �� ���������� 
         * <code>null</code>.<br>
         * ���� <i>value</i> �������� ����� <code>Date</code>, �� ������������ 
         * ��� ��������������.<br>
         * ���� <i>value</i> �������� ����� <code>Timestamp</code>, �� ������������ 
         * ����� ���������, ��������� value.getTime().<br>
         * ���� <i>value</i> �������� ����� <code>Number</code>, �� ������������ 
         * ����� ���������, ��������� value.longValue().<br>
         * ���� <i>value</i> ��� ������� �� ���������, �� ��� �������� 
         * ������������� �������� ������������ ������� �������� ���� ��
         * ��������� value.toString(). ��� ���� �������� ���������� 
         * <code>java.text.ParseException</code>.
         */
        @Override
        public Object toColumnType(Object value) {
            if (value == null) {
                return null;
            }
            if (value instanceof Date) {
                return value;
            }
            if (value instanceof Timestamp) {
                Timestamp ts = (Timestamp) value;
                return new Date(((Timestamp) value).getTime());
            }

            Object r; // = null;

            if (value instanceof Number) {
                r = new Date(Long.valueOf(((Number) value).longValue()));
            } else {
                r = dateValueOf(value.toString()); //may be NumberFormatException

            }
            return r;
        }

        private Date dateValueOf(String value) {

            String s = value;
            if ( value.contains(":")) {
                s = value.substring(0,9);
            }
            java.sql.Date d;//My 06.03.2012 = null;
            try {
                 d = java.sql.Date.valueOf(s);
            } catch (Exception e) {
                return new Date(0);
            }
            return new java.util.Date(d.getTime());

        }
    }//class PDBDateColumn

    /**
     * ����� ��������� ����������� ����� {@link tdo.DataColumn} ��� �������������
     * � ������������� �������, ����� ����������������. 
     * Sql-��� OTHER, ������������ ������� <code>java.sql.Types</code> �������������
     * ���� <code>java.lang.Object</code>.
     */
    public static class PDBJavaObjectColumn extends DataColumn implements Cloneable, Serializable {
        private Class instanceType;
        /**
         * �������������� �������� ����� � �������, ����������� ��� ������.
         * <code><pre>
         *   setType(Object.class);
         *   setSqlType(java.sql.Types.OTHER);            
         *   this.setDefaultValue(new Object());
         * </pre></code>
         */
        public PDBJavaObjectColumn() {
            this.setType(Object.class);
            this.setSqlType(java.sql.Types.OTHER);
            this.setDefaultValue(new Object());
            this.instanceType = Object.class;
        }

        /**
         * ������������� ����� �������� �� ��������� ��� ������, ��������� � ��������.
         * ���������� ��������� �������� ������ ��� <code>java.lang.Object</code>.
         * @throws IllegalArgumentException ����� ����� �������� ����� 
         *          <code>null</code> ��� ����� defaultValue.getClass() �� �����
         *          <code>java.lang.Object</code>.
         */
        @Override
        public void setDefaultValue(Object defaultValue) {
            //if (defaultValue == null || ! defaultValue.getClass().equals(instanceType)) {
            if (defaultValue == null ) {            
                throw new IllegalArgumentException("Column defaultValue class must be java.lang.Object");
            }
            super.setDefaultValue(defaultValue);
        }

        public Class getInstanceType() {
            return this.instanceType;
        }
        
        public void setInstanceType(Class type) {
            this.instanceType = type;
        }

        /**
         * 
         * @param type
         */
        @Override
        public void setType(Class type) {
            super.setType(Object.class);
        }

        /**
         * @inherited
         */
        @Override
        public synchronized Object clone() {
            return (PDBJavaObjectColumn) super.clone();
        }

        /**
         * 
         * @param obj ������������� ������
         * @return ������, �������� ���������� ��� ��������������.
         */
        @Override
        public Object toColumnType(Object obj) {
            return obj;
        }

        /**
         * ���������� ����� ��������� �������, ������������� ��� �������� 
         * �� ��������� ��� ������, ��������� � ��������.
         * 
         * @return �������� �� ���������. ���� ����� <code>getDefaultValue</code>
         *    ���������� <code>null</code>, �� ������������ <code>null</code>.
         *    � ��������� ������� ������������ ����� ��������� ���� 
         *    <code>java.lang.Object</code>.
         */
        @Override
        public Object blankValueInstance() {
            Object obj = getDefaultValue();

            if (obj == null) {
                return null;
            }
            if ( this.instanceType == null )
                return new Object();
            else 
                try {
                    return this.instanceType.newInstance();
                } catch(Exception e) {
                    return new Object();
                }
        }
    }//class PDBJavaObjectColumn

    /**
     * ����� ��������� ����������� ����� {@link tdo.DataColumn} ��� �������������
     * � ������� ���� <code>java.lang.String</code>. 
     * Sql-��� VARCHAR � CHAR, ������������ ������� <code>java.sql.Types</code> �������������
     * ���� <code>java.lang.String</code>.
     */
    public static class PDBStringColumn extends DataColumn implements Cloneable, Serializable {

        /**
         * �������������� �������� ����� � �������, ����������� ��� ������.
         * <code><pre>
         *   setType(String.class);
         *   setSqlType(java.sql.Types.CHAR);            
         *   this.setDefaultValue("");
         * </pre></code>
         */
        public PDBStringColumn() {
            this.setType(String.class);
            this.setSqlType(java.sql.Types.CHAR);
            this.setDefaultValue("");
        }

        /**
         * @return ���������� �������� ���� ������ ������ <code>java.lang.String</code>.
         */
        @Override
        public Class getType() {
            return String.class;
        }

        /**
         * 
         * @param obj ������������� � ������ ������ ������.
         * @return ���� �������� ��������� ����� <code>null</code>, �� ������������
         *    ������ ������.
         */
        /*        public String toString(Object obj) {
        if (obj == null) {
        return "";
        }
        return (String) obj;
        }
         */
        /**
         * ����������� �������� ��������� � ��� <code>java.lang.String</code>.
         * @param value ������������� ��������
         * @return ���� <i>value</i> ����� <code>null</code>, �� ���������� 
         * <code>null</code>. � ��������� ������ ��������� 
         *   <code>value.toString()</code>.
         */
        @Override
        public Object toColumnType(Object value) {
            return value == null ? null : value.toString();
        }

        /**
         * @inherited
         */
        @Override
        public synchronized Object clone() {
            return (PDBStringColumn) super.clone();
        }

        /**
         * ���������� ����� ��������� �������, ������������� ��� �������� 
         * �� ��������� ��� ������, ��������� � ��������.
         * 
         * @return �������� �� ���������. ���� ����� <code>getDefaultValue</code>
         *    ���������� <code>null</code>, �� ������������ <code>null</code>.
         *    � ��������� ������� ������������ ����� ��������� ���� 
         *   <code>java.lang.String</code> �� ��������� ������ 
         *   ��������, ������������� ������� <code>getDefaultValue()</code>.
         */
        @Override
        public Object blankValueInstance() {
            Object obj = getDefaultValue();
            if (obj == null) {
                return null;
            }
            //My 06.03.2012return new String((String) obj);
            return obj.toString();
        }

        @Override
        protected int compareNotNull(Object o1, Object o2) {
            return ((String) o1).compareTo((String) o2);
        }
    }//class PDBStringColumn

    /**
     * ����� ��������� ����������� ����� {@link tdo.DataColumn} ��� �������������
     * � ������� ���� <code>java.sql.Time</code>. 
     * Sql-��� TIME, ������������ ������� <code>java.sql.Types</code> �������������
     * ���� <code>java.sql.Time</code>.
     */
    public static class PDBTimeColumn extends DataColumn implements Cloneable, Serializable {

        /**
         * �������������� �������� ����� � �������, ����������� ��� ������.
         * <code><pre>
         *   setType(java.sql.Time.class);
         *   setSqlType(java.sql.Types.TIME);            
         *   this.setDefaultValue(new Time(0));
         * </pre></code>
         */
        public PDBTimeColumn() {
            this.setType(java.sql.Time.class);
            this.setSqlType(java.sql.Types.TIME);
            this.setDefaultValue(new Time(0));
        }

        /**
         * @return ���������� �������� ���� ������ ������ <code>java.sql.Time</code>.
         */
        @Override
        public Class getType() {
            return Time.class;
        }

        /**
         * @inherited
         */
        @Override
        public synchronized Object clone() {
            PDBTimeColumn obj = (PDBTimeColumn) super.clone();
            return obj;
        }

        @Override
        protected int compareNotNull(Object o1, Object o2) {
            Time d1 = (Time) o1;
            Time d2 = (Time) o2;

            int result = 0;
            if (d1.equals(d2)) {
                result = 0;
            }
            if (d1.before(d2)) {
                result = -1;
            }
            if (d1.after(d2)) {
                result = 1;
            }
            return result;

        }

        /**
         * ���������� ����� ��������� �������, ������������� ��� �������� 
         * �� ��������� ��� ������, ��������� � ��������.
         * 
         * @return �������� �� ���������. ���� ����� <code>getDefaultValue</code>
         *    ���������� <code>null</code>, �� ������������ <code>null</code>.
         *    � ��������� ������� ������������ ����� ��������� ���� 
         *   <code>java.sql.Time</code>, ����� <code>getTime()</code> ��������
         *   ����� �������� <code>getDefaultValue().getTime()</code>.
         */
        @Override
        public Object blankValueInstance() {
            Object obj = getDefaultValue();
            if (obj == null) {
                return null;
            }
            return new Time(((Time) obj).getTime());
        }
    }

    /**
     * ����� ��������� ����������� ����� {@link tdo.DataColumn} ��� �������������
     * � ������� ���� <code>java.sql.Timestamp</code>. 
     * Sql-��� TIMESTAMP, ������������ ������� <code>java.sql.Types</code> �������������
     * ���� <code>java.sql.Timestamp</code>.
     */
    public static class PDBTimestampColumn extends DataColumn implements Cloneable, Serializable {

        /**
         * �������������� �������� ����� � �������, ����������� ��� ������.
         * <code><pre>
         *   setType(java.sql.Timestamp.class);
         *   setSqlType(java.sql.Types.TIMESTAMP);            
         *   this.setDefaultValue(new Timestamp(0));
         * </pre></code>
         */
        public PDBTimestampColumn() {
            this.setType(Timestamp.class);
            this.setSqlType(java.sql.Types.TIMESTAMP);
            this.setDefaultValue(new Timestamp(0));
        }

        /**
         * @return ���������� �������� ���� ������ ������ <code>java.sql.Timestamp</code>.
         */
        @Override
        public Class getType() {
            return Timestamp.class;
        }

        /**
         * @inherited
         */
        @Override
        public synchronized Object clone() {
            PDBTimestampColumn obj = (PDBTimestampColumn) super.clone();
            return obj;
        }

        /**
         * @inherited
         */
        @Override
        protected int compareNotNull(Object o1, Object o2) {
            Timestamp d1 = (Timestamp) o1;
            Timestamp d2 = (Timestamp) o2;

            int result = 0;
            if (d1.equals(d2)) {
                result = 0;
            }
            if (d1.before(d2)) {
                result = -1;
            }
            if (d1.after(d2)) {
                result = 1;
            }
            return result;

        }

        /**
         * ���������� ����� ��������� �������, ������������� ��� �������� 
         * �� ��������� ��� ������, ��������� � ��������.
         * 
         * @return �������� �� ���������. ���� ����� <code>getDefaultValue</code>
         *    ���������� <code>null</code>, �� ������������ <code>null</code>.
         *    � ��������� ������� ������������ ����� ��������� ���� 
         *   <code>java.sql.Timestamp</code>, ����� <code>getTime()</code> ��������
         *   ����� �������� <code>getDefaultValue().getTime()</code>.
         */
        @Override
        public Object blankValueInstance() {
            Object obj = getDefaultValue();
            if (obj == null) {
                return null;
            }
            return new Timestamp(((Timestamp) obj).getTime());
        }

        /**
         * ����������� �������� ��������� � ��� <code>java.sql.Timestamp</code>.
         * @param value ������������� ��������
         * @return �������� ���� <code>java.sql.Timestamp</code>.          
         * ���� <i>value</i> ����� <code>null</code>, �� ���������� 
         * <code>null</code>.<br>
         * ���� <i>value</i> �������� ����� <code>Timestamp</code>, �� ������������ 
         * ��� ��������������.<br>
         * ���� <i>value</i> �������� ����� <code>Date</code>, �� ������������ 
         * ����� ���������, ��������� value.getTime().<br>
         * ���� <i>value</i> �������� ����� <code>Number</code>, �� ������������ 
         * ����� ���������, ��������� value.longValue().<br>
         * ���� <i>value</i> ��� ������� �� ���������, �� ��� �������� 
         * ������������� �������� ������������ ������� �������� ���� ��
         * ��������� value.toString(). ��� ���� �������� ���������� 
         * <code>java.text.ParseException</code>.
         */
        @Override
        public Object toColumnType(Object value) {
            if (value == null) {
                return null;
            }

            if (value instanceof Timestamp) {
                return value;
            }
            if (value instanceof Date) {
                Timestamp ts = (Timestamp) value;
                return new Timestamp(((Date) value).getTime());
            }

            Object r; // = null;

            if (value instanceof Number) {
                r = new Date(Long.valueOf(((Number) value).longValue()));
            } else {
                r = timestampValueOf(value.toString()); //may be ParseException

            }
            return r;
        }

        private Timestamp timestampValueOf(String value) {

            String s = value;
            if ( ! value.contains(":")) {
                s += " 00:00:00";
            }
            Timestamp ts;//My 06.03.2012 = null;
            try {
                 ts = Timestamp.valueOf(s);
            } catch (Exception e) {
                return new Timestamp(0);
            }
            return ts;

        }
    }//class PDBTimestampColumn
    
    /**
     * ����� ��������� ����������� ����� {@link tdo.DataColumn} ��� �������������
     * � ������� ���� <code>java.sql.Timestamp</code>. 
     * Sql-��� TIMESTAMP, ������������ ������� <code>java.sql.Types</code> �������������
     * ���� <code>java.sql.Timestamp</code>.
     */
    public static class PDBListColumn extends PDBJavaObjectColumn implements Cloneable, Serializable {

        /**
         * �������������� �������� ����� � �������, ����������� ��� ������.
         * <code><pre>
         *   setType(java.sql.Timestamp.class);
         *   setSqlType(java.sql.Types.TIMESTAMP);            
         *   this.setDefaultValue(new Timestamp(0));
         * </pre></code>
         */
        public PDBListColumn() {
            this.setType(List.class);
            this.setSqlType(java.sql.Types.OTHER);
            this.setDefaultValue(new ArrayList());
        }

        /**
         * @inherited
         */
        @Override
        public synchronized Object clone() {
            PDBListColumn obj = (PDBListColumn) super.clone();
            return obj;
        }

        /**
         * @inherited
         */
        @Override
        protected int compareNotNull(Object o1, Object o2) {
            List d1 = (List) o1;
            List d2 = (List) o2;

            int result = -1;
            if (d1.equals(d2)) {
                result = 0;
            }
            return result;

        }

        /**
         * ���������� ����� ��������� �������, ������������� ��� �������� 
         * �� ��������� ��� ������, ��������� � ��������.
         * 
         * @return �������� �� ���������. ���� ����� <code>getDefaultValue</code>
         *    ���������� <code>null</code>, �� ������������ <code>null</code>.
         *    � ��������� ������� ������������ ����� ��������� ���� 
         *   <code>java.sql.Timestamp</code>, ����� <code>getTime()</code> ��������
         *   ����� �������� <code>getDefaultValue().getTime()</code>.
         */
        @Override
        public Object blankValueInstance() {
            Object obj = getDefaultValue();
            if (obj == null) {
                return null;
            }
            if ( this.getInstanceType() != null ) {
                try {
                    return this.getInstanceType().newInstance();
                } catch(Exception e) {

                }
            }
            return new ArrayList();
        }

        /**
         * ����������� �������� ��������� � ��� <code>java.sql.Timestamp</code>.
         * @param value ������������� ��������
         * @return �������� ���� <code>java.sql.Timestamp</code>.          
         * ���� <i>value</i> ����� <code>null</code>, �� ���������� 
         * <code>null</code>.<br>
         * ���� <i>value</i> �������� ����� <code>Timestamp</code>, �� ������������ 
         * ��� ��������������.<br>
         * ���� <i>value</i> �������� ����� <code>Date</code>, �� ������������ 
         * ����� ���������, ��������� value.getTime().<br>
         * ���� <i>value</i> �������� ����� <code>Number</code>, �� ������������ 
         * ����� ���������, ��������� value.longValue().<br>
         * ���� <i>value</i> ��� ������� �� ���������, �� ��� �������� 
         * ������������� �������� ������������ ������� �������� ���� ��
         * ��������� value.toString(). ��� ���� �������� ���������� 
         * <code>java.text.ParseException</code>.
         */
        @Override
        public Object toColumnType(Object value) {
           return value;
        }
        @Override
        public boolean copy(Object source, Object target) {
            if ( target == null && source == null )
                return false;
            List t;//My 06.03.2012 = null;
            if ( target == null && source != null ) {
                try {
                    t = (List) source.getClass().newInstance();
                } catch(Exception e ) {
                    return false;
                }
            } else {
                t = (List)target;
            }
            t.clear();
            t.add((List)source);
            return true;
             
        }        
    }//class PDBListColumn
    
    } //class DataColumn