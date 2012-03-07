/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.impl;

import tdo.DataRow;
import tdo.impl.TreeBuilder.TreeNodeInfo;
import tdo.service.TableServices;
import tdo.service.TreeSorterServices;

/**
 *
 * @author valery
 */
public class DefaultTreeSorter extends DefaultSorter implements TreeSorter {

    protected int minRowIndex;
    protected int maxRowIndex;
    /**
     * Применяется для указания ряда узла агрегатной таблицы, данные которого
     * сортируются. Если равен -1, то применяются свойства minRowIndex и
     * maxRowIndex.
     */
    protected DataRow nodeRow;
    protected TreeNodeInfo treeNodeInfo;

    protected TreeSorterServices treeSorterServices;

    /** Creates a new instance of AggSorter */
    public DefaultTreeSorter() {
        this(null);
    }

/*    public DefaultTreeSorter(SimpleInternalView dataView) {
        super(dataView);
        nodeRow = null;
        treeNodeInfo = ((TreeTable) dataView.getTable()).getTreeNodeInfo();
    }
 */
    public DefaultTreeSorter(TableServices services) {
        super(services);
        nodeRow = null;
        this.treeSorterServices = (TreeSorterServices)services.getTreeSorterServices();
        treeNodeInfo = treeSorterServices.getTreeNodeInfo();
    }

    @Override
    public DataRow getNodeRow() {
        return this.nodeRow;
    }
    /**
     *
     */
    public void setNodeRow(DataRow row) {
        this.nodeRow = row;
    }

    @Override
    public TreeNodeInfo getTreeNodeInfo() {
        return this.treeNodeInfo;
    }

    public void setTreeNodeInfo(TreeNodeInfo info) {
        this.treeNodeInfo = info;
        minRowIndex = 0;
        maxRowIndex = treeSorterServices.getRowCount() - 1;


        if (treeNodeInfo.getTotalsPosition() == TreeBuilder.TOTALS_TOP) {
            minRowIndex++;
        }
        if (treeNodeInfo.getTotalsPosition() == TreeBuilder.TOTALS_BOTTOM) {
            maxRowIndex--;
        }
        if (treeNodeInfo.getTotalsPosition() == TreeBuilder.TOTALS_BOTH) {
            minRowIndex++;
            maxRowIndex--;
        }

    }

    @Override
    public void sort() {
        minRowIndex = 0;
        maxRowIndex = treeSorterServices.getRowCount() - 1;

        if (treeNodeInfo.getTotalsPosition() == TreeBuilder.TOTALS_TOP) {
            minRowIndex++;
        }
        if (treeNodeInfo.getTotalsPosition() == TreeBuilder.TOTALS_BOTTOM) {
            maxRowIndex--;
        }
        if (treeNodeInfo.getTotalsPosition() == TreeBuilder.TOTALS_BOTH) {
            minRowIndex++;
            maxRowIndex--;
        }

        sort(minRowIndex, maxRowIndex);
    }

    @Override
    public void sort(int minRow, int maxRow) {
        DefaultRowSort rs = new DefaultRowSort();
        DefaultViewAggComparator comp = new DefaultViewAggComparator(this.getTableServices(),this.getColumnList(),this.getSortDirection());
        //comp.setTableServices(this.getTableServices());

        treeNodeInfo.createIndice();
        comp.setTreeNodeInfo(treeNodeInfo);
        rs.sortAgg(this.getTableServices(), treeNodeInfo.getRowIndice(), comp, minRow, maxRow);
/*        if (this.getSortDirection() == Sorter.DESCENDING) {
            rs.sortAgg(this.getTableServices(), treeNodeInfo.getRowIndice(), comp, minRow, maxRow);
        } else {
            rs.sortAgg(this.getTableServices(), treeNodeInfo.getRowIndice(), comp, minRow, maxRow);
        }
 */
    }
}//class

