/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo;



import tdo.expr.IExpression;


/**
 * ��������� ���������������� ������ {@link DefaultExpressionContext} ��� 
 * ������������� ��� ����������� ��������.<p>
 * ��� ������ {@link #getValue(tdo.DataRow) } � {@link #getExpression() }
 * ������� ��� �������� ��������� ��������� ����������� ������� �������.<p>
 */

public class ColumnExpressionContext extends DefaultExpressionContext{    
   private IExpression expression;

   /**
    * ������� ��������� ������ � ������������� ��������� �������� ���������.
    * @param expr �������� ��������� � ���� ������ ������
    */
   public ColumnExpressionContext(String expr) {
        super(expr);

   }
   /**
    * ������� ��������� ������ � ������������� ��������� �������� ��������� �
    * ����� ������������.
    * @param expr �������� ��������� � ���� ������ ������
    * @param testMode true - �������� ����� ������������. false - ���������
    *   ����� ������������
    */
   public ColumnExpressionContext(String expr, boolean testMode) {
        super(expr,testMode);
   }
   /**
    * ���������� �������� ������������ ��������� ��� ��������� ����.
    * @param row ���, ������� �������� ������������ ��� ����������
    * @return ����������� �������� ���������
    */
   public Object getValue(DataRow row) {
        return getExpression().getValue(row);
   }
   /**
    * ���������� ���������������� ���������.
    * ���� ��������� ��� �� �������������, �� ����������� �����
    * {@link tdo.DefaultExpressionContext#createExpression() } . ����� 
    * ������������ ����� ��������� ���������
    * @return ������ <code>IExpression</code> ��� ��������� ���������� ���������
    *   ���������
    */
   public IExpression getExpression() {
       if ( this.expression == null ) 
          this.expression = this.createExpression();           
       return this.expression;
   }
}//class ColumnExpressionContext
