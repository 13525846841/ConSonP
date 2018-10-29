package com.netease.neliveplayer.playerkit.sdk.model;

/**
 * 解密信息
 * 只适用于点播
 */
public class DecryptionTokenInfo {
    /**
     * 获取密钥所需的令牌
     */
    public String transferToken;
    /**
     * 视频云用户创建的其子用户id
     */
    public String accid;
    /**
     * 开发者平台分配的AppKey
     */
    public String appKey;
    /**
     * 视频云用户子用户的token
     */
    public String token;

    public DecryptionTokenInfo(String transferToken, String accid, String appKey, String token) {
        this.transferToken = transferToken;
        this.accid = accid;
        this.appKey = appKey;
        this.token = token;
    }
}
