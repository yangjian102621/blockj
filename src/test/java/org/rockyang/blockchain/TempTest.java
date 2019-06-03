package org.rockyang.blockchain;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 临时测试文件，测试各种其他测试代码
 * @author yangjian
 * @since 2018-04-07 下午8:38.
 */
public class TempTest {

	@Test
	public void run() {
		List<C> list1 = new ArrayList<>();
		List<C> list2 = new ArrayList<>();
		list1.add(new C(1));
		list1.add(new C(2));
		list1.add(new C(3));
		list1.add(new C(4));

		for (Iterator i = list1.iterator(); i.hasNext();) {
			list2.add((C) i.next());
			i.remove();
		}

		System.out.println(list1);
		System.out.println(list2);
	}

	private class C {
		int value;

		C(int v) {
			this.value = v;
		}

		@Override
		public String toString() {
			return "C{" +
					"value=" + value +
					'}';
		}
	}


}
