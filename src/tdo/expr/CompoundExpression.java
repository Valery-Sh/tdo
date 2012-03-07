/*
 * CompoundExpression.java
 *
 */
package tdo.expr;

import java.util.Vector;
import tdo.NamedValues;
import tdo.tools.expr.LexConst;

/**
 *  ������� ����� ��� ��������� ���������. <p>
 * ��������� ��������� ���������� �� ������ ���, ��� ��������� ����������� ��
 * ����� ��� ����� ����������, � ������� ���������.
 * <p>� ��������� ���������� ��������� ���������� ���������:
 * <ul>
 *   <li>{@link tdo.expr.CompoundExpression.AndExpression} </li>
 *   <li>{@link tdo.expr.CompoundExpression.OrExpression} </li>
 *   <li>{@link tdo.expr.CompoundExpression.NotExpression} </li>
 *   <li>{@link tdo.expr.CompoundExpression.AndBetweenExpression} </li>
 * </ul>
 *
 * � ����� ��������� <i>"������ ����� �������"</i>, ������������� �������
 * {@link tdo.expr.CompoundExpression.CommaListExpression}
 *
 * 
 */
public abstract class CompoundExpression extends AbstractExpression {

    /**
     * ������������ ��� ���������� ��������� ���������.
     */
    private Vector list;

    /**
     * ������� ��������� ��������� � ������������� ������� ��������� ���������,
     * ������ ����.
     */
    public CompoundExpression() {
        this(5);
    }

    /**
     * ������� ��������� ��������� � ������������� �������� ������� ���������
     * ���������.
     * @param capacity ������� ������ ��������� ���������
     */
    public CompoundExpression(int capacity) {
        list = new Vector(capacity);
        setLexType();
    }

    /**
     * ��������� �������� ������� � ������ ��������� ���������.
     * @param op ����������� �������
     */
    public void add(IOperand op) {
        list.addElement(op);
    }

    /**
     * ���������� ������� ��������� �� ������� � ������ ���������.
     * @param index ������ � ������ ���������
     * @return ������� � �������� ��������
     */
    public IOperand get(int index) {
        return (IOperand) list.elementAt(index);
    }

    /**
     * @return true ����� ������ ��������� ��������� ����
     */
    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    /**
     * @return ������ ������ ���������.
     */
    public int size() {
        return list.size();
    }

    /**
     * ���������� <code>java.lang.Boolean</code>.
     * @return
     */
    @Override
    public Class getType() {
        return Boolean.class;
    }

    /**
     * ������������ ���������� ���������� �������� <code>AND</code> ���
     * ������� ������� ���������.<p>
     * �������������� ������ <code>getValue</code> ������
     * {@link tdo.expr.AbstractExpression}, �������� ������������ ������
     * ���������.
     *
     */
    public static class AndExpression extends CompoundExpression {

        /**
         * ���������� ������� �������� ��������� ��� �������� �����������
         * ��������� ���������.<p>
         * ��������� ��������� ��������� ��������� � ��������� �������� �������
         * ��������. ���� ��������� ������� ����� �������� <code>false</code>,
         * �� ����� ��������� ������ ����� <code>false</code>.
         *
         * @param values ����������� ��������� ���������, ��������,
         *  ��� <code>DataRow</code> �������
         * @return ������� �������� ��������� <code>true</code>, ���� ������ ����
         *      ��� ��� �������� ��������� ��������� ����� �������� <code>true</code>.
         *    ����� ������������� <code>false</code>.
         * @see #getValue(tdo.NamedValues[])
         * @see tdo.DataRow
         * @see tdo.Table
         */
        @Override
        public Object getValue(NamedValues values) {
            boolean b = true;
            for (int i = 0; i < size(); i++) {
                Boolean v = (Boolean) get(i).getValue(values);
                if (!v.booleanValue()) {
                    b = false;
                    break;
                }
            }
            return b;
        }

        /**
         * ���������� ������� �������� ��������� ��� ��������� ������� �����������
         * ��������� ���������.<p>
         * ��������� ��������� ��������� ��������� � ��������� �������� �������
         * ��������. ���� ��������� ������� ����� �������� <code>false</code>,
         * �� ����� ��������� ������ ����� <code>false</code>.
         *
         * @param values ������ ����������� ��������� ���������, ��������,
         *  ��� <code>DataRow</code> �������
         * @return ������� �������� ��������� <code>true</code>, ���� ������ ����
         *      ��� ��� �������� ��������� ��������� ����� �������� <code>true</code>.
         *    ����� ������������� <code>false</code>.
         * @see #getValue(tdo.NamedValues)
         * @see tdo.DataRow
         * @see tdo.Table
         */
        @Override
        public Object getValue(NamedValues[] values) {
            boolean b = true;
            for (int i = 0; i < size(); i++) {
                Boolean v = (Boolean) get(i).getValue(values);
                if (!v.booleanValue()) {
                    b = false;
                    break;
                }
            }
            return b;
        }

        /**
         * ������������� �������� �������� <code>lexType</code> ������
         * {@link tdo.tools.scanner.LScanner#AND } .
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.AND;
        }

        /**
         * ������ ��������� ������������� ������ ���������. <p>
         * ������ ������:
         * <pre>
         *   ( elem-1 AND elem-2 AND ... AND elem-n )
         * </pre>
         * @return
         */
        @Override
        public String toString() {
            String s = "(";
            for (int i = 0; i < size(); i++) {
                s += get(i).toString();
                if (i != size() - 1) {
                    s += " AND ";
                }
            }
            s += ")";
            return s;
        }
    }//class AndExpression

    /**
     * ������������ ���������� ���������� �������� <code>OR</code> ���
     * ������� ������� ���������.<p>
     * �������������� ������ <code>getValue</code> ������
     * {@link tdo.expr.AbstractExpression}, �������� ������������ ������
     * ���������.
     *
     */
    public static class OrExpression extends CompoundExpression {

        /**
         * ���������� ������� �������� ��������� ��� �������� �����������
         * ��������� ���������.<p>
         * ��������� ��������� ��������� ��������� � ��������� �������� �������
         * ��������. ���� ��������� ������� ����� �������� <code>true</code>,
         * �� ����� ��������� ������ ����� <code>true</code>.
         *
         * @param values ����������� ��������� ���������, ��������,
         *  ��� <code>DataRow</code> �������
         * @return ������� �������� ��������� <code>true</code>, ���� ���� ��
         *      ���� ������� ��������� ��������� ����� �������� <code>true</code>.
         *      ����� ������������� <code>false</code>.
         * @see #getValue(tdo.NamedValues[])
         * @see tdo.DataRow
         * @see tdo.Table
         */
        @Override
        public Object getValue(NamedValues values) {
            boolean b = false;
            for (int i = 0; i < size(); i++) {
                Boolean v = (Boolean) get(i).getValue(values);
                if (v.booleanValue()) {
                    b = true;
                    break;
                }
            }
            return b;
        }

        /**
         * ���������� ������� �������� ��������� ��� ��������� ������� �����������
         * ��������� ���������.<p>
         * ��������� ��������� ��������� ��������� � ��������� �������� �������
         * ��������. ���� ��������� ������� ����� �������� <code>true</code>,
         * �� ����� ��������� ������ ����� <code>true</code>.
         *
         * @param values ������ ����������� ��������� ���������, ��������,
         *  ��� <code>DataRow</code> �������
         * @return ������� �������� ��������� <code>true</code>, ���� ���� ��
         *      ���� ������� ��������� ��������� ����� �������� <code>true</code>.
         *      ����� ������������� <code>false</code>.
         * @see #getValue(tdo.NamedValues[])
         * @see tdo.DataRow
         * @see tdo.Table
         */
        @Override
        public Object getValue(NamedValues[] values) {
            boolean b = false;
            for (int i = 0; i < size(); i++) {
                Boolean v = (Boolean) get(i).getValue(values);
                if (v.booleanValue()) {
                    b = true;
                    break;
                }
            }
            return b;
        }

        /**
         * ������������� �������� �������� <code>lexType</code> ������
         * {@link tdo.tools.scanner.LScanner#OR } .
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.OR;
        }

        /**
         * ������ ��������� ������������� ������ ���������. <p>
         * ������ ������:
         * <pre>
         *   ( elem-1 OR elem-2 OR ... OR elem-n )
         * </pre>
         * @return
         */
        @Override
        public String toString() {
            String s = "(";
            for (int i = 0; i < size(); i++) {
                s += get(i).toString();
                if (i != size() - 1) {
                    s += " OR ";
                }
            }
            s += ")";
            return s;
        }
    }//class OrExpression

    /**
     * ������������ ���������� ����� "AND" ���������� ��������
     * <code>BETWEEN</code> .<p>
     * ��������� ����� ��������� ���������� ��������: <br>
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
     *
     *
     */
    public static class AndBetweenExpression extends CompoundExpression {
        /**
         * ���������� ������ ��������, ��������� �� ���� ���������.
         * ������� ������� ������������� �������� ������ ��������. ������ ��������
         * ������� �������� � ������������ "AND" between-���������.
         *
         * @param values ����������� ��������� ���������, ��������,
         *  ��� <code>DataRow</code> �������
         * @return ������ �������� �� ���� ���������
         * @see tdo.expr.AbstractOperator.BetweenOperator
         * @see tdo.expr.AbstractOperator.AndBetweenOperator
         * @see #getValue(tdo.NamedValues[])
         */
        @Override
        public Object getValue(NamedValues values) {
            Object[] o = new Object[]{this.get(0).getValue(values), this.get(1).getValue(values)};
            return o;

        }
        /**
         * ���������� ������ ��������, ��������� �� ���� ���������.
         * ������� ������� ������������� �������� ������ ��������. ������ ��������
         * ������� �������� � ������������ "AND" between-���������.
         *
         * @param values ������ ����������� ��������� ���������, ��������,
         *  ��� <code>DataRow</code> �������
         * @return ������ �������� �� ���� ���������
         * @see tdo.expr.AbstractOperator.BetweenOperator
         * @see tdo.expr.AbstractOperator.AndBetweenOperator
         * @see #getValue(tdo.NamedValues[])
         */
        @Override
        public Object getValue(NamedValues[] values) {
            Object[] o = new Object[]{this.get(0).getValue(values), this.get(1).getValue(values)};
            return o;

        }
        /**
         * @return �������� <code>Object[].class</code>
         */
        @Override
        public Class getType() {
            return Object[].class;
        }

        /**
         * ������������� �������� �������� <code>lexType</code> ������
         * {@link tdo.tools.scanner.LScanner#ANDBETWEEN } .
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.ANDBETWEEN;
        }

        /**
         * ������ ��������� ������������� ��������. <p>
         * ������ ������:
         * <pre>
         *   ( elem-1 AND elem-2 )
         * </pre>
         * @return
         */
        @Override
        public String toString() {
            String s = "(";
            s += get(0).toString();
            s += " AND ";
            s += get(1).toString();
            s += ")";
            return s;
        }
    }//class AndBetweenExpression

    /**
     * ������������ ���������� ��������� ��� ���������
     * {@link tdo.expr.AbstractOperator.CommaOperator}.
     * <p>� �������� ��������� ����� �������������� ������ ���������,
     * ����������� �������. ��� ������ ��������� ���������� ��������� �������
     * ��������� ������� ������. ��������� ���������, ������������ ��������
     * <code>getValue</code> �������� ��������� �������� ����
     * {@link tdo.expr.ValueList} .
     */
    public static class CommaListExpression extends CompoundExpression {
        /**
         * ������������ ��������� �������� ���������
         */
        protected ValueList valueList;

        /**
         * ���������� ��������� �������� ���������, ����������� � ���������
         * �������. <p>
         * ��������� ������ ���������, ��������� ��� ������� �� ��� �������� �
         *  ��������� ��� � ��������� ���� <code>ValueList</code> ������������
         * ��������.
         * @param values ����������� ��������� ���������, ��������,
         *  ��� <code>DataRow</code> �������
         * @return ��������� �������� ���������
         * @see tdo.expr.ValueList .
         * @see #getValue(tdo.NamedValues)
         */
        @Override
        public Object getValue(NamedValues values) {
            valueList = new ValueList(this.size());
            for (int i = 0; i < size(); i++) {
                valueList.add((get(i)).getValue(values));
            }
            return valueList;
        }
        /**
         * ���������� ��������� �������� ���������, ����������� � ���������
         * �������. <p>
         * ��������� ������ ���������, ��������� ��� ������� �� ��� �������� �
         *  ��������� ��� � ��������� ���� <code>ValueList</code> ������������
         * ��������.
         * @param values ������ ����������� ��������� ���������, ��������,
         *  ��� <code>DataRow</code> �������
         * @return ��������� �������� ���������
         * @see tdo.expr.ValueList .
         * @see #getValue(tdo.NamedValues)
         */
        @Override
        public Object getValue(NamedValues[] values) {
            valueList = new ValueList(this.size());
            for (int i = 0; i < size(); i++) {
                valueList.add((get(i)).getValue(values));
            }
            return valueList;
        }
        /**
         * ������������� �������� �������� <code>lexType</code> ������
         * {@link tdo.tools.scanner.LScanner#ANDBETWEEN } .
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.COMMA;
        }
        /**
         * ������ ��������� ������������� ��������. <p>
         * ������ ������:
         * <pre>
         *   ( elem-1,elem-2, ... ,elem-n )
         * </pre>
         * @return
         */
        @Override
        public String toString() {
            String s = "(";
            for (int i = 0; i < size(); i++) {
                s += get(i).toString();
                if (i != size() - 1) {
                    s += ",";
                }
            }
            s += ")";
            return s;
        }
    }//class CommaListExpression
}//CompoundExpression
