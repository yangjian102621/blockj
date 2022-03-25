package org.rockyang.jblock.crypto;

import org.rockyang.jblock.utils.Numeric;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

/**
 * 使用椭圆曲线算法生成密钥对
 * Elliptic Curve SECP-256k1 generated key pair.
 * @author yangjian
 */
public class ECKeyPair {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final BigInteger privateKeyValue;
    private final BigInteger publicKeyValue;

    public ECKeyPair(BigInteger privateKeyValue, BigInteger publicKeyValue) throws Exception {
        this.privateKeyValue = privateKeyValue;
        this.publicKeyValue = publicKeyValue;
        this.privateKey = Sign.privateKeyFromBigInteger(privateKeyValue);
        this.publicKey = Sign.publicKeyFromPrivate(privateKeyValue);
    }

    public ECKeyPair(PrivateKey privateKey, PublicKey publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        // 生成 BigInteger 形式的公钥和私钥
        BCECPrivateKey bcecPrivateKey = (BCECPrivateKey) this.privateKey;
        BCECPublicKey bcecPublicKey = (BCECPublicKey) this.publicKey;
        // 分别计算公钥和私钥的值
        BigInteger privateKeyValue = bcecPrivateKey.getD();
        byte[] publicKeyBytes = bcecPublicKey.getQ().getEncoded(false);
        BigInteger publicKeyValue = new BigInteger(1, Arrays.copyOfRange(publicKeyBytes, 1, publicKeyBytes.length));
        this.privateKeyValue = privateKeyValue;
        this.publicKeyValue = publicKeyValue;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     * export the private key to hex string
     * @return
     */
    public String exportPrivateKey() {

        return Numeric.toHexStringNoPrefix(this.getPrivateKeyValue());
    }

    /**
     * get the address
     * @return
     */
    public String getAddress() {
        return Keys.getAddress(this.getPublicKeyValue());
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public BigInteger getPrivateKeyValue() {
        return privateKeyValue;
    }

    public BigInteger getPublicKeyValue() {
        return publicKeyValue;
    }

    public static ECKeyPair create(KeyPair keyPair) {
        BCECPrivateKey privateKey = (BCECPrivateKey) keyPair.getPrivate();
        BCECPublicKey publicKey = (BCECPublicKey) keyPair.getPublic();

        return new ECKeyPair(privateKey, publicKey);
    }

    public static ECKeyPair create(BigInteger privateKeyValue) throws Exception {

        PrivateKey privateKey = Sign.privateKeyFromBigInteger(privateKeyValue);
        PublicKey publicKey = Sign.publicKeyFromPrivate(privateKeyValue);
        return new ECKeyPair(privateKey, publicKey);
    }

    public static ECKeyPair create(byte[] privateKey) throws Exception {
        return create(Numeric.toBigInt(privateKey));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ECKeyPair ecKeyPair = (ECKeyPair) o;

        if (privateKey != null
                ? !privateKey.equals(ecKeyPair.privateKey) : ecKeyPair.privateKey != null) {
            return false;
        }

        return publicKey != null
                ? publicKey.equals(ecKeyPair.publicKey) : ecKeyPair.publicKey == null;
    }

    @Override
    public int hashCode() {
        int result = privateKey != null ? privateKey.hashCode() : 0;
        result = 31 * result + (publicKey != null ? publicKey.hashCode() : 0);
        return result;
    }
}
