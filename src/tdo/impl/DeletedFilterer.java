/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.impl;

import tdo.DataRow;
import tdo.RowState;
import tdo.expr.RowEvaluator;
import tdo.service.TableServices;

/**
 *
 * @author valery
 */
public class DeletedFilterer extends DefaultFilterer {

    public DeletedFilterer() {
        super();
    }
    public DeletedFilterer(TableServices tableServices) {
        super(tableServices);
        this.setPredicate(new DeletedPredicate());
    }

    public class DeletedPredicate implements RowEvaluator {

        public boolean evaluate(int rowIndex) {
            DataRow row = DeletedFilterer.this.getTableServices().getFilterServices().getRow(rowIndex);
            return this.evaluate(row);
        }

        @Override
        public boolean evaluate(DataRow row) {
            RowState st = row.getState();
            if ( ! st.isDeleted() )
                return true;
            else
                return false;
        }

    }//class DeletedPredicate
}//class DeletedFilterer
