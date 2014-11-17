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
import java.lang.reflect.Member;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;

/**
 * 获取指定属性的值
 *
 * @author Gavin King
 */
public interface Getter extends Serializable {
	/**
	 * 获取给定实例属性的值
	 * 
	 * @param owner 包含该属性值的实例
	 * @return 被提取的属性值
	 * @throws HibernateException
	 */
	public Object get(Object owner) throws HibernateException;

	/**
	 * 获取给定实例属性的值
	 *
	 * @param owner 包含该属性值的实例
	 * @param mergeMap 从持久态到游离态存储属性的map
	 * @param session 请求发起的session
	 * @return 被提取的属性值
	 * @throws HibernateException
	 */
	public Object getForInsert(Object owner, Map mergeMap, SessionImplementor session) 
	throws HibernateException;

	/**
	 * 从属性映射中检查Member(反映有关单个成员（字段或方法或构造方法）的标识信息).  这个可能是变量也有可能是getter方法
	 *
	 * @return The mapped member.
	 */
	public Member getMember();

	/**
	 * 检索声明的Java Type
	 *
	 * @return 声明的Java Type
	 */
	public Class getReturnType();

	/**
	 * 检索getter-method名称
	 * <p/>
	 * 可选操作 (return null)
	 *
	 * @return getter方法的名称或null
	 */
	public String getMethodName();

	/**
	 * 检索getter-method.
	 * <p/>
	 * 可选操作 (return null)
	 *
	 * @return getter方法或null.
	 */
	public Method getMethod();
}
