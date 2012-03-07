/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.util;

/**
 *
 * @author Valery
 */
public class ListCell<E> {
    private int index;
    private E element;
    
    public E getElement() {
        return element;
    }

    public void setElement(E element) {
        this.element = element;
    }

    public int getIndex() {
        return this.index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
}
