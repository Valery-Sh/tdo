/*
 * Comparable.java
 * 
 * Created on 04.10.2007, 16:21:06
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.expr;

/**
 *
 * @author valery
 */
public interface Comparable {
    int compareObjects( Object obj, Object anotherObj );
    int compareObjects( Object obj, Object anotherObj, boolean nullMin );
}