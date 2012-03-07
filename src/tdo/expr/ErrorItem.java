/*
 * ErrorItem.java
 */
package tdo.expr;

import static tdo.expr.ExpressionContext.*;
/**
 * ������������ ���� ������� ���������� �� ������� � ���������. <p>
 * ������, ������������ �� ������ ������ ��������� ��������� ����������� ��
 * ���������� ������ ������� ������ {@link tdo.DefaultExpressionContext}, �������
 * ������������ ����� ��������� ��������� ������� ������.
 * <p>� ����������� �� ����������� ������ � ����� ��������� ���������, ����������
 * ��������������� � ��������� �������� �����������. ����� {@link #getMessage() }
 * ��������� �� ���� ������ ���������� �� ��������� ���������� � ���� ������
 * ������.
 *
 */
public class ErrorItem {

    public int code;
    public IExpression expr;
    public String errMessage;
    /**
     * ������� ��������� ������ ��� ��������� ���� ������ � ���������.<p>
     *
     * @param code ��� ������, ��� ���������� ����������� ������
     * <code>tdo.expr.ExpressionContext</code>.
     * @param expr ���������, ��������� ������
     * @see tdo.expr.ExpressionContext
     * @see tdo.expr.IExpression
     */
    public ErrorItem(int code, IExpression expr) {
        this.code = code;
        this.expr = expr;
    }
    /**
     * ������� ��������� ������ ��� ��������� ���� ������, ��������� �
     * ���������� ���������.<p>
     *
     * @param code ��� ������, ��� ���������� ����������� ������
     * <code>tdo.expr.ExpressionContext</code>.
     * @param expr ���������, ��������� ������
     * @param msg ��������� ��������� ����������� ���������� �� ������
     * @see tdo.expr.ExpressionContext
     * @see tdo.expr.IExpression
     */
    public ErrorItem(int code, IExpression expr, String msg) {
        this(code, expr);
        this.errMessage = msg;
    }

    /**
     * ����������� ��� ������, ��������� ��������� ��������� � ���������� ���.
     * @return ��������� ��������� � ������� � ����� ����������� ������
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
