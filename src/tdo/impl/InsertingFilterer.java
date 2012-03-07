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
public class InsertingFilterer extends DefaultFilterer{

    public InsertingFilterer() {
        this(null);
    }


    public InsertingFilterer(TableServices tableServices) {
        super(tableServices);
        this.setPredicate(new NotInsertingPredicate());
    }

    public class NotInsertingPredicate implements RowEvaluator {

        public boolean evaluate(int rowIndex) {
            DataRow row = InsertingFilterer.this.getTableServices().getFilterServices().getRow(rowIndex);
            return this.evaluate(row);
        }

        @Override
        public boolean evaluate(DataRow row) {
            RowState st = row.getState();
            if ( ! st.isInserting() )
                return true;
            else
                return false;
        }

    }//class NotInsertingPredicate
}//class InsertingFilterer

