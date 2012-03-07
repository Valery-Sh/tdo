package tdo.event;

import java.util.EventListener;

/**
 * <p>Title: Filis Application</p>
 * <p>Description: Freq Sensor's Support</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: IS</p>
 * @author VNS
 * @version 1.0
 */

public interface PendingEditingListener extends EventListener {
    void stopPendingEditing();
}