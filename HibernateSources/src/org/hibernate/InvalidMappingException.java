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
 * 当映射内容发现是无效的时将会抛出该异常
 * 类似于MappingException, 但这个类包含更多关于映射路径和类型信息(e.g. file, resource or url)
 * 
 * @author Max Rydahl Andersen
 *
 */
public class InvalidMappingException extends MappingException {

	private final String path;
	private final String type;

	public InvalidMappingException(String customMessage, String type, String path, Throwable cause) {
		super(customMessage, cause);
		this.type=type;
		this.path=path;
	}
	
	public InvalidMappingException(String customMessage, String type, String path) {
		super(customMessage);
		this.type=type;
		this.path=path;
	}
	
	public InvalidMappingException(String type, String path) {
		this("Could not parse mapping document from " + type + (path==null?"":" " + path), type, path);
	}

	public InvalidMappingException(String type, String path, Throwable cause) {
		this("Could not parse mapping document from " + type + (path==null?"":" " + path), type, path, cause);		
	}

	public String getType() {
		return type;
	}
	
	public String getPath() {
		return path;
	}
}
