package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yksj.consultation.comm.CommonAdapter;
import com.yksj.consultation.comm.CommonViewHolder;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.entity.NewEntity;
import com.yksj.healthtalk.utils.HStringUtil;

import org.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻详情 新闻评论adapter
 * 
 * @author jack
 * 
 */
public class AdtDynamicComment extends BaseAdapter {

	private LayoutInflater from;
	public List<NewEntity> datas = new ArrayList<NewEntity>();
	private ImageLoader mInstance;
	private ImageClick imacli;
	private  String numberCount;

	public AdtDynamicComment(Context cntext) {
		from = LayoutInflater.from(cntext);
		mInstance = ImageLoader.getInstance();
	}
	//评论总数
	public void setNumberCount(String number){
		this.numberCount =number;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	public void onBoundData(List<NewEntity> data){
		datas.clear();
		datas.addAll(data);
		notifyDataSetChanged();
	}
	public void addAll(List<NewEntity> data){
		datas.addAll(data);
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


	public void setOnImageclicl(ImageClick imageClick){
		this.imacli = imageClick;
	}

	public String getContent(NewEntity entity){
		String string =entity.UPPER_REPLY_ID ;
		if ("0".equals(string)) {
			return entity.COMMENT_CONTENT;
		} else {
			if (HStringUtil.isEmpty(entity.SUPPER_NICKNAME)) {
				return "回复" + string + "-"
						+  entity.COMMENT_CONTENT;
			} else {
				return "回复" +entity.SUPPER_NICKNAME+ "-"
						+ entity.COMMENT_CONTENT;
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		final NewEntity entity = datas.get(position);
		if("-1".equals(entity.CUSTOMER_ID)){
			convertView = from.inflate(R.layout.news_comment_size_list_item, null);
			TextView number =(TextView) convertView.findViewById(R.id.number);
			number.setText(numberCount+"条");
			return convertView;
		}else{
			if (convertView == null || convertView.getTag() ==null) {
				convertView = from.inflate(R.layout.news_comment_list_item,null);
				holder = new ViewHolder();
				holder.headerImageView = (ImageView) convertView.findViewById(R.id.head_image);
				holder.nameTextView = (TextView) convertView.findViewById(R.id.nameTxt);
				holder.timeTextView = (TextView) convertView.findViewById(R.id.timeTxt);
				holder.contentTextView = (TextView) convertView.findViewById(R.id.contentTxt);
				holder.repeatImageView = (ImageView) convertView.findViewById(R.id.comment_repeat);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			mInstance.displayImage(entity.CUSTOMER_SEX,entity.CLIENT_ICON_BACKGROUND,holder.headerImageView);
			holder.headerImageView.setBackgroundResource(R.drawable.expert_header_image);
			holder.nameTextView.setText(entity.CUSTOMER_NICKNAME);
			holder.nameTextView.setTag(entity.REPLY_ID);
			holder.timeTextView.setText(entity.REPLYTIME);
//			holder.contentTextView.setText(getContent(entity));
			holder.contentTextView.setText(entity.COMMENT_CONTENT);
			holder.headerImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if(imacli!=null){
						imacli.onImageClick(entity);
					}
				}
			});
		}

		return convertView;
	}

	class ViewHolder {
		TextView nameTextView;
		TextView timeTextView;
		TextView contentTextView;
		ImageView headerImageView;
		ImageView repeatImageView;
	}

	public interface ImageClick{
		void onImageClick(NewEntity entity);
	}


	class MyAdapter extends CommonAdapter<CustomerInfoEntity>{

		public MyAdapter(Context context) {
			super(context);
		}

		@Override
		public void onBoundView(CommonViewHolder helper, CustomerInfoEntity item) {
			Button bu =	helper.getView(0);
			bu.setText(item.getAreaCode());
		}

		@Override
		public int viewLayout() {
			return R.layout.accoun_balance_pagelayout;
		}

	}

}
