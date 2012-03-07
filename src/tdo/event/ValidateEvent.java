/*
 * ValidateEvent.java
 *
 * Created on 15 Декабрь 2006 г., 15:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.event;

import java.util.EventObject;
import tdo.DataRow;
import tdo.Table;
import tdo.impl.ValidationManager;
import tdo.impl.Validator;
import tdo.service.TableServices;

/**
 *  Предоставляет информацию об ошибке валидации ряда или колонки.
 * <p>Событие возбуждается, когда в таблице {@link tdo.Table} есть хотя бы один
 * зарегистрированный валидатор, который обнаружил ошибку в данных ряда или
 * колонки.
 * @see tdo.impl.Validator
 */
public class ValidateEvent extends EventObject{
    
    //private int rowIndex;

    private String message;
  //  private String cause;
    private DataRow row;
    private String columnName;

    public String getColumnName() {
        return columnName;
    }
    private Validator validator;
    private Object value;
    /**
     * Создает экземпляр класса для зананных источника события, ряда, индекса
     * колонки, валидатора, вызвавшего ошибку и значения.
     * <p>Этот конструктор обычно используется при валидации колонки.
     * @param source источник события, которым всегда является объект типа
     *   {@link tdo.impl.ValidationManager}
     * @param row ряд, при проверке которого обнаружена ошибка
     * @param columnIndex индекс колонки, при назначения для которой, обнаружена
     *      ошибка
     * @param validator валидатор, условие валидности которого не выполнено
     * @param value новое значение для колонки с индексом
     *      <code>columnIndex</code>
     */
    public ValidateEvent(ValidationManager source, DataRow row, String columnName, Validator validator, Object value) {
        super(source);
        this.row = row;
        this.columnName = columnName;
        this.validator = validator;
        this.value = value;
    }
    /**
     *
     * Создает экземпляр класса для зананных источника события, ряда, индекса
     * колонки, валидатора, вызвавшего ошибку и значения.
     *
     * <p>Этот конструктор обычно используется при валидации ряда.
     * @param source источник события, которым всегда является объект типа
     *   {@link tdo.impl.ValidationManager}
     * @param row ряд, при проверке которого обнаружена ошибка
     * @param validator валидатор, условие валидности которого не выполнено
     */
    public ValidateEvent(ValidationManager source, DataRow row, Validator validator) {
        super(source);
        this.row = row;
        this.validator = validator;
        this.columnName = null;
    }
    /**
     * Возвращает таблицу, при валидации которой обнаружена ошибка.
     * @return таблица, при валидации которой обнаружена ошибка.
     */
    public TableServices getTableServices() {
        return ((ValidationManager)getSource()).getTableServices();
    }

    /**
     * Возвращает объект, упрравляющий процессом валидации.
     * <p>Методы <code>validate</code> таблицы делегируют исполнение специальному
     * классу типа {@link tdo.impl.ValidationManager} , который предоставляет
     * средства для регистрации валидаторов, регистрации событий, запуска
     * процессов валидации и оповещения слушателей событий, если это необходимо.
     * @return объект, упрравляющий процессом валидации.
     */
    public ValidationManager getValidationManager() {
        return (ValidationManager)getSource();
    }
    /**
     * Возвращает индекс колонки таблицы, при валидации которой обнаружена ошибка.
     * @return индекс колонки
     */
/*    public int getColumnIndex() {
        if ( columnName == null )
            return -1;
        return this.getTable().getColumns().find(columnName);
    }
 */
    /**
     * Возвращает валидатор, обнаруживший ошибку.
     * @return валидатор, обнаруживший ошибку
     */
    public Validator getValidator() {
        return this.validator;
    }
    /**
     * Возвращает значение, при установке которого для колонки обнаружена ошибка.
     * @return значение, вызвавшее ошибку при установке
     */
    public Object getValue() {
        return this.value;
    }
    /**
     * Возвращает ряд, при валидации которого обнаружена ошибка.
     * @return ошибочный ряд
     */
    public DataRow getRow() {
        return this.row;
    }
    /**
     *  Возвращает форматированное сообщение, описывающее причину ошибки.
     * <p>Методы регистрации содержат параметр типа <code>java.lang.String</code>,
     * содержащий сообщение, которое используется при создании события. В состав
     * строки этого сообщения могут входить специальные последовательности
     * символов, которые замещаются реальными данными при выдаче данного метода.
     *
     * @return если после создании объекта выполнялся метод
     * {@link #setMessage(java.lang.String) },  то возвращается установленное. В
     *  противном случае, возвращается форматированная строка сообщения
     *  валидатора
     *
     * @see tdo.impl.RowValidator#getMessage()
     * @see tdo.impl.ColumnValidator#getMessage()
     */
    public String getMessage() {
        String s;//My 06.03.2012 = null;
        if ( this.message != null ) {
            return this.message;
        }
        s = validator.getMessage();
        if ( s == null )
            return s;
        return columnName != null ?
            getValidationManager().formatMessage(row, validator, columnName) :
            getValidationManager().formatMessage(row, validator);

    }
    /**
     * Устанавливает значение сообщения, описывающее ошибку.
     * Если это значение не равно <code>null</code>, то оно используется при
     * выполнении {@link #getMessage() }.
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
