package org.rockyang.blockj.web.controller;

import org.rockyang.blockj.base.model.Account;
import org.rockyang.blockj.base.model.Wallet;
import org.rockyang.blockj.base.vo.JsonVo;
import org.rockyang.blockj.service.AccountService;
import org.rockyang.blockj.service.WalletService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yangjian
 */
@RestController
@RequestMapping("/wallet")
public class WalletController
{

    private final WalletService walletService;
    private final AccountService accountService;

    public WalletController(WalletService walletService, AccountService accountService)
    {
        this.walletService = walletService;
        this.accountService = accountService;
    }

    @RequestMapping("/new")
    public JsonVo<Wallet> newWallet() throws Exception
    {
        Wallet wallet = new Wallet();
        walletService.addWallet(wallet);
        return new JsonVo<>(JsonVo.SUCCESS, wallet);
    }

    @RequestMapping("/new/memo")
    public JsonVo newMnemonicWallet()
    {

        throw new RuntimeException("Not implemented");
    }

    @RequestMapping("/list")
    public JsonVo walletList()
    {
        // @Note: we only return the local wallet infos
        // should not to export the private key
        List<Wallet> wallets = walletService.getWallets();
        if (wallets.size() == 0) {
            return new JsonVo(JsonVo.FAIL, "No wallet found");
        }

        for (Wallet w : wallets) {
            Account account = accountService.getAccount(w.getAddress());
            if (account != null) {
                w.setBalance(account.getBalance());
                w.setMessageNonce(account.getMessageNonce());
            }
        }
        return new JsonVo<>(JsonVo.SUCCESS, wallets);
    }

    @RequestMapping("/balance/{address}")
    public JsonVo walletBalance(@PathVariable String address)
    {
        BigDecimal balance = accountService.getBalance(address);
        return new JsonVo<>(JsonVo.SUCCESS, balance);
    }
}
