package com.aizone.blockchain.db;

import com.aizone.blockchain.Application;
import com.aizone.blockchain.net.base.Node;
import com.google.common.base.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.UUID;

/**
 * @author yangjian
 * @since 18-4-10
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class DBUtilsTest {

	static Logger logger = LoggerFactory.getLogger(DBUtilsTest.class);

	static final String KEY = "test-data";

	@Autowired
	private DBAccess dbAccess;

	@Test
	public void main() throws Exception {
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
	public void clearNodes() {

		dbAccess.clearNodes();
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
