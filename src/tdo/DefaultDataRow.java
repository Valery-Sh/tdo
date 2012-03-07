/*
 * DefaultDataRow.java
 * BUG LIST
 *   24.05.2008 ��� ��������� � ���� beginUpdateMode==true � !state.isEditing.
 *              ����� ������ endEdit � cancelEdit �� �������� ���� �� false
 * END BUG LIST
 */
package tdo;

import java.io.Serializable;
import static tdo.RowState.*;
import tdo.impl.ValidateException;
import tdo.service.DataRowServices;
import tdo.service.TableServices;

/**
 * ��������� ��������� {@link tdo.DataRow} ��� ������������� ������ ���� ������.
 * ������ ������ ������������ ��������� �������:
 * <ul>
 *   <li>������ � ��������� ������ �� �������������� ������� ��� �����;</li>
 *   <li>��������� ��������� ������, ����� ���������� ��������� ��������
 *      ��� ��������� ������� ��� �����;
 *   </li>
 *   <li>�������� ����;</li>
 *   <li>��������� <i>������������ ����������</i> ���� �������������� �������
 *       {@link #beginEdit}, {@link #endEdit} � {@link #cancelEdit}. 
 *       ������������ ��������� �������� ���������� ������ ���������� ������, ��
 *       ������ ������ <code>endEdit</code>;
 *   </li>
 *   <li>
 *       ��������� ������������ ����.
 *   </li>
 * </ul>
 * 
 * <p>������ ��� �������� �������� <code>state</code> ���� {@link tdo.RowState}.
 * ��� �������� ������������� �������� �� <i>������������(���������)</i>  
 * <i>�������</i> ��������� ����, � ������� <i>���� ������������ ���������</i>, 
 * ������������ ��������� ���������� ����, � ����� �������� ��������� ���������
 * ������������. 
 * 
 * <p><i>������������</i> ��� <i>���������</i> ��������� ���� ������������
 * ��������� �������� <code>originalState</code> ������� <code>RowState</code>.
 * ��� ���� � ������� ��������� DETACHED ��� �������� ����� DETACHED � ��������.
 * �������� DETACHED �������� <code>originalState</code> �������� ��� 
 * �������������� �������� ����. ����� ��� ����������� � ��������� INSERTING
 * ��� MANMADE �������� �������� ��������������� � MANMADE. ����� ��� 
 * ����������� � ��������� LOADED �������� �������� ��������������� � LOADED. 
 * ����� ��� ����������� �� ��������� INSERTING � ��������� DETACHED, 
 * �������� ����������� �������� DETACHED.
 * 
 * <p><i>�������</i> ��������� �������������� ���� ������������ ��������� �������� 
 * <code>editingState</code> ������� <code>RowState</code>. � ������ ������
 * ������� ��� ����� ��������� � ����� �� ��������� ���������:
 * <ul>
 *      <li>DETACHED</li>
 *      <li>LOADED</li>
 *      <li>INSERTING</li>
 *      <li>MANMADE</li>
 *      <li>UPDATING</li>
 *      <li>UPDATED</li>
 *      <li>DELETING</li>
 *      <li>DELETED</li>
 * </ul>
 * 
 * <p>���� <i>������������</i> ����������� ������� ��������� 
 * <code>beginEditMode</code>. �������� <code>true</code> ��������, ���
 * ���� ������������ ��������. <code>false</code>- ���������.
 * <p>������������ ��������� ���������� ���� �������������� ����� 
 * <code>originalState</code>. ����� ��� ������, �� ��� ������� ��������� �����
 * DELETED. ����� ����� ���� ���������� ��������� (������������) ��������� 
 * ����� ��������� ��������������� �������� �������� <code>originalState</code>.
 *
 * <p>��������� ��������� ������������ ���� ������� �� ����
 * �������:
 * <ol>
 *    <li><code>originalRow</code> ���� <code>tdo.DataRow;</li>
 *    <li><code>updatingRow ���� <code>tdo.DataRow</code></li>
 * </ol>
 * 
 * <h2>������������ ���������</h2>
 * 
 * <p>��� ��� ��� ����������� ����� ������������ <i>"������������ ���������"</i>.
 * ��� �����, ������ ��� ��������� ��������� ������ ����, ��� ���� �������� �����
 * <code>beginEdit()</code>, ������� <i>��������</i> ������������. 
 * <p>���� ��� ���� ������������ �� ������������, �� ����� ��������� ������ ����
 * �������� � �������� ���� �� ����������.���������� ���������
 * ��������.
 * 
 * <p>��������� ������� �� ������� ����������� ���� ������ �������� � �������
 *    ������ ������ ������-���� ����������. ����������� ������ � ���� ������
 *    � ���������� <code>java.sql.ResultSet</code> ������������ ��� ����������
 *    ������� <code>DataTable tbl;</code>. ������� ���� ������ � ����� ������ 
 *    ����� �������� ����: <i>startDate</i> - ���� ������ �� ������ � 
 *    <i>endDate</i> - ���� �����. ���� �� ������� �������� startDate=='13 mar 2002'
 *    � endDate=='12 may 2003'. ����� ��������� ��������� ������. �� ����� ����
 *    ������ ����: startDate=='13 mar 2004' � endDate=='13 mar 2005'. ����� 
 *    ������������, ��� ������� ������������, ��������� ����� ���� �������
 *    ����� ������� startDate �� ���������� ��������, � ���� ���������� �������
 *    � ��������� ������� ����. ���� �� ���������� � ���������� ������� ���������,
 *    ����� ��� <i><code>startDate <= endDate</code></i>, �� ��� ����� ���������. 
 *    ������, ���� ��������� ���� ������� ����������, �� ��������� ��������������
 *    �������� {@link tdo.impl.ValidateException}, ��� ������ �� ������. 
 * 
 * <p>������������ ��������� �����������, ��� ��������� �� ������������, �� ���
 * ���, ���� �� ����� ������ ����� {@link #endEdit}.
 * 
 * <p>������������ <i>����������</i> ����������� ������ {@link #beginEdit} 
 * ������ ��� ������������� ���������� ��������� �������:
 *  <ul>
 *     <li>������� �������� ������������ <code>beginEditMode</code> �����������
 *         � <code>false</code>, �.�. ������������ <i>���������</i>.</li>
 *     <li>�������� <code>editingState</code> �������� ���� �� ��������� �������� 
 *         <i>LOADED,MANMADE,UPDATED</i>. ����� ������ �������� ��������� �����.</i>.
 *     </li>
 *     <li>����� {@link tdo.Table#isLoading}</li> ���������� 
 *          <code>false</code>
 *      </li>
 *     <li>����� {@link tdo.Table#isEditProhibited}</li> ���������� 
 *          <code>false</code>
 *      </li>
 *  </ul>
 * 
 * <p>���� ��� �� ��������� ���� ������� ���������, �� <i>������������</i>
 * ����������, �.�. <code>beginEditMode</code> ��������������� � 
 * <code>true</code>. ���������� ���������� ����� {@link #updateRowVersions} , 
 * ������� ��������� �������� �� ���������� ������� ������ ����. �������, ��� 
 * �����  <code>beginEdit()</code> �� ������ �������� ��������� 
 * <code>state.editingState</code>.
 * 
 * <p>����������� <code>beginEditMode</code> ����� ���� ���������� �  
 * <code>false</code> ����������� ������ �� �������: {@link #endEdit} ���
 * {@link #cancelEdit}.
 * 
 * <p> ����� {@link #endEdit} �������� ������� ���� ��� �������������
 * ���������� ����, ��������� ���� �������:
 *  <ul>
 *     <li> ���� ��:
 *        <ol>
 *          <li>
 *             ��� ��������� � ����� �� ���������: INSERTING,UPDATING ��� DELETING
 *           </li>
 *           <li>��� ��������� � ��������� LOADED,MANMADE,UPDATED, 
 *                � ��� ���� <code>beginEditMode</code> ����������� � 
 *               <code>true</code>, �.�. ������������ <i>��������</i>.
 *           </li>
 *        </ol>  
 *     </li>
 *     <li>����� ������ {@link tdo.Table#fireValidate(tdo.DataRow)}
 *         �� ������ �������������� �������� {@link tdo.impl.ValidateException}
 *     </li>
 *  </ul>
 * 
 * <p> ����� {@link #cancelEdit} �������� ������� ���� ��� 
 * ���������� ������ �� ��������� ���� �������:
 *  <ul>
 *     <li>��� ��������� � ����� �� ���������: INSERTING,UPDATING ��� DELETING. 
 *     </li>
 *     <li>
 *         ��� ��������� � ��������� LOADED,MANMADE,UPDATED, � ��� ����
 *         <code>beginEditMode</code> ����������� � <code>true</code>, , �.�. 
 *         ������������ <i>���������</i>.
 *     </li>
 *  </ul>
 *
 * 
 * <h2>������������ ����</h2>
 * 
 * ��� ������� ���������, ������� ���� �� ������ ������ ������ 
 * <code>setValue</code> ������, ��� ����� ��������� ����� �������� ������ ����,
 * ���������� �������� �� ���������� ������ ���� � ���������� ������� 
 * <code>RowState</code>. 
 * 
 * <p>����� ������, ����� ���������� ��������� ������������, �����������,
 * ��� � ���������������� ���� ���� ���� ������� Person, ������� ���� 
 * ���������������.
 * ��������� SQL-������, ��������, "select * from Person" � �����������
 * �������, ��������, <code>java.sql.ResultSet</code> ��������� ������ ���� 
 * {@link tdo.Table}. ������ ����� ����� ������� ��������� � ������ ��������� 
 * ��� ������ <b>tbl</b> ���� <code>tdo.Table</code>
 * � ������ � ������� ���������� � ������������� �� ���� ������ ���������.
 * 
 * <p> �� ��������� � ������� <b>tbl</b> ��������� �����, 
 * ��������� ���� �������, � ����� ������������. ����������� ����� ��������
 * ������ ����� ���������� ��������� � ������� ���� ������ Person. ���������� ��
 * ����, ������������ �� JDBC ��� ������ �������� ��� ����������, �� ������
 * ������������ ����, ������������ ����������, <i>"������"</i> ������� 
 * <b>tbl</b>. ����� ���������, ���������� ����� ��� ������� ���� <b>tbl</b>
 * �������� �� �������:
 * <ul>
 *    <li>��� �������������, ������, �������� ��� ��������� ��� ������������
 *        "�������" ���� ����� ���?.
 *    </li>
 *    <li>
 *       ���� ��� �������������, �� ������ ���� ��� ������������ ���������?
 *    </li>
 * </ul>
 * 
 * <p>��� ����, ����� ���������� ������� ������� �������, ������, �����������
 * ��������� {@link tdo.Table} ���������� �������� <code>loading</code>.
 * ����� ��� ���������� <b>tbl</b> ������� �� <code>ResultSet</code>
 * �� ������������� �������� <code>loading</code> � <code>true</code>. 
 * ����������� ���� ����� ����� ��������� <code>LOADED</code>. �� ��������� 
 * ���������� ������������� �������� <code>loading</code> � <code>false</code>. 
 * � ����� �������, ����� ���� ����� ����������� � ��������� 
 * <code>INSERTING</code> ���  <code>MANMADE</code>.
 * 
 * <p> ��� ������� ������� ������� ������ �������� <code>originalRow</code> �
 * <code>updatingRow</code> ������ {@link tdo.RowState}. ��� ����� ������ 
 * ����������� ����, ������������ � ��������� LOADED, ��������� ����� ���������
 * <code>DataRow</code> � � ��� ���������� ���������� ���������� ��������,
 * ��������������� ����. ��� ������������ ����������� ������������� �������������
 * ���� � ����� ������, ����� �����������. ����� ���� ��� ��� ������������� �
 * ��� ���� ������� �������� ����� <code>endEdit</code> �� ����������� � 
 * ��������� UPDATED. 
 * 
 * <h2>��������� ������������ ���������</h2>
 * 
 * ������������ ��������� ���������� ������� <code>beginEdit</code>.
 * � ����������� �� �������� ��������� ����, ���������� �������� �� 
 * ��������� ������������ ����:
 * 
 * <ul>
 *      <li>LOADED - ��������� ����� ����, ������� ����������� �
 *                    {@link tdo.DefaultRowState#originalRow}.
 *                    �������� �������� {@link tdo.DefaultRowState#updatingRow} 
 *                    ��������������� � <code>null</code>. 
 *      <li>MANMADE  -  ��������� ����� ����, ������� ����������� �
 *                    {@link tdo.DefaultRowState#updatingRow}.
 *      <li>UPDATED   - ���������� MANMADE: ��������� ����� ����, ������� 
 *                      ����������� � {@link tdo.DefaultRowState#updatingRow}.
 * </ul>
 *
 * <b>����������.</b> ����� �� �������� ��������� ����.
 * 
 *  <h2>���������� �������������� ������� <code>endEdit</code></h2>
 * 
 * <p>��� ���������� �������������� ����, ������������ � ����
 * <code>beginEditMode</code> ������������ ����� {@link #endEdit()}. 
 * ��� ����, ������������ � ��������� INSERTING ��� UPDATING ����������� �����
 * {@link tdo.Table#fireValidate(tdo.DataRow)}, ������� ����� ��������� 
 * �������������� �������� {@link tdo.impl.ValidateException}, ��� ����� ��������
 * ����� �� ������� ������� ��������.
 * <p>���� ��� ������� ������ ��������, �� ��� ������� ���������, ������� ���� 
 * �� ������ ������ ����� ������:
 *    <ul>  
 *      <li>INSERTING  - ����������� ��������� MANMADE. �������� �������� 
 *                       {@link tdo.DefaultRowState#updatingRow} 
 *                       ��������������� � <code>null</code>. 
 *      </li>     
 *
 *      <li>UPDATING   - ����� ��������� ���� � ���� ������ ������� �� ��������
 *                       �������� <code>originalRow</code>. ���� ��� �������� 
 *                       <code>null</code>, �� ��� ��������, ��� ��������� 
 *                       ���������� ���� ���� MANMADE. ������� ����� ���������
 *                       ���������� MANMADE. � ��������� ������ ������ ���������
 *                       ������������� �������� UPDATED.
 *                       �������� �������� 
 *                       {@link tdo.DefaultRowState#updatingRow} 
 *                       ��������������� � <code>null</code>. 
 *      </li> 
 *   </ul>
 * 
 * <h2>������ �������������� ���� ������� <code>cancelEdit</code></h2>
 * 
 * <p>��� ������ �������������� ���� ������������ ����� {@link #cancelEdit()}. 
 * 
 * <p>��� ������� ���������, ������� ���� �� ������ ������ ����� ������:
 *    <ul>  
 *      <li>INSERTING  - ����������� ��������� DETACHED. ��� ������ ���� ������
 *                       �� �������. �������� <code>originalState</code> 
 *                       ����������� �������� DETACHED.
 *      </li>     
 *
 *      <li>UPDATING   - ����� ��������� ���� � ���� ������ ������� ��� �� 
 *              �������� �������� <code>originalRow</code>, ��� � �� 
 *              �������� �������� <code>updatingRow</code>. 
 * 
 *              ���� <code>originalRow</code> �� <code>null</code> � 
 *                <code>updatingRow</code> �� <code>null</code>, �� ����� 
 *                ���������� ���������� <code>UPDATED</code>. 
 *                ������ <code>updatingRow</code> ���������� � ������ ���. 
 *              ���� <code>originalRow</code> �� <code>null</code>, � 
 *                <code>updatingRow</code> ����� <code>null</code>, �� ����� 
 *                ���������� ���������� <code>LOADED</code>.
 *                ������ <code>originalRow</code> ���������� � ������ ���.
 *              ���� <code>originalRow</code> ����� <code>null</code>, 
 *                ��, ���������� �� <code>updateingRow</code>
 *                ����� ���������� ���������� <code>MANMADE</code>. 
 *                ������ <code>updatingRow</code> ���������� � ������ ���.
 *      </li> 
 *   </ul>
 * 
 * �������� <code>updatingRow</code>  � ����� ���������� ������ ����������� 
 * �������� <code>null</code>.
 * �������� <code>beginEditRow</code> ����������� �������� <code>false</code>.
 * 
 * <h2>��������� ������ ������� <code>setValue</code></h2>
 * 
 *  ����� <code>setValue</code>, ������ ����� �������� ������, ��������� 
 * �������� �� ��������� ������������.
 * 
 *  � ����������� �� �������� ��������� ����, ���������� ������ ���� ����������
 *  �� ��������� ��������:
 * 
 * <ul>
 *      <li>LOADED - ��������� ����� ����, ������� ����������� (��������
 *                    {@link tdo.DefaultRowState#originalRow}.
 *                    �������� �������� {@link tdo.DefaultRowState#updatingRow} 
 *                    ��������������� � <code>null</code>. 
 *      <li>MANMADE  -  ��������� ����� ����, ������� ����������� (��������
 *                    {@link tdo.DefaultRowState#updatingRow}.
 *      <li>UPDATED   - ���������� MANMADE: ��������� ����� ����, ������� 
 *                      ����������� (��������
 *                     {@link tdo.DefaultRowState#updatingRow}.
 * </ul>
 * 
 * <b>����������.</b> � ����������� ���� ������ ������������ �� ��� ���������. 
 *   ���� ��� ��������� � ������ ��������� �� �������� � �������� ���� �� 
 * ����������.
 * 
 * <p> ���� <i>������������ ����������</i> <b>���������</b>, �� ������
 * ���������� ������ <code>setValue</code> ��������� ��� �� �������� ��������� 
 * � ����� �� ��������, ����������� ����:
 * <ul>
 *      <li>DETACHED - <i>�� ����������</i></li>
 *      <li>LOADED - <i>UPDATED</i></li>
 *      <li>INSERTING - <i>�� ����������</i></li>
 *      <li>MANMADE  - <i>�� ����������</i></li>
 *      <li>UPDATING  - <i>UPDATED</i></li>
 *      <li>UPDATED   - <i>�� ����������</i></li>
 * </ul>
 *
 * <p> ���� <i>������������ ����������</i> <b>��������</b>, �� ������
 * ���������� ������ <code>setValue</code> ��������� ��� �� �������� �������� 
 * � ����� �� ��������, ����������� ����: 
 * <ul>
 *      <li>LOADED - <i>UPDATING</i></li>
 *      <li>INSERTING - <i>�� ����������</i></li>
 *      <li>MANMADE  - <i>UPDATING</i></li>
 *      <li>UPDATING  - <i>�� ����������</i></li>
 *      <li>UPDATED   - <i>UPDATING</i></li>
 * </ul>
 * 
 * <h2>�������� ����</code></h2>
 * 
 * ��� ��������� ����������� ������ {@link #delete}.     
 * ���� ��� ��������� � ��������� DETACHED ��� DELETED, �� ���������� ������
 * �����������. 
 * <p>��������� �������� ������� �� �������� ��������� ����.
 * <p>���� ������� ��������� INSERTING, �� ����� ���������� ���������� 
 * DETACHED. �������� <code>originalState</code> ����������� �������� DETACHED.
 * <p>���� ������� ��������� LOADED, �� ����� ���������� ���������� 
 * DELETED. 
 * <p>���� ������� ��������� MANMADE, �� ����� ���������� ���������� 
 * DELETED. 
 * <p>���� ������� ��������� UPDATING, �� ����� ���������� ���������� 
 * DELETED, � ������� ��� ����������������� �� <code>updatingRow</code>.
 * ����� - MANMADE ������� ��� ����������������� �� <code>updatingRow</code>.
 * 
 * 
 * <h2>��������� <i>DETACHED</i> </h2>
 * 
 * <p>����� ��� ������� ���������, �� �� �������� ���������: 
 * <code><i>DETACHED</i></code>. ��� ������ ���� ����� ������ 
 * <code>tdo.RowState</code> <code>isDetached()</code> ���������� 
 * <code>true</code>. ���� ��� �������� ���� �� ����� {@link tdo.Table}, 
 * �� ������������� ���������� <code>IllegalArgumentException</code>. 
 * <p>��� �������� ���������� ������ 
 * ����� ��������� ������ {@link tdo.RowState} � ����������� �������� 
 * <code>state</code>. �������� <code>readOnly</code> ��������������� � 
 * <code>false</code>. 
 * <p>�� ��������� DETACHED ��� ����� ���� ��������� � ������ ���������
 * ����������� ������ �� ���� �������:
 * <ul>
 *   <li>{@link #attach()}</li>
 *   <li>{@link #attachNew()}</li>
 * </ul>
 * 
 *  <p>���� ��� ��� ��������� � ���������, �������� �� <code>DETACHED</code>, ��
 *  ����� <code>attach()</code> �� ��������� �����-���� ���������, � �� �����
 *  ��� ����� <code>attachNew</code> ����������� ����������.
 * 
 * <p>����� <code><b>attach()</b></code> ��������� ��� � ���� �� ���������:
 *   <ul>
 *      <li>LOADED ���� ����� {@link tdo.Table#isLoading()}</li> ����������
 *          <code>true</code>. �������� <code>originalState</code> 
 *          ����������� �������� LOADED;
 *      <li>MANMADE � ��������� ������. �������� <code>originalState</code> 
 *          ����������� �������� MANMADE.</li>
 *   </ul>
 * 
 * <p>����� <code><b>attachNew()</b></code> ����������� ���������� 
 *   {@link tdo.DataOperationException} � �������, ����� 
 *   <code>table.isLoading()</code> ���������� <code>true</code> ��� ��� 
 *   ��������� � ���������, �������� �� <code>DETACHED</code>.
 * <p>����� <code>attachNew()</code>, ���� �� ����������� ����������, 
 * ������ ��������� ��� � ��������� <code>INSERTING</code>. �������� 
 * <code>originalState</code> ����������� �������� MANMADE;
 * <p> 
 * <p>������, ����������� ��������� <code>tdo.Table</code>, ���������� �����
 * <code>attach()</code> � <code>attachNew()</code> ��� ������� ����� �����. 
 * ����� {@link tdo.Table#addRow()} �������������� ������������ ��� ���������� 
 * "�������" ����, ������� ����� ����������� � ��������� <code>INSERTING</code> 
 * � ������� ��������� ��������� �� ������� ������� {@link #cancelEdit} ��� 
 * ����������� � ��������� <code>MANMADE</code> ������� {@link #endEdit}.
 * 
 * <p>����� {@link tdo.Table#addRow(tdo.DataRow)}
 * �������������� ������������ ��� ���������� ��� ������������� ����, ������� 
 * ������� �������������� ��� ��� LOADED ��� ��� MANMADE. ��������� 
 * LOADED ���� ��������, ��� ��� ��������� ��� ������, ������������ ������,
 * �.�. ��,������� ���� �� ������ ���������� ����, ����������� � , ��� 
 * ������������� ����� ���� �������������. ������, ����� ������� ����������� 
 * ������� �� ������� ����, �� ����� ��������� ������, ���������� ��� ���� �����
 * ���������� �������� ��� ��������� � ���� ������. ���������� ����, ������� 
 * ������������� ������ � ������� � ��������� LOADED, ������ ����� �
 * ��������� UPDATED ��� DELETED � ��� ���������� ���������� SQL-���������
 * UPDATE ��� DELETE. 
 * <p> ���� ��� ����������� � ������� � ��� ����������� ��������� MANMADE, ��
 * ������� ������������� ����� ��� "��������� ������������� �� ������� �����". 
 * � �������� ������  � ����� ��, � �������� �����, ��� ����� ������ ��� 
 * ��������� � ��������� MANMADE. ��� ���������� � ���� ������ ����������� 
 * SQL-�������� INSERT.
 * 
 * <h2>��������� ���� ����, ��������� ��������� �������� <code><i>DETACHED</i></code></h2>
 * 
 *  ���������� ������� <code>beginEdit,endEdit,cancelEdit</code> �� ��������
 * ������ � ���������. 
 * 
 * <h2>����� <code><i>setValue</i></code></h2>
 *  ����� �������� ������� ����, ��������������� ��� ������������� ���������� 
 * ��������� �������:
 *  <ul>
 *     <li>�������� <code>readOnly</code> �������, �������� ���������� ����� 
 *         <code>false</code> (��. {@link tdo.DataColumn}).
 *     </li>
 *     <li>�������� <code>editProhibited</code> ������� �����
 *         <code>false</code> (��. {@link tdo.Table#isEditProhibited() } ).
 *     </li>
 *     <li> ���� ����� �������� ������ ����, ��������������� ������� 
 *          <code>setValue</code> ����� <code>null</code> � �������� �������
 *          ��������� <code>null</code> ��������, �.�. 
 *          ����� {@link tdo.DataColumn#isNullable(}) ����������
 *         <code>true</code>. � ��������� ������ ������������� ����������
 *         <code>IllegalArgumentException</code>.
 *     </li>
 *  </ul>
 *  ���� ���� �� ���� �� ������� �� ���������, ����� �����������. <br>
 * ��� ���������� ���� ������� �������� ������ ���� �������������� � �����
 * �����������. 
 * 
 * <ul>
 *    <li>��������� <code>editingState</code> �� ����������.</li> 
 *    <li>�������� ������ ����, ������  �� ���������, �� �����������.</li
 *  </ul>
 * 
 * <h2>��������� <code>LOADED</code></h2>
 * 
 * 
 */
public class DefaultDataRow implements DataRow, Serializable {

    protected DefaultRowState state;
    private boolean readOnly;
    protected TableServices tableServices;
    protected DataRowServices rowServices;
    protected DataCellCollection cells;

    /**
     * ������� ��������� ������ ��� ��������� ��������� ������� � ��������������
     * �������� ���� ���������� �� ���������.
     * 
     * ������� ����� ������ ���� {@link tdo.DefaultRowState} � ��������� ���
     * �������� �������� <code>state</code>. ��������� ���� ���������������
     * DETACHED. �������� <code>readOnly</code> ����������� <code>false</code>.
     * 
     * @param services �������� �������, ��� ������� ��������� ������
     * @throws  IllegalArgumentException ���� �������� ��������� 
     *       <code>services</code> ����� <code>null</code>.
     */
    protected DefaultDataRow(TableServices services) {
        if (services == null) {
            throw new IllegalArgumentException("The 'services' parameter cannot be null");
        }
        this.state = new DefaultRowState(this);
        this.state.setEditingState(DETACHED);

        this.readOnly = false;
        this.tableServices = services;
        this.rowServices = services.getDataRowServices();
    }

    /**
     * ������� ��������� ������ ��� ��������� ��������� ������� � p��������
     * ��������� �����.
     * 
     * 
     * @param services �������� �������, ��� ������� ��������� ������
     * @cells �������� ��������� ����� ����
     * @throws  IllegalArgumentException ���� �������� ��������� 
     *       <code>services</code> ����� <code>null</code>.
     */
    protected DefaultDataRow(TableServices services, DataCellCollection cells) {
        this(services);
        this.cells = cells;
    }

    /**
     * ���������� ������� ��������� ����� ����.
     * 
     * ����� �� �������� ���������� � ���������� ��������� ��������������
     * ������ � � ������������ �������� ����� ���� � ��������������� �������.
     * ��� �������� �� ������� � �����������, ������� ������� ������ �������
     * ��� �� ���, ������������ ������� ���� <code>DataCellProvider</code>.
     * 
     * @return ������� ��������� ����� ����.
     * @see tdo.DataCellProvider
     * @see #setDataCellProvider
     */
    @Override
    public DataCellCollection getCellCollection() {
        return cells;
    }

    /**
     * ������������� ��������� ����� ����.
     * 
     * ����� �� �������� ���������� � ���������� ��������� ��������������
     * ������ � � ������������ �������� ����� ���� � ��������������� �������.
     * ��� �������� �� ������� � �����������, ������� ������� ������ �������
     * ��� �� ���, ������������ ������� ���� <code>DataCellProvider</code>.
     * 
     * @param cells ����� ������ ���������� ����� ����
     * @see tdo.DataCellProvider
     * @see #getDataCellProvider
     */
    protected void setCellCollection(DataCellCollection cells) {
        this.cells = cells;
    }

    /**
     * �������� ���������� ��������� ����.
     * 
     * ���� �������� ��������� <code>null</code>, �� ��������� �� ������������.
     * ���������� ������ ������ ����. ��������� ����� � ������� ����, � ��� 
     * ����� � �������� <code>state</code> �� ����������.
     * <p>���������� ���������� ���������� �����.
     * @param row ���, ���������� �������� ����������
     * @see #getDataCellProvider
     * @see #setDataCellProvider
     * @see tdo.DataCellProvider
     */
    @Override
    public void copyFrom(DataRow row) {
        if (row == null) {
            return;
        }
        cells.copyCells(row.getCellCollection());
    }

    /**
     * �������� �������� ��������� ������� � ������ ����.
     * 
     * ���������� ������ ������ ����. ��������� ����� � ������� ����, � ��� 
     * ����� � �������� <code>state</code> �� ����������.
     * 
     * <p>���������� ���������� ���������� �����.
     * 
     * @param obj ������� ��������, �������� �������� ���������� � ������ ���
     * @see #getDataCellProvider
     * @see #setDataCellProvider
     * @see tdo.DataCellProvider
     */
    @Override
    public void copyFrom(Object[] obj) {
        if (obj == null) {
            return;
        }
        cells.copyCells(obj);
    }

    /**
     * ������� � ���������� ����� ���, �������� ����� �������� ��������� ��
     * ���������� ����� �������� ����.
     * @return ����� ���-����� ��������
     */
    @Override
    public DataRow createCopy() {
        DataRow r = newRow();
        r.copyFrom(this);
        return r;
    }

    /**
     * ������� � ���������� ����� ���, �������� ����� �������� ��������� ��
     * ���������� ����� ��������� ����.
     * @return ����� ���-����� ��������� ����
     */
    @Override
    public DataRow createCopyOf(DataRow row) {
        DataRow r = newRow();
        r.copyFrom(row);
        return r;
    }

    private DataRow newRow() {
        return rowServices.createRow();
    }

    /**
     * ������� ������ ���� � ����������� �� ��� �������� ���������.
     * 
     * ���� ������� ��������� LOADED, �� ������� ����� �������� ����
     * � �������� �� � <code>originalRow</code> ������� <code>RowState</code>.
     * �������� <code>updatingRow</code> ����������� <code>null</code>.
     * 
     * <p>���� ������� ��������� MANMADE ��� UPDATED, �� ������� ����� �������� 
     * ���� � �������� �� � <code>updatingRow</code> ������� 
     * <code>RowState</code>. �������� <code>originalRow</code> ��������� 
     * �������� <code>null</code>.
     * 
     * <p>����� �� �������� �������� ��������� ���� � ���� ������������.
     * 
     * @see #setValue(Object,int)
     * @see #cancelEdit
     */
    protected void updateRowVersions() {
        if (getState().isLoaded()) {
            // the row has been inserted in LOADING mode,
            // hasn't been updated yet. =>
            // clone the row and save it in the row state object
            //DataRow orow = this.rowProvider.createRowCopy(this); // ����� ���������, ��� ����� ���� row
            DataRow orow = this.createCopy();
            state.setOriginalRow(orow);
            state.setUpdatingRow(null);
        }

        if (getState().isUpdated() || getState().isManMade()) {
            // clone the row and save it in the row state object
            // This copy might be used when method cancelEditing invoked.
            //DataRow orow = this.rowProvider.createRowCopy(this); // ����� ���������, ��� ����� ���� row
            DataRow orow = this.createCopy();
            state.setUpdatingRow(orow);
        }
    }

    /**
     * �������� ������ ����� � ����������� �� �������� ��������� ����.
     * 
     * ��������� ������������ ���������, �������� �������� 
     * <code>beginEditMode</code> �������� <code>false</code>.
     * 
     * ���� ������� ��������� ���� LOADED, �� �������� <code>originalRow</code>
     * � �������� <code>updatingRow</code> ������� <code>state</code> 
     * ��������� <code>null</code>.
     * <p> ���� ������� ��������� ���� UPDATED ��� MANDATE, 
     * �� �������� <code>updatingRow</code> ������� <code>state</code> 
     * ��������� <code>null</code>.
    
     * ���������� �� ������� {@link #endEdit} ��� 
     * {@link #cancelEdit}, ����� ���� <code>beginEditMode</code> ����� <code>true</code>,
     * ��� ��������� � ��������� LOADED,MANMADE ��� UPDATED
     * ��������� � ��������� ���� �����
     *
     */
    protected void resetRowVersions() {
        if (state.isLoaded()) {
            state.setOriginalRow(null);
            state.setUpdatingRow(null);
        } else if (state.isUpdated() || state.isManMade()) {
            state.setUpdatingRow(null);
        }
        state.beginEditMode = false;
    }

    /**
     * �������� ������� ��������� � ��������������� ���������� ���� � ��������� 
     * �� ���������.
     * 
     *  <p>���� ������������ ��������� ���� ��������, � ��������� ���� �� �����
     *  UPDATING � �� ����� INSERTING, �� �������� <code>originalRow</code> � 
     *  <code>updatingRow</code> ��������������� � <code>null</code>. 
     *  <code>beginEditMode</code> ��������������� � <code>false</code> � �����
     *  �����������.
     *  <p><b>����������.</b> ������������ ���� �� ����� ���� �������� ��� ����,
     *    ������������ � ��������� DETACHED.
     * 
     * <p>��� ������� ���������, ������� ���� �� ������ ������ ����� ������:
     *    <ul>  
     *      <li>INSERTING  - ����������� ��������� DETACHED. ��� ������ ���� ������
     *                       �� �������.
     *      </li>     
     *
     *      <li>UPDATING   - ����� ��������� ���� � ���� ������ ������� ��� �� 
     *              �������� �������� <code>originalRow</code>, ��� � �� 
     *              �������� �������� <code>updatingRow</code>. 
     * 
     *              ���� <code>originalRow</code> �� <code>null</code> � 
     *                <code>updatingRow</code> �� <code>null</code>, �� ����� 
     *                ���������� ���������� <code>UPDATED</code>. 
     *                ������ <code>updatingRow</code> ���������� � ������ ���. 
     *              ���� <code>originalRow</code> �� <code>null</code>, � 
     *                <code>updatingRow</code> ����� <code>null</code>, �� ����� 
     *                ���������� ���������� <code>LOADED</code>.
     *                ������ <code>originalRow</code> ���������� � ������ ���.
     *              ���� <code>originalRow</code> ����� <code>null</code>, 
     *                ��, ���������� �� <code>updateingRow</code>
     *                ����� ���������� ���������� <code>MANMADE</code>. 
     *                ������ <code>updatingRow</code> ���������� � ������ ���.
     *      </li> 
     *   </ul>
     * �������� <code>updatingRow</code>  � ����� ���������� ������ ����������� 
     * ��������  <code>null</code>.
     * �������� <code>beginEditRow</code> ����������� �������� <code>false</code>.
     * @see #setValue(Object,int)
     * @see #endEdit
     */
    @Override
    public void cancelEdit() {

        if (!state.isEditing() && state.beginEditMode) {
            resetRowVersions();
            notifyTableOf(CANCELEDIT_RESET_ROW_VERSIONS);            
            return;
        }

        if (!state.isEditing()) {
            return;
        }
        if (state.isInserting()) {
            // INSERTING/NONE ��� ����� �������� ��� Table.isLoading
            // => ������� ��������� ��� � ��� ���������
            state.setEditingState(DETACHED);
            state.setOriginalState(DETACHED);

            state.beginEditMode = false;
            notifyTableOf(CANCELEDIT_INSERTING);
            return;
        }

        if (state.isUpdating()) {

            DataRow oRow;
            //if (state.getOriginalRow() != null) {
            if (state.getOriginalState() == LOADED) {
                if (state.getUpdatingRow() != null) {
                    oRow = state.getUpdatingRow();
                    state.setUpdatingRow(null);
                    state.setEditingState(UPDATED);
                } else {
                    oRow = state.getOriginalRow();
                    state.setOriginalRow(null);
                    state.setEditingState(LOADED);
                }
            } else {
                // ��� ����� ��� �������� � ��������� MANMADE
                state.setEditingState(MANMADE);
                oRow = state.getUpdatingRow();
            }

            copyFrom(oRow);
            state.setUpdatingRow(null);

            state.beginEditMode = false;

            notifyTableOf(CANCELEDIT);
        }
    }

    /**
     * ��������� ������� �� ��������� ���� ��� ��� ���������.
     * 
     * @param cause �������, �� ������� ����������� ����������
     * @see tdo.Table#fireRowEditing(int, tdo.DataRow) 
     */
    private void notifyTableOf(int cause) {
        rowServices.processRowEditing(cause, this, -1);
    }

    /**
     * ��������� ������� �� ��������� ������� ����.
     * 
     * @param cause �������, �� ������� ����������� ����������
     * @param columnIndex �������, ��������� � ����������
     * 
     * @see tdo.Table#fireRowEditing(int, tdo.DataRow, int) 
     */
    private void notifyTableOf(int cause, int columnIndex) {
        rowServices.processRowEditing(cause, this, columnIndex);
    }

    /**
     * ��������� ��� �� ��������� DETACHED � ��������� LOADED ��� MANMADE.
     * 
     * ���� ������� ��������� ���� �� ����� DETACHED, �� �������� �� ������������.
     * <p>���� ����� ������� {@link tdo.Table#isLoading() } ���������� 
     * <code>true</code>, �� ��� ����������� � ��������� LOADED. � ��������� 
     * ������, ����� ���������� ���������� MANMADE.
     * 
     * ����� ������������ ��� ����������� �������������.
     * �� ������� ������������ � ����������. ������-���������� ����� 
     * �������������� � ������������ �����, ������� �������� �� ����������
     * <code>tdo.Table</code>.
     * @see #attachNew
     */
    @Override
    public void attach() {
        if (state.isDetached()) {
            if (rowServices.isLoading()) {
                state.setEditingState(LOADED);
                state.setOriginalState(LOADED);
            } else {
                state.setEditingState(MANMADE);
                state.setOriginalState(MANMADE);
            }
        }
        notifyTableOf(ATTACH);        
    }

    /**
     * ��������� ��� �� ��������� DETACHED � ��������� INSERTING.
     * 
     * ���� ������� ��������� ���� �� ����� DETACHED, �� ������������� ����������
     * {@link tdo.DataOperationException}.
     * <p>���� ����� ������� {@link tdo.Table#isLoading() } �� ������������� 
     * ���������� {@link tdo.DataOperationException}.
     * <p>����� ��������� ���� ���������� INSERTING, � �������� �������� 
     * <code>originalState</code> ��������������� ������ MANMADE.
     * 
     * <p>����� ������������ ��� ����������� �������������.
     * �� ������� ������������ � ����������. ������-���������� ����� 
     * �������������� � ������������ �����, ������� �������� �� ����������
     * <code>tdo.Table</code>.
     * @see #attach
     */
    @Override
    public void attachNew() {
        if (!state.isDetached()) {
            throw new DataOperationException("attachNew() : The row must be detached");
        }

        if (rowServices.isLoading()) {
            throw new DataOperationException("attachNew() : The table cannot be in loading state");
        }
        state.setEditingState(INSERTING);
        state.setOriginalState(MANMADE);
        notifyTableOf(ATTACHNEW);
    }

    /**
     * ������������� ����� �������� ������������ ����������� ��������� ����� ����.
     * 
     * ����� ����� ���� ������� � ������, ��������, ����� ��� ������� 
     * ������������ � <code>javax.swing.JTable</code> � �� �����, �����
     * ��������� ���� �� ����� ���� ������� ��� ��������������.
     * @param readOnly ����� �������� ����������
     */
    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * ���������� �������� ������������ ����������� ��������� ����� ����.
     * 
     * ����� ����� ���� ������� � ������, ��������, ����� ��� ������� 
     * ������������ � <code>javax.swing.JTable</code> � �� �����, �����
     * ��������� ���� �� ����� ���� ������� ��� ��������������.
     * 
     * @return <code>true</code> ���� �������� ����� ���� �� ����� ����������.
     *   <code>false</code> � ��������� ������.
     */
    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * ���������� ��������� �������, ������������ ��������� ����.
     * 
     * @return ��������� ���� <code>tdo.RowState</code>
     * @see {@link tdo.RowState}
     * @see {@link #DefaultRowState}
     */
    @Override
    public RowState getState() {
        return state;
    }

    /**
     * ���������� �������� �� ��������� ������� �������.
     * ����� ���������� ���������� ���������� {@link tdo.DataCellProvider }
     * @param columnIndex ������ �������
     * @return �������� ������ ����, ��������������� ��������� ������� �������
     */
    @Override
    public Object getValue(int columnIndex) {
        if (rowServices.isCalculated(columnIndex)) {
            return rowServices.calculateColumnValue(this, columnIndex);
        }
        return cells.getValue(columnIndex);
    }

    /**
     * ���������� �������� �� ��������� ����� �������.
     * ����� ���������� ���������� ���������� {@link tdo.DataCellProvider }     
     * @param columnName ��� �������
     * @return �������� ������ ����, ��������������� ��������� ����� �������
     */
    @Override
    public Object getValue(String columnName) {
        int columnIndex = rowServices.getColumnIndex(columnName);
        if (rowServices.isCalculated(columnIndex)) {
            return rowServices.calculateColumnValue(this, columnIndex);
        }
        return cells.getValue(columnName);
    }

    /**
     * 
     * �������� �������� ����. 
     * 
     * ���� ��� ��������� � ��������� DETACHED ��� DELETED, �� ���������� ������
     * �����������. 
     * <p>��������� �������� ������� �� �������� ��������� ����.
     * <p>���� ������� ��������� INSERTING, �� ����� ���������� ���������� 
     * DETACHED.
     * <p>���� ������� ��������� LOADED, �� ����� ���������� ���������� 
     * DELETED. 
     * <p>���� ������� ��������� MANMADE, �� ����� ���������� ���������� 
     * DELETED. 
     * <p>���� ������� ��������� UPDATING, �� ����� ���������� ���������� 
     * DELETED, � ������� ��� ����������������� �� <code>updatingRow</code>.
     * <p>� ����������, ���� ����������� ������������ ��� � ��������� �� 
     * ��������, ��:
     * <p>���� <code>originalState</code> LOADED ��� MANMADE, �� ������� ���
     *    �������� ��������� ������.
     * <p>���� <code>originalState</code> ����� UPDATED, �� ��� ��������, 
     *   ��� ��������� ���������� ���� ���� LOADED, ��� ������� � ������� ���
     *   �������� ������, ������� ���� �� ������ ��������. ����� ������������
     *   ����� ��������� ��������� �� ����������� <code>originalRow</code>. 
     */
    @Override
    public void delete() {
        if (state.isDeleted()) {
            return;
        }
        if (state.isDetached()) {
            throw new DataOperationException("delete{} method can't be applied to a row in DETACHED state");
        }
        int oldState = state.getEditingState();
        if (state.isEditing()) {
            cancelEdit();
            if (oldState == DefaultRowState.INSERTING) {
                return;
            }
            //My 06.03.2012oldState = state.getEditingState();
        }

        //chg 27.05.2008state.setOriginalState(oldState);
        state.setEditingState(DELETED);
        notifyTableOf(DELETE);
    }

    /**
     * �������� ������������ ��������� ����.
     * 
     * <p>������������ <i>����������</i> 
     * ������ ��� ������������� ���������� ��������� �������:
     *  <ul>
     *     <li>������� �������� ������������ <code>beginEditMode</code> �����������
     *         � <code>false</code>, �.�. ������������ <i>���������</i>.</li>
     *     <li>�������� <code>editingState</code> �������� ���� �� ��������� �������� 
     *         <i>LOADED,MANMADE,UPDATED</i>. ����� ������ �������� ��������� �����.</i>.
     *     </li>
     *     <li>����� {@link tdo.Table#isLoading}</li> ���������� 
     *          <code>false</code>
     *      </li>
     *     <li>����� {@link tdo.Table#isEditProhibited}</li> ���������� 
     *          <code>false</code>
     *      </li>
     *  </ul>
     * 
     * <p>���� ��� �� ��������� ���� ������� ���������, �� <i>������������</i>
     * ����������, �.�. <code>beginEditMode</code> ��������������� � 
     * <code>true</code>. ���������� ���������� ����� {@link #updateRowVersions},
     * ������� ��������� �������� �� ���������� ������� ������ ����. 
     * <p>�������, ��� ����� <code>beginEdit()</code> �� ������ �������� 
     * ���������  <code>state.editingState</code>.
     * @see #updateRowVersions
     */
    @Override
    public void beginEdit() {

        if (state.isEditing()) {
            return;
        }

        if (state.isDetached()) {
            return;
        }
        if (rowServices.isLoading()) {
            return;
        }
        if (state.beginEditMode) {
            return;
        }
        if (this.rowServices.isEditProhibited()) {
            return;
        }
        if (state.isDeleted()) {
            return;
        }

        state.beginEditMode = true;

        updateRowVersions();
        notifyTableOf(BEGINEDIT);

    }

    /**
     * ������������� ������ ����, ����������������� �������� ������� � ��������
     * ��������.
     *  ����� �� ���������� ������� ��������, ���� ��������� ���� �� ���� 
     * �� �������:
     * <ol>
     *   <li>
     *      ������� � �������� �������� ���������� ��� <code>readOnly</code>
     *   </li>
     *   <li>��� ��������� � ���������, �������� �� DETACHED, � �������� 
     *       {@link tdo.Table#isEditProhibited} �������, � �������
     *       ����������� ���, ���������� <code>true</code>.
     *   </li>
     *   <li>��� ��������� � ��������� DELETED</li>
     *   <li></li>
     * </ol>
     * 
     * <p>��� ����� ��������� ���������� <code>tdo.impl.ValidateException</code>
     * �, �������������, ��������� ���������� ������, �� ��������� �����-���� 
     * ��������. ���������� �������������, ���� ������� <code>columnIndex</code> 
     * ���������� ��� �� ����������� <code>null</code>, � �������� ��������� 
     * <code>value</code> ����� <code>null</code>.
     * 
     * <p>���� ��������� ���� ����� ���� ���������, �� ����� �����������
     * ������ �������� ������ ���� ����� ���� ��������� �������� �� ���������
     * <i>������������</i> ���� ������� ������ {@link #updateRowVersions}. �����
     * ������ ����������, ���� ������������ ��������� ��������� �������:
     * <ul>
     *   <li>
     *      ������� ��������� ���� �� �������� DETACHED, UPDATING,INSERTING;
     *   </i>
     *   <li>
     *      ����������� ���� ��������� (beginEditMode == false ).
     *   </i>
     * </ol>
     *
     *  <p>����� <code>setValue</code>, ������ ����� �������� ������, ��������� 
     * �������� �� ��������� ������������, ��������� ���������� ����������� 
     * ������ {@link #updateRowVersions}.
     * 
     * <p>����� ��������� ������ �������� ������ ���� ����������� ����������
     * �������� ��������� ���� ������� ������ {@link #updateState}.
     *     
     * @param value ����� �������� ������ ����
     * @param columnIndex ������ �������, ��� ������� ������������ ������ ����
     * @see #setValue(java.lang.Object, java.lang.String) 
     * @throws tdo.impl.ValidateException ������������ ��� ������ ������� ��� � 
     *          �������  <code>updateState</code>.
     * 
     * @see #updateRowVersions
     * @see #updateState
     */
    @Override
    public void setValue(Object value, int columnIndex) {

        if (!rowServices.isCellEditable(columnIndex)) {
            return;
        }
        if (rowServices.isEditProhibited() && !state.isDetached()) {
            return;
        }
        if (state.isDeleted()) {
            return;
        }
        if (!state.isDetached()) {
            String columnName = rowServices.getColumnName(columnIndex);
            boolean columnValid = rowServices.validate(this, columnName, value);

            if (!columnValid) {
                return;
            }
        }

        if (!(state.isDetached() || state.isEditing() || state.beginEditMode)) {
            updateRowVersions();
        }
        Object v = null;
        if (value != null) {
            v = rowServices.toColumnType(value, columnIndex);
        }

        cells.setValue(v, columnIndex);

        if (!state.isDetached()) {
            this.updateState(columnIndex);
        }
    } //method setValue

    /**
     * ��������� ������� ��������� ���� ����� ��������� ������ ����.
     * 
     * <p>����� ����� ��������� ���������� {@link tdo.impl.ValidateException}.
     * �������� ���������� ���������� ������, ���� <b><i>��</i><b> ����k���� 
     * �� ���� �� �������:
     * 
     * <ol>
     *    <li>
     *       �������, ������� ����������� ��� ��������� � ��������� LOADED;
     *    </li>
     *    <li>
     *       ������� ���������� ���� �������� INSERTING;
     *    </li>
     *    <li>
     *       �������� ������������ ��������� ���� ( beginEditMode == true )
     *    </li>
     * </ol>
     * 
     * 
     * <p> ���� <i>������������ ����������</i> <b>���������</b>, �� ������
     * ���������� ������ <code>setValue</code> ��������� ��� �� �������� ��������� 
     * � ����� �� ��������, ����������� ����:
     * <ul>
     *      <li>DETACHED - <i>�� ����������</i></li>
     *      <li>LOADED - <i>UPDATED</i></li>
     *      <li>INSERTING - <i>�� ����������</i></li>
     *      <li>MANMADE  - <i>�� ����������</i></li>
     *      <li>UPDATING  - <i>UPDATED</i></li>
     *      <li>UPDATED   - <i>�� ����������</i></li>
     * </ul>
     * 
     * <p> ���� <i>������������ ����������</i> <b>��������</b>, �� ������
     * ���������� ������ <code>setValue</code> ��������� ��� �� �������� �������� 
     * � ����� �� ��������, ����������� ����: 
     * <ul>
     *      <li>LOADED - <i>UPDATING</i></li>
     *      <li>INSERTING - <i>�� ����������</i></li>
     *      <li>MANMADE  - <i>UPDATING</i></li>
     *      <li>UPDATING  - <i>�� ����������</i></li>
     *      <li>UPDATED   - <i>UPDATING</i></li>
     * </ul>
     * @param columnIndex ������� � ���������� ���������
     * @throws tdo.impl.ValidateException
     * @see #setValue(Object,int)
     */
    protected void updateState(int columnIndex) {
        if (rowServices.isLoading()) {
            if (state.originalState == LOADED && state.originalRow != null) {
                state.setEditingState(UPDATED);
            }
            return;
        }
        if (state.isInserting()) {
            notifyTableOf(SETVALUE_INSERTING, columnIndex);
            return;
        }
        if (state.beginEditMode) {
            state.setEditingState(DefaultRowState.UPDATING);
            notifyTableOf(SETVALUE, columnIndex);
            return;
        }

        //01.08 rowServices.fireValidate(this);

        // ���������� ��� => ����� ��������� � ��������
        // ��������� UPDATED
        if (!state.isManMade()) {
            state.setEditingState(UPDATED);
        }

        notifyTableOf(SETVALUE, columnIndex);

    }

    /**
     * ������������� ������ ����, ����������������� ������ ������� � ��������
     * ��������.
     * ����� ���������� ���������� �������������� ������ {@link #setValue(Object,int)}.
     * @param value ����� �������� ������ ����
     * @param columnName ��� �������, ��� ������� ������������ ������ ����
     * @see #setValue(java.lang.Object, int) 
     */
    @Override
    public void setValue(Object value, String columnName) {
        int columnIndex = rowServices.getColumnIndex(columnName);

        setValue(value, columnIndex);
    }

    /**
     *  ��������� �������������� ����.
     *
     * <p>���� ��� ��������� � ��������� DETACHED, �� ����� �����������.
     *
     * <p>���� ��� ��������� � ��������� LOADED, MANMADE ��� UPDATED ,
     * �� <code>originalRow</code> � <code>updatingRow</code> ��������������� �
     * <code>null</code>. <code>beginEditMode</code> ��������������� �
     * <code>false</code>.
     *
     * <p>��� ����, ������������ � ��������� INSERTING ��� UPDATING �����������
     * ����� {@link tdo.Table#validate(tdo.DataRow, boolean) } , �������
     * ���������� ������� ��������, ������������ �������� �� ��� ������ ���������.
     * ���� ������������ <code>false</code>, �� ����� �����������,�� �������
     * ������� ��������.
     * <p>���� ��� ������� ������ ��������, �� ��� ������� ���������, ������� ����
     * �� ������ ������ ����� ������:
     *    <ul>
     *      <li>INSERTING  - ����������� ��������� MANMADE. �������� ��������
     *                       {@link tdo.DefaultRowState#updatingRow}
     *                       ��������������� � <code>null</code>.
     *      </li>
     *
     *      <li>UPDATING   - ����� ��������� ���� � ���� ������ ������� �� ��������
     *                       �������� <code>originalRow</code>. ���� ��������
     *                       <code>null</code>, �� ��� ��������, ��� ���������
     *                       ���������� ���� ���� MANMADE. ������� ����� ���������
     *                       ���������� MANMADE. � ��������� ������ ������ ���������
     *                       ������������� �������� UPDATED.
     *                       �������� ��������
     *                       {@link tdo.DefaultRowState#updatingRow}
     *                       ��������������� � <code>null</code>.
     *      </li>
     *   </ul>
     */
    @Override
    public void endEdit() {
        this.endEdit(false);
    }
    /**
     *  ��������� �������������� ����, �������� ��� ��� ��������� ����.
     * <p>���� �������� ��������� ������� �������� <code>true</code>, �� ������,
     * ��� ������ ����� - ��� ��������� ��������� ����, ���������� ����������
     * ���� {@link tdo.impl.ValidateException }, ���� ���������� ������, ���
     * ����� �������� ���������� ������. ���� ������ �� ���������� ��� ��������
     * ��������� ����� <code>false</code> ��:
     *
     * <p>���� ��� ��������� � ��������� DETACHED, �� ����� �����������.
     *
     * <p>���� ��� ��������� � ��������� LOADED, MANMADE ��� UPDATED ,
     * �� <code>originalRow</code> � <code>updatingRow</code> ��������������� �
     * <code>null</code>. <code>beginEditMode</code> ��������������� �
     * <code>false</code>.
     *
     * <p>��� ����, ������������ � ��������� INSERTING ��� UPDATING � ��������
     * ��������� <code>forceValidate</code> ����� <code>false</code> �����������
     * ����� {@link tdo.Table#validate(tdo.DataRow, boolean) } , �������
     * ���������� ������� ��������, ������������ �������� �� ��� ������ ���������.
     * ���� ������������ <code>false</code>, �� ����� �����������, �� �������
     * ������� ��������.
     * <p>���� ��� ������� ������ ��������, �� ��� ������� ���������, ������� ����
     * �� ������ ������ ����� ������:
     *    <ul>
     *      <li>INSERTING  - ����������� ��������� MANMADE. �������� ��������
     *                       {@link tdo.DefaultRowState#updatingRow}
     *                       ��������������� � <code>null</code>.
     *      </li>
     *
     *      <li>UPDATING   - ����� ��������� ���� � ���� ������ ������� �� ��������
     *                       �������� <code>originalRow</code>. ���� ��������
     *                       <code>null</code>, �� ��� ��������, ��� ���������
     *                       ���������� ���� ���� MANMADE. ������� ����� ���������
     *                       ���������� MANMADE. � ��������� ������ ������ ���������
     *                       ������������� �������� UPDATED.
     *                       �������� ��������
     *                       {@link tdo.DefaultRowState#updatingRow}
     *                       ��������������� � <code>null</code>.
     *      </li>
     *   </ul>
     */
    @Override
    public void endEdit(boolean throwException) {
        notifyTableOf(ENDEDIT_BEFORE);
        if ((!state.isEditing()) && state.beginEditMode) {
            resetRowVersions();
            notifyTableOf(ENDEDIT_RESET_ROW_VERSIONS);
            return;
        }

        if (!state.isEditing()) {
            return;
        }

        if (rowServices.isValidationEnabled() && rowServices.getValidationManager() != null &&
             ! rowServices.getValidationManager().validate(this, throwException) )
            return;

        int oldEditingState = state.getEditingState();
        int newEditingState = MANMADE;

        if (state.isInserting()) {
            state.setEditingState(MANMADE);
        }

        if (state.isUpdating()) {
            //if (state.getOriginalRow() == null) {
            if (state.getOriginalState() == MANMADE) {
                state.setEditingState(MANMADE);
                oldEditingState = UPDATING;
            } else {
                state.setEditingState(UPDATED);
                oldEditingState = UPDATING;
                newEditingState = UPDATED;
            }
        }

        state.setUpdatingRow(null);

        state.beginEditMode = false;

        if (oldEditingState == DefaultRowState.INSERTING && newEditingState == DefaultRowState.MANMADE) {
            notifyTableOf(ENDEDIT_INSERTING);
        } else {
            notifyTableOf(ENDEDIT);
        }
    }

    /**
     * ���������� ������ ���� � �������.
     * 
     * @return ������ ���� � �������
     * @see tdo.Table#find(tdo.DataRow) 
     */
    @Override
    public int getIndex() {
        return rowServices.getRowIndex(this);
    }

    /**
     * ���������� ������� �������,  ��� �������� ������ ���.
     * 
     * @return ��������
     * @see tdo.service.TableServices
     */
    @Override
    public TableServices getContext() {
        return this.tableServices;
    }

    @Override
    public <T> T getObject() {
        return (T)this.cells.getObject();
    }

    @Override
    public boolean validateColumn(String columnName, Object value) {
        return rowServices.validate(this, columnName, value);
    }

    @Override
    public boolean validate() {
        return rowServices.validate(this, false);
    }

    protected static class DefaultRowState implements RowState {

        private int editingState = LOADED; // default state
        public boolean beginEditMode;
        private int depth;
        /**
         * ������ ������ �� ������������ ���. <p>
         */
        private DataRow originalRow;
        /**
         * ������ ������ �� ���, ����������� � ��������� MANMADE ��� UPDATED
         * � ������������� � ��������� UPDATING. <p>
         */
        private DataRow updatingRow;
        /**
         * ������ �������� LOADED, MANMADE ��� DETACHED.
         */
        private int originalState;
        /**
         * ������ ������ �� ������� ���. <p>
         */
        private DataRow row;
        /**
         * ������������ ��� ��������� ���������, ������� ����� ���� �������,
         * �������� ��� ���������� � ����� ������.
         */
        private String message;

        /**
         * ����������� ��� ����������. <p>
         * @param forRow
         */
        public DefaultRowState(DataRow forRow) {
            this(DefaultRowState.LOADED, forRow);
        }

//  public RowState(IPDataStore dataStore,int rowIndex) {
        protected DefaultRowState(int editingState) {
            this(editingState, null);
        }

        protected DefaultRowState(int editingState, DataRow rowRef) {
            this.editingState = editingState;
            this.message = null;
            this.originalRow = null;
            this.depth = -1;
            this.beginEditMode = false;

        }

        @Override
        public int getOriginalState() {
            return this.originalState;
        }

        public void setOriginalState(int newState) {
            this.originalState = newState;
        }

        @Override
        public int getEditingState() {
            return this.editingState;
        }
        //int editingCount;

        public void setEditingState(int editingState) {
            switch (editingState) {
                case INSERTING:
                case UPDATING:
                    break;
                case MANMADE:
                case UPDATED:
                case DELETED:
                case LOADED:
                case DETACHED:
                    this.beginEditMode = false;
                    break;

            } //switch

            this.editingState = editingState;
        }

        @Override
        public boolean isDetached() {
            return editingState == DETACHED;
        }

        @Override
        public boolean isLoaded() {
            return editingState == LOADED;
        }

        @Override
        public boolean isEditing() {
            return isInserting() || isDeleting() || isUpdating();
        }

        @Override
        public boolean isInserting() {
            return editingState == INSERTING ? true : false;
        }

        @Override
        public boolean isUpdating() {
            return editingState == UPDATING ? true : false;
        }

        @Override
        public boolean isDeleting() {
            return editingState == DELETING ? true : false;
        }

        @Override
        public boolean isEdited() {
            return isManMade() || isDeleted() || isUpdated();
        }

        @Override
        public boolean isManMade() {
            return editingState == MANMADE ? true : false;
        }

        @Override
        public boolean isUpdated() {
            return editingState == UPDATED ? true : false;
        }

        @Override
        public boolean isDeleted() {
            return editingState == DELETED ? true : false;
        }

        /**
         * ���������� ������ ������������ ���. <p>
         * @return <code>DataRow</code>
         */
        @Override
        public DataRow getOriginalRow() {
            return this.originalRow;
        }

        /**
         * ������������� ������ �� ������������ ���.  <p>
         * @param row
         */
        public void setOriginalRow(DataRow row) {
            this.originalRow = row;
        }

        /**
         * ���������� ������ �� ���  ����������� � ��������� MANMADE ��� UPDATED
         * � ������������� � ��������� UPDATING. <p>
         * @return <code>DataRow</code>
         */
        @Override
        public DataRow getUpdatingRow() {
            return this.updatingRow;
        }

        /**
         * ������������� ������ �� ���  ����������� � ��������� MANMADE ��� UPDATED
         * � ������������� � ��������� UPDATING. <p>
         *
         * @param row
         */
        public void setUpdatingRow(DataRow row) {
            this.updatingRow = row;
        }

        /**
         * ���������� ������ ������� ���. <p>
         * @return <code>DataRow</code>
         */
        @Override
        public DataRow getRow() {
            return this.row;
        }

        /**
         * ������������� ������ �� ������� ���.  <p>
         * @param row
         */
        protected void setRow(DataRow row) {
            this.row = row;
        }

        /**
         * getter-����� �������� <code>messsage</code>.<p>
         * @return ��� <code>String</code>
         * @see message
         * @see setMessage
         */
        @Override
        public String getMessage() {
            return this.message;
        }

        /**
         * setter-����� �������� <code>messsage</code>.<p>
         * @param message ��� <code>String</code>
         * @see message
         * @see getMessage
         */
        @Override
        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString(int edstate) {
            String s = "";
            switch (edstate) {
                case LOADED:
                    s = "UNCHANGED";
                    break;
                case MANMADE:
                    s = "INSERTED";
                    break;
                case INSERTING:
                    s = "INSERTING";
                    break;
                case UPDATED:
                    s = "UPDATED";
                    break;
                case UPDATING:
                    s = "UPDATING";
                    break;
                case DELETING:
                    s = "DELETING";
                    break;
                case DELETED:
                    s = "DELETED";
                    break;
                case DETACHED:
                    s = "DETACHED";
                    break;
            }

            return s;
        }

        @Override
        public String toString() {
            String s;//My 06.03.2012 = "";
            s = "editingState=" + toString(editingState) + "; ";
            s += "originalState=" + toString(originalState) + "; ";
            s += "beginEditMode=" + beginEditMode + "; ";
            s += "message='" + message + "'; ";
            s += "; originalRow=" + (originalRow == null ? "null" : "PRESENT");
            return s;
        }

        @Override
        public int getDepth() {
            return depth;
        }

        @Override
        public void setDepth(int depth) {
            this.depth = depth;
        }

        @Override
        public void copyFrom(RowState source) {
            this.editingState = source.getEditingState();
            this.originalState = source.getOriginalState();
            this.beginEditMode = ((DefaultRowState) source).beginEditMode;
            this.depth = source.getDepth();
            this.message = source.getMessage();
        }

        @Override
        public boolean isBeginEditMode() {
            return this.beginEditMode;
        }

        @Override
        public void columnAdded(int columnIndex) {
            if (!isLoaded()) {
                if (getOriginalRow() != null) {
                    getOriginalRow().getCellCollection().columnAdded(columnIndex);
                }
                if (getUpdatingRow() != null) {
                    getUpdatingRow().getCellCollection().columnAdded(columnIndex);
                }

            }

        }

        @Override
        public void columnRemoved(int columnIndex) {
            if (!isLoaded()) {
                if (getOriginalRow() != null) {
                    getOriginalRow().getCellCollection().columnRemoved(columnIndex);
                }
                if (getUpdatingRow() != null) {
                    getUpdatingRow().getCellCollection().columnRemoved(columnIndex);
                }
            }
        }

        @Override
        public void columnMoved(int columnIndex, int oldCellIndex, int newCellIndex) {
            if (!isLoaded()) {
                if (getOriginalRow() != null) {
                    getOriginalRow().getCellCollection().columnMoved(columnIndex, oldCellIndex, newCellIndex);
                }
                if (getUpdatingRow() != null) {
                    getUpdatingRow().getCellCollection().columnMoved(columnIndex, oldCellIndex, newCellIndex);
                }
            }
        }
    }
}//DefaultDataRow
