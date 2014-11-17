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

import java.sql.SQLException;


/**
 * 封装SQLException，用来表示在JDBC调用期间出现的异常
 *
 * @see java.sql.SQLException
 * @author Gavin King
 */
public class JDBCException extends HibernateException {

	private SQLException sqle;
	private String sql;

	public JDBCException(String string, SQLException root) {
		super(string, root);
		sqle=root;
	}

	public JDBCException(String string, SQLException root, String sql) {
		this(string, root);
		this.sql = sql;
	}

	/**
	 * 获取SQLException的SQL状态
	 * @see java.sql.SQLException
	 * @return String
	 */
	public String getSQLState() {
		return sqle.getSQLState();
	}

	/**
	 * 获取SQLException的错误号码 errorCode
	 * @see java.sql.SQLException
	 * @return int the error code
	 */
	public int getErrorCode() {
		return sqle.getErrorCode();
	}

	/**
	 * 获取相关的 SQLException
	 * @return SQLException
	 */
	public SQLException getSQLException() {
		return sqle;
	}
	
	/**
	 * 获取引起异常的SQL语句（有可能为NULL）
	 */
	public String getSQL() {
		return sql;
	}

}
