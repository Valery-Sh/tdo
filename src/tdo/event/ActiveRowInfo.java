package tdo.event;
/**
 * <p>Title: Filis Application</p>
 * <p>Description: Freq Sensor's Support</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: IS</p>
 * @author VNS
 * @version 1.0
 */

public class ActiveRowInfo {
  public static final int EXTERNAL_MOVE = 0;

  public static final int KEY_DOWN  = 1;
  public static final int KEY_UP    = 2;
  public static final int PAGE_DOWN = 3;
  public static final int PAGE_UP   = 4;

  public static final int MOVE      = 5;
  public static final int MOUSE_MOVE= 6;

  private int adjustmentType = EXTERNAL_MOVE;
  private int activeRow;
  private int action = MOVE;

  public ActiveRowInfo(int activeRow) {
    this(activeRow,EXTERNAL_MOVE,MOVE);
  }
  public ActiveRowInfo(int activeRow, int adjustmentType) {
    this(activeRow,adjustmentType,MOVE);
  }
  public ActiveRowInfo(int activeRow, int adjustmentType, int action) {
    this.activeRow = activeRow;
    this.adjustmentType = adjustmentType;
    this.action = action;

  }

  public int getAdjustmentType() {
    return this.adjustmentType;
  }
  public int getActiveRow() {
    return this.activeRow;
  }

  public int getAction() {
    return this.action;
  }
  public void setAction(int action) {
    this.action = action;
  }
  public void setAdjustmentType(int adjustmentType) {
    this.adjustmentType = adjustmentType;
  }
  public void setActiveRow(int activeRow) {
    this.activeRow = activeRow;
  }

}