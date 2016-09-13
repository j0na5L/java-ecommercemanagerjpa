package se.bitsplz.jpa;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import se.bitsplz.jpa.model.Order;
import se.bitsplz.jpa.model.Product;
import se.bitsplz.jpa.model.Product.ProductStatus;
import se.bitsplz.jpa.model.User;
import se.bitsplz.jpa.model.User.UserStatus;
import se.bitsplz.jpa.repository.JpaOrderRepository;
import se.bitsplz.jpa.repository.JpaProductRepository;
import se.bitsplz.jpa.repository.JpaUserRepository;
import se.bitsplz.jpa.repository.OrderRepository;
import se.bitsplz.jpa.repository.ProductRepository;
import se.bitsplz.jpa.repository.UserRepository;


/* author github.com/... jonnakollin, j0na5, AlexAasen */
public class Main{
   
   private static final EntityManagerFactory factory = Persistence.createEntityManagerFactory("PersistenceUnit");
   private static UserRepository userRepository = new JpaUserRepository(factory);
   private static ProductRepository productRepository = new JpaProductRepository(factory);
   private static OrderRepository orderRepository = new JpaOrderRepository(factory);
   
   
   public static void main(String[] args){
      ECommerceManager eCommerceManager = new ECommerceManager(productRepository, userRepository, orderRepository);
      User user = new User("username", "firstName", "lastName", "password", UserStatus.ADMIN);
      User user2 = new User("Herbit", "Harry", "Potter", "password", UserStatus.ADMIN);
      
      eCommerceManager.addUser(user);
      eCommerceManager.addUser(user2);
      
      Product product = new Product(1001, "Tröja", 199.99, ProductStatus.MANUFACTURER_SENDING_TO_PEDIWEAR);
      Product product2 = new Product(1002, "Sjal", 79.95, ProductStatus.IN_STOCK);
      
      eCommerceManager.addProduct(product2);
      eCommerceManager.addProduct(product);
      Order order = new Order(eCommerceManager.getUserById(2L));
      order.addProduct(eCommerceManager.getProductById(3L), 1);
      order.addProduct(eCommerceManager.getProductById(4L), 4);
      eCommerceManager.placeOrder(order);
      
      order = new Order(eCommerceManager.getUserById(2L));
      order.addProduct(eCommerceManager.getProductById(4L), 2);
      eCommerceManager.placeOrder(order);
      order = eCommerceManager.getOrderById(5L);
      order.cartToString();
      
      // order.addProduct(eCommerceManager.getProductbyId(3L), 2);
      // eCommerceManager.placeOrder(order);
      // eCommerceManager.updateOrder(order, 1L);
      
      // eCommerceManager.updateStatus(1L, OrderStatus.CANCELLED);
      
      // System.out.println(eCommerceManager.getOrderById(5L).toString());
      // System.out.println(eCommerceManager.getAllOrders());
      // System.out.println(eCommerceManager.getAllOrders());
      // System.out.println(eCommerceManager.getOrderByStatus(OrderStatus.PLACED));
      // System.out.println(eCommerceManager.getOrdersByUser(user));
      
      // eCommerceManager.updateProduct(product);
      // eCommerceManager.updateStatus(product, new Status(ProductStatus.IN_STOCK));
      // System.out.println(eCommerceManager.getProductByProductName("Sjal").toString());
      // System.out.println(eCommerceManager.getAllProducts().toString());
      // System.out.println(eCommerceManager.getProductbyId(1L).toString());
      
      // System.out.println(user.getUserStatus());
      // eCommerceManager.updateStatus(user2, new Status(UserStatus.ADMIN));
      // eCommerceManager.updateUser(user2);
      // System.out.println(eCommerceManager.getUserbyId(1L).toString());
      // System.out.println(eCommerceManager.getAllUsers().toString());
      // System.out.println(eCommerceManager.getUserbyUsername("Herbit").toString());
      
   }
}
