/*
 * RowExpressionContext.java
 * 
 */

package tdo.expr;


import tdo.DataRow;
import tdo.DefaultExpressionContext;

/**
 * ��������� ���������������� ������ {@link tdo.DefaultExpressionContext},
 * �������� ��������� {@link tdo.expr.RowEvaluator}.
 * <p>������������, ��� �������, � ����������-�������� ������ {@link tdo.Table}
 * � �����������.
 *
 */
public class RowExpressionContext extends DefaultExpressionContext implements RowEvaluator{

    private IExpression expression;
    /**
     * ������� �������� ��������� ��� ��������� ���������� ���������.
     * @param expr �������� ��������� ���������
     */
    public RowExpressionContext(String expr) {
        super(expr);
    }

    /**
     * ��������� ���������� ��������� ���� ������� � ��������� ������ ����������
     * ��� ������� ��������.<p>
     * �������, ���� ��� �� ������, ������ ���� {@link tdo.expr.IExpression}
     * � ��������� ��� ���� ����� {@link tdo.expr.IExpression#getValue(tdo.NamedValues)  }
     *
     * @param row ����������� ���
     * @return �������� ��������, ��� ��������� ������ ����
     * @see #evaluate(tdo.DataRow[])
     * @see tdo.Table
     * @see tdo.impl.Filterer
     */
    @Override
    public boolean evaluate(DataRow row) {
        if ( this.expression == null )
            expression = this.createExpression();
        return ((Boolean)expression.getValue(row)).booleanValue();
    }
    /**
     * ��������� ���������� ��������� ������� ����� ������� � ��������� ������ ����������
     * ��� ������� ��������.<p>
     * �������, ���� ��� �� ������, ������ ���� {@link tdo.expr.IExpression}
     * � ��������� ��� ���� ����� {@link tdo.expr.IExpression#getValue(tdo.NamedValues[])  }
     *
     * @param rows ������ ����������� �����
     * @return �������� ��������, ��� ��������� ������ ����
     * @see #evaluate(tdo.DataRow)
     * @see tdo.Table
     * @see tdo.impl.Joiner
     */
    public boolean evaluate(DataRow[] rows) {
        if ( this.expression == null )
            expression = this.createExpression();
        return ((Boolean)expression.getValue(rows)).booleanValue();
    }

}
