package com.company.cyclicbarrier;

import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

	public static void main(String[] args) {
		int listSize = 2000;
		int numberToFind = 5;
		AtomicLong firstHalfCount = new AtomicLong();
		AtomicLong secondHalfCount = new AtomicLong();
		Random random = new Random();
		List<Integer> list = IntStream.range(0, listSize)
		                                 .map(operand -> random.nextInt(10))
		                                 .boxed()
		                                 .collect(Collectors.toList());

		Runnable barrierAction = () -> {
			long globalCount = firstHalfCount.get() + secondHalfCount.get();
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("finish global count");
			System.out.println("number of occurence is " + globalCount);
		};
		CyclicBarrier cyclicBarrier = new CyclicBarrier(2, barrierAction);

		Runnable firstHalfRunnable = () -> {

			try {
				firstHalfCount.getAndAdd(list.stream()
				    .limit(listSize/2)
				    .filter(i -> i == numberToFind)
				    .count());
				System.out.println("finish search in first half ...");
				cyclicBarrier.await();
			} catch(InterruptedException|BrokenBarrierException e) {
				e.printStackTrace();
			}

			System.out.println("release first thread...");
		};

		Runnable secondHalfRunnable = () -> {

			try {
				secondHalfCount.getAndAdd(list.stream()
				                             .skip(listSize/2)
				                             .filter(i -> i == numberToFind)
				                             .count());
				System.out.println("finish search in second half ...");
				cyclicBarrier.await();
			} catch(InterruptedException|BrokenBarrierException e) {
				e.printStackTrace();
			}

			System.out.println("release second thread...");
		};

		new Thread(firstHalfRunnable).start();
		new Thread(secondHalfRunnable).start();
	}
}
