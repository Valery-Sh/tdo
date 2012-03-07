/*
 * EvaluatorContainer.java
 *
 */

package tdo.expr;

import java.util.Vector;
import tdo.DataRow;

/**
 * ������� ����� ��� ����������� {@link tdo.expr.PAND} � {@link tdo.expr.POR } .
 * ������-���������� ��������� ������� ��������� �����, ������������ �������
 * "�" �/��� "���".
 */
public class EvaluatorContainer implements RowEvaluator{
    /**
     * ���������� ��������� ���������� ����������.
     */
    private Vector items;

    /**
     * ������� ��������� ������ � �������������� ���������� ���������.
     */
    protected EvaluatorContainer() {
        this.items = new Vector(2);
    }
    
   /**
     * ��������� �������� � ��������� ����������. <p>
     * 
     * @param re ����������� �������� ���� <code>RowEvaluator</code>
    *  @see tdo.expr.RowEvaluator
    */
    public void add(RowEvaluator re) {
        this.items.add(re);
    }

    /**
     * ������� �������� �� ��������� ���������� ���������-����������. <p>
     * 
     * @param re ��������� �������� ���� <code>RowEvaluator</code>
     * @see tdo.expr.RowEvaluator
     */
    public void remove(RowEvaluator re) {
        this.items.remove(re);
    }    
    /**
     * @return true , ���� ��������� �� �������� ���������. false - � ���������
     *    ������
     */
    public boolean isEmpty() {
        return this.items.isEmpty();
    }
    /**
     * ���������� ������� ��������� ���������� �� ��������� ��������
     * @param index ������ �������� ��������
     * @return ������� ���� <code>RowEvaluator</code>.
     * @see tdo.expr.RowEvaluator
     */
    public RowEvaluator get(int index) {
        return (RowEvaluator)this.items.elementAt(index);
    }
    /**
     * ���������� ���������� ��������� ����������.
     * @return ���������� ��������� ����������
     */
    public int getCount() {
        return this.items.size();
    }
    /**
     * ��� ��������� ���� ��������� ��� ��������� � ��������� � ����������
     * �������� ��������. <p>
     * @param row ����������� ���
     * @return ����� <code>false</code>. ������-���������� ������ ��������������
     *  �����, ������������ �������� ��� ������.
     * @see tdo.expr.RowEvaluator
     * @see tdo.DataRow
     * @see #evaluate(tdo.DataRow[])
     */
    @Override
    public boolean evaluate(DataRow row ) {
        return false;
    }
    /**
     * ��� ��������� ������� ����� ��������� ��� ��������� � ��������� � ����������
     * �������� ��������. <p>
     * @param rows ����������� ������ �����
     * @return ����� <code>false</code>. ������-���������� ������ ��������������
     *  �����, ������������ �������� ��� ������.
     * @see tdo.expr.RowEvaluator
     * @see tdo.DataRow
     * @see #evaluate(tdo.DataRow)
     */
    public boolean evaluate(DataRow[] rows ) {
        return false;
    }
    
}//class EvaluatorContainer
