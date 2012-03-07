/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.service;

import tdo.Table;
import tdo.TreeTable;
import tdo.impl.TreeBuilder.TreeNodeInfo;

/**
 *
 * @author Valery
 */
public class TreeSorterServicesImpl extends SorterServicesImpl implements TreeSorterServices{

    //protected TreeSorterServicesImpl() {
    //}
    public TreeSorterServicesImpl(Table table) {
        super(table);
    }

    @Override
    public TreeNodeInfo getTreeNodeInfo() {
        return ((TreeTable)table).getTreeNodeInfo();
    }

}
