package com.example.java8inaction.ch08;

import java.util.stream.Stream;
import static java.util.stream.Collectors.toList;

public class Peek {
	public static void main(String[] args) {
		Stream.of(2, 3, 4, 5).peek(x -> System.out.println("taking from stream: " + x)).map(x -> x + 17)
				.peek(x -> System.out.println("after map: " + x)).filter(x -> x % 2 == 0)
				.peek(x -> System.out.println("after filter: " + x)).limit(3)
				.peek(x -> System.out.println("after limit: " + x)).collect(toList());
	}
}