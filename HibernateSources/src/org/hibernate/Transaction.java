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

import javax.transaction.Synchronization;

/**
 * 允许应用程序自定义工作单元, 是底层事务实现的抽象(eg. JTA, JDBC).<br>
 * <br>
 * 一个事务被关联到<tt>Session</tt> ，通常实例化是通过调用<tt>Session.beginTransaction()</tt>.
 * A single session might span multiple transactions since
 * the notion of a session (一个应用程序与数据库的会话) 
 * is of coarser granularity than the notion of
 * a transaction. However, it is intended that there be at most one
 * uncommitted <tt>Transaction</tt> associated with a particular
 * <tt>Session</tt> at any time.<br>
 * <br>
 * 实现者并不打算是线程安全的。
 *
 * @see Session#beginTransaction()
 * @see org.hibernate.transaction.TransactionFactory
 * @author Anton van Straaten
 */
public interface Transaction {
	
	/**
	 * 启动一个新事务
	 */
	public void begin() throws HibernateException;

	/**
	 * 提交<tt>Session</tt>相关操作并且结束工作单元(除非我们设置{@link FlushMode#MANUAL}.
	 * </p>
	 * 这个方法仅仅提交初始化在这个对象的底层事务
	 *
	 * @throws HibernateException
	 */
	public void commit() throws HibernateException;

	/**
	 * 强制回滚底层的事务
	 *
	 * @throws HibernateException
	 */
	public void rollback() throws HibernateException;

	/**
	 * 事务回滚或设置回滚？
	 * <p/>
	 * This only accounts for actions initiated from this local transaction.
	 * If, for example, the underlying transaction is forced to rollback via
	 * some other means, this method still reports false because the rollback
	 * was not initiated from here.
	 *
	 * @return boolean True如果事务被回滚通过本地事务；否则False
	 * 
	 * @throws HibernateException
	 */
	public boolean wasRolledBack() throws HibernateException;

	/**
	 * 检查事务是否被成功提交
	 * <p/>
	 * This method could return <tt>false</tt> even after successful invocation
	 * of {@link #commit}.  As an example, JTA based strategies no-op on
	 * {@link #commit} calls if they did not start the transaction; in that case,
	 * they also report {@link #wasCommitted} as false.
	 *
	 * @return boolean True如果事务被(明确)提交通过本地事务；否则False
	 * @throws HibernateException
	 */
	public boolean wasCommitted() throws HibernateException;
	
	/**
	 * 事务是否活动
	 * <p/>
	 * 这个仅仅返回与本地事务相关的信息, 而不是实际的底层事务
	 *
	 * @return boolean True如果本地事务仍在活动
	 */
	public boolean isActive() throws HibernateException;

	/**
	 * 为本次事务注册用户同步回调
	 *
	 * @param synchronization The Synchronization callback to register.
	 * @throws HibernateException
	 */
	public void registerSynchronization(Synchronization synchronization) 
	throws HibernateException;

	/**
	 * 在这个实例中为任何通过调用<tt>begin()</tt>被启动的事务设定超时时间
	 *
	 * @param seconds 在超时之前秒数.
	 */
	public void setTimeout(int seconds);
}
