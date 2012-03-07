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
 * Выполняет обработку ( парсинг ) логических,
 * арифметических и строковых выражений, представленных в виде строки знаков.
 * Выражение в инфиксном виде является входной информацией, которая на выходе
 * преобразуется в постфиксный формат, а именно в формат <i>Обратной Польской
 * Записи</i> (RPN). <br>
 *
 * Ниже приведен пример выражения:
 *      <code><PRE>col1=5 and col2 > 7 and ( col3 LIKE '%ab' OR
 *                 col4 CONTAINING '56' )
 *      </PRE></CODE>
 * Следующий код демонстрируе использование этого выражения в Java-программе:
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
 * Допустимыми операндами выражения являются:
 * <UL>
 *   <LI>
 *      Идентификаторы, представляющими собой имена колонок так как они
 *      определены в классе {@link tdo.common.data.DataColumn}
 *   </LI>
 *   <LI>
 *      Строковые литералы, заключенные в одинарные кавычки, например,
 *      <code><PRE>'Mr. Smith'</PRE></CODE>
 *   </LI>
 *   <LI>
 *      Целочисленные литералы, например, <code><PRE>125</PRE></CODE>,
 *      которые соответствуют данным типа <code>int</code>
 *   </LI>
 *   <LI>
 *      Целочисленные литералы, например, <code><PRE>125L</PRE></CODE>,
 *      или <code><PRE>125l</PRE></CODE>
 *      которые соответствуют данным типа <code>long</code>
 *   </LI>
 *   <LI>
 *      Литералы с плавающей точкой, например, <code><PRE>125.7F</PRE></CODE>,
 *      или <code><PRE>125.7f</PRE></CODE> или <code><PRE>1257E-1F</PRE></CODE>
 *      которые соответствуют данным типа <code>float</code>
 *   </LI>
 *   <LI>
 *      Литералы с плавающей точкой, например, <code><PRE>125.7</PRE></CODE> или
 *      <code><PRE>1257E-1</PRE></CODE>
 *      которые соответствуют данным типа <code>double</code>
 *   </LI>
 *   <LI>
 *      Булевы литералы, принимающие значения <code>true</code> или
 *      <code>false</code>.
 *   </LI>
 *   <LI>
 *      Литерал <code>null</code>
 *   </LI>
 * </UL>
 *
 * Допустимыми арифметическими операторами выражения являются:
 * <UL>
 *   <LI>
 *      Сложение (+), например <code><PRE>col + 7</PRE></CODE>
 *   </LI>
 *   <LI>
 *      Вычитание (-), например <code><PRE>col - 7</PRE></CODE>
 *   </LI>
 *   <LI>
 *      Умножение (*), например <code><PRE>col * 7</PRE></CODE>
 *   </LI>
 *   <LI>
 *      Деление (/), например <code><PRE>col / 7</PRE></CODE>
 *   </LI>
 * </UL>
 *
 * Допустимым строковым оператором выражения является:
 * <UL>
 *   <LI>
 *      Сцепление (конкатенация) (||), например, 'ab' || 'cd'
 *   </LI>
 * </UL>
 *
 * Допустимыми  операторами сравнения в выражении являются:
 * <UL>
 *   <LI>
 *      Больше <code><PRE>(>)</PRE></code>
 *   </LI>
 *   <LI>
 *      Меньше <code><PRE>(<)</PRE></code>
 *   </LI>
 *   <LI>
 *      Равно <code><PRE>(=)</PRE></code>
 *   </LI>
 *   <LI>
 *      Не равно <code><PRE>(<>)</PRE></code>
 *   </LI>
 *   <LI>
 *      Больше или равно <code><PRE>(>=)</PRE></code>
 *   </LI>
 *   <LI>
 *      Меньше или равно<code><PRE>(<=)</PRE></code>
 *   </LI>
 *   <LI>
 *      Проверка на равенство значению <code>null</code> <code><PRE>(is null)</PRE></code>,
 *      например: <code><PRE>(Col2 is null)</PRE></code>
 *   </LI>
 *   <LI>
 *      Проверка на не равенство значению <code>null</code>
 *      <code><PRE>(is not null)</PRE></code>,
 *      например: <code><PRE>(Col2 is null)</PRE></code>
 *   </LI>
 *   <LI>
 *      Проверка на принадлежность значения заданному интервалу
 *      <code><PRE>(between)</PRE></code>,
 *      например: <code><PRE>(Col2 between 36 and 58)</PRE></code>
 *   </LI>
 *   <LI>
 *      Проверка на равенство значения одному из элементов списка
 *      <code><PRE>(in)</PRE></code>,
 *      например: <code><PRE>(Col2 in ( 1,2,36,58)</PRE></code>
 *   </LI>
 *   <LI>
 *      Проверка на равенство начальной подстроки строкового значения
 *      заданной строке
 *      <code><PRE>(starting with)</PRE></code>,
 *      например: <code><PRE>(Col2 starting with 'Mr.'</PRE></code>
 *   </LI>
 *   <LI>
 *      Проверка, содержит ли строка заданную подстроку
 *      <code><PRE>(containing)</PRE></code>,
 *      например: <code><PRE>(Col2 containing 'Mr.'</PRE></code>
 *   </LI>
 *   <LI>
 *      Проверка на соответствие wildcard-шаблону
 *      <code><PRE>(like)</PRE></code>,
 *      например: <code><PRE>(Col2 like '%Mr%'</PRE></code>
 *   </LI>
 *   <LI>
 *      Проверка на соответствие регулярному выражению
 *      <code><PRE>(regex)</PRE></code>,
 *      например: <code><PRE>(Col2 regex '%Mr%'</PRE></code>
 *   </LI>
 * </UL>
 * Допустимыми  логическими операторами  в выражении являются:
 * <UL>
 *   <LI>
 *      AND - логическое "И"
 *   </LI>
 *   <LI>
 *      OR - логическое "ИЛИ"
 *   </LI>
 *   <LI>
 *      NOT - логическое отрицание
 *   </LI>
 * </UL>
 */
public class ExprParser {

    private Symbol lastSymbol;
    /**
     * Ссылка на lex-сканер
     */
    protected BufferedScanner scanner;
    /**
     */
    protected ExpressionContext context;
    /**
     * Строковое исходное выражение в инфиксной форме.
     */
    protected String expression;
    /**
     * Вектор, являющияся результатом парсинга исходного выражение. Содержит
     * элементы RPN представления выражения. Каждый элемент - это объект,
     * реализующий интерфейс {@link IToken}.
     */
    protected Vector rpn;
    /**
     * Предназначен для использования внутри класса при построении
     * RPN- выражения.
     */
    protected RpnStack rpnStack;
    /**
     * Используется при обработке оператора <code>BETWEEN</code>.
     */
    protected int state;

    /**
     * Создает экземпляр класса для заданного строкового значения,
     * содержащего исходное выражение.
     * Инициализирует поля {@link #rpn} ,  {@link #rpnStack},{@link #scanner} и
     * {@link #state}
     * @param context
     * @param expr исходное выражение в инфиксной форме.
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
     * Сбрасывает состояние полей класса, позволяя повторно
     * использовать экземпляр класса  без создания нового экземпляра.
     */
    public void reset() {
        rpn = new Vector(20);
        rpnStack = new RpnStack();
        scanner.reset();
        state = -1;
    }

    /**
     * Обрабатывает исходное выражение, представленное строкой знаков и
     * создает объект типа {@link java.util.Vector}, содержащий
     * постфиксную форму выражения как RPN-запись, доступную пользовательскому
     * приложению через вызов метода {@link #getRPN}.
     *
     */
    public void parse() {
        IToken lastToken = null;
        while (scanner.hasNextSymbol()) {
            IToken token = null;
            Symbol symbol = scanner.nextSymbol();
            if (isUnarySign(symbol)) {
                //Унарный знак + или - заменяем на бинарную операцию
                Double d = new Double(0);
                IOperand op = this.createLiteralOperand(d);
                ((LiteralOperand) op).lexType = LexConst.FLOATING_POINT_LITERAL;
                dispatch(op);
                token = createToken(symbol);
            } else if (isIdentifier(symbol)) {
                //Это может быть имя колонки, имя колонки с предшествующим
                //алиасом или имя функции
                Symbol peeked = scanner.peekNextSymbol();

                if (peeked != null && !peeked.isEof() && isLeftParenthesis(peeked)) {
                    // Окружаем функцию скобками. Это потому, что
                    // функция должна иметь самый низкий приормтет, пока она
                    // не будет полностью обработана. С другой стороны, скобки
                    // позволяют функции быть обработанной отдельно, если она входит
                    // в состав выражения.
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
                //На предыдущем шаге обработалти функцию. теперь обработаем
                //случай, когда функция имеет пустой список параметров, например
                // isDeleted(). Для такогослучая подставляем искуственно  целоч.
                // литерал
                if (isLeftParenthesis(symbol) && lastToken instanceof FunctionOperator) {
                    Symbol peeked = scanner.peekNextSymbol();
                    if (peeked != null && !peeked.isEof() && isRightParenthesis(peeked)) {
                        Integer n = new Integer(0);
                        IOperand iop = this.createLiteralOperand(n);
                        ((LiteralOperand) iop).lexType = LexConst.INTEGER_LITERAL;
                        dispatch(iop);
                    } else if (peeked != null && !peeked.isEof() && ( peeked.sym == LexConst.MINUS || peeked.sym == LexConst.PLUS) ) {
                    //Унарный знак + или - заменяем на бинарную операцию
                        Double d = new Double(0);
                        IOperand op = this.createLiteralOperand(d);
                        ((LiteralOperand) op).lexType = LexConst.FLOATING_POINT_LITERAL;
                        dispatch(op);
                    }
                } /*else if (isUnarySign(symbol)) {
                    //Унарный знак + или - заменяем на бинарную операцию
                    Double d = new Double(0);
                    IOperand op = this.createLiteralOperand(d);
                    ((LiteralOperand) op).lexType = LexConst.FLOATING_POINT_LITERAL;
                    dispatch(op);
                }*/
                //Оператор, операнд или круглая скобка
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
     * Создает новый объект типа {@link FunctionOperator}, для функции
     * соответствующий заданному имени.
     * @param name не чувствительное к регистру строковое значение имени
     *      создаваемой функции.
     * @return экземпляр типа {@link FunctionOperator}.
     */
    protected IOperator createFunctionOperator(String name) {
        IOperator o = new FunctionOperator(name);
        o.setContext(context);
        return o;
    }

    /**
     * Производит проверку, является ли полученный от сканнера
     * объект типа {@link tdo.tools.logexpr#Symbol}, для которого,
     * метод <code>Symbol.getType() == LexConst.MINUS</code> или
     * <code>Symbol.getType() == LexConst.PLUS</code> унарной опрерацией
     * или нет.
     * @param symbol проверяемый объект.
     * @return true если проверяемый объект является унарным оператором.
     *         false - в противном случае.
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
     * Создает операнд типа {@link IdentifierOperand} для заданного имени колонки
     * и, возможно, отличного от null значения алиаса.
     * Имени колонки в выражении может предшествовать алиасное имя. Например,
     * <code><pre>a.Col2</pre></code>, где <i>a</i> - алиас, а <i>Col2</i> -
     * имя колонки.
     * @param name имя колонки таблицы типа {@link tdo#Table}
     * @param alias
     * @return значение типа {@link IdentifierOperand} для заданных имени и алиаса.
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
     * Создает объект требуемого типа, в соответствии с тем является ли
     * значение параметра оператором, операндом или одной из круглых
     * скобок.
     *
     * @param symbol объект типа {@link tdo.tools.logexpr#Symbol}, полученный
     *       от сканнера.
     * @return Любой объект, которым оперирует парсер реализует интерфейс
     *       {@link IToken}. Используя методы {@link #isOperator},
     *       {@link #isOperand}, {@link #isParenthesis} создает требуемый token
     *        и возвращает его.
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
     * Создает объект типа {@link RoundParenthesis} используя параметр для
     * определения свойства {@link RoundParenthesis#kind}.
     * Объект соответствующий левой и правой скобке различается по значению,
     * возвращаемому методом {@link RoundParenthesis#getKind}.
     *
     * @param symbol объект типа {@link tdo.tools.logexpr#Symbol}, полученный
     *       от сканнера.
     * @return объект типа {@link RoundParenthesis}, соответствующий правой
     *          или левой круглой скобке.
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
     * Создает и возвращает объект типа {@link IOperand}, соответствующий
     * заданному значению параметра.
     * Метод не используется для создания опреранда, являющегося именем колонки,
     * поскольку такой операнд создается отдельным методом
     * {@link #createColumnOperand}.
     * Метод различает следующие типы операндов:
     * <UL>
     *   <LI><code>LexConst.BOOLEAN_LITERAL</code>. При этом создается
     *        объект типа {@link LiteralOperand}, первым параметром которого
     *        является значение <code>Boolean.class</code>, а вторым
     *        <code>symbol.value</code>.
     *   </LI>
     *   <LI><code>LexConst.FLOATING_POINT_LITERAL</code>. При этом создается
     *        объект типа {@link LiteralOperand}, первым параметром которого
     *        является значение <code>Double.class</code>, а вторым
     *        <code>symbol.value</code>, преобразованный, если требуется в тип
     *        <code>double</code>.
     *   </LI>
     *   <LI><code>LexConst.INTEGER_LITERAL</code>. При этом создается
     *        объект типа {@link LiteralOperand}, первым параметром которого
     *        является значение <code>Integer.class</code> или
     *        <code>Long.class</code>, взависимости от типа
     *        <code>symbol.value</code>, а вторым -
     *        <code>symbol.value</code>.
     *   </LI>
     *   <LI><code>LexConst.STRING_LITERAL</code>. При этом создается
     *        объект типа {@link LiteralOperand}, первым параметром которого
     *        является значение <code>String.class</code>, а вторым
     *        <code>symbol.value</code>.
     *   </LI>
     *   <LI><code>LexConst.NULL_LITERAL</code>. При этом создается
     *        объект типа {@link LiteralOperand}, первым параметром которого
     *        является значение <code>Object.class</code>, а вторым
     *        <code>null</code>.
     *   </LI>
     * </UL>
     *
     * @param symbol объект типа {@link tdo.tools.logexpr#Symbol}, полученный
     *       от сканнера.
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
     * Создает экземпляр класса {@link LiteralOperand}, используя тип и значение
     * параметра <code>value</code>.
     * @param value значение, используемое для построения объекта
     * @return новый объект типа {@link LiteralOperand}
     */
    protected IOperand createLiteralOperand(Object value) {
        IOperand o = new LiteralOperand(value);
        o.setContext(context);
        return o;
    }

    /**
     * Создает новый объект класса, реализующего интерфейс {@link IOperator}
     * в соответствии со значение свойства <code>symbol.sym</code>.
     * Метод создает объект одного из классов:
     * <UL>
     *      <LI>{@link AndBetweenOperator} оператор, соответствующий ключевому
     *              слову <code>AND</code> опрератора сравнения
     *              <code>BETWEEN</code>.
     *      </LI>
     *      <LI>{@link AndOperator} оператор, соответствующий логическому
     *              опрератору <code>AND</code>.
     *      </LI>
     *      <LI>{@link OrOperator} оператор, соответствующий логическому
     *              оператору <code>AND</code>.
     *      </LI>
     *      <LI>{@link NotOperator} оператор, соответствующий логическому
     *              оператору <code>NOTD</code>.
     *      </LI>
     *      <LI>{@link LikeOperator} оператор, соответствующий
     *              оператору сравнения <code>LIKE</code>.
     *      </LI>
     *      <LI>{@link NotLikeOperator} оператор, соответствующий оператору
     *              сравнения <code>NOT LIKE</code>.
     *      </LI>
     *      <LI>{@link RegExOperator} оператор, соответствующий оператору
     *              сравнения <code>REGEX</code>.
     *      </LI>
     *      <LI>{@link NotRegExOperator} оператор, соответствующий оператору
     *              сравнения <code>NOT REGEX</code>.
     *      </LI>
     *      <LI>{@link CommaOperator} оператор, соответствующий оператору
     *              <code>, </code> - запятая.
     *      </LI>
     *      <LI>{@link ContainingOperator} оператор, соответствующий оператору
     *              сравнения <code>CONTAINING</code>.
     *      </LI>
     *      <LI>{@link NotContainingOperator} оператор, соответствующий оператору
     *              сравнения <code>NOT CONTAINING</code>.
     *      </LI>
     *      <LI>{@link StartingWithOperator} оператор, соответствующий оператору
     *              сравнения <code>STARTING WITH</code>.
     *      </LI>
     *      <LI>{@link NotStartingWithOperator} оператор, соответствующий оператору
     *              сравнения <code>NOT STARTING WITH</code>.
     *      </LI>
     *      <LI>{@link InOperator} оператор, соответствующий оператору
     *              сравнения <code>IN</code>.
     *      </LI>
     *      <LI>{@link NotInOperator} оператор, соответствующий оператору
     *              сравнения <code>NOT IN</code>.
     *      </LI>
     *      <LI>{@link BetweenOperator} оператор, соответствующий оператору
     *              сравнения <code>BETWEEN</code>.
     *      </LI>
     *      <LI>{@link NotBetweenOperator} оператор, соответствующий оператору
     *              сравнения <code>NOT BETWEEN</code>.
     *      </LI>
     *      <LI>{@link IsNullOperator} оператор, соответствующий оператору
     *              сравнения <code>IS NULL</code>.
     *      </LI>
     *      <LI>{@link IsNotNullOperator} оператор, соответствующий оператору
     *              сравнения <code>IS NOT NULL</code>.
     *      </LI>
     *      <LI>{@link GreaterOperator} оператор, соответствующий оператору
     *              сравнения <code><pre>></pre></code>.
     *      </LI>
     *      <LI>{@link GreaterEqualsOperator} оператор, соответствующий оператору
     *              сравнения <code><pre>>=</pre></code>.
     *      </LI>
     *      <LI>{@link EqualsOperator} оператор, соответствующий оператору
     *              сравнения <code><pre>=</pre></code>.
     *      </LI>
     *      <LI>{@link LessOperator} оператор, соответствующий оператору
     *              сравнения <code><pre><</pre></code>.
     *      </LI>
     *      <LI>{@link LessEqualsOperator} оператор, соответствующий оператору
     *              сравнения <code><pre><=</pre></code>.
     *      </LI>
     *      <LI>{@link NotEqualsOperator} оператор, соответствующий оператору
     *              сравнения <code><pre><></pre></code>.
     *      </LI>
     *      <LI>{@link AddOperator} оператор, соответствующий арифметическому
     *              оператору <code><pre>+</pre></code>.
     *      </LI>
     *      <LI>{@link SubtrOperator} оператор, соответствующий арифметическому
     *              оператору <code><pre>-</pre></code>.
     *      </LI>
     *      <LI>{@link MultOperator} оператор, соответствующий арифметическому
     *              оператору <code><pre>*</pre></code>.
     *      </LI>
     *      <LI>{@link DivOperator} оператор, соответствующий арифметическому
     *              оператору <code><pre>/</pre></code>.
     *      </LI>
     *      <LI>{@link ConcatOperator} оператор, соответствующий
     *              оператору конкатенации строк <code><pre>||</pre></code>.
     *      </LI>
     * </UL>
     * @param symbol
     * @return объектб реализующий интерфейс <code>IOperator</code> в
     *  соответствии со значением параметра.
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
     * Выполняет обработку объекта типа {@link IToken} и помещает его в
     * {@link #rpnStack} или в {@link #rpn}.
     * Объект <code>IToken</code> - это операнд, оператор или круглая скобка
     * (открывающая или закрывающая).
     * <pre>
     *   ЕСЛИ token есть левая круглая скобка ТО
     *          token помещается в вершину rpnStack (push).
     *   КОНЕЦ-ЕСЛИ
     *
     *   ЕСЛИ token есть правая круглая скобка ТО
     *        ЕСЛИ rpnStack пуст ТО
     *           выбрасывается исключение
     *        ИНАЧЕ
     *           ЦИКЛ-ПОКА rpnStack не пуст
     *              выбирается елемент из вершины rpnStack (pop)
     *              ЕСЛИ этот элемент левая скобка, ТО
     *                  цикл завершается (break)
     *              ИНАЧЕ
     *                  добавляем элемент к rpn
     *           КОНЕЦ-ЦИКЛА
     *           ЕСЛИ в цикле выше НЕ найдена левая скобка ТО
     *               выбрасывается исключение
     *           КОНЕЦ-ЕСЛИ
     *           ЕСЛИ rpnStack не пуст ТО
     *              не выполняя pop просматриваем элемент в вершине rpnStack (peek)
     *              ЕСЛИ это FunctionOperator ТО
     *                    Поскольку мы в методе parse искусственно вставили
     *                    откр. скобку перед обработкой функции, то теперь мы
     *                    должны имитировать наличие закр. скобки для чего:
     *                       извлекаем элемент типа FunctionOperator (pop)
     *                       добавляем его к rpn
     *                       выполняем pop для левой круглой скобки
     *              КОНЕЦ-ЕСЛИ
     *           КОНЕЦ-ЕСЛИ
     *
     *           завершаем метод
     *      ИНАЧЕ
     *           ЕСЛИ token - это операнд ТО
     *              добавляем к rpn
     *           КОНЕЦ-ЕСЛИ
     *           ЕСЛИ token - это оператор ТО
     *              ЕСЛИ rpnStack пуст  ИЛИ приоритет token строго больше
     *                                  элемента в вершине rpnStack
     *              ТО
     *                  поместить token в вершину rpnStack (push)
     *              ИНАЧЕ
     *                  ЦИКЛ-ПОКА rpnStack Не пуст
     *                      ЕСЛИ приоритет token строго больше элемента в
     *                          вершине rpnStack
     *                      TO
     *                          завершаем цикл (break)
     *                      ИНАЧЕ
     *                          выбираем элемент из вершины rpnStack
     *                          добавляем его к rpn
     *                      КОНЕЦ-ЕСЛИ
     *                  КОНЕЦ-ЦИКЛА
     *
     *                  добавляем token в вершину rpnStack
     *
     *           КОНЕЦ-ЕСЛИ
     *
     *   КОНЕЦ-ЕСЛИ
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
                    // Поскольку мы в методе parse искусственно вставили
                    // откр. скобку перед обработкой функции, то теперь мы
                    // должны имитировать наличие закр. скобки
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
     * Переносит оставшиеся элементы из стэка <code>rpnStack</code> в
     * rpn-коллекцию.
     * Этот метод завершает метод {@link #parse}
     */
    protected void freeRPNStack() {
        while (!this.rpnStack.empty()) {
            rpn.addElement(rpnStack.pop());
        }
    }

    /**
     * Возвращает объект типа {@link java.util#Vector}, содержащий выражение в постфиксной форме.
     * Элементами вектора являются объекты одного из типов:
     * <UL>
     *    <LI>{@link IOperand} - объектное представление операнда выражения; </LI>
     *    <LI>{@link IOperator} - объектное представление оператора выражения. </LI>
     * </UL>
     *
     * @return вектор, содержащий результат парсинга выражения.
     */
    public Vector getRPN() {
        return rpn;
    }

    /**
     * Поэлементно выводит на системную консоль содержимое поля результата {@link #rpn}.
     * Может быть применен для отладки и тестирования.
     */
    public void printRPN() {
        for (int i = 0; i < rpn.size(); i++) {
            System.out.println(i + "). " + rpn.get(i).toString());
        }
    }

    /**
     * Поэлементно выводит на системную консоль содержимое поля результата {@link #rpn}.
     * Может быть применен для отладки и тестирования.
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
     * Проверяет, является ли объект <code>symbol<code> опрератором выражения.
     * Хотя функция также является опереатором, но данный метод ее не распознает. Это связано
     * с особенности синтаксиса функции, поэтому для ее распознавания используется другой
     * алгоритм.
     *
     * @param symbol лексема, предоставленная сканнером.
     * @return true если свойство <code>symbol.sym</code> содержит одно из значений:
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
     *      false - в противном случае.
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
     * Проверяет, является ли объект <code>symbol<code> опрерандом выражения.
     *
     * @param symbol лексема, предоставленная сканнером.
     * @return true если свойство <code>symbol.sym</code> содержит одно из значений:
     *   <code><pre>
     *       LexConst.BOOLEAN_LITERAL,LexConst.INTEGER_LITERAL,
     *        LexConst.NULL_LITERAL,  LexConst.FLOATING_POINT_LITERAL,
     *       LexConst.STRING_LITERAL, LexConst.IDENTIFIER )
     *   </pre></code>
     *      false - в противном случае.
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
     * Проверяет, является ли объект <code>symbol<code> опрерандом-идентификатором выражения.
     *
     * @param symbol лексема, предоставленная сканнером.
     * @return true если свойство <code>symbol.sym</code> содержит значение
     *   <code> LexConst.IDENTIFIER
     *   </pre></code>
     *      false - в противном случае.
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
     * Проверяет, является ли объект <code>symbol<code> одной из круглых скобок.
     *
     * @param symbol лексема, предоставленная сканнером.
     * @return true если свойство <code>symbol.sym</code> содержит одно из значений:
     *   <code><pre> LexConst.LPAREN, LexConst.LPAREN </pre></code>
     *      false - в противном случае.
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
     * Проверяет, является ли объект <code>symbol<code> левой круглой скобкой.
     *
     * @param symbol лексема, предоставленная сканнером.
     * @return true если свойство <code>symbol.sym</code> содержит значение:
     *   <code><pre> LexConst.LPAREN </pre></code>
     *      false - в противном случае.
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
     * Проверяет, является ли объект <code>symbol<code> левой круглой скобкой.
     *
     * @param symbol лексема, предоставленная сканнером.
     * @return true если свойство <code>symbol.sym</code> содержит значение:
     *   <code><pre> LexConst.RPAREN </pre></code>
     *      false - в противном случае.
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
     * Проверяет, является ли объект <code>symbol<code> точкой.
     * Лексема "Точка" используется для представления имени колонки в виде алиасного и
     * основного имени.
     *
     * @param symbol лексема, предоставленная сканнером.
     * @return true если свойство <code>symbol.sym</code> содержит значение:
     *   <code><pre> LexConst.DOT </pre></code>
     *      false - в противном случае.
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
