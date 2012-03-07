/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdo.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import tdo.DataRow;
import tdo.DataRowCollection;
import tdo.event.ActiveRowEvent;
import tdo.event.ActiveRowListener;
import tdo.event.TableEvent;
import tdo.event.TableEvent.TableEventCause;
import tdo.event.TableListener;
import tdo.impl.TreeBuilder.TreeNodeInfo;
import tdo.service.TableServices;

/**
 *
 * @author Valery
 */
public class InternalViewManager implements ViewManager{
    TableServices services;
    List managedProcessList;
    private DataRowCollection originalRows;
    private DataRowCollection currentRows;
    private DataRowCollection relationSourceRows;
    private Filterer currentFilterer;
    private Relation relation;
    protected RelationHandler relationHandler;

    private Sorter currentSorter;
    private TreeSorter currentTreeSorter;

    //protected
    public InternalViewManager() {
        this(null,null);
    }

    public InternalViewManager(TableServices services,DataRowCollection originalRows) {
        this.services = services;
        this.originalRows = originalRows;
        this.currentRows = originalRows;
        currentFilterer = null;
        this.managedProcessList = new ArrayList(3);
        this.relationHandler = new RelationHandler();
    }
    @Override
    public Filterer[] getFilterers() {
        List<Filterer> v = new ArrayList<Filterer>(2);
        for (int i = 0; i < managedProcessList.size(); i++) {
            Object o = managedProcessList.get(i);
            if (o instanceof Filterer) {
                v.add((Filterer)o);
            }
        }
        if (v.isEmpty()) {
            return null;
        }
        Filterer[] f = new Filterer[v.size()];
        for (int i = 0; i < f.length; i++) {
            f[i] = (Filterer) v.get(i);
        }
        return f;
    }
    
    @Override
    public Filterer getCurrentFilterer() {
        return this.currentFilterer;
    }
    @Override
    public boolean filter(Filterer newFilterer) {
        boolean added = false;
        boolean filtererReplaced = false;
        if (this.check(newFilterer)) {
            this.currentFilterer = newFilterer;
            int i = managedProcessList.indexOf(newFilterer);
            if ( i >= 0 ) {
                managedProcessList.set(i,newFilterer);
                filtererReplaced = true;
            }
            else
                managedProcessList.add(newFilterer);

            if ( ! updateRelation() ) {
                if ( filtererReplaced )
                    processAll();
                else
                    processFilter(currentFilterer);
            }
            added = true;
            //fireDataModelStructure();
        }
        return added;
    }

    protected boolean check(Filterer newFilterer) {
        boolean r = true;
        if (this.currentFilterer == null && newFilterer == null) {
            r = false;
        } else if (newFilterer == null) {
            r = false;
        }
        return r;
    }

    protected boolean updateRelation() {
        boolean result = false;
        if (relation != null) {
            processAll();
            relationSourceRows = currentRows;
            //relation.refreshView(this.currentRows);
            refreshRelation();
            //fireDataModelStructure();
            result = true;
        }
        return result;

    }
    
    
    @Override
    public void cancelFilter(Filterer f) {
        if (managedProcessList.isEmpty()) {
            return;
        }
        int index = this.managedProcessList.indexOf(f);
        if (index < 0) {
            return;
        }
        currentRows = originalRows;
        managedProcessList.remove(index);

        for (int i = 0; i < managedProcessList.size(); i++) {
            Object o = managedProcessList.get(i);
            process(o);
            // Последний элемент запоминаем
            if (o instanceof Filterer) {
                this.currentFilterer = (Filterer) o;
            }
        }

        if (relation != null) {
            //relation.refreshView(this.getCurrentRows());
            relationSourceRows = currentRows;
            refreshRelation();
        }
    }

    @Override
    public void cancelFilters() {
        if (managedProcessList.isEmpty()) {
            return;
        }
        if (this.currentFilterer == null) {
            return;
        }
        currentRows = originalRows;
        int n = 0;

        Iterator iter = managedProcessList.iterator();
        while ( iter.hasNext() ) {
            Object o = iter.next();
            if (o instanceof Filterer) {
                iter.remove();
            }

        }


        this.currentFilterer = null;

        for (int i = 0; i < managedProcessList.size(); i++) {
            Object o = managedProcessList.get(i);
            process(o);
        }

        if (relation != null) {
            //relation.refreshView(this.getCurrentRows());
            relationSourceRows = currentRows;
            refreshRelation();
        }
    }


    public void processAll() {
        this.currentRows = originalRows;
        for (int i = 0; i < managedProcessList.size(); i++) {
            process(managedProcessList.get(i));
        }
    }

    private void process(Object action) {

        if (action instanceof Sorter && !(action instanceof TreeSorter)) {
            processSort((Sorter) action);
        } else if (action instanceof Filterer) {
            processFilter((Filterer) action);
        } else if (action instanceof TreeSorter) {
            processTreeSort((TreeSorter) action);
        }
    }

    protected void processFilter(Filterer filterer) {
        DataRow[] v = filterer.getRows();
        currentRows = this.getCurrentRows().create(services, false);
        this.getCurrentRows().copyFrom(v);

    }

    public void processSort(Sorter sorter) {
        //managedProcessList.check(sorter);
        currentRows = getCurrentRows().create(services, true);
        this.currentSorter = sorter; // now is current

        this.currentSorter.sort();

    }

    public void processTreeSort(TreeSorter sorter) {
        System.out.println("PROCESS TREESORTER");
    }

  
    @Override
    public DataRowCollection getCurrentRows() {
        return this.currentRows;
    }
    ////////////////////////////////////////////////////////////
    // Relation
    ////////////////////////////////////////////////////////////
    @Override
    public Relation getRelation() {
        return this.relation;
    }

    @Override
    public boolean setRelation(Relation newRelation) {
        boolean result = false;
        if ( relation == null && newRelation == null ) {
            return result;
        }
        if ( relation != null && relation.getParentTable() != null) {
            relation.getParentTable().removeActiveRowListener(relationHandler);
        }
        Relation old = relation;
        relation = newRelation;
        if (relation != null) {

            relation.getParentTable().addActiveRowListener(relationHandler);
            relationSourceRows = currentRows;
            refreshRelation();
            result = true;
        } else {
            old.getParentTable().removeActiveRowListener(relationHandler);
            managedProcessList.remove(old);
            processAll();
            result = true;

        }
        //fireDataModelStructure();

        return result;
        //table.fireDataModel(dataModelEvent);
        //table.setRowCount(this.getRowCount());
    }
    protected void refreshRelation() {

        //relation.setValues(values);
        currentRows = relationSourceRows;
        DataRowCollection v = relation.getRows();
        currentRows = currentRows.create(services, false);
        currentRows.copyFrom(v);
        fireDataModelStructure();
    }
    // TODO TableService
    protected void fireDataModelStructure() {
        TableEvent te = new TableEvent(relation.getTable(), TableEvent.ALL_ROWS,TableEvent.ALL_COLUMNS, TableEventCause.schema);
        relation.getTable().fireDataModel(te);
    }

    ///////////////////////////////////////////////////////////////////////////
    //                          SOTRTER
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Выполняет сортировку или воосстанавливает текущий currentRows в состояние
     * "ДО" сортировки.<p>
     * Если значение параметра newsorter == null и значение свойства
     * {@link #sorter} равно <code>null</code>, то никаких действий метод не
     * производит. <p>
     *
     * Если значение параметра newsorter равно <code>null</code> и значение
     * свойства {@link #sorter} <i>не</i> равно <code>null</code>, то
     * <OL>
     *   <LI>Елемент списка storeList с индексом SORT устанавливается в null</LI>
     *   <LI>Запускается метод doActionList{null).</LI>
     * </OL>
     * Если значение параметра newsorter <i>не</i> равно <code>null</code>, то
     * <OL>
     *   <LI>Если операция сортировки находится в режиме удержания, т.е.
     *       метод <code>isKeepingFor(SORT)</code> возвращает <code>true</code>,
     *       то удаляется старая операция ISorter из {@link #actionList}, если
     *       есть, а новая заносится в начало списка.
     *   </LI>
     *   <LI>Если операция сортировки НЕ находится в режиме удержания, т.е.
     *       метод <code>isKeepingFor(SORT)</code> возвращает <code>false</code>,
     *       то, если задана старая операция ISorter в {@link #actionList}, то
     *       она замещается новой, иначе новая операция добавляется в список
     *       операций.
     *   </LI>
     *   <LI>Запускается метод doActionList для новой операции.</LI>
     * </OL>
     * @param newsorter новая операция сортировки.
     */
    @Override
    public boolean sort(Sorter newSorter) {
    
        boolean added = false;
        boolean sorterReplaced = false;
        if (this.check(newSorter)) {
            this.currentSorter = newSorter;
            int i = managedProcessList.indexOf(newSorter);
            if ( i >= 0 ) {
                managedProcessList.set(i,newSorter);
                sorterReplaced = true;
            }
            else
                managedProcessList.add(newSorter);

            if ( ! updateRelation() ) {
                if ( sorterReplaced )
                    processAll();
                else
                    this.processSort(currentSorter);
            }
            added = true;
            //fireDataModelStructure();
        }
        return added;
    }
    
    protected boolean check(Sorter newSorter) {
        if (this.currentSorter == null && newSorter == null) {
            return false;
        }

        if (newSorter == null) {
            return false;
        }

        return true;
    }

    
    @Override
    public void cancelSort(Sorter sorter) {
        if ( sorter == null )
            return;
        if (managedProcessList.isEmpty()) {
            return;
        }
        int index = this.managedProcessList.indexOf(sorter);
        if (index < 0) {
            return;
        }
        currentRows = originalRows;
        managedProcessList.remove(index);
        if (sorter instanceof DefaultTreeSorter) {
            this.currentTreeSorter = null;
        } else {
            this.currentSorter = null;
        }


        for (int i = 0; i < managedProcessList.size(); i++) {
            Object o = managedProcessList.get(i);
            process(o);
            // Последний элемент запоминаем
            if (o instanceof DefaultTreeSorter) {
                this.currentTreeSorter = (TreeSorter) o;
            } else {
                this.currentSorter = (Sorter) o;
            }
        }

        if (relation != null) {
            relationSourceRows = currentRows;
            refreshRelation();
        }
    }

    @Override
    public void cancelSort() {
        if (managedProcessList.isEmpty()) {
            return;
        }
        if (this.currentSorter == null) {
            return;
        }
        currentRows = originalRows;
        int i = 0;
        //Object o = null;
        Iterator iter = managedProcessList.iterator();
        while ( iter.hasNext() ) {
            Object o = iter.next();
            if (o instanceof Sorter) {
                iter.remove();
            }

        }
/*        for ( Object o : managedProcessList) {
            //o = managedProcessList.get(i);
            if (o instanceof Sorter) {
                managedProcessList.remove(o);
                continue;
            }
            i++;
        }
*/
/*        while (!managedProcessList.isEmpty()) {
            o = managedProcessList.get(i);
            if (o instanceof Sorter) {
                managedProcessList.remove(i);
                continue;
            }
            i++;
        }
*/
        for (int n = 0; n < managedProcessList.size(); n++) {
            process(managedProcessList.get(n));
        }
        this.currentSorter = null;
        this.currentTreeSorter = null;
        if ( relation != null ) {
            relationSourceRows = currentRows;
            refreshRelation();
        }
    }

    @Override
    public Sorter getCurrentSorter() {
        return this.currentSorter;
    }
    //////////////////////////////////////////////////////////////////////////
    //                        TREE SORTER
    //////////////////////////////////////////////////////////////////////////
    @Override
    public TreeSorter sortTree(TreeSorter treeSorter) {
        treeSorter.getTreeNodeInfo().setRows(currentRows); //Вначале устанавливаем источник рядов
        currentRows = getCurrentRows().create(services, true); //Создаем результ. ряды
        managedProcessList.add(treeSorter);
        treeSorter.sort();
        return treeSorter;
    }

    @Override
    public TreeSorter sortNode(TreeSorter treeSorter) {
        TreeBuilder.TreeNodeInfo state = treeSorter.getTreeNodeInfo();
        state.setRows(currentRows);
        this.currentRows = getCurrentRows().create(services, true);

        int minRow = -1;
        int maxRow = -1;

        managedProcessList.add(treeSorter);
        int nodeIndex = this.services.getTreeSorterServices().find(treeSorter.getNodeRow());
        if (nodeIndex + 1 == this.services.getTreeSorterServices().getRowCount()
                && state.getGroupMode() == TreeBuilder.BEFORE) {
            return null;
        }
        if (state.getGroupMode() == TreeBuilder.BEFORE) {
            maxRow = defineMaxRowIndex(state,nodeIndex);
            minRow = ++nodeIndex;
        } else {
            minRow = defineMinRowIndex(state,nodeIndex);
            maxRow = --nodeIndex;
        }

        if (minRow == maxRow) {
            return null;
        }

        treeSorter.sort(minRow, maxRow);

        //TODO здесь надо бы aggSorter.getTreeNodeInfo().setRows(null)
        //чтобы не удерживать ссылку. Но, возможно надо подумать о применении
        treeSorter.getTreeNodeInfo().setRows(null);
        return treeSorter;
    }
    protected int defineMinRowIndex(TreeNodeInfo state,int start) {
        int result = start;

        int startDepth = services.getTreeSorterServices().getRow(start).getState().getDepth();
        // find nearest row whoose depth equals or less then startDepth
        result = start;
        if (start - 1 <= 0) {
            return result;
        }
        result--;

        while (result > 0) {
            int curDepth = services.getTreeSorterServices().getRow(result).getState().getDepth();
            if (curDepth <= startDepth) {
                // !!! found
                result++;
                break;
            }
            result--;
        } //while
        if (result == 0) {
            result = 1;
        }
        return result;
    }

    protected int defineMaxRowIndex(TreeNodeInfo state,int start) {
        int result = start;
        int rc = services.getTreeSorterServices().getRowCount();
        //int startDepth = services.getTreeSorterServices().getRow(start).getState().getDepth();
        int startDepth = state.getLevel(start);
        // find nearest row whoose depth equals or less then startDepth
        result = start;
        if (start + 1 >= rc) {
            return result;
        }
        result++;
        while (result < rc) {
            //int curDepth = services.getTreeSorterServices().getRow(result).getState().getDepth();
            int curDepth = state.getLevel(result);
            if (curDepth <= startDepth) {
                // !!! found
//                if ( curDepth > 0 ) {
                    result--;
                    break;
//                }
            }
            result++;
        } //while
        if (result == rc) {
            result = rc - 1;
        }
        return result;
    }

    public class RelationHandler implements ActiveRowListener, TableListener {
//        private Relation relation;
//        private ViewManager viewManager;

//        public RelationHandler(ViewManager viewManager ) {
//            this.relation = viewManager.getRelation();
//        }

        @Override
        public void activeRowChange(ActiveRowEvent e) {
            if (e.isChanging()) {
                return;
            }
            refreshRelation();
        }

        @Override
        public void tableChanged(TableEvent e) {
            if (e.getCause() == TableEventCause.update) {
                if (e.getChangedRow() >= 0 && contains(relation.getKeyColumns(), e.getChangedColumns())) {
                    refreshRelation();
                }
            }
        }

        private boolean contains(int[] indexes, int index) {
            if (indexes == null || indexes.length == 0) {
                return false;
            }
            for (int i = 0; i < indexes.length; i++) {
                if (indexes[i] == index) {
                    return true;
                }
            }
            return false;
        }

        private boolean contains(String[] names, int index) {
            if (names == null || names.length == 0) {
                return false;
            }
            for (int i = 0; i < names.length; i++) {
                int n = relation.getTable().getColumns().find(names[i]);
                if (n == index) {
                    return true;
                }
            }
            return false;
        }
    } //class RelationHandler


}//class ViewManager
