package se.bitsplz.jpa.repository;

import java.util.List;

import se.bitsplz.jpa.model.Order;
import se.bitsplz.jpa.model.User;
import se.bitsplz.jpa.model.Order.OrderStatus;

public interface OrderRepository extends CRUDRepository<Order>
{
   public List<Order> getOrdersByUser(User user);
   
   public List<Order> getOrdersByMinimumValue(double value);
   
   public List<Order> getOrderByStatus(OrderStatus orderStatus);

   public List<Order> getAll();
   
}
