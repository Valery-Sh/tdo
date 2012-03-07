package tdo;

import java.util.*;
import tdo.expr.*;
import tdo.service.TableServices;
import tdo.tools.scanner.ExpressionException;
//import tdo.tools.scanner.ExpressionException;

/**
 * ���������� ��������� ���������, ������������ � ��������� 
 * <code>tdo.DataTable</code>. 
 * <p>
 * ������������� ������ ��� ��������, ���������, �������� � ���������� ���������,
 * ����������� ������� ����� ���� ��������, ����� ������� �������, ��������� � 
 * �������.
 * <p>
 *  <p>����� createExpression() ������ ��� �������� ������� ���� 
 *  {@link tdo.expr.IExpression}. ����� �������� ��������� ��������� ����� ����
 *  ���������:
 * <ul>
 *   <li>���������� ������� ��������� ������ � ��������� ��� ��������� �������� 
 *       ���������.
 *   </li>
 *   <li>���������� ��������� ����� <code>createExpression()</code> � ����������
 *       ���������. ����� ����������� ��������� � ���������� ���������, ��� 
 *       ������ ����  <code>IExpression</code>  
 *   </li>
 *   <li>��� ��������� �������� ��������� ���������� ��������� ���� �� �������
 *       <code>getValue</code> ����������� ���������.
 *   </li>
 * </ul>
 * <p>����� ����� ���� ����������� ��� ���������� ��� ������ 
 * <code>tdo.Table</code>, ��������:
 * 
 *  <p><b>������ 1.</b> 
 * <code>
 *   <pre>
 *      ExpressionContext context = new DefaultExpressionContext("(2/3 + 7)*8");
 *      IExpression expr = context.createExpression();
 *      System.out.println("������ 1 ���������: " + expr.getValue());
 * 
 *   </pre>
 * </code>
 * ��������� �� �������:
 * 
 * <pre>������ 1 ���������: 61.333333333333336</pre>
 * 
 * <p>��� ������ � ��������� <code>tdo.Table</code> ����� �������������� ����
 * ��� ��������� ������. ����� ������� ������������ �������, ���������� ���������
 * ����� {@link #addTable(tdo.Table)} ��� {@link #addTable(String,tdo.Table)}.
 * 
 *  <p><b>������ 2.</b> 
 * <code>
 *   <pre>
 *      DataTable dt = new DataTable();
 *      dt.getColumns().add(String.class,"firstName");
 *      dt.getColumns().add(Integer.class,"age");
 *      dt.getColumns().add(Double.class,"salary");
 *      dt.addRow(new Object[] {"Tom", 30, 2000});
 *      dt.addRow(new Object[] {"Bill", 25, 3200});
 *      DefaultExpressionContext context = new DefaultExpressionContext("salary * 12");
 *      context.addTable(dt);
 * 
 *      IExpression expr = context.createExpression();
 *      System.out.println("������ 2.1 ���������: " + expr.getValue(dt.getRow(0)));
 *      System.out.println("������ 2.2 ���������: " + expr.getValue(dt.getRow(1)));
 * 
 *   </pre>
 * </code>
 * ��������� �� �������:
 * 
 * <pre>
 * ������ 2.1 ���������: 24000.0
 * ������ 2.2 ���������: 38400.0
 * </pre>
 * 
 * <p> �������� ��������� ������������� ��������, ������������� � ��������������
 *   ��������� ������, ����������� ���������� java-��� �������� 
 *   ���������-���������������. ���, ��� ����������� ���� ������, 
 *   �������������� ��������������� ����� ���� ����������� �����:
 *      <pre> <code>
 * 	Class getIdentifierType(String idName, String alias);
 *      </code></pre>
 *	���������� � ���� ��������� ��������� ������� ��������� � �� ������ 
 *   ������ �������� ������. �� ������ ������ ��������� ��������� ��������� 
 *   ����� ��������� ������, ���������� � ������� ������������� �������� 
 *   ��������� � �������������� ������ <code>addError</code>. ����� ��������� 
 *   ���������, ���������� ����� ���������� ���� �� ������ � ���������������� 
 *   �� �������, ������� ������ �������� ���� 
 *   {@link tdo.expr.ErrorItem} ������� {@link #getErrorList() }.
 * <p>�������� ��������� ������������ � ��������� ��������������-���������.
 * 
 *  <p><b>������ 3.</b> 
 * <code>
 *   <pre>
 * 
 *      ExpressionContext context = new DefaultExpressionContext("NumberFormat( (:divisible/:divisor + 7)*8,'###.00') ");
 *      context.setParameter("divisible",2);
 *      context.setParameter("divisor",3);
 *      IExpression expr = context.createExpression();
 *      System.out.println("������ 3.1 ���������: " + expr.getValue());
 * 
 *      context.setParameter("divisible",7);
 *      context.setParameter("divisor",2);
 *      System.out.println("������ 3.2 ���������: " + expr.getValue());
 * 
 *   </pre>
 * </code>
 * ��������� �� �������:
 * 
 * <pre>������ 3.1 ���������: 61.33</pre>
 * <pre>������ 3.2 ���������: 84.00</pre>
 * 
 * <p>��������� ����� ��������� �������������� � ��������������� <i>��������</i>.
 * ��������, ��� ��������� ������ �� ���������� ������, ��� ����������� 
 * ������������� ����� ������� ��� ��� ����� ������:
 *   <code>
 *    <pre>
 *          <�����.<���-�������>
 *   </pre>
 *   </code>
 * ��������, " a.firstName = 'Bill' and b.Department = 'other' ". � ���� 
 * ��������� ������ 'firstName' � 'Department' ������������ ������ <b>a</b> �
 * <b>b</b> ��������������.
 * <p>�������� ��������� ������������ ������, ������������ ������, ����������
 * �������� ������ � �������� ����������.
 * <p>���������� ��������� ������:
 *  ���� ��� �������:
 * <ol>
 *   <li>�������, ���������� �������� � ����������� � �� �������� � ��������;</li>
 *   <li>�������, ���������� �������� � ������ ����� ��������� ����� ��
 *       ��������� � �������.</li>
 * </ol>
 *  ��������� ������� ������������ ��� �� ������ ������� � ����� ��� �� ������
 * � ��������� �������� ���������� � ��������������� ������.
 * <code><pre> 
 *     // ***** ���������� *****
 *     DataTable dt = new DataTable();
 *     dt.getColumns().add(String.class,"Name");
 *     dt.getColumns().add(Integer.class,"age");
 *     dt.getColumns().add(Double.class,"salary");
 *     dt.addRow(new Object[] {"Tom", 30, 2000});
 *     dt.addRow(new Object[] {"Bill", 25, 3200});
 * 
 *     // ***** ����� ����� *****
 *     DataTable dt1 = new DataTable();
 *     dt1.getColumns().add(String.class,"Country");
 *     dt1.getColumns().add(Double.class,"DollarRate");
 *     dt1.addRow(new Object[] {"JPN", 1000});
 *     dt1.addRow(new Object[] {"RUS", 23.54});
 * 
 *     DefaultExpressionContext context = new DefaultExpressionContext(
 *             "c.salary * r.dollarRate");
 *     // ��������� � �������� ������� ����������� � ������� 'c'. 
 *     context.addTable("c",dt);
 *     // ��������� � �������� ������� ������ ����� � ������� 'r'. 
 *     context.addTable("r",dt1);
 *
 *     IExpression expr = context.createExpression();
 *     // ����� ��������� ��������� ��� ���� ������, ���������� ��������� ��� 
 *     // ��������� expr ����� � ����������, ���������� �������� �����
 *     DataRow[] rows = new DataRow[2];
 *     rows[0] = dt.getRow(0);  // �������� 0-� ��� �� ����. �����������
 *     rows[1] = dt1.getRow(0); // �������� 0-� ��� �� ����. ������ �����
 *     
 *     System.out.println("������ 3.1 ���������: " + expr.getValue(rows) );
 *     
 *     rows[0] = dt.getRow(0); // �������� 0-� ��� �� ����. �����������
 *     rows[1] = dt1.getRow(1);// �������� 1-� ��� �� ����. ������ �����
 * 
 *     System.out.println("������ 3.2 ���������: " + expr.getValue(rows) );
 * </pre></code>
 * �� �������:
 * <pre> 
 * ������ 3.1 ���������: 2000000.0
 * ������ 3.2 ���������: 47080.0
 * </pre>
 * 
 * <p>����� ����� ���� ������ ��� ��������� � ���� ������������. ��� ����� 
 * ������ �������� <code>testMode</code>. ���� �������� �������� ����������� � 
 * <code>true</code> �� ������ <code>getIdentifierType</code> � 
 * <code>getParameterType</code> �� ����������� ����������, � 
 * ���������� <code>null</code> ��������, ��� ����� �������� ������������ 
 * ��������� ��� �������� ����� �������. ����������, ���� ����� 
 * <code>getTable</code> ���������� <code>null</code>, �� ��������� ��������
 * ��������� �� ��������. ����� ������������� ������ ������� � ��������: 
 * {@link #isTestMode() } � {@link #setTestMode(boolean) }. ����� ����, 
 * ��������������� ������������ ��� �������� ��������� � �������� ����.
 *  
 * 
 */
public class DefaultExpressionContext implements ExpressionContext {
    protected NullValue nullValue = new NullValue();
    private boolean testMode;
    //private Table table;
//    private Hashtable tables;
    private Hashtable params;
    private Map<String,TableServices> tableServices;

    /**
     * ������ ��������� ���������
     */
    private String expressionString;
    /**
     * ������ ������, ���������� �������� {@link ErrorItem}.
     */
    private Vector errorList;
    /**
     * ���� ��� ���������� ������� � ��������� ����������� ����� 
     * {@link #addTable(tdo.Table) } ��� ������ �������� ������, �� ����� 
     * ������������ �������� "__DA__"
     */
    protected String defaultAlias = "__DA__";
    private Vector columnOperands;
    private Vector parameterOperands;

    /**
     * ������� ��������� ������ �� ��������� ���������� ���������
     * ������ <code>null</code>.
     */
    public DefaultExpressionContext() {
        this(null);
    }

    /**
     * ������� ��������� ������ �� ��������� ���������� ���������
     * ������ <code>null</code> � ��������� ��� � ����� ������������.
     * @param testMode true - �������� ���� ������������
     */
    public DefaultExpressionContext(boolean testMode) {
        this(null);
        this.testMode = testMode;
    }

    /**
     * ������� ��������� ������ ��� ��������� ���������� ���������.
     * @param expr ��������������� �������� �������� {@link #expressionString}
     */
    public DefaultExpressionContext(String expr) {
        this.expressionString = expr;
        errorList = new Vector(2);
        columnOperands = new Vector(5);
        parameterOperands = new Vector(5);

        //tables = new Hashtable(2);
        tableServices = new HashMap<String,TableServices>(2);
        params = new Hashtable(5);
        this.testMode = false;
    }

    /**
     * ������� ��������� ������ ��� ��������� ���������� ��������� � ���������
     * ��� � ����� ������������.
     * @param expr ��������������� �������� �������� {@link #expressionString}
     * @param testMode 
     */
    public DefaultExpressionContext(String expr, boolean testMode) {
        this.expressionString = expr;
        errorList = new Vector(2);
        columnOperands = new Vector(5);
        parameterOperands = new Vector(5);

        //tables = new Hashtable(2);
        tableServices = new HashMap<String,TableServices>(2);
        params = new Hashtable(5);
        this.testMode = testMode;
    }

    /**
     * ������� � ��������� ������ ��������� � ���������� ������� � ������� 
     * ������ "__DA__".
     * 
     * ���� ��������� �� �������� ����� ������� 
     * �� ������������ <code>null</code>.
     * 
     * @return ������ Table ��� <code>null</code>
     * @throws NullPointerException ���� ������� � ������� ������ �������� �� 
     *   ��������� ("__DA__") �� ������� � �������� ����� ������������.
     * 
     * @see #isTestMode() 
     * @see #setTestMode
     */
/*    public Table getTable() {
        return getTable(this.defaultAlias);
    }
*/
    public TableServices getTableServices() {
        return getTableServices(this.defaultAlias);
    }
    /**
    /**
     * ������� � ���������� ������� � �������� ������� ������.
     * 
     * ���� ��������� �� �������� ����� ������� �� ������������ <code>null</code>.
     * 
     * @param alias ����� ������� �������
     * @return ������ Table ��� <code>null</code>
     * @throws NullPointerException ���� ��� ��������� ������ ������� �� �������
     *    � �������� ����� ������������.
     * @see #isTestMode() 
     * @see #setTestMode
     */
/*    public Table getTable(String alias) {
        String a = alias == null ? defaultAlias : alias.toUpperCase();
        return (Table) tables.get(a);
    }
*/
    /**
     * ������� � ���������� ������� � �������� ������� ������.
     *
     * ���� ��������� �� �������� ����� ������� �� ������������ <code>null</code>.
     *
     * @param alias ����� ������� �������
     * @return ������ Table ��� <code>null</code>
     * @throws NullPointerException ���� ��� ��������� ������ ������� �� �������
     *    � �������� ����� ������������.
     * @see #isTestMode()
     * @see #setTestMode
     */
    public TableServices getTableServices(String alias) {
        String a = alias == null ? defaultAlias : alias.toUpperCase();
        return tableServices.get(a);
    }

    /**
     * ���������� ��� ������� ��� ��������� ����� ������� � ������ �������.
     * 
     * ���� ��������� �� �������� �������� ������� ������� � ����� ������������ 
     * ��������, �� ������������� ����������. <p>
     * 
     * ���� ��������� �� �������� �������� ������� ������� � ����� ������������ 
     * �������, �� ������������ <code>null</code> ��������. <p>
     * 
     * @param columnName ��� �������, ��������������� �������� <code>name</code>
     *      ������� <cpde>tdo.DataColumn</code>.
     * @param alias ����� �������, � ������� ����������� �������
     * @return ��� �������
     * @see tdo.DataColumn
     * @see #getParameterType(java.lang.String) 
     */
    @Override
    public Class getIdentifierType(String columnName, String alias) {
        TableServices srv = getTableServices(alias);
        if (srv == null && !isTestMode()) {
            throw new NullPointerException("A table with default alias='" + alias + "' is not found");
        } else if (srv == null) {
            return null;
        }
        return srv.getFilterServices().getColumnType(columnName);

    }

/*    public Class getIdentifierType(String columnName, String alias) {
        Table dt = getTable(alias);
        if (dt == null && !isTestMode()) {
            throw new NullPointerException("A table with default alias='" + alias + "' is not found");
        } else if (dt == null) {
            return null;
        }
        return dt.getColumns().get(columnName).getType();
        
    }
*/
    /**
     * ���������� ��� ������� �� ��������� �������.
     * @param rowIndex ������ ����
     * @return ������ ���� <code>tdo.DataRow</code>.
     * @see tdo.NamedValues
     * @see tdo.DataRow
     */
    public NamedValues getRow(int rowIndex) {
        return this.getTableServices().getFilterServices().getRow(rowIndex);
    }

    /**
     * ��� ��������� ���� � ������ ������� �� ��������� ���������� ��� ������ 
     * � �������.
     *
     * ����� ������ ���������� ��� ���������� ����������-�������, �����������,
     * ��������, ���������� ������ ����.
     * 
     * @param row ���, ��� �������� ������������ ������ � �������
     * @return ������ �������� ���� ��� �������� ������ -1, ���� ��� �� ������.
     */
    public int getRowIndex(NamedValues row) {
        //return this.getTable().find((DataRow) row);
        return ((DataRow) row).getIndex();
    }

    /**
     * ��� ��������� ���� � ������ ������� ���������� ������ ���� � �������.
     * ����� ������ ���������� ��� ���������� ����������-�������, �����������,
     * ��������, ���������� ������ ����.
     *
     * @param row ���, ��� �������� ������������ ������ � �������
     * @param alias ����� �������, � ������� ��������� ���
     * @return ������ �������� ���� ��� �������� ������ -1, ���� ��� �� ������.
     */
    public int getRowIndex(NamedValues row, String alias) {
        //return this.getTable(alias).find((DataRow) row);
        return ((DataRow) row).getIndex();
    }

    /**
     * ���������� ��� �������, �������� ������� � ����������� � �������� �������.
     * ��������������� ������ �������� ����� � ������������ ���, ������������� 
     * ������� � �������� �������. ���� �������� ��������� <code>alias</code>
     * ����� <code>null</code>, �� ��� ������ ������������ ����� �� ���������.
     * ���� ������� �� �������, �� ������������� ����������.
     * 
     * @param rows �������� �����, ���������� ������� ���.
     * @param alias ����� �������, ��� ������� ������������.
     * @return ������� ��� ��� <code>null</code>, ���� ��� �� ������
     * @throws NullPointerException ���� �� ������� ������� � �������� �������,
     *   ��� � ������� �� ���������.
     */
    @Override
/*    public NamedValues getNamedValues(NamedValues[] rows, String alias) {
        //String a = alias == null ? null : alias.toUpperCase();
        Table dt = (alias == null) ? getTable() : getTable(alias);
        if (dt == null) {
            dt = getTable();
        }
        if (dt == null) {
            throw new NullPointerException("A table with alias=" + alias + " is not found");
        }
        NamedValues r = null;
        for (int i = 0; i < rows.length; i++) {
            if (((DataRow) rows[i]).getContext().getCoreServices().getTable() == dt) {
                r = rows[i];
                break;
            }
        }
        return r;
    }
*/
    public NamedValues getNamedValues(NamedValues[] rows, String alias) {
        //String a = alias == null ? null : alias.toUpperCase();
        TableServices srv = (alias == null) ? getTableServices() : getTableServices(alias);
        if (srv == null) {
            srv = getTableServices();
        }
        if (srv == null) {
            throw new NullPointerException("A table with alias=" + alias + " is not found");
        }
        NamedValues r = null;
        for (int i = 0; i < rows.length; i++) {
            if (((DataRow) rows[i]).getContext() == srv) {
                r = rows[i];
                break;
            }
        }
        return r;
    }

    /**
     * ���������� �������� ��� ��������� ����� ���������.
     *  <p>���� ��� ��������� ����� <code>"PI"</code>, �� ������������ �������� 
     * ��������� <code>java.math.PI</code>.
     *  <p>���� ��� ��������� ����� <code>"E"</code>, �� ������������ �������� 
     * ��������� <code>java.math.E</code>.
     * 
     * @param row ��� �������. �� ������������
     * @param paramName ��� ��������� (�� ��������������� ���������). �������� 
     *   ����� ���� ������ � ����� ��������
     * @return �������� ���������
     */
    @Override
    public Object getParameterValue(NamedValues row, String paramName) {
        return getParamValueCheckConst(row, paramName);
    }

    private Object getParamValueCheckConst(NamedValues row, String paramName) {
        if (paramName.equals("PI")) {
            return Math.PI;
        }
        if (paramName.equals("E")) {
            return Math.E;
        }
        Object pv = params.get(paramName.toUpperCase());
        if ( pv instanceof NullValue )
            return null;
        else
            return pv;
    }

    private Object getParamValueCheckConst(NamedValues[] row, String paramName) {
        if (paramName.equals("PI")) {
            return Math.PI;
        }
        if (paramName.equals("E")) {
            return Math.E;
        }
        Object pv = params.get(paramName.toUpperCase());
        if ( pv instanceof NullValue )
            return null;
        else
            return pv;

    }

    /**
     * ���������� �������� ��� ��������� ����� ���������.
     *  <p>���� ��� ��������� ����� <code>"PI"</code>, �� ������������ �������� 
     * ��������� <code>java.math.PI</code>.
     *  <p>���� ��� ��������� ����� <code>"E"</code>, �� ������������ �������� 
     * ��������� <code>java.math.E</code>.
     * 
     * @param rows ������ ����� �������. �� ������������
     * @param paramName ��� ��������� (��� ��������������� ���������). �������� 
     *   ����� ���� ������ � ����� ��������
     * @return �������� ���������
     */
    @Override
    public Object getParameterValue(NamedValues[] rows, String paramName) {
        return getParamValueCheckConst(rows, paramName);
    }

    /**
     * ���������� ��� �������� ��������� �� ��� �����.
     * 
     * ���� �������� � �������� ������ �� ���������� � ����� ������������ 
     * ��������, �� ������������� ����������. <p>
     * 
     * ���� �������� � �������� ������ �� ���������� � ����� ������������ 
     * �������, �� ������������ <code>null</code>. <p>

     * @param paramName ��� ��������� (��� ��������������� ���������)
     * @return ��� �������� ��������� 
     */
    @Override
    public Class getParameterType(String paramName) {
        Object pv = params.get(paramName.toUpperCase());
        if (pv == null && !isTestMode()) {
            throw new NullPointerException("Parameter " + paramName + " is not found");
        } else if (pv == null) {
            return null;
        } else {
            return pv.getClass();
        }

    }

    /**
     * ��������� �������� � �������� ������  � ��������� � ���������.
     * @param paramName ��� ��������� (��� ��������������� ���������). ����� ����
     *    ����� � ����� ��������
     * @param value �������� ������ ���������
     */
    @Override
    public void setParameter(String paramName, Object value) {
        if ( value == null )
            params.put(paramName.toUpperCase(), nullValue);
        else
            params.put(paramName.toUpperCase(), value);
    }

    /**
     * ������� �������� � �������� ������  �� ���������.
     * @param paramName ��� ��������� (��� ��������������� ���������). ����� ����
     *    ����� � ����� ��������
     */
    @Override
    public void removeParameter(String paramName) {
        params.remove(paramName.toUpperCase());
    }

    /**
     * ��������� � ��������� ������ ��������� ������� � �������� 
     * �������.
     * 
     * @param alias ����� ����������� �������. �������� �� ����������� � ��������.
     *    ���� ����� <code>null</code>, �� ������������� ����������
     * @param table ����������� �������
     * @throws IllegalArgumentException ���� �������� ����� ����� 
     *   <code>null</code> ��� �������� ������ �������� ��� ������ �������.
     */
/*    public void addTable1(String alias, Table table) {
        if (alias == null || alias.trim().length() == 0) {
            throw new IllegalArgumentException("Parameter 'alias' must have a value");
        }
        this.tables.put(alias.toUpperCase(), table);
    }
*/
    /**
     * ��������� � ��������� ������ ��������� ������� � ��������
     * �������.
     *
     * @param alias ����� ����������� �������. �������� �� ����������� � ��������.
     *    ���� ����� <code>null</code>, �� ������������� ����������
     * @param table ����������� �������
     * @throws IllegalArgumentException ���� �������� ����� �����
     *   <code>null</code> ��� �������� ������ �������� ��� ������ �������.
     */
    public void addTableServices(String alias, TableServices tableServices) {
        if (alias == null || alias.trim().length() == 0) {
            throw new IllegalArgumentException("Parameter 'alias' must have a value");
        }
        this.tableServices.put(alias.toUpperCase(), tableServices);
    }

    /**
     * ��������� � ��������� ������ ��������� �������� ������� � 
     * ������� �� ��������� "__DA__".
     * 
     * @param table ����������� �������
     * @see tdo.Table
     * @see #defaultAlias
     */
/*    public void addTable(Table table) {
        this.addTable(defaultAlias, table);
    }
*/
    /**
     * ��������� � ��������� ������ ��������� �������� ������� �
     * ������� �� ��������� "__DA__".
     *
     * @param table ����������� �������
     * @see tdo.Table
     * @see #defaultAlias
     */
    public void addTableServices(TableServices tableServices) {
        this.addTableServices(defaultAlias, tableServices);
    }

    /**
     * @return ���������� �������� ��������� � ��������� �������
     */
    @Override
    public String getExpressionString() {
        return this.expressionString;
    }

    /**
     * ������������� ����� �������� ��������� � ��������� �������. <p>
     * @param expr ����� �������� ��������� � ��������� �������
     */
    @Override
    public void setExpressionString(String expr) {
        this.expressionString = expr;
    }

    /**
     * ��������� ������� � ����������� �� ������������ � �������� ���������
     * ��������� ������ � ������ ������.
     * @param code ��� ������ - �������� ����� �� ��������, ������������
     *   ����������� {@link tdo.expr.ExpressionContext}
     * @param expr ���������, ��� ��������� �������� ���������� ������
     * @see #addError(int, tdo.expr.IExpression, java.lang.String) 
     * @see tdo.expr.ErrorItem
     */
    @Override
    public void addError(int code, IExpression expr) {
        this.errorList.addElement(new tdo.expr.ErrorItem(code, expr));
    }

    /**
     * ��������� ������� � ����������� �� ������������ � �������� ���������
     * ��������� ������ � ������ ������.
     * @param code ��� ������ - �������� ����� �� ��������, ������������
     *   ����������� {@link tdo.expr.ExpressionContext}
     * @param expr ���������, ��� ��������� �������� ���������� ������
     * @param msg ���������, ���������� �������������� ���������� �� ������
     * @see #addError(int, tdo.expr.IExpression) 
     * @see tdo.expr.ErrorItem
     */
    @Override
    public void addError(int code, IExpression expr, String msg) {
        this.errorList.addElement(new ErrorItem(code, expr, msg));
    }

    /**
     * ���������� ��������� ���������, ���������� ���������� �� �������
     * ��������� ���������.  
     * @return ��������� ���������, ����������� ������
     */
    @Override
    public Vector getErrorList() {
        return this.errorList;
    }

    /**
     * ������� �� ��������� ������� ���������� �� �������, ���� ��� ���������� 
     * ��� ��������� ���������.
     * ������������ ������, ���������� �������� {@link tdo.expr.ErrorItem}.
     */
    @Override
    public void printErrors() {
        System.out.println("*** Error List ***");
        for (int i = 0; i < errorList.size(); i++) {
            ErrorItem er = (ErrorItem) errorList.elementAt(i);
            System.out.println("code=" + er.code);
            System.out.println("---" + (er.expr == null ? "parser" : er.expr.toString()));
            System.out.println("*** " + (er.getMessage()));
        }
    }

    /**
     * ������� �� ��������� ������� ���������� �� ������� �� ��������� ������, 
     * ���� ��� ����������. 
     * ��� ��������� ���������.
     * @see tdo.expr.ErrorItem
     */
    public static void printErrors(Vector errorList) {
        System.out.println("*** Error List ***");
        for (int i = 0; i < errorList.size(); i++) {
            ErrorItem er = (ErrorItem) errorList.elementAt(i);
            System.out.println("code=" + er.code);
            System.out.println("---" + (er.expr == null ? "parser" : er.expr.toString()));
            System.out.println("*** " + (er.getMessage()));
        }
    }

    /**
     * ���������� ��������� ��������, �������������� �������������� ���������.
     * � ��� ��������� ��������������-��������� �� ������.
     * @return ��������� ��������, ���� {@link tdo.expr.IdentifierOperand}. 
     * @see #getParameterOperands() 
     */
    @Override
    public Vector getIdentifierOperands() {
        return this.columnOperands;
    }

    /**
     * ���������� ��������� ��������, �������������� ��������������-��������� 
     * ���������.
     * ��������� ��������� ��� �������������� � �������������� �������� ':' - 
     * ���������.
     * @return ��������� ��������, ���� {@link tdo.expr.ParameterOperand}. 
     * @see #getIdentifierOperands() 
     */
    @Override
    public Vector getParameterOperands() {
        return this.parameterOperands;
    }

    /**
     * ������� � ���������� ��������� ���������� ���������� ���������.
     * @return ��������� ��������� ��������� ��������� � ���� IExpression �������.
     */
    @Override
    public IExpression createExpression() {
        errorList.clear();
        columnOperands.clear();
        parameterOperands.clear();

        ExprParser exprparser = new ExprParser(this, expressionString);
        try {
            exprparser.parse();
        } catch (ExpressionException e) {
            this.addError(EXPRPARSER, new ErrorExpression(this, e));
            throw e;
        }
        Vector rpn = exprparser.getRPN();
        Stack stack = new Stack();

        for (int i = 0; i < rpn.size(); i++) {
            IToken t = (IToken) rpn.elementAt(i);
            if (t instanceof IOperand) {
                stack.push(t);
                if (t instanceof IdentifierOperand) {
                    columnOperands.addElement(t);
                } else if (t instanceof ParameterOperand) {
                    parameterOperands.addElement(t);
                }
            } else {
                IOperator oper = (IOperator) t;
                if (oper.isUnary()) {
                    if (oper instanceof FunctionOperator) {
                        //str = " " + t.toExprString() + " (" + (String)stack.pop() +")";
                        IOperand op1 = (IOperand) stack.pop();
                        stack.push(oper.createExpression(this, op1));
                        continue;
                    } else {
                        IOperand op = (IOperand) stack.pop();
                        stack.push(oper.createExpression(this, op));
                    }
                } else {
                    if (stack.isEmpty()) {
                        this.addError(STRUCTURE, new ErrorExpression(this, oper, null, null));
                        throw new EmptyStackException();
                    }
                    IOperand op2 = (IOperand) stack.pop();
                    if (stack.isEmpty()) {
                        this.addError(STRUCTURE, new ErrorExpression(this, oper, null, op2));
                        throw new EmptyStackException();
                    }

                    IOperand op1 = (IOperand) stack.pop();
                    stack.push(oper.createExpression(this, op1, op2));
                }
            }
        }

        return (IExpression) stack.pop();
    }

    public void printExpr(IExpression e) {
        String s = e.getClass().getName();
        int l = s.lastIndexOf('$');
        if (l >= 0) {
            l++;
        } else {
            l = s.lastIndexOf('.') + 1;
        }
        s = s.substring(l);
        if ( e instanceof AbstractExpression ) {
            System.out.println(s + " : operator='" + ((AbstractExpression)e).getOperator() + "'" );
        } else if ( e instanceof IdentifierOperand ) {
            IdentifierOperand ei = (IdentifierOperand)e;
            s += " : identifier=";
            if ( ei.getAlias() != null ) {
                s += "'" + ei.getAlias() + ".";
            } else {
                s += "'";
            }
            System.out.println(s + ((IdentifierOperand)e).getName() + "'" );
        } else if ( e instanceof ParameterOperand ) {
            System.out.println(s + " : parameter='" + ((ParameterOperand)e).getName() + "'" );
        } else if ( e instanceof LiteralOperand ) {
            System.out.println(s + " : value='" + ((LiteralOperand)e).getValue() + "'" );
        } else {
            System.out.println(s );
        }

    }

    public static Vector test(String expression, TableServices tableServices) {
        Vector el = new Vector(1);
        Object result = null;
        DefaultExpressionContext c = new DefaultExpressionContext(expression);
        IExpression exp = c.createExpression();
        if (tableServices != null) {
            c.addTableServices(tableServices);
        }

        try {
            if (tableServices != null) {
                result = exp.getValue(tableServices.getFilterServices().getRow(0));
            } else {
                result = exp.getValue();
            }
        } catch (Exception e) {
            c.addError(TESTING, exp, e.getMessage());
        }

        if (c.getErrorList().isEmpty()) {
            c.getErrorList().addElement(result);
        }
        return c.getErrorList();
    }

    /**
     * ��������� ���������� ��������� �� ������ � ������ ������������.
     * @return true ��������, ��� ������� ��������� � ������ ������������. 
     *   false - � ��������� ������.
     * @see #setTestMode(boolean) 
     */
    public boolean isTestMode() {
        return this.testMode;
    }

    /**
     * �������� ��� ��������� ����� ������������.
     * @param testMode �������� true ��������, ��� ������� ����������� � �����
     *    ������������. false - ����� ������������ �����������.
     * @see #isTestMode() 
     */
    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

    protected static class NullValue {

    }
}//class DefaultExpressionContext
