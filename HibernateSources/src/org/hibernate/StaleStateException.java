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
 * 过时状态异常
 * 
 * 当版本号或timestamp检查失败时将会抛出该异常，
 * 表示<tt>Session</tt> 包含过时的数据 (当使用长事务与版本)。
 * 如果我们尝试去删除或修改一条已经不存在的行也会抛出该异常
 * <br>
 * 注意这个异常经常表示： the user failed to specify the
 * correct <tt>unsaved-value</tt> strategy for a class!
 *
 * @see StaleObjectStateException
 * @author Gavin King
 */
public class StaleStateException extends HibernateException {

	public StaleStateException(String s) {
		super(s);
	}
}
