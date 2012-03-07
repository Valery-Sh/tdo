package tdo.impl;

/*
 * Aggregator.java
 *
 * Created on 21 ������ 2006 �., 10:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import tdo.*;
import tdo.util.Strings;

/**
 * ������������� ����������� �������� ������ ������� ���� {@link Table}, �����������
 * �������������� ������ ��������� ������� ���� <code>Table</code>. <p>
 *
 * ��� ������� ����������� �� ����� ���� ��������� �������� ������. ������ ����� �������
 * ������������ ���� �� ������������� ������� {@link Table#createAggTable}.<p>
 * <b>������ �������������</b><p>
 *
 *  �������� ������� � ������� � �����������
 *   <code>
 *   <pre>
 *
 *  1.     DataTable empl = new DataTable();
 *  2.     empl.addRow( new String[] { ""Branch","Department","LastName","Salary" },
 *                    new Object[] { "South","Accounts","Smith",2000 } );
 *  3.     empl.addRow(  new Object[] {"South", "Accounts","Fisher",3500 } );
 *  4.     empl.addRow(  new Object[] {"North", "IT","Gates",15000 } );
 *  5.     empl.addRow(  new Object[] {"North", "IT","Wood",12000 } );
 *  6.     empl.addRow(  new Object[] { "North", "Accounts","Norton",2500 } );
 *   </pre>
 *   </code>
 *  ������� ����� ��������� ��������� ����:
 *  <code>
 *  <pre>
 *              Branch    Department     LastName    Salary
 *          -----------------------------------------------------
 *  0.          South      Accounts      Smith         2000
 *  1.          South      Accounts      Fisher        3500
 *  2.          North      IT            Gates         15000
 *  3.          North      IT            Wood          12000
 *  4.          North      Accounts      Norton        2500
 *
 *  </pre>
 *  </code>
 *  ������, ����������� ������� �� �������� Department � LastName:
 *  <code>
 *  <pre>
 * 7.     empl.setSort("Branch,Department");
 *  </pre>
 *  </code>
 *
 *  ��� ������������� ������ <code>Aggregator</code> �������� ������� <b>������</b>
 *  ���� �������������. <p>
 *  ��������� ����� <code>Aggregator</code> ��������:
 *  <code>
 *  <pre>
 *
 *  8.     Aggregator agg = new Aggregator(empl);
 *  9.     DataTable aggTable = agg.createTable();
 *  10.    agg.setColumnList("Branch,Department,LastName, SUM(salary) as TotalSalary");
 *  11.    agg.execute();
 *
 *   </pre>
 *   </code>
 *  ������ 8 ������ ���� ������� ��������� ������� ���� <code>Aggregator</code>.
 *  ������� ��������, ��� ����������� ������ ��������� �������� ����
 *  <code>DataTable</code>, ������� � ����� ������ �������� �������� �������
 *   <code>empl</code>.
 *  ������� �����  9 � 10 �������� �� �����. �������� ������ ������������� �
 *  ������ {@link #execute }. ����� ��������� �������� ������� ��� �� ����� �
 *  ��������� ���������� ( ��������� ) ���� � �������������� �������. ���������
 *  ���� ��������� ��� ��������� �������� ��������, �������� � ������ �����
 *  ����������. � ������ ������� "Branch,Department". <p>
 *
 *  ����� �������, ������ �� ������� ��������� � ���������� <code>aggTable</code>
 *  ����������� ���:
 *  <code>
 *  <pre>
 *             Branch           Department      LastName      TotalSalary
 *          ------------------------------------------------------------
 *   0.                                                         35000
 *   1.         North                                           29500
 *   2.         North            Accounts                       2500
 *   3.         North            IT                             27000
 *   4.         South                                           5500
 *   5.         South            Accounts                       4500
 *   6.                                                         35000
 *
 *  </pre>
 *  </code>
 *  �����, ��� �������������� ������� �������� �� 2 ���� ������ ��������.
 *  ���� � ��������� 0 � 6 ( ����� ������ � ����� ��������� ) ��������
 *  "���������"  ��� "totals" ������. � ���� ����� ��������� ������ ������,
 *  ��������������� "����������" ��������, �.�. �������� ������� � ������
 *  ������ {@link #setColumnList } ������������ ����� �������������
 *  ���������� �������. � ����� ������ - ���  ������� � ������ Salary,
 *  ��� ������� �� ������������ ���������� ������� SUM(salary).
 *  �������� �������, ���������� ��� ����� �������� �����
 *  ��������� �������� <code>totalsPosition</code>.
 *  ��� setter-����� {@link #setTotalsPosition} ����� ��������� ��� ��������
 *  ���� �� ��������:
 *  {@link #TOTALS_TOP}, {@link #TOTALS_BOTTOM}, {@link #TOTALS_BOTH}. <p>
 *  ��� � �������� 1 - ��� ���������� ��� ������ 1, �.�. ������������� �����
 *  ������ ������� ������ �����, �������������� ��� ���������� �������� �������.
 *  � ����� ������� - ��� ������� � ������ <code>Branch</code>.
 *  ���� ��� �������� � ������� TotalSalary ����� �������� ������� Salar� ���
 *  ����� � ������� Branch �������, ���������� �������� "North". ��� ���������
 *  �������� � ���� 4. <p>
 *  ���� � ��������� 2 � 3  - ��� ���������� ���� ������ 2. � ��� �����. <p>
 *  ������� ��������, ��� ������� <code>LastName</code> �� ���� ������� ������.
 *  ��� ������, ��� �� ������� �� � ������ ������� �������������� �������, �� ���
 *  �� ������ � ������ ������� ���������� � � ��� �� ��������� ������� ����������
 *  �������. ����� ������� <code>Aggregator</code> ������� �������, ��������� ��
 *  ������ �� ���������� �����, �� � �� "���������" ����� �������� �������. ��� �����
 *  ������������ ��� ���, ��� �������� ���� (����� ������ ��������):
 * <code>
 * <pre>
 *  8.     Aggregator agg = new Aggregator(empl);
 *  9.     DataTable aggTable = agg.createTable();
 *  10.    agg.setColumnList("Branch,Department,LastName, SUM(salary) as TotalSalary");
 *  11     <b>agg.setIncludeDetailRows(true);</b>
 *  12.    agg.execute();
 * </pre>
 * </code>
 *
 *  ������� ��������� �������, ���������� ��� �������������� ����, ��� � ���������.
 *  <code>
 *  <pre>
 *             Branch           Department      LastName      TotalSalary
 *          ------------------------------------------------------------
 *   0.                                                         35000
 *   1. <b>     North                                           29500 </b>
 *   2.      North            Accounts                       2500
 *   3. <i>     North            Accounts       Norton          2500 </i>
 *   4.      North            IT                             27000
 *   5. <i>     North            IT             Wood            12000 </i>
 *   6. <i>     North            IT             Gates           15000 </i>
 *   7. <b>     South                                           5500  </b>
 *   8.      South            Accounts                       5500
 *   9.  <i>    South            Accounts       Smith           2000 </i>
 *   10. <i>    South            Accounts       Fisher          3500 </i>
 *   11.                                                         35000
 *
 *  </pre>
 *  </code>
 *
 *  �� ��� ��� �� ������������� �������� �������������� ������� � ������, ����� ��������������
 *  ���� ���������� <i>������</i>, ��� ���������, � ������ ������� ������� ���������� ���,
 *  ��������������� ������ ������� �� ������ ����������, ����� ���, ��������������� ������ �������
 *  � .�.�. ����� ��������� �������� ���� �������. ��� ����� ������������� �������
 *  {@link setGroupMode } �� ��������� ��������� ������  {@link AFTER} ( �� ���������, ��������
 *  <code>groupMode</code> ����� {@link AFTER}.
 *  <code>
 *  <pre>
 *  8.     Aggregator agg = new Aggregator(empl);
 *  9.     DataTable aggTable = agg.createTable();
 *  10.    agg.setColumnList("Branch,Department,LastName, SUM(salary) as TotalSalary");
 *  11     agg.setIncludeDetailRows(true);
 *  12.    agg.setGroupMode(Aggregator.AFTER);
 *  13.    agg.execute();
 *  </pre>
 *  </code>
 *
 *  <code>
 *  <pre>
 *             Branch           Department      LastName      TotalSalary
 *          ------------------------------------------------------------
 *  0.                                                  		35000
 *  1. <i>      North           Accounts        Norton          2500 </i>
 *  2.       North           Accounts                        2500
 *  3. <i>      North           IT              Wood            12000 </i>
 *  4. <i>      North           IT              Gates           15000 </i>
 *  5.       North           IT                              27000
 *  6. <b>      North                                           29500 </b>
 *  7. <i>      South           Accounts        Smith           2000  </i>
 *  8. <i>      South           Accounts        Fisher          3500  </i>
 *  9.       South           Accounts                        5500
 *  10. <b>     South                                           5500  </b>
 *  11.                                                         35000
 *  </pre>
 *  </code>
 *
 *
 * @author valery
 */
public class TreeBuilder {

    protected GroupPart[] groupParts;
    /**
     * ���������, ��� ��������� �������� �������� <code>groupMode</code> ����� �������,
     * ��� ���������� ���� ������� ������ ���������.
     * @see #AFTER
     * @see #getGroupMode
     * @see #setGroupMode
     */
    public static final boolean BEFORE = false;
    /**
     * ���������, ��� ��������� �������� �������� <code>groupMode</code> ����� �������,
     * ��� ���������� ���� ������� ����� ���������.
     * @see #BEFORE
     * @see #getGroupMode
     * @see #setGroupMode
     */
    public static final boolean AFTER = true;
    /**
     * ����� �������� �������� <code>totalsPosition</code> ����� �������� ����� ����,
     * �������� ��� �������������� ������� ������ ����������� ������, �.�. ������ ��������� ����
     * ����� 0.
     * @see #setTotalsPosition
     * @see #TOTALS_BOTTOM
     * @see #TOTALS_BOTH
     * @see #TOTALS_NONE
     *
     */
    public static final int TOTALS_TOP = 1;
    /**
     * ����� �������� �������� <code>totalsPosition</code> ����� �������� ����� ����,
     * �������� ��� �������������� ������� ������ ����������� �����, �.�. ������ ��������� ����
     * ����� <i>(����������-����� - 1).</i>
     * @see #setTotalsPosition
     * @see #TOTALS_TOP
     * @see #TOTALS_BOTH
     * @see #TOTALS_NONE
     *
     */
    public static final int TOTALS_BOTTOM = 2;
    /**
     * ����� �������� �������� <code>totalsPosition</code> ����� �������� ����� ����,
     * �������� ��� �������������� �������  ����������� ��� � �������, �����. ������ ��������� ����
     * ����� 0, ��� � � ������� <i>(����������-����� - 1).</i>
     * @see #setTotalsPosition
     * @see #TOTALS_TOP
     * @see #TOTALS_BOTTOM
     * @see #TOTALS_NONE
     *
     */
    public static final int TOTALS_BOTH = 3;
    /**
     * ����� �������� �������� <code>totalsPosition</code> ����� �������� ����� ����,
     * �������� ���� �������������� �������  �� ���������.
     *
     * @see #setTotalsPosition
     * @see #TOTALS_TOP
     * @see #TOTALS_BOTTOM
     * @see #TOTALS_BOTH
     *
     */
    public static final int TOTALS_NONE = -1;
    /**
     * �������, �������� ��� ��������, ���������� �������������. �������� �����������
     * ������������� ������ ����� ��������.
     *
     * @see #Aggregator(Table)
     */
    protected Table sourceTable;
    /**
     * �������, ��������� ������� ��������� ������� {@link #createTable} - ��� ���������� �������������.
     */
    protected Table targetTable; // this datatable is used as a result dataTable.
    /**
     * ������ ������, ���������� ����������� ������� ������ ������� � (���) ���������� �������.<p>
     * �������� ������ ��������������� ������� {@link #setColumnList}. ��� �� ����������� ���������
     * ������.
     */
    private String columnList;
    private String sortColumnList;
    private boolean sortDirection;
    /**
     * ������ ������, ���������� ����������� ������� ������ ������� � (���) ���������� �������.<p>
     * �������� ������ ��������������� ������� {@link #setColumnList}. ��� �� ����������� ���������
     * ������.
     */
    private ColumnListExpressionContext columnExpressionContext;
    /**
     * ������ ��������, ����������� �������� ��������� ���������� ����� � ��������������
     * �������. <p>
     * ���������� ���������� �������� {@link #BEFORE} - �� ��������� � {@link #AFTER }.
     * ��������������� ������� {@link #setGroupMode }.
     */
    private boolean groupMode = BEFORE;
    /**
     * ������ ��������, ����������� �������� � ����������� �������� ���������� ����� �
     * ��������������  �������. <p>
     * ���������� ���������� �������� {@link #TOTALS_BOTH} - �� ���������, � {@link #TOTALS_TOP } �
     * {@link #TOTALS_BOTTOM}.
     * ��������������� ������� {@link #setTotalsPosition }
     */
    private int totalsPosition = TOTALS_BOTH;
    /**
     * ������ ��������, ����������� ���������� ��������������  ������� ��������� ������ �������� ��� ���. <p>
     * �������� <code>false</code> - �������� �� ��������: ��������� ���� �� ��������.
     *  <code>true</code> - � ��������� ������.
     * ��������������� ������� {@link #setIncludeDetailRows}
     */
    private boolean includeDetailRows;
    /**
     * �������� ������ ���� ������� �������������� (����������) �������, ������ �� �������
     * ������� ����������� ���������� �������. ��� ����� ������ � ������� ��������.
     * ��������, ���� {@link #columnList}, �������� ������� <code><b>MIN(birthDay) AS HerBirhDay</b></code>,
     * �� � ������� ����� ����������� ������� "HERBIRTHDAY". <p>
     * ����� ��������������� ������� ������ <code>getColumns</code> ������ {@link AggContainer}.
     * ������������ ��� ������������� ������ ������ � (���) ��� �����������.
     *
     * @see AggContainer#getColumns
     */
    protected String[] aggColumns;
    protected SortColumnInfo[] sortColumnInfo;
    /**
     * ���������� ������� �������������� (�������������).<p>
     * ����������� ��� ����� ���������� ������� ���������� �������� �������,
     * ����� ���������� ������� � ������������� <code>EXCLUDE</code> ���� 1 ���
     * ������.
     * @see exclCount
     * @see Sorter
     */
   //protected int levelCount;

    /**
     * ������� ����� ��������� ������ ��� �������� �������, ������������ ����������� <p>
     * @param sourceTable �������� �������.
     */
    public TreeBuilder(Table sourceTable) {
        totalsPosition = TOTALS_BOTH;
        this.sourceTable = sourceTable;
        //this.levelCount = -1;
    }
/*    public int getLevelCount() {
        if ( this.levelCount == -1 ) {

        }
    }
 */
    /**
     * ������� ��������� ���� {@link Table}, ������������ ��� ��������������
     * ������� �������������� ������.
     * @return ����� ������ Table.
     */
    public Table createTargetTable() {
        targetTable = sourceTable.createTable();
        createColumnListExpressionContext();
        return targetTable;
    }

    public Table createTargetTreeTable() {
        //25.07 targetTable = sourceTable.createTreeTable();
        targetTable = new TreeDataTable();
        createColumnListExpressionContext();
        return targetTable;
    }

    protected SortColumnInfo[] createSortColumnInfo() {
        
        if (sortColumnList == null) {
            return null;
        }
        sortColumnInfo = this.createSortColumnInfo(sortColumnList);
        for (int i = 0; i < sortColumnInfo.length; i++) {
            sortColumnInfo[i].columnIndex = sourceTable.getColumns().find(sortColumnInfo[i].columnName);
        }
        return sortColumnInfo;
    }

    protected void createColumnListExpressionContext() {
        columnExpressionContext = new ColumnListExpressionContext(columnList);
        // -- ��������� � �������� ��������� sourceTable ��� �����������
        // -- ����������� ����� ���������
        columnExpressionContext.addTableServices(sourceTable.getContext());
        columnExpressionContext.setTargetTable(targetTable);
        columnExpressionContext.createTargetColumns();
        sortColumnInfo = this.createSortColumnInfo();
        columnExpressionContext.setSortColumnInfo(sortColumnInfo);
    }

    protected void createColumnListExpressionContext(Table aTable, String alias) {
        columnExpressionContext = new ColumnListExpressionContext(columnList);
        columnExpressionContext.addTableServices(alias, aTable.getContext());
        // -- ��������� � �������� ��������� sourceTable ��� �����������
        // -- ����������� ����� ���������
        columnExpressionContext.addTableServices(sourceTable.getContext());
        columnExpressionContext.setTargetTable(targetTable);
        columnExpressionContext.createTargetColumns();
        sortColumnInfo = this.createSortColumnInfo();
        columnExpressionContext.setSortColumnInfo(sortColumnInfo);
    }

    public ColumnListExpressionContext getContext() {
        return this.columnExpressionContext;
    }

    /**
     * ������������� ������ ���������� �� �������� ������� �������. <p>
     * ������ columnList - ��� ������ ���������, ����������� �������, ������ �� �������
     * ������������ ����� ���� ��:
     * <UL>
     *   <li><i>���-�������</i>;
     *   <li>MIN(<i>���-�������</i>) [ AS <i>���-�������-1</i> ];</li>
     *   <li>MAX(<i>���-�������</i>) [ AS <i>���-�������-1</i> ];</li>
     *   <li>SUM(<i>���-�������</i>) [ AS <i>���-�������-1</i> ];</li>
     *   <li>COUNT(*) [ AS <i>���-�������-1</i> ];              </li>
     *   <li>LEVEL(*) [ AS <i>���-�������-1</i> ];              </li>
     *   <li><i>���-custom-�������</i>(*) [ AS <i>���-�������-1</i> ];               </li>
     *   <li><i>���-custom-�������</i>(<i>���-�������</i>) [ AS <i>���-�������-1</i> ]</li>
     * </UL>
     * ������ �� ��������� ����� ���� ����� � ����� ��������. ����������� �����
     * ���������� ��������, ����������� ��� ��������, ����� � ����� ���� �������
     * � ����� � ����� ��������� ����� AS. ��������,
     *  "field1   AS f1,  miN(      field2)"
     * @param columnList
     */
    public void setColumnList(String columnList) {
        this.columnList = columnList.toUpperCase();
    }

    public void setSortColumnList(String sortColumns) {
        this.sortColumnList = sortColumns;
    }

    /**
     * ������������� �������� ����������� ���������� �������� �������: ��
     * �������� ��� �� �����������.<p>
     *
     * @param sortDirection ��������� �������� ISorter#ASCENDING ���
     *      ISorter#DESCENDING.
     */
    public void setSortDirection(boolean sortDirection) {
        this.sortDirection = sortDirection;
    }

    /**
     * @return true ���� ��������� ������ �������� ������� ����������� �
     * �������������� (����������) �������. false - � ��������� ������.
     * @see #setIncludeDetailRows
     */
    public boolean getIncludeDetailRows() {
        return this.includeDetailRows;
    }

    /**
     * @param includeDetailRows ���� ����� true - ��������������� �����, �����
     *      ��������� ���� �������� ������� ����������� � ���������� �������.
     *      false ���������, ��� ��������� ���� �� �����������.
     * @see #getIncludeDetailRows
     */
    public void setIncludeDetailRows(boolean includeDetailRows) {
        this.includeDetailRows = includeDetailRows;
    }

    /**
     * ���������� ��������, ������������ ���������  ��� ��� �������� ���� �
     * �������������� ������� �, ���� ��, �� �� �������. <p>
     *
     *  @return �������� ������ �� ����� {@link TOTALS_NONE}, {@link TOTALS_TOP},
     *      {@link TOTALS_BOTTOM},{@link TOTALS_BOTH}
     */
    public int getTotalsPosition() {
        return this.totalsPosition;
    }

    /**
     * ������������� ������� ������ �������� ����� � ���������� �������. <p>
     * @param  totalsPosition  �������� ������ �� ����� {@link TOTALS_NONE},
     *     {@link TOTALS_TOP}, {@link TOTALS_BOTTOM},{@link TOTALS_BOTH}
     */
    public void setTotalsPosition(int totalsPosition) {
        this.totalsPosition = totalsPosition;
    }

    /**
     * ���������� ��������, ������������ ������� ������ ���������� �����.
     * @return {@link BEFORE} - ���������� ���� ��������� ����� ����������;
     *         {@link AFTER} - ���������� ���� ��������� ����� ���������.
     */
    public boolean getGroupMode() {
        return this.groupMode;
    }

    /**
     * ������������� ��������, ������������ ������� ������ ���������� �����.
     * ��� �� �������� ����� ������������� � {@link #aggContainer} .
     * @param groupMode
     *
     */
    public void setGroupMode(boolean groupMode) {
        this.groupMode = groupMode;
    }

    /**
     * �������������� ������ ������ ��� ���������� ������ {@link #execute}.
     * ����� ������ � ������ execute() ������, ��������� �������� ��������
     * ��� (execute) , ��� ������������� ��������� ����� ������ Aggregator.
     */
/*    protected void prepare() {
    }
     */
    /**
     * ��������� ������ ���� ISorter �������� ������� ���������� ����� �����
     * ����������. �� ������ ������� ���������� ������� � ������� �� ������
     * � ���������� {@link #sorterKeys}.
     * @param sortColumnList
     * @return
     */
    protected SortColumnInfo[] createSortColumnInfo(String sortColumnList) {
        String cList = Strings.compressSpaces(sortColumnList).trim();
        String[] s = Strings.split(cList.toUpperCase());
        SortColumnInfo[] sortInfo = new SortColumnInfo[s.length];

        for (int i = 0; i < sortInfo.length; i++) {
            sortInfo[i] = new SortColumnInfo();
        }
        for (int i = 0; i < s.length; i++) {
            sortInfo[i].sortDirection = Sorter.ASCENDING;
            sortInfo[i].caseSensitive = true;
            sortInfo[i].excluded = false;

            String[] s1 = Strings.split(s[i], " ");

            sortInfo[i].columnName = s1[0];

            for (int j = 0; j < s1.length; j++) {
                if (s1[j].toUpperCase().equals("ASC")) {
                    continue;
                }
                if (s1[j].toUpperCase().equals("DESC")) {
                    sortInfo[i].sortDirection = Sorter.DESCENDING;
                    continue;
                }

                if (s1[j].toUpperCase().equals("CASEINS") || s1[j].toUpperCase().equals("CI")) {
                    sortInfo[i].caseSensitive = false;
                    continue;
                }
                if (s1[j].toUpperCase().equals("EX") || s1[j].toUpperCase().equals("EXCLUDE")) {
                    sortInfo[i].excluded = true;
                    continue;
                }
            }
        }
        return sortInfo;
    }

    protected void createGroupParts() {
        groupParts = new GroupPart[sortColumnInfo.length];
        int id = groupParts.length - 1;
        for (int i = 0; i < groupParts.length; i++) {
            SortColumnInfo[] keys = new SortColumnInfo[i + 1];
            System.arraycopy(sortColumnInfo, 0, keys, 0, keys.length);
            groupParts[i] = new GroupPart(keys);
            //groupParts[i].id = id;
            id--;
        }
    }

    /**
     * ��������� ������ �� �������� �������������� ���������� �������. <p>
     * �������� ����������, ������� ����� {@link prepare}.
     * ��������� ������ {@link AggContainer#hasMoreData} �
     * {@link AggContainer#nextData} ������� �������������� ���������� �������.
     * @param mode
     */
    protected void executeSingle(boolean mode) {
        int start = 0;
        int end = sourceTable.getRowCount();
        int step = 1;

        if (mode == BEFORE) {
            start = sourceTable.getRowCount() - 1;
            end = 0;
            step = -1;
        }

        //this.setIncludeDetailRows(false);
        Object[] values;//My 06.03.2012 = null;
        Object[] totals = null;
        if (totalsPosition == TOTALS_TOP || totalsPosition == TOTALS_BOTH) {
            targetTable.addRow();
            getLastRow().getState().setDepth(0);
            //targetTable.getLastRow().getState().getAttribbutes().put("depth", 0);
        }
        for (int rowIndex = start; mode == AFTER ? rowIndex < end : rowIndex >= 0; rowIndex = mode == AFTER ? ++rowIndex : --rowIndex) {
            DataRow row = this.columnExpressionContext.getTable().getRow(rowIndex);
            //totals = this.columnExpressionContext.getRowValues(rowIndex);
            if (rowIndex == start) {
                columnExpressionContext.init(row);
            }
            totals = this.columnExpressionContext.getRowValues(row);
            if (this.includeDetailRows) {
                targetTable.addRow(totals);
            }
        } //for
        values = columnExpressionContext.getAggRowValues();
        targetTable.addRow(values);

        createTotalRows(totals);
        if (mode == BEFORE) {
            reverse();
        }
    }
    public DataRow getLastRow() {
        return targetTable.getRowCount() == 0 ? null : targetTable.getRow(targetTable.getRowCount() - 1);
    }

    /**
     * ��������� ������ �� �������� �������������� ���������� �������. <p>
     * �������� ����������, ������� ����� {@link prepare}.
     * ��������� ������ {@link AggContainer#hasMoreData} �
     * {@link AggContainer#nextData} ������� �������������� ���������� �������.
     */
    public void execute() {
        if (sortColumnInfo == null && hasAggFunction()) {
            executeSingle(groupMode);
        } else {
            execute(groupMode);
        }
        createTreeNodeInfo();
    }

    public boolean hasAggFunction() {
        return columnExpressionContext.getAggFunctions() != null && columnExpressionContext.getAggFunctions().length > 0;
    }

    /**
     * ��������� ������ �� �������� �������������� ���������� �������. <p>
     * �������� ����������, ������� ����� {@link prepare}.
     * ��������� ������ {@link AggContainer#hasMoreData} �
     * {@link AggContainer#nextData} ������� �������������� ���������� �������.
     * @param mode
     */
    protected void execute(boolean mode) {
        //this.levelCount = ((TreeTable)targetTable).getTreeNodeInfo().getLevelCount();
        int start = 0;
        int end = sourceTable.getRowCount();
        int step = 1;

        if (mode == BEFORE) {
            start = sourceTable.getRowCount() - 1;
            end = 0;
            step = -1;
        }

        createGroupParts();
        int levelCount = groupParts == null ? 0 : groupParts.length;        
        Object[] values;//My 06.03.2012 = null;
        Object[] totals = null;
        if (totalsPosition == TOTALS_TOP || totalsPosition == TOTALS_BOTH) {
            DataRow newRow =  targetTable.addRow();
            newRow.getState().setDepth(0);
            columnExpressionContext.updateInserted(newRow);
        }
        for (int rowIndex = start; mode == AFTER ? rowIndex < end : rowIndex >= 0; rowIndex = mode == AFTER ? ++rowIndex : --rowIndex) {
            DataRow row = this.columnExpressionContext.getTable().getRow(rowIndex);
            //totals = this.columnExpressionContext.getRowValues(rowIndex);
            if (rowIndex == start) {
                columnExpressionContext.init(row);
            }
            totals = this.columnExpressionContext.getRowValues(row);
            boolean detailAdded = false;
            if (groupParts != null) {
                for (int i = groupParts.length - 1; i >= 0; i--) {

                    if (groupParts[i].isGroupChanged(rowIndex, mode)) {
                        values = groupParts[i].getAggRowValues();
                        DataRow newRow = targetTable.addRow(values);
                        newRow.getState().setDepth(i + 1);
                        //int a = targetTable.getLastRow().getState().getDepth();
                        groupParts[i].reset(row);
                        columnExpressionContext.updateInserted(newRow);
                    } else {
                        if (i == groupParts.length - 1 && this.includeDetailRows) {
                            DataRow newRow = targetTable.addRow(totals);
                            newRow.getState().setDepth(levelCount+1);
//System.out.println("levelCount=" + levelCount +"; DDD=" + newRow.getState().getDepth());
                            detailAdded = true;
                            columnExpressionContext.updateInserted(newRow);
                        }
                    }
                    
                    //values = groupParts[i].getRowValues(rowIndex);
                } //for
            }
            if ((!detailAdded) && this.includeDetailRows) {
                DataRow newRow = targetTable.addRow(totals);
                newRow.getState().setDepth(levelCount+1);
                columnExpressionContext.updateInserted(getLastRow());
            }
        } //for
        if (groupParts != null) {
            for (int i = groupParts.length - 1; i >= 0; i--) {
                values = groupParts[i].getAggRowValues();
                DataRow newRow = targetTable.addRow(values);
                newRow.getState().setDepth(i + 1);
                columnExpressionContext.updateInserted(getLastRow());
                groupParts[i].reset(null);
            } //for
        }
        createTotalRows(totals);
        if (mode == BEFORE) {
            reverse();
        }
    }

    protected void createTotalRows(Object[] totals) {
        DataRow row;
        if (totalsPosition == TOTALS_TOP || totalsPosition == TOTALS_BOTH) {
            row = targetTable.getRow(0);
            for (int i = 0; i < targetTable.getColumns().getCount(); i++) {
                totals = this.columnExpressionContext.getAggRowValues();
                if (!columnExpressionContext.isKey(i)) {
                    row.setValue(totals[i], i);
                }
            }
            columnExpressionContext.updateInserted(row);
        }
        if (totalsPosition == TOTALS_BOTTOM || totalsPosition == TOTALS_BOTH) {
            row = targetTable.addRow();
            //row = targetTable.getNamedValues(targetTable.addRow());
            getLastRow().getState().setDepth(0);

            for (int i = 0; i < targetTable.getColumns().getCount(); i++) {
                totals = this.columnExpressionContext.getAggRowValues();
                if (!columnExpressionContext.isKey(i)) {
                    row.setValue(totals[i], i);
                }
            }
            this.columnExpressionContext.updateInserted(row);
        }
    }

    public void reverse() {
        if (targetTable.getRowCount() < 2) {
            return;
        }
        int start = 0;
        int end = targetTable.getRowCount() - 1;

        if (totalsPosition == TOTALS_TOP) {
            start = 1;
        }

        if (totalsPosition == TOTALS_BOTTOM) {
            end = targetTable.getRowCount() - 2;
        }

        if (totalsPosition == TOTALS_BOTH) {
            start = 1;
            end = targetTable.getRowCount() - 2;
        }

        reverse(targetTable, start, end);
    }

    public void reverse(Table table, int first, int last) {
        if (table.getRowCount() < 2) {
            return;
        }
        int start = first;
        int end = last;
        //int n = getRowCount() / 2;
        int n = (last - first + 1) / 2 + first;
        for (int i = first; i < n; i++) {
            DataRow r1 = table.getRow(i);
            table.setRow(table.getRow(end), start);
            table.setRow(r1, end);
            start++;
            end--;
        }
    }

/////////////////////////////////////////////////////////////////////////////
    /**
     * ������������ ���������� � ������������� ���������� ������ �������. <p>
     *
     */
    public static class SortColumnInfo {

        public boolean sortDirection = Sorter.ASCENDING;
        public boolean caseSensitive = true;
        public String columnName = null;
        public int columnIndex = -1;
        public boolean excluded = false;
        public int levelCount;
    } //class SortColumnInfo

    public class GroupPart {

        // public int id;
        protected ColumnListExpressionContext partExpressionContext;
        protected int lastKey;
        //protected CommaListExpression commaListExpression;
        protected SortColumnInfo[] keys;
        protected Object[] keyValues;

        /**
         * ���� ������ ����� ���������� ���� a0,a1,..,an �� lastKey ����� ���� 0,1,..., n.
         * ����� �������, ����� ����� ���� � ������ ����� ����������.
         * @param keys
         *
         */
        public GroupPart(SortColumnInfo[] keys) {

            this.keys = keys;
            keyValues = new Object[keys.length];
            for (int i = 0; i < keyValues.length; i++) {
                keyValues[i] = null;
            }
            partExpressionContext = new ColumnListExpressionContext(columnList);
            partExpressionContext.getExpression();
            // -- ��������� � �������� ��������� sourceTable ��� �����������
            // -- ����������� ����� ���������
            partExpressionContext.addTableServices(sourceTable.getContext());
            partExpressionContext.setTargetTable(targetTable);
            partExpressionContext.setSortColumnInfo(this.keys);
        }

        public void updateInserted(DataRow row) {
            partExpressionContext.updateInserted(row);
        }

        public boolean isGroupChanged(int rowIndex, boolean mode) {
            boolean r = false;
            int start;
            int end;
            if (mode == AFTER) {
                start = 0;
                end = sourceTable.getRowCount();
            } else {
                start = sourceTable.getRowCount() - 1;
                end = -1;
            }

            if (rowIndex == start) {
                for (int i = 0; i < keyValues.length; i++) {
                    keyValues[i] = sourceTable.getRow(rowIndex).getValue(keys[i].columnIndex);
                } //for
                return r;
            }

            if (rowIndex == end) {
                return true;
            }

            for (int i = 0; i < keyValues.length; i++) {
                Object o = sourceTable.getRow(rowIndex).getValue(keys[i].columnIndex);
                Object ko = keyValues[i];
                if (o == null || ko == null) {
                    if (o != null || ko != null) {
                        r = true;
                        break;
                    }
                }

                if (!o.equals(ko)) {
                    r = true;
                    break;
                }
            } //for
            // save if nessasary current key values
            if (r) {
                for (int i = 0; i < keyValues.length; i++) {
                    keyValues[i] = sourceTable.getRow(rowIndex).getValue(keys[i].columnIndex);
                } //for
            }

            return r;
        }

        public boolean isGroupChanged(int rowIndex) {
            return isGroupChanged(rowIndex, TreeBuilder.AFTER);
        }

        public Object[] getAggRowValues() {
            return this.partExpressionContext.getAggRowValues();
        }

        public Object[] getRowValues(int rowIndex) {
            DataRow row = this.partExpressionContext.getTable().getRow(rowIndex);
            return this.partExpressionContext.getRowValues(row);
        }

        public void reset(DataRow row) {
            partExpressionContext.reset(row);
        }
    } //class GroupPart

    /**
     * ������� � ���������� ������, ��������������� ���������� �� ��������,
     * ��� ������� ������� ���������� ������� (treeTable).<p>
     * ��� ���������� ����� ������������, ��������, ��� ����������� ����������
     * ( ���������� �� ����� ) �������. <p>
     * ��� ���������� ������� ��������������� �������� �������:
     * <ul>
     *   <li>includeDetailsRows</li>
     *   <li>groupMode</li>
     *   <li>totalsPosition</li>
     *   <li>sortDirection</li>
     *   <li>levelCount</li>
     * </ul>
     *
     * ����� �������� � ��������� ������� ������ �� ������ ��������� �
     * �������� {@link Table#treeNodeInfo} �������������� �������
     * {@link #targetTable}.
     *
     * ����� ������������ ��� ����������� �������������.
     *
     * @return ��������� ������
     */
    protected TreeNodeInfo createTreeNodeInfo() {
        TreeNodeInfo state = new TreeNodeInfo(targetTable);
        state.setIncludeDetailRows(includeDetailRows);
        state.setGroupMode(groupMode);
        state.setTotalsPosition(totalsPosition);
        state.setSortDirection(sortDirection);
        int r = 0;
        if ( sortColumnInfo != null ) {
            r = sortColumnInfo.length;
            for (int i = 0; i < sortColumnInfo.length; i++) {
                if (sortColumnInfo[i].excluded) {
                    r--;
                }
            }
        } 
        state.setLevelCount(r);
        //this.levelCount = r;
        ((TreeTable) targetTable).setTreeNodeInfo(state);
        return state;
    }

/**
     * ��������� ��������� ��������� ���������� �������.<p>
     *
     * ��������� ���������� ������� ����� ��� Table � �� �������� �����
     * ���� �������� � ��� ����� ������� ������� �����������, ����� ������� �
     * ���������� ������� ������������. ������ ��� �� ����� ��������, �� ����
     * ����� ��������� ����� �����������, ��������, ���� ������ ����, ����������
     * ��� ������� ������������� ��� ��� ����� �������� <code>groupMode</code>
     * ������� �������. ��� ������� ���� ��� ������� ������ ���, �.�. ������
     * ���� DataRow ��� ���������� ���������� � ����������,  �����������
     * �������������� ������� ������ ���� �� ������������ � ���������� ���������
     * � ������� ������� �����������. ������� � ������ {@link Table} �������
     * �������� <code>aggState</code> ���� AggState, �������� ��������
     * ��������������� ������� {@link Aggregator#execute}. ����� ����� ��������������
     * �������� ���������� ������� � ������ {@link #execute} ��������� ������
     * ���� <code>AggState</code>.
     *
     * @see Aggregator#createState
     * @see BaseTable#getAggState
     */
    public static class TreeNodeInfo {

        private Table table; //target Table
        private DataRowCollection rows; // dataSore for table
        private boolean includeDetailRows;
        private int totalsPosition;
        private int levelCount;
        private boolean sortDirection;
        private boolean groupMode;
        private int[] levelIndice;
        private int[] rowIndice;

        protected TreeNodeInfo() {
        }

        public TreeNodeInfo(Table table) {
            this.table = table;
        }
        /**
         * ��������������� ����� ����������� ����������� �� <code>ViewManager</code>.
         * @param rows
         */
        public void setRows(DataRowCollection rows) {
            this.rows = rows;
        }

        /**
         * @return true ���� ��������� ������ �������� ������� ��������� �
         * �������������� (����������) �������. false - � ��������� ������.
         * @see #setIncludeDetailRows
         */
        public boolean getIncludeDetailRows() {
            return this.includeDetailRows;
        }

        /**
         * @param includeDetailRows ���� ����� true - ��������������� �����, �����
         *      ��������� ���� �������� ������� ��������� � ���������� �������.
         *      false ���������, ��� ��������� ���� �� ���������.
         * @see #getIncludeDetailRows
         */
        public void setIncludeDetailRows(boolean includeDetailRows) {
            this.includeDetailRows = includeDetailRows;
        }

        /**
         * ���������� ��������, ������������ ��������  ��� ��� �������� ���� �
         * �������������� ������� �, ���� ��, �� �� �������. <p>
         *
         *  @return �������� ������ �� ����� {@link Aggregator#TOTALS_NONE},
         *      {@link Aggregator#TOTALS_TOP},
         *      {@link Aggregator#TOTALS_BOTTOM},{@link Aggregator#TOTALS_BOTH}
         */
        public int getTotalsPosition() {
            return this.totalsPosition;
        }

        /**
         * ������������� ������� ���������� �������� ����� � ���������� �������. <p>
         * @param  totalsPosition  �������� ������ �� �����
         *      {@link Aggregator#TOTALS_TOP},
         *      {@link Aggregator#TOTALS_BOTTOM},{@link Aggregator#TOTALS_BOTH}
         */
        public void setTotalsPosition(int totalsPosition) {
            this.totalsPosition = totalsPosition;
        }

        /**
         * �������� {@link #levelCount} ���������� ���������� �������
         * �������������, ������� ������ ����� ���������� �������
         * � ������ �������, �� �������� ������������� ����������
         * �������� �������, ������, ��� ���� ������� ��������������.
         * ��� �������� ���������� ����������� ������������ <i>EXCLUDE</code>
         * � ������� ����������. ��������, ��� ���������� ����������� ������:
         * <code>
         *  <pre>
         *      "State,City EXCLUDE,Department"
         *  </pre>
         * </code>
         * ��� ���������� ������������ ��� ��� �������, �� ��� ��������
         * <code>treeTable</code> ���������� ���� ��������� � ��������������
         * ������� ������ ��� ����: City � Department. ����� �������,
         * �������� �������� <code>levelCount</code> ����� 2.
         *
         *
         * @return ���������� ������� ��������������.
         */
        public int getLevelCount() {
            return levelCount;
        }

        /**
         * ��������������� ���������� ������� �������������. <p>
         * ����� ������������ ��� ����������� �������������.
         * @param levelCount
         */
        public void setLevelCount(int levelCount) {
            this.levelCount = levelCount;
        }

        /**
         * ���������� �������� ����������� ���������� �������� �������: ��
         * �������� ��� �� �����������.<p>
         *
         * @return ISorter#ASCENDING ��� ISorter#DESCENDING.
         */
        public boolean getSortDirection() {
            return this.sortDirection;
        }

        /**
         * ������������� �������� ����������� ���������� �������� �������: ��
         * �������� ��� �� �����������.<p>
         *
         * @param sortDirection ��������� �������� ISorter#ASCENDING ���
         *      ISorter#DESCENDING.
         */
        public void setSortDirection(boolean sortDirection) {
            this.sortDirection = sortDirection;
        }

        /**
         * ���������� ��������, ������������ ������� ������ ���������� �����.
         * @return {@link BEFORE} - ���������� ���� ��������� ����� ����������;
         *         {@link AFTER} - ���������� ���� ��������� ����� ���������.
         */
        public boolean getGroupMode() {
            return this.groupMode;
        }
        /**
         * ������������� ��������, ������������ ������� ������ ���������� �����.
         *
         * @param groupMode ��������� ���� �� ���� �������� {@link BEFORE} -
         *   ���������� ���� ��������� ����� ����������;
         *   {@link AFTER} - ���������� ���� ��������� ����� ���������.
         */
        public void setGroupMode(boolean groupMode) {
            this.groupMode = groupMode;
        }

        /**
         * ���������� ��� ���������� ������� �� ��� �������. <p>
         * ����� �� ������� �� ������-���� ������� ��� ����������, ��������
         * ������������ � ���������� �������, �.�. ���������� ��������
         * � dataStore ���������� �������.
         * ������������, ��������, ��� ���������� ���������� (tree)
         * �������.
         * @param rowIndex ������ ������������� ����.
         * @return ��� ������� ���� DataRow.
         */
        public DataRow getRow(int rowIndex) {
//            DataRow r = null;
//                r = rows.get(rowIndex);
            return this.rows.get(rowIndex);
        }

        /**
         * ���������� ����� ������ ������������� ��� ���� ���������� �������
         * � �������� �������� ����������. <p>
         * ��� �������� ����� ������������ ��������� ����� 0; ��� ���������,
         * ���� ������������, �����, �������� ������ �� 1 ������ ��������
         * {@link #getLevelCount}.
         *
         * @param rowIndex ������ ����, ��� �������� ������������ �������
         *      �������������.
         * @return ����� ������. �������� >= 0.
         */
        public int getLevel(int rowIndex) {
            DataRow row = getRow(rowIndex);
            int d = row.getState().getDepth();
            if ( isDetail(row) )
                d = levelCount+1;
            return d;
        }

        public int getLevel(DataRow row) {
            int d = row.getState().getDepth();
            if ( this.isDetail(row) ) {
                d = this.levelCount+1;
            }

            return d;
        }

        public boolean isDetail( DataRow row ) {
            boolean b = false;
            int d = row.getState().getDepth();
            if ( d == levelCount+1 ) {
                if ( this.includeDetailRows && this.table.getRowCount() > 0 ) {
                    if ( totalsPosition == TOTALS_TOP || totalsPosition == TOTALS_BOTH )
                       b = row != table.getRow(0) ;
                    else if ( totalsPosition == TOTALS_BOTTOM || totalsPosition == TOTALS_BOTH )
                       b = row != table.getRow(table.getRowCount()-1);
                }
            }

            return b;
        }
        /**
         * ������� ��� ������� <code>rowIndice</code> �
         * <code>levelIndice</code>. <p>
         * ���������� ������ <code>rowIndice</code> - ��� ������ ����� �����.
         * ������� i ������� �������� �������� i - ������ ����. �����, ����,
         * �������� ��������� ���������� ����� ������� � ������ � ����������
         * ��������� <code>DataRow</code> ��������� <code>rowIndice</code>.
         * �� ����� ����� ���������� ���� ������� ������ ������ ���� ����, ��
         * �������������� ����� ������� ������ �������� <code>rowIndice</code>
         * � ����� ��� ���������� ��������� ������ ���� � �������� ���������.<p>
         *
         * � ������� ������� levelIndice ����� ���������� ������� ����� �����.
         * ����, ��������, ��� ����������, ����������, ���� ������ ����,
         * ���������� ������ ���� ���������� ���������� � ����������� ��
         * ���������� ������, �.�. �� 1 ������, �� ��������� levelIndice[rowIndex]
         * �������� �������. ���� ��� rowIndex �������� �������� ����������
         * �����, �� levelIndice[rowIndex] == 0. ���� ��� rowIndex ��������
         * ���������� ������ 1, �� levelIndice[rowIndex] == -1.
         */
        public void createIndice() {
            //int levelCount = getLevelCount();
            if (rows == null || rows.getCount() == 0) {
                return;
            }
            levelIndice = new int[this.rows.getCount()];
            rowIndice = new int[this.rows.getCount()];

            int first = (groupMode == TreeBuilder.BEFORE) ? -1 : rows.getCount();
            int last = (groupMode == TreeBuilder.BEFORE) ? rows.getCount() - 1 : 0;
            int step = (groupMode == TreeBuilder.BEFORE) ? 1 : -1;

            int[] priorIndex = new int[levelCount + 1]; //��������� ��� ���������
            for (int i = 0; i < priorIndex.length; i++) {
                priorIndex[i] = -1;
            }
            //for ( int i=first; i <= last; i += step ) {
            int i = first;
            while (i != last) {
                i += step;
                int level = getLevel(i);
                if (level != 0) {
                    //���������� ��������
                    if (level == 1) {
                        levelIndice[i] = -1;
                    } else {
                        levelIndice[i] = priorIndex[level - 1];
                    }
                } else {
                    levelIndice[i] = -1;
                }
                rowIndice[i] = i;
                if (level <= levelCount ) // detail row
                    priorIndex[level] = i;
            }


        }

        public int[] getLevelIndice() {
            return levelIndice;
        }

        public int[] getRowIndice() {
            return rowIndice;
        }
    } //class GroupState
} //class