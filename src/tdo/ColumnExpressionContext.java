/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo;



import tdo.expr.IExpression;


/**
 * Расширяет функциональность класса {@link DefaultExpressionContext} для 
 * использования его вычисляемых колонках.<p>
 * два метода {@link #getValue(tdo.DataRow) } и {@link #getExpression() }
 * введены для удобства обработки выражений вычисляемых колонок таблицы.<p>
 */

public class ColumnExpressionContext extends DefaultExpressionContext{    
   private IExpression expression;

   /**
    * Создает экземпляр класса и устанавливает строковое значение выражения.
    * @param expr значение выражения в виде строки знаков
    */
   public ColumnExpressionContext(String expr) {
        super(expr);

   }
   /**
    * Создает экземпляр класса и устанавливает строковое значение выражения и
    * режим тестирования.
    * @param expr значение выражения в виде строки знаков
    * @param testMode true - включает режим тестирования. false - отключает
    *   режим тестирования
    */
   public ColumnExpressionContext(String expr, boolean testMode) {
        super(expr,testMode);
   }
   /**
    * Возвращает значение вычисленного выражения для заданного ряда.
    * @param row ряд, колонки которого используются при вычислении
    * @return вычисленное значение выражения
    */
   public Object getValue(DataRow row) {
        return getExpression().getValue(row);
   }
   /**
    * Возвращает скомпилированное выражение.
    * Если выражение еще не компилировано, то выполняется метод
    * {@link tdo.DefaultExpressionContext#createExpression() } . Иначе 
    * возвращается ранее созданное выражение
    * @return объект <code>IExpression</code> как результат компиляции исходного
    *   выражения
    */
   public IExpression getExpression() {
       if ( this.expression == null ) 
          this.expression = this.createExpression();           
       return this.expression;
   }
}//class ColumnExpressionContext
