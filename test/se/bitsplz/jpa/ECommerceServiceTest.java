package se.bitsplz.jpa;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.bitsplz.jpa.ECommerceManager;
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


public class ECommerceServiceTest{
   
   private UserRepository userRepository;
   private ProductRepository productRepository;
   private OrderRepository orderRepository;
   private ECommerceManager eCommerceManager;
   
   private static EntityManagerFactory factory;
   
   private final String username = "username";
   private final String firstName = "firstName";
   private final String lastName = "lastName";
   private final String password = "passWord12#";
   private final int productNumber = 1001;
   private final String productName = "Jeans";
   private final double productPrice = 799.99;
   private User user;
   private Product product;
   private Product product2;
   private Order order;
   
   
   @Before
   public void fillDatabase(){
      factory = Persistence.createEntityManagerFactory("PersistenceUnit");
      this.userRepository = new JpaUserRepository(factory);
      this.productRepository = new JpaProductRepository(factory);
      this.orderRepository = new JpaOrderRepository(factory);
      this.eCommerceManager = new ECommerceManager(this.productRepository, this.userRepository, this.orderRepository);
      
      this.user = new User(this.username, this.firstName, this.lastName, this.password, UserStatus.ADMIN);
      this.eCommerceManager.addUser(this.user);
      
      this.product = new Product(this.productNumber, this.productName, this.productPrice, ProductStatus.IN_STOCK);
      this.eCommerceManager.addProduct(this.product);
      
      this.product2 = new Product(1002, "shorts", 399.00, ProductStatus.IN_STOCK);
      this.eCommerceManager.addProduct(this.product2);
      
      this.order = new Order(this.user);
      this.order.addProduct(this.product, 2);
      this.eCommerceManager.placeOrder(this.order);
   }
   
   
   @Test
   public void thatUserWillBeAdded(){
      List<User> users = this.eCommerceManager.getAllUsers();
      assertThat(users.size(), is(1));
      assertNotNull("Users in list", users);
      assertThat(this.user, equalTo(users.get(0)));
   }
   
   
   @Test
   public void getUserByUsername(){
      User storedUser = this.eCommerceManager.getUserByUsername(this.username);
      assertThat(storedUser, equalTo(this.user));
   }
   
   
   @Test
   public void thatUserWillBeUpdated(){
      String updatedName = "updatedName";
      List<User> users = this.eCommerceManager.getAllUsers();
      User userToUpdate = users.get(0);
      userToUpdate.setFirstName(updatedName);
      this.eCommerceManager.addUser(userToUpdate);
      List<User> updatedUsers = this.eCommerceManager.getAllUsers();
      assertThat(updatedUsers.size(), is(1));
      assertThat(updatedUsers.get(0).getFirstName(), equalTo(updatedName));
   }
   
   
   @Test
   public void userWillBeRemoved(){
      List<Order> orders = this.eCommerceManager.getAllOrders();
      assertThat(orders.size(), is(1));
      this.eCommerceManager.deleteUser(this.user);
      List<User> users = this.eCommerceManager.getAllUsers();
      assertThat(users.size(), is(0));
      orders = this.eCommerceManager.getAllOrders();
      assertThat(orders.size(), is(0));
   }
   
   
   // <-------PRODUCTS----------->
   
   @Test
   public void thatProductsAreAdded(){
      List<Product> products = this.eCommerceManager.getAllProducts();
      assertThat(products.size(), is(2));
      assertThat(this.product, equalTo(products.get(0)));
   }
   
   
   @Test
   public void getProductByProductName(){
      Product storedProduct = this.eCommerceManager.getProductByProductName(this.productName);
      assertThat(storedProduct, equalTo(this.product));
   }
   
   
   @Test
   public void getProductByProductNumber(){
      Product storedProduct = this.eCommerceManager.getProductByProductNumber(this.productNumber);
      assertThat(storedProduct, equalTo(this.product));
   }
   
   
   @Test
   public void changeProductStatus(){
      List<Product> products = this.eCommerceManager.getAllProducts();
      assertNotNull(products);
      this.eCommerceManager.updateStatus(products.get(0).getId(), ProductStatus.OUT_OF_STOCK);
      products = this.eCommerceManager.getAllProducts();
      assertThat(products.get(0).getProductStatus(), equalTo(ProductStatus.OUT_OF_STOCK));
   }
   
   
   // <-------ORDERS---------->
   @Test
   public void thatOrdersArePlaced(){
      List<Order> placedOrder = this.eCommerceManager.getAllOrders();
      assertThat(placedOrder.size(), is(1));
   }
   
   
   @Test
   public void getOrdersByUser(){
      List<Order> orders = this.eCommerceManager.getOrdersByUser(this.user);
      assertThat(orders.size(), is(1));
      assertThat(orders.get(0), equalTo(this.order));
   }
   
   
   @Test
   public void thatOrderContainsCorrectProduct(){
      List<Order> orders = this.eCommerceManager.getAllOrders();
      assertThat(orders.get(0).getCart().containsKey(this.product), is(true));
   }
   
   
   @Test
   public void getAllOrdersUnderCertainValue(){
      Order order2 = new Order(this.eCommerceManager.getUserByUsername(this.username));
      order2.addProduct(this.product2, 10);
      this.eCommerceManager.placeOrder(order2);
      List<Order> orders = this.eCommerceManager.getOrdersByMinimumValue(1600);
      assertThat(orders.size(), is(1));
   }
   
   
   @Test
   public void getAllOrdersByOrderStatus(){
      Order order2 = new Order(this.eCommerceManager.getUserByUsername(this.username));
      order2.addProduct(this.product2, 10);
      this.eCommerceManager.placeOrder(order2);
      List<Order> orders = this.eCommerceManager.getOrderByStatus(OrderStatus.PLACED);
      assertThat(orders.size(), is(2));
   }
   
   
   @After
   public void afterTest(){
      factory.close();
   }
   
}
