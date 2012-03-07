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
 * Базовый класс для классов моделирующих арифметические операции и операцию 
 * конкатенации.<p>
 * 
 * К математическим операциям относятся стандартные арифметические операции, 
 * такие как <i>сложение,вычитание,умножение,деление</i>. Хотя операция 
 * <i>конкатенации</i> формально и не относится к арифметическим, но по своей 
 * природе ее можно считать такой. 
 * <p>Экземпляр класса оперирует двумя операндами, для которых определен 
 * оператор. Операнды являются объектами типа {@link tdo.expr.IOperand}. 
 * Оператор является объектом типа {@link tdo.expr.IOperator}. Для вычисления
 * математического выражения конкретный тип оператора может быть одним из:
 * 
 * <ol>
 *   <li>{@link tdo.expr.AbstractOperator.AddOperator}</li>
 *   <li>{@link tdo.expr.AbstractOperator.SubtrOperator}</li>
 *   <li>{@link tdo.expr.AbstractOperator.MultOperator}</li>
 *   <li>{@link tdo.expr.AbstractOperator.DivOperator}</li>
 *   <li>{@link tdo.expr.AbstractOperator.CancatOperator}</li>
 * </ol> 
 * 
 * <p>Каждому из пречисленных операторов соответствует класс выражения, 
 * наследующий  <code>MathExpression</code>:
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
     * Создает экземпляр класса для заданных контекта выражения,объекта
     * оператора и объектов операндов.
     * @param context контекст выражения
     * @param operator объект оператора
     * @param op1 первый операнд
     * @param op2 второй операнд
     */
    public MathExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
        this.setContext(context);
        this.operator = operator;
        this.op1 = op1;
        this.op2 = op2;
    }

    /**
     * Возвращает значение выражения для заданной коллекции
     * именованных элементов.<p>
     * Метод получает значения операндов, вызывая их методы <code>getValue</code>,
     * а затем вызывает метод <code>computeValues</code>, передавая
     * последнему в качестве параметров объект оператора и полученные
     * значения операндов.
     *
     * @param values коллекции именованных элементов, например,
     * объект типа <code>tdo.DataRow</code>.
     * @return значение выражения
     * @throws tdo.tools.scanner.ExpressionException при возникновении ошибок,
     *  например, когда операнды имеют недопустимый тип для заданного оператора.
     *  Перед возбуждением исключения информация об ошибке добавляется к
     *  коллекции ошибок контекста выражения
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
     * Возвращает значение выражения для заданного массива коллекций
     * именованных элементов.<p>
     * Метод вычисляет значения операндов, вызывая их методы <code>getValue</code>,
     * а затем вызывает метод <code>computeValues</code>, передавая
     * последнему в качестве параметров объект оператора и полученные
     * значения операндов.
     *
     * @param values массив коллекций именованных элементов, например,
     * массив объектов типа <code>tdo.DataRow</code>.
     * @return значение выражения
     * @throws tdo.tools.scanner.ExpressionException при возникновении ошибок,
     *  например, когда операнды имеют недопустимый тип для заданного оператора.
     *  Перед возбуждением исключения информация об ошибке добавляется к
     *  коллекции ошибок контекста выражения
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
     * Возвращает значение, вычисленное для заданных значений операндов.
     * Применяется, когда значения операндов выражения имеют тип
     * <code>java.lang.Double</code> или один или оба приведены к этому типу.
     * <p>Классы наследники реализуют метод для конкретной операции
     * @param op1 первый операнд выражения
     * @param op2 второй операнд выражения
     * @return вычисленное значение
     */
    public abstract Double computeDouble(Double op1, Double op2);
    /**
     * Возвращает значение, вычисленное для заданных значений операндов.
     * Применяется, когда значения операндов выражения имеют тип
     * <code>java.lang.Long</code> или один или оба приведены к этому типу.
     * <p>Классы наследники реализуют метод для конкретной операции
     * @param op1 первый операнд выражения
     * @param op2 второй операнд выражения
     * @return вычисленное значение
     */
    public abstract Long computeLong(Long op1, Long op2);
    /**
     * Возвращает значение, вычисленное для заданных значений операндов.
     * Применяется, когда значения операндов выражения имеют тип
     * <code>java.math.BigDecimal</code> или один или оба приведены к этому типу.
     * <p>Классы наследники реализуют метод для конкретной операции
     * @param op1 первый операнд выражения
     * @param op2 второй операнд выражения
     * @return вычисленное значение
     */
    public abstract BigDecimal computeBigDecimal(BigDecimal op1, BigDecimal op2);

    //public abstract String computeString(String op1, String op2);

    @Override
    protected void setLexType() {
        lexType = -1;
    }


    /**
     * Проверяет тип заданного операнда на допустимость.
     * 
     * Литеральный операнд, содержащий значение <code>null</code> также допустим
     * в арифметических выражениях, но он преобразуется в правильный тип 
     * <code>java.util.Integer</code> перед вызовом данного метода.
     * @param op проверяемый операнд.
     * @return <code>true</code> если вызов <i>op.getType()</i> дает один из
     *   типов: <code>Integer, Short, Byte, Float, Double, BigDecimal</code>.
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
     * Возвращает тип выражения.
     * Делегирует исполнение методу {@link #getType(tdo.expr.IOperator, tdo.expr.IOperand, tdo.expr.IOperand) }
     * @return класс выражения
     */
    @Override
    public Class getType() {
        return this.getType(operator, op1, op2);
    }
    /**
     * Вычисляет и возвращает тип выражения для заданных оператора и операндов.
     * 
     * <p>Значением типа может быть один из типов классов-оболочек пакета
     * <code>java.lang: Byte,Short,Integer,Long, Float,Double,BigDecimal,/<code>
     * если по заданным параметрам можно точно определить java класс 
     * результата возрвращаемого выражения.
     * <p>Если java класс результата выражения установить нельзя, 
     * <code>tdo.expr.MathExpression</code>. Особый случай, когда параметр 
     * <code>operator</code> является экземпляром класса 
     * <code>{@link tdo.expr.AbstractOperator.ConcatOperator}. В этом случае
     * возвращается значение <code>String.class</code>.
     * <p>Примечание. Тип выражения нельзя определить, когда в состав выражения
     *    входит идентификатор (тип IdentifierOperand) и/или параметр
     *   (тип ParameterOperand). Такие операнды запрашивают тип у контекста
     *   выражения. В таком случае, возможно, контекст еще не инициализирован
     *   и не может определить тип по имени идентификатора или параметра.
     *   Например, к объекту типа <code>DefaultExpressionContext</code> должны
     *   быть добавлены все таблицы <code>tdo.Table</code>, прежде, чем исходное
     *   выражение обрабатывается. В <code>DefaultExpressionContext</code>
     *   введено понятие <code><b>testMode</b></code>. Если выражение
     *   обрабатывается во включенной моде тестирования, то метод getType()
     *   может возвращать не точный тип данных.
     * 
     * <p>Ниже приведен алгоритм, используемый при определении типа:
     * 
     * <ul>
     *   <li>Если тип операнда <code>op1.getType()</code> и/или
     *       <code>op2.getType()</code>  равен/равны <code>null</code>,
     *       считается, что эти тип(ы) равны <code>Integer.class</code> и
     *       рассматриваются далее.
     *   </li>
     *   <li>Если оператор <code>operator</code> есть экземпляр класса
     *     <code>tdo.ConсatOperator</code>, то возвращается значение
     *     <code>String.class</code>.
     *   </li>
     *   <li>Если типы операнда <code>op1.getType()</code> есть
     *       {@link tdo.expr.IdentifierOperand} или  {@link tdo.expr.MathExpression},
     *       а тип второго операнда <code>op2.getType()</code> также равен
     *       одному из указанных выше, то возвращается значение 
     *       <code>tdo.expr.MathExpression.class</code>.
     *   </li>
     *   <li>Если только один из операндов  имеет тип 
     *       <code>tdo.expr.IdentifierOperand</code> или  
     *        <code>tdo.expr.MathExpression</code>, то второй должен иметь тип
     *        <code>Byte,Short,Integer,Long,Float,Double, BigDecimal</code>.
     *        Тогда возвращается значение <code>tdo.expr.MathExpression</code>.
     *        В противном случае возвращается 
     *        <code>tdo.expr.ErrorExpression.class</code>.
     *   </li>
     *   <li>Если один из операндов имеет тип <code>BigDecimal</code>, то 
     *       возвращается <code>BigDecimal.class</code>.
     *   </li>
     *   <li>Если один из операндов имеет тип <code>Double</code> или 
     *       <code>Float</code>, то 
     *       возвращается <code>Double.class</code>.
     *   </li>
     *   <li>Если оператор является экземпляром класса 
     *      {@link tdo.expr.AbstractOperator.DivOperator}, то возвращается
     *      <code>Double.class</code>. В противном случае, для 
     *       <code>Byte,Short,Integer,Long</code>, возвращается 
     *      <code>Long.class</code>, а для любого другого типа возвращается
     *      <code>tdo.expr.ErrorExpression</code>.
     *   </li>
     * </ul>
     *   
     * @param operator оператор, применяемый к заданным операндам.
     * @param op1 первый операнд для заданного оператора.
     * @param op2 второй операнд для заданного оператора.
     * @return вычисленный тип выражения
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
     * Вычисляет значение для заданного оператора по заданным значениям операндов.
     *
     * <p>Если <code>operator</code> является экземпляром типа
     * <code>tdo.expr.AbstractOperator.ConcatOperator</code>, то:
     * <ul>
     *    <li>если оба операнда равны <code>null</code>, то возвращается пустая
     *        строка знаков.
     *    </li>
     *    <li>Если только первый операнд имеет значение <code>null</code>, то
     *        возвращается значение <code>value2.toString()</code>.
     *    </li>
     *    <li>Если только второй операнд имеет значение <code>null</code>, то
     *        возвращается значение <code>value1.toString()</code>.
     *    </li>
     *    <li>Если оба значения операндов отличны от <code>null</code>,
     *        то возвращается сцепление строк:
     *        <code>value1.toString() + value2.toString()</code>
     * </ul>
     *
     * <p> Если один или оба значения операндов равны <code>null</code>, то
     * операнд(ы) со значением <code>null</code> преобразуется в
     * <code>java.lang.Integer(0)</code>. Далее анализируются преобразованные,
     * если необходимо, значения операндов.
     * <p>Если один из операндов имеет тип <code>java.math.BigDecimal</code>,
     *    то второй операнд преобразуется к этому типу и возвращается значение
     *    <oce>computeBigDecimal(value1,value2)</code>.
     * <p>Если один из операндов имеет тип <code>java.lang.String</code>,
     *    то оба операнда преобразуются в тип <code>java.lang.Double</code> и
     *    возвращается значение
     *    <code>computeDouble(v1,v2)</code>, где v1 и v2 имеют тип <code>Double</code>,
     *          как результат преобразования соответственно <i>value1</i> и
     *          <i>value2</i>
     * <p>Если один из операндов имеет тип <code>java.lang.Double</code>,
     *    то оба операнда преобразуются в тип <code>java.lang.Double</code> и
     *    возвращается значение
     *    <code>computeDouble(v1,v2)</code>, где v1 и v2 имеют тип <code>Double</code>,
     *          как результат преобразования соответственно <i>value1</i> и
     *          <i>value2</i>
     * <p>Если один из операндов имеет тип <code>java.lang.Float</code>,
     *    то оба операнда преобразуются в тип <code>java.lang.Double</code> и
     *    возвращается значение
     *    <code>computeDouble(v1,v2)</code>, где v1 и v2 имеют тип <code>Double</code>,
     *          как результат преобразования соответственно <i>value1</i> и
     *          <i>value2</i>
     * <p>Если это оператор деления, т.е. параметр <code>operator</code>
     *      имеет тип <code>DivOperator</code>,
     *    то оба операнда преобразуются в тип <code>java.lang.Double</code> и
     *    возвращается значение
     *    <code>computeDouble(v1,v2)</code>, где v1 и v2 имеют тип <code>Double</code>,
     *          как результат преобразования соответственно <i>value1</i> и
     *          <i>value2</i>
     * <p>Если один из операндов имеет тип <code>java.lang.Long</code>,
     *    то оба операнда преобразуются в тип <code>java.lang.Long</code> и
     *    возвращается значение
     *    <code>computeLong(v1,v2)</code>, где v1 и v2 имеют тип <code>Long</code>,
     *          как результат преобразования соответственно <i>value1</i> и
     *          <i>value2</i>
     * <p>Если один из операндов имеет тип <code>java.lang.Byte</code> или
     *    <code>java.lang.Short</code> или <code>java.lang.Integer</code>,
     *    то оба операнда преобразуются в тип <code>java.lang.Long</code> и
     *    возвращается значение
     *    <code>computeLong(v1,v2)</code>, где v1 и v2 имеют тип <code>Long</code>,
     *          как результат преобразования соответственно <i>value1</i> и
     *          <i>value2</i>
     * @param operator
     * @param value1
     * @param value2
     * @return вычисленное значение
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
     * Преобразует заданное значение в примитивный тип <code>double</code> и
     * возвращает его.
     * <p>Если значение параметра <code>value</code> равно <code>null</code>, то
     * возвращается значение 0D. <p>
     *
     * <p>Если значение параметра <code>value</code> одного из перечисленных типов:
     * <ul>
     *    <li><code>java.lang.Byte</code></li>
     *    <li><code>java.lang.Short</code></li>
     *    <li><code>java.lang.Integer</code></li>
     *    <li><code>java.lang.Long</code></li>
     *    <li><code>java.lang.Float</code></li>
     *    <li><code>java.lang.Double</code></li>
     *    <li><code>java.lang.String</code></li>
     * </ul>
     * то возвращается преобразованное в <code>double</code> значение. В
     * противном случае, возвращается значение 0d.
     *
     * @param value преобразуемое значение
     * @return преобразованное значение типа <code>double</code>
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
     * Преобразует заданное значение в примитивный тип <code>long</code> и
     * возвращает его.
     *
     * <p>Если значение параметра <code>value</code> равно <code>null</code>, то
     * возвращается значение 0L. 
     *
     * <p>Если значение параметра <code>value</code> одного из перечисленных типов:
     * <ul>
     *    <li><code>java.lang.Byte</code></li>
     *    <li><code>java.lang.Short</code></li>
     *    <li><code>java.lang.Integer</code></li>
     *    <li><code>java.lang.Long</code></li>
     *    <li><code>java.lang.Float</code></li>
     *    <li><code>java.lang.Double</code></li>
     *    <li><code>java.lang.String</code></li>
     * </ul>
     * то возвращается преобразованное в <code>long</code> значение. В
     * противном случае, возвращается значение 0L.
     *
     * @param value преобразуемое значение
     * @return преобразованное значение типа <code>long</code>
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
     * Преобразует заданное значение в примитивный тип <code>int</code> и
     * возвращает его.
     *
     * <p>Если значение параметра <code>value</code> равно <code>null</code>, то
     * возвращается значение 0.
     *
     * <p>Если значение параметра <code>value</code> одного из перечисленных типов:
     * <ul>
     *    <li><code>java.lang.Byte</code></li>
     *    <li><code>java.lang.Short</code></li>
     *    <li><code>java.lang.Integer</code></li>
     *    <li><code>java.lang.Long</code></li>
     *    <li><code>java.lang.Float</code></li>
     *    <li><code>java.lang.Double</code></li>
     *    <li><code>java.lang.String</code></li>
     * </ul>
     * то возвращается преобразованное в <code>int</code> значение. В
     * противном случае, возвращается значение 0.
     *
     * @param value преобразуемое значение
     * @return преобразованное значение типа <code>int</code>
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
     * Преобразует заданное значение в тип <code>BigDecimal</code> и
     * возвращает его.
     *
     * <p>Если значение параметра <code>value</code> равно <code>null</code>, то
     * возвращается значение <code> new BigDecimal(0)</code>. <p>
     * Если значение параметра <code>value</code> имеет тип <code>BigDecimal</code>,
     * то оно возвращается без преобразований.
     *
     * <p>Если значение параметра <code>value</code> одного из перечисленных типов:
     * <ul>
     *    <li><code>java.lang.Byte</code></li>
     *    <li><code>java.lang.Short</code></li>
     *    <li><code>java.lang.Integer</code></li>
     *    <li><code>java.lang.Long</code></li>
     *    <li><code>java.lang.Float</code></li>
     *    <li><code>java.lang.Double</code></li>
     *    <li><code>java.lang.String</code></li>
     * </ul>
     * то возвращается преобразованное в <code>BigDecimal</code> значение. В
     * противном случае, возвращается значение BigDecimal(0).
     *
     * @param value преобразуемое значение
     * @return преобразованное значение типа <code>BigDecimal</code>
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
     * Обеспечивает выполнение арифметического сложения двух операндов.
     * <p>Операндами выражения могут быть: <code>Byte, Short, Integer, Long,
     * Float, Double, BigDecimal</code>. Допустимы также данные типа
     * <code>String</code>, содержимое которых представляет собой правильное
     * число. Значение операнда <code>null</code> допустимо и преобразуется в
     * целочисленный ноль ( Integer(0) ).
     */
    public static class AddExpression extends MathExpression {

        /**
         * Создает экземпляр класса для заданных контекста, оператора и операндов.
         * @param context контекст выражения
         * @param operator экземпляр класса {@link tdo.expr.AbstractOperator.AddOperator}
         * @param op1 первое слагаемое
         * @param op2 второе слагаемое
         */
        public AddExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }

        /**
         * Складывает и возвращает два значения типа <code>java.lang.Double</code>.
         * @param op1 первое слагаемое
         * @param op2 второе слагаемое
         * @return результат сложения
         */
        @Override
        public Double computeDouble(Double op1, Double op2) {
            double d;
            d = op1.doubleValue() + op2.doubleValue();
            return new Double(d);
        }
        /**
         * Складывает и возвращает два значения типа <code>java.lang.Long</code>.
         * @param op1 первое слагаемое
         * @param op2 второе слагаемое
         * @return результат сложения
         */
        @Override
        public Long computeLong(Long op1, Long op2) {
            long d;
            d = op1.longValue() + op2.longValue();
            return new Long(d);
        }
        /**
         * Складывает и возвращает два значения типа <code>java.math.BigDecimal</code>.
         * @param op1 первое слагаемое
         * @param op2 второе слагаемое
         * @return результат сложения
         */
        @Override
        public BigDecimal computeBigDecimal(BigDecimal op1, BigDecimal op2) {
            return op1.add(op2);
        }
    } //class AddExpression

    /**
     * Обеспечивает выполнение арифметического вычитания двух операндов.
     * <p>Операндами выражения могут быть: <code>Byte, Short, Integer, Long,
     * Float, Double, BigDecimal</code>. Допустимы также данные типа
     * <code>String</code>, содержимое которых представляет собой правильное
     * число. Значение операнда <code>null</code> допустимо и преобразуется в
     * целочисленный ноль ( Integer(0) ).
     */
    public static class SubtrExpression extends MathExpression {
        /**
         * Создает экземпляр класса для заданных контекста, оператора и операндов.
         * @param context контекст выражения
         * @param operator экземпляр класса {@link tdo.expr.AbstractOperator.SubtrOperator}
         * @param op1 уменьшаемое
         * @param op2 вычитаемое
         */
        public SubtrExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * Возвращает разность двух значений как тип <code>java.lang.Double</code>.
         *
         * @param op1 уменьшаемое
         * @param op2 вычитаемое
         * @return разность
         */
        @Override
        public Double computeDouble(Double op1, Double op2) {
            double d;
            d = op1.doubleValue() - op2.doubleValue();
            return new Double(d);
        }
        /**
         * Возвращает разность двух значений как тип <code>java.lang.Long</code>.
         *
         * @param op1 уменьшаемое
         * @param op2 вычитаемое
         * @return разность
         */
        @Override
        public Long computeLong(Long op1, Long op2) {
            long d;
            d = op1.longValue() - op2.longValue();
            return new Long(d);
        }
        /**
         * Возвращает разность двух значений как тип <code>java.lang.BigDecimal</code>.
         *
         * @param op1 уменьшаемое
         * @param op2 вычитаемое
         * @return разность
         */
        @Override
        public BigDecimal computeBigDecimal(BigDecimal op1, BigDecimal op2) {
            return op1.subtract(op2);
        }


    } //class SubtrExpression

    /**
     * Обеспечивает выполнение арифметического умножения двух операндов.
     * <p>Операндами выражения могут быть: <code>Byte, Short, Integer, Long,
     * Float, Double, BigDecimal</code>. Допустимы также данные типа
     * <code>String</code>, содержимое которых представляет собой правильное
     * число. Значение операнда <code>null</code> допустимо и преобразуется в
     * целочисленный ноль ( Integer(0) ).
     */
    public static class MultExpression extends MathExpression {
        /**
         * Создает экземпляр класса для заданных контекста, оператора и операндов.
         * @param context контекст выражения
         * @param operator экземпляр класса {@link tdo.expr.AbstractOperator.MultOperator}
         * @param op1 первый сомножитель
         * @param op2 второй сомножитель
         */
        public MultExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * Возвращает результат умножения двух значений как тип <code>java.lang.Double</code>.
         *
         * @param op1 первый сомножитель
         * @param op2 второй сомножитель
         * @return результат умножения
         */
        @Override
        public Double computeDouble(Double op1, Double op2) {
            double d;
            d = op1.doubleValue() * op2.doubleValue();
            return new Double(d);
        }
        /**
         * Возвращает результат умножения двух значений как тип <code>java.lang.Long</code>.
         *
         * @param op1 первый сомножитель
         * @param op2 второй сомножитель
         * @return результат умножения
         */
        @Override
        public Long computeLong(Long op1, Long op2) {
            long d;
            d = op1.longValue() * op2.longValue();
            return new Long(d);
        }
        /**
         * Возвращает результат умножения двух значений как тип <code>java.lang.BigDecimal</code>.
         *
         * @param op1 первый сомножитель
         * @param op2 второй сомножитель
         * @return результат умножения
         */
        @Override
        public BigDecimal computeBigDecimal(BigDecimal op1, BigDecimal op2) {
            return op1.multiply(op2);
        }

    } //class MultExpression

    /**
     * Обеспечивает выполнение арифметического деления двух операндов.
     * <p>Операндами выражения могут быть: <code>Byte, Short, Integer, Long,
     * Float, Double, BigDecimal</code>. Допустимы также данные типа
     * <code>String</code>, содержимое которых представляет собой правильное
     * число. Значение операнда <code>null</code> допустимо и преобразуется в
     * целочисленный ноль ( Integer(0) ).
     */
    public static class DivExpression extends MathExpression {
        /**
         * Создает экземпляр класса для заданных контекста, оператора и операндов.
         * @param context контекст выражения
         * @param operator экземпляр класса {@link tdo.expr.AbstractOperator.MultOperator}
         * @param op1 делимое
         * @param op2 делитель
         */
        public DivExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }
        /**
         * Возвращает результат деления двух значений как тип <code>java.lang.Double</code>.
         * @param op1 делимое
         * @param op2 делитель
         * @return результат умножения
         */
        @Override
        public Double computeDouble(Double op1, Double op2) {
            double d;
            d = op1.doubleValue() / op2.doubleValue();
            return new Double(d);
        }
        /**
         * Возвращает результат деления двух значений как тип <code>java.lang.Long</code>.
         * @param op1 делимое
         * @param op2 делитель
         * @return результат умножения
         */
        @Override
        public Long computeLong(Long op1, Long op2) {
            long d;
            d = op1.longValue() / op2.longValue();
            return new Long(d);
        }
        /**
         * Возвращает результат деления двух значений как тип <code>java.lang.BigDecimal</code>.
         * Вычисление производится по формуле:
         * <pre>
         *  op1.divide(op2, op1.scale() - op2.scale(), BigDecimal.ROUND_HALF_UP);
         * </pre>
         * @param op1 первый сомножитель
         * @param op2 второй сомножитель
         * @return результат умножения
         */
        @Override
        public BigDecimal computeBigDecimal(BigDecimal op1, BigDecimal op2) {
            return op1.divide(op2, op1.scale() - op2.scale(), BigDecimal.ROUND_HALF_UP);
        }


    } //class DivExpression

    /**
     * Обеспечивает выполнение конкатенации строковых значений двух операндов.
     * <p>Операндами выражения могут быть любые объекты, для которых
     * определен метод <code>toString()</code>.
     * Значение операнда <code>null</code> допустимо и преобразуется в пустую
     * строку.
     */
    public static class ConcatExpression extends MathExpression {

        public ConcatExpression(ExpressionContext context, IOperator operator, IOperand op1, IOperand op2) {
            super(context, operator, op1, op2);
        }

        /**
         * Всегда выбрасывает исключение типа {@link tdo.tools.scanner.ExpressionException}.
         */
        @Override
        public Double computeDouble(Double op1, Double op2) {
            throw new ExpressionException("ConcatExpression.computeDouble() : Unsupported operation (" + op1 + "||" + op2 + ")");
        }
        /**
         * Всегда выбрасывает исключение типа {@link tdo.tools.scanner.ExpressionException}.
         */
        @Override
        public Long computeLong(Long op1, Long op2) {
            throw new ExpressionException("ConcatExpression.computeLong() : Unsupported operation (" + op1 + "||" + op2 + ")");
        }
        /**
         * Всегда выбрасывает исключение типа {@link tdo.tools.scanner.ExpressionException}.
         */
        @Override
        public BigDecimal computeBigDecimal(BigDecimal op1, BigDecimal op2) {
            throw new ExpressionException("ConcatExpression.computeBigDecimal() : Unsupported operation (" + op1 + "||" + op2 + ")");
        }
    } //class MultExpression

} //class MathExpression
