/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.impl;

import tdo.DataRow;
import tdo.expr.StartsWithEvaluator;
import tdo.service.TableServices;

/**
 * <p>ћетод {@link #locate(java.lang.Object) } выполн€ет поиск , 
 * начина€ с 0-го р€да. ≈сли поиск не удачен, ио возвращает -1. ћетод с такой
 * сигнатурой использует текущую установку свойства <code>caseSensitive</code>.
 * <p>ћетод {@link #locate(java.lang.Object, boolean) } выполн€ет поиск,
 * начина€ с 0-го р€да. ≈сли поиск не удачен, ио возвращает -1. ћетод с такой
 * сигнатурой использует текущую устанавливает новое значение
 * свойства <code>caseSensitive</code> рввное значению параметра.
 * результатом выполнени€ метов <code>locate</code> €вл€етс€ индекс найденого 
 * р€да или -1. ѕри этом, значение индекса или -1 сохран€етс€ во внутреннем поле
 * и используетс€ при примененииоднго из  методов <code>next</code>.
 * 
 * <p>ћетоды <code>next</code> используют текущую установку свойства 
 * <code>caseSensitive</code>.
 *
 *
 * 
 * 
 */
public class RowLocator  extends DefaultFilterer{

    protected boolean caseSensitive;
    protected int currentPos;

    public RowLocator() {
        super();
        currentPos = -1;
        caseSensitive = true;
    }
    public RowLocator(TableServices tableServices) {
        super(tableServices);
        currentPos = -1;
        caseSensitive = true;

    }
    public int locate(Object value) {
        return locate(value,this.caseSensitive);
    }
    public int locate(Object value, boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
        ((StartsWithEvaluator)this.getPredicate()).setValue(value);
        ((StartsWithEvaluator)this.getPredicate()).setCaseSensitive(caseSensitive);
        int r = -1;
        if ( getTableServices().getFilterServices().getRowCount() == 0 )
            return -1;

        for ( int i=0; i < getTableServices().getFilterServices().getRowCount(); i++ ) {
            DataRow row = getTableServices().getFilterServices().getRow(i);
            if ( this.getPredicate().evaluate(row) ) {
                r = i;
                break;
            }

        }//for
        currentPos = r;

        return r;
    }

    public int next() {
        int r = -1;
        ((StartsWithEvaluator)this.getPredicate()).setCaseSensitive(caseSensitive);

        int rc = getTableServices().getFilterServices().getRowCount();
        if ( rc == 0 || (currentPos + 1) >= rc )
            return currentPos;

        for ( int i=currentPos+1; i < getTableServices().getFilterServices().getRowCount(); i++ ) {
            DataRow row = getTableServices().getFilterServices().getRow(i);
            if ( this.getPredicate().evaluate(row) ) {
                r = i;
                break;
            }

        }//for
        if ( r == -1 )
            r = currentPos;
        else
            currentPos = r;

        return r;

    }

    public int next(Object value) {
        ((StartsWithEvaluator)this.getPredicate()).setValue(value);
        ((StartsWithEvaluator)this.getPredicate()).setCaseSensitive(caseSensitive);
        return next();
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }
}
