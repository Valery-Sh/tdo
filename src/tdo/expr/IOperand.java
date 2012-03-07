/*
 * IOperand.java
 * 
 * Created on 14.06.2007, 14:18:39
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.expr;

import tdo.NamedValues;

/**
 * ������������ ����� ������ ��� ��������� ������� ��������� ���������.<p>
 * ������ ���������� ��������� �������� �������� ��������� ��� ���������� ���������.
 * ��������� ���������� ����� <code>applyDataTable</code>
 */
public interface IOperand extends IToken{
  
    /**
     * ���������� �������� ��������� ��� ���������� ���������. 
     * @param row ��� �������
     * @return �������� ���������
     */
    Object getValue(NamedValues row);    
    Object getValue(NamedValues[] row);  
    Object getValue();        
    void   setContext(ExpressionContext context);
    Class getType();
    
}