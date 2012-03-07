/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.impl;

import java.util.ArrayList;
import java.util.List;
import tdo.DataRow;
import tdo.expr.ExpressionContext;
import tdo.expr.RowEvaluator;
import tdo.expr.RowExpressionContext;
import tdo.service.TableServices;

/**
 *
 * @author valery
 */
public class DefaultFilterer implements Filterer {

    public static final boolean CASE_INSENSITIVE = false;
    public static final boolean CASE_SENSITIVE   = true;

    private TableServices tableServices;

    private String expression;
    private RowEvaluator predicate;
    private ExpressionContext context;

    /** Creates a new instance of Filterer */
    public DefaultFilterer() {
        this(null);
    }

    /**
     * Creates a new instance of IRelation
     * @param tableServices
     */
    public DefaultFilterer(TableServices tableServices) {
        this.tableServices = tableServices;
    }

    public DefaultFilterer(TableServices tableServices, String expression) {
        this.tableServices = tableServices;
        this.setExpression(expression);
    }


    /*************************************************************************
     * Реализация интерфейса IFilterer.
     *************************************************************************
     */
    /**
     *
     * @return
     */
/*    @Override
    public SimpleInternalView getDataView() {
        return this.dataView;
    }

    @Override
    public void setDataView(SimpleInternalView dataView) {
        this.dataView = dataView;
    }
*/
    @Override
    public TableServices getTableServices() {
        return this.tableServices;
    }
    @Override
    public String getExpression() {
        return this.expression;
    }
    @Override
    public void setExpression( String expression ) {
        this.expression = expression;
        if ( expression != null ) {
            //this.predicate = createPredicate();
            //this.predicate = this.createExprPredicate();
            this.createExprPredicate();
        } else
            this.predicate = null;
    }
    @Override
    public RowEvaluator getPredicate() {
        return this.predicate;
    }
    @Override
    public void setPredicate(RowEvaluator predicate) {
        this.predicate = predicate;
    }

    protected void createExprPredicate() {
        context = new RowExpressionContext(this.getExpression());
        this.predicate = (RowExpressionContext)context;
        ((RowExpressionContext)context).addTableServices(tableServices);
    }

    public ExpressionContext getExpressionContext() {
        return this.context;
    }

    @Override
    public DataRow[] getRows() {

        //InternalRowCollection ds = dataView.getTable().getRows().createRowCollection();
        List<DataRow> ls = new ArrayList<DataRow>(20);
        if ( tableServices.getFilterServices().getRowCount() == 0 )
            return new DataRow[0];
        for ( int i=0; i < tableServices.getFilterServices().getRowCount(); i++ ) {
            if ( this.predicate.evaluate( tableServices.getFilterServices().getRow(i)) ) {
                ls.add(tableServices.getFilterServices().getRow(i));
            }
        }//for
        
        return ls.toArray(new DataRow[ls.size()]);
    }

    @Override
    public void setParameter(String paramName, Object value) {
        if ( this.getExpressionContext() != null )
            this.getExpressionContext().setParameter(paramName, value);
    }

    @Override
    public Object getParameterValue(String paramName) {
        DataRow r = null;
        return  this.getExpressionContext() != null ? this.getExpressionContext().getParameterValue(r, paramName) : null;

    }

}//class

