/**
 * IOperator.java
 * 
 * Created on 14.06.2007, 18:52:06 vvvv
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.expr;

/**
 * Представляет собой основу для различных классов операторов выражения.<p>
 * Методы интерфейса позволяют строить объекты {@link IExpression} по заданным
 * значениям операндов.
 * @see IOperand
 * @see AbstractOperator
 * @see FunctionOperator
 * 
 */
public interface IOperator extends IToken {
    void   setContext(ExpressionContext context);
    /**
     *  Строит объект <code>IExpression</code> для заданного операнда.
     * Эта форма перегруженного метода обычно используется унарными опереаторами,
     * т.е. операторами имеющими только один операнд. <p>
     *  Поскольку интерфейс <code>IExpression</code> является наследником <code>IOperand</code>,
     * то любое выражение также может быть использовано как операнд.
     * 
     * @param context 
     * @param op1 операнд, используемый при построении выражения
     * @return объектное представление выражения
     * @see IExpression
     * @see #createExpression(IOperand,IOperand)
     */
    IExpression createExpression(ExpressionContext context,IOperand op1); 
    /**
     *  Строит объект <code>IExpression</code> для заданных операндов.
     * Эта форма перегруженного метода обычно используется бинарными опереаторами,
     * т.е. операторами имеющими только два операнда.<p>
     *  Поскольку интерфейс <code>IExpression</code> является наследником <code>IOperand</code>,
     * то любое выражение также может быть использовано как операнд.

     * @param context 
     * @param op1 первый операнд, используемый при построении выражения
     * @param op2 второй операнд, используемый при построении выражения
     * @return объектное представление выражения
     * @see IExpression
     * @see #createExpression(IOperand)
     */
    IExpression createExpression(ExpressionContext context,IOperand op1, IOperand op2);    
    /**
     * 
     * @return значение <code>true</code>, если класс объекта, реализующего интерфейс
     *      является унарным оператором. <code>false</code> - в противном случае.
     */
    boolean isUnary();
}
