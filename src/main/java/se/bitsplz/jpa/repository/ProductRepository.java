package se.bitsplz.jpa.repository;

import java.util.List;

import se.bitsplz.jpa.model.Product;

public interface ProductRepository extends CRUDRepository<Product>
{
   
   public Product getProductByProductName(String productName);
   
   
   public Product getProductByItemNumber(int itemNumber);
   
   
   public List<Product> getAll();
}
