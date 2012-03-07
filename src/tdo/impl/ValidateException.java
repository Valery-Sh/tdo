/*
 * ValidateException.java
 *
 * Created on 15 Декабрь 2006 г., 14:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.impl;

import tdo.DataRow;

/**
 *
 * @author valery
 */
public class ValidateException extends RuntimeException{
    private int rowIndex;
    private int index; // validator collection index
    private DataRow row;

    /** Creates a new instance of ValidateException */
/*    public ValidateException() {
        super();
    }
 */
    public ValidateException(String message) {
        super(message);
    }

/*    public ValidateException(int rowIndex) {
        super();
    }
 */
    public ValidateException(String message, DataRow row, int vindex) {
        super(message);
        this.index = vindex;
        this.row = row;
    }

/*    public int getRowIndex() {
        return this.rowIndex;
    }
    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }
 */
    public DataRow getRow() {
        return this.row;
    }
    public void setRow(DataRow row) {
        this.row = row;
    }
    public int getIndex() {
        return this.index;
    }
    public void setIndex(int vindex) {
        this.index = vindex;
    }

}
