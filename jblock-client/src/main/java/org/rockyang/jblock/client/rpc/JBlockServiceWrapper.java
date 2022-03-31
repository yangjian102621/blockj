package org.rockyang.jblock.client.rpc;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.rockyang.jblock.client.exception.ApiError;
import org.rockyang.jblock.client.exception.ApiException;
import org.rockyang.jblock.client.vo.req.KeyInfoReq;
import org.rockyang.jblock.client.vo.res.*;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author yangjian
 */
public class JBlockServiceWrapper {

	private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

	private static final Retrofit.Builder builder = new Retrofit.Builder()
			.addConverterFactory(JacksonConverterFactory.create());

	private static Retrofit retrofit;

	private static JBlockService rpcService;

	public JBlockServiceWrapper(String baseUrl, boolean logDebug)
	{
		// open debug log model
		if (logDebug) {
			HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
			loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
			httpClient.addInterceptor(loggingInterceptor);
		}

		builder.baseUrl(baseUrl);
		builder.client(httpClient.build());
		builder.addConverterFactory(JacksonConverterFactory.create());
		retrofit = builder.build();
		rpcService =  retrofit.create(JBlockService.class);
	}

	/**
	 * Invoke the remote API Synchronously
	 * @param call
	 * @param <T>
	 * @return
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
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ApiException
	 */
	private static ApiError getApiError(Response<?> response) throws IOException, ApiException
	{
		return (ApiError) retrofit.responseBodyConverter(ApiError.class, new Annotation[0]).convert(response.errorBody());
	}

	/**
	 * get a new address
	 * @return
	 */
	public String newAddress()
	{
		Map<String, String> map = executeSync(rpcService.newAddress());
		return map.get("Address");
	}

	/**
	 * get all addresses of the filecoin daemon node
	 * @return
	 */
	public List<String> getAddressList()
	{
		Map<String, List> map = executeSync(rpcService.getAddressList());
		return map.get("Addresses");
	}

	/**
	 * get default address of node
	 * @return
	 */
	public String getDefaultAddress()
	{
		Map<String, String> map = executeSync(rpcService.getDefaultAddress());
		return map.get("Address");
	}

	/**
	 * export the specified wallet by address
	 * @param address
	 * @return
	 */
	public KeyInfo walletExport(String address)
	{
		WalletExportRes res = executeSync(rpcService.walletExport(address));
		KeyInfo keyInfo = res.getKeyInfo().get(0);
		keyInfo.setAddress(address);
		return keyInfo;
	}

	/**
	 * import wallet use private
	 * @param privateKey
	 * @return
	 */
	public String walletImport(String privateKey)
	{
		KeyInfoReq keyInfoReq = new KeyInfoReq();
		KeyInfoReq.KeyInfo keyInfo = new KeyInfoReq.KeyInfo(privateKey, "secp256k1");
		keyInfoReq.addKeyInfo(keyInfo);
		Map<String, List> map = executeSync(rpcService.walletImport(keyInfoReq));
		if (map.get("Addresses") == null) {
			return null;
		}
		return map.get("Addresses").get(0).toString();
	}

	/**
	 * get balance of specified address
	 * @return
	 */
	public BigDecimal getBalance(String address)
	{
		return executeSync(rpcService.getBalance(address));
	}

	/**
	 * send a transfer message
	 * @param from
	 * @param to
	 * @param value
	 * @param gasPrice
	 * @param gasLimit
	 * @return
	 */
	public String sendTransaction(String from, String to, BigDecimal value, BigDecimal gasPrice, Integer gasLimit)
	{
		SendMessageRes res = executeSync(rpcService.sendMessage(to, from, value, gasPrice, gasLimit));
		return res.getCid().getRoot();
	}

	/**
	 * get transaction status by Cid
	 * @param cid
	 * @return
	 */
	public MessageStatusRes.Message getTransaction(String cid)
	{
		MessageStatusRes res = executeSync(rpcService.getMessageStatus(cid));
		MessageStatusRes.Message message;
		if (res.isOnChain()) {
			message = res.getChainMsg().getMessage().getMeteredMessage().getMessage();
			message.setSuccess(true);
		} else {
			message = res.getPoolMsg().getMeteredMessage().getMessage();
			message.setSuccess(false);
		}
		return message;
	}

	/**
	 * query message status
	 * @param cid
	 * @return
	 */
	public MessageStatusRes getMessageStatus(String cid)
	{
		MessageStatusRes res = executeSync(rpcService.getMessageStatus(cid));
		if (res.isOnChain()) {
			res.getChainMsg().getMessage().getMeteredMessage().getMessage().setSuccess(true);
		} else {
			res.getChainMsg().getMessage().getMeteredMessage().getMessage().setSuccess(false);
		}
		return res;
	}

	/**
	 * get configuration of daemon
	 * @param key
	 * @return
	 */
	public Object config(String key)
	{
		return executeSync(rpcService.config(key));
	}

	/**
	 * update configuration of daemon
	 * @param key
	 * @param value
	 */
	public void config(String key, Object value)
	{
		Object[] params = new Object[2];
		params[0] = key;
		params[1] = value;
		executeSync(rpcService.config(params));
	}

	public String chainHead()
	{
		List<Cid> cids = executeSync(rpcService.chainHead());
		return cids.get(0).getRoot();
	}
}
