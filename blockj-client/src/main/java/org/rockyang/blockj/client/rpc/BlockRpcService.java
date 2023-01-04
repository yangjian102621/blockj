package org.rockyang.blockj.client.rpc;

import org.rockyang.blockj.base.model.Block;
import org.rockyang.blockj.base.model.Message;
import org.rockyang.blockj.base.model.Wallet;
import org.rockyang.blockj.base.vo.JsonVo;
import org.rockyang.blockj.base.vo.MnemonicWallet;
import retrofit2.Call;
import retrofit2.http.*;

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

    @FormUrlEncoded
    @POST("/wallet/new/mnemonic")
    Call<JsonVo<MnemonicWallet>> newMnemonicWallet(@Field("password") String password);

    @GET("/wallet/list")
    Call<JsonVo> walletList();

    @GET("/wallet/balance/{address}")
    Call<JsonVo<BigDecimal>> getBalance(@Path("address") String address);

    @FormUrlEncoded
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

    @GET("/chain/block/{height}")
    Call<JsonVo<Block>> getBlock(@Path("height") Long height);

    @GET("/net/peers")
    Call<JsonVo> netPeers();

    @GET("/net/listen")
    Call<JsonVo<String>> netListen();

    @GET("/net/connect")
    Call<JsonVo<String>> netConnect(@Field("address") String address);


}
