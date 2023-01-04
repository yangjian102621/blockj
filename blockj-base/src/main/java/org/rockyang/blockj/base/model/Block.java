package org.rockyang.blockj.base.model;

import org.rockyang.blockj.base.crypto.Hash;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Block
 *
 * @author yangjian
 */
public class Block implements Serializable
{

    private BlockHeader header;
    // block message
    private List<Message> messages;
    // public key of miner
    private String pubKey;
    // signature for current block
    private String blockSign;

    public Block(List<Message> messages)
    {
        this.messages = messages;
    }

    public Block(BlockHeader blockHeader)
    {
        this.header = blockHeader;
        this.messages = new CopyOnWriteArrayList<>();
    }

    public Block()
    {
    }

    public List<Message> getMessages()
    {
        return messages;
    }

    public void addMessage(Message message)
    {
        messages.add(message);
    }

    public void setMessages(List<Message> messages)
    {
        this.messages = messages;
    }

    public String getBlockSign()
    {
        return blockSign;
    }

    public void setBlockSign(String blockSign)
    {
        this.blockSign = blockSign;
    }

    public BlockHeader getHeader()
    {
        return header;
    }

    public void setHeader(BlockHeader header)
    {
        this.header = header;
    }

    public String getPubKey()
    {
        return pubKey;
    }

    public void setPubKey(String pubKey)
    {
        this.pubKey = pubKey;
    }

    private String buildMessages()
    {
        StringBuilder builder = new StringBuilder();
        messages.forEach(message -> {
            builder.append(message.getCid());
        });
        return builder.toString();
    }

    // generate block content id
    public String genCid()
    {
        return Hash.sha3(header.genCid());
    }

    @Override
    public String toString()
    {
        return "Block{" +
                "header=" + header +
                ", messages=" + messages +
                ", blockSign='" + blockSign + '\'' +
                '}';
    }
}
