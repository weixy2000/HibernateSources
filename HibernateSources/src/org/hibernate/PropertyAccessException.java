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

import org.hibernate.util.StringHelper;

/**
 * 出现问题，通过反射访问一个持久化类实例的属性，或通过CGLIB的。
 * 可能的根本原因，包括
 * <ul>
 * <li>安全检查失败
 * <li>一个异常出现的getter或setter方法中
 * <li>可为空的数据库列映射到一个原始类型的属性
 * <li>Hibernate类型不能强制转换成属性类型（或反之亦然）
 * </ul>
 * @author Gavin King
 */
public class PropertyAccessException extends HibernateException {

	private final Class persistentClass;
	private final String propertyName;
	private final boolean wasSetter;

	public PropertyAccessException(Throwable root, String s, boolean wasSetter, Class persistentClass, String propertyName) {
		super(s, root);
		this.persistentClass = persistentClass;
		this.wasSetter = wasSetter;
		this.propertyName = propertyName;
	}

	public Class getPersistentClass() {
		return persistentClass;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public String getMessage() {
		return super.getMessage() +
		( wasSetter ? " setter of " : " getter of ") +
		StringHelper.qualify( persistentClass.getName(), propertyName );
	}
}






