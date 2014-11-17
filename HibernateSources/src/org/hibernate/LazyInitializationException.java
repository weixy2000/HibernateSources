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

import org.slf4j.LoggerFactory;

/**
 * 表示访问session上下文没有采集的数据，将会抛出该异常
 * 例如，在session关闭之后去访问一个未初始化的代理或者集合。
 *
 * @see Hibernate#initialize(java.lang.Object)
 * @see Hibernate#isInitialized(java.lang.Object)
 * @author Gavin King
 */
public class LazyInitializationException extends HibernateException {

	public LazyInitializationException(String msg) {
		super(msg);
		LoggerFactory.getLogger( LazyInitializationException.class ).error( msg, this );
	}

}






