package com.company.waitnotify;

import java.util.concurrent.atomic.AtomicBoolean;

class PrintHelloWorld {

	int n;
	AtomicBoolean state = new AtomicBoolean(); //false print hello, true world
	public PrintHelloWorld(int n) {
		this.n = n;
	}


	void printHello() throws InterruptedException {
		for(int i = 0; i < n; i++) {
			synchronized(this) {
				while(state.get()) {
					this.wait();
				}
				System.out.println(Thread.currentThread().getName() + " hello");
				state.set(!state.get());
				this.notify();
			}

		}
	}

	void printWorld() throws InterruptedException {
		for(int i = 0; i < n; i++) {
			synchronized(this) {
				while(!state.get()) {
					this.wait();
				}
				System.out.println(Thread.currentThread().getName() + " world");
				state.set(!state.get());
				this.notify();
			}

		}
	}
}
public class Main {

	public static void main(String[] args) {

		PrintHelloWorld printHelloWorld = new PrintHelloWorld(5);

		Runnable runnable1 = () -> {
			try {
				printHelloWorld.printHello();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		};
		Runnable runnable2 = () -> {
			try {
				printHelloWorld.printWorld();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		};

		new Thread(runnable1).start();
		new Thread(runnable2).start();
	}
}
