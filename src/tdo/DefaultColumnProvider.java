/*
 */
package tdo;

import java.math.BigDecimal;
import tdo.DataColumn.PDBBigIntColumn;
import tdo.DataColumn.PDBBitColumn;
import tdo.DataColumn.PDBDateColumn;
import tdo.DataColumn.PDBDecimalColumn;
import tdo.DataColumn.PDBDoubleColumn;
import tdo.DataColumn.PDBIntegerColumn;
import tdo.DataColumn.PDBJavaObjectColumn;
import tdo.DataColumn.PDBListColumn;
import tdo.DataColumn.PDBRealColumn;
import tdo.DataColumn.PDBSmallIntColumn;
import tdo.DataColumn.PDBStringColumn;
import tdo.DataColumn.PDBTimeColumn;
import tdo.DataColumn.PDBTimestampColumn;
import tdo.DataColumn.PDBTinyIntColumn;

/**
 *
 * @author Valery
 */
public class DefaultColumnProvider implements ColumnProvider {

    @Override
    public DataColumn createColumn(Class clazz, String columnName) {
        int sqlType = getSqlType(clazz);
        if ( sqlType == java.sql.Types.OTHER ) {
            DataColumn column = createJavaObjectColumn(clazz,columnName);
            if ( column != null )
                return column;
        }
        return createColumn(sqlType, columnName);
    }
    protected DataColumn createJavaObjectColumn(Class clazz, String columnName) {
        DataColumn column;//My 06.03.2012 = null;
        if ( ! clazz.equals(java.util.List.class) )
            return null;
        column = new PDBListColumn();
        column.setSqlType(java.sql.Types.OTHER);
        column.setName(columnName);
        return column;
        
    }
    
    @Override
    public DataColumn createColumn(int sqlType, String columnName) {
        DataColumn column = null;
        switch (sqlType) {
            case java.sql.Types.INTEGER:
                column = new PDBIntegerColumn();
                column.setName(columnName);
                column.setSqlType(java.sql.Types.INTEGER);
                
                break;
            case java.sql.Types.BIGINT:
                column = new PDBBigIntColumn();
                column.setName(columnName);
                column.setSqlType(java.sql.Types.BIGINT);
                break;
                
            case java.sql.Types.SMALLINT:
                column = new PDBSmallIntColumn();
                column.setName(columnName);
                column.setSqlType(java.sql.Types.SMALLINT);
                break;
            case java.sql.Types.TINYINT:
                column = new PDBTinyIntColumn();
                column.setName(columnName);
                column.setSqlType(java.sql.Types.TINYINT);
                break;
            case java.sql.Types.REAL:
                column = new PDBRealColumn();
                column.setName(columnName);
                column.setSqlType(java.sql.Types.REAL);
                break;
                
            case java.sql.Types.FLOAT:
                column = new PDBDoubleColumn();
                column.setName(columnName);
                column.setSqlType(java.sql.Types.FLOAT);
                
                break;
            case java.sql.Types.DOUBLE:
                column = new PDBDoubleColumn();
                column.setName(columnName);
                column.setSqlType(java.sql.Types.DOUBLE);
                column.setSize(15);
                break;
            case java.sql.Types.VARCHAR :
                column = new PDBStringColumn();
                column.setName(columnName);
                column.setSqlType(java.sql.Types.CHAR);
                column.setScale(0);
                break;
            case java.sql.Types.CHAR :
                column = new PDBStringColumn();
                column.setName(columnName);
                column.setSqlType(java.sql.Types.CHAR);
                column.setScale(0);
                break;
            case java.sql.Types.LONGNVARCHAR:
                column = new PDBStringColumn();
                column.setName(columnName);
                column.setSqlType(java.sql.Types.LONGNVARCHAR);
                column.setScale(0);
                break;
                
            case java.sql.Types.NUMERIC :
                column = new PDBDecimalColumn();
                column.setName(columnName);
                column.setSqlType(java.sql.Types.NUMERIC);
                column.setSize(15);
                column.setScale(2);
                break;
                
            case java.sql.Types.DECIMAL :
                column = new PDBDecimalColumn();
                column.setName(columnName);
                column.setSqlType(java.sql.Types.DECIMAL);
                break;
            case java.sql.Types.TIMESTAMP:
                column = new PDBTimestampColumn();
                column.setName(columnName);
                column.setSqlType(java.sql.Types.TIMESTAMP);
                break;
            case java.sql.Types.TIME:
                column = new PDBTimeColumn();
                column.setName(columnName);
                column.setSqlType(java.sql.Types.TIME);
                break;
            case java.sql.Types.DATE:
                column = new PDBDateColumn();
                column.setName(columnName);
                column.setSqlType(java.sql.Types.DATE);
                break;
                
            case java.sql.Types.BIT:
                column = new PDBBitColumn();
                column.setName(columnName);
                column.setSqlType(java.sql.Types.BIT);
                break;
                
            case java.sql.Types.OTHER :
                column = new PDBJavaObjectColumn();
                column.setName(columnName);
                column.setSqlType(java.sql.Types.OTHER);
                break;
        }//switch
        return column;
    }
    

    public int getSqlType(Class type) {

        if (type.isPrimitive()) {
            // boolean, byte, char, short, int, long, float, and double            `
            if (Integer.TYPE.equals(type)) {
                return java.sql.Types.INTEGER;
            }
            if (Boolean.TYPE.equals(type)) {
                return java.sql.Types.BIT;
            }
            if (Byte.TYPE.equals(type)) {
                return java.sql.Types.TINYINT;
            }
            if (Character.TYPE.equals(type)) {
                return java.sql.Types.CHAR;
            }
            if (Short.TYPE.equals(type)) {
                return java.sql.Types.SMALLINT;
            }
            if (Long.TYPE.equals(type)) {
                return java.sql.Types.BIGINT;
            }
            if (Double.TYPE.equals(type)) {
                return java.sql.Types.DOUBLE;
            }
            if (Float.TYPE.equals(type)) {
                return java.sql.Types.FLOAT;
            }
        }

        if (type.equals((new String()).getClass())) {
            return java.sql.Types.VARCHAR;
        }
        if (type.equals((new Integer(0)).getClass())) {
            return java.sql.Types.INTEGER;
        }
        if (type.equals((new Short("0")).getClass())) {
            return java.sql.Types.SMALLINT;
        }
        if (type.equals((new Long(0)).getClass())) {
            return java.sql.Types.BIGINT;
        }
        if (type.equals((new BigDecimal(0)).getClass())) {
            return java.sql.Types.DECIMAL;
        }
        if (type.equals((new Double(0)).getClass())) {
            return java.sql.Types.DOUBLE;
        }
        if (type.equals((new Float(0)).getClass())) {
            return java.sql.Types.REAL;
        }
        if (type.equals((new Boolean(false)).getClass())) {
            return java.sql.Types.BIT;
        }
        if (type.equals((new Byte("0")).getClass())) {
            return java.sql.Types.TINYINT;
        }
        if (type.equals((new java.sql.Date(12)).getClass())) {
            return java.sql.Types.DATE;
        }
        if (type.equals(java.util.Date.class)) {
            return java.sql.Types.DATE;
        }
        if (type.equals((new java.sql.Timestamp(0)).getClass())) {
            return java.sql.Types.TIMESTAMP;
        }
        if (type.equals((new Character('a')).getClass())) {
            return java.sql.Types.CHAR;
        }
        if (type.equals((new java.sql.Time(0)).getClass())) {
            return java.sql.Types.TIME;
        }
        byte[] bAr = {};

        if (type.equals(bAr.getClass())) {
            return java.sql.Types.BINARY;
        }
        return java.sql.Types.OTHER;
    }
}//class DefaultColumnProvider
