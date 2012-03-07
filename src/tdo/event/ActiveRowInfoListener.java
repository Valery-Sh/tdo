package tdo.event;
import java.util.EventListener;

/**
 *
 * @author Valera
 */
public interface ActiveRowInfoListener extends EventListener {
    void activeRowInfoChanged(ActiveRowInfoEvent e);
}
