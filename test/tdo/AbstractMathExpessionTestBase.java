/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo;
import java.math.BigDecimal;
import tdo.expr.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author valery
 */
public abstract class AbstractMathExpessionTestBase {
    ExpressionContext context;
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        this.context = this.createContext();
    }
    
    public ExpressionContext getContext() {
        return this.context;
    }
    
/*    public Object[] getFirstOperands() {
        Object[] r = new Object[]{1,5,null, 10,50,null, 100.63, 200.75, null};
        return r;
    }
    public void getSecondOperands() {
        
    }
    public Object getExpectedAdd(Object o1, Object o2) {
        Object e = null;
        Object[] c = convert(o1,o2);
        return null;
    }
    
    public Object[] convert(Object o, int opNum) {
        int n0,n1 = 0;
        if ( opNum == 2 ) {
            n0 = 0;
            n1 = 1;
        } else {
            n0 = 1;
            n1 = 0;
        }
        Object[] r = new Object[2];
        r[n1] = o;
        if ( o.getClass().equals(Byte.class)) {
            r[n0] = new Byte("0");
        } else if ( o.getClass().equals(Short.class)) {
            r[n0] = new Short("0");
        } else if ( o.getClass().equals(Integer.class)) {
            r[n0] = new Integer(0);
        } if ( o.getClass().equals(Long.class)) {
            r[n0] = new Long(0);
        } if ( o.getClass().equals(Float.class)) {
            r[n0] = new Double(0);
            r[n1] = new Double( ((Float)o).doubleValue());
        } if ( o.getClass().equals(Double.class)) {
            r[n0] = new Double(0);
            r[n1] = o;
        }

            
        return r;
    }
    
    public Object[] convert(Object o1, Object o2) {
        Object[] r = new Object[2];
        if ( o1 == null && o2 == null ) {
            r[0] = 0;
            r[1] = 0;
            
        } else  if ( o1 == null ) {
                r = convert(o2, 2);
        } else {
                r = convert(o1, 1);
        }
            
        return r;
    }
 */   
    public void createData(Table t) {
        t.addRow( new Object[]{1,5,null, 10,50,null, 100.63, 200.75, null});
        t.addRow( new Object[]{5,1,null, 50,10,null, 200.75, 100.63, null});        
    }    
  
    public void createColumns(Table t) {
        DataColumnCollection dcc = t.getColumns();
        DataColumn c = dcc.add(Integer.class,"int1");
        c = dcc.add(Integer.class,"int2");
        c = dcc.add(Integer.class,"int3");
        c = dcc.add(Long.class,"long1");
        c = dcc.add(Long.class,"long2");
        c = dcc.add(Long.class,"long3");
        c = dcc.add(Double.class,"double1");
        c = dcc.add(Double.class,"double2");
        c = dcc.add(Double.class,"double3");
        
        
        
    }
    public ExpressionContext createContext() {
        DefaultExpressionContext c = null;
/*        Table dt = new DataTable();
        createColumns(dt);
        createData(dt);
 */ 
        c = new DefaultExpressionContext(true); //testMode=true
        
  //      c.addTable(dt);
        return c;
    }
    
    private IOperator createOperator(String opname) {
        IOperator op = null;
        if ( opname.equals("+") )
            op = new AbstractOperator.AddOperator();
        else if ( opname.equals("||") )
            op = new AbstractOperator.ConcatOperator();
        else if ( opname.equals("-") )
            op = new AbstractOperator.SubtrOperator();
        else if ( opname.equals("*") )
            op = new AbstractOperator.MultOperator();
        else if ( opname.equals("/") )
            op = new AbstractOperator.DivOperator();
        
        op.setContext(context);
        return op;
    }
    
    public MathExpression createExpression(String opname, Object op1, Object op2) {
        IOperand lop1;
        if (  op1 instanceof NamedOperand )
            lop1 = (IOperand)op1;
        else
            lop1  = new LiteralOperand(op1);
        IOperand lop2;
        if (  op2 instanceof NamedOperand )
            lop2 = (IOperand)op2;
        else
            lop2  = new LiteralOperand(op2);
        
        
        IOperator op = this.createOperator(opname);
        return (MathExpression)op.createExpression(context, lop1, lop2);
    }

    /**
     * Test of the method <code>getType</code> according to
     * {@link tdo.expr.MathExpression#getType(tdo.expr.IOperator, tdo.expr.IOperand, tdo.expr.IOperand) }
     * method specification.
     * 
     * @see tdo.expr.MathExpression
     */
    @Test 
    public void testGetType() {

        NamedValues row = null;
        MathExpression ae = this.createExpression("+", 5, 10);
        assertEquals(Long.class,ae.getType());
        
        ae = this.createExpression("+", 5, 10.1);
        assertEquals(Double.class,ae.getType());        

        ae = this.createExpression("+", 5.1, 10);
        assertEquals(Double.class,ae.getType());        
        //*********************************************************
        // Byte + others
        //*********************************************************
        ae = this.createExpression("+", new Byte("5"), 10);
        assertEquals(Long.class,ae.getType());        

        ae = this.createExpression("+", new Short("5"), 10);
        assertEquals(Long.class,ae.getType());        
        
        ae = this.createExpression("+", new Long("5"), 10);
        assertEquals(Long.class,ae.getType());        

        ae = this.createExpression("+", new Float(5.1), 10);
        assertEquals(Double.class,ae.getType());        

        ae = this.createExpression("+", new Double(5.1), 10);
        assertEquals(Double.class,ae.getType());        

        ae = this.createExpression("+", new BigDecimal(5.1), 10);
        assertEquals(BigDecimal.class,ae.getType());        
        
        ae = this.createExpression("+", new Byte("5"), new Byte("10"));
        assertEquals(Long.class,ae.getType());        

        ae = this.createExpression("+", new Short("5"), new Byte("10"));
        assertEquals(Long.class,ae.getType());        
        
        ae = this.createExpression("+", new Long("5"), new Byte("10"));
        assertEquals(Long.class,ae.getType());        

        ae = this.createExpression("+", new Float(5.1), new Byte("10"));
        assertEquals(Double.class,ae.getType());        

        ae = this.createExpression("+", new Double(5.1), new Byte("10"));
        assertEquals(Double.class,ae.getType());        

        ae = this.createExpression("+", new BigDecimal(5.1), new Byte("10"));
        assertEquals(BigDecimal.class,ae.getType());                

        //*********************************************************
        // Short + others
        //*********************************************************
        ae = this.createExpression("+", new Short("5"), 10);
        assertEquals(Long.class,ae.getType());        

        ae = this.createExpression("+", new Short("5"), new Short("10") );
        assertEquals(Long.class,ae.getType());        
        
        ae = this.createExpression("+", new Short("5"), new Long("10"));
        assertEquals(Long.class,ae.getType());        

        ae = this.createExpression("+", new Short("10"), new Float(5.1));
        assertEquals(Double.class,ae.getType());        

        ae = this.createExpression("+", new Short("10"),new Double(5.1));
        assertEquals(Double.class,ae.getType());        

        ae = this.createExpression("+", new Short("10"),new BigDecimal(5.1));
        assertEquals(BigDecimal.class,ae.getType());                

        //*********************************************************
        // Long + others
        //*********************************************************
        
        ae = this.createExpression("+", new Long("5"), new Long("10"));
        assertEquals(Long.class,ae.getType());        

        ae = this.createExpression("+", new Long("10"), new Float(5.1));
        assertEquals(Double.class,ae.getType());        

        ae = this.createExpression("+", new Long("10"),new Double(5.1));
        assertEquals(Double.class,ae.getType());        

        ae = this.createExpression("+", new Long("10"),new BigDecimal(5.1));
        assertEquals(BigDecimal.class,ae.getType());                

        ae = this.createExpression("+", new Float(5.1), new Long("10"));
        assertEquals(Double.class,ae.getType());        

        ae = this.createExpression("+", new Double(5.1), new Long("10") );
        assertEquals(Double.class,ae.getType());        

        ae = this.createExpression("+", new BigDecimal(5.1), new Long(5));
        assertEquals(BigDecimal.class,ae.getType());                
        
        //*********************************************************
        // Float + others
        //*********************************************************
        
        ae = this.createExpression("+", new Float("5"), new Float("10"));
        assertEquals(Double.class,ae.getType());        

        ae = this.createExpression("+", new Float("10"), new Double(5.1));
        assertEquals(Double.class,ae.getType());        

        ae = this.createExpression("+", new Float("10"),new BigDecimal(5.1));
        assertEquals(BigDecimal.class,ae.getType());                

        ae = this.createExpression("+", new Double(5.1),new Float("10") );
        assertEquals(Double.class,ae.getType());        

        ae = this.createExpression("+",new BigDecimal(5.1), new Float("10"));
        assertEquals(BigDecimal.class,ae.getType());                

        //*********************************************************
        // Double + others
        //*********************************************************

        ae = this.createExpression("+", new Double(5.1),new Double("10") );
        assertEquals(Double.class,ae.getType());        
        
        ae = this.createExpression("+", new Double("10"),new BigDecimal(5.1));
        assertEquals(BigDecimal.class,ae.getType());                


        ae = this.createExpression("+",new BigDecimal(5.1), new Double("10"));
        assertEquals(BigDecimal.class,ae.getType());                
        
        //*********************************************************
        // null + others
        //*********************************************************
        ae = this.createExpression("+", new Byte("5"), null);
        assertEquals(Long.class,ae.getType());        

        ae = this.createExpression("+", new Short("5"), null);
        assertEquals(Long.class,ae.getType());        

        ae = this.createExpression("+", new Integer("5"), null);
        assertEquals(Long.class,ae.getType());        
        
        ae = this.createExpression("+", new Long("5"), null);
        assertEquals(Long.class,ae.getType());        

        ae = this.createExpression("+", new Float(5.1), null);
        assertEquals(Double.class,ae.getType());        

        ae = this.createExpression("+", new Double(5.1), null);
        assertEquals(Double.class,ae.getType());        

        ae = this.createExpression("+", new BigDecimal(5.1), null);
        assertEquals(BigDecimal.class,ae.getType());        
        
        ae = this.createExpression("+", null, new Byte("10"));
        assertEquals(Long.class,ae.getType());        

        ae = this.createExpression("+", null, new Short("10"));
        assertEquals(Long.class,ae.getType());        
        
        ae = this.createExpression("+", null, new Integer("10"));
        assertEquals(Long.class,ae.getType());        

        ae = this.createExpression("+", null, new Long("10"));
        assertEquals(Long.class,ae.getType());        
        
        ae = this.createExpression("+", null, new Float(5.1));
        assertEquals(Double.class,ae.getType());        

        ae = this.createExpression("+", null,new Double(5.1));
        assertEquals(Double.class,ae.getType());        

        ae = this.createExpression("+", null,new BigDecimal(5.1));
        assertEquals(BigDecimal.class,ae.getType());                
        
        ae = this.createExpression("+", null,null);
        assertEquals(Long.class,ae.getType());                

        //
        // one or both operand are IdentifierOperand
        //
        ae = this.createExpression("+", new IdentifierOperand("testColumnName",this.context),new BigDecimal(5.1));
        assertEquals(MathExpression.class,ae.getType());                        
        ae = this.createExpression("+", 10.1d,new IdentifierOperand("testColumnName",this.context));
        assertEquals(MathExpression.class,ae.getType());                        
        ae = this.createExpression("+", new IdentifierOperand("testColumnName1",this.context),new IdentifierOperand("testColumnName2",this.context));
        assertEquals(MathExpression.class,ae.getType());                        
        //
        // Concatanation operator (||)
        //
        ae = this.createExpression("||", new IdentifierOperand("testColumnName1",this.context),new IdentifierOperand("testColumnName2",this.context));
        assertEquals(String.class,ae.getType());                        
        
        
    }
}//AbstractMathExpessionTestBase
