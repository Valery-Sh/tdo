/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo;

/**
 *
 * @author Valery
 */
public interface EditObserver {
    void attach(DataRow row);
    void attachNew(DataRow row);
    void beginEdit(DataRow row);
    
    void cancelEditResetRowVersions(DataRow row);
    void cancelEditInserting(DataRow row);
    void cancelEdit(DataRow row);
    
    void endEditBefore(DataRow row);
    void endEditResetRowVersions(DataRow row);
    void endEditInserting(DataRow row);
    void endEdit(DataRow row);

    void setValue(DataRow row,int columnIndex);
    void setValueInserting(DataRow row,int columnIndex);
    
    void delete(DataRow row);
    
    
}//interface EditObserver
