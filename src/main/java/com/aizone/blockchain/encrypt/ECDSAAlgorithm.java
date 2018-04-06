/**
 * Project Name:trustsql_sdk
 * File Name:ECDSAAlgoUtil.java
 * Package Name:com.tencent.trustsql.sdk.algo
 * Date:Jul 26, 20175:17:04 PM
 * Copyright (c) 2017, Tencent All Rights Reserved.
 */

package com.aizone.blockchain.encrypt;


import com.google.common.base.Objects;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.math.ec.ECPoint;
import org.spongycastle.asn1.ASN1InputStream;
import org.spongycastle.asn1.ASN1Integer;
import org.spongycastle.asn1.DERSequenceGenerator;
import org.spongycastle.asn1.DLSequence;
import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.crypto.digests.SHA256Digest;
import org.spongycastle.crypto.ec.CustomNamedCurves;
import org.spongycastle.crypto.params.ECDomainParameters;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.params.ECPublicKeyParameters;
import org.spongycastle.crypto.signers.ECDSASigner;
import org.spongycastle.crypto.signers.HMacDSAKCalculator;
import org.spongycastle.math.ec.FixedPointUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * 椭圆曲线加密算法实现
 * @author yangjian
 * @since JDK 1.7
 */
public class ECDSAAlgorithm {
    public static final ECDomainParameters CURVE;
    public static final BigInteger HALF_CURVE_ORDER;
    /**
     * 产生随机数的算法
     */
    public static final String RANDOM_NUMBER_ALGORITHM = "SHA1PRNG";
    /**
     * 随机数算法的提供者
     */
    public static final String RANDOM_NUMBER_ALGORITHM_PROVIDER = "SUN";
    /**
     * 私钥最大值
     */
    public static final BigInteger MAX_PRIVATE_KEY = new BigInteger
            ("00FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364140",
            16);

    static {
        X9ECParameters CURVE_PARAMS = CustomNamedCurves.getByName("secp256k1");
        FixedPointUtil.precompute(CURVE_PARAMS.getG(), 12);
        CURVE = new ECDomainParameters(CURVE_PARAMS.getCurve(), CURVE_PARAMS.getG(), CURVE_PARAMS.getN(),
                CURVE_PARAMS.getH());
        HALF_CURVE_ORDER = CURVE_PARAMS.getN().shiftRight(1);
    }

    public static String generatePrivateKey() {
        SecureRandom secureRandom;
        try {
            secureRandom = SecureRandom.getInstance(RANDOM_NUMBER_ALGORITHM,
                    RANDOM_NUMBER_ALGORITHM_PROVIDER);
        } catch (Exception e) {
            secureRandom = new SecureRandom();
        }
        // Generate the key, skipping as many as desired.
        byte[] privateKeyAttempt = new byte[32];
        secureRandom.nextBytes(privateKeyAttempt);
        BigInteger privateKeyCheck = new BigInteger(1, privateKeyAttempt);
        while (privateKeyCheck.compareTo(BigInteger.ZERO) == 0 || privateKeyCheck.compareTo(MAX_PRIVATE_KEY) > 0) {
            secureRandom.nextBytes(privateKeyAttempt);
            privateKeyCheck = new BigInteger(1, privateKeyAttempt);
        }
        String result = Base64.encodeBase64String(privateKeyAttempt);
        result = result.replaceAll("[\\s*\t\n\r]", "");
        return result;
    }

    /**
     * 生成公钥，encode为true时为短公钥
     * @param privateKeyBase64String
     * 私钥
     * @param encode
     * 是否使用base64缩短
     * @return
     * 公钥
     */
    public static String generatePublicKey(String privateKeyBase64String, boolean encode) {
        try {
            byte[] privateKeyBytes = Base64.decodeBase64(privateKeyBase64String);
            ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec("secp256k1");
            ECPoint pointQ = spec.getG().multiply(new BigInteger(1, privateKeyBytes));
            String result = Base64.encodeBase64String(pointQ.getEncoded(encode));
            result = result.replaceAll("[\\s*\t\n\r]", "");
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成长公钥
     * @param privateKeyBase64String
     * 私钥
     * @return
     * 公钥
     */
    public static String generatePublicKey(String privateKeyBase64String) {
        return generatePublicKey(privateKeyBase64String, false);
    }

    public static String decodePublicKey(String encodePubKeyBase64String) {
        try {
            byte[] encodePubkeyBytes = Base64.decodeBase64(encodePubKeyBase64String);
            ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec("secp256k1");
            ECPoint pointQ = spec.getG().getCurve().decodePoint(encodePubkeyBytes);
            String result = Base64.encodeBase64String(pointQ.getEncoded(false));
            result = result.replaceAll("[\\s*\t\n\r]", "");
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据公钥生成地址
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String getAddress(String publicKey) throws Exception {
        byte[] keyBytes = publicKey.getBytes("UTF-8");
        byte[] hashSha256 = BaseAlgorithm.encode("SHA-256", keyBytes);
        MessageDigest messageDigest = MessageDigest.getInstance("RipeMD160");
        messageDigest.update(hashSha256);
        byte[] hashRipeMD160 = messageDigest.digest();
        byte[] hashDoubleSha256 = BaseAlgorithm.encodeTwice("SHA-256", hashRipeMD160);
        byte[] rawAddr = new byte[1 + hashRipeMD160.length + 4];
        rawAddr[0] = 0;
        System.arraycopy(hashRipeMD160, 0, rawAddr, 1, hashRipeMD160.length);
        System.arraycopy(hashDoubleSha256, 0, rawAddr, hashRipeMD160.length + 1, 4);
        return Base58Algorithm.encode(rawAddr);
    }

    /**
     * 私用私钥签名数据
     * @param privateKey
     * @param data
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String sign(String privateKey, String data) throws UnsupportedEncodingException {
        return sign(privateKey, data.getBytes("UTF-8"));
    }

    /**
     * 私用私钥签名数据
     * @param privateKey
     * @param data
     * @return
     */
    public static String sign(String privateKey, byte[] data) {
        byte[] hash256 = BaseAlgorithm.encode("SHA-256", data);
        ECDSASigner signer = new ECDSASigner(new HMacDSAKCalculator(new SHA256Digest()));
        BigInteger pri = new BigInteger(1, Base64.decodeBase64(privateKey));
        ECPrivateKeyParameters privKey = new ECPrivateKeyParameters(pri, CURVE);
        signer.init(true, privKey);
        BigInteger[] components = signer.generateSignature(hash256);
        byte[] content = new ECDSASignature(components[0], components[1]).toCanonicalised().encodeToDER();
        String result = Base64.encodeBase64String(content);
        result = result.replaceAll("[\\s*\t\n\r]", "");
        return result;
    }

    /**
     * 根据公钥验证签名是否合法
     * @param srcStr 原始数据
     * @param sign
     * @param pubKey
     * @return
     * @throws Exception
     */
    public static boolean verify(String srcStr, String sign, String pubKey) throws Exception {
        byte[] hash256 = BaseAlgorithm.encode("SHA-256", srcStr.getBytes("UTF-8"));
        ECDSASignature eCDSASignature = ECDSASignature.decodeFromDER(Base64.decodeBase64(sign));
        ECDSASigner signer = new ECDSASigner();
        org.spongycastle.math.ec.ECPoint pub = CURVE.getCurve().decodePoint(Base64.decodeBase64(pubKey));
        ECPublicKeyParameters params = new ECPublicKeyParameters(CURVE.getCurve().decodePoint(pub.getEncoded()), CURVE);
        signer.init(false, params);
        return signer.verifySignature(hash256, eCDSASignature.r, eCDSASignature.s);
    }

    public static class ECDSASignature {
        /** The two components of the signature. */
        public final BigInteger r, s;

        /**
         * Constructs a signature with the given components. Does NOT
         * automatically canonicalise the signature.
         */
        public ECDSASignature(BigInteger r, BigInteger s) {
            this.r = r;
            this.s = s;
        }

        /**
         * Returns true if the S component is "low", that means it is below
         *  See <a href=
         * "https://github.com/bitcoin/bips/blob/master/bip-0062.mediawiki#Low_S_values_in_signatures">
         * BIP62</a>.
         */
        public boolean isCanonical() {
            return s.compareTo(HALF_CURVE_ORDER) <= 0;
        }

        /**
         * Will automatically adjust the S component to be less than or equal to
         * half the curve order, if necessary. This is required because for
         * every signature (r,s) the signature (r, -s (mod N)) is a valid
         * signature of the same message. However, we dislike the ability to
         * modify the bits of a Bitcoin transaction after it's been signed, as
         * that violates various assumed invariants. Thus in future only one of
         * those forms will be considered legal and the other will be banned.
         */
        public ECDSASignature toCanonicalised() {
            if (!isCanonical()) {
                // The order of the curve is the number of valid points that
                // exist on that curve. If S is in the upper
                // half of the number of valid points, then bring it back to the
                // lower half. Otherwise, imagine that
                // N = 10
                // s = 8, so (-8 % 10 == 2) thus both (r, 8) and (r, 2) are
                // valid solutions.
                // 10 - 8 == 2, giving us always the latter solution, which is
                // canonical.
                return new ECDSASignature(r, CURVE.getN().subtract(s));
            } else {
                return this;
            }
        }

        /**
         * DER is an international standard for serializing data structures
         * which is widely used in cryptography. It's somewhat like protocol
         * buffers but less convenient. This method returns a standard DER
         * encoding of the signature, as recognized by OpenSSL and other
         * libraries.
         */
        public byte[] encodeToDER() {
            try {
                return derByteStream().toByteArray();
            } catch (IOException e) {
                // Cannot happen.
                throw new RuntimeException(e);
            }
        }

        public static ECDSASignature decodeFromDER(byte[] bytes) {
            ASN1InputStream decoder = null;
            try {
                decoder = new ASN1InputStream(bytes);
                DLSequence seq = (DLSequence) decoder.readObject();
                if (seq == null) {
                    throw new RuntimeException("Reached past end of ASN.1 stream.");
                }
                ASN1Integer r, s;
                try {
                    r = (ASN1Integer) seq.getObjectAt(0);
                    s = (ASN1Integer) seq.getObjectAt(1);
                } catch (ClassCastException e) {
                    throw new IllegalArgumentException(e);
                }
                // OpenSSL deviates from the DER spec by interpreting these
                // values as unsigned, though they should not be
                // Thus, we always use the positive versions. See:
                // http://r6.ca/blog/20111119T211504Z.html
                return new ECDSASignature(r.getPositiveValue(), s.getPositiveValue());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (decoder != null) {
                    try {
                        decoder.close();
                    } catch (IOException x) {
                    }
                }
            }
        }

        protected ByteArrayOutputStream derByteStream() throws IOException {
            // Usually 70-72 bytes.
            ByteArrayOutputStream bos = new ByteArrayOutputStream(72);
            DERSequenceGenerator seq = new DERSequenceGenerator(bos);
            seq.addObject(new ASN1Integer(r));
            seq.addObject(new ASN1Integer(s));
            seq.close();
            return bos;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ECDSASignature other = (ECDSASignature) o;
            return r.equals(other.r) && s.equals(other.s);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(r, s);
        }
    }

}
