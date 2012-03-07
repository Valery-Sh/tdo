/*
 * Binder.java
 *
 * Created on 29 ќкт€брь 2006 г., 10:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.bind;

import java.awt.Component;
import tdo.Table;
import tdo.event.ActiveRowListener;
import tdo.event.TableListener;

/**
 *
 * @author Valera
 */
public interface Binder extends ActiveRowListener, TableListener{
    IPositionManager getPositionManager();
    void             setPositionManager( IPositionManager positionManager);
    Component        getComponent();
    void             setComponent(Component comp);
    String           getColumnName();
    void             setColumnName(String columnName);
    void             removing();
    public void      dataTableChanged( Table newDataTable);
    public void      dataTableChanging(Table oldDataTable, Table newDataTable);
    
}
