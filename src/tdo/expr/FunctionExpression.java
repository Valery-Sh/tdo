/*
 * MathExpression.java
 */
package tdo.expr;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import tdo.NamedValues;
import tdo.expr.CompoundExpression.CommaListExpression;
import tdo.tools.scanner.ExpressionException;
import tdo.util.ExprUtil;
import tdo.util.Strings;

/**
 */
public abstract class FunctionExpression extends AbstractExpression {

    protected int saveRowIndex;

    public FunctionExpression(ExpressionContext context, IOperator operator, IOperand op1) {
        this.setContext(context);
        this.operator = operator;
        this.op1 = op1;
        this.op2 = null;
    }

    @Override
    public Object getValue(NamedValues row) {
        Object v1 = op1 == null ? null : op1.getValue(row);

        return computeFunction(this.operator, v1);
    }

    @Override
    public Object getValue(NamedValues[] rows) {
        Object v1 = op1 == null ? null : op1.getValue(rows);

        return computeFunction(this.operator, v1);
    }

    protected Object computeFunction(IOperator oper, Object values) {
        return null;
    }

    @Override
    public String toString() {
        String s;//My 06.03.2012 = "";
        String s1 = op1 == null ? "" : op1.toString();
        s = operator.toString() + "(" + op1.toString() + ")";
        return s;
    }

    public static class UnknownFunctionExpression extends FunctionExpression {

        public UnknownFunctionExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
        }

        @Override
        public Class getType() {
            return ErrorExpression.class;
        }
    }

    /**
     * &nbsp; &nbsp;&nbsp;&nbsp;���������� ������, ����������� ��������� �������
     * �������������� �������� ������ ������ � ������� �������.
     * <h4>���������:</h4>
     *      <b>UpperCase(</b> <i>str-expr</i> <b> )</b>
     * <h4>�������� �����: <i>U, Upper</i> </h4>
     * &nbsp; &nbsp;&nbsp;&nbsp;��������� ������� ������ � ������� �������.
     * <p>&nbsp; &nbsp;&nbsp;&nbsp;���� �������� <code><i>str-expr</i></code>
     * ����� <code>null</code>, �� �������������� �� ������������.
     * ��� ��������� �������� ��������� ������������� � ������ �����������
     * ������ <code>toString()</code>.
     */
    public static class UpperCaseExpression extends FunctionExpression {

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;������� ��������� ������ ��� ���������
         * ���������, ��������� � ��������.
         * @param context �������� ���������
         * @param operator ������ ���� {@link tdo.expr.FunctionOperator}
         * @param op1 �������� ������ ����. ����� ��������� ��������
         *      <code>null</code>.
         */
        public UpperCaseExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
        }

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp; ��������� ���������� �������� �������. <p>
         * @param oper ��������
         * @param values ������������� ������ ������
         * @return <code>null</code>, ���� <vode>values==null</code>. �����
         *   ��� <code>values</code> ����������� ����� <code>toString</code> �,
         *   �����, ����� <code>toUpperCase</code>.
         */
        @Override
        protected Object computeFunction(IOperator oper, Object values) {
            return values == null ? null : values.toString().toUpperCase();
        }
        /**
         * @return �������� <code>String.class</code>
         */
        @Override
        public Class getType() {
            return String.class;
        }
    } //class UpperCaseExpression
    /**
     * &nbsp; &nbsp;&nbsp;&nbsp;���������� ������, ����������� ��������� �������
     * �������������� �������� ������ ������ � ������ �������.
     * <h4>���������:</h4>
     *      <b>LowerCase( </b><i>str-expr</i><b> )</b>
     * <h4>�������� �����: <i>Lower, L</i>
     * &nbsp; &nbsp;&nbsp;&nbsp;��������� ������� ������ � ������ �������.
     * <p>&nbsp; &nbsp;&nbsp;&nbsp;���� �������� <code><i>str-expr</i></code>
     * ����� <code>null</code>, �� �������������� �� ������������. ��� ���������
     * �������� ��������� ������������� � ������ ����������� ������
     * <code>toString()</code>.
     */
    public static class LowerCaseExpression extends FunctionExpression {

        /**
         * ������� ��������� ������ ��� ��������� ���������, ��������� �
         * ��������.
         * @param context �������� ���������
         * @param operator ������ ���� {@link tdo.expr.FunctionOperator}
         * @param op1 �������� ������ ����. ����� ��������� ��������
         *      <code>null</code>.
         */
        public LowerCaseExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
        }
        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;��������� ���������� �������� �������. <p>
         * @param oper ��������
         * @param values ������������� ������ ������
         * @return <code>null</code>, ���� <vode>values==null</code>. �����
         *   ��� <code>values</code> ����������� ����� <code>toString</code> �,
         *   �����, ����� <code>toLowerCase</code>.
         */
        @Override
        protected Object computeFunction(IOperator oper, Object values) {
            return values == null ? null : values.toString().toLowerCase();
        }
        /**
         * @return �������� <code>String.class</code>
         */
        @Override
        public Class getType() {
            return String.class;
        }
    } //class LowerCaseExpression

    /**
     * &nbsp; &nbsp;&nbsp;&nbsp;���������� ������, ����������� ��������� �������
     * �������������� ������� ������� ������ ������ � ������� �������.
     * <h4>���������:</h4>
     *      <b>Proper(</b> <i>str-expr</i> <b>)</b>
     * <h4>�������� �����: <i>P</i>
     * <p>&nbsp; &nbsp;&nbsp;&nbsp;��������� ������ ������ ������ � ������� �������.
     * <p>&nbsp; &nbsp;&nbsp;&nbsp;���� �������� <code><i>str-expr</i></code>
     * ����� <code>null</code>, �� �������������� �� ������������.
     */
    public static class ProperExpression extends FunctionExpression {
        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;������� ��������� ������ ��� ���������
         * ���������, ��������� � ��������.
         * @param context �������� ���������
         * @param operator ������ ���� {@link tdo.expr.FunctionOperator}
         * @param op1 �������� ������ ����. ����� ��������� ��������
         *      <code>null</code>.
         */
        public ProperExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
        }
        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;��������� ���������� �������� �������. <p>
         * @param oper ��������
         * @param values ������������� ������ ������
         * @return <code>null</code>, ���� <vode>values==null</code>. �����
         *   ��� <code>values</code> ����������� ��������������.
         */
        @Override
        protected Object computeFunction(IOperator oper, Object values) {

            if (values == null) {
                return null;
            }
            String s = ((String) values).toString();

            if (s.isEmpty()) {
                return s;
            }
            if (s.length() == 1) {
                return s.toUpperCase();
            }
            return s.substring(0, 1).toUpperCase() + s.substring(1);
        }

        /**
         * @return �������� <code>String.class</code>
         */
        @Override
        public Class getType() {
            return String.class;
        }
    } //class ProperExpression

    /**
     * &nbsp;&nbsp;&nbsp;&nbsp;���������� ������, ����������� ��������� ������� 
     * ��������� ��������� �� �������� ������ ������.
     *
     * <h4>���������:</h4>
     *      <b>Substring(</b> <i>source</i>, <i>startpos</i> [ , <i>endpos</i> ]<b> )</b>
     * <h4>�������� �����: <i>Substr</i></h4>
     * &nbsp;&nbsp;&nbsp;&nbsp;�������� ���������, ������� � �������
     * <code>startpos</code> � ���������� �������� <code>endpos</code> ��
     * �������� ������ <code>source</code>.
     * <p>���� �������� <code>endpos</code> �� �����, �� ����������� ������
     * <code><i>source.length - 1</i><code>.
     *
     * <p><b>����������.</b> ��������� �������� ���������� � 0.
     * <p>&nbsp; &nbsp;&nbsp;&nbsp;���� �������� source ����� <code>null</code>, �� �������������� ��
     * ������������. ��� ��������� �������� ��������� ������������� � ������
     * ����������� ������ <code>toString()</code>.
     */
    public static class SubstringExpression extends FunctionExpression {

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;������� ��������� ������ ��� ���������
         * ���������, ��������� � ��������.
         * @param context �������� ���������
         * @param operator ������ ���� {@link tdo.expr.FunctionOperator}
         * @param op1 ��������� ����
         *      {@link tdo.expr.CompoundExpression.CommaListExpression}.
         */
        public SubstringExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
        }

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;��������� ���������� �������� �������. <p>
         * @param oper ��������
         * @param values ������ ���� {@link tdo.expr.ValueList}, ����������
         *  ������ ����������
         * @return <code>null</code>, ���� �������� �������� ������ ������
         *  ����� <vode>null</code>. �����, ������������ ���������� ���������
         * @throws tdo.tools.scanner.ExpressionException, ���� ����������
         *      ���������� ������ ���� ��� ������ ����. ��� ������ ��������
         *      ��������� ��� �������� ������� ������ <code>null</code>. ���
         *      ���������/�������� ������� �� �������� ������ ��� ��������
         *      ������� ������ ���������.
         */
        @Override
        protected Object computeFunction(IOperator oper, Object values) {

            if (!(values instanceof ValueList)) {
                String msg = "Substring Function accepts two or three parameters";
                this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                throw new ExpressionException(msg);
            }
            ValueList args = (ValueList) values;

            String source = (String) args.get(0);
            if (source == null) {
                return null;
            }
            if (source.isEmpty()) {
                return "";
            }
            if (args.size() < 2 || args.size() > 3) {
                String msg = "Substring Function accepts two or three parameters";
                this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                throw new ExpressionException(msg);
            }
            Object ostart = args.get(1);
            Object oend;//My 06.03.2012 = null;
            if (ostart == null) {
                String msg = "Substring Function: start or end parameter cannot be null";
                this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                throw new ExpressionException(msg);
            }
            int start = 0;
            int end = 0;

            if (args.size() == 3) {
                oend = args.get(2);
                if (oend == null) {
                    String msg = "Substring Function: start or end parameter cannot be null";
                    this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                    throw new ExpressionException(msg);
                }
            } else {
                oend = String.valueOf(source.length() - 1);
            }

            try {
                String s1 = ostart.toString();
                String s2 = oend.toString();

                start = Integer.parseInt((String) s1);
                end = Integer.parseInt((String) s2);
            } catch (Exception e) {
                String msg = "SubstringFunction: Illegal start or end value ( start=" + ostart.toString() + "; end=" + oend.toString();
                this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                throw new ExpressionException(msg);
            }

            if (start > source.length() || end > source.length() || end < start) {
                return "";
            }
            return source.substring(start, end);
        }

        /**
         * @return �������� <code>String.class</code>
         */
        @Override
        public Class getType() {
            return String.class;
        }
    } //class SubstringExpression

    /**
     * &nbsp; &nbsp;&nbsp;&nbsp;���������� ������� ���������� ������ ������ ��
     * �������� ����� �������� ��������.<p>
     * 
     * 
     * <h4>��� �������: <i>PADL</i> ��� <i>PL</i> ��� <i>PADR</i> ��� <i>PR</i>.
     *
     * &nbsp; &nbsp;&nbsp;&nbsp;������� PADL � PL ��������� �����, � �������
     * PADR � PR ������.
     *
     * <h4>���������:</h4>
     * 
     *&nbsp; &nbsp;&nbsp;&nbsp;<b>PADL(</b> <i>expr</i>,<i>size</i>[<i>char</i>] <b>)</b>
     *&nbsp; &nbsp;&nbsp;&nbsp;<b>PADR(</b><i>expr</i>,<i>size</i>[<i>char</i>]<b>)</b>
     * 
     * <p>&nbsp; &nbsp;&nbsp;&nbsp;���� <i>expr</i> ����� ��� Number � ��������
     * <i>char</i> ������, ��, �� ���������, ���������� ������������ �������� '0'.
     * <p>&nbsp; &nbsp;&nbsp;&nbsp;
     *  ���� <i>expr</i> ����� ��� �������� �� Number � �������� <i>char</i>
     * ������, ��, �� ��������� ���������� ������������ �������� �������.
     */
    public static class PadExpression extends FunctionExpression {

        protected String fname;

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;������� ��������� ������ ��� ���������
         * ���������, ��������� � ��������.
         * @param fname ���������� ��� �������, ��� ��� ����� ������������ ���
         *   ������� � ������� <code>PADL</code> � <code>PADR</code>.
         *
         * @param context �������� ���������
         * @param operator ������ ���� {@link tdo.expr.FunctionOperator}
         * @param op1 ��������� ����
         *      {@link tdo.expr.CompoundExpression.CommaListExpression}.
         */
        public PadExpression(String fname, ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            this.fname = fname;
        }

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;��������� �������� �������. <p>
         * @param oper ��������
         * @param values ������ ���� {@link tdo.expr.ValueList}, ����������
         *  ������ ����������
         * @return <code>null</code>, ���� �������� �������� ������ ������
         *  ����� <vode>null</code>. �����, ������������ ����������� ���������
         *  �������� ������
         * @throws tdo.tools.scanner.ExpressionException, ���� ����������
         *      ���������� ������ ���� ��� ������ ����.
         */
        @Override
        protected Object computeFunction(IOperator oper, Object values) {

            if (!(values instanceof ValueList)) {
                String msg = fname + " Function accepts two or three parameters";
                this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                throw new ExpressionException(msg);
            }
            ValueList args = (ValueList) values;

            Object osource = args.get(0);
            String source;

            char fsym = ' ';

            if (osource == null) {
                return null;
            }

            if (osource instanceof java.lang.Number) {
                fsym = '0';
            }
            source = osource.toString();

            int size = MathExpression.toInteger(args.get(1));

            if (source.length() >= size) {
                return source;
            }

            if (args.size() == 3) {
                fsym = ((String) args.get(2)).charAt(0);
            }

            int count = size - source.length();
            StringBuilder sb = new StringBuilder(size);
            char[] fill = new char[count];
            for (int i = 0; i < fill.length; i++) {
                fill[i] = fsym;
            }
            if (fname.endsWith("L")) {
                sb.append(fill);
                sb.append(source);
            } else {
                sb.append(source);
                sb.append(fill);
            }
            return sb.toString();
        }

        /**
         * @return �������� <code>String.class</code>
         */
        @Override
        public Class getType() {
            return String.class;
        }
    } //class PadExpression

    /**
     * &nbsp; &nbsp; &nbsp; &nbsp; ���������� ������� ��������� ���������� �
     * ���������� �������� �������� ������ ������.
     * <h4>��� �������: <i>TRIM</i> ��� <i>TR</i> </h4>
     *
     *
     * <h4>���������:<h4>
     *   <b>TRIM(</b><i>expr</i><b>)</b>
     * 
     */
    public static class TrimExpression extends FunctionExpression {

        protected String fname;

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;������� ��������� ������ ��� ���������
         * ���������, ��������� � ��������.
         * @param fname ���������� ��� �������.
         *
         * @param context �������� ���������
         * @param operator ������ ���� {@link tdo.expr.FunctionOperator}
         * @param op1 �������, ���������� ��������� ���������
         */
        public TrimExpression(String fname, ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            this.fname = fname;
        }

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;��������� �������� �������. <p>
         * @param oper ��������
         * @param values ������ ���� {@link tdo.expr.ValueList}, ����������
         *  ������ ����������
         * @return <code>null</code>, ���� �������� ��������
         *  ����� <vode>null</code>. �����, ������������ ����������� ���������
         *  �������� ������
         * @throws tdo.tools.scanner.ExpressionException, ���� ����������
         *      ���������� ������ ������.
         */
        @Override
        protected Object computeFunction(IOperator oper, Object values) {

            if (values instanceof ValueList) {
                String msg = fname + " Function accepts only one parameters";
                this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                throw new ExpressionException(msg);
            }
            if (values == null) {
                return null;
            }
            return values.toString().trim();
        }

        /**
         * @return �������� <code>String.class</code>
         */
        @Override
        public Class getType() {
            return String.class;
        }
    } //class TrimExpression

    /**
     * &nbsp; &nbsp;&nbsp;&nbsp;���������� ������� ����������������� �����
     * �����������, ����� �������, ��� 1-� ������ ������� ����� ���������� �
     * ��������� �����.
     * 
     * 
     * <h4>��� �������: <i>Header</i> ��� <i>Hd</i>. </h4>
     * <h4>���������:</h4>
     *
     *   <b>Header(</b><i>expr</i>[,<i>delimeters</i>]<b>)</b>

     * &nbsp; &nbsp;&nbsp;&nbsp;����  <i>edelimeters</i> ������, �� �� ���������
     *  �� ��������� ��������� ��������: " .,:-_;", ���
     * <i>������,�����,�������,���������,�����, �������������, ����� � �������</i>.
     */
    public static class HeaderExpression extends FunctionExpression {

        protected String fname;

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;������� ��������� ������ ��� ���������
         * ���������, ��������� � ��������.
         * @param fname ���������� ��� �������.
         *
         * @param context �������� ���������
         * @param operator ������ ���� {@link tdo.expr.FunctionOperator}
         * @param op1 {@link tdo.expr.CompoundExpression.CommaListExpression}.
         */
        public HeaderExpression(String fname, ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            this.fname = fname;
        }

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;��������� �������� �������. <p>
         * @param oper ��������
         * @param values ������ ���� {@link tdo.expr.ValueList}, ����������
         *  ������ ���������� ���, ���� ����� ������� ������������ � �����
         *  ����������, �� ������ ������������� ����
         * @return <code>null</code>, ���� �������� ��������
         *  ����� <vode>null</code>. �����, ������������ ��������������� ������
         * @throws tdo.tools.scanner.ExpressionException, ���� ����������
         *      ���������� ������ ����.
         */
        @Override
        protected Object computeFunction(IOperator oper, Object values) {

            if ((values instanceof ValueList) && ((ValueList) values).size() > 2) {
                String msg = " Header Function accepts one or two parameters";
                this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                throw new ExpressionException(msg);
            }
            String str;//My 06.03.2012 = null;
            String pattern = " .,:-_;";
            if (values == null) {
                return null;
            }
            if (values instanceof ValueList) {
                str = (String) ((ValueList) values).get(0);
                pattern = (String) ((ValueList) values).get(1);
            } else {
                str = values.toString();
            }

            String s[] = Strings.split(str, pattern, true);
            StringBuilder sb = new StringBuilder(str.length());
            for (int i = 0; i < s.length; i++) {
                sb.append(proper(s[i]));
            }
            return sb.toString();
        }

        /**
         * @return �������� <code>String.class</code>
         */
        @Override
        public Class getType() {
            return String.class;
        }

        /**
         * ��������� ������ ������ ������ � ������� �������.<p>
         * @param source ������������� ������
         * @return ������, ������ ������ ������� ������������ � ������� �������.
         */
        protected String proper(String source) {
            if (source.length() == 0) {
                return " ";  // space
            }

            if (source.length() == 1) {
                return source.toUpperCase();
            }
            return source.substring(0, 1).toUpperCase() + source.substring(1);

        }
    } //class HeaderExpression

    /**
     * ������������ ���������� �������� ������� <code>IIF</code>.
     * <h4>���������:</h4>
     * <b>IIF(</b> <i>cond-expr</i>, <i>value1</i>, <i>value2</i> <b>)</b>
     * <h4>���������:</h4>
     * <ul>
     *      <li><i>cond-expr</i> ������ ������� - ������� ���������, �������� ��������
     *      ������ <code>true</code> ����������, ��� ������������ ���������
     *      �������� <code><i>value1</i></code>. �������� ������
     *      <code>false</code> ����������, ��� ������������ ���������
     *      �������� <code><i>value2</i></code>.
     *      </li>
     *      <li><i>value1<i> - ������������,����� �������, ��������
     *          1-� ���������� ���������� <code>true</code>
     *      </li>
     *      <li><i>value2<i> - ������������,����� �������, ��������
     *          1-� ���������� ���������� <code>false</code>
     *      </li>
     * </ul>
     */
    public static class IIFExpression extends FunctionExpression {

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;������� ��������� ������ ��� ���������
         * ���������, ��������� � ��������.
         *
         * @param context �������� ���������
         * @param operator ������ ���� {@link tdo.expr.FunctionOperator}
         * @param op1 {@link tdo.expr.CompoundExpression.CommaListExpression}.
         */
        public IIFExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
        }

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;��������� �������� �������. <p>
         * @param oper ��������
         * @param values ������ ���� {@link tdo.expr.ValueList}, ����������
         *  ������ ���������� ���, ���� ����� ������� ������������ � �����
         *  ����������, �� ������ ������������� ����
         * @return <code>null</code>, ���� �������� ��������
         *  ����� <vode>null</code>. �����, ������������ ��������������� ������
         * @throws tdo.tools.scanner.ExpressionException, ���� ����������
         *      ���������� ������ �� ����� ���� ��� ����� ��� �������� �������
         *       �������� ������� �� <code>Boolean.class</code>.
         */
        @Override
        protected Object computeFunction(IOperator oper, Object values) {
            String msg;
            if (!(values instanceof ValueList)) {
                msg = "IIF function accepts parameter of type ValueList";
                this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                throw new ExpressionException(msg);
            }
            ValueList args = (ValueList) values;

            if (args.size() != 3) {
                msg = "IIF Function accepts exactly three parameters";
                this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                throw new ExpressionException(msg);
            }
            Object o = args.get(0);
            //Boolean expr = null;

            if (o == null || !(o instanceof Boolean)) {
                msg = "IIF Function has first parameter  of type java.lang.Boolean";
                this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                throw new ExpressionException(msg);
            }

            Boolean expr = (Boolean) o;
            if (expr.booleanValue() == true) {
                return args.get(1);
            } else {
                return args.get(2);
            }
        }

        /**
         * @return �������� <code>Object.class</code>
         */
        @Override
        public Class getType() {
            return Object.class;
        }
    } //class IIFExpression

    ///**************************************************
    /// Convert Classes
    ///**************************************************
    /**
     * ������� ����� ��� �������, ����������� �������������� �������� �� ������
     * ���� � ������.
     */
    public static class FunctionConvertExpression extends FunctionExpression {

        protected int scale;
        protected int size;

        public FunctionConvertExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            scale = 2;
            size = 15;
        }

        @Override
        public Class getType() {
            return Object.class;
        }

        public int getScale() {
            return 2;
        }
    }

    /**
     * ���������� ������� �������������� �������� � ��� java.math.BigDecimal.
     * <h4>��� �������: <i>BigDecimal</i> ��� <i>Dec</i>.</h4>
     * <h4>���������:</h4>
     * 
     *   <b>BigDecimal(</b><i>expr</i>[,scale]<b>)</b>
     * 
     */
    public static class BigDecimalExpression extends FunctionConvertExpression {

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;������� ��������� ������ ��� ���������
         * ���������, ��������� � ��������.
         *
         * @param context �������� ���������
         * @param operator ������ ���� {@link tdo.expr.FunctionOperator}
         * @param op1 {@link tdo.expr.CompoundExpression.CommaListExpression}
         *  ��� ������ ������������� ����, ������� ����� ���� ������������
         *  � �������� ��������.
         */
        public BigDecimalExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
        }

        /**
         * @return �������� <code>BigDecimal.class</code>
         */
        @Override
        public Class getType() {
            return BigDecimal.class;
        }

        /**
         * ���������� �������� ��������� <code>scale</code>, ��������� ��� ������
         * �������.
         * @return �������� ��������� <code>scalr</code>.
         */
        @Override
        public int getScale() {
            int r = 2;
            NamedValues row = null;
            Object sco;

            IOperand op = this.getOp1();
            if (op instanceof CommaListExpression) {
                CommaListExpression cle = (CommaListExpression) op;
                r = ((Number) cle.get(1).getValue(row)).intValue();
            }
            return r;
        }

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;��������� �������� �������. <p>
         *  &nbsp; &nbsp;&nbsp;&nbsp;���� �������� �������� <code>values</code>
         *  ����� <vode>null</code>, �� ���������, ��� ��������� ��������
         *  ������ <code>BigDecimal(0)</code>.
         *  &nbsp; &nbsp;&nbsp;&nbsp;�������� �������� ������������� �
         * <code>BigDecimal</code> � � ����������� ���������� �����������: <br>
         * &nbsp; &nbsp;&nbsp;&nbsp;setScale(<i>scale</i>, BigDecimal.ROUND_HALF_UP);
         * <br>
         * ��� <code><i>scale</i></code> - �������� ��������� <code>scale</code>.
         * <p> &nbsp; &nbsp;&nbsp;&nbsp;<b>����������.</b> ���� ��� ������
         * ������� �� ����� �������� <code>scale</code>, �� �� ���������
         * ����������� ������ 2.
         *
         * @param oper ��������
         * @param values ������ ���� {@link tdo.expr.ValueList}, ����������
         *  ������ ���������� ���, ���� ����� ������� ������������ � �����
         *  ����������, �� ������ ������������� ����, ������� ����� ����
         *  ������������ � <code>java.math.BigDecimal</code>.
         * @return ��������������� ��������
         * @throws tdo.tools.scanner.ExpressionException, ���� ����������
         *      ���������� ������ �� ����� 1 ��� 2 ��� ����� ���� ����� ��������
         *      <code>scale</code> � ��� �������� ����� <code>null</code> ���
         *      ��� �������� �� ����� <code>java.lang.Number</code>.
         */
        @Override
        protected Object computeFunction(IOperator oper, Object values) {
            String msg;
            Object v;
            int wscale;
            if (values instanceof ValueList) {
                ValueList args = (ValueList) values;
                if (args.size() < 1 || args.size() > 2) {
                    msg = "BigDecimalFunction accepts one or two parameters";
                    this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                    throw new ExpressionException(msg);
                }
                if (args.size() == 2) {
                    Object sco = args.get(1);
                    if (sco == null || !(sco instanceof Number)) {
                        msg = "BigDecimalFunction: scale parameter cannot be null and must be of Number type";
                        this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                        throw new ExpressionException(msg);
                    }
                }

                v = args.get(0);
            } else {
                v = values;
            }
            wscale = getScale();


            BigDecimal bd = ExprUtil.toBigDecimal(v);
            bd = bd.setScale(wscale, BigDecimal.ROUND_HALF_UP);
            return bd;
        }
    }//class BigDecimalExpression

    /**
     * ���������� ������� �������������� �������� � ���
     * <code>java.lang.Double</code>.
     * <h4>��� �������: <i>Double</i> ��� <i>Dou</i>.</h4>
     * <h4>���������:</h4>
     * 
     *   <b>Double(</b><i>expr</i><b>)</b><LI>
     * 
     */
    public static class DoubleExpression extends FunctionConvertExpression {

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;������� ��������� ������ ��� ���������
         * ���������, ��������� � ��������.
         *
         * @param context �������� ���������
         * @param operator ������ ���� {@link tdo.expr.FunctionOperator}
         * @param op1 ������ ������������� ����, ������� ����� ���� ������������
         *  � �������� ��������.
         */
        public DoubleExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            scale = -1;
            size = 15;
        }

        /**
         * @return �������� <code>Double.class</code>
         */
        @Override
        public Class getType() {
            return Double.class;
        }

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;��������� �������� �������. <p>
         *  &nbsp; &nbsp;&nbsp;&nbsp;���� �������� �������� <code>values</code>
         *  ����� <vode>null</code>, �� ���������, ��� ��������� ��������
         *  ������ <code>BigDecimal(0)</code>.
         *
         * @param oper ��������
         * @param values ��������, ������� ����� ����
         *  ������������� � <code>java.lang.Double</code>.
         * @return ��������������� ��������
         * @throws tdo.tools.scanner.ExpressionException, ���� ����������
         *      ���������� ������ 1.
         */
        @Override
        protected Object computeFunction(IOperator oper, Object values) {

            if (values instanceof ValueList) {
                String msg = "DoubleFunction accepts one parameter";
                this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                throw new ExpressionException(msg);
            }

            Double bd = ExprUtil.toDouble(values);
            return bd;
        }
    }//class DoubleExpression

    /**
     * ���������� ������� �������������� �������� � ���
     * <code>java.lang.Float</code>.
     * <h4>��� �������: <i>Float</i> ��� <i>Flo</i> ��� <i>Real</i>.</h4>
     * <h4>���������:</h4>
     *
     *   <b>Float(</b><i>expr</i><b>)</b><LI>
     *
     */
    public static class FloatExpression extends FunctionConvertExpression {

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;������� ��������� ������ ��� ���������
         * ���������, ��������� � ��������.
         *
         * @param context �������� ���������
         * @param operator ������ ���� {@link tdo.expr.FunctionOperator}
         * @param op1 ������ ������������� ����, ������� ����� ���� ������������
         *  � �������� ��������.
         */
        public FloatExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            scale = -1;
            size = 15;
        }

        /**
         * @return �������� <code>Double.class</code>
         */
        @Override
        public Class getType() {
            return Float.class;
        }

        /**
         *  &nbsp; &nbsp;&nbsp;&nbsp;��������� �������� �������. <p>
         *  &nbsp; &nbsp;&nbsp;&nbsp;���� �������� �������� <code>values</code>
         *  ����� <vode>null</code>, �� ���������, ��� ��������� ��������
         *  ������ <code>Float(0)</code>.
         *
         * @param oper ��������
         * @param values ��������, ������� ����� ����
         *  ������������� � <code>java.lang.Float</code>.
         * @return ��������������� ��������
         * @throws tdo.tools.scanner.ExpressionException, ���� ����������
         *      ���������� ������ 1.
         */
        @Override
        protected Object computeFunction(IOperator oper, Object values) {

            if (values instanceof ValueList) {
                String msg = "FloatFunction accepts one parameter";
                this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                throw new ExpressionException(msg);
            }

            Float bd = ExprUtil.toDouble(values).floatValue();
            return bd;
        }
    }//class FloatExpression

    /**
     * ���������� ������� �������������� �������� � ���
     * <code>java.lang.Integer</code>.
     * <h4>��� �������: <i>Integer</i> ��� <i>Int</i>.</h4>
     * <h4>���������:</h4>
     *
     *   <b>Integer(</b><i>expr</i><b>)</b><LI>
     *
     */
    public static class IntegerExpression extends FunctionConvertExpression {

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;������� ��������� ������ ��� ���������
         * ���������, ��������� � ��������.
         *
         * @param context �������� ���������
         * @param operator ������ ���� {@link tdo.expr.FunctionOperator}
         * @param op1 ������ ������������� ����, ������� ����� ���� ������������
         *  � �������� ��������.
         */
        public IntegerExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            scale = 0;
            size = 10;
        }

        /**
         * @return �������� <code>Integer.class</code>
         */
        @Override
        public Class getType() {
            return Integer.class;
        }

        /**
         *  &nbsp; &nbsp;&nbsp;&nbsp;��������� �������� �������. <p>
         *  &nbsp; &nbsp;&nbsp;&nbsp;���� �������� �������� <code>values</code>
         *  ����� <vode>null</code>, �� ���������, ��� ��������� ��������
         *  ������ <code>Integer(0)</code>.
         *
         * @param oper ��������
         * @param values ��������, ������� ����� ����
         *  ������������� � <code>java.lang.Integer</code>.
         * @return ��������������� ��������
         * @throws tdo.tools.scanner.ExpressionException, ���� ����������
         *      ���������� ������ 1.
         */
        @Override
        protected Object computeFunction(IOperator oper, Object values) {

            if (values instanceof ValueList) {
                String msg = "IntegerFunction accepts one parameter";
                this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                throw new ExpressionException(msg);
            }

            Integer bd = ExprUtil.toLong(values).intValue();
            return bd;
        }
    }//class IntegerExpression

    /**
     * ���������� ������� �������������� �������� � ���
     * <code>java.lang.Long</code>.
     * <h4>��� �������: <i>Long</i> ��� <i>BigInt</i> ��� <i>Lo</i>.</h4>
     * <h4>���������:</h4>
     *
     *   <b>Long(</b><i>expr</i><b>)</b><LI>
     *
     */
    public static class LongExpression extends FunctionConvertExpression {

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;������� ��������� ������ ��� ���������
         * ���������, ��������� � ��������.
         *
         * @param context �������� ���������
         * @param operator ������ ���� {@link tdo.expr.FunctionOperator}
         * @param op1 ������ ������������� ����, ������� ����� ���� ������������
         *  � �������� ��������.
         */
        public LongExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            scale = 0;
            size = 20;
        }

        /**
         * @return �������� <code>Long.class</code>
         */
        @Override
        public Class getType() {
            return Long.class;
        }

        /**
         *  &nbsp; &nbsp;&nbsp;&nbsp;��������� �������� �������. <p>
         *  &nbsp; &nbsp;&nbsp;&nbsp;���� �������� �������� <code>values</code>
         *  ����� <vode>null</code>, �� ���������, ��� ��������� ��������
         *  ������ <code>Long(0)</code>.
         *
         * @param oper ��������
         * @param values ��������, ������� ����� ����
         *  ������������� � <code>java.lang.Long</code>.
         * @return ��������������� ��������
         * @throws tdo.tools.scanner.ExpressionException, ���� ����������
         *      ���������� ������ 1.
         */
        @Override
        protected Object computeFunction(IOperator oper, Object values) {

            if (values instanceof ValueList) {
                String msg = "Long Function accepts one parameter";
                this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                throw new ExpressionException(msg);
            }

            Long bd = ExprUtil.toLong(values);
            return bd;
        }
    }//class LongExpression

    /**
     * ���������� ������� �������������� �������� � ���
     * <code>java.lang.Short</code>.
     * <h4>��� �������: <i>Short</i> ��� <i>SmallInt</i> ��� <i>Small</i>.</h4>
     * <h4>���������:</h4>
     *
     *   <b>Short(</b><i>expr</i><b>)</b><LI>
     *
     */
    public static class ShortExpression extends FunctionConvertExpression {

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;������� ��������� ������ ��� ���������
         * ���������, ��������� � ��������.
         *
         * @param context �������� ���������
         * @param operator ������ ���� {@link tdo.expr.FunctionOperator}
         * @param op1 ������ ������������� ����, ������� ����� ���� ������������
         *  � �������� ��������.
         */
        public ShortExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            scale = 0;
            size = 6;
        }

        /**
         * @return �������� <code>Short.class</code>
         */
        @Override
        public Class getType() {
            return Short.class;
        }

        /**
         *  &nbsp; &nbsp;&nbsp;&nbsp;��������� �������� �������. <p>
         *  &nbsp; &nbsp;&nbsp;&nbsp;���� �������� �������� <code>values</code>
         *  ����� <vode>null</code>, �� ���������, ��� ��������� ��������
         *  ������ <code>Short(0)</code>.
         *
         * @param oper ��������
         * @param values ��������, ������� ����� ����
         *  ������������� � <code>java.lang.Short</code>.
         * @return ��������������� ��������
         * @throws tdo.tools.scanner.ExpressionException, ���� ����������
         *      ���������� ������ 1.
         */
        @Override
        protected Object computeFunction(IOperator oper, Object values) {

            if (values instanceof ValueList) {
                String msg = "Short Function accepts one parameter";
                this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                throw new ExpressionException(msg);
            }

            Short bd = ExprUtil.toLong(values).shortValue();
            return bd;
        }
    }//class ShortExpression

    /**
     * ���������� ������� �������������� �������� � ���
     * <code>java.lang.Short</code>.
     * <h4>��� �������: <i>Short</i> ��� <i>TinyInt</i> ��� <i>Tiny</i>.</h4>
     * <h4>���������:</h4>
     *
     *   <b>Short(</b><i>expr</i><b>)</b><LI>
     *
     */
    public static class ByteExpression extends FunctionConvertExpression {

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;������� ��������� ������ ��� ���������
         * ���������, ��������� � ��������.
         *
         * @param context �������� ���������
         * @param operator ������ ���� {@link tdo.expr.FunctionOperator}
         * @param op1 ������ ������������� ����, ������� ����� ���� ������������
         *  � �������� ��������.
         */
        public ByteExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            scale = 0;
            size = 6;
        }

        /**
         * @return �������� <code>Byte.class</code>
         */
        @Override
        public Class getType() {
            return Byte.class;
        }

        /**
         *  &nbsp; &nbsp;&nbsp;&nbsp;��������� �������� �������. <p>
         *  &nbsp; &nbsp;&nbsp;&nbsp;���� �������� �������� <code>values</code>
         *  ����� <vode>null</code>, �� ���������, ��� ��������� ��������
         *  ������ <code>Byte(0)</code>.
         *
         * @param oper ��������
         * @param values ��������, ������� ����� ����
         *  ������������� � <code>java.lang.Byte</code>.
         * @return ��������������� ��������
         * @throws tdo.tools.scanner.ExpressionException, ���� ����������
         *      ���������� ������ 1.
         */
        @Override
        protected Object computeFunction(IOperator oper, Object values) {

            if (values instanceof ValueList) {
                String msg = "Byte Function accepts one parameter";
                this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                throw new ExpressionException(msg);
            }

            Byte bd = ExprUtil.toLong(values).byteValue();
            return bd;
        }
    }//class ByteExpression

    /**
     * &nbsp; &nbsp;&nbsp; &nbsp;���������� ������� �������� ���� �� ��������
     * ����, ������ � ���. <p>
     *
     * <h4>��� �������: <i><b>$</b></i>.</h4>
     * <h4>���������:</h4>
     *
     *   &nbsp; &nbsp;&nbsp; &nbsp;
     * <b>$(</b><i>year-expr</i>, <i>month-expr</i>,<i>day-expr</i><b>)</b>
     * <h4>���������:</h4>
     * <ul>
     *   <li><i>year-expr</i> - ��������� ���� <code>java.lang.Integer</code>
     *          �������������� ���.
     *   </li>
     *   <li><i>month-expr</i> - ��������� ���� <code>java.lang.Integer</code>
     *          �������������� �����.
     *   </li>
     *   <li><i>day-expr</i> - ��������� ���� <code>java.lang.Integer</code>
     *          �������������� ���� ������.
     *   </li>
     * </ul>
     *
     */
    public static class DateConstantExpression extends FunctionConvertExpression {

        SimpleDateFormat df;

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;������� ��������� ������ ��� ���������
         * ���������, ��������� � ��������.
         *
         * @param context �������� ���������
         * @param operator ������ ���� {@link tdo.expr.FunctionOperator}
         * @param op1 ������ ���� {@link tdo.expr.CompoundExpression.CommaListExpression}
         *
         */
        public DateConstantExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            df = new SimpleDateFormat("dd.MM.yyyy");
        }

        /**
         * @return �������� <code>Date.class</code>
         */
        @Override
        public Class getType() {
            return Date.class;
        }

        /**
         *  &nbsp; &nbsp;&nbsp;&nbsp;��������� �������� �������. <p>
         *
         * @param oper ��������
         * @param values ������ ���� {@link tdo.expr.ValueList} - ������ ��������
         *    ����, ������ � ���� ������.
         * @return ����� �������� ����
         * @throws tdo.tools.scanner.ExpressionException, ���� ����������
         *      ���������� �� ����� 3.
         */
        @Override
        protected Object computeFunction(IOperator oper, Object values) {
            Integer year;
            Integer month;
            Integer day;

            String pattern = null;
            String localeLang = null;
            String s = null;
            if (values instanceof ValueList) {
                ValueList args = (ValueList) values;
                if (args.size() != 3) {
                    String msg = "DateConstant Function accepts exactly three parameter";
                    this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                    throw new ExpressionException(msg);
                }
                year = (Integer) args.get(0);
                month = (Integer) args.get(1);
                day = (Integer) args.get(2);


                s = day.toString() + "." + month.toString() + "." + year.toString();
            }

            return ExprUtil.dateValueOf(s, df);
        }
    }//class DateConstantExpression

    /**
     * &nbsp;&nbsp;&nbsp;&nbsp;���������� ������� �������������� ����������
     * �������� �������� � ������ ���� ���� <code>java.util.Date</code>
     * �������� ��������� ������� � ���� �����.
     * <h4>��� �������: <i>Date</i> ��� <i>Dat</i>. </h4>
     * <h4>���������:</h4>
     *
     *   <b>Date(</b><i>date-expr</i>,[<i>pattern</i>[,<i>localeLang</i>]]<b>)</b>
     *
     * <h4>���������:</h4>
     * <ul>
     *   <li><i>date-expr</i>  � ��������� ������������� ����, �������
     *      ������������� � <code>java.util.Date</code>. �� ����������� ��������
     *      <code>null</code>.
     *   </li>
     *   <li><i>pattern</i>   -  ��������� ���������, �������������� ����� ������
     *      (�������) �������� �������� ������� ��������� �������� ����.
     *   </li>
     *   <li><i>localeLang</i>  - ��������� ��� ������, ��� ���������� �������������
     *      ������ <code>java.util.Locale</code>. �������� �ru� - �������, �
     *      �en� - 	����������.
     *   </i>
     * </ul>
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;E��� <code><i>pattern</i></code> �� �����, ��
     *    �������������� ������������ � ��������������  �������� ������� "dd.MM.yy".
     * <p>&nbsp;&nbsp;&nbsp;&nbsp; ���� �� ����� <code><i>localeLang</i></code>,
     *    �� �������������� ����������� �������� ������� �������� ����������
     *    ������������ ������� ����������. ��� ����������� ������������� �������
     *    ���������� ����� ����� ����� ���� ����������� ��� ������������� ���� �
     *    ������� ������ ������.
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;�������� <i><code>date-expr</code></i> ������
     *    <code>null</code>, ������������� � <code>java.util.Date</code> ��
     *    ��������� <code>Date(0)</code> � ������������.
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;�������������� ���������� � ������� ������
     *   <code>java.text.SimpleDateFormat</code>.
     * <p>
     * 	<b>������</b>. ����� ������ <code><i>date-expr</i></code> ��������
     *      �������� ���� <code>java.util.Date</code>, ���������������
     *      <b><i>'��� 12 2005'</b><i>.
     *	��� ����� ���� �������� ������ <b>'MMM, d yyyy'</b>.
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;���� ��������� ���������
     * <code>Date( date-expr, 'MMM, d yyyy')</code>, �� ��� Windows � ��������
     * ��������� ����������� ���������� ��������� ����.
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;���� ��������� ���������
     * <code>Date( date-expr, 'MMM, d yyyy','ru')</code>, �� ��� Windows �
     * �������� ��������� ����������� ���������� ��������� ����.
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;���� ��������� ���������
     * <code>Date(date-expr, 'MMM, d yyyy','en')</code>, �� �������������
     * ����������.
     */
    public static class DateExpression extends FunctionConvertExpression {

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;������� ��������� ������ ��� ���������
         * ���������, ��������� � ��������.
         *
         * @param context �������� ���������
         * @param operator ������ ���� {@link tdo.expr.FunctionOperator}
         * @param op1 ���� ���������� ����� ������ ��������� �� ��� ������ ����
         * {@link tdo.expr.CompoundExpression.CommaListExpression}.
         *      ���� ��������� ������ ���� ��������, �� ��� ���������
         *      ������������� ���� ���� <code>java.lang.String</code>.
         *
         */
        public DateExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            scale = 0;
            size = 6;
        }

        /**
         * @return �������� <code>Date.class</code>
         */
        @Override
        public Class getType() {
            return Date.class;
        }

        /**
         *  &nbsp; &nbsp;&nbsp;&nbsp;��������� �������� �������. <p>
         *  &nbsp; &nbsp;&nbsp;&nbsp;���� �������� �������� <code>values</code>
         *  ����� <vode>null</code>, �� ������������� ����������.
         *
         * @param oper ��������
         * @param values ������ ���� {@link tdo.expr.ValueList} - ������ ��������
         *    ����������. ����� ��������� ��� ��� ��� ��������. ���� ��� ������
         *     ������� ����� ������ ���� ��������, �� �� ������ ��������� ������
         *     ������ � <code>values</code> ����� ���
         *     <code>java.lang.String</code>.
         * @return �������� ����, ���������� ��������������� �� ����������
         *       �������������
         * @throws tdo.tools.scanner.ExpressionException, ���� ����������
         *      ���������� ������ 3 ��� �������� ���������, �����������
         *      ������������� ������ ����� <code>null</code>.
         */
        @Override
        protected Object computeFunction(IOperator oper, Object values) {
            String pattern = null;
            String localeLang = null;
            Object v;
            if (values instanceof ValueList) {
                ValueList args = (ValueList) values;
                if (args.size() > 3) {
                    String msg = "Date Function accepts one or two or three parameter";
                    this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                    throw new ExpressionException(msg);
                }
                pattern = (String) args.get(1);
                v = args.get(0);
                if (args.size() == 3) {
                    localeLang = (String) args.get(2);
                }
            } else {
                v = values;
            }
            if (v == null) {
                String msg = "Date Function caannot convert null value";
                this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                throw new ExpressionException(msg);
            }

            Date bd = ExprUtil.dateValueOf(v.toString(), pattern, localeLang);

            return bd;
        }
    }//class DateExpression

    /**
     * &nbsp; &nbsp;&nbsp;&nbsp;���������� �������, ����������� ������� �������� ������� ����,
     * ������������� � �������.
     * <h4>���������:</h4>
     * <b><code>Now()</code></b>
     * <h4>�������� ���:  <i><code>today</code></i></h4>
     * <p>&nbsp; &nbsp;&nbsp;&nbsp;���������� �������� ������� ����,
     * ������������� � �������, ���� <code>java.util.Date.</code>
     */
    public static class NowExpression extends FunctionConvertExpression {

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;������� ��������� ������ ��� ���������
         * ���������, ��������� � ��������.
         *
         * @param context �������� ���������
         * @param operator ������ ���� {@link tdo.expr.FunctionOperator}
         * @param op1 ������ ����
         * {@link tdo.expr.CompoundExpression.CommaListExpression}.
         *
         */
        public NowExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
        }

        /**
         * @return �������� <code>Date.class</code>
         */
        @Override
        public Class getType() {
            return Date.class;
        }

        /**
         *  &nbsp; &nbsp;&nbsp;&nbsp;��������� �������� �������. <p>
         *
         * @param oper ��������
         * @param values ��� ������� �������� �� ����� � ����� ���������
         *   ����� ��������
         * @return �������� ����, ������������� � �������
         */
        @Override
        protected Object computeFunction(IOperator oper, Object values) {
            return new java.util.Date();
        }
    }//class NowExpression

    /**
     * &nbsp;&nbsp;&nbsp;&nbsp;���������� ������� �������������� ����������
     * �������� �������� � ������ ���� ���� <code>java.sql.Timestamp</code>
     * �������� ��������� ������� � ���� �����.
     * <h4>��� �������: <i>Date</i> ��� <i>Dat</i>. </h4>
     * <h4>���������:</h4>
     *
     *   <b>Timestamp(</b><i>date-expr</i>,[<i>pattern</i>[,<i>localeLang</i>]]<b>)</b>
     *
     * <h4>���������:</h4>
     * <ul>
     *   <li><i>date-expr</i>  � ��������� ������������� ����, �������
     *      ������������� � <code>java.sql.Timestamp</code>. �� �����������
     *      �������� <code>null</code>.
     *   </li>
     *   <li><i>pattern</i>   -  ��������� ���������, �������������� ����� ������
     *      (�������) �������� �������� ������� ��������� �������� ����.
     *   </li>
     *   <li><i>localeLang</i>  - ��������� ��� ������, ��� ���������� �������������
     *      ������ <code>java.util.Locale</code>. �������� �ru� - �������, �
     *      �en� - 	����������.
     *   </i>
     * </ul>
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;E��� <code><i>pattern</i></code> �� �����, ��
     *    �������������� ������������ � ��������������  �������� ������� "dd.MM.yy".
     * <p>&nbsp;&nbsp;&nbsp;&nbsp; ���� �� ����� <code><i>localeLang</i></code>,
     *    �� �������������� ����������� �������� ������� �������� ����������
     *    ������������ ������� ����������. ��� ����������� ������������� �������
     *    ���������� ����� ����� ����� ���� ����������� ��� ������������� ���� �
     *    ������� ������ ������.
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;�������� <i><code>date-expr</code></i> ������
     *    <code>null</code>, ������������� � <code>java.sql.Timestamp</code> ��
     *    ��������� <code>Timestamp(0)</code> � ������������.
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;�������������� ���������� � ������� ������
     *   <code>java.text.SimpleDateFormat</code>.
     * <p>
     * 	<b>������</b>. ����� ������ <code><i>date-expr</i></code> ��������
     *      �������� ���� <code>java.sql.Timestamp</code>, ���������������
     *      <b><i>'��� 12 2005'</b><i>.
     *	��� ����� ���� �������� ������ <b>'MMM, d yyyy'</b>.
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;���� ��������� ���������
     * <code>Date( date-expr, 'MMM, d yyyy')</code>, �� ��� Windows � ��������
     * ��������� ����������� ���������� ��������� ����.
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;���� ��������� ���������
     * <code>Date( date-expr, 'MMM, d yyyy','ru')</code>, �� ��� Windows �
     * �������� ��������� ����������� ���������� ��������� ����.
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;���� ��������� ���������
     * <code>Date(date-expr, 'MMM, d yyyy','en')</code>, �� �������������
     * ����������.
     */
    public static class TimestampExpression extends FunctionConvertExpression {

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;������� ��������� ������ ��� ���������
         * ���������, ��������� � ��������.
         *
         * @param context �������� ���������
         * @param operator ������ ���� {@link tdo.expr.FunctionOperator}
         * @param op1 ���� ���������� ����� ������ ��������� �� ��� ������ ����
         * {@link tdo.expr.CompoundExpression.CommaListExpression}.
         *      ���� ��������� ������ ���� ��������, �� ��� ���������
         *      ������������� ���� ���� <code>java.lang.String</code>.
         *
         */
        public TimestampExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            scale = 0;
            size = 6;
        }

        /**
         * @return �������� <code>java.sql.Timestamp.class</code>
         */
        @Override
        public Class getType() {
            return Timestamp.class;
        }

        /**
         *  &nbsp; &nbsp;&nbsp;&nbsp;��������� �������� �������. <p>
         *  &nbsp; &nbsp;&nbsp;&nbsp;���� �������� �������� <code>values</code>
         *  ����� <vode>null</code>, �� ������������� ����������.
         *
         * @param oper ��������
         * @param values ������ ���� {@link tdo.expr.ValueList} - ������ ��������
         *    ����������. ����� ��������� ��� ��� ��� ��������. ���� ��� ������
         *     ������� ����� ������ ���� ��������, �� �� ������ ��������� ������
         *     ������ � <code>values</code> ����� ���
         *     <code>java.lang.String</code>.
         * @return �������� ����, ���������� ��������������� �� ����������
         *       �������������
         * @throws tdo.tools.scanner.ExpressionException, ���� ����������
         *      ���������� ������ 3 ��� �������� ���������, �����������
         *      ������������� ������ ����� <code>null</code>.
         */
        @Override
        protected Object computeFunction(IOperator oper, Object values) {
            String pattern = null;
            Object v;
            String localeLang = null;

            if (values instanceof ValueList) {
                ValueList args = (ValueList) values;
                if (args.size() > 3) {
                    String msg = "Timestamp Function accepts one or two or three parameter";
                    this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                    throw new ExpressionException(msg);
                }
                pattern = (String) args.get(1);
                v = args.get(0);
                if (args.size() == 3) {
                    localeLang = (String) args.get(2);
                }

            } else {
                v = values;
            }
            if (v == null) {
                String msg = "Timestamp Function caannot convert null value";
                this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                throw new ExpressionException(msg);
            }

            Timestamp bd = ExprUtil.timestampValueOf(v.toString(), pattern, localeLang);
            return bd;
        }
    }//class TimestampExpression

    /**
     * &nbsp; &nbsp;&nbsp;&nbsp;��������� �������� ������� �������������� ���������
     * �������� �������� ��������� ������� ��� �������������� ���������
     * �������� � ���������.
     *
     * <h4>��� �������: <i>NumberFormat</i> ��� <i>NumFormat</i> ��� <i>NF</i>.</h4>
     * &nbsp; &nbsp;&nbsp;&nbsp; ���
     * <h4>��� �������: <i>RNumberFormat</i> ��� <i>RNumFormat</i> ��� <i>RNF</i>.</h4>
     * &nbsp; &nbsp;&nbsp;&nbsp;��� ��� ������ ����������� �� �����.
     * ������ ������ ��������� �������������� � ����
     * <code>java.math.RoundingMode.HALF_UP</code>, � ������ � ����
     * <code>java.math.RoundingMode.HALF_EVEN</code>.
     * 
     * <h4>���������:</h4>
     * 
     *   <b>NumberFormat(</b><i>expr</i>[,<i>pattern</i>[,<i>ecsep</i>[,<i>groupsep</i>]]]<b>)</b>
     * <h4>���������:</h4>
     * <ul>
     *   <li><i>expr</i> - ������������� �������� ���������.
     *              <code>null</code> �������� ��������������� ��� Intrger(0).
     *   </li>
     *   <li><i>pattern</i> - ������ ��������������. ���� �� ����� ��� �����
     *     <code>null</code>, �� � <code><i>expr</i></code> ������ �����������
     *     ����� <code>toString()</code>.
     *  </li>
     *   <li><i>ecsep</i> - ������-����������� ����� � ������� �����, �������
     *        ��������� � ��������� ��������������. ���� �� �����, �� ��
     *        ���������, ������� �����.
     *   </li>
     *   <li><i>groupsep</i> - ������-����������� ����� ����, �������
     *        ��������� � ��������� ��������������. ������, ������ ��� �������.
     *         ���� �� �����, �� �� ���������, ������� ������.
     *   </li>
     * </ul>
     * <p>&nbsp; &nbsp;&nbsp;&nbsp;��� �������������� ������������ java �����
     * <code>java.text.DecimalFormat</code>.
     * <p>&nbsp; &nbsp;&nbsp;&nbsp;���� �� ����� ��������
     * <i>code>pattern</code></i>, �� �������������� �� ������������, ��������
     * ������ <code>null</code> ������������� � "0", ����� � �������� �����������
     * ����� <code>toString()</code>.
     * <<h4>������.</h4>
     * &nbsp; &nbsp;&nbsp;&nbsp;���� �����, �������� �������� '12345678.126'.
     * ���������� �������:
     * <pre>
     *      NumberFormat(12345678.125,'########.###')
     * </pre>
     * ���� ���������: <b>12345678.125</b>
     * <p>������� ������ ������-����������� ����� � ������� ������. ����� ���
     * ����� ������ "����� � ��������":
     * <pre>
     *      NumberFormat(12345678.125,'########.###', ';' )
     * </pre>
     * ���� ���������: <b>12345678;125</b>
     * <p>��� ������ �������������, ������ ������-����������� ����� ����,
     *  ��������, ������:
     * <pre>
     *      NumberFormat(12345678.125,'#####,###.###', ';',' ' )
     * </pre>
     * ���� ���������: <b>12 345 678;125<b>
     *
     * <h4>����������</code></h4>
     *
     *  &nbsp; &nbsp;&nbsp;&nbsp;������� � ������� NumberFormat,NumFormat,NF
     * �������� ���������� �������� �� �������� ����������. ��������, ���� ���
     * ����� 12345678.125 ��������� ������ '########.##', �.�. ������ ��� �����
     * ����� ���������� ����� � ��������� ������� <code>NumberFormat</code>, ��
     * ���������� ���������: <b>12345678.13</b>, ��������� ������� ���������
     * �������������� � ���� ���������� ������
     * <code>java.math.RoundingMode.HALF_UP</code>. ���� ��
     * ��������� ������� <code>RNumberFormat</code>, �� ���������� ���������:
     * <b>12345678.12</b>. ��� ������� ����������� � ����
     * <code>java.math.RoundingMode.HALF_EVEN</code>.
     *
     *
     * <p>��� ����� ��������� ���������� c�. <code>java.text.NumberFormat</code>.
     *
     */
    public static class NumberFormatExpression extends FunctionConvertExpression {

        protected String fname;
        private java.text.DecimalFormat df;

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;������� ��������� ������ ��� ���������
         * ���������, ��������� � ��������.
         *
         * @param fname ��� �������. ��������� �������� "NF" ��� "RNF".
         * @param context �������� ���������
         * @param operator ������ ���� {@link tdo.expr.FunctionOperator}
         * @param op1 ���� ���������� ����� ������ ��������� �� ��� ������ ����
         * {@link tdo.expr.CompoundExpression.CommaListExpression}.
         *      ���� ��������� ������ ���� ��������, �� ��� �������� ��������
         *      ���������.
         *
         */
        public NumberFormatExpression(String fname, ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            this.fname = fname;
            scale = 0;
            size = 6;
            //java.text.DecimalFormat decfm = new java.text.DecimalFormat("###.########");
            df = new java.text.DecimalFormat("#,##,###.##");
        }

        /**
         * @return �������� <code>java.lang.String.class</code>
         */
        @Override
        public Class getType() {
            return String.class;
        }

        /**
         *  &nbsp; &nbsp;&nbsp;&nbsp;��������� �������� �������. <p>
         *
         * @param oper ��������
         * @param values ������ ���� {@link tdo.expr.ValueList} - ������ ��������
         *    ����������. ����� ��������� �� ������ �� ������� ���������. ����
         *    ��� ������ ������� ����� ������ ���� ��������, �� �� ������
         *    ��������� �������� ��������. ���� �������� ������� ��������� ������
         *    ������� ����� <code>null</code>, �� ���������, ��� ��� �����
         *    <code>java.lang.Integer(0)</code>.
         * @return ������ ������ ��� ��������� �������������� ��������� ��������.
         * @throws tdo.tools.scanner.ExpressionException, ���� ����������
         *      ���������� ������ 4
         */
        @Override
        protected Object computeFunction(IOperator oper, Object values) {
            String pattern = null;
            char decSeparator = '.';
            char groupSeparator = ' ';

            Object v;
            if (values instanceof ValueList) {
                ValueList args = (ValueList) values;
                if (args.size() > 4) {
                    String msg = "NumberFormat Function accepts up to four parameter";
                    this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                    throw new ExpressionException(msg);
                }
                v = args.get(0);
                if (args.size() > 1) {
                    pattern = (String) args.get(1);
                }
                if (args.size() > 2) {
                    decSeparator = ((String) args.get(2)).charAt(0);
                }
                if (args.size() > 3) {
                    groupSeparator = ((String) args.get(3)).charAt(0);
                }
            } else {
                v = values;
            }
            return toDecimalString(v, pattern, decSeparator, groupSeparator);
        }

        private String toDecimalString(
                Object value,
                String pattern,
                char decimalSeparator,
                char groupingSeparator) {
            Object obj = value;
            String r;//My 06.03.2012 = "0";

            if (pattern != null) {
                if (value == null) {
                    obj = new Integer(0); // cannot be null
                }
                df = new java.text.DecimalFormat();
                df.applyPattern(pattern);
                //df.setGroupingUsed(true);
                java.text.DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
                dfs.setDecimalSeparator(decimalSeparator);
                dfs.setGroupingSeparator(groupingSeparator);

                df.setDecimalFormatSymbols(dfs);
                if (fname.equals("NF")) {
                    df.setRoundingMode(RoundingMode.HALF_UP);
                }

                r = df.format(obj);
            } else {
                if (value == null) {
                    r = "0";
                } else {
                    r = value.toString();
                }
            }

            return r;
        }
    }//class NumberFormatExpression

    /**
     * &nbsp; &nbsp;&nbsp; &nbsp;���������� ������� �������������� ��������
     * ��������, ����������� ���� �������� ��������� ������� � ���� �����  �
     * ��������� ��������.
     * <h4>��� �������: <i>DateFormat</i> ��� <i>DF</i>. <h4>
     * <h4>���������:</h4>
     *
     *   <b>DateFormat(</b><i>date-expr</i>,[<i>pattern</i>[,<i>localeLang</i>]]<b>)</b>
     *
     * <h4>���������:</h4>
     * <ul>
     *   <li><i>date-expr</i>  � ��������� ���� <code>java.util.Date</code>, �������
     *      ������������� � <code>java.lang.String</code>.
     *   </li>
     *   <li><i>pattern</i>   -  ��������� ���������, �������������� ����� ������
     *      (�������) �������� �������� ���� ������ ���� ������������ ���
     *      ������.
     *   </li>
     *   <li><i>localeLang</i>  - ��������� ��� ������, ��� ����������
     *      ������������� ������ <code>java.util.Locale</code>. ��������
     *      �ru� - �������, � �en� - ����������.
     *   </i>
     * </ul>
     * <p>E��� <code><i>pattern</i></code> �� �����, �� ��������������
     *    ������������ � ��������������  �������� ������� "dd.MM.yy".
     * <p>���� �� ����� <code><i>localeLang</i></code>, �� ��������������
     *    ����������� �������� ������� �������� ���������� ������������ �������
     *    ����������. ��� ����������� ������������� ������� ���������� �����
     *    ����� ����� ���� ����������� ��� ������������� ���� � ������� ������
     *    ������.
     * <p>�������� <i><code>date-expr</code></i> ������ <code>null</code>,
     *    ������������� � <code>java.util.Date</code> �� ���������
     *    <code>Date(0)</code> ����� ��������������� � ������ ������.
     * <p>�������������� ���������� � ������� ������
     *   <code>java.text.SimpleDateFormat</code>.
     * <p>
     * 	<b>������</b>. ����� ������ <code><i>date-expr</i></code> ��������
     *      �������� ���� <code>java.util.Date</code>, ���������������
     *      <b><i>12 ���� 2005</b><i>.
     *	��� ����� ���� �������� ������ <b>'MMM, d yyyy'</b>.
     * <p>���� ��������� ��������� Code>DateFormat( date-expr, 'MMM, d yyyy')</code>,
     * �� ��� Windows � �������� ��������� ����������� ������������
     * <b>����, 12 2005�</b>.
     *<p>���� ��������� ��������� <code>DateFormat(date-expr, 'MMM, d yyyy','en')</code>,
     * �� ������������ ������ <b>�Jul, 12 2005�</b>, ��������� �������
     * ���������� �� ���� ���������������� �������, �� ����� ����� ������������
     * ���� � ����������.
     */
    public static class DateFormatExpression extends FunctionConvertExpression {

        private java.text.SimpleDateFormat df;

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;������� ��������� ������ ��� ���������
         * ���������, ��������� � ��������.
         *
         * @param context �������� ���������
         * @param operator ������ ���� {@link tdo.expr.FunctionOperator}
         * @param op1 ���� ���������� ����� ������ ��������� �� ��� ������ ����
         * {@link tdo.expr.CompoundExpression.CommaListExpression}.
         *      ���� ��������� ������ ���� ��������, �� ��� ��������
         *      ��������� ��� ����.
         */
        public DateFormatExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            df = new java.text.SimpleDateFormat("dd.MM.yy");
        }

        /**
         * @return �������� <code>java.lang.String.class</code>
         */
        @Override
        public Class getType() {
            return String.class;
        }

        /**
         *  &nbsp; &nbsp;&nbsp;&nbsp;��������� �������� �������. <p>
         *
         * @param oper ��������
         * @param values ������ ���� {@link tdo.expr.ValueList} - ������ ��������
         *    ����������. ����� ��������� ��� ��� ��� ��������. ���� ��� ������
         *     ������� ����� ������ ���� ��������, �� �� � , ��������������,
         *     <code>values</code> ������ ��������� ��������� ����
         *     <code>java.util.Date</code>.
         * @return ������ ������, ���������� ��������������� �� ����
         * @throws tdo.tools.scanner.ExpressionException, ���� ����������
         *      ���������� ������ 3
         */
        @Override
        protected Object computeFunction(IOperator oper, Object values) {
            String pattern = null;
            String localeLang = null;
            Date d;
            if (values instanceof ValueList) {
                ValueList args = (ValueList) values;
                if (args.size() > 3) {
                    String msg = "DateFormat Function accepts up to three parameter";
                    this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                    throw new ExpressionException(msg);
                }
                d = (Date) args.get(0);
                if (args.size() > 1) {
                    pattern = (String) args.get(1);
                }
                if (args.size() > 2) {
                    localeLang = (String) args.get(2);
                }

            } else {
                d = (Date) values;
            }
            return format(d, pattern, localeLang);
        }

        private String format(
                Date value,
                String pattern,
                String localeLang) {

            if (localeLang != null) {
                df = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.FULL, new Locale(localeLang));
            } else {
                df = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.FULL);
            }
            String r;//My 06.03.2012 = null;
            //SimpleDateFormat df = new SimpleDateFormat();
            df.applyPattern(pattern != null ? pattern : "dd.MM.yy");
            try {
                r = df.format(value);
            } catch (Exception e) {
                r = df.format(new Date(0));
            }
            return r;
        }
    }//class DateFormatExpression

    /**
     * &nbsp; &nbsp;&nbsp;&nbsp; ������������ ���������� ������� �� ������
     * <code>java.math</code>.
     * &nbsp; &nbsp;&nbsp;&nbsp;��� ������� ������ �� ������
     * <code>java.Math</code> ���������� ����������� ������� ���������.
     * &nbsp; &nbsp;&nbsp;&nbsp;� ������� �������� ������ ������������
     * ������� ������� ������ <code>java.math</code>.
     * <table border="1">
     * <thead>
     * <tr>
     *      <th>���</th>
     *      <th>��������</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td>ABS</td>
     * <td>�������������  ������� �� ������ <code>java.Math.abs(Double)</code></td>
     * </tr>
     * <tr>
     * <td>ACOS</td>
     * <td>�������������  ������� �� ������ <code>java.Math.acos(Double)</code></td>
     * </tr>
     * <tr>
     * <td>ASIN</td>
     * <td>�������������  ������� �� ������ <code>java.Math.asin(Double)</code></td>
     * </tr>
     * <tr>
     * <td>ATAN</td>
     * <td>�������������  ������� �� ������ <code>java.Math.atan(Double)</code></td>
     * </tr>
     * <tr>
     * <td>CEIL</td>
     * <td>�������������  ������� �� ������ <code>java.Math.ceil(Double)</code></td>
     * </tr>
     * <tr>
     * <td>COS</td>
     * <td>�������������  ������� �� ������ <code>java.Math.cos(Double)</code></td>
     * </tr>
     * <tr>
     * <td>SIN</td>
     * <td>�������������  ������� �� ������ <code>java.Math.sin(Double)</code></td>
     * </tr>
     * <tr>
     * <td>TAN</td>
     * <td>�������������  ������� �� ������ <code>java.Math.tan(Double)</code></td>
     * </tr>
     * <tr>
     * <td>EXP</td>
     * <td>�������������  ������� �� ������ <code>java.Math.exp(Double)</code></td>
     * </tr>
     * <tr>
     * <td>FLOOR</td>
     * <td>�������������  ������� �� ������ <code>java.Math.floor(Double)</code></td>
     * </tr>
     * <tr>
     * <td>LOG</td>
     * <td>�������������  ������� �� ������ <code>java.Math.log(Double)</code></td>
     * </tr>
     * <tr>
     * <td>RINT</td>
     * <td>�������������  ������� �� ������ <code>java.Math.rint(Double)</code></td>
     * </tr>
     * <tr>
     * <td>ROUND</td>
     * <td>�������������  ������� �� ������ <code>java.Math.round(Double)</code></td>
     * </tr>
     * <tr>
     * <td>SQRT</td>
     * <td>�������������  ������� �� ������ <code>java.Math.sqrt(Double)</code></td>
     * </tr>
     * <tr>
     * <td>TODEGREES</td>
     * <td>�������������  ������� �� ������ <code>java.Math.toDegrees(Double)</code></td>
     * </tr>
     * <tr>
     * <td>TORADIANS</td>
     * <td>�������������  ������� �� ������ <code>java.Math.toRadians(Double)</code></td>
     * </tr>
     * <tr>
     * <td>ATAN2</td>
     * <td>�������������  ������� �� ������ <code>java.Math.atan2(Double,Double)</code></td>
     * </tr>
     * <tr>
     * <td>POW</td>
     * <td>�������������  ������� �� ������ <code>java.Math.pow(Double,Double)</code></td>
     * </tr>
     * <tr>
     * <td>RANDOM</td>
     * <td>�������������  ������� �� ������ <code>java.Math.random()</code></td>
     * </tr>
     * <tr>
     * <td>IEEEREMAINDER</td>
     * <td>�������������  ������� �� ������ <code>java.Math.IEEEremainder(Double,Double)</code></td>
     * </tr>
     * </tbody>
     * </table>
     */
    public static class JavaMathExpression extends FunctionExpression {

        /**
         * ��� �������, ����������� �������������.
         */
        protected String fname;

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;������� ��������� ������ ��� ���������
         * ���������, ��������� � ��������.
         *
         * @param context �������� ���������
         * @param operator ������ ���� {@link tdo.expr.FunctionOperator}
         * @param op1 ���� ���������� ����� ������ ��������� �� ��� ������ ����
         * {@link tdo.expr.CompoundExpression.CommaListExpression}. ��������
         *   � ���� ��������� ������� �� ���������� �������
         */
        public JavaMathExpression(String fname, ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            this.fname = fname;
        }

        /**
         * ��������� �������� �������. <p>
         *
         * @param oper
         * @param values �������� ��� ������ �������� ����
         * {@link tdo.expr.ValueList} � ����������� �� ���������� �������
         * @return �������� �������.
         */
        @Override
        protected Object computeFunction(IOperator oper, Object values) {
            Double r = null;

            if (values instanceof ValueList) {
                ValueList args = (ValueList) values;
                if (args.size() != 2) {
                    String msg = "JavaMathFunction accepts two parameter";
                    this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                    throw new ExpressionException(msg);
                }
                Double d1 = MathExpression.toDouble(args.get(0));
                Double d2 = MathExpression.toDouble(args.get(1));
                if (fname.equals("ATAN2")) {
                    r = Math.atan2(d1, d2);
                } else if (fname.equals("POW")) {
                    r = Math.pow(d1, d2);
                } else if (fname.equals("IEEEREMAINDER")) {
                    r = Math.IEEEremainder(d1, d2);
                }
            } else {
                Double d = MathExpression.toDouble(values);
                if (fname.equals("RANDOM")) {
                    r = Math.random();
                } else if (fname.equals("ABS")) {
                    r = Math.abs(d);
                } else if (fname.equals("ACOS")) {
                    r = Math.acos(d);
                } else if (fname.equals("ASIN")) {
                    r = Math.asin(d);
                } else if (fname.equals("ATAN")) {
                    r = Math.atan(d);
                } else if (fname.equals("CEIL")) {
                    r = Math.ceil(d);
                } else if (fname.equals("COS")) {
                    r = Math.cos(d);
                } else if (fname.equals("EXP")) {
                    r = Math.exp(d);
                } else if (fname.equals("FLOOR")) {
                    r = Math.floor(d);
                } else if (fname.equals("LOG")) {
                    r = Math.log(d);
                } else if (fname.equals("RINT")) {
                    r = Math.rint(d);
                } else if (fname.equals("ROUND")) {
                    r = new Double(Math.round(d));
                } else if (fname.equals("SIN")) {
                    r = Math.sin(d);
                } else if (fname.equals("SQRT")) {
                    r = Math.sqrt(d);
                } else if (fname.equals("TAN")) {
                    r = Math.tan(d);
                } else if (fname.equals("TODEGREES")) {
                    r = Math.toDegrees(d);
                } else if (fname.equals("TORADIANS")) {
                    r = Math.toRadians(d);
                }
            }
            return r;
        }

        /**
         * @return ���������� �������� <code>java.lang.Double.class</code>
         */
        @Override
        public Class getType() {
            return Double.class;
        }
    } //class JavaMathExpression
    } //class FunctionExpression
