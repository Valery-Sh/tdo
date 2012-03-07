/*
 * ExpressionContext.java
 */
package tdo.expr;

import java.util.Vector;
import tdo.NamedValues;

/**
 * ����� ������������ ����� ��� ��������� ���������. <p>
 * ������������� ������ ������ ��������� ���������� ����������������:
 * <ol>
 *   <li>������������� �������� � ���� ����������� ���������������.
 *       ���, ��������, ��������� �������� ��������� � ���������.
 *   </li>
 *   <li>
 *     ������������ ���������������� ��������� ������
 *   </li>
 *   <li>
 *     ������������ ������������� �� ����� ��������, ��� <code>tdo.Table,
 *     tdo.DataRow</code>  � �.�. ��� �������������� ����� ������������, ��� 
 *     �������� ������������� ���������, ��������, ��� ������������ � ������� 
 *     JUnit Framework. 
 *   </li>
 *  </ol>
 *  <p>��������� �������� ����� createExpression() ��� �������� ������� ���� 
 *  {@link tdo.expr.IExpression}. ����� �������� ��������� ��������� ����� ����
 *  ���������:
 * <ul>
 *   <li>���������� ������� ��������� ������, ������������ ��������� � ���������
 *       ��� ��������� �������� ���������.
 *   </li>
 *   <li>���������� ��������� ����� <code>createExpression()</code> � ����������
 *       ���������. ����� ����������� ��������� � ���������� ���������, ��� 
 *       ������ ����  <code>IExpression</code>  
 *   </li>
 *   <li>��� ��������� �������� ��������� ���������� ��������� ���� �� �������
 *       <code>getValue</code> ����������� ���������.
 *   </li>
 * </ul>
 *  <p><b>������ 1.</b> ��� ������� ���������� ���������� ����������
 * {@link tdo.DefaultExpressionContext}.
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
 *  <p><b>������ 2.</b> 
 * <code>
 *   <pre>
 *      ExpressionContext context = new DefaultExpressionContext("NumberFormat( (2/3 + 7)*8),'###.## ");
 *      IExpression expr = context.createExpression();
 *      System.out.println("������ 2 ���������: " + expr.getValue());
 * 
 *   </pre>
 * </code>
 * ��������� �� �������:
 * 
 * <pre>������ 2 ���������: 61.33</pre>

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
 * <p>��������� ����� ��������� �������������� � ��������������� <p>��������</p>.
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
 *
 */
public interface ExpressionContext {

    /**
     * �������� ������������ ��������� ��� �������� ���������
     */
    static final int OPERANDTYPE = 100;
    /**
     * �������� ������������ ��������� ��� �������������� ��������.
     */
    static final int MATH_OPERANDTYPE = 120;
    /**
     * ������ �������� ������� ExprParser.
     */
    static final int EXPRPARSER = 140;
    /**
     * ������ "����������� �������".
     */
    static final int UNKNOWNFUNCTION = 160;
    /**
     * ������ � ���������.
     */
    static final int STRUCTURE = 170;
    /**
     * ������ �������������� ��������.
     */
    static final int MATHEXPRESSION = 180;
    /**
     * ������ �������� ���������.
     */
    static final int COMPAREEXPRESSION = 190;
    /**
     * ������ ��������� �������.
     */
    static final int FUNCTIONEXPRESSION = 200;
    /**
     * ������ ������������.
     */
    static final int TESTING = 210;

    /**
     * ��������� ���������� �� ������ � ��������� ������.
     * 
     * @param code �������� ����� �� ����������� ��������, ������������ �
     *    ����������.
     * @param expr ������� ���������, ��� ��������� �������� ��������� ������.
     * @see #addError(int, tdo.expr.IExpression, java.lang.String) 
     * @see #getErrorList() 
     * @see tdo.expr.ErrorItem
     */
    void addError(int code, IExpression expr);

    /**
     * ��������� ���������� �� ������ � ��������� ������.
     * 
     * @param code �������� ����� �� ����������� ��������, ������������ �
     *    ����������.
     * @param expr ������� ���������, ��� ��������� �������� ��������� ������.
     * @param msg ��������� ��������� � ��������������� �����������
     * @see #addError(int, tdo.expr.IExpression) 
     * @see #getErrorList() 
     * @see tdo.expr.ErrorItem
     */
    void addError(int code, IExpression expr, String msg);

    /**
     * ������� � ���������� ������ ���� <code>IExpression</code> �� 
     * ������������� ���������� ���������.
     * 
     * @return ��������� ��������� ��������� ���������.
     * @see tdo.expr.IExpression
     * @see #getExpressionString() 
     */
    IExpression createExpression();
    /**
     * ���������� ��������� ��������, �������������� �������������� ���������.
     * � ��� ��������� ��������������-��������� �� ������.
     * @return ��������� ��������, ���� {@link tdo.expr.IdentifierOperand}. 
     * @see #getParameterOperands() 
     */
    Vector getIdentifierOperands();

    /**
     * ���������� ����� �������������� �� �������� ����� � ������.
     * @param idName 
     * @param alias 
     * @return ������ Table ��� <code>null</code>
     */
    Class getIdentifierType(String idName, String alias);

    Vector getErrorList();

    /**
     * @return ���������� �������� ��������� � ��������� �������
     */
    String getExpressionString();
    /**
     * ���������� ��������� ��������, �������������� ��������������-��������� 
     * ���������.
     * ��������� ��������� ��� �������������� � �������������� �������� ':' - 
     * ���������.
     * @return ��������� ��������, ���� {@link tdo.expr.ParameterOperand}. 
     * @see #getIdentifierOperands() 
     */
    Vector getParameterOperands();
    /**
     * ���������� ����� ��������������-��������� �� ��������� �����.
     * @param paramName ��� ���������. ����� ���� ������� � ����� �������� � ��
     * �������� �������������� ������-���������.
     * @return ������ <code>java.lang.Class</code>
     */
    Class getParameterType(String paramName);

    /**
     * ���������� �������� ��������������� ��������� ����� ���������.
     * @param nvalues 
     * @param paramName ��� ���������. ����� ���� ������� � ����� �������� � ��
     * �������� �������������� ������-���������.
     * @return �������� ���������
     */
    Object getParameterValue(NamedValues nvalues, String paramName);
    /**
     * ���������� �������� ��������������� ��������� ����� ���������.
     * @param nvalues ������, ��������, �������� ���� <code>tdo.DataRow</code>
     * @param paramName ��� ���������. ����� ���� ������� � ����� �������� � ��
     * �������� �������������� ������-���������.
     * @return �������� ���������
     */
    Object getParameterValue(NamedValues[] nvalues, String paramName);
    /**
     * ���������� �������� ��������������� ��������� ����� ���������.
     * @param nvalues ������, ��������, �������� ���� <code>tdo.DataRow</code> 
     *   �� �������� �� ��������� ������ ���������� ���� � ������������ ��� 
     *   ��������� ������
     * @param alias ����� ��������������. ����� ���� ������� � ����� ��������.
     * @return ��������� ���������� ��������, ��������� �� �����
     */
    NamedValues getNamedValues(NamedValues[] nvalues, String alias);
    /**
     * ������� �������� � ��� �������� �� ���������� ��������� ����������
     * @param paramName ��� ���������� ���������. �� �������� �������������� 
     * ������-���������.
     */
    void removeParameter(String paramName);

    /**
     * ������������� ����� �������� ��������� � ��������� �������. <p>
     * @param expr ����� �������� ��������� � ��������� �������
     */
    void setExpressionString(String expr);

    /**
     * ��������� �������� � ��������������� �������� � ���������� ��������� 
     * ����������.
     * 
     * @param paramName ��� ������������ ���������. �� �������� �������������� 
     * ������-���������.
     * @param value �������� ���������
     */
    void setParameter(String paramName, Object value);

    /**
     * �������� ���������� �� �������, ��������� ��� ��������� ���������.
     */
    void printErrors();
} //interface ExpressionContext
