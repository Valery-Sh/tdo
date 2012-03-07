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
     * &nbsp; &nbsp;&nbsp;&nbsp;Определяет объект, вычисляющий выражение функции
     * преобразования символов строки знаков в верхний регистр.
     * <h4>Синтаксис:</h4>
     *      <b>UpperCase(</b> <i>str-expr</i> <b> )</b>
     * <h4>Алиасные имена: <i>U, Upper</i> </h4>
     * &nbsp; &nbsp;&nbsp;&nbsp;Переводит символы строки в верхний регистр.
     * <p>&nbsp; &nbsp;&nbsp;&nbsp;Если значение <code><i>str-expr</i></code>
     * равно <code>null</code>, то преобразования не производится.
     * Для остальных значений выражение преобразуется в строку применением
     * метода <code>toString()</code>.
     */
    public static class UpperCaseExpression extends FunctionExpression {

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;Создает экземпляр класса для заданного
         * контекста, оператора и операнда.
         * @param context контекст выражения
         * @param operator объект типа {@link tdo.expr.FunctionOperator}
         * @param op1 опреранд любого типа. Может принимать значение
         *      <code>null</code>.
         */
        public UpperCaseExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
        }

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp; Выполняет вычисление значения функции. <p>
         * @param oper оператор
         * @param values преобразуемая строка знаков
         * @return <code>null</code>, если <vode>values==null</code>. Иначе
         *   для <code>values</code> применяется метод <code>toString</code> и,
         *   затем, метод <code>toUpperCase</code>.
         */
        @Override
        protected Object computeFunction(IOperator oper, Object values) {
            return values == null ? null : values.toString().toUpperCase();
        }
        /**
         * @return значение <code>String.class</code>
         */
        @Override
        public Class getType() {
            return String.class;
        }
    } //class UpperCaseExpression
    /**
     * &nbsp; &nbsp;&nbsp;&nbsp;Определяет объект, вычисляющий выражение функции
     * преобразования символов строки знаков в нижний регистр.
     * <h4>Синтаксис:</h4>
     *      <b>LowerCase( </b><i>str-expr</i><b> )</b>
     * <h4>Алиасные имена: <i>Lower, L</i>
     * &nbsp; &nbsp;&nbsp;&nbsp;Переводит символы строки в нижний регистр.
     * <p>&nbsp; &nbsp;&nbsp;&nbsp;Если значение <code><i>str-expr</i></code>
     * равно <code>null</code>, то преобразования не производится. Для остальных
     * значений выражение преобразуется в строку применением метода
     * <code>toString()</code>.
     */
    public static class LowerCaseExpression extends FunctionExpression {

        /**
         * Создает экземпляр класса для заданного контекста, оператора и
         * операнда.
         * @param context контекст выражения
         * @param operator объект типа {@link tdo.expr.FunctionOperator}
         * @param op1 опреранд любого типа. Может принимать значение
         *      <code>null</code>.
         */
        public LowerCaseExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
        }
        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;Выполняет вычисление значения функции. <p>
         * @param oper оператор
         * @param values преобразуемая строка знаков
         * @return <code>null</code>, если <vode>values==null</code>. Иначе
         *   для <code>values</code> применяется метод <code>toString</code> и,
         *   затем, метод <code>toLowerCase</code>.
         */
        @Override
        protected Object computeFunction(IOperator oper, Object values) {
            return values == null ? null : values.toString().toLowerCase();
        }
        /**
         * @return значение <code>String.class</code>
         */
        @Override
        public Class getType() {
            return String.class;
        }
    } //class LowerCaseExpression

    /**
     * &nbsp; &nbsp;&nbsp;&nbsp;Определяет объект, вычисляющий выражение функции
     * преобразования первого символа строки знаков в верхний регистр.
     * <h4>Синтаксис:</h4>
     *      <b>Proper(</b> <i>str-expr</i> <b>)</b>
     * <h4>Алиасные имена: <i>P</i>
     * <p>&nbsp; &nbsp;&nbsp;&nbsp;Переводит первый символ строки в верхний регистр.
     * <p>&nbsp; &nbsp;&nbsp;&nbsp;Если значение <code><i>str-expr</i></code>
     * равно <code>null</code>, то преобразования не производится.
     */
    public static class ProperExpression extends FunctionExpression {
        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;Создает экземпляр класса для заданного
         * контекста, оператора и операнда.
         * @param context контекст выражения
         * @param operator объект типа {@link tdo.expr.FunctionOperator}
         * @param op1 опреранд любого типа. Может принимать значение
         *      <code>null</code>.
         */
        public ProperExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
        }
        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;Выполняет вычисление значения функции. <p>
         * @param oper оператор
         * @param values преобразуемая строка знаков
         * @return <code>null</code>, если <vode>values==null</code>. Иначе
         *   для <code>values</code> применяется преобразование.
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
         * @return значение <code>String.class</code>
         */
        @Override
        public Class getType() {
            return String.class;
        }
    } //class ProperExpression

    /**
     * &nbsp;&nbsp;&nbsp;&nbsp;Определяет объект, вычисляющий выражение функции 
     * выделения подстроки из заданной строки знаков.
     *
     * <h4>Синтаксис:</h4>
     *      <b>Substring(</b> <i>source</i>, <i>startpos</i> [ , <i>endpos</i> ]<b> )</b>
     * <h4>Алиасные имена: <i>Substr</i></h4>
     * &nbsp;&nbsp;&nbsp;&nbsp;Выделяет подстроку, начиная с позиции
     * <code>startpos</code> и заканчивая позицией <code>endpos</code> из
     * заданной строки <code>source</code>.
     * <p>Если параметр <code>endpos</code> не задан, то принимается равным
     * <code><i>source.length - 1</i><code>.
     *
     * <p><b>Примечание.</b> Нумерация символов начинается с 0.
     * <p>&nbsp; &nbsp;&nbsp;&nbsp;Если значение source равно <code>null</code>, то преобразования не
     * производится. Для остальных значений выражение преобразуется в строку
     * применением метода <code>toString()</code>.
     */
    public static class SubstringExpression extends FunctionExpression {

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;Создает экземпляр класса для заданного
         * контекста, оператора и операнда.
         * @param context контекст выражения
         * @param operator объект типа {@link tdo.expr.FunctionOperator}
         * @param op1 выражение типа
         *      {@link tdo.expr.CompoundExpression.CommaListExpression}.
         */
        public SubstringExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
        }

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;Выполняет вычисление значения функции. <p>
         * @param oper оператор
         * @param values объект типа {@link tdo.expr.ValueList}, содержащий
         *  список параметров
         * @return <code>null</code>, если значение исходной строки знаков
         *  равно <vode>null</code>. Иначе, возвращается выделенная подстрока
         * @throws tdo.tools.scanner.ExpressionException, если количество
         *      параметров меньше двух или больше трех. Или задано значение
         *      начальной или конечной позиции равное <code>null</code>. Или
         *      начальная/конечная позиция не является числом или конечная
         *      позиция меньше начальной.
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
         * @return значение <code>String.class</code>
         */
        @Override
        public Class getType() {
            return String.class;
        }
    } //class SubstringExpression

    /**
     * &nbsp; &nbsp;&nbsp;&nbsp;Определяет функцию дополнения строки знаков до
     * заданной длины заданным символом.<p>
     * 
     * 
     * <h4>Имя функции: <i>PADL</i> или <i>PL</i> или <i>PADR</i> или <i>PR</i>.
     *
     * &nbsp; &nbsp;&nbsp;&nbsp;Функция PADL и PL дополняют слева, а функции
     * PADR и PR справа.
     *
     * <h4>Синтаксис:</h4>
     * 
     *&nbsp; &nbsp;&nbsp;&nbsp;<b>PADL(</b> <i>expr</i>,<i>size</i>[<i>char</i>] <b>)</b>
     *&nbsp; &nbsp;&nbsp;&nbsp;<b>PADR(</b><i>expr</i>,<i>size</i>[<i>char</i>]<b>)</b>
     * 
     * <p>&nbsp; &nbsp;&nbsp;&nbsp;Если <i>expr</i> имеет тип Number и параметр
     * <i>char</i> опущен, то, по умолчанию, дополнение производится символом '0'.
     * <p>&nbsp; &nbsp;&nbsp;&nbsp;
     *  Если <i>expr</i> имеет тип отличный от Number и параметр <i>char</i>
     * опущен, то, по умолчанию дополнение производится символом пробела.
     */
    public static class PadExpression extends FunctionExpression {

        protected String fname;

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;Создает экземпляр класса для заданного
         * контекста, оператора и операнда.
         * @param fname символьное имя функции, так как класс обрабатывает две
         *   функции с именами <code>PADL</code> и <code>PADR</code>.
         *
         * @param context контекст выражения
         * @param operator объект типа {@link tdo.expr.FunctionOperator}
         * @param op1 выражение типа
         *      {@link tdo.expr.CompoundExpression.CommaListExpression}.
         */
        public PadExpression(String fname, ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            this.fname = fname;
        }

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;Вычисляет значения функции. <p>
         * @param oper оператор
         * @param values объект типа {@link tdo.expr.ValueList}, содержащий
         *  список параметров
         * @return <code>null</code>, если значение исходной строки знаков
         *  равно <vode>null</code>. Иначе, возвращается дополненная требуемым
         *  символом строка
         * @throws tdo.tools.scanner.ExpressionException, если количество
         *      параметров меньше двух или больше трех.
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
         * @return значение <code>String.class</code>
         */
        @Override
        public Class getType() {
            return String.class;
        }
    } //class PadExpression

    /**
     * &nbsp; &nbsp; &nbsp; &nbsp; Определяет функцию отсечения лидирующих и
     * замыкающих символов заданной строки знаков.
     * <h4>Имя функции: <i>TRIM</i> или <i>TR</i> </h4>
     *
     *
     * <h4>Синтаксис:<h4>
     *   <b>TRIM(</b><i>expr</i><b>)</b>
     * 
     */
    public static class TrimExpression extends FunctionExpression {

        protected String fname;

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;Создает экземпляр класса для заданного
         * контекста, оператора и операнда.
         * @param fname символьное имя функции.
         *
         * @param context контекст выражения
         * @param operator объект типа {@link tdo.expr.FunctionOperator}
         * @param op1 операнд, содержащий строковое выражение
         */
        public TrimExpression(String fname, ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            this.fname = fname;
        }

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;Вычисляет значения функции. <p>
         * @param oper оператор
         * @param values объект типа {@link tdo.expr.ValueList}, содержащий
         *  список параметров
         * @return <code>null</code>, если значение операнда
         *  равно <vode>null</code>. Иначе, возвращается дополненная требуемым
         *  символом строка
         * @throws tdo.tools.scanner.ExpressionException, если количество
         *      параметров больше одного.
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
         * @return значение <code>String.class</code>
         */
        @Override
        public Class getType() {
            return String.class;
        }
    } //class TrimExpression

    /**
     * &nbsp; &nbsp;&nbsp;&nbsp;Определяет функцию преобразовывающую слова
     * предложения, таким образом, что 1-я литера каждого слова начинается с
     * заглавной буквы.
     * 
     * 
     * <h4>Имя функции: <i>Header</i> или <i>Hd</i>. </h4>
     * <h4>Синтаксис:</h4>
     *
     *   <b>Header(</b><i>expr</i>[,<i>delimeters</i>]<b>)</b>

     * &nbsp; &nbsp;&nbsp;&nbsp;Если  <i>edelimeters</i> опущен, то по умолчанию
     *  он принимает строковое значение: " .,:-_;", или
     * <i>пробел,точка,запятая,двоеточие,дефис, подчеркивание, точка с запятой</i>.
     */
    public static class HeaderExpression extends FunctionExpression {

        protected String fname;

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;Создает экземпляр класса для заданного
         * контекста, оператора и операнда.
         * @param fname символьное имя функции.
         *
         * @param context контекст выражения
         * @param operator объект типа {@link tdo.expr.FunctionOperator}
         * @param op1 {@link tdo.expr.CompoundExpression.CommaListExpression}.
         */
        public HeaderExpression(String fname, ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            this.fname = fname;
        }

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;Вычисляет значения функции. <p>
         * @param oper оператор
         * @param values объект типа {@link tdo.expr.ValueList}, содержащий
         *  список параметров или, если вызов функции производится с одним
         *  параметром, то объект произвольного тапа
         * @return <code>null</code>, если значение операнда
         *  равно <vode>null</code>. Иначе, возвращается преобразованная строка
         * @throws tdo.tools.scanner.ExpressionException, если количество
         *      параметров больше двух.
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
         * @return значение <code>String.class</code>
         */
        @Override
        public Class getType() {
            return String.class;
        }

        /**
         * Переводит первый символ строкм в верхний регистр.<p>
         * @param source преобразуемая строка
         * @return Строка, первый символ которой преобразован в верхний регистр.
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
     * Обеспечивает вычисление значения функции <code>IIF</code>.
     * <h4>Синтаксис:</h4>
     * <b>IIF(</b> <i>cond-expr</i>, <i>value1</i>, <i>value2</i> <b>)</b>
     * <h4>Параметры:</h4>
     * <ul>
     *      <li><i>cond-expr</i> первый операнд - булевое выражение, значение которого
     *      равное <code>true</code> определяет, что возвращаемым значением
     *      является <code><i>value1</i></code>. Значение равное
     *      <code>false</code> определяет, что возвращаемым значением
     *      является <code><i>value2</i></code>.
     *      </li>
     *      <li><i>value1<i> - возвращается,когда условие, заданное
     *          1-м параметром возврпщает <code>true</code>
     *      </li>
     *      <li><i>value2<i> - возвращается,когда условие, заданное
     *          1-м параметром возврпщает <code>false</code>
     *      </li>
     * </ul>
     */
    public static class IIFExpression extends FunctionExpression {

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;Создает экземпляр класса для заданного
         * контекста, оператора и операнда.
         *
         * @param context контекст выражения
         * @param operator объект типа {@link tdo.expr.FunctionOperator}
         * @param op1 {@link tdo.expr.CompoundExpression.CommaListExpression}.
         */
        public IIFExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
        }

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;Вычисляет значения функции. <p>
         * @param oper оператор
         * @param values объект типа {@link tdo.expr.ValueList}, содержащий
         *  список параметров или, если вызов функции производится с одним
         *  параметром, то объект произвольного тапа
         * @return <code>null</code>, если значение операнда
         *  равно <vode>null</code>. Иначе, возвращается преобразованная строка
         * @throws tdo.tools.scanner.ExpressionException, если количество
         *      параметров больше не равно трем или когда тип значение первого
         *       операнда отличен от <code>Boolean.class</code>.
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
         * @return значение <code>Object.class</code>
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
     * Базовый класс для классов, выполняющих преобразование значений из одного
     * типа в другой.
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
     * Определяет функцию преобразования операнда в тип java.math.BigDecimal.
     * <h4>Имя функции: <i>BigDecimal</i> или <i>Dec</i>.</h4>
     * <h4>Синтаксис:</h4>
     * 
     *   <b>BigDecimal(</b><i>expr</i>[,scale]<b>)</b>
     * 
     */
    public static class BigDecimalExpression extends FunctionConvertExpression {

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;Создает экземпляр класса для заданного
         * контекста, оператора и операнда.
         *
         * @param context контекст выражения
         * @param operator объект типа {@link tdo.expr.FunctionOperator}
         * @param op1 {@link tdo.expr.CompoundExpression.CommaListExpression}
         *  или объект произвольного типа, который может быть преобразован
         *  в числовое значение.
         */
        public BigDecimalExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
        }

        /**
         * @return значение <code>BigDecimal.class</code>
         */
        @Override
        public Class getType() {
            return BigDecimal.class;
        }

        /**
         * Возвращает значение параметра <code>scale</code>, заданного при вызове
         * функции.
         * @return значение параметра <code>scalr</code>.
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
         * &nbsp; &nbsp;&nbsp;&nbsp;Вычисляет значения функции. <p>
         *  &nbsp; &nbsp;&nbsp;&nbsp;Если значение операнда <code>values</code>
         *  равно <vode>null</code>, то считается, что значением является
         *  объект <code>BigDecimal(0)</code>.
         *  &nbsp; &nbsp;&nbsp;&nbsp;Исходное значение преобразуется в
         * <code>BigDecimal</code> и к полученному результату применяется: <br>
         * &nbsp; &nbsp;&nbsp;&nbsp;setScale(<i>scale</i>, BigDecimal.ROUND_HALF_UP);
         * <br>
         * где <code><i>scale</i></code> - значение параметра <code>scale</code>.
         * <p> &nbsp; &nbsp;&nbsp;&nbsp;<b>Примечание.</b> Если при вызове
         * функции не задан параметр <code>scale</code>, то по умолчанию
         * принимается равным 2.
         *
         * @param oper оператор
         * @param values объект типа {@link tdo.expr.ValueList}, содержащий
         *  список параметров или, если вызов функции производится с одним
         *  параметром, то объект произвольного тапа, который может быть
         *  преобразован в <code>java.math.BigDecimal</code>.
         * @return преобразованное значение
         * @throws tdo.tools.scanner.ExpressionException, если количество
         *      параметров больше не равно 1 или 2 или когда явно задан параметр
         *      <code>scale</code> и его значение равно <code>null</code> или
         *      тип значения не равен <code>java.lang.Number</code>.
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
     * Определяет функцию преобразования операнда в тип
     * <code>java.lang.Double</code>.
     * <h4>Имя функции: <i>Double</i> или <i>Dou</i>.</h4>
     * <h4>Синтаксис:</h4>
     * 
     *   <b>Double(</b><i>expr</i><b>)</b><LI>
     * 
     */
    public static class DoubleExpression extends FunctionConvertExpression {

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;Создает экземпляр класса для заданного
         * контекста, оператора и операнда.
         *
         * @param context контекст выражения
         * @param operator объект типа {@link tdo.expr.FunctionOperator}
         * @param op1 объект произвольного типа, который может быть преобразован
         *  в числовое значение.
         */
        public DoubleExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            scale = -1;
            size = 15;
        }

        /**
         * @return значение <code>Double.class</code>
         */
        @Override
        public Class getType() {
            return Double.class;
        }

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;Вычисляет значения функции. <p>
         *  &nbsp; &nbsp;&nbsp;&nbsp;Если значение операнда <code>values</code>
         *  равно <vode>null</code>, то считается, что значением является
         *  объект <code>BigDecimal(0)</code>.
         *
         * @param oper оператор
         * @param values значение, которое может быть
         *  преобразовано в <code>java.lang.Double</code>.
         * @return преобразованное значение
         * @throws tdo.tools.scanner.ExpressionException, если количество
         *      параметров больше 1.
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
     * Определяет функцию преобразования операнда в тип
     * <code>java.lang.Float</code>.
     * <h4>Имя функции: <i>Float</i> или <i>Flo</i> или <i>Real</i>.</h4>
     * <h4>Синтаксис:</h4>
     *
     *   <b>Float(</b><i>expr</i><b>)</b><LI>
     *
     */
    public static class FloatExpression extends FunctionConvertExpression {

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;Создает экземпляр класса для заданного
         * контекста, оператора и операнда.
         *
         * @param context контекст выражения
         * @param operator объект типа {@link tdo.expr.FunctionOperator}
         * @param op1 объект произвольного типа, который может быть преобразован
         *  в числовое значение.
         */
        public FloatExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            scale = -1;
            size = 15;
        }

        /**
         * @return значение <code>Double.class</code>
         */
        @Override
        public Class getType() {
            return Float.class;
        }

        /**
         *  &nbsp; &nbsp;&nbsp;&nbsp;Вычисляет значения функции. <p>
         *  &nbsp; &nbsp;&nbsp;&nbsp;Если значение операнда <code>values</code>
         *  равно <vode>null</code>, то считается, что значением является
         *  объект <code>Float(0)</code>.
         *
         * @param oper оператор
         * @param values значение, которое может быть
         *  преобразовано в <code>java.lang.Float</code>.
         * @return преобразованное значение
         * @throws tdo.tools.scanner.ExpressionException, если количество
         *      параметров больше 1.
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
     * Определяет функцию преобразования операнда в тип
     * <code>java.lang.Integer</code>.
     * <h4>Имя функции: <i>Integer</i> или <i>Int</i>.</h4>
     * <h4>Синтаксис:</h4>
     *
     *   <b>Integer(</b><i>expr</i><b>)</b><LI>
     *
     */
    public static class IntegerExpression extends FunctionConvertExpression {

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;Создает экземпляр класса для заданного
         * контекста, оператора и операнда.
         *
         * @param context контекст выражения
         * @param operator объект типа {@link tdo.expr.FunctionOperator}
         * @param op1 объект произвольного типа, который может быть преобразован
         *  в числовое значение.
         */
        public IntegerExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            scale = 0;
            size = 10;
        }

        /**
         * @return значение <code>Integer.class</code>
         */
        @Override
        public Class getType() {
            return Integer.class;
        }

        /**
         *  &nbsp; &nbsp;&nbsp;&nbsp;Вычисляет значения функции. <p>
         *  &nbsp; &nbsp;&nbsp;&nbsp;Если значение операнда <code>values</code>
         *  равно <vode>null</code>, то считается, что значением является
         *  объект <code>Integer(0)</code>.
         *
         * @param oper оператор
         * @param values значение, которое может быть
         *  преобразовано в <code>java.lang.Integer</code>.
         * @return преобразованное значение
         * @throws tdo.tools.scanner.ExpressionException, если количество
         *      параметров больше 1.
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
     * Определяет функцию преобразования операнда в тип
     * <code>java.lang.Long</code>.
     * <h4>Имя функции: <i>Long</i> или <i>BigInt</i> или <i>Lo</i>.</h4>
     * <h4>Синтаксис:</h4>
     *
     *   <b>Long(</b><i>expr</i><b>)</b><LI>
     *
     */
    public static class LongExpression extends FunctionConvertExpression {

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;Создает экземпляр класса для заданного
         * контекста, оператора и операнда.
         *
         * @param context контекст выражения
         * @param operator объект типа {@link tdo.expr.FunctionOperator}
         * @param op1 объект произвольного типа, который может быть преобразован
         *  в числовое значение.
         */
        public LongExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            scale = 0;
            size = 20;
        }

        /**
         * @return значение <code>Long.class</code>
         */
        @Override
        public Class getType() {
            return Long.class;
        }

        /**
         *  &nbsp; &nbsp;&nbsp;&nbsp;Вычисляет значения функции. <p>
         *  &nbsp; &nbsp;&nbsp;&nbsp;Если значение операнда <code>values</code>
         *  равно <vode>null</code>, то считается, что значением является
         *  объект <code>Long(0)</code>.
         *
         * @param oper оператор
         * @param values значение, которое может быть
         *  преобразовано в <code>java.lang.Long</code>.
         * @return преобразованное значение
         * @throws tdo.tools.scanner.ExpressionException, если количество
         *      параметров больше 1.
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
     * Определяет функцию преобразования операнда в тип
     * <code>java.lang.Short</code>.
     * <h4>Имя функции: <i>Short</i> или <i>SmallInt</i> или <i>Small</i>.</h4>
     * <h4>Синтаксис:</h4>
     *
     *   <b>Short(</b><i>expr</i><b>)</b><LI>
     *
     */
    public static class ShortExpression extends FunctionConvertExpression {

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;Создает экземпляр класса для заданного
         * контекста, оператора и операнда.
         *
         * @param context контекст выражения
         * @param operator объект типа {@link tdo.expr.FunctionOperator}
         * @param op1 объект произвольного типа, который может быть преобразован
         *  в числовое значение.
         */
        public ShortExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            scale = 0;
            size = 6;
        }

        /**
         * @return значение <code>Short.class</code>
         */
        @Override
        public Class getType() {
            return Short.class;
        }

        /**
         *  &nbsp; &nbsp;&nbsp;&nbsp;Вычисляет значения функции. <p>
         *  &nbsp; &nbsp;&nbsp;&nbsp;Если значение операнда <code>values</code>
         *  равно <vode>null</code>, то считается, что значением является
         *  объект <code>Short(0)</code>.
         *
         * @param oper оператор
         * @param values значение, которое может быть
         *  преобразовано в <code>java.lang.Short</code>.
         * @return преобразованное значение
         * @throws tdo.tools.scanner.ExpressionException, если количество
         *      параметров больше 1.
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
     * Определяет функцию преобразования операнда в тип
     * <code>java.lang.Short</code>.
     * <h4>Имя функции: <i>Short</i> или <i>TinyInt</i> или <i>Tiny</i>.</h4>
     * <h4>Синтаксис:</h4>
     *
     *   <b>Short(</b><i>expr</i><b>)</b><LI>
     *
     */
    public static class ByteExpression extends FunctionConvertExpression {

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;Создает экземпляр класса для заданного
         * контекста, оператора и операнда.
         *
         * @param context контекст выражения
         * @param operator объект типа {@link tdo.expr.FunctionOperator}
         * @param op1 объект произвольного типа, который может быть преобразован
         *  в числовое значение.
         */
        public ByteExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            scale = 0;
            size = 6;
        }

        /**
         * @return значение <code>Byte.class</code>
         */
        @Override
        public Class getType() {
            return Byte.class;
        }

        /**
         *  &nbsp; &nbsp;&nbsp;&nbsp;Вычисляет значения функции. <p>
         *  &nbsp; &nbsp;&nbsp;&nbsp;Если значение операнда <code>values</code>
         *  равно <vode>null</code>, то считается, что значением является
         *  объект <code>Byte(0)</code>.
         *
         * @param oper оператор
         * @param values значение, которое может быть
         *  преобразовано в <code>java.lang.Byte</code>.
         * @return преобразованное значение
         * @throws tdo.tools.scanner.ExpressionException, если количество
         *      параметров больше 1.
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
     * &nbsp; &nbsp;&nbsp; &nbsp;Определяет функцию создания даты по заданным
     * году, месяцу и дню. <p>
     *
     * <h4>Имя функции: <i><b>$</b></i>.</h4>
     * <h4>Синтаксис:</h4>
     *
     *   &nbsp; &nbsp;&nbsp; &nbsp;
     * <b>$(</b><i>year-expr</i>, <i>month-expr</i>,<i>day-expr</i><b>)</b>
     * <h4>Параметры:</h4>
     * <ul>
     *   <li><i>year-expr</i> - выражение типа <code>java.lang.Integer</code>
     *          представляющее год.
     *   </li>
     *   <li><i>month-expr</i> - выражение типа <code>java.lang.Integer</code>
     *          представляющее месяц.
     *   </li>
     *   <li><i>day-expr</i> - выражение типа <code>java.lang.Integer</code>
     *          представляющее дату месяца.
     *   </li>
     * </ul>
     *
     */
    public static class DateConstantExpression extends FunctionConvertExpression {

        SimpleDateFormat df;

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;Создает экземпляр класса для заданного
         * контекста, оператора и операнда.
         *
         * @param context контекст выражения
         * @param operator объект типа {@link tdo.expr.FunctionOperator}
         * @param op1 объект типа {@link tdo.expr.CompoundExpression.CommaListExpression}
         *
         */
        public DateConstantExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            df = new SimpleDateFormat("dd.MM.yyyy");
        }

        /**
         * @return Значение <code>Date.class</code>
         */
        @Override
        public Class getType() {
            return Date.class;
        }

        /**
         *  &nbsp; &nbsp;&nbsp;&nbsp;Вычисляет значения функции. <p>
         *
         * @param oper оператор
         * @param values объект типа {@link tdo.expr.ValueList} - список значений
         *    года, месяца и даты месяца.
         * @return новое значение даты
         * @throws tdo.tools.scanner.ExpressionException, если количество
         *      параметров не равно 3.
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
     * &nbsp;&nbsp;&nbsp;&nbsp;Определяет функцию преобразования строкового
     * значения операнда в объект даты типа <code>java.util.Date</code>
     * согласно заданному шаблону и коду языка.
     * <h4>Имя функции: <i>Date</i> или <i>Dat</i>. </h4>
     * <h4>Синтаксис:</h4>
     *
     *   <b>Date(</b><i>date-expr</i>,[<i>pattern</i>[,<i>localeLang</i>]]<b>)</b>
     *
     * <h4>Параметры:</h4>
     * <ul>
     *   <li><i>date-expr</i>  — строковое представление даты, которое
     *      преобразуется в <code>java.util.Date</code>. Не допускается значение
     *      <code>null</code>.
     *   </li>
     *   <li><i>pattern</i>   -  строковое выражения, представляющее собой шаблон
     *      (образец) согласно которому создано строковое значение даты.
     *   </li>
     *   <li><i>localeLang</i>  - строковый код страны, как определено конструктором
     *      класса <code>java.util.Locale</code>. Например «ru» - русский, а
     *      «en» - 	английский.
     *   </i>
     * </ul>
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;Eсли <code><i>pattern</i></code> не задан, то
     *    преобразование производится с использованием  значения равного "dd.MM.yy".
     * <p>&nbsp;&nbsp;&nbsp;&nbsp; Если не задан <code><i>localeLang</i></code>,
     *    то преобразование выполняется согласно текущим языковым установкам
     *    операционной системы компьютера. Для правильного использования функции
     *    необходимо точно знать какой язык использован для представления даты в
     *    формате строки знаков.
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;Значение <i><code>date-expr</code></i> равное
     *    <code>null</code>, преобразуется в <code>java.util.Date</code> со
     *    значением <code>Date(0)</code> и возвращается.
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;Преобразование проводится с помощью класса
     *   <code>java.text.SimpleDateFormat</code>.
     * <p>
     * 	<b>Пример</b>. Пусть строка <code><i>date-expr</i></code> содержит
     *      значение типа <code>java.util.Date</code>, соответствующее
     *      <b><i>'июл 12 2005'</b><i>.
     *	Для такой даты применим шаблон <b>'MMM, d yyyy'</b>.
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;Если выполнить выражение
     * <code>Date( date-expr, 'MMM, d yyyy')</code>, то для Windows с русскими
     * языковыми установками возвращает требуемую дату.
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;Если выполнить выражение
     * <code>Date( date-expr, 'MMM, d yyyy','ru')</code>, то для Windows с
     * русскими языковыми установками возвращает требуемую дату.
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;Если выполнить выражение
     * <code>Date(date-expr, 'MMM, d yyyy','en')</code>, то выбрасывается
     * исключение.
     */
    public static class DateExpression extends FunctionConvertExpression {

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;Создает экземпляр класса для заданного
         * контекста, оператора и операнда.
         *
         * @param context контекст выражения
         * @param operator объект типа {@link tdo.expr.FunctionOperator}
         * @param op1 если определено более одного параметра то это объект типа
         * {@link tdo.expr.CompoundExpression.CommaListExpression}.
         *      Если определен только один параметр, то это строковое
         *      представление даты типа <code>java.lang.String</code>.
         *
         */
        public DateExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            scale = 0;
            size = 6;
        }

        /**
         * @return Значение <code>Date.class</code>
         */
        @Override
        public Class getType() {
            return Date.class;
        }

        /**
         *  &nbsp; &nbsp;&nbsp;&nbsp;Вычисляет значения функции. <p>
         *  &nbsp; &nbsp;&nbsp;&nbsp;Если значение операнда <code>values</code>
         *  равно <vode>null</code>, то выбрасывается исключение.
         *
         * @param oper оператор
         * @param values объект типа {@link tdo.expr.ValueList} - список значений
         *    параметров. Может содержать два или три элемента. Если при вызове
         *     функции задан только один параметр, то он должен содержать строку
         *     знаков и <code>values</code> имеет тип
         *     <code>java.lang.String</code>.
         * @return значение даты, полученное преобразованием из строкового
         *       представления
         * @throws tdo.tools.scanner.ExpressionException, если количество
         *      параметров больше 3 или значение параметра, содержащего
         *      преобразуемую строку равно <code>null</code>.
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
     * &nbsp; &nbsp;&nbsp;&nbsp;Определяет функцию, позволяющую получит значение текущей даты,
     * установленной в системе.
     * <h4>Синтаксис:</h4>
     * <b><code>Now()</code></b>
     * <h4>Алиасное имя:  <i><code>today</code></i></h4>
     * <p>&nbsp; &nbsp;&nbsp;&nbsp;Возвращает значение текущей даты,
     * установленной в системе, типа <code>java.util.Date.</code>
     */
    public static class NowExpression extends FunctionConvertExpression {

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;Создает экземпляр класса для заданного
         * контекста, оператора и операнда.
         *
         * @param context контекст выражения
         * @param operator объект типа {@link tdo.expr.FunctionOperator}
         * @param op1 объект типа
         * {@link tdo.expr.CompoundExpression.CommaListExpression}.
         *
         */
        public NowExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
        }

        /**
         * @return Значение <code>Date.class</code>
         */
        @Override
        public Class getType() {
            return Date.class;
        }

        /**
         *  &nbsp; &nbsp;&nbsp;&nbsp;Вычисляет значения функции. <p>
         *
         * @param oper оператор
         * @param values для функции значения не имеет и может содердать
         *   любое значение
         * @return значение даты, установленное в системе
         */
        @Override
        protected Object computeFunction(IOperator oper, Object values) {
            return new java.util.Date();
        }
    }//class NowExpression

    /**
     * &nbsp;&nbsp;&nbsp;&nbsp;Определяет функцию преобразования строкового
     * значения операнда в объект даты типа <code>java.sql.Timestamp</code>
     * согласно заданному шаблону и коду языка.
     * <h4>Имя функции: <i>Date</i> или <i>Dat</i>. </h4>
     * <h4>Синтаксис:</h4>
     *
     *   <b>Timestamp(</b><i>date-expr</i>,[<i>pattern</i>[,<i>localeLang</i>]]<b>)</b>
     *
     * <h4>Параметры:</h4>
     * <ul>
     *   <li><i>date-expr</i>  — строковое представление даты, которое
     *      преобразуется в <code>java.sql.Timestamp</code>. Не допускается
     *      значение <code>null</code>.
     *   </li>
     *   <li><i>pattern</i>   -  строковое выражения, представляющее собой шаблон
     *      (образец) согласно которому создано строковое значение даты.
     *   </li>
     *   <li><i>localeLang</i>  - строковый код страны, как определено конструктором
     *      класса <code>java.util.Locale</code>. Например «ru» - русский, а
     *      «en» - 	английский.
     *   </i>
     * </ul>
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;Eсли <code><i>pattern</i></code> не задан, то
     *    преобразование производится с использованием  значения равного "dd.MM.yy".
     * <p>&nbsp;&nbsp;&nbsp;&nbsp; Если не задан <code><i>localeLang</i></code>,
     *    то преобразование выполняется согласно текущим языковым установкам
     *    операционной системы компьютера. Для правильного использования функции
     *    необходимо точно знать какой язык использован для представления даты в
     *    формате строки знаков.
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;Значение <i><code>date-expr</code></i> равное
     *    <code>null</code>, преобразуется в <code>java.sql.Timestamp</code> со
     *    значением <code>Timestamp(0)</code> и возвращается.
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;Преобразование проводится с помощью класса
     *   <code>java.text.SimpleDateFormat</code>.
     * <p>
     * 	<b>Пример</b>. Пусть строка <code><i>date-expr</i></code> содержит
     *      значение типа <code>java.sql.Timestamp</code>, соответствующее
     *      <b><i>'июл 12 2005'</b><i>.
     *	Для такой даты применим шаблон <b>'MMM, d yyyy'</b>.
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;Если выполнить выражение
     * <code>Date( date-expr, 'MMM, d yyyy')</code>, то для Windows с русскими
     * языковыми установками возвращает требуемую дату.
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;Если выполнить выражение
     * <code>Date( date-expr, 'MMM, d yyyy','ru')</code>, то для Windows с
     * русскими языковыми установками возвращает требуемую дату.
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;Если выполнить выражение
     * <code>Date(date-expr, 'MMM, d yyyy','en')</code>, то выбрасывается
     * исключение.
     */
    public static class TimestampExpression extends FunctionConvertExpression {

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;Создает экземпляр класса для заданного
         * контекста, оператора и операнда.
         *
         * @param context контекст выражения
         * @param operator объект типа {@link tdo.expr.FunctionOperator}
         * @param op1 если определено более одного параметра то это объект типа
         * {@link tdo.expr.CompoundExpression.CommaListExpression}.
         *      Если определен только один параметр, то это строковое
         *      представление даты типа <code>java.lang.String</code>.
         *
         */
        public TimestampExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            scale = 0;
            size = 6;
        }

        /**
         * @return Значение <code>java.sql.Timestamp.class</code>
         */
        @Override
        public Class getType() {
            return Timestamp.class;
        }

        /**
         *  &nbsp; &nbsp;&nbsp;&nbsp;Вычисляет значения функции. <p>
         *  &nbsp; &nbsp;&nbsp;&nbsp;Если значение операнда <code>values</code>
         *  равно <vode>null</code>, то выбрасывается исключение.
         *
         * @param oper оператор
         * @param values объект типа {@link tdo.expr.ValueList} - список значений
         *    параметров. Может содержать два или три элемента. Если при вызове
         *     функции задан только один параметр, то он должен содержать строку
         *     знаков и <code>values</code> имеет тип
         *     <code>java.lang.String</code>.
         * @return значение даты, полученное преобразованием из строкового
         *       представления
         * @throws tdo.tools.scanner.ExpressionException, если количество
         *      параметров больше 3 или значение параметра, содержащего
         *      преобразуемую строку равно <code>null</code>.
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
     * &nbsp; &nbsp;&nbsp;&nbsp;Вычисляет значение функции форматирования числового
     * операнда согласно заданному шаблону при преобразования числового
     * значения в строковое.
     *
     * <h4>Имя функции: <i>NumberFormat</i> или <i>NumFormat</i> или <i>NF</i>.</h4>
     * &nbsp; &nbsp;&nbsp;&nbsp; или
     * <h4>Имя функции: <i>RNumberFormat</i> или <i>RNumFormat</i> или <i>RNF</i>.</h4>
     * &nbsp; &nbsp;&nbsp;&nbsp;Эти две группы различаются по имени.
     * Первая группа выполняет форматирование в моде
     * <code>java.math.RoundingMode.HALF_UP</code>, а вторая в моде
     * <code>java.math.RoundingMode.HALF_EVEN</code>.
     * 
     * <h4>Синтаксис:</h4>
     * 
     *   <b>NumberFormat(</b><i>expr</i>[,<i>pattern</i>[,<i>ecsep</i>[,<i>groupsep</i>]]]<b>)</b>
     * <h4>Параметры:</h4>
     * <ul>
     *   <li><i>expr</i> - преобразуемое числовое выражение.
     *              <code>null</code> значение рассматривается как Intrger(0).
     *   </li>
     *   <li><i>pattern</i> - шаблон форматирования. Если не задан или равен
     *     <code>null</code>, то к <code><i>expr</i></code> просто применяется
     *     метод <code>toString()</code>.
     *  </li>
     *   <li><i>ecsep</i> - символ-разделитель целой и дробной части, который
     *        выводится в результат преобразования. Если не задан, то по
     *        умолчанию, берется точка.
     *   </li>
     *   <li><i>groupsep</i> - символ-разделитель групп цифр, который
     *        выводится в результат преобразования. Обычно, пробел или запятая.
     *         Если не задан, то по умолчанию, берется пробел.
     *   </li>
     * </ul>
     * <p>&nbsp; &nbsp;&nbsp;&nbsp;При преобразовании используется java класс
     * <code>java.text.DecimalFormat</code>.
     * <p>&nbsp; &nbsp;&nbsp;&nbsp;Если не задан параметр
     * <i>code>pattern</code></i>, то преобразования не производится, значение
     * равное <code>null</code> преобразуется в "0", иначе к значению применяется
     * метод <code>toString()</code>.
     * <<h4>Пример.</h4>
     * &nbsp; &nbsp;&nbsp;&nbsp;Есть число, значение которого '12345678.126'.
     * Применение функции:
     * <pre>
     *      NumberFormat(12345678.125,'########.###')
     * </pre>
     * дает результат: <b>12345678.125</b>
     * <p>Изменим теперь символ-разделитель целой и дробной частей. Пусть это
     * будет символ "точка с запятолй":
     * <pre>
     *      NumberFormat(12345678.125,'########.###', ';' )
     * </pre>
     * дает результат: <b>12345678;125</b>
     * <p>Для лучшей читабельности, введем символ-разделитель групп цифр,
     *  например, пробел:
     * <pre>
     *      NumberFormat(12345678.125,'#####,###.###', ';',' ' )
     * </pre>
     * дает результат: <b>12 345 678;125<b>
     *
     * <h4>Округление</code></h4>
     *
     *  &nbsp; &nbsp;&nbsp;&nbsp;Функции с именами NumberFormat,NumFormat,NF
     * проводят округление значения по правилам арифметики. Например, если для
     * числа 12345678.125 применить шаблон '########.##', т.е. указав два знака
     * после десятичной точки и выполнить функцию <code>NumberFormat</code>, то
     * получается результат: <b>12345678.13</b>, поскольку функция выполняет
     * форматирование в моде округления равной
     * <code>java.math.RoundingMode.HALF_UP</code>. Если же
     * выполнить функцию <code>RNumberFormat</code>, то получается результат:
     * <b>12345678.12</b>. Эта функция выполняется в моде
     * <code>java.math.RoundingMode.HALF_EVEN</code>.
     *
     *
     * <p>Для более подробной информации cм. <code>java.text.NumberFormat</code>.
     *
     */
    public static class NumberFormatExpression extends FunctionConvertExpression {

        protected String fname;
        private java.text.DecimalFormat df;

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;Создает экземпляр класса для заданного
         * контекста, оператора и операнда.
         *
         * @param fname имя функции. Принимает значение "NF" или "RNF".
         * @param context контекст выражения
         * @param operator объект типа {@link tdo.expr.FunctionOperator}
         * @param op1 если определено более одного параметра то это объект типа
         * {@link tdo.expr.CompoundExpression.CommaListExpression}.
         *      Если определен только один параметр, то это исходное числовое
         *      выражение.
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
         * @return Значение <code>java.lang.String.class</code>
         */
        @Override
        public Class getType() {
            return String.class;
        }

        /**
         *  &nbsp; &nbsp;&nbsp;&nbsp;Вычисляет значения функции. <p>
         *
         * @param oper оператор
         * @param values объект типа {@link tdo.expr.ValueList} - список значений
         *    параметров. Может содержать от одного до четырех элементов. Если
         *    при вызове функции задан только один параметр, то он должен
         *    содержать числовое значение. Если значение первого параметра вызова
         *    функции равно <code>null</code>, то считается, что оно равно
         *    <code>java.lang.Integer(0)</code>.
         * @return строка знаков как результат форматирования числового значение.
         * @throws tdo.tools.scanner.ExpressionException, если количество
         *      параметров больше 4
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
     * &nbsp; &nbsp;&nbsp; &nbsp;Определяет функцию преобразования значения
     * операнда, содержащего дату согласно заданному шаблону и коду языка  в
     * строковое значение.
     * <h4>Имя функции: <i>DateFormat</i> или <i>DF</i>. <h4>
     * <h4>Синтаксис:</h4>
     *
     *   <b>DateFormat(</b><i>date-expr</i>,[<i>pattern</i>[,<i>localeLang</i>]]<b>)</b>
     *
     * <h4>Параметры:</h4>
     * <ul>
     *   <li><i>date-expr</i>  — выражение типа <code>java.util.Date</code>, которое
     *      преобразуется в <code>java.lang.String</code>.
     *   </li>
     *   <li><i>pattern</i>   -  строковое выражения, представляющее собой шаблон
     *      (образец) согласно которому дата должна быть представлена как
     *      строка.
     *   </li>
     *   <li><i>localeLang</i>  - строковый код страны, как определено
     *      конструктором класса <code>java.util.Locale</code>. Например
     *      «ru» - русский, а «en» - английский.
     *   </i>
     * </ul>
     * <p>Eсли <code><i>pattern</i></code> не задан, то преобразование
     *    производится с использованием  значения равного "dd.MM.yy".
     * <p>Если не задан <code><i>localeLang</i></code>, то преобразование
     *    выполняется согласно текущим языковым установкам операционной системы
     *    компьютера. Для правильного использования функции необходимо точно
     *    знать какой язык использован для представления даты в формате строки
     *    знаков.
     * <p>Значение <i><code>date-expr</code></i> равное <code>null</code>,
     *    преобразуется в <code>java.util.Date</code> со значением
     *    <code>Date(0)</code> перед преобразованием в строку знаков.
     * <p>Преобразование проводится с помощью класса
     *   <code>java.text.SimpleDateFormat</code>.
     * <p>
     * 	<b>Пример</b>. Пусть строка <code><i>date-expr</i></code> содержит
     *      значение типа <code>java.util.Date</code>, соответствующее
     *      <b><i>12 июля 2005</b><i>.
     *	Для такой даты применим шаблон <b>'MMM, d yyyy'</b>.
     * <p>Если выполнить выражение Code>DateFormat( date-expr, 'MMM, d yyyy')</code>,
     * то для Windows с русскими языковыми установками возвращается
     * <b>«Июл, 12 2005»</b>.
     *<p>Если выполнить выражение <code>DateFormat(date-expr, 'MMM, d yyyy','en')</code>,
     * то возвращактся строка <b>«Jul, 12 2005»</b>, поскольку третьим
     * параметром мы явно проинформировали функцию, на каком языке представлена
     * дата — английском.
     */
    public static class DateFormatExpression extends FunctionConvertExpression {

        private java.text.SimpleDateFormat df;

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;Создает экземпляр класса для заданного
         * контекста, оператора и операнда.
         *
         * @param context контекст выражения
         * @param operator объект типа {@link tdo.expr.FunctionOperator}
         * @param op1 если определено более одного параметра то это объект типа
         * {@link tdo.expr.CompoundExpression.CommaListExpression}.
         *      Если определен только один параметр, то это исходное
         *      выражение для даты.
         */
        public DateFormatExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            df = new java.text.SimpleDateFormat("dd.MM.yy");
        }

        /**
         * @return Значение <code>java.lang.String.class</code>
         */
        @Override
        public Class getType() {
            return String.class;
        }

        /**
         *  &nbsp; &nbsp;&nbsp;&nbsp;Вычисляет значения функции. <p>
         *
         * @param oper оператор
         * @param values объект типа {@link tdo.expr.ValueList} - список значений
         *    параметров. Может содержать два или три элемента. Если при вызове
         *     функции задан только один параметр, то он и , соответственно,
         *     <code>values</code> должен содержать выражение типа
         *     <code>java.util.Date</code>.
         * @return строка знаков, полученная преобразованием из даты
         * @throws tdo.tools.scanner.ExpressionException, если количество
         *      параметров больше 3
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
     * &nbsp; &nbsp;&nbsp;&nbsp; Обеспечивает вычисление функций из пакета
     * <code>java.math</code>.
     * &nbsp; &nbsp;&nbsp;&nbsp;Для каждого метода из пакета
     * <code>java.Math</code> определена одноименная функция выражений.
     * &nbsp; &nbsp;&nbsp;&nbsp;В таблице приведен список соответствия
     * функций методам пакета <code>java.math</code>.
     * <table border="1">
     * <thead>
     * <tr>
     *      <th>Имя</th>
     *      <th>Описание</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td>ABS</td>
     * <td>Соответствует  функции из пакета <code>java.Math.abs(Double)</code></td>
     * </tr>
     * <tr>
     * <td>ACOS</td>
     * <td>Соответствует  функции из пакета <code>java.Math.acos(Double)</code></td>
     * </tr>
     * <tr>
     * <td>ASIN</td>
     * <td>Соответствует  функции из пакета <code>java.Math.asin(Double)</code></td>
     * </tr>
     * <tr>
     * <td>ATAN</td>
     * <td>Соответствует  функции из пакета <code>java.Math.atan(Double)</code></td>
     * </tr>
     * <tr>
     * <td>CEIL</td>
     * <td>Соответствует  функции из пакета <code>java.Math.ceil(Double)</code></td>
     * </tr>
     * <tr>
     * <td>COS</td>
     * <td>Соответствует  функции из пакета <code>java.Math.cos(Double)</code></td>
     * </tr>
     * <tr>
     * <td>SIN</td>
     * <td>Соответствует  функции из пакета <code>java.Math.sin(Double)</code></td>
     * </tr>
     * <tr>
     * <td>TAN</td>
     * <td>Соответствует  функции из пакета <code>java.Math.tan(Double)</code></td>
     * </tr>
     * <tr>
     * <td>EXP</td>
     * <td>Соответствует  функции из пакета <code>java.Math.exp(Double)</code></td>
     * </tr>
     * <tr>
     * <td>FLOOR</td>
     * <td>Соответствует  функции из пакета <code>java.Math.floor(Double)</code></td>
     * </tr>
     * <tr>
     * <td>LOG</td>
     * <td>Соответствует  функции из пакета <code>java.Math.log(Double)</code></td>
     * </tr>
     * <tr>
     * <td>RINT</td>
     * <td>Соответствует  функции из пакета <code>java.Math.rint(Double)</code></td>
     * </tr>
     * <tr>
     * <td>ROUND</td>
     * <td>Соответствует  функции из пакета <code>java.Math.round(Double)</code></td>
     * </tr>
     * <tr>
     * <td>SQRT</td>
     * <td>Соответствует  функции из пакета <code>java.Math.sqrt(Double)</code></td>
     * </tr>
     * <tr>
     * <td>TODEGREES</td>
     * <td>Соответствует  функции из пакета <code>java.Math.toDegrees(Double)</code></td>
     * </tr>
     * <tr>
     * <td>TORADIANS</td>
     * <td>Соответствует  функции из пакета <code>java.Math.toRadians(Double)</code></td>
     * </tr>
     * <tr>
     * <td>ATAN2</td>
     * <td>Соответствует  функции из пакета <code>java.Math.atan2(Double,Double)</code></td>
     * </tr>
     * <tr>
     * <td>POW</td>
     * <td>Соответствует  функции из пакета <code>java.Math.pow(Double,Double)</code></td>
     * </tr>
     * <tr>
     * <td>RANDOM</td>
     * <td>Соответствует  функции из пакета <code>java.Math.random()</code></td>
     * </tr>
     * <tr>
     * <td>IEEEREMAINDER</td>
     * <td>Соответствует  функции из пакета <code>java.Math.IEEEremainder(Double,Double)</code></td>
     * </tr>
     * </tbody>
     * </table>
     */
    public static class JavaMathExpression extends FunctionExpression {

        /**
         * Имя функции, назначенное конструктором.
         */
        protected String fname;

        /**
         * &nbsp; &nbsp;&nbsp;&nbsp;Создает экземпляр класса для заданного
         * контекста, оператора и операнда.
         *
         * @param context контекст выражения
         * @param operator объект типа {@link tdo.expr.FunctionOperator}
         * @param op1 если определено более одного параметра то это объект типа
         * {@link tdo.expr.CompoundExpression.CommaListExpression}. Значения
         *   и типы элементов зависят от конкретной функции
         */
        public JavaMathExpression(String fname, ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            this.fname = fname;
        }

        /**
         * Вычисляет значение функции. <p>
         *
         * @param oper
         * @param values значение или список значений типа
         * {@link tdo.expr.ValueList} в зависимости от конкретной функции
         * @return значение функции.
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
         * @return Возвращает значение <code>java.lang.Double.class</code>
         */
        @Override
        public Class getType() {
            return Double.class;
        }
    } //class JavaMathExpression
    } //class FunctionExpression
