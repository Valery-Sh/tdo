/*
 * DataModel.java
 *
 * Created on 27 Март 2007 г., 9:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.impl;


import tdo.event.ActiveRowListener;
import tdo.event.DataTableListener;
import tdo.event.PendingEditingListener;

/**
 *
 * @author valery
 */
public interface DataModel {
//    public void addDataModelListener(DataModelListener l);

    /**
     * Removes a listener from the list that is notified each time a
     * change to the data model occurs.
     *
     * @param	l		the TableModelListener
     */
//    public void removeDataModelListener(DataModelListener l);

    public void addDataTableListener(DataTableListener l);
    public void removeDataTableListener(DataTableListener l);



    public void addActiveRowListener(ActiveRowListener l);
    public void removeActiveRowListener(ActiveRowListener l);

    /**
     * Регистрирует обработчик события остановки подвисших изменения полей.<p>
     * @param l - тип <code>PendingEditingListener</code>.
     */
    public void addPendingEditingListener(PendingEditingListener l);

  /**
   * Удаляет обработчик события остановки подвисших изменения полей.<p>
   * @param l - тип <code>PendingEditingListener</code>.
   */

    public void removePendingEditingListener(PendingEditingListener l);
    
}
