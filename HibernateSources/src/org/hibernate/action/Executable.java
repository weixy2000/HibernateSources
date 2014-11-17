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
package org.hibernate.action;

import org.hibernate.HibernateException;

import java.io.Serializable;

/**
 * 在数据库insert/update/delete操作执行完成后可能需要执行一个对二级缓存进行管理操作
 * 
 * @author Gavin King
 * @author Steve Ebersole
 */
public interface Executable {
	/**
	 * 返回本次action中受影响的表空间
	 *
	 * @return 被本次action影响的表空间
	 */
	public Serializable[] getPropertySpaces();

	/**
	 * 在执行任何action之前被调用。让action进行一些预操作
	 *
	 * @throws HibernateException 表明预操作过程中异常
	 */
	public void beforeExecutions() throws HibernateException;

	/**
	 * 执行action
	 *
	 * @throws HibernateException 表明执行过程中异常
	 */
	public void execute() throws HibernateException;

	/**
	 * 如果有的话，获取事务完成之后的处理方法(after-transaction-completion)
	 *
	 * @return 返回after-transaction-completion，没有的话返回null
	 */
	public AfterTransactionCompletionProcess getAfterTransactionCompletionProcess();

	/**
	 * 如果有的话，获取事务完成之前的处理方法(before-transaction-completion)
	 *
	 * @return 返回before-transaction-completion，没有的话返回null
	 */
	public BeforeTransactionCompletionProcess getBeforeTransactionCompletionProcess();
}
