/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.temp;

import tdo.expr.ExpressionContext;
import tdo.expr.FunctionExpression;
import tdo.expr.IOperand;
import tdo.expr.IOperator;

    public  class MyLowExpression extends FunctionExpression {

        public MyLowExpression(ExpressionContext context, IOperator operator, IOperand op1) {
            super(context, operator, op1);
        }

        @Override
        protected Object computeFunction(IOperator oper, Object values) {
            return values == null ? null : values.toString().toLowerCase();
        }

        @Override
        public Class getType() {
            return String.class;
        }
    } //class UpperCaseExpression
