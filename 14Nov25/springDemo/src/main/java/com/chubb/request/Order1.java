package com.chubb.request;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class Order 
{
	
	//attributes
	
	@NotBlank
	private String item;
	
	@Min(value=1,message="price>0")
	private float price;
	
	//@Min(value=1)
	@Range(min=1,max=10, message="quantity between 1 and 10")
	private int quantity;
	
	private float totalAmount;
	
	Address address;

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	//getters and setters
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
