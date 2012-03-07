/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo;


import java.util.ArrayList;
import tdo.service.CellCollectionServices;
import tdo.service.TableServices;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import tdo.service.CellServices;
import static org.junit.Assert.*;

/**
 * Test of the class {@link tdo.DefaultDataCellCollection}.
 * <code>tdo.DefaultDataCellCollection</code> object invokes some 
 * <code>tdo.Table</code> methods through services defined in 
 * {@link tdo.service.TableServices#getCellServices()}.
 * <p>For the test to execute we have created two internal classes that
 * emulate a real table object behavior:
 * <ol>
 *   <li>{@link DefaultDataCellCollectionTest.DefaultDataCellCollectionTableContext}</li>
 *   <li>{@link DefaultDataCellCollectionTest.CellServicesImpl}</li>
 * </ol>
 * 
 * 
 * 
 */
public class ObjectDataCellCollectionTest {
    

    TableServices context;
    CellServices cellService;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }
    
    public ObjectDataCellCollection createCells(boolean includeFields) {

        RowObjectFake rowObject = new RowObjectFake();
        context = new TableContextFake(rowObject,includeFields);
        cellService = (CellServices) context.getCellServices();
        return new ObjectDataCellCollection(this.context,new ObjectRowSupportFake(context, rowObject, includeFields));
   }
    public ObjectDataCellCollection createCells() {
        return this.createCells(false);
    }
    
    @Test
    public void testCopyCells() {
        //
        // signiture: copyCells(DataCellCollection)
        //
        ObjectDataCellCollection cells = createCells();
        ObjectDataCellCollection source = createCells();        
        
        source.setValue("cell0m", 0);
        cells.copyCells(source);
        assertEquals("cell0m",cells.getValue(0));        
        //
        // signiture: copyCells(Object[])
        //
        cells = createCells();
        String[] ssource = new String[] {"Hillary","Clinton","female"};
        cells.copyCells(ssource);
        assertEquals("Hillary",cells.getValue(0));
        assertEquals("Clinton",cells.getValue(1));        
        assertEquals("female",cells.getValue(2));        

        Object[] osource = new Object[] {"Hillary","Clinton","female",61};
        cells.copyCells(osource);
        assertEquals("Hillary",cells.getValue(0));
        assertEquals("Clinton",cells.getValue(1));        
        assertEquals("female",cells.getValue(2));        
        assertEquals(61,cells.getValue(3));        
        
    }
    @Test
    public void testGetPropertyValue() {
        // *********
        // includeFields == false 
        // *********
        ObjectDataCellCollection cells = createCells();
        assertEquals(null,(String)cells.getPropertyValue("firstName"));
        
        cells.setValue("cell1m", "LASTNAME");
        assertEquals("cell1m",(String)cells.getPropertyValue("lastName"));
        
        // *********
        // includeFields == true 
        // *********
        cells = createCells(true);
        assertEquals(null,(String)cells.getPropertyValue("notes"));
        
        cells.setValue("cellOfNotes", "NOTES");
        assertEquals("cellOfNotes",(String)cells.getPropertyValue("notes"));

        cells.setValue(200, "HEIGHT");
        assertEquals(200,(Integer)cells.getPropertyValue("height"));

        cells.setValue(null, "HEIGHT");
        assertEquals(0,(Integer)cells.getPropertyValue("height"));
        
    }

    @Test
    public void testGetValue() {
        // *********
        // includeFields == false 
        // *********
        
        //
        // signature: getValue(int)
        //
        ObjectDataCellCollection cells = createCells();
        assertEquals(null,cells.getValue(0));
        cells.setValue("Clinton", "LASTNAME");
        assertEquals("Clinton",cells.getValue(1));
        
        //
        // signature: getValue(String)
        //
        assertEquals(null,cells.getValue("FIRSTNAME"));
        assertEquals("Clinton",cells.getValue("LASTNAME"));

        // *********
        // includeFields == true
        // *********
        
        //
        // signature: getValue(int)
        //
        cells = createCells(true);
        assertEquals(null,cells.getValue(0));
        cells.setValue("Clinton", "LASTNAME");
        assertEquals("Clinton",cells.getValue(1));
        
        cells.setValue(170, "HEIGHT");        
        assertEquals(170,cells.getValue(5));
        
        //
        // signature: getValue(String)
        //
        assertEquals(null,cells.getValue("FIRSTNAME"));
        assertEquals("Clinton",cells.getValue("LASTNAME"));
        assertEquals(170,cells.getValue("HEIGHT"));
        
    }

    @Test
    public void testSetPropertyValue() {
        
        ObjectDataCellCollection cells = createCells();
        cells.setPropertyValue("firstName", "Hillary");
        assertEquals("Hillary",cells.getValue(0));
        cells.setPropertyValue("lastName", "Clinton");
        assertEquals("Clinton",cells.getValue(1));
        cells.setPropertyValue("gender", "female");
        assertEquals("female",cells.getValue(2));
        
    }
    
    @Test
    public void testSetValue() {
        //
        // signiture: setValue(String)
        //
        
        ObjectDataCellCollection cells = createCells();
        Object r = cells.setValue("Hillary","FIRSTNAME" );
        assertEquals("Hillary",cells.getValue(0)); // new value
        assertEquals(null,(String)r); // old value
        
        r = cells.setValue("Clinton","LASTNAME");
        assertEquals("Clinton",cells.getValue(1));
        assertEquals(null,(String)r);
        
        r = cells.setValue("female","GENDER");
        assertEquals("female",cells.getValue(2));
        assertEquals(null,(String)r);
        //
        // signiture: setValue(int)
        //
        cells = createCells();
        r = cells.setValue("Hillary",0 );
        assertEquals("Hillary",cells.getValue(0)); // new value
        assertEquals(null,(String)r); // old value
        
        r = cells.setValue("Clinton",1);
        assertEquals("Clinton",cells.getValue(1));
        assertEquals(null,(String)r);
        
        r = cells.setValue("female",2);
        assertEquals("female",cells.getValue(2));
        assertEquals(null,(String)r);
      
    }
    
    @Test
    public void testSize() {
        ObjectDataCellCollection cells = createCells();
        assertEquals(4, cells.size());
        cells = createCells(true);
        assertEquals(6, cells.size());
        
    }
   
   public class ObjectRowSupportFake implements ObjectValueAccessor{
       
        private TableContextFake context;
        private boolean includeFields;

        Object rowObject;
        
        ////
/*        List<String> columnNames;
        Map<String,Object> nameValueMap;
        Map<String,String> columnNameMap;        
        List<String> fieldNames;                
        List<String> propNames;                        
        private CellServices cellService;
*/        
        
        public ObjectRowSupportFake(TableServices context, Object rowObject, boolean includeFields) {
            
            this.context = (TableContextFake)context;
            this.includeFields = includeFields;
            this.rowObject = rowObject;
        } 
        protected void init() {
/*            columnNames = new ArrayList<String>(4);            
            fieldNames = new ArrayList<String>(2);            
            
            columnNames.add("FIRSTNAME");
            columnNames.add("LASTNAME");
            columnNames.add("GENDER");
            columnNames.add("AGE");
            
            propNames.add("firstNAME");
            propNames.add("lastNAME");
            propNames.add("gender");
            propNames.add("age");
            
            
            if ( this.includeFields ) {
                columnNames.add("NOTES");
                columnNames.add("HEIGHT");
                fieldNames.add("notes");
                fieldNames.add("height");
            }
            
            nameValueMap = new HashMap<String,Object>();
            
            nameValueMap.put("firstName", "Bill");
            nameValueMap.put("lastName", "Gates");         
            nameValueMap.put("gender", "male"); 
            nameValueMap.put("age", 61); 
            if ( this.includeFields ) {
                nameValueMap.put("notes", "MS President"); 
                nameValueMap.put("height", 182); 
            }
            
            
            columnNameMap = new HashMap<String,String>();
            
            columnNameMap.put("FIRSTNAME", "firstName");
            columnNameMap.put("LASTNAME", "lastName");
            columnNameMap.put("GENDER", "gender");
            columnNameMap.put("AGE", "age");            
            if ( this.includeFields ) {
                columnNameMap.put("NOTES", "notes");
                columnNameMap.put("HEIGHT", "height");
            }
  */          
            
        }
        public ObjectRowSupportFake(TableServices context, Map<String,Object> nameValueMap) {
            //this.nameValueMap = nameValueMap;;
            this.context = (TableContextFake)context;
        }
        
        
        @Override
        public Object getValue(String pname, Object bean) {
            Object r = null;
            if ( pname.equals("firstName"))
                r = context.rowObject.getFirstName();
            else if ( pname.equals("lastName"))
                r = context.rowObject.getLastName();
            else if ( pname.equals("gender"))
                r = context.rowObject.getGender();
            else if ( pname.equals("age"))
                r = context.rowObject.getAge();
            else if ( pname.equals("notes"))
                r = context.rowObject.notes;
            else if ( pname.equals("height"))
                r = context.rowObject.height;

            return r;
        }

        
        @Override
        public void setValue(String pname, Object bean, Object value) {
            if ( pname.equals("firstName"))
                context.rowObject.setFirstName((String)value);
            else if ( pname.equals("lastName"))
                context.rowObject.setLastName((String)value);
            else if ( pname.equals("gender"))
                context.rowObject.setGender((String)value);
            else if ( pname.equals("age"))
                context.rowObject.setAge( value==null ? 0 : (Integer)value);
            else if ( pname.equals("notes"))
                context.rowObject.notes = (String)value;
            else if ( pname.equals("height"))
                context.rowObject.height = value==null ? 0 : (Integer)value;

        }

        @Override
        public int size() {
            return context.fieldNames.size() + context.propNames.size();
        }

        @Override
        public boolean getIncludeFields() {
            return this.includeFields;
        }
       
   }//class ObjectRowSupportFake
   
   public class TableContextFake extends tdo.service.AbstractTableServices {
        

        List<String> fieldNames;                
        List<String> propNames;                        
        
        List<String> columnNames;
        Map<String,Object> nameValueMap;
        Map<String,String> columnNameMap;        
        private CellServices cellService;
        
        RowObjectFake rowObject;
        boolean includeFields;
                
        public TableContextFake(Object rowObject,boolean includeFields) {
            this.rowObject = (RowObjectFake)rowObject;
            this.includeFields = includeFields;
            
            columnNames = new ArrayList<String>(6);            
            fieldNames = new ArrayList<String>(2);            
            propNames = new ArrayList<String>(4);            
            
            columnNames.add("FIRSTNAME");
            columnNames.add("LASTNAME");
            columnNames.add("GENDER");
            columnNames.add("AGE");
            
            propNames.add("firstNAME");
            propNames.add("lastNAME");
            propNames.add("gender");
            propNames.add("age");
            
            
            if ( this.includeFields ) {
                columnNames.add("NOTES");
                columnNames.add("HEIGHT");
                fieldNames.add("notes");
                fieldNames.add("height");
            }
            
            columnNameMap = new HashMap<String,String>();
            
            columnNameMap.put("FIRSTNAME", "firstName");
            columnNameMap.put("LASTNAME", "lastName");
            columnNameMap.put("GENDER", "gender");
            columnNameMap.put("AGE", "age");            
            if ( this.includeFields ) {
                columnNameMap.put("NOTES", "notes");
                columnNameMap.put("HEIGHT", "height");
            }
            
            this.cellService = new CellServicesFake(this);
        }

        @Override
        public CellServices getCellServices() {
            return (CellServices) this.cellService;
        }

        @Override
        public CellCollectionServices getCellCollectionServices() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }//class ObjectDataCellCollectionTableContext
   public class CellServicesFake implements tdo.service.CellServices {

        TableContextFake context;

        public CellServicesFake(TableServices context) {
            this.context = (TableContextFake)context;
        }

        @Override
        public int getCellIndex(int columnIndex) {
            return columnIndex;
        }

        @Override
        public int getColumnIndex(String columnName) {
            return context.columnNames.indexOf(columnName);
        }

        @Override
        public int getColumnCount() {
            return this.context.columnNames.size();
        }

        @Override
        public boolean isDataKind(int columnIndex) {
           return true;
        }

        @Override
        public String getPropertyName(int columnIndex) {
            String columnName = context.columnNames.get(columnIndex);
            String pname = context.columnNameMap.get(columnName);
            return pname;
        }

        @Override
        public String getFieldName(int columnIndex) {
            String columnName = context.columnNames.get(columnIndex);
            String fname = context.columnNameMap.get(columnName);
            return fname;
        }

        @Override
        public Object createBlankObject(int columnIndex) {
            return "cell" + columnIndex;
        }
    }//class CellServicesImpl

    public class RowObjectFake {
        
        private String firstName;
        private String lastName;        
        private String gender;        
        
        private int age;

        public String notes;
        public int height;
        
        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
        
        
    }
}//class ObjectDataCellCollectionTest
