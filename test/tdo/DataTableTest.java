/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo;
import org.junit.Test;

/**
 *
 * @author Valery
 */
public class DataTableTest extends SimpleTableTestBase{

    @Override
    public Table createTable() {
        DataTable dt = new DataTable();
        dt.getColumns().add(String.class,"firstName");
        dt.getColumns().add(String.class,"lastName");        
        dt.getColumns().add(Integer.class);        

        return dt;
    }
    @Override
    public Table createNoSchemaTable() {
        DataTable dt = new DataTable();
        return dt;
    }
    
    @Test
    public void testIsLoading() {
        
    }
}
