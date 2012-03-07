/*
 * RoundParenthesis.java
 * 
 * Created on 14.06.2007, 20:28:01
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.expr;

/**
 *
 * @author Valera
 */
public class RoundParenthesis implements IToken{
    public static final boolean LEFT  = true;
    public static final boolean RIGHT = false;
    
    private boolean kind;
    
    public RoundParenthesis(boolean kind) {
        this.kind = kind;
    }
    public boolean isLeft() {
        return kind == LEFT;
    }
    public boolean isRight() {
        return kind == RIGHT;
    }
    public boolean getKind() {
        return kind;
    }
    public void setKind(boolean kind) {
        this.kind = kind;
    }
    @Override
    public int getPriority() {
        return 0;
    }
    
}//class RoundParensesis
