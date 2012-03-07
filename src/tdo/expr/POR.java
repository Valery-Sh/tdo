/*
 * POR.java
 *
 * Created on 14 Ноябрь 2006 г., 10:39
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
public class POR extends EvaluatorContainer{
    
    /**
     * Creates a new instance of POR
     */
    public POR() {
    }
    
/*    public boolean evaluate( int rowIndex) {
        this.setRowIndex(rowIndex);
        if ( isEmpty() )
            return true;

        for ( int i=0; i < getCount(); i++ ) {
            if ( this.get(i).evaluate(rowIndex) )
                return true;
        }//for   
        return false;
    }
  */  
    @Override
    public boolean evaluate( DataRow row) {
        //this.setRowIndex(rowIndex);
        if ( isEmpty() )
            return true;

        for ( int i=0; i < getCount(); i++ ) {
            if ( this.get(i).evaluate(row) )
                return true;
        }//for   
        return false;
    }
 /*   public boolean evaluate( DataRow[] rows) {
        //this.setRowIndex(rowIndex);
        if ( isEmpty() )
            return true;

        for ( int i=0; i < getCount(); i++ ) {
            if ( this.get(i).evaluate(rows
                    ) )
                return true;
        }//for   
        return false;
    }
*/    
}//class
