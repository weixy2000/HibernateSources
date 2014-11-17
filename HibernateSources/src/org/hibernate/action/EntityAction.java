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
package org.hibernate.action;

import org.hibernate.AssertionFailure;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.pretty.MessageHelper;
import org.hibernate.util.StringHelper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * 实体实例insert/update/delete相关action操作的基类
 *
 * @author Gavin King
 */
public abstract class EntityAction
		implements Executable, Serializable, Comparable, AfterTransactionCompletionProcess {

	private final String entityName;
	private final Serializable id;
	private final Object instance;
	private final SessionImplementor session;

	private transient EntityPersister persister;

	/**
	 * 初始化一个action
	 *
	 * @param session The session from which this action is coming.
	 * @param id The id of the entity
	 * @param instance The entiyt instance
	 * @param persister The entity persister
	 */
	protected EntityAction(SessionImplementor session, Serializable id, Object instance, EntityPersister persister) {
		this.entityName = persister.getEntityName();
		this.id = id;
		this.instance = instance;
		this.session = session;
		this.persister = persister;
	}

	public BeforeTransactionCompletionProcess getBeforeTransactionCompletionProcess() {
		return null;
	}

	public AfterTransactionCompletionProcess getAfterTransactionCompletionProcess() {
		return needsAfterTransactionCompletion()
				? this
				: null;
	}

	protected abstract boolean hasPostCommitEventListeners();

	public boolean needsAfterTransactionCompletion() {
		return persister.hasCache() || hasPostCommitEventListeners();
	}

	/**
	 * 返回实体名称访问器
	 *
	 * @return 实体name
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * 返回实体ID访问器
	 *
	 * @return 实体id
	 */
	public final Serializable getId() {
		if ( id instanceof DelayedPostInsertIdentifier ) {
			return session.getPersistenceContext().getEntry( instance ).getId();
		}
		return id;
	}

	/**
	 * 返回实体实例访问器
	 *
	 * @return entity实例
	 */
	public final Object getInstance() {
		return instance;
	}

	/**
	 * 返回原始session访问器
	 *
	 * @return acttion发起的session
	 */
	public final SessionImplementor getSession() {
		return session;
	}

	/**
	 * 返回实体persister访问器
	 *
	 * @return 实体persister
	 */
	public final EntityPersister getPersister() {
		return persister;
	}

	public final Serializable[] getPropertySpaces() {
		return persister.getPropertySpaces();
	}

	public void beforeExecutions() {
		throw new AssertionFailure( "beforeExecutions() called for non-collection action" );
	}

	public String toString() {
		return StringHelper.unqualify( getClass().getName() ) + MessageHelper.infoString( entityName, id );
	}

	public int compareTo(Object other) {
		EntityAction action = ( EntityAction ) other;
		//sort first by entity name
		int roleComparison = entityName.compareTo( action.entityName );
		if ( roleComparison != 0 ) {
			return roleComparison;
		}
		else {
			//then by id
			return persister.getIdentifierType().compare( id, action.id, session.getEntityMode() );
		}
	}

	/**
	 * 序列化...
	 *
	 * @param ois Thed object stream
	 * @throws IOException Problem performing the default stream reading
	 * @throws ClassNotFoundException Problem performing the default stream reading
	 */
	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();
		persister = session.getFactory().getEntityPersister( entityName );
	}
}

