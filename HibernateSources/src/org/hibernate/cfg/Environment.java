/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 *
 */
package org.hibernate.cfg;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.hibernate.HibernateException;
import org.hibernate.Version;
import org.hibernate.bytecode.BytecodeProvider;
import org.hibernate.util.ConfigHelper;
import org.hibernate.util.PropertiesHelper;


/**
 * 提供一个访问配置信息的<tt>Properties</tt>对象
 * <br><br>
 * Hibernate has two property scopes:
 * <ul>
 * <li><b>Factory-level</b> properties may be passed to the <tt>SessionFactory</tt> when it
 * instantiated. Each instance might have different property values. If no
 * properties are specified, the factory calls <tt>Environment.getProperties()</tt>.
 * <li><b>System-level</b> properties are shared by all factory instances and are always
 * determined by the <tt>Environment</tt> properties.
 * </ul>
 * The only system-level properties are
 * <ul>
 * <li><tt>hibernate.jdbc.use_streams_for_binary</tt>
 * <li><tt>hibernate.cglib.use_reflection_optimizer</tt>
 * </ul>
 * <tt>Environment</tt> properties are populated by calling <tt>System.getProperties()</tt>
 * and then from a resource named <tt>/hibernate.properties</tt> if it exists. System
 * properties override properties specified in <tt>hibernate.properties</tt>.<br>
 * <br>
 * The <tt>SessionFactory</tt> is controlled by the following properties.
 * Properties may be either be <tt>System</tt> properties, properties
 * defined in a resource named <tt>/hibernate.properties</tt> or an instance of
 * <tt>java.util.Properties</tt> passed to
 * <tt>Configuration.buildSessionFactory()</tt><br>
 * <br>
 * <table>
 * <tr><td><b>property</b></td><td><b>meaning</b></td></tr>
 * <tr>
 *   <td><tt>hibernate.dialect</tt></td>
 *   <td><tt>org.hibernate.dialect.Dialect</tt>的子类名称</td>
 * </tr>
 * <tr>
 *   <td><tt>hibernate.cache.provider_class</tt></td>
 *   <td><tt>自定义的CacheProvider实现类的名称,(如果没有指定使用EHCache)</td>
 * </tr>
 * <tr>
 *   <td><tt>hibernate.connection.provider_class</tt></td>
 *   <td><tt>org.hibernate.connection.ConnectionProvider</tt>的子类名称(如果没有指定使用hueristics)</td>
 * </tr>
 * <tr><td><tt>hibernate.connection.username</tt></td><td>数据库用户名</td></tr>
 * <tr><td><tt>hibernate.connection.password</tt></td><td>数据库密码</td></tr>
 * <tr>
 *   <td><tt>hibernate.connection.url</tt></td>
 *   <td>JDBC URL (当使用<tt>java.sql.DriverManager</tt>时)</td>
 * </tr>
 * <tr>
 *   <td><tt>hibernate.connection.driver_class</tt></td>
 *   <td>JDBC驱动类名</td>
 * </tr>
 * <tr>
 *   <td><tt>hibernate.connection.isolation</tt></td>
 *   <td>JDBC事务隔离等级(仅当使用<tt>java.sql.DriverManager</tt>时)
 *   </td>
 * </tr>
 *   <td><tt>hibernate.connection.pool_size</tt></td>
 *   <td>连接池的最大连接数目(仅当使用<tt>java.sql.DriverManager</tt>)
 *   </td>
 * </tr>
 * <tr>
 *   <td><tt>hibernate.connection.datasource</tt></td>
 *   <td>数据源JNDI名称 (当使用<tt>javax.sql.Datasource</tt>时)</td>
 * </tr>
 * <tr>
 *   <td><tt>hibernate.jndi.url</tt></td><td>JNDI <tt>InitialContext</tt> URL</td>
 * </tr>
 * <tr>
 *   <td><tt>hibernate.jndi.class</tt></td><td>JNDI <tt>InitialContext</tt> 类名称</td>
 * </tr>
 * <tr>
 *   <td><tt>hibernate.max_fetch_depth</tt></td>
 *   <td>maximum depth of outer join fetching</td>
 * </tr>
 * <tr>
 *   <td><tt>hibernate.jdbc.batch_size</tt></td>
 *   <td>允许Hibernate使用JDBC2的批量更新,指定JDBC一次最多提交多少SQL语句</td>
 * </tr>
 * <tr>
 *   <td><tt>hibernate.jdbc.fetch_size</tt></td>
 *   <td>非零值，指定JDBC抓取数量的大小 (调用Statement.setFetchSize()). </td>
 * </tr>
 * <tr>
 *   <td><tt>hibernate.jdbc.use_scrollable_resultset</tt></td>
 *   <td>允许Hibernate使用JDBC2的可滚动结果集. (只有在使用用户提供的JDBC连接时，这个选项才是必要的, 否则Hibernate会使用连接的元数据. )</td>
 * </tr>
 * <tr>
 *   <td><tt>hibernate.jdbc.use_getGeneratedKeys</tt></td>
 *   <td>启用JDBC3在insert之后使用PreparedStatement.getGeneratedKeys()方法获取本地自动生成的keys. 要求JDBC3+驱动且JRE1.4+</td>
 * </tr>
 * <tr>
 *   <td><tt>hibernate.hbm2ddl.auto</tt></td>
 *   <td>在SessionFactory创建时，自动检查数据库结构，或者将数据库schema的DDL导出到数据库. 
 *   		使用 create-drop时,在显式关闭SessionFactory时，将drop掉数据库schema. 
 * 			取值 validate | update | create | create-drop 
 *	 </td>
 * </tr>
 * <tr>
 *   <td><tt>hibernate.default_schema</tt></td>
 *   <td>在生成的SQL中, 将给定的schema/tablespace附加于非全限定名的表名上. (可选的)</td>
 * </tr>
 * <tr>
 *   <td><tt>hibernate.default_catalog</tt></td>
 *   <td>在生成的SQL中, 将给定的catalog附加于非全限定名的表名上. (可选的)</td>
 * </tr>
 * <tr>
 *   <td><tt>hibernate.session_factory_name</tt></td>
 *   <td>如果设置，SessionFactory创建后，将自动使用这个名字绑定到JNDI中, 这个名称也常常被使用来支持跨JVM <tt>
 *   Session</tt> (de)serialization.</td>
 * </tr>
 * <tr>
 *   <td><tt>hibernate.transaction.manager_lookup_class</tt></td>
 *   <td><tt>TransactionManagerLookup</tt>接口的实现类,当使用JVM级缓存，或在JTA环境中使用hilo生成器的时候需要该类. </td>
 * </tr>
 * <tr>
 *   <td><tt>hibernate.transaction.factory_class</tt></td>
 *   <td>一个TransactionFactory的类名, 用于Hibernate Transaction API (默认为JDBCTransactionFactory). </td>
 * </tr>
 * <tr>
 *   <td><tt>hibernate.query.substitutions</tt></td><td>将Hibernate查询中的符号映射到SQL查询中的符号 (符号可能是函数名或常量名字).</td>
 * </tr>
 * </table>
 *
 * @see org.hibernate.SessionFactory
 * @author Gavin King
 */
public final class Environment {
	/**
	 * <tt>ConnectionProvider</tt> implementor to use when obtaining connections
	 */
	public static final String CONNECTION_PROVIDER ="hibernate.connection.provider_class";
	/**
	 * JDBC driver class
	 */
	public static final String DRIVER ="hibernate.connection.driver_class";
	/**
	 * JDBC transaction isolation level
	 */
	public static final String ISOLATION ="hibernate.connection.isolation";
	/**
	 * JDBC URL
	 */
	public static final String URL ="hibernate.connection.url";
	/**
	 * JDBC user
	 */
	public static final String USER ="hibernate.connection.username";
	/**
	 * JDBC password
	 */
	public static final String PASS ="hibernate.connection.password";
	/**
	 * JDBC autocommit mode
	 */
	public static final String AUTOCOMMIT ="hibernate.connection.autocommit";
	/**
	 * Maximum number of inactive connections for Hibernate's connection pool
	 */
	public static final String POOL_SIZE ="hibernate.connection.pool_size";
	/**
	 * <tt>java.sql.Datasource</tt> JNDI name
	 */
	public static final String DATASOURCE ="hibernate.connection.datasource";
	/**
	 * prefix for arbitrary JDBC connection properties
	 */
	public static final String CONNECTION_PREFIX = "hibernate.connection";

	/**
	 * JNDI initial context class, <tt>Context.INITIAL_CONTEXT_FACTORY</tt>
	 */
	public static final String JNDI_CLASS ="hibernate.jndi.class";
	/**
	 * JNDI provider URL, <tt>Context.PROVIDER_URL</tt>
	 */
	public static final String JNDI_URL ="hibernate.jndi.url";
	/**
	 * prefix for arbitrary JNDI <tt>InitialContext</tt> properties
	 */
	public static final String JNDI_PREFIX = "hibernate.jndi";
	/**
	 * JNDI name to bind to <tt>SessionFactory</tt>
	 */
	public static final String SESSION_FACTORY_NAME = "hibernate.session_factory_name";

	/**
	 * Hibernate SQL {@link org.hibernate.dialect.Dialect} class
	 */
	public static final String DIALECT ="hibernate.dialect";

	/**
	 * {@link org.hibernate.dialect.resolver.DialectResolver} classes to register with the
	 * {@link org.hibernate.dialect.resolver.DialectFactory}
	 */
	public static final String DIALECT_RESOLVERS = "hibernate.dialect_resolvers";

	/**
	 * A default database schema (owner) name to use for unqualified tablenames
	 */
	public static final String DEFAULT_SCHEMA = "hibernate.default_schema";
	/**
	 * A default database catalog name to use for unqualified tablenames
	 */
	public static final String DEFAULT_CATALOG = "hibernate.default_catalog";

	/**
	 * Enable logging of generated SQL to the console
	 */
	public static final String SHOW_SQL ="hibernate.show_sql";
	/**
	 * Enable formatting of SQL logged to the console
	 */
	public static final String FORMAT_SQL ="hibernate.format_sql";
	/**
	 * Add comments to the generated SQL
	 */
	public static final String USE_SQL_COMMENTS ="hibernate.use_sql_comments";
	/**
	 * Maximum depth of outer join fetching
	 */
	public static final String MAX_FETCH_DEPTH = "hibernate.max_fetch_depth";
	/**
	 * The default batch size for batch fetching
	 */
	public static final String DEFAULT_BATCH_FETCH_SIZE = "hibernate.default_batch_fetch_size";
	/**
	 * Use <tt>java.io</tt> streams to read / write binary data from / to JDBC
	 */
	public static final String USE_STREAMS_FOR_BINARY = "hibernate.jdbc.use_streams_for_binary";
	/**
	 * Use JDBC scrollable <tt>ResultSet</tt>s. This property is only necessary when there is
	 * no <tt>ConnectionProvider</tt>, ie. the user is supplying JDBC connections.
	 */
	public static final String USE_SCROLLABLE_RESULTSET = "hibernate.jdbc.use_scrollable_resultset";
	/**
	 * Tells the JDBC driver to attempt to retrieve row Id with the JDBC 3.0 PreparedStatement.getGeneratedKeys()
	 * method. In general, performance will be better if this property is set to true and the underlying
	 * JDBC driver supports getGeneratedKeys().
	 */
	public static final String USE_GET_GENERATED_KEYS = "hibernate.jdbc.use_get_generated_keys";
	/**
	 * Gives the JDBC driver a hint as to the number of rows that should be fetched from the database
	 * when more rows are needed. If <tt>0</tt>, JDBC driver default settings will be used.
	 */
	public static final String STATEMENT_FETCH_SIZE = "hibernate.jdbc.fetch_size";
	/**
	 * Maximum JDBC batch size. A nonzero value enables batch updates.
	 */
	public static final String STATEMENT_BATCH_SIZE = "hibernate.jdbc.batch_size";
	/**
	 * Select a custom batcher.
	 */
	public static final String BATCH_STRATEGY = "hibernate.jdbc.factory_class";
	/**
	 * Should versioned data be included in batching?
	 */
	public static final String BATCH_VERSIONED_DATA = "hibernate.jdbc.batch_versioned_data";
	/**
	 * An XSLT resource used to generate "custom" XML
	 */
	public static final String OUTPUT_STYLESHEET ="hibernate.xml.output_stylesheet";

	/**
	 * Maximum size of C3P0 connection pool
	 */
	public static final String C3P0_MAX_SIZE = "hibernate.c3p0.max_size";
	/**
	 * Minimum size of C3P0 connection pool
	 */
	public static final String C3P0_MIN_SIZE = "hibernate.c3p0.min_size";

	/**
	 * Maximum idle time for C3P0 connection pool
	 */
	public static final String C3P0_TIMEOUT = "hibernate.c3p0.timeout";
	/**
	 * Maximum size of C3P0 statement cache
	 */
	public static final String C3P0_MAX_STATEMENTS = "hibernate.c3p0.max_statements";
	/**
	 * Number of connections acquired when pool is exhausted
	 */
	public static final String C3P0_ACQUIRE_INCREMENT = "hibernate.c3p0.acquire_increment";
	/**
	 * Idle time before a C3P0 pooled connection is validated
	 */
	public static final String C3P0_IDLE_TEST_PERIOD = "hibernate.c3p0.idle_test_period";

	/**
	 * Proxool/Hibernate property prefix
	 */
	public static final String PROXOOL_PREFIX = "hibernate.proxool";
	/**
	 * Proxool property to configure the Proxool Provider using an XML (<tt>/path/to/file.xml</tt>)
	 */
	public static final String PROXOOL_XML = "hibernate.proxool.xml";
	/**
	 * Proxool property to configure the Proxool Provider  using a properties file (<tt>/path/to/proxool.properties</tt>)
	 */
	public static final String PROXOOL_PROPERTIES = "hibernate.proxool.properties";
	/**
	 * Proxool property to configure the Proxool Provider from an already existing pool (<tt>true</tt> / <tt>false</tt>)
	 */
	public static final String PROXOOL_EXISTING_POOL = "hibernate.proxool.existing_pool";
	/**
	 * Proxool property with the Proxool pool alias to use
	 * (Required for <tt>PROXOOL_EXISTING_POOL</tt>, <tt>PROXOOL_PROPERTIES</tt>, or
	 * <tt>PROXOOL_XML</tt>)
	 */
	public static final String PROXOOL_POOL_ALIAS = "hibernate.proxool.pool_alias";

	/**
	 * 如果开启, session在事务完成后将被自动关闭。 目前更好的方法是使用自动session上下文管理。
	 */
	public static final String AUTO_CLOSE_SESSION = "hibernate.transaction.auto_close_session";
	/**
	 * 如果开启, session在事务完成后将被自动清洗(flush)。 目前更好的方法是使用自动session上下文管理。
	 */
	public static final String FLUSH_BEFORE_COMPLETION = "hibernate.transaction.flush_before_completion";
	/**
	 * 指定Hibernate释放JDBC连接的方式.
	 */
	public static final String RELEASE_CONNECTIONS = "hibernate.connection.release_mode";
	/**
	 * 为"当前" Session指定一个(自定义的)策略。
	 */
	public static final String CURRENT_SESSION_CONTEXT_CLASS = "hibernate.current_session_context_class";
	/**
	 * 一个<tt>TransactionFactory</tt>的类名, 用于Hibernate <tt>Transaction</tt> API (默认为JDBCTransactionFactory). 
	 */
	public static final String TRANSACTION_STRATEGY = "hibernate.transaction.factory_class";
	/**
	 * 一个<tt>TransactionManagerLookup</tt>的类名 - 当使用JVM级缓存，或在JTA环境中使用hilo生成器的时候需要该类. 用于获得<tt>TransactionManager</tt>
	 */
	public static final String TRANSACTION_MANAGER_STRATEGY = "hibernate.transaction.manager_lookup_class";
	/**
	 * 一个JNDI名字，被JTATransactionFactory用来从应用服务器获取JTA <tt>UserTransaction</tt>
	 */
	public static final String USER_TRANSACTION = "jta.UserTransaction";

	/**
	 * CacheProvider的实现类
	 */
	public static final String CACHE_PROVIDER = "hibernate.cache.provider_class";

	/**
	 * The {@link org.hibernate.cache.RegionFactory} implementation class
	 */
	public static final String CACHE_REGION_FACTORY = "hibernate.cache.region.factory_class";

	/**
	 * <tt>CacheProvider</tt>实现类
	 */
	public static final String CACHE_PROVIDER_CONFIG = "hibernate.cache.provider_configuration_file_resource_path";
	/**
	 * The <tt>CacheProvider</tt> JNDI namespace, if pre-bound to JNDI.
	 */
	public static final String CACHE_NAMESPACE = "hibernate.cache.jndi";
	/**
	 * 允许查询缓存, 个别查询仍然需要被设置为可缓存的. (默认关闭的)
	 */
	public static final String USE_QUERY_CACHE = "hibernate.cache.use_query_cache";
	/**
	 * 自定义实现QueryCacheFactory接口的类名, 默认为内建的StandardQueryCacheFactory. 
	 */
	public static final String QUERY_CACHE_FACTORY = "hibernate.cache.query_cache_factory";
	/**
	 * 能用来完全禁止使用二级缓存. 对那些在类的映射定义中指定<cache>的类，会默认开启二级缓存. 
	 */
	public static final String USE_SECOND_LEVEL_CACHE = "hibernate.cache.use_second_level_cache";
	/**
	 * 以频繁的读操作为代价, 优化二级缓存来最小化写操作. 在Hibernate3中，这个设置对集群缓存非常有用, 对集群缓存的实现而言，默认是开启的. 
	 */
	public static final String USE_MINIMAL_PUTS = "hibernate.cache.use_minimal_puts";
	/**
	 * 二级缓存区域名的前缀. 
	 */
	public static final String CACHE_REGION_PREFIX = "hibernate.cache.region_prefix";
	/**
	 * 强制Hibernate以更人性化的格式将数据存入二级缓存. 
	 */
	public static final String USE_STRUCTURED_CACHE = "hibernate.cache.use_structured_entries";

	/**
	 * 启用统计数据的采集
	 */
	public static final String GENERATE_STATISTICS = "hibernate.generate_statistics";

	public static final String USE_IDENTIFIER_ROLLBACK = "hibernate.use_identifier_rollback";

	/**
	 * 使用字节码库优化属性的访问
	 * 开启CGLIB来替代运行时反射机制(系统级属性). 反射机制有时在除错时比较有用. 注意即使关闭这个优化, Hibernate还是需要CGLIB. 你不能在hibernate.cfg.xml中设置此属性. 
	 */
	public static final String USE_REFLECTION_OPTIMIZER = "hibernate.bytecode.use_reflection_optimizer";

	/**
	 * HQL查询解析器工厂的类名
	 */
	public static final String QUERY_TRANSLATOR = "hibernate.query.factory_class";

	/**
	 * 将Hibernate查询中的符号映射到SQL查询中的符号 (符号可能是函数名或常量名字). 
	 */
	public static final String QUERY_SUBSTITUTIONS = "hibernate.query.substitutions";

	/**
	 * Should named queries be checked during startup (the default is enabled).
	 * <p/>
	 * Mainly intended for test environments.
	 */
	public static final String QUERY_STARTUP_CHECKING = "hibernate.query.startup_check";

	/**
	 * Auto export/update schema using hbm2ddl tool. Valid values are <tt>update</tt>,
	 * <tt>create</tt>, <tt>create-drop</tt> and <tt>validate</tt>.
	 */
	public static final String HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";

	/**
	 * The {@link org.hibernate.exception.SQLExceptionConverter} to use for converting SQLExceptions
	 * to Hibernate's JDBCException hierarchy.  The default is to use the configured
	 * {@link org.hibernate.dialect.Dialect}'s preferred SQLExceptionConverter.
	 */
	public static final String SQL_EXCEPTION_CONVERTER = "hibernate.jdbc.sql_exception_converter";

	/**
	 * Enable wrapping of JDBC result sets in order to speed up column name lookups for
	 * broken JDBC drivers
	 */
	public static final String WRAP_RESULT_SETS = "hibernate.jdbc.wrap_result_sets";

	/**
	 * Enable ordering of update statements by primary key value
	 */
	public static final String ORDER_UPDATES = "hibernate.order_updates";

	/**
	 * Enable ordering of insert statements for the purpose of more efficient JDBC batching.
	 */
	public static final String ORDER_INSERTS = "hibernate.order_inserts";

	/**
	 * The EntityMode in which set the Session opened from the SessionFactory.
	 */
    public static final String DEFAULT_ENTITY_MODE = "hibernate.default_entity_mode";

    /**
     * The jacc context id of the deployment
     */
    public static final String JACC_CONTEXTID = "hibernate.jacc_context_id";

	/**
	 * Should all database identifiers be quoted.
	 */
	public static final String GLOBALLY_QUOTED_IDENTIFIERS = "hibernate.globally_quoted_identifiers";

	/**
	 * Enable nullability checking.
	 * Raises an exception if a property marked as not-null is null.
	 * Default to false if Bean Validation is present in the classpath and Hibernate Annotations is used,
	 * true otherwise.
	 */
	public static final String CHECK_NULLABILITY = "hibernate.check_nullability";


	public static final String BYTECODE_PROVIDER = "hibernate.bytecode.provider";

	public static final String JPAQL_STRICT_COMPLIANCE= "hibernate.query.jpaql_strict_compliance";

	/**
	 * When using pooled {@link org.hibernate.id.enhanced.Optimizer optimizers}, prefer interpreting the
	 * database value as the lower (lo) boundary.  The default is to interpret it as the high boundary.
	 */
	public static final String PREFER_POOLED_VALUES_LO = "hibernate.id.optimizer.pooled.prefer_lo";

	private static final BytecodeProvider BYTECODE_PROVIDER_INSTANCE;
	private static final boolean ENABLE_BINARY_STREAMS;
	private static final boolean ENABLE_REFLECTION_OPTIMIZER;
	private static final boolean JVM_SUPPORTS_LINKED_HASH_COLLECTIONS;
	private static final boolean JVM_HAS_TIMESTAMP_BUG;
	private static final boolean JVM_HAS_JDK14_TIMESTAMP;
	private static final boolean JVM_SUPPORTS_GET_GENERATED_KEYS;

	private static final Properties GLOBAL_PROPERTIES;//全局属性集合
	private static final HashMap ISOLATION_LEVELS = new HashMap();//事务隔离等级
	private static final Map OBSOLETE_PROPERTIES = new HashMap();//过时属性集合
	private static final Map RENAMED_PROPERTIES = new HashMap();//重命名属性集合

	private static final Logger log = LoggerFactory.getLogger(Environment.class);

	/**
	 * 当有任何过时的或重命名的属性名称被使用时向用户发出警告（旧版本的Hibernate使用的属性）
	 *
	 * @param props 指定属性.
	 */
	public static void verifyProperties(Properties props) {
		Iterator iter = props.keySet().iterator();
		Map propertiesToAdd = new HashMap();
		while ( iter.hasNext() ) {
			final Object propertyName = iter.next();//属性名
			Object newPropertyName = OBSOLETE_PROPERTIES.get( propertyName );//获取新的属性名
			if ( newPropertyName != null ) {//已过时
				log.warn( "Usage of obsolete property: " + propertyName + " no longer supported, use: " + newPropertyName );
			}
			newPropertyName = RENAMED_PROPERTIES.get( propertyName );//被重命名属性
			if ( newPropertyName != null ) {//已被重命名
				log.warn( "Property [" + propertyName + "] has been renamed to [" + newPropertyName + "]; update your properties appropriately" );
				if ( ! props.containsKey( newPropertyName ) ) {
					propertiesToAdd.put( newPropertyName, props.get( propertyName ) );
				}
			}
		}
		props.putAll(propertiesToAdd);
	}

	static {

		log.info( "Hibernate " + Version.getVersionString() );

		RENAMED_PROPERTIES.put( "hibernate.cglib.use_reflection_optimizer", USE_REFLECTION_OPTIMIZER );

		ISOLATION_LEVELS.put( new Integer(Connection.TRANSACTION_NONE), "NONE" );
		ISOLATION_LEVELS.put( new Integer(Connection.TRANSACTION_READ_UNCOMMITTED), "READ_UNCOMMITTED" );
		ISOLATION_LEVELS.put( new Integer(Connection.TRANSACTION_READ_COMMITTED), "READ_COMMITTED" );
		ISOLATION_LEVELS.put( new Integer(Connection.TRANSACTION_REPEATABLE_READ), "REPEATABLE_READ" );
		ISOLATION_LEVELS.put( new Integer(Connection.TRANSACTION_SERIALIZABLE), "SERIALIZABLE" );

		GLOBAL_PROPERTIES = new Properties();
		//Set USE_REFLECTION_OPTIMIZER to false to fix HHH-227
		GLOBAL_PROPERTIES.setProperty( USE_REFLECTION_OPTIMIZER, Boolean.FALSE.toString() );

		try {
			InputStream stream = ConfigHelper.getResourceAsStream("/hibernate.properties");
			try {
				GLOBAL_PROPERTIES.load(stream);
				log.info( "loaded properties from resource hibernate.properties: " + PropertiesHelper.maskOut(GLOBAL_PROPERTIES, PASS) );
			}
			catch (Exception e) {
				log.error("problem loading properties from hibernate.properties");
			}
			finally {
				try{
					stream.close();
				}
				catch (IOException ioe){
					log.error("could not close stream on hibernate.properties", ioe);
				}
			}
		}
		catch (HibernateException he) {
			log.info("hibernate.properties not found");
		}

		try {
			GLOBAL_PROPERTIES.putAll( System.getProperties() );
		}
		catch (SecurityException se) {
			log.warn("could not copy system properties, system properties will be ignored");
		}

		verifyProperties(GLOBAL_PROPERTIES);

		ENABLE_BINARY_STREAMS = PropertiesHelper.getBoolean(USE_STREAMS_FOR_BINARY, GLOBAL_PROPERTIES);
		ENABLE_REFLECTION_OPTIMIZER = PropertiesHelper.getBoolean(USE_REFLECTION_OPTIMIZER, GLOBAL_PROPERTIES);

		if (ENABLE_BINARY_STREAMS) {
			log.info("using java.io streams to persist binary types");
		}
		if (ENABLE_REFLECTION_OPTIMIZER) {
			log.info("using bytecode reflection optimizer");
		}
		BYTECODE_PROVIDER_INSTANCE = buildBytecodeProvider( GLOBAL_PROPERTIES );

		boolean getGeneratedKeysSupport;
		try {
			Statement.class.getMethod("getGeneratedKeys", null);
			getGeneratedKeysSupport = true;
		}
		catch (NoSuchMethodException nsme) {
			getGeneratedKeysSupport = false;
		}
		JVM_SUPPORTS_GET_GENERATED_KEYS = getGeneratedKeysSupport;
		if (!JVM_SUPPORTS_GET_GENERATED_KEYS) {
			log.info("JVM does not support Statement.getGeneratedKeys()");
		}

		boolean linkedHashSupport;
		try {
			Class.forName("java.util.LinkedHashSet");
			linkedHashSupport = true;
		}
		catch (ClassNotFoundException cnfe) {
			linkedHashSupport = false;
		}
		JVM_SUPPORTS_LINKED_HASH_COLLECTIONS = linkedHashSupport;
		if (!JVM_SUPPORTS_LINKED_HASH_COLLECTIONS) {
			log.info("JVM does not support LinkedHasMap, LinkedHashSet - ordered maps and sets disabled");
		}

		long x = 123456789;
		JVM_HAS_TIMESTAMP_BUG = new Timestamp(x).getTime() != x;
		if (JVM_HAS_TIMESTAMP_BUG) {
			log.info("using workaround for JVM bug in java.sql.Timestamp");
		}

		Timestamp t = new Timestamp(0);
		t.setNanos(5 * 1000000);
		JVM_HAS_JDK14_TIMESTAMP = t.getTime() == 5;
		if (JVM_HAS_JDK14_TIMESTAMP) {
			log.info("using JDK 1.4 java.sql.Timestamp handling");
		}
		else {
			log.info("using pre JDK 1.4 java.sql.Timestamp handling");
		}
	}

	public static BytecodeProvider getBytecodeProvider() {
		return BYTECODE_PROVIDER_INSTANCE;
	}

	/**
	 * Does this JVM's implementation of {@link java.sql.Timestamp} have a bug in which the following is true:<code>
	 * new java.sql.Timestamp( x ).getTime() != x
	 * </code>
	 * <p/>
	 * NOTE : IBM JDK 1.3.1 the only known JVM to exhibit this behavior.
	 *
	 * @return True if the JVM's {@link Timestamp} implementa
	 */
	public static boolean jvmHasTimestampBug() {
		return JVM_HAS_TIMESTAMP_BUG;
	}

	/**
	 * Does this JVM handle {@link java.sql.Timestamp} in the JDK 1.4 compliant way wrt to nano rolling>
	 *
	 * @return True if the JDK 1.4 (JDBC3) specification for {@link java.sql.Timestamp} nano rolling is adhered to.
	 *
	 * @deprecated Starting with 3.3 Hibernate requires JDK 1.4 or higher
	 */
	public static boolean jvmHasJDK14Timestamp() {
		return JVM_HAS_JDK14_TIMESTAMP;
	}

	/**
	 * Does this JVM support {@link java.util.LinkedHashSet} and {@link java.util.LinkedHashMap}?
	 * <p/>
	 * Note, this is true for JDK 1.4 and above; hence the deprecation.
	 *
	 * @return True if {@link java.util.LinkedHashSet} and {@link java.util.LinkedHashMap} are available.
	 *
	 * @deprecated Starting with 3.3 Hibernate requires JDK 1.4 or higher
	 * @see java.util.LinkedHashSet
	 * @see java.util.LinkedHashMap
	 */
	public static boolean jvmSupportsLinkedHashCollections() {
		return JVM_SUPPORTS_LINKED_HASH_COLLECTIONS;
	}

	/**
	 * Does this JDK/JVM define the JDBC {@link Statement} interface with a 'getGeneratedKeys' method?
	 * <p/>
	 * Note, this is true for JDK 1.4 and above; hence the deprecation.
	 *
	 * @return True if generated keys can be retrieved via Statement; false otherwise.
	 *
	 * @see Statement
	 * @deprecated Starting with 3.3 Hibernate requires JDK 1.4 or higher
	 */
	public static boolean jvmSupportsGetGeneratedKeys() {
		return JVM_SUPPORTS_GET_GENERATED_KEYS;
	}

	/**
	 * Should we use streams to bind binary types to JDBC IN parameters?
	 *
	 * @return True if streams should be used for binary data handling; false otherwise.
	 *
	 * @see #USE_STREAMS_FOR_BINARY
	 */
	public static boolean useStreamsForBinary() {
		return ENABLE_BINARY_STREAMS;
	}

	/**
	 * Should we use reflection optimization?
	 *
	 * @return True if reflection optimization should be used; false otherwise.
	 *
	 * @see #USE_REFLECTION_OPTIMIZER
	 * @see #getBytecodeProvider()
	 * @see BytecodeProvider#getReflectionOptimizer
	 */
	public static boolean useReflectionOptimizer() {
		return ENABLE_REFLECTION_OPTIMIZER;
	}

	/**
	 * Disallow instantiation
	 */
	private Environment() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Return <tt>System</tt> properties, extended by any properties specified
	 * in <tt>hibernate.properties</tt>.
	 * @return Properties
	 */
	public static Properties getProperties() {
		Properties copy = new Properties();
		copy.putAll(GLOBAL_PROPERTIES);
		return copy;
	}

	/**
	 * Get the name of a JDBC transaction isolation level
	 *
	 * @see java.sql.Connection
	 * @param isolation as defined by <tt>java.sql.Connection</tt>
	 * @return a human-readable name
	 */
	public static String isolationLevelToString(int isolation) {
		return (String) ISOLATION_LEVELS.get( new Integer(isolation) );
	}

	public static BytecodeProvider buildBytecodeProvider(Properties properties) {
		String provider = PropertiesHelper.getString( BYTECODE_PROVIDER, properties, "javassist" );
		log.info( "Bytecode provider name : " + provider );
		return buildBytecodeProvider( provider );
	}

	private static BytecodeProvider buildBytecodeProvider(String providerName) {
		if ( "javassist".equals( providerName ) ) {
			return new org.hibernate.bytecode.javassist.BytecodeProviderImpl();
		}
		else if ( "cglib".equals( providerName ) ) {
			return new org.hibernate.bytecode.cglib.BytecodeProviderImpl();
		}

		log.warn( "unrecognized bytecode provider [" + providerName + "], using javassist by default" );
		return new org.hibernate.bytecode.javassist.BytecodeProviderImpl();
	}

}
