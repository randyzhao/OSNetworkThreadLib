package com.harvin1;

public class Basket {
private Good[] basket;
	
	public Basket() {
		basket = new Good[100];
	}
	
	public void add(Good g) {
		basket[0] = g;
	}
	
	public Good remove() {
		Good g = null;
		
		g = basket[0];
		basket[0] = null;
		
		return g;
	}
}
