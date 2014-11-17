/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2009 by Red Hat Inc and/or its affiliates or by
 * third-party contributors as indicated by either @author tags or express
 * copyright attribution statements applied by the authors.  All
 * third-party contributions are distributed under license by Red Hat Inc.
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
 */
package org.hibernate.property;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionFactoryImplementor;

/**
 * 设置指定属性的值
 * 
 * @author Gavin King
 */
public interface Setter extends Serializable {
	/**
	 * 设置给定实例属性的值
	 *
	 * @param target 需要设置该属性值的实例
	 * @param value 将要被设置的值
	 * @param factory 请求发起的session factory
	 * @throws HibernateException
	 */
	public void set(Object target, Object value, SessionFactoryImplementor factory) throws HibernateException;
	/**
	 * 返回setter方法名称
	 * 
	 * 可选操作 (return null)
	 */
	public String getMethodName();
	/**
	 * 返回setter方法
	 * 
	 * 可选操作 (return null)
	 */
	public Method getMethod();
}
