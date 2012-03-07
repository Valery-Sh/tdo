/*
 * DataRowView.java
 */

package tdo;

import tdo.service.TableServices;

/**
 *
 * @author Valera
 */
public interface DataRowView extends DataRow {
    DataRow getViewOwnerRow();
    TableServices getViewOwnerContext();
}
