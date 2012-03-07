/*
 * CompoundOperand.java
 *
 */

package tdo.expr;

import tdo.NamedValues;
import tdo.tools.expr.LexConst;

/**
 * Базовый класс для классов, экземпляры которых создаются объектами,
 * соответствующими операторам выражений.<p>
 * Например, объект класса {@link EqualsOperator} создает экземпляр 
 * {@link EqualsExpresion}. <p>
 * Класс, кроме того, что реализует методы интерфейса <code>IExpression</code>,
 * вводит новые свойства и методы доступа к ним, такие как:
 * <ul>
 *   <li>{@link #getOperator() } возвращает объект типа <code>IOperator</code></li>
 *   <li>{@link #getOp1() } возвращает объект типа <code>IOperand</code>,
 *            соответствующий первому операнду выражения</li>
 *   <li>{@link #getOp2() } возвращает объект типа <code>IOperand</code>,
 *            соответствующий второму операнду выражения</li>
 * </ul>
 * Классы-наследники обеспечивают реализацию методов <code>getValue</code>.
 * Эти методы возвращают результат вычисления выражений.<p>
 * За исключением классов-операндов, таких как <code>LiteralOperand,
 * NamedOperand, IdentifierOperand, ParameterOperand</code>, все остальные
 * наследуют от класса <code>AbstractExpression</code>.
 * 
 */
public  abstract class AbstractExpression implements IExpression{

    /**
     * Когда объект типа {@link IOperator} создает соответствующий ему 
     * объект типа <code>IExpression</code>, используя метод 
     * <code>createExpression</code>, то значение данного поля устанавливается
     * равным целочисленному типу <i>оператора</i>, как определено в 
     * {@link tdo.tools.expr.LexConst}.
     */
    public int lexType; // for test and print
    
    protected IOperator operator;
    protected IOperand op1;
    protected IOperand op2;
    
    private ExpressionContext context;

/*    public AbstractExpression() {
    }
 */
    /**
     * Устанавливает контекст, в котором обрабатывается выражение.
     * @param context ссылка на контекст выражения
     * @see #getContext()
     * @see tdo.expr.ExpressionContext
     */
    @Override
    public void setContext(ExpressionContext context) {
        this.context = context;
    }
    /**
     * Возвращаен контекст, в котором обрабатывается выражение.
     * @return context ссылка на контекст выражения
     * @see tdo.expr.ExpressionContext
     * @see #setContext(tdo.expr.ExpressionContext)
     */
    public ExpressionContext getContext() {
        return this.context;
    }    


    protected void setLexType() {
        lexType = -2;
    }

    /**
     * Возвращает значение выражения для заданной коллекции именнованных
     * элементов.
     * Метод выбрасывает исключение при попытке его применения. Классы-наследники
     * должны переопределить его для выполнения реальных действий.
     * @param row коллекция именованных элементов, например типа
     *     <code>tdo.DataRoe</code>
     * @return значение выражения
     */
    @Override
    public Object getValue(NamedValues row) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    /**
     * Возвращает значение выражения.
     * Делегирует выполнение методу {@link #getValue(NamedValues)} со значением
     * параметра равным <code>null</code>.
     * @return значение выражения
     */
    @Override
    public Object getValue() {
        NamedValues row = null;
        return this.getValue(row);
    }
    /**
     * Возвращает оператор выражения.
     * @return объект оператора типа {@link tdo.expr.IOperator}, создавшего выражение
     */
    public IOperator getOperator() {
        return this.operator;
    }
    /**
     * Возвращает объект опрератора, соответствующий первому операнду.
     * @return объект типа {@link tdo.expr.IOperand}
     * @see #getOp2()
     */
    public IOperand getOp1() {
        return this.op1;
    }
    /**
     * Возвращает объект опрератора, соответствующий второму операнду.
     * Некоторые операторы являются <i>унарными</i> и, поэтому, для них второй
     * операнд не используется
     * @return объект типа {@link tdo.expr.IOperand}
     * @see #getOp1()
     * @see tdo.expr.IOperator#isUnary()
     */
    public IOperand getOp2() {
        return this.op2;
    }
    /**
     *
     * @return всегда -1.
     */
    @Override
    public int getPriority() {
        return -1;
    }
    
    @Override
    public String toString() {
        String s = "";
        String s1 = op1 == null ? "" : op1.toString();
        String s2 = op2 == null ? "" : op2.toString();
        if ( operator.isUnary() )
            s = "(" + operator.toString() + " " + s1 + ")";
        else
            s = "(" + s1 + " " + operator.toString() + " " + s2 + ")";
        return s;    
    }
    /**
     * Класс, обслуживающий <code>NOT</code> оператор для булевого операнда.
     * Метод <code>getType</code> возвращает <code>Boolean.class</code>.
     */
    public static class NotExpression extends AbstractExpression{
        
        private IOperand op;
        /**
         * Создает экземпляр класса для заданного операнда.
         * Поскольку операнд является унарным, то одного операнда достаточно.
         * @param op операнд, к которому применяется оператор <code>NOT</code>
         * @see tdo.expr.AbstractOperator.NotOperator
         */
        public NotExpression(IOperand op) {
            this.op = op;
        }
        /**
         * Возвращает значение выражения для заданной именованной коллекции.
         * Метод получает значение операнда как <code>boolean</code> и применяет
         * к нему логичиский java-оператор <code><b>!</b></code>.
         * 
         * @param row коллекция элементов, доступ к которым производится по
         *   имени, например, <code>tdo.DataRow</code>
         * @return значение типа <code>Boolean</code>
         * @see #getValue(tdo.NamedValues[])
         * @see tdo.expr.AbstractExpression#getValue()
         */
        @Override
        public Object getValue(NamedValues row) {
            boolean b = ((Boolean)op.getValue(row)).booleanValue();
            return new Boolean( !b ); 
        }
        /**
         * Возвращает значение выражения для заданного массива именованных коллекций.
         *
         * @param row массив коллекций элементов, доступ к которым производится по
         *   имени, например, <code>tdo.DataRow[]</code>
         * @return значение типа <code>Boolean</code>
         * @see #getValue(tdo.NamedValues)
         * @see tdo.expr.AbstractExpression#getValue()
         */
        @Override
        public Object getValue(NamedValues[] row) {
            boolean b = ((Boolean)op.getValue(row)).booleanValue();
            return new Boolean( !b ); 
        }
        /**
         * 
         * @return
         */
        public IOperand getOp() {
            return op;
        }

        @Override
        public Class getType() {
            return Boolean.class;
        }        
        @Override
        protected void setLexType() {
            lexType = LexConst.NOT;
        }
        @Override
        public String toString() {
           return "NOT (" +  op.toString() + ")";
        }
        
    }//class NotExpression
    
}//class AbstractExpression
