/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.support;

import tdo.*;
import tdo.support.DataRowTst;

/**
 *
 * @author Valery
 */
public class RowCollectionServicesTst implements tdo.service.RowCollectionServices {

    @Override
    public DataRow createRow() {
        return new DataRowTst();
    }

}
