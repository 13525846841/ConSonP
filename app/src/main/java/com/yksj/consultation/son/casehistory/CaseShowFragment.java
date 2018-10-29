package com.yksj.consultation.son.casehistory;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yksj.consultation.comm.ImageGalleryActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.AppData;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.ImageItem;
import com.yksj.healthtalk.utils.ViewFinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.DefaultConfigurationFactory;
import org.universalimageloader.core.DisplayImageOptions;
import org.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * 展示会诊信息的界面
 * Created by lmk on 15/9/25.
 */
public class CaseShowFragment extends Fragment {
    private View view;
    private String content;
    private LinearLayout patientPicLayout, patientVdoLayout, caseItemsLayout, caseImgLayout, caseVideoLayout;
    private ArrayList<JSONObject> datas;//网络加载过来的数据
    private ImageLoader mImageLoader;
    private LayoutInflater mInflater;//
    private TextView tvName, tvSex, tvAge, tvPhone, tvAddr, tvDesc, tvPicTip, tvAllergy;
    private DisplayImageOptions mOptions;//画廊异步读取操作

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fgt_case_content, null);
        mImageLoader = ImageLoader.getInstance();
        content = getArguments().getString("result");
        initView();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    private void initData() {
        if (content != null && content.length() > 0) {
            datas = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(content);
                onBoundPatientInfo(object);
                JSONObject record = object.getJSONObject("RECORD");
                JSONArray array = record.getJSONArray("CONTENT");
                for (int i = 0; i < array.length(); i++) {
                    datas.add(array.getJSONObject(i));
                }
                if (datas.size() != 0) {
                    onBoundDetailData(datas);//绑定数据到LinearLayout
                }
                onBoundImgData(record);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private boolean dutyExpanded = false;//过敏史
    private boolean dutyExpanded2 = false;//病情说明
    private ImageView imgDutyIndex, imgDutyIndex2;

    private void initView() {
        mOptions = DefaultConfigurationFactory.createApplyPicDisplayImageOptions(getActivity());
        caseImgLayout = (LinearLayout) view.findViewById(R.id.fgt_case_img_layout);
        caseVideoLayout = (LinearLayout) view.findViewById(R.id.fgt_case_img_layout_vdo);
        caseItemsLayout = (LinearLayout) view.findViewById(R.id.fgt_case_template_linearlayout);
        tvPicTip = (TextView) view.findViewById(R.id.fgt_case_img_layout_tv);
        tvAddr = (TextView) view.findViewById(R.id.doctor_write_case_patient_addr2);
        tvName = (TextView) view.findViewById(R.id.doctor_write_case_patient_name);
        tvSex = (TextView) view.findViewById(R.id.doctor_write_case_patient_sex);
        tvAge = (TextView) view.findViewById(R.id.doctor_write_case_patient_age);
        tvPhone = (TextView) view.findViewById(R.id.doctor_write_case_patient_phone2);
        tvDesc = (TextView) view.findViewById(R.id.doctor_write_case_patient_desc2);
        patientPicLayout = (LinearLayout) view.findViewById(R.id.doctor_write_case_patient_picture_layout);
        patientVdoLayout = (LinearLayout) view.findViewById(R.id.doctor_write_case_patient_video_layout);
        mInflater = LayoutInflater.from(getActivity());

        tvAllergy = (TextView) view.findViewById(R.id.doctor_write_case_patient_desc2_allergy);

        imgDutyIndex = (ImageView) view.findViewById(R.id.doctor_write_case_patient_desc_allergy_more);
        imgDutyIndex2 = (ImageView) view.findViewById(R.id.doctor_write_case_patient_desc2_more);

    }

    private void lineCount() {
        //疾病说明
        if (!TextUtils.isEmpty(illness)) {
            if (illness.length() < 50)//字数小雨,将展开按钮隐藏
                imgDutyIndex2.setVisibility(View.INVISIBLE);
            else
                tvDesc.setMaxLines(2);
            imgDutyIndex2.setImageResource(R.drawable.gengduos);
            imgDutyIndex2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dutyExpanded2) {
                        dutyExpanded2 = false;
                        tvDesc.setMaxLines(2);
                        imgDutyIndex2.setImageResource(R.drawable.gengduos);
                    } else {
                        dutyExpanded2 = true;
                        tvDesc.setMaxLines(100);
                        imgDutyIndex2.setImageResource(R.drawable.shouqis);
                    }
                }
            });
        } else {
            tvDesc.setVisibility(View.GONE);
            imgDutyIndex2.setVisibility(View.GONE);
        }

        //过敏史
        if (!TextUtils.isEmpty(allergy)) {
            if (allergy.length() < 50)//字数小于,将展开按钮隐藏
                imgDutyIndex.setVisibility(View.INVISIBLE);
            else
                tvAllergy.setMaxLines(2);
            imgDutyIndex.setImageResource(R.drawable.gengduos);
            imgDutyIndex.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dutyExpanded) {
                        dutyExpanded = false;
                        tvAllergy.setMaxLines(2);
                        imgDutyIndex.setImageResource(R.drawable.gengduos);
                    } else {
                        dutyExpanded = true;
                        tvAllergy.setMaxLines(100);
                        imgDutyIndex.setImageResource(R.drawable.shouqis);
                    }
                }
            });
        } else {
            tvAllergy.setVisibility(View.GONE);
            imgDutyIndex.setVisibility(View.GONE);
        }
    }

    //患者信息
    private ArrayList<JSONObject> pPicList = new ArrayList<>();
    private ArrayList<JSONObject> pVideoList = new ArrayList<>();
    private ArrayList<JSONObject> pThumbnailList = new ArrayList<>();

    ArrayList<ImageItem> pVideosList = new ArrayList<>();//视频list类

    //病历信息
    private ArrayList<JSONObject> picList = new ArrayList<>();
    private ArrayList<JSONObject> videoList = new ArrayList<>();
    private ArrayList<JSONObject> thumbnailList = new ArrayList<>();

    ArrayList<ImageItem> videosList = new ArrayList<>();//视频list类

    String allergy = "";
    String illness = "";

    private void onBoundPatientInfo(JSONObject object) throws JSONException {
        if (object == null) {
            return;
        }
        String str = object.optString("CUSTNAME");
        if (!HStringUtil.isEmpty(str) && tvName != null) tvName.setText(str);
        final StringBuilder sb = new StringBuilder();
        if ("M".equals(object.optString("SEX")))
            tvSex.setText("男");
        else
            tvSex.setText("女");
        tvAge.setText(object.optString("AGE"));
        tvAddr.setText(object.optString("AREA"));
        tvPhone.setText(object.optString("PHONE"));

        if (!HStringUtil.isEmpty(object.optString("CONDESC"))) {
            illness = object.optString("CONDESC");
            tvDesc.setText(illness);
        } else {
            tvDesc.setVisibility(View.GONE);
        }

        if (!HStringUtil.isEmpty(object.optString("ALLERGY"))) {
            allergy = object.optString("ALLERGY");
            tvAllergy.setText(allergy);
        } else {
            tvAllergy.setVisibility(View.GONE);
        }

        JSONArray array = object.getJSONArray("PICS");
        int count = array.length();


        try {
            pPicList.clear();
            pVideoList.clear();
            pThumbnailList.clear();
            pVideosList.clear();
            patientPicLayout.removeAllViews();
            patientVdoLayout.removeAllViews();

            //资源准备
            for (int m = 0; m < count; m++) {
                JSONObject jsonObject = array.getJSONObject(m);
                if (AppData.PIC_TYPE.equals(jsonObject.optString("PIC_TYPE"))) {
                    pPicList.add(jsonObject);
                } else if (AppData.VIDEO_TYPE.equals(jsonObject.optString("PIC_TYPE"))) {
                    pVideoList.add(jsonObject);
                } else if (AppData.THUMBNAIL_TYPE.equals(jsonObject.optString("PIC_TYPE"))) {
                    pThumbnailList.add(jsonObject);
                }
            }
            lineCount();
            int videoCount = pVideoList.size();
            if (videoCount > 0) {
                for (int i = 0; i < videoCount; i++) {
                    ImageItem imageItem = new ImageItem();
                    imageItem.pidId = pVideoList.get(i).optInt("ID");
                    imageItem.setThumbnailPath(pVideoList.get(i).optString("SMALL"));
                    imageItem.setImagePath(pVideoList.get(i).optString("BIG"));
                    if (pThumbnailList.size() >= videoCount) {
                        imageItem.thumbnailId = pThumbnailList.get(i).optInt("ID");
                        imageItem.set_thumbnailPath(pThumbnailList.get(i).optString("SMALL"));
                        imageItem.set_imagePath(pThumbnailList.get(i).optString("BIG"));
                    }
                    imageItem.isNetPic = true;
                    pVideosList.add(imageItem);
                }
            }
            //图片的适配
            if (count > 0) {
                view.findViewById(R.id.doctor_write_case_patient_picture).setVisibility(View.VISIBLE);
            } else {
                view.findViewById(R.id.doctor_write_case_patient_picture).setVisibility(View.GONE);
            }

            if (pPicList.size() > 0) {
                view.findViewById(R.id.hs_pic).setVisibility(View.VISIBLE);
            } else {
                view.findViewById(R.id.hs_pic).setVisibility(View.GONE);
            }

            if (pVideoList.size() > 0) {
                view.findViewById(R.id.hs_video).setVisibility(View.VISIBLE);
            } else {
                view.findViewById(R.id.hs_video).setVisibility(View.GONE);
            }
            String[] arrays = null;//病历图片
            //图片key集合
            arrays = new String[pPicList.size()];
            for (int t = 0; t < pPicList.size(); t++) {
                arrays[t] = pPicList.get(t).optString("BIG");
            }

            for (int i = 0; i < pPicList.size(); i++) {
                final int index = i;
                View view = mInflater.inflate(R.layout.aty_applyform_gallery, caseImgLayout, false);
                ImageView img = (ImageView) view.findViewById(R.id.image_illpic);
                mImageLoader.displayImage(pPicList.get(i).optString("SMALL"), img, mOptions);
                final String[] finalArray = arrays;
                img.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ImageGalleryActivity.class);
                        intent.putExtra(ImageGalleryActivity.URLS_KEY, finalArray);
                        intent.putExtra(ImageGalleryActivity.TYPE_KEY, 1);
                        intent.putExtra("type", 1);// 0,1单个,多个
                        intent.putExtra("position", index);
                        startActivity(intent);
                    }
                });
                patientPicLayout.addView(view);
            }

            for (int j = 0; j < pVideosList.size(); j++) {
                final int index = j;
                View view = mInflater.inflate(R.layout.aty_applyform_gallery_video, caseVideoLayout, false);
                ImageView img = (ImageView) view.findViewById(R.id.image_illpic);
                String thumbnail = pVideosList.get(j).get_imagePath();
                if (!HStringUtil.isEmpty(thumbnail)) {
                    mImageLoader.displayImage(thumbnail, img, mOptions);
                } else {
                    img.setBackgroundResource(R.drawable.video_src_erral);
                }
                img.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(HTalkApplication.getHttpUrls().URL_DOWNLOAVIDEO + pVideosList.get(index).getImagePath());
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "video/mp4");
                        startActivity(intent);

                    }
                });
                patientVdoLayout.addView(view);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


//        if (count > 0) {
//            for (int m = 0; m < count; m++) {
//                sb.append(array.getJSONObject(m).optString("BIG") + ",");
//            }
//            if (sb.length() > 0)
//                sb.deleteCharAt(sb.length() - 1);
//            patientPicLayout.removeAllViews();
//            for (int k = 0; k < array.length(); k++) {
//                final int imgPosition = k;
//                final JSONObject imgObject = array.getJSONObject(k);
////                ImageView imageview = new ImageView(getActivity());
////                imageview.setLayoutParams(new ViewGroup.LayoutParams(DensityUtils.dip2px(getActivity(), 78), DensityUtils.dip2px(getActivity(), 78)));
////                imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                View view = mInflater.inflate(R.layout.aty_applyform_gallery, patientPicLayout, false);
//                ImageView imageview = (ImageView) view.findViewById(R.id.image_illpic);
//                mImageLoader.displayImage(imgObject.optString("SMALL"), imageview);//加载小图片
//                imageview.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(getActivity(), ImageGalleryActivity.class);
//                        intent.putExtra(ImageGalleryActivity.URLS_KEY, sb.toString().split(","));
//                        intent.putExtra(ImageGalleryActivity.TYPE_KEY, 1);
//                        intent.putExtra("type", 1);// 0,1单个,多个
//                        intent.putExtra("position", imgPosition);
//                        startActivityForResult(intent, 100);
//                    }
//                });
//                patientPicLayout.addView(view);
//
//            }
//        } else {
//            view.findViewById(R.id.hs_pic).setVisibility(View.GONE);
//            view.findViewById(R.id.doctor_write_case_patient_picture).setVisibility(View.GONE);
//        }


    }


    /**
     * 绑定LinearLayout数据
     * 患者已经填写完了病历并且上传成功,这时是加载显示病历
     */
    private void onBoundDetailData(ArrayList<JSONObject> datas) {
        if (datas.size() > 0) {
            caseItemsLayout.removeAllViews();
            for (int i = 0; i < datas.size(); i++) {
                ViewFinder finder;
                final int index = i;
                final JSONObject entity = datas.get(i);
                View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.apt_consultion_case_item_show, null, true);
                finder = new ViewFinder(itemView);
                if (itemView == null)
                    return;
                TextView tvCategoryTitle = finder.find(R.id.case_template_item_show_title);
                ;
                if (i == 0) {//第一个一定是开始,显示CLASSNAME
                    tvCategoryTitle.setVisibility(View.VISIBLE);
                    tvCategoryTitle.setText(entity.optString("CLASSNAME"));//分类名称
                } else {//后面的家判断是否显示CLASSNAME
                    JSONObject entity2 = datas.get(i - 1);
                    if (!(entity2.optInt("CLASSID") == entity.optInt("CLASSID"))) {//分类开始
                        tvCategoryTitle.setVisibility(View.VISIBLE);
                        tvCategoryTitle.setText(entity.optString("CLASSNAME"));//分类名称
                    }
                }
                if (entity.optInt("NEFILL") == 1) {//必填
                    finder.find(R.id.case_template_item_show_star).setVisibility(View.VISIBLE);
                }
                finder.setText(R.id.case_template_item_show_name, entity.optString("ITEMNAME"));//先把本item的标题附上去

                TextView tvLeft = (TextView) itemView.findViewById(R.id.case_template_item_show_text_left);
                TextView tvMiddle = (TextView) itemView.findViewById(R.id.case_template_item_show_text_middle);
                TextView tvRight = (TextView) itemView.findViewById(R.id.case_template_item_show_text_right);
                LinearLayout imgLayout = finder.find(R.id.case_template_item_show_images);

                switch (entity.optInt("ITEMTYPE")) {
                    case 10://文字填写
                    case 20://单选
                    case 30://多选
                    case 40://单数字填写
                    case 60://日期
                    case 80://日期
                        tvLeft.setText(entity.optString("INFO"));
                        break;
                    case 50://区域数字填写90~100
                        tvLeft.setText(entity.optString("INFO"));
                        tvMiddle.setVisibility(View.VISIBLE);
                        tvRight.setVisibility(View.VISIBLE);
                        tvRight.setText(entity.optString("INFO2"));
                        break;
                    case 70://大文本域填写
                        tvLeft.setText(entity.optString("INFO"));
                        break;
                    case 90://只有ItemName
                        tvLeft.setVisibility(View.GONE);
                        break;
                }
                caseItemsLayout.addView(itemView);
            }
        }
    }


//    //绑定图片
//    private void onBoundImgData(String recordfiles) {
//        caseImgLayout.removeAllViews();
//        caseVideoLayout.removeAllViews();
//        final StringBuilder sb = new StringBuilder();
//        try {
//            JSONArray imgArray = new JSONArray(recordfiles);
//
////            for (int m = 0; m < imgArray.length(); m++) {
////                if (AppData.PIC_TYPE.equals(imgArray.getJSONObject(m).optString("TYPE"))) {
////                    sb.append(imgArray.getJSONObject(m).optString("ICON").replace("-small", "") + ",");
////                }
////
////            }
////            if (sb.length() > 0)
////                sb.deleteCharAt(sb.length() - 1);
////
////            int count = imgArray.length();
//////            int videoCount = 0;
////
////            for (int e = 0; e < count; e++) {
////                final JSONObject vdoObject = imgArray.getJSONObject(e);
////                if (AppData.VIDEO_TYPE.equals(vdoObject.optString("TYPE"))) {
////                    videoCount++;
////                }
////            }
////
////            if (imgArray != null && imgArray.length() > 0) {
////                view.findViewById(R.id.fgt_case_img_layout_tv).setVisibility(View.VISIBLE);
////                if (videoCount > 0) {
////                    view.findViewById(R.id.fgt_case_template_horscroll2_vdo).setVisibility(View.VISIBLE);
////                }
////                if (count - 2 * videoCount > 0) {
////                    view.findViewById(R.id.fgt_case_img_layout_horscrollview).setVisibility(View.VISIBLE);
////                }
////            } else {
////                return;
////            }
////
////            for (int k = 0; k < imgArray.length(); k++) {
////                final int imgPosition = k;
////                final JSONObject imgObject = imgArray.getJSONObject(k);
////                if (AppData.PIC_TYPE.equals(imgObject.optString("TYPE"))) {
//////                ImageView imageview = new ImageView(getActivity());
//////                imageview.setLayoutParams(new ViewGroup.LayoutParams(DensityUtils.dip2px(getActivity(), 78), DensityUtils.dip2px(getActivity(), 78)));
//////                imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
////                    View view = mInflater.inflate(R.layout.aty_applyform_gallery, caseImgLayout, false);
////                    ImageView imageview = (ImageView) view.findViewById(R.id.image_illpic);
////                    mImageLoader.displayImage(imgObject.optString("ICON"), imageview);//加载小图片
////                    imageview.setOnClickListener(new View.OnClickListener() {
////                        @Override
////                        public void onClick(View v) {
////                            Intent intent = new Intent(getActivity(), ImageGalleryActivity.class);
////                            intent.putExtra(ImageGalleryActivity.URLS_KEY, sb.toString().split(","));
////                            intent.putExtra(ImageGalleryActivity.TYPE_KEY, 1);
////                            intent.putExtra("type", 1);// 0,1单个,多个
////                            intent.putExtra("position", imgPosition);
////                            startActivityForResult(intent, 100);
////                        }
////                    });
////                    caseImgLayout.addView(view);
////                }else if (AppData.VIDEO_TYPE.equals(imgObject.optString("TYPE"))) {
////                    final JSONObject videoObject = imgArray.getJSONObject(k + videoCount);
////                    View view = mInflater.inflate(R.layout.aty_applyform_video_gallery, caseVideoLayout, false);
////                    ImageView imageview = (ImageView) view.findViewById(R.id.image_illpic);
////                    mImageLoader.displayImage(videoObject.optString("ICON"), imageview);//加载小图片
////                    imageview.setOnClickListener(new View.OnClickListener() {
////                        @Override
////                        public void onClick(View v) {
//////                            Intent i = new Intent(getActivity(), InternetVideoDemo.class);
//////                            i.putExtra("url", HTalkApplication.getHttpUrls().URL_DOWNLOAVIDEO + imgObject.optString("URL"));
//////                            startActivity(i);
////                            Uri uri = Uri.parse(HTalkApplication.getHttpUrls().URL_DOWNLOAVIDEO + imgObject.optString("URL"));
////                            Intent intent = new Intent(Intent.ACTION_VIEW);
////                            intent.setDataAndType(uri, "video/mp4");
////                            startActivity(intent);
////
////
////                        }
////                    });
////                    caseVideoLayout.addView(view);
////                }
////            }
//
//
//            picList.clear();
//            videoList.clear();
//            thumbnailList.clear();
//            videosList.clear();
//            caseImgLayout.removeAllViews();
//            caseVideoLayout.removeAllViews();
//
//            int count = imgArray.length();
//
//            //资源准备
//            for (int m = 0; m < count; m++) {
//                JSONObject jsonObject = imgArray.getJSONObject(m);
//                if (AppData.PIC_TYPE.equals(jsonObject.optString("PIC_TYPE"))) {
//                    picList.add(jsonObject);
//                } else if (AppData.VIDEO_TYPE.equals(jsonObject.optString("PIC_TYPE"))) {
//                    videoList.add(jsonObject);
//                } else if (AppData.THUMBNAIL_TYPE.equals(jsonObject.optString("PIC_TYPE"))) {
//                    thumbnailList.add(jsonObject);
//                }
//            }
//
//            int videoCount = videoList.size();
//            if (videoCount > 0) {
//                for (int i = 0; i < videoCount; i++) {
//                    ImageItem imageItem = new ImageItem();
//                    imageItem.pidId = videoList.get(i).optInt("ID");
//                    imageItem.setThumbnailPath(videoList.get(i).optString("SMALL"));
//                    imageItem.setImagePath(videoList.get(i).optString("BIG"));
//                    if (thumbnailList.size() >= videoCount) {
//                        imageItem.thumbnailId = thumbnailList.get(i).optInt("ID");
//                        imageItem.set_thumbnailPath(thumbnailList.get(i).optString("SMALL"));
//                        imageItem.set_imagePath(thumbnailList.get(i).optString("BIG"));
//                    }
//                    imageItem.isNetPic = true;
//                    videosList.add(imageItem);
//                }
//            }
//            //图片的适配
//            if (count > 0) {
//                view.findViewById(R.id.fgt_case_img_layout_tv).setVisibility(View.VISIBLE);
//            } else {
//                view.findViewById(R.id.fgt_case_img_layout_tv).setVisibility(View.GONE);
//            }
//
//            if (picList.size() > 0) {
//                view.findViewById(R.id.fgt_case_img_layout_horscrollview).setVisibility(View.VISIBLE);
//            } else {
//                view.findViewById(R.id.fgt_case_img_layout_horscrollview).setVisibility(View.GONE);
//            }
//
//            if (videoList.size() > 0) {
//                view.findViewById(R.id.fgt_case_template_horscroll2_vdo).setVisibility(View.VISIBLE);
//            } else {
//                view.findViewById(R.id.fgt_case_template_horscroll2_vdo).setVisibility(View.GONE);
//            }
//            String[] array = null;//病历图片
//            //图片key集合
//            array = new String[picList.size()];
//            for (int t = 0; t < picList.size(); t++) {
//                array[t] = picList.get(t).optString("BIG");
//            }
//
//            for (int i = 0; i < picList.size(); i++) {
//                final int index = i;
//                View view = mInflater.inflate(R.layout.aty_applyform_gallery, caseImgLayout, false);
//                ImageView img = (ImageView) view.findViewById(R.id.image_illpic);
//                mImageLoader.displayImage(picList.get(i).optString("SMALL"), img, mOptions);
//                final String[] finalArray = array;
//                img.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(getActivity(), ImageGalleryActivity.class);
//                        intent.putExtra(ImageGalleryActivity.URLS_KEY, finalArray);
//                        intent.putExtra(ImageGalleryActivity.TYPE_KEY, 1);
//                        intent.putExtra("type", 1);// 0,1单个,多个
//                        intent.putExtra("position", index);
//                        startActivity(intent);
//                    }
//                });
//                caseImgLayout.addView(view);
//            }
//            for (int j = 0; j < videosList.size(); j++) {
//                final int index = j;
//                View view = mInflater.inflate(R.layout.aty_applyform_gallery_video, caseVideoLayout, false);
//                ImageView img = (ImageView) view.findViewById(R.id.image_illpic);
//                String thumbnail = videosList.get(j).get_imagePath();
//                if (!HStringUtil.isEmpty(thumbnail)) {
//                    mImageLoader.displayImage(thumbnail, img, mOptions);
//                } else {
//                    img.setBackgroundResource(R.drawable.video_src_erral);
//                }
//                img.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        Uri uri = Uri.parse(HTalkApplication.getHttpUrls().URL_DOWNLOAVIDEO + videosList.get(index).getImagePath());
//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        intent.setDataAndType(uri, "video/mp4");
//                        startActivity(intent);
//
//                    }
//                });
//                caseVideoLayout.addView(view);
//            }
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    public void setResult(String result) {
        content = result;
        initData();
    }

    private void onBoundImgData(JSONObject object) throws JSONException {
        picList.clear();
        videoList.clear();
        thumbnailList.clear();
        videosList.clear();

        caseImgLayout.removeAllViews();
        caseVideoLayout.removeAllViews();

        JSONArray array = object.getJSONArray("RECORDFILE");
        int count = array.length();

        try {
            //资源准备
            for (int m = 0; m < count; m++) {
                JSONObject jsonObject = array.getJSONObject(m);
                if (AppData.PIC_TYPE.equals(jsonObject.optString("TYPE"))) {
                    picList.add(jsonObject);
                } else if (AppData.VIDEO_TYPE.equals(jsonObject.optString("TYPE"))) {
                    videoList.add(jsonObject);
                } else if (AppData.THUMBNAIL_TYPE.equals(jsonObject.optString("TYPE"))) {
                    thumbnailList.add(jsonObject);
                }
            }

            int videoCount = videoList.size();
            if (videoCount > 0) {
                for (int i = 0; i < videoCount; i++) {
                    ImageItem imageItem = new ImageItem();
                    imageItem.pidId = videoList.get(i).optInt("ID");
                    imageItem.setThumbnailPath(videoList.get(i).optString("ICON"));
                    imageItem.setImagePath(videoList.get(i).optString("URL"));
                    if (thumbnailList.size() >= videoCount) {
                        imageItem.thumbnailId = thumbnailList.get(i).optInt("ID");
                        imageItem.set_thumbnailPath(thumbnailList.get(i).optString("ICON"));
                        imageItem.set_imagePath(thumbnailList.get(i).optString("URL"));
                    }
                    imageItem.isNetPic = true;
                    videosList.add(imageItem);
                }
            }
            //图片的适配
            if (count > 0) {
                view.findViewById(R.id.fgt_case_img_layout_tv).setVisibility(View.VISIBLE);
            } else {
                view.findViewById(R.id.fgt_case_img_layout_tv).setVisibility(View.GONE);
            }

            if (picList.size() > 0) {
                view.findViewById(R.id.fgt_case_img_layout_horscrollview).setVisibility(View.VISIBLE);
            } else {
                view.findViewById(R.id.fgt_case_img_layout_horscrollview).setVisibility(View.GONE);
            }

            if (videoList.size() > 0) {
                view.findViewById(R.id.fgt_case_template_horscroll2_vdo).setVisibility(View.VISIBLE);
            } else {
                view.findViewById(R.id.fgt_case_template_horscroll2_vdo).setVisibility(View.GONE);
            }
            String[] arrays = null;//病历图片
            //图片key集合
            arrays = new String[picList.size()];
            for (int t = 0; t < picList.size(); t++) {
                arrays[t] = picList.get(t).optString("URL");
            }

            for (int i = 0; i < picList.size(); i++) {
                final int index = i;
                View view = mInflater.inflate(R.layout.aty_applyform_gallery, caseImgLayout, false);
                ImageView img = (ImageView) view.findViewById(R.id.image_illpic);
                mImageLoader.displayImage(picList.get(i).optString("ICON"), img, mOptions);
                final String[] finalArray = arrays;
                img.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ImageGalleryActivity.class);
                        intent.putExtra(ImageGalleryActivity.URLS_KEY, finalArray);
                        intent.putExtra(ImageGalleryActivity.TYPE_KEY, 1);
                        intent.putExtra("type", 1);// 0,1单个,多个
                        intent.putExtra("position", index);
                        startActivity(intent);
                    }
                });
                caseImgLayout.addView(view);
            }
            for (int j = 0; j < videosList.size(); j++) {
                final int index = j;
                View view = mInflater.inflate(R.layout.aty_applyform_gallery_video, caseVideoLayout, false);
                ImageView img = (ImageView) view.findViewById(R.id.image_illpic);
                String thumbnail = videosList.get(j).get_imagePath();
                if (!HStringUtil.isEmpty(thumbnail)) {
                    mImageLoader.displayImage(thumbnail, img, mOptions);
                } else {
                    img.setBackgroundResource(R.drawable.video_src_erral);
                }
                img.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(HTalkApplication.getHttpUrls().URL_DOWNLOAVIDEO + videosList.get(index).getImagePath());
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "video/mp4");
                        startActivity(intent);

                    }
                });
                caseVideoLayout.addView(view);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
