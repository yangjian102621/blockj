package org.rockyang.blockj.client.rpc.impl;

import com.alibaba.fastjson.JSON;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.rockyang.blockj.base.model.Message;
import org.rockyang.blockj.base.model.Peer;
import org.rockyang.blockj.base.model.Wallet;
import org.rockyang.blockj.base.vo.JsonVo;
import org.rockyang.blockj.client.exception.ApiError;
import org.rockyang.blockj.client.exception.ApiException;
import org.rockyang.blockj.client.rpc.BlockRpcService;
import org.rockyang.blockj.client.rpc.BlockService;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author yangjian
 */
public class BlockServiceImpl implements BlockService
{

    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static final Retrofit.Builder builder = new Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create());

    private static Retrofit retrofit;

    private static BlockRpcService rpcService;

    public BlockServiceImpl(String baseUrl, boolean debug)
    {
        // open debug log model
        if (debug) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(loggingInterceptor);
        }

        builder.baseUrl(baseUrl);
        builder.client(httpClient.build());
        builder.addConverterFactory(JacksonConverterFactory.create());
        retrofit = builder.build();
        rpcService = retrofit.create(BlockRpcService.class);
    }

    /**
     * Invoke the remote API Synchronously
     */
    public static <T> T executeSync(Call<T> call)
    {
        try {
            Response<T> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                ApiError apiError = getApiError(response);
                throw new ApiException(apiError);
            }
        } catch (IOException e) {
            throw new ApiException(e);
        }
    }

    /**
     * get api error message
     */
    private static ApiError getApiError(Response<?> response) throws IOException, ApiException
    {
        assert response.errorBody() != null;
        return (ApiError) retrofit.responseBodyConverter(ApiError.class, new Annotation[0]).convert(response.errorBody());
    }

    // create a new wallet
    public JsonVo<Wallet> newWallet()
    {
        return executeSync(rpcService.newWallet());
    }

    // get wallets list
    public JsonVo<List<Wallet>> walletList()
    {
        JsonVo res = executeSync(rpcService.walletList());
        if (res.isOK()) {
            String s = JSON.toJSONString(res.getData());
            List<Wallet> wallets = JSON.parseArray(s, Wallet.class);
            return new JsonVo<>(res.getCode(), wallets);
        } else {
            return new JsonVo<>(res.getCode(), res.getMessage());
        }
    }

    public JsonVo<BigDecimal> getBalance(String address)
    {
        return executeSync(rpcService.getBalance(address));
    }

    public JsonVo<String> sendMessage(String from, String to, BigDecimal value, String param)
    {
        return executeSync(rpcService.sendMessage(to, from, value, param));
    }

    public JsonVo<Message> getMessage(String cid)
    {
        return executeSync(rpcService.getMessage(cid));
    }

    public JsonVo<Long> chainHead()
    {
        return executeSync(rpcService.chainHead());
    }

    @Override
    public JsonVo<List<Peer>> netPeers()
    {
        JsonVo res = executeSync(rpcService.netPeers());
        if (res.isOK()) {
            String s = JSON.toJSONString(res.getData());
            List<Peer> peers = JSON.parseArray(s, Peer.class);
            return new JsonVo<>(res.getCode(), peers);
        } else {
            return new JsonVo<>(res.getCode(), res.getMessage());
        }
    }

    @Override
    public JsonVo<String> netListen()
    {
        return executeSync(rpcService.netListen());
    }

    @Override
    public JsonVo<String> netConnect(String peerAddress)
    {
        return executeSync(rpcService.netConnect(peerAddress));
    }
}
