/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdo.service;

import tdo.*;

/**
 *
 * @author valery
 */
public class CellCollectionServicesImpl implements CellCollectionServices {

    protected Table table;

    public CellCollectionServicesImpl(Table table) {
        this.table = table;
    }

    /**
     * 
     * @return
     */
    @Override
    public DataCellCollection createCellCollection() {
        return this.createCellCollection(null);
    }

    @Override
    public DataCellCollection createCellCollection(Object bean) {
        DataCellCollection dcc;//isEmpty() = null;
        if ( table.getClass().equals(SimpleTable.class )  ) {
            return new DefaultDataCellCollection(((SimpleTable)table).getContext());
        }

        if (((DataTable)table).getDefaultObjectRowSupport() == null) {
            dcc = new DefaultDataCellCollection(table.getContext());
        } else {
            ObjectRowSupport dos = ((DataTable)table).getDefaultObjectRowSupport();

            ObjectDataCellCollection ors = new ObjectDataCellCollection(table.getContext(), dos);
            ors.setObjectClass(dos.getObjectClass());
            if (bean == null) {
                try {
                    Class c = Class.forName(dos.getObjectClass().getName());
                    ors.setObject(c.newInstance());

                } catch (Exception ex) {
                    throw new RuntimeException(ex.getMessage());
                }
            } else {
                ors.setObject(bean);
            }

            dcc = ors;

        }
        return dcc;


    }
}//class CellCollectionServicesImpl
