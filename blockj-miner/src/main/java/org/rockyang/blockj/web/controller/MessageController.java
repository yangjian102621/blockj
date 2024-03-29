package org.rockyang.blockj.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.rockyang.blockj.base.model.Message;
import org.rockyang.blockj.base.model.Wallet;
import org.rockyang.blockj.base.vo.JsonVo;
import org.rockyang.blockj.chain.MessagePool;
import org.rockyang.blockj.service.MessageService;
import org.rockyang.blockj.service.WalletService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @author yangjian
 */
@RestController
@RequestMapping("/message")
public class MessageController
{

    private final MessageService messageService;
    private final MessagePool messagePool;
    private final WalletService walletService;

    public MessageController(
            MessageService messageService,
            MessagePool messagePool,
            WalletService walletService)
    {
        this.messageService = messageService;
        this.messagePool = messagePool;
        this.walletService = walletService;
    }

    @GetMapping("/get/{cid}")
    public JsonVo getMessage(@PathVariable String cid)
    {
        if (StringUtils.isBlank(cid)) {
            return new JsonVo(JsonVo.FAIL, "Must pass message cid");
        }

        // 1. search message in leveldb
        // 2. search message in message pool
        Message message = messageService.getMessage(cid);
        if (message == null) {
            message = messagePool.getMessage(cid);
        }

        if (message == null) {
            return new JsonVo(JsonVo.FAIL, "No message found");
        } else {
            return new JsonVo<>(JsonVo.SUCCESS, message);
        }
    }

    @PostMapping("/send")
    public JsonVo sendMessage(
            @RequestParam("from") String from,
            @RequestParam("to") String to,
            @RequestParam("value") BigDecimal value,
            @RequestParam(value = "param", required = false) String param
    ) throws Exception
    {
        // 如果没有传入 from 地址，则使用默认的钱包地址
        if (StringUtils.isBlank(from)) {
            Wallet defaultWallet = walletService.getDefaultWallet();
            if (defaultWallet == null) {
                return new JsonVo(JsonVo.FAIL, "default wallet is not set, you need to pass the from address");
            }
            from = defaultWallet.getAddress();
        }

        if (StringUtils.isBlank(to)) {
            new JsonVo(JsonVo.FAIL, "must pass the to address");
        }

        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            return new JsonVo(JsonVo.FAIL, "the value of send amount must > 0");
        }

        String cid = messageService.sendMessage(from, to, value, param);
        return new JsonVo<>(JsonVo.SUCCESS, "", cid);
    }
}
