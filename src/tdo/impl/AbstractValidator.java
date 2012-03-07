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
 * ������� ����� ��� �������� ����������� ���� ��� �������.
 * <p>��������������, ��� ��� ��������� ���� ������������ ����� 
 * {@link #validate(tdo.DataRow) } , 
 * � ��� ��������� ������� - {@link #validate(tdo.DataRow, java.lang.Object) }.
 */
public abstract class AbstractValidator implements Validator {

    protected TableServices tableServices;

    private String message;
    private ExpressionContext expressionContext;
    private String expression;
    private IExpression iexpression;

    /**
     * ���������� �������� ��������� ��� ���������, ������������� ��� �������
     * ���������.
     * @return �������� ��������� ���������
     */
    @Override
    public ExpressionContext getExpressionContext() {
        return this.expressionContext;
    }
    /**
     * ��������� �������� ��� ������� �� ����������.
     * @param row ����������� ���
     * @return <code>true</code> , � ������, ���� �������� ������ �������. <code>
     *  false</code> - � ��������� ������.
     * @see #validate(tdo.DataRow, java.lang.Object)
     */
    @Override
    public boolean validate(DataRow row) {
        return validateExpr(row);
    }
   /**
     * ��������� �������� ��� ������� �� ����������.
     * <p>���������� ���������� ������  {@link #validate(tdo.DataRow)
     * @param row ����������� ���
     * @return <code>true</code> , � ������, ���� �������� ������ �������. <code>
     *  false</code> - � ��������� ������.
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
     * ���������� ������, ���������� ������� ��������� ������ � ������� ��
     * �������������.<p>
     * message ����� �������� � ���� ��� ��������� ����������� ������������������
     * ��������.
     * <ul>
     * <li>#i  - ��� ��������� ���������� �������� ������� ���� Validator ��
     * ������ ����������� ����.</li>
     * <li>#ri  - ��� ��������� ���������� �������� ����, ��� ��������
     * ����������� ���������.</li>
     * <li>#t  - ��������� �������� ����� �������, ���������� �������
     *     <code>table.getTableName()</code>.
     * </li>
     * <li>#e � ���� ��� ���������� ���������� ���������� ����� �� ���������
     * ��������� ���������, �� ��������� ���������� �� ����� ���������.
     * </li>
     * </ul>
     * @return ���������� �� ������
     * @see #setMessage(java.lang.String)
     * @see tdo.impl.ValidationManager
     */
    @Override
    public String getMessage() {
        return this.message;
    }
    /**
     * ������������� ���������� ��������� ������� ��������� ������ � ������� ��
     * �������������.<p>
     * message ����� �������� � ���� ��� ��������� ����������� ������������������
     * ��������.
     * <ul>
     * <li>#i  - ��� ��������� ���������� �������� ������� ���� Validator ��
     * ������ ����������� ����.</li>
     * <li>#ri  - ��� ��������� ���������� �������� ����, ��� ��������
     * ����������� ���������.</li>
     * <li>#t  - ��������� �������� ����� �������, ���������� �������
     *     <code>table.getTableName()</code>.
     * </li>
     * <li>#e � ���� ��� ���������� ���������� ���������� ����� �� ���������
     * ��������� ���������, �� ��������� ���������� �� ����� ���������.
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
