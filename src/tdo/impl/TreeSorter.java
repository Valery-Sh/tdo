/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.impl;

import tdo.DataRow;
import tdo.impl.TreeBuilder.TreeNodeInfo;

/**
 *
 * @author valery
 */
public interface TreeSorter extends Sorter {
    TreeNodeInfo getTreeNodeInfo();
    DataRow getNodeRow();
    void sort(int minRow, int maxRow);

}
