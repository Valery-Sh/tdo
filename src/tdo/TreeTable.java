/*
 * TreeTable.java
 *
 */

package tdo;

import tdo.impl.Sorter;
import tdo.impl.TreeBuilder.TreeNodeInfo;
import tdo.impl.TreeSorter;

/**
 *
 * @author valery
 */
public interface TreeTable extends Table {

    TreeNodeInfo getTreeNodeInfo();
    void setTreeNodeInfo(TreeNodeInfo treeNodeInfo);
    TreeSorter sortTree(String columnList, boolean direction);
    TreeSorter sortTree(String columnList);
    TreeSorter sortNode(int rowIndex, String columnList, boolean direction);
    TreeSorter sortNode(int rowIndex, String columnList);
    void cancelSort(Sorter sorter);

} //interface TreeNodeInfo