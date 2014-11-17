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
 * 刷出(每间隔一段时间，Session会执行一些必需的SQL语句来把内存中的对象的状态同步到JDBC连接中)方式
 *
 * @see Session#setFlushMode(FlushMode)
 * @see Query#setFlushMode(FlushMode)
 * @see Criteria#setFlushMode(FlushMode)
 *
 * @author Gavin King
 */
public final class FlushMode implements Serializable {
	private static final Map INSTANCES = new HashMap();

	private final int level;
	private final String name;

	private FlushMode(int level, String name) {
		this.level = level;
		this.name = name;
	}

	public String toString() {
		return name;
	}

	/**
	 * 除非调用{@link Session#flush}，否则从不刷出，该模式对于只读的事务非常的高效
	 *
	 * @deprecated 用{@link #MANUAL}替代.
	 */
	public static final FlushMode NEVER = new FlushMode( 0, "NEVER" );

	/**
	 * 除非调用{@link Session#flush}，否则从不刷出，该模式对于只读的事务非常的高效
	 */
	public static final FlushMode MANUAL = new FlushMode( 0, "MANUAL" );

	/**
	 * 当调用{@link Transaction#commit}时刷出
	 * is called.
	 */
	public static final FlushMode COMMIT = new FlushMode(5, "COMMIT");

	/**
	 * 在某些为了确保查询最新数据时才执行刷出，这个是默认的刷出模式
	 */
	public static final FlushMode AUTO = new FlushMode(10, "AUTO");

	/**
	 * 每次在查询之前都进行刷出。这种在大多数情况下是没必要而且低效的
	 */
	public static final FlushMode ALWAYS = new FlushMode(20, "ALWAYS");
	
	/**
	 * 是否低于指定模式
	 * @param other
	 * @return
	 */
	public boolean lessThan(FlushMode other) {
		return this.level<other.level;
	}

	/**
	 * 静态块初始化
	 */
	static {
		INSTANCES.put( NEVER.name, NEVER );
		INSTANCES.put( MANUAL.name, MANUAL );
		INSTANCES.put( AUTO.name, AUTO );
		INSTANCES.put( ALWAYS.name, ALWAYS );
		INSTANCES.put( COMMIT.name, COMMIT );
	}

	/**
	 * 判断是否手工刷出
	 * @param mode
	 * @return
	 */
	public static boolean isManualFlushMode(FlushMode mode) {
		return MANUAL.level == mode.level;
	}

	/**
	 * 获取刷出模式
	 * @return
	 */
	private Object readResolve() {
		return INSTANCES.get( name );
	}

	/**
	 * 获取刷出模式
	 * @return
	 */
	public static FlushMode parse(String name) {
		return ( FlushMode ) INSTANCES.get( name );
	}
}
