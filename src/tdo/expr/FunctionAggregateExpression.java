/*
 * FunctionAggregateExpression.java
 *
 * Created on 26.06.2007, 11:04:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.expr;

import tdo.ColumnListExpressionContext;
import tdo.DataColumn;
import tdo.DataRow;
import tdo.util.ExprUtil;

/**
 * Базовый класс для всех агрегатных функций.<p>
 * Аггрегатная функция имеет свойсиво {@link DataColumn}, для создания которой
 * эта функция была использована. Метод {@link #getType} возвращает тип,
 * соответствующий {@link DataColumn#getType}.
 *
 *
 */
public abstract class FunctionAggregateExpression extends FunctionExpression {

    /**
     * Сохраняет индекс колонки, создаваемый агрегатной функцией для
     * таблицы (@link ColumnExpressionContext#targetTable}.
     */
    protected int columnIndex;
    protected Object aggValue = null;
    protected String columnName;

    public FunctionAggregateExpression(ExpressionContext context, IOperator operator, IOperand op1) {
        super(context, operator, op1);
        columnIndex = -1;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

/*    public Object getAggValue(int rowIndex) {
    Object old = this.aggValue;
    getValue(rowIndex);
    return old;
    }
     */
    public Object getAggValue() {
        return this.aggValue;
    }

    public void updateInserted(DataRow insertedRow) {
    }

    public void reset(DataRow row) {
        if (row == null) {
            aggValue = null;
        } else {
            aggValue = getValue(row);
        }
    }


    public void init(DataRow row) {
        if (row == null) {
            aggValue = null;
        } else {
            aggValue = getValue(row);
        }
    }

    @Override
    public Class getType() {
        ColumnListExpressionContext context = (ColumnListExpressionContext) this.getContext();
        Class c = context.getColumnType(this);
        if ( c == null ) {
            return op1.getType();
        }
        return c;
    }


    public static class LevelAggExpression extends FunctionAggregateExpression {

        public LevelAggExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
        }
        @Override
        public void init(DataRow row) {
            aggValue = null;
        }
        @Override
        public void reset(DataRow row) {
            aggValue = null;
        }
        @Override
        public Class getType() {
            return Integer.class;
        }
/*        @Override
        protected Object computeFunction(IOperator oper, Object values) {
            //aggValue = 7;
            return values;
        }
*/
        
        @Override
        public void updateInserted(DataRow insertedRow) {
            if ( insertedRow != null ) {
                insertedRow.setValue(insertedRow.getState().getDepth(), columnName);
            }
        }

    } //class SumAggExpression

    /**
     *
     */
    public static class SumAggExpression extends FunctionAggregateExpression {

        public SumAggExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
        }
        @Override
        public void init(DataRow row) {
            aggValue = null;
        }
        @Override
        public void reset(DataRow row) {
            aggValue = null;
        }
        
        @Override
        protected Object computeFunction(IOperator oper, Object values) {
            aggValue = ExprUtil.add(values, this.aggValue);
            return values;
        }
    } //class SumAggExpression
    public static class CountAggExpression extends FunctionAggregateExpression {

        public CountAggExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
        }
        @Override
        public void init(DataRow row) {
            aggValue = null;
        }
        @Override
        public void reset(DataRow row) {
            aggValue = null;
        }
        @Override
        public Class getType() {
            return Integer.class;
        }
        @Override
        protected Object computeFunction(IOperator oper, Object values) {
            aggValue = ExprUtil.add(1, this.aggValue);
            return values;
        }
    } //class CountAggExpression
    
    public static class AvgAggExpression extends FunctionAggregateExpression {
        
        protected Object sumAggValue;
        protected Object countAggValue;
        
        public AvgAggExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
            sumAggValue = null;
            countAggValue = null;
        }
        
        @Override
        public void init(DataRow row) {
            aggValue = null;
            sumAggValue = null;            
            countAggValue = null;            
        }
        @Override
        public void reset(DataRow row) {
            aggValue = null;
            sumAggValue = null;            
            countAggValue = null;            
        }
        
        @Override
        protected Object computeFunction(IOperator oper, Object values) {
            sumAggValue = ExprUtil.add(values, this.sumAggValue);
            countAggValue = ExprUtil.add(1, this.countAggValue);
            
            aggValue = ExprUtil.divDoubleSafe(sumAggValue, countAggValue,Double.valueOf(0d));
            return values;
        }
    } //class AvgAggExpression
    

    public static class MinAggExpression extends FunctionAggregateExpression {

        public MinAggExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
        }

        @Override
        protected Object computeFunction(IOperator oper, Object values) {
            aggValue = ExprUtil.min(values, this.aggValue);
            return values;
        }
    } //class MinAggExpression
    
    public static class MaxAggExpression extends FunctionAggregateExpression {

        public MaxAggExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
        }

        @Override
        protected Object computeFunction(IOperator oper, Object values) {
            aggValue = ExprUtil.max(values, this.aggValue);
            return values;
        }
  
    } //class MaxAggExpression
    
}