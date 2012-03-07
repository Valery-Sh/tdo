/*
 * TableView.java
 *
 */

package tdo;

/**
 *
 * @author Valera
 */
public interface TableView extends Table {

    DataRowView createRowView(DataRow row);

    Table getViewOwner();

    void setViewOwner(Table table);
}