/*
 * PNOT.java
 *
 * Created on 14 Ноябрь 2006 г., 12:38
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
public class PNOT implements RowEvaluator {
    
    private RowEvaluator predicate;
    private int rowIndex;
    /** Creates a new instance of PNOT */
    public PNOT() {
        this(null);
    }
    public PNOT(RowEvaluator p) {
        this.predicate = p;
    }
    
    public RowEvaluator getPredicate() {
        return this.predicate;
    }
    public void setPredicate( RowEvaluator p) {
        this.predicate = p;
    }
    
/*    public boolean evaluate( int rowIndex) {
        this.setRowIndex(rowIndex);
        if ( this.predicate == null )
            return true;
        return ! this.predicate.evaluate(rowIndex);
    }
*/    
    @Override
    public boolean evaluate( DataRow row) {
        //this.setRowIndex(rowIndex);
        if ( this.predicate == null )
            return true;
        return ! this.predicate.evaluate(row);
    }
/*    public boolean evaluate( DataRow[] rows) {
        //this.setRowIndex(rowIndex);
        if ( this.predicate == null )
            return true;
        return ! this.predicate.evaluate(rows);
    }
*/    
/*    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }
 */ 
/*    public static boolean evaluate( RowEvaluator p ) {
        if ( p == null )
            return true;
        return ! p.evaluate( this.getRowIndex());
        
    }
 */ 
}
