/*
 * Strings.java
 *
 * Created on 11 ������ 2006 �., 19:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.util;

import java.util.StringTokenizer;
import java.util.Vector;

/**
 *
 * @author Valera
 */
public class Strings {
    
    /** Creates a new instance of Strings */
    public Strings() {
    }
    
    public static String[] split( String s ) {
        return split( s, "," );
    }
    
    public static String[] split( String s, String dlms ) {
        
        if ( s == null || s.trim().length() == 0 )
            return null;
        
        String[] result;
        Vector v = new Vector(10);
        StringTokenizer st = new StringTokenizer( s, dlms);
        while( st.hasMoreTokens() ) {
            String s1 = st.nextToken();
            //System.out.println( s1 );
            v.add( s1.trim() );
        }//while
        
        result = new String[v.size()];
        v.copyInto(result);
        return result;
    }

    public static String[] split( String s, String dlms, boolean returnDelims ) {
        
        if ( s == null || s.trim().length() == 0 )
            return null;
        
        String[] result;
        Vector v = new Vector(10);
        StringTokenizer st = new StringTokenizer( s, dlms, returnDelims);
        while( st.hasMoreTokens() ) {
            String s1 = st.nextToken();
            //System.out.println( s1 );
            v.add( s1.trim() );
        }//while
        
        result = new String[v.size()];
        v.copyInto(result);
        return result;
    }
    
    public static String compressSpaces( String s ) {
        if ( s == null || s.length() == 0 )
            return s;
        char[] c = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while ( i < c.length ) {
             sb.append(c[i]);
             if ( Character.isSpaceChar(c[i++]) ) {
                
                while ( i < c.length && Character.isSpaceChar(c[i]) ) {
                    i++;
                    break;
                }
            } 
        }//while
        
        return sb.toString();
    }
  /**
     * ������� �������, � ������, ��� ������� ����� Character.isSpaceChar
     * ���������� <code>true</code>. <p>
     * @param s �������� ������
     * @return ����� ������ � ���������� ��������� ��������.
     */
    public static String removeSpaces( String s ) {
        if ( s == null || s.length() == 0 )
            return s;
        return removeSpaces( s, 0, s.length()-1);
    }
    
    /**
     * ������� �������, � ���������, ��� ������� ����� Character.isSpaceChar
     * ���������� <code>true</code>. <p>
     * @param s �������� ������
     * @param left ����� ������� ������ s, ������������ ��������� ������ 
     *              ���������;
     * @param right ������ ������� ������ s, ������������ �������� ������ 
     *              ���������
     * @return ����� ������ � ���������� ��������� ��������.
     * @throws IllegalArgumentException ���� left > right
     */
    public static String removeSpaces( String s,int left, int right ) {
        if ( s == null || s.length() == 0 )
            return s;
        if ( left > right )
            throw new IllegalArgumentException("Left position must be equal or less than Right position");
        char[] c = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while ( i < c.length ) {
             if ( i >= left && i <right && Character.isSpaceChar(c[i]) ) { 
                 i++;
                 continue;
             }
             sb.append(c[i++]);
        }//while
        
        return sb.toString();
    }
    
}
