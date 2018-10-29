package com.yksj.consultation.son.login;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.healthtalk.utils.LogUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;


import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import de.greenrobot.event.EventBus;

public class AtyThirdLogin extends BaseFragmentActivity implements View.OnClickListener, Handler.Callback {
    private TextView mUserType, mUserName;
    private ImageView mUserHead;
    private static final int INTENT_ACTION_PICTURE = 0;
    private static final int INTENT_ACTION_CAREMA = 1;
    private static final int INTENT_ACTION_CROP = 2;
    private static final int LOAD_USER_ICON = 3;
    private static final String PICTURE_NAME = "UserIcon.jpg";
    private static String tmpPlatform;
    private Platform platform;
    //    private ImageView ivUserIcon, ivBoy, ivGril;
    private String picturePath; //图片路径
    String str = "";
    private Bundle bundle;

//    public static final void setOnLoginListener(OnLoginListener login) {
//        tmpRegisterListener = login;
//    }

    public static final void setPlatform(String platName) {
        tmpPlatform = platName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ShareSDK.initSDK(this);
        platform = ShareSDK.getPlatform(tmpPlatform);
        tmpPlatform = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_thirdlogin);
        EventBus.getDefault().register(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {

        initTitle();
        titleLeftBtn.setOnClickListener(this);
        titleTextV.setText("QQ登录");
        mUserType = (TextView) findViewById(R.id.tv_usertype);
        mUserName = (TextView) findViewById(R.id.tv_username);
        mUserHead = (ImageView) findViewById(R.id.image_head);
        findViewById(R.id.register).setOnClickListener(this);
        findViewById(R.id.connect).setOnClickListener(this);
        initData();
    }

    /*初始化用户数据*/
    private void initData() {
        String gender = "";
        if (platform != null) {
            gender = platform.getDb().getUserGender();
            if (gender.equals("m")) {
//                    userInfo.setUserGender(UserInfo.Gender.MALE);
//                    ivBoy.setVisibility(View.VISIBLE);
//                    ivGril.setVisibility(View.INVISIBLE);
            } else {
//                    userInfo.setUserGender(UserInfo.Gender.FEMALE);
//                    ivBoy.setVisibility(View.INVISIBLE);
//                    ivGril.setVisibility(View.VISIBLE);
            }
//                userInfo.setUserIcon(platform.getDb().getUserIcon());
//                userInfo.setUserName(platform.getDb().getUserName());
//                userInfo.setUserNote(platform.getDb().getUserId());
            if (QQ.NAME.equals(platform.getDb().getPlatformNname())) {
                str = QQ.NAME;
            } else if (SinaWeibo.NAME.equals(platform.getDb().getPlatformNname())) {
                str = "新浪";
            } else if (Wechat.NAME.equals(platform.getDb().getPlatformNname())) {
                str = "微信";
            }
//        mUserType.setText("亲爱的" + platform.getDb().getPlatformNname() + "用户: ");
            mUserType.setText("亲爱的" + str + "用户: ");
            mUserName.setText(platform.getDb().getUserName());
            // 加载头像
            if (!TextUtils.isEmpty(platform.getDb().getUserIcon())) {
                loadIcon();
            }
        }

        //先把getUserIcon获取到的图片保存在本地，再通过ImageView调用显示出来
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String thumPicture = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getPackageName() + "/download";
            File pictureParent = new File(thumPicture);
            File pictureFile = new File(pictureParent, PICTURE_NAME);

            if (!pictureParent.exists()) {
                pictureParent.mkdirs();
            }
            try {
                if (!pictureFile.exists()) {
                    pictureFile.createNewFile();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //最终图片路径
            picturePath = pictureFile.getAbsolutePath();
            LogUtil.d("picturePath ==>>", picturePath);
        } else {
            LogUtil.d("change user icon ==>>", "there is not sdcard!");
        }

        bundle = new Bundle();
        bundle.putString("PLATFORM_NAME", str);
        bundle.putLong("EXPIRESIN", platform.getDb().getExpiresIn());
        bundle.putLong("EXPIRESTIME", platform.getDb().getExpiresTime());
        bundle.putString("TOKEN", platform.getDb().getToken());
        bundle.putString("TOKENSECRET", platform.getDb().getTokenSecret());
        bundle.putString("USERGENDER", platform.getDb().getUserGender());
        bundle.putString("USERICON", platform.getDb().getUserIcon());
        bundle.putString("USERNAME", platform.getDb().getUserName());
        bundle.putString("USERID", platform.getDb().getUserId());

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.register:
                intent = new Intent(this, RegisterActivity.class);
                intent.putExtras(bundle);
                intent.putExtra("registerType", "1");
                startActivity(intent);
                break;
            case R.id.connect:
                intent = new Intent(this, AtyLoginBound.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case LOAD_USER_ICON:
//                    ivUserIcon.setImageURI(Uri.parse(picturePath));
                break;
        }
        return false;
    }

    /**
     * 加载用户登陆后返回的头像
     */
    private void loadIcon() {
        final String imageUrl = platform.getDb().getUserIcon();
        new Thread(new Runnable() {
            public void run() {
                try {
                    URL picUrl = new URL(imageUrl);
                    Bitmap userIcon = BitmapFactory.decodeStream(picUrl.openStream());
                    FileOutputStream b = null;
                    try {
                        b = new FileOutputStream(picturePath);
                        userIcon.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            b.flush();
                            b.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Message msg = new Message();
                    msg.what = LOAD_USER_ICON;
                    mUserHead.post(new Runnable() {
                        public void run() {
                            mUserHead.setImageURI(Uri.parse(picturePath));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 接受返回的图片源
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INTENT_ACTION_PICTURE && resultCode == RESULT_OK && null != data) {
            Cursor c = getBaseContext().getContentResolver().query(data.getData(), null, null, null, null);
            c.moveToNext();
            String path = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
            c.close();
            if (new File(path).exists()) {
                Log.d(getClass().getSimpleName(), "onActivityResult == " + path + " == exist");
//                    userInfo.setUserIcon(path);
//                    ivUserIcon.setImageBitmap(Tool.compressImageFromFile(path));
            }
        } else if (requestCode == INTENT_ACTION_CAREMA && resultCode == RESULT_OK) {
//                userInfo.setUserIcon(picturePath);
//                ivUserIcon.setImageDrawable(Drawable.createFromPath(picturePath));
        } else if (requestCode == INTENT_ACTION_CROP && resultCode == RESULT_OK && null != data) {
//                ivUserIcon.setImageDrawable(Drawable.createFromPath(picturePath));
        }
    }

    /*从相册获取图片，把一张本地图片当做头像*/
    private void getPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, INTENT_ACTION_PICTURE);
    }


    /**
     * 登录之后,会调用此方法
     *
     * @param log
     */
    public void onEventMainThread(MyEvent log) {
        if (log.code == 12||log.code==14) {//绑定登录成功后
            finish();
        }
    }
}
