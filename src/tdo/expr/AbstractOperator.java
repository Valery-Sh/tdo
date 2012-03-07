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
 * Базовый класс для определения всех классов, реализующих интерфейс <code>IOperator</code>.
 * Основное назначение - создание специализированных для классов-наследников выражений
 * типа <code>IExpression</code>.
 * <p> Когда при обработке исходного выражения встречается элемент, являющийся
 * оператором, то создается экземпляр класса <code> IOperator</code>, соответствующего
 * элементу. Например, для оператора сложения <b>"+"</b> создается объект типа
 * <code>tdo.expr.AbstractOperator.AddOperator</code>. Задачей объекта-оператора
 * является создание соответствующего ему выражения. Например, упомянутый
 * выше оператора сложения умеет создавать объекты типа
 * <code>tdo.expr.AbstractExpression.AddExpression. Создание выражения производится
 * применением метода
 * {@link #createExpression(tdo.expr.ExpressionContext, tdo.expr.IOperand, tdo.expr.IOperand)} .
 * Созданному выражению, назначается контекст выражения, ссылка на создающий
 * выражение оператор и операнды. Теперь, когда выражение вычисляется, оно
 * обладает всей необходимой информацией для этого.
 * <p>Ряд методов, определенных классом, выполняют вспомогательные функции,
 * такие как, контроль типов операндов. К таким методам относятся:
 * {@link #getGroupType(java.lang.Class) } ,
 * {@link #checkCompareOperands(tdo.expr.IOperand, tdo.expr.IOperand)  } ,
 * {@link #checkLiteralOperands(tdo.expr.IOperand, tdo.expr.IOperand)} ,
 * {@link #checkLogicalOperands(tdo.expr.IOperand, tdo.expr.IOperand)} .
 *
 */
public abstract class AbstractOperator implements IOperator {

    private ExpressionContext context;
    /**
     * Тип оператора, как он определен в {@link tdo.tools.logexpr#LexConst}.
     */
    public int lexType;
    /**
     * Первый ( или единственный ) операнда. Используется при построении выражения
     * для конкретного оператора.
     */
    protected IOperand op1;
    /**
     * Второй операнд. Используется при построении выражения
     * для конкретного оператора.
     */
    protected IOperand op2;

    /**
     * Устанавливает контекст, в котором обрабатывается выражение.
     *
     * @param context устанавливаемый контекст
     */
    @Override
    public void setContext(ExpressionContext context) {
        this.context = context;
    }

    /**
     * Создает объект выражения для заданных контекста, оператора и пары операндов.
     * Сохраняет операнды в защищенных полях {@link #op1} и {@link #op2}.
     * Данный класс не создает <code>IExpression</code> и возвращает <code>null</code>.
     * Классы наследники должны переоределить такое поведение.
     */
    @Override
    public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
        this.op1 = op1;
        this.op2 = op2;
        return null;
    }

    /**
     * Создает объект выражения для заданных контекста, оператора и единственного
     * операнда.
     * Сохраняет операнд в защищенном поле {@link #op1}.
     * Делегирует исполнение перегруженному методу
     * {@link #createExpression(tdo.expr.ExpressionContext,IOperand,IOperand}
     * со значением второго операнда, равным <code>null</code>.
     */
    @Override
    public IExpression createExpression(ExpressionContext context, IOperand op1) {
        return createExpression(context, op1, null);
    }

    /**
     * @return true, если класс объекта, реализующего интерфейс
     *    является унарным оператором. false - в противном случае. В данном классе
     *    всегда возвращает <code>false</code>.
     */
    @Override
    public boolean isUnary() {
        return false;
    }

    /**
     * Возвращает класс, представляющий собой групповой тип, для заданного класса.
     * Гпупповой тип - это условное понятие, используемое для удобства. Для
     * классов <code>java.lang.Byte, java.lang.Short, java.lang.Integer,
     * java.lang.Long, java.lang.Float, java.lang.Double, java.math.BigDecimal</code>,
     * в качестве группового типа выступает класс {@link tdo.expr.MathExpression} .
     * <p>Для класса {@link tdo.expr.IdentifierOperand} групповым типом считается
     *    этот же класс. Аналогично для классов {@link tdo.expr.ParameterOperand},
     *    {@link tdo.expr.ErrorExpression}, <code>java.lang.Boolean</code>,
     *    <code>java.lang.String</code>.
     * <p>Для классов <code>java.sqlTimestamp, java.sql.Date, java.util.Date
     * </code> групповым типом считается <code>java.sql.Date</code>.
     *
     * @param c класс, для которого определяется групповой тип
     * @return класс, являющийся групповым типом
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
     * Проверяет тип операнлов на допустимость использования в логических
     * выражениях <code>NOT, AND, OR</code>.
     * Для логических операций допустимым типом операнда является один из:
     * <ul>
     *   <li>java.lang.Boolean;</li>
     *   <li>tdo.expr.IdentifierOperand;</li>
     *   <li>tdo.expr.ParameterOperand;</li>
     * </ul>
     * @param op1 первый проверяемый операнд
     * @param op2 второй проверяемый операнд
     * @return true , когда опреранды допустимы для использования в логических
     *   выражениях. false - в противном случае.
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
     * Проверяет, является ли операнд математическим выражением.
     * Операнд относится к математическим выражением, если его метод
     * {@link tdo.expr.IOperand#getType() } возвращает один из типов:
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
     * @param op операнд, тип которого проверяется
     * @return true если операнд определяется как математический. false -
     *  в противном случае
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
     * Проверяет, является ли операнд строковым выражением.
     * Операнд относится к строковым выражениям, если его метод
     * {@link tdo.expr.IOperand#getType() } возвращает тип
     *   <code>java.lang.String</code>. <code>null</code> значение не относится
     *  к строковым выражениям
     * @param op операнд, тип которого проверяется
     * @return true если операнд определяется как строковый. false -
     *  в противном случае
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
     * Проверяет, является ли операнд выражением над датой.
     * Операнд относится к выражениям над датой, если его метод
     * {@link tdo.expr.IOperand#getType() } не равен <code>null</code> и равен
     *   одному из:
     * <ul>
     *   <li>java.util.Date;</li>
     *   <li>java.sql.Timestamp;</li>
     * </ul>
     * @param op операнд, тип которого проверяется
     * @return true если операнд определяется как выражения над датой. false -
     *  в противном случае
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
     * Проверяет, является ли операнд выражением над объектом времени.
     * Операнд относится к выражениям над временем, если его метод
     * {@link tdo.expr.IOperand#getType() } не равен <code>null</code> и равен
     *  <code>java.sql.Time;</code>.
     * @param op операнд, тип которого проверяется
     * @return true если операнд определяется как выражения над временем. false -
     *  в противном случае
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
     * Проверяет, является ли операнд булевым выражением.
     * Операнд относится к булевому выражению, если его метод
     * {@link tdo.expr.IOperand#getType() } не равен <code>null</code> и равен
     *  <code>java.lang.Boolean;</code>.
     * @param op операнд, тип которого проверяется
     * @return true если операнд определяется как булевое выражение. false -
     *  в противном случае
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
     * Проверяет операнды на допустимость использования в операциях сравнения.
     * Используются в классах, определяющих опреаторы сравнения при создании
     * выражений.
     * <p>Операнд является недопустимым для использования в операции сравнения,
     * если его метод {@link tdo.expr.IOperand#getType() } возвращает значение
     * <code>tdo.expr.ErrorExpression</code><p>
     * Если метод <code>getType()</code> для обеих выражений возвратил значения,
     * отличные от <code>tdo.expr.ErrorExpression</code>, то
     * операнд является допустимым для использования в операции сравнения,
     * когда выполнено одно из условия:
     * <ol>
     *   <li>один из или оба операнда равны <code>null</code>;</li>
     *   <li>Для обеих операндов метод {@link #isMathExpression(tdo.expr.IOperand)
     *       возвращает <code>true</code>;
     *   </li>
     *   <li>Для одного из перандов метод {@link #isMathExpression(tdo.expr.IOperand)
     *       возвращает <code>true</code>, а для другого метод
     *      {@link #isStringExpression(tdo.expr.IOperand) возвращает <code>true</code>;
     *   </li>
     *   <li>Для обеих операндов метод {@link #isDateExpression(tdo.expr.IOperand)
     *       возвращает <code>true</code>;
     *   </li>
     *   <li>Для одного из перандов метод {@link #isDateExpression(tdo.expr.IOperand)
     *       возвращает <code>true</code>, а для другого метод
     *      {@link #isStringExpression(tdo.expr.IOperand) возвращает <code>true</code>;
     *   </li>
     *   <li>Для обеих операндов метод {@link #isBooleanExpression(tdo.expr.IOperand)
     *       возвращает <code>true</code>;
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
     * Устанавливает значение поля {@link #lexType}. Поскольку метод объявлен
     * с модификатором <code>abstract</code>, то классы-наследники должны его
     * переопределять.
     */
    protected abstract void setLexType();
    // ************************************************************************
    // Arithmetic Operators
    // ************************************************************************
    /**
     * Соответствует арифметическому оператору "+" - сложение.<p>
     * Имеет приоритет равный 80.
     * Операция бинарная.
     */
    public static class AddOperator extends AbstractOperator {

        /**
         * Создает объект типа {@link AddExpression}, позволяющий выполнить
         * арифметическое сложение значений, определяемых операндами.
         * Может зарегистрировать ошибку, добавив ее к списку ошибок контекста
         * с кодом {@link tdo.expr.ExpressionContext#MATH_OPERANDTYPE.
         *
         * @return объект типа {@link AddExpression}.
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
         * Возвращает значение приоритета оператора сложения равное 80.
         */
        @Override
        public int getPriority() {
            return 80;
        }

        /**
         * Устанавливает значение {@link #lexType} равным <code>LexConst.PLUS</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.PLUS;
        }

        /**
         * @return строку знаков, содержащую значение "+".
         */
        @Override
        public String toString() {
            return "+";
        }
    }//class AddOperator

    /**
     * Соответствует арифметическому оператору "-" - вычитание.<p>
     * Имеет приоритет равный 80.
     * Операция бинарная.
     */
    public static class SubtrOperator extends AbstractOperator {

        /**
         * Создает объект типа {@link SubtrExpression}, позволяющий выполнить
         * арифметическое вычитание значений, определяемых операндами.
         * Может зарегистрировать ошибку, добавив ее к списку ошибок контекста
         * с кодом {@link tdo.expr.ExpressionContext#MATH_OPERANDTYPE.
         * @return объект типа {@link SubtrExpression}.
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
         * Возвращает значение приоритета оператора вычитания равное 80.
         */
        @Override
        public int getPriority() {
            return 80;
        }

        /**
         * Устанавливает значение {@link #lexType} равным <code>LexConst.MINUS</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.MINUS;
        }

        /**
         * @return строку знаков, содержащую значение "-".
         */
        @Override
        public String toString() {
            return "-";
        }
    }//class SubtrOperator

    /**
     * Соответствует арифметическому оператору "*" - умножение.<p>
     * Имеет приоритет равный 100.
     * Операция бинарная.
     */
    public static class MultOperator extends AbstractOperator {

        /**
         * Создает объект типа {@link MultExpression}, позволяющий выполнить
         * арифметическое умножение значений, определяемых операндами.
         * Может зарегистрировать ошибку, добавив ее к списку ошибок контекста
         * с кодом {@link tdo.expr.ExpressionContext#MATH_OPERANDTYPE.
         * @return объект типа {@link MultExpression}.
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
         * Возвращает значение приоритета оператора умножения равное 100.
         */
        @Override
        public int getPriority() {
            return 100;
        }

        /**
         * Устанавливает значение {@link #lexType} равным <code>LexConst.MULT</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.MULT;
        }

        /**
         * @return строку знаков, содержащую значение "*".
         */
        @Override
        public String toString() {
            return "*";
        }
    }////class MultOperator

    /**
     * Соответствует арифметическому оператору "/" - деление.<p>
     * Имеет приоритет равный 100.
     * Операция бинарная.
     */
    public static class DivOperator extends AbstractOperator {

        /**
         * Создает объект типа {@link DivExpression}, позволяющий выполнить
         * арифметическое деление значений, определяемых операндами.
         * Может зарегистрировать ошибку, добавив ее к списку ошибок контекста
         * с кодом {@link tdo.expr.ExpressionContext#MATH_OPERANDTYPE.
         * @return объект типа {@link DivExpression}.
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
         * Возвращает значение приоритета оператора деления равное 100.
         */
        @Override
        public int getPriority() {
            return 100;
        }

        /**
         * Устанавливает значение {@link #lexType} равным <code>LexConst.DIV</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.DIV;
        }

        /**
         * @return строку знаков, содержащую значение "/".
         */
        @Override
        public String toString() {
            return "/";
        }
    }

    /**
     * Соответствует  оператору конкатенации "||".<p>
     * Имеет приоритет равный 80.
     * Операция бинарная.
     */
    public static class ConcatOperator extends AbstractOperator {

        /**
         * Создает объект типа {@link ConcatExpression}, позволяющий выполнить
         * сцепление строковых значений, определяемых операндами.
         * @return объект типа {@link ConcatExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            return new ConcatExpression(context, this, op1, op2);
        }

        /**
         * Возвращает значение приоритета оператора конкатенации равное 80.
         */
        @Override
        public int getPriority() {
            return 80;
        }

        /**
         * Устанавливает значение {@link #lexType} равным <code>LexConst.CONCAT</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.CONCAT;
        }

        /**
         * @return строку знаков, содержащую значение "||".
         */
        @Override
        public String toString() {
            return "||";
        }
    }//class ConcatOperator

    /**
     * Соответствует оператору сравнения "=" - равно.<p>
     * Имеет приоритет равный 60.
     * Операция бинарная.
     */
    public static class EqualsOperator extends AbstractOperator {

        /**
         * Создает объект типа {@link EqualsExpression}, позволяющий выполнить
         * сравнение на равно значений, определяемых операндами.
         * Может зарегистрировать ошибку, добавив ее к списку ошибок контекста
         * с кодом {@link tdo.expr.ExpressionContext#OPERANDTYPE.
         * @return объект типа {@link EqualsExpression}.
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
         * Возвращает значение приоритета оператора сравнения "равно" равное 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * Устанавливает значение {@link #lexType} равным <code>LexConst.EQ</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.EQ;
        }

        /**
         * @return строку знаков, содержащую значение "=".
         */
        @Override
        public String toString() {
            return "=";
        }
    }//class EqualsOperator

    /**
     * Соответствует оператору сравнения <pre>">"</pre> - строго больше.<p>
     * Имеет приоритет равный 60.
     * Операция бинарная.
     */
    public static class GreaterOperator extends AbstractOperator {

        /**
         * Создает объект типа {@link GreaterExpression}, позволяющий выполнить
         * сравнение на строго больше значений, определяемых операндами.
         * Может зарегистрировать ошибку, добавив ее к списку ошибок контекста
         * с кодом {@link tdo.expr.ExpressionContext#OPERANDTYPE.
         * @return объект типа {@link GreaterExpression}.
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
         * Возвращает значение приоритета оператора сравнения "больше" равное 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * Устанавливает значение {@link #lexType} равным <code>LexConst.GT</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.GT;
        }

        /**
         * @return строку знаков, содержащую значение <pre>">"</pre>.
         */
        @Override
        public String toString() {
            return ">";
        }
    }//class GreaterOperator

    /**
     * Соответствует оператору сравнения <pre>"<"</pre> - строго меньше.<p>
     * Имеет приоритет равный 60.
     * Операция бинарная.
     */
    public static class LessOperator extends AbstractOperator {

        /**
         * Создает объект типа {@link LessExpression}, позволяющий выполнить
         * сравнение на строго меньше значений, определяемых операндами.
         * Может зарегистрировать ошибку, добавив ее к списку ошибок контекста
         * с кодом {@link tdo.expr.ExpressionContext#OPERANDTYPE.
         * @return объект типа {@link LessExpression}.
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
         * Возвращает значение приоритета оператора сравнения "строго меньше" равное 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * Устанавливает значение {@link #lexType} равным <code>LexConst.LT</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.LT;
        }

        /**
         * @return строку знаков, содержащую значение <pre>"<"</pre>.
         */
        @Override
        public String toString() {
            return "<";
        }
    }//class LessOperator

    /**
     * Соответствует оператору сравнения <pre>">="</pre> - больше или равно.<p>
     * Имеет приоритет равный 60.
     * Операция бинарная.
     */
    public static class GreaterEqualsOperator extends AbstractOperator {

        /**
         * Создает объект типа {@link GreaterEqualsExpression}, позволяющий выполнить
         * сравнение на больше или равно значений, определяемых операндами.
         * Может зарегистрировать ошибку, добавив ее к списку ошибок контекста
         * с кодом {@link tdo.expr.ExpressionContext#OPERANDTYPE.
         * @return объект типа {@link GreaterEqualsExpression}.
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
         * Возвращает значение приоритета оператора сравнения "больше или равно" равное 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * Устанавливает значение {@link #lexType} равным <code>LexConst.GTEQ</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.GTEQ;
        }

        /**
         * @return строку знаков, содержащую значение <pre>">="</pre>.
         */
        @Override
        public String toString() {
            return ">=";
        }
    }//class GreaterEqualsOperator

    /**
     * Соответствует оператору сравнения <pre>"<="</pre> - меньше или равно.<p>
     * Имеет приоритет равный 60.
     * Операция бинарная.
     */
    public static class LessEqualsOperator extends AbstractOperator {

        /**
         * Создает объект типа {@link LessEqualsExpression}, позволяющий выполнить
         * сравнение на меньше или равно значений, определяемых операндами.
         * Может зарегистрировать ошибку, добавив ее к списку ошибок контекста
         * с кодом {@link tdo.expr.ExpressionContext#OPERANDTYPE.
         * @return объект типа {@link LessEqualsExpression}.
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
         * Возвращает значение приоритета оператора сравнения "меньше или равно" равное 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * Устанавливает значение {@link #lexType} равным <code>LexConst.LTEQ</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.LTEQ;
        }

        /**
         * @return строку знаков, содержащую значение <pre>"<="</pre>.
         */
        @Override
        public String toString() {
            return "<=";
        }
    }//class LessEqualsOperator

    /**
     * Соответствует оператору сравнения <pre>"between"</pre> - "между".<p>
     * Имеет приоритет равный 60.
     * Операция бинарная.
     */
    public static class BetweenOperator extends AbstractOperator {

        /**
         * Создает объект типа {@link BetweenExpression}, позволяющий выполнить
         * сравнение в интервале значений, определяемых операндами.
         * @return объект типа {@link BetweenExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            return new BetweenExpression(context, this, op1, op2);
        }

        /**
         * Возвращает значение приоритета оператора сравнения в интервале равное 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * Устанавливает значение {@link #lexType} равным <code>LexConst.BETWEEN</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.BETWEEN;
        }

        /**
         * @return строку знаков, содержащую значение <pre>"BETWEEN"</pre>.
         */
        @Override
        public String toString() {
            return "BETWEEN";
        }
    }//class BetweenOperator

    /**
     * Соответствует оператору сравнения <pre>"<>"</pre> - "не равно".<p>
     * Имеет приоритет равный 60.
     * Операция бинарная.
     */
    public static class NotEqualsOperator extends AbstractOperator {

        /**
         * Создает объект типа {@link NotEqualsExpression}, позволяющий выполнить
         * сравнение на не равно значений, определяемых операндами.
         * Может зарегистрировать ошибку, добавив ее к списку ошибок контекста
         * с кодом {@link tdo.expr.ExpressionContext#OPERANDTYPE.
         * @return объект типа {@link NotEqualsExpression}.
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
         * Возвращает значение приоритета оператора сравнения равное 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * Устанавливает значение {@link #lexType} равным <code>LexConst.NOTEQ</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.NOTEQ;
        }

        /**
         * @return строку знаков, содержащую значение <pre>"<>"</pre>.
         */
        @Override
        public String toString() {
            return "<>";
        }
    }//class NotEqualsOperator

    /**
     * Соответствует оператору сравнения <pre>"IS NULL"</pre> - "равно null".<p>
     * Имеет приоритет равный 60.
     * Операция бинарная.
     */
    public static class IsNullOperator extends AbstractOperator {

        /**
         * Создает объект типа {@link IsNullExpression}, позволяющий выполнить
         * сравнение на равно <code>null</code> значений, определяемых операндами.
         * @return объект типа {@link IsNullExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            return new IsNullExpression(context, this, op1, op2);
        }

        /**
         * Возвращает значение приоритета оператора сравнения равное 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * Устанавливает значение {@link #lexType} равным <code>LexConst.ISNULL</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.ISNULL;
        }

        /**
         * @return строку знаков, содержащую значение <pre>"IS"</pre>.
         */
        @Override
        public String toString() {
            return "IS";
        }
    }//class IsNullOperator

    /**
     * Соответствует оператору сравнения <pre>"IS NOT NULL"</pre> - "не равно null".<p>
     * Имеет приоритет равный 60.
     * Операция бинарная.
     */
    public static class IsNotNullOperator extends AbstractOperator {

        /**
         * Создает объект типа {@link IsNotNullExpression}, позволяющий выполнить
         * сравнение на НЕ равно <code>null</code> значений, определяемых операндами.
         * @return объект типа {@link IsNotNullExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            return new IsNotNullExpression(context, this, op1, op2);
        }

        /**
         * Возвращает значение приоритета оператора сравнения равное 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * Устанавливает значение {@link #lexType} равным <code>LexConst.ISNOTNULL</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.ISNOTNULL;
        }

        /**
         * @return строку знаков, содержащую значение <pre>"IS NOT"</pre>.
         */
        @Override
        public String toString() {
            return "IS NOT";
        }
    }//class IsNotNullOperator

    /**
     * Соответствует оператору сравнения <pre>"CANTAINING"</pre> .<p>
     * Имеет приоритет равный 60.
     * Операция бинарная.
     */
    public static class ContainingOperator extends AbstractOperator {

        /**
         * Создает объект типа {@link ContainingExpression}, позволяющий выполнить
         * проверку на наличие заданной во втором операнде строки как подстроки первого операнда.
         * @return объект типа {@link ContainingExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            return new ContainingExpression(context, this, op1, op2);
        }

        /**
         * Возвращает значение приоритета оператора сравнения равное 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * Устанавливает значение {@link #lexType} равным <code>LexConst.CONTAINING</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.CONTAINING;
        }

        /**
         * @return строку знаков, содержащую значение <pre>"CONTAINING"</pre>.
         */
        @Override
        public String toString() {
            return "CONTAINING";
        }
    }//class ContainingOperator

    /**
     * Соответствует оператору сравнения <pre>"NOT CANTAINING"</pre> .<p>
     * Имеет приоритет равный 60.
     * Операция бинарная.
     */
    public static class NotContainingOperator extends AbstractOperator {

        /**
         * Создает объект типа {@link NotContainingExpression}, позволяющий выполнить
         * проверку на отсутствие заданной во втором операнде строки как подстроки первого 
         * операнда.
         * @return объект типа {@link NotContainingExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            return new NotContainingExpression(context, this, op1, op2);
        }

        /**
         * Возвращает значение приоритета оператора сравнения равное 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * Устанавливает значение {@link #lexType} равным <code>LexConst.NOTCONTAINING</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.NOTCONTAINING;
        }

        /**
         * @return строку знаков, содержащую значение <pre>"NOT CONTAINING"</pre>.
         */
        @Override
        public String toString() {
            return "NOT CONTAINING";
        }
    }//class ContainingOperator

    /**
     * Соответствует оператору сравнения <pre>"STARTING WITH"</pre> .<p>
     * Имеет приоритет равный 60.
     * Операция бинарная.
     */
    public static class StartingWithOperator extends AbstractOperator {

        /**
         * Создает объект типа {@link StartingWithExpression}, позволяющий выполнить
         * проверку начинается ли строка, определяемая первым опрерандом 
         * с подстроки заданной  вторым операндома.
         * @return объект типа {@link StartingWithExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            return new StartingWithExpression(context, this, op1, op2);
        }

        /**
         * Возвращает значение приоритета оператора сравнения равное 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * Устанавливает значение {@link #lexType} равным <code>LexConst.STARTINGWITH</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.STARTINGWITH;
        }

        /**
         * @return строку знаков, содержащую значение <pre>"STARTING"</pre>.
         */
        @Override
        public String toString() {
            return "STARTING";
        }
    }//class StaringWithOperator

    /**
     * Соответствует оператору сравнения <pre>"NOT STARTING WITH"</pre> .<p>
     * Имеет приоритет равный 60.
     * Операция бинарная.
     */
    public static class NotStartingWithOperator extends AbstractOperator {

        /**
         * Создает объект типа {@link NotStartingWithExpression}, позволяющий выполнить
         * проверку на несовпадение первых символов строки, заданной первым операндрм
         * с подстрокой, заданной вторым оперендом.
         * @return объект типа {@link NotStartingWithExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            return new NotStartingWithExpression(context, this, op1, op2);
        }

        /**
         * Возвращает значение приоритета оператора сравнения равное 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * Устанавливает значение {@link #lexType} равным <code>LexConst.NOTSTARTINGWITH</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.NOTSTARTINGWITH;
        }

        /**
         * @return строку знаков, содержащую значение <pre>"NOT STARTING"</pre>.
         */
        @Override
        public String toString() {
            return "NOT STARTING";
        }
    }//class NotStaringWithOperator

    // *********************************************************************
    /**
     * Соответствует логическомму оператору  <pre>"AND"</pre> .<p>
     * Имеет приоритет равный 40.
     * Операция бинарная.
     */
    public static class AndOperator extends AbstractOperator {

        /**
         * Создает объект типа {@link AndExpression}, позволяющий выполнить
         * проверку двух операндов, используя логику "И".
         * Может зарегистрировать ошибку, добавив ее к списку ошибок контекста
         * с кодом {@link tdo.expr.ExpressionContext#OPERANDTYPE.
         * @return объект типа {@link AndExpression}.
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
         * Возвращает значение приоритета оператора сравнения равное 40.
         */
        @Override
        public int getPriority() {
            return 40;
        }

        /**
         * Устанавливает значение {@link #lexType} равным <code>LexConst.AND</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.AND;
        }

        /**
         * @return строку знаков, содержащую значение <pre>"AND"</pre>.
         */
        @Override
        public String toString() {
            return "AND";
        }
    }//class AndOperator

    /**
     * Соответствует оператору  <pre>"AND"</pre>, входящему в состав оператора <code>BETWEEN</code> .<p>
     * Имеет приоритет равный 70.
     * Операция бинарная.
     */
    public static class AndBetweenOperator extends AbstractOperator {

        /**
         * Создает объект типа {@link AndBetweenExpression}, позволяющий создать
         * из двух операндов второй оператнд для оператора <code>BETWEEN</code>.
         * @return объект типа {@link AndBetweenExpression}.
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
         * Возвращает значение приоритета оператора сравнения равное 70.
         */
        @Override
        public int getPriority() {
            return 70;
        }

        /**
         * Устанавливает значение {@link #lexType} равным <code>LexConst.ANDBETWEEN</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.ANDBETWEEN;
        }

        /**
         * @return строку знаков, содержащую значение <pre>"AND"</pre>.
         */
        @Override
        public String toString() {
            return "AND";
        }
    }//class AndBetweenOperator

    /**
     * Соответствует логическомму оператору  <pre>"OR"</pre> .<p>
     * Имеет приоритет равный 30.
     * Операция бинарная.
     */
    public static class OrOperator extends AbstractOperator {

        /**
         * Создает объект типа {@link OrExpression}, позволяющий выполнить
         * проверку двух операндов, используя логику "ИЛИ".
         * Может зарегистрировать ошибку, добавив ее к списку ошибок контекста
         * с кодом {@link tdo.expr.ExpressionContext#OPERANDTYPE.
         * @return объект типа {@link OrExpression}.
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
         * Возвращает значение приоритета оператора сравнения равное 30.
         */
        @Override
        public int getPriority() {
            return 30;
        }

        /**
         * Устанавливает значение {@link #lexType} равным <code>LexConst.OR</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.OR;
        }

        /**
         * @return строку знаков, содержащую значение <pre>"OR"</pre>.
         */
        @Override
        public String toString() {
            return "OR";
        }
    }//class OrOperator

    /**
     * Соответствует логическомму оператору отрицания <pre>"NOT"</pre> .<p>
     * Имеет приоритет равный 50.
     * Операция унарная.
     */
    public static class NotOperator extends AbstractOperator {

        /**
         * Создает объект типа {@link NotExpression}, позволяющий выполнить
         * операцию логического отрицания над операндом.
         * @param op опреранд 
         * @return объект типа {@link NotExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op) {
            super.createExpression(context, op);
            AbstractExpression expr = new NotExpression(op);
            expr.setContext(context);
            return expr;
        }

        /**
         * Возвращает значение приоритета оператора сравнения равное 50.
         */
        @Override
        public int getPriority() {
            return 50;
        }

        /**
         * @return true указывающий, что данный оператор является <i>унарным</i>.
         */
        @Override
        public boolean isUnary() {
            return true;
        }

        /**
         * Устанавливает значение {@link #lexType} равным <code>LexConst.NOT</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.NOT;
        }

        /**
         * @return строку знаков, содержащую значение <pre>"NOT"</pre>.
         */
        @Override
        public String toString() {
            return "NOT";
        }
    }//class NotOperator

    /**
     * Соответствует  оператору сравнения <pre>"LIKE"</pre> - проверка на соответствие шаблону.<p>
     * Имеет приоритет равный 60.
     * Операция бинарная.
     */
    public static class LikeOperator extends AbstractOperator {

        /**
         * Создает объект типа {@link LikeExpression}, позволяющий выполнить
         * операцию сравнения первого операнда на соответствие шаблону, определяемому вторым операндом.
         * @param op1 первый, проверяемый операнд
         * @param op2 второй опреранд - шаблон (wildcard).
         * @return объект типа {@link LikeExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            return new LikeExpression(context, this, op1, op2);
        }

        /**
         * Возвращает значение приоритета оператора сравнения равное 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * Устанавливает значение {@link #lexType} равным <code>LexConst.LIKE</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.LIKE;
        }

        /**
         * @return строку знаков, содержащую значение <pre>"LIKE"</pre>.
         */
        @Override
        public String toString() {
            return "LIKE";
        }
    }//class LikeOperator

    /**
     * Соответствует  оператору сравнения <pre>"NOT LIKE"</pre> - проверка на НЕсоответствие шаблону.<p>
     * Имеет приоритет равный 60.
     * Операция бинарная.
     */
    public static class NotLikeOperator extends AbstractOperator {

        /**
         * Создает объект типа {@link NotLikeExpression}, позволяющий выполнить
         * операцию сравнения первого операнда на НЕсоответствие шаблону, определяемому вторым операндом.
         * @param op1 первый, проверяемый операнд
         * @param op2 второй опреранд - шаблон (wildcard).
         * @return объект типа {@link NotLikeExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            return new NotLikeExpression(context, this, op1, op2);
        }

        /**
         * Возвращает значение приоритета оператора сравнения равное 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * Устанавливает значение {@link #lexType} равным <code>LexConst.NOTLIKE</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.NOTLIKE;
        }

        /**
         * @return строку знаков, содержащую значение <pre>"NOT LIKE"</pre>.
         */
        @Override
        public String toString() {
            return "NOT LIKE";
        }
    }//class NotLikeOperator

    /**
     * Соответствует  оператору сравнения <pre>"REGEX"</pre> - проверка на соответствие регулярному выражению.<p>
     * Имеет приоритет равный 60.
     * Операция бинарная.
     */
    public static class RegExOperator extends AbstractOperator {

        /**
         * Создает объект типа {@link RegExExpression}, позволяющий выполнить
         * операцию сравнения первого операнда на соответствие регулярному выражению,
         * определяемому вторым операндом.
         * @param op1 первый, проверяемый операнд
         * @param op2 второй опреранд - регулярное выражение.
         * @return объект типа {@link RegExExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            return new RegExExpression(context, this, op1, op2);
        }

        /**
         * Возвращает значение приоритета оператора сравнения равное 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * Устанавливает значение {@link #lexType} равным <code>LexConst.REGEX</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.REGEX;
        }

        /**
         * @return строку знаков, содержащую значение <pre>"REGEX"</pre>.
         */
        @Override
        public String toString() {
            return "REGEX";
        }
    }//class RegExOperator

    /**
     * Соответствует  оператору сравнения <pre>"NOT REGEX"</pre> - проверка на НЕсоответствие 
     * регулярному выражению.<p>
     * Имеет приоритет равный 60.
     * Операция бинарная.
     */
    public static class NotRegExOperator extends AbstractOperator {

        /**
         * Создает объект типа {@link NotRegExExpression}, позволяющий выполнить
         * операцию сравнения первого операнда на НЕсоответствие регулярному выражению,
         * определяемому вторым операндом.
         * @param op1 первый, проверяемый операнд
         * @param op2 второй опреранд - регулярное выражение.
         * @return объект типа {@link RegExExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            return new NotRegExExpression(context, this, op1, op2);
        }

        /**
         * Возвращает значение приоритета оператора сравнения равное 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * Устанавливает значение {@link #lexType} равным <code>LexConst.NOTREGEX</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.NOTREGEX;
        }

        /**
         * @return строку знаков, содержащую значение <pre>"NOT REGEX"</pre>.
         */
        @Override
        public String toString() {
            return "NOT REGEX";
        }
    }//class NotRegExOperator

    /**
     * Соответствует  оператору  <pre>","</pre> - запятая. 
     * Имеет приоритет равный 11.
     * Операция бинарная.
     */
    public static class CommaOperator extends AbstractOperator {

        /**
         * Создает объект типа {@link CommaListExpression}.
         * Если первый операнд является экземпляром <code>CommaListExpressio</code>, 
         * то второй операнд добавляется к коллекции, представленной первым операндом.
         * В противном случае, создается новый пустой экземпляр <code>CommaListExpression</code>
         * и к нему последовательно добавляется первый и второй операнд. 
         * @param op1 первый операнд
         * @param op2 второй опреранд
         * @return объект типа {@link CommaListExpression}.
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
         * Возвращает значение приоритета оператора сравнения равное 11.
         */
        @Override
        public int getPriority() {
            return 11;
        }

        /**
         * Устанавливает значение {@link #lexType} равным <code>LexConst.COMMA</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.COMMA;
        }

        /**
         * @return строку знаков, содержащую значение <pre>","</pre>.
         */
        @Override
        public String toString() {
            return ",";
        }
    }//class OrOperator

    /**
     * Соответствует  оператору сравнения <pre>"IN"</pre> - проверка на наличие значения в списке.
     *  <p>
     * Имеет приоритет равный 60.
     * Операция бинарная.
     */
    public static class InOperator extends AbstractOperator {

        /**
         * Создает объект типа {@link InExpression}, позволяющему проверить наличие значения,
         * определяемого первым операндом в списке значений, заданным вторым операндом.
         * @param op1 первый операнд
         * @param op2 второй опреранд
         * @return объект типа {@link InExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            return new InExpression(context, this, op1, op2);
        }

        /**
         * Возвращает значение приоритета оператора сравнения равное 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * Устанавливает значение {@link #lexType} равным <code>LexConst.IN</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.IN;
        }

        /**
         * @return строку знаков, содержащую значение <pre>"IN"</pre>.
         */
        @Override
        public String toString() {
            return "IN";
        }
    }//class InOperator

    /**
     * Соответствует  оператору сравнения <pre>"NOT IN"</pre> - проверка на отсутствие значения в списке.
     *  <p>
     * Имеет приоритет равный 60.
     * Операция бинарная.
     */
    public static class NotInOperator extends AbstractOperator {

        /**
         * Создает объект типа {@link NotInExpression}, позволяющему проверить отсутствие значения,
         * определяемого первым операндом в списке значений, заданным вторым операндом.
         * @param op1 первый операнд
         * @param op2 второй опреранд
         * @return объект типа {@link NotInExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            return new NotInExpression(context, this, op1, op2);
        }

        /**
         * Возвращает значение приоритета оператора сравнения равное 60.
         */
        @Override
        public int getPriority() {
            return 60;
        }

        /**
         * Устанавливает значение {@link #lexType} равным <code>LexConst.NOTIN</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.NOTIN;
        }

        /**
         * @return строку знаков, содержащую значение <pre>"NOT IN"</pre>.
         */
        @Override
        public String toString() {
            return "NOT IN";
        }
    }//class NotInOperator

    /**
     * Соответствует  оператору <pre>"AS"</pre> - назначение имени колонки выражению.<p>
     * Имеет приоритет равный 12.
     * Оператор применяется при создании списков колонок.
     * Операция бинарная.
     */
    public static class AsOperator extends AbstractOperator {

        /**
         * Создает объект типа {@link ASExpression}, позволяющий выполнить
         * назначение имени выражению.
         * @param op1 первый операнд
         * @param op2 второй опреранд - назначаемое имя.
         * @return объект типа {@link ASExpression}.
         */
        @Override
        public IExpression createExpression(ExpressionContext context, IOperand op1, IOperand op2) {
            super.createExpression(context, op1, op2);
            return new ASExpression(context, this, op1, op2);
        }

        /**
         * Возвращает значение приоритета оператора сравнения равное 12.
         */
        @Override
        public int getPriority() {
            return 12;
        }

        /**
         * Устанавливает значение {@link #lexType} равным <code>LexConst.AS</code>.
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.AS;
        }

        /**
         * @return строку знаков, содержащую значение <pre>"AS"</pre>.
         */
        @Override
        public String toString() {
            return "AS";
        }
    }//class RegExOperator
}//class AbstractOperator
