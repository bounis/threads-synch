package com.company.exchanger;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

class Producer implements Runnable {

	Exchanger<String> exchanger;

	public Producer(Exchanger<String> exchanger) {
		this.exchanger = exchanger;
	}

	@Override
	public void run() {
		for(int i = 0; i < 10; i++) {
			try {
				exchanger.exchange("producer_" + i);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class Consumer implements Runnable {

	Exchanger<String> exchanger;

	public Consumer(Exchanger<String> exchanger) {
		this.exchanger = exchanger;
	}

	@Override
	public void run() {
		for(int i = 0; i < 10; i++) {
			try {
				TimeUnit.SECONDS.sleep(2);
				String received = exchanger.exchange(null);
				System.out.println("received from producer ----- " + received);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
public class Main {

	public static void main(String[] args) {
		Exchanger<String> exchanger = new Exchanger<>();

		new Thread(new Producer(exchanger)).start();
		new Thread(new Consumer(exchanger)).start();
	}
}
