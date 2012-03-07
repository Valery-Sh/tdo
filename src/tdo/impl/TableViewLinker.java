/*
 * TableViewLinker.java
 * 
 * Created on 08.10.2007, 9:32:19
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.impl;

import java.util.ArrayList;
import java.util.List;
import tdo.event.DataColumnCollectionListener;
import tdo.event.RowEditingEvent;
import tdo.event.RowEditingListener;

/**
 *
 * @author valery
 */
public class TableViewLinker {
    
    private List<RowEditingListener> listeners; 
    private List<DataColumnCollectionListener> columnlisteners; 
    
    public void addDataColumnCollectionListener(DataColumnCollectionListener l){
        if ( columnlisteners == null )
            columnlisteners = new ArrayList<DataColumnCollectionListener>(2);
        columnlisteners.add(l);
    }
    public void removeDataColumnCollectionListener(DataColumnCollectionListener l){
        if ( columnlisteners == null || columnlisteners.isEmpty() ) 
            return;
        columnlisteners.remove(l);
    }
    
    public void addRowEditingListener(RowEditingListener l){
        if ( listeners == null )
            listeners = new ArrayList<RowEditingListener>(2);
        listeners.add(l);
    }
    
    public void removeRowEditingListener(RowEditingListener l){
        if ( listeners == null || listeners.isEmpty() ) 
            return;
        listeners.remove(l);
    }
    
    public void fireRowEditing(RowEditingEvent e) {
        if ( listeners == null || listeners.isEmpty() ) 
            return;
        for ( int i=0; i < listeners.size();i++ )    
            listeners.get(i).processRowEditing(e);
    }
    
    public List<RowEditingListener> getListeners() {
        return this.listeners;
    }
    
    public List<DataColumnCollectionListener> getColumnListeners() {
        return this.columnlisteners;
    }
    
}//TableViewLinker
