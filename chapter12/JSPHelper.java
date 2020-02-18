package com.ctimn;

import java.util.*;

public class JSPHelper {

	private int customerId;

	private String firstName;
	private String lastName;
	private String phone;
	private ArrayList items = new ArrayList();

	public void setCustomerId(String id){
		customerId = Integer.parseInt(id);
		loadCustomer();
	}

	public String getCustomerId(){
		return Integer.toString(customerId);
	}

	public String getFirstName(){
		return firstName;
	}

	public String getLastName(){
		return lastName;
	}

	public String getPhone(){
		return phone;
	}

	public Iterator getItemsOrdered(){
		return items.iterator();
	}

	private void loadCustomer(){

		switch (customerId){

			case 1:
				firstName = "David";
				lastName = "Hemphill";
				phone = "555-555-5555";
				items.add("Grass Seed #6633");
				break;

			case 2:
				firstName = "James";
				lastName = "White";
				phone = "555-555-5555";
				items.add("Fertilizer #3345");
				items.add("Fertilizer #9976");
				items.add("Plant Food #9906");
				break;

			case 3:
				firstName = "Scott";
				lastName = "King";
				phone = "555-555-5555";
				items.add("Weed Killer #3345");
				items.add("Grass-B-Gone #998");
				break;

			case 4:
				firstName = "Amy";
				lastName = "Votava";
				items.add("Orchid Food #112");
				items.add("Grow Light #KJ44");
				phone = "555-555-5555";
				break;

			case 5:
				firstName = "Olivia";
				lastName = "Hemphill";
				phone = "555-555-5555";
				items.add("Sunflower Seeds #248");
				break;

			default:
				firstName = "Undefined";
				lastName = "Undefined";
				phone = "Undefined";
		}
	}
}