/*
 * Joiner.java
 *
 * Created on 30.08.2007, 18:40:33
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.impl;

import java.util.Enumeration;
import tdo.DataRow;
import tdo.DefaultExpressionContext;
import tdo.Table;
import tdo.expr.ExpressionContext;
import tdo.expr.IExpression;
import tdo.expr.IdentifierOperand;

public class Joiner extends TreeBuilder {

    protected Table joinTable;
    protected ExpressionContext onExprContext;
    protected ExpressionContext whereExprContext;
    private IExpression onExpression;
    private IExpression whereExpression;

    protected Plan plan;

    public Joiner(Table source, Table joinTable, String onExpr, String whereExpr) {
        this(source, joinTable, onExpr, whereExpr, null);
    }

    public Joiner(Table source, Table joinTable, String onExpr, String whereExpr, Plan plan) {
        super(source);
        this.plan = plan;
        //getContext().addTable("a", joinTable);
        this.joinTable = joinTable;

        if (onExpr != null) {
            onExprContext = new DefaultExpressionContext(onExpr);
            ((DefaultExpressionContext)onExprContext).addTableServices(sourceTable.getContext());
            ((DefaultExpressionContext)onExprContext).addTableServices("a", joinTable.getContext());
            onExpression = onExprContext == null ? null : onExprContext.createExpression();
        }
        if (whereExpr != null) {
            whereExprContext = new DefaultExpressionContext(whereExpr);
            whereExpression = whereExprContext == null ? null : whereExprContext.createExpression();
        }
        
        if ( plan != null ) {
            plan.setJoiner(this);
        }
    }

    /**
     * Создает экземпляр типа {@link Table}, используемый как результирующая
     * таблица агрегированных данных.
     * @return новый объект Table.
     */
    public Table createTargetTable(Table joinTable) {
        this.targetTable = sourceTable.createTable();
        createColumnListExpressionContext(joinTable, "a");
        if (whereExprContext != null) {
            ((DefaultExpressionContext)whereExprContext).addTableServices(targetTable.getContext());
        }
        return this.targetTable;
    }

    @Override
    protected void execute(boolean mode) {
        int start = 0;
        int end = sourceTable.getRowCount();

        int jstart;//My 06.03.2012 = 0;
        int jend;//My 06.03.2012 = joinTable.getRowCount() - 1;

        //int end = 10;
        int step = 1;

        //        createGroupParts();
        //        Object[] values = null;
        Object[] totals;//My 06.03.2012 = null;
        DataRow[] rows = new DataRow[2];

        if (plan == null) {
            plan = new Plan(this);
        }
        for (int rowIndex = start; rowIndex < end; rowIndex++) {
            rows[0] = this.sourceTable.getRow(rowIndex);

            plan.scanLeft(rowIndex);
            jstart = plan.getStartRight() >= 0 ? plan.getStartRight() : 0;
            jend = plan.getEndRight() >= 0 ? plan.getEndRight() : joinTable.getRowCount() - 1;
            
            int firstMatched = -1;
            int lastMatched  = -1;
            for (int j = jstart; j <= jend; j++) {
                rows[1] = joinTable.getRow(j);
                Boolean b = (onExpression == null) ? true : (Boolean) onExpression.getValue(rows);
                if (b.booleanValue()) {
                    if ( firstMatched < 0 )
                        firstMatched =  j;
                    lastMatched = j;
                    totals = getContext().getRowValues(rows);
                    if (whereExpression != null) {
                        //DataRow r = targetTable.createRow(totals);
                        DataRow r = targetTable.createRow();
                        r.copyFrom(totals);
                        Boolean b1 = (whereExpression == null) ? true : (Boolean) whereExpression.getValue(r);
                        if (b1.booleanValue()) {
                            targetTable.addRow(r);
                        }
                    } else {
                        targetTable.addRow(totals);
                    }
                }
            } //for
            
            this.plan.setFirstMatchedRight(firstMatched);
            this.plan.setLastMatchedRight(lastMatched);            
        } //for
    }

    @Override
    protected TreeNodeInfo createTreeNodeInfo() {
/*        TreeNodeInfo state = new TreeNodeInfo(targetTable);
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
 */
        return null;
    }
    
    public static class Plan {

        protected Joiner joiner;
        private IdentifierOperand[] leftColumnOperands;

        //protected String condition;
        protected boolean keepRightIndex;
        protected int priorMatchedLeft;
        protected int lastMatchedLeft;

        protected int currentRight;
        protected int currentLeft;
        protected int firstMatchedRight;
        protected int lastMatchedRight;

        //protected ExpressionContext conditionContext;
        //protected IExpression condExpr;

        protected int startRight;
        protected int endRight;

       public Plan(boolean keepIndex) {
            //this.condition = condition;
            this.keepRightIndex = keepIndex;
            init();
       }
        
       public Plan(Joiner joiner, boolean keepIndex) {
            //this.condition = condition;
            this.keepRightIndex = keepIndex;
            this.joiner = joiner;
            leftColumnOperands = leftColumns(); // the array of the left table column operands
            init();
        }

        protected Plan(Joiner joiner) {
            //this.condition = condition;
            this.keepRightIndex = false;
            this.joiner = joiner;
            leftColumnOperands = leftColumns(); // the array of the left table column operands
            init();
        }

        public void setJoiner(Joiner joiner) {
            this.joiner = joiner;
            leftColumnOperands = leftColumns(); // the array of the left table column operands
        }

        private void init() {
        }

        /**
         * @return the count of ColumnOperands in the onexpression
         *         for the left table
         */
        private IdentifierOperand[] leftColumns() {
            int count = leftColumnCount();
            if (count == 0) {
                return new IdentifierOperand[0];
            }
            IdentifierOperand[] r = new IdentifierOperand[count];
            Enumeration en = joiner.onExprContext.getIdentifierOperands().elements();
            int i = 0;
            while (en.hasMoreElements()) {
                IdentifierOperand co = (IdentifierOperand) en.nextElement();
                if (co.getAlias() == null) {
                    r[i++] = co;
                }
            } //while
            return r;
        }

        /**
         * @return the count of ColumnOperands in the onexpression
         *         for the left table
         */
        private int leftColumnCount() {
            int r = 0;
            if (joiner.onExprContext != null) {
                Enumeration en = joiner.onExprContext.getIdentifierOperands().elements();
                while (en.hasMoreElements()) {
                    IdentifierOperand co = (IdentifierOperand) en.nextElement();
                    if (co.getAlias() == null) {
                        r++;
                    }
                } //while
            }
            return r;
        }

        /**
         * Вызывается сразу после считывания очередного ряда "левой" таблицы.
         *
         */
        public void scanLeft(int rowIndex) {
            priorMatchedLeft = lastMatchedLeft;
            lastMatchedLeft = -1;

            currentLeft = rowIndex;

            if (evalCond()) {
                startRight = firstMatchedRight;
                endRight = lastMatchedRight;
            } else {
                if (this.keepRightIndex) {
                    startRight = lastMatchedRight + 1;
                    endRight = joiner.joinTable.getRowCount() - 1;
                } else {
                    startRight = joiner.joinTable.getRowCount() > 0 ? 0 : -1;
                    endRight = joiner.joinTable.getRowCount() - 1;
                }
            }
            firstMatchedRight = -1;
            lastMatchedRight = -1;
        }

        /**
         * Вызывается сразу после считывания очередного ряда "правой" таблицы.
         *
         */
        public void scanRight(int rowIndex) {
            currentRight = rowIndex;
        }

        public int scanRightAfter(boolean onCond, boolean whereCond) {
            int result = 0; //continue
            if (onCond && firstMatchedRight < 0) {
                firstMatchedRight = currentRight;
                lastMatchedRight = currentRight;
            } else if (onCond) {
                lastMatchedRight = currentRight;
                lastMatchedLeft = currentLeft;
            } else if (keepRightIndex && firstMatchedRight >= 0 && !onCond) {
                result = -1; // stop
            }
            return result;
        }

        private boolean evalCond() {
            Boolean b = true;
            if (currentLeft == 0) {
                return false;
            }
            for (int i = 0; i < leftColumnOperands.length; i++) {
                String columnName = leftColumnOperands[i].getName();
                Object o1 = joiner.sourceTable.getRow(currentLeft).getValue(columnName);
                Object o2 = joiner.sourceTable.getRow(currentLeft - 1).getValue(columnName);
                if (o1 == null && o2 == null) {
                    continue;
                } else if (o1 == null || o2 == null || !o1.equals(o2) ) {
                    b = false;
                    break;
                } 
            }
            return b;
        }
        
        public void setFirstMatchedRight(int rowIndex) {
            this.firstMatchedRight = rowIndex;
        }
        
        public void setLastMatchedRight(int rowIndex) {
            this.lastMatchedRight = rowIndex;
        }
        public int getStartRight() {
            return startRight;
        }
        public int getEndRight() {
            return endRight;
        }
    } //class Plan
} //class Joiner
