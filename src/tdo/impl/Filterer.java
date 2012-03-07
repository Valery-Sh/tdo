/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdo.impl;

import tdo.DataRow;
import tdo.expr.RowEvaluator;
import tdo.service.TableServices;

/**
 *
 * @author valery
 */
public interface Filterer {
    //SimpleInternalView   getDataView();
    //void        setDataView( SimpleInternalView dataView);

    TableServices getTableServices();

    String getExpression();

    void setExpression(String expression);

    void setParameter(String paramName, Object value);

    Object getParameterValue(String paramName);

    RowEvaluator getPredicate();

    void setPredicate(RowEvaluator predicate);
    //ExpressionContext getExpressionContext();
    //InternalRowCollection getRows();
    //DataRowCollection  getRows();

    DataRow[] getRows();
}
