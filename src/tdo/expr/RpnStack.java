/*
 * RpnStack.java
 * 
 * Created on 15.06.2007, 9:45:40
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.expr;

import java.util.EmptyStackException;
import java.util.Stack;


/**
 * Класс-оболочка класса {@link java.util.Stack).
 * 
 */
public class RpnStack {
    
    protected Stack s;
    
    public RpnStack() {
        s = new Stack();
    }
    
    public void push( IToken token) {
        s.push(token);
        
    }
    
    public IToken pop() {
        return (IToken)s.pop();
    }

    public IToken peek() {
        return (IToken)s.peek();    
    }

    public IToken peek(int index) {
        if ( index < 0 || index >= s.size() )
            throw new EmptyStackException();
        return (IToken)s.elementAt(index);    
    }
    
    public boolean empty() {
        return s.empty();
    }
}