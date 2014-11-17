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
package org.hibernate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 实例代表一个关系数据库表中的行锁定模式。
 * 它的目的是让Hibernate自动获得正确的锁定级别，而用户不用担心在这上面会花太多的时间。
 * 一些“先进”的用户，不妨明确地指定锁水平。
 *
 * @see Session#lock(Object,LockMode)
 * @author Gavin King
 */
public final class LockMode implements Serializable {
	private final int level;
	private final String name;
	private static final Map INSTANCES = new HashMap();

	private LockMode(int level, String name) {
		this.level=level;
		this.name=name;
	}
	public String toString() {
		return name;
	}
	/**
	 * 检查当前锁定模式的限制性是否大于给定的锁定模式
	 *
	 * @param mode LockMode to check
	 * @return true if this lock mode is more restrictive than given lock mode
	 */
	public boolean greaterThan(LockMode mode) {
		return level > mode.level;
	}
	/**
	 * 检查当前锁定模式的限制性是否小于给定的锁定模式
	 *
	 * @param mode LockMode to check
	 * @return true if this lock mode is less restrictive than given lock mode
	 */
	public boolean lessThan(LockMode mode) {
		return level < mode.level;
	}
	/**
	 * 无需锁定。有缓存用缓存，没缓存则从数据库读
	 * <br/>
	 * 这是默认锁定模式。
	 */
	public static final LockMode NONE = new LockMode(0, "NONE");
	/**
	 * 共享锁定。直接从数据库读，不使用缓存数据
	 */
	public static final LockMode READ = new LockMode(5, "READ");
	/**
	 * 升级锁。相当于SQL语句SELECT...FOR UPDATE，被SELECT的数据都被数据库锁住了，不能被其他事务修改
	 * 
	 * @deprecated instead use PESSIMISTIC_WRITE
	 */
	public static final LockMode UPGRADE = new LockMode(10, "UPGRADE");
	/**
	 * 企图获取升级锁，使用一个Oracle-style<tt>SELECT...FOR UPDATE NOWAIT</tt>。
	 * 该锁定模式语义：一旦获得升级锁便与<tt>UPGRADE</tt>相同
	 */
	public static final LockMode UPGRADE_NOWAIT = new LockMode(10, "UPGRADE_NOWAIT");
	/**
	 * 当Hibernate更新或者插入一行记录的时候，锁定级别自动设置为LockMode.WRITE。    
	 * This lock mode is for internal use only and is
	 * not a valid mode for <tt>load()</tt> or <tt>lock()</tt> (both
	 * of which throw exceptions if WRITE is specified).
	 */
	public static final LockMode WRITE = new LockMode(10, "WRITE");

	/**
	 * Similiar to {@link #UPGRADE} except that, for versioned entities,
	 * it results in a forced version increment.
	 * @deprecated instead use PESSIMISTIC_FORCE_INCREMENT
	 */
	public static final LockMode FORCE = new LockMode( 15, "FORCE" );

	/**
	 *  start of javax.persistence.LockModeType equivalent modes
	 */

	/**
	 * Optimisticly assume that transaction will not experience contention for
	 * entities.  The entity version will be verified near the transaction end.  
	 */
	public static final LockMode OPTIMISTIC = new LockMode( 3, "OPTIMISTIC");

	/**
	 * Optimisticly assume that transaction will not experience contention for entities.
	 * The entity version will be verified and incremented near the transaction end. 
	 */
	public static final LockMode OPTIMISTIC_FORCE_INCREMENT = new LockMode( 4, "OPTIMISTIC_FORCE_INCREMENT");

	/**
	 * Implemented as PESSIMISTIC_WRITE.
	 * TODO:  introduce separate support for PESSIMISTIC_READ
	 */
	public static final LockMode PESSIMISTIC_READ = new LockMode( 12, "PESSIMISTIC_READ");

	/**
	 * Transaction will obtain a database lock immediately.
	 * TODO:  add PESSIMISTIC_WRITE_NOWAIT
	 */
	public static final LockMode PESSIMISTIC_WRITE = new LockMode( 13, "PESSIMISTIC_WRITE");

	/**
	 * Transaction will immediately increment the entity version.
	 */
	public static final LockMode PESSIMISTIC_FORCE_INCREMENT = new LockMode( 17, "PESSIMISTIC_FORCE_INCREMENT");

	/**
	 *  end of javax.persistence.LockModeType modes
	 */
	
	static {
		INSTANCES.put( NONE.name, NONE );
		INSTANCES.put( READ.name, READ );
		INSTANCES.put( UPGRADE.name, UPGRADE );
		INSTANCES.put( UPGRADE_NOWAIT.name, UPGRADE_NOWAIT );
		INSTANCES.put( WRITE.name, WRITE );
		INSTANCES.put( FORCE.name, FORCE );
		INSTANCES.put( OPTIMISTIC.name, OPTIMISTIC);
		INSTANCES.put( OPTIMISTIC_FORCE_INCREMENT.name, OPTIMISTIC_FORCE_INCREMENT);
		INSTANCES.put( PESSIMISTIC_READ. name, PESSIMISTIC_READ);
		INSTANCES.put( PESSIMISTIC_WRITE.name, PESSIMISTIC_WRITE);
		INSTANCES.put( PESSIMISTIC_FORCE_INCREMENT.name, PESSIMISTIC_FORCE_INCREMENT);
	}

	private Object readResolve() {
		return parse( name );
	}

	public static LockMode parse(String name) {
		return ( LockMode ) INSTANCES.get(name);
	}
}
