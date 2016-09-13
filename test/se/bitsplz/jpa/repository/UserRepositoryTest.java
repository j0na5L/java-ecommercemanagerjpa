package se.bitsplz.jpa.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.bitsplz.jpa.model.Order;
import se.bitsplz.jpa.model.Product;
import se.bitsplz.jpa.model.User;
import se.bitsplz.jpa.model.Product.ProductStatus;
import se.bitsplz.jpa.model.User.UserStatus;
import se.bitsplz.jpa.repository.JpaOrderRepository;
import se.bitsplz.jpa.repository.JpaProductRepository;
import se.bitsplz.jpa.repository.JpaUserRepository;
import se.bitsplz.jpa.repository.OrderRepository;
import se.bitsplz.jpa.repository.ProductRepository;
import se.bitsplz.jpa.repository.UserRepository;


public class UserRepositoryTest{
   
   UserRepository userRepository;
   OrderRepository orderRepository;
   ProductRepository productRepository;
   EntityManagerFactory factory;
   private static final String username = "username";
   private static final String firstName = "firstName";
   private static final String lastName = "lastName";
   private static final String password = "Password12#";
   private static final UserStatus userStatus = UserStatus.ADMIN;
   
   private final int productNumber = 1001;
   private final String productName = "Jeans";
   private final double productPrice = 799.99;
   
   User user;
   Product product;
   Order order;
   
   
   @Before
   public void setUp() throws Exception{
      this.userRepository = new JpaUserRepository(EntityManagerTest.factory);
      this.orderRepository = new JpaOrderRepository(EntityManagerTest.factory);
      this.productRepository = new JpaProductRepository(EntityManagerTest.factory);
      createDatabaseTest();
   }
   
   
   private void createDatabaseTest(){
      this.user = new User(username, firstName, lastName, password, userStatus);
      this.product = new Product(this.productNumber, this.productName, this.productPrice, ProductStatus.IN_STOCK);
      this.order = new Order(this.user);
      this.order.addProduct(this.product, 2);
      this.userRepository.createOrUpdate(this.user);
      this.productRepository.createOrUpdate(this.product);
      this.orderRepository.createOrUpdate(this.order);
   }
   
   
   private void removeTestData(){
      List<User> findUser = this.userRepository.getAll();
      if(findUser.size() != 0){
         for(int i = 0; i < findUser.size(); i++){
            this.userRepository.delete(findUser.get(i));
         }
      }
      List<Order> findOrder = this.orderRepository.getAll();
      if(findOrder.size() != 0){
         for(int i = 0; i < findOrder.size(); i++){
            this.orderRepository.delete(findOrder.get(i));
         }
      }
      List<Product> findProduct = this.productRepository.getAll();
      if(findProduct.size() != 0){
         for(int i = 0; i < findProduct.size(); i++){
            this.productRepository.delete(findProduct.get(i));
         }
      }
   }
   
   
   @Test
   public void thatUserWillBeAdded(){
      List<User> users = this.userRepository.getAll();
      assertThat(users.size(), is(1));
      assertNotNull("Users in list is null", users);
      assertThat(this.user, equalTo(users.get(0)));
   }
   
   
   @Test
   public void getUserByUsername(){
      User storedUser = this.userRepository.getUserByUsername(username);
      assertThat(storedUser, equalTo(this.user));
   }
   
   
   @Test
   public void thatUserWillBeUpdated(){
      String updatedName = "updatedName";
      List<User> users = this.userRepository.getAll();
      User userToUpdate = users.get(0);
      userToUpdate.setFirstName(updatedName);
      this.userRepository.createOrUpdate(userToUpdate);
      List<User> updatedUsers = this.userRepository.getAll();
      assertThat(updatedUsers.size(), is(1));
      assertThat(updatedUsers.get(0).getFirstName(), equalTo(updatedName));
   }
   
   
   @Test
   public void userWillBeRemoved(){
      List<Order> orders = this.orderRepository.getAll();
      assertThat(orders.size(), is(1));
      this.userRepository.delete(this.user);
      List<User> users = this.userRepository.getAll();
      assertThat(users.size(), is(0));
      orders = this.orderRepository.getAll();
      assertThat(orders.size(), is(0));
   }
   
   
   @Test
   public void getUserByTheirId(){
      List<User> users = this.userRepository.getAll();
      assertThat(users.size(), is(1));
      User storedUser = this.userRepository.findById(users.get(0).getId());
      assertThat(storedUser, equalTo(this.user));
   }
   
   
   @After
   public void clearDatabase(){
      removeTestData();
   }
   
}
