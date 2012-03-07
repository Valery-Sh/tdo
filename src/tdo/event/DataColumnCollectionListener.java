/*
 * DataColumnsListener.java
 *
 * Created on 24 ќкт€брь 2006 г., 18:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.event;

/**
 *
 * @author Valera
 */
public interface DataColumnCollectionListener  extends java.util.EventListener
{
    void columnsChanged( DataColumnCollectionEvent e);
}
