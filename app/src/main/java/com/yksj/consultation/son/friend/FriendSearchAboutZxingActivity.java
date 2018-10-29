package com.yksj.consultation.son.friend;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.squareup.okhttp.Request;
import com.yksj.consultation.camera.CameraManager;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.comm.SingleBtnFragmentDialog.OnClickSureBtnListener;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.doctor.AtyDoctorMassage;
import com.yksj.consultation.son.home.DoctorStudioActivity;
import com.yksj.consultation.son.setting.SettingWebUIActivity;
import com.yksj.healthtalk.decoding.CaptureActivityHandler;
import com.yksj.healthtalk.decoding.InactivityTimer;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.JsonHttpResponseHandler;
import com.yksj.healthtalk.net.http.MyResultCallback;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.utils.FriendUtil;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.SystemUtils;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.Utils;
import com.yksj.healthtalk.views.ViewfinderView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 二维码查找
 *
 * @author Administrator
 */
public class FriendSearchAboutZxingActivity extends BaseFragmentActivity
        implements SurfaceHolder.Callback, OnClickListener,
        OnClickSureBtnListener {
    private static final float BEEP_VOLUME = 0.10f;
    private boolean playBeep;

    private boolean vibrate;
    private Vector<BarcodeFormat> decodeFormats;
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private MediaPlayer mediaPlayer;
    private boolean hasSurface;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private CameraManager mCameraManager;
    private String content;
    private SurfaceHolder surfaceHolder;
    private final int PHOTO_PICKED_WITH_DATA = 1;
    private final int QRCODE_ERROR = 2;
    private byte[] mContent;
    private ContentResolver resolver = null;
    // 那个二维码查询 +多一个这个
    // String type = request.getParameter("TYPE");// 0-社交场 1-医生馆
    private String type;// 0-社交场 1-医生馆 2 商户 3全局搜索   5 扫描二维码进入医生工作室

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.friend_search_zxing);
        initWidget();
        initData();
        CameraManager.init(getApplication());
        mCameraManager = CameraManager.get();
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra("type")) {// 0-社交场 1-医生馆
            type = intent.getStringExtra("type");
        } else {
            ToastUtil.showShort(getApplicationContext(), "type is null");
            finish();
        }
        titleTextV.setText("二维码扫描");
        titleRightBtn2.setVisibility(View.VISIBLE);
        titleRightBtn2.setText(R.string.photo_album);
        entity = SmartFoxClient.getLoginUserInfo();
    }

    private void initWidget() {
        initTitle();
        titleLeftBtn.setOnClickListener(this);
        titleRightBtn2.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
        // if (mCameraManager.isFlashOn()) {
        // mCameraManager.doSetFlash(false);
        // }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        if (photo != null) {
            photo.recycle();
        }
        super.onDestroy();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
            if (handler == null) {
                handler = new CaptureActivityHandler(this, decodeFormats,
                        characterSet);
            }
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    /**
     * 扫描完成
     *
     * @param obj
     * @param barcode
     * @author ddliu
     */
    public void handleDecode(Result obj, Bitmap barcode) {
        inactivityTimer.onActivity();
        viewfinderView.drawResultBitmap(barcode);
        playBeepSoundAndVibrate();
        String resultStr = obj.getText();
        // txtResult.setText(obj.getBarcodeFormat().toString() + ":" +
        // resultStr);
        // ToastUtil.showShort(getApplicationContext(), resultStr);
        parseResult(resultStr);
    }

    @Override
    protected void onStop() {
        // if (mCameraManager.isFlashOn()) {
        // mCameraManager.doSetFlash(false);
        // }
        super.onStop();
    }

    @Override
    protected void onStart() {
        // if (!mCameraManager.isFlashOn()) {
        // mCameraManager.doSetFlash(true);
        // }
        super.onStart();
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };
    private CustomerInfoEntity entity;
    private Bitmap photo;
    private DisplayMetrics dm;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.title_right2:
                if (!SystemUtils.getScdExit()) {
                    ToastUtil.showShort(this, "SD卡拔出,壹健康用户头像,语音,图片等功能不可用");
                    return;
                }
                Intent intent = getPhotoPickIntent();
                startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
                resolver = getContentResolver();
                break;
            default:
                break;
        }
    }

    /*
     * public Intent getPhotoPickIntent() { Intent intent = new
     * Intent(Intent.ACTION_GET_CONTENT, null); intent.setType("image/*");
     * intent.putExtra("return-data", true); return intent; }
     */
    private Intent getPhotoPickIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.putExtra("return-data", true);
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_PICKED_WITH_DATA && resultCode == RESULT_OK) {
            photo = data.getParcelableExtra("data");
            if (photo != null) {
                Result result = Utils.getTextFromImage(photo);
                parseResult(result);
            } else {
                try {
                    // 获得图片的uri
                    Uri originalUri = data.getData();
                    // 将图片内容解析成字节数组
                    mContent = readStream(resolver.openInputStream(Uri
                            .parse(originalUri.toString())));
                    // 将字节数组转换为ImageView可调用的Bitmap对象
                    photo = getPicFromBytes(mContent, null);
                    Result result = Utils.getTextFromImage(photo);
                    parseResult(result);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (photo == null) {
                    ToastUtil.showShort(getApplicationContext(),
                            R.string.canot_find_image);
                }

            }
        } else if (requestCode == QRCODE_ERROR && resultCode == RESULT_OK) {
            SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(),
                    "可能该用户不是医生,请尝试在朋友圈里面查找该用户"
            );
        }
    }

    public Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
                        opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }

    public byte[] readStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;
    }

    /**
     * 解析结果
     *
     * @param str
     */
    private void parseResult(String str) {
        // 判断是否是壹健康的二维码
//      String res = FriendUtil.isHealthQrCode(getApplicationContext(), str);
        if (HStringUtil.isEmpty(str)) {
            finish();
            return;
        }
        if (str.startsWith("liuyijiankang")) {//六一健康
            String[] temp = null;
            temp = str.split("_");

            if (temp.length == 3) {
                switch (type) {
                    case "6":
                        boundWX(temp[1]);
                        break;
                    case "5"://医生工作室
                        Intent intent = new Intent(this, DoctorStudioActivity.class);
                        intent.putExtra("DOCTOR_ID", temp[1]);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        } else {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri weibo_url = Uri.parse(str);
            intent.setData(weibo_url);
            startActivity(intent);


//            // 进入商户扫描二维码
//            if (!type.equals("2") && !type.equals("3")) {
//                if (!TextUtils.isEmpty(res) && res.equalsIgnoreCase("cm")) {
//                    SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(),
//                            "您的二维码不是服务区中的单位"
//                    );
//                } else if (!TextUtils.isEmpty(res) && res.equalsIgnoreCase("code")) {
//                    String code = FriendUtil.getQrCode(str);
////					FindCustomerInfoByCustId(code);
//                } else {
////					Intent intent = new Intent(this, QrCodeResultActivity.class);
////					intent.putExtra("result", str);
////					startActivity(intent);
//
//                }
//            } else if (type.equals("2")) {
//                if (!TextUtils.isEmpty(res) && res.equalsIgnoreCase("code")) {
//                    SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(),
//                            "请到朋友交流或者专家医生中试试"
//                    );
//                } else if (!TextUtils.isEmpty(res) && res.equalsIgnoreCase("cm")) {
//                    String code = FriendUtil.getQrCode(str);
//                    if (!TextUtils.isEmpty(code)) {
//                        FindServerMerchant(code);
//                    }
//
//                }
//            } else if (type.equals("3")) {//全局
//                if (!TextUtils.isEmpty(res) && res.equalsIgnoreCase("cm")) {
//                    String code = FriendUtil.getQrCode(str);
//                    if (!TextUtils.isEmpty(code)) {
//                        FindServerMerchant(code);
//                    }
//                } else if (!TextUtils.isEmpty(res) && res.equalsIgnoreCase("code")) {
//                    String code = FriendUtil.getQrCode(str);
//                    String type = FriendUtil.getQrDoctorClientType(str);
//                    FindCustomerInfoByCustId(code, type);
//                } else {
//                    Intent intent = new Intent(this, QrCodeResultActivity.class);
//                    intent.putExtra("result", str);
//                    startActivity(intent);
//                }
//            }
        }
    }

    // 心意健康扫描用户
    private void FindCustomerInfoByCustId(String code, String clientType) {
        HttpRestClient.doHttpFindCustomerByQrCode(code, clientType, new MyResultCallback<JSONObject>() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(JSONObject response) {
                super.onResponse(response);
                if (response != null) {
                    if ("1".equals(response.optString("code"))) {
                        JSONObject object = response.optJSONObject("result");
                        if (object != null) {
                            if ("10".equals(object.optString("DOCTOR_ROLE"))) {
                                Intent intent = new Intent(FriendSearchAboutZxingActivity.this, AtyDoctorMassage.class);
                                intent.putExtra("type", 0);//专家
                                intent.putExtra("id", object.optString("CUSTOMER_ID"));
                                intent.putExtra("CLINIC", "CLINIC");
                                startActivity(intent);
                            } else if ("20".equals(object.optString("DOCTOR_ROLE"))) {
                                Intent intent = new Intent(FriendSearchAboutZxingActivity.this, AtyDoctorMassage.class);
                                intent.putExtra("type", 1);//医生
                                intent.putExtra("ORDER", 1);
                                intent.putExtra("id", object.optString("CUSTOMER_ID"));
                                startActivity(intent);
                            } else {
                                ToastUtil.showShort(getApplicationContext(), R.string.request_error);
                                finish();
                            }
                        } else {
                            ToastUtil.showShort(getApplicationContext(), R.string.request_error);
                            finish();
                        }
                    } else {
                        Intent intent = new Intent(FriendSearchAboutZxingActivity.this, SettingWebUIActivity.class);
                        intent.putExtra("url", response.optString("result"));
                        startActivity(intent);
//						ToastUtil.showShort(getApplicationContext(),response.optString("message"));
                    }
                } else {
                    ToastUtil.showShort(getApplicationContext(), R.string.request_error);
                    finish();
                }
            }
        }, this);
    }
    //老版壹健康扫描用户
//	private void FindCustomerInfoByCustId(String code) {
//		HttpRestClient.doHttpFindCustomerInfoByCustId(type, code, null,
//				SmartFoxClient.getLoginUserId(),
//				new ObjectHttpResponseHandler(this) {
//					@Override
//					public Object onParseResponse(String content) {
//						CustomerInfoEntity entity = null;
//						if (TextUtils.isEmpty(content)
//								&& content.equalsIgnoreCase("N")) {
//							ToastUtil.showShort(getApplicationContext(),
//									R.string.request_error);
//						} else {
//							try {
//								if (content.contains("error_message")) {
//									return content;
//								} else {
//									entity = DataParseUtil
//											.jsonToCustmerInfo(new JSONObject(
//													content));
//								}
//							} catch (JSONException e) {
//							}
//						}
//						return entity;
//					}
//
//					@Override
//					public void onSuccess(int statusCode, Object response) {
//						if (response != null) {
//							if (response instanceof CustomerInfoEntity) {
//								CustomerInfoEntity entity = (CustomerInfoEntity) response;
//								PersonInfoUtil.choiceActivity(entity.getId(), FriendSearchAboutZxingActivity.this, String.valueOf(entity.getRoldid()));
////                            if (!mInfoEntity.getId().equals(SmartFoxClient.getLoginUserId())) {
////								if (mInfoEntity.isDoctor()) {
////									Intent intent = new Intent(getApplicationContext(),DoctorMainActivity.class);
////									intent.putExtra("id", mInfoEntity.getId());
////									intent.putExtra("type", "0");
////									startActivity(intent);
////								}else{
////									Intent intent = new Intent(getApplicationContext(),PersonInfoActivity.class);
////									intent.putExtra("PersonInfo", mInfoEntity);
////									startActivity(intent);}
////								} else {
////									Intent intent = new Intent(getApplicationContext(),PersonInfoActivity.class);
////									startActivity(intent);
////								}
//							} else if (response instanceof String) {
//								JSONObject object;
//								try {
//									object = new JSONObject(String.valueOf(response));
//									SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), object.getString("error_message")
//											);
//								} catch (JSONException e) {
//									// TODO Auto-generated catch
//									// block
//									e.printStackTrace();
//								}
//							}
//						} else {
//						}
//						super.onSuccess(statusCode, response);
//					}
//				});
//	}


    // 扫描商户
    public void FindServerMerchant(String code) {
        HttpRestClient.doHttpServerMerchant(String.valueOf(dm.widthPixels),
                String.valueOf(dm.heightPixels),
                SmartFoxClient.getLoginUserId(), code,
                new JsonHttpResponseHandler(this) {
                    private Bundle mBundle;

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        String str = response.optString("VALID_FLAG", "2");
                        String merchantId = response.optString("MERCHANT_ID");
                        if ("2".equals(str) || TextUtils.isEmpty(merchantId)) {// 不可以进入
                            SingleBtnFragmentDialog.showDefault(
                                    getSupportFragmentManager(),
                                    "即将开通,敬请期待!");
                        } else {
                            mBundle = new Bundle();
                            mBundle.putString("content", response.toString());
                            mBundle.putString("MERCHANT_ID", merchantId);
                        }
                        super.onSuccess(statusCode, response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    private void parseResult(Result result) {
        if (result == null || TextUtils.isEmpty(result.toString())) {
            ToastUtil.showShort(getApplicationContext(), R.string.wrong_format);
        } else {
            // 判断是否是壹健康的二维码
            parseResult(result.toString());
        }
    }

    @Override
    public void onClickSureHander() {
        viewfinderView.drawResultBitmap(null);
        if (handler != null) {
            handler.sendEmptyMessage(R.id.restart_preview);
        }
    }

    /**
     * 绑定微信
     */
    private void boundWX(String id) {
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("customer_id", id));
        pairs.add(new BasicNameValuePair("rel_customer_id", LoginServiceManeger.instance().getLoginEntity().getId()));
        HttpRestClient.OKHttpBound(pairs, new MyResultCallback<JSONObject>(this) {

            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    if ("0".equals(response.optString("code"))) {
                        ToastUtil.showShort(response.optString("message"));
                        finish();
                    } else {
                        ToastUtil.showShort(response.optString("message"));
                    }
                }
            }
        }, this);
    }
}