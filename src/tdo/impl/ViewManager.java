/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdo.impl;

import tdo.DataRowCollection;

/**
 *
 * @author Valery
 */
public interface ViewManager {

    DataRowCollection getCurrentRows();
    ///////////////////////////////////////////////
    // Filterer
    ///////////////////////////////////////////////
    boolean filter(Filterer filterer);

    void cancelFilter(Filterer filterer);

    void cancelFilters();

    Filterer[] getFilterers();

    public Filterer getCurrentFilterer();
    ///////////////////////////////////////////////
    // Relation
    ///////////////////////////////////////////////
    boolean setRelation(Relation relation);

    Relation getRelation();
    ///////////////////////////////////////////////
    // Sorter
    ///////////////////////////////////////////////
    boolean sort(Sorter sorter);

    void cancelSort(Sorter sorter);

    void cancelSort();
    // TreeSorter
    ///////////////////////////////////////////////

    Sorter getCurrentSorter();    ///////////////////////////////////////////////
    TreeSorter sortTree(TreeSorter treeSorter);
    TreeSorter sortNode(TreeSorter treeSorter);
}//ViewManager
