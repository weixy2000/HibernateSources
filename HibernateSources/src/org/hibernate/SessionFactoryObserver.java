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
 * {@link SessionFactory}基本事件的响应.
 *
 * @author Steve Ebersole
 */
public interface SessionFactoryObserver extends Serializable {
	/**
	 * 回调函数，表明给定的工厂已经被创建，并正准备使用
	 *
	 * @param factory 被初始化的工厂
	 */
	public void sessionFactoryCreated(SessionFactory factory);

	/**
	 * 回调函数，表明给定的工厂已经关闭.  
	 * 
	 * 注意：自从工厂被关闭后所有与该工厂有关联的操作都应该很谨慎
	 *
	 * @param factory 被关闭的工厂
	 */
	public void sessionFactoryClosed(SessionFactory factory);
}
