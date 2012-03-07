
package tdo.impl;

import tdo.service.TableServices;

/**
 * Служит для создания объектов, используемых при валидации определенной
 * колонки таблицы. <p>
 * @see tdo.impl.ValidationManager
 * @see tdo.impl.RowValidator
 */
public class ColumnValidator extends AbstractValidator{

    // Declared protected to use in subclass when creating custom validator
    protected String columnName;
    /**
     * Создает экземпляр класса для заданной таблицы и имени колонки.
     * <p>
     * @param tableServices
     * @param columnName имя колонки которая проверяется на валидность
     */
    public ColumnValidator(TableServices tableServices, String columnName) {
        this.tableServices = tableServices;
        this.columnName = columnName;
    }
    /**
     * Возвращает имя колонки для которой выполняется валидация.
     * @return имя колонки
     */
    public String getColumnName() {
        return this.columnName;
    }
}
