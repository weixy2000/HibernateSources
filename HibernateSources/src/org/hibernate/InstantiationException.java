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

/**
 * 在程序运行时Hibernate无法实例化 一个实体 或 组件类 抛出该异常
 * 
 *
 * @author Gavin King
 */

public class InstantiationException extends HibernateException {

	private final Class clazz;

	public InstantiationException(String s, Class clazz, Throwable root) {
		super(s, root);
		this.clazz = clazz;
	}

	public InstantiationException(String s, Class clazz) {
		super(s);
		this.clazz = clazz;
	}

	public InstantiationException(String s, Class clazz, Exception e) {
		super(s, e);
		this.clazz = clazz;
	}

	/**
	 * 获取持久化类
	 * @return
	 */
	public Class getPersistentClass() {
		return clazz;
	}

	public String getMessage() {
		return super.getMessage() + clazz.getName();
	}

}






