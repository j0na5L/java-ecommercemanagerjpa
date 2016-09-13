package se.bitsplz.jpa.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.bitsplz.jpa.model.Order;
import se.bitsplz.jpa.model.Product;
import se.bitsplz.jpa.model.User;
import se.bitsplz.jpa.model.Order.OrderStatus;
import se.bitsplz.jpa.model.Product.ProductStatus;
import se.bitsplz.jpa.model.User.UserStatus;
import se.bitsplz.jpa.repository.JpaOrderRepository;
import se.bitsplz.jpa.repository.JpaProductRepository;
import se.bitsplz.jpa.repository.JpaUserRepository;
import se.bitsplz.jpa.repository.OrderRepository;
import se.bitsplz.jpa.repository.ProductRepository;
import se.bitsplz.jpa.repository.UserRepository;


public class OrderRepositoryTest{
   
   private final String username = "username";
   private final String firstName = "firstName";
   private final String lastName = "lastName";
   private final String password = "Password12#";
   private final UserStatus userStatus = UserStatus.USER;
   private final int productNumber = 1001;
   private final String productName = "Jeans";
   private final double productPrice = 799.99;
   
   UserRepository userRepository;
   OrderRepository orderRepository;
   ProductRepository productRepository;
   User user;
   Order order;
   Product product;
   Product product2;
   
   
   @Before
   public void setUp() throws Exception{
      this.userRepository = new JpaUserRepository(EntityManagerTest.factory);
      this.orderRepository = new JpaOrderRepository(EntityManagerTest.factory);
      this.productRepository = new JpaProductRepository(EntityManagerTest.factory);
      createDatabaseTest();
   }
   
   
   private void createDatabaseTest(){
      this.user = new User(this.username, this.firstName, this.lastName, this.password, this.userStatus);
      this.product = new Product(this.productNumber, this.productName, this.productPrice, ProductStatus.IN_STOCK);
      this.product2 = new Product(1002, "shorts", 399.00, ProductStatus.IN_STOCK);
      this.order = new Order(this.user);
      this.order.addProduct(this.product, 2);
      this.userRepository.createOrUpdate(this.user);
      this.productRepository.createOrUpdate(this.product);
      this.productRepository.createOrUpdate(this.product2);
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
   public void thatOrdersArePlaced(){
      List<Order> placedOrder = this.orderRepository.getAll();
      assertThat(placedOrder.size(), is(1));
      Order otherOrder = placedOrder.get(0);
      assertThat(otherOrder, equalTo(this.order));
   }
   
   
   @Test
   public void getOrdersByUser(){
      List<User> users = this.userRepository.getAll();
      assertThat(users.size(), is(1));
      List<Order> orders = this.orderRepository.getOrdersByUser(users.get(0));
      assertThat(orders.size(), is(1));
      assertThat(this.order, equalTo(orders.get(0)));
   }
   
   
   @Test
   public void thatOrderContainsCorrectProduct(){
      List<Order> orders = this.orderRepository.getAll();
      assertThat(orders.get(0).getCart().containsKey(this.product), is(true));
   }
   
   
   @Test
   public void getAllOrdersUnderCertainValue(){
      List<User> users = this.userRepository.getAll();
      assertThat(users.size(), is(1));
      Order order2 = new Order(users.get(0));
      order2.addProduct(this.product2, 10);
      order2.setOrderStatus(OrderStatus.SHIPPED);
      this.orderRepository.createOrUpdate(order2);
      List<Order> placedOrders = this.orderRepository.getAll();
      assertThat(placedOrders.size(), is(2));
      placedOrders = this.orderRepository.getOrdersByMinimumValue(1600);
      assertThat(placedOrders.size(), is(1));
   }
   
   
   @Test
   public void getAllOrdersByOrderStatus(){
      List<Order> orders = this.orderRepository.getOrderByStatus(OrderStatus.PLACED);
      assertThat(orders.size(), is(1));
   }
   
   
   @Test
   public void getOrderById(){
      Order storedOrder = this.orderRepository.findById(this.order.getId());
      assertThat(storedOrder, equalTo(this.order));
   }
   
   
   @After
   public void closeFactory(){
      removeTestData();
   }
}
