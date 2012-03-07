/*
 * @(#)ChangeEvent.java	1.14 01/12/03
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package tdo.event;

import java.util.EventObject;


/**
 * ChangeEvent is used to notify interested parties that
 * state has changed in the event source.
 * <p>
 * <strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with
 * future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running
 * the same version of Swing.  As of 1.4, support for long term storage
 * of all JavaBeans<sup><font size="-2">TM</font></sup>
 * has been added to the <code>java.beans</code> package.
 * Please see {@link java.beans.XMLEncoder}.
 *
 * @version 1.14 12/03/01
 * @author Jeff Dinkins
 */
public class ChangeStateEvent extends EventObject {
  /**
   * ������ ������ �� ������ ���� <code>EventObject</code>.<p>
   * ���������� � �������, ����� ����������� ���������� �������,
   * ���������� �������� �������� ������ �� ��������� ����
   * <code>ChangeStateEvent</code> ������������ ���������� ��
   * �������-��������� �������, �� ����� ��������� ���������� ��
   * ������������ �������. ��������, ������ ���� <code>PCellEditor</code>
   * �������� ����� <code>editingStoped</code> �����������, �������, ���
   * ������� �������� <code>PTable</code>. PTable ������ ������� �������
   * � ���������� ��������, �������, � ���� ������� ������� �� ����, �����
   * ������� ���������� ������ �� ��������� �������������� ������.
   */
    private EventObject originalEvent = null;
    /**
     * Constructs a ChangeEvent object.
     *
     * @param source  the Object that is the source of the event
     *                (typically <code>this</code>)
     */

    public ChangeStateEvent(Object source) {
        this(source,null);
    }

    public ChangeStateEvent(Object source,EventObject originalEvent) {
        super(source);
        this.originalEvent = originalEvent;
    }
    /**
     * getter - ����� �������� <code>originalEvent</code>.<p>
     * @return - ������ ���� <code>EventObject</code>.
     * @see #originalEvent
     */
    public EventObject getOriginalEvent() {
      return this.originalEvent;
    }

    /**
     * setter - ����� �������� <code>originalEvent</code>.<p>
     * @param e 
     * @see #originalEvent
     * @see #getOriginalEvent
     */
    public void setOriginalEvent(EventObject e) {
      originalEvent = e;
    }

}

