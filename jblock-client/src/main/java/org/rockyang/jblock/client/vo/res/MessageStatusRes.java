package org.rockyang.jblock.client.vo.res;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * query message status response VO
 * @author yangjian
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class MessageStatusRes {

	// 是否在待打包交易池
	@JsonProperty("InPool")
	private boolean inPool;

	// 交易池信息
	@JsonProperty("PoolMsg")
	private MessageExt poolMsg;

	@JsonProperty("InOutbox")
	private boolean inOutbox;

	// 交易是否上链
	@JsonProperty("OnChain")
	private boolean onChain;

	@JsonProperty("ChainMsg")
	private ChainMsg chainMsg;

	public boolean isInPool() {
		return inPool;
	}

	public void setInPool(boolean inPool) {
		this.inPool = inPool;
	}

	public MessageExt getPoolMsg() {
		return poolMsg;
	}

	public void setPoolMsg(MessageExt poolMsg) {
		this.poolMsg = poolMsg;
	}

	public boolean isInOutbox() {
		return inOutbox;
	}

	public void setInOutbox(boolean inOutbox) {
		this.inOutbox = inOutbox;
	}

	public boolean isOnChain() {
		return onChain;
	}

	public void setOnChain(boolean onChain) {
		this.onChain = onChain;
	}

	public ChainMsg getChainMsg() {
		return chainMsg;
	}

	public void setChainMsg(ChainMsg chainMsg) {
		this.chainMsg = chainMsg;
	}

	/**
	 * 交易上链信息
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class ChainMsg {

		@JsonProperty("Message")
		private MessageExt message;

		@JsonProperty("Block")
		private Block block;

		@JsonProperty("Receipt")
		private MessageReceipt receipt;

		public MessageExt getMessage() {
			return message;
		}

		public void setMessage(MessageExt message) {
			this.message = message;
		}

		public Block getBlock() {
			return block;
		}

		public void setBlock(Block block) {
			this.block = block;
		}

		public MessageReceipt getReceipt() {
			return receipt;
		}

		public void setReceipt(MessageReceipt receipt) {
			this.receipt = receipt;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class MessageExt {
		private MeteredMessage meteredMessage;
		private String signature;

		public MeteredMessage getMeteredMessage() {
			return meteredMessage;
		}

		public void setMeteredMessage(MeteredMessage meteredMessage) {
			this.meteredMessage = meteredMessage;
		}

		public String getSignature() {
			return signature;
		}

		public void setSignature(String signature) {
			this.signature = signature;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class MeteredMessage {

		private Message message;
		private BigDecimal gasPrice;
		private Integer gasLimit;

		public Message getMessage() {
			return message;
		}

		public void setMessage(Message message) {
			this.message = message;
		}

		public BigDecimal getGasPrice() {
			return gasPrice;
		}

		public void setGasPrice(BigDecimal gasPrice) {
			this.gasPrice = gasPrice;
		}

		public Integer getGasLimit() {
			return gasLimit;
		}

		public void setGasLimit(Integer gasLimit) {
			this.gasLimit = gasLimit;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class Message {

		private String to;
		private String from;
		private BigDecimal value;
		private Integer nonce;
		private String method;
		private String params;
		@JsonIgnore
		private boolean success;

		public String getTo() {
			return to;
		}

		public void setTo(String to) {
			this.to = to;
		}

		public String getFrom() {
			return from;
		}

		public void setFrom(String from) {
			this.from = from;
		}

		public BigDecimal getValue() {
			return value;
		}

		public void setValue(BigDecimal value) {
			this.value = value;
		}

		public Integer getNonce() {
			return nonce;
		}

		public void setNonce(Integer nonce) {
			this.nonce = nonce;
		}

		public String getMethod() {
			return method;
		}

		public void setMethod(String method) {
			this.method = method;
		}

		public String getParams() {
			return params;
		}

		public void setParams(String params) {
			this.params = params;
		}

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		@Override
		public String toString() {
			return "Message{" +
					"to='" + to + '\'' +
					", from='" + from + '\'' +
					", value=" + value +
					", nonce=" + nonce +
					", method='" + method + '\'' +
					", params=" + params +
					", success=" + success +
					'}';
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class Block {

		private String miner;
		private List<Ticket> tickets;
		private List<Cid> parents;
		private BigInteger parentWeight;
		private Integer height;
		private Integer nonce;
		private Cid messages;
		private Cid stateRoot;
		private Cid messageReceipts;
		private Object proof;
		private Integer timestamp;
		private String blocksig;

		public String getMiner() {
			return miner;
		}

		public void setMiner(String miner) {
			this.miner = miner;
		}

		public List<Ticket> getTickets()
		{
			return tickets;
		}

		public void setTickets(List<Ticket> tickets)
		{
			this.tickets = tickets;
		}

		public List<Cid> getParents() {
			return parents;
		}

		public void setParents(List<Cid> parents) {
			this.parents = parents;
		}

		public BigInteger getParentWeight() {
			return parentWeight;
		}

		public void setParentWeight(BigInteger parentWeight) {
			this.parentWeight = parentWeight;
		}

		public Integer getHeight() {
			return height;
		}

		public void setHeight(Integer height) {
			this.height = height;
		}

		public Integer getNonce() {
			return nonce;
		}

		public void setNonce(Integer nonce) {
			this.nonce = nonce;
		}

		public Cid getMessages()
		{
			return messages;
		}

		public void setMessages(Cid messages)
		{
			this.messages = messages;
		}

		public Cid getStateRoot() {
			return stateRoot;
		}

		public void setStateRoot(Cid stateRoot) {
			this.stateRoot = stateRoot;
		}

		public Cid getMessageReceipts()
		{
			return messageReceipts;
		}

		public void setMessageReceipts(Cid messageReceipts)
		{
			this.messageReceipts = messageReceipts;
		}

		public Object getProof() {
			return proof;
		}

		public void setProof(Object proof) {
			this.proof = proof;
		}

		public Integer getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(Integer timestamp) {
			this.timestamp = timestamp;
		}

		public String getBlocksig()
		{
			return blocksig;
		}

		public void setBlocksig(String blocksig)
		{
			this.blocksig = blocksig;
		}
	}
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class Ticket {
		private String VRFProof;
		private String VDFResult;
		private String VDFProof;

		public String getVRFProof()
		{
			return VRFProof;
		}

		public void setVRFProof(String VRFProof)
		{
			this.VRFProof = VRFProof;
		}

		public String getVDFResult()
		{
			return VDFResult;
		}

		public void setVDFResult(String VDFResult)
		{
			this.VDFResult = VDFResult;
		}

		public String getVDFProof()
		{
			return VDFProof;
		}

		public void setVDFProof(String VDFProof)
		{
			this.VDFProof = VDFProof;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class MessageReceipt {
		private Integer exitCode;
		@JsonProperty("return")
		private Object ret;
		private BigDecimal gasAttoFIL;

		public Integer getExitCode() {
			return exitCode;
		}

		public void setExitCode(Integer exitCode) {
			this.exitCode = exitCode;
		}

		public Object getRet() {
			return ret;
		}

		public void setRet(Object ret) {
			this.ret = ret;
		}

		public BigDecimal getGasAttoFIL() {
			return gasAttoFIL;
		}

		public void setGasAttoFIL(BigDecimal gasAttoFIL) {
			this.gasAttoFIL = gasAttoFIL;
		}
	}
}
