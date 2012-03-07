/*
 * RowEditingListener.java
 * 
 * Created on 08.10.2007, 10:29:10
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.event;

import java.util.EventListener;

/**
 *
 * @author valery
 */
public interface RowEditingListener extends EventListener{
    
    void processRowEditing(RowEditingEvent e);

}
