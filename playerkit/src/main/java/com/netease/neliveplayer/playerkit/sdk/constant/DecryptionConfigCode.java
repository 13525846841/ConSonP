package com.netease.neliveplayer.playerkit.sdk.constant;

/**
 * 解密选项
 * 只适用于点播
 */
public interface DecryptionConfigCode {

    /**
     * 不需要对视频进行解密
     */
    int CODE_Decryptio_NONE = 0;

    /**
     * 使用解密信息对视频进行解密
     */
    int CODE_DECRYPTIO_INFO = 1;

    /**
     * 解密秘钥对视频进行解密
     */
    int CODE_DECRYPTIO_KEY = 2;

}
