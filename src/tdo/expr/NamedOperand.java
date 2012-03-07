/*
 * NamedOperand.java
 * 
 */

package tdo.expr;


import tdo.tools.expr.LexConst;

/**
 * Базовый абстрактный класс для классов {@link tdo.expr.IdentifierOperand},
 * {@link tdo.expr.ParameterOperand}.<p>
 * Выражение может содержать именованные операнды, представляющие собой
 * идентификаторы и/или параметры. Класс обеспечивает свойство <code>name</code>
 * для именованных операндов.
 *
 * Вводит свойство <code>context</code> типа {@link tdo.expr.ExpressionContext}
 * @see #getName()
 * @see #getContext() 
 *
 * 
 */
public abstract class NamedOperand implements IExpression{
    public int lexType; // for test and print
    /**
     * Хранит значение имени идентификатора или параметра
     */
    private String name;
    /**
     * Хранит ссылку на контекст выржения.
     */
    protected ExpressionContext context;
    /**
     * Создает экземпляр класса со значением имени равным <code>null</code>.
     */
    public NamedOperand() {
        this(null);
    }
    /**
     * Создает экземпляр класса с заданным значением имени.
     * @param id значение имени операнда
     */
    public NamedOperand(String id) {
        this.name = id;
        lexType = LexConst.IDENTIFIER;
    }
/*    public NamedOperand(String id, String alias) {
        this.name = id;
        this.alias = alias;
        lexType = LexConst.IDENTIFIER;
    }
*/
  //  @Override
    /**
     * Возвражает контекст выражения
     * @return контекст выражения
     * @see #setContext(tdo.expr.ExpressionContext)
     */
    public ExpressionContext getContext() {
        return context;
    }
    /**
     * Устанавливает значение контекста выражения
     * @see #getContext()
     */
    @Override
    public void setContext(ExpressionContext context) {
        this.context = context;
    }
    /**
     * Возвращает имя операнда.
     * @return имя операнда
     * @see #setName(java.lang.String)
     */
    public String getName() {
        return name;
    }
    /**
     * Назначает имя операнда.
     * @param id новое имя операнда
     * @see #getName()
     */
    public void setName(String id) {
        name = id;
    }
/*    public String getAlias() {
        return alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }
*/

    /**
     * Возвращакт тип значений, представляемых операндом.
     * @return значение <code>null</code>. Классы-наследники должны
     *   переопределять метод для предоставления действительного типа
     */
    @Override
    public Class getType() {
        return null;
    }        

    //@Override
    /**
     * Устанавливает целочисленное значение типа, в смысле лексического
     * анализатора, равное {@link tdo.tools.expr.LexConst#IDENTIFIER}.
     */
    protected void setLexType() {
        lexType = LexConst.IDENTIFIER;
    }
/*    @Override
    public String toString() {
        String s = "";
        if ( alias != null )
            s = alias + ".";
        s += name;
        return s;
    }
 */
/*
    @Override
    public String toString() {
        return this.name;
    }
*/
}
