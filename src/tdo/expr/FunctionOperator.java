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
 * <p> Когда при обработке исходного выражения встречается элемент, являющийся
 * идентификатором, за которым следует открывающая круглая скобка, то такой
 * элемент определяется как оператор-функция, для которого создается экземпляр
 * класса <code>FunctionOperator</code>. Задачей оператора
 * является создание соответствующего ему выражения базового типа
 * {@link tdo.expr.FunctionExpression}.
 * <p>Создание выражения производится применением метода
 * {@link #createExpression(tdo.expr.ExpressionContext, tdo.expr.IOperand, tdo.expr.IOperand)} .
 * Созданному выражению, назначается контекст выражения, ссылка на создающий
 * выражение оператор и операнды. Теперь, когда выражение вычисляется, оно
 * обладает всей необходимой информацией для этого.
 * <p>Оператор-функция имеет имя, которое доступно для чтения и записи методами
 * {@link #getName() } и {@link #setName(java.lang.String) } .
 * <p>Оператор-функция определяется как унарный оператор, метод {@link #isUnary() }
 * которого возвращает <code>true</code>.
 * <p>В таблице ниже приведен список имен функций и краткое описание
 * <table border="1">
 * <thead>
 * <tr>
 *      <th>Имя</th>
 *      <th>Описание</th>
 * </tr>
 * </thead>
 * <tbody>
 * <tr>
 * <td>UpperCase</td>
 * <td>Алиасы: Upper, U. Переводит символы строки в верхний регистр</td>
 * </tr>
 * <tr>
 * <td>LowerCase</td>
 * <td>Алиасы: Lower, L. Переводит символы строки в нижний регистр</td>
 * </tr>
 * <tr>
 * <td>Proper</td>
 * <td>Алиасы: P. Переводит самый первый символ строки в верхний регистр</td>
 * </tr>
 * <tr>
 * <td>Header</td>
 * <td>Алиасы: Hd. Переводит самый первый символ каждого слова строки в верхний
 *   регистр
 * </td>
 * </tr>
 * <tr>
 * <td>Trim</td>
 * <td>Алиасы: Tr. Отсекает крайние символы пробела слева и справа строки
 * </td>
 * </tr>
 * <tr>
 * <td>Padl</td>
 * <td>Алиасы: PL. В зависимости от параметров функции и типа операнда добавляет
 *   к строке слева заданное количество символов
 * </td>
 * </tr>
 * <tr>
 * <td>Padr</td>
 * <td>Алиасы: Pr. В зависимости от параметров функции и типа операнда добавляет
 *   к строке справа заданное количество символов
 * </td>
 * </tr>
 * <tr>
 * <td>Substring</td>
 * <td>Алиасы: Substr. выделяет подстроку из заданной строки
 * </td>
 * </tr>
 * <tr>
 * <td>NumberFormat</td>
 * <td>Алиасы: NumFormat, NF. Форматирует числовое значение согласно заданному
 *       шаблону и возвращает строковое представление
 * </td>
 * </tr>
 * <tr>
 * <td>RNumberFormat</td>
 * <td>Алиасы: RNumFormat, RNF. Форматирует числовое значение согласно заданному
 *       шаблону и возвращает строковое представление. Производит округление,
 *       до знака, последнего в шаблоне.
 * </td>
 * </tr>
 * <tr>
 * <td>DateFormat</td>
 * <td>Алиасы: DF. Форматирует значение даты согласно заданному шаблону и
 *                 возвращает строковое представление.
 * </td>
 * </tr>
 * <tr>
 * <td>RowValue</td>
 * <td>Алиасы: Rv. Возвращает значение ячейки ряда для заданного индекса таблицы и
 * заданного имени колонки.
 * </td>
 * </tr>
 * <tr>
 * <td>IIF</td>
 * <td>Возвращает одно из двух заданных значений, в зависимости от заданного
 *   условия.
 * </td>
 * </tr>
 * <tr>
 * <td>ROWNO</td>
 * <td>Возвращает индекс текущего ряда для заданного алиаса таблицы или
 *  алиаса по умолчанию, если опущен параметр.
 * </td>
 * </tr>
 * <tr>
 * <td>RANDOM</td>
 * <td>Возвращает значение <code>java.Math.random()</code>
 * </td>
 * </tr>
 * <tr>
 * <td>DEPTH</td>
 * <td>Возвращает значение {@link tdo.RowState#getDepth() } для текущего ряда и
 *   заданного алиаса таблицы или алиаса по умолчанию, если опущен параметр.
 * </td>
 * </tr>
 * <tr>
 * <td>ISDELETED</td>
 * <td>Алиасы: IsDel. Возвращает значение <code>true</code> если текущий ряд удален
 *  для заданного алиаса таблицы или алиаса по умолчанию, если опущен параметр.
 *  Если ряд не удален возвращает <code>false</code>.
 * </td>
 * </tr>
 * <tr>
 * <td>ROWSTATE</td>
 * <td>Возвращает значение свойства <code>editingState</code> текущего ряда
 *   для заданного алиаса таблицы или алиаса по умолчанию, если опущен параметр.
 *</td>
 *</tr>
 * <tr>
 * <td>BIGDECIMAL</td>
 * <td>Алиасы: Dec. Преобразует заданное значение в тип
 *   <code>java.math.BigDecimal</code>.
 * </td>
 * </tr>
 * <tr>
 * <td>DOUBLE</td>
 * <td>Алиасы: Dou. Преобразует заданное значение в тип
 *   <code>java.lang.Double</code>.
 * </td>
 * </tr>
 * <tr>
 * <td>FLOAT</td>
 * <td>Алиасы: FL, REAL. Преобразует заданное значение в тип
 *   <code>java.lang.Float</code>.
 * </td>
 * </tr>
 * <tr>
 * <td>LONG</td>
 * <td>Алиасы: LO, BigInt. Преобразует заданное значение в тип
 *   <code>java.lang.Long</code>.
 * </td>
 * </tr>
 * <tr>
 * <td>INTEGER</td>
 * <td>Алиасы: Int. Преобразует заданное значение в тип
 *   <code>java.lang.Integer</code>.
 * </td>
 * </tr>
 * <tr>
 * <td>SHORT</td>
 * <td>Алиасы: Small, Smallint. Преобразует заданное значение в тип
 *   <code>java.lang.Short</code>.
 * </td>
 * </tr>
 * <tr>
 * <td>BYTE</td>
 * <td>Алиасы: Tiny, TyniInt. Преобразует заданное значение в тип
 *   <code>java.lang.Byte</code>.
 * </td>
 * </tr>
 * <tr>
 * <td>Date</td>
 * <td>Алиасы: Dat. Преобразует заданное значение в тип
 *   <code>java.util.Date</code> по заданному образцу.
 * </td>
 * </tr>
 * <tr>
 * <td>$</td>
 * <td>	Создает и возвращает объект типа <code>java.util.Date</code> по заданным
 *     целочисленным значениям года, месяца и дня месяца.
 * </td>
 * </tr>
 * <tr>
 * <td>TIMESTAMP</td>
 * <td>Алиасы: TSM. Преобразует заданное значение в тип
 *   <code>java.sql.Timestamp</code> по заданному образцу.
 * </td>
 * </tr>
 * <tr>
 * <td>Now</td>
 * <td>Алиасы: today. Возвращает текущую дату как объект типа
 *   <code>java.util.Date</code>.
 * </td>
 * </tr>
 *
 * <tr>
 * <td>ABS</td>
 * <td>Соответствует  функции из пакета <code>java.Math.abs(Double)</code></td>
 * </tr>
 * <tr>
 * <td>ACOS</td>
 * <td>Соответствует  функции из пакета <code>java.Math.acos(Double)</code></td>
 * </tr>
 * <tr>
 * <td>ASIN</td>
 * <td>Соответствует  функции из пакета <code>java.Math.asin(Double)</code></td>
 * </tr>
 * <tr>
 * <td>ATAN</td>
 * <td>Соответствует  функции из пакета <code>java.Math.atan(Double)</code></td>
 * </tr>
 * <tr>
 * <td>CEIL</td>
 * <td>Соответствует  функции из пакета <code>java.Math.ceil(Double)</code></td>
 * </tr>
 * <tr>
 * <td>COS</td>
 * <td>Соответствует  функции из пакета <code>java.Math.cos(Double)</code></td>
 * </tr>
 * <tr>
 * <td>SIN</td>
 * <td>Соответствует  функции из пакета <code>java.Math.sin(Double)</code></td>
 * </tr>
 * <tr>
 * <td>TAN</td>
 * <td>Соответствует  функции из пакета <code>java.Math.tan(Double)</code></td>
 * </tr>
 * <tr>
 * <td>EXP</td>
 * <td>Соответствует  функции из пакета <code>java.Math.exp(Double)</code></td>
 * </tr>
 * <tr>
 * <td>FLOOR</td>
 * <td>Соответствует  функции из пакета <code>java.Math.floor(Double)</code></td>
 * </tr>
 * <tr>
 * <td>LOG</td>
 * <td>Соответствует  функции из пакета <code>java.Math.log(Double)</code></td>
 * </tr>
 * <tr>
 * <td>RINT</td>
 * <td>Соответствует  функции из пакета <code>java.Math.rint(Double)</code></td>
 * </tr>
 * <tr>
 * <td>ROUND</td>
 * <td>Соответствует  функции из пакета <code>java.Math.round(Double)</code></td>
 * </tr>
 * <tr>
 * <td>SQRT</td>
 * <td>Соответствует  функции из пакета <code>java.Math.sqrt(Double)</code></td>
 * </tr>
 * <tr>
 * <td>TODEGREES</td>
 * <td>Соответствует  функции из пакета <code>java.Math.toDegrees(Double)</code></td>
 * </tr>
 * <tr>
 * <td>TORADIANS</td>
 * <td>Соответствует  функции из пакета <code>java.Math.toRadians(Double)</code></td>
 * </tr>
 * <tr>
 * <td>ATAN2</td>
 * <td>Соответствует  функции из пакета <code>java.Math.atan2(Double,Double)</code></td>
 * </tr>
 * <tr>
 * <td>POW</td>
 * <td>Соответствует  функции из пакета <code>java.Math.pow(Double,Double)</code></td>
 * </tr>
 * <tr>
 * <td>IEEEREMAINDER</td>
 * <td>Соответствует  функции из пакета <code>java.Math.IEEEremainder(Double,Double)</code></td>
 * </tr>
 * </tbody>
 * </table>
 */
public class FunctionOperator extends AbstractOperator {

    /**
     * Хранит имя функции
     */
    private String name;
    /**
     * Создает экземпляр класса для значения имени функции <code>null</code>.
     */
    public FunctionOperator() {
        this(null);
    }

    /**
     * Создает экземпляр класса для заданного имени функции.
     * @param fname имя функции
     */
    public FunctionOperator(String fname) {
        this.name = fname;
    }

    /**
     * Возвращает имя функции.
     * @return имя функции
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает новое значение имени функции.
     * @param fname новое имя функции
     */
    public void setName(String fname) {
        name = fname;
    }

    /**
     * Устанавливает значение свойства <code>lexType</code>
     * равным значению {@link tdo.tools.expr.LexConst#IDENTIFIER }.
     */
    @Override
    protected void setLexType() {
        lexType = LexConst.IDENTIFIER;
    }
    /**
     * Возвращает значение приоритета оператора при вычислении выражения.
     * @return значение приоритета равное 10
     */
    @Override
    public int getPriority() {
        return 10;
    }
    /**
     * @return строку знаков, содержащую имя функции
     */
    @Override
    public String toString() {
        return name;
    }
    /**
     * @return возвращает <code>true</code>, указывая что оператор унарный
     */
    @Override
    public boolean isUnary() {
        return true;
    }

    /**
     * Создает выражение для заданного контекста и операнда.<p>
     * В зависимости от имени функции определяется класс, наследующий от
     * {@link tdo.expr.FunctionExpression } и создается его экземпляр.
     * @param context контекст выражения
     * @param op1 операнд выражения
     * @return созданное выражение
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
