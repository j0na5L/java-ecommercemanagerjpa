package se.bitsplz.jpa.repository;

import java.util.List;

import se.bitsplz.jpa.model.AbstractEntity;


public interface CRUDRepository<E extends AbstractEntity>{
   
   E createOrUpdate(E entity);
   
   
   E delete(E entity);
   
   
   E findById(Long id);
//   E findById(Long id) throws RepositoryException;
   
   
   List<E> getAll();
   
}
