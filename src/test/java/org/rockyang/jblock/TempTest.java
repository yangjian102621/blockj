package org.rockyang.jblock;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 临时测试文件，测试各种其他测试代码
 * @author yangjian
 * @since 2018-04-07 下午8:38.
 */
public class TempTest {

	@Test
	public void run() {

		List<User> list = new ArrayList<>();
		list.add(new User("zhangsan"));
		list.add(new User("lisi"));
		list.add(new User("wangmazi"));

		for (User user : list) {
			user.setName("rock");
		}

		System.out.println(list);

	}

	private class User {

		private String name;

		public User(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "User{" +
					"name='" + name + '\'' +
					'}';
		}
	}



}
