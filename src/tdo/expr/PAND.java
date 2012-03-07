/*
 * PAND.java
 *
 * Created on 14 Ноябрь 2006 г., 10:50
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.expr;

import tdo.DataRow;

/**
 *
 * @author valery
 */
public class PAND extends EvaluatorContainer{
    
    /**
     * Creates a new instance of PAND
     */
    public PAND() {
    }
    
/*    public boolean evaluate(int rowIndex) {
        this.setRowIndex(rowIndex);
        
        if ( isEmpty() )
            return true;

        for ( int i=0; i < getCount(); i++ ) {
            if ( ! this.get(i).evaluate(rowIndex) )
                return false;
        }//for   
        return true;
    }
  */  
    @Override
    public boolean evaluate(DataRow row) {
        //this.setRowIndex(rowIndex);
        
        if ( isEmpty() )
            return true;

        for ( int i=0; i < getCount(); i++ ) {
            if ( ! this.get(i).evaluate(row) )
                return false;
        }//for   
        return true;
    }
/*
    public boolean evaluate(DataRow[] rows) {
        //this.setRowIndex(rowIndex);
        
        if ( isEmpty() )
            return true;

        for ( int i=0; i < getCount(); i++ ) {
//            ExpressionPredicate ep;
//            if ( this.get(i)
            if ( ! this.get(i).evaluate(rows) )
                return false;
        }//for   
        return true;
        
    }
  */  
}//class
