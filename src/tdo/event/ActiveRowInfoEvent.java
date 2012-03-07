package tdo.event;
/*
 * ActiveRowEvent.java
 *
 * Created on 2 Ноябрь 2006 г., 18:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.util.EventObject;

/**
 *
 * @author Valera
 */
public class ActiveRowInfoEvent extends EventObject {
    
    private ActiveRowInfo oldValue;
    private ActiveRowInfo newValue;
    
    public ActiveRowInfoEvent(Object source) {
        this(source, null,null);
    }
    public ActiveRowInfoEvent(Object source, ActiveRowInfo oldValue, ActiveRowInfo newValue) {
        super(source);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
    
    public ActiveRowInfo getOldValue() {
        return this.oldValue;
    }
   public void setOldValue(ActiveRowInfo value) {
        this.oldValue = value;
   }
   
    public ActiveRowInfo getNewValue() {
        return this.newValue;
    }
   public void setNewValue(ActiveRowInfo value) {
        this.newValue = value;
   }
   
}//class
