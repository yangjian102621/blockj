package org.rockyang.jblock.client.rpc;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.rockyang.jblock.base.model.Message;
import org.rockyang.jblock.base.model.Wallet;
import org.rockyang.jblock.base.vo.JsonVo;
import org.rockyang.jblock.client.exception.ApiError;
import org.rockyang.jblock.client.exception.ApiException;
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
		rpcService = retrofit.create(JBlockService.class);
	}

	/**
	 * Invoke the remote API Synchronously
	 *
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
	 *
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

	// create a new wallet
	public String newWallet()
	{
		JsonVo<Wallet> res = executeSync(rpcService.newWallet());
		if (res.isOK()) {
			return res.getData().getAddress();
		}
		return null;
	}

	// get wallets list
	public List<Wallet> walletList()
	{
		JsonVo<List<Wallet>> res = executeSync(rpcService.walletList());
		if (res.isOK()) {
			return res.getData();
		}
		return null;
	}

	public BigDecimal getBalance(String address)
	{
		JsonVo<BigDecimal> res = executeSync(rpcService.getBalance(address));
		return res.getData();
	}

	public String sendMessage(String from, String to, BigDecimal value, String param)
	{
		JsonVo<String> res = executeSync(rpcService.sendMessage(to, from, value, param));
		return res.getData();
	}

	public Message getMessage(String cid)
	{
		JsonVo<Message> res = executeSync(rpcService.getMessage(cid));
		if (res.isOK()) {
			return res.getData();
		}
		return null;
	}

	public Long chainHead()
	{
		JsonVo<Long> res = executeSync(rpcService.chainHead());
		if (res.isOK()) {
			return res.getData();
		}
		return null;
	}
}
