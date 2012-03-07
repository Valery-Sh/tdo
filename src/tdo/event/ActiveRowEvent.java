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
public class ActiveRowEvent extends EventObject {

    public static final boolean CHANGING = true;
    public static final boolean CHANGED = false;
    private int oldValue;
    private int newValue;
    private boolean changing;

    public ActiveRowEvent(Object source) {
        this(source, -1, -1);
        this.changing = false;
    }

    public ActiveRowEvent(Object source, int oldValue, int newValue) {
        super(source);
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.changing = false;
    }

    public ActiveRowEvent(Object source, int oldValue, int newValue, boolean changing) {
        super(source);
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.changing = changing;
    }

    public int getOldValue() {
        return this.oldValue;
    }

    public void setOldValue(int value) {
        this.oldValue = value;
    }

    public boolean isChanging() {
        return this.changing;
    }

    public int getNewValue() {
        return this.newValue;
    }

    public void setNewValue(int value) {
        this.newValue = value;
    }
}//class
