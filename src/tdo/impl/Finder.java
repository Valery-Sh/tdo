
package tdo.impl;



import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import tdo.DataRow;
import tdo.Table;
import tdo.expr.RowEvaluator;
import tdo.expr.RowExpressionContext;
import tdo.service.FilterServices;
import tdo.service.TableServices;

/**
 *
 * @author valery
 */
public class Finder {
    
    private Map<String,Object> parameters;
    protected int currentPos;
    protected TableServices tableServices;    
    protected FilterServices services;
    private   String expression;
    private RowEvaluator predicate;

    /** Creates a new instance of Finder */
    public Finder(Table table) {
        this(table.getContext());
    }
    public Finder(TableServices tableServices) {
        currentPos = -1;
        this.tableServices = tableServices;
        this.services = tableServices.getFilterServices();
    }
    
    public void setParameter(String name, Object value) {
        
        if ( parameters == null ) {
            parameters = new HashMap<String,Object>(2);
        }
        parameters.put(name, value);
        if ( this.predicate != null ) {
            ((RowExpressionContext)this.predicate).setParameter(name,value);
        }
    }
    public void removeParameter(String name) {
        parameters.remove(name);
        if ( this.predicate != null ) {
            ((RowExpressionContext)this.predicate).removeParameter(name);
        }
        
    }
    
    public int first() {
        int r = -1;
        int rowCount = services.getRowCount();    
        if ( rowCount == 0 )
            return -1;
        
        for ( int i=0; i < rowCount; i++ ) {
            DataRow row = services.getRow(i);
            if ( this.getPredicate().evaluate(row) ) {
                r = i;
                break;
            }
        }//for
        this.currentPos = r;
        return r;

    }
    
    public int find() {
        return next(-1);
    }
    
    public int find(String expression) {
        this.expression = expression;
        this.predicate = null;
        currentPos = -1;
        return next(currentPos);
    }
    
    public int next( int startFrom) {
        int r = -1;
        int rc = services.getRowCount();
        if ( rc == 0 || (startFrom + 1) >= rc )
            return -1;

        for ( int i=startFrom+1; i < services.getRowCount(); i++ ) {
            DataRow row = services.getRow(i);
            if ( this.getPredicate().evaluate(row) ) {
                r = i;
                break;
            }

        }//for
        this.currentPos = r;
        return r;

    }

    public int next() {
        return next(currentPos);
    }

    public int prior( int startFrom) {
        int r = -1;
        int rc = services.getRowCount();
        if ( rc == 0 || (startFrom - 1) < 0 )
            return -1;

        for ( int i=startFrom-1; i >= 0; i-- ) {
            DataRow row = services.getRow(i);
            if ( this.getPredicate().evaluate(row) ) {
                r = i;
                break;
            }

        }//for
        this.currentPos = r;
        return r;

    }

    public int prior() {
        return prior(currentPos);

    }

    public int last() {
        int r = -1;
        int rc = services.getRowCount();
        if ( rc == 0 )
            return -1;

        for ( int i=rc-1; i >= 0; i-- ) {
            DataRow row = services.getRow(i);
            if ( this.getPredicate().evaluate(row) ) {
                r = i;
                break;
            }

        }//for
        this.currentPos = r;
        return r;

    }
    public String getExpression() {
        return this.expression;
    }
    public void setExpression( String expression ) {
        this.expression = expression;
    }



    public RowEvaluator getPredicate() {
        if ( expression == null ) {
            predicate = null;
        } else if ( predicate == null ) {
            predicate = createExprPredicate();
        }
        return this.predicate;
    }
    public void setPredicate(RowEvaluator predicate) {
        this.predicate = predicate;
    }

    protected RowEvaluator createExprPredicate() {
        RowExpressionContext ip = new RowExpressionContext(this.getExpression());
        ip.addTableServices(tableServices);
        if ( parameters != null ) {

            for ( Entry<String,Object> e : parameters.entrySet()) {
                ip.setParameter(e.getKey(), e.getValue());
            }
        }
        return ip;

    }

}
