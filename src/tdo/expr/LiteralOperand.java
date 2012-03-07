/*
 * LiteralOperand.java
 */

package tdo.expr;

import tdo.NamedValues;

/**
 * ����� ������������ ��� ������������� ��������, ���������� ����������
 * ��������� � �����, ��� �������� �������� �������� � � ��� �����.<p>
 * ������ ��������� ��������� ���������� ����������� �������� �����:
 * <code>Boolean,Float,Double,Integer,Long,String</code>. <p>
 * ����� � �������� ��������� ����������� �������, ��������, ����� ��� ������
 * ������, �� ���������� ��������� ������� ��� ���� ������ ������� ������, �
 * �������� ��������� �� ���������� ���� <code>Object value</code>. ��� ��������,
 * ������������ ������� getType ������������  ��������� ��������. <p>
 * ��� �������� ������������ �������� ����������� ���� �� ���� �������������:
 * ������ ��������� ���� �������� <code><i>value</i></code>. ���� ��������
 * ��������� (� ������������� �������� ��������)  ����� <code>null</code>, ��
 * ��� �������� ����� <ode>null</code>, �.�. ����� getType ��������� ��� ������
 * �������� <code>null</code>. 	������ ����������� ����������� ��� ������
 * ������� ����. ��� ��������������� ������  �������� ���������
 * <i><code>clazz</code></i>, ���������� �� ��������� <i><code>value</code></i>.
 * �������� value ��������� ����������� �� ���������� ����  ������� ������.
 * ��� ������ getValue ������ ���������� �������� �� ����������� ����
 * <code>value</code>, ������������� ������������� ��� �������� �������.
 *
 */
public class LiteralOperand implements IExpression {
    /**
     * For print 
     */
    public int lexType;
    
    private Object value;
    private Class  type;
    private ExpressionContext context;
    
/*    public LiteralOperand() {
        this(null);
    }
 */
    /**
     * ������� ����� ��������� ������ ��� ��������� ��������.
     * ������������� �������� �������� <code>type</code>, ���������
     * <code>value.getClass()</code> �����. ������, ���� �������� ���������
     * <code>value</code> ����� <code>null</code>, �� � �������� ��������
     * <code>type</code> ����� ��������������� � <code>null</code>.
     *
     * @param value �������� ��������, ��� �������� ��������� ������
     */
    public LiteralOperand(Object value) {
        //this(Object.class,value);
        //this.type = value == null ? Object.class : value.getClass();
        this.type = value == null ? null : value.getClass();        
        this.value = value;
    }
    /**
     * ������� ����� ��������� ������ ��� ��������� ������ � ��������.
     * ������������� �������� �������� <code>type</code>, ��������� ��������
     * <code><i>clazz</i></code>.
     *
     * @param clazz ��������������� ��� ��������
     * @param value �������� ��������, ��� �������� ��������� ������
     */
    public LiteralOperand(Class clazz,Object value) {
        this.type = clazz;
        this.value = value;
    }

/*    
    public Object getValue(int rowIndex) {
        return value;
    }
 */
    /**
     * ���������� �������� ��������.
     * ��������� �������� ��������, �������� � ���������� ������������ ��������,
     * �� �������� �� ����� �������� � ����� ���� �����, � ��� ����� �
     * <code>null</code>
     *
     * @param row ��������� ����������� ���������
     * @return �������� ������������ ��������
     * @see #getValue(tdo.NamedValues[])
     * @see #getValue()
     */
    @Override
    public Object getValue(NamedValues row) {
        return value;
    }
    /**
     * ���������� �������� ��������.
     * ��������� �������� ��������, �������� � ���������� ������������ ��������,
     * �� �������� �� ����� �������� � ����� ���� �����, � ��� ����� �
     * <code>null</code>
     *
     * @param row ������, ���������� �������� �������� ��������� �����������
     *        ���������
     * @return �������� ������������ ��������
     * @see #getValue(tdo.NamedValues)
     * @see #getValue()
     */

    @Override
    public Object getValue(NamedValues[] row) {
        return value;
    }
    /**
     * ���������� �������� ��������.
     * ��������� �������� ��������, �������� � ���������� ������������ ��������,
     * �� �������� �� ����� �������� � ����� ���� �����, � ��� ����� �
     * <code>null</code>
     *
     * @param row ������, ���������� �������� �������� ��������� �����������
     *        ���������
     * @return �������� ������������ ��������
     * @see #getValue(tdo.NamedValues)
     * @see #getValue(tdo.NamedValues[])
     */
    @Override
    public Object getValue() {
        return value;
    }
    /**
     * ������������� ����� �������� ��������.
     *
     * @param value ����� ��������
     *
     * @see #getValue(tdo.NamedValues[])
     * @see #getValue()
     * @see #getValue(tdo.NamedValues[])
     */
    
    public void setValue(Object value) {
        this.value = value;
    }
    /**
     * ���������� ��� �������� �������������� ��������, ��������������
     * ����������� �������.
     * @return ��� ��������
     */
    @Override
    public Class getType() {
        return this.type;
    }
    /**
     * ���������� ��������� �������� � ���������.
     * �������� >= 0 ����� ����� ������ ��� ����������. ����� ������ -1.
     * @return �������� ������ -1.
     */
    @Override
    public int getPriority() {
        return -1;
    }
    
    @Override
    public String toString() {
        String s = "";
        if ( value != null && value instanceof String  ) {
            s += "'" + value + "'";
        } else {
            s = "" + value;
        }
        return s;
    }

    
    
    /**
     * ������������� ��������, � ������� �������������� �������.
     * @param context ������ �� ��������
     */
    @Override
    public void setContext(ExpressionContext context) {
        this.context = context;
    }
}
