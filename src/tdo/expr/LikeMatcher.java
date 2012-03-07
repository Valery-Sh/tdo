/*
 * PWildCardMatcher.java
 *
 * Created on 14 Ноябрь 2006 г., 15:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.expr;
import java.util.regex.Pattern;
/**
 *
 * @author valery
 */
public class LikeMatcher {
    
    Pattern pattern;
    RegExMatcher matcher;
    String  target;
    
    public LikeMatcher(String regex) {
        if ( regex == null )
            throw new IllegalArgumentException("PWildCardMatcher: regex cannot be null");
        String s = regex.replace(".","\\.");
        s = regex.replace("%",".*");
        s = s.replace('_','.');
        matcher = new RegExMatcher(s);
    }
    public void setTarget( String target ) {
       matcher.setTarget(target);
    }
    public boolean matches() {
        return matcher.matches();
    }
}
