package org.rockyang.jblock.client.rpc;

import org.rockyang.jblock.client.vo.req.KeyInfoReq;
import org.rockyang.jblock.client.vo.res.Cid;
import org.rockyang.jblock.client.vo.res.MessageStatusRes;
import org.rockyang.jblock.client.vo.res.SendMessageRes;
import org.rockyang.jblock.client.vo.res.WalletExportRes;
import retrofit2.Call;
import retrofit2.http.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * JBlock RPC service
 * @author yangjian
 */
public interface JBlockService {

	@GET("/wallet/new")
	Call<Map<String, String>> newWallet();

	@GET("/wallet/list")
	Call<Map<String, List>> walletList();

	@GET("/wallet/default")
	Call<Map<String, String>> getDefaultWallet();

	@GET("/wallet/export")
	Call<WalletExportRes> walletExport(@Query("address") String address);

	@POST("/wallet/import")
	@Multipart
	Call<Map<String, List>> walletImport(@Part("walletFile") KeyInfoReq keyInfoReq);

	@GET("/wallet/balance")
	Call<BigDecimal> getBalance(@Query("arg") String address);

	@POST("/message/send")
	Call<SendMessageRes> sendMessage(@Query("to") String to,
	                                 @Query("from") String from,
	                                 @Query("value") BigDecimal value);
	@GET("/message/get")
	Call<MessageStatusRes> getMessage(@Query("cid") String cid);


	@GET("/chain/head")
	Call<List<Cid>> chainHead();
}
