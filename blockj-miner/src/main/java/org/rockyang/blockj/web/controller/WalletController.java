package org.rockyang.blockj.web.controller;

import org.rockyang.blockj.base.crypto.ECKeyPair;
import org.rockyang.blockj.base.crypto.MnemonicUtils;
import org.rockyang.blockj.base.crypto.SecureRandomUtils;
import org.rockyang.blockj.base.model.Account;
import org.rockyang.blockj.base.model.Wallet;
import org.rockyang.blockj.base.vo.JsonVo;
import org.rockyang.blockj.base.vo.MnemonicWallet;
import org.rockyang.blockj.service.AccountService;
import org.rockyang.blockj.service.WalletService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static org.rockyang.blockj.base.crypto.Hash.sha256;

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

    @PostMapping("/new/mnemonic")
    public JsonVo<MnemonicWallet> newMnemonicWallet(@RequestParam("password") String password) throws Exception
    {
        byte[] initialEntropy = new byte[16];
        SecureRandomUtils.secureRandom().nextBytes(initialEntropy);

        String mnemonic = MnemonicUtils.generateMnemonic(initialEntropy);
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, password);
        ECKeyPair privateKey = ECKeyPair.create(sha256(seed));

        Wallet wallet = new Wallet(privateKey);
        walletService.addWallet(wallet);

        return new JsonVo<>(JsonVo.SUCCESS, new MnemonicWallet(mnemonic, wallet));
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
