package org.rockyang.blockchain.crypto;

import org.rockyang.blockchain.constants.CryptoConstants;
import org.rockyang.blockchain.utils.Numeric;
import org.rockyang.blockchain.utils.Strings;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;

import java.math.BigInteger;
import java.security.*;
import java.util.Arrays;


/**
 * Crypto key utilities.
 * @author yangjian
 */
public class Keys {

    static final int PRIVATE_KEY_SIZE = 32;
    static final int PUBLIC_KEY_SIZE = 64;

    public static final int ADDRESS_SIZE = 160;
    public static final int ADDRESS_LENGTH_IN_HEX = ADDRESS_SIZE >> 2;
    static final int PUBLIC_KEY_LENGTH_IN_HEX = PUBLIC_KEY_SIZE << 1;
    public static final int PRIVATE_KEY_LENGTH_IN_HEX = PRIVATE_KEY_SIZE << 1;

    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    private Keys() { }

    /**
     * Create a keypair using SECP-256k1 curve.
     *
     * <p>Private keypairs are encoded using PKCS8
     *
     * <p>Private keys are encoded using X.509
     */
    static KeyPair createSecp256k1KeyPair() throws NoSuchProviderException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException {

        // 注册 BC Provider
        Security.addProvider(new BouncyCastleProvider());
        // 创建椭圆曲线算法的密钥对生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
                CryptoConstants.KEY_GEN_ALGORITHM,
                BouncyCastleProvider
                .PROVIDER_NAME);
        // 椭圆曲线（EC）域参数设定
        ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec(CryptoConstants.EC_PARAM_SPEC);
        keyPairGenerator.initialize(ecSpec, new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }

    public static ECKeyPair createEcKeyPair() throws InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, NoSuchProviderException {
        KeyPair keyPair = createSecp256k1KeyPair();
        return ECKeyPair.create(keyPair);
    }

    public static String getAddress(ECKeyPair ecKeyPair) {
        return getAddress(ecKeyPair.getPublicKeyValue());
    }

    public static String getAddress(BigInteger publicKey) {
        return getAddress(
                Numeric.toHexStringWithPrefixZeroPadded(publicKey, PUBLIC_KEY_LENGTH_IN_HEX));
    }

    public static String getAddress(String publicKey) {
        String publicKeyNoPrefix = Numeric.cleanHexPrefix(publicKey);

        if (publicKeyNoPrefix.length() < PUBLIC_KEY_LENGTH_IN_HEX) {
            publicKeyNoPrefix = Strings.zeros(
                    PUBLIC_KEY_LENGTH_IN_HEX - publicKeyNoPrefix.length())
                    + publicKeyNoPrefix;
        }
        String hash = Hash.sha3(publicKeyNoPrefix);
        // right most 160 bits
        return Numeric.HEX_PREFIX + hash.substring(hash.length() - ADDRESS_LENGTH_IN_HEX);
    }

    /**
     * get address with 0x prefix
     * @param publicKey
     * @return
     */
    public static String getAddressWithoutPrefix(BigInteger publicKey) {
        return Numeric.cleanHexPrefix(getAddress(publicKey));
    }

    public static byte[] getAddress(byte[] publicKey) {
        byte[] hash = Hash.sha3(publicKey);
        // right most 160 bits
        return Arrays.copyOfRange(hash, hash.length - 20, hash.length);
    }

    /**
     * Checksum address encoding as per
     * <a href="https://github.com/ethereum/EIPs/blob/master/EIPS/eip-55.md">EIP-55</a>.
     *
     * @param address a valid hex encoded address
     * @return hex encoded checksum address
     */
    public static String toChecksumAddress(String address) {
        String lowercaseAddress = Numeric.cleanHexPrefix(address).toLowerCase();
        String addressHash = Numeric.cleanHexPrefix(Hash.sha3String(lowercaseAddress));

        StringBuilder result = new StringBuilder(lowercaseAddress.length() + 2);

        result.append("0x");

        for (int i = 0; i < lowercaseAddress.length(); i++) {
            if (Integer.parseInt(String.valueOf(addressHash.charAt(i)), 16) >= 8) {
                result.append(String.valueOf(lowercaseAddress.charAt(i)).toUpperCase());
            } else {
                result.append(lowercaseAddress.charAt(i));
            }
        }

        return result.toString();
    }

    public static byte[] serialize(ECKeyPair ecKeyPair) {
        byte[] privateKey = Numeric.toBytesPadded(ecKeyPair.getPrivateKeyValue(), PRIVATE_KEY_SIZE);
        byte[] publicKey = Numeric.toBytesPadded(ecKeyPair.getPublicKeyValue(), PUBLIC_KEY_SIZE);

        byte[] result = Arrays.copyOf(privateKey, PRIVATE_KEY_SIZE + PUBLIC_KEY_SIZE);
        System.arraycopy(publicKey, 0, result, PRIVATE_KEY_SIZE, PUBLIC_KEY_SIZE);
        return result;
    }

    public static ECKeyPair deserialize(byte[] input) throws Exception {
        if (input.length != PRIVATE_KEY_SIZE + PUBLIC_KEY_SIZE) {
            throw new RuntimeException("Invalid input key size");
        }

        BigInteger privateKey = Numeric.toBigInt(input, 0, PRIVATE_KEY_SIZE);
        BigInteger publicKey = Numeric.toBigInt(input, PRIVATE_KEY_SIZE, PUBLIC_KEY_SIZE);

        return new ECKeyPair(privateKey, publicKey);
    }

    /**
     * 将 byte[] 公钥转成字符串
     * @param publicKey
     * @return
     */
    public static String publicKeyEncode(byte[] publicKey) {
        return Base58.encode(publicKey);
    }

    /**
     * 将字符串转成 byte[]
     * @param publicKey
     * @return
     */
    public static byte[] publicKeyDecode(String publicKey) {
        return Base58.decode(publicKey);
    }
}
