/*
 * Copyright 2008-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nbd.ocp.common.repository.base.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.nbd.ocp.common.repository.base.IOcpBaseDao;
import com.nbd.ocp.common.repository.base.IOcpBaseDo;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.provider.QueryExtractor;
import org.springframework.data.jpa.repository.query.JpaQueryLookupStrategy;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.Optional;

/**
 * Special adapter for Springs {@link org.springframework.beans.factory.FactoryBean} interface to allow easy setup of
 * repository factories via Spring configuration.
 *
 * @author Oliver Gierke
 * @author Eberhard Wolff
 * @author Mark Paluch
 * @param <T> the type of the repository
 */
public class OcpJpaRepositoryFactoryBean<T extends IOcpBaseDao<S, ID>, S extends IOcpBaseDo, ID extends Serializable>
		extends JpaRepositoryFactoryBean<T, S, ID> {

	private @Nullable EntityManager entityManager;

	/**
	 * Creates a new {@link OcpJpaRepositoryFactoryBean} for the given repository interface.
	 *
	 * @param repositoryInterface must not be {@literal null}.
	 */
	public OcpJpaRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
		super(repositoryInterface);
	}

	/**
	 * The {@link EntityManager} to be used.
	 *
	 * @param entityManager the entityManager to set
	 */
	@Override
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
		this.entityManager = entityManager;
	}

	@Override
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
		return new OcpRepositoryFactory(entityManager);
	}

	private static class OcpRepositoryFactory<T extends IOcpBaseDo, I extends Serializable> extends JpaRepositoryFactory {

		private final EntityManager em;
		private final QueryExtractor extractor;

		public OcpRepositoryFactory(EntityManager em) {
			super(em);
			this.extractor = PersistenceProvider.fromEntityManager(em);
			this.em = em;
		}

		@Override
		protected Object getTargetRepository(RepositoryInformation information) {
			return new OcpRepositoryImpl<T, I>((Class<T>) information.getDomainType(), em);
		}

		@Override
		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
			return OcpRepositoryImpl.class;
		}
		@Override
		protected Optional<QueryLookupStrategy> getQueryLookupStrategy(@Nullable QueryLookupStrategy.Key key,
																	   EvaluationContextProvider evaluationContextProvider) {
			return Optional.of(JpaQueryLookupStrategy.create(em, key,extractor, evaluationContextProvider));
		}

	}


}
