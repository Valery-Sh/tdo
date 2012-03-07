/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbdesktopbasicapp;

import tdo.LinkedTableValidator;
import tdo.event.ValidateEvent;
import tdo.event.ValidateListener;
import tdo.impl.Validator;

/**
 *
 * @author Valery
 */
public class TestValidateListener implements ValidateListener{
        /**
     * Вызывается при обработке ошибок валидации.
     * @param e событие, описывающее ошибку валидации
     */
    public void processError(ValidateEvent e) {
        Validator v = e.getValidator();
        if ( v instanceof LinkedTableValidator ) {
            LinkedTableValidator lv = (LinkedTableValidator)v;
            //Validator cv = lv.getChildValidator();
            String s = "Master table name = '" + lv.getTable().getTableName() +
                    "', row=" + lv.getTable().find(e.getRow() ) +
                    ". " + e.getMessage();
            System.out.println(s);
            lv.setMasterMessage("Master table name = '#t', row=#ri. " + e.getMessage());
            System.out.println(lv.getMasterMessage());
        }
    }

}
