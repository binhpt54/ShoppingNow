/**
 * 
 */
package com.vinova_g12.shoppingnow.data;

/**
 * @author codai2810
 *
 */
public class Item {
	private int id;
	private String name;
	private int priority;
	private float quantity;
	private float price;
	private String unit;
	private int status;
	private String place;
	private String time;
	private String date;
	
	public Item() {}
	
	public Item(String name, int priority, float quantity, float price,
			String unit, String place, String time, String date) {
		this.name = name;
		this.priority = priority;
		this.quantity = quantity;
		this.price = price;
		this.unit = unit;
		this.place = place;
		this.time = time;
		this.date = date;
		this.status = 0;
	}

	public Item(String name, int priority, float quantity, float price,
			String unit, String place, String time, String date, int status) {
		this.name = name;
		this.priority = priority;
		this.quantity = quantity;
		this.price = price;
		this.unit = unit;
		this.status = status;
		this.place = place;
		this.time = time;
		this.date = date;
	}
	public int getId() {
		return id;
	}
	
	public void setName(String iname) {
		name = iname;
	}
	
	public String getName() {
		return name;
	}
	
	public void setPriority(int ipriority) {
		priority = ipriority;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public void setQuantity(float iquantity) {
		quantity = iquantity;
	}
	
	public float getQuantity() {
		return quantity;
	}
	
	public void setPrice(float iprice) {
		price = iprice;
	}
	
	public float getPrice() {
		return price;
	}
	
	public void setUnit(String iunit) {
		unit = iunit;
	}
	
	public String getUnit() {
		return unit;
	}
	
	public void setStatus(int istatus) {
		status = istatus;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setPlace(String iplace) {
		place = iplace;
	}
	
	public String getPlace() {
		return place;
	}
	
	public void setTime(String itime) {
		time = itime;
	}
	
	public String getTime() {
		return time;
	}
	
	public void setDate(String idate) {
		date = idate;
	}
	
	public String getDate() {
		return date;
	}
	
	//TODO
	public String toString() {
		return null;
	}

	public void setId(int int1) {
		id = int1;
	}
}
