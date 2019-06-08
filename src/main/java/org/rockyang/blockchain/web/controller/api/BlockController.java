package org.rockyang.blockchain.web.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.rockyang.blockchain.conf.AppConf;
import org.rockyang.blockchain.core.Block;
import org.rockyang.blockchain.core.BlockChain;
import org.rockyang.blockchain.core.Transaction;
import org.rockyang.blockchain.crypto.Credentials;
import org.rockyang.blockchain.db.DBAccess;
import org.rockyang.blockchain.net.base.Node;
import org.rockyang.blockchain.utils.JsonVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author yangjian
 * @since 2018-04-07 上午10:50.
 */
@RestController
@RequestMapping("/api/chain")
public class BlockController {

	@Autowired
	private DBAccess dbAccess;
	@Autowired
	private BlockChain blockChain;
	@Autowired
	private AppConf appConf;

	/**
	 * 启动挖矿
	 * @param request
	 * @return
	 */
	@PostMapping("/mining")
	public JsonVo mining(HttpServletRequest request) throws Exception
	{
		Block block = blockChain.mining();
		JsonVo vo = new JsonVo();
		vo.setCode(JsonVo.CODE_SUCCESS);
		vo.setMessage("Create a new block");
		vo.setItem(block);
		return vo;
	}

	/**
	 * 浏览头区块
	 * @param request
	 * @return
	 */
	@PostMapping("/block/head")
	public JsonVo blockHead(HttpServletRequest request)
	{

		Optional<Block> block = dbAccess.getLastBlock();
		JsonVo success = JsonVo.success();
		if (block.isPresent()) {
			success.setItem(block.get());
		}
		return success;

	}

	/**
	 * 发送交易
	 * @param txVo
	 * @return
	 */
	@PostMapping("/send_transactions")
	public JsonVo sendTransaction(@RequestBody JSONObject txVo) throws Exception {
		Preconditions.checkNotNull(txVo.get("to"), "Recipient is needed.");
		Preconditions.checkNotNull(txVo.get("amount"), "Amount is needed.");
		Preconditions.checkNotNull(txVo.get("priKey"), "Private Key is needed.");
		Credentials credentials = Credentials.create(txVo.getString("priKey"));
		Transaction transaction = blockChain.sendTransaction(
				credentials,
				txVo.getString("to"),
				txVo.getBigDecimal("amount"),
				txVo.getString("data"));

		//如果开启了自动挖矿，则直接自动挖矿
		if (appConf.isAutoMining()) {
			blockChain.mining();
		}
		JsonVo success = JsonVo.success();
		success.setItem(transaction);
		return success;
	}

	/**
	 * 添加节点
	 * @param node
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/node/add")
	public JsonVo addNode(@RequestBody JSONObject node) throws Exception {

		Preconditions.checkNotNull(node.getString("ip"), "server ip is needed.");
		Preconditions.checkNotNull(node.getInteger("port"), "server port is need.");

		blockChain.addNode(node.getString("ip"), node.getInteger("port"));
		return JsonVo.success();
	}

	/**
	 * 查看节点列表
	 * @param request
	 * @return
	 */
	@PostMapping("node/view")
	public JsonVo nodeList(HttpServletRequest request) {

		Optional<List<Node>> nodeList = dbAccess.getNodeList();
		JsonVo success = JsonVo.success();
		if (nodeList.isPresent()) {
			success.setItem(nodeList.get());
		}
		return success;
	}

}
