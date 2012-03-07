/*
 * RowState.java
 *
 */

package tdo;

/**
 *
 * @author valery
 */
public interface RowState {
    public static final int DETACHED = -1; //  р€д создан, но в Table не добавлен
    public static final int LOADED = 0; //после считывани€ из бд
    public static final int INSERTING = 2; //ƒобавленна€ или вставленна€, но еще
                                           //не было Post
    public static final int MANMADE = 4; //ƒобавленна€ или вставленна€ и уже Post
    public static final int UPDATING = 6; //ќбновл€ютс€ данные, но ещене было Post
    public static final int UPDATED = 8; //—одержит измененые данные
    public static final int DELETING = 10; //”дал€емый р€д.
    public static final int DELETED = 12; //”даленный р€д.

    public int getOriginalState();

    public int getEditingState();

    public void copyFrom(RowState source);

    public boolean isDetached();

    public boolean isLoaded();

    public boolean isEditing();

    public boolean isInserting();

    public boolean isUpdating();

    public boolean isDeleting();

    public boolean isEdited();

    public boolean isManMade();

    public boolean isUpdated();

    public boolean isDeleted();

    boolean isBeginEditMode();

    /**
     * ¬озвращает ссылку на оригинальный р€д. <p>
     * @return <code>DataRow</code>
     */
    public DataRow getOriginalRow();

    /**
     * ¬озвращает ссылку на р€д  наход€щийс€ в состо€нии MANMADE или UPDATED
     * и перевод€щийс€ в состо€ние UPDATING. <p>
     * @return <code>DataRow</code>
     */
    public DataRow getUpdatingRow();

    /**
     * ¬озвращает ссылку на текущий р€д. <p>
     * @return <code>DataRow</code>
     */
    public DataRow getRow();

    /**
     * getter-метод свойства <code>messsage</code>.<p>
     * @return тип <code>String</code>
     * @see message
     * @see setMessage
     */
    public String getMessage();

    /**
     * setter-метод свойства <code>messsage</code>.<p>
     * @param message тип <code>String</code>
     * @see message
     * @see getMessage
     */
    void setMessage(String message);

    int getDepth();

    void setDepth(int state);
    
    String toString(int state);
    
    void columnAdded(int columnIndex);
    void columnRemoved(int columnIndex);
    void columnMoved(int columnIndex, int oldCellIndex, int newCellIndex);    
}