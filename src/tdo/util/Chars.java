/*
 * Chars.java
 *
 * Created on 21 Ноябрь 2006 г., 8:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.util;

/**
 *
 * @author valery
 */
public class Chars {
    
    /** Creates a new instance of Chars */
    public Chars() {
    }
    public static boolean startsWith( char[] c, int pos, String s) {
        boolean r = false;
        if ( pos + s.length() - 1 < c.length )
            if ( new String(c, pos, s.length() ).toUpperCase().equals(s) )
                r = true;
        return r;
    }
    
}//class
