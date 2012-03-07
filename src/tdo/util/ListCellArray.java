/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdo.util;

import java.util.ArrayList;
import java.util.Collection;
import tdo.impl.IndexEnabled;

/**
 *
 * @author Valery
 */
public class ListCellArray<E> extends ArrayList<E>
        implements java.io.Serializable {

    /**
     * Constructs an empty list with the specified initial capacity.
     *
     * @param   initialCapacity   the initial capacity of the list
     * @exception IllegalArgumentException if the specified initial capacity
     *            is negative
     */
    public ListCellArray(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Constructs an empty list with an initial capacity of ten.
     */
    public ListCellArray() {
        this(10);
    }

    /**
     * Constructs a list containing the elements of the specified
     * collection, in the order they are returned by the collection's
     * iterator.
     *
     * @param c the collection whose elements are to be placed into this list
     * @throws NullPointerException if the specified collection is null
     */
    public ListCellArray(Collection<? extends E> c) {
        super(c);
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * @param e element to be appended to this list
     * @return <tt>true</tt> (as specified by {@link Collection#add})
     */
    @Override
    public boolean add(E e) {
        super.add(e);
        updateIndexer(e, size() - 1);
        return true;
    }

    /**
     * Inserts the specified element at the specified position in this
     * list. Shifts the element currently at that position (if any) and
     * any subsequent elements to the right (adds one to their indices).
     *
     * @param index index at which the specified element is to be inserted
     * @param element element to be inserted
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public void add(int index, E element) {
        super.add(index, element);
        if (element instanceof IndexEnabled) {
            this.updateIndexer(index);
        }

    }

    protected void updateIndexer(E e, int index) {
        if (e instanceof IndexEnabled) {
            ((IndexEnabled) e).setIndex(index);
        }
    }

    protected void updateIndexer(int startIndex) {
        for (int i = startIndex; i < size(); i++) {
            ((IndexEnabled) get(i)).setIndex(i);
        }
    }

    /**
     * Appends all of the elements in the specified collection to the end of
     * this list, in the order that they are returned by the
     * specified collection's Iterator.  The behavior of this operation is
     * undefined if the specified collection is modified while the operation
     * is in progress.  (This implies that the behavior of this call is
     * undefined if the specified collection is this list, and this
     * list is nonempty.)
     *
     * @param c collection containing elements to be added to this list
     * @return <tt>true</tt> if this list changed as a result of the call
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean b = super.addAll(c);
        this.updateIndexer(size()-1);
        return b;
    }
    
    /**
     * Inserts all of the elements in the specified collection into this
     * list, starting at the specified position.  Shifts the element
     * currently at that position (if any) and any subsequent elements to
     * the right (increases their indices).  The new elements will appear
     * in the list in the order that they are returned by the
     * specified collection's iterator.
     *
     * @param index index at which to insert the first element from the
     *              specified collection
     * @param c collection containing elements to be added to this list
     * @return <tt>true</tt> if this list changed as a result of the call
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        boolean b = super.addAll(index, c);
        this.updateIndexer(index);
        return b;
    }
    /**
     * Removes the element at the specified position in this list.
     * Shifts any subsequent elements to the left (subtracts one from their
     * indices).
     *
     * @param index the index of the element to be removed
     * @return the element that was removed from the list
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public E remove(int index) {
        int oldSize = size();
        E e = super.remove(index);
        if ( ! isEmpty()) {
           if ( index < oldSize-1) {
               this.updateIndexer(index);
           }
        }

        return e;
    }
    /**
     * Removes from this list all of the elements whose index is between
     * <tt>fromIndex</tt>, inclusive, and <tt>toIndex</tt>, exclusive.
     * Shifts any succeeding elements to the left (reduces their index).
     * This call shortens the list by <tt>(toIndex - fromIndex)</tt> elements.
     * (If <tt>toIndex==fromIndex</tt>, this operation has no effect.)
     *
     * @param fromIndex index of first element to be removed
     * @param toIndex index after last element to be removed
     * @throws IndexOutOfBoundsException if fromIndex or toIndex out of
     *              range (fromIndex &lt; 0 || fromIndex &gt;= size() || toIndex
     *              &gt; size() || toIndex &lt; fromIndex)
     */
    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        int oldSize = size();
        super.removeRange(fromIndex, toIndex);
        if ( ! isEmpty()) {
           if ( fromIndex < oldSize-1) {
               this.updateIndexer(fromIndex);
           }
        }
    }
    
    /**
     * Replaces the element at the specified position in this list with
     * the specified element.
     *
     * @param index index of the element to replace
     * @param element element to be stored at the specified position
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public E set(int index, E element) {
        E oldValue = (E) get(index);
        E e = super.set(index, element);
        this.updateIndexer(element, index);
        return oldValue;
    }
    
}
