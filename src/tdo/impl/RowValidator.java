/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.impl;

import tdo.service.TableServices;

/**
 * ������ ��� �������� ��������, ������������ ��� ��������� ����� �������. <p>
 * @see tdo.impl.ValidationManager
 * @see tdo.impl.ColumnValidator
 */
public class RowValidator extends AbstractValidator{

    /**
     * ������� ��������� ������ ��� �������� �������.<p>
     * @param tableServices ������� �������, ��� ������� ����������� �� ����������
     */
    public RowValidator(TableServices tableServices) {
        this.tableServices = tableServices;
    }


    
}//class RowValidator

