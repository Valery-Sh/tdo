/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.impl;

import tdo.DataRow;
import tdo.DefaultExpressionContext;
import tdo.expr.ExpressionContext;
import tdo.expr.IExpression;
import tdo.service.TableServices;

/**
 * Базовый класс для создания валидаторов ряда или колонки.
 * <p>Предполагается, что для валидации ряда используется метод 
 * {@link #validate(tdo.DataRow) } , 
 * а для валидации колонки - {@link #validate(tdo.DataRow, java.lang.Object) }.
 */
public abstract class AbstractValidator implements Validator {

    protected TableServices tableServices;

    private String message;
    private ExpressionContext expressionContext;
    private String expression;
    private IExpression iexpression;

    /**
     * Возвращает контекст выражения для выражения, используемого как условие
     * валидации.
     * @return контекст выражения валидации
     */
    @Override
    public ExpressionContext getExpressionContext() {
        return this.expressionContext;
    }
    /**
     * Проверяет заданный ряд таблицы на валидность.
     * @param row проверяемый ряд
     * @return <code>true</code> , в случае, если проверка прошла успешно. <code>
     *  false</code> - в противном случае.
     * @see #validate(tdo.DataRow, java.lang.Object)
     */
    @Override
    public boolean validate(DataRow row) {
        return validateExpr(row);
    }
   /**
     * Проверяет заданный ряд таблицы на валидность.
     * <p>Делегирует исполнение методу  {@link #validate(tdo.DataRow)
     * @param row проверяемый ряд
     * @return <code>true</code> , в случае, если проверка прошла успешно. <code>
     *  false</code> - в противном случае.
     * @see #validate(tdo.DataRow)
     */
    @Override
    public boolean validate(DataRow row, Object value) {
        return validateExpr(row,value);
    }

    private boolean eval(DataRow row) {
        if ( this.iexpression == null )
            iexpression = this.expressionContext.createExpression();
        return ((Boolean)iexpression.getValue(row)).booleanValue();
    }

    private boolean validateExpr(DataRow row) {
        if ( this.expressionContext != null  )
            return eval(row);

        return true;

    }
    private boolean validateExpr(DataRow row, Object value) {
        if ( this.expressionContext != null  ) {
            expressionContext.setParameter("value", value);
            return eval(row);
        }

        return true;

    }
    /**
     * Возвращает строку, содержимое которой описывает ошибку и причину ее
     * возникновения.<p>
     * message может включать в себя как подстроки специальные последовательности
     * символов.
     * <ul>
     * <li>#i  - эта подстрока заменяется индексом объекта типа Validator из
     * списка валидаторов ряда.</li>
     * <li>#ri  - эта подстрока заменяется индексом ряда, для которого
     * выполняется валидация.</li>
     * <li>#t  - строковое значение имени таблицы, получаемое вызовом
     *     <code>table.getTableName()</code>.
     * </li>
     * <li>#e — если для регистации валидатора применялся метод со строковым
     * значением выражения, то подстрока заменяется на текст выражения.
     * </li>
     * </ul>
     * @return сообщшение об ошибке
     * @see #setMessage(java.lang.String)
     * @see tdo.impl.ValidationManager
     */
    @Override
    public String getMessage() {
        return this.message;
    }
    /**
     * Устанавливает содержимое сообщения которое описывает ошибку и причину ее
     * возникновения.<p>
     * message может включать в себя как подстроки специальные последовательности
     * символов.
     * <ul>
     * <li>#i  - эта подстрока заменяется индексом объекта типа Validator из
     * списка валидаторов ряда.</li>
     * <li>#ri  - эта подстрока заменяется индексом ряда, для которого
     * выполняется валидация.</li>
     * <li>#t  - строковое значение имени таблицы, получаемое вызовом
     *     <code>table.getTableName()</code>.
     * </li>
     * <li>#e — если для регистации валидатора применялся метод со строковым
     * значением выражения, то подстрока заменяется на текст выражения.
     * </li>
     * </ul>
     *
     * @see #getMessage()
     * @see tdo.impl.ValidationManager
     */
    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    public String getExpression() {
        return this.expression;
    }
    public void setExpression( String expression ) {
        this.expression = expression;
        if ( expression != null ) {
            //this.predicate = createPredicate();
            //this.predicate = this.createExprPredicate();
            this.createExpressionContext();
        }
    }

    protected void createExpressionContext() {
        expressionContext = new DefaultExpressionContext(this.getExpression());
        ((DefaultExpressionContext)expressionContext).addTableServices(tableServices);
    }

}
