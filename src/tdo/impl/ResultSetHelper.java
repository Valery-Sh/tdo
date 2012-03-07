/*
 * ResultSetHelper.java
 *
 * Created on 06.10.2007, 20:33:30
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdo.impl;

/**
 *
 * @author Valera
 */
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import tdo.DataColumn;
import tdo.DataColumnCollection;
import tdo.DataRow;
import tdo.Table;

public class ResultSetHelper {

    //private Table table;

    public static void populate(Table table, ResultSet resultSet) {
        populate(table, resultSet, null);
    }

    public static void populate(Table table, ResultSet resultSet, String codePage) {
        table.clear();
        table.setLoading(true);
        createColumns(table, resultSet);

        fillData(table, resultSet, codePage);
        table.setLoading(false);

    }

    protected static void createColumns(Table table, ResultSet resultSet) {

        String errorMsg;//My 06.03.2012 = null;
        DataColumnCollection columns = table.getColumns().create();
        try {
            fillDataColumns(resultSet, columns);
            DataColumnCollection tableColumns = table.getColumns();
            for (int i = 0; i < tableColumns.getCount(); i++) {
                DataColumn dc = columns.get(i);
                if (dc.getKind() != DataColumn.DATA_KIND) {
                    columns.add(dc);
                }
            }
            table.setColumns(columns);
        } catch (SQLException sqlEx) {
            errorMsg = "N 0001 : PDataAdapter.createDataStoreColumns() : SQLERROR: \n";
            while (sqlEx != null) {
                errorMsg += sqlEx.getErrorCode() +
                        ", SQLState: " + sqlEx.getSQLState() +
                        ", Message: " + sqlEx.getMessage() +
                        ", Vendor: " + sqlEx.getErrorCode();
                System.out.println(errorMsg);
                sqlEx = sqlEx.getNextException();
            }

        }
    }

    /**
     * Заполняет существующую коллекцию типа <DataColumnCollection</code>.<p>
     *  На основе метаданных объекта типа <code>java.sql.ResultSet</code>,
     *  заданного первым параметром создает объекты типа <code>DataColumn</code>
     * и заносит их в коллекцию типа <code>DataColumnCollection</code>, определяемую
     * вторым параметром. Содержимое коллекции <code>columns</code> предварительно
     * очищается, т.е. из нее удаляются все (если есть) елементы.
     *
     * @param resultSet
     * @param columns
     * @throws SQLException
     */
    public static void fillDataColumns(ResultSet resultSet, DataColumnCollection columns)
            throws SQLException {
        String errorMsg;//My 06.03.2012 = null;
        ResultSetMetaData meta;
        columns.clear();
        try {
            meta = resultSet.getMetaData();
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                DataColumn column = columns.add(meta.getColumnType(i),
                        meta.getColumnName(i));
                column.setPrecision(meta.getPrecision(i));
                column.setScale(meta.getScale(i));
                column.setNullable(meta.isNullable(i));

            } //for

        } catch (SQLException sqlEx) {
            errorMsg = "N 0001 : PCommand.fillDataColumns : SQLERROR: \n";
            while (sqlEx != null) {
                errorMsg += sqlEx.getErrorCode() +
                        ", SQLState: " + sqlEx.getSQLState() +
                        ", Message: " + sqlEx.getMessage() +
                        ", Vendor: " + sqlEx.getErrorCode();
                System.out.println(errorMsg);
                sqlEx = sqlEx.getNextException();
            }
            throw new SQLException(errorMsg +
                    "CODE=" + sqlEx.getErrorCode() + " : " +
                    sqlEx.getMessage());

        } catch (Exception ex) {
            errorMsg = "N 0002 : PCommand.fillDataColumns : ERROR: \n" +
                    ex.getMessage();
            System.out.println(errorMsg);
            throw new SQLException(errorMsg);

        }

    }

    @SuppressWarnings("empty-statement")
    protected static void fillData(Table table, ResultSet resultSet, String codePage) {
        String errorMsg;//My 06.03.2012 = "";
        int n = 0;
        int dataColumnCount = table.getColumns().getCount(DataColumn.DATA_KIND);
        int columnCount = table.getColumns().getCount();

        DataRow row;

        try {
            while (resultSet.next()) {
                row = table.createRow();
                ++n;
                Object obj;//My 06.03.2012 = null;
                for (int i = 0; i < dataColumnCount; ++i) {
                    Object uo = resultSet.getObject(i + 1);
                    if (uo != null) {
                        obj = decodeObject(table, i, resultSet, codePage);
                    } else {
                        obj = null;
                    }
                    if (obj == null) {
                        Object o = resultSet.getObject(i + 1);
                        row.setValue(o, i);
                    } else {
                        row.setValue(obj, i);
                    }
                } //for
                table.addRow(row);
            } //while
        } catch (SQLException sqlEx) {
            errorMsg = "N 0001 : PDataAdapter.fillDataStore(IPDataStore dataStore)" +
                    " : SQLERROR: \n";
            while (sqlEx != null) {
                errorMsg += sqlEx.getErrorCode() +
                        ", SQLState: " + sqlEx.getSQLState() +
                        ", Message: " + sqlEx.getMessage() +
                        ", Vendor: " + sqlEx.getErrorCode();
                System.out.println(errorMsg);
                sqlEx = sqlEx.getNextException();
            }

        }

    }

    protected static Object decodeObject(Table table, int columnIndex, ResultSet resultSet, String codePage) {
        if (codePage != null && !codePage.equals("none")) {
            int fieldType = table.getColumns().get(columnIndex).getSqlType();
            if (fieldType == java.sql.Types.CHAR ||
                    fieldType == java.sql.Types.VARCHAR) {
                return getAsString(columnIndex + 1, resultSet, codePage);
            }
        }
        return null;
    }


    public static String getAsString(int columnIndex, ResultSet resultSet, String codePage) {
        String str;//My 06.03.2012 = null;
        try {
            byte[] b = resultSet.getBytes(columnIndex);
            if (resultSet.wasNull()) {
                return "";
            }
            str = new String(b, codePage);
        } catch (SQLException sqlEx) {
            while (sqlEx != null) {
                System.out.println(
                        "PDatabase.getAsString(int columnIndex,ResultSet resultSet,String codePage) " +
                        " : SQLERROR: \n" +
                        sqlEx.getErrorCode() +
                        ", SQLState: " + sqlEx.getSQLState() +
                        ", Message: " + sqlEx.getMessage() +
                        ", Vendor: " + sqlEx.getErrorCode());
                sqlEx = sqlEx.getNextException();
            }

            str = "";
        } catch (Exception ex) {
            System.out.println(
                    "PDatabase.getAsString(int columnIndex,ResultSet resultSet,String codePage) " +
                    " : ERROR: \n");
            //My 06.03.2012ex.printStackTrace();

            str = "";
        }

        if (str == null) {
            str = "";
        }
        return str;

    }
}
