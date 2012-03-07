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
 * Базовый класс для классов моделирующих операции сравнения.<p>
 *
 * К операциям сравнения относятся:
 * <ul>
 *   <li>
 *      стандартные операции сравнения, такие как <i>равно, не равно, больше,
 *      больше или равно, меньше,
 *      меньше или равно; </i>
 *   </li>
 *   <li>
 *     <b>in </b> операция, устанавливающая содержит ли список, заданный вторым
 *        операндом, значение, заданное первым операндом;
 *   </li>
 *   <li>
 *     <b>not in </b> операция, обратная к in;
 *   </li>
 *
 *   <li>
 *      <b>between</b> операция, устанавливающая, находится ли значение внутри
 *      заданного интервала;
 *   </li>
 *   <li>
 *      <b>containing</b> операция, устанавливающая, является ли заданная строка
 *          подстрока другой строки.
 *   </li>
 *   <li>
 *      <b>not containing</b> операция - обратная операция к containing;
 *   </li>
 *   <li>
 *      <b>starting with</b> операция, устанавливающая, начинается ли заданная
 *          строка с заданной последовательности символов;
 *   </li>
 *   <li>
 *      <b>not starting with</b> операция, обратная к starting with;
 *   </li>
 *   <li>
 *      <b>is null</b> операция, проверяющая значение на равенство <code>null</code>;
 *   </li>
 *   <li>
 *      <b>not is null</b> операция, проверяющая значение на не равенство
 *          <code>null</code>;
 *   </li>
 *   <li>
 *      <b>like</b> операция, проверяющая значение на соответствие шаблону;
 *   </li>
 *   <li>
 *      <b>not like</b> операция, проверяющая значение на несоответствие шаблону;
 *   </li>
 *   <li>
 *      <b>regex</b>операция, проверяющая значение на соответствие регулярному
 *              выражению;
 *   </li>
 *   <li>
 *      <b>not regex</b> операция, проверяющая значение на несоответствие
 *          регулярному выражению;
 *   </li>
 * </ul>
 *
 * <p>Экземпляр класса оперирует двумя операндами, для которых определен
 * оператор. Операнды являются объектами типа {@link tdo.expr.IOperand}.
 * Оператор является объектом типа {@link tdo.expr.IOperator}. Для вычисления
 * выражения сравнения конкретный тип оператора может быть одним из:
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
 * <p>Каждому из пречисленных операторов соответствует класс выражения,
 * наследующий  <code>CompareExpression</code>:
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
     * Создает экземпляр класса для заданных контекта выражения,объекта
     * оператора и объектов операндов.
     * @param context контекст выражения
     * @param operator объект оператора
     * @param op1 первый операнд
     * @param op2 второй операнд
     */
    public CompareExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
        this.setContext(context);
        this.operator = operator;
        this.op1 = op1;
        this.op2 = op2;
    }

    /**
     * Возвращает значение выражения для заданной коллекции
     * именованных элементов.<p>
     * Делегирует выполненние методу <code>compare</code>, передавая
     * последнему в качестве параметра именованную коллекцию, например,
     * <code>tdo.DataRow</code>.
     *
     * @param values коллекции именованных элементов, например,
     * объект типа <code>tdo.DataRow</code>.
     * @return значение выражения
     * @see #compare(tdo.NamedValues)
     * @see #getValue(tdo.NamedValues[])
     */
    @Override
    public Object getValue(NamedValues values) {
        return this.compare(values);
    }

    /**
     * Возвращает значение выражения для заданного массива коллекций
     * именованных элементов.<p>
     * Делегирует выполненние методу <code>compare</code>, передавая
     * последнему в качестве параметра массив именованных коллекций, например,
     * <code>tdo.DataRow</code>.
     *
     * @param values коллекции именованных элементов, например,
     * объект типа <code>tdo.DataRow</code>.
     * @return значение выражения
     * @see #compare(tdo.NamedValues[])
     * @see #getValue(tdo.NamedValues)
     */
    @Override
    public Object getValue(NamedValues[] values) {
        return this.compare(values);
    }

    /**
     * Возвращает значение <code>Boolean.class</code>.
     * @return тип результата выражение, который всегда равен
     * <code>Boolean.class</code>.
     */
    @Override
    public Class getType() {
        return Boolean.class;
    }

    /**
     * Выполняет сравнение операндов по умолчанию.
     * @param values именованная коллекция элементов.<p>
     * @return true , если выполнено одно из условий:
     * <ul>
     *   <li>Оба операнда равны <code>null</code>;</li>
     *   <li>Оба операнда не равны <code>null</code> и
     *       метод <code>op1.equals(op2)</code> дает <code>true</code>.
     *   </li>
     * </ul>
     * В противном случае возвращается <code>false</code>
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
     * Выполняет сравнение операндов по умолчанию.
     * @param values массив именованных коллекций элементов.<p>
     * @return true , если выполнено одно из условий:
     * <ul>
     *   <li>Оба операнда равны <code>null</code>;</li>
     *   <li>Оба операнда не равны <code>null</code> и
     *       метод <code>op1.equals(op2)</code> дает <code>true</code>.
     *   </li>
     * </ul>
     * В противном случае возвращается <code>false</code>
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
     * Возвращает целочисленный результат сравнения двух объектов.
     * <p>Сравниваемые объекты могут принадлежать разным типам, поэтому перед
     * сравнением производится преобразование типов и пара преобразованных
     * значений, в зависимости от операндав, передается одному из методов:
     * <ul>
     *   <li>{@link #compareBigDecimal(java.math.BigDecimal, java.math.BigDecimal)}</li>
     *   <li>{@link #compareDouble(double, double) }</li>
     *   <li>{@link #compareLong(long, long)}</li>
     * </ul>
     *
     * <p>Сравнение выполняется по следующему алгоритму:
     *  <p>Если оба операнда равны <code>null</code>, то возвращается 0;
     *  <p>Если первый операнд равен <code>null</code> то возвращается -1;
     *  <p>Если второй операнд равен <code>null</code> то возвращается 1;
     *  <p>Если первый операнд равен второму в смысле java-операции сравнения
     *      <code>==</code> то возвращается 0;
     *  <p>Если один из операндов имеет тип <code>java.math.BigDecimal</code>, то
     *     другой операнд преобразуется, если необходимо, в тип
     *     <code>java.math.BigDecimal</code>, выполняется метод
     *     {@link #compareBigDecimal(java.math.BigDecimal, java.math.BigDecimal) }
     *     и возвращается результат его выполнения.
     *  <p>Если один из операндов имеет тип <code>java.lang.Double</code>, то
     *     другой операнд преобразуется, если необходимо, в тип
     *     <code>java.lang.Double</code>, выполняется метод
     *     {@link #compareDouble(java.lang.Double, java.lang.Double) }
     *     и возвращается результат его выполнения.
     *  <p>Если один из операндов имеет тип <code>java.lang.Float</code>, то
     *     оба операнда преобразуются,в тип <code>java.lang.Double</code>,
     *     выполняется метод
     *     {@link #compareDouble(java.lang.Double, java.lang.Double) }
     *     и возвращается результат его выполнения.
     *  <p>Если тип одного из операндов <code>java.lang.Byte,
     *     java.lang.Short, java.lang.Integer</code>, то
     *     оба операнда преобразуются, если необходимо, в тип
     *     <code>java.lang.Long</code>, выполняется метод
     *     {@link #compareLong(java.lang.Long, java.lang.Long) }
     *     и возвращается результат его выполнения.
     *  <p>Если оба операнда имеют тип <code>java.util.Date</code>, то
     *     возвращается результат выполнения метода <code>Date.compareTo(Date).
     *  <p>Если оба операнда имеют тип <code>java.sql.Timestamp</code>, то
     *     возвращается результат выполнения метода <code>Timestamp.compareTo(Timestamp).
     *  <p>Если первый операнд имеют тип <code>java.sql.Timestamp</code>
     *     а второй операнд <code>java.util.Date</code>, то
     *     выполняется метод
     *     то возвращается результат выполнения метода
     *     <code>((Timestamp)firstValue).compareTo((Date)secondValue)</code>.
     *
     *  <p>Если первый операнд имеют тип <code>java.util.Date</code>
     *     а второй операнд <code>java.sql.Timestamp</code>, то
     *     выполняется метод
     *     <code>((Timestamp)secondValue).compareTo((Date)firstValue)</code> и,
     *     если значение результата равно 0, то возвращается 0. Иначе
     *     возвращается результат с противоположным знаком.
     *  <p>Если один из операндов имеет тип <code>java.util.Date</code>
     *     а второй операнд <code>java.lang.String</code>, то строковый операнд
     *     преобразуется в тип <code>java.util.Date</code>
     *     то возвращается результат выполнения метода
     *     <code>Date.compareTo(Date)</code>.
     *  <p>Если один из операндов имеет тип <code>java.sql.Timestamp</code>
     *     а второй операнд <code>java.lang.String</code>, то строковый операнд
     *     преобразуется в тип <code>java.sql.Timestamp</code>
     *     то возвращается результат выполнения метода
     *     <code>java.sql.Timestamp.compareTo(java.sql.Timestamp)</code>.
     *  <p>Если оба операнда имеют тип <code>java.lang.String</code>
     *     то возвращается результат выполнения метода
     *     <code>String.compareTo(String)</code>.
     *  <p>Если первый операнд имеет тип <code>java.lang.String</code>
     *     то второй преобразуется в строку методом <code>toString()</code>
     *      и возвращается результат выполнения метода
     *     <code>String.compareTo(String)</code>.
     *  <p>Если второй операнд имеет тип <code>java.lang.String</code>
     *     то первый преобразуется в строку методом <code>toString()</code>
     *      и возвращается результат выполнения метода
     *     <code>String.compareTo(String)</code>.
     *  <p> Если выполнено условие
     *     <code>  firstValue.getClass().equals(secondValue.getClass()) )\</code>
     *     и если  firstValue.equals(secondValue), то возвращается 0.
     *  <p> В противном случае, т.е. ни одно из условий выше не выполнено,                   return 0;
     *      возвращается -1.
     * @param firstValue значение первого операнда
     * @param secondValue значение второго операнда
     * @return результат сравнения: 0 - операнды равны, 1 - первый операнд
     *   больше второго, -1 - первый операнд меньше второго
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
     * Возвращает результат сравнения десятичных операндов.
     * @param op1 первый операнд
     * @param op2 второй операнд
     * @return результат сравнения: 0 - операнды равны, 1 - первый операнд
     *   больше второго, -1 - первый операнд меньше второго
     */
    protected int compareBigDecimal(BigDecimal op1, BigDecimal op2) {
        return op1.compareTo(op2);
    }

    /**
     * Возвращает результат сравнения операндов с плавающей точкой.
     * @param op1 первый операнд
     * @param op2 второй операнд
     * @return результат сравнения: 0 - операнды равны, 1 - первый операнд
     *   больше второго, -1 - первый операнд меньше второго
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
     * Возвращает результат сравнения длинных целых операндов.
     * @param op1 первый операнд
     * @param op2 второй операнд
     * @return результат сравнения: 0 - операнды равны, 1 - первый операнд
     *   больше второго, -1 - первый операнд меньше второго
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
     * Обеспечивает операцию сравнения типа "равно".
     */
    public static class EqualsExpression extends CompareExpression {

        /**
         * Создает экземпляр класса для заданных контекста, оператора и
         * операндов.
         * @param context контекст выражения
         * @param operator оператор типа {@link tdo.expr.AbstractOperator.EqualsOperator}
         * @param op1 первый операнд
         * @param op2 второй операнд
         */
        public EqualsExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
            setLexType();
        }

        /**
         * Устанавливает значение свойства <code>lexType</code>
         * равным значению {@link tdo.tools.expr.LexConst#EQ}.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.EQ;
        }

        /**
         * Выполняет сравнение операндов для заданной коллекции именованных
         * элементов.<p>
         * Вычисляет значения первого и второго операндов и выполняет
         * защищенный метод {@link #eval(java.lang.Object, java.lang.Object),
         * передавая ему в качестве параметров вычисленные значения.
         * @param values именованная коллекция элементов.
         * @return результат сравнения.
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
         * Выполняет сравнение операндов для заданного массива коллекций
         * именованных элементов.<p>
         * Вычисляет значения первого и второго операндов и выполняет
         * защищенный метод {@link #eval(java.lang.Object, java.lang.Object),
         * передавая ему в качестве параметров вычисленные значения..
         * @param values массив именованных коллекций элементов.
         * @return результат сравнения.
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
         * Сравнивает два операнда на равенство.
         * Делегирует исполнение методу
         * {@link tdo.expr.CompareExpression#compareStd(java.lang.Object, java.lang.Object) }
         * @param v1 первый операнд
         * @param v2 второй операнд
         * @return true если операнды равны. false - в противном случае.
         */
        protected boolean eval(Object v1, Object v2) {
            return compareStd(v1, v2) == 0 ? true : false;
        }
    }//class EqualsExpression
    /**
     * Обеспечивает операцию проверки значения операнда на <code>null</code>.
     */
    public static class IsNullExpression extends CompareExpression {
        /**
         * Создает экземпляр класса для заданных контекста, оператора и
         * операндов.
         * @param context контекст выражения
         * @param operator оператор типа {@link tdo.expr.AbstractOperator.IsNullOperator}
         * @param op1 первый операнд
         * @param op2 второй операнд
         */
        public IsNullExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
            setLexType();
        }
        /**
         * Устанавливает значение свойства <code>lexType</code>
         * равным значению {@link tdo.tools.expr.LexConst#ISNULL}.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.ISNULL;
        }
        /**
         * Проверяет значение первого операнда на <code>null</code> для
         * заданной коллекции именованных элементов.<p>
         * Вычисляет значения первого операнда и выполняет
         * защищенный метод {@link #eval(java.lang.Object, java.lang.Object),
         * передавая ему в качестве первого параметра вычисленные значение, а
         * в качестве второго значение <code>null</code>.
         * @param values именованная коллекция элементов.
         * @return результат сравнения. <code>true</code> - если первый
         *   операнд равен <code>null</code>. Иначе <code>false</code>.
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
         * Проверяет значение первого операнда на <code>null</code> для
         * заданного массива коллекций именованных элементов.<p>
         * Вычисляет значения первого операнда и выполняет
         * защищенный метод {@link #eval(java.lang.Object, java.lang.Object),
         * передавая ему в качестве первого параметра вычисленные значение, а
         * в качестве второго значение <code>null</code>.
         * @param values именованная коллекция элементов.
         * @return результат сравнения. <code>true</code> - если первый
         *   операнд равен <code>null</code>. Иначе <code>false</code>.
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
         * Проверяет первый операнд на значение <code>null</code>.
         * @param v1 проверяемый операнд
         * @param v2 второй операнд, равный <code>null</code>
         * @return true если первый операнд равнен и<code>null</code>.
         *      false - в противном случае.
         */
        protected boolean eval(Object v1, Object v2) {
            return v1 == null ? true : false;
        }
    }//class IsNullExpression
    /**
     * Обеспечивает операцию проверки значения операнда на неравенство значению
     * <code>null</code>.
     */
    public static class IsNotNullExpression extends IsNullExpression {
   /**
         * Создает экземпляр класса для заданных контекста, оператора и
         * операндов.
         * @param context контекст выражения
         * @param operator оператор типа {@link tdo.expr.AbstractOperator.IsNotNullOperator}
         * @param op1 первый операнд
         * @param op2 второй операнд
         */
        public IsNotNullExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
            setLexType();
        }
        /**
         * Устанавливает значение свойства <code>lexType</code>
         * равным значению {@link tdo.tools.expr.LexConst#ISNOTNULL}.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.ISNOTNULL;
        }
        /**
         * Проверяет первый операнд на значение <code>null</code>.
         * @param v1 проверяемый операнд
         * @param v2 второй операнд, равный <code>null</code>
         * @return true если первый операнд НЕ равнен <code>null</code>.
         *      false - в противном случае.
         */
        @Override
        protected boolean eval(Object v1, Object v2) {
            return v1 == null ? false : true;
        }
    }//class IsNotNullExpression
    /**
     * Обеспечивает операцию сравнения типа "больше".
     */
    public static class GreaterExpression extends EqualsExpression {
        /**
         * Создает экземпляр класса для заданных контекста, оператора и
         * операндов.
         * @param context контекст выражения
         * @param operator оператор типа {@link tdo.expr.AbstractOperator.GreaterOperator}
         * @param op1 первый операнд
         * @param op2 второй операнд
         */
        public GreaterExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * Устанавливает значение свойства <code>lexType</code>
         * равным значению {@link tdo.tools.expr.LexConst#GT}.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.GT;
        }
        /**
         * Проверяет, является ли значение первого операнда большим, чем значение
         * второго.
         * Делегирует исполнение методу
         * {@link tdo.expr.CompareExpression#compareStd(java.lang.Object, java.lang.Object) }
         * @param v1 первый операнд
         * @param v2 второй операнд
         * @return true если первый операнд больше второго. false - в противном случае.
         */
        @Override
        protected boolean eval(Object v1, Object v2) {
            return compareStd(v1, v2) > 0 ? true : false;
        }
    }//class GreaterExpression
    /**
     * Обеспечивает операцию сравнения типа "меньше".
     */
    public static class LessExpression extends EqualsExpression {
        /**
         * Создает экземпляр класса для заданных контекста, оператора и
         * операндов.
         * @param context контекст выражения
         * @param operator оператор типа {@link tdo.expr.AbstractOperator.LessOperator}
         * @param op1 первый операнд
         * @param op2 второй операнд
         */
        public LessExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * Устанавливает значение свойства <code>lexType</code>
         * равным значению {@link tdo.tools.expr.LexConst#LT}.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.LT;
        }
        /**
         * Проверяет, является ли значение первого операнда меньшим, чем значение
         * второго.
         * Делегирует исполнение методу
         * {@link tdo.expr.CompareExpression#compareStd(java.lang.Object, java.lang.Object) }
         * @param v1 первый операнд
         * @param v2 второй операнд
         * @return true если первый операнд меньше второго. false - в противном случае.
         */
        @Override
        protected boolean eval(Object v1, Object v2) {
            return compareStd(v1, v2) < 0 ? true : false;
        }
    }//class LessExpression
    /**
     * Обеспечивает операцию сравнения типа "больше или равно".
     */
    public static class GreaterEqualsExpression extends EqualsExpression {
        /**
         * Создает экземпляр класса для заданных контекста, оператора и
         * операндов.
         * @param context контекст выражения
         * @param operator оператор типа {@link tdo.expr.AbstractOperator.GreaterEqualsOperator}
         * @param op1 первый операнд
         * @param op2 второй операнд
         */
        public GreaterEqualsExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * Устанавливает значение свойства <code>lexType</code>
         * равным значению {@link tdo.tools.expr.LexConst#GTEQ}.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.GTEQ;
        }
        /**
         * Проверяет, является ли значение первого операнда большим или равным,
         * чем значение второго.
         * Делегирует исполнение методу
         * {@link tdo.expr.CompareExpression#compareStd(java.lang.Object, java.lang.Object) }
         * @param v1 первый операнд
         * @param v2 второй операнд
         * @return true если первый операнд больше или равен второго. false - в противном случае.
         */
        @Override
        protected boolean eval(Object v1, Object v2) {
            int r = compareStd(v1, v2);
            return r >= 0 ? true : false;
        }
    }//class GreaterEqualeExpression
    /**
     * Обеспечивает операцию сравнения типа "меньше или равно".
     */
    public static class LessEqualsExpression extends EqualsExpression {
        /**
         * Создает экземпляр класса для заданных контекста, оператора и
         * операндов.
         * @param context контекст выражения
         * @param operator оператор типа {@link tdo.expr.AbstractOperator.LessEqualsOperator}
         * @param op1 первый операнд
         * @param op2 второй операнд
         */
        public LessEqualsExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * Устанавливает значение свойства <code>lexType</code>
         * равным значению {@link tdo.tools.expr.LexConst#LTEQ}.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.LTEQ;
        }
        /**
         * Проверяет, является ли значение первого операнда меньшим или равным,
         * чем значение второго.
         * Делегирует исполнение методу
         * {@link tdo.expr.CompareExpression#compareStd(java.lang.Object, java.lang.Object) }
         * @param v1 первый операнд
         * @param v2 второй операнд
         * @return true если первый операнд меньше или равен второго. false - в противном случае.
         */
        @Override
        protected boolean eval(Object v1, Object v2) {
            int r = compareStd(v1, v2);
            return r <= 0 ? true : false;
        }
    }//class LessEqualsExpression
    /**
     * Обеспечивает операцию сравнения типа "не равно".
     */
    public static class NotEqualsExpression extends EqualsExpression {
        /**
         * Создает экземпляр класса для заданных контекста, оператора и
         * операндов.
         * @param context контекст выражения
         * @param operator оператор типа {@link tdo.expr.AbstractOperator.NotEqualsOperator}
         * @param op1 первый операнд
         * @param op2 второй операнд
         */
        public NotEqualsExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * Устанавливает значение свойства <code>lexType</code>
         * равным значению {@link tdo.tools.expr.LexConst#NOTEQ}.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.NOTEQ;
        }
        /**
         * Проверяет на неравенство первого операнда второму.
         * Делегирует исполнение методу
         * {@link tdo.expr.CompareExpression#compareStd(java.lang.Object, java.lang.Object) }
         * @param v1 первый операнд
         * @param v2 второй операнд
         * @return true если первый операнд не равен второму. false - в противном случае.
         */
        @Override
        protected boolean eval(Object v1, Object v2) {
            int r = compareStd(v1, v2);
            return r != 0 ? true : false;
        }
    }//class NotEqualsExpression
    /**
     * Обеспечивает выполнение "between" оператора.<p>
     * Исходное выражение может содержать логический оператор: <br>
     * <b><i>op1 between op2 and op3</i></b>
     * <p>При компиляции выражения этот оператор представлен как два оператора:
     * <ol>
     *   <li>{@link tdo.expr.AbstractOperator.BetweenOperator} . Работает с двумя
     *       операндами:
     *       <ol>
     *          <li>op1</li>
     *          <li>op[] - массив, 0-м элементов которого является op2, а 1-м
     *              op3
     *          </li>
     *       </ol>
     *   </li>
     *   <li>
     *      {@link tdo.expr.CompoundExpression.AndBetweenExpression} , который
     *      строит массив опрерандов op2 и op3.
     *   </li>
     * </ol>
     * Класс <code>BetweenExpression</code> предполагает, что операция
     * выполняется над двумя операндами.<p>
     * В выражении between участвуют три операнда. В процессе вычисления могут
     * производится преобразования типов. Первый операнд сравнивается со вторым
     * по правилам "&gt.=" - больше или равно, а также с третьим по правилам "&lt.="
     * меньше или равно. Преобразование типов, при этом, проводится как
     * определено методом
     * {@link tdo.expr.CompareExpression#compareStd(java.lang.Object, java.lang.Object) } .
     */
    public static class BetweenExpression extends EqualsExpression {
        /**
         * Создает экземпляр класса для заданного контекста, оператора и
         * операндов. <p>
         *
         * @param context контекст выражения
         * @param operator объект типа
         *      {@link tdo.expr.AbstractOperator.BetweenOperator}
         * @param op1 первый операнд. Его значение проверяется на вхождение
         *   в диапазон значений, определяемый вторым операндом
         * @param op2 второй операнд. Его значение должно быть массивом из
         *    двух элементов: 0-й соответствует  <i>левой границе</i> диапазона
         *    значений и 1-й элемент соответствует  <i>правой границе</i>
         *    диапазона.
         */
        public BetweenExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * Устанавливает значение свойства <code>lexType</code>
         * равным значению {@link tdo.tools.expr.LexConst#BETWEEN }.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.BETWEEN;
        }
        /**
         * Выполняет проверку значения первого операнда выражения на вхождение
         * в диапазон значений, определенных вторым операндом для заданной
         * коллекции именованных элементов.<p>
         * Второй операнд должен иметь значение равное массиву из двух элементов:
         * <i>левая и правая</i> границы диапазона.
         *
         * @param values именованная коллекция элементов, например,
         * <code>tdo.DataRow</code>
         * @return <code>true</code> , если значение первого операнда больше или равно
         *  значению левой границы диапазона и меньше или равно значению правой
         *  границы диапазона. <code>false</code> - в противном случае.
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
         * Выполняет проверку значения первого операнда выражения на вхождение
         * в диапазон значений, определенных вторым операндом для заданного
         * массива коллекций именованных элементов.<p>
         * Второй операнд должен иметь значение равное массиву из двух элементов:
         * <i>левая и правая</i> границы диапазона.
         *
         * @param values массив именованных коллекций элементов, например,
         * <code>tdo.DataRow</code>
         * @return <code>true</code> , если значение первого операнда больше или равно
         *  значению левой границы диапазона и меньше или равно значению правой
         *  границы диапазона. <code>false</code> - в противном случае.
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
         * Сравнивает значения двух объектов и возвращает результат сравнения. <p>
         * Делегирует исполнение операции сравнения методу
         * {@link tdo.expr.CompareExpression#compareStd(java.lang.Object, java.lang.Object) } .
         *
         * @param v1 первый сравниваемый операнд
         * @param v2 второй сравниваемый операнд
         * @return <code>true</code> когда значение первого операнда
         *   больше или равно значению второго операндаю <code>false</code> - в
         *   противном случае
         */
        @Override
        protected boolean eval(Object v1, Object v2) {
            int r = compareStd(v1, v2);
            return r >= 0 ? true : false;
        }
        /**
         * Сравнивает значения двух объектов и возвращает результат сравнения. <p>
         * Делегирует исполнение операции сравнения методу
         * {@link tdo.expr.CompareExpression#compareStd(java.lang.Object, java.lang.Object) } .
         *
         * @param v1 первый сравниваемый операнд
         * @param v2 второй сравниваемый операнд
         * @return <code>true</code> когда значение первого операнда
         *   меньше или равно значению второго операндаю <code>false</code> - в
         *   противном случае
         */
        protected boolean eval1(Object v1, Object v2) {
            int r = compareStd(v1, v2);
            return r <= 0 ? true : false;
        }
    }//class BetweenExpression
    /**
     * Обеспечивает выполнение оператора, проверяющего содержит ли строка,
     * определенная первым операндом строковое значение второго операнда
     * как подстроку.<p>
     * Преобразование типов не производится. К значениям обоих операндов, не
     * равным <code>null</code> применяется метод <code>toString()</code> перед
     * сравнением.
     *
     * @see tdo.expr.AbstractOperator.ContainingOperator
     */
    public static class ContainingExpression extends EqualsExpression {
        /**
         * Создает экземпляр класса для заданных контекста,
         * оператора и операндов.<p>
         *
         * @param context контекст выражения
         * @param operator объект типа
         *      {@link tdo.expr.AbstractOperator.ContainingOperator}

         * @param op1 первый операнд
         * @param op2 второй операнд
         */
        public ContainingExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * Устанавливает значение свойства <code>lexType</code>
         * равным значению {@link tdo.tools.expr.LexConst#CONTAINING }.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.CONTAINING;
        }
        /**
         * Возвращает результат проверки, содержит ли строка знаков,
         * определяемая первым операндом, подстроку, заданную вторым операндом.<p>
         * @param v1 значение первого операнда
         * @param v2 значение второго операнда
         * @return <code>true</code> если оба операнда не равны <code>null</code>
         *      и строка, полученная применением <code>v1.toString()</code>
         *      содержит подстроку, полученную применением
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
     * Обеспечивает выполнение оператора, проверяющего начинается ли строка,
     * определенная первым операндом со строкового значения, заданным вторым
     * операндом.<p>
     * Преобразование типов не производится. К значениям обоих операндов, не
     * равным <code>null</code> применяется метод <code>toString()</code> перед
     * сравнением.
     *
     * @see tdo.expr.AbstractOperator.StartingWithOperator
     */
    public static class StartingWithExpression extends EqualsExpression {
        /**
         * Создает экземпляр класса для заданных контекста,
         * оператора и операндов.<p>
         *
         * @param context контекст выражения
         * @param operator объект типа
         *      {@link tdo.expr.AbstractOperator.StartingWithOperator }
         *
         * @param op1 первый операнд
         * @param op2 второй операнд
         */
        public StartingWithExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * Устанавливает значение свойства <code>lexType</code>
         * равным значению {@link tdo.tools.expr.LexConst#STARTINGWITH }.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.STARTINGWITH;
        }
        /**
         * Возвращает результат проверки, начинается ли строка знаков,
         * определяемая первым операндом, с подстроки, заданной вторым операндом.<p>
         * @param v1 значение первого операнда
         * @param v2 значение второго операнда
         * @return <code>true</code> если оба операнда не равны <code>null</code>
         *      и строка, полученная применением <code>v1.toString()</code>
         *      начинается с подстроки, полученной применением
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
     * Обеспечивает выполнение оператора, проверяющего начинается ли строка,
     * определенная первым операндом со строкового значения, заданным вторым
     * операндом.<p>
     * Преобразование типов не производится. К значениям обоих операндов, не
     * равным <code>null</code> применяется метод <code>toString()</code> перед
     * сравнением.
     *
     * @see tdo.expr.AbstractOperator.NotStartingWithOperator
     */
    public static class NotStartingWithExpression extends StartingWithExpression {
        /**
         * Создает экземпляр класса для заданных контекста,
         * оператора и операндов.<p>
         *
         * @param context контекст выражения
         * @param operator объект типа
         *      {@link tdo.expr.AbstractOperator.NotStartingWithOperator }
         *
         * @param op1 первый операнд
         * @param op2 второй операнд
         */
        public NotStartingWithExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * Устанавливает значение свойства <code>lexType</code>
         * равным значению {@link tdo.tools.expr.LexConst#NOTSTARTINGWITH }.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.NOTSTARTINGWITH;
        }
        /**
         * Возвращает результат проверки, начинается ли строка знаков,
         * определяемая первым операндом, с подстроки, заданной вторым операндом.<p>
         * @param v1 значение первого операнда
         * @param v2 значение второго операнда
         * @return <code>true</code> если хоты бы один операнд равен <code>null</code>
         *      или строка, полученная применением <code>v1.toString()</code>
         *      НЕ начинается с подстроки, полученной применением
         *      <code>v2.toString()</code>.
         */
        @Override
        protected boolean eval(Object v1, Object v2) {
            return !super.eval(v1, v2);
        }
    }//class NotStaringWithExpression
    /**
     * Обеспечивает выполнение оператора, проверяющего содержит ли строка,
     * определенная первым операндом строковое значение второго операнда
     * как подстроку.<p>
     *
     * Преобразование типов не производится. К значениям обоих операндов, не
     * равным <code>null</code> применяется метод <code>toString()</code> перед
     * сравнением.
     *
     * @see tdo.expr.AbstractOperator.NotContainingOperator
     * @see tdo.expr.CompareExpression.ContainingExpression

     */
    public static class NotContainingExpression extends ContainingExpression {
        /**
         * Создает экземпляр класса для заданных контекста,
         * оператора и операндов.<p>
         *
         * @param context контекст выражения
         * @param operator объект типа
         *      {@link tdo.expr.AbstractOperator.NotContainingOperator}
         *
         * @param op1 первый операнд
         * @param op2 второй операнд
         */
        public NotContainingExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * Устанавливает значение свойства <code>lexType</code>
         * равным значению {@link tdo.tools.expr.LexConst#NOTCONTAINING }.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.NOTCONTAINING;
        }
        /**
         * Возвращает результат проверки, содержит ли строка знаков,
         * определяемая первым операндом, подстроку, заданную вторым операндом.<p>
         * @param v1 значение первого операнда
         * @param v2 значение второго операнда
         * @return <code>true</code> если хотя бы один операнд равен <code>null</code>
         *      или строка, полученная применением <code>v1.toString()</code>
         *      НЕ содержит подстроку, полученную применением
         *      <code>v2.toString()</code>.
         */
        @Override
        protected boolean eval(Object v1, Object v2) {
            return !super.eval(v1, v2);
        }
    }//class NotContainingExpression
    /**
     * Обеспечивает выполнение оператора сравнения операнда по заданному образцу.<p>
     * Выражение может содержать оперратор вида: <br>
     * <b>op1 LIKE op2</b>. <br>
     * где op1 и op2 должны быть строковыми выражениями, при этом op2 является
     * <i>образцом</i>, содержащим символы <i>образца</i>  '%' и/или
     * '_' и любые другие символы.
     * <p> Условия LIKE предназначены для сопоставления символьных строк с
     * заданным образцом,  т.е. для проверки значения заданного строкового
     * операнда, что бы определить, соответствует ли оно установленному образцу.
     * <p>Символы <i>образца</i> трактуются:
     * <ul>
     *      <li>Символ подчеркивания "_" обозначает любой отдельный символ;</li>
     *      <li>Знак процента "%" представляет любую последовательность из k
     *          символов (где k может быть равно нулю);
     *      </li>
     *      <li>Все другие символы обозначают сами себя.
     *      </li>
     * </ul>
     * <p><b>Пример 1.</b> Выражение:
     * <pre>
     *    'Bill Gates' LIKE '%Gates'
     * </pre>
     * Даст <code>true</code> для приведенного выше выражения. Строка слева от
     * ключевого слова <code>LIKE</code> может содержать любые символы перед
     * словом 'Gates'.
     *
     * <p><b>Пример 2.</b> Выражение:
     * <pre>
     *    'Bill Gates' LIKE 'B%ates'
     * </pre>
     * Даст <code>true</code> для приведенного выше выражения. Строка слева от
     * ключевого слова <code>LIKE</code> может содержать любые символы между
     * символом 'B' и строкой 'ates', например, 'Bob Lates' LIKE 'B%ates'
     * также даст <code>true</code>.
     * словом 'Gates'.
     *
     * <p><b>Пример 3.</b> Выражение:
     * <pre>
     *    'Bill Gates' LIKE 'Bill _ates'
     * </pre>
     * Даст <code>true</code> для приведенного выше выражения. Строка слева от
     * ключевого слова <code>LIKE</code> может содержать любой символ вместо
     * символа 'G', например, 'Bill Lates' LIKE 'Bill _ates' также даст
     * <code>true</code>.
     * <p><b>Пример 4.</b> Выражение:
     * <pre>
     *    'Bob Gates' LIKE 'B% Gate_'
     * </pre>
     * Даст <code>true</code> для приведенного выше выражения. Строка слева от
     * ключевого слова <code>LIKE</code> может содержать любую строку после
     * символа 'B', затем следует пробел, строка 'Gate' и любой символ.
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
         * Создает экземпляр класса для заданных контекста,
         * оператора и операндов.<p>
         *
         * @param context контекст выражения
         * @param operator объект типа
         *      {@link tdo.expr.AbstractOperator.LikeOperator}
         *
         * @param op1 первый операнд
         * @param op2 второй операнд
         */
        public LikeExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
            wildCard = "";
        }
        /**
         * Устанавливает значение свойства <code>lexType</code>
         * равным значению {@link tdo.tools.expr.LexConst#LIKE }.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.LIKE;
        }
        /**
         * Возвращает результат сравнения заданного строкового значения по
         * заданному образцу.
         * <p>Если значение параметра <code>v1</code> или <code>v2</code> равно
         * <code>null</code>, то в контексте регистрируется информация об ошибке
         * с кодом {@link tdo.expr.ExpressionContext#COMPAREEXPRESSION} и
         * выбрасывается исключение типа {@link tdo.expr.ErrorExpression}.
         * <p>Если одно из значений параметров <code>v1</code> или <code>v2</code>
         * не является строкой знаков <code>java.lang.String</code>,
         * то в контексте регистрируется информация об ошибке
         * с кодом {@link tdo.expr.ExpressionContext#COMPAREEXPRESSION} и
         * выбрасывается исключение типа {@link tdo.expr.ErrorExpression}.
         * @param v1 значение проверяемого по образцу операнда
         * @param v2 строка знаков, содержащая <i>образец</i>.
         * @return <code>true</code> - если результат сравнения по образцу
         *      успешен. <code>false</code> - в противном случае.
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
     * Обеспечивает выполнение оператора NOT сравнения операнда по заданному образцу.<p>
     * Выражение может содержать оператор вида: <br>
     * <b>op1 NOT LIKE op2</b>. <br>
     * где op1 и op2 должны быть строковыми выражениями, при этом op2 является
     * <i>образцом</i>, содержащим символы <i>образца</i>  '%' и/или
     * '_' и любые другие символы.
     *
     * @see tdo.expr.CompareExpression.LikeExpression
     * @see tdo.expr.LikeMatcher
     * @see tdo.expr.RegExMatcher
     *
     */
    public static class NotLikeExpression extends LikeExpression {
        /**
         * Создает экземпляр класса для заданных контекста,
         * оператора и операндов.<p>
         *
         * @param context контекст выражения
         * @param operator объект типа
         *      {@link tdo.expr.AbstractOperator.NotLikeOperator}
         *
         * @param op1 первый операнд
         * @param op2 второй операнд
         */
        public NotLikeExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * Устанавливает значение свойства <code>lexType</code>
         * равным значению {@link tdo.tools.expr.LexConst#NOYLIKE }.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.NOTLIKE;
        }
        /**
         * Возвращает обратный результат сравнения заданного строкового значения по
         * заданному образцу.
         * <p>Если значение параметра <code>v1</code> или <code>v2</code> равно
         * <code>null</code>, то в контексте регистрируется информация об ошибке
         * с кодом {@link tdo.expr.ExpressionContext#COMPAREEXPRESSION} и
         * выбрасывается исключение типа {@link tdo.expr.ErrorExpression}.
         * <p>Если одно из значений параметров <code>v1</code> или <code>v2</code>
         * не является строкой знаков <code>java.lang.String</code>,
         * то в контексте регистрируется информация об ошибке
         * с кодом {@link tdo.expr.ExpressionContext#COMPAREEXPRESSION} и
         * выбрасывается исключение типа {@link tdo.expr.ErrorExpression}.
         * @param v1 значение проверяемого по образцу операнда
         * @param v2 строка знаков, содержащая <i>образец</i>.
         * @return <code>true</code> - если сравнение по образцу
         *      НЕ прошло. <code>false</code> - в противном случае.
         */
        @Override
        protected boolean eval(Object v1, Object v2) {
            return !super.eval(v1, v2);
        }
    }//class NotLikeExpression
//'B. Gates' regex '(B(ill)?|B.) Gates'
//'Bill Gates' regex '(B(ill)?|B.) Gates'

    /**
     * Обеспечивает выполнение оператора сравнения операнда по заданному
     * регулярному выражению.<p>
     * Выражение может содержать оператор вида: <br>
     * <b>op1 RegEx op2</b>. <br>
     * где op1 и op2 должны быть строковыми выражениями, при этом op2 является
     * <i>образцом</i>, содержащим регулярное выражение.
     * <p>Для вычисления значения выражения используется класс 
     * <code>tdo.expr.RegExMatcher</code>, представляющий собой оболочку 
     * над <code>java.util.regex</code> API.
     *
     * <p><b>Пример 1.</b> Выражение:
     * <pre>
     *    'B. Gates' regex '(B(ill)?|B.) Gates'
     * </pre>
     * Вернет <code>true</code> для приведенного выше выражения. Точно также
     * вернет <code>true</code> выражение:
     * <pre>
     *    'Bill Gates' regex '(B(ill)?|B.) Gates'
     * </pre>
     *
     * <p><b>Пример 2.</b> Выражение:
     * <pre>
     *    '1' RegEx '[0-9]{1,5}'
     *    '12' RegEx '[0-9]{1,5}'
     *    '123' RegEx '[0-9]{1,5}'
     *    '1234' RegEx '[0-9]{1,5}'
     *    '12345' RegEx '[0-9]{1,5}'
     * </pre>
     * Все приведенные выражения дают при вычислении <code>true</code>.
     * В них проверяется левый операнд на то, что его символы должны быть
     * цифрами и  количество цифр от 1 до 5. Так что следующее выражение даст
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
         * Создает экземпляр класса для заданных контекста,
         * оператора и операндов.<p>
         *
         * @param context контекст выражения
         * @param operator объект типа
         *      {@link tdo.expr.AbstractOperator.RegExOperator}
         *
         * @param op1 первый, проверяемый операнд
         * @param op2 второй операнд, содержащий строковое регулярное выражение.
         */
        public RegExExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
            regEx = "";
        }
        /**
         * Устанавливает значение свойства <code>lexType</code>
         * равным значению {@link tdo.tools.expr.LexConst#REGEX }.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.REGEX;
        }
        /**
         * Возвращает результат сравнения заданного строкового значения по
         * заданному регулярному выражению.
         * <p>Если значение параметра <code>v1</code> или <code>v2</code> равно
         * <code>null</code>, то в контексте регистрируется информация об ошибке
         * с кодом {@link tdo.expr.ExpressionContext#COMPAREEXPRESSION} и
         * выбрасывается исключение типа {@link tdo.expr.ErrorExpression}.
         * <p>Если одно из значений параметров <code>v1</code> или <code>v2</code>
         * не является строкой знаков <code>java.lang.String</code>,
         * то в контексте регистрируется информация об ошибке
         * с кодом {@link tdo.expr.ExpressionContext#COMPAREEXPRESSION} и
         * выбрасывается исключение типа {@link tdo.expr.ErrorExpression}.
         * @param v1 значение проверяемого по регулярному выражению операнда
         * @param v2 строка знаков, содержащая <i>регулярное выражение</i>.
         * @return <code>true</code> - если левый операнд удовлетворяет
         *      регулярному выражению. <code>false</code> - в противном случае.
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
     * Обеспечивает выполнение оператора <i>обратного</i> (NOT) сравнения
     * операнда по заданному регулярному выражению.<p>
     * Выражение может содержать оператор вида: <br>
     * <b>op1 NOT RegEx op2</b>. <br>
     * где op1 и op2 должны быть строковыми выражениями, при этом op2 является
     * <i>образцом</i>, содержащим регулярное выражение.
     * <p>Для вычисления значения выражения используется класс
     * <code>tdo.expr.RegExMatcher</code>, представляющий собой оболочку
     * над <code>java.util.regex</code> API.
     * @see tdo.expr.LikeMatcher
     * @see tdo.expr.RegExMatcher
     * @see tdo.expr.CompareExpression.RegExExpression
     * @see tdo.expr.RegExMatcher
     */
    public static class NotRegExExpression extends RegExExpression {
        /**
         * Создает экземпляр класса для заданных контекста,
         * оператора и операндов.<p>
         *
         * @param context контекст выражения
         * @param operator объект типа
         *      {@link tdo.expr.AbstractOperator.NotRegExOperator}
         *
         * @param op1 первый, проверяемый операнд
         * @param op2 второй операнд, содержащий строковое регулярное выражение.
         */
        public NotRegExExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * Устанавливает значение свойства <code>lexType</code>
         * равным значению {@link tdo.tools.expr.LexConst#NOTREGEX }.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.NOTREGEX;
        }
        /**
         * Возвращает <i>обратный</i> результат сравнения заданного строкового
         * значения по заданному регулярному выражению.
         * <p>Если значение параметра <code>v1</code> или <code>v2</code> равно
         * <code>null</code>, то в контексте регистрируется информация об ошибке
         * с кодом {@link tdo.expr.ExpressionContext#COMPAREEXPRESSION} и
         * выбрасывается исключение типа {@link tdo.expr.ErrorExpression}.
         * <p>Если одно из значений параметров <code>v1</code> или <code>v2</code>
         * не является строкой знаков <code>java.lang.String</code>,
         * то в контексте регистрируется информация об ошибке
         * с кодом {@link tdo.expr.ExpressionContext#COMPAREEXPRESSION} и
         * выбрасывается исключение типа {@link tdo.expr.ErrorExpression}.
         * @param v1 значение проверяемого по регулярному выражению операнда
         * @param v2 строка знаков, содержащая <i>регулярное выражение</i>.
         * @return <code>true</code> - если левый операнд НЕ удовлетворяет
         *      регулярному выражению. <code>false</code> - в противном случае.
         */
        @Override
        protected boolean eval(Object v1, Object v2) {
            return !super.eval(v1, v2);

        }
    }//class NotRegExExpression
    /**
     * Обеспечивает выполнение оператора IN, определяющего , содержится ли
     * значение  в заданном списке.<p>
     * Выражение может содержать оператор вида: <br>
     * <b>5 IN (1,4,6,5)</b>. <br>
     * которое проверяет, содержится ли операнд ( в данном случае литерал 5)
     * в списке.
     * <b>Пример.</b> Проверка, содержится ли строка "Test" в списке слов:
     *   "Mam,Dad,Test"<br>
     * <b>'Test' IN ('Mam','Dad','Test')</b>. <br>
     * дает <code>true</code>
     *  <p>Примечание. Следует обратить внимание на то, что круглые скобки,
     *    в которые заключен список являются обязательными.
     * @see tdo.expr.ValueList
     * @see tdo.expr.CompareExpression.NotInExpression
     */
    public static class InExpression extends CompareExpression {
        /**
         * Создает экземпляр класса для заданных контекста,
         * оператора и операндов.<p>
         *
         * @param context контекст выражения
         * @param operator объект типа
         *      {@link tdo.expr.AbstractOperator.InOperator}
         *
         * @param op1 первый, проверяемый операнд
         * @param op2 второй операнд, содержащий список элементов.
         */
        public InExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * Вычисляет значения операндов и возвращает результат вызова
         * метода {@link #eval(java.lang.Object, java.lang.Object) } для этих
         * значений.<p>
         * @param values именованная коллекция элементов, например,
         *  <code>tdo.DataRow</code>.
         * @return результат сравнения
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
         * Вычисляет значения операндов и возвращает результат вызова
         * метода {@link #eval(java.lang.Object, java.lang.Object) } для этих
         * значений.<p>
         * @param values массив именованных коллекций элементов, например,
         *  <code>tdo.DataRow</code>.
         * @return результат сравнения
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
         * Первый операнд - это объект произвольного типа. Второй операнд
         * должен иметь тип <code>tdo.expr.ValueList</code>.<p>
         * Если значение первого операнда равно <code>null</code>, то, если
         * список <code>ValueList</code>, содержит <code>null</code>, то
         * возвращается <code>true</code>.
         * @param v1 объект произвольного типа
         * @param v2 объект типа <code>tdo.expr.ValueList</code>
         * @return <code>true</code> если объект <i>v1</i> содержится в списке
         *     <i>v2</i>. <code>false</code> - в противном случае.
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
         * Устанавливает значение свойства <code>lexType</code>
         * равным значению {@link tdo.tools.expr.LexConst#IN }.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.IN;
        }
    }//class InExpression
    /**
     * Обеспечивает выполнение оператора NOT IN, определяющего , содержится ли
     * значение  в заданном списке.<p>
     * Выражение может содержать оператор вида: <br>
     * <b>5 NOT IN (1,4,6)</b>. <br>
     * которое проверяет, содержится ли операнд ( в данном случае литерал 5)
     * в списке.
     * <b>Пример.</b> Проверка, содержится ли строка "Test" в списке слов:
     *   "Mam,Dad,Test"<br>
     * <b>'Test' NOT IN ('Mam','Dad','Test')</b>. <br>
     * дает <code>false</code>, поскольку в списке есть элемент со значением
     * "Test".
     *  <p>Примечание. Следует обратить внимание на то, что круглые скобки,
     *    в которые заключен список являются обязательными.
     * @see tdo.expr.CompareExpression.InExpression
     */
    public static class NotInExpression extends InExpression {
        /**
         * Создает экземпляр класса для заданных контекста,
         * оператора и операндов.<p>
         *
         * @param context контекст выражения
         * @param operator объект типа
         *      {@link tdo.expr.AbstractOperator.InOperator}
         *
         * @param op1 первый, проверяемый операнд
         * @param op2 второй операнд, содержащий список элементов.
         */
        public NotInExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * Делегирует исполнение методу суперкласса и возвращает противоположное
         * значение.<p>
         * @param v1 проверяемое значение
         * @param v2 список элементов
         * @return <code>true</code> если проверяемый элемент не содержится в
         *   списке. <code>false</code> - в противном случае.
         */
        @Override
        protected boolean eval(Object v1, Object v2) {
            return !super.eval(v1, v2);
        }
        /**
         * Устанавливает значение свойства <code>lexType</code>
         * равным значению {@link tdo.tools.expr.LexConst#NOTIN }.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.NOTIN;
        }
    }//class NotInExpression
}//class CompareExpression
