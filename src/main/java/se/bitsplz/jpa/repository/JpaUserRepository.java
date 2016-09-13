package se.bitsplz.jpa.repository;

import static java.util.function.Function.identity;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import se.bitsplz.jpa.model.User;


public final class JpaUserRepository extends AbstractJpaRepository<User> implements UserRepository{
   
   public JpaUserRepository(EntityManagerFactory factory){
      super(factory, User.class);
   }
   
   
   @Override
   public User getUserByUsername(String username){
      return querySingle("User.ByUsername", query -> query.setParameter("username", username));
   }
   
   
   @Override
   public List<User> getAll(){
      return query("User.GetAll", identity());
   }
   
}
