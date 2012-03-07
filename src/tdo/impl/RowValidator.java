/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.impl;

import tdo.service.TableServices;

/**
 * —лужит дл€ создани€ объектов, используемых при валидации р€дов таблицы. <p>
 * @see tdo.impl.ValidationManager
 * @see tdo.impl.ColumnValidator
 */
public class RowValidator extends AbstractValidator{

    /**
     * —оздает экземпл€р класса дл€ заданной таблицы.<p>
     * @param tableServices сервисы таблицы, р€д которой провер€етс€ на валидность
     */
    public RowValidator(TableServices tableServices) {
        this.tableServices = tableServices;
    }


    
}//class RowValidator

