package tdo.event;

import java.util.EventListener;

public interface ColumnValueChangedListener extends EventListener {
  void updateCalculatedColumns(ColumnValueChangedEvent e);
  Object calculateColumnValues( ColumnValueChangedEvent e );
}