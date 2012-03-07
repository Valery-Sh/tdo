package tdo.impl;

import java.sql.SQLException;
/**
 * <p>Title: Filis Application</p>
 * <p>Description: Freq Sensor's Support</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: IS</p>
 * @author VNS
 * @version 1.0
 */

public interface ICommand     {
  public int  executeQuery()  throws SQLException;

}