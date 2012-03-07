/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo;

/**
 *
 */
public interface ObjectValueAccessor {
    Object getValue(String pname, Object bean);
    void setValue(String pname, Object bean,Object value);    
    boolean getIncludeFields();
    int size();
}
