/**
 * 
 */
package com.bcv.services;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 30/10/2015 08:43:31
 * 2015
 * mail : oraclefedora@gmail.com
 */
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.bcv.dto.Product;
@Path("/customers")
public class CustomerResource {
	
	   public CustomerResource()
	   {
	   }
	   @GET
		@Path("/get")
		@Produces("application/json")
		public Product getProductInJSON() {

			Product product = new Product();
			product.setName("iPad 3");
	 
			return product; 

		}
	   

}
