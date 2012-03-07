/*
 * PositionManager.java
 *
 * Created on 29 Октябрь 2006 г., 10:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.bind;

import java.awt.Component;
import java.util.Enumeration;
import java.util.Hashtable;
import tdo.Table;
import tdo.event.ActiveRowEvent;
import tdo.impl.ValidateException;

/**
 * Класс реализует интерфейс {@link pbind.IPositionManager} и предоставляет 
 * средства для связывания объекта типа {@link pdata.BaseDataTable} и одного или нескольких
 * компонент типа {@link java.awt.Component}. <p>
 * Рассмотрим пример. <p>
 * Предположим мы разработали desktop приложение фрэйм которого
 * содержит компоненты javax.swing.JTable, ссылка но который доступна в переменной
 * jtable1, javax.swing.JLabel, ссылка но который хранится в переменной jlabel1.
 * 
 * Создадим объект типа pdata.BaseDataTable
 * 
 * <code><pre>
 *  1.     BaseDataTable empl = new BaseDataTable();
 *  2.     empl.addRow( new String[] { "Branch","Department","LastName","Salary" },
 *                    new Object[] { "South","Accounts","Smith",2000 } );
 *  3.     empl.addRow(  new Object[] {"South", "Accounts","Fisher",3500 } );
 *  4.     empl.addRow(  new Object[] {"North", "IT","Gates",15000 } );
 *  5.     empl.addRow(  new Object[] {"North", "IT","Wood",12000 } );
 *  6.     empl.addRow(  new Object[] { "North", "Accounts","Norton",2500 } );
 * 
 * </pre></code>
 * 
 *  Расширим текст кода следующими строками:
 * 
 *  <code><pre>
 *  7.  IPositionManager pm = new PositionManager(empl);
 *  8.  pm.add(jtable1);
 *  9.  pm.add(jlabel1,"LastName");  
 *  </pre></code>
 * 
 * Запустим приложение. Установим фокус на jtable1, и используя мышь и (или) клавиатуру 
 * , будем перемещаться по рядам таблицы. C изменением выбранной строки
 * jtable1 автоматически меняется текст компоненты jlabel1. <p>
 * Если изменить значение в какой-либо ячейке jtable1, соответствующей колонке LastName,
 * например, значение "Fisher" в строке 1 изменим на "Bob", то  изменилось также значение
 * текста в jlabel1.  <p>
 * В строке 7 кода создается экземпляр PositionManager с использованием конструктора,
 * принимающего в качестве параметра ссылку на таблицу <code>empl</code>. В строке 8
 * компонент <code>jtable1</code> добавляется к PositionManager и, следовательно, связывается
 * с объектом <code>empl</code>. В строке 9 к PositionManager добавляем компонент 
 * <code>jlabel1</code>, указывая, что связь осуществляется для колонки с именем "LastName".
 */
public class PositionManager implements IPositionManager{
    Table oldDataTable;
    private Table dataTable;
    private Hashtable binders;
    private Hashtable binderMap;
    /** Creates a new instance of PositionManager */
    public PositionManager() {
        this(null);
    }

    public PositionManager(Table table) {
        this.dataTable = table;
        binders = new Hashtable(10);
        binderMap = new Hashtable(10);
        binderMap.put("javax.swing.JTable","tdo.swing.bind.JTableBinder");
        binderMap.put("javax.swing.JTextField","tdo.swing.bind.JTextFieldBinder");
        binderMap.put("javax.swing.JFormattedTextField","pswing.bind.JFormatedTextFieldBinder");
        binderMap.put("javax.swing.JLabel","tdo.swing.bind.JLabelBinder");
        binderMap.put("javax.swing.JList","tdo.swing.bind.JListBinder");
        binderMap.put("javax.swing.JComboBox","tdo.swing.bind.JComboBoxBinder");
        
        binderMap.put("tdo.swing.table.TableViewer","tdo.swing.bind.JTableBinder");        
        binderMap.put("tdo.swing.table.DataViewer","tdo.swing.bind.JTableBinder");                
                       
    }
    
    // ***********************************************************************
    //  IPositionmanager implementation
    // ***********************************************************************
    
    /**
     * Устанавливает значение ячейки таблицы {@link pdata.BaseDataTable}.<p>
     * Вызывает метод {@link pdata.BaseDataTable.setValue} класса BaseDataTable. Если при этом возникает
     * исключение ситуация, то оно перехватывается и далее не выбрасывается.<p>
     * 
     * 
     * @param value  - новое значение ячейки.
     * @param rowIndex -  Индекс изменяемого ряда.
     * @param columnIndex -  Индекс колонки изменяемого ряда.
     * @return true - если метод setValue класса DBaseDataTableне выбросил
     * исключения. false - в противном случае.
     */
    @Override
    public boolean setValue( Object value,int rowIndex, int columnIndex) {
        boolean result = true;
        if ( dataTable == null  )
            return false;
        
        try {
            dataTable.getRow(rowIndex).setValue(value, columnIndex);
        } catch ( Exception ex) {
            result = false;
        }
        return result;
    }
    @Override
    public boolean setValue( Object value,int rowIndex, String columnName) {
        return setValue(value, rowIndex,dataTable.getColumns().find(columnName));
    }
    /**
     * Устанавливает новое значение активного ряда объекта <code>BaseDataTable</code>. <p>
     * 
     * 
     * @param rowIndex  индекс нового активного ряда.
     * @return true, если метод {@link pdata.BaseDataTable#moveTo(int)} не выбросил
     * исключения. false - в противном случае.
     */
    @Override
    public boolean moveTo( int rowIndex ) {
        boolean result = true;
        if ( dataTable == null  )
            return false;
        try {
            //20.07.2008dataTable.moveTo(rowIndex);
            dataTable.setActiveRowIndex(rowIndex);
        } catch ( ValidateException ex) {
            result = false;
        } catch ( Exception ex) {
            result = false;
        }
        return result;
    }
    
    /**
     * Возвращает значение ссылки на связанную таблицы. <p>
     * 
     * 
     * @return связанная таблица типа {@link pdata.BaseDataTable}.
     */
    @Override
    public Table getTable() {
        return this.dataTable;
    }
    
    /**
     * Устанавливает значение свойства для связываемой таблицы. <p>
     * Перед установкой нового значения вызывается метод  {@link #dataTableChanging}.
     * После установкой нового значения вызывается метод {@link #dataTableChanged}.
     *
     * @param dataTable устанавливаемое значения ссылки на связываемую таблицу.
     */
    @Override
    public void setTable( Table dataTable) {
        dataTableChanging( this.dataTable, dataTable);
        this.dataTable = dataTable;
        dataTableChanged(dataTable);
    }
    /**
     * Добавляет компонент к коллекции "связанных" компонент.<p>
     * Просто вызывает перегруженный метод add со значением null второго параметра.<p>
     * Метод применим в случае, когда компонент связывается с BaseDataTable без использования
     * какой либо колонки. Таким является, например, JTable.
     * 
     * 
     * @param comp - связываемый компонент.
     */
    @Override
    public void add(Component comp) {
        this.add( comp, null);
    }
    
    /**
     * Добавляет компонент к коллекции "связанных" компонент.<p>
     * 
     * 
     * @param comp типа java.awt.Component - связываемый компонент.
     * @param clumnName типа java.lang.String. Имя колонки объекта BaseDataTable.
     */
    @Override
    public void add(Component comp, String columnName) {
        if (binders.contains(comp) ) {
            throw new IllegalArgumentException("Allredy contains this component: " + comp.getClass().getName());
        }
        Binder binder = this.createBinder( comp, columnName);
        this.binders.put(comp,binder);
        
    }
    /**
     * Удаляет компонент из коллекции "связанных" компонент.<p>
     * При удалении из коллекции, удаляются назначенные при добавлении обработчики событий,
     * а также восстанавливаются значения свойств, которые изменяются автоматически при добавлении.
     * Такие свойства и обработчики  специфичны для каждой реализации Binder интерфейса. <p>
     * @param comp типа java.awt.Component - удаляемый связанный компонент.
     */
    @Override
    public void remove(Component comp) {
        Binder binder = (Binder)binders.get(comp);
        this.uninstallDataTableListeners(binder);
        binder.removing();
        this.binders.remove(comp);
    }

    
    /**
     * Вызывается сразу после изменением значения свойства dataTable.<p>
     * Метод выполняется даже, если старое значение и новое одни и теже. <p>
     * Просматривается вся коллекция binders и для каждого входящего в нее элемента binder типа Binder выполняется
     * метод binder.dataTableChanged, позволяя каждому элементу провести самостоятельную обработку. <p>
     * Выполняется методы <code>this.uninstallDataTableListeners(binder)</code>.
     * <code>binder.propertyChange</code> с целью начальной
     * установки на активную запись в BaseDataTable.<p>
     * 
     * 
     * @param newDataTable тип BaseDataTable. Новое значение свойства dataTable.
     */
    @Override
    public void dataTableChanged(Table newDataTable) {
        Enumeration elements = this.binders.elements();
        while ( elements.hasMoreElements() ) {
            Binder b = (Binder)elements.nextElement();
            b.dataTableChanged(newDataTable);
            if ( newDataTable != null ) {
                installDataTableListeners( b );
                
                int activeRow = newDataTable.getActiveRowIndex();
                Object value = b.getColumnName() == null ? null : dataTable.getRow(activeRow).getValue(b.getColumnName());
                b.activeRowChange(new ActiveRowEvent(this));
            }
            
        }//while
    }
    /**
     * Вызывается непосредственно перед изменением значения свойства dataTable.<p>
     * Метод выполняется даже, если старое значение и новое одни и теже. <p>
     * Просматривается вся коллекция binders и для каждого входящего в нее элемента binder типа Binder выполняется
     * метод <code>this.uninstallDataTableListeners(binder)</code>.
     * Выполняется также binder.dataTableChanging позволяя каждому элементу провести самостоятельную обработку. <p>
     * для созданного объекта Binder метод propertyChange с целью начальной
     * установки на активную запись в BaseDataTable.<p>
     * 
     * 
     * @param oldDataTable тип BaseDataTable. Старое значение свойства dataTable.
     * @param newDataTable тип BaseDataTable. Новое значение свойства dataTable.
     */
    @Override
    public void dataTableChanging(Table oldDataTable,Table newDataTable) {
        //oldDataTable = this.dataTable;
        if ( oldDataTable == null )
            return;
        Enumeration elements = this.binders.elements();
        while ( elements.hasMoreElements() ) {
            Binder b = (Binder)elements.nextElement();
            uninstallDataTableListeners(b);
            b.dataTableChanging(oldDataTable, newDataTable);
        }//while
    }
    // ***********************************************************************
    //  END OF IPositionmanager implementation
    // ***********************************************************************
    
    /**
     * Возвращает ссылку на hash-таблицу, содержащую пары значений имен классов
     * компонент и соответствующих им классов типа {@link pbind.Binder). <p>
     * Класс PosisionManager может провести связывание любого компонента типа
     * {@link java.awt.Component) только, если для класса этого компонента
     * существует класс, реализующий интерфейс {@link pbind.Binder} и этот класс
     * зарегистрирован PositionManager. Под регистрацией подразумевается занесение
     * пары <i>ключ/значение</i> в hash-таблицу. Ключом является полностью 
     * квалифицированное строковое значение имени класса компонента, а значением -
     * полностью квалифицированное строковое значение имени класса, реализующего
     * интерфейс pbind.Binder.
     *  
     * @return hash-тавлица соответствия имен классов компонента и его класса типа 
     *              pbind.Binder.
     */
    public Hashtable getBinderMap() {
        return this.binderMap;
    }
    
    /**
     * Возвращает устанавливает ссылку ссылку на hash-таблицу, содержащую пары 
     * значений имен классов компонент и соответствующих им классов типа 
     * {@link pbind.Binder). <p>
     * Класс <code>PosisionManager</code> может провести связывание любого компонента типа
     * {@link java.awt.Component) только, если для класса этого компонента
     * существует класс, реализующий интерфейс {@link pbind.Binder} и этот класс
     * зарегистрирован PositionManager. Под регистрацией подразумевается занесение
     * пары <i>ключ/значение</i> в hash-таблицу. Ключом является полностью 
     * квалифицированное строковое значение имени класса компонента, а значением -
     * полностью квалифицированное строковое значение имени класса, реализующего
     * интерфейс pbind.Binder.
     *
     * @param binderMap - новое устанавливаемое значение ссылки на hash-таблицу.
     */
    public void setBinderMap(Hashtable binderMap) {
        this.binderMap = binderMap;
    }
    
    /**
     * Создает экземпляр объекта типа {@link pbind.Binder}.<p>
     * Метод создает экземпляр объекта динамически, используя тип связываемого компонента. <p>
     * Полные имена типов компонент и соответствующие им имена типов, реализующих интерфейс Binder
     * представлены коллекцией binderMap - свойство типа Hashtable. <p>
     * Используя первый параметр, метод определяет имя типа связываемого компонента и обращается к свойству
     * binderMap для извлечения имени класса, реализующего интерфейс Binder. Класс загружается,
     * и инициализируется. При этом устанавливаются свойства полученного экземпляра Binder, 
     * такие как positionManager, columnName, component. <p>
     * Если свойство dataTable не равно null, то к объекту dataTable добавляются созданный Binder 
     * как обработчики событий событий:
     * 
     * {@link pdata.event.ActiveRowEvent} и {@link pdata.event.DataTableEvent}. <p>
     * 
     * Вызывается для созданного объекта Binder метод activeRowChanged с целью начальной
     * установки на активную запись в BaseDataTable.<p>
     * 
     * 
     * 
     * @param comp  - связываемый компонент.
     * @param columnName
     * @return созданный объект pbind.Binder.
     */
    protected Binder createBinder( Component comp, String columnName ) {
        Class cl = null;
        Binder binder = null;
        
        try {
            String bname = (String)this.binderMap.get(comp.getClass().getName());
            cl = Class.forName(bname);
        } catch ( ClassNotFoundException ex) {
            throw new IllegalArgumentException("Binder not found for component" + comp.getClass().getName() +"; " + ex.getMessage());
        }
        try {
            binder = (Binder)cl.newInstance();
            binder.setPositionManager(this);
            binder.setColumnName(columnName);
            binder.setComponent(comp);
            if ( dataTable != null ) {
                installDataTableListeners( binder );
                
                int activeRow = dataTable.getActiveRowIndex();
                Object value = columnName == null ? null : dataTable.getRow(activeRow).getValue(columnName);
                binder.activeRowChange(new ActiveRowEvent(this));
            }
            
        } catch ( Exception ex) {
            throw new IllegalArgumentException("Cannot instantiate binder for component " + comp.getClass().getName() +"; " + ex.getMessage());
        }
        
        return binder;
    }
    
    /**
     * Добавляются обработчики событий событий ActiveRowChangeEvent и DataTableEvent
     * к объекту dataTable.<p>
     * @param binder  - обработчик событий ActiveRowChangeEvent и DataTableEvent.
     */
    protected void installDataTableListeners( Binder binder ) {
        this.dataTable.addActiveRowListener(binder);
        //this.dataTable.addActiveRowListener(binder);
        this.dataTable.addTableListener(binder);
    }
    
    /**
     * Удаляются обработчики событий событий ActiveRowChangeEvent и DataTableEvent
     * объекта dataTable.<p>
     * @param binder типа Binder - удаляемый обработчик событий ActiveRowChangeEvent и DataTableEvent.
     */
    protected void uninstallDataTableListeners( Binder binder ) {
        this.dataTable.removeActiveRowListener(binder);
        this.dataTable.removeTableListener(binder);
    }
    
    
}//class
