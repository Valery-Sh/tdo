/*
 * DataTableListener.java
 *
 * Created on 1 Ноябрь 2006 г., 12:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.event;



/**
 * TableModelListener defines the interface for an object that listens
 * to changes in a TableModel.
 *
 * @version 1.13 12/03/01
 * @author Alan Chung
 * @see javax.swing.table.TableModel
 */

public interface DataTableListener
{
    public void tableChanged(tdo.event.DataTableEvent e);
}

