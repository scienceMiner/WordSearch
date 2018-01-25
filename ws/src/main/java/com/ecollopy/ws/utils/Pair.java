package com.ecollopy.ws.utils;

public class Pair<X,Y> {

	public final X x;
	public final Y y;
	
	public Pair(X x, Y y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return "(" + x + "," + y + ")";
	}
	
	public boolean equals(Object other) {
		if (other == null) { 
			return false;
		}
		
		if (other == this) {
			return true;
		}
		
		if (!(other instanceof Pair)) {
			return false;
		}
		
		Pair<X,Y> other_ = (Pair<X,Y>) other;
		return other_.x == this.x && other_.y == this.y;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y == null) ? 0 : y.hashCode());
		return result;
	}
	
	public X getFirst()
	{
		return x;
	}
	
	public Y getSecond()
	{
		return y;
	}
}
