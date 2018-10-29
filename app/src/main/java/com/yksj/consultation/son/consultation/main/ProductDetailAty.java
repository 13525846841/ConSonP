package com.yksj.consultation.son.consultation.main;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.ImageGalleryActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.smallone.ui.InternetVideoDemo;
import com.yksj.consultation.son.views.CustomScrollDurationView;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.ScreenUtils;
import com.yksj.healthtalk.utils.ThreadManager;
import com.yksj.healthtalk.utils.WeakHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.DefaultConfigurationFactory;
import org.universalimageloader.core.DisplayImageOptions;
import org.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 购物车 商品详情
 */
public class ProductDetailAty extends BaseFragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private int mAmount;//购买商品数量
    private String goodId;//商品ID

    private TextView goodName;
    private TextView goodPrice;
    private TextView count;
    private TextView freight;

    //private TextView showGoods;
    public ProductTopFragment productTopFragment;

    private int CHATTINGCODE = 2;
    private int item = 0;//Viewpager的被选中项
    private TabsPagerAdapter adapter;
    View mView;
    /**
     * 当前显示的是第几页
     */
    private int current = 0;
    private ViewPager mViewPager;
    private List<JSONObject> list = null;//轮播图
    private final int TIME = 3000;
    private LinearLayout mLlDot;
    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1000:
                    current = mViewPager.getCurrentItem();
                    current++;
                    mViewPager.setCurrentItem(current, true);
                    mHandler.sendEmptyMessageDelayed(1000, TIME);
                    break;
            }
        }
    };

    private String productName = "";
    private String productPrice = "";
    private String productFreight = "";
    private String imageUrl = "";
    private ImageLoader mImageLoader;
    private LinearLayout mImageLayout;
    private List<String> urlList = new ArrayList<String>();// 所有图片路径,便于点击
    public JSONObject contentObject;//内容数据
    private JSONObject object = null;
    private int previousSelectedPosition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail_aty);
        initView();
    }

    private void initView() {
        initTitle();
        titleLeftBtn.setOnClickListener(this);
        titleTextV.setText("商品详情");
        mViewPager = (ViewPager) findViewById(R.id.shop_pager);
        mLlDot = (LinearLayout) findViewById(R.id.ll_dot);

        findViewById(R.id.shopping).setOnClickListener(this);
        goodId = getIntent().getStringExtra("good_id");
        goodName = (TextView) findViewById(R.id.goods_name);
        goodPrice = (TextView) findViewById(R.id.good_price);
        count = (TextView) findViewById(R.id.count);
        freight = (TextView) findViewById(R.id.freight);
        mImageLayout = (LinearLayout) findViewById(R.id.shop_good_images);

        mImageLoader = ImageLoader.getInstance();

  		/*主要代码段  限制viewpage 滑动速度*/
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            //设置加速度 ，通过改变FixedSpeedScroller这个类中的mDuration来改变动画时间（如mScroller.setmDuration(mMyDuration);）
            CustomScrollDurationView mScroller = new CustomScrollDurationView(mViewPager.getContext(), new AccelerateInterpolator());
            mField.set(mViewPager, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initData();
    }
    private ImageView[] indicationPoint;//指示点控件
    private void initData() {
        Map<String,String> map=new HashMap<>();
        map.put("Type", "findGoodById");
        map.put("good_id", goodId);

        HttpRestClient.OKHttGoodsServlet(map, new HResultCallback<String>(this) {
            @Override
            public void onError(Request request, Exception e) {
            }
            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))){
                        if (!HStringUtil.isEmpty(obj.optString("server_params"))){
                            object = obj.optJSONObject("server_params");
                            JSONArray picArray = object.optJSONArray("picures");

                            JSONObject jsonobject = null;
                            list = new ArrayList<>();

                            indicationPoint = new ImageView[picArray.length()];
                            try {
                                for (int i = 0; i < picArray.length(); i++) {
                                    jsonobject = picArray.getJSONObject(i);
                                    list.add(jsonobject);
                                }
                                //动态生成指示点
                                indicationPoint = new ImageView[list.size()];
                                for (int i = 0; i < indicationPoint.length; i++) {
                                    ImageView point = new ImageView(ProductDetailAty.this);
                                    LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(20, 20);
                                    layout.setMargins(10, 10, 10, 10);
                                    point.setLayoutParams(layout);
                                    indicationPoint[i] = point;
                                    if (i == 0) {
                                        indicationPoint[i].setBackgroundResource(R.drawable.dot_selected);
                                    } else {
                                        indicationPoint[i].setBackgroundResource(R.drawable.dot_normal);
                                    }
                                    mLlDot.addView(point);
                                }
                                adapter = new TabsPagerAdapter();
                                adapter.onBoundData(list);
                                mViewPager.setAdapter(adapter);
                                mViewPager.setOnPageChangeListener(ProductDetailAty.this);
                                mViewPager.setCurrentItem(0);
                                mHandler.sendEmptyMessageDelayed(1000, TIME);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            JSONObject goods = object.optJSONObject("goods");

                            freight.setText("运费:" + object.optString("FREIGHT"));
                            productName = goods.optString("GOODS_NAME");
                            productPrice = goods.optString("CURRENT_PRICE");
                            imageUrl = goods.optString("GOOD_BIG_PIC");
                            productFreight = object.optString("FREIGHT");
                            goodName.setText(productName);
                            goodPrice.setText("￥ "+productPrice);
                            count.setText("库存: "+goods.optString("STOCK_SALE_COUNT"));

                            if (object.optJSONArray("contents")!=null){
                                onParseData();//适配数据
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        },this);
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (list.size()>0){
            int newPosition =position % list.size();
            for (int i = 0; i < list.size(); i++) {
                if (i == newPosition) {
                    indicationPoint[i].setBackgroundResource(R.drawable.dot_selected);
                } else {
                    indicationPoint[i].setBackgroundResource(R.drawable.dot_normal);
                }
            }
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    private void onParseData() {
        int picCount = 0;
        DisplayImageOptions mDisplayImageOptions = DefaultConfigurationFactory
                .createGalleryDisplayImageOptions(this);
        DisplayImageOptions mDisplayVideoOptions = DefaultConfigurationFactory
                .createVideoDisplayImageOptions(this);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mImageLayout.setDividerDrawable(getResources().getDrawable(R.drawable.divider_horizontal_line));
        try {
            JSONArray jArray = object.optJSONArray("contents");
            for (int i = 0; i < jArray.length(); i++) {
                final JSONObject object = jArray.getJSONObject(i);

                if (object.optInt("CONTENT_TYPE") == 20) {//文字
                    TextView mNewContent = new TextView(this);
                    mNewContent.setTextColor(getResources().getColor(R.color.color_text1));
                    mNewContent.setTextSize(16);
                    mNewContent.setPadding(0, 20, 0, 20);
                    if (!HStringUtil.isEmpty(object.optString("CONTENT_INFO"))) {
                        handleFriendTalkContent(object.optString("CONTENT_INFO"), mNewContent);
                        mImageLayout.addView(mNewContent);
                    }
                }else if (object.optInt("CONTENT_TYPE") == 10) {//图片
                        final ImageView mImageView = new ImageView(this);
                        mImageView.setAdjustViewBounds(true);
                        mImageView.setLayoutParams(params);
                        mImageView.setPadding(0, 12, 0, 12);
                        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        mImageView.setMaxWidth(ScreenUtils.getScreenWidth(this));
                        mImageView.setMaxHeight(ScreenUtils.getScreenWidth(this) * 5);
                        mImageView.setTag(picCount);
                        picCount++;
                        urlList.add(object.optString("SMALL_PICTURE"));
                        mImageLoader.displayImage(object.optString("SMALL_PICTURE"), mImageView, mDisplayImageOptions);
                        mImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ProductDetailAty.this, ImageGalleryActivity.class);
                                intent.putExtra(ImageGalleryActivity.URLS_KEY, urlList.toArray(new String[urlList.size()]));
                                intent.putExtra(ImageGalleryActivity.TYPE_KEY, 1);// 0,1单个,多个
                                intent.putExtra(ImageGalleryActivity.POSITION, (Integer) mImageView.getTag());// 0,1单个,多个
                                startActivityForResult(intent, 100);
                            }
                        });
                        mImageLayout.addView(mImageView);

                    } else if (object.optInt("CONTENT_TYPE") == 30) {
                        final View view = LayoutInflater.from(this).inflate(R.layout.item_video, null);
                        ImageView mImageView = (ImageView) view.findViewById(R.id.image);
                        mImageView.setPadding(0, 12, 0, 12);
                        mImageLoader.displayImage(object.optString("PICTURE_URL_2X"), mImageView, mDisplayVideoOptions);
                        mImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(ProductDetailAty.this, InternetVideoDemo.class);
                                i.putExtra("url", object.optString("PICTURE_URL_2X"));
                                startActivity(i);
                            }
                        });
                        mImageLayout.addView(view);
                    }
                }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.shopping:
                Intent intent = new Intent(this,SureOrderActivity.class);
                intent.putExtra("GOODS_NAME",productName);
                intent.putExtra("GOOD_BIG_PIC",imageUrl);
                intent.putExtra("CURRENT_PRICE",productPrice);
                intent.putExtra("FREIGHT",productFreight);
                intent.putExtra("GOODID",goodId);
                startActivity(intent);
                break;

        }
    }

    private class TabsPagerAdapter extends PagerAdapter {
        public TabsPagerAdapter() {
            super();
            instance = ImageLoader.getInstance();
        }

        final List<JSONObject> datas = new ArrayList<JSONObject>();
        private ImageLoader instance;

        public void onBoundData(List<JSONObject> list) {
            datas.clear();
            datas.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final ImageView view = (ImageView) ProductDetailAty.this.getLayoutInflater().inflate(R.layout.main_viewpage_image,
                    null);
            if (datas.size() > 0) {
                int location = position % datas.size();
                container.addView(view);
                try {
                    final String url = datas.get(location).optString("BIG_PICTURE");
                    instance.displayImage(url, view);
                } catch (Exception e) {
                }
                return view;
            } else {
                return null;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }
    /**
     * 网络图片Getter
     */
    private NetworkImageGetter mImageGetter;
    private void handleFriendTalkContent(final String content, final TextView editText) {

        final WeakHandler handler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 0x101:
                        editText.setText((CharSequence) msg.obj);
                        break;
                }
                return false;
            }
        });
        // 因为从网上下载图片是耗时操作 所以要开启新线程
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                mImageGetter = new NetworkImageGetter(editText, content, handler);
                CharSequence test = Html.fromHtml(content, mImageGetter, null);
                Message msg = Message.obtain();
                msg.what = 0x101;
                msg.obj = test;
                handler.sendMessage(msg);

            }
        });
    }

    /**
     * 网络图片
     *
     * @author Susie
     */
    private final class NetworkImageGetter implements Html.ImageGetter {
        private TextView mEditText;
        private String content;
        private WeakHandler handler;

        public NetworkImageGetter(TextView mEditText, String content, WeakHandler handler) {
            this.mEditText = mEditText;
            this.content = content;
            this.handler = handler;
        }

        @Override
        public Drawable getDrawable(String source) {
            Drawable drawable = null;
            String handleSource = source;
            String[] sourceStrArray = null;
            if (source.contains("faces")) {
                sourceStrArray = handleSource.split("faces/");
            } else {
                sourceStrArray = handleSource.split("path=/");
            }
            // 封装路径
            if (sourceStrArray.length < 2) {
                return null;
            }
            String path = sourceStrArray[1].replace("/", "");
            File file = new File(Environment.getExternalStorageDirectory(), path);
            // 判断是否以http开头
            if (source.startsWith("http")) {
                // 判断路径是否存在
                if (file.exists()) {
                    // 存在即获取drawable
                    drawable = Drawable.createFromPath(file.getAbsolutePath());
                    if (drawable != null) {
                        int a = drawable.getIntrinsicWidth();
                        int b = drawable.getIntrinsicHeight();
                        drawable.setBounds(0, 0, 4 * a, 4 * b);
                    }
                } else {
                    // 不存在即开启异步任务加载网络图片
                    AsyncLoadNetworkPic networkPic = new AsyncLoadNetworkPic(mEditText, content);
                    networkPic.execute(source);
                }
            }


            return drawable;
        }
    }
    /**
     * 加载网络图片异步类
     *
     * @author Susie
     */
    private final class AsyncLoadNetworkPic extends AsyncTask<String, Integer, Void> {
        private TextView mEditText;
        private String content;

        public AsyncLoadNetworkPic(TextView editText, String content) {
            this.mEditText = editText;
            this.content = content;
        }

        @Override
        protected Void doInBackground(String... params) {
            // 加载网络图片
            loadNetPic(params);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // 当执行完成后再次为其设置一次
            mEditText.setText(Html.fromHtml(content, mImageGetter, null));
        }

        /**
         * 加载网络图片
         */
        private void loadNetPic(String... params) {
            String path = params[0];
            String handleSource = path;
            String[] sourceStrArray = null;
            if (handleSource.contains("faces/")) {
                sourceStrArray = handleSource.split("faces/");
            } else {
                sourceStrArray = handleSource.split("path=/");
            }

            if (sourceStrArray.length < 2) {
                return;
            }
            String paths = sourceStrArray[1].replace("/", "");
            File file = new File(Environment.getExternalStorageDirectory(), paths);

            InputStream in = null;

            FileOutputStream out = null;

            try {
                URL url = new URL(path);

                HttpURLConnection connUrl = (HttpURLConnection) url.openConnection();

                connUrl.setConnectTimeout(5000);

                connUrl.setRequestMethod("GET");

                if (connUrl.getResponseCode() == 200) {

                    in = connUrl.getInputStream();

                    out = new FileOutputStream(file);

                    byte[] buffer = new byte[1024];

                    int len;

                    while ((len = in.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                } else {
//                    Log.i(TAG, connUrl.getResponseCode() + "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
