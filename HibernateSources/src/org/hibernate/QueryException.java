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
 * 
 * 由于无效的query语法等原因无法将Hibernate query翻译成 SQL语句时抛出该异常
 * 
 */
public class QueryException extends HibernateException {

	private String queryString;

	public QueryException(String message) {
		super(message);
	}
	public QueryException(String message, Throwable e) {
		super(message, e);
	}

	public QueryException(String message, String queryString) {
		super(message);
		this.queryString = queryString;
	}

	public QueryException(Exception e) {
		super(e);
	}
	
	/**
	 * 获取查询语句
	 * @return
	 */
	public String getQueryString() {
		return queryString;
	}

	/**
	 * 设置查询语句
	 * @param queryString
	 */
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	/**
	 * 获取异常信息
	 */
	public String getMessage() {
		String msg = super.getMessage();
		if ( queryString!=null ) msg += " [" + queryString + ']';
		return msg;
	}

}







