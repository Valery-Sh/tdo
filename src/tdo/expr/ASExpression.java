/*
 * ASExpressiion.java
 * 
 * Created on 25.06.2007, 14:28:18
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.expr;

import tdo.NamedValues;

/**
 *
 * @author valery
 */
public class ASExpression extends CompareExpression{
    private String name;
    public ASExpression(ExpressionContext context,IOperator operator, IOperand op1, IOperand op2) {
         super(context,operator, op1, op2);
         this.name = ((IdentifierOperand)getOp2()).getName();        
    }
    
/*    public Object getValue(int rowIndex) {
        return this.getOp1().getValue(rowIndex);
    }
*/
    @Override
    public Object getValue(NamedValues row) {
        return this.getOp1().getValue(row);
    }
    @Override
    public Object getValue(NamedValues[] rows) {
        return this.getOp1().getValue(rows);
    }
    
    @Override
    public Class getType() {
       return getOp1().getType();
    }            
    public String getName() {
        return name;
    }
    public void setName(String id) {
        name = id;
    }
    
}//class ASEXpression
