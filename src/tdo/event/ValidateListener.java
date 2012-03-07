/*
 * ValidateListener.java
 *
 * Created on 15 Декабрь 2006 г., 16:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.event;

import java.util.EventListener;

/**
 * Определяет обработчик события {@link tdo.event.ValidateEvent}
 * 
 */
public interface ValidateListener extends EventListener{
    /**
     * Вызывается при обработке ошибок валидации.
     * @param e событие, описывающее ошибку валидации
     */
    void processError(ValidateEvent e);
}
