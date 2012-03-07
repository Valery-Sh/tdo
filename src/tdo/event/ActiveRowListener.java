package tdo.event;
/*
 * ActiveRowListener.java
 *
 * Created on 2 Ноябрь 2006 г., 18:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */


import java.util.EventListener;

/**
 *
 * @author Valera
 */
public interface ActiveRowListener extends EventListener {
    void activeRowChange(ActiveRowEvent e);
    //void activeRowChanging(ActiveRowEvent e);
}
