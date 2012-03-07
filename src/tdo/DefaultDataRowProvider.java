package tdo;

import java.io.Serializable;
import tdo.service.TableServices;
/**
 * –еализаци€ интерфейса <code>tdo.DataRowProvider</code> позвол€ет создавать
 * объекты р€дов, с типом <code>tdo.DefaultDataRow</code>.
 * <p>  аждый объект класса исполн€етс€ в заданном контексте таблицы
 * {@link tdo.service.TableServices}.
 * <p>ќбъекты типа <code>tdo.DefaultDataRow</code> требуют назначени€ им 
 * экземпл€ра {@link tdo.DataCellCollection}, дл€ создани€ которых методы класса
 * используют сервисы 
 * {@link tdo.service.TableServices#getCellCollectionServices}. Ёто позвол€ет
 * использовать один и тот же класс <code>DefaultDataRow</code> и один и 
 * тот же провайдер р€дов дл€ обработки данных как в традиционной форме списка,
 * так и простых java bean объектов.
 * @see tdo.DataRow
 * @see tdo.ObjectDataCellCollection
 * @see tdo.DataRowViewProvider 
 * @see tdo.DataRowView
 */
public class DefaultDataRowProvider implements DataRowProvider,Serializable{
    /**
     *  онтекст таблицы, устанавливаемый через конструктор
     */
    private TableServices context;
    /**
     * —оздает новый экземпл€р класса дл€ задданного контекста таблицы.
     * @param context контекст таблицы
     */
    public DefaultDataRowProvider(TableServices context) {
        this.context = context;
    }
    /**
     * @return контекст таблицы
     */
    public TableServices getContext() {
        return context;
    }
    
    /**
     * —оздает и возвращает новый экземпл€р р€да дл€ заданного java bean объекта.
     * Runtime тип возвращаемого объекта - <code>tdo.DefaultDataRow</code>.
     * <p>ћетод запрашивает сервисы 
     * {@link tdo.service.TableServices#getCellCollectionServices } дл€ создани€
     * экземпл€ра <code>tdo.DataCellCollection</code> по заданному объекту и
     * назначает его вновь созданному р€ду. 
     * 
     * @param obj объект java bean - данные р€да.
     * @return новый экземпл€р класса <code>tdo.DefaultDataRow</code>.
     * @see #createRow()
     * @see tdo.DataRow
     * @see tdo.DefaultDataRow
     * @see tdo.DataCellCollection
     */
    @Override
    public  DataRow createRow(Object obj) {
        DataCellCollection dcc = context.getCellCollectionServices().createCellCollection(obj);
        DefaultDataRow row = new DefaultDataRow(context,dcc);
        return row;
    }
    /**
     * —оздает и возвращает "пустой р€д" как объект типа <code>DataRow</code>.
     * <p>ћетод запрашивает сервисы 
     * {@link tdo.service.TableServices#getCellCollectionServices } дл€ создани€
     * экземпл€ра <code>tdo.DataCellCollection</code> и назначает его вновь 
     * созданному р€ду. 
     * 
     * @return новый р€д, тип которого <code>tdo.DefaultDataRow</code>
     * @see #createRow(Object)
     * @see tdo.DataRow
     * @see tdo.DefaultDataRow
     * @see tdo.DataCellCollection
     */
    @Override
    public  DataRow createRow() {
        DataCellCollection dcc = context.getCellCollectionServices().createCellCollection();
        DefaultDataRow row = new DefaultDataRow(context,dcc);        
        return row;
    }
    /**
     * —оздает и возвращает новый р€д с копи€ми содержимого €чеек заданного р€да.
     * 
     * —осто€ние полей и свойств р€да, в том числе и свойства <code>state</code>
     * не измен€етс€.
     * 
     * @param row р€д, €чейки крторого копируютс€
     * @return новый р€д-копи€ заданного, тип которого 
     *    <code>tdo.DefaultDataRow</code> 
     * 
     * @see #createRow()
     * @see #createRow(Object)
     */
    @Override
    public DataRow createRowCopy(DataRow row) {
        if ( row == null )
            return null; 
        DataCellCollection dcc = context.getCellCollectionServices().createCellCollection();
        DefaultDataRow newRow = new DefaultDataRow(context,dcc);        
        
        newRow.copyFrom(row);
        return newRow;
    }
}//class DefaultDataRowProvider
