/*
 * PositionManager.java
 *
 * Created on 29 ������� 2006 �., 10:27
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
 * ����� ��������� ��������� {@link pbind.IPositionManager} � ������������� 
 * �������� ��� ���������� ������� ���� {@link pdata.BaseDataTable} � ������ ��� ����������
 * ��������� ���� {@link java.awt.Component}. <p>
 * ���������� ������. <p>
 * ����������� �� ����������� desktop ���������� ����� ��������
 * �������� ���������� javax.swing.JTable, ������ �� ������� �������� � ����������
 * jtable1, javax.swing.JLabel, ������ �� ������� �������� � ���������� jlabel1.
 * 
 * �������� ������ ���� pdata.BaseDataTable
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
 *  �������� ����� ���� ���������� ��������:
 * 
 *  <code><pre>
 *  7.  IPositionManager pm = new PositionManager(empl);
 *  8.  pm.add(jtable1);
 *  9.  pm.add(jlabel1,"LastName");  
 *  </pre></code>
 * 
 * �������� ����������. ��������� ����� �� jtable1, � ��������� ���� � (���) ���������� 
 * , ����� ������������ �� ����� �������. C ���������� ��������� ������
 * jtable1 ������������� �������� ����� ���������� jlabel1. <p>
 * ���� �������� �������� � �����-���� ������ jtable1, ��������������� ������� LastName,
 * ��������, �������� "Fisher" � ������ 1 ������� �� "Bob", ��  ���������� ����� ��������
 * ������ � jlabel1.  <p>
 * � ������ 7 ���� ��������� ��������� PositionManager � �������������� ������������,
 * ������������ � �������� ��������� ������ �� ������� <code>empl</code>. � ������ 8
 * ��������� <code>jtable1</code> ����������� � PositionManager �, �������������, �����������
 * � �������� <code>empl</code>. � ������ 9 � PositionManager ��������� ��������� 
 * <code>jlabel1</code>, ��������, ��� ����� �������������� ��� ������� � ������ "LastName".
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
     * ������������� �������� ������ ������� {@link pdata.BaseDataTable}.<p>
     * �������� ����� {@link pdata.BaseDataTable.setValue} ������ BaseDataTable. ���� ��� ���� ���������
     * ���������� ��������, �� ��� ��������������� � ����� �� �������������.<p>
     * 
     * 
     * @param value  - ����� �������� ������.
     * @param rowIndex -  ������ ����������� ����.
     * @param columnIndex -  ������ ������� ����������� ����.
     * @return true - ���� ����� setValue ������ DBaseDataTable�� ��������
     * ����������. false - � ��������� ������.
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
     * ������������� ����� �������� ��������� ���� ������� <code>BaseDataTable</code>. <p>
     * 
     * 
     * @param rowIndex  ������ ������ ��������� ����.
     * @return true, ���� ����� {@link pdata.BaseDataTable#moveTo(int)} �� ��������
     * ����������. false - � ��������� ������.
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
     * ���������� �������� ������ �� ��������� �������. <p>
     * 
     * 
     * @return ��������� ������� ���� {@link pdata.BaseDataTable}.
     */
    @Override
    public Table getTable() {
        return this.dataTable;
    }
    
    /**
     * ������������� �������� �������� ��� ����������� �������. <p>
     * ����� ���������� ������ �������� ���������� �����  {@link #dataTableChanging}.
     * ����� ���������� ������ �������� ���������� ����� {@link #dataTableChanged}.
     *
     * @param dataTable ��������������� �������� ������ �� ����������� �������.
     */
    @Override
    public void setTable( Table dataTable) {
        dataTableChanging( this.dataTable, dataTable);
        this.dataTable = dataTable;
        dataTableChanged(dataTable);
    }
    /**
     * ��������� ��������� � ��������� "���������" ���������.<p>
     * ������ �������� ������������� ����� add �� ��������� null ������� ���������.<p>
     * ����� �������� � ������, ����� ��������� ����������� � BaseDataTable ��� �������������
     * ����� ���� �������. ����� ��������, ��������, JTable.
     * 
     * 
     * @param comp - ����������� ���������.
     */
    @Override
    public void add(Component comp) {
        this.add( comp, null);
    }
    
    /**
     * ��������� ��������� � ��������� "���������" ���������.<p>
     * 
     * 
     * @param comp ���� java.awt.Component - ����������� ���������.
     * @param clumnName ���� java.lang.String. ��� ������� ������� BaseDataTable.
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
     * ������� ��������� �� ��������� "���������" ���������.<p>
     * ��� �������� �� ���������, ��������� ����������� ��� ���������� ����������� �������,
     * � ����� ����������������� �������� �������, ������� ���������� ������������� ��� ����������.
     * ����� �������� � �����������  ���������� ��� ������ ���������� Binder ����������. <p>
     * @param comp ���� java.awt.Component - ��������� ��������� ���������.
     */
    @Override
    public void remove(Component comp) {
        Binder binder = (Binder)binders.get(comp);
        this.uninstallDataTableListeners(binder);
        binder.removing();
        this.binders.remove(comp);
    }

    
    /**
     * ���������� ����� ����� ���������� �������� �������� dataTable.<p>
     * ����� ����������� ����, ���� ������ �������� � ����� ���� � ����. <p>
     * ��������������� ��� ��������� binders � ��� ������� ��������� � ��� �������� binder ���� Binder �����������
     * ����� binder.dataTableChanged, �������� ������� �������� �������� ��������������� ���������. <p>
     * ����������� ������ <code>this.uninstallDataTableListeners(binder)</code>.
     * <code>binder.propertyChange</code> � ����� ���������
     * ��������� �� �������� ������ � BaseDataTable.<p>
     * 
     * 
     * @param newDataTable ��� BaseDataTable. ����� �������� �������� dataTable.
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
     * ���������� ��������������� ����� ���������� �������� �������� dataTable.<p>
     * ����� ����������� ����, ���� ������ �������� � ����� ���� � ����. <p>
     * ��������������� ��� ��������� binders � ��� ������� ��������� � ��� �������� binder ���� Binder �����������
     * ����� <code>this.uninstallDataTableListeners(binder)</code>.
     * ����������� ����� binder.dataTableChanging �������� ������� �������� �������� ��������������� ���������. <p>
     * ��� ���������� ������� Binder ����� propertyChange � ����� ���������
     * ��������� �� �������� ������ � BaseDataTable.<p>
     * 
     * 
     * @param oldDataTable ��� BaseDataTable. ������ �������� �������� dataTable.
     * @param newDataTable ��� BaseDataTable. ����� �������� �������� dataTable.
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
     * ���������� ������ �� hash-�������, ���������� ���� �������� ���� �������
     * ��������� � ��������������� �� ������� ���� {@link pbind.Binder). <p>
     * ����� PosisionManager ����� �������� ���������� ������ ���������� ����
     * {@link java.awt.Component) ������, ���� ��� ������ ����� ����������
     * ���������� �����, ����������� ��������� {@link pbind.Binder} � ���� �����
     * ��������������� PositionManager. ��� ������������ ��������������� ���������
     * ���� <i>����/��������</i> � hash-�������. ������ �������� ��������� 
     * ����������������� ��������� �������� ����� ������ ����������, � ��������� -
     * ��������� ����������������� ��������� �������� ����� ������, ������������
     * ��������� pbind.Binder.
     *  
     * @return hash-������� ������������ ���� ������� ���������� � ��� ������ ���� 
     *              pbind.Binder.
     */
    public Hashtable getBinderMap() {
        return this.binderMap;
    }
    
    /**
     * ���������� ������������� ������ ������ �� hash-�������, ���������� ���� 
     * �������� ���� ������� ��������� � ��������������� �� ������� ���� 
     * {@link pbind.Binder). <p>
     * ����� <code>PosisionManager</code> ����� �������� ���������� ������ ���������� ����
     * {@link java.awt.Component) ������, ���� ��� ������ ����� ����������
     * ���������� �����, ����������� ��������� {@link pbind.Binder} � ���� �����
     * ��������������� PositionManager. ��� ������������ ��������������� ���������
     * ���� <i>����/��������</i> � hash-�������. ������ �������� ��������� 
     * ����������������� ��������� �������� ����� ������ ����������, � ��������� -
     * ��������� ����������������� ��������� �������� ����� ������, ������������
     * ��������� pbind.Binder.
     *
     * @param binderMap - ����� ��������������� �������� ������ �� hash-�������.
     */
    public void setBinderMap(Hashtable binderMap) {
        this.binderMap = binderMap;
    }
    
    /**
     * ������� ��������� ������� ���� {@link pbind.Binder}.<p>
     * ����� ������� ��������� ������� �����������, ��������� ��� ������������ ����������. <p>
     * ������ ����� ����� ��������� � ��������������� �� ����� �����, ����������� ��������� Binder
     * ������������ ���������� binderMap - �������� ���� Hashtable. <p>
     * ��������� ������ ��������, ����� ���������� ��� ���� ������������ ���������� � ���������� � ��������
     * binderMap ��� ���������� ����� ������, ������������ ��������� Binder. ����� �����������,
     * � ����������������. ��� ���� ��������������� �������� ����������� ���������� Binder, 
     * ����� ��� positionManager, columnName, component. <p>
     * ���� �������� dataTable �� ����� null, �� � ������� dataTable ����������� ��������� Binder 
     * ��� ����������� ������� �������:
     * 
     * {@link pdata.event.ActiveRowEvent} � {@link pdata.event.DataTableEvent}. <p>
     * 
     * ���������� ��� ���������� ������� Binder ����� activeRowChanged � ����� ���������
     * ��������� �� �������� ������ � BaseDataTable.<p>
     * 
     * 
     * 
     * @param comp  - ����������� ���������.
     * @param columnName
     * @return ��������� ������ pbind.Binder.
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
     * ����������� ����������� ������� ������� ActiveRowChangeEvent � DataTableEvent
     * � ������� dataTable.<p>
     * @param binder  - ���������� ������� ActiveRowChangeEvent � DataTableEvent.
     */
    protected void installDataTableListeners( Binder binder ) {
        this.dataTable.addActiveRowListener(binder);
        //this.dataTable.addActiveRowListener(binder);
        this.dataTable.addTableListener(binder);
    }
    
    /**
     * ��������� ����������� ������� ������� ActiveRowChangeEvent � DataTableEvent
     * ������� dataTable.<p>
     * @param binder ���� Binder - ��������� ���������� ������� ActiveRowChangeEvent � DataTableEvent.
     */
    protected void uninstallDataTableListeners( Binder binder ) {
        this.dataTable.removeActiveRowListener(binder);
        this.dataTable.removeTableListener(binder);
    }
    
    
}//class
