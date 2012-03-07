/*
 * DataOperationException.java
 */


package tdo;

/**
 *
 * @author valery
 */
public class DataOperationException extends RuntimeException{
    private int rowIndex;
    private int columnIndex;

    /** Creates a new instance of ValidateException */
    public DataOperationException() {
        super();
    }
    public DataOperationException(String message) {
        super(message);
        this.rowIndex = -1;
        this.columnIndex = -1;
        
    }

    public DataOperationException(int rowIndex) {
        super();
        this.rowIndex = rowIndex;
        this.columnIndex = -1;
        
    }
    public DataOperationException(String message, int rowIndex) {
        super(message);
        this.rowIndex = rowIndex;
        this.columnIndex = -1;
        
    }
    public DataOperationException(String message, int rowIndex, int columnIndex) {
        super(message);
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        
    }

    public int getRowIndex() {
        return this.rowIndex;
    }
    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }
    public int getColumnIndex() {
        return this.columnIndex;
    }
    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }
    
}
