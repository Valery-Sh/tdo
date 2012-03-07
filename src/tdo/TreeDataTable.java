/*
 * TreeDataTable.java
 * 
 */

package tdo;

import tdo.impl.DefaultTreeSorter;
import tdo.impl.Sorter;
import tdo.impl.TreeBuilder.TreeNodeInfo;
import tdo.impl.TreeSorter;

/**
 *
 * @author valery
 */
public class TreeDataTable extends DataTable implements TreeTable{
    private TreeNodeInfo treeNodeInfo;
    
    @Override
    public TreeNodeInfo getTreeNodeInfo() {
        return this.treeNodeInfo;
    }

    @Override
    public void setTreeNodeInfo(TreeNodeInfo treeNodeInfo) {
        this.treeNodeInfo = treeNodeInfo;
    }
    @Override
    public TreeSorter sortTree(String columnList, boolean direction) {
        if (columnList == null || columnList.length() == 0) {
            return null;
        }
        TreeSorter treeSorter = new DefaultTreeSorter(getContext());
        treeSorter.setColumnList(columnList);
        treeSorter.setSortDirection(direction);



        //TreeSorter aggSorter = ((TreeSortView)view).sortTree(columnList, direction);
        view.getViewManager().sortTree(treeSorter);
        internalMoveTop();
        fireDataModelAllRows(this);
        return treeSorter;
    }
    @Override
    public TreeSorter sortTree(String columnList) {
        return this.sortTree(columnList, Sorter.ASCENDING);
    }
    @Override
    public TreeSorter sortNode(int rowIndex, String columnList, boolean direction) {
        if (columnList == null || columnList.length() == 0) {
            return null;
        }
        TreeSorter treeSorter = new DefaultTreeSorter(getContext());
        treeSorter.setColumnList(columnList);
        treeSorter.setSortDirection(direction);
        DataRow row = getRow(rowIndex);
        ((DefaultTreeSorter)treeSorter).setNodeRow(row);

        //TreeSorter aggSorter = ((TreeSortView)view).sortTree(columnList, direction);
        view.getViewManager().sortNode(treeSorter);
        internalMoveTop();
        fireDataModelAllRows(this);
        return treeSorter;

    }
/*    public TreeSorter sortNode(int rowIndex, String columnList, boolean direction) {
        if (columnList == null || columnList.length() == 0) {
            return null;
        }

        TreeSorter sorter = ((TreeSortView)view).sortNode(rowIndex, columnList, direction);
        internalMoveTop();
        fireDataModelAllRows(this);
        return sorter;
    }
*/
    @Override
    public TreeSorter sortNode(int rowIndex, String columnList) {
        return this.sortNode(rowIndex, columnList, Sorter.ASCENDING);
    }

/*
    @Override
    public void fireTable(TableEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fireDataModel(TableEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
*/
/*    public TreeBuilder.TreeNodeInfo crateTreeNodeInfo() {
        return new TreeBuilder.TreeNodeInfo(this);
    }
*/
}
