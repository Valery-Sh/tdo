/*
 * CompareOperand.java
 */
package tdo.expr;

import java.math.BigDecimal;
import java.sql.Timestamp;
import tdo.NamedValues;
import tdo.tools.expr.LexConst;
import tdo.tools.scanner.ExpressionException;

/**
 * ������� ����� ��� ������� ������������ �������� ���������.<p>
 *
 * � ��������� ��������� ���������:
 * <ul>
 *   <li>
 *      ����������� �������� ���������, ����� ��� <i>�����, �� �����, ������,
 *      ������ ��� �����, ������,
 *      ������ ��� �����; </i>
 *   </li>
 *   <li>
 *     <b>in </b> ��������, ��������������� �������� �� ������, �������� ������
 *        ���������, ��������, �������� ������ ���������;
 *   </li>
 *   <li>
 *     <b>not in </b> ��������, �������� � in;
 *   </li>
 *
 *   <li>
 *      <b>between</b> ��������, ���������������, ��������� �� �������� ������
 *      ��������� ���������;
 *   </li>
 *   <li>
 *      <b>containing</b> ��������, ���������������, �������� �� �������� ������
 *          ��������� ������ ������.
 *   </li>
 *   <li>
 *      <b>not containing</b> �������� - �������� �������� � containing;
 *   </li>
 *   <li>
 *      <b>starting with</b> ��������, ���������������, ���������� �� ��������
 *          ������ � �������� ������������������ ��������;
 *   </li>
 *   <li>
 *      <b>not starting with</b> ��������, �������� � starting with;
 *   </li>
 *   <li>
 *      <b>is null</b> ��������, ����������� �������� �� ��������� <code>null</code>;
 *   </li>
 *   <li>
 *      <b>not is null</b> ��������, ����������� �������� �� �� ���������
 *          <code>null</code>;
 *   </li>
 *   <li>
 *      <b>like</b> ��������, ����������� �������� �� ������������ �������;
 *   </li>
 *   <li>
 *      <b>not like</b> ��������, ����������� �������� �� �������������� �������;
 *   </li>
 *   <li>
 *      <b>regex</b>��������, ����������� �������� �� ������������ �����������
 *              ���������;
 *   </li>
 *   <li>
 *      <b>not regex</b> ��������, ����������� �������� �� ��������������
 *          ����������� ���������;
 *   </li>
 * </ul>
 *
 * <p>��������� ������ ��������� ����� ����������, ��� ������� ���������
 * ��������. �������� �������� ��������� ���� {@link tdo.expr.IOperand}.
 * �������� �������� �������� ���� {@link tdo.expr.IOperator}. ��� ����������
 * ��������� ��������� ���������� ��� ��������� ����� ���� ����� ��:
 *
 * <ol>
 *   <li>{@link tdo.expr.AbstractOperator.EqualsOperator}</li>
 *   <li>{@link tdo.expr.AbstractOperator.NotEqualsOperator}</li>
 *   <li>{@link tdo.expr.AbstractOperator.GreaterOperator}</li>
 *   <li>{@link tdo.expr.AbstractOperator.GreaterEqualsOperator}</li>
 *   <li>{@link tdo.expr.AbstractOperator.LessOperator}</li>
 *   <li>{@link tdo.expr.AbstractOperator.LessEqualsOperator}</li>
 *   <li>{@link tdo.expr.AbstractOperator.BetweenOperator}</li>
 *   <li>{@link tdo.expr.AbstractOperator.ContainingOperator}</li>
 *   <li>{@link tdo.expr.AbstractOperator.NotContainingOperator}</li>
 *
 *   <li>{@link tdo.expr.AbstractOperator.StartingWithOperator}</li>
 *   <li>{@link tdo.expr.AbstractOperator.NotStartingWithOperator}</li>
 *   <li>{@link tdo.expr.AbstractOperator.InOperator}</li>
 *   <li>{@link tdo.expr.AbstractOperator.NotInOperator}</li>
 *   <li>{@link tdo.expr.AbstractOperator.IsNullOperator}</li>
 *   <li>{@link tdo.expr.AbstractOperator.IsNotNullOperator}</li>
 *   <li>{@link tdo.expr.AbstractOperator.LikeOperator}</li>
 *   <li>{@link tdo.expr.AbstractOperator.NotLikeOperator}</li>
 *   <li>{@link tdo.expr.AbstractOperator.RegExOperator}</li>
 *   <li>{@link tdo.expr.AbstractOperator.NotRegExOperator}</li>
 * </ol>
 *
 * <p>������� �� ������������ ���������� ������������� ����� ���������,
 * �����������  <code>CompareExpression</code>:
 *
 * <ol>
 *   <li>{@link tdo.expr.CompareExpression.EqualsExpression}</li>
 *   <li>{@link tdo.expr.CompareExpression.NotEqualsExpression}</li>
 *   <li>{@link tdo.expr.CompareExpression.GreaterExpression}</li>
 *   <li>{@link tdo.expr.CompareExpression.GreaterEqualsExpression}</li>
 *   <li>{@link tdo.expr.CompareExpression.LessExpression}</li>
 *   <li>{@link tdo.expr.CompareExpression.LessEqualsExpression}</li>
 *   <li>{@link tdo.expr.CompareExpression.BetweenExpression}</li>
 *   <li>{@link tdo.expr.CompareExpression.ContainingExpression}</li>
 *   <li>{@link tdo.expr.CompareExpression.NotContainingExpression}</li>
 *   <li>{@link tdo.expr.CompareExpression.StartingWithExpression}</li>
 *   <li>{@link tdo.expr.CompareExpression.NotStartingWithExpression}</li>
 *   <li>{@link tdo.expr.CompareExpression.InExpression}</li>
 *   <li>{@link tdo.expr.CompareExpression.NotInExpression}</li>
 *   <li>{@link tdo.expr.CompareExpression.IsNullExpression}</li>
 *   <li>{@link tdo.expr.CompareExpression.IsNotNullExpression}</li>
 *   <li>{@link tdo.expr.CompareExpression.LikeExpression}</li>
 *   <li>{@link tdo.expr.CompareExpression.NotLikeExpression}</li>
 *   <li>{@link tdo.expr.CompareExpression.RegExExpression}</li>
 *   <li>{@link tdo.expr.CompareExpression.NotRegExExpression}</li>
 * </ol>
 *
 */
public class CompareExpression extends AbstractExpression {

    /**
     * ������� ��������� ������ ��� �������� �������� ���������,�������
     * ��������� � �������� ���������.
     * @param context �������� ���������
     * @param operator ������ ���������
     * @param op1 ������ �������
     * @param op2 ������ �������
     */
    public CompareExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
        this.setContext(context);
        this.operator = operator;
        this.op1 = op1;
        this.op2 = op2;
    }

    /**
     * ���������� �������� ��������� ��� �������� ���������
     * ����������� ���������.<p>
     * ���������� ����������� ������ <code>compare</code>, ���������
     * ���������� � �������� ��������� ����������� ���������, ��������,
     * <code>tdo.DataRow</code>.
     *
     * @param values ��������� ����������� ���������, ��������,
     * ������ ���� <code>tdo.DataRow</code>.
     * @return �������� ���������
     * @see #compare(tdo.NamedValues)
     * @see #getValue(tdo.NamedValues[])
     */
    @Override
    public Object getValue(NamedValues values) {
        return this.compare(values);
    }

    /**
     * ���������� �������� ��������� ��� ��������� ������� ���������
     * ����������� ���������.<p>
     * ���������� ����������� ������ <code>compare</code>, ���������
     * ���������� � �������� ��������� ������ ����������� ���������, ��������,
     * <code>tdo.DataRow</code>.
     *
     * @param values ��������� ����������� ���������, ��������,
     * ������ ���� <code>tdo.DataRow</code>.
     * @return �������� ���������
     * @see #compare(tdo.NamedValues[])
     * @see #getValue(tdo.NamedValues)
     */
    @Override
    public Object getValue(NamedValues[] values) {
        return this.compare(values);
    }

    /**
     * ���������� �������� <code>Boolean.class</code>.
     * @return ��� ���������� ���������, ������� ������ �����
     * <code>Boolean.class</code>.
     */
    @Override
    public Class getType() {
        return Boolean.class;
    }

    /**
     * ��������� ��������� ��������� �� ���������.
     * @param values ����������� ��������� ���������.<p>
     * @return true , ���� ��������� ���� �� �������:
     * <ul>
     *   <li>��� �������� ����� <code>null</code>;</li>
     *   <li>��� �������� �� ����� <code>null</code> �
     *       ����� <code>op1.equals(op2)</code> ���� <code>true</code>.
     *   </li>
     * </ul>
     * � ��������� ������ ������������ <code>false</code>
     * @see #compare(tdo.NamedValues[])
     */
    protected Boolean compare(NamedValues values) {
        boolean b;//My 06.03.2012 = false;
        if (op1 == null && op2 == null) {
            b = true;
        } else {
            b = (op1 != null && op2 != null && op1.equals(op2));
        }
        return b;
    }

    /**
     * ��������� ��������� ��������� �� ���������.
     * @param values ������ ����������� ��������� ���������.<p>
     * @return true , ���� ��������� ���� �� �������:
     * <ul>
     *   <li>��� �������� ����� <code>null</code>;</li>
     *   <li>��� �������� �� ����� <code>null</code> �
     *       ����� <code>op1.equals(op2)</code> ���� <code>true</code>.
     *   </li>
     * </ul>
     * � ��������� ������ ������������ <code>false</code>
     * @see #compare(tdo.NamedValues)
     */
    protected Boolean compare(NamedValues[] values) {
        boolean b = false;
        if (op1 == null && op2 == null) {
            b = true;
        } else if (op1 != null && op2 != null && op1.equals(op2)) {
            b = true;
        }
        return b;
    }

    //************************************************************
    /**
     * ���������� ������������� ��������� ��������� ���� ��������.
     * <p>������������ ������� ����� ������������ ������ �����, ������� �����
     * ���������� ������������ �������������� ����� � ���� ���������������
     * ��������, � ����������� �� ���������, ���������� ������ �� �������:
     * <ul>
     *   <li>{@link #compareBigDecimal(java.math.BigDecimal, java.math.BigDecimal)}</li>
     *   <li>{@link #compareDouble(double, double) }</li>
     *   <li>{@link #compareLong(long, long)}</li>
     * </ul>
     *
     * <p>��������� ����������� �� ���������� ���������:
     *  <p>���� ��� �������� ����� <code>null</code>, �� ������������ 0;
     *  <p>���� ������ ������� ����� <code>null</code> �� ������������ -1;
     *  <p>���� ������ ������� ����� <code>null</code> �� ������������ 1;
     *  <p>���� ������ ������� ����� ������� � ������ java-�������� ���������
     *      <code>==</code> �� ������������ 0;
     *  <p>���� ���� �� ��������� ����� ��� <code>java.math.BigDecimal</code>, ��
     *     ������ ������� �������������, ���� ����������, � ���
     *     <code>java.math.BigDecimal</code>, ����������� �����
     *     {@link #compareBigDecimal(java.math.BigDecimal, java.math.BigDecimal) }
     *     � ������������ ��������� ��� ����������.
     *  <p>���� ���� �� ��������� ����� ��� <code>java.lang.Double</code>, ��
     *     ������ ������� �������������, ���� ����������, � ���
     *     <code>java.lang.Double</code>, ����������� �����
     *     {@link #compareDouble(java.lang.Double, java.lang.Double) }
     *     � ������������ ��������� ��� ����������.
     *  <p>���� ���� �� ��������� ����� ��� <code>java.lang.Float</code>, ��
     *     ��� �������� �������������,� ��� <code>java.lang.Double</code>,
     *     ����������� �����
     *     {@link #compareDouble(java.lang.Double, java.lang.Double) }
     *     � ������������ ��������� ��� ����������.
     *  <p>���� ��� ������ �� ��������� <code>java.lang.Byte,
     *     java.lang.Short, java.lang.Integer</code>, ��
     *     ��� �������� �������������, ���� ����������, � ���
     *     <code>java.lang.Long</code>, ����������� �����
     *     {@link #compareLong(java.lang.Long, java.lang.Long) }
     *     � ������������ ��������� ��� ����������.
     *  <p>���� ��� �������� ����� ��� <code>java.util.Date</code>, ��
     *     ������������ ��������� ���������� ������ <code>Date.compareTo(Date).
     *  <p>���� ��� �������� ����� ��� <code>java.sql.Timestamp</code>, ��
     *     ������������ ��������� ���������� ������ <code>Timestamp.compareTo(Timestamp).
     *  <p>���� ������ ������� ����� ��� <code>java.sql.Timestamp</code>
     *     � ������ ������� <code>java.util.Date</code>, ��
     *     ����������� �����
     *     �� ������������ ��������� ���������� ������
     *     <code>((Timestamp)firstValue).compareTo((Date)secondValue)</code>.
     *
     *  <p>���� ������ ������� ����� ��� <code>java.util.Date</code>
     *     � ������ ������� <code>java.sql.Timestamp</code>, ��
     *     ����������� �����
     *     <code>((Timestamp)secondValue).compareTo((Date)firstValue)</code> �,
     *     ���� �������� ���������� ����� 0, �� ������������ 0. �����
     *     ������������ ��������� � ��������������� ������.
     *  <p>���� ���� �� ��������� ����� ��� <code>java.util.Date</code>
     *     � ������ ������� <code>java.lang.String</code>, �� ��������� �������
     *     ������������� � ��� <code>java.util.Date</code>
     *     �� ������������ ��������� ���������� ������
     *     <code>Date.compareTo(Date)</code>.
     *  <p>���� ���� �� ��������� ����� ��� <code>java.sql.Timestamp</code>
     *     � ������ ������� <code>java.lang.String</code>, �� ��������� �������
     *     ������������� � ��� <code>java.sql.Timestamp</code>
     *     �� ������������ ��������� ���������� ������
     *     <code>java.sql.Timestamp.compareTo(java.sql.Timestamp)</code>.
     *  <p>���� ��� �������� ����� ��� <code>java.lang.String</code>
     *     �� ������������ ��������� ���������� ������
     *     <code>String.compareTo(String)</code>.
     *  <p>���� ������ ������� ����� ��� <code>java.lang.String</code>
     *     �� ������ ������������� � ������ ������� <code>toString()</code>
     *      � ������������ ��������� ���������� ������
     *     <code>String.compareTo(String)</code>.
     *  <p>���� ������ ������� ����� ��� <code>java.lang.String</code>
     *     �� ������ ������������� � ������ ������� <code>toString()</code>
     *      � ������������ ��������� ���������� ������
     *     <code>String.compareTo(String)</code>.
     *  <p> ���� ��������� �������
     *     <code>  firstValue.getClass().equals(secondValue.getClass()) )\</code>
     *     � ����  firstValue.equals(secondValue), �� ������������ 0.
     *  <p> � ��������� ������, �.�. �� ���� �� ������� ���� �� ���������,                   return 0;
     *      ������������ -1.
     * @param firstValue �������� ������� ��������
     * @param secondValue �������� ������� ��������
     * @return ��������� ���������: 0 - �������� �����, 1 - ������ �������
     *   ������ �������, -1 - ������ ������� ������ �������
     */
    protected int compareStd(Object firstValue, Object secondValue) {

        if (firstValue == null && secondValue == null) {
            return 0;
        }
        if (firstValue == null) {
            return -1;
        }
        if (secondValue == null) {
            return 1;
        }

        if (firstValue == secondValue) {
            return 0;
        }

        if (firstValue instanceof BigDecimal) {
            BigDecimal p1 = (BigDecimal) firstValue;
            BigDecimal p2 = MathExpression.toBigDecimal(secondValue);
            return compareBigDecimal(p1, p2);
        }

        if (secondValue instanceof BigDecimal) {
            BigDecimal p1 = MathExpression.toBigDecimal(secondValue);
            BigDecimal p2 = (BigDecimal) firstValue;
            return compareBigDecimal(p1, p2);
        }

        if (firstValue instanceof Double) {
            double p1 = ((Double) firstValue).doubleValue();
            double p2 = MathExpression.toDouble(secondValue);
            return compareDouble(p1, p2);
        }
        if (secondValue instanceof Double) {
            double p2 = ((Double) secondValue).doubleValue();
            double p1 = MathExpression.toDouble(firstValue);
            return compareDouble(p1, p2);
        }

        if (firstValue instanceof Float) {
            double p1 = ((Float) firstValue).doubleValue();
            double p2 = MathExpression.toDouble(secondValue);
            return compareDouble(p1, p2);
        }
        if (secondValue instanceof Float) {
            double p2 = ((Float) secondValue).doubleValue();
            double p1 = MathExpression.toDouble(firstValue);
            return compareDouble(p1, p2);
        }

        if (firstValue instanceof Long) {
            long p1 = ((Long) firstValue).longValue();
            long p2 = MathExpression.toLong(secondValue);
            return compareLong(p1, p2);
        }
        if (secondValue instanceof Long) {
            long p2 = ((Long) secondValue).longValue();
            long p1 = MathExpression.toLong(firstValue);
            return compareLong(p1, p2);
        }

        if (firstValue instanceof Integer || firstValue instanceof Short ||
                firstValue instanceof Byte ||
                secondValue instanceof Integer || secondValue instanceof Short ||
                secondValue instanceof Byte) {
            long p1 = MathExpression.toLong(firstValue);
            long p2 = MathExpression.toLong(secondValue);
            return compareLong(p1, p2);
        }

        if (firstValue instanceof java.util.Date && secondValue instanceof java.util.Date) {
            //java.util.Date d = (java.sql.Date) secondValue;
            return ((java.util.Date) firstValue).compareTo((java.util.Date)secondValue);
        }

        if (firstValue instanceof java.sql.Timestamp && secondValue instanceof java.sql.Timestamp) {
            Timestamp d = (java.sql.Timestamp) secondValue;
            return ((java.sql.Timestamp) firstValue).compareTo(d);
        }

        if (firstValue instanceof java.sql.Timestamp && secondValue instanceof java.util.Date) {
            return ((java.sql.Timestamp) firstValue).compareTo((java.util.Date) secondValue);
        }

        if (firstValue instanceof java.util.Date && secondValue instanceof java.sql.Timestamp) {
            int rc = ((java.sql.Timestamp) secondValue).compareTo((java.util.Date) firstValue);
            if (rc == 0) {
                return 0;
            }
            return -rc;
        }

        if (firstValue instanceof java.sql.Timestamp && secondValue instanceof String) {
            java.sql.Timestamp d = toTimestamp((String) secondValue);
            return ((java.sql.Timestamp) firstValue).compareTo(d);
        }

        if (firstValue instanceof java.lang.String && secondValue instanceof java.sql.Timestamp) {
            java.sql.Timestamp d = toTimestamp((String) firstValue);
            return d.compareTo((Timestamp) secondValue);
        }

        if (firstValue instanceof java.util.Date && secondValue instanceof String) {
            java.sql.Date sd = toSqlDate((String) secondValue);
            java.util.Date d = new java.util.Date(sd.getTime());
            return ((java.util.Date) firstValue).compareTo(d);
        }
        if (firstValue instanceof String && secondValue instanceof java.util.Date) {
            //String s =
            java.sql.Date sd = toSqlDate((String) firstValue);
            java.util.Date d = new java.util.Date(sd.getTime());
            return d.compareTo((java.util.Date) secondValue);
        }



        if (firstValue instanceof String && secondValue instanceof String) {
            return ((String) firstValue).compareTo((String) secondValue);
        }
        if (firstValue instanceof String) {
            return ((String) firstValue).compareTo(secondValue.toString());
        }

        if (secondValue instanceof String) {
            return ((String) firstValue).toString().compareTo((String) secondValue);
        }

        if (firstValue.getClass().equals(secondValue.getClass())) {
            if (firstValue.equals(secondValue)) {
                return 0;
            }
        }

        return -1;

    }

    protected Timestamp toTimestamp(String value) {
            String s = value;
            if ( ! value.contains(":")) {
                s += " 00:00:00";
            }
            return Timestamp.valueOf(s);

    }
    protected java.sql.Date toSqlDate(String value) {
            String s = value;
            if ( value.contains(":")) {
                s += value.substring(0,9);
            }
            return java.sql.Date.valueOf(s);

    }

    /**
     * ���������� ��������� ��������� ���������� ���������.
     * @param op1 ������ �������
     * @param op2 ������ �������
     * @return ��������� ���������: 0 - �������� �����, 1 - ������ �������
     *   ������ �������, -1 - ������ ������� ������ �������
     */
    protected int compareBigDecimal(BigDecimal op1, BigDecimal op2) {
        return op1.compareTo(op2);
    }

    /**
     * ���������� ��������� ��������� ��������� � ��������� ������.
     * @param op1 ������ �������
     * @param op2 ������ �������
     * @return ��������� ���������: 0 - �������� �����, 1 - ������ �������
     *   ������ �������, -1 - ������ ������� ������ �������
     */
    protected int compareDouble(double op1, double op2) {

        if (op1 - op2 > 0) {
            return 1;
        }
        if (op1 - op2 < 0) {
            return -1;
        }
        return 0;
    }

    /**
     * ���������� ��������� ��������� ������� ����� ���������.
     * @param op1 ������ �������
     * @param op2 ������ �������
     * @return ��������� ���������: 0 - �������� �����, 1 - ������ �������
     *   ������ �������, -1 - ������ ������� ������ �������
     */
    protected int compareLong(long op1, long op2) {

        if (op1 - op2 > 0) {
            return 1;
        }
        if (op1 - op2 < 0) {
            return -1;
        }
        return 0;
    }

    ////////////////////////////////////////////////////////////
    /**
     * ������������ �������� ��������� ���� "�����".
     */
    public static class EqualsExpression extends CompareExpression {

        /**
         * ������� ��������� ������ ��� �������� ���������, ��������� �
         * ���������.
         * @param context �������� ���������
         * @param operator �������� ���� {@link tdo.expr.AbstractOperator.EqualsOperator}
         * @param op1 ������ �������
         * @param op2 ������ �������
         */
        public EqualsExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
            setLexType();
        }

        /**
         * ������������� �������� �������� <code>lexType</code>
         * ������ �������� {@link tdo.tools.expr.LexConst#EQ}.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.EQ;
        }

        /**
         * ��������� ��������� ��������� ��� �������� ��������� �����������
         * ���������.<p>
         * ��������� �������� ������� � ������� ��������� � ���������
         * ���������� ����� {@link #eval(java.lang.Object, java.lang.Object),
         * ��������� ��� � �������� ���������� ����������� ��������.
         * @param values ����������� ��������� ���������.
         * @return ��������� ���������.
         * @see #compare(tdo.NamedValues[])
         * @see #eval(java.lang.Object, java.lang.Object)
         */
        @Override
        protected Boolean compare(NamedValues values) {
            boolean b;
            Object v1 = getOp1().getValue(values);
            Object v2 = getOp2().getValue(values);
            b = eval(v1, v2);
            return b;
        }
        /**
         * ��������� ��������� ��������� ��� ��������� ������� ���������
         * ����������� ���������.<p>
         * ��������� �������� ������� � ������� ��������� � ���������
         * ���������� ����� {@link #eval(java.lang.Object, java.lang.Object),
         * ��������� ��� � �������� ���������� ����������� ��������..
         * @param values ������ ����������� ��������� ���������.
         * @return ��������� ���������.
         * @see #compare(tdo.NamedValues)
         * @see #eval(java.lang.Object, java.lang.Object)
         */
        @Override
        protected Boolean compare(NamedValues[] rows) {
            boolean b;
            Object v1 = getOp1().getValue(rows);
            Object v2 = getOp2().getValue(rows);
            b = eval(v1, v2);
            return b;

        }

        /**
         * ���������� ��� �������� �� ���������.
         * ���������� ���������� ������
         * {@link tdo.expr.CompareExpression#compareStd(java.lang.Object, java.lang.Object) }
         * @param v1 ������ �������
         * @param v2 ������ �������
         * @return true ���� �������� �����. false - � ��������� ������.
         */
        protected boolean eval(Object v1, Object v2) {
            return compareStd(v1, v2) == 0 ? true : false;
        }
    }//class EqualsExpression
    /**
     * ������������ �������� �������� �������� �������� �� <code>null</code>.
     */
    public static class IsNullExpression extends CompareExpression {
        /**
         * ������� ��������� ������ ��� �������� ���������, ��������� �
         * ���������.
         * @param context �������� ���������
         * @param operator �������� ���� {@link tdo.expr.AbstractOperator.IsNullOperator}
         * @param op1 ������ �������
         * @param op2 ������ �������
         */
        public IsNullExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
            setLexType();
        }
        /**
         * ������������� �������� �������� <code>lexType</code>
         * ������ �������� {@link tdo.tools.expr.LexConst#ISNULL}.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.ISNULL;
        }
        /**
         * ��������� �������� ������� �������� �� <code>null</code> ���
         * �������� ��������� ����������� ���������.<p>
         * ��������� �������� ������� �������� � ���������
         * ���������� ����� {@link #eval(java.lang.Object, java.lang.Object),
         * ��������� ��� � �������� ������� ��������� ����������� ��������, �
         * � �������� ������� �������� <code>null</code>.
         * @param values ����������� ��������� ���������.
         * @return ��������� ���������. <code>true</code> - ���� ������
         *   ������� ����� <code>null</code>. ����� <code>false</code>.
         * @see #compare(tdo.NamedValues[])
         * @see #eval(java.lang.Object, java.lang.Object)
         */
        @Override
        protected Boolean compare(NamedValues row) {
            boolean b;
            Object v1 = getOp1().getValue(row);
            Object v2 = null;
            b = eval(v1, v2);
            return b;

        }
        /**
         * ��������� �������� ������� �������� �� <code>null</code> ���
         * ��������� ������� ��������� ����������� ���������.<p>
         * ��������� �������� ������� �������� � ���������
         * ���������� ����� {@link #eval(java.lang.Object, java.lang.Object),
         * ��������� ��� � �������� ������� ��������� ����������� ��������, �
         * � �������� ������� �������� <code>null</code>.
         * @param values ����������� ��������� ���������.
         * @return ��������� ���������. <code>true</code> - ���� ������
         *   ������� ����� <code>null</code>. ����� <code>false</code>.
         * @see #compare(tdo.NamedValues)
         * @see #eval(java.lang.Object, java.lang.Object)
         */
        @Override
        protected Boolean compare(NamedValues[] rows) {
            boolean b;
            Object v1 = getOp1().getValue(rows);
            Object v2 = null;
            b = eval(v1, v2);
            return b;
        }
        /**
         * ��������� ������ ������� �� �������� <code>null</code>.
         * @param v1 ����������� �������
         * @param v2 ������ �������, ������ <code>null</code>
         * @return true ���� ������ ������� ������ �<code>null</code>.
         *      false - � ��������� ������.
         */
        protected boolean eval(Object v1, Object v2) {
            return v1 == null ? true : false;
        }
    }//class IsNullExpression
    /**
     * ������������ �������� �������� �������� �������� �� ����������� ��������
     * <code>null</code>.
     */
    public static class IsNotNullExpression extends IsNullExpression {
   /**
         * ������� ��������� ������ ��� �������� ���������, ��������� �
         * ���������.
         * @param context �������� ���������
         * @param operator �������� ���� {@link tdo.expr.AbstractOperator.IsNotNullOperator}
         * @param op1 ������ �������
         * @param op2 ������ �������
         */
        public IsNotNullExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
            setLexType();
        }
        /**
         * ������������� �������� �������� <code>lexType</code>
         * ������ �������� {@link tdo.tools.expr.LexConst#ISNOTNULL}.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.ISNOTNULL;
        }
        /**
         * ��������� ������ ������� �� �������� <code>null</code>.
         * @param v1 ����������� �������
         * @param v2 ������ �������, ������ <code>null</code>
         * @return true ���� ������ ������� �� ������ <code>null</code>.
         *      false - � ��������� ������.
         */
        @Override
        protected boolean eval(Object v1, Object v2) {
            return v1 == null ? false : true;
        }
    }//class IsNotNullExpression
    /**
     * ������������ �������� ��������� ���� "������".
     */
    public static class GreaterExpression extends EqualsExpression {
        /**
         * ������� ��������� ������ ��� �������� ���������, ��������� �
         * ���������.
         * @param context �������� ���������
         * @param operator �������� ���� {@link tdo.expr.AbstractOperator.GreaterOperator}
         * @param op1 ������ �������
         * @param op2 ������ �������
         */
        public GreaterExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * ������������� �������� �������� <code>lexType</code>
         * ������ �������� {@link tdo.tools.expr.LexConst#GT}.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.GT;
        }
        /**
         * ���������, �������� �� �������� ������� �������� �������, ��� ��������
         * �������.
         * ���������� ���������� ������
         * {@link tdo.expr.CompareExpression#compareStd(java.lang.Object, java.lang.Object) }
         * @param v1 ������ �������
         * @param v2 ������ �������
         * @return true ���� ������ ������� ������ �������. false - � ��������� ������.
         */
        @Override
        protected boolean eval(Object v1, Object v2) {
            return compareStd(v1, v2) > 0 ? true : false;
        }
    }//class GreaterExpression
    /**
     * ������������ �������� ��������� ���� "������".
     */
    public static class LessExpression extends EqualsExpression {
        /**
         * ������� ��������� ������ ��� �������� ���������, ��������� �
         * ���������.
         * @param context �������� ���������
         * @param operator �������� ���� {@link tdo.expr.AbstractOperator.LessOperator}
         * @param op1 ������ �������
         * @param op2 ������ �������
         */
        public LessExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * ������������� �������� �������� <code>lexType</code>
         * ������ �������� {@link tdo.tools.expr.LexConst#LT}.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.LT;
        }
        /**
         * ���������, �������� �� �������� ������� �������� �������, ��� ��������
         * �������.
         * ���������� ���������� ������
         * {@link tdo.expr.CompareExpression#compareStd(java.lang.Object, java.lang.Object) }
         * @param v1 ������ �������
         * @param v2 ������ �������
         * @return true ���� ������ ������� ������ �������. false - � ��������� ������.
         */
        @Override
        protected boolean eval(Object v1, Object v2) {
            return compareStd(v1, v2) < 0 ? true : false;
        }
    }//class LessExpression
    /**
     * ������������ �������� ��������� ���� "������ ��� �����".
     */
    public static class GreaterEqualsExpression extends EqualsExpression {
        /**
         * ������� ��������� ������ ��� �������� ���������, ��������� �
         * ���������.
         * @param context �������� ���������
         * @param operator �������� ���� {@link tdo.expr.AbstractOperator.GreaterEqualsOperator}
         * @param op1 ������ �������
         * @param op2 ������ �������
         */
        public GreaterEqualsExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * ������������� �������� �������� <code>lexType</code>
         * ������ �������� {@link tdo.tools.expr.LexConst#GTEQ}.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.GTEQ;
        }
        /**
         * ���������, �������� �� �������� ������� �������� ������� ��� ������,
         * ��� �������� �������.
         * ���������� ���������� ������
         * {@link tdo.expr.CompareExpression#compareStd(java.lang.Object, java.lang.Object) }
         * @param v1 ������ �������
         * @param v2 ������ �������
         * @return true ���� ������ ������� ������ ��� ����� �������. false - � ��������� ������.
         */
        @Override
        protected boolean eval(Object v1, Object v2) {
            int r = compareStd(v1, v2);
            return r >= 0 ? true : false;
        }
    }//class GreaterEqualeExpression
    /**
     * ������������ �������� ��������� ���� "������ ��� �����".
     */
    public static class LessEqualsExpression extends EqualsExpression {
        /**
         * ������� ��������� ������ ��� �������� ���������, ��������� �
         * ���������.
         * @param context �������� ���������
         * @param operator �������� ���� {@link tdo.expr.AbstractOperator.LessEqualsOperator}
         * @param op1 ������ �������
         * @param op2 ������ �������
         */
        public LessEqualsExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * ������������� �������� �������� <code>lexType</code>
         * ������ �������� {@link tdo.tools.expr.LexConst#LTEQ}.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.LTEQ;
        }
        /**
         * ���������, �������� �� �������� ������� �������� ������� ��� ������,
         * ��� �������� �������.
         * ���������� ���������� ������
         * {@link tdo.expr.CompareExpression#compareStd(java.lang.Object, java.lang.Object) }
         * @param v1 ������ �������
         * @param v2 ������ �������
         * @return true ���� ������ ������� ������ ��� ����� �������. false - � ��������� ������.
         */
        @Override
        protected boolean eval(Object v1, Object v2) {
            int r = compareStd(v1, v2);
            return r <= 0 ? true : false;
        }
    }//class LessEqualsExpression
    /**
     * ������������ �������� ��������� ���� "�� �����".
     */
    public static class NotEqualsExpression extends EqualsExpression {
        /**
         * ������� ��������� ������ ��� �������� ���������, ��������� �
         * ���������.
         * @param context �������� ���������
         * @param operator �������� ���� {@link tdo.expr.AbstractOperator.NotEqualsOperator}
         * @param op1 ������ �������
         * @param op2 ������ �������
         */
        public NotEqualsExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * ������������� �������� �������� <code>lexType</code>
         * ������ �������� {@link tdo.tools.expr.LexConst#NOTEQ}.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.NOTEQ;
        }
        /**
         * ��������� �� ����������� ������� �������� �������.
         * ���������� ���������� ������
         * {@link tdo.expr.CompareExpression#compareStd(java.lang.Object, java.lang.Object) }
         * @param v1 ������ �������
         * @param v2 ������ �������
         * @return true ���� ������ ������� �� ����� �������. false - � ��������� ������.
         */
        @Override
        protected boolean eval(Object v1, Object v2) {
            int r = compareStd(v1, v2);
            return r != 0 ? true : false;
        }
    }//class NotEqualsExpression
    /**
     * ������������ ���������� "between" ���������.<p>
     * �������� ��������� ����� ��������� ���������� ��������: <br>
     * <b><i>op1 between op2 and op3</i></b>
     * <p>��� ���������� ��������� ���� �������� ����������� ��� ��� ���������:
     * <ol>
     *   <li>{@link tdo.expr.AbstractOperator.BetweenOperator} . �������� � �����
     *       ����������:
     *       <ol>
     *          <li>op1</li>
     *          <li>op[] - ������, 0-� ��������� �������� �������� op2, � 1-�
     *              op3
     *          </li>
     *       </ol>
     *   </li>
     *   <li>
     *      {@link tdo.expr.CompoundExpression.AndBetweenExpression} , �������
     *      ������ ������ ���������� op2 � op3.
     *   </li>
     * </ol>
     * ����� <code>BetweenExpression</code> ������������, ��� ��������
     * ����������� ��� ����� ����������.<p>
     * � ��������� between ��������� ��� ��������. � �������� ���������� �����
     * ������������ �������������� �����. ������ ������� ������������ �� ������
     * �� �������� "&gt.=" - ������ ��� �����, � ����� � ������� �� �������� "&lt.="
     * ������ ��� �����. �������������� �����, ��� ����, ���������� ���
     * ���������� �������
     * {@link tdo.expr.CompareExpression#compareStd(java.lang.Object, java.lang.Object) } .
     */
    public static class BetweenExpression extends EqualsExpression {
        /**
         * ������� ��������� ������ ��� ��������� ���������, ��������� �
         * ���������. <p>
         *
         * @param context �������� ���������
         * @param operator ������ ����
         *      {@link tdo.expr.AbstractOperator.BetweenOperator}
         * @param op1 ������ �������. ��� �������� ����������� �� ���������
         *   � �������� ��������, ������������ ������ ���������
         * @param op2 ������ �������. ��� �������� ������ ���� �������� ��
         *    ���� ���������: 0-� �������������  <i>����� �������</i> ���������
         *    �������� � 1-� ������� �������������  <i>������ �������</i>
         *    ���������.
         */
        public BetweenExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * ������������� �������� �������� <code>lexType</code>
         * ������ �������� {@link tdo.tools.expr.LexConst#BETWEEN }.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.BETWEEN;
        }
        /**
         * ��������� �������� �������� ������� �������� ��������� �� ���������
         * � �������� ��������, ������������ ������ ��������� ��� ��������
         * ��������� ����������� ���������.<p>
         * ������ ������� ������ ����� �������� ������ ������� �� ���� ���������:
         * <i>����� � ������</i> ������� ���������.
         *
         * @param values ����������� ��������� ���������, ��������,
         * <code>tdo.DataRow</code>
         * @return <code>true</code> , ���� �������� ������� �������� ������ ��� �����
         *  �������� ����� ������� ��������� � ������ ��� ����� �������� ������
         *  ������� ���������. <code>false</code> - � ��������� ������.
         */
        @Override
        protected Boolean compare(NamedValues values) {
            boolean b;
            Object v1 = getOp1().getValue(values);
            Object[] v2 = (Object[]) getOp2().getValue(values);
            b = eval(v1, v2[0]);
            if (b) {
                b = eval1(v1, v2[1]);
            }
            return b;

        }
        /**
         * ��������� �������� �������� ������� �������� ��������� �� ���������
         * � �������� ��������, ������������ ������ ��������� ��� ���������
         * ������� ��������� ����������� ���������.<p>
         * ������ ������� ������ ����� �������� ������ ������� �� ���� ���������:
         * <i>����� � ������</i> ������� ���������.
         *
         * @param values ������ ����������� ��������� ���������, ��������,
         * <code>tdo.DataRow</code>
         * @return <code>true</code> , ���� �������� ������� �������� ������ ��� �����
         *  �������� ����� ������� ��������� � ������ ��� ����� �������� ������
         *  ������� ���������. <code>false</code> - � ��������� ������.
         */
        @Override
        protected Boolean compare(NamedValues[] rows) {
            boolean b;
            Object v1 = getOp1().getValue(rows);
            Object[] v2 = (Object[]) getOp2().getValue(rows);
            b = eval(v1, v2[0]);
            if (b) {
                b = eval1(v1, v2[1]);
            }
            return b;

        }
        /**
         * ���������� �������� ���� �������� � ���������� ��������� ���������. <p>
         * ���������� ���������� �������� ��������� ������
         * {@link tdo.expr.CompareExpression#compareStd(java.lang.Object, java.lang.Object) } .
         *
         * @param v1 ������ ������������ �������
         * @param v2 ������ ������������ �������
         * @return <code>true</code> ����� �������� ������� ��������
         *   ������ ��� ����� �������� ������� ��������� <code>false</code> - �
         *   ��������� ������
         */
        @Override
        protected boolean eval(Object v1, Object v2) {
            int r = compareStd(v1, v2);
            return r >= 0 ? true : false;
        }
        /**
         * ���������� �������� ���� �������� � ���������� ��������� ���������. <p>
         * ���������� ���������� �������� ��������� ������
         * {@link tdo.expr.CompareExpression#compareStd(java.lang.Object, java.lang.Object) } .
         *
         * @param v1 ������ ������������ �������
         * @param v2 ������ ������������ �������
         * @return <code>true</code> ����� �������� ������� ��������
         *   ������ ��� ����� �������� ������� ��������� <code>false</code> - �
         *   ��������� ������
         */
        protected boolean eval1(Object v1, Object v2) {
            int r = compareStd(v1, v2);
            return r <= 0 ? true : false;
        }
    }//class BetweenExpression
    /**
     * ������������ ���������� ���������, ������������ �������� �� ������,
     * ������������ ������ ��������� ��������� �������� ������� ��������
     * ��� ���������.<p>
     * �������������� ����� �� ������������. � ��������� ����� ���������, ��
     * ������ <code>null</code> ����������� ����� <code>toString()</code> �����
     * ����������.
     *
     * @see tdo.expr.AbstractOperator.ContainingOperator
     */
    public static class ContainingExpression extends EqualsExpression {
        /**
         * ������� ��������� ������ ��� �������� ���������,
         * ��������� � ���������.<p>
         *
         * @param context �������� ���������
         * @param operator ������ ����
         *      {@link tdo.expr.AbstractOperator.ContainingOperator}

         * @param op1 ������ �������
         * @param op2 ������ �������
         */
        public ContainingExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * ������������� �������� �������� <code>lexType</code>
         * ������ �������� {@link tdo.tools.expr.LexConst#CONTAINING }.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.CONTAINING;
        }
        /**
         * ���������� ��������� ��������, �������� �� ������ ������,
         * ������������ ������ ���������, ���������, �������� ������ ���������.<p>
         * @param v1 �������� ������� ��������
         * @param v2 �������� ������� ��������
         * @return <code>true</code> ���� ��� �������� �� ����� <code>null</code>
         *      � ������, ���������� ����������� <code>v1.toString()</code>
         *      �������� ���������, ���������� �����������
         *      <code>v2.toString()</code>.
         */
        @Override
        protected boolean eval(Object v1, Object v2) {
            if (v1 == null || v2 == null) {
                return false;
            }
            String s1 = v1.toString();
            String s2 = v2.toString();
            return s1.indexOf(s2) >= 0 ? true : false;
        }
    }//class ContainingExpression
    /**
     * ������������ ���������� ���������, ������������ ���������� �� ������,
     * ������������ ������ ��������� �� ���������� ��������, �������� ������
     * ���������.<p>
     * �������������� ����� �� ������������. � ��������� ����� ���������, ��
     * ������ <code>null</code> ����������� ����� <code>toString()</code> �����
     * ����������.
     *
     * @see tdo.expr.AbstractOperator.StartingWithOperator
     */
    public static class StartingWithExpression extends EqualsExpression {
        /**
         * ������� ��������� ������ ��� �������� ���������,
         * ��������� � ���������.<p>
         *
         * @param context �������� ���������
         * @param operator ������ ����
         *      {@link tdo.expr.AbstractOperator.StartingWithOperator }
         *
         * @param op1 ������ �������
         * @param op2 ������ �������
         */
        public StartingWithExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * ������������� �������� �������� <code>lexType</code>
         * ������ �������� {@link tdo.tools.expr.LexConst#STARTINGWITH }.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.STARTINGWITH;
        }
        /**
         * ���������� ��������� ��������, ���������� �� ������ ������,
         * ������������ ������ ���������, � ���������, �������� ������ ���������.<p>
         * @param v1 �������� ������� ��������
         * @param v2 �������� ������� ��������
         * @return <code>true</code> ���� ��� �������� �� ����� <code>null</code>
         *      � ������, ���������� ����������� <code>v1.toString()</code>
         *      ���������� � ���������, ���������� �����������
         *      <code>v2.toString()</code>.
         */
        @Override
        protected boolean eval(Object v1, Object v2) {
            if (v1 == null || v2 == null) {
                return false;
            }
            String s1 = v1.toString();
            String s2 = v2.toString();
            return s1.indexOf(s2) == 0 ? true : false;
        }
    }//class StaringWithExpression
    /**
     *
     * ������������ ���������� ���������, ������������ ���������� �� ������,
     * ������������ ������ ��������� �� ���������� ��������, �������� ������
     * ���������.<p>
     * �������������� ����� �� ������������. � ��������� ����� ���������, ��
     * ������ <code>null</code> ����������� ����� <code>toString()</code> �����
     * ����������.
     *
     * @see tdo.expr.AbstractOperator.NotStartingWithOperator
     */
    public static class NotStartingWithExpression extends StartingWithExpression {
        /**
         * ������� ��������� ������ ��� �������� ���������,
         * ��������� � ���������.<p>
         *
         * @param context �������� ���������
         * @param operator ������ ����
         *      {@link tdo.expr.AbstractOperator.NotStartingWithOperator }
         *
         * @param op1 ������ �������
         * @param op2 ������ �������
         */
        public NotStartingWithExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * ������������� �������� �������� <code>lexType</code>
         * ������ �������� {@link tdo.tools.expr.LexConst#NOTSTARTINGWITH }.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.NOTSTARTINGWITH;
        }
        /**
         * ���������� ��������� ��������, ���������� �� ������ ������,
         * ������������ ������ ���������, � ���������, �������� ������ ���������.<p>
         * @param v1 �������� ������� ��������
         * @param v2 �������� ������� ��������
         * @return <code>true</code> ���� ���� �� ���� ������� ����� <code>null</code>
         *      ��� ������, ���������� ����������� <code>v1.toString()</code>
         *      �� ���������� � ���������, ���������� �����������
         *      <code>v2.toString()</code>.
         */
        @Override
        protected boolean eval(Object v1, Object v2) {
            return !super.eval(v1, v2);
        }
    }//class NotStaringWithExpression
    /**
     * ������������ ���������� ���������, ������������ �������� �� ������,
     * ������������ ������ ��������� ��������� �������� ������� ��������
     * ��� ���������.<p>
     *
     * �������������� ����� �� ������������. � ��������� ����� ���������, ��
     * ������ <code>null</code> ����������� ����� <code>toString()</code> �����
     * ����������.
     *
     * @see tdo.expr.AbstractOperator.NotContainingOperator
     * @see tdo.expr.CompareExpression.ContainingExpression

     */
    public static class NotContainingExpression extends ContainingExpression {
        /**
         * ������� ��������� ������ ��� �������� ���������,
         * ��������� � ���������.<p>
         *
         * @param context �������� ���������
         * @param operator ������ ����
         *      {@link tdo.expr.AbstractOperator.NotContainingOperator}
         *
         * @param op1 ������ �������
         * @param op2 ������ �������
         */
        public NotContainingExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * ������������� �������� �������� <code>lexType</code>
         * ������ �������� {@link tdo.tools.expr.LexConst#NOTCONTAINING }.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.NOTCONTAINING;
        }
        /**
         * ���������� ��������� ��������, �������� �� ������ ������,
         * ������������ ������ ���������, ���������, �������� ������ ���������.<p>
         * @param v1 �������� ������� ��������
         * @param v2 �������� ������� ��������
         * @return <code>true</code> ���� ���� �� ���� ������� ����� <code>null</code>
         *      ��� ������, ���������� ����������� <code>v1.toString()</code>
         *      �� �������� ���������, ���������� �����������
         *      <code>v2.toString()</code>.
         */
        @Override
        protected boolean eval(Object v1, Object v2) {
            return !super.eval(v1, v2);
        }
    }//class NotContainingExpression
    /**
     * ������������ ���������� ��������� ��������� �������� �� ��������� �������.<p>
     * ��������� ����� ��������� ��������� ����: <br>
     * <b>op1 LIKE op2</b>. <br>
     * ��� op1 � op2 ������ ���� ���������� �����������, ��� ���� op2 ��������
     * <i>��������</i>, ���������� ������� <i>�������</i>  '%' �/���
     * '_' � ����� ������ �������.
     * <p> ������� LIKE ������������� ��� ������������� ���������� ����� �
     * �������� ��������,  �.�. ��� �������� �������� ��������� ����������
     * ��������, ��� �� ����������, ������������� �� ��� �������������� �������.
     * <p>������� <i>�������</i> ����������:
     * <ul>
     *      <li>������ ������������� "_" ���������� ����� ��������� ������;</li>
     *      <li>���� �������� "%" ������������ ����� ������������������ �� k
     *          �������� (��� k ����� ���� ����� ����);
     *      </li>
     *      <li>��� ������ ������� ���������� ���� ����.
     *      </li>
     * </ul>
     * <p><b>������ 1.</b> ���������:
     * <pre>
     *    'Bill Gates' LIKE '%Gates'
     * </pre>
     * ���� <code>true</code> ��� ������������ ���� ���������. ������ ����� ��
     * ��������� ����� <code>LIKE</code> ����� ��������� ����� ������� �����
     * ������ 'Gates'.
     *
     * <p><b>������ 2.</b> ���������:
     * <pre>
     *    'Bill Gates' LIKE 'B%ates'
     * </pre>
     * ���� <code>true</code> ��� ������������ ���� ���������. ������ ����� ��
     * ��������� ����� <code>LIKE</code> ����� ��������� ����� ������� �����
     * �������� 'B' � ������� 'ates', ��������, 'Bob Lates' LIKE 'B%ates'
     * ����� ���� <code>true</code>.
     * ������ 'Gates'.
     *
     * <p><b>������ 3.</b> ���������:
     * <pre>
     *    'Bill Gates' LIKE 'Bill _ates'
     * </pre>
     * ���� <code>true</code> ��� ������������ ���� ���������. ������ ����� ��
     * ��������� ����� <code>LIKE</code> ����� ��������� ����� ������ ������
     * ������� 'G', ��������, 'Bill Lates' LIKE 'Bill _ates' ����� ����
     * <code>true</code>.
     * <p><b>������ 4.</b> ���������:
     * <pre>
     *    'Bob Gates' LIKE 'B% Gate_'
     * </pre>
     * ���� <code>true</code> ��� ������������ ���� ���������. ������ ����� ��
     * ��������� ����� <code>LIKE</code> ����� ��������� ����� ������ �����
     * ������� 'B', ����� ������� ������, ������ 'Gate' � ����� ������.
     *
     * @see tdo.expr.LikeMatcher
     * @see tdo.expr.RegExMatcher
     * @see tdo.expr.CompareExpression.NotLikeExpression
     *
     */
    public static class LikeExpression extends EqualsExpression {

        protected LikeMatcher likeMatcher;
        
        protected String wildCard;
        
        /**
         * ������� ��������� ������ ��� �������� ���������,
         * ��������� � ���������.<p>
         *
         * @param context �������� ���������
         * @param operator ������ ����
         *      {@link tdo.expr.AbstractOperator.LikeOperator}
         *
         * @param op1 ������ �������
         * @param op2 ������ �������
         */
        public LikeExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
            wildCard = "";
        }
        /**
         * ������������� �������� �������� <code>lexType</code>
         * ������ �������� {@link tdo.tools.expr.LexConst#LIKE }.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.LIKE;
        }
        /**
         * ���������� ��������� ��������� ��������� ���������� �������� ��
         * ��������� �������.
         * <p>���� �������� ��������� <code>v1</code> ��� <code>v2</code> �����
         * <code>null</code>, �� � ��������� �������������� ���������� �� ������
         * � ����� {@link tdo.expr.ExpressionContext#COMPAREEXPRESSION} �
         * ������������� ���������� ���� {@link tdo.expr.ErrorExpression}.
         * <p>���� ���� �� �������� ���������� <code>v1</code> ��� <code>v2</code>
         * �� �������� ������� ������ <code>java.lang.String</code>,
         * �� � ��������� �������������� ���������� �� ������
         * � ����� {@link tdo.expr.ExpressionContext#COMPAREEXPRESSION} �
         * ������������� ���������� ���� {@link tdo.expr.ErrorExpression}.
         * @param v1 �������� ������������ �� ������� ��������
         * @param v2 ������ ������, ���������� <i>�������</i>.
         * @return <code>true</code> - ���� ��������� ��������� �� �������
         *      �������. <code>false</code> - � ��������� ������.
         */
        @Override
        protected boolean eval(Object v1, Object v2) {
            String msg;//My 06.03.2012 = "";
            if (v1 == null || v2 == null) {
                msg = "LikeExpression.eval() : Operand cannot be null (" +
                        v1 + " like '" + v2 + "')";
                this.getContext().addError(ExpressionContext.COMPAREEXPRESSION, this, msg);
                throw new ExpressionException(msg);
            }
            if (!(v1 instanceof String && v2 instanceof String)) {
                msg = "LikeExpression.eval() : Operand must be String (" +
                        v1 + " like '" + v2 + "')";
                this.getContext().addError(ExpressionContext.COMPAREEXPRESSION, this, msg);

                throw new ExpressionException(msg);
            }
            if (this.likeMatcher == null || !wildCard.equals(v2)) {
                likeMatcher = new LikeMatcher((String) v2);
                this.wildCard = (String) v2;
            }
            likeMatcher.setTarget(v1.toString());
            boolean r = false;
            if (this.likeMatcher.matches()) {
                r = true;
            }

            return r;
        }
    }//class LikeExpression
    /**
     * ������������ ���������� ��������� NOT ��������� �������� �� ��������� �������.<p>
     * ��������� ����� ��������� �������� ����: <br>
     * <b>op1 NOT LIKE op2</b>. <br>
     * ��� op1 � op2 ������ ���� ���������� �����������, ��� ���� op2 ��������
     * <i>��������</i>, ���������� ������� <i>�������</i>  '%' �/���
     * '_' � ����� ������ �������.
     *
     * @see tdo.expr.CompareExpression.LikeExpression
     * @see tdo.expr.LikeMatcher
     * @see tdo.expr.RegExMatcher
     *
     */
    public static class NotLikeExpression extends LikeExpression {
        /**
         * ������� ��������� ������ ��� �������� ���������,
         * ��������� � ���������.<p>
         *
         * @param context �������� ���������
         * @param operator ������ ����
         *      {@link tdo.expr.AbstractOperator.NotLikeOperator}
         *
         * @param op1 ������ �������
         * @param op2 ������ �������
         */
        public NotLikeExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * ������������� �������� �������� <code>lexType</code>
         * ������ �������� {@link tdo.tools.expr.LexConst#NOYLIKE }.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.NOTLIKE;
        }
        /**
         * ���������� �������� ��������� ��������� ��������� ���������� �������� ��
         * ��������� �������.
         * <p>���� �������� ��������� <code>v1</code> ��� <code>v2</code> �����
         * <code>null</code>, �� � ��������� �������������� ���������� �� ������
         * � ����� {@link tdo.expr.ExpressionContext#COMPAREEXPRESSION} �
         * ������������� ���������� ���� {@link tdo.expr.ErrorExpression}.
         * <p>���� ���� �� �������� ���������� <code>v1</code> ��� <code>v2</code>
         * �� �������� ������� ������ <code>java.lang.String</code>,
         * �� � ��������� �������������� ���������� �� ������
         * � ����� {@link tdo.expr.ExpressionContext#COMPAREEXPRESSION} �
         * ������������� ���������� ���� {@link tdo.expr.ErrorExpression}.
         * @param v1 �������� ������������ �� ������� ��������
         * @param v2 ������ ������, ���������� <i>�������</i>.
         * @return <code>true</code> - ���� ��������� �� �������
         *      �� ������. <code>false</code> - � ��������� ������.
         */
        @Override
        protected boolean eval(Object v1, Object v2) {
            return !super.eval(v1, v2);
        }
    }//class NotLikeExpression
//'B. Gates' regex '(B(ill)?|B.) Gates'
//'Bill Gates' regex '(B(ill)?|B.) Gates'

    /**
     * ������������ ���������� ��������� ��������� �������� �� ���������
     * ����������� ���������.<p>
     * ��������� ����� ��������� �������� ����: <br>
     * <b>op1 RegEx op2</b>. <br>
     * ��� op1 � op2 ������ ���� ���������� �����������, ��� ���� op2 ��������
     * <i>��������</i>, ���������� ���������� ���������.
     * <p>��� ���������� �������� ��������� ������������ ����� 
     * <code>tdo.expr.RegExMatcher</code>, �������������� ����� �������� 
     * ��� <code>java.util.regex</code> API.
     *
     * <p><b>������ 1.</b> ���������:
     * <pre>
     *    'B. Gates' regex '(B(ill)?|B.) Gates'
     * </pre>
     * ������ <code>true</code> ��� ������������ ���� ���������. ����� �����
     * ������ <code>true</code> ���������:
     * <pre>
     *    'Bill Gates' regex '(B(ill)?|B.) Gates'
     * </pre>
     *
     * <p><b>������ 2.</b> ���������:
     * <pre>
     *    '1' RegEx '[0-9]{1,5}'
     *    '12' RegEx '[0-9]{1,5}'
     *    '123' RegEx '[0-9]{1,5}'
     *    '1234' RegEx '[0-9]{1,5}'
     *    '12345' RegEx '[0-9]{1,5}'
     * </pre>
     * ��� ����������� ��������� ���� ��� ���������� <code>true</code>.
     * � ��� ����������� ����� ������� �� ��, ��� ��� ������� ������ ����
     * ������� �  ���������� ���� �� 1 �� 5. ��� ��� ��������� ��������� ����
     * <code>false</code>.
     * <pre>
     *    '1246' RegEx '[0-9]{1,5}'
     * </pre>
     *
     * @see tdo.expr.LikeMatcher
     * @see tdo.expr.RegExMatcher
     * @see tdo.expr.CompareExpression.NotRegExExpression

     * @see tdo.expr.RegExMatcher
     */
    public static class RegExExpression extends EqualsExpression {
        //protected PWildCardMatcher likeMatcher;

        protected RegExMatcher regExMatcher;
        //protected String wildCard;
        protected String regEx;
        /**
         * ������� ��������� ������ ��� �������� ���������,
         * ��������� � ���������.<p>
         *
         * @param context �������� ���������
         * @param operator ������ ����
         *      {@link tdo.expr.AbstractOperator.RegExOperator}
         *
         * @param op1 ������, ����������� �������
         * @param op2 ������ �������, ���������� ��������� ���������� ���������.
         */
        public RegExExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
            regEx = "";
        }
        /**
         * ������������� �������� �������� <code>lexType</code>
         * ������ �������� {@link tdo.tools.expr.LexConst#REGEX }.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.REGEX;
        }
        /**
         * ���������� ��������� ��������� ��������� ���������� �������� ��
         * ��������� ����������� ���������.
         * <p>���� �������� ��������� <code>v1</code> ��� <code>v2</code> �����
         * <code>null</code>, �� � ��������� �������������� ���������� �� ������
         * � ����� {@link tdo.expr.ExpressionContext#COMPAREEXPRESSION} �
         * ������������� ���������� ���� {@link tdo.expr.ErrorExpression}.
         * <p>���� ���� �� �������� ���������� <code>v1</code> ��� <code>v2</code>
         * �� �������� ������� ������ <code>java.lang.String</code>,
         * �� � ��������� �������������� ���������� �� ������
         * � ����� {@link tdo.expr.ExpressionContext#COMPAREEXPRESSION} �
         * ������������� ���������� ���� {@link tdo.expr.ErrorExpression}.
         * @param v1 �������� ������������ �� ����������� ��������� ��������
         * @param v2 ������ ������, ���������� <i>���������� ���������</i>.
         * @return <code>true</code> - ���� ����� ������� �������������
         *      ����������� ���������. <code>false</code> - � ��������� ������.
         */
        @Override
        protected boolean eval(Object v1, Object v2) {
            String msg;//My 06.03.2012 = "";
            if (v1 == null || v2 == null) {
                msg = "RegExExpression.eval() : Operand cannot be null (" +
                        v1 + " like '" + v2 + "')";
                this.getContext().addError(ExpressionContext.COMPAREEXPRESSION, this, msg);
                throw new ExpressionException(msg);
            }
            if (!(v1 instanceof String && v2 instanceof String)) {
                msg = "RegExExpression.eval() : Operand must be String (" +
                        v1 + " like '" + v2 + "')";
                this.getContext().addError(ExpressionContext.COMPAREEXPRESSION, this, msg);
                throw new ExpressionException("RegExExpression.eval() : Operand must be String (" +
                        v1 + " like '" + v2 + "')");
            }

            if (this.regExMatcher == null || !regEx.equals(v2)) {
                regExMatcher = new RegExMatcher((String) v2);
                this.regEx = (String) v2;
            }
            regExMatcher.setTarget(v1.toString());
            boolean r = false;
            if (this.regExMatcher.matches()) {
                r = true;
            }

            return r;
        }
    }//class RegExExpression
    /**
     * ������������ ���������� ��������� <i>���������</i> (NOT) ���������
     * �������� �� ��������� ����������� ���������.<p>
     * ��������� ����� ��������� �������� ����: <br>
     * <b>op1 NOT RegEx op2</b>. <br>
     * ��� op1 � op2 ������ ���� ���������� �����������, ��� ���� op2 ��������
     * <i>��������</i>, ���������� ���������� ���������.
     * <p>��� ���������� �������� ��������� ������������ �����
     * <code>tdo.expr.RegExMatcher</code>, �������������� ����� ��������
     * ��� <code>java.util.regex</code> API.
     * @see tdo.expr.LikeMatcher
     * @see tdo.expr.RegExMatcher
     * @see tdo.expr.CompareExpression.RegExExpression
     * @see tdo.expr.RegExMatcher
     */
    public static class NotRegExExpression extends RegExExpression {
        /**
         * ������� ��������� ������ ��� �������� ���������,
         * ��������� � ���������.<p>
         *
         * @param context �������� ���������
         * @param operator ������ ����
         *      {@link tdo.expr.AbstractOperator.NotRegExOperator}
         *
         * @param op1 ������, ����������� �������
         * @param op2 ������ �������, ���������� ��������� ���������� ���������.
         */
        public NotRegExExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * ������������� �������� �������� <code>lexType</code>
         * ������ �������� {@link tdo.tools.expr.LexConst#NOTREGEX }.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.NOTREGEX;
        }
        /**
         * ���������� <i>��������</i> ��������� ��������� ��������� ����������
         * �������� �� ��������� ����������� ���������.
         * <p>���� �������� ��������� <code>v1</code> ��� <code>v2</code> �����
         * <code>null</code>, �� � ��������� �������������� ���������� �� ������
         * � ����� {@link tdo.expr.ExpressionContext#COMPAREEXPRESSION} �
         * ������������� ���������� ���� {@link tdo.expr.ErrorExpression}.
         * <p>���� ���� �� �������� ���������� <code>v1</code> ��� <code>v2</code>
         * �� �������� ������� ������ <code>java.lang.String</code>,
         * �� � ��������� �������������� ���������� �� ������
         * � ����� {@link tdo.expr.ExpressionContext#COMPAREEXPRESSION} �
         * ������������� ���������� ���� {@link tdo.expr.ErrorExpression}.
         * @param v1 �������� ������������ �� ����������� ��������� ��������
         * @param v2 ������ ������, ���������� <i>���������� ���������</i>.
         * @return <code>true</code> - ���� ����� ������� �� �������������
         *      ����������� ���������. <code>false</code> - � ��������� ������.
         */
        @Override
        protected boolean eval(Object v1, Object v2) {
            return !super.eval(v1, v2);

        }
    }//class NotRegExExpression
    /**
     * ������������ ���������� ��������� IN, ������������� , ���������� ��
     * ��������  � �������� ������.<p>
     * ��������� ����� ��������� �������� ����: <br>
     * <b>5 IN (1,4,6,5)</b>. <br>
     * ������� ���������, ���������� �� ������� ( � ������ ������ ������� 5)
     * � ������.
     * <b>������.</b> ��������, ���������� �� ������ "Test" � ������ ����:
     *   "Mam,Dad,Test"<br>
     * <b>'Test' IN ('Mam','Dad','Test')</b>. <br>
     * ���� <code>true</code>
     *  <p>����������. ������� �������� �������� �� ��, ��� ������� ������,
     *    � ������� �������� ������ �������� �������������.
     * @see tdo.expr.ValueList
     * @see tdo.expr.CompareExpression.NotInExpression
     */
    public static class InExpression extends CompareExpression {
        /**
         * ������� ��������� ������ ��� �������� ���������,
         * ��������� � ���������.<p>
         *
         * @param context �������� ���������
         * @param operator ������ ����
         *      {@link tdo.expr.AbstractOperator.InOperator}
         *
         * @param op1 ������, ����������� �������
         * @param op2 ������ �������, ���������� ������ ���������.
         */
        public InExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * ��������� �������� ��������� � ���������� ��������� ������
         * ������ {@link #eval(java.lang.Object, java.lang.Object) } ��� ����
         * ��������.<p>
         * @param values ����������� ��������� ���������, ��������,
         *  <code>tdo.DataRow</code>.
         * @return ��������� ���������
         */
        @Override
        protected Boolean compare(NamedValues values) {
            boolean b;
            Object v1 = getOp1().getValue(values);
            Object v2 = getOp2().getValue(values);
            b = eval(v1, v2);
            return b;

        }
        /**
         * ��������� �������� ��������� � ���������� ��������� ������
         * ������ {@link #eval(java.lang.Object, java.lang.Object) } ��� ����
         * ��������.<p>
         * @param values ������ ����������� ��������� ���������, ��������,
         *  <code>tdo.DataRow</code>.
         * @return ��������� ���������
         */
        @Override
        protected Boolean compare(NamedValues[] values) {
            boolean b;
            Object v1 = getOp1().getValue(values);
            Object v2 = getOp2().getValue(values);
            b = eval(v1, v2);
            return b;

        }
        /**
         * ������ ������� - ��� ������ ������������� ����. ������ �������
         * ������ ����� ��� <code>tdo.expr.ValueList</code>.<p>
         * ���� �������� ������� �������� ����� <code>null</code>, ��, ����
         * ������ <code>ValueList</code>, �������� <code>null</code>, ��
         * ������������ <code>true</code>.
         * @param v1 ������ ������������� ����
         * @param v2 ������ ���� <code>tdo.expr.ValueList</code>
         * @return <code>true</code> ���� ������ <i>v1</i> ���������� � ������
         *     <i>v2</i>. <code>false</code> - � ��������� ������.
         *
         * @see tdo.expr.ValueList
         */
        protected boolean eval(Object v1, Object v2) {
            boolean b = false;
            if (v2 instanceof ValueList) {
                ValueList values = (ValueList) v2;
                if ( v1 == null) {
                    if ( values.contains(null) )
                        return true;
                    else
                        return false;
                }
                for (int i = 0; i < values.size(); i++) {

                    Object v = values.get(i);
                    int r = compareStd(v1, v);
                    b = r == 0;
                    if (b) {
                        break;
                    }
                }
                return b;
            } else {
                int r = compareStd(v1, v2);
                return r == 0 ? true : false;
            }
        }
        /**
         * ������������� �������� �������� <code>lexType</code>
         * ������ �������� {@link tdo.tools.expr.LexConst#IN }.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.IN;
        }
    }//class InExpression
    /**
     * ������������ ���������� ��������� NOT IN, ������������� , ���������� ��
     * ��������  � �������� ������.<p>
     * ��������� ����� ��������� �������� ����: <br>
     * <b>5 NOT IN (1,4,6)</b>. <br>
     * ������� ���������, ���������� �� ������� ( � ������ ������ ������� 5)
     * � ������.
     * <b>������.</b> ��������, ���������� �� ������ "Test" � ������ ����:
     *   "Mam,Dad,Test"<br>
     * <b>'Test' NOT IN ('Mam','Dad','Test')</b>. <br>
     * ���� <code>false</code>, ��������� � ������ ���� ������� �� ���������
     * "Test".
     *  <p>����������. ������� �������� �������� �� ��, ��� ������� ������,
     *    � ������� �������� ������ �������� �������������.
     * @see tdo.expr.CompareExpression.InExpression
     */
    public static class NotInExpression extends InExpression {
        /**
         * ������� ��������� ������ ��� �������� ���������,
         * ��������� � ���������.<p>
         *
         * @param context �������� ���������
         * @param operator ������ ����
         *      {@link tdo.expr.AbstractOperator.InOperator}
         *
         * @param op1 ������, ����������� �������
         * @param op2 ������ �������, ���������� ������ ���������.
         */
        public NotInExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * ���������� ���������� ������ ����������� � ���������� ���������������
         * ��������.<p>
         * @param v1 ����������� ��������
         * @param v2 ������ ���������
         * @return <code>true</code> ���� ����������� ������� �� ���������� �
         *   ������. <code>false</code> - � ��������� ������.
         */
        @Override
        protected boolean eval(Object v1, Object v2) {
            return !super.eval(v1, v2);
        }
        /**
         * ������������� �������� �������� <code>lexType</code>
         * ������ �������� {@link tdo.tools.expr.LexConst#NOTIN }.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.NOTIN;
        }
    }//class NotInExpression
}//class CompareExpression
