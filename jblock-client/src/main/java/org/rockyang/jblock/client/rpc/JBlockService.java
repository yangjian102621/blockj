package org.rockyang.jblock.client.rpc;

import org.rockyang.jblock.base.model.Account;
import org.rockyang.jblock.base.model.Message;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.math.BigDecimal;
import java.util.List;

/**
 * JBlock RPC service
 *
 * @author yangjian
 */
public interface JBlockService {

	@GET("/wallet/new")
	Call<String> newWallet();

	@GET("/wallet/list")
	Call<List<Account>> walletList();

	@GET("/wallet/balance")
	Call<BigDecimal> getBalance(@Query("arg") String address);

	@POST("/message/send")
	Call<String> sendMessage(
			@Query("to") String to,
			@Query("from") String from,
			@Query("value") BigDecimal value,
			@Query("data") String data);

	@GET("/message/get")
	Call<Message> getMessage(@Query("cid") String cid);


	@GET("/chain/head")
	Call<Long> chainHead();
}
