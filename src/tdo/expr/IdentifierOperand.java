/*
 * IdentifierOperand.java
 */
package tdo.expr;



import tdo.NamedValues;
import tdo.tools.scanner.ExpressionException;

/**
 * ������������ ���������������� ���������, �������������� � ����������
 * ��� �������������. <p>
 * � ���������� ������������� ����� ������: <br>
 * <b>[</b><i>alias.</i><b>]</b><i>���</i>
 * <p>
 *  <i>�����</i> ������������, ���� ����� ���������-���������������
 * ����������� ������ ������������� ����. ��������, �������� �����������
 * ���� ������ ���� <code>tdo.Table</code> ������� �������� � ����� ��
 * ���� ������ ����������� <i>��� �������</i> �������, ��� ���� ������������
 * �����.
 * <p> ����� - ��� �������������, �.�. ������������������ ���������
 * ����, ���� � ����� �������������. ������ �������� ������ ���� ����� ��� ����
 * �������������.
 * <p>�������������� ���������� � �������  ������������� ������� ���������
 * � {@link tdo.DefaultExpressionContext}.
 * 
 */
public class IdentifierOperand extends NamedOperand{
    /**
     * ������ �������� ������.
     */
    protected String alias;
    //protected int columnIndex;
    /**
     * ������� ��������� ������ �� �������� ����� ������ <code>null</code> �
     * ��������� ������ ������ <code>null</code>.
     */
    public IdentifierOperand() {
        this(null,(String)null);
    }
    /**
     * ������� ��������� ������ � �������� ������ � ��������� ������ ������
     * <code>null</code>.
     */
    public IdentifierOperand(String columnName) {
        this(columnName,(String)null);
    }
    /**
     * ������� ��������� ������ � �������� ������, ��������� ������ ������
     * <code>null</code> � �������� ���������� ���������.
     */
    public IdentifierOperand(String columnName,ExpressionContext context) {
        this(columnName,(String)null);
        this.setContext(context);
    }
    /**
     * ������� ��������� ������ � �������� ������ � �������� ��������� ������.
     */
    public IdentifierOperand(String columnName, String alias) {
        //super(columnName,alias);
        super(columnName);
        this.alias = alias;
        //columnIndex = -1;
    }
    /**
     * ���������� �������� ���������.
     * ���������� ���������� ������ {@link #getValue(NamedValues)} �� ���������
     * ��������� ������ <code>null</code>.
     * @return �������� ���������
     */
    @Override
    public Object getValue() {
        NamedValues row = null;
        return this.getValue(row);
    }
    /**
     * ���������� �������� ��������� ��� �������� ��������� ������������ ��������.
     *
     * @param values ��������� ����������� ��������.
     * @return �������� ���������
     * @throws ExpressionException ���� ��������� <code>values</code> ��
     *  �������� �������� � ������, ���������� {@link #getName() }
     * @see #getValue(tdo.NamedValues[])
     * @see #getValue()
     */
    @Override
    public Object getValue(NamedValues values) {
        Object value = null;
        try {
            value = values.getValue(getName());
        } catch(Exception e) {
            throw new ExpressionException("IdentifierOperand.getValue(NamedValues) : identifier {" + getName() + "} doesn't exist" );
        }
        return value;
    }    
    /**
     * ���������� �������� ��������� ��� ��������� ������� ���������
     * ������������ ��������.
     *
     * @param values ������ ��������� ����������� ��������.
     * @return �������� ���������
     * @throws ExpressionException ���� ��������� <code>values</code> ��
     *  �������� �������� � ������, ���������� {@link #getName() }
     * @see #getValue(tdo.NamedValues)
     * @see #getValue()
     */
    @Override
    public Object getValue(NamedValues[] values) {
        NamedValues r = context.getNamedValues(values,getAlias());
        Object value = null;
        try {
            value = r.getValue(getName());            
        } catch(Exception e) {
            throw new ExpressionException("IdentifierOperand.getValue(NamedValues[]) : identifier {" + getName() + "} doesn't exist" );
        }
        return value;
    }
    /**
     * ���������� ����� ��������, ��������������� ��������������.
     * ����� �������� ����� �������� ������� ������
     * <code>tdo.expr.ExpressionContext.getIdentifierType</code>,
     * ��������� ��� ��� � ����� � �������� ����������. ���� ����� ���������
     * ������ �������� <code>null</code>, �� ������������
     * <code>IdentifierOperand.class</code>, ����� �������� ��� ���������,
     * ����������� �������� ���� <code>tdo.DefaultExpressionContext</code>,
     * ������������ � ���� ������������.
     * @return <code>IdentifierOperand.class</code> ��� ����� ��������,
     *   ��������������� ��������������.
     * @see tdo.expr.ExpressionContext#getIdentifierType(java.lang.String, java.lang.String)
     * @see tdo.DefaultExpressionContext
     */
    @Override
    public Class getType() {
       Class c = context.getIdentifierType(getName(),getAlias() );
       return c == null ? IdentifierOperand.class : c;
    }
    /**
     * @return �������� -1, ��������� �������� ������� ��� ������ ����,
     *     ���������� ��� ����������, �� �� ��� ���������
     */
    @Override
    public int getPriority() {
        return -1;
    }
    /**
     * ���������� ����� ��������������.
     * @return ����� ��������������
     * @see #setAlias(java.lang.String)
     */
    public String getAlias() {
        return alias;
    }
    /**
     * ������������� �������� ������ ��������������.
     * @see #getAlias()
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }
    /**
     * ���������� ������ ������, �������������� �������������.
     * @return �������� <code>getName()</code>, ���� ��������
     * ������ ����� <code>null</code>, ���
     *  <code>getAlias() + "." + getName() � ��������� ������
     */
    @Override
    public String toString() {
        String s = "";
        if ( alias != null )
            s = alias + ".";
        s += this.getName();
        return s;
    }

}// class IdentifierOperand
