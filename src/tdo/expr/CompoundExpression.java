/*
 * CompoundExpression.java
 *
 */
package tdo.expr;

import java.util.Vector;
import tdo.NamedValues;
import tdo.tools.expr.LexConst;

/**
 *  Базовый класс для составных выражений. <p>
 * Составные выражения отличаются от прочих тем, что позволяют оперировать не
 * одним или двумя операндами, а списком операндов.
 * <p>К составным выражениям относятся логические выражения:
 * <ul>
 *   <li>{@link tdo.expr.CompoundExpression.AndExpression} </li>
 *   <li>{@link tdo.expr.CompoundExpression.OrExpression} </li>
 *   <li>{@link tdo.expr.CompoundExpression.NotExpression} </li>
 *   <li>{@link tdo.expr.CompoundExpression.AndBetweenExpression} </li>
 * </ul>
 *
 * а также выражение <i>"список через запятую"</i>, обслуживаемое классом
 * {@link tdo.expr.CompoundExpression.CommaListExpression}
 *
 * 
 */
public abstract class CompoundExpression extends AbstractExpression {

    /**
     * Используется как внутреннее хранилище операндов.
     */
    private Vector list;

    /**
     * Создает экземпляр выражения и устанавливает емкость хранилища операндов,
     * равное пяти.
     */
    public CompoundExpression() {
        this(5);
    }

    /**
     * Создает экземпляр выражения и устанавливает заданную емкость хранилища
     * операндов.
     * @param capacity емкость списка операндов выражения
     */
    public CompoundExpression(int capacity) {
        list = new Vector(capacity);
        setLexType();
    }

    /**
     * Добавляет заданный операнд к списку операндов выражения.
     * @param op добавляемый операнд
     */
    public void add(IOperand op) {
        list.addElement(op);
    }

    /**
     * Возвращает операнд выражения по индексу в списке операндов.
     * @param index индекс в списке операндов
     * @return операнд с заданным индексом
     */
    public IOperand get(int index) {
        return (IOperand) list.elementAt(index);
    }

    /**
     * @return true когда список операндов выражения пуст
     */
    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    /**
     * @return размер списка операндов.
     */
    public int size() {
        return list.size();
    }

    /**
     * Возвращает <code>java.lang.Boolean</code>.
     * @return
     */
    @Override
    public Class getType() {
        return Boolean.class;
    }

    /**
     * Обеспечивает вычисление логической операции <code>AND</code> над
     * списком булевых выражений.<p>
     * Переопределяет методы <code>getValue</code> класса
     * {@link tdo.expr.AbstractExpression}, позволяя обрабатывать список
     * операндов.
     *
     */
    public static class AndExpression extends CompoundExpression {

        /**
         * Возвращает булевое значение выражения для заданной именованной
         * коллекции элементов.<p>
         * Сканирует коллекцию операндов выражения и вычисляет значение каждого
         * операнда. Если очередной операнд имеет значение <code>false</code>,
         * то общий результат метода будет <code>false</code>.
         *
         * @param values именованная коллекция элементов, например,
         *  ряд <code>DataRow</code> таблицы
         * @return булевое значение выражения <code>true</code>, если список пуст
         *      или все операнды коллекции операндов имеют значение <code>true</code>.
         *    Иначе воозвращается <code>false</code>.
         * @see #getValue(tdo.NamedValues[])
         * @see tdo.DataRow
         * @see tdo.Table
         */
        @Override
        public Object getValue(NamedValues values) {
            boolean b = true;
            for (int i = 0; i < size(); i++) {
                Boolean v = (Boolean) get(i).getValue(values);
                if (!v.booleanValue()) {
                    b = false;
                    break;
                }
            }
            return b;
        }

        /**
         * Возвращает булевое значение выражения для заданного массива именованных
         * коллекций элементов.<p>
         * Сканирует коллекцию операндов выражения и вычисляет значение каждого
         * операнда. Если очередной операнд имеет значение <code>false</code>,
         * то общий результат метода будет <code>false</code>.
         *
         * @param values массив именованных коллекций элементов, например,
         *  ряд <code>DataRow</code> таблицы
         * @return булевое значение выражения <code>true</code>, если список пуст
         *      или все операнды коллекции операндов имеют значение <code>true</code>.
         *    Иначе воозвращается <code>false</code>.
         * @see #getValue(tdo.NamedValues)
         * @see tdo.DataRow
         * @see tdo.Table
         */
        @Override
        public Object getValue(NamedValues[] values) {
            boolean b = true;
            for (int i = 0; i < size(); i++) {
                Boolean v = (Boolean) get(i).getValue(values);
                if (!v.booleanValue()) {
                    b = false;
                    break;
                }
            }
            return b;
        }

        /**
         * Устанавливает значение свойства <code>lexType</code> равным
         * {@link tdo.tools.scanner.LScanner#AND } .
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.AND;
        }

        /**
         * Строит строковое представление списка операндов. <p>
         * Формат вывода:
         * <pre>
         *   ( elem-1 AND elem-2 AND ... AND elem-n )
         * </pre>
         * @return
         */
        @Override
        public String toString() {
            String s = "(";
            for (int i = 0; i < size(); i++) {
                s += get(i).toString();
                if (i != size() - 1) {
                    s += " AND ";
                }
            }
            s += ")";
            return s;
        }
    }//class AndExpression

    /**
     * Обеспечивает вычисление логической операции <code>OR</code> над
     * списком булевых выражений.<p>
     * Переопределяет методы <code>getValue</code> класса
     * {@link tdo.expr.AbstractExpression}, позволяя обрабатывать список
     * операндов.
     *
     */
    public static class OrExpression extends CompoundExpression {

        /**
         * Возвращает булевое значение выражения для заданной именованной
         * коллекции элементов.<p>
         * Сканирует коллекцию операндов выражения и вычисляет значение каждого
         * операнда. Если очередной операнд имеет значение <code>true</code>,
         * то общий результат метода будет <code>true</code>.
         *
         * @param values именованная коллекция элементов, например,
         *  ряд <code>DataRow</code> таблицы
         * @return булевое значение выражения <code>true</code>, если хотя бы
         *      один операнд коллекции операндов имеют значение <code>true</code>.
         *      Иначе воозвращается <code>false</code>.
         * @see #getValue(tdo.NamedValues[])
         * @see tdo.DataRow
         * @see tdo.Table
         */
        @Override
        public Object getValue(NamedValues values) {
            boolean b = false;
            for (int i = 0; i < size(); i++) {
                Boolean v = (Boolean) get(i).getValue(values);
                if (v.booleanValue()) {
                    b = true;
                    break;
                }
            }
            return b;
        }

        /**
         * Возвращает булевое значение выражения для заданного массива именованной
         * коллекции элементов.<p>
         * Сканирует коллекцию операндов выражения и вычисляет значение каждого
         * операнда. Если очередной операнд имеет значение <code>true</code>,
         * то общий результат метода будет <code>true</code>.
         *
         * @param values массив именованных коллекций элементов, например,
         *  ряд <code>DataRow</code> таблицы
         * @return булевое значение выражения <code>true</code>, если хотя бы
         *      один операнд коллекции операндов имеют значение <code>true</code>.
         *      Иначе воозвращается <code>false</code>.
         * @see #getValue(tdo.NamedValues[])
         * @see tdo.DataRow
         * @see tdo.Table
         */
        @Override
        public Object getValue(NamedValues[] values) {
            boolean b = false;
            for (int i = 0; i < size(); i++) {
                Boolean v = (Boolean) get(i).getValue(values);
                if (v.booleanValue()) {
                    b = true;
                    break;
                }
            }
            return b;
        }

        /**
         * Устанавливает значение свойства <code>lexType</code> равным
         * {@link tdo.tools.scanner.LScanner#OR } .
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.OR;
        }

        /**
         * Строит строковое представление списка операндов. <p>
         * Формат вывода:
         * <pre>
         *   ( elem-1 OR elem-2 OR ... OR elem-n )
         * </pre>
         * @return
         */
        @Override
        public String toString() {
            String s = "(";
            for (int i = 0; i < size(); i++) {
                s += get(i).toString();
                if (i != size() - 1) {
                    s += " OR ";
                }
            }
            s += ")";
            return s;
        }
    }//class OrExpression

    /**
     * Обеспечивает вычисление части "AND" логической операции
     * <code>BETWEEN</code> .<p>
     * Выражение может содержать логический оператор: <br>
     * <b><i>op1 between op2 and op3</i></b>
     * <p>При компиляции выражения этот оператор представлен как два оператора:
     * <ol>
     *   <li>{@link tdo.expr.AbstractOperator.BetweenOperator} . Работает с двумя
     *       операндами:
     *       <ol>
     *          <li>op1</li> 
     *          <li>op[] - массив, 0-м элементов которого является op2, а 1-м
     *              op3
     *          </li>
     *       </ol>
     *   </li>
     *   <li>
     *      {@link tdo.expr.CompoundExpression.AndBetweenExpression} , который
     *      строит массив опрерандов op2 и op3.
     *   </li>
     * </ol>
     *
     *
     */
    public static class AndBetweenExpression extends CompoundExpression {
        /**
         * Возвращает массив объектов, состоящий из двух элементов.
         * Нулевой элемент соответствует значению левого операнда. Второй значению
         * правого элемента в подвыражении "AND" between-оператора.
         *
         * @param values именованная коллекция элементов, например,
         *  ряд <code>DataRow</code> таблицы
         * @return массив объектов из двух элементов
         * @see tdo.expr.AbstractOperator.BetweenOperator
         * @see tdo.expr.AbstractOperator.AndBetweenOperator
         * @see #getValue(tdo.NamedValues[])
         */
        @Override
        public Object getValue(NamedValues values) {
            Object[] o = new Object[]{this.get(0).getValue(values), this.get(1).getValue(values)};
            return o;

        }
        /**
         * Возвращает массив объектов, состоящий из двух элементов.
         * Нулевой элемент соответствует значению левого операнда. Второй значению
         * правого элемента в подвыражении "AND" between-оператора.
         *
         * @param values массив именованных коллекций элементов, например,
         *  ряд <code>DataRow</code> таблицы
         * @return массив объектов из двух элементов
         * @see tdo.expr.AbstractOperator.BetweenOperator
         * @see tdo.expr.AbstractOperator.AndBetweenOperator
         * @see #getValue(tdo.NamedValues[])
         */
        @Override
        public Object getValue(NamedValues[] values) {
            Object[] o = new Object[]{this.get(0).getValue(values), this.get(1).getValue(values)};
            return o;

        }
        /**
         * @return значение <code>Object[].class</code>
         */
        @Override
        public Class getType() {
            return Object[].class;
        }

        /**
         * Устанавливает значение свойства <code>lexType</code> равным
         * {@link tdo.tools.scanner.LScanner#ANDBETWEEN } .
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.ANDBETWEEN;
        }

        /**
         * Строит строковое представление перандов. <p>
         * Формат вывода:
         * <pre>
         *   ( elem-1 AND elem-2 )
         * </pre>
         * @return
         */
        @Override
        public String toString() {
            String s = "(";
            s += get(0).toString();
            s += " AND ";
            s += get(1).toString();
            s += ")";
            return s;
        }
    }//class AndBetweenExpression

    /**
     * Обеспечивает вычисление выражения для оператора
     * {@link tdo.expr.AbstractOperator.CommaOperator}.
     * <p>В исходном выражении может использоваться список операндов,
     * разделенных запятой. Для такого оператора компилятор выражения создает
     * экземпляр данного класса. Значением выражения, возвращаемым методами
     * <code>getValue</code> является коллекция значений типа
     * {@link tdo.expr.ValueList} .
     */
    public static class CommaListExpression extends CompoundExpression {
        /**
         * Вычмисляемая коллекция значений выражения
         */
        protected ValueList valueList;

        /**
         * Возвращает коллекцию значений операндов, разделенных в выражении
         * запятой. <p>
         * Сканирует список операндов, вычисляет для каждого из них значение и
         *  добавляет его в коллекцию типа <code>ValueList</code> возвращаемых
         * значений.
         * @param values именованная коллекция элементов, например,
         *  ряд <code>DataRow</code> таблицы
         * @return коллекция значений операндов
         * @see tdo.expr.ValueList .
         * @see #getValue(tdo.NamedValues)
         */
        @Override
        public Object getValue(NamedValues values) {
            valueList = new ValueList(this.size());
            for (int i = 0; i < size(); i++) {
                valueList.add((get(i)).getValue(values));
            }
            return valueList;
        }
        /**
         * Возвращает коллекцию значений операндов, разделенных в выражении
         * запятой. <p>
         * Сканирует список операндов, вычисляет для каждого из них значение и
         *  добавляет его в коллекцию типа <code>ValueList</code> возвращаемых
         * значений.
         * @param values массив именованных коллекций элементов, например,
         *  ряд <code>DataRow</code> таблицы
         * @return коллекция значений операндов
         * @see tdo.expr.ValueList .
         * @see #getValue(tdo.NamedValues)
         */
        @Override
        public Object getValue(NamedValues[] values) {
            valueList = new ValueList(this.size());
            for (int i = 0; i < size(); i++) {
                valueList.add((get(i)).getValue(values));
            }
            return valueList;
        }
        /**
         * Устанавливает значение свойства <code>lexType</code> равным
         * {@link tdo.tools.scanner.LScanner#ANDBETWEEN } .
         */
        @Override
        protected void setLexType() {
            lexType = LexConst.COMMA;
        }
        /**
         * Строит строковое представление перандов. <p>
         * Формат вывода:
         * <pre>
         *   ( elem-1,elem-2, ... ,elem-n )
         * </pre>
         * @return
         */
        @Override
        public String toString() {
            String s = "(";
            for (int i = 0; i < size(); i++) {
                s += get(i).toString();
                if (i != size() - 1) {
                    s += ",";
                }
            }
            s += ")";
            return s;
        }
    }//class CommaListExpression
}//CompoundExpression
