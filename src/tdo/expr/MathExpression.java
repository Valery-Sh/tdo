/*
 * MathExpression.java
 *
 */
package tdo.expr;

import java.math.BigDecimal;
import tdo.NamedValues;
import tdo.expr.AbstractOperator.ConcatOperator;
import tdo.expr.AbstractOperator.DivOperator;
import tdo.tools.scanner.ExpressionException;

/**
 * ������� ����� ��� ������� ������������ �������������� �������� � �������� 
 * ������������.<p>
 * 
 * � �������������� ��������� ��������� ����������� �������������� ��������, 
 * ����� ��� <i>��������,���������,���������,�������</i>. ���� �������� 
 * <i>������������</i> ��������� � �� ��������� � ��������������, �� �� ����� 
 * ������� �� ����� ������� �����. 
 * <p>��������� ������ ��������� ����� ����������, ��� ������� ��������� 
 * ��������. �������� �������� ��������� ���� {@link tdo.expr.IOperand}. 
 * �������� �������� �������� ���� {@link tdo.expr.IOperator}. ��� ����������
 * ��������������� ��������� ���������� ��� ��������� ����� ���� ����� ��:
 * 
 * <ol>
 *   <li>{@link tdo.expr.AbstractOperator.AddOperator}</li>
 *   <li>{@link tdo.expr.AbstractOperator.SubtrOperator}</li>
 *   <li>{@link tdo.expr.AbstractOperator.MultOperator}</li>
 *   <li>{@link tdo.expr.AbstractOperator.DivOperator}</li>
 *   <li>{@link tdo.expr.AbstractOperator.CancatOperator}</li>
 * </ol> 
 * 
 * <p>������� �� ������������ ���������� ������������� ����� ���������, 
 * �����������  <code>MathExpression</code>:
 * 
 * <ol>
 *   <li>{@link tdo.expr.MathExpression.AddExpression}</li>
 *   <li>{@link tdo.expr.MathExpression.SubtrExpression}</li>
 *   <li>{@link tdo.expr.MathExpression.MultExpression}</li>
 *   <li>{@link tdo.expr.MathExpression.DivExpression}</li>
 *   <li>{@link tdo.expr.MathExpression.ConcatExpression}</li>
 * </ol>
 * 
 */
public abstract class MathExpression extends AbstractExpression {

    //protected int saveRowIndex;
    /**
     * ������� ��������� ������ ��� �������� �������� ���������,�������
     * ��������� � �������� ���������.
     * @param context �������� ���������
     * @param operator ������ ���������
     * @param op1 ������ �������
     * @param op2 ������ �������
     */
    public MathExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
        this.setContext(context);
        this.operator = operator;
        this.op1 = op1;
        this.op2 = op2;
    }

    /**
     * ���������� �������� ��������� ��� �������� ���������
     * ����������� ���������.<p>
     * ����� �������� �������� ���������, ������� �� ������ <code>getValue</code>,
     * � ����� �������� ����� <code>computeValues</code>, ���������
     * ���������� � �������� ���������� ������ ��������� � ����������
     * �������� ���������.
     *
     * @param values ��������� ����������� ���������, ��������,
     * ������ ���� <code>tdo.DataRow</code>.
     * @return �������� ���������
     * @throws tdo.tools.scanner.ExpressionException ��� ������������� ������,
     *  ��������, ����� �������� ����� ������������ ��� ��� ��������� ���������.
     *  ����� ������������ ���������� ���������� �� ������ ����������� �
     *  ��������� ������ ��������� ���������
     * @see #computeValues(tdo.expr.IOperator, java.lang.Object, java.lang.Object)
     * @see tdo.expr.ExpressionContext#addError(int, tdo.expr.IExpression, java.lang.String)
     * @see tdo.expr.ExpressionContext#addError(int, tdo.expr.IExpression, java.lang.String)
     * @see #getValue(tdo.NamedValues[])
     */
    @Override
    public Object getValue(NamedValues values) {

        Object r = null;
        try {
            Object v1 = op1 == null ? null : op1.getValue(values);
            Object v2 = op2 == null ? null : op2.getValue(values);
            r = computeValues(this.operator, v1, v2);
        } catch(Exception e) {
            this.getContext().addError(ExpressionContext.MATHEXPRESSION, this,e.getMessage());
            throw new ExpressionException(ExpressionContext.MATHEXPRESSION,0,e.getMessage());
        }
        return r;

    }
    /**
     * ���������� �������� ��������� ��� ��������� ������� ���������
     * ����������� ���������.<p>
     * ����� ��������� �������� ���������, ������� �� ������ <code>getValue</code>,
     * � ����� �������� ����� <code>computeValues</code>, ���������
     * ���������� � �������� ���������� ������ ��������� � ����������
     * �������� ���������.
     *
     * @param values ������ ��������� ����������� ���������, ��������,
     * ������ �������� ���� <code>tdo.DataRow</code>.
     * @return �������� ���������
     * @throws tdo.tools.scanner.ExpressionException ��� ������������� ������,
     *  ��������, ����� �������� ����� ������������ ��� ��� ��������� ���������.
     *  ����� ������������ ���������� ���������� �� ������ ����������� �
     *  ��������� ������ ��������� ���������
     * @see #computeValues(tdo.expr.IOperator, java.lang.Object, java.lang.Object)
     * @see tdo.expr.ExpressionContext#addError(int, tdo.expr.IExpression, java.lang.String)
     * @see tdo.expr.ExpressionContext#addError(int, tdo.expr.IExpression, java.lang.String)
     * @see #getValue(tdo.NamedValues)
     */
    @Override
    public Object getValue(NamedValues[] values) {
        Object r = null;
        try {
            Object v1 = op1 == null ? null : op1.getValue(values);
            Object v2 = op2 == null ? null : op2.getValue(values);
            r = computeValues(this.operator, v1, v2);
        } catch(Exception e) {
            this.getContext().addError(ExpressionContext.MATHEXPRESSION, this,e.getMessage());
            throw new ExpressionException(ExpressionContext.MATHEXPRESSION,0,e.getMessage());
        }
        return r;
    }

/*    protected Object compute() {
        return null;
    }
*/
    /**
     * ���������� ��������, ����������� ��� �������� �������� ���������.
     * �����������, ����� �������� ��������� ��������� ����� ���
     * <code>java.lang.Double</code> ��� ���� ��� ��� ��������� � ����� ����.
     * <p>������ ���������� ��������� ����� ��� ���������� ��������
     * @param op1 ������ ������� ���������
     * @param op2 ������ ������� ���������
     * @return ����������� ��������
     */
    public abstract Double computeDouble(Double op1, Double op2);
    /**
     * ���������� ��������, ����������� ��� �������� �������� ���������.
     * �����������, ����� �������� ��������� ��������� ����� ���
     * <code>java.lang.Long</code> ��� ���� ��� ��� ��������� � ����� ����.
     * <p>������ ���������� ��������� ����� ��� ���������� ��������
     * @param op1 ������ ������� ���������
     * @param op2 ������ ������� ���������
     * @return ����������� ��������
     */
    public abstract Long computeLong(Long op1, Long op2);
    /**
     * ���������� ��������, ����������� ��� �������� �������� ���������.
     * �����������, ����� �������� ��������� ��������� ����� ���
     * <code>java.math.BigDecimal</code> ��� ���� ��� ��� ��������� � ����� ����.
     * <p>������ ���������� ��������� ����� ��� ���������� ��������
     * @param op1 ������ ������� ���������
     * @param op2 ������ ������� ���������
     * @return ����������� ��������
     */
    public abstract BigDecimal computeBigDecimal(BigDecimal op1, BigDecimal op2);

    //public abstract String computeString(String op1, String op2);

    @Override
    protected void setLexType() {
        lexType = -1;
    }


    /**
     * ��������� ��� ��������� �������� �� ������������.
     * 
     * ����������� �������, ���������� �������� <code>null</code> ����� ��������
     * � �������������� ����������, �� �� ������������� � ���������� ��� 
     * <code>java.util.Integer</code> ����� ������� ������� ������.
     * @param op ����������� �������.
     * @return <code>true</code> ���� ����� <i>op.getType()</i> ���� ���� ��
     *   �����: <code>Integer, Short, Byte, Float, Double, BigDecimal</code>.
     */
    private boolean isValidMathType(IOperand op) {
        String ss;
        if ( op == null || op.getType() == null )
            return true;
        if (op instanceof LiteralOperand && op.getType().equals(String.class)) {
            String s = (String)op.getValue();
            if (s != null) {
                try {
                    Double.parseDouble(s);
                    return true;
                } catch (Exception e) {
                    return false;
                }

            }
        }
        Class opclass = op.getType();
        if (opclass.equals(Integer.class) || opclass.equals(Short.class) || opclass.equals(Byte.class) || opclass.equals(Long.class) || opclass.equals(Float.class) || opclass.equals(Double.class) || opclass.equals(BigDecimal.class)) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * ���������� ��� ���������.
     * ���������� ���������� ������ {@link #getType(tdo.expr.IOperator, tdo.expr.IOperand, tdo.expr.IOperand) }
     * @return ����� ���������
     */
    @Override
    public Class getType() {
        return this.getType(operator, op1, op2);
    }
    /**
     * ��������� � ���������� ��� ��������� ��� �������� ��������� � ���������.
     * 
     * <p>��������� ���� ����� ���� ���� �� ����� �������-�������� ������
     * <code>java.lang: Byte,Short,Integer,Long, Float,Double,BigDecimal,/<code>
     * ���� �� �������� ���������� ����� ����� ���������� java ����� 
     * ���������� �������������� ���������.
     * <p>���� java ����� ���������� ��������� ���������� ������, 
     * <code>tdo.expr.MathExpression</code>. ������ ������, ����� �������� 
     * <code>operator</code> �������� ����������� ������ 
     * <code>{@link tdo.expr.AbstractOperator.ConcatOperator}. � ���� ������
     * ������������ �������� <code>String.class</code>.
     * <p>����������. ��� ��������� ������ ����������, ����� � ������ ���������
     *    ������ ������������� (��� IdentifierOperand) �/��� ��������
     *   (��� ParameterOperand). ����� �������� ����������� ��� � ���������
     *   ���������. � ����� ������, ��������, �������� ��� �� ���������������
     *   � �� ����� ���������� ��� �� ����� �������������� ��� ���������.
     *   ��������, � ������� ���� <code>DefaultExpressionContext</code> ������
     *   ���� ��������� ��� ������� <code>tdo.Table</code>, ������, ��� ��������
     *   ��������� ��������������. � <code>DefaultExpressionContext</code>
     *   ������� ������� <code><b>testMode</b></code>. ���� ���������
     *   �������������� �� ���������� ���� ������������, �� ����� getType()
     *   ����� ���������� �� ������ ��� ������.
     * 
     * <p>���� �������� ��������, ������������ ��� ����������� ����:
     * 
     * <ul>
     *   <li>���� ��� �������� <code>op1.getType()</code> �/���
     *       <code>op2.getType()</code>  �����/����� <code>null</code>,
     *       ���������, ��� ��� ���(�) ����� <code>Integer.class</code> �
     *       ��������������� �����.
     *   </li>
     *   <li>���� �������� <code>operator</code> ���� ��������� ������
     *     <code>tdo.Con�atOperator</code>, �� ������������ ��������
     *     <code>String.class</code>.
     *   </li>
     *   <li>���� ���� �������� <code>op1.getType()</code> ����
     *       {@link tdo.expr.IdentifierOperand} ���  {@link tdo.expr.MathExpression},
     *       � ��� ������� �������� <code>op2.getType()</code> ����� �����
     *       ������ �� ��������� ����, �� ������������ �������� 
     *       <code>tdo.expr.MathExpression.class</code>.
     *   </li>
     *   <li>���� ������ ���� �� ���������  ����� ��� 
     *       <code>tdo.expr.IdentifierOperand</code> ���  
     *        <code>tdo.expr.MathExpression</code>, �� ������ ������ ����� ���
     *        <code>Byte,Short,Integer,Long,Float,Double, BigDecimal</code>.
     *        ����� ������������ �������� <code>tdo.expr.MathExpression</code>.
     *        � ��������� ������ ������������ 
     *        <code>tdo.expr.ErrorExpression.class</code>.
     *   </li>
     *   <li>���� ���� �� ��������� ����� ��� <code>BigDecimal</code>, �� 
     *       ������������ <code>BigDecimal.class</code>.
     *   </li>
     *   <li>���� ���� �� ��������� ����� ��� <code>Double</code> ��� 
     *       <code>Float</code>, �� 
     *       ������������ <code>Double.class</code>.
     *   </li>
     *   <li>���� �������� �������� ����������� ������ 
     *      {@link tdo.expr.AbstractOperator.DivOperator}, �� ������������
     *      <code>Double.class</code>. � ��������� ������, ��� 
     *       <code>Byte,Short,Integer,Long</code>, ������������ 
     *      <code>Long.class</code>, � ��� ������ ������� ���� ������������
     *      <code>tdo.expr.ErrorExpression</code>.
     *   </li>
     * </ul>
     *   
     * @param operator ��������, ����������� � �������� ���������.
     * @param op1 ������ ������� ��� ��������� ���������.
     * @param op2 ������ ������� ��� ��������� ���������.
     * @return ����������� ��� ���������
     */
    protected Class getType(IOperator operator, IOperand op1, IOperand op2) {

        if (operator instanceof ConcatOperator) {
            return String.class;
        }

        Class op1class = op1.getType();
        Class op2class = op2.getType();

        if (op1class == null && op2class == null) {
            op1class = Integer.class;
            op2class = Integer.class;
        } else if (op1class == null) {
            op1class = Integer.class;
        } else if (op2class == null) {
            op2class = Integer.class;
        }
        if (op1 instanceof LiteralOperand) {
            if (!isValidMathType(op1)) {
                return ErrorExpression.class;
            }
        }
        if (op2 instanceof LiteralOperand) {
            if (!isValidMathType(op2)) {
                return ErrorExpression.class;
            }
        }

        if ((op1class.equals(IdentifierOperand.class) || op1class.equals(MathExpression.class)) && (op2class.equals(IdentifierOperand.class) || op2class.equals(MathExpression.class))) {
            return MathExpression.class;
        }
        if (op1class.equals(IdentifierOperand.class) || op1class.equals(MathExpression.class)) {
            if (isValidMathType(op2)) {
                return MathExpression.class;
            } else {
                return ErrorExpression.class;
            }
        }
        if (op2class.equals(IdentifierOperand.class) || op2class.equals(MathExpression.class)) {
            if (isValidMathType(op1)) {
                return MathExpression.class;
            } else {
                return ErrorExpression.class;
            }
        }


        if (op1class.equals(BigDecimal.class) || op2class.equals(BigDecimal.class)) {
            return BigDecimal.class;
        }

        if (op1class.equals(Double.class) || op2class.equals(Double.class)) {
            return Double.class;
        }

        if (op1class.equals(Float.class) || op2class.equals(Float.class)) {
            return Double.class;
        }

        
        if (operator instanceof DivOperator) {
            return Double.class;
        }

        if ( op1class.equals(String.class ) || op2class.equals(String.class )) {
            return Double.class;
        }
        
        if (op1class.equals(Long.class) || op2class.equals(Long.class)) {
            return Long.class;
        }

        if (op1class.equals(Integer.class) || op1class.equals(Short.class) || op1class.equals(Byte.class)) {
            return Long.class;
        }

        return ErrorExpression.class;
    }

    /**
     * ��������� �������� ��� ��������� ��������� �� �������� ��������� ���������.
     *
     * <p>���� <code>operator</code> �������� ����������� ����
     * <code>tdo.expr.AbstractOperator.ConcatOperator</code>, ��:
     * <ul>
     *    <li>���� ��� �������� ����� <code>null</code>, �� ������������ ������
     *        ������ ������.
     *    </li>
     *    <li>���� ������ ������ ������� ����� �������� <code>null</code>, ��
     *        ������������ �������� <code>value2.toString()</code>.
     *    </li>
     *    <li>���� ������ ������ ������� ����� �������� <code>null</code>, ��
     *        ������������ �������� <code>value1.toString()</code>.
     *    </li>
     *    <li>���� ��� �������� ��������� ������� �� <code>null</code>,
     *        �� ������������ ��������� �����:
     *        <code>value1.toString() + value2.toString()</code>
     * </ul>
     *
     * <p> ���� ���� ��� ��� �������� ��������� ����� <code>null</code>, ��
     * �������(�) �� ��������� <code>null</code> ������������� �
     * <code>java.lang.Integer(0)</code>. ����� ������������� ���������������,
     * ���� ����������, �������� ���������.
     * <p>���� ���� �� ��������� ����� ��� <code>java.math.BigDecimal</code>,
     *    �� ������ ������� ������������� � ����� ���� � ������������ ��������
     *    <oce>computeBigDecimal(value1,value2)</code>.
     * <p>���� ���� �� ��������� ����� ��� <code>java.lang.String</code>,
     *    �� ��� �������� ������������� � ��� <code>java.lang.Double</code> �
     *    ������������ ��������
     *    <code>computeDouble(v1,v2)</code>, ��� v1 � v2 ����� ��� <code>Double</code>,
     *          ��� ��������� �������������� �������������� <i>value1</i> �
     *          <i>value2</i>
     * <p>���� ���� �� ��������� ����� ��� <code>java.lang.Double</code>,
     *    �� ��� �������� ������������� � ��� <code>java.lang.Double</code> �
     *    ������������ ��������
     *    <code>computeDouble(v1,v2)</code>, ��� v1 � v2 ����� ��� <code>Double</code>,
     *          ��� ��������� �������������� �������������� <i>value1</i> �
     *          <i>value2</i>
     * <p>���� ���� �� ��������� ����� ��� <code>java.lang.Float</code>,
     *    �� ��� �������� ������������� � ��� <code>java.lang.Double</code> �
     *    ������������ ��������
     *    <code>computeDouble(v1,v2)</code>, ��� v1 � v2 ����� ��� <code>Double</code>,
     *          ��� ��������� �������������� �������������� <i>value1</i> �
     *          <i>value2</i>
     * <p>���� ��� �������� �������, �.�. �������� <code>operator</code>
     *      ����� ��� <code>DivOperator</code>,
     *    �� ��� �������� ������������� � ��� <code>java.lang.Double</code> �
     *    ������������ ��������
     *    <code>computeDouble(v1,v2)</code>, ��� v1 � v2 ����� ��� <code>Double</code>,
     *          ��� ��������� �������������� �������������� <i>value1</i> �
     *          <i>value2</i>
     * <p>���� ���� �� ��������� ����� ��� <code>java.lang.Long</code>,
     *    �� ��� �������� ������������� � ��� <code>java.lang.Long</code> �
     *    ������������ ��������
     *    <code>computeLong(v1,v2)</code>, ��� v1 � v2 ����� ��� <code>Long</code>,
     *          ��� ��������� �������������� �������������� <i>value1</i> �
     *          <i>value2</i>
     * <p>���� ���� �� ��������� ����� ��� <code>java.lang.Byte</code> ���
     *    <code>java.lang.Short</code> ��� <code>java.lang.Integer</code>,
     *    �� ��� �������� ������������� � ��� <code>java.lang.Long</code> �
     *    ������������ ��������
     *    <code>computeLong(v1,v2)</code>, ��� v1 � v2 ����� ��� <code>Long</code>,
     *          ��� ��������� �������������� �������������� <i>value1</i> �
     *          <i>value2</i>
     * @param operator
     * @param value1
     * @param value2
     * @return ����������� ��������
     * @see #toBigDecimal(java.lang.Object)
     * @see #toDouble(java.lang.Object)
     * @see #toLong(java.lang.Object)
     */
    protected Object computeValues(IOperator operator, Object value1, Object value2) {
        Object firstValue;
        Object secondValue;

        if (operator instanceof ConcatOperator) {
            if (value1 == null && value2 == null) {
                return "";
            }
            if (value1 == null) {
                return value2.toString();
            }
            if (value2 == null) {
                return value1.toString();
            }
            return value1.toString() + value2.toString();
        }


        if (value1 == null && value2 == null) {
            firstValue = new Integer(0);
            secondValue = new Integer(0);
        } else if (value1 == null) {
            firstValue = new Integer(0);
            secondValue = value2;
        } else if (value2 == null) {
            secondValue = new Integer(0);
            firstValue = value1;
        } else {
            firstValue = value1;
            secondValue = value2;
        }

        if (firstValue instanceof BigDecimal) {
            BigDecimal o1 = (BigDecimal) firstValue;
            BigDecimal o2 = toBigDecimal(secondValue);
            return computeBigDecimal(o1, o2);
        }

        /*        if (secondValue instanceof BigDecimal) {
        BigDecimal op1 = toBigDecimal(secondValue);
        BigDecimal op2 = (BigDecimal) firstValue;
        return computeBigDecimal(op1, op2);
        }
         */
        if (secondValue instanceof BigDecimal) {
            BigDecimal o1 = toBigDecimal(firstValue);
            BigDecimal o2 = (BigDecimal) secondValue;
            return computeBigDecimal(o1, o2);
        }
        if (firstValue instanceof String || secondValue instanceof String ) {
            Double o1 = toDouble(firstValue);
            Double o2 = toDouble(secondValue);
            return computeDouble(o1, o2);
        }

        if (firstValue instanceof Double) {
            double o1 = ((Double) firstValue).doubleValue();
            double o2 = toDouble(secondValue);
            return computeDouble(o1, o2);
        }
        if (secondValue instanceof Double) {
            double o2 = ((Double) secondValue).doubleValue();
            double o1 = toDouble(firstValue);
            return computeDouble(o1, o2);
        }

        if (firstValue instanceof Float) {
            double o1 = ((Float) firstValue).doubleValue();
            double o2 = toDouble(secondValue);
            return computeDouble(o1, o2);
        }
        if (secondValue instanceof Float) {
            double o2 = ((Float) secondValue).doubleValue();
            double o1 = toDouble(firstValue);
            return computeDouble(o1, o2);
        }
        if (operator instanceof DivOperator) {
            double o2 = toDouble(secondValue);
            double o1 = toDouble(firstValue);
            return computeDouble(o1, o2);
        }

        if (firstValue instanceof Long) {
            long o1 = ((Long) firstValue).longValue();
            long o2 = toLong(secondValue);
            return computeLong(o1, o2);
        }
        if (secondValue instanceof Long) {
            long o2 = ((Long) secondValue).longValue();
            long o1 = toLong(firstValue);
            return computeLong(o1, o2);
        }

        if (firstValue instanceof Integer || firstValue instanceof Short || firstValue instanceof Byte) {
            long o1 = toLong(firstValue);
            long o2 = toLong(secondValue);
            return computeLong(o1, o2);
        }

        return null;
    }
    /**
     * ����������� �������� �������� � ����������� ��� <code>double</code> �
     * ���������� ���.
     * <p>���� �������� ��������� <code>value</code> ����� <code>null</code>, ��
     * ������������ �������� 0D. <p>
     *
     * <p>���� �������� ��������� <code>value</code> ������ �� ������������� �����:
     * <ul>
     *    <li><code>java.lang.Byte</code></li>
     *    <li><code>java.lang.Short</code></li>
     *    <li><code>java.lang.Integer</code></li>
     *    <li><code>java.lang.Long</code></li>
     *    <li><code>java.lang.Float</code></li>
     *    <li><code>java.lang.Double</code></li>
     *    <li><code>java.lang.String</code></li>
     * </ul>
     * �� ������������ ��������������� � <code>double</code> ��������. �
     * ��������� ������, ������������ �������� 0d.
     *
     * @param value ������������� ��������
     * @return ��������������� �������� ���� <code>double</code>
     */
    public static double toDouble(Object value) {
        if (value == null) {
            return 0d;
        }

        double r = 0d;

        if (value instanceof Double) {
            r = ((Double) value).doubleValue();
        } else if (value instanceof Float) {
            r = ((Float) value).doubleValue();
        } else if (value instanceof Integer) {
            r = ((Integer) value).doubleValue();
        } else if (value instanceof Long) {
            r = ((Long) value).doubleValue();
        } else if (value instanceof Short) {
            r = ((Short) value).doubleValue();
        } else if (value instanceof Byte) {
            r = ((Byte) value).doubleValue();
        } else if (value instanceof String) {
            r = Double.parseDouble((String) value);
        }
        return r;
    }
    /**
     * ����������� �������� �������� � ����������� ��� <code>long</code> �
     * ���������� ���.
     *
     * <p>���� �������� ��������� <code>value</code> ����� <code>null</code>, ��
     * ������������ �������� 0L. 
     *
     * <p>���� �������� ��������� <code>value</code> ������ �� ������������� �����:
     * <ul>
     *    <li><code>java.lang.Byte</code></li>
     *    <li><code>java.lang.Short</code></li>
     *    <li><code>java.lang.Integer</code></li>
     *    <li><code>java.lang.Long</code></li>
     *    <li><code>java.lang.Float</code></li>
     *    <li><code>java.lang.Double</code></li>
     *    <li><code>java.lang.String</code></li>
     * </ul>
     * �� ������������ ��������������� � <code>long</code> ��������. �
     * ��������� ������, ������������ �������� 0L.
     *
     * @param value ������������� ��������
     * @return ��������������� �������� ���� <code>long</code>
     */
    public static long toLong(Object value) {
        long r = 0L;
        if (value == null) {
            return 0L;
        }
        if (value instanceof Double) {
            r = ((Double) value).longValue();
        } else if (value instanceof Float) {
            r = ((Float) value).longValue();
        } else if (value instanceof Integer) {
            r = ((Integer) value).intValue();
        } else if (value instanceof Long) {
            r = ((Long) value).longValue();
        } else if (value instanceof Short) {
            r = ((Short) value).longValue();
        } else if (value instanceof Byte) {
            r = ((Byte) value).longValue();
        }
        if (value instanceof String) {
            r = Long.parseLong((String) value);
        }
        return r;
    }
    /**
     * ����������� �������� �������� � ����������� ��� <code>int</code> �
     * ���������� ���.
     *
     * <p>���� �������� ��������� <code>value</code> ����� <code>null</code>, ��
     * ������������ �������� 0.
     *
     * <p>���� �������� ��������� <code>value</code> ������ �� ������������� �����:
     * <ul>
     *    <li><code>java.lang.Byte</code></li>
     *    <li><code>java.lang.Short</code></li>
     *    <li><code>java.lang.Integer</code></li>
     *    <li><code>java.lang.Long</code></li>
     *    <li><code>java.lang.Float</code></li>
     *    <li><code>java.lang.Double</code></li>
     *    <li><code>java.lang.String</code></li>
     * </ul>
     * �� ������������ ��������������� � <code>int</code> ��������. �
     * ��������� ������, ������������ �������� 0.
     *
     * @param value ������������� ��������
     * @return ��������������� �������� ���� <code>int</code>
     */
    public static int toInteger(Object value) {
        int r = 0;
        if (value == null) {
            return 0;
        }
        if (value instanceof Double) {
            r = ((Double) value).intValue();
        } else if (value instanceof Float) {
            r = ((Float) value).intValue();
        } else if (value instanceof Integer) {
            r = ((Integer) value).intValue();
        } else if (value instanceof Long) {
            r = ((Long) value).intValue();
        } else if (value instanceof Short) {
            r = ((Short) value).intValue();
        } else if (value instanceof Byte) {
            r = ((Byte) value).intValue();
        }
        if (value instanceof String) {
            r = Integer.parseInt((String) value);
        }
        return r;
    }
    /**
     * ����������� �������� �������� � ��� <code>BigDecimal</code> �
     * ���������� ���.
     *
     * <p>���� �������� ��������� <code>value</code> ����� <code>null</code>, ��
     * ������������ �������� <code> new BigDecimal(0)</code>. <p>
     * ���� �������� ��������� <code>value</code> ����� ��� <code>BigDecimal</code>,
     * �� ��� ������������ ��� ��������������.
     *
     * <p>���� �������� ��������� <code>value</code> ������ �� ������������� �����:
     * <ul>
     *    <li><code>java.lang.Byte</code></li>
     *    <li><code>java.lang.Short</code></li>
     *    <li><code>java.lang.Integer</code></li>
     *    <li><code>java.lang.Long</code></li>
     *    <li><code>java.lang.Float</code></li>
     *    <li><code>java.lang.Double</code></li>
     *    <li><code>java.lang.String</code></li>
     * </ul>
     * �� ������������ ��������������� � <code>BigDecimal</code> ��������. �
     * ��������� ������, ������������ �������� BigDecimal(0).
     *
     * @param value ������������� ��������
     * @return ��������������� �������� ���� <code>BigDecimal</code>
     */
    public static BigDecimal toBigDecimal(Object value) {
        BigDecimal r = new BigDecimal(0);
        if (value == null) {
            return r;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal)value;
        }

        if (value instanceof Double) {
            r = new BigDecimal(((Double) value).doubleValue());
        } else if (value instanceof Float) {
            r = new BigDecimal(((Float) value).floatValue());
        } else if (value instanceof Integer) {
            r = new BigDecimal(((Integer) value).intValue());
        } else if (value instanceof Long) {
            r = new BigDecimal(((Long) value).longValue());
        } else if (value instanceof Short) {
            r = new BigDecimal(((Short) value).shortValue());
        } else if (value instanceof Byte) {
            r = new BigDecimal(((Byte) value).byteValue());
        }
        if (value instanceof String) {
            r = new BigDecimal((String) value);
        }
        return r;
    }

    //////////////////////////////////////////////////

    /**
     * ������������ ���������� ��������������� �������� ���� ���������.
     * <p>���������� ��������� ����� ����: <code>Byte, Short, Integer, Long,
     * Float, Double, BigDecimal</code>. ��������� ����� ������ ����
     * <code>String</code>, ���������� ������� ������������ ����� ����������
     * �����. �������� �������� <code>null</code> ��������� � ������������� �
     * ������������� ���� ( Integer(0) ).
     */
    public static class AddExpression extends MathExpression {

        /**
         * ������� ��������� ������ ��� �������� ���������, ��������� � ���������.
         * @param context �������� ���������
         * @param operator ��������� ������ {@link tdo.expr.AbstractOperator.AddOperator}
         * @param op1 ������ ���������
         * @param op2 ������ ���������
         */
        public AddExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }

        /**
         * ���������� � ���������� ��� �������� ���� <code>java.lang.Double</code>.
         * @param op1 ������ ���������
         * @param op2 ������ ���������
         * @return ��������� ��������
         */
        @Override
        public Double computeDouble(Double op1, Double op2) {
            double d;
            d = op1.doubleValue() + op2.doubleValue();
            return new Double(d);
        }
        /**
         * ���������� � ���������� ��� �������� ���� <code>java.lang.Long</code>.
         * @param op1 ������ ���������
         * @param op2 ������ ���������
         * @return ��������� ��������
         */
        @Override
        public Long computeLong(Long op1, Long op2) {
            long d;
            d = op1.longValue() + op2.longValue();
            return new Long(d);
        }
        /**
         * ���������� � ���������� ��� �������� ���� <code>java.math.BigDecimal</code>.
         * @param op1 ������ ���������
         * @param op2 ������ ���������
         * @return ��������� ��������
         */
        @Override
        public BigDecimal computeBigDecimal(BigDecimal op1, BigDecimal op2) {
            return op1.add(op2);
        }
    } //class AddExpression

    /**
     * ������������ ���������� ��������������� ��������� ���� ���������.
     * <p>���������� ��������� ����� ����: <code>Byte, Short, Integer, Long,
     * Float, Double, BigDecimal</code>. ��������� ����� ������ ����
     * <code>String</code>, ���������� ������� ������������ ����� ����������
     * �����. �������� �������� <code>null</code> ��������� � ������������� �
     * ������������� ���� ( Integer(0) ).
     */
    public static class SubtrExpression extends MathExpression {
        /**
         * ������� ��������� ������ ��� �������� ���������, ��������� � ���������.
         * @param context �������� ���������
         * @param operator ��������� ������ {@link tdo.expr.AbstractOperator.SubtrOperator}
         * @param op1 �����������
         * @param op2 ����������
         */
        public SubtrExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * ���������� �������� ���� �������� ��� ��� <code>java.lang.Double</code>.
         *
         * @param op1 �����������
         * @param op2 ����������
         * @return ��������
         */
        @Override
        public Double computeDouble(Double op1, Double op2) {
            double d;
            d = op1.doubleValue() - op2.doubleValue();
            return new Double(d);
        }
        /**
         * ���������� �������� ���� �������� ��� ��� <code>java.lang.Long</code>.
         *
         * @param op1 �����������
         * @param op2 ����������
         * @return ��������
         */
        @Override
        public Long computeLong(Long op1, Long op2) {
            long d;
            d = op1.longValue() - op2.longValue();
            return new Long(d);
        }
        /**
         * ���������� �������� ���� �������� ��� ��� <code>java.lang.BigDecimal</code>.
         *
         * @param op1 �����������
         * @param op2 ����������
         * @return ��������
         */
        @Override
        public BigDecimal computeBigDecimal(BigDecimal op1, BigDecimal op2) {
            return op1.subtract(op2);
        }


    } //class SubtrExpression

    /**
     * ������������ ���������� ��������������� ��������� ���� ���������.
     * <p>���������� ��������� ����� ����: <code>Byte, Short, Integer, Long,
     * Float, Double, BigDecimal</code>. ��������� ����� ������ ����
     * <code>String</code>, ���������� ������� ������������ ����� ����������
     * �����. �������� �������� <code>null</code> ��������� � ������������� �
     * ������������� ���� ( Integer(0) ).
     */
    public static class MultExpression extends MathExpression {
        /**
         * ������� ��������� ������ ��� �������� ���������, ��������� � ���������.
         * @param context �������� ���������
         * @param operator ��������� ������ {@link tdo.expr.AbstractOperator.MultOperator}
         * @param op1 ������ �����������
         * @param op2 ������ �����������
         */
        public MultExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * ���������� ��������� ��������� ���� �������� ��� ��� <code>java.lang.Double</code>.
         *
         * @param op1 ������ �����������
         * @param op2 ������ �����������
         * @return ��������� ���������
         */
        @Override
        public Double computeDouble(Double op1, Double op2) {
            double d;
            d = op1.doubleValue() * op2.doubleValue();
            return new Double(d);
        }
        /**
         * ���������� ��������� ��������� ���� �������� ��� ��� <code>java.lang.Long</code>.
         *
         * @param op1 ������ �����������
         * @param op2 ������ �����������
         * @return ��������� ���������
         */
        @Override
        public Long computeLong(Long op1, Long op2) {
            long d;
            d = op1.longValue() * op2.longValue();
            return new Long(d);
        }
        /**
         * ���������� ��������� ��������� ���� �������� ��� ��� <code>java.lang.BigDecimal</code>.
         *
         * @param op1 ������ �����������
         * @param op2 ������ �����������
         * @return ��������� ���������
         */
        @Override
        public BigDecimal computeBigDecimal(BigDecimal op1, BigDecimal op2) {
            return op1.multiply(op2);
        }

    } //class MultExpression

    /**
     * ������������ ���������� ��������������� ������� ���� ���������.
     * <p>���������� ��������� ����� ����: <code>Byte, Short, Integer, Long,
     * Float, Double, BigDecimal</code>. ��������� ����� ������ ����
     * <code>String</code>, ���������� ������� ������������ ����� ����������
     * �����. �������� �������� <code>null</code> ��������� � ������������� �
     * ������������� ���� ( Integer(0) ).
     */
    public static class DivExpression extends MathExpression {
        /**
         * ������� ��������� ������ ��� �������� ���������, ��������� � ���������.
         * @param context �������� ���������
         * @param operator ��������� ������ {@link tdo.expr.AbstractOperator.MultOperator}
         * @param op1 �������
         * @param op2 ��������
         */
        public DivExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * ���������� ��������� ������� ���� �������� ��� ��� <code>java.lang.Double</code>.
         * @param op1 �������
         * @param op2 ��������
         * @return ��������� ���������
         */
        @Override
        public Double computeDouble(Double op1, Double op2) {
            double d;
            d = op1.doubleValue() / op2.doubleValue();
            return new Double(d);
        }
        /**
         * ���������� ��������� ������� ���� �������� ��� ��� <code>java.lang.Long</code>.
         * @param op1 �������
         * @param op2 ��������
         * @return ��������� ���������
         */
        @Override
        public Long computeLong(Long op1, Long op2) {
            long d;
            d = op1.longValue() / op2.longValue();
            return new Long(d);
        }
        /**
         * ���������� ��������� ������� ���� �������� ��� ��� <code>java.lang.BigDecimal</code>.
         * ���������� ������������ �� �������:
         * <pre>
         *  op1.divide(op2, op1.scale() - op2.scale(), BigDecimal.ROUND_HALF_UP);
         * </pre>
         * @param op1 ������ �����������
         * @param op2 ������ �����������
         * @return ��������� ���������
         */
        @Override
        public BigDecimal computeBigDecimal(BigDecimal op1, BigDecimal op2) {
            return op1.divide(op2, op1.scale() - op2.scale(), BigDecimal.ROUND_HALF_UP);
        }


    } //class DivExpression

    /**
     * ������������ ���������� ������������ ��������� �������� ���� ���������.
     * <p>���������� ��������� ����� ���� ����� �������, ��� �������
     * ��������� ����� <code>toString()</code>.
     * �������� �������� <code>null</code> ��������� � ������������� � ������
     * ������.
     */
    public static class ConcatExpression extends MathExpression {

        public ConcatExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }

        /**
         * ������ ����������� ���������� ���� {@link tdo.tools.scanner.ExpressionException}.
         */
        @Override
        public Double computeDouble(Double op1, Double op2) {
            throw new ExpressionException("ConcatExpression.computeDouble() : Unsupported operation (" + op1 + "||" + op2 + ")");
        }
        /**
         * ������ ����������� ���������� ���� {@link tdo.tools.scanner.ExpressionException}.
         */
        @Override
        public Long computeLong(Long op1, Long op2) {
            throw new ExpressionException("ConcatExpression.computeLong() : Unsupported operation (" + op1 + "||" + op2 + ")");
        }
        /**
         * ������ ����������� ���������� ���� {@link tdo.tools.scanner.ExpressionException}.
         */
        @Override
        public BigDecimal computeBigDecimal(BigDecimal op1, BigDecimal op2) {
            throw new ExpressionException("ConcatExpression.computeBigDecimal() : Unsupported operation (" + op1 + "||" + op2 + ")");
        }
    } //class MultExpression

} //class MathExpression
