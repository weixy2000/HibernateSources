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

/**
 * 当通过主键标识值调用<tt>Session.load()</tt> 方法无法获取一行时将会抛出该异常。
 * 尽管在数据库中没有行，那么在调用<tt>load()</tt>方法也可能不抛出该异常，因为<tt>load()</tt>可能返回一个代理(proxy)。
 * 应用程序应该使用<tt>Session.get()</tt>去测试数据库中是否存在有行。<br>
 * <br> 
 * 像所有的Hibernate异常一样, 这个异常被认为是不可恢复的 
 *
 * @author Gavin King
 */
public class ObjectNotFoundException extends UnresolvableObjectException {

	public ObjectNotFoundException(Serializable identifier, String clazz) {
		super(identifier, clazz);
	}
}
