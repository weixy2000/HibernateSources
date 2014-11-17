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
package org.hibernate.mapping;

/**
 * 能够处理元属性的通用接口
 * 
 * @since 3.0.1
 */
public interface MetaAttributable {

	/**
	 * 获取元属性集合
	 * 
	 * @return
	 */
	public java.util.Map getMetaAttributes();

	/**
	 * 设置元属性集合
	 * 
	 * @param metas
	 */
	public void setMetaAttributes(java.util.Map metas);
	
	/**
	 * 根据属性名得到元属性
	 * 
	 * @param name
	 * @return
	 */
	public MetaAttribute getMetaAttribute(String name);

}
