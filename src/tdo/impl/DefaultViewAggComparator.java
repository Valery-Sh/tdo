/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.impl;

import tdo.DataRow;
import tdo.impl.TreeBuilder.TreeNodeInfo;
import tdo.service.TableServices;

/**
 *
 * @author valery
 */
public class DefaultViewAggComparator extends DefaultViewComparator{

    private TreeNodeInfo sourceTableNodeInfo;
    protected int[] priorLevelRowIndex;
    protected int[] indice;

    private boolean sortDirection;

/*    public DefaultViewAggComparator() {
        super();
    }

    public DefaultViewAggComparator(String columnList ) {
        super(columnList);
    }
    public DefaultViewAggComparator(String columnList, boolean sortDirection ) {
        //super(columnList);        
        super(columnList,sortDirection);
        this.sortDirection = sortDirection;
    }
 */
    public DefaultViewAggComparator(TableServices services,String columnList, boolean sortDirection ) {
        //super(columnList);        
        super(services,columnList,sortDirection);
        this.sortDirection = sortDirection;
    }
/*
    public DefaultViewAggComparator(String columnList, int level0, int level1 ) {
        super(columnList);
    }
 */ 
    int nn = 0;
    public int compareRows(DataRow row, int ind, DataRow aRow, int aInd) {
        int r;//My 06.03.2012 = 0;

        int LR1 = sourceTableNodeInfo.getLevel(ind);
        int LR2 = sourceTableNodeInfo.getLevel(aInd);
        //int LR1 = row.getState().getDepth();
        //int LR2 = aRow.getState().getDepth();

        int IR1 = ind;
        int IR2 = aInd;

System.out.println("N: " + (++nn) + "; compareRows: IR1=" + IR1 +
        "; IR2=" + IR2 + "; LR1=" + LR1 + "; LR2="+LR2 +"; PR1=" + priorLevelRowIndex[IR1]);

        if ( row == aRow)
            r = 0;
        else
        if ( LR1 != LR2 )
            r = compDifLevels(row,IR1,aRow,IR2,LR1, LR2);
        else
            r = compEqualLevels(row,IR1,aRow,IR2,LR1, LR2);

        return r;
    }

    public int compDifLevels(DataRow row, int IR1, DataRow aRow, int IR2, int LR1, int LR2) {
        int r;//My 06.03.2012 = 0;
//System.out.println("N: " + (++nn) + "; compDifLevels: IR1=" + IR1 +
//        "; IR2=" + IR2 + "; LR1=" + LR1 + "; LR2="+LR2 +"; PR1=" + priorLevelRowIndex[IR1] +
//        "row.index=" + row.getIndex() + "; aRow.index=" + aRow.getIndex());
    if ( nn == 44 ) {
         System.out.println("");
         //throw new RuntimeException("ssss");
    }

        if ( LR1 > LR2 ) {

            //int PR1 = priorLevelRowIndex[indice[IR1]];
            //r = compareRows(sourceTableNodeInfo.getRow(PR1), sourceTableNodeInfo.getRow(PR1).getIndex(), aRow, IR2);
            int PR1 = priorLevelRowIndex[IR1];
if ( PR1 < 0 )
    System.out.println("");
            r = compareRows(sourceTableNodeInfo.getRow(PR1), PR1, aRow, IR2);

            // r может быть равно 0, если сравниваемые ряда равны как ОБЪЕКТЫ !!!
            if ( r== 0 ) {
                if ( isGroupAfter() && isDescending() ) {
                    r = 1;
                } else {
                if ( isGroupAfter() && ! isDescending() ) {
                    r = -1;
                } else
                if ( (! isGroupAfter()) && (! isDescending()) )
                    r = 1;
                else
                if ( (! isGroupAfter()) && isDescending() )
                    r = -1;
                }
            }
        } //if ( LR1 > LR2 ) {
        else {
            //*****  LR1 < LR2 *****
            //int PR2 = priorLevelRowIndex[indice[IR2]];
            //r = compareRows(row, IR1,sourceTableNodeInfo.getRow(PR2),sourceTableNodeInfo.getRow(PR2).getIndex());


            int PR2 = priorLevelRowIndex[IR2];
            r = compareRows(row, IR1,sourceTableNodeInfo.getRow(PR2),PR2);

            if ( r == 0 ) {
                if ( isGroupAfter() && isDescending() )
                    r = -1;
                else
                    if ( isGroupAfter() && ! isDescending() )
                        r = 1;
                    else
                        if ( (! isGroupAfter()) && (! isDescending()) )
                            r = -1;
                        else
                            if ( (! isGroupAfter()) && isDescending() )
                                r = 1;
            }
        }
        return r;
    }//compDifLevels()

    /**
     *
     */
    public int compEqualLevels(DataRow row, int IR1, DataRow aRow, int IR2, int LR1, int LR2) {
        int r;//My 06.03.2012 = 0;
        int PR1 = priorLevelRowIndex[IR1];
        int PR2 = priorLevelRowIndex[IR2];
//System.out.println("N: " + (++nn) + "; compEqualLevels: IR1=" + IR1 +
//        "; IR2=" + IR2 + "; LR1=" + LR1 + "; LR2="+LR2 +"; PR1=" + PR1 +"; PR2=" + PR2);
        if (PR1 == PR2) {
            r = super.compareRows(row,aRow);
            if ( r == 0 )
                r = compEqualRows(IR1,IR2);
        } else {
            // PR1 != PR2
            //r = compareRows(sourceTableNodeInfo.getRow(PR1),PR1, sourceTableNodeInfo.getRow(PR2),PR2);
            r = compareRows(sourceTableNodeInfo.getRow(PR1),PR1, sourceTableNodeInfo.getRow(PR2),PR2);
        }

        return r;
    }

    /**
     * Сравнение двух рядов, которые НЕ равны как объекты, но равны по
     * значениям полей сортировки.<p>
     */
    protected int compEqualRows(int IR1, int IR2) {
        int r = 0;
        if ( isGroupAfter() && isDescending() ) {
            if ( IR1 < IR2)
                r = 1;
            else
                r = -1;
        } else {
            if (isGroupAfter() && ! isDescending() ) {
                if ( IR1 > IR2)
                    r = 1;
                else
                    r = -1;
            } else {
                if ( ! isGroupAfter() && ! isDescending() ) {
                    if ( IR1 > IR2)
                        r = 1;
                    else
                        r = -1;

                } else {
                    if ( ! isGroupAfter() && isDescending() ) {
                        if ( IR1 < IR2)
                            r = 1;
                        else
                            r = -1;
                    }
                }
            }
        }
        return r;
    }


    protected boolean isGroupAfter() {
        return this.sourceTableNodeInfo.getGroupMode();
    }

    protected boolean isDescending() {
        //return this.getView().getAggSorter().getSortDirection() == Sorter.DESCENDING;
    //    return this.sourceTableNodeInfo.getSortDirection() == Sorter.DESCENDING;
        return this.sortDirection == Sorter.DESCENDING;
    }
    int[] oindice;
    public void setTreeNodeInfo( TreeNodeInfo state ) {
        this.sourceTableNodeInfo = state;
        state.createIndice();
        priorLevelRowIndex = state.getLevelIndice();

        indice = state.getRowIndice(); // currentRow -> originalRow
        oindice = new int[indice.length];
        System.arraycopy(indice, 0, oindice, 0, indice.length);

    }
}//class

