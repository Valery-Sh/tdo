/*
 * ValueList.java
 *
 * Created on 11.06.2007, 22:03:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.expr;

import java.util.Vector;
import tdo.tools.expr.LexConst;

/**
 *
 * @author Valera
 */
public class ValueList {
    private Vector list;
    public ValueList() {
        this(5);
    }
    public ValueList(int capacity) {
        list = new Vector(capacity);
    }
    
    public Object getValue() {
        return this;
    }
    
    public int getType() {
        return LexConst.VALUELIST;
    }
    
    public void add(Object value) {
        list.addElement(value);
    }
    public int size() {
        return list.size();
    }
    public Object get(int index) {
        return list.elementAt(index);
    }
    public void set(int index, Object value) {
        list.setElementAt(value, index);
    }

    public boolean contains(Object o ) {
        return list.contains(o);
    }
    @Override
    public String toString() {
        String s = "{";
        for ( int i=0; i < size(); i++ ) {
            s += get(i).toString();
            if ( i != size() - 1 )
                s += ",";
        }
        s += "}";
        return s;
    }
    
}
