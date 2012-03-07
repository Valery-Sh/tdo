/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo;

/**
 *
 * @author Valery
 */
public class CollectionFactory {
    public static CollectionFactory newInstance() {
        return new CollectionFactory();
    }
    public DataRowCollection newRowCollection() {
        return new DefaultDataRowCollection();
    }
    
}//class CollectionFactory
