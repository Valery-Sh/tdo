/*
 * 111 To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdo;

/**
 *
 * @author Valery
 */
public class AbstractEditObserver implements EditObserver {

/*    protected List objectList;

    public AbstractEditObserver(List objectList) {
        this.objectList = objectList;
    }
*/
    @Override
    public void attach(DataRow row) {
/*        if (objectList != null) {
            objectList.add(row.getCellCollection().getObject());
        }
        //objectListChanged(row);
 */
    }

    @Override
    public void attachNew(DataRow row) {
/*        if (objectList != null) {
            objectList.add(row.getCellCollection().getObject());
        }
 */
    }

    @Override
    public void beginEdit(DataRow row) {
    }

    @Override
    public void cancelEditResetRowVersions(DataRow row) {
    }

    @Override
    public void cancelEditInserting(DataRow row) {
    }

    @Override
    public void cancelEdit(DataRow row) {
    }

    @Override
    public void endEditBefore(DataRow row) {
    }

    @Override
    public void endEditResetRowVersions(DataRow row) {
    }

    @Override
    public void endEditInserting(DataRow row) {
    }

    @Override
    public void endEdit(DataRow row) {
    }

    @Override
    public void setValue(DataRow row, int columnIndex) {
    }

    @Override
    public void setValueInserting(DataRow row,int columnIndex) {
    }

    @Override
    public void delete(DataRow row) {
    }
}//class AbstractEditObserver
