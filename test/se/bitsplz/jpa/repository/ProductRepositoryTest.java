package se.bitsplz.jpa.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.bitsplz.jpa.model.Product;
import se.bitsplz.jpa.model.Product.ProductStatus;
import se.bitsplz.jpa.repository.JpaProductRepository;
import se.bitsplz.jpa.repository.ProductRepository;


public class ProductRepositoryTest{
   
   private final int productNumber = 1001;
   private final String productName = "Jeans";
   private final double productPrice = 799.99;
   
   ProductRepository productRepository;
   Product product;
   
   
   @Before
   public void setUp() throws Exception{
      this.productRepository = new JpaProductRepository(EntityManagerTest.factory);
      createDatabaseTest();
   }
   
   
   private void createDatabaseTest(){
      this.product = new Product(this.productNumber, this.productName, this.productPrice, ProductStatus.IN_STOCK);
      this.productRepository.createOrUpdate(this.product);
   }
   
   
   private void removeTestData(){
      List<Product> findProduct = this.productRepository.getAll();
      if(findProduct.size() != 0){
         for(int i = 0; i < findProduct.size(); i++){
            this.productRepository.delete(findProduct.get(i));
         }
      }
   }
   
   
   @Test
   public void getProductById(){
      List<Product> products = this.productRepository.getAll();
      assertThat(products.size(), is(1));
      Product storedProduct = this.productRepository.findById(products.get(0).getId());
      assertThat(storedProduct, equalTo(this.product));
   }
   
   
   @Test
   public void getAllProducts(){
      List<Product> products = this.productRepository.getAll();
      assertThat(products.size(), is(1));
      assertThat(products.get(0), equalTo(this.product));
   }
   
   
   @Test
   public void getProductByProductName(){
      Product storedProduct = this.productRepository.getProductByProductName(this.productName);
      assertNotNull("Is not stored", storedProduct);
      assertThat(storedProduct, equalTo(this.product));
   }
   
   
   @Test
   public void getProductByProductNumber(){
      Product storedProduct = this.productRepository.getProductByItemNumber(this.productNumber);
      assertNotNull("Is not stored", storedProduct);
      assertThat(storedProduct, equalTo(this.product));
   }
   
   
   @Test
   public void addProductToDatabase(){
      List<Product> products = this.productRepository.getAll();
      assertThat(products.size(), is(1));
      Product product2 = new Product(1002, "Jumper", 99.99, ProductStatus.IN_STOCK);
      this.productRepository.createOrUpdate(product2);
      products = this.productRepository.getAll();
      assertThat(products.size(), is(2));
      assertThat(products.contains(product2), is(true));
      assertThat(products.contains(this.product), is(true));
   }
   
   
   @Test
   public void updateProduct(){
      List<Product> products = this.productRepository.getAll();
      assertThat(products.size(), is(1));
      Product updateProduct = products.get(0);
      updateProduct.setProductStatus(ProductStatus.MANUFACTURER_SENDING_TO_PEDIWEAR);
      updateProduct.setPrice(1499.99);
      this.productRepository.createOrUpdate(updateProduct);
      products = this.productRepository.getAll();
      assertThat(products.size(), is(1));
      assertThat(products.get(0).getPrice(), is(1499.99));
      assertThat(products.get(0).getProductStatus(), equalTo(ProductStatus.MANUFACTURER_SENDING_TO_PEDIWEAR));
   }
   
   
   @After
   public void clearData(){
      removeTestData();
   }
   
}
