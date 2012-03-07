package tdo;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSetMetaData;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import tdo.util.Strings;

/**
 * The <code>DataColumn</code> object specifies the schema for a single column in a DataTable.<p>
 * The class is defined as an abstract class and serves as base class for all implementations
 * specific for a given type.<p> 
 * Ниже приведен список предопределенных классов, наследующих DataColumn. 
 * Эти классы реализованы как внутреннне и предназначениы для обработки 
 * данных в форматах, соответствующих форматам полей в таблицах баз данных <p>
 * <UL>
 *   <LI>{@link DataColumn.PDBBigIntColumn}</LI>
 *   <LI>{@link DataColumn.PDBBitColumn}</LI>
 *   <LI>{@link DataColumn.PDBDateColumn}</LI>
 *   <LI>{@link DataColumn.PDBDecimalColumn}</LI>
 *   <LI>{@link DataColumn.PDBDoubleColumn}</LI>
 *   <LI>{@link DataColumn.PDBIntegerColumn}</LI>
 *   <LI>{@link DataColumn.PDBJavaObjectColumn}</LI>
 *   <LI>{@link DataColumn.PDBRealColumn}</LI>
 *   <LI>{@link DataColumn.PDBSmallIntColumn}</LI>
 *   <LI>{@link DataColumn.PDBStringColumn}</LI>
 *   <LI>{@link DataColumn.PDBTimeColumn}</LI>
 *   <LI>{@link DataColumn.PDBTimestampColumn}</LI>
 *   <LI>{@link DataColumn.PDBTinyIntColumn}</LI>
 * </UL>
 * 
 * Класс содержит множество свойств, большинство из которых связано с метаданными
 * класса <code>java.sql.ResultSetMetaData</code> и, как правило, не используется
 * приложением, но могут быть полезными, например, при создании таблиц баз данных.<p>
 * Обычно пользователь не будет использовать конструкторы перечисленных классов.
 * Вместо них классом {@link tdo.DataColumnCollection} предоставляются более 
 * удобные методы <code>addXXX</code>.
 * 
 * 
 * @version 1.0
 */
public abstract class DataColumn implements Cloneable, tdo.expr.Comparable, Serializable {

    /**
     * Используется для установки и/или доступа к значению свойства 
     * <b>{@link #kind}</b>. Указывает, что колонка описывает реальные данные.
     * @see #getKind
     * @see #setKind
     */
    public static final int DATA_KIND = 10;
    /**
     * Используется для установки и/или доступа к значению свойства 
     * <b>{@link #kind}</b>. Указывает, что колонка описывает вычисляемые данные.
     * @see #getKind
     * @see #setKind
     */
    public static final int CALC_KIND = 11;
    /**
     * Используется для установки и/или доступа к значению свойства 
     * <b>{@link #kind}</b>. Указывает, что колонка описывает вычисляемые 
     * lookup-данные. В данной версии не используется.
     * @see #getKind
     * @see #setKind
     */
    public static final int LOOKUP_KIND = 12;
    /**
     * Используется для группового доступа ко всем колонкам.
     * @see tdo.DataColumnCollection#getCount(int)
     * @see tdo.DataColumnCollection#setCount(int,int)
     */
    public static final int ALL_KIND = 14;
    /**
     * Используется для группового доступа только к вычисляемым и lookup-колонкам.
     * @see tdo.DataColumnCollection#getCount(int)
     * @see tdo.DataColumnCollection#setCount(int,int)
     */
    public static final int CALC_AND_LOOKUP = 15;
    private int kind = DATA_KIND;
    private String name = null;
    private int sqlType = java.sql.Types.NULL;
    private Class type;
    private boolean readOnly = false;
    private int size = 10;
    private int precision = 10;
    private int scale = 2;
    private int nullable = ResultSetMetaData.columnNullable;
    private boolean autoIncrement = false;
    private boolean caseSensitive;
    private boolean currency = false;
    private boolean signed;
    private boolean searchable;
    private String label;
    private String tableName; // as in sql db table

    private String schemaName; // as in sql db table

    private int cellIndex;
    /**
     * Хранит значение свойства POJO, когда в качестве ряда таблицы выступает
     * java bean.
     */
    private String propertyName = null;
    private String fieldName = null;
    private String expression;
    private ColumnExpressionContext columnExpression;
    /**
     * Индекс  в коллекции {@link DataColumnCollection}.<p>
     * Если колонка находится в коллекции, то последняя должна обеспечить
     * поддержку значения данного свойства при вставке, добавлении или
     * удалении колонки из коллекции.<p>
     * Значение по умолчанию равно -1 и устанавливается при дабавлении колонки
     * в DataColumnCollection.
     * @see #getIndex
     * @see #setIndex
     * @see tdo.DataColumnCollection
     */
    private int index = -1;
    private PropertyChangeSupport changeSupport;
    private Object defaultValue;

    /**
     * getter-метод свойства <code>name</code>.
     * @return строковое значение <b>имени</b> колонки - свойство <code>name</code>.
     * @see #setName
     */
    public String getName() {
        return this.name;
    }

    /**
     * Устанавливает строковое значение <b>имени</b> колонки - свойство <code>name</code>.
     * Перед установкой значение параметра преобразуется в верхний регистр.
     * Возбуждает событие <code>PropertyChangeEvent</code>. <p>
     * Побочной функцией метода является установка значения свойства 
     * <code>label</code>. Значение параметра <code>name</code> может 
     * представлять собой строку состоящую из двух частей, разделенных запятой.
     * Первая часть до запятой - значение свойства <code>name</code>. 
     * Вторая часть после запятой - значение свойства <code>label</code>. <p>
     * Если значение параметра в действительности состоит из двух частей, то свойство 
     * <code>label</code> устанавливается независимо от своего текущего значения.<p>
     * Если значение параметра содержит только имя колонки, т.е. состоит из 
     * одной части, то проверяется текущее значение свойства <code>label</code>.
     * Если равно <code>null</code>, то значение свойства <code>label</code> 
     * устанавливается равным значению параметра (не свойства) <code>name</code>.
     * 
     * @param name значение имени колонки. Должно быть отлично от <code>null</code>.
     * @see #getName
     * @see #firePropertyChange
     * @see #getLabel
     * @see #setLabel
     * @throws IllegalArgumentException , когда значение параметра равно 
     * <code>null</code> или длина строки равна 0 или первым символом в значении
     * параметра является запятая.
     */
    public void setName(String name) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("Column name cannot be null and length must be  > 0 ");
        }
        String pname = name.trim();
        if (pname.startsWith(",")) {
            throw new IllegalArgumentException("First letter cannot be comma");
        }
        String oldValue = this.name;
        String[] nmlb = Strings.split(pname);
        this.name = nmlb[0].toUpperCase();
        if (nmlb.length > 1) {
            this.label = nmlb[1];
        }
        this.firePropertyChange("name", oldValue, this.name);
        if (this.label == null) {
            this.setLabel(name);
        }

    }

    /**
     * Возвращает строковое значение выражения, используемого для вычисляемых
     * колонок. <p> 
     * @see DataColumnCollection#addCalculated(java.lang.Class, java.lang.String) 
     * @see DataColumnCollection#addCalculated(int, java.lang.String) ) 
     * @see DataColumnCollection#addCalculated(int, java.lang.String, int, int)  
     * 
     * @return  строковое значение выражения, используемого для вычисляемых
     * колонок. 
     */
    public String getExpression() {
        return this.expression;
    }

    /**
     * Устанавливает значение выражения, используемого для вычисляемых
     * колонок. <p> 
     * <b>Примечание.</b> Помимо установки значения свойства 
     * <code>expression</code>, метод создает экземпляр класса 
     * {@link tdo.ColumnExpressionContext} и устанвливает значение свойства 
     * {@link #columnExpression}. 
     * @param expr новое значение свойства <code>expression</code>. Если равно 
     * <code>null</code> или длина строки без начальных и конечных пробелов равна
     * 0, то <code>expression</code> и <code>columnExpressiont</code> 
     * устанавливаются в <code>null</code>.
     * 
     * @see #getColumnExpression
     * @see DataColumnCollection#addCalculated(java.lang.Class, java.lang.String) 
     * @see DataColumnCollection#addCalculated(int, java.lang.String) ) 
     * @see DataColumnCollection#addCalculated(int, java.lang.String, int, int)  
     * @see tdo.ColumnExpressionContext
     */
    public void setExpression(String expr) {
        this.expression = expr;
        if (expr != null && expr.trim().length() > 0) {
            columnExpression = new ColumnExpressionContext(expr);
        } else {
            this.expression = null;
            columnExpression = null;
        }
    }

    /**
     * Возвращает значение контекста выражения колонки.<p>
     * @return значение свойства columnExpression
     * @see #setExpression
     */
    public ColumnExpressionContext getColumnExpression() {
        return this.columnExpression;
    }

    // Returns the type of the values for this DataColumn as a Java Class.
    /**
     * Возвращает тип для значений, определяемых колонкой как Java Class.
     * 
     * @return тип для значений, определяемых колонкой как Java Class.
     */
    public Class getType() {
        return type;
    }

    /**
     * Устанавливает новое значение свойства <code>type</code> - тип значений
     * для данных, описываемых колонкой как Jacva Class. <p> 
     * Модификатор доступа метода = <code>protected</code>. Пользователь, если 
     * только он не разработчик нового класса, наследующего 
     * <code>DataColumn</code>, не может использовать метод. Значение свойства
     * определяется классом - наследником <code>DataColumn</code>.
     * 
     * @param type новое значение типа значений, определяемых колонкой
     */
    protected void setType(Class type) {
        this.type = type;
    }

    /**
     * Возвращает sql-тип значений, описываемых колонкой, как определено
     * константами класса <code>java.sql.Types</code>.<p>
     * Значение свойства <code>sqlType</code> устанавливается одним из следующих
     * способов:
     * <ol>
     *   <li>Применением оператора <code>new</code> к конструктору без параметров
     *       для одного из предопределенных классов-наследников 
     *       <code>DataColumn</code>:
     *       <UL>
     *          <LI>{@link DataColumn.PDBBigIntColumn}. Значение свойства 
     *              sqlType устанавливается равным 
     *              <code>java.sql.Types.BIGINT</code>.
     *              Значение свойства <code>type</code> устанавливается равным 
     *              java.lang.Long</code>.
     *          </LI>
     *          <LI>{@link DataColumn.PDBBitColumn}. Значение свойства 
     *              sqlType устанавливается равным 
     *              <code>java.sql.Types.BIT</code>.
     *              Значение свойства <code>type</code> устанавливается равным 
     *              java.lang.Boolean</code>.
     *          </LI>
     *          <LI>{@link DataColumn.PDBDateColumn}. Значение свойства 
     *              sqlType устанавливается равным 
     *              <code>java.sql.Types.DATE</code>.
     *              Значение свойства <code>type</code> устанавливается равным 
     *              java.util.Date</code>.
     *          </LI>
     *          <LI>{@link DataColumn.PDBDecimalColumn}. Значение свойства 
     *              sqlType устанавливается равным 
     *              <code>java.sql.Types.DECIMAL</code>.
     *              Значение свойства <code>type</code> устанавливается равным 
     *              java.math.BigDecimal</code>.
     *          </LI>
     *          <LI>{@link DataColumn.PDBDoubleColumn}. Значение свойства 
     *              sqlType устанавливается равным 
     *              <code>java.sql.Types.DOUBLE</code>.
     *              Значение свойства <code>type</code> устанавливается равным 
     *              java.lang.Double</code>.
     *          </LI>
     *          <LI>{@link DataColumn.PDBIntegerColumn}. Значение свойства 
     *              sqlType устанавливается равным 
     *              <code>java.sql.Types.INTEGER</code>.
     *              Значение свойства <code>type</code> устанавливается равным 
     *              java.lang.Integer</code>.
     *          </LI>
     *          <LI>{@link DataColumn.PDBJavaObjectColumn}. Значение свойства 
     *              sqlType устанавливается равным 
     *              <code>java.sql.Types.OTHER</code>.
     *              Значение свойства <code>type</code> устанавливается равным 
     *              java.lang.Object</code>.
     *          </LI>
     *          <LI>{@link DataColumn.PDBRealColumn}. Значение свойства 
     *              sqlType устанавливается равным 
     *              <code>java.sql.Types.REAL</code>.
     *              Значение свойства <code>type</code> устанавливается равным 
     *              java.lang.Float</code>.
     *          </LI>
     *          <LI>{@link DataColumn.PDBSmallIntColumn}. Значение свойства 
     *              sqlType устанавливается равным 
     *              <code>java.sql.Types.SMALLINT</code>.
     *              Значение свойства <code>type</code> устанавливается равным 
     *              java.lang.Short</code>.
     *          </LI>
     *          <LI>{@link DataColumn.PDBStringColumn}. Значение свойства 
     *              sqlType устанавливается равным 
     *              <code>java.sql.Types.CHAR</code>.
     *              Значение свойства <code>type</code> устанавливается равным 
     *              java.lang.String</code>.
     *          </LI>
     *          <LI>{@link DataColumn.PDBTimeColumn}. Значение свойства 
     *              sqlType устанавливается равным 
     *              <code>java.sql.Types.TIME</code>.
     *              Значение свойства <code>type</code> устанавливается равным 
     *              java.sql.Time</code>.
     *          </LI>
     *          <LI>{@link DataColumn.PDBTimestampColumn}. Значение свойства 
     *              sqlType устанавливается равным 
     *              <code>java.sql.Types.TIMESTAMP</code>.
     *              Значение свойства <code>type</code> устанавливается равным 
     *              java.lang.Timestamp</code>.
     *          </LI>
     *          <LI>{@link DataColumn.PDBTinyIntColumn}. Значение свойства 
     *              sqlType устанавливается равным 
     *              <code>java.sql.Types.TINYINT</code>.
     *              Значение свойства <code>type</code> устанавливается равным 
     *              java.lang.Byte</code>.
     *          </LI>
    
     *       </UL>
     *   </li>  
     *   <li>Использованием одного из перегруженных методов <b>addXXX</b> класса 
     *       {@link tdo.DataColumnCollection}, первым параметром которого 
     *       является <code>sqlType</code>:
     *          <ul>
     *              <li>{@link tdo.DataColumnCollection#add(int)}</li>
     *              <li>{@link tdo.DataColumnCollection#add(int,String,int,int)};
     *              </li>
     *              <li>{@link tdo.DataColumnCollection#addCalculated(int,String)};
     *              </li>
     *              <li>{@link tdo.DataColumnCollection#addCalculated(int,String,int,int)};
     *              </li>
     *          </ul> 
     *          В этом случае, используя параметр, соответствующий 
     *          <code>sqlType</code> выбирается подходящий класс-наследник 
     *          <code>DataColumn</code> и создается его экземпляр, а именно:
     *          <ul>
     *              <li>Для sqlType равного <code>java.sql.Types.INTEGER</code>
     *                   соответствует класс {@link PDBIntegerColumn}
     *              </li>               
     *              <li>Для sqlType равного <code>java.sql.Types.BIGINT</code>
     *                   соответствует класс {@link PDBBigIntColumn}
     *              </li>               
     *              <li>Для sqlType равного <code>java.sql.Types.SMALLINT</code>
     *                   соответствует класс {@link PDBSmallIntColumn}
     *              </li>               
     *              <li>Для sqlType равного <code>java.sql.Types.TINYINT</code>
     *                   соответствует класс {@link PDBTinyIntColumn}
     *              </li>               
     *              <li>Для sqlType равного <code>java.sql.Types.REAL</code>
     *                   соответствует класс {@link PDBRealColumn}
     *              </li>               
     *              <li>Для sqlType равного <code>java.sql.Types.FLOAT</code>
     *                   соответствует класс {@link PDBDoubleColumn}
     *              </li>               
     *              <li>Для sqlType равного <code>java.sql.Types.DOUBLE</code>
     *                   соответствует класс {@link PDBDoubleColumn}
     *              </li>               
     *              <li>Для sqlType равного <code>java.sql.Types.VARCHAR</code>
     *                  соответствует класс {@link PDBStringColumn} и 
     *                  устанавливается для его экземпляра значение свойства
     *                  <code>sqlType</code> равным <code>java.sql.Types.CHAR</code>
     *              </li>               
     *              <li>Для sqlType равного <code>java.sql.Types.CHAR</code>
     *                  соответствует класс {@link PDBStringColumn} и 
     *                  устанавливается для его экземпляра значение свойства
     *                  <code>sqlType</code> равным <code>java.sql.Types.CHAR</code>
     *              </li>               
     *              <li>Для sqlType равного <code>java.sql.Types.LONGVARCHAR</code>
     *                  соответствует класс {@link PDBStringColumn} и 
     *                  устанавливается для его экземпляра значение свойства
     *                  <code>sqlType</code> равным <code>java.sql.Types.LONGVARCHAR</code>
     *              </li>               
     *              <li>Для sqlType равного <code>java.sql.Types.NUMERIC</code>
     *                  соответствует класс {@link PDBDecimalColumn} и 
     *                  устанавливается для его экземпляра значение свойства
     *                  <code>sqlType</code> равным <code>java.sql.Types.NUMERIC</code>
     *              </li>               
     *              <li>Для sqlType равного <code>java.sql.Types.DECIMAL</code>
     *                  соответствует класс {@link PDBDecimalColumn} и 
     *                  устанавливается для его экземпляра значение свойства
     *                  <code>sqlType</code> равным <code>java.sql.Types.DECIMAL</code>
     *              </li>               
     *              <li>Для sqlType равного <code>java.sql.Types.TIMESTAMPL</code>
     *                  соответствует класс {@link PDBTimestampColumn} и 
     *                  устанавливается для его экземпляра значение свойства
     *                  <code>sqlType</code> равным <code>java.sql.Types.TIMESTAMPO</code>
     *              </li>               
     *              <li>Для sqlType равного <code>java.sql.Types.TIME</code>
     *                  соответствует класс {@link PDBTimeColumn} и 
     *                  устанавливается для его экземпляра значение свойства
     *                  <code>sqlType</code> равным <code>java.sql.Types.Time</code>
     *              </li>               
     *              <li>Для sqlType равного <code>java.sql.Types.DATE</code>
     *                  соответствует класс {@link PDBDateColumn} и 
     *                  устанавливается для его экземпляра значение свойства
     *                  <code>sqlType</code> равным <code>java.sql.Types.DATE</code>
     *              </li>               
     *              <li>Для sqlType равного <code>java.sql.Types.BIT</code>
     *                  соответствует класс {@link PDBBitColumn} и 
     *                  устанавливается для его экземпляра значение свойства
     *                  <code>sqlType</code> равным <code>java.sql.Types.BIT</code>
     *              </li>               
     *              <li>Для sqlType равного <code>java.sql.Types.OTHER</code>
     *                  соответствует класс {@link PDBJavaObjectColumn} и 
     *                  устанавливается для его экземпляра значение свойства
     *                  <code>sqlType</code> равным <code>java.sql.Types.OTHER</code>
     *              </li>               
     *          </ul>
     *   </li>
     *   <li>Использованием метода {@link tdo.impl.AbstractTable#populate(java.sql.ResultSet)}.
     *       В этом случае <code>sqlType</code> каждой колонки извлекается из метаданных
     *       путем вызова <code>ResultSetMetaData.getColumnType</code>. 
     *       Дальнейшие действия выполняются аналогично предыдущему пункту 2.
     *   </li>
    
     * </ol>
     * @return целое значение как определено класссом <code>java.sql.Types</code>. 
     */
    public int getSqlType() {
        return this.sqlType;
    }

    /**
     * Устанавливает значение свойства <code>sqlType</code> равным значению параметра.
     * 
     * @param sqlType новое значение sql-типа, как определено классом 
     *        <code>java.sql.Type</code>.
     * @see #getSqlType() 
     */
    protected void setSqlType(int sqlType) {
        this.sqlType = sqlType;
    }

    /**
     * Возвращает значение, информирующее можно ли изменять данные, описываемые колонкой.
     * @return <code>false</code>, если данные, которые описывает колонка разрешается
     *  редактировать. <code>true</code> - в противном случае.
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Устанавливает значение флага, определяющего допускается или нет 
     * редактирование данных, соответствующих колонке.
     * По умолчанию для колонок, свойство <code> kind</code> которых равно 
     * {@link DataColumn#CALC_KIND} значение свойства должно быть <code>true</code>.
     * Метод не производит никаких действий для таких колонок.
     * Для колонок, свойство <code>kind</code> которых равно 
     * {@link DataColumn#DATA_KIND} значение свойства по умолчанию установлено
     * в <code>false</code>, но может быть изменено на <code>true</code>.
     * 
     * @param readOnly равный <code>true</code> запрещает редактирование. 
     *      Значение равное <code>false</code> позволяет редактирование.
     * @see #isReadOnly
     * @see #setKind
     */
    public void setReadOnly(boolean readOnly) {
        if (this.getKind() == CALC_KIND) {
            return;
        }
        this.readOnly = readOnly;
    }

    /**
     * Возвращает размер данных, соответствующих колонке. 
     * <p>Значение этого свойства всегда равно значению свойства 
     * <code>precision</code> и всегда изменяется при изменении последнего.<p>
     * Когда колонка создается на основе <code>ResultSetMetaData</code>, то 
     * значение его устанавливается равным 
     * <code>ResultSetMeteData.getPrecision</code>. <p>
     * Когда колонка создается конструктором одного из предопределенных
     * классов-наследников <code>DataColumn</code>, то значение зависит 
     * от класса следующим образом:
     * <UL>
     *   <LI>{@link DataColumn.PDBBigIntColumn}     : (20,0)</LI>
     *   <LI>{@link DataColumn.PDBBitColumn}        : (?)</LI>
     *   <LI>{@link DataColumn.PDBDateColumn}       : (?)</LI>
     *   <LI>{@link DataColumn.PDBDecimalColumn}    : (15,2)</LI>
     *   <LI>{@link DataColumn.PDBDoubleColumn}     : (15,-1)</LI>
     *   <LI>{@link DataColumn.PDBIntegerColumn}    : (10,0)</LI>
     *   <LI>{@link DataColumn.PDBJavaObjectColumn} : (?) </LI>
     *   <LI>{@link DataColumn.PDBRealColumn}       : (15,2)</LI>
     *   <LI>{@link DataColumn.PDBSmallIntColumn}   : (6,0)</LI>
     *   <LI>{@link DataColumn.PDBStringColumn}     : (?)</LI>
     *   <LI>{@link DataColumn.PDBTimeColumn}       : (?)</LI>
     *   <LI>{@link DataColumn.PDBTimestampColumn}  : (?)</LI>
     *   <LI>{@link DataColumn.PDBTinyIntColumn}    : (4,0)</LI>
     * </UL>
     * <b>Примечание:</b> там, где указано (?) на самом деле будет использовано
     * (10,2). <p>
     * Важно отметить, что значение свойства <code>size</code> (а также свойства
     * <code>scale</code> и <code>precision</code> ) нигде в рамках 
     * TDO Framework не используется.  Возможным применением в приложении могут 
     * быть DDL-операции над базой данных.
     * 
     * Get the column's specified column size. For numeric data, 
     * this is the maximum precision. For character data, this is the length 
     * in characters. For datetime datatypes, this is the length in characters
     * of the String representation (assuming the maximum allowed precision 
     * of the fractional seconds component). For binary data, this is the length
     * in bytes. For the ROWID datatype, this is the length in bytes. 
     * 0 is returned for data types where the column size is not applicable.
     * 
     * @return размер данных, соответствующих колонке. 
     * @see #getPrecision
     * @see #setPrecision
     * @see #getScale
     * @see #setScale
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Устанавливает размер данных, соответствующих колонке. <p>
     * Значение свойства <code>precision</code> устанавливается равным значению
     * <code>size</code>.
     * 
     * @param size новое значение
     * @see #getSize
     * @see #getPrecision
     * @see #setPrecision
     * @see #getScale
     * @see #setScale
     */
    public void setSize(int size) {
        int old = this.size;
        this.precision = size;
        this.size = size;
    }

    /**
     * Возвращает размер данных, соответствующих колонке. 
     * <p>Значение этого свойства всегда равно значению свойства 
     * <code>size</code> и всегда изменяется при изменении последнего.<p>
     * @return размер данных, соответствующих колонке. 
     * @see #getSize
     * @see #setPrecision
     * @see #getScale
     * @see #setScale
     */
    public int getPrecision() {
        return this.precision;
    }

    /**
     * Устанавливает размер данных, соответствующих колонке. <p>
     * Значение свойства <code>size</code> устанавливается равным значению
     * <code>precision</code>.
     * 
     * @param precision новое значение размера данных, представляемых колонкой.
     * @see #getSize
     * @see #setSize
     * @see #getPrecision
     * @see #getScale
     * @see #setScale
     */
    public void setPrecision(int precision) {
        int old = this.precision;
        this.size = precision;
        this.precision = precision;
    }

    /**
     * Возвращает значение <i>scale</i> для данных, представляемых колонкой.<p>
     * Предполагается, что свойство <code>scale</code> используется для данных 
     * типа <code>java.math.BigDecimal</code>.
     * 
     * @return <code>scale</code>
     * @see #setScale 
     * @see #getPrecision
     * @see #getSize
     * @see java.math.BigDecimal
     */
    public int getScale() {
        return this.scale;
    }

    /**
     * Устанавливает новое значение <i>scale</i> для данных, представляемых колонкой.<p>
     * Предполагается, что свойство <code>scale</code> используется для данных 
     * типа <code>java.math.BigDecimal</code>.
     * @param scale
     * @see #getScale 
     * @see #getPrecision
     * @see #getSize
     * @see java.math.BigDecimal
     */
    public void setScale(int scale) {
        int old = this.scale;
        this.scale = scale;
    }

    /**
     * Возвращает значение, указывающее, является ли колонка автоматически
     * нумеруемой. <p>
     * Свойства <code>autoIncrement</code> соответствует одноименному свойству
     * класса <code>java.sql.ResultSetMeteData</code>. Обычно заполняется, когда
     * <code>DataColumn</code> используется в связи 
     * <code>java.sql.ResutSet</code>. 
     * <p>В классах TDO Framework нигде не используется. Возможное применение 
     * - генерирование SQL DDL создания таблицы базы данных.
     * 
     * @return autoIncrement
     */
    public boolean isAutoIncrement() {
        return this.autoIncrement;
    }

    /**
     * Устанавливает новое значение, указывающее, является ли колонка 
     * автоматически нумеруемой. <p>
     * Свойства <code>autoIncrement</code> соответствует одноименному свойству
     * класса <code>java.sql.ResultSetMeteData</code>. Обычно заполняется, когда
     * <code>DataColumn</code> используется в связи с 
     * <code>java.sql.ResutSet</code>. 
     * <p>В классах TDO Framework нигде не используется. Возможное применение 
     * - генерирование SQL DDL создания таблицы базы данных.
     * 
     * @param value новое значение. <code>true</code> колонка автонумеруемая. 
     *          <code>false</code> - в противном случае
     */
    public void setAutoIncrement(boolean value) {
        this.autoIncrement = value;
    }

    /**
     * Возвращает значение, указывающее, являются ли данные описываемые колонкой
     * чувствительными к регистру<p>
     * Свойство <code>caseSensitive</code> соответствует одноименному свойству
     * класса <code>java.sql.ResultSetMeteData</code>. Обычно заполняется, когда
     * <code>DataColumn</code> используется в связи с использованием 
     * <code>java.sql.ResutSet</code>. 
     * <p>В классах TDO Framework нигде не используется. Возможное применение 
     * - генерирование SQL DDL создания таблицы базы данных.
     * 
     * @return caseSensitive
     */
    public boolean isCaseSensitive() {
        return this.caseSensitive;
    }

    /**
     * Устанавливает новое значение, указывающее, являются ли данные описываемые
     * колонкой чувствительными к регистру<p>
     * Свойство <code>caseSensitive</code> соответствует одноименному свойству
     * класса <code>java.sql.ResultSetMeteData</code>. Обычно заполняется, когда
     * <code>DataColumn</code> используется в связи с 
     * <code>java.sql.ResutSet</code>. 
     * <p>В классах TDO Framework нигде не используется. Возможное применение 
     * - генерирование SQL DDL создания таблицы базы данных.
     * 
     * @param  value . <code>true</code> - данные чувствительны к регистру. 
     * <code>false</code> - в противном случае.
     */
    public void setCaseSensitive(boolean value) {
        this.caseSensitive = value;
    }

    /**
     * Возвращает значение, указывающее, представляют ли данные описываемые колонкой
     * денежные значения<p>
     * Свойство <code>currency</code> соответствует одноименному свойству
     * класса <code>java.sql.ResultSetMeteData</code>. Обычно заполняется, когда
     * <code>DataColumn</code> используется в связи с 
     * <code>java.sql.ResutSet</code>. 
     * <p>В классах TDO Framework нигде не используется. Возможное применение 
     * - генерирование SQL DDL создания таблицы базы данных.
     * 
     * @return currency
     */
    public boolean isCurrency() {
        return this.currency;
    }

    /**
     * Устанавливает новое значение, указывающее, представляют ли данные 
     * описываемые колонкой денежные значения<p>
     * Свойство <code>currency</code> соответствует одноименному свойству
     * класса <code>java.sql.ResultSetMeteData</code>. Обычно заполняется, когда
     * <code>DataColumn</code> используется в связи с 
     * <code>java.sql.ResutSet</code>. 
     * <p>В классах TDO Framework нигде не используется. Возможное применение 
     * - генерирование SQL DDL создания таблицы базы данных.
     * 
     * @param value
     */
    public void setCurrency(boolean value) {
        this.currency = value;
    }

    /**
     * Возвращает значение, указывающее, представляют ли данные описываемые колонкой
     * числовые данные со знаком<p>
     * Свойство <code>signed</code> соответствует одноименному свойству
     * класса <code>java.sql.ResultSetMeteData</code>. Обычно заполняется, когда
     * <code>DataColumn</code> используется в связи с 
     * <code>java.sql.ResutSet</code>. 
     * <p>В классах TDO Framework нигде не используется. Возможное применение 
     * - генерирование SQL DDL создания таблицы базы данных.
     * 
     * @return signed
     */
    public boolean isSigned() {
        return this.signed;
    }

    /**
     * Устанавливает новое значение, указывающее, представляют ли данные 
     * описываемые колонкой числовые данные со знаком<p>
     * Свойство <code>signed</code> соответствует одноименному свойству
     * класса <code>java.sql.ResultSetMeteData</code>. Обычно заполняется, когда
     * <code>DataColumn</code> используется в связи с 
     * <code>java.sql.ResutSet</code>. 
     * <p>В классах TDO Framework нигде не используется. Возможное применение 
     * - генерирование SQL DDL создания таблицы базы данных.
     * 
     * @param value - новое значение свойства <code>signed</code>.
     */
    public void setSigned(boolean value) {
        this.signed = value;
    }

    /**
     * Возвращает значение, указывающее, можно ли использовать колонку 
     * в предложении <code>where</code>.<p>
     * Свойство <code>searchable</code> соответствует одноименному свойству
     * класса <code>java.sql.ResultSetMeteData</code>. Обычно заполняется, когда
     * <code>DataColumn</code> используется в связи с 
     * <code>java.sql.ResutSet</code>. 
     * <p>В классах TDO Framework нигде не используется. Возможное применение 
     * - генерирование SQL DDL создания таблицы базы данных.
     * 
     * @return searchable
     */
    public boolean isSearchable() {
        return this.searchable;
    }

    /**
     * Устанавливает новое значение, указывающее, можно ли использовать колонку 
     * в предложении <code>where</code>.<p>
     * Свойство <code>searchable</code> соответствует одноименному свойству
     * класса <code>java.sql.ResultSetMeteData</code>. Обычно заполняется, когда
     * <code>DataColumn</code> используется в связи с 
     * <code>java.sql.ResutSet</code>. 
     * <p>В классах TDO Framework нигде не используется. Возможное применение 
     * - генерирование SQL DDL создания таблицы базы данных.
     * 
     * @param value
     */
    public void setSearchable(boolean value) {
        this.searchable = value;
    }

    /**
     * Возвращает значение, которое может использоваться как заголовок
     * при печати и отображении на экране. <p>
     * 
     * @return значение свойства
     * @see #setLabel
     * @see #getName
     * @see #setName
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Устанавливает новое значение, которое может использоваться как заголовок
     * при печати и отображении на экране. <p>
     * Свойство <code>label</code> соответствует свойству 
     * <code>columnLabel</code> класса <code>java.sql.ResultSetMeteData</code>.
     * 
     * <p>Возбуждает событие <code>PropertyChangeEvent</code> в случае, если
     * новое значение не равно текущему.
     * 
     * @param label - новое значение свойства
     * @see #getLabel
     * @see #getName
     * @see #setName
    
     */
    public void setLabel(String label) {
        if (this.label != null && this.label.equals(label)) {
            return;
        }
        if (this.label == null && label == null) {
            return;
        }
        String oldLabel = this.label;
        this.label = label;

        firePropertyChange("label", oldLabel, this.label);
    }

    /**
     * Возвращает индекс колонки  в коллекции {@link DataColumnCollection},
     * если колонка уже содержится в коллекции.
     * Коллекция <code>DataColumnCollection</code> должна обеспечить
     * поддержку значения свойства <code>index</code> при вставке, добавлении мли
     * удалении колонки из коллекции.
     *
     * @return  - индекс колонки в колллекции
     * @see #setIndex
     * @see AbstractDataColumns
     */
/*    public int getIndex() {
        return this.index;
    }
*/
    /**
     * Устанавливает новое значение индекса колонки  в коллекции {@link DataColumnCollection},
     * если колонка уже содержится в коллекции.
     * Коллекция <code>DataColumnCollection</code> должна обеспечить
     * поддержку значения свойства <code>index</code> при вставке, добавлении мли
     * удалении колонки из коллекции. <p>
     * Установка нового значения свойства <code>index</code> производится, только,
     * если новое значение не совпадает с текущим. При этом возбуждается событие
     * <code>PropertyChangeEvent</code>. Реализация 
     * <code>DataColumnCollection<code><p> должна обеспечить правильную 
     * реорганизацию индексов, обрабатывая событие изменеия свойства.
     * @param index новое значение индекса колонки в коллекции
     * @see #getIndex
     * @see tdo.DataColumnCollection
     */
/*    public void setIndex(int index) {
        if (index == this.index) {
            return;
        }
        int oldIndex = this.index;
        this.index = index;
        this.firePropertyChange("index", oldIndex, index);
    }
*/
    /**
     * Возвращает значение индекса ячейки хранилища данных ряда.<p>
     * <code>DataColumn</code> используется совместно c {@link tdo.Table},
     * {@link tdo.DataRow} и т.д.. Свойство <code>cellIndex</code> указывает 
     * на индекс элементов объектов <code>DataRow</code>. Метод, вообще говоря,
     * должен был быть объявлен как private, protected или package. Объявление
     * его public связано с языковым ограничением.
     * @return cellIndex
     */
    public int getCellIndex() {
        return this.cellIndex;
    }

    /**
     * Устанавливает новое значение индекса ячейки хранилища данных ряда.<p>
     * <code>DataColumn</code> используется совместно c {@link tdo.Table},
     * {@link tdo.DataRow} и т.д.. Свойство <code>cellIndex</code> указывает 
     * на индекс элементов объектов <code>DataRow</code>. Метод, вообще говоря,
     * должен был быть объявлен как private, protected или package. Объявление
     * его public связано с языковым ограничением. Метод предназначен
     * для внутреннего использования и не следует использовать его в
     * приложении.
     * @param cellIndex 
     */
    protected void setCellIndex(int cellIndex) {
        this.cellIndex = cellIndex;
    }

    /**
     * Возвращает значение, определяющее могут ли данные, описываемые колонкой
     * принимать значение <code>null</code>.
     * Свойство <code>nullable</code> соответствует одноименному свойству
     * класса <code>java.sql.ResultSetMeteData</code>. Обычно заполняется, когда
     * <code>DataColumn</code> используется в связи с 
     * <code>java.sql.ResutSet</code>. В зависимости от реализации, значение 
     * свойства может влиять на результат, возвращаемый методом 
     * {@link #createBlankObject() }. Для проверки могут ли данные описываемые
     * колонкой принимать <code>null</code> значения рекомендуется использовать
     * метод {@link #isNullable}.
     * 
     * @return новое значение
     * @see #isNullable
     * @see #createBlankObject
     */
    public int getNullable() {
        return this.nullable;
    }

    /**
     * Устанавливает новое значение, определяющее могут ли данные, описываемые 
     * колонкой принимать значение <code>null</code>.
     * Свойство <code>nullable</code> соответствует одноименному свойству
     * класса <code>java.sql.ResultSetMeteData</code>. Обычно заполняется, когда
     * <code>DataColumn</code> используется в связи с 
     * <code>java.sql.ResutSet</code>. В зависимости от реализации, значение 
     * свойства может влиять на результат, возвращаемый методом 
     * {@link #createBlankObject() }. Для проверки могут ли данные описываемые
     * колонкой принимать <code>null</code> значения рекомендуется использовать
     * метод {@link #isNullable}.
     * 
     * @param nullable новое значение
     * @see #isNullable
     * @see #createBlankObject
     */
    public void setNullable(int nullable) {
        this.nullable = nullable;
    }

    /**
     * Возвращает значение, определяющее могут ли данные, описываемые колонкой
     * принимать значение <code>null</code>.
     * Свойство <code>nullable</code> соответствует одноименному свойству
     * класса <code>java.sql.ResultSetMeteData</code>. Обычно заполняется, когда
     * <code>DataColumn</code> используется в связи с 
     * <code>java.sql.ResutSet</code>. В зависимости от реализации, значение 
     * свойства может влиять на результат, возвращаемый методом 
     * {@link #createBlankObject() }. Класс <code>ResulSetMetaData</code>
     * определяет несколько констант, описывающих понятие <code>nullable</code>
     * с точки зрения возможной реализации реляционной СУБД. С точки зрения 
     * tdo.DataColumn колонка считается <code>nullable</code>, если значение
     * свойства принимает одно из двух значений : 
     * <ul>
     *   <li>ResultSetMetaData.columnNullable</li>
     *   <li>ResultSetMetaData.columnNullableUnknown</li>
     * </ul>
     * 
     * @return true , если колонка может принимать <code>null</code> значения.
     *  false в противном случае.
     * @see #isNullable
     * @see #createBlankObject
     */
    public boolean isNullable() {
        return (nullable == ResultSetMetaData.columnNullable) || (nullable == ResultSetMetaData.columnNullableUnknown);
    }

    /**
     * @return Возвращает значение свойства POJO, соответствующего колонке. <p>
     * @see #setProperyName
     */
    public String getPropertyName() {
        return this.propertyName;
    }

    /**
     * Устанавливает значение свойства POJO, соответствующего колонке. <p>
     * @param propertyName
     * @see #getProperyName
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * @return Возвращает значение свойства POJO, соответствующего колонке. <p>
     * @see #setProperyName
     */
    public String getFieldName() {
        return this.fieldName;
    }

    /**
     * Устанавливает значение свойства POJO, соответствующего колонке. <p>
     * @param fieldName
     * @see #getProperyName
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * Возвращает новый экземпляр объекта, используемого как значение 
     * по умолчанию для данных, связанных с колонкой.
     * В классе DataColumn метод возвращает новый экземпляр
     * <code>java.lang.Object</code>. Классы-наследники как правило 
     * переопределяют метод.
     * 
     * @return значение по умолчанию
     * @see #getDefaultValue
     * @see #setDefaultValue
     * @see #blankValueInstance
     */
    public Object blankValueInstance() {
        return new Object();
    }

    /**
     * Возвращает значение по умолчанию для данных, связанных с колонкой.
     * Свойство <code>defaultValue</code> используется для построения
     * экземпляра объекта данных, когда явно не определено значение.
     * Метод декларирован как <code>protected</code>, чтобы избежать
     * его применеия для <code>mutable</code> объектов. Рекомендуется
     * использовать метод {@link #defualtValueInstance}, который всегда
     * возвращает не значение свойства <code>defaultValue</code>, а
     * новый экземпляр.
     * 
     * @return значение по умолчанию
     * @see #setDefaultValue
     * @see #blankValueInstance
     */
    protected Object getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * Устанавливает новое значение по умолчанию для данных, связанных с колонкой.
     * Свойство <code>defaultValue</code> используется для построения
     * экземпляра объекта данных, когда явно не определено значение.
     * 
     * @see #getDefaultValue
     * @see #blankValueInstance
     * @throws IllegalArgumentException когда новое значение равно 
     *          <code>null</code>.
     */
    public void setDefaultValue(Object defaultValue) {
        if (defaultValue == null) {
            throw new IllegalArgumentException("Column defaultValue cannnot be null");
        }
        this.defaultValue = defaultValue;
    }

    /**
     * Возвращает значение имени таблицы базы данных.<p>
     * Свойство <code>tableName</code> соответствует одноименному свойству
     * класса <code>java.sql.ResultSetMeteData</code>. Обычно заполняется, когда
     * <code>DataColumn</code> используется в связи с 
     * <code>java.sql.ResutSet</code>. 
     * <p>В классах TDO Framework нигде не используется. Возможное применение 
     * - генерирование SQL DDL создания таблицы базы данных.
     * 
     * @return имя таблицы базы данных
     */
    public String getTableName() {
        return this.tableName;
    }

    /**
     * Устанавливает новое значение имени таблицы базы данных.<p>
     * Свойство <code>tableName</code> соответствует одноименному свойству
     * класса <code>java.sql.ResultSetMeteData</code>. Обычно заполняется, когда
     * <code>DataColumn</code> используется в связи с 
     * <code>java.sql.ResutSet</code>. 
     * <p>В классах TDO Framework нигде не используется. Возможное применение 
     * - генерирование SQL DDL создания таблицы базы данных.
     * 
     * @param tableName имя таблицы базы данных
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Возвращает значение имени схемы таблицы базы данных.<p>
     * Свойство <code>schemaName</code> соответствует одноименному свойству
     * класса <code>java.sql.ResultSetMeteData</code>. Обычно заполняется, когда
     * <code>DataColumn</code> используется в связи с 
     * <code>java.sql.ResutSet</code>. 
     * <p>В классах TDO Framework нигде не используется. Возможное применение 
     * - генерирование SQL DDL создания таблицы базы данных.
     * 
     * @return имя схемы таблицы базы данных
     */
    public String getSchemaName() {
        return this.schemaName;
    }

    /**
     * Устанавливает новое значение имени схемы таблицы базы данных.<p>
     * Свойство <code>schemaName</code> соответствует одноименному свойству
     * класса <code>java.sql.ResultSetMeteData</code>. Обычно заполняется, когда
     * <code>DataColumn</code> используется в связи с 
     * <code>java.sql.ResutSet</code>. 
     * <p>В классах TDO Framework нигде не используется. Возможное применение 
     * - генерирование SQL DDL создания таблицы базы данных.
     * 
     * @param schemaName новое имя схемы таблицы базы данных
     */
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    /**
     * Возвращает значение, определяющее способ вычисления значения данных, 
     * связанных с колонкой.<p>
     * Данная версия поддерживает два способа: <i>обычные данные</i> и
     * <i>вычисляемые данные</i>. Когда применяется способ <i>обычные данные</i>,
     * то значение свойства равно {@link #DATA_KIND}. Когда применяется способ 
     * <i>вычисляемые данные</i>, то значение свойства равно {@link #CALC_KIND}.
     * 
     * @return kind
     */
    public int getKind() {
        return this.kind;
    }

    /**
     * Возвращает значение, определяющее способ вычисления значения данных, 
     * связанных с колонкой<p>
     * Данная версия поддерживает два способа: <i>обычные данные</i> и
     * <i>вычисляемые данные</i>. Когда применяется способ <i>обычные данные</i>,
     * то значение свойства равно {@link #DATA_KIND}. Когда применяется способ 
     * <i>вычисляемые данные</i>, то значение свойства равно {@link #CALC_KIND}.
     * <p>Поскольку данные, соответствующие вычисляемой колонке не могут 
     * подвергаться модификации, то при установке нового значения равным 
     * <code>CALC_KIND</code> автоматически устанавливается значение свойства
     * <code>readOnly</code> в <code>true</code>.<p>
     * Установка нового значения свойства <code>kind</code> производится, только,
     * если новое значение не совпадает с текущим. При этом возбуждается событие
     * <code>PropertyChangeEvent</code>.<p>
     * <b>Примечание.</b>При попытке установить новое значение, отличное от одного
     * из <ul> <li>DATA_KIND</li><li>CALC_KIND</li></ul> метод игнорирует 
     * назначение и завершается. Такое поведение может быть изменено в 
     * последующих версиях.
     * 
     * @param kind новое значение способа вычисления
     * @see #getKind
     */
    public void setKind(int kind) {
        if (this.kind == kind) {
            return;
        }
        if (kind != CALC_KIND && kind != DATA_KIND) {
            return;
        }
        int oldKind = this.kind;
        this.kind = kind;
        if (kind == CALC_KIND) {
            this.setReadOnly(true);
        }
        firePropertyChange("kind", oldKind, this.kind);
    }

    /**
     * Возвращает новый экземпляр типа Object или null. <p>
     * Классы наследники должны, как правило, переопределить этот метод и 
     * создавать новые экземпляры учитывая тип обрабатывемых данных. Например,
     * если колонка предначначена для обработки данных типа java.lang.Integer,
     * то логично создавать и возвращать, например, <code> new Integer(0)</code>.
     * 
     * @return null - если метод isNullable() возвращает true;
     *         Возвращает значение свойства defaultValue в противном случае.
     */
    public Object createBlankObject() {
        if (isNullable()) {
            return null;
        }
        return blankValueInstance();
    }
    public void assign(DataColumn source ) {
            this.name = source.name;
            this.kind = source.kind;
            this.scale = source.scale;
            this.size = source.size;
            this.precision = source.precision;
            this.sqlType = source.sqlType;
            this.cellIndex = source.cellIndex;
            this.type = source.type;
            this.readOnly = source.readOnly;
            this.nullable = source.nullable;
            this.autoIncrement = source.autoIncrement;
            this.caseSensitive = source.caseSensitive;
            this.currency = source.currency;
            this.signed = source.signed;
            this.searchable = source.searchable;
            this.label = source.label;
            this.tableName = source.tableName;
            this.schemaName = source.schemaName; // as in sql db table

            this.cellIndex = source.cellIndex;

            this.propertyName = source.propertyName;
            this.fieldName = source.fieldName;
            this.index = source.index;

            this.expression = source.expression;
        
    }
    /**
     * Создает новый объект, являющийся копией данного.
     * Реализует интерфейс {@link java.lang.Cloneable}. Классы-наследники должны
     * переопределить метод для отражения дополнительных возможностей.
     * @return клон текущего объекта
     */
    @Override
    public synchronized Object clone() {
        try {
            DataColumn obj = (DataColumn) super.clone();
            obj.name = this.name;
            obj.kind = this.kind;
            obj.scale = this.scale;
            obj.size = this.size;
            obj.precision = this.precision;
            obj.sqlType = this.sqlType;
            obj.changeSupport = null;
            obj.cellIndex = this.cellIndex;
            obj.type = this.type;
            obj.readOnly = this.readOnly;
            obj.nullable = this.nullable;
            obj.autoIncrement = this.autoIncrement;
            obj.caseSensitive = this.caseSensitive;
            obj.currency = this.currency;
            obj.signed = this.signed;
            obj.searchable = this.searchable;
            obj.label = this.label;
            obj.tableName = this.tableName;
            obj.schemaName = this.schemaName; // as in sql db table

            obj.cellIndex = this.cellIndex;

            obj.propertyName = this.propertyName;
            obj.fieldName = this.fieldName;
            obj.index = this.index;

            obj.setExpression(this.expression);

            return obj;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    /**
     * Возвращает объект, соответствующий типу колонки, используя объект, 
     * заданный параметром. <p>
     *
     * @param obj объект над которым выполняется метод
     * @return новый объект или объект заданный параметром, если не требуется
     *    преобразования типов.
     */
    public Object toColumnType(Object obj) {
        return obj;
    }
    
    public boolean copy(Object source, Object target) {
        return false;
    }
    
    /**
     * Сравнивает два заданных объекта, считая, что <code>null</code> объект
     * всегда принимает меньшее значение, если только другой объект не равен 
     * <code>null</code>. Если оба объекта равны <code>null</code>, то они 
     * считаются равными.
     * Метод используется при реализации сортировки, фильтрации и оценки
     * выражений классами TDO.
     * @param obj первый сравниваемый объект
     * @param anotherObj второй сравниваемый объект
     * @return 0 - если объекты считаются равными. 1 - если значение первого
     * объекта строго больше значения второго. -1  - если значение первого
     * объекта строго меньше значения второго. 
     * @see #compareObjects(Object,Object,boolean)
     */
    @Override
    public int compareObjects(Object obj, Object anotherObj) {
        return this.compareObjects(obj, anotherObj, true);
    }

    /**
     * Сравнивает два заданных объекта. Как интерпретировать <code>null</code>
     * значения объекта зависит от состояния третьего параметра.
     * В любом случае, если оба объекта равны <code>null</code>, то они 
     * считаются равными.
     * Метод данного класса преобразует объекты не равные <code>null</code> в 
     * строки знаков <code>obj.toString</code>, <code>anotherObj.toString</code>
     * и проводит сравнение строк методом <code>compareTo</code>. Классы, 
     * наследующие данный, обычно переопределяют метод для корректной оценки
     * обектов конкретного типа. <p>
     * Метод используется при реализации сортировки, фильтрации и оценки
     * выражений классами TDO.
     * @param obj первый сравниваемый объект
     * @param anotherObj второй сравниваемый объект
     * @param nullMin если равен <code>true</code> то <code>null</code> значение
     *   считается минимальным. false - означает, что <code>null</code> значение
     *   оценивается как максимальное. 
     * @return 0 - если объекты  равны. 1 - если значение первого
     * объекта строго больше значения второго. -1  - если значение первого
     * объекта строго меньше значения второго. 
     * 
     * @see #compareObjects(Object,Object)
     */
    @Override
    public int compareObjects(Object obj, Object anotherObj, boolean nullMin) {
        int nullRes = nullMin ? -1 : 1;
        if (obj == null && anotherObj == null) {
            return 0;
        }
        if (obj == null) {
            return nullRes;
        }
        if (anotherObj == null) {
            return -nullRes;
        }
        return compareNotNull(obj, anotherObj);
    }

    /**
     * Удобный метод, созданный для совместного использования с методом
     * {@link #compareObjects(Object,Object,boolean)}.
     * Метод compareObjects производит все необходимые действия, когда один
     * или оба параметры имеют значение <code>null</code>. Если оба значения 
     * отличны от <code>null</code>, то вызывается данный метод. Классы,
     * наследующие данный, могут должны переопределить данный метод, выполнение
     * которого зависит от типа данных.
     * @param o1 первый сравниваемый операнд
     * @param o2 второй сравниваемый операнд
     * @return 0 - если объекты считаются равными. 1 - если значение первого
     * объекта строго больше значения второго. -1  - если значение первого
     * объекта строго меньше значения второго. 
     */
    protected int compareNotNull(Object o1, Object o2) {
        String s1 = o1.toString();
        String s2 = o2.toString();
        return s1.compareTo(s2);

    }

    /**
     * Возбуждает событие PropertyChangeEvent, когда свойство имеет тип не 
     * являющийся java.lang.String, boolean, int.
     * @param propertyName имя измененого свойства
     * @param oldValue значение свойства перед назначением ему нового значения.
     * @param newValue новое значение свойства.
     */
    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (changeSupport != null) {
            changeSupport.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    /**
     * Возбуждает событие PropertyChangeEvent, когда свойство имеет тип int.
     * @param propertyName имя измененого свойства
     * @param oldValue значение свойства перед назначением ему нового значения.
     * @param newValue новое значение свойства.
     */
    public void firePropertyChange(String propertyName, int oldValue, int newValue) {
        if (changeSupport != null) {
            changeSupport.firePropertyChange(propertyName, oldValue, newValue);

        }
    }

    /**
     * Возбуждает событие PropertyChangeEvent, когда свойство имеет тип boolean.
     * @param propertyName имя измененого свойства
     * @param oldValue значение свойства перед назначением ему нового значения.
     * @param newValue новое значение свойства.
     */
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        if (changeSupport != null) {
            changeSupport.firePropertyChange(propertyName, oldValue, newValue);

        }
    }

    /**
     * Возбуждает событие PropertyChangeEvent, когда свойство имеет тип String.
     * @param propertyName имя измененого свойства
     * @param oldValue значение свойства перед назначением ему нового значения.
     * @param newValue новое значение свойства.
     */
    public void firePropertyChange(String propertyName, String oldValue, String newValue) {
        if (changeSupport != null) {
            changeSupport.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    /**
     * Добавляет заданного слушателя события <code>PropertyChangeEvent</code> к 
     * внутренней коллекции.
     * @param listener добавляемый обработчик события
     */
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        if (changeSupport == null) {
            changeSupport = new PropertyChangeSupport(this);
        }
        changeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Удаляет заданного слушателя события <code>PropertyChangeEvent</code> из 
     * внутренней коллекции.
     * @param listener удаляемый обработчик события
     */
    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        if (changeSupport != null) {
            changeSupport.removePropertyChangeListener(listener);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Internal classes implementing class tdo.DataColumn
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public static abstract class PDBNumberColumn extends DataColumn implements Cloneable, Serializable {

        @Override
        public Object clone() {
            PDBNumberColumn obj = (PDBNumberColumn) super.clone();
            return obj;
        }
    } //PDBNumericColumn


    /**
     * Класс реализует абстрактный класс {@link tdo.DataColumn} для использования
     * с данными типа <code>java.lang.Long</code>. 
     * Sql-тип BIGINT, определенный классом <code>java.sql.Types</code> соответствует
     * типу <code>java.lang.Long</code>.
     */
    public static class PDBBigIntColumn extends PDBNumberColumn implements Cloneable, Serializable {

        /**
         * Инициализирует значение полей и свойств, специфичных для класса.
         * <code><pre>
         *   setSize(20);
         *   setScale(0);
         *   setType(Long.class);
         *   setSqlType(java.sql.Types.BIGINT);            
         *   setDefaultValue(new Long(0));
         * </pre></code>
         */
        public PDBBigIntColumn() {

            this.setSize(20);
            this.setScale(0);
            this.setType(Long.class);
            this.setSqlType(java.sql.Types.BIGINT);
            this.setDefaultValue(new Long(0));
        }

        /**
         * @return Возвращает значение типа данных равное <code>java.lang.Long</code>.
         */
        @Override
        public Class getType() {
            return Long.class;
        }

        /**
         * @inherited
         */
        @Override
        public synchronized Object clone() {
            PDBBigIntColumn obj = (PDBBigIntColumn) super.clone();
            return obj;
        }

        /**
         * Возвращает новый экземпляр объекта, используемого как значение 
         * по умолчанию для данных, связанных с колонкой.
         * 
         * @return значение по умолчанию. Если метод <code>getDefaultValue</code>
         *    возвращает <code>null</code>, то возвращается <code>null</code>.
         *    В остальных случаях возвращается новый экземпляр типа 
         *   <code>java.lang.Long</code>, метод <code>longValue</code> которого
         *   равен значению <code>getDefaultValue().longValue()</code>.
         */
        @Override
        public Object blankValueInstance() {
            Object obj = getDefaultValue();
            if (obj == null) {
                return null;
            }
            Long d = (Long) obj;
            return new Long(d.longValue());
        }

        /**
         * Преобразует значение параметра в тип <code>java.lang.Long</code>.
         * @param value преобразуемое значение
         * @return значение типа <code>java.lang.Long</code>.          
         * Если <i>value</i> равно <code>null</code>, то возвращает 
         * <code>null</code>.<br>
         * Если <i>value</i> является типом <code>Long</code>, то возвращается 
         * без преобразований.<br>
         * Если <i>value</i> является типом <code>Number</code>, то создается и 
         * возвращается  экземпляр типа <code>Long</code>.<br>
         * Если <i>value</i> тип отличен от указанных, то производится попытка
         * выполнить <code>new Long(value.toString()</code>. При этом может быть
         * выброшено исключение <code>NumberFormatException</code>.
         * @throws NumberFormatException
         */
        @Override
        public Object toColumnType(Object value) {

            if (value instanceof Long) {
                return value;
            }

            if (value == null) {
                return null;
            }

            Object r; // = null;

            if (value instanceof Number) {
                r = Long.valueOf(((Number) value).longValue());
            } else {
                r = new Long(value.toString()); //may be NumberFormatException

            }
            return r;
        }

        @Override
        protected int compareNotNull(Object o1, Object o2) {
            return ((Long) o1).compareTo((Long) o2);
        }
    } //class PDBBigIntColumn


    /**
     * Класс реализует абстрактный класс {@link tdo.DataColumn} для использования
     * с данными типа <code>java.lang.Double</code>. 
     * Sql-тип DOUBLE, определенный классом <code>java.sql.Types</code> соответствует
     * типу <code>java.lang.Double</code>.
     */
    public static class PDBDoubleColumn extends PDBNumberColumn implements Cloneable, Serializable {

        /**
         * Инициализирует значение полей и свойств, специфичных для класса.
         * <code><pre>
         *   setSize(15);
         *   setScale(-1);
         *   setType(Double.class);
         *   setSqlType(java.sql.Types.DOUBLE);            
         *   setDefaultValue(new Double(0));
         * </pre></code>
         */
        public PDBDoubleColumn() {
            this.setSize(15);
            this.setScale(-1);
            this.setType(Double.class);
            this.setSqlType(java.sql.Types.DOUBLE);
            this.setDefaultValue(new Double(0));
        }

        /**
         * @return Возвращает значение типа данных равное <code>java.lang.Double</code>.
         */
        @Override
        public Class getType() {
            return Double.class;
        }

        /**
         * @inherited
         */
        @Override
        public synchronized Object clone() {
            PDBDoubleColumn obj = (PDBDoubleColumn) super.clone();
            return obj;
        }

        @Override
        protected int compareNotNull(Object o1, Object o2) {
            return ((Double) o1).compareTo((Double) o2);
        }

        /**
         * Возвращает новый экземпляр объекта, используемого как значение 
         * по умолчанию для данных, связанных с колонкой.
         * 
         * @return значение по умолчанию. Если метод <code>getDefaultValue</code>
         *    возвращает <code>null</code>, то возвращается <code>null</code>.
         *    В остальных случаях возвращается новый экземпляр типа 
         *   <code>java.lang.Double</code>, метод <code>doubleValue</code> которого
         *   равен значению <code>getDefaultValue().doubleValue()</code>.
         */
        @Override
        public Object blankValueInstance() {
            Object obj = getDefaultValue();
            if (obj == null) {
                return null;
            }
            Double d = (Double) obj;
            return new Double(d.doubleValue());
        }

        /**
         * Преобразует значение параметра в тип <code>java.lang.Double</code>.
         * @param value преобразуемое значение
         * @return значение типа <code>java.lang.Double</code>.          
         * Если <i>value</i> равно <code>null</code>, то возвращает 
         * <code>null</code>.<br>
         * Если <i>value</i> является типом <code>Double</code>, то возвращается 
         * без преобразований.<br>
         * Если <i>value</i> является типом <code>Number</code>, то создается и 
         * возвращается  экземпляр типа <code>Double</code>.<br>
         * Если <i>value</i> тип отличен от указанных, то производится попытка
         * выполнить <code>new Double(value.toString()</code>. При этом может быть
         * выброшено исключение <code>NumberFormatException</code>.
         * @throws NumberFormatException
         */
        @Override
        public Object toColumnType(Object value) {
            Object r = null;

            if (value instanceof Double) {
                r = value;
            } else if (value instanceof Number) {
                r = new Double(((Number) value).doubleValue());
            } else if (value != null) {
                r = Double.valueOf(value.toString()); //may be NumberFormatException

            }
            return r;
        }
    }

    /**
     * Класс реализует абстрактный класс {@link tdo.DataColumn} для использования
     * с данными типа <code>java.math.BigDecoimal</code>. 
     * Sql-тип DECIMAL, определенный классом <code>java.sql.Types</code> соответствует
     * типу <code>java.math.BigDecimal</code>.
     */
    public static class PDBDecimalColumn extends PDBNumberColumn implements Cloneable, Serializable {

        public static final BigDecimal ZEROVALUE = new BigDecimal(0);

        /**
         * Инициализирует значение полей и свойств, специфичных для класса.
         * <code><pre>
         *   setSize(15);
         *   setScale(2);
         *   setType(BigDecimal.class);
         *   setSqlType(java.sql.Types.DECIMAL);            
         *   this.setDefaultValue(new BigDecimal(0));
         *   this.setDefaultValue(((BigDecimal) getDefaultValue()).setScale(getScale(), BigDecimal.ROUND_HALF_UP));
         * </pre></code>
         */
        public PDBDecimalColumn() {
            this.setSize(15);
            this.setScale(2);
            this.setType(BigDecimal.class);
            this.setSqlType(java.sql.Types.DECIMAL);
            this.setDefaultValue(new BigDecimal(0));
            this.setDefaultValue(((BigDecimal) blankValueInstance()).setScale(getScale(), BigDecimal.ROUND_HALF_UP));
        }

        /**
         * @return Возвращает значение типа данных равное <code>java.math.BigDecimal</code>.
         */
        @Override
        public Class getType() {
            return BigDecimal.class;
        }

        /**
         * @inherited
         */
        @Override
        public synchronized Object clone() {
            PDBDecimalColumn obj = (PDBDecimalColumn) super.clone();
            return obj;
        }

        @Override
        protected int compareNotNull(Object o1, Object o2) {
            return ((BigDecimal) o1).compareTo((BigDecimal) o2);
        }

        /**
         * Возвращает новый экземпляр объекта, используемого как значение 
         * по умолчанию для данных, связанных с колонкой.
         * 
         * @return значение по умолчанию. Если метод <code>getDefaultValue</code>
         *    возвращает <code>null</code>, то возвращается <code>null</code>.
         *    В остальных случаях возвращается новый экземпляр типа 
         *   <code>java.math.BigDecimal</code>. 
         */
        @Override
        public Object blankValueInstance() {
            Object obj = getDefaultValue();
            if (obj == null) {
                return null;
            }
            BigDecimal bd;
            if (obj instanceof Double) {
                Double d = (Double) obj;
                return new Double(d.doubleValue());
            }
            bd = (BigDecimal) obj;
            return new BigDecimal(bd.unscaledValue(), bd.scale());
        }

        /**
         * Преобразует значение параметра в тип <code>java.math.BigDecimal</code>.
         * @param value преобразуемое значение
         * @return значение типа <code>java.math.BigDecimal</code>.          
         * <ol>
         * <li>Если <i>value</i> равно <code>null</code>, то возвращает 
         * <code>null</code>.</li>
         * <li>Если <i>value</i> является типом <code>java.math.BigDecimal</code>, 
         * то возвращается  <i>value</i> без каких-либо изменеий.</li>
         * <li>Если <i>value</i> является типом <code>BigInteger</code>, то создается и 
         * возвращается  экземпляр типа <code>java.math.BigDecimal</code> 
         * применением конструктора с сигнатурой 
         * <code>BigDecimal(BigInteger)</code>.</li>
         * <li>Если <i>value</i> является типом <code>Number</code>, то создается и 
         * возвращается  экземпляр типа <code>java.math.BigDecimal</code>
         * применением конструктора с сигнатурой 
         * <code>BigDecimal(double)</code>.</li>
         * <li>Если <i>value</i> тип отличен от указанных, то производится попытка
         * выполнить <code>new BigDecimal(value.toString()</code>. При этом может быть
         * выброшено исключение <code>NumberFormatException</code>.</li>
         * Если результат (кроме случаев 1) и 2) ) отличен от <code>null</code>,
         * то для возвращаемого результата устанавливается:
         * <code>setScale( getScale(), BigDecimal.ROUND_HALF_UP);</code>
         * @throws NumberFormatException
         */
        @Override
        public Object toColumnType(Object value) {
            if (value == null) {
                return null;
            }
            Object r = null;

            if (value instanceof BigDecimal) {
                r = value;
            } else if (value instanceof BigInteger) {
                r = new BigDecimal((BigInteger) value);
            } else if (value instanceof Number) {
                r = new BigDecimal(((Number) value).doubleValue());
            } else if (value != null) {
                r = new BigDecimal(value.toString()); //may be NumberFormatException

            }
            if (r != null) {
                r = ((BigDecimal) r).setScale(getScale(), BigDecimal.ROUND_HALF_UP);
            }
            return r;
        }
    } // class PDBDecimalColumn


    /**
     * Класс реализует абстрактный класс {@link tdo.DataColumn} для использования
     * с данными типа <code>java.lang.Integer</code>. 
     * Sql-тип INTEGER, определенный классом <code>java.sql.Types</code> соответствует
     * типу <code>java.lang.Integer</code>.
     */
    public static class PDBIntegerColumn extends PDBNumberColumn implements Cloneable, Serializable {

        /**
         * Инициализирует значение полей и свойств, специфичных для класса.
         * <code><pre>
         *   setSize(10);
         *   setScale(0);
         *   setType(Integer.class);
         *   setSqlType(java.sql.Types.INTEGER);            
         *   this.setDefaultValue(new Integer(0));
         * </pre></code>
         */
        public PDBIntegerColumn() {
            this.setSqlType(java.sql.Types.INTEGER);
            this.setType(Integer.class);
            this.setDefaultValue(new Integer(0));

            this.setSize(10);
            this.setScale(0);
        }

        /**
         * @return Возвращает значение типа данных равное <code>java.lang.Integer</code>.
         */
        @Override
        public Class getType() {
            return Integer.class;
        }

        /**
         * 
         * @inherited
         */
        @Override
        public synchronized Object clone() {
            PDBIntegerColumn obj = (PDBIntegerColumn) super.clone();
            return obj;
        }

        @Override
        protected int compareNotNull(Object o1, Object o2) {
            return ((Integer) o1).compareTo((Integer) o2);
        }

        /**
         * Возвращает новый экземпляр объекта, используемого как значение 
         * по умолчанию для данных, связанных с колонкой.
         * 
         * @return значение по умолчанию. Если метод <code>getDefaultValue</code>
         *    возвращает <code>null</code>, то возвращается <code>null</code>.
         *    В остальных случаях возвращается новый экземпляр типа 
         *   <code>java.lang.Integer</code>, метод <code>intValue</code> которого
         *   равен значению <code>getDefaultValue().intValue()</code>.
         */
        @Override
        public Object blankValueInstance() {
            Object obj = getDefaultValue();

            if (obj == null) {
                return null;
            }
            Integer d = (Integer) obj;
            return new Integer(d.intValue());
        }

        /**
         * Преобразует значение параметра в тип <code>java.lang.Integer</code>.
         * @param value преобразуемое значение
         * @return значение типа <code>java.lang.Integer</code>.          
         * Если <i>value</i> равно <code>null</code>, то возвращает 
         * <code>null</code>.<br>
         * Если <i>value</i> является типом <code>Integer</code>, то возвращается 
         * без преобразований.<br>
         * Если <i>value</i> является типом <code>Number</code>, то создается и 
         * возвращается  экземпляр типа <code>Integer</code>.<br>
         * Если <i>value</i> тип отличен от указанных, то производится попытка
         * выполнить <code>new Integer(value.toString()</code>. При этом может быть
         * выброшено исключение <code>NumberFormatException</code>.
         * @throws NumberFormatException
         */
        @Override
        public Object toColumnType(Object value) {
            Object r = null;

            if (value instanceof Integer) {
                r = value;
            } else if (value instanceof Number) {
                r = new Integer(((Number) value).intValue());
            } else if (value != null) {
                r = Integer.valueOf(value.toString()); //may be NumberFormatException

            }
            return r;
        }
    } //PDBIntegerColumn


    /**
     * Класс реализует абстрактный класс {@link tdo.DataColumn} для использования
     * с данными типа <code>java.lang.Float</code>. 
     * Sql-тип REAL, определенный классом <code>java.sql.Types</code> соответствует
     * типу <code>java.lang.Float</code>.
     */
    public static class PDBRealColumn extends PDBNumberColumn implements Cloneable, Serializable {

        /**
         * Инициализирует значение полей и свойств, специфичных для класса.
         * <code><pre>
         *   setSize(15);
         *   setScale(-1);
         *   setType(Float.class);
         *   setSqlType(java.sql.Types.REAL);            
         *   this.setDefaultValue(new Float(0));
         * </pre></code>
         */
        public PDBRealColumn() {
            this.setSize(15);
            this.setScale(-1);
            this.setType(Float.class);
            this.setSqlType(java.sql.Types.REAL);
            this.setDefaultValue(new Float(0));
        }

        /**
         * @return Возвращает значение типа данных равное <code>java.lang.Float</code>.
         */
        @Override
        public Class getType() {
            return Float.class;
        }

        /**
         * @inherited
         */
        @Override
        public synchronized Object clone() {
            PDBRealColumn obj = (PDBRealColumn) super.clone();
            return obj;
        }

        @Override
        protected int compareNotNull(Object o1, Object o2) {
            return ((Float) o1).compareTo((Float) o2);
        }

        /**
         * Возвращает новый экземпляр объекта, используемого как значение 
         * по умолчанию для данных, связанных с колонкой.
         * 
         * @return значение по умолчанию. Если метод <code>getDefaultValue</code>
         *    возвращает <code>null</code>, то возвращается <code>null</code>.
         *    В остальных случаях возвращается новый экземпляр типа 
         *   <code>java.lang.Float</code>, метод <code>floatValue</code> которого
         *   равен значению <code>getDefaultValue().floatValue()</code>.
         */
        @Override
        public Object blankValueInstance() {
            Object obj = getDefaultValue();
            if (obj == null) {
                return null;
            }
            Float d = (Float) obj;
            return new Float(d.floatValue());
        }

        /**
         * Преобразует значение параметра в тип <code>java.lang.Float</code>.
         * @param value преобразуемое значение
         * @return значение типа <code>java.lang.Float</code>.          
         * Если <i>value</i> равно <code>null</code>, то возвращает 
         * <code>null</code>.<br>
         * Если <i>value</i> является типом <code>Float</code>, то возвращается 
         * без преобразований.<br>
         * Если <i>value</i> является типом <code>Number</code>, то создается и 
         * возвращается  экземпляр типа <code>Float</code>.<br>
         * Если <i>value</i> тип отличен от указанных, то производится попытка
         * выполнить <code>new Float(value.toString()</code>. При этом может быть
         * выброшено исключение <code>NumberFormatException</code>.
         * @throws NumberFormatException
         */
        @Override
        public Object toColumnType(Object value) {
            Object r = null;

            if (value instanceof Float) {
                r = value;
            } else if (value instanceof Number) {
                r = new Float(((Number) value).floatValue());
            } else if (value != null) {
                r = Float.valueOf(value.toString()); //may be NumberFormatException

            }
            return r;
        }
    }

    /**
     * Класс реализует абстрактный класс {@link tdo.DataColumn} для использования
     * с данными типа <code>java.lang.Short</code>. 
     * Sql-тип REAL, определенный классом <code>java.sql.Types</code> соответствует
     * типу <code>java.lang.Short</code>.
     */
    public static class PDBSmallIntColumn extends PDBNumberColumn implements Cloneable, Serializable {

        /**
         * Инициализирует значение полей и свойств, специфичных для класса.
         * <code><pre>
         *   setSize(6);
         *   setScale(0);
         *   setType(Short.class);
         *   setSqlType(java.sql.Types.SMALLINT);            
         *   this.setDefaultValue(new Short("0"));
         * </pre></code>
         */
        public PDBSmallIntColumn() {
            this.setSize(6);
            this.setScale(0);
            this.setType(Short.class);
            this.setSqlType(java.sql.Types.SMALLINT);
            this.setDefaultValue(new Short("0"));
        }

        /**
         * @return Возвращает значение типа данных равное <code>java.lang.Short</code>.
         */
        @Override
        public Class getType() {
            return Short.class;
        }

        /**
         * @inherited
         */
        @Override
        public synchronized Object clone() {
            PDBSmallIntColumn obj = (PDBSmallIntColumn) super.clone();
            return obj;
        }

        @Override
        protected int compareNotNull(Object o1, Object o2) {
            return ((Short) o1).compareTo((Short) o2);
        }

        /**
         * Возвращает новый экземпляр объекта, используемого как значение 
         * по умолчанию для данных, связанных с колонкой.
         * 
         * @return значение по умолчанию. Если метод <code>getDefaultValue</code>
         *    возвращает <code>null</code>, то возвращается <code>null</code>.
         *    В остальных случаях возвращается новый экземпляр типа 
         *   <code>java.lang.Short</code>, метод <code>shortValue</code> которого
         *   равен значению <code>getDefaultValue().shortValue()</code>.
         */
        @Override
        public Object blankValueInstance() {
            Object obj = getDefaultValue();

            if (obj == null) {
                return null;
            }
            Short d = (Short) obj;
            return new Short(d.shortValue());
        }

        /**
         * Преобразует значение параметра в тип <code>java.lang.Short</code>.
         * @param value преобразуемое значение
         * @return значение типа <code>java.lang.Short</code>.          
         * Если <i>value</i> равно <code>null</code>, то возвращает 
         * <code>null</code>.<br>
         * Если <i>value</i> является типом <code>Short</code>, то возвращается 
         * без преобразований.<br>
         * Если <i>value</i> является типом <code>Number</code>, то создается и 
         * возвращается  экземпляр типа <code>Short</code>.<br>
         * Если <i>value</i> тип отличен от указанных, то производится попытка
         * выполнить <code>new Short(value.toString()</code>. При этом может быть
         * выброшено исключение <code>NumberFormatException</code>.
         * @throws NumberFormatException
         */
        @Override
        public Object toColumnType(Object value) {
            Object r = null;

            if (value instanceof Short) {
                r = value;
            } else if (value instanceof Number) {
                r = new Short(((Number) value).shortValue());
            } else if (value != null) {
                r = Short.valueOf(value.toString()); //may be NumberFormatException

            }
            return r;
        }
    }

    /**
     * Класс реализует абстрактный класс {@link tdo.DataColumn} для использования
     * с данными типа <code>java.lang.Byte</code>. 
     * Sql-тип TINYINT, определенный классом <code>java.sql.Types</code> соответствует
     * типу <code>java.lang.Byte</code>.
     */
    public static class PDBTinyIntColumn extends PDBNumberColumn implements Cloneable, Serializable {

        /**
         * Инициализирует значение полей и свойств, специфичных для класса.
         * <code><pre>
         *   setSize(4);
         *   setScale(0);
         *   setType(Byte.class);
         *   setSqlType(java.sql.Types.TINYINT);            
         *   this.setDefaultValue(new Byte("0"));
         * </pre></code>
         */
        public PDBTinyIntColumn() {

            this.setSize(4);
            this.setScale(0);
            this.setType(Byte.class);
            this.setSqlType(java.sql.Types.TINYINT);

            this.setDefaultValue(new Byte("0"));
        }

        /**
         * @return Возвращает значение типа данных равное <code>java.lang.Byte</code>.
         */
        @Override
        public Class getType() {
            return Byte.class;
        }

        /**
         * @inherited
         */
        @Override
        public synchronized Object clone() {
            PDBTinyIntColumn obj = (PDBTinyIntColumn) super.clone();
            return obj;
        }

        @Override
        protected int compareNotNull(Object o1, Object o2) {
            return ((Byte) o1).compareTo((Byte) o2);
        }

        /**
         * Возвращает новый экземпляр объекта, используемого как значение 
         * по умолчанию для данных, связанных с колонкой.
         * 
         * @return значение по умолчанию. Если метод <code>getDefaultValue</code>
         *    возвращает <code>null</code>, то возвращается <code>null</code>.
         *    В остальных случаях возвращается новый экземпляр типа 
         *   <code>java.lang.Byte</code>, метод <code>byteValue</code> которого
         *   равен значению <code>getDefaultValue().byteValue()</code>.
         */
        @Override
        public Object blankValueInstance() {
            Object obj = getDefaultValue();

            if (obj == null) {
                return null;
            }
            Byte d = (Byte) obj;
            return new Byte(d.byteValue());
        }

        /**
         * Преобразует значение параметра в тип <code>java.lang.Byte</code>.
         * @param value преобразуемое значение
         * @return значение типа <code>java.lang.Byte</code>.          
         * Если <i>value</i> равно <code>null</code>, то возвращает 
         * <code>null</code>.<br>
         * Если <i>value</i> является типом <code>Byte</code>, то возвращается 
         * без преобразований.<br>
         * Если <i>value</i> является типом <code>Number</code>, то создается и 
         * возвращается  экземпляр типа <code>Byte</code>.<br>
         * Если <i>value</i> тип отличен от указанных, то производится попытка
         * выполнить <code>new Byte(value.toString()</code>. При этом может быть
         * выброшено исключение <code>NumberFormatException</code>.
         * @throws NumberFormatException
         */
        @Override
        public Object toColumnType(Object value) {
            Object r;//My 06.03.2012 = null;
            if (value == null) {
                return null;
            }
            if (value instanceof Byte) {
                return value;
            }
            if (value instanceof Number) {
                r = new Byte(((Number) value).byteValue());
            } else {
                r = Byte.valueOf(value.toString()); //may be NumberFormatException

            }
            return r;
        }
    }

    /**
     * Класс реализует абстрактный класс {@link tdo.DataColumn} для использования
     * с данными типа <code>java.lang.Boolean</code>. 
     * Sql-тип BIT, определенный классом <code>java.sql.Types</code> соответствует
     * типу <code>java.lang.Boolean</code>.
     */
    public static class PDBBitColumn extends DataColumn implements Cloneable, Serializable {

        /**
         * Инициализирует значение полей и свойств, специфичных для класса.
         * <code><pre>
         *   setType(Boolean.class);
         *   setSqlType(java.sql.Types.BIT);            
         *   this.setDefaultValue(new Boolean("0"));
         * </pre></code>
         */
        public PDBBitColumn() {
            this.setType(Boolean.class);
            this.setSqlType(java.sql.Types.BIT);
            this.setDefaultValue(new Boolean(false));
        }

        /**
         * @return Возвращает значение типа данных равное <code>java.lang.Boolean</code>.
         */
        @Override
        public Class getType() {
            return Boolean.class;
        }

        /**
         * @inherited
         */
        @Override
        public synchronized Object clone() {
            PDBBitColumn obj = (PDBBitColumn) super.clone();

            return obj;
        }

        /**
         * Преобразует значение параметра в тип <code>java.lang.Boolean</code>.
         * @param value преобразуемое значение
         * @return значение типа <code>java.lang.Boolean</code>.          
         * Если <i>value</i> равно <code>null</code>, то возвращает 
         * <code>null</code>.<br>
         * Если <i>value</i> является типом <code>Boolean</code>, то возвращается 
         * без преобразований.<br>
         * Если <i>value</i> тип отличен от указанных, то 
         * выполняется <code>Boolean.valueOf(value.toString())</code>.
         */
        @Override
        public Object toColumnType(Object value) {
            if (value instanceof Boolean) {
                return value;
            }
            if (value == null) {
                return null;
            }
            return Boolean.valueOf(value.toString());
        }

        @Override
        protected int compareNotNull(Object o1, Object o2) {
            return ((Boolean) o1).compareTo((Boolean) o2);
        }

        /**
         * Возвращает новый экземпляр объекта, используемого как значение 
         * по умолчанию для данных, связанных с колонкой.
         * 
         * @return значение по умолчанию. Если метод <code>getDefaultValue</code>
         *    возвращает <code>null</code>, то возвращается <code>null</code>.
         *    В остальных случаях возвращается новый экземпляр типа 
         *   <code>java.lang.Boolean</code>, метод <code>booleanValue</code> которого
         *   равен значению <code>getDefaultValue().booleanValue()</code>.
         */
        @Override
        public Object blankValueInstance() {
            Object obj = getDefaultValue();
            if (obj == null) {
                return null;
            }
            return new Boolean(((Boolean) obj).booleanValue());
        }
    }

    /**
     * Класс реализует абстрактный класс {@link tdo.DataColumn} для использования
     * с данными типа <code>java.util.Date</code>. 
     * Sql-тип DATE, определенный классом <code>java.sql.Types</code> соответствует
     * типу <code>java.util.Date</code>.
     */
    public static class PDBDateColumn extends DataColumn implements Cloneable, Serializable {

        /**
         * Инициализирует значение полей и свойств, специфичных для класса.
         * <code><pre>
         *   setType(java.util.Date.class);
         *   setSqlType(java.sql.Types.DATE);            
         *   this.setDefaultValue(new java.util.Date(0));
         * </pre></code>
         */
        public PDBDateColumn() {

            this.setType(java.util.Date.class);
            this.setSqlType(java.sql.Types.DATE);
            this.setDefaultValue(new java.util.Date(0));
        }

        /**
         * @return Возвращает значение типа данных равное <code>java.util.Date</code>.
         */
        @Override
        public Class getType() {
            return java.util.Date.class;
        }

        /**
         * @inherited
         */
        @Override
        public synchronized Object clone() {
            PDBDateColumn obj = (PDBDateColumn) super.clone();

            return obj;
        }

        @Override
        protected int compareNotNull(Object o1, Object o2) {
            java.util.Date d1 = (java.util.Date) o1;
            java.util.Date d2 = (java.util.Date) o2;

            int result = 0;
            if (d1.equals(d2)) {
                result = 0;
            }
            if (d1.before(d2)) {
                result = -1;
            }
            if (d1.after(d2)) {
                result = 1;
            }
            return result;

        }

        /**
         * Возвращает новый экземпляр объекта, используемого как значение 
         * по умолчанию для данных, связанных с колонкой.
         * 
         * @return значение по умолчанию. Если метод <code>getDefaultValue</code>
         *    возвращает <code>null</code>, то возвращается <code>null</code>.
         *    В остальных случаях возвращается новый экземпляр типа 
         *   <code>java.util.Date</code>, метод <code>getTime()</code> которого
         *   равен значению <code>getDefaultValue().getTime()</code>.
         */
        @Override
        public Object blankValueInstance() {
            Object obj = getDefaultValue();
            if (obj == null) {
                return null;
            }
            return new Date(((Date) obj).getTime());
        }

        /**
         * Преобразует значение параметра в тип <code>java.util.Date</code>.
         * @param value преобразуемое значение
         * @return значение типа <code>java.util.Date</code>.          
         * Если <i>value</i> равно <code>null</code>, то возвращает 
         * <code>null</code>.<br>
         * Если <i>value</i> является типом <code>Date</code>, то возвращается 
         * без преобразований.<br>
         * Если <i>value</i> является типом <code>Timestamp</code>, то возвращается 
         * новый экземпляр, используя value.getTime().<br>
         * Если <i>value</i> является типом <code>Number</code>, то возвращается 
         * новый экземпляр, используя value.longValue().<br>
         * Если <i>value</i> тип отличен от указанных, то для создания 
         * возвращаемого значения производится попытка создания даты из
         * выражения value.toString(). При этом возможно исключение 
         * <code>java.text.ParseException</code>.
         */
        @Override
        public Object toColumnType(Object value) {
            if (value == null) {
                return null;
            }
            if (value instanceof Date) {
                return value;
            }
            if (value instanceof Timestamp) {
                Timestamp ts = (Timestamp) value;
                return new Date(((Timestamp) value).getTime());
            }

            Object r; // = null;

            if (value instanceof Number) {
                r = new Date(Long.valueOf(((Number) value).longValue()));
            } else {
                r = dateValueOf(value.toString()); //may be NumberFormatException

            }
            return r;
        }

        private Date dateValueOf(String value) {

            String s = value;
            if ( value.contains(":")) {
                s = value.substring(0,9);
            }
            java.sql.Date d;//My 06.03.2012 = null;
            try {
                 d = java.sql.Date.valueOf(s);
            } catch (Exception e) {
                return new Date(0);
            }
            return new java.util.Date(d.getTime());

        }
    }//class PDBDateColumn

    /**
     * Класс реализует абстрактный класс {@link tdo.DataColumn} для использования
     * с произвольными данными, кроме предопределенных. 
     * Sql-тип OTHER, определенный классом <code>java.sql.Types</code> соответствует
     * типу <code>java.lang.Object</code>.
     */
    public static class PDBJavaObjectColumn extends DataColumn implements Cloneable, Serializable {
        private Class instanceType;
        /**
         * Инициализирует значение полей и свойств, специфичных для класса.
         * <code><pre>
         *   setType(Object.class);
         *   setSqlType(java.sql.Types.OTHER);            
         *   this.setDefaultValue(new Object());
         * </pre></code>
         */
        public PDBJavaObjectColumn() {
            this.setType(Object.class);
            this.setSqlType(java.sql.Types.OTHER);
            this.setDefaultValue(new Object());
            this.instanceType = Object.class;
        }

        /**
         * Устанавливает новое значение по умолчанию для данных, связанных с колонкой.
         * Допустимым значением является только тип <code>java.lang.Object</code>.
         * @throws IllegalArgumentException когда новое значение равно 
         *          <code>null</code> или метод defaultValue.getClass() не равен
         *          <code>java.lang.Object</code>.
         */
        @Override
        public void setDefaultValue(Object defaultValue) {
            //if (defaultValue == null || ! defaultValue.getClass().equals(instanceType)) {
            if (defaultValue == null ) {            
                throw new IllegalArgumentException("Column defaultValue class must be java.lang.Object");
            }
            super.setDefaultValue(defaultValue);
        }

        public Class getInstanceType() {
            return this.instanceType;
        }
        
        public void setInstanceType(Class type) {
            this.instanceType = type;
        }

        /**
         * 
         * @param type
         */
        @Override
        public void setType(Class type) {
            super.setType(Object.class);
        }

        /**
         * @inherited
         */
        @Override
        public synchronized Object clone() {
            return (PDBJavaObjectColumn) super.clone();
        }

        /**
         * 
         * @param obj преобразуемый объект
         * @return объект, заданный параметром без преобразования.
         */
        @Override
        public Object toColumnType(Object obj) {
            return obj;
        }

        /**
         * Возвращает новый экземпляр объекта, используемого как значение 
         * по умолчанию для данных, связанных с колонкой.
         * 
         * @return значение по умолчанию. Если метод <code>getDefaultValue</code>
         *    возвращает <code>null</code>, то возвращается <code>null</code>.
         *    В остальных случаях возвращается новый экземпляр типа 
         *    <code>java.lang.Object</code>.
         */
        @Override
        public Object blankValueInstance() {
            Object obj = getDefaultValue();

            if (obj == null) {
                return null;
            }
            if ( this.instanceType == null )
                return new Object();
            else 
                try {
                    return this.instanceType.newInstance();
                } catch(Exception e) {
                    return new Object();
                }
        }
    }//class PDBJavaObjectColumn

    /**
     * Класс реализует абстрактный класс {@link tdo.DataColumn} для использования
     * с данными типа <code>java.lang.String</code>. 
     * Sql-тип VARCHAR и CHAR, определенные классом <code>java.sql.Types</code> соответствует
     * типу <code>java.lang.String</code>.
     */
    public static class PDBStringColumn extends DataColumn implements Cloneable, Serializable {

        /**
         * Инициализирует значение полей и свойств, специфичных для класса.
         * <code><pre>
         *   setType(String.class);
         *   setSqlType(java.sql.Types.CHAR);            
         *   this.setDefaultValue("");
         * </pre></code>
         */
        public PDBStringColumn() {
            this.setType(String.class);
            this.setSqlType(java.sql.Types.CHAR);
            this.setDefaultValue("");
        }

        /**
         * @return Возвращает значение типа данных равное <code>java.lang.String</code>.
         */
        @Override
        public Class getType() {
            return String.class;
        }

        /**
         * 
         * @param obj преобразуемый в строку знаков объект.
         * @return Если значение параметра равно <code>null</code>, то возвращается
         *    пустая строка.
         */
        /*        public String toString(Object obj) {
        if (obj == null) {
        return "";
        }
        return (String) obj;
        }
         */
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
            return (PDBStringColumn) super.clone();
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
            //My 06.03.2012return new String((String) obj);
            return obj.toString();
        }

        @Override
        protected int compareNotNull(Object o1, Object o2) {
            return ((String) o1).compareTo((String) o2);
        }
    }//class PDBStringColumn

    /**
     * Класс реализует абстрактный класс {@link tdo.DataColumn} для использования
     * с данными типа <code>java.sql.Time</code>. 
     * Sql-тип TIME, определенный классом <code>java.sql.Types</code> соответствует
     * типу <code>java.sql.Time</code>.
     */
    public static class PDBTimeColumn extends DataColumn implements Cloneable, Serializable {

        /**
         * Инициализирует значение полей и свойств, специфичных для класса.
         * <code><pre>
         *   setType(java.sql.Time.class);
         *   setSqlType(java.sql.Types.TIME);            
         *   this.setDefaultValue(new Time(0));
         * </pre></code>
         */
        public PDBTimeColumn() {
            this.setType(java.sql.Time.class);
            this.setSqlType(java.sql.Types.TIME);
            this.setDefaultValue(new Time(0));
        }

        /**
         * @return Возвращает значение типа данных равное <code>java.sql.Time</code>.
         */
        @Override
        public Class getType() {
            return Time.class;
        }

        /**
         * @inherited
         */
        @Override
        public synchronized Object clone() {
            PDBTimeColumn obj = (PDBTimeColumn) super.clone();
            return obj;
        }

        @Override
        protected int compareNotNull(Object o1, Object o2) {
            Time d1 = (Time) o1;
            Time d2 = (Time) o2;

            int result = 0;
            if (d1.equals(d2)) {
                result = 0;
            }
            if (d1.before(d2)) {
                result = -1;
            }
            if (d1.after(d2)) {
                result = 1;
            }
            return result;

        }

        /**
         * Возвращает новый экземпляр объекта, используемого как значение 
         * по умолчанию для данных, связанных с колонкой.
         * 
         * @return значение по умолчанию. Если метод <code>getDefaultValue</code>
         *    возвращает <code>null</code>, то возвращается <code>null</code>.
         *    В остальных случаях возвращается новый экземпляр типа 
         *   <code>java.sql.Time</code>, метод <code>getTime()</code> которого
         *   равен значению <code>getDefaultValue().getTime()</code>.
         */
        @Override
        public Object blankValueInstance() {
            Object obj = getDefaultValue();
            if (obj == null) {
                return null;
            }
            return new Time(((Time) obj).getTime());
        }
    }

    /**
     * Класс реализует абстрактный класс {@link tdo.DataColumn} для использования
     * с данными типа <code>java.sql.Timestamp</code>. 
     * Sql-тип TIMESTAMP, определенный классом <code>java.sql.Types</code> соответствует
     * типу <code>java.sql.Timestamp</code>.
     */
    public static class PDBTimestampColumn extends DataColumn implements Cloneable, Serializable {

        /**
         * Инициализирует значение полей и свойств, специфичных для класса.
         * <code><pre>
         *   setType(java.sql.Timestamp.class);
         *   setSqlType(java.sql.Types.TIMESTAMP);            
         *   this.setDefaultValue(new Timestamp(0));
         * </pre></code>
         */
        public PDBTimestampColumn() {
            this.setType(Timestamp.class);
            this.setSqlType(java.sql.Types.TIMESTAMP);
            this.setDefaultValue(new Timestamp(0));
        }

        /**
         * @return Возвращает значение типа данных равное <code>java.sql.Timestamp</code>.
         */
        @Override
        public Class getType() {
            return Timestamp.class;
        }

        /**
         * @inherited
         */
        @Override
        public synchronized Object clone() {
            PDBTimestampColumn obj = (PDBTimestampColumn) super.clone();
            return obj;
        }

        /**
         * @inherited
         */
        @Override
        protected int compareNotNull(Object o1, Object o2) {
            Timestamp d1 = (Timestamp) o1;
            Timestamp d2 = (Timestamp) o2;

            int result = 0;
            if (d1.equals(d2)) {
                result = 0;
            }
            if (d1.before(d2)) {
                result = -1;
            }
            if (d1.after(d2)) {
                result = 1;
            }
            return result;

        }

        /**
         * Возвращает новый экземпляр объекта, используемого как значение 
         * по умолчанию для данных, связанных с колонкой.
         * 
         * @return значение по умолчанию. Если метод <code>getDefaultValue</code>
         *    возвращает <code>null</code>, то возвращается <code>null</code>.
         *    В остальных случаях возвращается новый экземпляр типа 
         *   <code>java.sql.Timestamp</code>, метод <code>getTime()</code> которого
         *   равен значению <code>getDefaultValue().getTime()</code>.
         */
        @Override
        public Object blankValueInstance() {
            Object obj = getDefaultValue();
            if (obj == null) {
                return null;
            }
            return new Timestamp(((Timestamp) obj).getTime());
        }

        /**
         * Преобразует значение параметра в тип <code>java.sql.Timestamp</code>.
         * @param value преобразуемое значение
         * @return значение типа <code>java.sql.Timestamp</code>.          
         * Если <i>value</i> равно <code>null</code>, то возвращает 
         * <code>null</code>.<br>
         * Если <i>value</i> является типом <code>Timestamp</code>, то возвращается 
         * без преобразований.<br>
         * Если <i>value</i> является типом <code>Date</code>, то возвращается 
         * новый экземпляр, используя value.getTime().<br>
         * Если <i>value</i> является типом <code>Number</code>, то возвращается 
         * новый экземпляр, используя value.longValue().<br>
         * Если <i>value</i> тип отличен от указанных, то для создания 
         * возвращаемого значения производится попытка создания даты из
         * выражения value.toString(). При этом возможно исключение 
         * <code>java.text.ParseException</code>.
         */
        @Override
        public Object toColumnType(Object value) {
            if (value == null) {
                return null;
            }

            if (value instanceof Timestamp) {
                return value;
            }
            if (value instanceof Date) {
                Timestamp ts = (Timestamp) value;
                return new Timestamp(((Date) value).getTime());
            }

            Object r; // = null;

            if (value instanceof Number) {
                r = new Date(Long.valueOf(((Number) value).longValue()));
            } else {
                r = timestampValueOf(value.toString()); //may be ParseException

            }
            return r;
        }

        private Timestamp timestampValueOf(String value) {

            String s = value;
            if ( ! value.contains(":")) {
                s += " 00:00:00";
            }
            Timestamp ts;//My 06.03.2012 = null;
            try {
                 ts = Timestamp.valueOf(s);
            } catch (Exception e) {
                return new Timestamp(0);
            }
            return ts;

        }
    }//class PDBTimestampColumn
    
    /**
     * Класс реализует абстрактный класс {@link tdo.DataColumn} для использования
     * с данными типа <code>java.sql.Timestamp</code>. 
     * Sql-тип TIMESTAMP, определенный классом <code>java.sql.Types</code> соответствует
     * типу <code>java.sql.Timestamp</code>.
     */
    public static class PDBListColumn extends PDBJavaObjectColumn implements Cloneable, Serializable {

        /**
         * Инициализирует значение полей и свойств, специфичных для класса.
         * <code><pre>
         *   setType(java.sql.Timestamp.class);
         *   setSqlType(java.sql.Types.TIMESTAMP);            
         *   this.setDefaultValue(new Timestamp(0));
         * </pre></code>
         */
        public PDBListColumn() {
            this.setType(List.class);
            this.setSqlType(java.sql.Types.OTHER);
            this.setDefaultValue(new ArrayList());
        }

        /**
         * @inherited
         */
        @Override
        public synchronized Object clone() {
            PDBListColumn obj = (PDBListColumn) super.clone();
            return obj;
        }

        /**
         * @inherited
         */
        @Override
        protected int compareNotNull(Object o1, Object o2) {
            List d1 = (List) o1;
            List d2 = (List) o2;

            int result = -1;
            if (d1.equals(d2)) {
                result = 0;
            }
            return result;

        }

        /**
         * Возвращает новый экземпляр объекта, используемого как значение 
         * по умолчанию для данных, связанных с колонкой.
         * 
         * @return значение по умолчанию. Если метод <code>getDefaultValue</code>
         *    возвращает <code>null</code>, то возвращается <code>null</code>.
         *    В остальных случаях возвращается новый экземпляр типа 
         *   <code>java.sql.Timestamp</code>, метод <code>getTime()</code> которого
         *   равен значению <code>getDefaultValue().getTime()</code>.
         */
        @Override
        public Object blankValueInstance() {
            Object obj = getDefaultValue();
            if (obj == null) {
                return null;
            }
            if ( this.getInstanceType() != null ) {
                try {
                    return this.getInstanceType().newInstance();
                } catch(Exception e) {

                }
            }
            return new ArrayList();
        }

        /**
         * Преобразует значение параметра в тип <code>java.sql.Timestamp</code>.
         * @param value преобразуемое значение
         * @return значение типа <code>java.sql.Timestamp</code>.          
         * Если <i>value</i> равно <code>null</code>, то возвращает 
         * <code>null</code>.<br>
         * Если <i>value</i> является типом <code>Timestamp</code>, то возвращается 
         * без преобразований.<br>
         * Если <i>value</i> является типом <code>Date</code>, то возвращается 
         * новый экземпляр, используя value.getTime().<br>
         * Если <i>value</i> является типом <code>Number</code>, то возвращается 
         * новый экземпляр, используя value.longValue().<br>
         * Если <i>value</i> тип отличен от указанных, то для создания 
         * возвращаемого значения производится попытка создания даты из
         * выражения value.toString(). При этом возможно исключение 
         * <code>java.text.ParseException</code>.
         */
        @Override
        public Object toColumnType(Object value) {
           return value;
        }
        @Override
        public boolean copy(Object source, Object target) {
            if ( target == null && source == null )
                return false;
            List t;//My 06.03.2012 = null;
            if ( target == null && source != null ) {
                try {
                    t = (List) source.getClass().newInstance();
                } catch(Exception e ) {
                    return false;
                }
            } else {
                t = (List)target;
            }
            t.clear();
            t.add((List)source);
            return true;
             
        }        
    }//class PDBListColumn
    
    } //class DataColumn