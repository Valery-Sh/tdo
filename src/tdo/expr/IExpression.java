/*
 * IExpression.java
 * 
 * Created on 15.06.2007, 0:03:55
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.expr;

/**
 * ѕредставл€ет собой основу дл€ различных классов выражений.<p>
 * »нтерфейс не определ€ет каких-либо методов и служит маркером классов.
 * ѕоскольку он наследует интерфейсу <code>IOperator</code>, то значение
 * выражени€ может быть получено одним из перегруженных методов
 * {@link IOperand#getValue}
 * @see IOperand
 * 
 */
public interface IExpression extends IOperand{
}
