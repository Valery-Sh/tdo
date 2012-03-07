/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.support;

import tdo.*;

/**
 *
 * @author Valery
 */
public class DataColumnProviderTst implements ColumnProvider {
    @Override
    public DataColumn createColumn(Class clazz, String columnName) {
        return new StringColumn(clazz,columnName);
    }

    @Override
    public DataColumn createColumn(int sqlType, String columnName) {
        return new StringColumn(sqlType,columnName);
    }
    public static class StringColumn extends DataColumn implements Cloneable {
        
        /**
         * Инициализирует значение полей и свойств, специфичных для класса.
         * <code><pre>
         *   setType(String.class);
         *   setSqlType(java.sql.Types.CHAR);            
         *   this.setDefaultValue("");
         * </pre></code>
         */
        public StringColumn() {
            this.setType(String.class);
            this.setSqlType(java.sql.Types.CHAR);
            this.setDefaultValue("");
        }
        public StringColumn(Class clazz,String columnName) {
            this.setType(clazz);
            this.setName(columnName);
        }
        public StringColumn(int sqlType,String columnName) {
            this.setSqlType(java.sql.Types.CHAR);
            this.setName(columnName);
        }

        /**
         * @return Возвращает значение типа данных равное <code>java.lang.String</code>.
         */
        @Override
        public Class getType() {
            return String.class;
        }

        /**
         * Преобразует значение параметра в тип <code>java.lang.String</code>.
         * @param value преобразуемое значение
         * @return Если <i>value</i> равно <code>null</code>, то возвращает 
         * <code>null</code>. В противном случае выполняет 
         *   <code>value.toString()</code>.
         */
        @Override
        public Object toColumnType(Object value) {
            return value == null ? null : value.toString();
        }

        /**
         * @inherited
         */
        @Override
        public synchronized Object clone() {
            return (StringColumn) super.clone();
        }

        /**
         * Возвращает новый экземпляр объекта, используемого как значение 
         * по умолчанию для данных, связанных с колонкой.
         * 
         * @return значение по умолчанию. Если метод <code>getDefaultValue</code>
         *    возвращает <code>null</code>, то возвращается <code>null</code>.
         *    В остальных случаях возвращается новый экземпляр типа 
         *   <code>java.lang.String</code> со значением равным 
         *   значению, возвращаемому методом <code>getDefaultValue()</code>.
         */
        @Override
        public Object blankValueInstance() {
            Object obj = getDefaultValue();
            if (obj == null) {
                return null;
            }
            return new String((String) obj);
        }

        @Override
        protected int compareNotNull(Object o1, Object o2) {
            return ((String) o1).compareTo((String) o2);
        }
    }//class StringColumn

}//class DataColumnProviderTst
