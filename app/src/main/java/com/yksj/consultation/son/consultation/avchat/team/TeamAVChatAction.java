package com.yksj.consultation.son.consultation.avchat.team;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.model.AVChatChannelInfo;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.CustomNotificationConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.avchat.AVChatProfile;
import com.yksj.consultation.son.consultation.avchat.cache.DemoCache;
import com.yksj.consultation.son.consultation.avchat.cache.SimpleCallback;
import com.yksj.consultation.son.consultation.avchat.cache.TeamDataCache;
import com.yksj.consultation.son.consultation.avchat.team.util.LogUtil;
import com.yksj.consultation.son.consultation.avchat.utils.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by hzchenkang on 2017/5/3.
 */

public class TeamAVChatAction extends AVChatAction {

    private static final int MAX_INVITE_NUM = 8;

    // private String teamID;

    private LaunchTransaction transaction;

    public TeamAVChatAction(AVChatType avChatType) {
        super(avChatType);
    }

    @Override
    public void startAudioVideoCall(AVChatType avChatType) {

        if (AVChatProfile.getInstance().isAVChatting()) {
            Toast.makeText(getActivity(), "正在进行P2P视频通话，请先退出", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TeamAVChatHelper.sharedInstance().isTeamAVChatting()) {
            // 视频通话界面正在运行，singleTop所以直接调起来
            Intent localIntent = new Intent();
            localIntent.setClass(getActivity(), TeamAVChatActivity.class);
            localIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            getActivity().startActivity(localIntent);
            return;
        }

        if (transaction != null) {
            return;
        }

        final String tid = getAccount();
        if (TextUtils.isEmpty(tid)) {
            return;
        }
        transaction = new LaunchTransaction();
        transaction.setTeamID(tid);

        // load 一把群成员
        TeamDataCache.getInstance().fetchTeamMemberList(tid, new SimpleCallback<List<TeamMember>>() {
            @Override
            public void onResult(boolean success, List<TeamMember> result) {
                // 检查下 tid 是否相等
                if (!checkTransactionValid()) {
                    return;
                }
                if (success && result != null) {
                    if (result.size() < 2) {
                        transaction = null;
                        Toast.makeText(getActivity(), getActivity().getString(R.string.t_avchat_not_start_with_less_member), Toast.LENGTH_SHORT).show();
                    } else {
//                        NimUIKit.startContactSelect(getActivity(), getContactSelectOption(tid), TeamRequestCode.REQUEST_TEAM_VIDEO);
                    }
                }
            }
        });
    }

    public void onSelectedAccountFail() {
        transaction = null;
    }

//    public void onSelectedAccountsResult(final ArrayList<String> accounts) {
////        LogUtil.ui("start teamVideo " + getAccount() + " accounts = " + accounts);
//
////        if (!checkTransactionValid()) {
////            return;
////        }
//
//        final String roomName = StringUtil.get32UUID();
////        LogUtil.ui("create room " + roomName);
//        // 创建房间
//        boolean webRTCCompat = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(getActivity().getString(R.string.nrtc_setting_other_webrtc_compat_key), true);
//        AVChatManager.getInstance().createRoom(roomName, null, webRTCCompat, new AVChatCallback<AVChatChannelInfo>() {
//            @Override
//            public void onSuccess(AVChatChannelInfo avChatChannelInfo) {
//                onCreateRoomSuccess(roomName, accounts);
////                transaction.setRoomName(roomName);
////                String teamName = TeamDataCache.getInstance().getTeamName(transaction.getTeamID());
//                TeamAVChatHelper.sharedInstance().setTeamAVChatting(true);
////                TeamAVChatActivity.startActivity(AtyMain.this, false, transaction.getTeamID(), roomName, accounts, "123");
//                TeamAVChatActivity.startActivity(getActivity(), false, groupId, roomName, accounts, roomName);
////                transaction = null;
//            }
//
//            @Override
//            public void onFailed(int code) {
////                if (!checkTransactionValid()) {
////                    return;
////                }
//                onCreateRoomFail();
//            }
//
//            @Override
//            public void onException(Throwable exception) {
////                if (!checkTransactionValid()) {
////                    return;
////                }
//                onCreateRoomFail();
//            }
//        });
//    }


    public void onSelectedAccountsResult(final ArrayList<String> accounts) {
        LogUtil.ui("start teamVideo " + getAccount() + " accounts = " + accounts);

//        if (!checkTransactionValid()) {
//            return;
//        }

        final String roomName = StringUtil.get32UUID();
        LogUtil.ui("create room " + roomName);
        // 创建房间
        boolean webRTCCompat = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(getActivity().getString(R.string.nrtc_setting_other_webrtc_compat_key), true);
        AVChatManager.getInstance().createRoom(roomName, null, true, new AVChatCallback<AVChatChannelInfo>() {
            @Override
            public void onSuccess(AVChatChannelInfo avChatChannelInfo) {
                LogUtil.ui("create room " + roomName + " success !");
//                if (!checkTransactionValid()) {
//                    return;
//                }
//                onCreateRoomSuccess(roomName, accounts,);
//                transaction.setRoomName(roomName);

//                String teamName = TeamDataCache.getInstance().getTeamName("136114919");

                TeamAVChatHelper.sharedInstance().setTeamAVChatting(true);
                TeamAVChatActivity.startActivity(getActivity(), false,"136114919", roomName, accounts, "136114919");
//                transaction = null;
            }

            @Override
            public void onFailed(int code) {
                if (!checkTransactionValid()) {
                    return;
                }
//                onCreateRoomFail();
            }

            @Override
            public void onException(Throwable exception) {
//                if (!checkTransactionValid()) {
//                    return;
//                }
                onCreateRoomFail();
            }
        });
    }


    private boolean checkTransactionValid() {
        if (transaction == null) {
            return false;
        }
        if (transaction.getTeamID() == null || !transaction.getTeamID().equals(getAccount())) {
            transaction = null;
            return false;
        }
        return true;
    }

    //
//    private ContactSelectActivity.Option getContactSelectOption(String teamId) {
//        ContactSelectActivity.Option option = new ContactSelectActivity.Option();
//        option.type = ContactSelectActivity.ContactSelectType.TEAM_MEMBER;
//        option.teamId = teamId;
//        option.maxSelectNum = MAX_INVITE_NUM;
//        option.maxSelectNumVisible = true;
//        option.title = NimUIKit.getContext().getString(com.netease.nim.uikit.R.string.invite_member);
//        option.maxSelectedTip = NimUIKit.getContext().getString(com.netease.nim.uikit.R.string.reach_capacity);
//        option.itemFilter = new ContactItemFilter() {
//            @Override
//            public boolean filter(AbsContactItem item) {
//                IContact contact = ((ContactItem) item).getContact();
//                // 过滤掉自己
//                return contact.getContactId().equals(DemoCache.getAccount());
//            }
//        };
//        return option;
//    }

    private void onCreateRoomSuccess(String roomName, List<String> accounts,String groupId) {
//        String teamID = transaction.getTeamID();
        String teamID = groupId;
        // 在群里发送tip消息
        IMMessage message = MessageBuilder.createTipMessage(teamID, SessionTypeEnum.Team);
        CustomMessageConfig tipConfig = new CustomMessageConfig();
        tipConfig.enableHistory = false;
        tipConfig.enableRoaming = false;
        tipConfig.enablePush = false;
        String teamNick = TeamDataCache.getInstance().getDisplayNameWithoutMe(teamID, DemoCache.getAccount());
        message.setContent(teamNick + "发起了视频聊天");
        message.setConfig(tipConfig);
        sendMessage(message);
        // 对各个成员发送点对点自定义通知
//        String teamName = TeamDataCache.getInstance().getTeamName(transaction.getTeamID());
//        String teamName = TeamDataCache.getInstance().getTeamName(id);
        String content = TeamAVChatHelper.sharedInstance().buildContent(roomName, teamID, accounts, teamID);
        CustomNotificationConfig config = new CustomNotificationConfig();
        config.enablePush = true;
        config.enablePushNick = false;
        config.enableUnreadCount = true;

        for (String account : accounts) {
            CustomNotification command = new CustomNotification();
            command.setSessionId(account);
            command.setSessionType(SessionTypeEnum.P2P);
            command.setConfig(config);
            command.setContent(content);
            command.setApnsText(teamNick + "[网络通话]");

            command.setSendToOnlineUserOnly(false);
            NIMClient.getService(MsgService.class).sendCustomNotification(command);
        }
    }

    private void onCreateRoomFail() {
        // 本地插一条tip消息
        IMMessage message = MessageBuilder.createTipMessage(transaction.getTeamID(), SessionTypeEnum.Team);
        message.setContent(getActivity().getString(R.string.t_avchat_create_room_fail));
        message.setStatus(MsgStatusEnum.success);
        NIMClient.getService(MsgService.class).saveMessageToLocal(message, true);
    }

    private class LaunchTransaction implements Serializable {
        private String teamID;
        private String roomName;

        public String getRoomName() {
            return roomName;
        }

        public String getTeamID() {
            return teamID;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }

        public void setTeamID(String teamID) {
            this.teamID = teamID;
        }
    }





    public void onSelectedAccountsResult(final ArrayList<String> accounts, final String groupId) {
//        LogUtil.ui("start teamVideo " + getAccount() + " accounts = " + accounts);

//        if (!checkTransactionValid()) {
//            return;
//        }

        final String roomName = StringUtil.get32UUID();
        LogUtil.ui("create room " + roomName);
        // 创建房间
//        boolean webRTCCompat = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(getString(R.string.nrtc_setting_other_webrtc_compat_key), true);
        AVChatManager.getInstance().createRoom(roomName, null, true, new AVChatCallback<AVChatChannelInfo>() {
            @Override
            public void onSuccess(AVChatChannelInfo avChatChannelInfo) {
//                LogUtil.ui("create room " + roomName + " success !");
//                if (!checkTransactionValid()) {
//                    return;
//                }
                onCreateRoomSuccess(roomName, accounts,groupId);
//                transaction.setRoomName(roomName);

//                String teamName = TeamDataCache.getInstance().getTeamName(transaction.getTeamID());

                TeamAVChatHelper.sharedInstance().setTeamAVChatting(true);
//                TeamAVChatActivity.startActivity(getActivity(), false, "137182216", roomName, accounts, "137182216");
                TeamAVChatActivity.startActivity(getActivity(), false, groupId, roomName, accounts, groupId);
//                transaction = null;
            }

            @Override
            public void onFailed(int code) {
//                if (!checkTransactionValid()) {
//                    return;
//                }
                onCreateRoomFail();
            }

            @Override
            public void onException(Throwable exception) {
//                if (!checkTransactionValid()) {
//                    return;
//                }
                onCreateRoomFail();
            }
        });
    }
}
