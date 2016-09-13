package se.bitsplz.jpa.repository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({UserRepositoryTest.class, OrderRepositoryTest.class, ProductRepositoryTest.class})
public class EntityManagerTest{
   
   public static EntityManagerFactory factory;
   
   
   @BeforeClass
   public static void setUpBeforeClass() throws Exception{
      factory = Persistence.createEntityManagerFactory("PersistenceUnit");
   }
   
   
   @AfterClass
   public static void tearDownAfterClass() throws Exception{
      if(factory != null){
         factory.close();
      }
   }
   
}
