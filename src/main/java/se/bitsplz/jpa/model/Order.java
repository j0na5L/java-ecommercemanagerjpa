package se.bitsplz.jpa.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


@Entity(name = "Orders")
@NamedQueries(value = {@NamedQuery(name = "Orders.GetAll", query = "select o from Orders o"), @NamedQuery(name = "Orders.ByStatus", query = "select o from Orders o where o.orderStatus = :status"), @NamedQuery(name = "Orders.ByMinimumValue", query = "select o from Orders o where o.orderValue >= :orderValue"), @NamedQuery(name = "Orders.ByUser", query = "select o from Orders o where o.user = :userid")})
public class Order extends AbstractEntity{
   
   @Column
   private double orderValue;
   @ElementCollection(fetch = FetchType.EAGER)
   @CollectionTable
   @Column(name = "quantity")
   private Map<Product, Integer> orderRows;
   @Enumerated(EnumType.STRING)
   private OrderStatus orderStatus;
   @ManyToOne
   private User user;
   
   public enum OrderStatus{
      PLACED, SHIPPED, PAYED, CANCELLED;
   }
   
   
   protected Order(){
   }
   
   
   public Order(User user){
      this.user = user;
      this.orderRows = new HashMap<>();
   }
   
   
   public double getOrderValue(){
      return this.orderValue;
   }
   
   
   public Map<Product, Integer> getCart(){
      return this.orderRows;
   }
   
   
   public void setOrderValues(Order order){
      this.orderRows = order.orderRows;
      this.orderValue = order.orderValue;
      this.orderStatus = order.orderStatus;
   }
   
   
   public void addProduct(Product product, int amount){
      if(this.orderRows.containsKey(product)){
         int newAmount = (this.orderRows.get(product) + amount);
         updateProductAmount(product, newAmount);
      }else{
         this.orderRows.put(product, amount);
         calculateValue(product, amount);
      }
      this.orderStatus = OrderStatus.PLACED;
   }
   
   
   public void removeProduct(Product product){
      int amount = this.orderRows.get(product);
      this.orderRows.remove(product);
      removeValue(product, amount);
   }
   
   
   public void updateProductAmount(Product product, int newAmount){
      removeProduct(product);
      this.orderRows.put(product, newAmount);
      calculateValue(product, newAmount);
   }
   
   
   private void calculateValue(Product product, int amount){
      this.orderValue += (product.getPrice() * amount);
   }
   
   
   private void removeValue(Product product, int amount){
      this.orderValue -= (product.getPrice() * amount);
   }
   
   
   public void setOrderStatus(OrderStatus status){
      this.orderStatus = status;
   }
   
   
   public OrderStatus getOrderStatus(){
      return this.orderStatus;
   }
   
   
   @Override
   public int hashCode(){
      int result = 11;
      result += 37 * this.orderRows.hashCode();
      result += 37 + this.orderValue;
      result += 37 * this.user.hashCode();
      result += 37 * this.orderStatus.hashCode();
      return result;
   }
   
   
   @Override
   public boolean equals(Object other){
      if(this == other){
         return true;
      }
      if(other instanceof Order){
         Order otherOrder = (Order)other;
         return this.orderRows.equals(otherOrder.orderRows) && this.user.equals(otherOrder.user) && this.orderValue == otherOrder.orderValue && this.orderStatus.equals(otherOrder.orderStatus);
      }
      return false;
   }
   
   
   public String cartToString(){
      String cart = "";
      for(Map.Entry<Product, Integer> entry : this.orderRows.entrySet()){
         cart += entry.getKey().cartToString() + " : " + entry.getValue() + "\n";
      }
      
      return cart;
   }
   
   
   @Override
   public String toString(){
      return "Order id: " + getId() + ", User: " + this.user.getUsername() + ", Cart: " + cartToString() + ". Total " + this.orderValue + "kr " + this.orderStatus.toString();
   }
}
