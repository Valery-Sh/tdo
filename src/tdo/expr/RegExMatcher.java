/*
 * RegExMatcher.java
 *
 * Created on 14 Ноябрь 2006 г., 15:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.expr;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *
 * @author valery
 */
public class RegExMatcher {
    Pattern pattern;
    Matcher matcher;
    String  target;
    
    public RegExMatcher(String regex) {
        pattern = Pattern.compile( regex );
        //matcher = pattern..matcher();
    }
    public void setTarget( String target ) {
        matcher = pattern.matcher(target);
    }
    public boolean matches() {
        return matcher.matches();
    }
}//class
