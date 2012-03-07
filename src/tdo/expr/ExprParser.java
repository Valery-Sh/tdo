/*
 * ExprParser.java
 *
 * Created on 14.06.2007, 14:15:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tdo.expr;

import java.util.Vector;
import tdo.expr.AbstractOperator.AddOperator;
import tdo.expr.AbstractOperator.AndBetweenOperator;
import tdo.expr.AbstractOperator.AndOperator;
import tdo.expr.AbstractOperator.AsOperator;
import tdo.expr.AbstractOperator.BetweenOperator;
import tdo.expr.AbstractOperator.CommaOperator;
import tdo.expr.AbstractOperator.ConcatOperator;
import tdo.expr.AbstractOperator.ContainingOperator;
import tdo.expr.AbstractOperator.DivOperator;
import tdo.expr.AbstractOperator.EqualsOperator;
import tdo.expr.AbstractOperator.GreaterEqualsOperator;
import tdo.expr.AbstractOperator.GreaterOperator;
import tdo.expr.AbstractOperator.InOperator;
import tdo.expr.AbstractOperator.IsNotNullOperator;
import tdo.expr.AbstractOperator.IsNullOperator;
import tdo.expr.AbstractOperator.LessEqualsOperator;
import tdo.expr.AbstractOperator.LessOperator;
import tdo.expr.AbstractOperator.LikeOperator;
import tdo.expr.AbstractOperator.MultOperator;
import tdo.expr.AbstractOperator.NotContainingOperator;
import tdo.expr.AbstractOperator.NotEqualsOperator;
import tdo.expr.AbstractOperator.NotInOperator;
import tdo.expr.AbstractOperator.NotLikeOperator;
import tdo.expr.AbstractOperator.NotOperator;
import tdo.expr.AbstractOperator.NotRegExOperator;
import tdo.expr.AbstractOperator.NotStartingWithOperator;
import tdo.expr.AbstractOperator.OrOperator;
import tdo.expr.AbstractOperator.RegExOperator;
import tdo.expr.AbstractOperator.StartingWithOperator;
import tdo.expr.AbstractOperator.SubtrOperator;
import tdo.tools.expr.LexConst;
import tdo.tools.expr.Symbol;
import tdo.tools.scanner.BufferedScanner;
import tdo.tools.scanner.ExpressionException;

/**
 * ��������� ��������� ( ������� ) ����������,
 * �������������� � ��������� ���������, �������������� � ���� ������ ������.
 * ��������� � ��������� ���� �������� ������� �����������, ������� �� ������
 * ������������� � ����������� ������, � ������ � ������ <i>�������� ��������
 * ������</i> (RPN). <br>
 *
 * ���� �������� ������ ���������:
 *      <code><PRE>col1=5 and col2 > 7 and ( col3 LIKE '%ab' OR
 *                 col4 CONTAINING '56' )
 *      </PRE></CODE>
 * ��������� ��� ������������ ������������� ����� ��������� � Java-���������:
 *      <code><PRE>
 *          .......
 *          .......
 *        String expr = "col1=5 and col2 > 7 and ( col3 LIKE '%ab' OR " +
 *                      "col4 CONTAINING '56' )";
 *        ExprParser parser = new ExprParser(expr);
 *        parser.parse();
 *        Vector rpn = exprparser.getRPN();
 *      </PRE></CODE>
 *
 * ����������� ���������� ��������� ��������:
 * <UL>
 *   <LI>
 *      ��������������, ��������������� ����� ����� ������� ��� ��� ���
 *      ���������� � ������ {@link tdo.common.data.DataColumn}
 *   </LI>
 *   <LI>
 *      ��������� ��������, ����������� � ��������� �������, ��������,
 *      <code><PRE>'Mr. Smith'</PRE></CODE>
 *   </LI>
 *   <LI>
 *      ������������� ��������, ��������, <code><PRE>125</PRE></CODE>,
 *      ������� ������������� ������ ���� <code>int</code>
 *   </LI>
 *   <LI>
 *      ������������� ��������, ��������, <code><PRE>125L</PRE></CODE>,
 *      ��� <code><PRE>125l</PRE></CODE>
 *      ������� ������������� ������ ���� <code>long</code>
 *   </LI>
 *   <LI>
 *      �������� � ��������� ������, ��������, <code><PRE>125.7F</PRE></CODE>,
 *      ��� <code><PRE>125.7f</PRE></CODE> ��� <code><PRE>1257E-1F</PRE></CODE>
 *      ������� ������������� ������ ���� <code>float</code>
 *   </LI>
 *   <LI>
 *      �������� � ��������� ������, ��������, <code><PRE>125.7</PRE></CODE> ���
 *      <code><PRE>1257E-1</PRE></CODE>
 *      ������� ������������� ������ ���� <code>double</code>
 *   </LI>
 *   <LI>
 *      ������ ��������, ����������� �������� <code>true</code> ���
 *      <code>false</code>.
 *   </LI>
 *   <LI>
 *      ������� <code>null</code>
 *   </LI>
 * </UL>
 *
 * ����������� ��������������� ����������� ��������� ��������:
 * <UL>
 *   <LI>
 *      �������� (+), �������� <code><PRE>col + 7</PRE></CODE>
 *   </LI>
 *   <LI>
 *      ��������� (-), �������� <code><PRE>col - 7</PRE></CODE>
 *   </LI>
 *   <LI>
 *      ��������� (*), �������� <code><PRE>col * 7</PRE></CODE>
 *   </LI>
 *   <LI>
 *      ������� (/), �������� <code><PRE>col / 7</PRE></CODE>
 *   </LI>
 * </UL>
 *
 * ���������� ��������� ���������� ��������� ��������:
 * <UL>
 *   <LI>
 *      ��������� (������������) (||), ��������, 'ab' || 'cd'
 *   </LI>
 * </UL>
 *
 * �����������  ����������� ��������� � ��������� ��������:
 * <UL>
 *   <LI>
 *      ������ <code><PRE>(>)</PRE></code>
 *   </LI>
 *   <LI>
 *      ������ <code><PRE>(<)</PRE></code>
 *   </LI>
 *   <LI>
 *      ����� <code><PRE>(=)</PRE></code>
 *   </LI>
 *   <LI>
 *      �� ����� <code><PRE>(<>)</PRE></code>
 *   </LI>
 *   <LI>
 *      ������ ��� ����� <code><PRE>(>=)</PRE></code>
 *   </LI>
 *   <LI>
 *      ������ ��� �����<code><PRE>(<=)</PRE></code>
 *   </LI>
 *   <LI>
 *      �������� �� ��������� �������� <code>null</code> <code><PRE>(is null)</PRE></code>,
 *      ��������: <code><PRE>(Col2 is null)</PRE></code>
 *   </LI>
 *   <LI>
 *      �������� �� �� ��������� �������� <code>null</code>
 *      <code><PRE>(is not null)</PRE></code>,
 *      ��������: <code><PRE>(Col2 is null)</PRE></code>
 *   </LI>
 *   <LI>
 *      �������� �� �������������� �������� ��������� ���������
 *      <code><PRE>(between)</PRE></code>,
 *      ��������: <code><PRE>(Col2 between 36 and 58)</PRE></code>
 *   </LI>
 *   <LI>
 *      �������� �� ��������� �������� ������ �� ��������� ������
 *      <code><PRE>(in)</PRE></code>,
 *      ��������: <code><PRE>(Col2 in ( 1,2,36,58)</PRE></code>
 *   </LI>
 *   <LI>
 *      �������� �� ��������� ��������� ��������� ���������� ��������
 *      �������� ������
 *      <code><PRE>(starting with)</PRE></code>,
 *      ��������: <code><PRE>(Col2 starting with 'Mr.'</PRE></code>
 *   </LI>
 *   <LI>
 *      ��������, �������� �� ������ �������� ���������
 *      <code><PRE>(containing)</PRE></code>,
 *      ��������: <code><PRE>(Col2 containing 'Mr.'</PRE></code>
 *   </LI>
 *   <LI>
 *      �������� �� ������������ wildcard-�������
 *      <code><PRE>(like)</PRE></code>,
 *      ��������: <code><PRE>(Col2 like '%Mr%'</PRE></code>
 *   </LI>
 *   <LI>
 *      �������� �� ������������ ����������� ���������
 *      <code><PRE>(regex)</PRE></code>,
 *      ��������: <code><PRE>(Col2 regex '%Mr%'</PRE></code>
 *   </LI>
 * </UL>
 * �����������  ����������� �����������  � ��������� ��������:
 * <UL>
 *   <LI>
 *      AND - ���������� "�"
 *   </LI>
 *   <LI>
 *      OR - ���������� "���"
 *   </LI>
 *   <LI>
 *      NOT - ���������� ���������
 *   </LI>
 * </UL>
 */
public class ExprParser {

    private Symbol lastSymbol;
    /**
     * ������ �� lex-������
     */
    protected BufferedScanner scanner;
    /**
     */
    protected ExpressionContext context;
    /**
     * ��������� �������� ��������� � ��������� �����.
     */
    protected String expression;
    /**
     * ������, ���������� ����������� �������� ��������� ���������. ��������
     * �������� RPN ������������� ���������. ������ ������� - ��� ������,
     * ����������� ��������� {@link IToken}.
     */
    protected Vector rpn;
    /**
     * ������������ ��� ������������� ������ ������ ��� ����������
     * RPN- ���������.
     */
    protected RpnStack rpnStack;
    /**
     * ������������ ��� ��������� ��������� <code>BETWEEN</code>.
     */
    protected int state;

    /**
     * ������� ��������� ������ ��� ��������� ���������� ��������,
     * ����������� �������� ���������.
     * �������������� ���� {@link #rpn} ,  {@link #rpnStack},{@link #scanner} �
     * {@link #state}
     * @param context
     * @param expr �������� ��������� � ��������� �����.
     */
    public ExprParser(ExpressionContext context, String expr) {
        this.context = context;
        expression = expr;
        rpn = new Vector(20);
        rpnStack = new RpnStack();
        scanner = new BufferedScanner(expression);
        state = -1;
    }

    /**
     * ���������� ��������� ����� ������, �������� ��������
     * ������������ ��������� ������  ��� �������� ������ ����������.
     */
    public void reset() {
        rpn = new Vector(20);
        rpnStack = new RpnStack();
        scanner.reset();
        state = -1;
    }

    /**
     * ������������ �������� ���������, �������������� ������� ������ �
     * ������� ������ ���� {@link java.util.Vector}, ����������
     * ����������� ����� ��������� ��� RPN-������, ��������� �����������������
     * ���������� ����� ����� ������ {@link #getRPN}.
     *
     */
    public void parse() {
        IToken lastToken = null;
        while (scanner.hasNextSymbol()) {
            IToken token = null;
            Symbol symbol = scanner.nextSymbol();
            if (isUnarySign(symbol)) {
                //������� ���� + ��� - �������� �� �������� ��������
                Double d = new Double(0);
                IOperand op = this.createLiteralOperand(d);
                ((LiteralOperand) op).lexType = LexConst.FLOATING_POINT_LITERAL;
                dispatch(op);
                token = createToken(symbol);
            } else if (isIdentifier(symbol)) {
                //��� ����� ���� ��� �������, ��� ������� � ��������������
                //������� ��� ��� �������
                Symbol peeked = scanner.peekNextSymbol();

                if (peeked != null && !peeked.isEof() && isLeftParenthesis(peeked)) {
                    // �������� ������� ��������. ��� ������, ���
                    // ������� ������ ����� ����� ������ ���������, ���� ���
                    // �� ����� ��������� ����������. � ������ �������, ������
                    // ��������� ������� ���� ������������ ��������, ���� ��� ������
                    // � ������ ���������.
                    IToken fictLeftParen = new RoundParenthesis(RoundParenthesis.LEFT);
                    dispatch(fictLeftParen);
                    token = createFunctionOperator((String) symbol.value); //?error when null

                } else if (peeked != null && !peeked.isEof() && isDot(peeked)) {
                    // qualified name of kind tableName.columnName
                    String alias = (String) symbol.value;
                    scanner.hasNextSymbol();
                    scanner.nextSymbol(); //  DOT

                    if (!scanner.hasNextSymbol()) {
                        throw new ExpressionException(13, peeked.left, "Parser.parse() : '" + alias + ".' Invalid identifier for qualified name");
                    }
                    Symbol cnsym = scanner.nextSymbol(); //  columnName

                    if (cnsym.sym != LexConst.IDENTIFIER) {
                        throw new ExpressionException(14, peeked.left, "Parser.parse() : '" + alias + ".' Invalid identifier for qualified name");
                    }
                    token = createColumnOperand(((String) cnsym.value).toUpperCase(), alias.toUpperCase());
                } else {
                    token = createColumnOperand(((String) symbol.value).toUpperCase(), null);
                }
            } else if (isParameter(symbol)) {
                token = createParameterOperand(((String) symbol.value).toUpperCase());
            } else {
                //�� ���������� ���� ����������� �������. ������ ����������
                //������, ����� ������� ����� ������ ������ ����������, ��������
                // isDeleted(). ��� ������������ ����������� �����������  �����.
                // �������
                if (isLeftParenthesis(symbol) && lastToken instanceof FunctionOperator) {
                    Symbol peeked = scanner.peekNextSymbol();
                    if (peeked != null && !peeked.isEof() && isRightParenthesis(peeked)) {
                        Integer n = new Integer(0);
                        IOperand iop = this.createLiteralOperand(n);
                        ((LiteralOperand) iop).lexType = LexConst.INTEGER_LITERAL;
                        dispatch(iop);
                    } else if (peeked != null && !peeked.isEof() && ( peeked.sym == LexConst.MINUS || peeked.sym == LexConst.PLUS) ) {
                    //������� ���� + ��� - �������� �� �������� ��������
                        Double d = new Double(0);
                        IOperand op = this.createLiteralOperand(d);
                        ((LiteralOperand) op).lexType = LexConst.FLOATING_POINT_LITERAL;
                        dispatch(op);
                    }
                } /*else if (isUnarySign(symbol)) {
                    //������� ���� + ��� - �������� �� �������� ��������
                    Double d = new Double(0);
                    IOperand op = this.createLiteralOperand(d);
                    ((LiteralOperand) op).lexType = LexConst.FLOATING_POINT_LITERAL;
                    dispatch(op);
                }*/
                //��������, ������� ��� ������� ������
                token = createToken(symbol);
            }

            dispatch(token);

            if (!isParenthesis(symbol)) {
                lastSymbol = symbol;
                lastToken = token;
            }
        }//while

        freeRPNStack();
    }

    /**
     * ������� ����� ������ ���� {@link FunctionOperator}, ��� �������
     * ��������������� ��������� �����.
     * @param name �� �������������� � �������� ��������� �������� �����
     *      ����������� �������.
     * @return ��������� ���� {@link FunctionOperator}.
     */
    protected IOperator createFunctionOperator(String name) {
        IOperator o = new FunctionOperator(name);
        o.setContext(context);
        return o;
    }

    /**
     * ���������� ��������, �������� �� ���������� �� ��������
     * ������ ���� {@link tdo.tools.logexpr#Symbol}, ��� ��������,
     * ����� <code>Symbol.getType() == LexConst.MINUS</code> ���
     * <code>Symbol.getType() == LexConst.PLUS</code> ������� ����������
     * ��� ���.
     * @param symbol ����������� ������.
     * @return true ���� ����������� ������ �������� ������� ����������.
     *         false - � ��������� ������.
     */
    protected boolean isUnarySign(Symbol symbol) {
        boolean r = false;
        //lastToken always contains previously treated token not left or right parenth
        if (symbol.sym == LexConst.MINUS || symbol.sym == LexConst.PLUS) {
            r = lastSymbol == null ? true : isOperator(lastSymbol);
        }
        return r;
    }

    /**
     * ������� ������� ���� {@link IdentifierOperand} ��� ��������� ����� �������
     * �, ��������, ��������� �� null �������� ������.
     * ����� ������� � ��������� ����� �������������� �������� ���. ��������,
     * <code><pre>a.Col2</pre></code>, ��� <i>a</i> - �����, � <i>Col2</i> -
     * ��� �������.
     * @param name ��� ������� ������� ���� {@link tdo#Table}
     * @param alias
     * @return �������� ���� {@link IdentifierOperand} ��� �������� ����� � ������.
     */
    protected IOperand createColumnOperand(String name, String alias) {
        IOperand o = new IdentifierOperand(name, alias);
        o.setContext(context);
        return o;
    }

    protected IOperand createParameterOperand(String name) {
        String nm = name.substring(1);
        IOperand o = new ParameterOperand(nm.toUpperCase());
        o.setContext(context);
        return o;
    }

    /**
     * ������� ������ ���������� ����, � ������������ � ��� �������� ��
     * �������� ��������� ����������, ��������� ��� ����� �� �������
     * ������.
     *
     * @param symbol ������ ���� {@link tdo.tools.logexpr#Symbol}, ����������
     *       �� ��������.
     * @return ����� ������, ������� ��������� ������ ��������� ���������
     *       {@link IToken}. ��������� ������ {@link #isOperator},
     *       {@link #isOperand}, {@link #isParenthesis} ������� ��������� token
     *        � ���������� ���.
     */
    protected IToken createToken(Symbol symbol) {
        IToken t = null;
        if (isOperator(symbol)) {
            t = createOperator(symbol);
        } else if (isOperand(symbol)) {
            t = createOperand(symbol);
        }
        if (isParenthesis(symbol)) {
            t = this.createParenthesis(symbol);
        }
        return t;
    }

    /**
     * ������� ������ ���� {@link RoundParenthesis} ��������� �������� ���
     * ����������� �������� {@link RoundParenthesis#kind}.
     * ������ ��������������� ����� � ������ ������ ����������� �� ��������,
     * ������������� ������� {@link RoundParenthesis#getKind}.
     *
     * @param symbol ������ ���� {@link tdo.tools.logexpr#Symbol}, ����������
     *       �� ��������.
     * @return ������ ���� {@link RoundParenthesis}, ��������������� ������
     *          ��� ����� ������� ������.
     *
     */
    protected IToken createParenthesis(Symbol symbol) {
        IToken t;//My 06.03.2012 = null;
        if (symbol.sym == LexConst.LPAREN) {
            t = new RoundParenthesis(RoundParenthesis.LEFT);
        } else {
            t = new RoundParenthesis(RoundParenthesis.RIGHT);
        }
        return t;
    }

    /**
     * ������� � ���������� ������ ���� {@link IOperand}, ���������������
     * ��������� �������� ���������.
     * ����� �� ������������ ��� �������� ���������, ����������� ������ �������,
     * ��������� ����� ������� ��������� ��������� �������
     * {@link #createColumnOperand}.
     * ����� ��������� ��������� ���� ���������:
     * <UL>
     *   <LI><code>LexConst.BOOLEAN_LITERAL</code>. ��� ���� ���������
     *        ������ ���� {@link LiteralOperand}, ������ ���������� ��������
     *        �������� �������� <code>Boolean.class</code>, � ������
     *        <code>symbol.value</code>.
     *   </LI>
     *   <LI><code>LexConst.FLOATING_POINT_LITERAL</code>. ��� ���� ���������
     *        ������ ���� {@link LiteralOperand}, ������ ���������� ��������
     *        �������� �������� <code>Double.class</code>, � ������
     *        <code>symbol.value</code>, ���������������, ���� ��������� � ���
     *        <code>double</code>.
     *   </LI>
     *   <LI><code>LexConst.INTEGER_LITERAL</code>. ��� ���� ���������
     *        ������ ���� {@link LiteralOperand}, ������ ���������� ��������
     *        �������� �������� <code>Integer.class</code> ���
     *        <code>Long.class</code>, ������������ �� ����
     *        <code>symbol.value</code>, � ������ -
     *        <code>symbol.value</code>.
     *   </LI>
     *   <LI><code>LexConst.STRING_LITERAL</code>. ��� ���� ���������
     *        ������ ���� {@link LiteralOperand}, ������ ���������� ��������
     *        �������� �������� <code>String.class</code>, � ������
     *        <code>symbol.value</code>.
     *   </LI>
     *   <LI><code>LexConst.NULL_LITERAL</code>. ��� ���� ���������
     *        ������ ���� {@link LiteralOperand}, ������ ���������� ��������
     *        �������� �������� <code>Object.class</code>, � ������
     *        <code>null</code>.
     *   </LI>
     * </UL>
     *
     * @param symbol ������ ���� {@link tdo.tools.logexpr#Symbol}, ����������
     *       �� ��������.
     * @return
     */
    protected IOperand createOperand(Symbol symbol) {
        IOperand token = null;
        int sym = symbol.sym;
        switch (symbol.sym) {
            case LexConst.BOOLEAN_LITERAL:
                token = new LiteralOperand(Boolean.class, symbol.value);
                break;
            case LexConst.FLOATING_POINT_LITERAL:
                Double d;
                if (symbol.value instanceof Float) {
                    d = new Double(((Float) symbol.value).doubleValue());
                } else {
                    d = new Double((Double) symbol.value);
                }
                token = new LiteralOperand(Double.class, d);
                break;
            case LexConst.INTEGER_LITERAL:

                if (symbol.value instanceof Integer) {
                    Integer i = new Integer(((Integer) symbol.value).intValue());
                    token = new LiteralOperand(Integer.class, i);
                } else {
                    Long i = new Long(((Long) symbol.value).intValue());
                    token = new LiteralOperand(Integer.class, i);
                }
                break;
            case LexConst.STRING_LITERAL:
                token = new LiteralOperand(String.class, symbol.value);
                break;
            case LexConst.NULL_LITERAL:
                token = new LiteralOperand(Object.class, null);
                break;
        } //switch

        token.setContext(context);
        return token;
    }

    /**
     * ������� ��������� ������ {@link LiteralOperand}, ��������� ��� � ��������
     * ��������� <code>value</code>.
     * @param value ��������, ������������ ��� ���������� �������
     * @return ����� ������ ���� {@link LiteralOperand}
     */
    protected IOperand createLiteralOperand(Object value) {
        IOperand o = new LiteralOperand(value);
        o.setContext(context);
        return o;
    }

    /**
     * ������� ����� ������ ������, ������������ ��������� {@link IOperator}
     * � ������������ �� �������� �������� <code>symbol.sym</code>.
     * ����� ������� ������ ������ �� �������:
     * <UL>
     *      <LI>{@link AndBetweenOperator} ��������, ��������������� ���������
     *              ����� <code>AND</code> ���������� ���������
     *              <code>BETWEEN</code>.
     *      </LI>
     *      <LI>{@link AndOperator} ��������, ��������������� �����������
     *              ���������� <code>AND</code>.
     *      </LI>
     *      <LI>{@link OrOperator} ��������, ��������������� �����������
     *              ��������� <code>AND</code>.
     *      </LI>
     *      <LI>{@link NotOperator} ��������, ��������������� �����������
     *              ��������� <code>NOTD</code>.
     *      </LI>
     *      <LI>{@link LikeOperator} ��������, ���������������
     *              ��������� ��������� <code>LIKE</code>.
     *      </LI>
     *      <LI>{@link NotLikeOperator} ��������, ��������������� ���������
     *              ��������� <code>NOT LIKE</code>.
     *      </LI>
     *      <LI>{@link RegExOperator} ��������, ��������������� ���������
     *              ��������� <code>REGEX</code>.
     *      </LI>
     *      <LI>{@link NotRegExOperator} ��������, ��������������� ���������
     *              ��������� <code>NOT REGEX</code>.
     *      </LI>
     *      <LI>{@link CommaOperator} ��������, ��������������� ���������
     *              <code>, </code> - �������.
     *      </LI>
     *      <LI>{@link ContainingOperator} ��������, ��������������� ���������
     *              ��������� <code>CONTAINING</code>.
     *      </LI>
     *      <LI>{@link NotContainingOperator} ��������, ��������������� ���������
     *              ��������� <code>NOT CONTAINING</code>.
     *      </LI>
     *      <LI>{@link StartingWithOperator} ��������, ��������������� ���������
     *              ��������� <code>STARTING WITH</code>.
     *      </LI>
     *      <LI>{@link NotStartingWithOperator} ��������, ��������������� ���������
     *              ��������� <code>NOT STARTING WITH</code>.
     *      </LI>
     *      <LI>{@link InOperator} ��������, ��������������� ���������
     *              ��������� <code>IN</code>.
     *      </LI>
     *      <LI>{@link NotInOperator} ��������, ��������������� ���������
     *              ��������� <code>NOT IN</code>.
     *      </LI>
     *      <LI>{@link BetweenOperator} ��������, ��������������� ���������
     *              ��������� <code>BETWEEN</code>.
     *      </LI>
     *      <LI>{@link NotBetweenOperator} ��������, ��������������� ���������
     *              ��������� <code>NOT BETWEEN</code>.
     *      </LI>
     *      <LI>{@link IsNullOperator} ��������, ��������������� ���������
     *              ��������� <code>IS NULL</code>.
     *      </LI>
     *      <LI>{@link IsNotNullOperator} ��������, ��������������� ���������
     *              ��������� <code>IS NOT NULL</code>.
     *      </LI>
     *      <LI>{@link GreaterOperator} ��������, ��������������� ���������
     *              ��������� <code><pre>></pre></code>.
     *      </LI>
     *      <LI>{@link GreaterEqualsOperator} ��������, ��������������� ���������
     *              ��������� <code><pre>>=</pre></code>.
     *      </LI>
     *      <LI>{@link EqualsOperator} ��������, ��������������� ���������
     *              ��������� <code><pre>=</pre></code>.
     *      </LI>
     *      <LI>{@link LessOperator} ��������, ��������������� ���������
     *              ��������� <code><pre><</pre></code>.
     *      </LI>
     *      <LI>{@link LessEqualsOperator} ��������, ��������������� ���������
     *              ��������� <code><pre><=</pre></code>.
     *      </LI>
     *      <LI>{@link NotEqualsOperator} ��������, ��������������� ���������
     *              ��������� <code><pre><></pre></code>.
     *      </LI>
     *      <LI>{@link AddOperator} ��������, ��������������� ���������������
     *              ��������� <code><pre>+</pre></code>.
     *      </LI>
     *      <LI>{@link SubtrOperator} ��������, ��������������� ���������������
     *              ��������� <code><pre>-</pre></code>.
     *      </LI>
     *      <LI>{@link MultOperator} ��������, ��������������� ���������������
     *              ��������� <code><pre>*</pre></code>.
     *      </LI>
     *      <LI>{@link DivOperator} ��������, ��������������� ���������������
     *              ��������� <code><pre>/</pre></code>.
     *      </LI>
     *      <LI>{@link ConcatOperator} ��������, ���������������
     *              ��������� ������������ ����� <code><pre>||</pre></code>.
     *      </LI>
     * </UL>
     * @param symbol
     * @return ������� ����������� ��������� <code>IOperator</code> �
     *  ������������ �� ��������� ���������.
     */
    protected IOperator createOperator(Symbol symbol) {
        IOperator token = null;
        switch (symbol.sym) {
            case LexConst.AND:
                if (state == LexConst.BETWEEN) {
                    token = new AndBetweenOperator();
                    state = -1;
                } else if (state == LexConst.NOTBETWEEN) {
                    //token = new AndBetweenOperator();
                    state = -1;
                } else {
                    token = new AndOperator();
                }
                break;
            case LexConst.OR:
                token = new OrOperator();
                break;
            case LexConst.NOT:
                token = new NotOperator();
                break;
            case LexConst.LIKE:
                token = new LikeOperator();
                break;
            case LexConst.NOTLIKE:
                token = new NotLikeOperator();
                break;
            case LexConst.REGEX:
                token = new RegExOperator();
                break;
            case LexConst.NOTREGEX:
                token = new NotRegExOperator();
                break;
            case LexConst.COMMA:
                token = new CommaOperator();
                break;
            case LexConst.IN:
                token = new InOperator();
                break;
            case LexConst.NOTIN:
                token = new NotInOperator();
                break;
            case LexConst.CONTAINING:
                token = new ContainingOperator();
                break;
            case LexConst.NOTCONTAINING:
                token = new NotContainingOperator();
                break;
            case LexConst.STARTINGWITH:
                token = new StartingWithOperator();
                break;
            case LexConst.NOTSTARTINGWITH:
                token = new NotStartingWithOperator();
                break;
            case LexConst.BETWEEN:
                token = new BetweenOperator();
                state = LexConst.BETWEEN;
                break;
            case LexConst.ISNULL:
                token = new IsNullOperator();
                break;
            case LexConst.ISNOTNULL:
                token = new IsNotNullOperator();
                break;
            case LexConst.GT:
                token = new GreaterOperator();
                break;
            case LexConst.LT:
                token = new LessOperator();
                break;
            case LexConst.EQ:
                token = new EqualsOperator();
                break;
            case LexConst.NOTEQ:
                token = new NotEqualsOperator();
                break;
            case LexConst.GTEQ:
                token = new GreaterEqualsOperator();
                break;
            case LexConst.LTEQ:
                token = new LessEqualsOperator();
                break;
            case LexConst.MINUS:
                token = new SubtrOperator();
                break;
            case LexConst.PLUS:
                token = new AddOperator();
                break;
            case LexConst.DIV:
                token = new DivOperator();
                break;
            case LexConst.MULT:
                token = new MultOperator();
                break;
            case LexConst.CONCAT:
                token = new ConcatOperator();
                break;
            case LexConst.AS:
                token = new AsOperator();
                break;
        } //switch

        token.setContext(context);
        return token;
    }

    /**
     * ��������� ��������� ������� ���� {@link IToken} � �������� ��� �
     * {@link #rpnStack} ��� � {@link #rpn}.
     * ������ <code>IToken</code> - ��� �������, �������� ��� ������� ������
     * (����������� ��� �����������).
     * <pre>
     *   ���� token ���� ����� ������� ������ ��
     *          token ���������� � ������� rpnStack (push).
     *   �����-����
     *
     *   ���� token ���� ������ ������� ������ ��
     *        ���� rpnStack ���� ��
     *           ������������� ����������
     *        �����
     *           ����-���� rpnStack �� ����
     *              ���������� ������� �� ������� rpnStack (pop)
     *              ���� ���� ������� ����� ������, ��
     *                  ���� ����������� (break)
     *              �����
     *                  ��������� ������� � rpn
     *           �����-�����
     *           ���� � ����� ���� �� ������� ����� ������ ��
     *               ������������� ����������
     *           �����-����
     *           ���� rpnStack �� ���� ��
     *              �� �������� pop ������������� ������� � ������� rpnStack (peek)
     *              ���� ��� FunctionOperator ��
     *                    ��������� �� � ������ parse ������������ ��������
     *                    ����. ������ ����� ���������� �������, �� ������ ��
     *                    ������ ����������� ������� ����. ������ ��� ����:
     *                       ��������� ������� ���� FunctionOperator (pop)
     *                       ��������� ��� � rpn
     *                       ��������� pop ��� ����� ������� ������
     *              �����-����
     *           �����-����
     *
     *           ��������� �����
     *      �����
     *           ���� token - ��� ������� ��
     *              ��������� � rpn
     *           �����-����
     *           ���� token - ��� �������� ��
     *              ���� rpnStack ����  ��� ��������� token ������ ������
     *                                  �������� � ������� rpnStack
     *              ��
     *                  ��������� token � ������� rpnStack (push)
     *              �����
     *                  ����-���� rpnStack �� ����
     *                      ���� ��������� token ������ ������ �������� �
     *                          ������� rpnStack
     *                      TO
     *                          ��������� ���� (break)
     *                      �����
     *                          �������� ������� �� ������� rpnStack
     *                          ��������� ��� � rpn
     *                      �����-����
     *                  �����-�����
     *
     *                  ��������� token � ������� rpnStack
     *
     *           �����-����
     *
     *   �����-����
     * </pre>
     *
     * @param token
     */
    protected void dispatch(IToken token) {
        IToken t;//My 06.03.2012 = null; // current

        if (token instanceof RoundParenthesis) {
            if (((RoundParenthesis) token).getKind() == RoundParenthesis.LEFT) {
                this.rpnStack.push(token);
                return;
            }

            if (rpnStack.empty()) {
                throw new ExpressionException(11, -1, "ExprParser.dispatch(IToken) : Parenthesis doesn't match");
            }
            boolean found = false;
            while (!rpnStack.empty()) {
                t = rpnStack.pop();
                if (t instanceof RoundParenthesis && ((RoundParenthesis) t).getKind() == RoundParenthesis.LEFT) {
                    found = true;
                    break;
                }

                rpn.addElement(t);
            } //while

            if (!found) {
                throw new ExpressionException(12, -1, "ExprParser.dispatch(IToken) : The Left parenthesis cannot be found");
            }
            if (!rpnStack.empty()) {
                IToken t1 = rpnStack.peek();
                if (t1 instanceof FunctionOperator) {
                    // ��������� �� � ������ parse ������������ ��������
                    // ����. ������ ����� ���������� �������, �� ������ ��
                    // ������ ����������� ������� ����. ������
                    rpnStack.pop(); // function

                    rpn.addElement(t1);
                    rpnStack.pop(); // left parenth

                }
            }

            return;
        }

        if (token instanceof IOperand) {
            rpn.addElement(token);
            return;
        }

        if (token instanceof IOperator) {
            IOperator oper = (IOperator) token;
            if (rpnStack.empty() || oper.getPriority() > rpnStack.peek().getPriority()) {
                rpnStack.push(token);
            } else {
                while (!rpnStack.empty()) {
                    if (token.getPriority() > rpnStack.peek().getPriority()) {
                        break;
                    }
                    IToken tk = rpnStack.pop();
                    rpn.addElement(tk);
                } //while

                rpnStack.push(token);
            }
        }
    }

    /**
     * ��������� ���������� �������� �� ����� <code>rpnStack</code> �
     * rpn-���������.
     * ���� ����� ��������� ����� {@link #parse}
     */
    protected void freeRPNStack() {
        while (!this.rpnStack.empty()) {
            rpn.addElement(rpnStack.pop());
        }
    }

    /**
     * ���������� ������ ���� {@link java.util#Vector}, ���������� ��������� � ����������� �����.
     * ���������� ������� �������� ������� ������ �� �����:
     * <UL>
     *    <LI>{@link IOperand} - ��������� ������������� �������� ���������; </LI>
     *    <LI>{@link IOperator} - ��������� ������������� ��������� ���������. </LI>
     * </UL>
     *
     * @return ������, ���������� ��������� �������� ���������.
     */
    public Vector getRPN() {
        return rpn;
    }

    /**
     * ����������� ������� �� ��������� ������� ���������� ���� ���������� {@link #rpn}.
     * ����� ���� �������� ��� ������� � ������������.
     */
    public void printRPN() {
        for (int i = 0; i < rpn.size(); i++) {
            System.out.println(i + "). " + rpn.get(i).toString());
        }
    }

    /**
     * ����������� ������� �� ��������� ������� ���������� ���� ���������� {@link #rpn}.
     * ����� ���� �������� ��� ������� � ������������.
     */
    public void printRPN(boolean withType) {
        if ( ! withType ) {
            printRPN();
            return;
        }
        for (int i = 0; i < rpn.size(); i++) {
            IToken  t = (IToken)rpn.get(i);
            String s = t.getClass().getName();
            int l = s.lastIndexOf('$');
            if ( l >=0 )
                l++;
            else
                l = s.lastIndexOf('.')+1;
            s = s.substring(l);
            System.out.println(i + "). " + "[" + s + " : "
                    +rpn.get(i).toString() + "]");
        }
    }

    /*    public String toExprString(IToken token) {
    return " not yet implemented ";
    }
    public String toString(IToken token) {
    return " not yet implemented ";
    }
    public String fromRPN() {
    String str = "";
    Vector r = new Vector(20);
    Stack s  = new Stack();
    return str;
    }
     */
    ///////////////////////////////////////////
    /**
     * ���������, �������� �� ������ <code>symbol<code> ����������� ���������.
     * ���� ������� ����� �������� �����������, �� ������ ����� �� �� ����������. ��� �������
     * � ����������� ���������� �������, ������� ��� �� ������������� ������������ ������
     * ��������.
     *
     * @param symbol �������, ��������������� ���������.
     * @return true ���� �������� <code>symbol.sym</code> �������� ���� �� ��������:
     *   <code><pre>
     *       LexConst.COMMA, LexConst.EQ, LexConst.GT, LexConst.LT,
     *       LexConst.LTEQ, LexConst.GTEQ, LexConst.NOTEQ, LexConst.AND,
     *       LexConst.OR,LexConst.NOT, LexConst.PLUS,LexConst.MINUS,
     *       LexConst.MULT,LexConst.DIV, LexConst.CONCAT,LexConst.ISNULL,
     *       LexConst.ISNOTNULL,LexConst.BETWEEN, LexConst.NOTBETWEEN,LexConst.IN,
     *       LexConst.NOTIN,LexConst.CONTAINING, LexConst.NOTCONTAINING,
     *       LexConst.STARTINGWITH, LexConst.NOTSTARTINGWITH,
     *       LexConst.LIKE,LexConst.NOTLIKE, LexConst.REGEX,LexConst.NOTREGEX,
     *       LexConst.ANDBETWEEN
     *  </pre></code>
     *      false - � ��������� ������.
     */
    protected boolean isOperator(Symbol symbol) {
        int sym = symbol.sym;
        if (sym == LexConst.COMMA || sym == LexConst.EQ || sym == LexConst.GT || sym == LexConst.LT || sym == LexConst.LTEQ || sym == LexConst.GTEQ || sym == LexConst.NOTEQ || sym == LexConst.AND || sym == LexConst.OR || sym == LexConst.NOT || sym == LexConst.PLUS || sym == LexConst.MINUS || sym == LexConst.MULT || sym == LexConst.DIV || sym == LexConst.CONCAT || sym == LexConst.ISNULL || sym == LexConst.ISNOTNULL || sym == LexConst.BETWEEN || sym == LexConst.NOTBETWEEN || sym == LexConst.IN || sym == LexConst.NOTIN || sym == LexConst.CONTAINING || sym == LexConst.NOTCONTAINING || sym == LexConst.STARTINGWITH || sym == LexConst.NOTSTARTINGWITH || sym == LexConst.LIKE || sym == LexConst.NOTLIKE || sym == LexConst.REGEX || sym == LexConst.NOTREGEX || sym == LexConst.ANDBETWEEN || sym == LexConst.AS) {
            return true;
        } else {
            return false;
        }
    }
    protected boolean isFunctionOperator(Symbol symbol) {
        return symbol.sym == LexConst.FUNCTION ;
    }
    /**
     * ���������, �������� �� ������ <code>symbol<code> ���������� ���������.
     *
     * @param symbol �������, ��������������� ���������.
     * @return true ���� �������� <code>symbol.sym</code> �������� ���� �� ��������:
     *   <code><pre>
     *       LexConst.BOOLEAN_LITERAL,LexConst.INTEGER_LITERAL,
     *        LexConst.NULL_LITERAL,  LexConst.FLOATING_POINT_LITERAL,
     *       LexConst.STRING_LITERAL, LexConst.IDENTIFIER )
     *   </pre></code>
     *      false - � ��������� ������.
     */
    protected boolean isOperand(Symbol symbol) {
        int sym = symbol.sym;
        if (sym == LexConst.BOOLEAN_LITERAL || sym == LexConst.INTEGER_LITERAL || sym == LexConst.NULL_LITERAL || sym == LexConst.FLOATING_POINT_LITERAL || sym == LexConst.STRING_LITERAL ||
                sym == LexConst.IDENTIFIER || sym == LexConst.PARAMETER) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ���������, �������� �� ������ <code>symbol<code> ����������-��������������� ���������.
     *
     * @param symbol �������, ��������������� ���������.
     * @return true ���� �������� <code>symbol.sym</code> �������� ��������
     *   <code> LexConst.IDENTIFIER
     *   </pre></code>
     *      false - � ��������� ������.
     */
    protected boolean isIdentifier(Symbol symbol) {
        int sym = symbol.sym;
        if (sym == LexConst.IDENTIFIER) {
            return true;
        } else {
            return false;
        }
    }

    protected boolean isParameter(Symbol symbol) {
        int sym = symbol.sym;
        if (sym == LexConst.PARAMETER) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ���������, �������� �� ������ <code>symbol<code> ����� �� ������� ������.
     *
     * @param symbol �������, ��������������� ���������.
     * @return true ���� �������� <code>symbol.sym</code> �������� ���� �� ��������:
     *   <code><pre> LexConst.LPAREN, LexConst.LPAREN </pre></code>
     *      false - � ��������� ������.
     */
    protected boolean isParenthesis(Symbol symbol) {
        int sym = symbol.sym;
        if (sym == LexConst.LPAREN || sym == LexConst.RPAREN) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ���������, �������� �� ������ <code>symbol<code> ����� ������� �������.
     *
     * @param symbol �������, ��������������� ���������.
     * @return true ���� �������� <code>symbol.sym</code> �������� ��������:
     *   <code><pre> LexConst.LPAREN </pre></code>
     *      false - � ��������� ������.
     */
    protected boolean isLeftParenthesis(Symbol symbol) {
        int sym = symbol.sym;
        if (sym == LexConst.LPAREN) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ���������, �������� �� ������ <code>symbol<code> ����� ������� �������.
     *
     * @param symbol �������, ��������������� ���������.
     * @return true ���� �������� <code>symbol.sym</code> �������� ��������:
     *   <code><pre> LexConst.RPAREN </pre></code>
     *      false - � ��������� ������.
     */
    protected boolean isRightParenthesis(Symbol symbol) {
        int sym = symbol.sym;
        if (sym == LexConst.RPAREN) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ���������, �������� �� ������ <code>symbol<code> ������.
     * ������� "�����" ������������ ��� ������������� ����� ������� � ���� ��������� �
     * ��������� �����.
     *
     * @param symbol �������, ��������������� ���������.
     * @return true ���� �������� <code>symbol.sym</code> �������� ��������:
     *   <code><pre> LexConst.DOT </pre></code>
     *      false - � ��������� ������.
     */
    protected boolean isDot(Symbol symbol) {
        int sym = symbol.sym;
        if (sym == LexConst.DOT) {
            return true;
        } else {
            return false;
        }
    }
} //class ExprParser
