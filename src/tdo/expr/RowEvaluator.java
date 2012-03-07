/*
 * RowEvaluator.java
 *
 */

package tdo.expr;

import tdo.DataRow;


/**
 * ���������� ����������������, ����������� ������� ������ {@link tdo.DataRow}.
 * ������������ ��� �������������� � �������� �������.
 */
public interface RowEvaluator {
    /**
     * ��������� ��� ������� � ���������� ���������, ��� ������� ��������.
     * @param row ����������� ���
     * @return true ��� false
     */
    boolean evaluate(DataRow row);    
      
}//interface RowEvaluator
