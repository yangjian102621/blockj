package org.rockyang.blockj.client.rpc;

import org.rockyang.blockj.base.model.Message;
import org.rockyang.blockj.base.model.Wallet;
import org.rockyang.blockj.base.vo.JsonVo;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.math.BigDecimal;

/**
 * blockj RPC service interface
 *
 * @author yangjian
 */
public interface BlockRpcService
{

    @GET("/wallet/new")
    Call<JsonVo<Wallet>> newWallet();

    @GET("/wallet/list")
    Call<JsonVo> walletList();

    @POST("/wallet/balance/{address}")
    Call<JsonVo<BigDecimal>> getBalance(@Path("address") String address);

    @POST("/message/send")
    Call<JsonVo<String>> sendMessage(
            @Field("to") String to,
            @Field("from") String from,
            @Field("value") BigDecimal value,
            @Field("data") String data);

    @GET("/message/get/{cid}")
    Call<JsonVo<Message>> getMessage(@Path("cid") String cid);

    @GET("/chain/head")
    Call<JsonVo<Long>> chainHead();

    @GET("/chain/block/get")
    Call<JsonVo<Long>> getBlock(
            @Field("hash") String hash,
            @Field("height") Long height
    );

    @GET("/net/peers")
    Call<JsonVo> netPeers();

    @GET("/net/listen")
    Call<JsonVo<String>> netListen();

    @GET("/net/connect")
    Call<JsonVo<String>> netConnect(@Field("address") String address);


}
