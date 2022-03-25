package org.rockyang.jblock.db;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.rockyang.jblock.Application;
import org.rockyang.jblock.net.base.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author yangjian
 * @since 18-4-10
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class RocksDbTest {

	static Logger logger = LoggerFactory.getLogger(RocksDbTest.class);

	static final String KEY = "test-data";

	@Autowired
	private DBAccess dbAccess;

	@Test
	public void put() {
		//put data
		String data = UUID.randomUUID().toString();
		dbAccess.put(KEY, data);
		//get data by key
		Optional<Object> o = dbAccess.get(KEY);
		if (o.isPresent()) {
			String s = (String) o.get();
			logger.info(s);
			assert data.equals(s);
		}
	}

	@Test
	public void addNode() {
		Node node = new Node("127.0.0.1", 6789);
		dbAccess.addNode(node);
		Optional<List<Node>> nodeList = dbAccess.getNodeList();
		if (nodeList.isPresent()) {
			System.out.println(nodeList.get());
		}
	}
}
