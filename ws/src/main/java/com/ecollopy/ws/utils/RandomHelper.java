package com.ecollopy.ws.utils;

import java.util.Random;

import com.ecollopy.ws.utils.WordProps.Alignment;
import com.ecollopy.ws.utils.WordProps.Order;

public class RandomHelper {

	private static class RandomSelect<E extends Enum<?>>
	{
		private static final Random RND = new Random();
		private final E[] values;
		
		public RandomSelect(Class<E> token) {
			values = token.getEnumConstants();
		}
		
		public E random() {
			return values[RND.nextInt(values.length)];
		}
	}
	
	private static final RandomSelect<Alignment> r = new RandomSelect<Alignment>(Alignment.class);
	
	public static Alignment setAlignment()
	{
		return r.random();
	}
	
	private static final RandomSelect<Order> rs = new RandomSelect<Order>(Order.class);
	
	public static Order setOrder()
	{
		return rs.random();
	}
}
