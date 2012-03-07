/*
 * DartaRowViewProvider.java
 */
package tdo;

import tdo.service.TableServices;

/**
 *
 * @author Valera
 */
public class DataRowViewProvider extends DefaultDataRowProvider {

    public DataRowViewProvider(TableServices context) {
        super(context);
    }

    /**
     * —оздает и возвращает экземпл€р р€да.
     * Ќовый р€д имеет тип <code>{@link tdo.DataRowView}.
     * @return новый экземпл€р р€да дл€ <code>TableView</code>
     */
    @Override
    public DataRow createRow() {
        DataRow r = getContext().getDataRowViewServices().createViewOwnerRow();
        DefaultDataRowView row =
                new DefaultDataRowView(getContext(), r.getContext(), r);

        DefaultRowViewCellCollection dvs = new DefaultRowViewCellCollection(row.getViewOwnerRow().getCellCollection());
        row.setCellCollection(dvs);
        return row;
    }

    /**
     * —оздает и возвращает экземпл€р на основе массива значений и интервала
     * индексов колонок.
     * 
     * Ќовый р€д имеет тип <code>{@link tdo.DataRowView}.
     * @param values массив значений колонок р€да
     * @param start начальное значение индекса колонки
     * @param end конечное значение индекса колонки
     * @return новый экземпл€р р€да дл€ <code>TableView</code>
     */
/*    @Override
    public DataRow createRow(Object[] values, int start, int end) {

        //Table odt = getContext().getCoreServices().getViewOwner();
        DataRow oRow = getContext().getDataRowViewServices().createViewOwnerRow(values, start, end);
        DefaultDataRowView drv = new DefaultDataRowView(getContext(), oRow.getContext(), oRow);
        DataRowViewCellProvider dvs = new DataRowViewCellProvider(oRow);
        drv.setDataCellProvider(dvs);
        drv.setDataRowProvider(this);
        
        return drv;

    }
*/
    public DataRowView createRowView() {
        return (DataRowView) createRow();
    }

    /**
     * ƒл€ заданного "обычного" р€да создаетс€ и возвращаетс€ его новый 
     * р€д-представление.
     * <i>—осто€ние</i> нового р€да-представлени€, (свойство <code>state</code> типа 
     * {@link tdo.RowState} ) будет копией <i>состо€ни€</i> заданного
     * параметром р€да.
     * 
     * @param viewOwnerRow р€д, дл€ которого создаетс€ представление
     * @return новый экземпл€р р€да-представлени€
     * @see tdo.RowState#copyFrom(tdo.RowState) 
     */
    public DataRowView createRowView(DataRow viewOwnerRow) {
        if (viewOwnerRow == null) {
            return null;
        }
        DefaultDataRowView drv = new DefaultDataRowView(getContext(), viewOwnerRow.getContext(), viewOwnerRow);
        //DefaultRowViewCellCollection dvs = new DefaultRowViewCellCollection(viewOwnerRow);
        DefaultRowViewCellCollection dvs = new DefaultRowViewCellCollection(drv.getViewOwnerRow().getCellCollection());        
        drv.setCellCollection(dvs);
        //drv.setDataRowProvider(this);

        drv.getState().copyFrom(viewOwnerRow.getState());

        return drv;

    }

    /**
     * —оздает и возвращает дл€ заданного представлени€ р€да новое 
     * представленик р€да.
     * 
     * »з р€да, €вл€ющегос€ представлением извлекаетс€ р€д таблицы-владельца
     * представлени€. ƒл€ него создаетс€ новый, обычный р€д, куда копируютс€ 
     * €чейки р€да таблицы-владельца. —оздаетс€ новый р€д-представление дл€ 
     * созданной нами копии, который есть результат метода. <p>
     * —осто€ние полей и свойств р€да, в том числе и свойства <code>state</code>
     * не измен€етс€.
     * 
     * @param rowView представление р€да
     * @return новый р€д-представление 
     */
    @Override
    public DataRow createRowCopy(DataRow rowView) {
        if (rowView == null) {
            return null;
        }
        DataRow ownerRow = ((DataRowView) rowView).getViewOwnerRow();
        DataRow newOwnerRow = ownerRow.createCopy();

        return createRowView(newOwnerRow);
    }
}//class DataRowViewProvider
