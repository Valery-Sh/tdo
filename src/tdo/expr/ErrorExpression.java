/*
 * ErrorExpression.java
 * 
 */

package tdo.expr;

import tdo.NamedValues;
import tdo.tools.scanner.ExpressionException;

/**
 * ѕримен€етс€, когда исходное выражение содержит ошибки, но выбрасывать
 * исключительную ситуацию не желательно.
 * ѕозвол€ет все же создать выражение, обрабатываемое как ошибочное.
 *
 */
public class ErrorExpression extends AbstractExpression {
    
    protected ExpressionException exprException;
    
    public ErrorExpression() {
    }
    /**
     * »спользуетс€ в <code>tdo.DefaultExpressionContext</code> при регистрации
     * ошибок в выражении, когда ошибка обнаружена на последующем после парсинга
     * этапе обработки выражени€.<p>
     * Ќа этом этапе, как правило можно определить оператор, вызвавший ошибку
     * и операнды.
     * @param context контекст выражени€
     * @param oper оператор, при обработке которого обнаружена ошибка.
     * @param op1 первый операнд
     * @param op2 второй операнд
     */
    public ErrorExpression(ExpressionContext context, IOperator oper, IOperand op1, IOperand op2) {
        this.operator = oper;
        this.op1 = op1;
        this.op2 = op2;
        this.exprException = null;
        this.setContext(context);
    }
    /**
    /**
     * »спользуетс€ в <code>tdo.DefaultExpressionContext</code> при регистрации
     * ошибок в выражении, когда ошибка обнаружена при парсинге исходного
     * выражени€.<p>
     * ≈сли ошибка обнаружена парсером, то последний выбрасывает исключительную
     * ситуации, котора€ перехватываетс€ и добавл€етс€ в список ошибок контекста.
     * ћетоды <code>getOperator, getOp1 и getOp2</code> в данном случае
     * возвращают <code>null</code>. ѕричина ошибки доступна методом
     * {@link #getExprException() }
     * @param context контекст выражени€
     * @param exception выброшенное парсером исключение
     */
    public ErrorExpression(ExpressionContext context, ExpressionException exception ) {
        this.operator = null;
        this.op1 = null;
        this.op2 = null;
        this.exprException = exception;       
        this.setContext(context);
    }
    /**
     * @return ¬озвращает объект иискючени€, возникшего при парсинге исходного
     *  выражени€.
     */
    public ExpressionException getExprException() {
        return this.exprException;
    }
    /**
     * ѕри попытке вызвать метод возникает исключительна€ ситуаци€.
     * @param row
     * @return
     * @throws java.lang.UnsupportedOperationException
     */
    @Override
    public Object getValue(NamedValues[] row) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return {@link tdo.expr.ErrorExpression#getClass() }
     */
    @Override
    public Class getType() {
        return ErrorExpression.class;
    }
    /**
     * ¬озврщает результат выполнени€ метода суперкласса, если значение свойства
     * <code>exprException</code> равно <code>null</code>. ¬ противном случае,
     * возвращает исходное строковое выражение.
     * @return строку или подстроку выражени€, содержащего ошибку.
     * @see #getExprException()
     */
    @Override
    public String toString() {
        if ( this.exprException == null )
            return super.toString();
        String es = this.getContext().getExpressionString();
        return es;
    }
}//class ErrorExpression
