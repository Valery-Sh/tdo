/*
 * ObjectRowSupport.java
 *
 */
package tdo;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import tdo.service.ColumnServices;
import tdo.service.TableServices;

/**
 *
 * @author valery
 */
public abstract class ObjectRowSupport implements ObjectValueAccessor{

    private TableServices context;
    /**
     * <p>The class whose properties (and, optionally, fields) we are
     * exposing.</p>
     */
    private Class clazz = null;
    private Class stopClazz = null;
    /**
     * <p>Map of {@link Field}s for fields, keyed by field name.  This
     * is only populated if <code>includeFields</code> is set to <code>true</code>.</p>
     */
    // private FieldMap fields = null;
    private Map<String, ObjectFieldSupport> fields = null;
    /**
     * <p>Flag indicating whether we should expose public fields as well as
     * properties as {@link FieldKey}s.</p>
     */
    private boolean includeFields = false;
    /**
     * <p>Map of {@link PropertyDescriptor}s for properties, keyed by property name.</p>
     */
    private Map<String, ObjectPropertySupport> props = null;

    /**
     * <p>Construct a new support instance wrapping the specified class,
     * with the specified flag for including public fields.</p>
     *
     * <p><strong>WARNING</strong> - Instances of this class will not be
     * <code>Serializable</code>, so callers should not attempt to save
     * fields containing such instances.</p>
     *
     * @param clazz Class whose properties should be exposed
     * @param includeFields Flag indicating whether public fields should
     *  also be included
     */
    public ObjectRowSupport(TableServices context, Class clazz, Class stopClazz, boolean includeFields) {
        this.context = context;
        this.clazz = clazz;
        this.stopClazz = stopClazz;
        if (stopClazz == null) {
            this.stopClazz = clazz.getSuperclass();
        }
        this.includeFields = includeFields;
        introspect();

    }
    private static Object[] EMPTY = new Object[0];
    public Class getObjectClass() {
        return this.clazz;
    }
    /**
     * <p>Return the read only state of the field associated with the
     * specified {@link FieldKey}, if it can be determined, otherwise,
     * return <code>true</code>.</p>
     *
     * @param fieldName
     * @return 
     */
    public boolean isReadOnly(String fieldName) {
        ObjectPropertySupport ops = props.get(fieldName);
        if (ops != null) {
            return ops.getWriteMethod() == null;
        }
        if (includeFields) {
            ObjectFieldSupport ofs = fields.get(fieldName);
            if (ofs != null) {
                return ofs.isReadOnly();
            }
        }
        return true;

    }

    /**
     * <p>Return the value for the specified {@link FieldKey}, from the
     * specified base object.</p>
     *
     * @param fieldName  for the requested field
     * @param bean Base object to be used
     * @return 
     */
    @Override
    public Object getValue(String fieldName, Object bean) {

        if (bean == null) {
            return null;
        }

        ObjectPropertySupport ops = (ObjectPropertySupport) props.get(fieldName);
        if (ops != null && ops.getReadMethod() != null) {
            try {
                return ops.getReadMethod().invoke(bean, EMPTY);
            } catch (Exception ex) {
                return null;
            }
        }
        if (includeFields) {
            ObjectFieldSupport ofs = (ObjectFieldSupport) fields.get(fieldName);
            Field field = ofs.getField();
            if (field != null) {
                try {
                    return field.get(bean);
                } catch (Exception ex) {
                    return null;
                }
            }
        }
        return null;

    }

    /**
     * <p>Set the value for the specified {@link FieldKey}, on the
     * specified base object.</p>
     *
     * @param fieldName 
     * @param bean 
     * @param value Value to be set
     *
     * @exception IllegalArgumentException if a type mismatch occurs
     * @exception IllegalStateException if setting a read only field
     *  is attempted
     */
    @Override
    public void setValue(String fieldName, Object bean, Object value) {
        ObjectPropertySupport ops = props.get(fieldName);
        if (ops != null) {
            if (ops.getWriteMethod() == null) {
                return;
            }
            try {
                ops.getWriteMethod().invoke(bean, new Object[]{value});
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (Exception e) {
            }
        }
        if (includeFields) {
            ObjectFieldSupport ofs = fields.get(fieldName);
            if (ofs == null) {
                return;
            }
            Field field = ofs.getField();
            if (field != null && !ofs.isReadOnly()) {
                try {
                    field.set(bean, value);
                } catch (Exception e) {
                }
            }
        }

    }
    // --------------------------------------------------------- Private Methods
    //protected abstract FieldMap createFieldMap();
    private DataColumn columnForProperty(String propName) {
        //DataColumnCollection columns = this.table.getColumns();
        DataColumn result = null;

        for (int i = 0; i < context.getColumnServices().getColumnCount(); i++) {
            if (context.getColumnServices().columns(i).getPropertyName().equals(propName)) {
                result = context.getColumnServices().columns(i);
                break;
            }
        }
        return result;
    }

    private DataColumn columnForField(String fieldName) {
        //DataColumnCollection columns = this.table.getColumns();
        DataColumn result = null;

        for (int i = 0; i < context.getColumnServices().getColumnCount(); i++) {
            if (context.getColumnServices().columns(i).getFieldName().equals(fieldName)) {
                result = context.getColumnServices().columns(i);
                break;
            }
        }
        return result;
    }

    /**
     * <p>Introspect the public properties (and optionally the public fields)
     * of the class we are wrapping.</p>
     */
    private void introspect() {

        props = new HashMap<String, ObjectPropertySupport>();
        if (includeFields) {
            //fields = this.createFieldMap();
            fields = new HashMap<String, ObjectFieldSupport>();
        }

        boolean haveColumns = false; // The table doesn'n have columns defined

        //DataColumnCollection columns = this.table.getColumns();
        if (context.getColumnServices().hasColumns() && context.getColumnServices().getColumnCount(DataColumn.DATA_KIND) > 0) {
            haveColumns = true;
        // Introspect the properties and fields of the specified class
        }
        try {
            BeanInfo binfo = Introspector.getBeanInfo(clazz, stopClazz);
            PropertyDescriptor[] cprops = binfo.getPropertyDescriptors();

            for (int i = 0; i < cprops.length; i++) {
                if (haveColumns) {
                    DataColumn dc = this.columnForProperty(cprops[i].getName());
                    this.props.put(cprops[i].getName(), new ObjectPropertySupport(cprops[i], dc));
                } else {
                    this.props.put(cprops[i].getName(), new ObjectPropertySupport(cprops[i]));
                }
            }//for
            if (includeFields) {
                Field[] cfields = clazz.getFields();
                for (int i = 0; i < cfields.length; i++) {
                    if (((cfields[i].getModifiers() & Modifier.PUBLIC) != 0) &&
                            !this.props.containsKey(cfields[i].getName())) {
                        if (haveColumns) {
                            DataColumn dc = this.columnForField(cfields[i].getName());
                            this.fields.put(cfields[i].getName(), new ObjectFieldSupport(cfields[i], dc));
                        } else {
                            this.fields.put(cfields[i].getName(), new ObjectFieldSupport(cfields[i]));
                        }
                    }
                }
            }
        } catch (IntrospectionException ex) {
        }

        if (!haveColumns) {
            createColumns();
        }
    }

    protected abstract void createColumns();

    protected Map<String, ObjectFieldSupport> getFieldMap() {
        return this.fields;
    }

    protected Map<String, ObjectPropertySupport> getPropertyMap() {
        return this.props;
    }

    public void setContext(TableServices context) {
        this.context = context;
    }

    public TableServices getContext() {
        return this.context;
    }

    @Override
    public boolean getIncludeFields() {
        return this.includeFields;
    }

    protected String produceColumnName() {

        //DataColumnCollection columns = this.getTable().getColumns();
        ColumnServices cs = context.getColumnServices();
        String template = "COL_";
        String s;
        int p = -1;
        DataColumn dc;//My 06.03.2012 = null;
        for (int i = 0; i < cs.getColumnCount(); i++) {
            dc = cs.columns(i);
            s = dc.getName().toUpperCase();
            if (s.startsWith(template)) {
                p = Math.max(Integer.parseInt(s.substring(template.length())), p);
            }
        }//for

        return template + String.valueOf(++p);
    }
    
    @Override
    public int size() {
        int p = props == null ? 0 : props.size();
        int f = fields == null ? 0 : fields.size();
        return p + f;
    }
}
