package com.yksj.consultation.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.ShareEntity;

import java.util.List;

/**
 * Created by hekl on 18/5/8.
 */

public class FamousDocCommentAdapter extends BaseRecyclerAdapter<ShareEntity.ResultBean.CommentBean> {
    public FamousDocCommentAdapter(List<ShareEntity.ResultBean.CommentBean> list, Context context) {
        super(list, context);
    }

    @Override
    public int returnLayout() {
        return R.layout.item_famous_doc_share_comment;
    }

    @Override
    public void onBaseBindViewHolder(ViewHolder holder, int position) {
        TextView tvComment= (TextView) holder.itemView.findViewById(R.id.tvComment);
        ShareEntity.ResultBean.CommentBean commentBean = list.get(position);
        String comment= commentBean.getCUSTOMER_NAME()+":"+commentBean.getCOMMENT_CONTENT();
        SpannableString spannableString = new SpannableString(comment);
        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#8492b0"));
        String[] split = comment.split(":");
        int length = split[0].length();
        spannableString.setSpan(span,0,length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvComment.setText(spannableString);
    }
}
