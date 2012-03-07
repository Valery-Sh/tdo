/*
 * NamedOperand.java
 * 
 */

package tdo.expr;


import tdo.tools.expr.LexConst;

/**
 * ������� ����������� ����� ��� ������� {@link tdo.expr.IdentifierOperand},
 * {@link tdo.expr.ParameterOperand}.<p>
 * ��������� ����� ��������� ����������� ��������, �������������� �����
 * �������������� �/��� ���������. ����� ������������ �������� <code>name</code>
 * ��� ����������� ���������.
 *
 * ������ �������� <code>context</code> ���� {@link tdo.expr.ExpressionContext}
 * @see #getName()
 * @see #getContext() 
 *
 * 
 */
public abstract class NamedOperand implements IExpression{
    public int lexType; // for test and print
    /**
     * ������ �������� ����� �������������� ��� ���������
     */
    private String name;
    /**
     * ������ ������ �� �������� ��������.
     */
    protected ExpressionContext context;
    /**
     * ������� ��������� ������ �� ��������� ����� ������ <code>null</code>.
     */
    public NamedOperand() {
        this(null);
    }
    /**
     * ������� ��������� ������ � �������� ��������� �����.
     * @param id �������� ����� ��������
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
     * ���������� �������� ���������
     * @return �������� ���������
     * @see #setContext(tdo.expr.ExpressionContext)
     */
    public ExpressionContext getContext() {
        return context;
    }
    /**
     * ������������� �������� ��������� ���������
     * @see #getContext()
     */
    @Override
    public void setContext(ExpressionContext context) {
        this.context = context;
    }
    /**
     * ���������� ��� ��������.
     * @return ��� ��������
     * @see #setName(java.lang.String)
     */
    public String getName() {
        return name;
    }
    /**
     * ��������� ��� ��������.
     * @param id ����� ��� ��������
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
     * ���������� ��� ��������, �������������� ���������.
     * @return �������� <code>null</code>. ������-���������� ������
     *   �������������� ����� ��� �������������� ��������������� ����
     */
    @Override
    public Class getType() {
        return null;
    }        

    //@Override
    /**
     * ������������� ������������� �������� ����, � ������ ������������
     * �����������, ������ {@link tdo.tools.expr.LexConst#IDENTIFIER}.
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
