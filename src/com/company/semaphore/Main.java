package com.company.semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

	public static void main(String[] args) {
		UserStore userStore = new UserStore(3);

		ExecutorService executorService = Executors.newCachedThreadPool();

		for(int i = 1; i <= 5; i++) {
			String user = "user_" + i;
			sleep(1000);
			executorService.submit(() ->{
				userStore.login(user);
			});
		}

		for(int i = 1; i <= 2; i++) {
			String user = "user_" + i;
			sleep(1000);
			executorService.submit(() ->{
				userStore.logout(user);
			});
		}
	};

	public static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
}


