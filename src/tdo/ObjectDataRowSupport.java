/*
 * ObjectDataRowSupport.java
 *
 */

package tdo;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import tdo.service.TableServices;

/**
 *
 * @author Valera
 */
public class ObjectDataRowSupport extends ObjectRowSupport implements Serializable{
    
    /** Creates a new instance of ObjectDataRowSupport 
     * @param context 
     * @param clazz 
     * @param stopClazz 
     * @param includeFields 
     */
    public ObjectDataRowSupport(TableServices context, Class clazz,Class stopClazz,
            boolean includeFields) {
        super(context,clazz,stopClazz,includeFields);
    }
    @Override
    protected void createColumns() {
        DataColumnCollection columns = getContext().getCoreServices().getTable().getColumns();
        
        Map<String,ObjectPropertySupport> pmap = this.getPropertyMap();
        Map<String,ObjectFieldSupport> fmap = this.getFieldMap();
        
        int pmapSize = pmap == null ? 0 : pmap.size();
        int fmapSize = fmap == null ? 0 : fmap.size();
        
        int i = 0;
        Iterator<ObjectPropertySupport> piterator = pmap.values().iterator();
        while ( piterator.hasNext()) {
            ObjectPropertySupport ps = piterator.next();
            String cn = ps.getPropertyDescriptor().getName().toUpperCase();
            if ( getContext().getColumnServices().getColumnIndex(cn) >= 0 ) {
                cn = produceColumnName();
            }
            Class cl = ps.getPropertyDescriptor().getPropertyType();
            if ( cl == null )
                cl = java.lang.Object.class;
            
            DataColumn dc = columns.add(cl,cn);
            dc.setPropertyName(ps.getPropertyDescriptor().getName());
            dc.setReadOnly( ps.isReadOnly() );
        }
        
        if ( ! this.getIncludeFields() )
            return;
        
        i = 0;
        Iterator<ObjectFieldSupport> fiterator = fmap.values().iterator();
        while ( fiterator.hasNext()) {
            ObjectFieldSupport ps = fiterator.next();
            String fname = ps.getField().getName();
            String cn = fname.toUpperCase();
            if ( columns.find(cn) >= 0 ) {
                cn = produceColumnName();
            }
            Class cl = ps.getField().getType();
            if ( cl == null )
                cl = java.lang.Object.class;
            
            DataColumn dc = columns.add(cl,cn);
            dc.setFieldName(fname);
            dc.setReadOnly( ps.isReadOnly() );
        }

    }
    
    
    
}
