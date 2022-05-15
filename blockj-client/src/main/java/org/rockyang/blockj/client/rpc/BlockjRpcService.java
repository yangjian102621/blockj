package org.rockyang.blockj.client.rpc;

import org.rockyang.blockj.base.model.Message;
import org.rockyang.blockj.base.model.Wallet;
import org.rockyang.blockj.base.vo.JsonVo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.math.BigDecimal;
import java.util.List;

/**
 * blockj RPC service interface
 *
 * @author yangjian
 */
public interface BlockjRpcService {

	@GET("/wallet/new")
	Call<JsonVo<Wallet>> newWallet();

	@GET("/wallet/list")
	Call<JsonVo<List<Wallet>>> walletList();

	@GET("/wallet/balance")
	Call<JsonVo<BigDecimal>> getBalance(@Query("arg") String address);

	@POST("/message/send")
	Call<JsonVo<String>> sendMessage(
			@Query("to") String to,
			@Query("from") String from,
			@Query("value") BigDecimal value,
			@Query("data") String data);

	@GET("/message/get")
	Call<JsonVo<Message>> getMessage(@Query("cid") String cid);


	@GET("/chain/head")
	Call<JsonVo<Long>> chainHead();
}
