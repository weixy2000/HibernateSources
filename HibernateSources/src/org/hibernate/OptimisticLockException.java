/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2009, Red Hat Middleware LLC or third-party contributors as
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

/**
 * 
 * 当乐观锁定(即把所有锁定都延迟到即将执行更新之前才做)发生冲突时抛出该异常。
 *
 * @author Scott Marlow
 */
public class OptimisticLockException extends HibernateException {

	Object entity;

	/**
	 * 乐观锁异常
	 * @param s
	 */
	public OptimisticLockException(String s) {
		super(s);
	}

	/**
	 * 乐观锁异常
	 * @param s
	 */
	public OptimisticLockException(String s, Object entity) {
		super(s);
		this.entity = entity;
	}

	public Object getEntity() {
		return entity;
	}


}