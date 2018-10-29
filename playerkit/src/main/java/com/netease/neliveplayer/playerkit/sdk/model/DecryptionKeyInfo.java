package com.netease.neliveplayer.playerkit.sdk.model;

/**
 * 解密密钥
 * 只适用于点播
 */
public class DecryptionKeyInfo {
    /**
     * 密钥
     */
    public byte[] flvKey;
    /**
     * 密钥长度
     */
    public int flvKeyLen;

    public DecryptionKeyInfo(byte[] flvKey, int flvKeyLen) {
        this.flvKey = flvKey;
        this.flvKeyLen = flvKeyLen;
    }
}
