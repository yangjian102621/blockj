package org.rockyang.jblock.crypto;

/**
 * Data class encapsulating a BIP-39 compatible Ethereum wallet.
 * @author yangjian
 */
public class Bip39Wallet {

    private final ECKeyPair keyPair;
    /**
     * Path to wallet file.
     */
    private final String filename;

    /**
     * Generated BIP-39 mnemonic for the wallet.
     */
    private final String mnemonic;

    public Bip39Wallet(ECKeyPair keyPair, String filename, String mnemonic) {
        this.keyPair = keyPair;
        this.filename = filename;
        this.mnemonic = mnemonic;
    }

    public String getFilename() {
        return filename;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public ECKeyPair getKeyPair() {
        return keyPair;
    }

    @Override
    public String toString() {
        return "Bip39Wallet{"
                + "filename='" + filename + '\''
                + ", mnemonic='" + mnemonic + '\''
                + '}';
    }
}
