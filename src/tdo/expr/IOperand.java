/*
 * IOperand.java
 * 
 * Created on 14.06.2007, 14:18:39
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.expr;

import tdo.NamedValues;

/**
 * ѕредставл€ет собой основу дл€ различных классов операндов выражени€.<p>
 * ћетоды интерфейса позвол€ют получить значени€ операндов при вычислении выражений.
 * »нтерфейс определ€ет метод <code>applyDataTable</code>
 */
public interface IOperand extends IToken{
  
    /**
     * ¬озвращает значение опреранда при вычислении выражени€. 
     * @param row р€д таблицы
     * @return значение опреранда
     */
    Object getValue(NamedValues row);    
    Object getValue(NamedValues[] row);  
    Object getValue();        
    void   setContext(ExpressionContext context);
    Class getType();
    
}