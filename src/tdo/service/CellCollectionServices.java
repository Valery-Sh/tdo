/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.service;

import tdo.DataCellCollection;

/**
 *
 * @author valery
 */
public interface CellCollectionServices {
    DataCellCollection createCellCollection();
    DataCellCollection createCellCollection(Object bean);
    
}
