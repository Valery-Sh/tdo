/*
 * FunctionOperator.java
 * 
 * Created on 14.06.2007, 19:46:42
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tdo.expr;

import java.lang.reflect.Constructor;
import tdo.DataSet;
import tdo.expr.FunctionAggregateExpression.AvgAggExpression;
import tdo.expr.FunctionAggregateExpression.CountAggExpression;
import tdo.expr.FunctionAggregateExpression.LevelAggExpression;
import tdo.expr.FunctionAggregateExpression.MaxAggExpression;
import tdo.expr.FunctionAggregateExpression.MinAggExpression;
import tdo.expr.FunctionAggregateExpression.SumAggExpression;
import tdo.expr.FunctionExpression.BigDecimalExpression;
import tdo.expr.FunctionExpression.ByteExpression;
import tdo.expr.FunctionExpression.DateConstantExpression;
import tdo.expr.FunctionExpression.DateExpression;
import tdo.expr.FunctionExpression.DateFormatExpression;
import tdo.expr.FunctionExpression.DoubleExpression;
import tdo.expr.FunctionExpression.FloatExpression;
import tdo.expr.FunctionExpression.HeaderExpression;
import tdo.expr.FunctionExpression.IIFExpression;
import tdo.expr.FunctionExpression.IntegerExpression;
import tdo.expr.FunctionExpression.JavaMathExpression;
import tdo.expr.FunctionExpression.LongExpression;
import tdo.expr.FunctionExpression.LowerCaseExpression;
import tdo.expr.FunctionExpression.NowExpression;
import tdo.expr.FunctionExpression.NumberFormatExpression;
import tdo.expr.FunctionExpression.PadExpression;
import tdo.expr.FunctionExpression.ProperExpression;
import tdo.expr.FunctionExpression.ShortExpression;
import tdo.expr.FunctionExpression.SubstringExpression;
import tdo.expr.FunctionExpression.TimestampExpression;
import tdo.expr.FunctionExpression.TrimExpression;
import tdo.expr.FunctionExpression.UnknownFunctionExpression;
import tdo.expr.FunctionExpression.UpperCaseExpression;
import tdo.table.expr.*;
import tdo.tools.expr.LexConst;

/**
 * <p> ����� ��� ��������� ��������� ��������� ����������� �������, ����������
 * ���������������, �� ������� ������� ����������� ������� ������, �� �����
 * ������� ������������ ��� ��������-�������, ��� �������� ��������� ���������
 * ������ <code>FunctionOperator</code>. ������� ���������
 * �������� �������� ���������������� ��� ��������� �������� ����
 * {@link tdo.expr.FunctionExpression}.
 * <p>�������� ��������� ������������ ����������� ������
 * {@link #createExpression(tdo.expr.ExpressionContext, tdo.expr.IOperand, tdo.expr.IOperand)} .
 * ���������� ���������, ����������� �������� ���������, ������ �� ���������
 * ��������� �������� � ��������. ������, ����� ��������� �����������, ���
 * �������� ���� ����������� ����������� ��� �����.
 * <p>��������-������� ����� ���, ������� �������� ��� ������ � ������ ��������
 * {@link #getName() } � {@link #setName(java.lang.String) } .
 * <p>��������-������� ������������ ��� ������� ��������, ����� {@link #isUnary() }
 * �������� ���������� <code>true</code>.
 * <p>� ������� ���� �������� ������ ���� ������� � ������� ��������
 * <table border="1">
 * <thead>
 * <tr>
 *      <th>���</th>
 *      <th>��������</th>
 * </tr>
 * </thead>
 * <tbody>
 * <tr>
 * <td>UpperCase</td>
 * <td>������: Upper, U. ��������� ������� ������ � ������� �������</td>
 * </tr>
 * <tr>
 * <td>LowerCase</td>
 * <td>������: Lower, L. ��������� ������� ������ � ������ �������</td>
 * </tr>
 * <tr>
 * <td>Proper</td>
 * <td>������: P. ��������� ����� ������ ������ ������ � ������� �������</td>
 * </tr>
 * <tr>
 * <td>Header</td>
 * <td>������: Hd. ��������� ����� ������ ������ ������� ����� ������ � �������
 *   �������
 * </td>
 * </tr>
 * <tr>
 * <td>Trim</td>
 * <td>������: Tr. �������� ������� ������� ������� ����� � ������ ������
 * </td>
 * </tr>
 * <tr>
 * <td>Padl</td>
 * <td>������: PL. � ����������� �� ���������� ������� � ���� �������� ���������
 *   � ������ ����� �������� ���������� ��������
 * </td>
 * </tr>
 * <tr>
 * <td>Padr</td>
 * <td>������: Pr. � ����������� �� ���������� ������� � ���� �������� ���������
 *   � ������ ������ �������� ���������� ��������
 * </td>
 * </tr>
 * <tr>
 * <td>Substring</td>
 * <td>������: Substr. �������� ��������� �� �������� ������
 * </td>
 * </tr>
 * <tr>
 * <td>NumberFormat</td>
 * <td>������: NumFormat, NF. ����������� �������� �������� �������� ���������
 *       ������� � ���������� ��������� �������������
 * </td>
 * </tr>
 * <tr>
 * <td>RNumberFormat</td>
 * <td>������: RNumFormat, RNF. ����������� �������� �������� �������� ���������
 *       ������� � ���������� ��������� �������������. ���������� ����������,
 *       �� �����, ���������� � �������.
 * </td>
 * </tr>
 * <tr>
 * <td>DateFormat</td>
 * <td>������: DF. ����������� �������� ���� �������� ��������� ������� �
 *                 ���������� ��������� �������������.
 * </td>
 * </tr>
 * <tr>
 * <td>RowValue</td>
 * <td>������: Rv. ���������� �������� ������ ���� ��� ��������� ������� ������� �
 * ��������� ����� �������.
 * </td>
 * </tr>
 * <tr>
 * <td>IIF</td>
 * <td>���������� ���� �� ���� �������� ��������, � ����������� �� ���������
 *   �������.
 * </td>
 * </tr>
 * <tr>
 * <td>ROWNO</td>
 * <td>���������� ������ �������� ���� ��� ��������� ������ ������� ���
 *  ������ �� ���������, ���� ������ ��������.
 * </td>
 * </tr>
 * <tr>
 * <td>RANDOM</td>
 * <td>���������� �������� <code>java.Math.random()</code>
 * </td>
 * </tr>
 * <tr>
 * <td>DEPTH</td>
 * <td>���������� �������� {@link tdo.RowState#getDepth() } ��� �������� ���� �
 *   ��������� ������ ������� ��� ������ �� ���������, ���� ������ ��������.
 * </td>
 * </tr>
 * <tr>
 * <td>ISDELETED</td>
 * <td>������: IsDel. ���������� �������� <code>true</code> ���� ������� ��� ������
 *  ��� ��������� ������ ������� ��� ������ �� ���������, ���� ������ ��������.
 *  ���� ��� �� ������ ���������� <code>false</code>.
 * </td>
 * </tr>
 * <tr>
 * <td>ROWSTATE</td>
 * <td>���������� �������� �������� <code>editingState</code> �������� ����
 *   ��� ��������� ������ ������� ��� ������ �� ���������, ���� ������ ��������.
 *</td>
 *</tr>
 * <tr>
 * <td>BIGDECIMAL</td>
 * <td>������: Dec. ����������� �������� �������� � ���
 *   <code>java.math.BigDecimal</code>.
 * </td>
 * </tr>
 * <tr>
 * <td>DOUBLE</td>
 * <td>������: Dou. ����������� �������� �������� � ���
 *   <code>java.lang.Double</code>.
 * </td>
 * </tr>
 * <tr>
 * <td>FLOAT</td>
 * <td>������: FL, REAL. ����������� �������� �������� � ���
 *   <code>java.lang.Float</code>.
 * </td>
 * </tr>
 * <tr>
 * <td>LONG</td>
 * <td>������: LO, BigInt. ����������� �������� �������� � ���
 *   <code>java.lang.Long</code>.
 * </td>
 * </tr>
 * <tr>
 * <td>INTEGER</td>
 * <td>������: Int. ����������� �������� �������� � ���
 *   <code>java.lang.Integer</code>.
 * </td>
 * </tr>
 * <tr>
 * <td>SHORT</td>
 * <td>������: Small, Smallint. ����������� �������� �������� � ���
 *   <code>java.lang.Short</code>.
 * </td>
 * </tr>
 * <tr>
 * <td>BYTE</td>
 * <td>������: Tiny, TyniInt. ����������� �������� �������� � ���
 *   <code>java.lang.Byte</code>.
 * </td>
 * </tr>
 * <tr>
 * <td>Date</td>
 * <td>������: Dat. ����������� �������� �������� � ���
 *   <code>java.util.Date</code> �� ��������� �������.
 * </td>
 * </tr>
 * <tr>
 * <td>$</td>
 * <td>	������� � ���������� ������ ���� <code>java.util.Date</code> �� ��������
 *     ������������� ��������� ����, ������ � ��� ������.
 * </td>
 * </tr>
 * <tr>
 * <td>TIMESTAMP</td>
 * <td>������: TSM. ����������� �������� �������� � ���
 *   <code>java.sql.Timestamp</code> �� ��������� �������.
 * </td>
 * </tr>
 * <tr>
 * <td>Now</td>
 * <td>������: today. ���������� ������� ���� ��� ������ ����
 *   <code>java.util.Date</code>.
 * </td>
 * </tr>
 *
 * <tr>
 * <td>ABS</td>
 * <td>�������������  ������� �� ������ <code>java.Math.abs(Double)</code></td>
 * </tr>
 * <tr>
 * <td>ACOS</td>
 * <td>�������������  ������� �� ������ <code>java.Math.acos(Double)</code></td>
 * </tr>
 * <tr>
 * <td>ASIN</td>
 * <td>�������������  ������� �� ������ <code>java.Math.asin(Double)</code></td>
 * </tr>
 * <tr>
 * <td>ATAN</td>
 * <td>�������������  ������� �� ������ <code>java.Math.atan(Double)</code></td>
 * </tr>
 * <tr>
 * <td>CEIL</td>
 * <td>�������������  ������� �� ������ <code>java.Math.ceil(Double)</code></td>
 * </tr>
 * <tr>
 * <td>COS</td>
 * <td>�������������  ������� �� ������ <code>java.Math.cos(Double)</code></td>
 * </tr>
 * <tr>
 * <td>SIN</td>
 * <td>�������������  ������� �� ������ <code>java.Math.sin(Double)</code></td>
 * </tr>
 * <tr>
 * <td>TAN</td>
 * <td>�������������  ������� �� ������ <code>java.Math.tan(Double)</code></td>
 * </tr>
 * <tr>
 * <td>EXP</td>
 * <td>�������������  ������� �� ������ <code>java.Math.exp(Double)</code></td>
 * </tr>
 * <tr>
 * <td>FLOOR</td>
 * <td>�������������  ������� �� ������ <code>java.Math.floor(Double)</code></td>
 * </tr>
 * <tr>
 * <td>LOG</td>
 * <td>�������������  ������� �� ������ <code>java.Math.log(Double)</code></td>
 * </tr>
 * <tr>
 * <td>RINT</td>
 * <td>�������������  ������� �� ������ <code>java.Math.rint(Double)</code></td>
 * </tr>
 * <tr>
 * <td>ROUND</td>
 * <td>�������������  ������� �� ������ <code>java.Math.round(Double)</code></td>
 * </tr>
 * <tr>
 * <td>SQRT</td>
 * <td>�������������  ������� �� ������ <code>java.Math.sqrt(Double)</code></td>
 * </tr>
 * <tr>
 * <td>TODEGREES</td>
 * <td>�������������  ������� �� ������ <code>java.Math.toDegrees(Double)</code></td>
 * </tr>
 * <tr>
 * <td>TORADIANS</td>
 * <td>�������������  ������� �� ������ <code>java.Math.toRadians(Double)</code></td>
 * </tr>
 * <tr>
 * <td>ATAN2</td>
 * <td>�������������  ������� �� ������ <code>java.Math.atan2(Double,Double)</code></td>
 * </tr>
 * <tr>
 * <td>POW</td>
 * <td>�������������  ������� �� ������ <code>java.Math.pow(Double,Double)</code></td>
 * </tr>
 * <tr>
 * <td>IEEEREMAINDER</td>
 * <td>�������������  ������� �� ������ <code>java.Math.IEEEremainder(Double,Double)</code></td>
 * </tr>
 * </tbody>
 * </table>
 */
public class FunctionOperator extends AbstractOperator {

    /**
     * ������ ��� �������
     */
    private String name;
    /**
     * ������� ��������� ������ ��� �������� ����� ������� <code>null</code>.
     */
    public FunctionOperator() {
        this(null);
    }

    /**
     * ������� ��������� ������ ��� ��������� ����� �������.
     * @param fname ��� �������
     */
    public FunctionOperator(String fname) {
        this.name = fname;
    }

    /**
     * ���������� ��� �������.
     * @return ��� �������
     */
    public String getName() {
        return name;
    }

    /**
     * ������������� ����� �������� ����� �������.
     * @param fname ����� ��� �������
     */
    public void setName(String fname) {
        name = fname;
    }

    /**
     * ������������� �������� �������� <code>lexType</code>
     * ������ �������� {@link tdo.tools.expr.LexConst#IDENTIFIER }.
     */
    @Override
    protected void setLexType() {
        lexType = LexConst.IDENTIFIER;
    }
    /**
     * ���������� �������� ���������� ��������� ��� ���������� ���������.
     * @return �������� ���������� ������ 10
     */
    @Override
    public int getPriority() {
        return 10;
    }
    /**
     * @return ������ ������, ���������� ��� �������
     */
    @Override
    public String toString() {
        return name;
    }
    /**
     * @return ���������� <code>true</code>, �������� ��� �������� �������
     */
    @Override
    public boolean isUnary() {
        return true;
    }

    /**
     * ������� ��������� ��� ��������� ��������� � ��������.<p>
     * � ����������� �� ����� ������� ������������ �����, ����������� ��
     * {@link tdo.expr.FunctionExpression } � ��������� ��� ���������.
     * @param context �������� ���������
     * @param op1 ������� ���������
     * @return ��������� ���������
     */
    @Override
    public IExpression createExpression(ExpressionContext context, IOperand op1) {
        IExpression expr = null;
        String f = this.getName().toUpperCase();
        if (f.equals("U") || f.equals("UPPER") || f.equals("UPPERCASE")) {
            expr = new UpperCaseExpression(context, this, op1);
            return expr;
        }
        if (f.equals("L") || f.equals("LOWER") || f.equals("LOWERCASE")) {
            expr = new LowerCaseExpression(context, this, op1);
            return expr;
        }
        if (f.equals("P") || f.equals("PROPER")) {
            expr = new ProperExpression(context, this, op1);
            return expr;
        }
        if (f.equals("HEADER") || f.equals("HD")) {
            expr = new HeaderExpression(f, context, this, op1);
            return expr;
        }
        if (f.equals("TRIM") || f.equals("TR")) {
            expr = new TrimExpression(f, context, this, op1);
            return expr;
        }
        
        if (f.equals("PADL") || f.equals("PL") ||
                f.equals("PADR") || f.equals("PR")) {
            expr = new PadExpression(f, context, this, op1);
            return expr;
        }
        if (f.equals("SUBSTR") || f.equals("SUBSTRING")) {
            expr = new SubstringExpression(context, this, op1);
            return expr;
        }

        if (f.equals("NUMBERFORMAT") || f.equals("NUMFORMAT") || f.equals("NF")) {
            expr = new NumberFormatExpression("NF",context, this, op1);
            return expr;
        }
        if (f.equals("RNUMBERFORMAT") || f.equals("RNUMFORMAT") || f.equals("RNF")) {
            expr = new NumberFormatExpression("RNF",context, this, op1);
            return expr;
        }

        if (f.equals("RV") || f.equals("ROWVALUE")) {
            expr = new RowValueExpression(context, this, op1);
            return expr;
        }

        if (f.equals("IIF")) {
            expr = new IIFExpression(context, this, op1);
            return expr;
        }
        if (f.equals("ROWNO")) {
            expr = new RowNoExpression(context, this, op1);
            return expr;
        }

        if (f.equals("DEPTH")) {
            expr = new DepthExpression(context, this, op1);
            return expr;
        }
        if (f.equals("LEVEL")) {
            expr = new LevelAggExpression(context, this, op1);
            return expr;
        }

        if (f.equals("SUM")) {
            expr = new SumAggExpression(context, this, op1);
            return expr;
        }
        if (f.equals("COUNT")) {
            expr = new CountAggExpression(context, this, op1);
            return expr;
        }
        if (f.equals("AVG")) {
            expr = new AvgAggExpression(context, this, op1);
            return expr;
        }

        if (f.equals("MIN")) {
            expr = new MinAggExpression(context, this, op1);
            return expr;
        }
        if (f.equals("MAX")) {
            expr = new MaxAggExpression(context, this, op1);
            return expr;
        }

        if (f.equals("ISDELETED") || f.equals("ISDEL")) {
            expr = new IsDeletedExpression(context, this, op1);
            return expr;
        }
        if (f.equals("ROWSTATE")) {
            expr = new RowStateExpression(context, this, op1);
            return expr;
        }

        // Covertion functions
        if (f.equals("BIGDECIMAL") || f.equals("DEC")) {
            expr = new BigDecimalExpression(context, this, op1);
            return expr;
        }
        if (f.equals("DOUBLE") || f.equals("DOU")) {
            expr = new DoubleExpression(context, this, op1);
            return expr;
        }
        if (f.equals("FLOAT") || f.equals("FL") || f.equals("REAL")) {
            expr = new FloatExpression(context, this, op1);
            return expr;
        }

        if (f.equals("INTEGER") || f.equals("INT")) {
            expr = new IntegerExpression(context, this, op1);
            return expr;
        }
        if (f.equals("LONG") || f.equals("LO") || f.equals("BIGINT")) {
            expr = new LongExpression(context, this, op1);
            return expr;
        }
        if (f.equals("SHORT") || f.equals("SMALL") || f.equals("SMALLINT")) {
            expr = new ShortExpression(context, this, op1);
            return expr;
        }
        if (f.equals("BYTE") || f.equals("TINY") || f.equals("TINYINT")) {
            expr = new ByteExpression(context, this, op1);
            return expr;
        }
        if (f.equals("DATE") || f.equals("DAT") ) {
            expr = new DateExpression(context, this, op1);
            return expr;
        }
        if (f.equals("$") ) {
            expr = new DateConstantExpression(context, this, op1);
            return expr;
        }

        if (f.equals("TIMESTAMP") || f.equals("TSM")) {
            expr = new TimestampExpression(context, this, op1);
            return expr;
        }
        if (f.equals("NOW") || f.equals("TODAY")) {
            expr = new NowExpression(context, this, op1);
            return expr;
        }
        if (f.equals("DATEFORMAT") || f.equals("DF")) {
            expr = new DateFormatExpression(context, this, op1);
            return expr;
        }

        // java.Math functions

        if (f.equals("ABS") || f.equals("ACOS") || f.equals("ASIN") ||
                f.equals("RANDOM") || f.equals("ATAN") || f.equals("ATAN2") ||
                f.equals("CEIL") || f.equals("COS") ||
                f.equals("EXP") || f.equals("FLOOR") ||
                f.equals("LOG") || f.equals("POW") ||
                f.equals("RINT") || f.equals("ROUND") ||
                f.equals("SIN") || f.equals("SQRT") ||
                f.equals("TAN") || f.equals("TODEGREES") ||
                f.equals("TORADIANS") || f.equals("IEEEREMAINDER")) {
            expr = new JavaMathExpression(f, context, this, op1);
            return expr;
        }


        String s = DataSet.getFunctionClass(f);
        if (s != null) {
            try {
                Class fclass = Class.forName(s);
                Constructor c = fclass.getConstructor(ExpressionContext.class, IOperator.class, IOperand.class);
                expr = (FunctionExpression) c.newInstance(context, this, op1);
            } catch (Exception e) {
                expr = null;
            }

        } else {
            context.addError(ExpressionContext.UNKNOWNFUNCTION, new UnknownFunctionExpression(context, this, op1));
        }
        return expr;
    }
}//class FunctionOperator
