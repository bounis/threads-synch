package com.company.semaphore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

public class UserStore {

	private static final List<String> users = Collections.synchronizedList(new ArrayList<>());
	Semaphore semaphore;

	public UserStore(int n) {
		this.semaphore = new Semaphore(n);
	}

	void login (String user) {
			boolean added = false;
		try {
			semaphore.acquire();
			added = users.add(user);
			System.out.println("login " + user);
		} catch(InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (!added) {
				semaphore.release();
			}
		}
	}

	void logout(String user) {
		boolean removed = users.remove(user);
		System.out.println("logout " + user);
		if (removed) {
			semaphore.release();
		}
	}
}
