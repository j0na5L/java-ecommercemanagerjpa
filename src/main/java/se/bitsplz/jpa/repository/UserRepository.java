package se.bitsplz.jpa.repository;

import se.bitsplz.jpa.model.User;


public interface UserRepository extends CRUDRepository<User>{
 
      public User getUserByUsername(String username);

}
