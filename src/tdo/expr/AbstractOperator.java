/*
 * AbstractOperator.java
 *
 * Created on 15.06.2007, 8:37:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tdo.expr;

import java.math.BigDecimal;
import java.sql.Timestamp;

import tdo.expr.AbstractExpression.NotExpression;
import tdo.expr.CompareExpression.BetweenExpression;
import tdo.expr.CompareExpression.ContainingExpression;
import tdo.expr.CompareExpression.EqualsExpression;
import tdo.expr.CompareExpression.GreaterEqualsExpression;
import tdo.expr.CompareExpression.GreaterExpression;
import tdo.expr.CompareExpression.InExpression;
import tdo.expr.CompareExpression.IsNotNullExpression;
import tdo.expr.CompareExpression.IsNullExpression;
import tdo.expr.CompareExpression.LessEqualsExpression;
import tdo.expr.CompareExpression.LessExpression;
import tdo.expr.CompareExpression.LikeExpression;
import tdo.expr.CompareExpression.NotContainingExpression;
import tdo.expr.CompareExpression.NotEqualsExpression;
import tdo.expr.CompareExpression.NotInExpression;
import tdo.expr.CompareExpression.NotLikeExpression;
import tdo.expr.CompareExpression.NotRegExExpression;
import tdo.expr.CompareExpression.NotStartingWithExpression;
import tdo.expr.CompareExpression.RegExExpression;
import tdo.expr.CompareExpression.StartingWithExpression;
import tdo.expr.CompoundExpression.AndBetweenExpression;
import tdo.expr.CompoundExpression.AndExpression;
import tdo.expr.CompoundExpression.CommaListExpression;
import tdo.expr.CompoundExpression.OrExpression;
import tdo.expr.MathExpression.AddExpression;
import tdo.expr.MathExpression.ConcatExpression;
import tdo.expr.MathExpression.DivExpression;
import tdo.expr.MathExpression.MultExpression;
import tdo.expr.MathExpression.SubtrExpression;
import tdo.tools.expr.LexConst;

/**
 * ������� ����� ��� ����������� ���� �������, ����������� ��������� <code>IOperator</code>.
 * �������� ���������� - �������� ������������������ ��� �������-����������� ���������
 * ���� <code>IExpression</code>.
 * <p> ����� ��� ��������� ��������� ��������� ����������� �������, ����������
 * ����������, �� ��������� ��������� ������ <code> IOperator</code>, ����������������
 * ��������. ��������, ��� ��������� �������� <b>"+"</b> ��������� ������ ����
 * <code>tdo.expr.AbstractOperator.AddOperator</code>. ������� �������-���������
 * �������� �������� ���������������� ��� ���������. ��������, ����������
 * ���� ��������� �������� ����� ��������� ������� ����
 * <code>tdo.expr.AbstractExpression.AddExpression. �������� ��������� ������������
 * ����������� ������
 * {@link #createExpression(tdo.expr.ExpressionContext, tdo.expr.IOperand, tdo.expr.IOperand)} .
 * ���������� ���������, ����������� �������� ���������, ������ �� ���������
 * ��������� �������� � ��������. ������, ����� ��������� �����������, ���
 * �������� ���� ����������� ����������� ��� �����.
 * <p>��� �������, ������������ �������, ��������� ��������������� �������,
 * ����� ���, �������� ����� ���������. � ����� ������� ���������:
 * {@link #getGroupType(java.lang.Class) } ,
 * {@link #checkCompareOperands(tdo.expr.IOperand, tdo.expr.IOperand)  } ,
 * {@link #checkLiteralOperands(tdo.expr.IOperand, tdo.expr.IOperand)} ,
 * {@link #checkLogicalOperands(tdo.expr.IOperand, tdo.expr.IOperand)} .
 *
 */
public abstract class AbstractOperator implements IOperator {

    private ExpressionContext context;
    /**
     * ��� ���������, ��� �� ��������� � {@link tdo.tools.logexpr#LexConst}.
     */
    public int lexType;
    /**
     * ������ ( ��� ������������ ) ��������. ������������ ��� ���������� ���������
     * ��� ����������� ���������.
     */
    protected IOperand op1;
    /**
     * ������ �������. ������������ ��� ���������� ���������
     * ��� ����������� ���������.
     */
    protected IOperand op2;

    /**
     * ������������� ��������, � ������� �������������� ���������.
     *
     * @param context ��������������� ��������
     */
    @Override
    public void setContext(ExpressionContext context) {
        this.context = context;
    }

    /**
     * ������� ������ ��������� ��� �������� ���������, ��������� � ���� ���������.
     * ��������� �������� � ���������� ����� {@link #op1} � {@link #op2}.
     * ������ ����� �� ������� <code>IExpression</code> � ���������� <code>null</code>.
     * ������ ���������� ������ ������������� ����� ���������.
     */
    @Override
    public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
        this.op1 = op1;
        this.op2 = op2;
        return null;
    }

    /**
     * ������� ������ ��������� ��� �������� ���������, ��������� � �������������
     * ��������.
     * ��������� ������� � ���������� ���� {@link #op1}.
     * ���������� ���������� �������������� ������
     * {@link #createExpression(tdo.expr.ExpressionContext,IOperand,IOperand}
     * �� ��������� ������� ��������, ������ <code>null</code>.
     */
    @Override
    public IExpression createExpression(ExpressionContext context, IOperand op1) {
        return createExpression(context, op1, null);
    }

    /**
     * @return true, ���� ����� �������, ������������ ���������
     *    �������� ������� ����������. false - � ��������� ������. � ������ ������
     *    ������ ���������� <code>false</code>.
     */
    @Override
    public boolean isUnary() {
        return false;
    }

    /**
     * ���������� �����, �������������� ����� ��������� ���, ��� ��������� ������.
     * ��������� ��� - ��� �������� �������, ������������ ��� ��������. ���
     * ������� <code>java.lang.Byte, java.lang.Short, java.lang.Integer,
     * java.lang.Long, java.lang.Float, java.lang.Double, java.math.BigDecimal</code>,
     * � �������� ���������� ���� ��������� ����� {@link tdo.expr.MathExpression} .
     * <p>��� ������ {@link tdo.expr.IdentifierOperand} ��������� ����� ���������
     *    ���� �� �����. ���������� ��� ������� {@link tdo.expr.ParameterOperand},
     *    {@link tdo.expr.ErrorExpression}, <code>java.lang.Boolean</code>,
     *    <code>java.lang.String</code>.
     * <p>��� ������� <code>java.sqlTimestamp, java.sql.Date, java.util.Date
     * </code> ��������� ����� ��������� <code>java.sql.Date</code>.
     *
     * @param c �����, ��� �������� ������������ ��������� ���
     * @return �����, ���������� ��������� �����
     */
    protected Class getGroupType(Class c) {
        Class r = null;
        if (c == null) {
            r = null;
        } else if (c.equals(Integer.class) ||
                c.equals(Long.class) ||
                c.equals(Short.class) ||
                c.equals(Byte.class) ||
                c.equals(Float.class) ||
                c.equals(Double.class) ||
                c.equals(BigDecimal.class) ||
                c.equals(MathExpression.class)) {
            r = MathExpression.class;
        } else if (c.equals(IdentifierOperand.class)) {
            r = IdentifierOperand.class;
        } else if (c.equals(ParameterOperand.class)) {
            r = ParameterOperand.class;
        } else if (c.equals(Boolean.class)) {
            r = Boolean.class;
        } else if (c.equals(Timestamp.class) || c.equals(java.sql.Date.class) ||
                c.equals(java.util.Date.class)) {
            r = java.sql.Date.class;
        } else if (c.equals(String.class)) {
            r = String.class;
        } else if (c.equals(ErrorExpression.class)) {
            r = ErrorExpression.class;
        }
        return r;
    }

    /**
     * ��������� ��� ��������� �� ������������ ������������� � ����������
     * ���������� <code>NOT, AND, OR</code>.
     * ��� ���������� �������� ���������� ����� �������� �������� ���� ��:
     * <ul>
     *   <li>java.lang.Boolean;</li>
     *   <li>tdo.expr.IdentifierOperand;</li>
     *   <li>tdo.expr.ParameterOperand;</li>
     * </ul>
     * @param op1 ������ ����������� �������
     * @param op2 ������ ����������� �������
     * @return true , ����� ��������� ��������� ��� ������������� � ����������
     *   ����������. false - � ��������� ������.
     */
    protected boolean checkLogicalOperands(IOperand op1, IOperand op2) {
        boolean r = false;
        if (op1.getType().equals(Boolean.class)) {
            r = true;
        } else if (op1.getType().equals(IdentifierOperand.class)) {
            r = true;
        } else if (op1.getType().equals(ParameterOperand.class)) {
            r = true;
        }
        if (r) {
            if (op2.getType().equals(Boolean.class)) {
                r = true;
            } else if (op2.getType().equals(IdentifierOperand.class)) {
                r = true;
            } else if (op2.getType().equals(ParameterOperand.class)) {
                r = true;
            } else {
                r = false;
            }
        }

        return r;
    }

    /**
     * ���������, �������� �� ������� �������������� ����������.
     * ������� ��������� � �������������� ����������, ���� ��� �����
     * {@link tdo.expr.IOperand#getType() } ���������� ���� �� �����:
     * <ul>
     *   <li><code>java.lang.Byte</code>;<li>
     *   <li><code>java.lang.Short</code>;<li>
     *   <li><code>java.lang.Integer</code>;<li>
     *   <li><code>java.lang.Long</code>;<li>
     *   <li><code>java.lang.Float</code>;<li>
     *   <li><code>java.lang.Double</code>;<li>
     *   <li><code>java.lang.BigDecimal</code>;<li>
     *   <li><code>tdo.expr.MathExpression.<li>
     * </ul>
     * @param op �������, ��� �������� �����������
     * @return true ���� ������� ������������ ��� ��������������. false -
     *  � ��������� ������
     *
     */
    protected boolean isMathExpression(IOperand op) {
        boolean r = true;
        Class c = op.getType();

        if (c == null) {
            r = false;
        } else if (c.equals(Integer.class) || c.equals(Long.class) ||
                c.equals(Short.class) || c.equals(Byte.class) ||
                c.equals(Float.class) || c.equals(Double.class) ||
                c.equals(BigDecimal.class) || c.equals(MathExpression.class)) {
            r = true;
        }
        return r;
    }

    /**
     * ���������, �������� �� ������� ��������� ����������.
     * ������� ��������� � ��������� ����������, ���� ��� �����
     * {@link tdo.expr.IOperand#getType() } ���������� ���
     *   <code>java.lang.String</code>. <code>null</code> �������� �� ���������
     *  � ��������� ����������
     * @param op �������, ��� �������� �����������
     * @return true ���� ������� ������������ ��� ���������. false -
     *  � ��������� ������
     *
     */
    protected boolean isStringExpression(IOperand op) {
        boolean r = false;
        Class c = op.getType();

        if (c == null) {
            r = false;
        } else if (c.equals(String.class)) {
            r = true;
        }
        return r;
    }

    /**
     * ���������, �������� �� ������� ���������� ��� �����.
     * ������� ��������� � ���������� ��� �����, ���� ��� �����
     * {@link tdo.expr.IOperand#getType() } �� ����� <code>null</code> � �����
     *   ������ ��:
     * <ul>
     *   <li>java.util.Date;</li>
     *   <li>java.sql.Timestamp;</li>
     * </ul>
     * @param op �������, ��� �������� �����������
     * @return true ���� ������� ������������ ��� ��������� ��� �����. false -
     *  � ��������� ������
     *
     */
    protected boolean isDateExpression(IOperand op) {
        boolean r = false;
        Class c = op.getType();

        if (c == null) {
            r = false;
        } else if (c.equals(java.util.Date.class) || c.equals(java.sql.Timestamp.class)) {
            r = true;
        }
        return r;
    }

    /**
     * ���������, �������� �� ������� ���������� ��� �������� �������.
     * ������� ��������� � ���������� ��� ��������, ���� ��� �����
     * {@link tdo.expr.IOperand#getType() } �� ����� <code>null</code> � �����
     *  <code>java.sql.Time;</code>.
     * @param op �������, ��� �������� �����������
     * @return true ���� ������� ������������ ��� ��������� ��� ��������. false -
     *  � ��������� ������
     *
     */
    protected boolean isTimeExpression(IOperand op) {
        boolean r = false;
        Class c = op.getType();

        if (c == null) {
            r = false;
        } else if (c.equals(java.sql.Time.class)) {
            r = true;
        }
        return r;
    }

    /**
     * ���������, �������� �� ������� ������� ����������.
     * ������� ��������� � �������� ���������, ���� ��� �����
     * {@link tdo.expr.IOperand#getType() } �� ����� <code>null</code> � �����
     *  <code>java.lang.Boolean;</code>.
     * @param op �������, ��� �������� �����������
     * @return true ���� ������� ������������ ��� ������� ���������. false -
     *  � ��������� ������
     *
     */
    protected boolean isBooleanExpression(IOperand op) {
        boolean r = false;
        Class c = op.getType();

        if (c == null) {
            r = false;
        } else if (c.equals(Boolean.class)) {
            r = true;
        }
        return r;
    }

    /**
     * ��������� �������� �� ������������ ������������� � ��������� ���������.
     * ������������ � �������, ������������ ��������� ��������� ��� ��������
     * ���������.
     * <p>������� �������� ������������ ��� ������������� � �������� ���������,
     * ���� ��� ����� {@link tdo.expr.IOperand#getType() } ���������� ��������
     * <code>tdo.expr.ErrorExpression</code><p>
     * ���� ����� <code>getType()</code> ��� ����� ��������� ��������� ��������,
     * �������� �� <code>tdo.expr.ErrorExpression</code>, ��
     * ������� �������� ���������� ��� ������������� � �������� ���������,
     * ����� ��������� ���� �� �������:
     * <ol>
     *   <li>���� �� ��� ��� �������� ����� <code>null</code>;</li>
     *   <li>��� ����� ��������� ����� {@link #isMathExpression(tdo.expr.IOperand)
     *       ���������� <code>true</code>;
     *   </li>
     *   <li>��� ������ �� �������� ����� {@link #isMathExpression(tdo.expr.IOperand)
     *       ���������� <code>true</code>, � ��� ������� �����
     *      {@link #isStringExpression(tdo.expr.IOperand) ���������� <code>true</code>;
     *   </li>
     *   <li>��� ����� ��������� ����� {@link #isDateExpression(tdo.expr.IOperand)
     *       ���������� <code>true</code>;
     *   </li>
     *   <li>��� ������ �� �������� ����� {@link #isDateExpression(tdo.expr.IOperand)
     *       ���������� <code>true</code>, � ��� ������� �����
     *      {@link #isStringExpression(tdo.expr.IOperand) ���������� <code>true</code>;
     *   </li>
     *   <li>��� ����� ��������� ����� {@link #isBooleanExpression(tdo.expr.IOperand)
     *       ���������� <code>true</code>;
     *   </li>
     * </ol>
     * @param op1
     * @param op2
     * @return
     */
    protected boolean checkCompareOperands(IOperand op1, IOperand op2) {
        if (op1 == null || op2 == null) {
            return true;
        }

        Class c1 = op1.getType();
        Class c2 = op2.getType();

        if (c1.equals(ErrorExpression.class) || c2.equals(ErrorExpression.class)) {
            return false;
        }

        if (c1 == null || c2 == null) {
            return true;
        }


        boolean r = false;

        if (isMathExpression(op1) && isMathExpression(op2)) {
            r = true;
        } else if (isMathExpression(op1) && isStringExpression(op2)) {
            r = true;
        } else if (isMathExpression(op2) && isStringExpression(op1)) {
            r = true;
        } else if (isStringExpression(op1) && isStringExpression(op2)) {
            r = true;
        } else if (isDateExpression(op1) && isDateExpression(op2)) {
            r = true;
        } else if (isDateExpression(op1) && isStringExpression(op2)) {
            r = true;
        } else if (isStringExpression(op1) && isDateExpression(op2)) {
            r = true;
        } else if (isBooleanExpression(op1) && isBooleanExpression(op2)) {
            r = true;
        }


        return r;

    }
    /*
    protected boolean checkCompareOperands1(IOperand op1, IOperand op2) {
    Class c1 = getGroupType(op1.getType());
    Class c2 = getGroupType(op2.getType());

    boolean r = false;
    if (c1.equals(ErrorExpression.class) || c2.equals(ErrorExpression.class)) {
    return r;
    }

    if (c1.equals(MathExpression.class)) {
    if (c2.equals(MathExpression.class)) {
    r = true;
    } else if (c2.equals(IdentifierOperand.class)) {
    r = true;
    } else if (c2.equals(ParameterOperand.class)) {
    r = true;
    } else if (c2.equals(String.class)) {
    r = this.checkLiteralOperands(op2, op1);
    }
    return r;
    }
    if (c1.equals(IdentifierOperand.class)) {
    if (c2.equals(MathExpression.class)) {
    r = true;
    } else if (c2.equals(IdentifierOperand.class)) {
    r = true;
    } else if (c2.equals(ParameterOperand.class)) {
    r = true;
    } else if (c2.equals(String.class)) {
    r = this.checkLiteralOperands(op2, op1);
    } else if (c2.equals(Boolean.class)) {
    r = true;
    } else if (c2.equals(java.sql.Date.class)) {
    r = true;
    }
    return r;
    }

    if (c1.equals(String.class)) {
    if (c2.equals(MathExpression.class)) {
    r = this.checkLiteralOperands(op1, op2);
    } else if (c2.equals(IdentifierOperand.class)) {
    r = true;
    } else if (c2.equals(ParameterOperand.class)) {
    r = true;
    }
    if (c2.equals(java.sql.Date.class)) {
    r = true;
    } else if (c2.equals(String.class)) {
    r = true;
    }
    return r;
    }
    if (c1.equals(Boolean.class)) {
    if (c2.equals(Boolean.class)) {
    r = true;
    }
    return r;
    }
    if (c1.equals(Boolean.class)) {
    if (c2.equals(Boolean.class)) {
    r = true;
    }
    if (c2.equals(IdentifierOperand.class)) {
    r = true;
    }
    if (c2.equals(ParameterOperand.class)) {
    r = true;
    }
    return r;
    }
    if (c1.equals(java.sql.Date.class)) {
    if (c2.equals(java.sql.Date.class)) {
    r = true;
    }
    if (c2.equals(IdentifierOperand.class)) {
    r = true;
    }
    if (c2.equals(ParameterOperand.class)) {
    r = true;
    }
    return r;
    }
    return r;

    }
     */
    /*    protected boolean checkLiteralOperands(IOperand op1, IOperand op2) {
    boolean r = true;
    if (op1 instanceof LiteralOperand) {
    NamedValues nv = null;
    String s = (String) op1.getValue(nv);
    if (s != null) {
    try {
    Double.parseDouble(s);
    r = true;
    } catch (Exception e) {
    r = false;
    }
    }
    } else {
    r = true;
    }
    return r;
    }
     */

    /**
     * ������������� �������� ���� {@link #lexType}. ��������� ����� ��������
     * � ������������� <code>abstract</code>, �� ������-���������� ������ ���
     * ��������������.
     */
    protected abstract void setLexType();
    // ************************************************************************
    // Arithmetic Operators
    // ************************************************************************
    /**
     * ������������� ��������������� ��������� "+" - ��������.<p>
     * ����� ��������� ������ 80.
     * �������� ��������.
     */
    public static class AddOperator extends AbstractOperator {

        /**
         * ������� ������ ���� {@link AddExpression}, ����������� ���������
         * �������������� �������� ��������, ������������ ����������.
         * ����� ���������������� ������, ������� �� � ������ ������ ���������
         * � ����� {@link tdo.expr.ExpressionContext#MATH_OPERANDTYPE.
         *
         * @return ������ ���� {@link AddExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            IExpression ex = new AddExpression(context, this, op1, op2);
            if (ex.getType().equals(ErrorExpression.class)) {
                context.addError(context.MATH_OPERANDTYPE, ex);
            }
            return ex;
        }

        /**
         * ���������� �������� ���������� ��������� �������� ������ 80.
         */
        @Override
        public int getPriority() {
            return 80;
        }

        /**
         * ������������� �������� {@link #lexType} ������ <code>LexConst.PLUS</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.PLUS;
        }

        /**
         * @return ������ ������, ���������� �������� "+".
         */
        @Override
        public String toString() {
            return "+";
        }
    }//class AddOperator

    /**
     * ������������� ��������������� ��������� "-" - ���������.<p>
     * ����� ��������� ������ 80.
     * �������� ��������.
     */
    public static class SubtrOperator extends AbstractOperator {

        /**
         * ������� ������ ���� {@link SubtrExpression}, ����������� ���������
         * �������������� ��������� ��������, ������������ ����������.
         * ����� ���������������� ������, ������� �� � ������ ������ ���������
         * � ����� {@link tdo.expr.ExpressionContext#MATH_OPERANDTYPE.
         * @return ������ ���� {@link SubtrExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            IExpression ex = new SubtrExpression(context, this, op1, op2);
            if (ex.getType().equals(ErrorExpression.class)) {
                context.addError(context.MATH_OPERANDTYPE, ex);
            }
            return ex;

        }

        /**
         * ���������� �������� ���������� ��������� ��������� ������ 80.
         */
        @Override
        public int getPriority() {
            return 80;
        }

        /**
         * ������������� �������� {@link #lexType} ������ <code>LexConst.MINUS</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.MINUS;
        }

        /**
         * @return ������ ������, ���������� �������� "-".
         */
        @Override
        public String toString() {
            return "-";
        }
    }//class SubtrOperator

    /**
     * ������������� ��������������� ��������� "*" - ���������.<p>
     * ����� ��������� ������ 100.
     * �������� ��������.
     */
    public static class MultOperator extends AbstractOperator {

        /**
         * ������� ������ ���� {@link MultExpression}, ����������� ���������
         * �������������� ��������� ��������, ������������ ����������.
         * ����� ���������������� ������, ������� �� � ������ ������ ���������
         * � ����� {@link tdo.expr.ExpressionContext#MATH_OPERANDTYPE.
         * @return ������ ���� {@link MultExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            IExpression ex = new MultExpression(context, this, op1, op2);
            if (ex.getType().equals(ErrorExpression.class)) {
                context.addError(context.MATH_OPERANDTYPE, ex);
            }
            return ex;

        }

        /**
         * ���������� �������� ���������� ��������� ��������� ������ 100.
         */
        @Override
        public int getPriority() {
            return 100;
        }

        /**
         * ������������� �������� {@link #lexType} ������ <code>LexConst.MULT</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.MULT;
        }

        /**
         * @return ������ ������, ���������� �������� "*".
         */
        @Override
        public String toString() {
            return "*";
        }
    }////class MultOperator

    /**
     * ������������� ��������������� ��������� "/" - �������.<p>
     * ����� ��������� ������ 100.
     * �������� ��������.
     */
    public static class DivOperator extends AbstractOperator {

        /**
         * ������� ������ ���� {@link DivExpression}, ����������� ���������
         * �������������� ������� ��������, ������������ ����������.
         * ����� ���������������� ������, ������� �� � ������ ������ ���������
         * � ����� {@link tdo.expr.ExpressionContext#MATH_OPERANDTYPE.
         * @return ������ ���� {@link DivExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {

            super.createExpression(context, op1, op2);
            IExpression ex = new DivExpression(context, this, op1, op2);
            if (ex.getType().equals(ErrorExpression.class)) {
                context.addError(context.MATH_OPERANDTYPE, ex);
            }
            return ex;

        }

        /**
         * ���������� �������� ���������� ��������� ������� ������ 100.
         */
        @Override
        public int getPriority() {
            return 100;
        }

        /**
         * ������������� �������� {@link #lexType} ������ <code>LexConst.DIV</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.DIV;
        }

        /**
         * @return ������ ������, ���������� �������� "/".
         */
        @Override
        public String toString() {
            return "/";
        }
    }

    /**
     * �������������  ��������� ������������ "||".<p>
     * ����� ��������� ������ 80.
     * �������� ��������.
     */
    public static class ConcatOperator extends AbstractOperator {

        /**
         * ������� ������ ���� {@link ConcatExpression}, ����������� ���������
         * ��������� ��������� ��������, ������������ ����������.
         * @return ������ ���� {@link ConcatExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            return new ConcatExpression(context, this, op1, op2);
        }

        /**
         * ���������� �������� ���������� ��������� ������������ ������ 80.
         */
        @Override
        public int getPriority() {
            return 80;
        }

        /**
         * ������������� �������� {@link #lexType} ������ <code>LexConst.CONCAT</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.CONCAT;
        }

        /**
         * @return ������ ������, ���������� �������� "||".
         */
        @Override
        public String toString() {
            return "||";
        }
    }//class ConcatOperator

    /**
     * ������������� ��������� ��������� "=" - �����.<p>
     * ����� ��������� ������ 60.
     * �������� ��������.
     */
    public static class EqualsOperator extends AbstractOperator {

        /**
         * ������� ������ ���� {@link EqualsExpression}, ����������� ���������
         * ��������� �� ����� ��������, ������������ ����������.
         * ����� ���������������� ������, ������� �� � ������ ������ ���������
         * � ����� {@link tdo.expr.ExpressionContext#OPERANDTYPE.
         * @return ������ ���� {@link EqualsExpression}.
         * @see AbstractOperator#checkCompareOperands(tdo.expr.IOperand, tdo.expr.IOperand)
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            IExpression ex = new EqualsExpression(context, this, op1, op2);
            if (!this.checkCompareOperands(op1, op2)) {
                context.addError(context.OPERANDTYPE, ex);
            }
            return ex;
        }

        /**
         * ���������� �������� ���������� ��������� ��������� "�����" ������ 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * ������������� �������� {@link #lexType} ������ <code>LexConst.EQ</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.EQ;
        }

        /**
         * @return ������ ������, ���������� �������� "=".
         */
        @Override
        public String toString() {
            return "=";
        }
    }//class EqualsOperator

    /**
     * ������������� ��������� ��������� <pre>">"</pre> - ������ ������.<p>
     * ����� ��������� ������ 60.
     * �������� ��������.
     */
    public static class GreaterOperator extends AbstractOperator {

        /**
         * ������� ������ ���� {@link GreaterExpression}, ����������� ���������
         * ��������� �� ������ ������ ��������, ������������ ����������.
         * ����� ���������������� ������, ������� �� � ������ ������ ���������
         * � ����� {@link tdo.expr.ExpressionContext#OPERANDTYPE.
         * @return ������ ���� {@link GreaterExpression}.
         * @see AbstractOperator#checkCompareOperands(tdo.expr.IOperand, tdo.expr.IOperand)
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            IExpression ex = new GreaterExpression(context, this, op1, op2);
            if (!this.checkCompareOperands(op1, op2)) {
                context.addError(context.OPERANDTYPE, ex);
            }
            return ex;
        }

        /**
         * ���������� �������� ���������� ��������� ��������� "������" ������ 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * ������������� �������� {@link #lexType} ������ <code>LexConst.GT</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.GT;
        }

        /**
         * @return ������ ������, ���������� �������� <pre>">"</pre>.
         */
        @Override
        public String toString() {
            return ">";
        }
    }//class GreaterOperator

    /**
     * ������������� ��������� ��������� <pre>"<"</pre> - ������ ������.<p>
     * ����� ��������� ������ 60.
     * �������� ��������.
     */
    public static class LessOperator extends AbstractOperator {

        /**
         * ������� ������ ���� {@link LessExpression}, ����������� ���������
         * ��������� �� ������ ������ ��������, ������������ ����������.
         * ����� ���������������� ������, ������� �� � ������ ������ ���������
         * � ����� {@link tdo.expr.ExpressionContext#OPERANDTYPE.
         * @return ������ ���� {@link LessExpression}.
         * @see AbstractOperator#checkCompareOperands(tdo.expr.IOperand, tdo.expr.IOperand)
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            IExpression ex = new LessExpression(context, this, op1, op2);
            if (!this.checkCompareOperands(op1, op2)) {
                context.addError(context.OPERANDTYPE, ex);
            }
            return ex;

        }

        /**
         * ���������� �������� ���������� ��������� ��������� "������ ������" ������ 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * ������������� �������� {@link #lexType} ������ <code>LexConst.LT</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.LT;
        }

        /**
         * @return ������ ������, ���������� �������� <pre>"<"</pre>.
         */
        @Override
        public String toString() {
            return "<";
        }
    }//class LessOperator

    /**
     * ������������� ��������� ��������� <pre>">="</pre> - ������ ��� �����.<p>
     * ����� ��������� ������ 60.
     * �������� ��������.
     */
    public static class GreaterEqualsOperator extends AbstractOperator {

        /**
         * ������� ������ ���� {@link GreaterEqualsExpression}, ����������� ���������
         * ��������� �� ������ ��� ����� ��������, ������������ ����������.
         * ����� ���������������� ������, ������� �� � ������ ������ ���������
         * � ����� {@link tdo.expr.ExpressionContext#OPERANDTYPE.
         * @return ������ ���� {@link GreaterEqualsExpression}.
         * @see AbstractOperator#checkCompareOperands(tdo.expr.IOperand, tdo.expr.IOperand)
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            IExpression ex = new GreaterEqualsExpression(context, this, op1, op2);
            if (!this.checkCompareOperands(op1, op2)) {
                context.addError(context.OPERANDTYPE, ex);
            }
            return ex;

        }

        /**
         * ���������� �������� ���������� ��������� ��������� "������ ��� �����" ������ 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * ������������� �������� {@link #lexType} ������ <code>LexConst.GTEQ</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.GTEQ;
        }

        /**
         * @return ������ ������, ���������� �������� <pre>">="</pre>.
         */
        @Override
        public String toString() {
            return ">=";
        }
    }//class GreaterEqualsOperator

    /**
     * ������������� ��������� ��������� <pre>"<="</pre> - ������ ��� �����.<p>
     * ����� ��������� ������ 60.
     * �������� ��������.
     */
    public static class LessEqualsOperator extends AbstractOperator {

        /**
         * ������� ������ ���� {@link LessEqualsExpression}, ����������� ���������
         * ��������� �� ������ ��� ����� ��������, ������������ ����������.
         * ����� ���������������� ������, ������� �� � ������ ������ ���������
         * � ����� {@link tdo.expr.ExpressionContext#OPERANDTYPE.
         * @return ������ ���� {@link LessEqualsExpression}.
         * @see AbstractOperator#checkCompareOperands(tdo.expr.IOperand, tdo.expr.IOperand)
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            IExpression ex = new LessEqualsExpression(context, this, op1, op2);
            if (!this.checkCompareOperands(op1, op2)) {
                context.addError(context.OPERANDTYPE, ex);
            }
            return ex;

        }

        /**
         * ���������� �������� ���������� ��������� ��������� "������ ��� �����" ������ 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * ������������� �������� {@link #lexType} ������ <code>LexConst.LTEQ</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.LTEQ;
        }

        /**
         * @return ������ ������, ���������� �������� <pre>"<="</pre>.
         */
        @Override
        public String toString() {
            return "<=";
        }
    }//class LessEqualsOperator

    /**
     * ������������� ��������� ��������� <pre>"between"</pre> - "�����".<p>
     * ����� ��������� ������ 60.
     * �������� ��������.
     */
    public static class BetweenOperator extends AbstractOperator {

        /**
         * ������� ������ ���� {@link BetweenExpression}, ����������� ���������
         * ��������� � ��������� ��������, ������������ ����������.
         * @return ������ ���� {@link BetweenExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            return new BetweenExpression(context, this, op1, op2);
        }

        /**
         * ���������� �������� ���������� ��������� ��������� � ��������� ������ 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * ������������� �������� {@link #lexType} ������ <code>LexConst.BETWEEN</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.BETWEEN;
        }

        /**
         * @return ������ ������, ���������� �������� <pre>"BETWEEN"</pre>.
         */
        @Override
        public String toString() {
            return "BETWEEN";
        }
    }//class BetweenOperator

    /**
     * ������������� ��������� ��������� <pre>"<>"</pre> - "�� �����".<p>
     * ����� ��������� ������ 60.
     * �������� ��������.
     */
    public static class NotEqualsOperator extends AbstractOperator {

        /**
         * ������� ������ ���� {@link NotEqualsExpression}, ����������� ���������
         * ��������� �� �� ����� ��������, ������������ ����������.
         * ����� ���������������� ������, ������� �� � ������ ������ ���������
         * � ����� {@link tdo.expr.ExpressionContext#OPERANDTYPE.
         * @return ������ ���� {@link NotEqualsExpression}.
         * @see AbstractOperator#checkCompareOperands(tdo.expr.IOperand, tdo.expr.IOperand)
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            IExpression ex = new NotEqualsExpression(context, this, op1, op2);
            if (!this.checkCompareOperands(op1, op2)) {
                context.addError(context.OPERANDTYPE, ex);
            }
            return ex;

        }

        /**
         * ���������� �������� ���������� ��������� ��������� ������ 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * ������������� �������� {@link #lexType} ������ <code>LexConst.NOTEQ</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.NOTEQ;
        }

        /**
         * @return ������ ������, ���������� �������� <pre>"<>"</pre>.
         */
        @Override
        public String toString() {
            return "<>";
        }
    }//class NotEqualsOperator

    /**
     * ������������� ��������� ��������� <pre>"IS NULL"</pre> - "����� null".<p>
     * ����� ��������� ������ 60.
     * �������� ��������.
     */
    public static class IsNullOperator extends AbstractOperator {

        /**
         * ������� ������ ���� {@link IsNullExpression}, ����������� ���������
         * ��������� �� ����� <code>null</code> ��������, ������������ ����������.
         * @return ������ ���� {@link IsNullExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            return new IsNullExpression(context, this, op1, op2);
        }

        /**
         * ���������� �������� ���������� ��������� ��������� ������ 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * ������������� �������� {@link #lexType} ������ <code>LexConst.ISNULL</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.ISNULL;
        }

        /**
         * @return ������ ������, ���������� �������� <pre>"IS"</pre>.
         */
        @Override
        public String toString() {
            return "IS";
        }
    }//class IsNullOperator

    /**
     * ������������� ��������� ��������� <pre>"IS NOT NULL"</pre> - "�� ����� null".<p>
     * ����� ��������� ������ 60.
     * �������� ��������.
     */
    public static class IsNotNullOperator extends AbstractOperator {

        /**
         * ������� ������ ���� {@link IsNotNullExpression}, ����������� ���������
         * ��������� �� �� ����� <code>null</code> ��������, ������������ ����������.
         * @return ������ ���� {@link IsNotNullExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            return new IsNotNullExpression(context, this, op1, op2);
        }

        /**
         * ���������� �������� ���������� ��������� ��������� ������ 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * ������������� �������� {@link #lexType} ������ <code>LexConst.ISNOTNULL</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.ISNOTNULL;
        }

        /**
         * @return ������ ������, ���������� �������� <pre>"IS NOT"</pre>.
         */
        @Override
        public String toString() {
            return "IS NOT";
        }
    }//class IsNotNullOperator

    /**
     * ������������� ��������� ��������� <pre>"CANTAINING"</pre> .<p>
     * ����� ��������� ������ 60.
     * �������� ��������.
     */
    public static class ContainingOperator extends AbstractOperator {

        /**
         * ������� ������ ���� {@link ContainingExpression}, ����������� ���������
         * �������� �� ������� �������� �� ������ �������� ������ ��� ��������� ������� ��������.
         * @return ������ ���� {@link ContainingExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            return new ContainingExpression(context, this, op1, op2);
        }

        /**
         * ���������� �������� ���������� ��������� ��������� ������ 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * ������������� �������� {@link #lexType} ������ <code>LexConst.CONTAINING</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.CONTAINING;
        }

        /**
         * @return ������ ������, ���������� �������� <pre>"CONTAINING"</pre>.
         */
        @Override
        public String toString() {
            return "CONTAINING";
        }
    }//class ContainingOperator

    /**
     * ������������� ��������� ��������� <pre>"NOT CANTAINING"</pre> .<p>
     * ����� ��������� ������ 60.
     * �������� ��������.
     */
    public static class NotContainingOperator extends AbstractOperator {

        /**
         * ������� ������ ���� {@link NotContainingExpression}, ����������� ���������
         * �������� �� ���������� �������� �� ������ �������� ������ ��� ��������� ������� 
         * ��������.
         * @return ������ ���� {@link NotContainingExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            return new NotContainingExpression(context, this, op1, op2);
        }

        /**
         * ���������� �������� ���������� ��������� ��������� ������ 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * ������������� �������� {@link #lexType} ������ <code>LexConst.NOTCONTAINING</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.NOTCONTAINING;
        }

        /**
         * @return ������ ������, ���������� �������� <pre>"NOT CONTAINING"</pre>.
         */
        @Override
        public String toString() {
            return "NOT CONTAINING";
        }
    }//class ContainingOperator

    /**
     * ������������� ��������� ��������� <pre>"STARTING WITH"</pre> .<p>
     * ����� ��������� ������ 60.
     * �������� ��������.
     */
    public static class StartingWithOperator extends AbstractOperator {

        /**
         * ������� ������ ���� {@link StartingWithExpression}, ����������� ���������
         * �������� ���������� �� ������, ������������ ������ ���������� 
         * � ��������� ��������  ������ ����������.
         * @return ������ ���� {@link StartingWithExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            return new StartingWithExpression(context, this, op1, op2);
        }

        /**
         * ���������� �������� ���������� ��������� ��������� ������ 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * ������������� �������� {@link #lexType} ������ <code>LexConst.STARTINGWITH</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.STARTINGWITH;
        }

        /**
         * @return ������ ������, ���������� �������� <pre>"STARTING"</pre>.
         */
        @Override
        public String toString() {
            return "STARTING";
        }
    }//class StaringWithOperator

    /**
     * ������������� ��������� ��������� <pre>"NOT STARTING WITH"</pre> .<p>
     * ����� ��������� ������ 60.
     * �������� ��������.
     */
    public static class NotStartingWithOperator extends AbstractOperator {

        /**
         * ������� ������ ���� {@link NotStartingWithExpression}, ����������� ���������
         * �������� �� ������������ ������ �������� ������, �������� ������ ���������
         * � ����������, �������� ������ ���������.
         * @return ������ ���� {@link NotStartingWithExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            return new NotStartingWithExpression(context, this, op1, op2);
        }

        /**
         * ���������� �������� ���������� ��������� ��������� ������ 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * ������������� �������� {@link #lexType} ������ <code>LexConst.NOTSTARTINGWITH</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.NOTSTARTINGWITH;
        }

        /**
         * @return ������ ������, ���������� �������� <pre>"NOT STARTING"</pre>.
         */
        @Override
        public String toString() {
            return "NOT STARTING";
        }
    }//class NotStaringWithOperator

    // *********************************************************************
    /**
     * ������������� ������������ ���������  <pre>"AND"</pre> .<p>
     * ����� ��������� ������ 40.
     * �������� ��������.
     */
    public static class AndOperator extends AbstractOperator {

        /**
         * ������� ������ ���� {@link AndExpression}, ����������� ���������
         * �������� ���� ���������, ��������� ������ "�".
         * ����� ���������������� ������, ������� �� � ������ ������ ���������
         * � ����� {@link tdo.expr.ExpressionContext#OPERANDTYPE.
         * @return ������ ���� {@link AndExpression}.
         * @see AbstractOperator#checkLogicalOperands(tdo.expr.IOperand, tdo.expr.IOperand)
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            CompoundExpression ex = new AndExpression();
            ex.setContext(context);
            ex.add(op1);
            ex.add(op2);
            if (!this.checkLogicalOperands(op1, op2)) {
                context.addError(context.OPERANDTYPE, ex);
            }
            return ex;
        }

        /**
         * ���������� �������� ���������� ��������� ��������� ������ 40.
         */
        @Override
        public int getPriority() {
            return 40;
        }

        /**
         * ������������� �������� {@link #lexType} ������ <code>LexConst.AND</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.AND;
        }

        /**
         * @return ������ ������, ���������� �������� <pre>"AND"</pre>.
         */
        @Override
        public String toString() {
            return "AND";
        }
    }//class AndOperator

    /**
     * ������������� ���������  <pre>"AND"</pre>, ��������� � ������ ��������� <code>BETWEEN</code> .<p>
     * ����� ��������� ������ 70.
     * �������� ��������.
     */
    public static class AndBetweenOperator extends AbstractOperator {

        /**
         * ������� ������ ���� {@link AndBetweenExpression}, ����������� �������
         * �� ���� ��������� ������ �������� ��� ��������� <code>BETWEEN</code>.
         * @return ������ ���� {@link AndBetweenExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            CompoundExpression ce = new AndBetweenExpression();
            ce.setContext(context);
            ce.add(op1);
            ce.add(op2);
            return ce;
        }

        /**
         * ���������� �������� ���������� ��������� ��������� ������ 70.
         */
        @Override
        public int getPriority() {
            return 70;
        }

        /**
         * ������������� �������� {@link #lexType} ������ <code>LexConst.ANDBETWEEN</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.ANDBETWEEN;
        }

        /**
         * @return ������ ������, ���������� �������� <pre>"AND"</pre>.
         */
        @Override
        public String toString() {
            return "AND";
        }
    }//class AndBetweenOperator

    /**
     * ������������� ������������ ���������  <pre>"OR"</pre> .<p>
     * ����� ��������� ������ 30.
     * �������� ��������.
     */
    public static class OrOperator extends AbstractOperator {

        /**
         * ������� ������ ���� {@link OrExpression}, ����������� ���������
         * �������� ���� ���������, ��������� ������ "���".
         * ����� ���������������� ������, ������� �� � ������ ������ ���������
         * � ����� {@link tdo.expr.ExpressionContext#OPERANDTYPE.
         * @return ������ ���� {@link OrExpression}.
         * @see AbstractOperator#checkLogicalOperands(tdo.expr.IOperand, tdo.expr.IOperand)
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            CompoundExpression ex = new OrExpression();
            ex.setContext(context);
            ex.add(op1);
            ex.add(op2);
            if (!this.checkLogicalOperands(op1, op2)) {
                context.addError(context.OPERANDTYPE, ex);
            }
            return ex;
        }

        /**
         * ���������� �������� ���������� ��������� ��������� ������ 30.
         */
        @Override
        public int getPriority() {
            return 30;
        }

        /**
         * ������������� �������� {@link #lexType} ������ <code>LexConst.OR</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.OR;
        }

        /**
         * @return ������ ������, ���������� �������� <pre>"OR"</pre>.
         */
        @Override
        public String toString() {
            return "OR";
        }
    }//class OrOperator

    /**
     * ������������� ������������ ��������� ��������� <pre>"NOT"</pre> .<p>
     * ����� ��������� ������ 50.
     * �������� �������.
     */
    public static class NotOperator extends AbstractOperator {

        /**
         * ������� ������ ���� {@link NotExpression}, ����������� ���������
         * �������� ����������� ��������� ��� ���������.
         * @param op �������� 
         * @return ������ ���� {@link NotExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op) {
            super.createExpression(context, op);
            AbstractExpression expr = new NotExpression(op);
            expr.setContext(context);
            return expr;
        }

        /**
         * ���������� �������� ���������� ��������� ��������� ������ 50.
         */
        @Override
        public int getPriority() {
            return 50;
        }

        /**
         * @return true �����������, ��� ������ �������� �������� <i>�������</i>.
         */
        @Override
        public boolean isUnary() {
            return true;
        }

        /**
         * ������������� �������� {@link #lexType} ������ <code>LexConst.NOT</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.NOT;
        }

        /**
         * @return ������ ������, ���������� �������� <pre>"NOT"</pre>.
         */
        @Override
        public String toString() {
            return "NOT";
        }
    }//class NotOperator

    /**
     * �������������  ��������� ��������� <pre>"LIKE"</pre> - �������� �� ������������ �������.<p>
     * ����� ��������� ������ 60.
     * �������� ��������.
     */
    public static class LikeOperator extends AbstractOperator {

        /**
         * ������� ������ ���� {@link LikeExpression}, ����������� ���������
         * �������� ��������� ������� �������� �� ������������ �������, ������������� ������ ���������.
         * @param op1 ������, ����������� �������
         * @param op2 ������ �������� - ������ (wildcard).
         * @return ������ ���� {@link LikeExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            return new LikeExpression(context, this, op1, op2);
        }

        /**
         * ���������� �������� ���������� ��������� ��������� ������ 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * ������������� �������� {@link #lexType} ������ <code>LexConst.LIKE</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.LIKE;
        }

        /**
         * @return ������ ������, ���������� �������� <pre>"LIKE"</pre>.
         */
        @Override
        public String toString() {
            return "LIKE";
        }
    }//class LikeOperator

    /**
     * �������������  ��������� ��������� <pre>"NOT LIKE"</pre> - �������� �� �������������� �������.<p>
     * ����� ��������� ������ 60.
     * �������� ��������.
     */
    public static class NotLikeOperator extends AbstractOperator {

        /**
         * ������� ������ ���� {@link NotLikeExpression}, ����������� ���������
         * �������� ��������� ������� �������� �� �������������� �������, ������������� ������ ���������.
         * @param op1 ������, ����������� �������
         * @param op2 ������ �������� - ������ (wildcard).
         * @return ������ ���� {@link NotLikeExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            return new NotLikeExpression(context, this, op1, op2);
        }

        /**
         * ���������� �������� ���������� ��������� ��������� ������ 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * ������������� �������� {@link #lexType} ������ <code>LexConst.NOTLIKE</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.NOTLIKE;
        }

        /**
         * @return ������ ������, ���������� �������� <pre>"NOT LIKE"</pre>.
         */
        @Override
        public String toString() {
            return "NOT LIKE";
        }
    }//class NotLikeOperator

    /**
     * �������������  ��������� ��������� <pre>"REGEX"</pre> - �������� �� ������������ ����������� ���������.<p>
     * ����� ��������� ������ 60.
     * �������� ��������.
     */
    public static class RegExOperator extends AbstractOperator {

        /**
         * ������� ������ ���� {@link RegExExpression}, ����������� ���������
         * �������� ��������� ������� �������� �� ������������ ����������� ���������,
         * ������������� ������ ���������.
         * @param op1 ������, ����������� �������
         * @param op2 ������ �������� - ���������� ���������.
         * @return ������ ���� {@link RegExExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            return new RegExExpression(context, this, op1, op2);
        }

        /**
         * ���������� �������� ���������� ��������� ��������� ������ 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * ������������� �������� {@link #lexType} ������ <code>LexConst.REGEX</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.REGEX;
        }

        /**
         * @return ������ ������, ���������� �������� <pre>"REGEX"</pre>.
         */
        @Override
        public String toString() {
            return "REGEX";
        }
    }//class RegExOperator

    /**
     * �������������  ��������� ��������� <pre>"NOT REGEX"</pre> - �������� �� �������������� 
     * ����������� ���������.<p>
     * ����� ��������� ������ 60.
     * �������� ��������.
     */
    public static class NotRegExOperator extends AbstractOperator {

        /**
         * ������� ������ ���� {@link NotRegExExpression}, ����������� ���������
         * �������� ��������� ������� �������� �� �������������� ����������� ���������,
         * ������������� ������ ���������.
         * @param op1 ������, ����������� �������
         * @param op2 ������ �������� - ���������� ���������.
         * @return ������ ���� {@link RegExExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            return new NotRegExExpression(context, this, op1, op2);
        }

        /**
         * ���������� �������� ���������� ��������� ��������� ������ 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * ������������� �������� {@link #lexType} ������ <code>LexConst.NOTREGEX</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.NOTREGEX;
        }

        /**
         * @return ������ ������, ���������� �������� <pre>"NOT REGEX"</pre>.
         */
        @Override
        public String toString() {
            return "NOT REGEX";
        }
    }//class NotRegExOperator

    /**
     * �������������  ���������  <pre>","</pre> - �������. 
     * ����� ��������� ������ 11.
     * �������� ��������.
     */
    public static class CommaOperator extends AbstractOperator {

        /**
         * ������� ������ ���� {@link CommaListExpression}.
         * ���� ������ ������� �������� ����������� <code>CommaListExpressio</code>, 
         * �� ������ ������� ����������� � ���������, �������������� ������ ���������.
         * � ��������� ������, ��������� ����� ������ ��������� <code>CommaListExpression</code>
         * � � ���� ��������������� ����������� ������ � ������ �������. 
         * @param op1 ������ �������
         * @param op2 ������ ��������
         * @return ������ ���� {@link CommaListExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            this.op1 = op1;
            this.op2 = op2;
            CompoundExpression commaList;
            if (!(op1 instanceof CommaListExpression)) {
                commaList = new CommaListExpression();
                commaList.add(op1);
                commaList.add(op2);
            } else {
                commaList = (CompoundExpression) op1;
                commaList.add(op2);
            }
            commaList.setContext(context);
            return commaList;
        }

        /**
         * ���������� �������� ���������� ��������� ��������� ������ 11.
         */
        @Override
        public int getPriority() {
            return 11;
        }

        /**
         * ������������� �������� {@link #lexType} ������ <code>LexConst.COMMA</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.COMMA;
        }

        /**
         * @return ������ ������, ���������� �������� <pre>","</pre>.
         */
        @Override
        public String toString() {
            return ",";
        }
    }//class OrOperator

    /**
     * �������������  ��������� ��������� <pre>"IN"</pre> - �������� �� ������� �������� � ������.
     *  <p>
     * ����� ��������� ������ 60.
     * �������� ��������.
     */
    public static class InOperator extends AbstractOperator {

        /**
         * ������� ������ ���� {@link InExpression}, ������������ ��������� ������� ��������,
         * ������������� ������ ��������� � ������ ��������, �������� ������ ���������.
         * @param op1 ������ �������
         * @param op2 ������ ��������
         * @return ������ ���� {@link InExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            return new InExpression(context, this, op1, op2);
        }

        /**
         * ���������� �������� ���������� ��������� ��������� ������ 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * ������������� �������� {@link #lexType} ������ <code>LexConst.IN</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.IN;
        }

        /**
         * @return ������ ������, ���������� �������� <pre>"IN"</pre>.
         */
        @Override
        public String toString() {
            return "IN";
        }
    }//class InOperator

    /**
     * �������������  ��������� ��������� <pre>"NOT IN"</pre> - �������� �� ���������� �������� � ������.
     *  <p>
     * ����� ��������� ������ 60.
     * �������� ��������.
     */
    public static class NotInOperator extends AbstractOperator {

        /**
         * ������� ������ ���� {@link NotInExpression}, ������������ ��������� ���������� ��������,
         * ������������� ������ ��������� � ������ ��������, �������� ������ ���������.
         * @param op1 ������ �������
         * @param op2 ������ ��������
         * @return ������ ���� {@link NotInExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            return new NotInExpression(context, this, op1, op2);
        }

        /**
         * ���������� �������� ���������� ��������� ��������� ������ 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * ������������� �������� {@link #lexType} ������ <code>LexConst.NOTIN</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.NOTIN;
        }

        /**
         * @return ������ ������, ���������� �������� <pre>"NOT IN"</pre>.
         */
        @Override
        public String toString() {
            return "NOT IN";
        }
    }//class NotInOperator

    /**
     * �������������  ��������� <pre>"AS"</pre> - ���������� ����� ������� ���������.<p>
     * ����� ��������� ������ 12.
     * �������� ����������� ��� �������� ������� �������.
     * �������� ��������.
     */
    public static class AsOperator extends AbstractOperator {

        /**
         * ������� ������ ���� {@link ASExpression}, ����������� ���������
         * ���������� ����� ���������.
         * @param op1 ������ �������
         * @param op2 ������ �������� - ����������� ���.
         * @return ������ ���� {@link ASExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            return new ASExpression(context, this, op1, op2);
        }

        /**
         * ���������� �������� ���������� ��������� ��������� ������ 12.
         */
        @Override
        public int getPriority() {
            return 12;
        }

        /**
         * ������������� �������� {@link #lexType} ������ <code>LexConst.AS</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.AS;
        }

        /**
         * @return ������ ������, ���������� �������� <pre>"AS"</pre>.
         */
        @Override
        public String toString() {
            return "AS";
        }
    }//class RegExOperator
}//class AbstractOperator
