
package tdo.impl;

import tdo.service.TableServices;

/**
 * ������ ��� �������� ��������, ������������ ��� ��������� ������������
 * ������� �������. <p>
 * @see tdo.impl.ValidationManager
 * @see tdo.impl.RowValidator
 */
public class ColumnValidator extends AbstractValidator{

    // Declared protected to use in subclass when creating custom validator
    protected String columnName;
    /**
     * ������� ��������� ������ ��� �������� ������� � ����� �������.
     * <p>
     * @param tableServices
     * @param columnName ��� ������� ������� ����������� �� ����������
     */
    public ColumnValidator(TableServices tableServices, String columnName) {
        this.tableServices = tableServices;
        this.columnName = columnName;
    }
    /**
     * ���������� ��� ������� ��� ������� ����������� ���������.
     * @return ��� �������
     */
    public String getColumnName() {
        return this.columnName;
    }
}
