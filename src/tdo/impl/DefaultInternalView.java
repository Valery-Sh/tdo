package tdo.impl;



import tdo.DataRowCollection;
import tdo.SimpleTable;

public class DefaultInternalView extends SimpleInternalViewImpl  {


    public DefaultInternalView(SimpleTable table) {
        super(table);
        //this.actionObjects = new Vector(5);
    }
    public DefaultInternalView(SimpleTable table, DataRowCollection tableRows) {
        super(table,tableRows);
       // this.actionObjects = new Vector(5);
    }


}//class DefaultInternalView
