/*
 * ExprUtil.java
 *
 * Created on 19.09.2007, 14:18:49
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.util;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author valery
 */
public class ExprUtil {

    /**
     * @return new Date objects incremented by a given number of days.
     */
    protected static Date incrDays(Date date, Number incr) {
        Date newDate;//isEmpty() = null;
        long days;//My 06.03.2012 = 0;
        if (incr == null) {
            if (date instanceof Timestamp) {
                return new Timestamp(date.getTime());
            } else {
                return new Date(date.getTime());
            }
        } else {
            if (!(incr instanceof Number)) {
                throw new NumberFormatException("for Date increment the second param must be of Number type");
            }
            days = incr.longValue();
        }

        long ms = date.getTime() + days * 86400000;
        if (date instanceof Timestamp) {
            newDate = new Timestamp(ms);
        } else {
            newDate = new Date(ms);
        }
        return newDate;
    }

    /**
     * @return new Date objects decremented by a given number of days.
     */
    protected static Date decrDays(Date date, Number incr) {
        if (incr == null) {
            return date;
        } else {
            if (!(incr instanceof Number)) {
                throw new NumberFormatException("for Date increment the second param must be of Number type");
            }
            return incrDays(date, Long.valueOf(-incr.longValue()));
        }
    }

    /**
     * @return long value as a number of days between given dates.
     *  for example if d1 = '2000/01/01' and d2 = '1999/31/12' then
     *  result equals 1.
     * @param d1
     * @param d2
     */
    protected static long subtrDates(Date d1, Date d2) {
        return ( d1.getTime() - d2.getTime() ) / 86400000;
    }


    public static Object add(Object o1, Object o2) {
        if (o1 instanceof Date) {
            return incrDays((Date) o1, (Number) o2);
        }

        Object[] operands = createOperands(o1, o2);
        Object r = null;
        if (operands[0] instanceof BigDecimal) {
            r = ((BigDecimal) operands[0]).add((BigDecimal) operands[1]);
        } else if (operands[0] instanceof Double) {
            r = ((Double) operands[0]).doubleValue() + ((Double) operands[1]).doubleValue();
        } else if (operands[0] instanceof Float) {
            r = ((Float) operands[0]).floatValue() + ((Float) operands[1]).floatValue();
        } else if (operands[0] instanceof Long) {
            r = ((Long) operands[0]).longValue() + ((Long) operands[1]).longValue();
        } else if (operands[0] instanceof Integer) {
            r = ((Integer) operands[0]).intValue() + ((Integer) operands[1]).intValue();
        } else if (operands[0] instanceof Short) {
            r = ((Short) operands[0]).shortValue() + ((Short) operands[1]).shortValue();
        } else if (operands[0] instanceof Byte) {
            r = ((Byte) operands[0]).byteValue() + ((Byte) operands[1]).byteValue();
        } else {
            throw new NumberFormatException("add operands must be of Numeric type");
        }
        return r;
    }

    /**
     * The method may be used:
     * <ul>
     *   <li>to numericaly subtract one value from another;</li>
     *   <li>to calculate number of days between two given dates;</li> 
     *   <li>to decrement a given date by a given number of days</li>
     * </ul>
     * If o1 and o2 are of a Number type then the method performs numerical
     * subtraction where o1 serves as minuend and o2 as subtrahend. null valued
     * parameter is considered to be an instance of java.util.Integer(0) so than 
     * an exception doesn't occurs.
     * <p>
     * 
     * @param o1
     * @param o1
     */ 
    public static Object subtr(Object o1, Object o2) {

        if (o1 instanceof Date) {
            if (o2 instanceof Date) {
                return subtrDates((Date) o1, (Date) o2);
            } else if ( o2 instanceof Number )
                return decrDays((Date) o1, (Number) o2);
            else if ( o2 == null ) {
                return decrDays((Date) o1, 0);
            } else {
                throw new IllegalArgumentException( "subtr(Date,Number) : "  +
                        "Second parameter must be of java.util.Number type " +
                        "but found " + o2.getClass() );
            }
        }

        Object[] operands = createOperands(o1, o2);
        
        Object r = null;
        if (operands[0] instanceof BigDecimal) {
            r = ((BigDecimal) operands[0]).subtract((BigDecimal) operands[1]);
        } else if (operands[0] instanceof Double) {
            r = ((Double) operands[0]).doubleValue() - ((Double) operands[1]).doubleValue();
        } else if (operands[0] instanceof Float) {
            r = ((Float) operands[0]).floatValue() - ((Float) operands[1]).floatValue();
        } else if (operands[0] instanceof Long) {
            r = ((Long) operands[0]).longValue() - ((Long) operands[1]).longValue();
        } else if (operands[0] instanceof Integer) {
            r = ((Integer) operands[0]).intValue() - ((Integer) operands[1]).intValue();
        } else if (operands[0] instanceof Short) {
            r = ((Short) operands[0]).shortValue() - ((Short) operands[1]).shortValue();
        } else if (operands[0] instanceof Byte) {
            r = ((Byte) operands[0]).byteValue() - ((Byte) operands[1]).byteValue();
        } else {
            throw new NumberFormatException("subtract operands must be of Numeric type");
        }
        return r;
    }

    public static Object mult(Object o1, Object o2) {
        Object[] operands = createOperands(o1, o2);
        Object r = null;
        if (operands[0] instanceof BigDecimal) {
            r = ((BigDecimal) operands[0]).multiply((BigDecimal) operands[1]);
        } else if (operands[0] instanceof Double) {
            r = ((Double) operands[0]).doubleValue() * ((Double) operands[1]).doubleValue();
        } else if (operands[0] instanceof Float) {
            r = ((Float) operands[0]).floatValue() * ((Float) operands[1]).floatValue();
        } else if (operands[0] instanceof Long) {
            r = ((Long) operands[0]).longValue() * ((Long) operands[1]).longValue();
        } else if (operands[0] instanceof Integer) {
            r = ((Integer) operands[0]).intValue() * ((Integer) operands[1]).intValue();
        } else if (operands[0] instanceof Short) {
            r = ((Short) operands[0]).shortValue() * ((Short) operands[1]).shortValue();
        } else if (operands[0] instanceof Byte) {
            r = ((Byte) operands[0]).byteValue() * ((Byte) operands[1]).byteValue();
        } else {
            throw new NumberFormatException("multiply operands must be of Numeric type");
        }
        return r;
    }

    public static Object div(Object o1, Object o2) {
        Object[] operands = createOperands(o1, o2);
        Object r = null;

        if (operands[0] instanceof Double) {
            r = ((Double) operands[0]).doubleValue() / ((Double) operands[1]).doubleValue();
        } else if (operands[0] instanceof Float) {
            r = ((Float) operands[0]).floatValue() / ((Float) operands[1]).floatValue();
        } else if (operands[0] instanceof Long) {
            r = ((Long) operands[0]).longValue() / ((Long) operands[1]).longValue();
        } else if (operands[0] instanceof Integer) {
            r = ((Integer) operands[0]).intValue() / ((Integer) operands[1]).intValue();
        } else if (operands[0] instanceof Short) {
            r = ((Short) operands[0]).shortValue() / ((Short) operands[1]).shortValue();
        } else if (operands[0] instanceof Byte) {
            r = ((Byte) operands[0]).byteValue() / ((Byte) operands[1]).byteValue();
        } else {
            throw new NumberFormatException("divide operands must be of Numeric type");
        }
        return r;
    }

    /**
     * Выполняет деление аналогично {@link #div}, но предварительно
     * проверяет второй операнд на нулевое значение.
     * Если второй операнд равен нулю, то возвращает значение третьего операнда.
     *
     */
    public static Object divSafe(Object o1, Object o2, Object altResult) {
        Object[] operands = createOperands(o1, o2);
        Object r = null;

        if (operands[0] instanceof BigDecimal) {
            BigDecimal n = (BigDecimal) operands[1];
            if (n.compareTo(new BigDecimal(0)) == 0) {
                r = altResult;
            } else {
                r = ((BigDecimal) operands[0]).divide(n, BigDecimal.ROUND_HALF_UP);
            }
        } else if (operands[0] instanceof Double) {
            double n = ((Double) operands[1]).doubleValue();
            if (n == 0) {
                r = altResult;
            } else {
                r = Double.valueOf(((Double) operands[0]).doubleValue() / n);
            }
        } else if (operands[0] instanceof Float) {
            float n = ((Float) operands[1]).floatValue();
            if (n == 0) {
                r = altResult;
            } else {
                r = Float.valueOf(((Float) operands[0]).floatValue() / n);
            }
        } else if (operands[0] instanceof Long) {
            long n = ((Long) operands[1]).longValue();
            if (n == 0) {
                r = altResult;
            } else {
                r = Long.valueOf(((Long) operands[0]).longValue() / n);
            }
        } else if (operands[0] instanceof Integer) {
            int n = ((Integer) operands[1]).intValue();
            if (n == 0) {
                r = altResult;
            } else {
                r = Integer.valueOf(((Integer) operands[0]).intValue() / n);
            }
        } else {
            throw new NumberFormatException("divide operands must be of Numeric type");
        }

        return r;
    }


    protected static boolean isNaN(Number n) {
        boolean r = false;
        if ( n instanceof Float )
            r = Float.isNaN((Float)n);
        if ( n instanceof Double )
            r = Double.isNaN((Double)n);
        return r;
    }
    protected static boolean isInfinite(Number n) {
        boolean r = false;
        if ( n instanceof Float )
            r = Float.isInfinite((Float)n);
        if ( n instanceof Double )
            r = Double.isInfinite((Double)n);
        return r;
    }
    
    /**
     * 
     * One or both parameter can be <code>null</code>. If a parameter value 
     * is null then it is considered to be equals to 0d value. 
     * @param n1 the first value to be compared.
     * @param n2 the second value to be compared.
     * @return  <code>0</code> if <code>n1</code> is
     *		numerically equal to <code>n2</code>; 
     *          a value <code>-1</code>
     *          if <code>n1</code> is numerically less than
     *		<code>n2</code>; 
     *          and <code>1</code>
     *		if <code>n1</code> is numerically greater than
     *		<code>n2</code>.
     */
    protected static int compareFloating(Number n1, Number n2) {
       double d1 = 0d; 
       double d2 = 0d;        
       
       if ( n1 != null ) 
           d1 = n1.doubleValue();
       if ( n2 != null )
           d2 = n2.doubleValue();
       return compareDouble(d1,d2);    
    }
    
    public static int compareNumbers(Number n1, Number n2) {
        if (n1 == n2) {
            return 0;
        }
        if (isNaN(n1) || isNaN(n2) || isInfinite(n1) || isInfinite(n2)) 
            return compareFloating(n1,n2);
        
        BigDecimal v1 = (n1 instanceof BigDecimal) ? (BigDecimal) n1 : toBigDecimal(n1);
        BigDecimal v2 = (n2 instanceof BigDecimal) ? (BigDecimal) n2 : toBigDecimal(n2);
        return v1.compareTo(v2);
    }

    protected static int compareDates(Date d1, Date d2) {
        long t1 = d1.getTime();
        long t2 = d2.getTime();
        return (t1 == t2) ? 0 : (t1 > t2 ? 1 : -1);
    }

  /**
     * Compares the two specified <code>double</code> values. The sign
     * of the integer value returned is the same as that of the
     * integer that would be returned by the call:
     * <pre>
     *    new Double(d1).compareTo(new Double(d2))
     * </pre>
     *
     * @param   d1        the first <code>double</code> to compare
     * @param   d2        the second <code>double</code> to compare
     * @return  the value <code>0</code> if <code>d1</code> is
     *		numerically equal to <code>d2</code>; a value less than
     *          <code>0</code> if <code>d1</code> is numerically less than
     *		<code>d2</code>; and a value greater than <code>0</code>
     *		if <code>d1</code> is numerically greater than
     *		<code>d2</code>.
     */
    public static int compareDouble(double d1, double d2) {
        if (d1 < d2)
            return -1;		 // Neither val is NaN, thisVal is smaller
        if (d1 > d2)
            return 1;		 // Neither val is NaN, thisVal is larger

        long d1Bits = Double.doubleToLongBits(d1);
        long d2Bits = Double.doubleToLongBits(d2);

        return (d1Bits == d2Bits ?  0 : // Values are equal
                (d1Bits < d2Bits ? -1 : // (-0.0, 0.0) or (!NaN, NaN)
                                    1));// (0.0, -0.0) or (NaN, !NaN)
    }

    public static int compare(Object obj, Object anotherObj) {
        return compare(obj, anotherObj, true);
    }

    public static int compare(Object obj, Object anotherObj, boolean nullMin) {
        int nullRes = nullMin ? -1 : 1;
        if (obj == null && anotherObj == null) {
            return 0;
        }
        if (obj == null) {
            return nullRes;
        }

        if (anotherObj == null) {
            return -nullRes;
        }

        if (obj instanceof Date && anotherObj instanceof Date) {
            return compareDates((Date) obj, (Date) anotherObj);
        }

        if (obj instanceof Number && anotherObj instanceof Number) {
            return compareNumbers((Number) obj, (Number) anotherObj);
        }

        String s1 = obj.toString();
        String s2 = anotherObj.toString();

        int r = s1.compareTo(s2);
        return r == 0 ? 0 : (r > 0 ? 1 : -1);
    }

    public static Object min(Object o1, Object o2) {
        int r = compare(o1, o2);
        return r <= 0 ? o1 : o2;
    }

    public static Object max(Object o1, Object o2) {
        int r = compare(o1, o2);
        return r >= 0 ? o1 : o2;
    }


    public static Object[] createOperands(Object op1, Object op2) {

        Object[] operands = {null, null};
        if (op1 == null && op2 == null) {
            operands[0] = new Integer(0);
            operands[1] = new Integer(0);
            return operands;
        }
        Object o1 = op1;
        Object o2 = op2;

        if (op1 == null) {
        //My 06.03.2012    op1 = Integer.valueOf(0);
        }
        if (op2 == null) {
            //My 06.03.2012 op2 = Integer.valueOf(0);
        }
        Object v1 = null;
        Object v2 = null;


        if (o1 instanceof BigDecimal || o2 instanceof BigDecimal) {
            if (o1 instanceof BigDecimal && o2 instanceof BigDecimal) {
                v1 = o1;
                v2 = o2;
            } else if (o1 instanceof BigDecimal) {
                if (o2 instanceof Double && (((Double) o2).isNaN() || ((Double) o2).isInfinite())) {
                    v1 = toDouble(o1);
                    v2 = o2;
                    //throw new NumberFormatException("Operand isNaN or isInfinite");
                } else if (o2 instanceof Float && (((Float) o2).isNaN() || ((Float) o2).isInfinite())) {
                    v1 = toFloat(o1);
                    v2 = o2;
                    //throw new NumberFormatException("Operand isNaN or isInfinite");
                } else {
                    v1 = o1;
                    v2 = toBigDecimal(o2);
                }
            } else {
                if (o1 instanceof Double && (((Double) o1).isNaN() || ((Double) o1).isInfinite())) {
                    v1 = o1;
                    //My 06.03.2012v2 = toDouble(o2);
                    //throw new NumberFormatException("Operand isNaN or isInfinite");
                } else if (o1 instanceof Float && (((Float) o1).isNaN() || ((Float) o1).isInfinite())) {
                    v1 = o1;
                    //My 06.03.2012v2 = toFloat(o2);
                    //throw new NumberFormatException("Operand isNaN or isInfinite");
                } else {
                    v1 = toBigDecimal(o1);
                }
                v2 = o2;
            }
        } else if (o1 instanceof Double || o2 instanceof Double) {
            if (o1 instanceof Double && o2 instanceof Double) {
                v1 = o1;
                v2 = o2;
            } else if (o1 instanceof Double) {
                v1 = o1;
                v2 = toDouble(o2);
            } else {
                v1 = toDouble(o1);
                v2 = o2;
            }
        } else if (o1 instanceof Float || o2 instanceof Float) {
            if (o1 instanceof Float) {
                v1 = o1;
                v2 = Float.valueOf(toDouble(o2).floatValue());
            } else {
                v1 = Float.valueOf(toDouble(o1).floatValue());
                v2 = o2;
            }
        } else if (o1 instanceof Long || o2 instanceof Long) {

            if (o1 instanceof Long) {
                v1 = o1;
                v2 = toLong(o2);
            } else {
                v1 = toLong(o1);
                v2 = o2;
            }
        } else if (o1 instanceof Integer || o2 instanceof Integer) {

            if (o1 instanceof Integer) {
                v1 = o1;
                v2 = Integer.valueOf(toLong(o2).intValue());
            } else {
                v1 = Integer.valueOf(toLong(o1).intValue());
                v2 = o2;
            }
        } else if (o1 instanceof Short || o2 instanceof Short) {
            if (o1 instanceof Short) {
                v1 = Integer.valueOf(((Short) o1).intValue());
                v2 = Integer.valueOf(toLong(o2).intValue());
            } else {
                v1 = Integer.valueOf(toLong(o1).intValue());
                v2 = Integer.valueOf(((Short) o2).intValue());
            }
        } else if (o1 instanceof Byte || o2 instanceof Byte) {
            if (o1 instanceof Byte && o2 instanceof Byte) {
                v1 = Integer.valueOf(((Byte) o1).intValue());
                v2 = Integer.valueOf(((Byte) o2).intValue());
            } else

            if (o1 instanceof Byte) {
                v1 = Integer.valueOf(((Byte) o1).intValue());
                v2 = Integer.valueOf(toLong(o2).intValue());
            } else {
                v1 = Integer.valueOf(toLong(o1).intValue());
                v2 = Integer.valueOf(((Byte) o2).intValue());
            }
        } else {
            throw new NumberFormatException("Operand is not a Number");
        }

        operands[0] = v1;
        operands[1] = v2;

        return operands;
    }

/*
    public static Object[] createDivOperands(Object o1, Object o2) {
    Object[] r = new Object[]{o1, o2};
    if (o1 instanceof BigDecimal) {
    r[0] = o1;
    r[1] = toBigDecimal(o2);
    } else if (o2 instanceof BigDecimal) {
    r[0] = toBigDecimal(o1);
    r[1] = o2;
    } else {
    r = createOperands(o1, o2);
    }
    return r;
    }
     */
    public static BigDecimal toBigDecimal(Object o) {
        if (o instanceof BigDecimal) {
            return (BigDecimal) o;
        }

        BigDecimal r = new BigDecimal(0);

        if (o == null) {
            return r;
        }
        if (o instanceof Number) {
            r = new BigDecimal(((Number) o).doubleValue());
        } else {
            throw new NumberFormatException("ExprUtil.toBigDecimal(" + o.toString() + ")");
        }
        return r;
    }

    public static Float toFloat(Object o) {
        Float r = Float.valueOf(0f);
        if (o == null) {
            return r;
        }

        if (o instanceof Number) {
            r = Float.valueOf(((Number) o).floatValue());
        } else {
            throw new NumberFormatException("ExprUtil.toDouble(" + o.toString() + ")");
        }
        return r;
    }

    public static Double toDouble(Object o) {
        Double r = Double.valueOf(0d);
        if (o == null) {
            return r;
        }

        if (o instanceof Number) {
            r = Double.valueOf(((Number) o).doubleValue());
        } else {
            throw new NumberFormatException("ExprUtil.toDouble(" + o.toString() + ")");
        }
        return r;
    }

    public static Long toLong(Object o) {
        Long r = Long.valueOf(0);

        if (o == null) {
            return r;
        }
        if (o instanceof Number) {
            r = ((Number) o).longValue();
        } else {
            throw new NumberFormatException("ExprUtil.toLong(" + o.toString() + ")");
        }
        return r;
    }

    public static Double divDouble(Object o1, Object o2) {
        Double d = new Double(0);
        Double d1 = toDouble(o1);
        Double d2 = toDouble(o2);
        return d1 / d2;
    }

    public static Double divDoubleSafe(Object o1, Object o2, Double altResult) {
        Double d1 = toDouble(o1);
        Double d2 = toDouble(o2);
        return d2.doubleValue() == 0 ? altResult : d1 / d2;
    }

    public static Timestamp timestampValueOf(String value, DateFormat df) {
        Date d = dateValueOf(value, df);
        return new Timestamp(d.getTime());
    }

    public static Timestamp timestampValueOf(String value) {
        return timestampValueOf(value, new SimpleDateFormat("dd.MM.yy"));
    }

    public static Timestamp timestampValueOf(String value, String pattern) {
        if (pattern == null) {
            return timestampValueOf(value);
        }
        Date d = dateValueOf(value, pattern);
        return new Timestamp(d.getTime());
    }

    public static Timestamp timestampValueOf(String value, String pattern, String localeLang) {
        if (localeLang == null) {
            return timestampValueOf(value,pattern);
        }
        Date d = dateValueOf(value, pattern,localeLang);
        return new Timestamp(d.getTime());

    }

    public static Date dateValueOf(String value, DateFormat df) {
        java.util.Date dt;
        try {
            dt = df.parse(value.trim());
        } catch (ParseException e) {
            dt = new Date(0);
        }
        return dt;
    }

    public static Date dateValueOf(String value) {
        return dateValueOf(value, new SimpleDateFormat("dd.MM.yy"));
    }

    public static Date dateValueOf(String value, String pattern) {
        if (pattern == null) {
            return dateValueOf(value);
        }
        java.util.Date dt;
        SimpleDateFormat df = new SimpleDateFormat();
        df.applyPattern(pattern != null ? pattern : "dd.MM.yy");
        try {
            dt = df.parse(value.trim());
//            dt = new Date(dt.getTime());
        } catch (ParseException e) {
            dt = new Date(0);
        }
        return dt;
    }

    public static Date dateValueOf(String value, String pattern, String localeLang) {

        if (localeLang == null) {
            return dateValueOf(value,pattern);
        }
        java.util.Date dt;

        SimpleDateFormat df = (SimpleDateFormat)DateFormat.getDateInstance(DateFormat.FULL, new Locale(localeLang));

        //SimpleDateFormat df = new SimpleDateFormat();
        df.applyPattern(pattern != null ? pattern : "dd.MM.yy");
        try {
            dt = df.parse(value.trim());
//            dt = new Date(dt.getTime());
        } catch (ParseException e) {
            dt = new Date(0);
        }
        return dt;
    }

    public static Time timeValueOf(String value) {
        if (value == null) {
            return new Time(0);
        }
        return Time.valueOf(value);
    }
} //ExprUtil