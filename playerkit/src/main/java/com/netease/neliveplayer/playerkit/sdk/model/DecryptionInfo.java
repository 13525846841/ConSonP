package com.netease.neliveplayer.playerkit.sdk.model;

import com.netease.neliveplayer.playerkit.sdk.constant.DecryptionConfigCode;

/**
 * 点播视频解密配置
 */
public class DecryptionInfo {
    /**
     * 解密选项
     * 只适用于点播
     *  {@link DecryptionConfigCode}
     */
    public int decryptionConfig;

    /**
     * 设置解密信息
     * 只适用于点播
     * 使用解密信息对视频进行解密时需要设置相关的解密信息
     */
    public DecryptionTokenInfo decryptionTokenInfo;

    /**
     * 设置解密密钥
     * 只适用于点播
     * 使用解密秘钥对视频进行解密时需要设置相关的解密秘钥
     */
    public DecryptionKeyInfo decryptionKeyInfo;
}
