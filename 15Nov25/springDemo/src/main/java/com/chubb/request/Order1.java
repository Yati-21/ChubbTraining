package com.chubb.request;


import java.util.List;

import org.hibernate.validator.constraints.Range;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;


@Entity
public class Order1 
{
	
	//attributes
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id; //primary key
	
	@NotBlank
	private String item;
	
	@Min(value=1,message="price>0")
	private float price;
	
	//@Min(value=1)
	@Range(min=1,max=10, message="quantity between 1 and 10")
	private int quantity;
	
	private float totalAmount;

	@ManyToOne(cascade = CascadeType.ALL) 
	@JoinColumn(name = "address_id") //address_id:: address is the table name and id is the attribute in address table which is used to join
	private Address address;
	
	@ManyToMany(cascade = CascadeType.ALL) 
	@JoinTable(
			name="order_customer",    //join tabel name
			joinColumns= @JoinColumn(name = "order_id"),
			inverseJoinColumns = @JoinColumn(name= "customer_id")
	)
	private List<Customer> customers;
	


	//getters and setters
	
	public List<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}

	public Address getAddress() {
		return address;
	}
	
	public void setAddress(Address address) {
		this.address = address;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public float getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(float totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	

}
