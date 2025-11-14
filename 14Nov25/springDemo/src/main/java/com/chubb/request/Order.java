package com.chubb.request;

import jakarta.validation.constraints.Min;

public class Order 
{
	
	//attributes
	private String item;
	
	@Min(value=1)
	private float price;
	
	private int quantity;
	private float totalAmount;

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
