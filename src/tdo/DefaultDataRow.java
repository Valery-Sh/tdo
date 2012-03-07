/*
 * DefaultDataRow.java
 * BUG LIST
 *   24.05.2008 Ряд находится в моде beginUpdateMode==true и !state.isEditing.
 *              Тогда метода endEdit и cancelEdit не изменяли моду на false
 * END BUG LIST
 */
package tdo;

import java.io.Serializable;
import static tdo.RowState.*;
import tdo.impl.ValidateException;
import tdo.service.DataRowServices;
import tdo.service.TableServices;

/**
 * Реализует интерфейс {@link tdo.DataRow} для представления одного ряда данных.
 * Методы класса обеспечивают следующие функции:
 * <ul>
 *   <li>Доступ к элементам данных по целочисленному индексу или имени;</li>
 *   <li>Изменение элементов данных, путем назначения заданного значения
 *      для заданного индекса или имени;
 *   </li>
 *   <li>Удаление ряда;</li>
 *   <li>Поддержку <i>буфферизации обновлений</i> ряда использованием методов
 *       {@link #beginEdit}, {@link #endEdit} и {@link #cancelEdit}. 
 *       Буфферизация позволяет отложить применения правил валидности данных, до
 *       выдачи метода <code>endEdit</code>;
 *   </li>
 *   <li>
 *       Поддержку версионности ряда.
 *   </li>
 * </ul>
 * 
 * <p>Каждый ряд содержит свойство <code>state</code> типа {@link tdo.RowState}.
 * Это свойство предоставляет сведения об <i>оригинальном(первичном)</i>  
 * <i>текущем</i> состоянии ряда, о текущей <i>моде буфферизации изменений</i>, 
 * оригинальном состоянии удаленного ряда, а также содержит хранилище поддержки
 * версионности. 
 * 
 * <p><i>Оригинальное</i> или <i>первичное</i> состояние ряда определяется
 * значением свойства <code>originalState</code> объекта <code>RowState</code>.
 * Для ряда в текущем состоянии DETACHED его значение также DETACHED и наоборот.
 * Значение DETACHED свойство <code>originalState</code> получает при 
 * первоначальном создании ряда. Когда ряд переводится в состояние INSERTING
 * или MANMADE значение свойства устанавливается в MANMADE. Когда ряд 
 * переводится в состояние LOADED значение свойства устанавливается в LOADED. 
 * Когда ряд переводится из состояния INSERTING в состояние DETACHED, 
 * свойству назначается значение DETACHED.
 * 
 * <p><i>Текущее</i> состояние редактирования ряда определяется значением свойства 
 * <code>editingState</code> объекта <code>RowState</code>. В каждый момент
 * времени ряд может находится в одном из следующих состояний:
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
 * <p>Мода <i>буфферизации</i> описывается булевым свойством 
 * <code>beginEditMode</code>. Значение <code>true</code> означает, что
 * мода буфферизации включена. <code>false</code>- выключена.
 * <p>Оригинальное состояние удаленного ряда поддерживается полем 
 * <code>originalState</code>. Когда ряд удален, то его текущее состояние равно
 * DELETED. Чтобы можно было определить первичное (оригинальное) состояние 
 * перед удалением устанавливается значение свойства <code>originalState</code>.
 *
 * <p>Хранилище поддержки версионности ряда состоит из двух
 * свойств:
 * <ol>
 *    <li><code>originalRow</code> типа <code>tdo.DataRow;</li>
 *    <li><code>updatingRow типа <code>tdo.DataRow</code></li>
 * </ol>
 * 
 * <h2>Буфферизация изменений</h2>
 * 
 * <p>Ряд при его модификации может поддерживать <i>"буфферизацию изменений"</i>.
 * Для этого, прежде чем проводить изменение данных ряда, для него выдается метод
 * <code>beginEdit()</code>, который <i>включает</i> буфферизацию. 
 * <p>Если для ряда буфферизация не используется, то любое изменения данных ряда
 * приводит к проверке ряда на вылидность.Рассмотрим следующий
 * сценарий.
 * 
 * <p>Требуется извлечь из таблицы реляционной базы данных сведения о прежних
 *    местах работы какого-либо сотрудника. Выполняется запрос к базе данных
 *    и полученный <code>java.sql.ResultSet</code> используется для заполнения
 *    таблицы <code>DataTable tbl;</code>. Таблица мест работы в числе других 
 *    полей содержит поля: <i>startDate</i> - дата приема на работу и 
 *    <i>endDate</i> - дата ухода. Одна из звписей содержит startDate=='13 mar 2002'
 *    и endDate=='12 may 2003'. Здесь оказалась случайная ошибка. На самом деле
 *    должно быть: startDate=='13 mar 2004' и endDate=='13 mar 2005'. Можно 
 *    предположить, что сначала пользователь, используя какую либо оконную
 *    форму изменит startDate на правильное значение, а зтем попытается перейти
 *    к изменению второго поля. Если не определены в приложении правила валидации,
 *    такие как <i><code>startDate <= endDate</code></i>, то все будет нормально. 
 *    Однако, если указанное выше правило определено, то возникает исключительная
 *    ситуация {@link tdo.impl.ValidateException}, что весьма не удобно. 
 * 
 * <p>Буфферизация изменений гарантирует, что валидация не производится, до тех
 * пор, пока не будет вызван метод {@link #endEdit}.
 * 
 * <p>Буфферизация <i>включается</i> выполнением метода {@link #beginEdit} 
 * только при одновременном выполнении следующих условий:
 *  <ul>
 *     <li>текущее значение модификатора <code>beginEditMode</code> установлено
 *         в <code>false</code>, т.е. буфферизация <i>выключена</i>.</li>
 *     <li>значение <code>editingState</code> содержит одно из следующих значений 
 *         <i>LOADED,MANMADE,UPDATED</i>. Любое другое значение завершает метод.</i>.
 *     </li>
 *     <li>метод {@link tdo.Table#isLoading}</li> возвращает 
 *          <code>false</code>
 *      </li>
 *     <li>метод {@link tdo.Table#isEditProhibited}</li> возвращает 
 *          <code>false</code>
 *      </li>
 *  </ul>
 * 
 * <p>Если все из указанных выше условий выполнены, то <i>буфферизация</i>
 * включается, т.е. <code>beginEditMode</code> устанавливается в 
 * <code>true</code>. Вызывается защищенный метод {@link #updateRowVersions} , 
 * который выполняет действия по сохранению текущих данных ряда. Отметим, что 
 * метод  <code>beginEdit()</code> не меняет текущего состояния 
 * <code>state.editingState</code>.
 * 
 * <p>Модификатор <code>beginEditMode</code> может быть установлен в  
 * <code>false</code> выполнением одного из методов: {@link #endEdit} или
 * {@link #cancelEdit}.
 * 
 * <p> Метод {@link #endEdit} изменяет текущую моду при одновременном
 * выполнении всех, указанных ниже условий:
 *  <ul>
 *     <li> Одно из:
 *        <ol>
 *          <li>
 *             Ряд находится в одном из состояний: INSERTING,UPDATING или DELETING
 *           </li>
 *           <li>Ряд находится в состоянии LOADED,MANMADE,UPDATED, 
 *                а его мода <code>beginEditMode</code> установлена в 
 *               <code>true</code>, т.е. буфферизация <i>включена</i>.
 *           </li>
 *        </ol>  
 *     </li>
 *     <li>Вызов метода {@link tdo.Table#fireValidate(tdo.DataRow)}
 *         не вызвал исключительную ситуацию {@link tdo.impl.ValidateException}
 *     </li>
 *  </ul>
 * 
 * <p> Метод {@link #cancelEdit} изменяет текущую моду при 
 * выполнении одного из указанных ниже условия:
 *  <ul>
 *     <li>Ряд находится в одном из состояний: INSERTING,UPDATING или DELETING. 
 *     </li>
 *     <li>
 *         Ряд находится в состоянии LOADED,MANMADE,UPDATED, а его мода
 *         <code>beginEditMode</code> установлена в <code>true</code>, , т.е. 
 *         буфферизация <i>выключена</i>.
 *     </li>
 *  </ul>
 *
 * 
 * <h2>Версионность ряда</h2>
 * 
 * Для каждого состояния, которое было на момент вызова метода 
 * <code>setValue</code> прежде, чем будет назначено новое значение ячейке ряда,
 * проводятся действия по сохранению данных ряда в хранилищах объекта 
 * <code>RowState</code>. 
 * 
 * <p>Чтобы понять, зачем необходима поддержка версионности, предположим,
 * что в пользовательской базе базе есть таблица Person, которую надо 
 * отредактировать.
 * Выполняем SQL-запрос, например, "select * from Person" и полученными
 * данными, например, <code>java.sql.ResultSet</code> заполняем объект типа 
 * {@link tdo.Table}. Теперь образ рядов таблицы находится в памяти программы 
 * как объект <b>tbl</b> типа <code>tdo.Table</code>
 * и работа с данными проводится в отсоединенном от базы данных состоянии.
 * 
 * <p> Мы дабавляем в таблицу <b>tbl</b> несколько рядов, 
 * некоторые ряды удаляем, а часть модифицируем. Завершением таких действий
 * должно стать проведение изменений в таблице базы данных Person. Независимо от
 * того, используется ли JDBC или другие средства для репликации, мы должны
 * предоставить коду, выполняющему репликацию, <i>"дельту"</i> таблицы 
 * <b>tbl</b>. Более конкретно, необходимо уметь для каждого ряда <b>tbl</b>
 * ответить на вопросы:
 * <ul>
 *    <li>Ряд модифицирован, удален, оставлен без изменения или пользователь
 *        "вручную" ввел новый ряд?.
 *    </li>
 *    <li>
 *       Если ряд модифицирован, то каково было его оригинальное состояние?
 *    </li>
 * </ul>
 * 
 * <p>Для того, чтобы обеспечить решение первого вопроса, классы, реализующие
 * интерфейс {@link tdo.Table} определяет свойство <code>loading</code>.
 * Тогда при заполнении <b>tbl</b> данными из <code>ResultSet</code>
 * мы устанавливаем свойство <code>loading</code> в <code>true</code>. 
 * Добавляемые ряды будут иметь состояние <code>LOADED</code>. По окончании 
 * заполнения устанавливаем свойство <code>loading</code> в <code>false</code>. 
 * С этого момента, новые ряды будут добавляться в состоянии 
 * <code>INSERTING</code> или  <code>MANMADE</code>.
 * 
 * <p> Для решения второго вопроса служат свойства <code>originalRow</code> и
 * <code>updatingRow</code> класса {@link tdo.RowState}. При самой первой 
 * модификации ряда, находящегося в состоянии LOADED, создается новый экземпляр
 * <code>DataRow</code> и в его содержимое копируется содержимое текущего,
 * модифицируемого ряда. Это обеспечивает возможность восстановлени оригинального
 * ряда в любой момент, когда понадобится. После того как ряд модифицирован и
 * для него успешно исполнен метод <code>endEdit</code> он переводится в 
 * состояние UPDATED. 
 * 
 * <h2>Включение буфферизации изменений</h2>
 * 
 * Буфферизация изменений включается методом <code>beginEdit</code>.
 * В зависимости от текущего состояния ряда, проводятся действия по 
 * поддержки версионности ряда:
 * 
 * <ul>
 *      <li>LOADED - создается копия ряда, которая сохраняется в
 *                    {@link tdo.DefaultRowState#originalRow}.
 *                    Значение свойства {@link tdo.DefaultRowState#updatingRow} 
 *                    устанавливается в <code>null</code>. 
 *      <li>MANMADE  -  создается копия ряда, которая сохраняется в
 *                    {@link tdo.DefaultRowState#updatingRow}.
 *      <li>UPDATED   - аналогично MANMADE: создается копия ряда, которая 
 *                      сохраняется в {@link tdo.DefaultRowState#updatingRow}.
 * </ul>
 *
 * <b>Примечание.</b> метод не изменяет состояние ряда.
 * 
 *  <h2>Завершения редактирование методом <code>endEdit</code></h2>
 * 
 * <p>Для завершения редактирование ряда, находящегося в моде
 * <code>beginEditMode</code> используется метод {@link #endEdit()}. 
 * Для ряда, находящегося в состоянии INSERTING или UPDATING выполняется вызов
 * {@link tdo.Table#fireValidate(tdo.DataRow)}, который может выбросить 
 * исключительную ситуацию {@link tdo.impl.ValidateException}, тем самым завершая
 * метод не проводя никаких действий.
 * <p>Если ряд успешно прошел проверку, то для каждого состояния, которое было 
 * на момент вызова этого метода:
 *    <ul>  
 *      <li>INSERTING  - назначается состояние MANMADE. Значение свойства 
 *                       {@link tdo.DefaultRowState#updatingRow} 
 *                       устанавливается в <code>null</code>. 
 *      </li>     
 *
 *      <li>UPDATING   - Новое состояние ряда в этом случае зависит от значения
 *                       свойства <code>originalRow</code>. Если оно содержит 
 *                       <code>null</code>, то это означает, что первичным 
 *                       состоянием ряда было MANMADE. Поэтому новое состояние
 *                       становится MANMADE. В противном случае новому состоянию
 *                       присваивается значение UPDATED.
 *                       Значение свойства 
 *                       {@link tdo.DefaultRowState#updatingRow} 
 *                       устанавливается в <code>null</code>. 
 *      </li> 
 *   </ul>
 * 
 * <h2>Отмена редактирования ряда методом <code>cancelEdit</code></h2>
 * 
 * <p>Для отмены редактирование ряда используется метод {@link #cancelEdit()}. 
 * 
 * <p>Для каждого состояния, которое было на момент вызова этого метода:
 *    <ul>  
 *      <li>INSERTING  - назначается состояние DETACHED. Ряд должен быть удален
 *                       из таблицы. Свойству <code>originalState</code> 
 *                       назначается значение DETACHED.
 *      </li>     
 *
 *      <li>UPDATING   - Новое состояние ряда в этом случае зависит как от 
 *              значения свойства <code>originalRow</code>, так и от 
 *              значения свойства <code>updatingRow</code>. 
 * 
 *              Если <code>originalRow</code> не <code>null</code> и 
 *                <code>updatingRow</code> не <code>null</code>, то новым 
 *                состоянием становится <code>UPDATED</code>. 
 *                Ячейки <code>updatingRow</code> копируются в данный ряд. 
 *              Если <code>originalRow</code> не <code>null</code>, а 
 *                <code>updatingRow</code> равен <code>null</code>, то новым 
 *                состоянием становится <code>LOADED</code>.
 *                Ячейки <code>originalRow</code> копируются в данный ряд.
 *              Если <code>originalRow</code> равен <code>null</code>, 
 *                то, независимо от <code>updateingRow</code>
 *                новым состоянием становится <code>MANMADE</code>. 
 *                Ячейки <code>updatingRow</code> копируются в данный ряд.
 *      </li> 
 *   </ul>
 * 
 * Свойству <code>updatingRow</code>  в конце исполнения метода назначается 
 * значение <code>null</code>.
 * Свойству <code>beginEditRow</code> назначается значение <code>false</code>.
 * 
 * <h2>Изменение данных методом <code>setValue</code></h2>
 * 
 *  Метод <code>setValue</code>, помимо своей основной задачи, выполняет 
 * действия по поддержке версионности.
 * 
 *  В зависимости от текущего состояния ряда, обновление версий ряда проводится
 *  по следующим правилам:
 * 
 * <ul>
 *      <li>LOADED - создается копия ряда, которая сохраняется (свойство
 *                    {@link tdo.DefaultRowState#originalRow}.
 *                    Значение свойства {@link tdo.DefaultRowState#updatingRow} 
 *                    устанавливается в <code>null</code>. 
 *      <li>MANMADE  -  создается копия ряда, которая сохраняется (свойстве
 *                    {@link tdo.DefaultRowState#updatingRow}.
 *      <li>UPDATED   - аналогично MANMADE: создается копия ряда, которая 
 *                      сохраняется (свойстве
 *                     {@link tdo.DefaultRowState#updatingRow}.
 * </ul>
 * 
 * <b>Примечание.</b> В приведенном выше списке присутствуют не все состояния. 
 *   Если ряд находится в другом состоянии то действий с версиями ряда не 
 * проводится.
 * 
 * <p> Если <i>буфферизация измененний</i> <b>выключена</b>, то каждое
 * выполнение метода <code>setValue</code> переводит ряд из текущего состояния 
 * в новое по правилам, приведенным ниже:
 * <ul>
 *      <li>DETACHED - <i>не изменяется</i></li>
 *      <li>LOADED - <i>UPDATED</i></li>
 *      <li>INSERTING - <i>не изменяется</i></li>
 *      <li>MANMADE  - <i>не изменяется</i></li>
 *      <li>UPDATING  - <i>UPDATED</i></li>
 *      <li>UPDATED   - <i>не изменяется</i></li>
 * </ul>
 *
 * <p> Если <i>буфферизация измененний</i> <b>включена</b>, то каждое
 * выполнение метода <code>setValue</code> переводит ряд из текущего состояни 
 * в новое по правилам, приведенным ниже: 
 * <ul>
 *      <li>LOADED - <i>UPDATING</i></li>
 *      <li>INSERTING - <i>не изменяется</i></li>
 *      <li>MANMADE  - <i>UPDATING</i></li>
 *      <li>UPDATING  - <i>не изменяется</i></li>
 *      <li>UPDATED   - <i>UPDATING</i></li>
 * </ul>
 * 
 * <h2>Удаление ряда</code></h2>
 * 
 * Ряд удаляется применением метода {@link #delete}.     
 * Если ряд находится в состоянии DETACHED или DELETED, то выполнение метода
 * завершается. 
 * <p>Результат удаления зависит от текущего состояния ряда.
 * <p>Если текущее состояние INSERTING, то новым состоянием становится 
 * DETACHED. Свойству <code>originalState</code> назначается значение DETACHED.
 * <p>Если текущее состояние LOADED, то новым состоянием становится 
 * DELETED. 
 * <p>Если текущее состояние MANMADE, то новым состоянием становится 
 * DELETED. 
 * <p>Если текущее состояние UPDATING, то новым состоянием становится 
 * DELETED, а текущий ряд восстанавливается из <code>updatingRow</code>.
 * Иначе - MANMADE текущий ряд восстанавливается из <code>updatingRow</code>.
 * 
 * 
 * <h2>Состояние <i>DETACHED</i> </h2>
 * 
 * <p>Когда ряд впервые создается, то он получает состояние: 
 * <code><i>DETACHED</i></code>. для такого ряда метод класса 
 * <code>tdo.RowState</code> <code>isDetached()</code> возвращает 
 * <code>true</code>. Если при создании ряда не задан {@link tdo.Table}, 
 * то выбрасывается исключение <code>IllegalArgumentException</code>. 
 * <p>При создании экземпляра класса 
 * также создается объект {@link tdo.RowState} и назначается свойству 
 * <code>state</code>. Свойство <code>readOnly</code> устанавливается в 
 * <code>false</code>. 
 * <p>Из состояния DETACHED ряд может быть переведен в другое состояние
 * применением одного из двух методов:
 * <ul>
 *   <li>{@link #attach()}</li>
 *   <li>{@link #attachNew()}</li>
 * </ul>
 * 
 *  <p>Если ряд уже находился в состоянии, отличном от <code>DETACHED</code>, то
 *  метод <code>attach()</code> не выполняет каких-либо опрераций, в то время
 *  как метод <code>attachNew</code> выбрасывает исключение.
 * 
 * <p>Метод <code><b>attach()</b></code> переводит ряд в одно из состояний:
 *   <ul>
 *      <li>LOADED если метод {@link tdo.Table#isLoading()}</li> возвращает
 *          <code>true</code>. Свойству <code>originalState</code> 
 *          назначается значение LOADED;
 *      <li>MANMADE в противном случае. Свойству <code>originalState</code> 
 *          назначается значение MANMADE.</li>
 *   </ul>
 * 
 * <p>Метод <code><b>attachNew()</b></code> выбрасывает исключение 
 *   {@link tdo.DataOperationException} в случаях, когда 
 *   <code>table.isLoading()</code> возвращает <code>true</code> или ряд 
 *   находится в состоянии, отличном от <code>DETACHED</code>.
 * <p>Метод <code>attachNew()</code>, если не выбрасывает исключение, 
 * всегда переводит ряд в состояние <code>INSERTING</code>. Свойству 
 * <code>originalState</code> назначается значение MANMADE;
 * <p> 
 * <p>Классы, реализующие интерфейс <code>tdo.Table</code>, используют метод
 * <code>attach()</code> и <code>attachNew()</code> при вставке новых рядов. 
 * Метод {@link tdo.Table#addRow()} предполагается использовать для добавления 
 * "пустого" ряда, который сразу переводится в состоянии <code>INSERTING</code> 
 * и который физически удаляется из таблицы методом {@link #cancelEdit} или 
 * переводится в состояние <code>MANMADE</code> методом {@link #endEdit}.
 * 
 * <p>Метод {@link tdo.Table#addRow(tdo.DataRow)}
 * предполагается использовать для добавления уже существующего ряда, который 
 * таблица интерпретирует или как LOADED или как MANMADE. Состояние 
 * LOADED ряда означает, что при изменении его данных, оригинальные данные,
 * т.е. те,которые были на момент добавления ряда, сохраняются и , при 
 * необходимости могут быть восстановлены. Обычно, когда таблица заполняется 
 * данными из таблицы СУБД, то после изменении одного, нескольких или всех рядов
 * необходимо отразить эти изменения в базе данных. Измененные ряды, которые 
 * первоначально попали в таблицу в состоянии LOADED, теперь будут в
 * состоянии UPDATED или DELETED и для сохранения потребуюся SQL-опрерации
 * UPDATE или DELETE. 
 * <p> Если ряд добавляется в таблицу и ему назначается состояние MANMADE, то
 * таблица рассматривает такой ряд "введенный пользователем за текущий сеанс". 
 * В процессе работы  с рядом он, в конечном счете, или будет удален или 
 * останется в состоянии MANMADE. Для репликации в базу данных потребуется 
 * SQL-операция INSERT.
 * 
 * <h2>Жизненный цикл ряда, начальное состояние которого <code><i>DETACHED</i></code></h2>
 * 
 *  Применение методов <code>beginEdit,endEdit,cancelEdit</code> не изменяют
 * данные и состояние. 
 * 
 * <h2>Метод <code><i>setValue</i></code></h2>
 *  Новое значения колонки ряда, устанавливается при одновременном выполнении 
 * следующих условий:
 *  <ul>
 *     <li>Свойство <code>readOnly</code> колонки, заданной параметром равно 
 *         <code>false</code> (см. {@link tdo.DataColumn}).
 *     </li>
 *     <li>Свойство <code>editProhibited</code> таблицы равно
 *         <code>false</code> (см. {@link tdo.Table#isEditProhibited() } ).
 *     </li>
 *     <li> Если новое значение ячейки ряда, устанавливаемое методом 
 *          <code>setValue</code> равно <code>null</code> и заданная колонка
 *          допускает <code>null</code> значения, т.е. 
 *          метод {@link tdo.DataColumn#isNullable(}) возвращает
 *         <code>true</code>. В противном случае выбрасывается исключение
 *         <code>IllegalArgumentException</code>.
 *     </li>
 *  </ul>
 *  Если хотя бы одно из условий не выполнено, метод завершается. <br>
 * При выполнении всех условий заданная ячейка ряда модифицируется и метод
 * завершается. 
 * 
 * <ul>
 *    <li>Состояние <code>editingState</code> не изменяется.</li> 
 *    <li>Исходное данные ряда, бывшие  до изменения, не сохраняются.</li
 *  </ul>
 * 
 * <h2>Состояние <code>LOADED</code></h2>
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
     * Создает экземпляр класса для заданного контекста таблицы и инициализирует
     * свойства ряда значениями по умолчанию.
     * 
     * Создает новый объект типа {@link tdo.DefaultRowState} и назначает его
     * значение свойству <code>state</code>. Состояние ряда устанавливается
     * DETACHED. Свойству <code>readOnly</code> назначается <code>false</code>.
     * 
     * @param services контекст таблицы, для которой создается объект
     * @throws  IllegalArgumentException если значение параметра 
     *       <code>services</code> равно <code>null</code>.
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
     * Создает экземпляр класса для заданного контекста таблицы и pзаданной
     * коллекции ячеек.
     * 
     * 
     * @param services контекст таблицы, для которой создается объект
     * @cells заданная коллекция ячеек ряда
     * @throws  IllegalArgumentException если значение параметра 
     *       <code>services</code> равно <code>null</code>.
     */
    protected DefaultDataRow(TableServices services, DataCellCollection cells) {
        this(services);
        this.cells = cells;
    }

    /**
     * Возвращает текущий провайдер ячеек ряда.
     * 
     * Класс не содержит информации о внутренней структуре обрабатываемых
     * данных и о соответствии индексов ячеек ряда и действительными данными.
     * Все операции по доступу к содержимому, которые требуют индекс колонки
     * или ее имя, делегируются объекту типа <code>DataCellProvider</code>.
     * 
     * @return текущий провайдер ячеек ряда.
     * @see tdo.DataCellProvider
     * @see #setDataCellProvider
     */
    @Override
    public DataCellCollection getCellCollection() {
        return cells;
    }

    /**
     * Устанавливает провайдер ячеек ряда.
     * 
     * Класс не содержит информации о внутренней структуре обрабатываемых
     * данных и о соответствии индексов ячеек ряда и действительными данными.
     * Все операции по доступу к содержимому, которые требуют индекс колонки
     * или ее имя, делегируются объекту типа <code>DataCellProvider</code>.
     * 
     * @param cells новый объект провайдера ячеек ряда
     * @see tdo.DataCellProvider
     * @see #getDataCellProvider
     */
    protected void setCellCollection(DataCellCollection cells) {
        this.cells = cells;
    }

    /**
     * Копирует содержимое заданного ряда.
     * 
     * Если значение параметра <code>null</code>, то операцияя не производится.
     * Копируются только ячейки ряда. Состояние полей и свойств ряда, в том 
     * числе и свойства <code>state</code> не изменяется.
     * <p>Делегирует выполнение провайдеру ячеек.
     * @param row ряд, содержимое которого копируется
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
     * Копирует элементы заданного массива в ячейки ряда.
     * 
     * Копируются только ячейки ряда. Состояние полей и свойств ряда, в том 
     * числе и свойства <code>state</code> не изменяется.
     * 
     * <p>Делегирует выполнение провайдеру ячеек.
     * 
     * @param obj мпассив значений, элементы которого копируется в данный ряд
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
     * Создает и возвращает новый ряд, значение ячеек которого совпадает со
     * значениями ячеек текущего ряда.
     * @return новый ряд-копия текущего
     */
    @Override
    public DataRow createCopy() {
        DataRow r = newRow();
        r.copyFrom(this);
        return r;
    }

    /**
     * Создает и возвращает новый ряд, значение ячеек которого совпадает со
     * значениями ячеек заданного ряда.
     * @return новый ряд-копия заданного ряда
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
     * Создает версии ряда в зависимости от его текущего состояния.
     * 
     * Если текущее состояние LOADED, то создает копию текущего ряда
     * и помещает ее в <code>originalRow</code> объекта <code>RowState</code>.
     * Свойству <code>updatingRow</code> назначается <code>null</code>.
     * 
     * <p>Если текущее состояние MANMADE или UPDATED, то создает копию текущего 
     * ряда и помещает ее в <code>updatingRow</code> объекта 
     * <code>RowState</code>. Свойство <code>originalRow</code> сохраняет 
     * значение <code>null</code>.
     * 
     * <p>Метод не изменяет текущего состояния ряда и моды буфферизации.
     * 
     * @see #setValue(Object,int)
     * @see #cancelEdit
     */
    protected void updateRowVersions() {
        if (getState().isLoaded()) {
            // the row has been inserted in LOADING mode,
            // hasn't been updated yet. =>
            // clone the row and save it in the row state object
            //DataRow orow = this.rowProvider.createRowCopy(this); // новый экземпляр, как копия ряда row
            DataRow orow = this.createCopy();
            state.setOriginalRow(orow);
            state.setUpdatingRow(null);
        }

        if (getState().isUpdated() || getState().isManMade()) {
            // clone the row and save it in the row state object
            // This copy might be used when method cancelEditing invoked.
            //DataRow orow = this.rowProvider.createRowCopy(this); // новый экземпляр, как копия ряда row
            DataRow orow = this.createCopy();
            state.setUpdatingRow(orow);
        }
    }

    /**
     * Изменяет версии рядов в зависимости от текущего состояния ряда.
     * 
     * Выключает буфферизацию изменений, назначая свойству 
     * <code>beginEditMode</code> значение <code>false</code>.
     * 
     * Если текущее состояние ряда LOADED, то свойству <code>originalRow</code>
     * и свойству <code>updatingRow</code> объекта <code>state</code> 
     * назначает <code>null</code>.
     * <p> Если текущее состояние ряда UPDATED или MANDATE, 
     * то свойству <code>updatingRow</code> объекта <code>state</code> 
     * назначает <code>null</code>.
    
     * Вызывается из методов {@link #endEdit} или 
     * {@link #cancelEdit}, когда мода <code>beginEditMode</code> равна <code>true</code>,
     * рад находится в состоянии LOADED,MANMADE или UPDATED
     * переведен в состояние моды ишпшь
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
     * Отменяет текущие изменения и восстанавливает содержимое ряда в состояние 
     * до изменений.
     * 
     *  <p>Если буфферизация изменений ряда включена, и состояние ряда не равно
     *  UPDATING и не равно INSERTING, то значения <code>originalRow</code> и 
     *  <code>updatingRow</code> устанавливаются в <code>null</code>. 
     *  <code>beginEditMode</code> устанавливается в <code>false</code> и метод
     *  завершается.
     *  <p><b>Примечание.</b> Буфферизация ряда не может быть включена для ряда,
     *    находящегося в состоянии DETACHED.
     * 
     * <p>Для каждого состояния, которое было на момент вызова этого метода:
     *    <ul>  
     *      <li>INSERTING  - назначается состояние DETACHED. Ряд должен быть удален
     *                       из таблицы.
     *      </li>     
     *
     *      <li>UPDATING   - Новое состояние ряда в этом случае зависит как от 
     *              значения свойства <code>originalRow</code>, так и от 
     *              значения свойства <code>updatingRow</code>. 
     * 
     *              Если <code>originalRow</code> не <code>null</code> и 
     *                <code>updatingRow</code> не <code>null</code>, то новым 
     *                состоянием становится <code>UPDATED</code>. 
     *                Ячейки <code>updatingRow</code> копируются в данный ряд. 
     *              Если <code>originalRow</code> не <code>null</code>, а 
     *                <code>updatingRow</code> равен <code>null</code>, то новым 
     *                состоянием становится <code>LOADED</code>.
     *                Ячейки <code>originalRow</code> копируются в данный ряд.
     *              Если <code>originalRow</code> равен <code>null</code>, 
     *                то, независимо от <code>updateingRow</code>
     *                новым состоянием становится <code>MANMADE</code>. 
     *                Ячейки <code>updatingRow</code> копируются в данный ряд.
     *      </li> 
     *   </ul>
     * Свойству <code>updatingRow</code>  в конце исполнения метода назначается 
     * значение  <code>null</code>.
     * Свойству <code>beginEditRow</code> назначается значение <code>false</code>.
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
            // INSERTING/NONE ряд ранее вставлен при Table.isLoading
            // => удаляем полностью ряд и его состояние
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
                // Ряд ранее был загружен в состоянии MANMADE
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
     * Оповещает таблицу об изменения ряда или его состояния.
     * 
     * @param cause причина, по которой выполняется оповещение
     * @see tdo.Table#fireRowEditing(int, tdo.DataRow) 
     */
    private void notifyTableOf(int cause) {
        rowServices.processRowEditing(cause, this, -1);
    }

    /**
     * Оповещает таблицу об изменения колонки ряда.
     * 
     * @param cause причина, по которой выполняется оповещение
     * @param columnIndex колонка, связанная с изменением
     * 
     * @see tdo.Table#fireRowEditing(int, tdo.DataRow, int) 
     */
    private void notifyTableOf(int cause, int columnIndex) {
        rowServices.processRowEditing(cause, this, columnIndex);
    }

    /**
     * Переводит ряд из состояния DETACHED в состояние LOADED или MANMADE.
     * 
     * Если текущее состояние ряда не равно DETACHED, то действий не производится.
     * <p>Если метод таблицы {@link tdo.Table#isLoading() } возвращает 
     * <code>true</code>, то ряд переводится в состояние LOADED. В противном 
     * случае, новым состоянием становится MANMADE.
     * 
     * Метод предназначен для внутреннего использования.
     * Не следует использовать в приложении. Классы-наследники могут 
     * переопределять и использовать метод, обращая внимание на реализацию
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
     * Переводит ряд из состояния DETACHED в состояние INSERTING.
     * 
     * Если текущее состояние ряда не равно DETACHED, то выбрасывается исключение
     * {@link tdo.DataOperationException}.
     * <p>Если метод таблицы {@link tdo.Table#isLoading() } то выбрасывается 
     * исключение {@link tdo.DataOperationException}.
     * <p>Новое состояние ряда становится INSERTING, а значение свойства 
     * <code>originalState</code> устанавливается равным MANMADE.
     * 
     * <p>Метод предназначен для внутреннего использования.
     * Не следует использовать в приложении. Классы-наследники могут 
     * переопределять и использовать метод, обращая внимание на реализацию
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
     * Устанавливает новое значение индицирующее возможность изменения ячеек ряда.
     * 
     * Метод может быть полезен в случае, например, когда ряд таблица 
     * отображается в <code>javax.swing.JTable</code> и мы хотим, чтобы
     * некоторые ряды не могли быть выбраны для редактирования.
     * @param readOnly новое значение индикатора
     */
    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * Возвращает значение индицирующее возможность изменения ячеек ряда.
     * 
     * Метод может быть полезен в случае, например, когда ряд таблица 
     * отображается в <code>javax.swing.JTable</code> и мы хотим, чтобы
     * некоторые ряды не могли быть выбраны для редактирования.
     * 
     * @return <code>true</code> если значения ячеек ряда не могут изменяться.
     *   <code>false</code> в противном случае.
     */
    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Возвращает экземпляр объекта, описывающего состояние ряда.
     * 
     * @return экземпляр типа <code>tdo.RowState</code>
     * @see {@link tdo.RowState}
     * @see {@link #DefaultRowState}
     */
    @Override
    public RowState getState() {
        return state;
    }

    /**
     * Возвращает значение по заданному индексу колонки.
     * Метод делегирует исполнение провайдеру {@link tdo.DataCellProvider }
     * @param columnIndex индекс колонки
     * @return значение ячейки ряда, соответствующее заданному индексу колонки
     */
    @Override
    public Object getValue(int columnIndex) {
        if (rowServices.isCalculated(columnIndex)) {
            return rowServices.calculateColumnValue(this, columnIndex);
        }
        return cells.getValue(columnIndex);
    }

    /**
     * Возвращает значение по заданному имени колонки.
     * Метод делегирует исполнение провайдеру {@link tdo.DataCellProvider }     
     * @param columnName имя колонки
     * @return значение ячейки ряда, соответствующее заданному имени колонки
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
     * Проводит удаление ряда. 
     * 
     * Если ряд находится в состоянии DETACHED или DELETED, то выполнение метода
     * завершается. 
     * <p>Результат удаления зависит от текущего состояния ряда.
     * <p>Если текущее состояние INSERTING, то новым состоянием становится 
     * DETACHED.
     * <p>Если текущее состояние LOADED, то новым состоянием становится 
     * DELETED. 
     * <p>Если текущее состояние MANMADE, то новым состоянием становится 
     * DELETED. 
     * <p>Если текущее состояние UPDATING, то новым состоянием становится 
     * DELETED, а текущий ряд восстанавливается из <code>updatingRow</code>.
     * <p>В дальнейшем, если потребуется восстановить ряд в состояние ДО 
     * удаления, то:
     * <p>Если <code>originalState</code> LOADED или MANMADE, то текущий ряд
     *    содержит первичные данные.
     * <p>Если <code>originalState</code> равен UPDATED, то это означает, 
     *   что первичным состоянием ряда было LOADED, ряд менялся и текущий ряд
     *   содержит данные, которые были на момент удаления. Можно восстановить
     *   также первичное состояние по содержимому <code>originalRow</code>. 
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
     * Включает буфферизацию изменений ряда.
     * 
     * <p>Буфферизация <i>включается</i> 
     * только при одновременном выполнении следующих условий:
     *  <ul>
     *     <li>текущее значение модификатора <code>beginEditMode</code> установлено
     *         в <code>false</code>, т.е. буфферизация <i>выключена</i>.</li>
     *     <li>значение <code>editingState</code> содержит одно из следующих значений 
     *         <i>LOADED,MANMADE,UPDATED</i>. Любое другое значение завершает метод.</i>.
     *     </li>
     *     <li>метод {@link tdo.Table#isLoading}</li> возвращает 
     *          <code>false</code>
     *      </li>
     *     <li>метод {@link tdo.Table#isEditProhibited}</li> возвращает 
     *          <code>false</code>
     *      </li>
     *  </ul>
     * 
     * <p>Если все из указанных выше условий выполнены, то <i>буфферизация</i>
     * включается, т.е. <code>beginEditMode</code> устанавливается в 
     * <code>true</code>. Вызывается защищенный метод {@link #updateRowVersions},
     * который выполняет действия по сохранению текущих данных ряда. 
     * <p>Отметим, что метод <code>beginEdit()</code> не меняет текущего 
     * состояния  <code>state.editingState</code>.
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
     * Устанавливает ячейку ряда, специфицированную индексом колонки в заданное
     * значение.
     *  Метод не производит нмкаких действий, если выполнено хотя бы одно 
     * из условий:
     * <ol>
     *   <li>
     *      Колонка с заданным индексом определена как <code>readOnly</code>
     *   </li>
     *   <li>Ряд находится в состоянии, отличном от DETACHED, а свойство 
     *       {@link tdo.Table#isEditProhibited} таблицы, к которой
     *       принадлежит ряд, возвращает <code>true</code>.
     *   </li>
     *   <li>Ряд находится в состоянии DELETED</li>
     *   <li></li>
     * </ol>
     * 
     * <p>Ряд может выбросить исключение <code>tdo.impl.ValidateException</code>
     * и, следовательно, завершить выполнение метода, не произведя каких-либо 
     * операций. Исключение выбрасывается, если колонка <code>columnIndex</code> 
     * определена как не допускающая <code>null</code>, а значение параметра 
     * <code>value</code> равно <code>null</code>.
     * 
     * <p>Если изменение ряда может быть проведено, то перед назначением
     * нового значения ячейке ряда могут быть выполнены действия по поддержки
     * <i>версионности</i> ряда вызовом метола {@link #updateRowVersions}. вызов
     * метода проводится, если одновременно выполнены следующие условия:
     * <ul>
     *   <li>
     *      Текущее состояние ряда не является DETACHED, UPDATING,INSERTING;
     *   </i>
     *   <li>
     *      Буферизация ряда выключена (beginEditMode == false ).
     *   </i>
     * </ol>
     *
     *  <p>Метод <code>setValue</code>, помимо своей основной задачи, выполняет 
     * действия по поддержке версионности, делегируя исполнение защищенному 
     * методу {@link #updateRowVersions}.
     * 
     * <p>После установки нового значения ячейки ряда выполняется обновление
     * текущего состояния ряда вызовом метода {@link #updateState}.
     *     
     * @param value новое значение ячейки ряда
     * @param columnIndex индекс колонки, для которой определяется ячейка ряда
     * @see #setValue(java.lang.Object, java.lang.String) 
     * @throws tdo.impl.ValidateException инициируется как данным методом так и 
     *          методом  <code>updateState</code>.
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
     * Обновляет текущее состояние ряда после изменения ячейки ряда.
     * 
     * <p>Метод может выбросить исключение {@link tdo.impl.ValidateException}.
     * Контроль валидности проводится только, если <b><i>не</i><b> выпоkнено 
     * ни одно из условий:
     * 
     * <ol>
     *    <li>
     *       Таблица, которой принадлежит ряд находится в состоянии LOADED;
     *    </li>
     *    <li>
     *       Текущим состоянием ряда является INSERTING;
     *    </li>
     *    <li>
     *       Включена буфферизация изменений ряда ( beginEditMode == true )
     *    </li>
     * </ol>
     * 
     * 
     * <p> Если <i>буфферизация измененний</i> <b>выключена</b>, то каждое
     * выполнение метода <code>setValue</code> переводит ряд из текущего состояния 
     * в новое по правилам, приведенным ниже:
     * <ul>
     *      <li>DETACHED - <i>не изменяется</i></li>
     *      <li>LOADED - <i>UPDATED</i></li>
     *      <li>INSERTING - <i>не изменяется</i></li>
     *      <li>MANMADE  - <i>не изменяется</i></li>
     *      <li>UPDATING  - <i>UPDATED</i></li>
     *      <li>UPDATED   - <i>не изменяется</i></li>
     * </ul>
     * 
     * <p> Если <i>буфферизация измененний</i> <b>включена</b>, то каждое
     * выполнение метода <code>setValue</code> переводит ряд из текущего состояни 
     * в новое по правилам, приведенным ниже: 
     * <ul>
     *      <li>LOADED - <i>UPDATING</i></li>
     *      <li>INSERTING - <i>не изменяется</i></li>
     *      <li>MANMADE  - <i>UPDATING</i></li>
     *      <li>UPDATING  - <i>не изменяется</i></li>
     *      <li>UPDATED   - <i>UPDATING</i></li>
     * </ul>
     * @param columnIndex колонка с измененным значением
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

        // Изменяется ряд => сразу переводим в конечное
        // состояние UPDATED
        if (!state.isManMade()) {
            state.setEditingState(UPDATED);
        }

        notifyTableOf(SETVALUE, columnIndex);

    }

    /**
     * Устанавливает ячейку ряда, специфицированную именем колонки в заданное
     * значение.
     * Метод делегирует исполнение перегруженному методу {@link #setValue(Object,int)}.
     * @param value новое значение ячейки ряда
     * @param columnName имя колонки, для которой определяется ячейка ряда
     * @see #setValue(java.lang.Object, int) 
     */
    @Override
    public void setValue(Object value, String columnName) {
        int columnIndex = rowServices.getColumnIndex(columnName);

        setValue(value, columnIndex);
    }

    /**
     *  Завершает редактирование ряда.
     *
     * <p>Если ряд находится в состоянии DETACHED, то метод завершается.
     *
     * <p>Если ряд находится в состоянии LOADED, MANMADE или UPDATED ,
     * то <code>originalRow</code> и <code>updatingRow</code> устанавливаются в
     * <code>null</code>. <code>beginEditMode</code> устанавливается в
     * <code>false</code>.
     *
     * <p>Для ряда, находящегося в состоянии INSERTING или UPDATING выполняется
     * вызов {@link tdo.Table#validate(tdo.DataRow, boolean) } , который
     * возвращает булевое значение, индицирующее содержит ли ряд ошибки валидации.
     * Если возвращается <code>false</code>, то метод завершается,не проводя
     * никаких действий.
     * <p>Если ряд успешно прошел проверку, то для каждого состояния, которое было
     * на момент вызова этого метода:
     *    <ul>
     *      <li>INSERTING  - назначается состояние MANMADE. Значение свойства
     *                       {@link tdo.DefaultRowState#updatingRow}
     *                       устанавливается в <code>null</code>.
     *      </li>
     *
     *      <li>UPDATING   - Новое состояние ряда в этом случае зависит от значения
     *                       свойства <code>originalRow</code>. Если содержит
     *                       <code>null</code>, то это означает, что первичным
     *                       состоянием ряда было MANMADE. Поэтому новое состояние
     *                       становится MANMADE. В противном случае новому состоянию
     *                       присваивается значение UPDATED.
     *                       Значение свойства
     *                       {@link tdo.DefaultRowState#updatingRow}
     *                       устанавливается в <code>null</code>.
     *      </li>
     *   </ul>
     */
    @Override
    public void endEdit() {
        this.endEdit(false);
    }
    /**
     *  Завершает редактирование ряда, форсируя или нет валидацию ряда.
     * <p>Если параметр принимает булевое значение <code>true</code>, то первое,
     * что делает метод - это выполняет валидацию ряда, выбрасываю исключение
     * типа {@link tdo.impl.ValidateException }, если обнаружены ошибки, тем
     * самым прерывая выполнение метода. Если ошибки не обнаружены или значение
     * параметра равно <code>false</code> то:
     *
     * <p>Если ряд находится в состоянии DETACHED, то метод завершается.
     *
     * <p>Если ряд находится в состоянии LOADED, MANMADE или UPDATED ,
     * то <code>originalRow</code> и <code>updatingRow</code> устанавливаются в
     * <code>null</code>. <code>beginEditMode</code> устанавливается в
     * <code>false</code>.
     *
     * <p>Для ряда, находящегося в состоянии INSERTING или UPDATING и значение
     * параметра <code>forceValidate</code> равно <code>false</code> выполняется
     * вызов {@link tdo.Table#validate(tdo.DataRow, boolean) } , который
     * возвращает булевое значение, индицирующее содержит ли ряд ошибки валидации.
     * Если возвращается <code>false</code>, то метод завершается, не проводя
     * никаких действий.
     * <p>Если ряд успешно прошел проверку, то для каждого состояния, которое было
     * на момент вызова этого метода:
     *    <ul>
     *      <li>INSERTING  - назначается состояние MANMADE. Значение свойства
     *                       {@link tdo.DefaultRowState#updatingRow}
     *                       устанавливается в <code>null</code>.
     *      </li>
     *
     *      <li>UPDATING   - Новое состояние ряда в этом случае зависит от значения
     *                       свойства <code>originalRow</code>. Если содержит
     *                       <code>null</code>, то это означает, что первичным
     *                       состоянием ряда было MANMADE. Поэтому новое состояние
     *                       становится MANMADE. В противном случае новому состоянию
     *                       присваивается значение UPDATED.
     *                       Значение свойства
     *                       {@link tdo.DefaultRowState#updatingRow}
     *                       устанавливается в <code>null</code>.
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
     * Возвращает индекс ряда в таблице.
     * 
     * @return индекс ряда в таблице
     * @see tdo.Table#find(tdo.DataRow) 
     */
    @Override
    public int getIndex() {
        return rowServices.getRowIndex(this);
    }

    /**
     * Возвращает контект таблицы,  для которого создан ряд.
     * 
     * @return контекст
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
         * Хранит ссылку на оригинальный ряд. <p>
         */
        private DataRow originalRow;
        /**
         * Хранит ссылку на ряд, находящийся в состоянии MANMADE или UPDATED
         * и переводящийся в состояние UPDATING. <p>
         */
        private DataRow updatingRow;
        /**
         * Хранит значение LOADED, MANMADE или DETACHED.
         */
        private int originalState;
        /**
         * Хранит ссылку на текущий ряд. <p>
         */
        private DataRow row;
        /**
         * Используется для текстовых сообщений, которые могут быть созданы,
         * например при репликации с базой данных.
         */
        private String message;

        /**
         * Конструктор без параметров. <p>
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
         * Возвращает ссылку оригинальный ряд. <p>
         * @return <code>DataRow</code>
         */
        @Override
        public DataRow getOriginalRow() {
            return this.originalRow;
        }

        /**
         * Устанавливает ссылку на оригинальный ряд.  <p>
         * @param row
         */
        public void setOriginalRow(DataRow row) {
            this.originalRow = row;
        }

        /**
         * Возвращает ссылку на ряд  находящийся в состоянии MANMADE или UPDATED
         * и переводящийся в состояние UPDATING. <p>
         * @return <code>DataRow</code>
         */
        @Override
        public DataRow getUpdatingRow() {
            return this.updatingRow;
        }

        /**
         * Устанавливает ссылку на ряд  находящийся в состоянии MANMADE или UPDATED
         * и переводящийся в состояние UPDATING. <p>
         *
         * @param row
         */
        public void setUpdatingRow(DataRow row) {
            this.updatingRow = row;
        }

        /**
         * Возвращает ссылку текущий ряд. <p>
         * @return <code>DataRow</code>
         */
        @Override
        public DataRow getRow() {
            return this.row;
        }

        /**
         * Устанавливает ссылку на текущий ряд.  <p>
         * @param row
         */
        protected void setRow(DataRow row) {
            this.row = row;
        }

        /**
         * getter-метод свойства <code>messsage</code>.<p>
         * @return тип <code>String</code>
         * @see message
         * @see setMessage
         */
        @Override
        public String getMessage() {
            return this.message;
        }

        /**
         * setter-метод свойства <code>messsage</code>.<p>
         * @param message тип <code>String</code>
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
