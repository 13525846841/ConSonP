package com.yksj.consultation.son.consultation.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.WheelUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 添加就诊人
 */
public class AddPersonSeekAty extends BaseFragmentActivity implements View.OnClickListener {

    private View mainView;
    private PopupWindow pop;
    private View wheelView;
    private TextView CURRENTVIEW;
    private TextView sexEdit;
    private TextView ageEdit;
    private TextView allergyText;//过敏史
    private String customerId = "";
    private EditText name, phone, idCard;
    private static final int WARNING = 1;
    private String text = "";//过敏史

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person_seek_aty);

        initView();
    }

    private void initView() {
        initTitle();
        titleLeftBtn.setOnClickListener(this);
        titleTextV.setText("添加就诊人");

        sexEdit = (TextView) findViewById(R.id.sex);
        ageEdit = (TextView) findViewById(R.id.age);
        allergyText = (TextView) findViewById(R.id.tv_text);
        name = (EditText) findViewById(R.id.edit_name);
        phone = (EditText) findViewById(R.id.edit_phone);
        idCard = (EditText) findViewById(R.id.edit_id);

        findViewById(R.id.rl_sex).setOnClickListener(this);//性别
        findViewById(R.id.rl_time).setOnClickListener(this);//年龄
        findViewById(R.id.rl_allergy).setOnClickListener(this);
        findViewById(R.id.tv_text).setOnClickListener(this);

        findViewById(R.id.modify).setOnClickListener(this);//确定

        mainView = getLayoutInflater().inflate(R.layout.full_person_message, null);
        wheelView = getLayoutInflater().inflate(R.layout.wheel, null);
        wheelView.findViewById(R.id.wheel_cancel).setOnClickListener(this);
        wheelView.findViewById(R.id.wheel_sure).setOnClickListener(this);
        pop = new PopupWindow(wheelView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        customerId = LoginServiceManeger.instance().getLoginEntity().getId();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.wheel_cancel:
                if (pop != null)
                    pop.dismiss();
                break;
            case R.id.modify:
                save();
                break;

            case R.id.wheel_sure:
                if (pop != null)
                    pop.dismiss();
                if (WheelUtils.getCurrent() != null) {
                    setText();
                }
                break;
            case R.id.rl_sex://性别
                closeKeyBoard();
                CURRENTVIEW = sexEdit;
                setXingbie();
                break;
            case R.id.rl_time://性别
                closeKeyBoard();
                CURRENTVIEW = ageEdit;
                setAge();
                break;
            case R.id.rl_allergy://过敏史
            case R.id.tv_text://过敏史
                Intent intent = new Intent(this, AddTextActivity.class);
                intent.putExtra(AddTextActivity.TYPE, 1);
                intent.putExtra(AddTextActivity.CONTENTS, text);
                startActivityForResult(intent, WARNING);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case WARNING:
                if (resultCode == RESULT_OK) {
                    text = data.getStringExtra("content");
                    allergyText.setText(text);
                }
                break;
            default:
                break;
        }
    }

    private void save() {
        if (TextUtils.isEmpty(name.getText().toString())) {
            SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "请填写姓名");
            return;
        }
        if (TextUtils.isEmpty(sexEdit.getText().toString().trim())) {
            SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "请填写性别");
            return;
        }
        if (TextUtils.isEmpty(ageEdit.getText().toString().trim())) {
            SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "请填写年龄");
            return;
        }
        if (TextUtils.isEmpty(phone.getText().toString())) {
            SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "请填写电话号码");
            return;
        }
        if (!isMobileNO(phone.getText().toString())) {
            SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "请填写正确电话号码");
//            ToastUtil.showToastPanl("请填写正确电话号码");
            return;
        }
        if (TextUtils.isEmpty(idCard.getText().toString())) {
            SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "请填写身份证号");
//            ToastUtil.showToastPanl("请填写身份证号");
            return;
        }
        if (!IDCardValidate(idCard.getText().toString())) {
            SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "请填写正确的身份证号");
//            ToastUtil.showToastPanl("请填写正确的身份证号");
            return;
        }
        if (TextUtils.isEmpty(text)) {
            SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "请填写过敏史");
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("op", "createVisitingPerson");
        map.put("customer_id", customerId);
        map.put("person_name", name.getText().toString());
        if ("女".equals(sexEdit.getText().toString().trim())) {
            map.put("person_sex", "W");
        } else if ("男".equals(sexEdit.getText().toString().trim())) {
            map.put("person_sex", "M");
        }
        map.put("person_age", ageEdit.getText().toString().trim());
        map.put("person_phone", phone.getText().toString());
        map.put("person_identity", idCard.getText().toString());
        map.put("person_allergy", text);
        HttpRestClient.OKHttPersonSeek(map, new HResultCallback<String>(this) {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if ("1".equals(obj.optString("code"))) {
                        ToastUtil.showShort(obj.optString("message"));
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, this);
    }

    /**
     * 设置年龄
     */
    private void setAge() {
        String[] ageList = new String[100];
        for (int i = 0; i < 100; i++) {
            ageList[i] = i + 1 + "";
        }
        WheelUtils.setSingleWheel(this, ageList, mainView, pop, wheelView,
                false);

    }

    /**
     * 设置性别
     */
    private void setXingbie() {
        String[] xingbie = getResources().getStringArray(R.array.sex);
        WheelUtils.setSingleWheel(this, xingbie, mainView, pop, wheelView,
                false);
    }

    /**
     * 设置内容
     */
    public void setText() {
        if (CURRENTVIEW.equals(ageEdit)) {
            ageEdit.setText(WheelUtils.getCurrent());
        } else if (CURRENTVIEW.equals(sexEdit)) {
            sexEdit.setText(WheelUtils.getCurrent());
        }
    }

    /**
     * 关闭软键盘
     */
    private void closeKeyBoard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // 手機號碼驗證
    public static boolean isMobileNO(String mobiles) {
/**
 * 匹配以下开头的号码 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
 * 增加183
 ***/
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,3,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 功能：身份证的有效验证
     *
     * @param IDStr 身份证号 [url=home.php?mod=space&uid=7300]@return[/url] 有效：返回""
     *              无效：返回String信息
     */
    public static boolean IDCardValidate(String IDStr) {
        @SuppressWarnings("unused")
        String errorInfo = "";// 记录错误信息
        String[] ValCodeArr = {"1", "0", "X", "9", "8", "7", "6", "5", "4",
                "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
                "9", "10", "5", "8", "4", "2"};
        String Ai = "";
// ================ 号码的长度 15位或18位 ================
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            errorInfo = "身份证号码长度应该为15位或18位。";
            return false;
        }
// =======================(end)========================
// ================ 数字 除最后以为都为数字 ================
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        } else if (IDStr.length() == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }
        if (isNumeric(Ai) == false) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            return false;
        }
// =======================(end)========================
// ================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 月份
        if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
            errorInfo = "身份证生日无效。";
            return false;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                    || (gc.getTime().getTime() - s.parse(
                    strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                errorInfo = "身份证生日不在有效范围。";
                return false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errorInfo = "身份证月份无效";
            return false;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errorInfo = "身份证日期无效";
            return false;
        }
// =====================(end)=====================
// ================ 地区码时候有效 ================
        Map<String, String> h = GetAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
            errorInfo = "身份证地区编码错误。";
            return false;
        }
// ==============================================
// ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi
                    + Integer.parseInt(String.valueOf(Ai.charAt(i)))
                    * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;
        if (IDStr.length() == 18) {
            if (Ai.equals(IDStr) == false) {
                errorInfo = "身份证无效，不是合法的身份证号码";
                return false;
            }
        }
// =====================(end)=====================
        return true;
    }

    /**
     * 功能：设置地区编码
     *
     * @return Hashtable 对象
     */
    private static Map<String, String> GetAreaCode() {
        Map<String, String> hashtable = new HashMap<String, String>();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }

    /**
     * 功能：判断字符串是否为数字
     *
     * @param str
     * @return
     */
    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 功能：判断字符串是否为日期格式
     *
     * @param
     * @return
     */
    public static boolean isDate(String strDate) {
        String regxStr = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        Pattern pattern = Pattern.compile(regxStr);
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }
}
