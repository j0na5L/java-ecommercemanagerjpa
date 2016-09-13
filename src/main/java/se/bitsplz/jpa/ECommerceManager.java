package se.bitsplz.jpa;

import java.util.List;

import se.bitsplz.jpa.model.Order;
import se.bitsplz.jpa.model.Product;
import se.bitsplz.jpa.model.User;
import se.bitsplz.jpa.model.Order.OrderStatus;
import se.bitsplz.jpa.model.Product.ProductStatus;
import se.bitsplz.jpa.model.User.UserStatus;
import se.bitsplz.jpa.repository.OrderRepository;
import se.bitsplz.jpa.repository.ProductRepository;
import se.bitsplz.jpa.repository.UserRepository;


public class ECommerceManager{
   
   private UserRepository userRepository;
   private ProductRepository productRepository;
   private OrderRepository orderRepository;
   
   
   public ECommerceManager(ProductRepository productRepository, UserRepository userRepository, OrderRepository orderRepository){
      this.userRepository = userRepository;
      this.productRepository = productRepository;
      this.orderRepository = orderRepository;
   }
   
   
   // ----------USER
   public User getUserById(Long id){
      return this.userRepository.findById(id);
   }
   
   
   public List<User> getAllUsers(){
      return this.userRepository.getAll();
   }
   
   
   public User getUserByUsername(String username){
      return this.userRepository.getUserByUsername(username);
   }
   
   
   public User addUser(User user){
      return this.userRepository.createOrUpdate(user);
   }
   
   
   public User updateUser(Long id, User inputUser){
      User user = this.userRepository.findById(id);
      user.updateValues(inputUser);
      return this.userRepository.createOrUpdate(user);
   }
   
   
   public User updateStatus(Long id, UserStatus userStatus){
      User user = this.userRepository.findById(id);
      user.setUserStatus(userStatus);
      return this.userRepository.createOrUpdate(user);
   }
   
   
   public User deleteUser(User user){
      return this.userRepository.delete(user);
   }
   
   
   // ----------PRODUCT
   public Product getProductById(Long id){
      return this.productRepository.findById(id);
   }
   
   
   public List<Product> getAllProducts(){
      return this.productRepository.getAll();
   }
   
   
   public Product getProductByProductName(String productName){
      return this.productRepository.getProductByProductName(productName);
   }
   
   
   public Product getProductByProductNumber(int productNumber){
      return this.productRepository.getProductByItemNumber(productNumber);
   }
   
   public Product addProduct(Product product){
      return this.productRepository.createOrUpdate(product);
   }
   
   
   public Product updateProduct(Long id, Product inputProduct){
      Product product = getProductById(id);
      product.updateValues(inputProduct);
      return addProduct(product);
   }
   
   
   public Product updateStatus(Long id, ProductStatus productStatus){
      Product product = getProductById(id);
      product.setProductStatus(productStatus);
      return this.productRepository.createOrUpdate(product);
   }
   
   
   public Product deleteProduct(Product product){
      return this.productRepository.delete(product);
   }
   
   
   // ----------ORDER
   public Order getOrderById(Long id){
      return this.orderRepository.findById(id);
   }
   
   
   public List<Order> getAllOrders(){
      return this.orderRepository.getAll();
   }
   
   
   public List<Order> getOrdersByUser(User user){
      return this.orderRepository.getOrdersByUser(user);
   }
   
   
   public List<Order> getOrderByStatus(OrderStatus orderStatus){
      return this.orderRepository.getOrderByStatus(orderStatus);
   }
   
   
   public List<Order> getOrdersByMinimumValue(double value){
      return this.orderRepository.getOrdersByMinimumValue(value);
   }
   
   
   public Order placeOrder(Order order){
      order.setOrderStatus(OrderStatus.PLACED);
      return this.orderRepository.createOrUpdate(order);
   }
   
   
   public Order updateOrder(Long id, Order inputorder){
      Order order = this.orderRepository.findById(id);
      if(inputorder.getOrderStatus() == null){
         inputorder.setOrderStatus(OrderStatus.PLACED);
      }
      order.setOrderValues(inputorder);
      return this.orderRepository.createOrUpdate(order);
   }
   
   
   public Order updateStatus(Long id, OrderStatus status){
      Order order = this.orderRepository.findById(id);
      order.setOrderStatus(status);
      return this.orderRepository.createOrUpdate(order);
   }
   
   
   public Order deleteOrder(Order order){
      return this.orderRepository.delete(order);
   }
}
