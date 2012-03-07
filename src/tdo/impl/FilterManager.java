/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdo.impl;

/**
 *
 * @author Valery
 */
public class FilterManager {

    private Filterer currentFilterer;
/*
    public void filter(Filterer newfilterer) {

        if (this.currentFilterer == null && newfilterer == null) {
            return;
        }

        if (newfilterer == null) {
            return;
        }

        this.currentFilterer = newfilterer;

        actionObjects.addElement(newfilterer);

        if (relation != null) {
            refresh();
            relation.refreshView(this);
            refreshRelation(relation.getValues());
            fireDataModelStructure();
            //table.fireDataModel(dataModelEvent);
            //table.setRowCount(this.getRowCount());

            return;
        }

        DataRow[] v = this.currentFilterer.getRows();
        currentRows = currentRows.create(this.table.getContext(), false);
        currentRows.copyFrom(v);
        fireDataModelStructure();
    }
*/
}//class FilterManager
