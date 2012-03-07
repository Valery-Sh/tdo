/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.impl;

import tdo.DataRow;
import tdo.service.TableServices;

/**
 *
 * @author valery
 */
public class DefaultRowSort {

    /** This is a generic version of C.A.R Hoare's Quick Sort
     * algorithm.  This will handle arrays that are already
     * sorted, and arrays with duplicate keys.<BR>
     *
     * If you think of a one dimensional array as going from
     * the lowest index on the left to the highest index on the right
     * then the parameters to this function are lowest index or
     * left and highest index or right.  The first time you call
     * this function it will be with the parameters 0, a.length - 1.
     *
     * @param a       a string array
     * @param lo0     left boundary of array partition
     * @param hi0     right boundary of array partition
     */
    void QuickSort(String a[], int lo0, int hi0) {
        int lo = lo0;
        int hi = hi0;
        String mid;

        if ( hi0 > lo0) {

         /* Arbitrarily establishing partition element as the midpoint of
          * the array.
          */
            mid = a[ ( lo0 + hi0 ) / 2 ];

            // loop through the array until indices cross
            while( lo <= hi ) {
            /* find the first element that is greater than or equal to
             * the partition element starting from the left Index.
             */
                while( ( lo < hi0 ) && ( a[lo].compareTo(mid) < 0 ) )
                    ++lo;

            /* find an element that is smaller than or equal to
             * the partition element starting from the right Index.
             */
                while( ( hi > lo0 ) && ( a[hi].compareTo(mid) > 0 ) )
                    --hi;

                // if the indexes have not crossed, swap
                if( lo <= hi ) {
                    String t = a[hi];
                    a[hi] = a[lo];
                    a[lo] = t;

                    ++lo;
                    --hi;
                }
            }

         /* If the right index has not reached the left side of array
          * must now sort the left partition.
          */
            if( lo0 < hi )
                QuickSort( a, lo0, hi );

         /* If the left index has not reached the right side of array
          * must now sort the right partition.
          */
            if( lo < hi0 )
                QuickSort( a, lo, hi0 );

        }

    }
    public void sort(TableServices services, DefaultViewComparator comp) {
        sort( services, comp, 0, services.getSorterServices().getRowCount()-1);
    }
/*    public void sortDesc(TableServices services, DefaultViewComparator comp) {
        sortDesc( services, comp, 0, services.getSorterServices().getRowCount()-1);
    }
*/
    public void sort(TableServices services, DefaultViewComparator comp, int lo0, int hi0) {
        int lo = lo0;
        int hi = hi0;
        DataRow mid;

        if ( hi0 > lo0) {

         /* Arbitrarily establishing partition element as the midpoint of
          * the array.
          */
            mid = services.getSorterServices().getRow( ( lo0 + hi0 ) / 2 );


            // loop through the array until indices cross
            while( lo <= hi ) {
            /* find the first element that is greater than or equal to
             * the partition element starting from the left Index.
             */
                //while( ( lo < hi0 ) && ( a[lo].compareTo(mid) < 0 ) )
                while( ( lo < hi0 ) && ( comp.compareRows(services.getSorterServices().getRow(lo), mid) < 0 ) )
                    ++lo;

            /* find an element that is smaller than or equal to
             * the partition element starting from the right Index.
             */
                //while( ( hi > lo0 ) && ( a[hi].compareTo(mid) > 0 ) )
                while( ( hi > lo0 ) && ( comp.compareRows(services.getSorterServices().getRow(hi), mid) > 0 ) )
                    --hi;

                // if the indexes have not crossed, swap
                if( lo <= hi ) {
                    DataRow t = services.getSorterServices().getRow(hi);
                    //a[hi] = a[lo];
                    //a[lo] = t;
                    services.getSorterServices().setRow( services.getSorterServices().getRow(lo), hi);
                    services.getSorterServices().setRow( t, lo);
                    ++lo;
                    --hi;
                }
            }

         /* If the right index has not reached the left side of array
          * must now sort the left partition.
          */
            if( lo0 < hi )
                sort( services, comp, lo0, hi );

         /* If the left index has not reached the right side of array
          * must now sort the right partition.
          */
            if( lo < hi0 )
                sort( services, comp, lo, hi0 );

        }

    }
/*
    public void sortDesc(TableServices services, DefaultViewComparator comp, int lo0, int hi0) {
        int lo = lo0;
        int hi = hi0;
        DataRow mid;

        if ( hi0 > lo0) {

            mid = services.getSorterServices().getRow( ( lo0 + hi0 ) / 2 );


            // loop through the array until indices cross
            while( lo <= hi ) {
            // find the first element that is greater than or equal to
            // the partition element starting from the left Index.
            //
                //while( ( lo < hi0 ) && ( a[lo].compareTo(mid) < 0 ) )
                while( ( lo < hi0 ) && ( comp.compareRows(services.getSorterServices().getRow(lo), mid) > 0 ) )

                    ++lo;

            // find an element that is smaller than or equal to
            // the partition element starting from the right Index.
            //
                //while( ( hi > lo0 ) && ( a[hi].compareTo(mid) > 0 ) )
                while( ( hi > lo0 ) && ( comp.compareRows(services.getSorterServices().getRow(hi), mid) < 0 ) )
                    --hi;

                // if the indexes have not crossed, swap
                if( lo <= hi ) {
                    DataRow t = services.getSorterServices().getRow(hi);
                    //a[hi] = a[lo];
                    //a[lo] = t;
                    services.getSorterServices().setRow( services.getSorterServices().getRow(lo), hi);
                    services.getSorterServices().setRow( t, lo);
                    ++lo;
                    --hi;
                }
            }

         // If the right index has not reached the left side of array
         // must now sort the left partition.
         //
            if( lo0 < hi )
                sortDesc( services, comp, lo0, hi );

         // If the left index has not reached the right side of array
         // must now sort the right partition.
         //
            if( lo < hi0 )
                sortDesc( services, comp, lo, hi0 );

        }

    }
*/
////////////////////////////////////////////
    public void sortAgg(TableServices services, int[] indice, DefaultViewAggComparator comp) {
        sortAgg( services, indice, comp, 0, services.getSorterServices().getRowCount()-1);
    }
/*    public void sortAggDesc(TableServices services, int[] indice, DefaultViewAggComparator comp) {
        sortAggDesc( services, indice, comp, 0, services.getSorterServices().getRowCount()-1);
    }
*/
    public void sortAgg(TableServices services, int[] indice, DefaultViewAggComparator comp, int lo0, int hi0) {
        int lo = lo0;
        int hi = hi0;
        DataRow mid;
        int midInd;

        if ( hi0 > lo0) {

         /* Arbitrarily establishing partition element as the midpoint of
          * the array.
          */
            //         mid = a[ ( lo0 + hi0 ) / 2 ];
            midInd = (lo0 + hi0 ) / 2 ;
            mid = services.getSorterServices().getRow( midInd );


            // loop through the array until indices cross
            while( lo <= hi ) {
            /* find the first element that is greater than or equal to
             * the partition element starting from the left Index.
             */
//                while( ( lo < hi0 ) && ( comp.compareRows(services.getSorterServices().getRow(lo),indice[lo], mid, indice[midInd]) < 0 ) )
//                    ++lo;
                while( lo < hi0 )
                  if ( comp.compareRows(services.getSorterServices().getRow(lo),indice[lo], mid, indice[midInd]) < 0 )
                    ++lo;
                  else
                    break;


            /* find an element that is smaller than or equal to
             * the partition element starting from the right Index.
             */

                while( ( hi > lo0 ) && ( comp.compareRows(services.getSorterServices().getRow(hi),indice[hi], mid, indice[midInd]) > 0 ) ) {
                    --hi;

                }
                // if the indexes have not crossed, swap
                if( lo <= hi ) {

                    DataRow t = services.getSorterServices().getRow(hi);
                    int tindice = indice[hi];
                    services.getSorterServices().setRow( services.getSorterServices().getRow(lo), hi);
                    services.getSorterServices().setRow( t, lo);
                    indice[hi] = indice[lo];
                    indice[lo] = tindice;
//
/*                     DataRow t = table.getRow(hi);
                    int tindice = indice[hi];
                    table.setRow( table.getRow(lo), hi);
                    table.setRow( t, lo);
                    indice[hi] = indice[lo];
                    indice[lo] = tindice;
 */
//
                    if ( lo == midInd ) {
                        midInd = hi;
                        mid = services.getSorterServices().getRow(  midInd );
                        //System.out.println("lo == midInd");
                    } else

                    if ( hi == midInd ) {
                        midInd = lo;
                        mid = services.getSorterServices().getRow(  midInd );
                        //System.out.println("hi == midInd");
                    }


                    ++lo;
                    --hi;
                }
            }

         /* If the right index has not reached the left side of array
          * must now sort the left partition.
          */
            if( lo0 < hi )
                sortAgg( services, indice, comp, lo0, hi );

         /* If the left index has not reached the right side of array
          * must now sort the right partition.
          */
            if( lo < hi0 )
                sortAgg( services, indice,comp, lo, hi0 );

        }

    }
/*
    public void sortAggDesc(TableServices services, int[] indice, DefaultViewAggComparator comp, int lo0, int hi0) {
        int my = 0;
        int lo = lo0;
        int hi = hi0;
        DataRow mid;
        int midInd;

        //DataRow loRow = view.getRow(lo);
        if ( hi0 > lo0) {

            //         mid = a[ ( lo0 + hi0 ) / 2 ];
            midInd = (lo0 + hi0 ) / 2 ;
            mid = services.getSorterServices().getRow(  midInd );

            // loop through the array until indices cross
            while( lo <= hi ) {
                //while( ( lo < hi0 ) && ( a[lo].compareTo(mid) < 0 ) )
                if ( my == 1)
                    my = 1;
                while( ( lo < hi0 ) && ( comp.compareRows(services.getSorterServices().getRow(lo),indice[lo], mid, indice[midInd])  > 0 ) )
                //while( ( lo < hi0 ) && ( comp.compareRows(services.getSorterServices().getRow(lo),lo, mid, midInd)  > 0 ) )
                    ++lo;

                //while( ( hi > lo0 ) && ( a[hi].compareTo(mid) > 0 ) )
               while( ( hi > lo0 ) && ( comp.compareRows(services.getSorterServices().getRow(hi),indice[hi], mid, indice[midInd]) < 0 ) )
               // while( ( hi > lo0 ) && ( comp.compareRows(services.getSorterServices().getRow(hi),hi, mid, midInd) < 0 ) )
                    --hi;
                // if the indexes have not crossed, swap
                if( lo <= hi ) {
                    DataRow t = services.getSorterServices().getRow(hi);
                    int tindice = indice[hi];
                    services.getSorterServices().setRow( services.getSorterServices().getRow(lo), hi);
                    services.getSorterServices().setRow( t, lo);
                    indice[hi] = indice[lo];
                    indice[lo] = tindice;

                    if ( lo == midInd ) {
                        midInd = hi;
                        mid = services.getSorterServices().getRow(  midInd );
                        //System.out.println("lo == midInd");
                    } else if ( hi == midInd ) {
                        midInd = lo;
                        mid = services.getSorterServices().getRow(  midInd );
                        //System.out.println("hi == midInd");
                    }
                    my++;
                    ++lo;
                    --hi;
                    //return;
                }
            }

            if( lo0 < hi )
                sortAggDesc( services, indice,comp, lo0, hi );

            if( lo < hi0 )
                sortAggDesc( services, indice,comp, lo, hi0 );
        }
    }
 */
}//class
