/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tdo.DataRow;
import tdo.event.ValidateEvent;
import tdo.event.ValidateListener;
import tdo.expr.ExpressionContext;
import tdo.expr.RowExpressionContext;
import tdo.service.TableServices;

/**
 * Управляет регистрацией и выполнением валидаторов ряда и колонок.
 * <p>Таблица типа <code>tdo.Table</code> позволяет регистрировать неограниченное число
 * валидаторов — объектов типа tdo.impl.Validator. Для хранения таких объектов,
 * а также их применения предназначен класс ValidationManager.
 * Валидаторы рядов при регистрации добавляются в коллекцию типа
 * <code>java.util.List</code>. Приложению эта коллекция предоставляется классом
 * через метод <code>getRowValidators()</code>. Пример последовательности
 * действий  приложения:
 * <code><pre>
 * ValidationManager vm  =  table.getValidationManager();
 * List<Validator> ls  = v m.getRowValidators();
 * </pre></code>
 * <p>Эта коллекция может быть использована, например, при анализе возникших
 * ошибок.
 * <p>Валидаторы колонок при регистрации добавляются в коллекцию типа
 * <code><pre>java.util.Map<String,List<Validator>></pre></code>.
 * Приложению эта коллекция предоставляется классом через метод
 * <code>getColumnValidators()</code>.
 * Пример последовательности действий  приложения:
 * <code><pre>
 * ValidationManager vm  =  table.getValidationManager();
 * Map<String,List<Validator>>   map =  vm.getColumnValidators();
 * List<Validator>  cls  = map.get(column-name);
 * </pre>
 * </code>
 *
 * <p>Когда в процессе обработки данных ряда таблицы, требуется выполнить
 * валидацию ряда, то выполняется один из перегруженных методов:
 * {@link #validate(DataRow) } или  {@link #validate(DataRow, boolean) }
 * менеджера. Код метода, последовательно извлекает из списка валидаторов ряда,
 * начиная с 0-го элемента валидатор — объект типа Validator и выполняется его
 * метод <code>validate</code>. Если для всех валидаторов получен результат
 * <code>true</code>, то считается, что ряд успешно прошел проверку и ошибок не
 * обнаружено. Если, хотя бы для одного из валидаторов, его метод
 * <code>validate</code> вернул <code>false</code>, то считается, что ряд
 * содержит ошибочные данные.
 * <p>Когда в процессе обработки колонки ряда таблицы, требуется выполнить
 * валидацию колонки, то выполняется метод:
 * {@link #validate(tdo.DataRow, java.lang.String, java.lang.Object) }
 * мэнеджера. Метод, используя имя заданной колонки как ключ, извлекает список
 * валидаторов колонки, и, затем,  начиная с 0-го элемента последовательно
 * применяет его метод <code>validate</code>. Если для всех валидаторов списка
 * получен результат <code>true</code>, то считается, что колонка успешно прошла
 * проверку и ошибок не обнаружено. Если, хотя бы для одного из валидаторов, его
 * метод <code>validate</code> вернул <code>false</code>, то считается, что
 * колонка содержит ошибочные данные.
 * <h2>Регистрация валидаторов</h2>
 * Регистрация валидатора ряда производится менеджером валидации применением
 * одного из методов:
 * <ol><li>void addRowValidator(Validator validator,String msg)</li>
 * <li>Validator addRowValidator(String expression,String msg)</li>
 * Если для ряда регистрируется несколько валидаторов, то для каждого из них
 * может быть применен любой их приведенных выше методов.
 * <p><i>Первая</i> форма метода является наиболее гибкой, позволяя для валидации
 * разработать класс, реализующий интерфейс {@link  tdo.impl.Validator} .
 * <p><i>Вторая</i> форма метода более удобна, т.к. позволяет задать правила
 * валидации строковым выражением над именами колонок таблицы, литералами,
 * функциями и параметрами.
 * <p>Для удаления валидатора ряда используется метод:
 * {@link #removeRowValidator(tdo.impl.Validator) }
 * Регистрация валидатора колонки производится менеджером валидации применением
 * одного из методов:
 * <ol>
 * <li>void addColumnValidator(String columnName, Validator validator,String msg)
 * </li>
 * <li>Validator addColumnValidator(String columnName, String expression,String msg)
 * </li>
 * </ol>
 * <p>Если для колонки регистрируется несколько валидаторов, то для каждого из
 * них может быть применен любой их приведенных выше методов.
 * <p><i>Первая</i> форма метода является наиболее гибкой, позволяя для валидации
 * разработать класс, реализующий интерфейс <code>Validator</code>.
 * <p><i>Вторая</i> форма метода более удобна, т.к. позволяет задать правила
 * валидации строковым выражением над именами колонок таблицы, литералами,
 * функциями и параметрами.
 * <p>Для удаления валидатора колонки по заданному имени колонки используется метод:
 * {@link #removeColumnValidator(java.lang.String, tdo.impl.Validator)
 *
 * <h2>Обработка событий валидации</h2>
 *
 *  Когда валидатор колонки или ряда обнаруживает ошибку валидации, он возбуждает
 * событие типа {@ link tdo.event.ValidateEvent} и оповещает все
 * загегистрированные обработчики этого события. Обработчик события, должен
 * реализовывать интерфейс {@link tdo.event.ValidateListener}. События
 * валидации регистрируются классом <code>ValidationManager</code> одним из
 * методов:
 * <ol>
 * <li>void addRowValidateListener(ValidateListener l) </li>
 * <li>void addColumnValidateListener(ValidateListener l)</li>
 * <p>Для удаления обработчика используется один из методов соответственно:
 * <ol>
 * <li>void removeRowValidateListener(ValidateListener l)</li>
 * <li>void removeColumnValidateListener(ValidateListener l)</li>
 * </ol>
 */
public class ValidationManager {
    protected TableServices tableServices;
    //protected Table table;
    /**
     * Список зарегистрированных валидаторов ряда.
     */
    protected List<Validator> rowValidators;
    /**
     * Зарегистрированные валидаторы колонок.<p>
     * Каждому имени колонки соответствует вход в <code>map</code>-таблицу где
     * объектом-значением является список валидаторов
     */
    protected Map<String,List<Validator>> columnValidators;
    /**
     * Список зарегистрированных обработчиков события <code>ValidateEvent</code>
     * возбуждоемого валидаторами ряда.
     */
    protected List<ValidateListener> validateList;
    /**
     * Список зарегистрированных обработчиков события <code>ValidateEvent</code>
     * возбуждоемого валидаторами колонки.
     */
    protected List<ValidateListener> columnValidateList;
    /**
     * Создает экземпляр класса для заданной таблицы.
     * @param services сервисы таблицы, для которой создается объект
     */
    public ValidationManager(TableServices services) {
        //this.table = table;
        this.tableServices = services;
    }
    /**
     * Возвращает список всех валидаторов ряда.
     * @return список валидаторов ряда
     */
    public List<Validator> getRowValidators() {
        return this.rowValidators;
    }
    /**
     * Возвращает map-тавлицу всех валидаторов для всех колонок.
     * @return map-таблица валидаторов колонок
     */
    public Map<String,List<Validator>> getColumnValidators() {
        return this.columnValidators;
    }
    /**
     * Возвращает сервисы таблицы, для которой выполняется валидация.
     * @return сервися проверяемой таблицы
     */
    public TableServices getTableServices() {
        return this.tableServices;
    }
    /**
     * Удаляет валидаторы и обработчики событий. <p>
     * Удаляет все валидаторы ряда, все валидаторы всех колонок. Также удаляются
     * обработчики события <code>ValidateEvent</code> как для ряда, так и
     * для колонок.
     */
    public void clear() {
        this.rowValidators.clear();
        this.columnValidators.clear();
        this.validateList.clear();
        this.columnValidateList.clear();
    }
    //////////////////////////////////////////////////////////////////////////
    //   COLUMN VALIDATORS
    //////////////////////////////////////////////////////////////////////////
    /**
     * Создает и добавляет в map-таблицу валидаторов новый объект для
     * заданных имени колонки, логического выражения и сообщения об ошибке.
     *
     * @param columnName имя колонки, которая проверяется валидатором
     * @param expression логическое выражение для контроля валидности 
     *   нового значения колонки
     * @param msg сообщение об ошибке. Если имеет значение <code>null</code>,
     *    то создаваемый валидатор сохраняет свое собственное сообщение.
     * @return <code>null</code>, если <code>expression</code> равен
     *   <code>null</code> или его длина равна 0.
     *
     * @see #addColumnValidateListener(tdo.event.ValidateListener)
     * @see #addColumnValidator(java.lang.String,tdo.impl.Validator,java.lang.String)
     */
    public Validator addColumnValidator(String columnName, String expression,String msg) {
        if (expression == null || expression.trim().length() == 0) {
            return null;
        }
        ColumnValidator cv = new ColumnValidator(this.tableServices, columnName);
        cv.setExpression(expression);
        if ( msg != null )
            cv.setMessage(msg);
        if (columnValidators == null) {
            columnValidators = new HashMap<String,List<Validator>>(5);
        }
        List<Validator> vl = columnValidators.get(columnName.toUpperCase());
        if ( vl == null ) {
            vl = new ArrayList<Validator>(3);
            columnValidators.put(columnName.toUpperCase(), vl);

        }
        vl.add(cv);

        return cv;
    }
    /**
     * Создает и добавляет в map-таблицу валидаторов новый объект для
     * заданных имени колонки, логического выражения и сообщения об ошибке.
     *
     * @param columnName имя колонки, которая проверяется валидатором
     * @param validator валидатор нового значения колонки
     * @param msg сообщение об ошибке. Если имеет значение <code>null</code>,
     *    то создаваемый валидатор сохраняет свое собственное сообщение.
     *
     * @see #addColumnValidateListener(tdo.event.ValidateListener)
     * @see #addColumnValidator(java.lang.String,java.lang.String,java.lang.String)
     */
    public void addColumnValidator(String columnName, Validator validator,String msg) {
        if (validator == null) {
            return;
        }
        if (columnValidators == null) {
            columnValidators = new HashMap<String,List<Validator>>(5);
        }
        if ( msg != null ) {
            validator.setMessage(msg);
        }
        List<Validator> vl = columnValidators.get(columnName.toUpperCase());
        if ( vl == null ) {
            vl = new ArrayList<Validator>(3);
            columnValidators.put(columnName.toUpperCase(), vl);

        }
        vl.add(validator);

    }
    /**
     * Удаляет заданный валидатор колонки из списка, соответствующего заданому
     * имени колонки.
     *
     * @param columnName колонка, для которой удаляется валидатор
     * @param validator удаляемый валидатор
     */
    public void removeColumnValidator(String columnName, Validator validator) {
        if (columnValidators == null || columnValidators.isEmpty()) {
            return;
        }
        List<Validator> vl = columnValidators.get(columnName);
        if ( vl == null || vl.isEmpty() )
            return;
        vl.remove(validator);

    }
   /**
     * Удаляет все валидаторы колонки, соответствующие заданому
     * имени колонки.
     *
     * @param columnName колонка, для которой удаляются валидаторы
     */
    public void removeColumnValidators(String columnName) {
        if (columnValidators == null || columnValidators.isEmpty()) {
            return;
        }
        columnValidators.remove(columnName);

    }

    //////////////////////////////////////////////////////////////////////////
    //   ROW VALIDATORS
    //////////////////////////////////////////////////////////////////////////

    /**
     * Создает и добавляет в список валидаторов новый объект для
     * заданного логического выражения и заданного сообщения об ошибке.
     *
     * @param expression логическое выражение для контроля валидности рядов
     *   таблицы
     * @param msg сообщение об ошибке. Если имеет значение <code>null</code>,
     *    то создаваемый валидатор сохраняет свое собственное сообщение.
     * @return <code>null</code>, если <code>expression</code> равен
     *   <code>null</code> или его длина равна 0.
     *
     * @see #addRowValidateListener(tdo.event.ValidateListener)
     * @see #addRowValidator(tdo.impl.Validator,java.lang.String)
     */
    public Validator addRowValidator(String expression,String msg) {
        if (expression == null || expression.trim().length() == 0) {
            return null;
        }
        RowValidator rv = new RowValidator(this.tableServices);
        rv.setExpression(expression);
        if ( msg != null )
            rv.setMessage(msg);
        if (rowValidators == null) {
            rowValidators = new ArrayList<Validator>(5);
        }
        rowValidators.add(rv);
        return rv;
    }
    /**
     * Создает и добавляет в список валидаторов новый объект для
     * заданного валидатора и заданного сообщения об ошибке.
     * @param validator добавляемый валидатор
     * @param msg сообщение об ошибке. Если имеет значение <code>null</code>,
     *    то валидатор <code>validator</code>сохраняет свое собственное
     *    сообщение. В противном случае, <code>msg</code> замещает собственное
     *    сообщение валидатора.
     * @see #addRowValidateListener(tdo.event.ValidateListener)
     * @see #addRowValidator(java.lang.String, java.lang.String)
     */
    public void addRowValidator(Validator validator,String msg) {
        if (validator == null) {
            return;
        }
        if (rowValidators == null) {
            rowValidators = new ArrayList<Validator>(5);
        }
        if ( msg != null ) {
            validator.setMessage(msg);
        }
        rowValidators.add(validator);
    }
    /**
     * Удаляет заданный валидатор из списка зарегистрированных валидаторов.
     *
     * @param validator удаляемый валидатор
     */
    public void removeRowValidator(Validator validator) {
        if (rowValidators == null || rowValidators.isEmpty()) {
            return;
        }
        rowValidators.remove(validator);
    }

    /**
     * Выполняет валидацию заданного ряда выбрасывая или нет исключение в случае
     * обнаружения ошибки. <p>
     *
     * Последовательно выполняет валидаторы из списка валидаторов ряда, начиная
     * с валидатора с индексом 0. Первый валидатор, обнаруживший ошибку завершает
     * обработку. При этом, если параметр <code>throwException</code> равен
     * <code>true</code> выбрасывается исключение
     * <code>tdo.impl.ValidateException</code>.
     * @param row проверяемый ряд
     * @param throwException указывает следует ли при ошибке выбрасывать
     *   исключение. Значение <code>true</code> означает, что будет выброшени
     *   исключение {@link tdo.impl.ValidateException} при ошибке, обнаруженной
     *   хотя бы одним валидатором
     * @return <code>true</code> все валидаторы завершились успешно, не
     * обнаружив ошибки. <code>false</code> - в противном случае (возможно
     *   только, если параметр <code>throwException</code> равен <code>false</code>.
     */
    public boolean validate(DataRow row, boolean throwException) {

        if (rowValidators == null || rowValidators.isEmpty()) {
            return true;
        }
        boolean b = true;
        int vindex = -1;
        Validator v = null;
        for (int i = 0; i < rowValidators.size(); i++) {
            v = rowValidators.get(i);
            if (!v.validate(row)) {
                b = false;
                vindex = i;
                break;
            }
        }
        //table.editProhibited = true;

        if (validateList != null && validateList.size() > 0 &&  vindex >= 0 ) {
            ValidateEvent e = new ValidateEvent(this, row, v );
            for (int i = 0; i < validateList.size(); i++) {
                validateList.get(i).processError(e);
            }
        }

        //editProhibited = false;
        if ( vindex >= 0 && throwException) {
            throw new ValidateException("Row validate ERROR", row, vindex);
        }
        return vindex < 0 ? true : false;

    }

    public void notifyListeners(Validator v, int validatorIndex, DataRow row, boolean throwException) {
        if (validateList != null && validateList.size() > 0  ) {
            ValidateEvent e = new ValidateEvent(this, row, v );
            for (int i = 0; i < validateList.size(); i++) {
                validateList.get(i).processError(e);
            }
        }

        //editProhibited = false;
        if ( throwException  ) {
            throw new ValidateException("Row validate ERROR", row, validatorIndex);
        }

    }
    
    /**
     * Выполняет валидацию заданного ряда.
     * <p>Делегирует исполнение методу {@link #validate(tdo.DataRow, boolean) при
     * значении второго параметра равным <code>false</code>.
     * <p>
     * Последовательно выполняет валидаторы из списка валидаторов ряда, начиная
     * с валидатора с индексом 0. Первый валидатор, обнаруживший ошибку завершает
     * обработку.
     * @param row проверяемый ряд
     * @return <code>true</code> все валидаторы завершились успешно, не
     * обнаружив ошибки. <code>false</code> - в противном случае
     */
    public boolean validate(DataRow row) {
        return this.validate(row, false);
    }
    
    /**
     * Выполняет валидацию заданного ряда выбрасывая или нет исключение в случае
     * обнаружения ошибки. <p>
     *
     * Последовательно выполняет валидаторы из списка валидаторов ряда, начиная
     * с валидатора с индексом 0. Первый валидатор, обнаруживший ошибку завершает
     * обработку. При этом, если параметр <code>throwException</code> равен
     * <code>true</code> выбрасывается исключение
     * <code>tdo.impl.ValidateException</code>.
     * @param row проверяемый ряд
     * @param throwException указывает следует ли при ошибке выбрасывать
     *   исключение. Значение <code>true</code> означает, что будет выброшени
     *   исключение {@link tdo.impl.ValidateException} при ошибке, обнаруженной
     *   хотя бы одним валидатором
     * @return <code>true</code> все валидаторы завершились успешно, не
     * обнаружив ошибки. <code>false</code> - в противном случае (возможно
     *   только, если параметр <code>throwException</code> равен <code>false</code>.
     */
    public Validator validateSilent(DataRow row) {

        if (rowValidators == null || rowValidators.isEmpty()) {
            return null;
        }
        Validator result = null;
        Validator v;//isEmpty() = null;
        for (int i = 0; i < rowValidators.size(); i++) {
            v = rowValidators.get(i);
            if (! v.validate(row)) {
                result = v;
                break;
            }
        }
        return result;

    }
    
    /**
     * Выполняет валидацию заданного ряда для заданной колонки и при заданном
     * новом устанавливаемом значении колонки.
     * <p>Метод завершается не выполняясь, если не зарегистрировано ни одного
     * валидатора для заданной колонки или имя колонки равно <code>null</code>.
     * <p>
     * Последовательно выполняет валидаторы из списка валидаторов колонки, начиная
     * с валидатора с индексом 0. Первый валидатор, обнаруживший ошибку завершает
     * обработку.
     * @param row проверяемый ряд
     * @param columnName
     * @param value
     * @return <code>true</code> все валидаторы завершились успешно, не
     * обнаружив ошибки. <code>false</code> - в противном случае
     */
    public boolean validate(DataRow row, String columnName, Object value) {

        int vindex = -1;

        if (columnValidators == null || columnValidators.isEmpty() )
            return true;

        //String columnName = table.getColumns().get(columnIndex).getName();

        if ( columnName == null )
            return true;

        List<Validator> vl = columnValidators.get(columnName.toUpperCase().trim());
        if ( vl == null || vl.isEmpty() )
            return true;

        for (int i = 0; i < vl.size(); i++) {
            if ( ! vl.get(i).validate(row,value) ) {
                vindex = i;
                break;
            }
        }

        //editProhibited = true;

        if (columnValidateList != null && columnValidateList.size() > 0 &&  vindex >= 0 ) {
            ValidateEvent e = new ValidateEvent(this, row, columnName, vl.get(vindex), value);
            for (int i = 0; i < columnValidateList.size(); i++) {
                columnValidateList.get(i).processError(e);
            }
        }

        //editProhibited = false;

        return vindex < 0 ? true : false;
    }


    /**
     * Регистрирует обработчик события контроля  данных.<p>
     * @param l - тип <code>ValidateListener</code>.
     */
    public void addRowValidateListener(ValidateListener l) {
        if (validateList == null) {
            validateList = new ArrayList<ValidateListener>();
        }
        validateList.add(l);
    }

    /**
     * Удаляет обработчик события валидаторов ряда .<p>
     * @param l - тип <code>ValidateListener</code>.
     */
    public void removeRowValidateListener(ValidateListener l) {
        if (validateList == null) {
            return;
        }
        validateList.remove(l);
    }

    /**
     * Регистрирует обработчик события валидаторов колонок.<p>
     * @param l - тип <code>ValidateListener</code>.
     */
    public void addColumnValidateListener(ValidateListener l) {
        if (columnValidateList == null) {
            columnValidateList = new ArrayList<ValidateListener>();
        }
        columnValidateList.add(l);
    }

    /**
     * Удаляет обработчик события валидаторов солонок.<p>
     * @param l - тип <code>ValidateListener</code>.
     */
    public void removeColumnValidateListener(ValidateListener l) {
        if (columnValidateList == null) {
            return;
        }
        columnValidateList.remove(l);
    }

    /**
     * Форматирует строку знаков, содержимое которой представляет собой 
     * сообщение об ошибке валидации ряда и может включать замещаемые 
     * последовательности стмволов:
     * <ul>
     * <li>#i - индекс валидатора в списке валидаторов</li>
     * <li>#ri - индекс ряда в таблице</li>
     * <li>#e - строковое выражение, если задано</li>
     * </ul>
     * @param row
     * @param v
     * @return
     */
    public String formatMessage(DataRow row, Validator v ) {
        String m = v.getMessage();
        if ( m.contains("#i") )
            m = m.replaceAll("#i" , "" + this.rowValidators.indexOf(v));
        if ( m.contains("#ri" ) )
            m = m.replaceAll("#ri" , "" + (row == null ? -1 : row.getIndex()) );
        if ( m.contains("#t") )
            m = m.replaceAll("#t" , this.tableServices.getFilterServices().getTableName());
        if ( m.contains("#e") ) {

            ExpressionContext ec = v.getExpressionContext();
            String r = "";
            if ( ec != null )
              r = ec.getExpressionString();
            r = r == null ? "" : r;
            m = m.replaceAll("#e" , r);
        }

        return m;
    }
    /**
     * Форматирует строку знаков, содержимое которой представляет собой 
     * сообщение об ошибке валидации ряда и может включать замещаемые 
     * последовательности стмволов:
     * <ul>
     * <li>#i - индекс валидатора в списке валидаторов</li>
     * <li>#ri - индекс ряда в таблице</li>
     * <li>#t - имя таблицы</li>
     * <li>#e - строковое выражение, если задано</li>
     * <li>#cn - имя колонки</li>
     * <li>#v - новое (ошибочное) значение колонки</li>
     * </ul>
     * @param row
     * @param v
     * @param columnIndex
     * @param columnName
     * @return
     */
    public String formatMessage(DataRow row, Validator v, String columnName ) {
        String m = v.getMessage();
        if ( m.contains("#i") ) {
            //String cn = table.getColumns().get(columnIndex).getName();
            m = m.replaceAll("#i" , "" + this.columnValidators.get(columnName.toUpperCase().trim()).indexOf(v));
        }
        if ( m.contains("#ri" ) )
            m = m.replaceAll("#ri" , "" + (row == null ? -1 : row.getIndex()) );
        if ( m.contains("#t") )
            m = m.replaceAll("#t" , this.tableServices.getFilterServices().getTableName());
        if ( m.contains("#e") ) {
            ExpressionContext ec = ((ColumnValidator)v).getExpressionContext();
            String r = "";
            if ( ec != null )
                r = ec.getExpressionString();
            r = r == null ? "" : r;
            m = m.replaceAll("#e" , r);
        }

        if ( m.contains("#сn") )
            m = columnName != null ? "" : m.replaceAll("#cn" , columnName );

        if ( m.contains("#v") ) {
            RowExpressionContext ec = (RowExpressionContext) v.getExpressionContext();
            Object o = "";
            if ( ec != null )
                o = ec.getParameterValue(row, "value");
            String r = ( o == null ? "" : o.toString());
            m = m.replaceAll("#v" , r);
        }

        return m;

    }

}//class ValidationManager
