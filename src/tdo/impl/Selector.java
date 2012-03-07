/*
 * Selector.java
 *
 * Created on 8 ������� 2006 �., 9:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.impl;

import tdo.Table;

/**
 *
 * @author valery
 */
public class Selector extends TreeBuilder{
    
    public Selector(Table source) {
        super(source);
        
        setTotalsPosition(TOTALS_NONE);
        this.setGroupMode(AFTER);
        this.setIncludeDetailRows(true);
    }
    
    @Override
    public TreeNodeInfo createTreeNodeInfo() {
        return null;
    }
    @Override
    protected void createGroupParts() {
        
        if ( sortColumnInfo == null ) {
            groupParts = null;
            return;
        }
            
        groupParts = new GroupPart[1];
        int id = groupParts.length - 1;
        SortColumnInfo[] keys = new SortColumnInfo[sortColumnInfo.length];
        System.arraycopy(sortColumnInfo, 0, keys, 0, keys.length);
        groupParts[0] = new GroupPart(keys);
        //groupParts[0].id = id;
    }

    /**
     * �������������� ������ ������ ��� ���������� ������ {@link #execute}.
     * ����� ������ � ������ execute() ������, ��������� �������� ��������
     * ��� (execute) , ��� ������������� ��������� ����� ������ TreeBuilder.
     */
/*    protected void prepare() {
        
    }
*/    
}
