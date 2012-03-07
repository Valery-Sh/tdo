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
    public static final int DETACHED = -1; //  ��� ������, �� � Table �� ��������
    public static final int LOADED = 0; //����� ���������� �� ��
    public static final int INSERTING = 2; //����������� ��� �����������, �� ���
                                           //�� ���� Post
    public static final int MANMADE = 4; //����������� ��� ����������� � ��� Post
    public static final int UPDATING = 6; //����������� ������, �� ����� ���� Post
    public static final int UPDATED = 8; //�������� ��������� ������
    public static final int DELETING = 10; //��������� ���.
    public static final int DELETED = 12; //��������� ���.

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
     * ���������� ������ �� ������������ ���. <p>
     * @return <code>DataRow</code>
     */
    public DataRow getOriginalRow();

    /**
     * ���������� ������ �� ���  ����������� � ��������� MANMADE ��� UPDATED
     * � ������������� � ��������� UPDATING. <p>
     * @return <code>DataRow</code>
     */
    public DataRow getUpdatingRow();

    /**
     * ���������� ������ �� ������� ���. <p>
     * @return <code>DataRow</code>
     */
    public DataRow getRow();

    /**
     * getter-����� �������� <code>messsage</code>.<p>
     * @return ��� <code>String</code>
     * @see message
     * @see setMessage
     */
    public String getMessage();

    /**
     * setter-����� �������� <code>messsage</code>.<p>
     * @param message ��� <code>String</code>
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