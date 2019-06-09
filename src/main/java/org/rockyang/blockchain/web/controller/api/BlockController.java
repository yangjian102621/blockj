package org.rockyang.blockchain.web.controller.api;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.rockyang.blockchain.conf.AppConfig;
import org.rockyang.blockchain.core.Block;
import org.rockyang.blockchain.core.BlockChain;
import org.rockyang.blockchain.db.DBAccess;
import org.rockyang.blockchain.net.base.Node;
import org.rockyang.blockchain.utils.JsonVo;
import org.rockyang.blockchain.web.vo.req.NodeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author yangjian
 * @since 2018-04-07 上午10:50.
 */
@RestController
@RequestMapping("/api/chain")
@Api(tags = "Chain API", description = "区块链相关的 API")
public class BlockController {

	@Autowired
	private DBAccess dbAccess;
	@Autowired
	private BlockChain blockChain;
	@Autowired
	private AppConfig appConfig;

	/**
	 * 启动挖矿
	 * @return
	 */
	@ApiOperation(value="启动挖矿")
	@GetMapping("/mining")
	public JsonVo mining() throws Exception
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
	@ApiOperation(value="浏览头区块信息", notes="获取最新的区块信息")
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
	 * 添加节点
	 * @param node
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="添加节点", notes="添加并连接一个节点")
	@ApiImplicitParam(name = "node", required = true, dataType = "NodeVo")
	@PostMapping("/node/add")
	public JsonVo addNode(@RequestBody NodeVo node) throws Exception {

		Preconditions.checkNotNull(node.getIp(), "server ip is needed.");
		Preconditions.checkNotNull(node.getPort(), "server port is need.");

		blockChain.addNode(node.getIp(), node.getPort());
		return JsonVo.success();
	}

	/**
	 * 查看节点列表
	 * @return
	 */
	@ApiOperation(value="获取节点列表", notes="获取当前接连相连接的所有节点")
	@PostMapping("node/view")
	public JsonVo nodeList()
	{
		Optional<List<Node>> nodeList = dbAccess.getNodeList();
		JsonVo success = JsonVo.success();
		if (nodeList.isPresent()) {
			success.setItem(nodeList.get());
		}
		return success;
	}

}
