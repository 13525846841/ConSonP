package com.yksj.consultation.son.consultation.avchat.team;


import com.yksj.consultation.son.consultation.avchat.team.input.BaseMultiItemFetchLoadAdapter;
import com.yksj.consultation.son.consultation.avchat.team.input.BaseViewHolder;
import com.yksj.consultation.son.consultation.avchat.team.input.animation.RecyclerViewHolder;

/**
 * Created by huangjun on 2017/5/9.
 */

abstract class TeamAVChatItemViewHolderBase extends RecyclerViewHolder<BaseMultiItemFetchLoadAdapter, BaseViewHolder, TeamAVChatItem> {

    TeamAVChatItemViewHolderBase(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    public void convert(final BaseViewHolder holder, TeamAVChatItem data, int position, boolean isScrolling) {
        inflate(holder);
        refresh(data);
    }

    protected abstract void inflate(final BaseViewHolder holder);

    protected abstract void refresh(final TeamAVChatItem data);
}
