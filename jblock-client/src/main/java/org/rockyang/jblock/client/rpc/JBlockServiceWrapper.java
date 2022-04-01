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

	public JBlockServiceWrapper(String baseUrl, boolean debug)
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
		assert response.errorBody() != null;
		return (ApiError) retrofit.responseBodyConverter(ApiError.class, new Annotation[0]).convert(response.errorBody());
	}

	/**
	 * get a new address
	 * @return
	 */
	public String newWallet()
	{
		Map<String, String> map = executeSync(rpcService.newWallet());
		return map.get("Address");
	}

	/**
	 * get all addresses of the filecoin daemon node
	 * @return
	 */
	public List walletList()
	{
		Map<String, List> map = executeSync(rpcService.walletList());
		return map.get("Addresses");
	}

	public String getDefaultWallet()
	{
		Map<String, String> map = executeSync(rpcService.getDefaultWallet());
		return map.get("Address");
	}

	public KeyInfo walletExport(String address)
	{
		WalletExportRes res = executeSync(rpcService.walletExport(address));
		KeyInfo keyInfo = res.getKeyInfo().get(0);
		keyInfo.setAddress(address);
		return keyInfo;
	}

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

	public BigDecimal getBalance(String address)
	{
		return executeSync(rpcService.getBalance(address));
	}

	public String sendMessage(String from, String to, BigDecimal value)
	{
		SendMessageRes res = executeSync(rpcService.sendMessage(to, from, value));
		return res.getCid().getRoot();
	}

	public MessageStatusRes getMessage(String cid)
	{
		MessageStatusRes res = executeSync(rpcService.getMessage(cid));
		if (res.isOnChain()) {
			res.getChainMsg().getMessage().getMeteredMessage().getMessage().setSuccess(true);
		} else {
			res.getChainMsg().getMessage().getMeteredMessage().getMessage().setSuccess(false);
		}
		return res;
	}

	public String chainHead()
	{
		List<Cid> cids = executeSync(rpcService.chainHead());
		return cids.get(0).getRoot();
	}
}
