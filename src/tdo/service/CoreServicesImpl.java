/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.service;

import java.io.Serializable;
import tdo.Table;

/**
 *
 * @author valery
 */
public class CoreServicesImpl implements CoreServices, Serializable{
    protected Table table; 
    
    public CoreServicesImpl(Table table) {
        this.table = table;
    }

    @Override
    public Table getTable() {
        return this.table;
    }
 

}
