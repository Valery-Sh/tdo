/*
 * ErrorItem.java
 */
package tdo.expr;

import static tdo.expr.ExpressionContext.*;
/**
 * Представляет один элемент информации об ошибках в выражении. <p>
 * Ошибки, обнаруженные на разных этапах обработки выражения сохраняются во
 * внутреннем списке объекта класса {@link tdo.DefaultExpressionContext}, который
 * представляет собой коллекцию элементов данного класса.
 * <p>В зависимости от серьезности ошибки и этапа обработки выражения, информация
 * предоставляется с различной степенью детализации. Метод {@link #getMessage() }
 * позволяет по коду ошибки определить ее смысловое содержание в виде строки
 * знаков.
 *
 */
public class ErrorItem {

    public int code;
    public IExpression expr;
    public String errMessage;
    /**
     * Создает экземпляр класса для заданного кода ошибки и выражения.<p>
     *
     * @param code код ошибки, как определено константами класса
     * <code>tdo.expr.ExpressionContext</code>.
     * @param expr выражение, вызвавшее ошибку
     * @see tdo.expr.ExpressionContext
     * @see tdo.expr.IExpression
     */
    public ErrorItem(int code, IExpression expr) {
        this.code = code;
        this.expr = expr;
    }
    /**
     * Создает экземпляр класса для заданного кода ошибки, выражения и
     * текстового сообщения.<p>
     *
     * @param code код ошибки, как определено константами класса
     * <code>tdo.expr.ExpressionContext</code>.
     * @param expr выражение, вызвавшее ошибку
     * @param msg текстовое сообщение дополняющее информацию об ошибке
     * @see tdo.expr.ExpressionContext
     * @see tdo.expr.IExpression
     */
    public ErrorItem(int code, IExpression expr, String msg) {
        this(code, expr);
        this.errMessage = msg;
    }

    /**
     * Анализирует код ошибки, формирует текстовое сообщение и возвращает его.
     * @return текстовое сообщение о причине и месте обнаружения ошибки
     */
    public String getMessage() {
        String m = "";
        switch (code) {
            case EXPRPARSER:
                m = "Code=" + EXPRPARSER + " (LEX PARSER). " +
                        " Unknown token. No " + ((ErrorExpression) expr).getExprException().getSymbolNumber() + " -> '" +
                        ((ErrorExpression) expr).getContext().getExpressionString() + "'<- " +
                        " Parser code No " + ((ErrorExpression) expr).getExprException().getCode() +
                        ". " + ((ErrorExpression) expr).getExprException().getMessage();
                break;
            case ExpressionContext.OPERANDTYPE:
                m = "Code=" + OPERANDTYPE + " (Illegal Operand Type). " +
                        " ->'" + expr.toString() + "'<- " +
                        " in  Expression '" +
                        ((AbstractExpression) expr).getContext().getExpressionString() +
                        "'<-";
                break;
            case ExpressionContext.MATH_OPERANDTYPE:
                m = "Code=" + MATH_OPERANDTYPE + " (Illegal Math Operand Type). " +
                        " ->'" + expr.toString() + "'<- " +
                        " in  Expression '" +
                        ((AbstractExpression) expr).getContext().getExpressionString() +
                        "'<-";
                break;
            case ExpressionContext.MATHEXPRESSION:
                m = "Code=" + MATHEXPRESSION + " ( Math Runtime Computing ). " +
                        " ->'" + expr.toString() + "'<- " +
                        " in  Expression '" +
                        ((AbstractExpression) expr).getContext().getExpressionString() +
                        "'<- " + (errMessage == null ? "" : "MSG: " + errMessage);
                break;
            case ExpressionContext.FUNCTIONEXPRESSION:
                m = "Code=" + TESTING + " ( Function ). " +
                        " ->'" + expr.toString() + "'<- " +
                        " in  Expression '" +
                        ((AbstractExpression) expr).getContext().getExpressionString() +
                        "'<- " + (errMessage == null ? "" : "MSG: " + errMessage);
                break;

            case ExpressionContext.TESTING:
                m = "Code=" + TESTING + " ( Test Method ). " +
                        " ->'" + expr.toString() + "'<- " +
                        " in  Expression '" +
                        ((AbstractExpression) expr).getContext().getExpressionString() +
                        "'<- " + (errMessage == null ? "" : "MSG: " + errMessage);
                break;


        }//switch
        return m;
    }
} //class ErrorItem
