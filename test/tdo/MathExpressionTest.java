/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdo;

import tdo.expr.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;

/**
 *
 * @author valery
 */
public class MathExpressionTest extends AbstractMathExpessionTestBase {

    String operationName = "+";
    Long expInt_Null;
    Long expNull_Int;
    Long expInt_Int;
    Double expInt_Float;
    Double expFloat_Int;
    Double expInt_Double;
    Double expDouble_Int;
    BigDecimal expDec_Int;
    BigDecimal expInt_Dec;
    BigDecimal expDec_Null;
    BigDecimal expNull_Dec;
    BigDecimal expDec_Dec;
    BigDecimal expDec_Float;
    BigDecimal expFloat_Dec;
    BigDecimal expDec_Double;
    BigDecimal expDouble_Dec;
    Double expDouble_Null;
    Double expNull_Double;
    Double expDouble_Float;
    Double expFloat_Double;
    Double expDouble_Double;
    Double expFloat_Null;
    Double expNull_Float;
    Double expFloat_Float;

    @Test
    public void testAddExpression() {
        operationName = "+";
        expInt_Null = new Long(5);
        expNull_Int = new Long(10);
        expInt_Int = new Long(15);
        expInt_Float = new Double(5D).doubleValue() + new Double(10.2f).doubleValue();
        expFloat_Int = new Double(10D).doubleValue() + new Double(5.1f).doubleValue();
        expInt_Double = new Double(5D).doubleValue() + new Double(10.2D).doubleValue();
        expDouble_Int = new Double(10D).doubleValue() + new Double(5.1D).doubleValue();
        expDec_Int = new BigDecimal(new Double(10D + 5.1D).doubleValue());

        expInt_Dec = new BigDecimal(new Double(5D + 10.2D).doubleValue());

        expDec_Null = new BigDecimal(5.1D);

        expNull_Dec = new BigDecimal(new Double(10.2D).doubleValue());
        expDec_Dec = new BigDecimal(5).add(new BigDecimal(10.2));
        //expDec_Float = new BigDecimal(new Float(5.1).floatValue()).add(new BigDecimal(10.2));
        expDec_Float = new BigDecimal(5.1).add(new BigDecimal(10.2f));
        expFloat_Dec = new BigDecimal(new Float(5.1).doubleValue()).add(new BigDecimal(10.2));
        expDec_Double = new BigDecimal(new Double(5.1).doubleValue()).add(new BigDecimal(10.2));
        expDouble_Dec = new BigDecimal(new Double(5.1).doubleValue()).add(new BigDecimal(10.2));

        expDouble_Null = new Double(5.1);
        expNull_Double = new Double(10.2);
        expDouble_Float = new Double(5.1D).doubleValue() + new Double(10.2f).doubleValue();
        expFloat_Double = new Double(10.2D + 5.1f);
        expDouble_Double = new Double(10.2D + 5.1D);

        expFloat_Null = new Double(5.1f);
        expNull_Float = new Double(10.2f);
        expFloat_Float = new Double(5.1f).doubleValue() + new Double(10.2f).doubleValue();

        this.testMathCommon();
    }

    @Test
    public void testSubstrExpression() {
        operationName = "-";
        expInt_Null = new Long(5);
        expNull_Int = new Long(-10);
        expInt_Int = new Long(-5);
        expInt_Float = new Double(5D).doubleValue() - new Double(10.2f).doubleValue();
        expFloat_Int = new Double(5.1f).doubleValue() - new Double(10D).doubleValue();

        expInt_Double = new Double(5D).doubleValue() - new Double(10.2D).doubleValue();
        expDouble_Int = new Double(5.1D).doubleValue() - new Double(10D).doubleValue();
        expDec_Int = new BigDecimal(new Double(5.1D - 10D).doubleValue());

        expInt_Dec = new BigDecimal(new Double(5D - 10.2D).doubleValue());

        expDec_Null = new BigDecimal(5.1D);

        expNull_Dec = new BigDecimal(new Double(-10.2D).doubleValue());
        expDec_Dec = new BigDecimal(5).subtract(new BigDecimal(10.2));
        //expDec_Float = new BigDecimal(new Float(5.1).floatValue()).add(new BigDecimal(10.2));
        expDec_Float = new BigDecimal(5.1).subtract(new BigDecimal(10.2f));
        expFloat_Dec = new BigDecimal(new Float(5.1).doubleValue()).subtract(new BigDecimal(10.2));
        expDec_Double = new BigDecimal(new Double(5.1).doubleValue()).subtract(new BigDecimal(10.2));
        expDouble_Dec = new BigDecimal(new Double(5.1).doubleValue()).subtract(new BigDecimal(10.2));

        expDouble_Null = new Double(5.1);
        expNull_Double = new Double(-10.2d);
        expDouble_Float = new Double(5.1D).doubleValue() - new Double(10.2f).doubleValue();
        expFloat_Double = new Double(5.1f - 10.2d);
        expDouble_Double = new Double(5.1D - 10.2d);

        expFloat_Null = new Double(5.1f);
        expNull_Float = new Double(-10.2f);
        expFloat_Float = new Double(5.1f).doubleValue() - new Double(10.2f).doubleValue();

        this.testMathCommon();

    }

    @Test
    public void testMultExpression() {
        operationName = "*";
        expInt_Null = new Long(0);
        expNull_Int = new Long(0);
        expInt_Int = new Long(50);
        expInt_Float = new Double(5D).doubleValue() * new Double(10.2f).doubleValue();
        expFloat_Int = new Double(5.1f).doubleValue() * new Double(10D).doubleValue();

        expInt_Double = new Double(5D).doubleValue() * new Double(10.2D).doubleValue();
        expDouble_Int = new Double(5.1D).doubleValue() * new Double(10D).doubleValue();
        expDec_Int = new BigDecimal(5.1).multiply(new BigDecimal(10));

        expInt_Dec = new BigDecimal(5).multiply(new BigDecimal(10.2D));

        expDec_Null = new BigDecimal(5.1).multiply(new BigDecimal(0));

        expNull_Dec = new BigDecimal(0).multiply(new BigDecimal(10.2));
        expDec_Dec = new BigDecimal(5).multiply(new BigDecimal(10.2));
        //expDec_Float = new BigDecimal(new Float(5.1).floatValue()).add(new BigDecimal(10.2));
        expDec_Float = new BigDecimal(5.1).multiply(new BigDecimal(10.2f));
        expFloat_Dec = new BigDecimal(new Float(5.1).doubleValue()).multiply(new BigDecimal(10.2));
        expDec_Double = new BigDecimal(new Double(5.1).doubleValue()).multiply(new BigDecimal(10.2));
        expDouble_Dec = new BigDecimal(new Double(5.1).doubleValue()).multiply(new BigDecimal(10.2));

        expDouble_Null = new Double(0);
        expNull_Double = new Double(0d);
        expDouble_Float = new Double(5.1D).doubleValue() * new Double(10.2f).doubleValue();
        expFloat_Double = new Double(5.1f * 10.2d);
        expDouble_Double = new Double(5.1D * 10.2d);

        expFloat_Null = new Double(0);
        expNull_Float = new Double(0);
        expFloat_Float = new Double(5.1f).doubleValue() * new Double(10.2f).doubleValue();

        this.testMathCommon();

    }

    @Test
    public void testDivExpression() {
        operationName = "/";
        expInt_Null = new Long(0);
        expNull_Int = new Long(0);
        expInt_Int = new Long(50);
        expInt_Float = new Double(5D).doubleValue() / new Double(10.2f).doubleValue();
        expFloat_Int = new Double(5.1f).doubleValue() / new Double(10D).doubleValue();
        int scale;
        expInt_Double = new Double(5D).doubleValue() / new Double(10.2D).doubleValue();
        expDouble_Int = new Double(5.1D).doubleValue() / new Double(10D).doubleValue();
        scale = new BigDecimal(5.1).scale() - new BigDecimal(10).scale();        
        expDec_Int = new BigDecimal(5.1).divide(new BigDecimal(10),scale,BigDecimal.ROUND_HALF_UP);
        scale = new BigDecimal(5).scale() - new BigDecimal(10.2D).scale();
        expInt_Dec = new BigDecimal(5).divide(new BigDecimal(10.2D),scale,BigDecimal.ROUND_HALF_UP);

        //expDec_Null = new BigDecimal(5.1).divide(new BigDecimal(0));

        expNull_Dec = new BigDecimal(0).divide(new BigDecimal(10.2));
        expDec_Dec = new BigDecimal(5).divide(new BigDecimal(10.2),-2,BigDecimal.ROUND_HALF_UP);
        //expDec_Float = new BigDecimal(new Float(5.1).floatValue()).add(new BigDecimal(10.2));
        scale = new BigDecimal(5.1).scale() - new BigDecimal(10.2f).scale();        
        expDec_Float = new BigDecimal(5.1).divide(new BigDecimal(10.2f),scale,BigDecimal.ROUND_HALF_UP);
        scale = new BigDecimal(5.1f).scale() - new BigDecimal(10.2).scale();        
        expFloat_Dec = new BigDecimal(new Float(5.1).doubleValue()).divide(new BigDecimal(10.2),scale,BigDecimal.ROUND_HALF_UP);
        scale = new BigDecimal(5.1).scale() - new BigDecimal(10.2).scale();                        
        expDec_Double = new BigDecimal(new Double(5.1).doubleValue()).divide(new BigDecimal(10.2),scale,BigDecimal.ROUND_HALF_UP);
        scale = new BigDecimal(5.1).scale() - new BigDecimal(10.2).scale();                
        expDouble_Dec = new BigDecimal(new Double(5.1).doubleValue()).divide(new BigDecimal(10.2),scale,BigDecimal.ROUND_HALF_UP);

        expDouble_Null = new Double(5.1d/0d);
        expNull_Double = new Double(0d);
        expDouble_Float = new Double(5.1D).doubleValue() / new Double(10.2f).doubleValue();
        expFloat_Double = new Double(5.1f / 10.2d);
        expDouble_Double = new Double(5.1D / 10.2d);

        expFloat_Null = new Double(5.1f / 0f);
        expNull_Float = new Double(0d);
        expFloat_Float = new Double(5.1f).doubleValue() / new Double(10.2f).doubleValue();

        this.testMathCommon();

    }
    @Test
    public void testConcatExpression() {

        /////////////////////////////
        NamedValues row = null;
        MathExpression ae;
        ae = this.createExpression("||", 5, 10);
        assertEquals("510", (String) ae.getValue(row));
        ae = this.createExpression("||", 5, 10.4);
        assertEquals("510.4", (String) ae.getValue(row));
        ae = this.createExpression("||", 5.67, 10.4);
        assertEquals("5.6710.4", (String) ae.getValue(row));
        ae = this.createExpression("||", "5.67", 10.4);
        assertEquals("5.6710.4", (String) ae.getValue(row));
        ae = this.createExpression("||", 5.67, "10.4");
        assertEquals("5.6710.4", (String) ae.getValue(row));
        ae = this.createExpression("||", "5.67", "10.4");
        assertEquals("5.6710.4", (String) ae.getValue(row));
        ae = this.createExpression("||", "5.67", true);
        assertEquals("5.67true", (String) ae.getValue(row));
        ae = this.createExpression("||", false, true);
        assertEquals("falsetrue", (String) ae.getValue(row));
        
            
    }
    public void testMathCommon() {

        /////////////////////////////
        NamedValues row = null;
        MathExpression ae;
        if (this.operationName.equals("/")) {
            ae = this.createExpression(operationName, 5, 10);
            assertEquals(expDouble_Double, (Double) ae.getValue(row));
            
            ae = this.createExpression(operationName, 5, new Byte("10"));
            assertEquals(expDouble_Double, (Double) ae.getValue(row));

            ae = this.createExpression(operationName, 5, new Short("10"));
            assertEquals(expDouble_Double, (Double) ae.getValue(row));


            ae = this.createExpression(operationName, 5, new Integer("10"));
            assertEquals(expDouble_Double, (Double) ae.getValue(row));

            ae = this.createExpression(operationName, 5, new Long("10"));
            assertEquals(expDouble_Double, (Double) ae.getValue(row));


        } else {
            ae = this.createExpression(operationName, 5, new Byte("10"));
            assertEquals(expInt_Int, (Long) ae.getValue(row));

            ae = this.createExpression(operationName, 5, new Short("10"));
            assertEquals(expInt_Int, (Long) ae.getValue(row));

            ae = this.createExpression(operationName, 5, new Integer("10"));
            assertEquals(expInt_Int, (Long) ae.getValue(row));

            ae = this.createExpression(operationName, 5, new Long("10"));
            assertEquals(expInt_Int, (Long) ae.getValue(row));
        }

        /////
        ae = this.createExpression(operationName, 5, new Float("10.2"));
        assertEquals(expInt_Float, (Double) ae.getValue(row));

        ae = this.createExpression(operationName, 5, new Double("10.2"));
        assertEquals(expInt_Double, (Double) ae.getValue(row));

        ae = this.createExpression(operationName, 5, new BigDecimal(10.2));
        assertEquals(expInt_Dec, (BigDecimal) ae.getValue(row));

        ////
        if (this.operationName.equals("/")) {
            ae = this.createExpression(operationName, null, new Byte("10"));
            assertEquals(expNull_Double, (Double) ae.getValue(row));

            ae = this.createExpression(operationName, null, new Short("10"));
            assertEquals(expNull_Double, (Double) ae.getValue(row));

            ae = this.createExpression(operationName, null, new Integer("10"));
            assertEquals(expNull_Double, (Double) ae.getValue(row));

            /////////

            ae = this.createExpression(operationName, null, new Long("10"));
            assertEquals(expNull_Double, (Double) ae.getValue(row));


        } else {
            ae = this.createExpression(operationName, null, new Byte("10"));
            assertEquals(expNull_Int, (Long) ae.getValue(row));

            ae = this.createExpression(operationName, null, new Short("10"));
            assertEquals(expNull_Int, (Long) ae.getValue(row));

            ae = this.createExpression(operationName, null, new Integer("10"));
            assertEquals(expNull_Int, (Long) ae.getValue(row));

            /////////

            ae = this.createExpression(operationName, null, new Long("10"));
            assertEquals(expNull_Int, (Long) ae.getValue(row));
        }

        ae = this.createExpression(operationName, null, new Float("10.2"));
        assertEquals(expNull_Float, (Double) ae.getValue(row));

        ae = this.createExpression(operationName, null, new Double("10.2"));
        assertEquals(expNull_Double, (Double) ae.getValue(row));

        ae = this.createExpression(operationName, null, new BigDecimal(10.2));
        assertEquals(expNull_Dec, (BigDecimal) ae.getValue(row));

        if (this.operationName.equals("/")) {
            ae = this.createExpression(operationName, null, null);
            assertEquals(0D / 0D, (Double) ae.getValue(row));

        } else {
            ae = this.createExpression(operationName, null, null);
            assertEquals(0L, (Long) ae.getValue(row));
        }
        //
        // replace operands
        //
        if (this.operationName.equals("/")) {
            ae = this.createExpression(operationName, new Byte("5"), 10);
            assertEquals(expDouble_Double, (Double) ae.getValue(row));

            ae = this.createExpression(operationName, new Short("5"), 10);
            assertEquals(expDouble_Double, (Double) ae.getValue(row));

            ae = this.createExpression(operationName, new Integer("5"), 10);
            assertEquals(expDouble_Double, (Double) ae.getValue(row));

            ae = this.createExpression(operationName, new Long("5"), 10);
            assertEquals(expDouble_Double, (Double) ae.getValue(row));

        } else {
            ae = this.createExpression(operationName, new Byte("5"), 10);
            assertEquals(expInt_Int, (Long) ae.getValue(row));

            ae = this.createExpression(operationName, new Short("5"), 10);
            assertEquals(expInt_Int, (Long) ae.getValue(row));

            ae = this.createExpression(operationName, new Integer("5"), 10);
            assertEquals(expInt_Int, (Long) ae.getValue(row));

            ae = this.createExpression(operationName, new Long("5"), 10);
            assertEquals(expInt_Int, (Long) ae.getValue(row));
        }

        ae = this.createExpression(operationName, new Float("5.1"), 10);
        assertEquals(expFloat_Int, (Double) ae.getValue(row));

        ae = this.createExpression(operationName, new Double("5.1"), 10);
        assertEquals(expDouble_Int, (Double) ae.getValue(row));

        ///
        if (this.operationName.equals("/")) {
            ae = this.createExpression(operationName, new Byte("5"), null);
            assertEquals(expDouble_Null, (Double) ae.getValue(row));

            ae = this.createExpression(operationName, new Short("5"), null);
            assertEquals(expDouble_Null, (Double) ae.getValue(row));

            ae = this.createExpression(operationName, new Integer("5"), null);
            assertEquals(expDouble_Null, (Double) ae.getValue(row));

            ae = this.createExpression(operationName, new Long("5"), null);
            assertEquals(expDouble_Null, (Double) ae.getValue(row));

        } else {
            ae = this.createExpression(operationName, new Byte("5"), null);
            assertEquals(expInt_Null, (Long) ae.getValue(row));

            ae = this.createExpression(operationName, new Short("5"), null);
            assertEquals(expInt_Null, (Long) ae.getValue(row));

            ae = this.createExpression(operationName, new Integer("5"), null);
            assertEquals(expInt_Null, (Long) ae.getValue(row));

            ae = this.createExpression(operationName, new Long("5"), null);
            assertEquals(expInt_Null, (Long) ae.getValue(row));
        }


        ae = this.createExpression(operationName, new Float("5.1"), null);
        assertEquals(expFloat_Null, (Double) ae.getValue(row));

        ae = this.createExpression(operationName, new Double("5.1"), null);
        assertEquals(expDouble_Null, (Double) ae.getValue(row));
        if ( operationName.equals("/") ) {
            try {
                ae = this.createExpression(operationName, new BigDecimal(5.1), null);
                new BigDecimal(5.1).divide((BigDecimal) ae.getValue(row));
                fail("Zero division!!!");
            } catch(Exception e) {
                
            }
        } else {
            ae = this.createExpression(operationName, new BigDecimal(5.1), null);
            assertEquals(expDec_Null, (BigDecimal) ae.getValue(row));
        }
        //********************************************************************
        // Byte + others
        //********************************************************************
        if (this.operationName.equals("/")) {
            ae = this.createExpression(operationName, new Byte("5"), new Byte("10"));
            assertEquals(expDouble_Double, (Double) ae.getValue(row));

            ae = this.createExpression(operationName, new Byte("5"), new Short("10"));
            assertEquals(expDouble_Double, (Double) ae.getValue(row));

            ae = this.createExpression(operationName, new Byte("5"), new Integer("10"));
            assertEquals(expDouble_Double, (Double) ae.getValue(row));

            ae = this.createExpression(operationName, new Byte("5"), new Long("10"));
            assertEquals(expDouble_Double, (Double) ae.getValue(row));

        } else {
            ae = this.createExpression(operationName, new Byte("5"), new Byte("10"));
            assertEquals(expInt_Int, (Long) ae.getValue(row));

            ae = this.createExpression(operationName, new Byte("5"), new Short("10"));
            assertEquals(expInt_Int, (Long) ae.getValue(row));

            ae = this.createExpression(operationName, new Byte("5"), new Integer("10"));
            assertEquals(expInt_Int, (Long) ae.getValue(row));

            ae = this.createExpression(operationName, new Byte("5"), new Long("10"));
            assertEquals(expInt_Int, (Long) ae.getValue(row));
        }

        ae = this.createExpression(operationName, new Byte("5"), new Float("10.2"));
        assertEquals(expInt_Float, (Double) ae.getValue(row));

        ae = this.createExpression(operationName, new Byte("5"), new Double("10.2"));
        assertEquals(expInt_Double, (Double) ae.getValue(row));

        ae = this.createExpression(operationName, new Byte("5"), new BigDecimal(10.2));
        assertEquals(expInt_Dec, (BigDecimal) ae.getValue(row));

        //
        // replace operands
        //
        if (this.operationName.equals("/")) {
            ae = this.createExpression(operationName, new Short("5"), new Byte("10"));
            assertEquals(expDouble_Double, (Double) ae.getValue(row));

            ae = this.createExpression(operationName, new Integer("5"), new Byte("10"));
            assertEquals(expDouble_Double, (Double) ae.getValue(row));

            ae = this.createExpression(operationName, new Long("5"), new Byte("10"));
            assertEquals(expDouble_Double, (Double) ae.getValue(row));

        } else {
            ae = this.createExpression(operationName, new Short("5"), new Byte("10"));
            assertEquals(expInt_Int, (Long) ae.getValue(row));

            ae = this.createExpression(operationName, new Integer("5"), new Byte("10"));
            assertEquals(expInt_Int, (Long) ae.getValue(row));

            ae = this.createExpression(operationName, new Long("5"), new Byte("10"));
            assertEquals(expInt_Int, (Long) ae.getValue(row));
        }
        ae = this.createExpression(operationName, new Float("5.1"), new Byte("10"));
        assertEquals(expFloat_Int, (Double) ae.getValue(row));

        ae = this.createExpression(operationName, new Double("5.1"), new Byte("10"));
        assertEquals(expDouble_Int, (Double) ae.getValue(row));

        ae = this.createExpression(operationName, new BigDecimal(5.1), new Byte("10"));
        assertEquals(expDec_Int, (BigDecimal) ae.getValue(row));

        //********************************************************************
        // Short + others
        //********************************************************************
        if (this.operationName.equals("/")) {
            ae = this.createExpression(operationName, new Short("5"), new Long("10"));
            assertEquals(expDouble_Double, (Double) ae.getValue(row));

        } else {
            ae = this.createExpression(operationName, new Short("5"), new Long("10"));
            assertEquals(expInt_Int, (Long) ae.getValue(row));
        }
        ae = this.createExpression(operationName, new Short("5"), new Float("10.2"));
        assertEquals(expInt_Float, (Double) ae.getValue(row));

        ae = this.createExpression(operationName, new Short("5"), new Double("10.2"));
        assertEquals(expInt_Double, (Double) ae.getValue(row));

        ae = this.createExpression(operationName, new Short("5"), new BigDecimal(10.2));
        assertEquals(expInt_Dec, (BigDecimal) ae.getValue(row));
        //
        // replace operands
        //
        if (this.operationName.equals("/")) {
            ae = this.createExpression(operationName, new Long("5"), new Short("10"));
            assertEquals(expDouble_Double, (Double) ae.getValue(row));

        } else {
            ae = this.createExpression(operationName, new Long("5"), new Short("10"));
            assertEquals(expInt_Int, (Long) ae.getValue(row));
        }
        ae = this.createExpression(operationName, new Float("5.1"), new Short("10"));
        assertEquals(expFloat_Int, (Double) ae.getValue(row));

        ae = this.createExpression(operationName, new Double("5.1"), new Short("10"));
        assertEquals(expDouble_Int, (Double) ae.getValue(row));

        ae = this.createExpression(operationName, new BigDecimal(5.1), new Short("10"));
        assertEquals(expDec_Int, (BigDecimal) ae.getValue(row));

        //********************************************************************
        // Long + others
        //********************************************************************
        if (this.operationName.equals("/")) {
            ae = this.createExpression(operationName, new Long("5"), new Long("10"));            
            assertEquals(expDouble_Double, (Double) ae.getValue(row));
        } else {
            ae = this.createExpression(operationName, new Long("5"), new Long("10"));
            assertEquals(expInt_Int, (Long) ae.getValue(row));
        }
        ae = this.createExpression(operationName, new Long("5"), new Float("10.2"));
        assertEquals(expInt_Float, (Double) ae.getValue(row));

        ae = this.createExpression(operationName, new Long("5"), new Double("10.2"));
        assertEquals(expInt_Double, (Double) ae.getValue(row));

        ae = this.createExpression(operationName, new Long("5"), new BigDecimal(10.2));
        assertEquals(expInt_Dec, (BigDecimal) ae.getValue(row));

        //
        // replace operands
        //
        ae = this.createExpression(operationName, new Float("5.1"), new Long("10"));
        assertEquals(expFloat_Int, (Double) ae.getValue(row));

        ae = this.createExpression(operationName, new Double("5.1"), new Long("10"));
        assertEquals(expDouble_Int, (Double) ae.getValue(row));

        ae = this.createExpression(operationName, new BigDecimal(5.1), new Long("10"));
        assertEquals(expDec_Int, (BigDecimal) ae.getValue(row));

        //********************************************************************
        // Float + others
        //********************************************************************

        ae = this.createExpression(operationName, new Float("5.1"), new Float("10.2"));
        assertEquals(expFloat_Float, (Double) ae.getValue(row));

        ae = this.createExpression(operationName, new Float("5.1"), new Double("10.2"));
        assertEquals(expFloat_Double, (Double) ae.getValue(row));

        ae = this.createExpression(operationName, new Float("5.1"), new BigDecimal(10.2));
        assertEquals(expFloat_Dec, (BigDecimal) ae.getValue(row));

        ae = this.createExpression(operationName, new Double("5.1"), new Float("10.2"));
        assertEquals(expDouble_Float, (Double) ae.getValue(row));

        ae = this.createExpression(operationName, new BigDecimal(5.1), new Float("10.2"));
        assertEquals(expDec_Float, (BigDecimal) ae.getValue(row));

        //********************************************************************
        // Double + others
        //********************************************************************
        ae = this.createExpression(operationName, new Double("5.1"), new Double("10.2"));
        assertEquals(expDouble_Double, (Double) ae.getValue(row));


        ae = this.createExpression(operationName, new Double("5.1"), new BigDecimal(10.2));
        assertEquals(expDouble_Dec, (BigDecimal) ae.getValue(row));

        ae = this.createExpression(operationName, new BigDecimal(5.1), new Double("10.2"));
        assertEquals(expDec_Double, (BigDecimal) ae.getValue(row));
        //
        // replace operands
        //
        ae = this.createExpression(operationName, new Float("5.1"), new Long("10"));
        assertEquals(expFloat_Int, (Double) ae.getValue(row));

        ae = this.createExpression(operationName, new Double("5.1"), new Long("10"));
        assertEquals(expDouble_Int, (Double) ae.getValue(row));

        ae = this.createExpression(operationName, new BigDecimal(5.1), new Long("10"));
        assertEquals(expDec_Int, (BigDecimal) ae.getValue(row));

    }
}
