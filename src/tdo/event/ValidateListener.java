/*
 * ValidateListener.java
 *
 * Created on 15 ������� 2006 �., 16:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.event;

import java.util.EventListener;

/**
 * ���������� ���������� ������� {@link tdo.event.ValidateEvent}
 * 
 */
public interface ValidateListener extends EventListener{
    /**
     * ���������� ��� ��������� ������ ���������.
     * @param e �������, ����������� ������ ���������
     */
    void processError(ValidateEvent e);
}
