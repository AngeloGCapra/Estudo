package br.upf.userdept.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

/**
 * @author Angelo G. Capra
 */

@Repository
public abstract class UserRepositoryImpl implements UserRepository {
	
	@PersistenceContext	
	private EntityManager em;
	
}
