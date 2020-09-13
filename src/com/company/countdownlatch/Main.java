package com.company.countdownlatch;


import javax.swing.text.StyledEditorKit.BoldAction;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class DatabaseChecker implements Runnable {

	CountDownLatch countDownLatch;

	Map<String, Boolean> servicesState = new HashMap<>();

	public DatabaseChecker(CountDownLatch countDownLatch, Map<String, Boolean> servicesState) {
		this.countDownLatch = countDownLatch;
		this.servicesState = servicesState;
	}

	@Override
	public void run() {
		try {
			System.out.println("checking database ...");
			TimeUnit.SECONDS.sleep(4);
			System.out.println("database is up");
			servicesState.put("databaseService", true);
		} catch(Exception e) {
			System.out.println("database is down");
			servicesState.put("databaseService", false);
			e.printStackTrace();
		} finally {
			countDownLatch.countDown();
		}
	}
}

class FileSystemChecker implements Runnable {

	CountDownLatch countDownLatch;

	Map<String, Boolean> servicesState = new HashMap<>();

	public FileSystemChecker(CountDownLatch countDownLatch,
	                         Map<String, Boolean> servicesState) {
		this.countDownLatch = countDownLatch;
		this.servicesState = servicesState;
	}

	@Override
	public void run() {
		try {
			System.out.println("checking filesystem ...");
			TimeUnit.SECONDS.sleep(4);
//			System.out.println("filesystem is up");
//			servicesState.put("filesystemService", true);
			throw new RuntimeException();
		} catch(Exception e) {
			System.out.println("filesystem is down");
			servicesState.put("filesystemService", false);
			e.printStackTrace();
		} finally {
			countDownLatch.countDown();
		}
	}
}
public class Main {

	public static void main(String[] args) throws InterruptedException {
		CountDownLatch countDownLatch = new CountDownLatch(2);
		Map<String, Boolean> servicesState = new HashMap<>();
		new Thread(new DatabaseChecker(countDownLatch, servicesState)).start();
		new Thread(new FileSystemChecker(countDownLatch, servicesState)).start();

		countDownLatch.await();

		boolean allUp = true;
		for(String s : servicesState.keySet()) {
			if (!servicesState.get(s)) {
				allUp = false;
				break;
			}
		}

		if (allUp) {
			System.out.println("all services are up, start app with success");
		} else {
			throw new RuntimeException("some services are down, can not start app");
		}

	}
}
