package com.yksj.healthtalk.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.yksj.consultation.adapter.ArrayWheelAdapter;
import com.yksj.consultation.adapter.SeleteAdapter;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.views.EditTextBox;
import com.yksj.consultation.son.views.EditTextBox.OnBottomListener;
import com.yksj.consultation.son.views.OnWheelChangedListener;
import com.yksj.consultation.son.views.WheelView;

public class WheelUtils {
    private static String current;
    private static String current1;
    private static String current2;
    private static List<String> list;
    private static String firstName;
    private static String code;
    private static String firstCode;
    //private static String firstName;
    private static int part = 2;
    private static int cate = 0;
    private static int item = 0;

    private static int ageIndex = 0;
    private static Map<String, String> partMap;
    private static String limitNo = "";


    /**
     * 时间选择
     */
    public static PopupWindow showDateSelect(Context context, LayoutInflater inflater, final OnClickListener listener) {
        View view = inflater.inflate(R.layout.wheel, null);
        final WheelView wheel1 = (WheelView) view.findViewById(R.id.wheel);
        final WheelView wheel2 = (WheelView) view.findViewById(R.id.wheel_right);
        final DecimalFormat mDecimalFormat = new DecimalFormat("00");
        List<HashMap> hourList = new ArrayList<HashMap>();
        List<String> cacheDate = new ArrayList<String>();
        for (int i = 0; i < 24; i++) {
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("name", String.valueOf(mDecimalFormat.format(i)));
            hourList.add(hashMap);
            cacheDate.add(String.valueOf(mDecimalFormat.format(i)));
        }
        wheel1.setTag(cacheDate);
        List<HashMap> minueList = new ArrayList<HashMap>();
        List<String> minueListDate = new ArrayList<String>();
        for (int i = 0; i < 12; i++) {
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("name", String.valueOf(mDecimalFormat.format(i * 5)));
            minueList.add(hashMap);
            minueListDate.add(String.valueOf(mDecimalFormat.format(i * 5)));
        }
        wheel2.setTag(minueListDate);
        wheel1.setAdapter(new ArrayWheelAdapter(hourList, hourList.size()));
        wheel2.setAdapter(new ArrayWheelAdapter(minueList, minueList.size()));
        setValHeight(context, view);
        final PopupWindow pop = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setPopeWindow(context, view, pop);
        Button wheelCancel = (Button) view.findViewById(R.id.wheel_cancel);
        wheelCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });
        Button wheelSure = (Button) view.findViewById(R.id.wheel_sure);
        wheelSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    String hour = wheel1.getAdapter().getItem(wheel1.getCurrentItem());
                    String minute = wheel2.getAdapter().getItem(wheel2.getCurrentItem());
                    v.setTag(new String[]{hour, minute});
                    listener.onClick(v);
                }
                pop.dismiss();
            }
        });

        return pop;


    }

    /**
     * 显示选择原因的内容，非滚轮，为退款选择理由的内容
     *
     * @param context
     * @param list
     * @param
     * @param
     * @param
     * @param
     */
    public static PopupWindow showReason(
            final Context context,
            final List<Map<String, String>> list,
            final View parentView,
            final OnClickListener listener) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View wheelView = inflater.inflate(R.layout.selete_reason, null);
        final PopupWindow pop = new PopupWindow(wheelView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final ListView listview = (ListView) wheelView.findViewById(R.id.listview_reason);
        final SeleteAdapter adapter = new SeleteAdapter(context, list, list.size());
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    view.setTag(R.id.listview_reason, position);
                    listener.onClick(view);
                }
                pop.dismiss();
            }
        });
        final Button wheelOne = (Button) wheelView.findViewById(R.id.close_for);
        wheelOne.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });
        pop.setFocusable(true);
        pop.setAnimationStyle(R.style.AnimationPreview);
        pop.setTouchable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setOutsideTouchable(true);
        pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
        return pop;
    }

    /**
     * 设置单滚轮
     *
     * @param context
     * @param str       数据源
     * @param mainView  pop在此View上显示
     * @param pop       pop窗口
     * @param wheelView pop中显示的View
     * @param isCyc     是否循环
     */
    public static void setSingleWheel(Context context, final Object[] str, View mainView, PopupWindow pop, View wheelView, boolean isCyc) {
        current = (String) str[0];
        current1 = null;
        current2 = null;
        item = 0;
        setValHeight(context, wheelView);
        WheelView wheel = (WheelView) wheelView.findViewById(R.id.wheel);
        final WheelView wheelRight = (WheelView) wheelView.findViewById(R.id.wheel_right);

        wheel.removeAllChangingListener();
        wheel.setAdapter(new ArrayWheelAdapter(str, str.length));
        WheelUtils.setWheel(wheel, isCyc);
        wheel.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                current = (String) str[newValue];
                item = newValue;
            }
        });
        wheelRight.setVisibility(View.GONE);
        WheelUtils.setPopeWindow(context, mainView, pop);
    }

    /**
     * 设置双滚轮(不关联)
     */
    public static void setDoubleWheel1(Context context,
                                       final List<Map<String, String>> list,
                                       final List<Map<String, String>> list1,
                                       View mainView, PopupWindow pop, View wheelView) {
        limitNo = context.getString(R.string.limit_no);
        current = null;
        current1 = null;
        current2 = null;
        item = 0;
        if (list.size() == 0) {
            return;
        }
        setValHeight(context, wheelView);
        WheelView wheel = (WheelView) wheelView.findViewById(R.id.wheel);
        final WheelView wheelRight = (WheelView) wheelView.findViewById(R.id.wheel_right);
        wheelRight.setVisibility(View.VISIBLE);
        wheel.removeAllChangingListener();
        wheel.setAdapter(new ArrayWheelAdapter(list, list.size()));
        setWheel(wheel, false);
//		wheel.setVisibleItems(9);
        firstCode = list.get(0).get("code");
        //firstName = list.get(0).get("name");
        wheel.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                cate = newValue;
                firstName = list.get(newValue).get("name");
                firstCode = list.get(newValue).get("code");
                current = firstName + list1.get(0).get("name");
                current1 = firstName;
                current2 = list1.get(0).get("name");
                code = firstCode;

            }
        });

        wheelRight.removeAllChangingListener();
        firstName = list.get(0).get("name");
        current1 = firstName;
        current2 = list1.get(0).get("name");
        current = firstName + list1.get(0).get("name");
        code = firstCode;

        wheelRight.setAdapter(new ArrayWheelAdapter(list1, list1.size()));
        setWheel(wheelRight, false);
//		wheelRight.setVisibleItems(9);
        wheelRight.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                current = firstName + list1.get(newValue).get("name");
                current1 = firstName;
                current2 = list1.get(newValue).get("name");
                code = firstCode;

            }
        });
        if (pop != null)
            setPopeWindow(context, mainView, pop);

    }

    /**
     * @return
     * @name 年
     * @month 月 List<List<String>>
     * 滚轮日历  从现在开始 往前100年
     */
    private static List<Map<String, Object>> getCalendarList() {
        List<Map<String, Object>> yearsList = new ArrayList<Map<String, Object>>();
        int yearDistance = 100;//相距现在多少年
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), 0, 0, 0, 0, 0);
        calendar.add(Calendar.YEAR, -yearDistance);
        calendar.set(Calendar.MONTH, 0);

        while (yearDistance > -2) {
            int moths = 0;
            //年
            Map<String, Object> yearMap = new HashMap<String, Object>();
            yearsList.add(yearMap);
            String addYear = String.valueOf(calendar.get(Calendar.YEAR)) + "年";
            int add_Year = calendar.get(Calendar.YEAR);
            yearMap.put("name", addYear);
            List<List<Map<String, String>>> monthsList = new ArrayList<List<Map<String, String>>>();
            yearMap.put("month", monthsList);
            Calendar calendar2 = Calendar.getInstance();
            int year = calendar2.get(Calendar.YEAR);
            int month = calendar2.get(Calendar.MONTH);
            int day = calendar2.get(Calendar.DAY_OF_MONTH);

            if (year == add_Year) {
                while (moths < month + 1) {
                    int add_day = 0;
                    if (moths == month) {
                        add_day = day;
                    } else {
                        add_day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    }

                    List<Map<String, String>> daysList = new ArrayList<Map<String, String>>();
                    //一个月多少天
                    for (int i = 1; i < add_day + 1; i++) {
                        Map<String, String> map = new HashMap<String, String>();
                        if (i < 10) {
                            map.put("name", "0" + i + "日");
                        } else {
                            map.put("name", i + "日");
                        }
                        daysList.add(map);
                    }
                    monthsList.add(daysList);
                    ++moths;
                    calendar.add(Calendar.MONTH, 1);
                }
            } else {
                //计算一月多少天
                while (moths < 12) {
                    List<Map<String, String>> daysList = new ArrayList<Map<String, String>>();
                    //一个月多少天
                    int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) + 1;
                    for (int i = 1; i < days; i++) {
                        Map<String, String> map = new HashMap<String, String>();
                        if (i < 10) {
                            map.put("name", "0" + i + "日");
                        } else {
                            map.put("name", i + "日");
                        }
                        daysList.add(map);
                    }
                    monthsList.add(daysList);
                    ++moths;
                    calendar.add(Calendar.MONTH, 1);
                }
            }


            --yearDistance;
        }
        return yearsList;
    }

    /**
     * @return
     * @name 年
     * @month 月 List<List<String>>
     * 从现在开始 往后10年
     */
    private static List<Map<String, Object>> getCalendarFromNowList() {
        List<Map<String, Object>> yearsList = new ArrayList<Map<String, Object>>();
        int yearDistance = 0;//相距现在多少年
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), 0, 0, 0, 0, 0);
        calendar.add(Calendar.YEAR, -yearDistance);
        calendar.set(Calendar.MONTH, 0);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);

        while (yearDistance < 10) {
            int moths = 0;
            //年
            Map<String, Object> yearMap = new HashMap<String, Object>();
            yearsList.add(yearMap);
            yearMap.put("name", String.valueOf(calendar.get(Calendar.YEAR)) + "年");
            List<List<Map<String, String>>> monthsList = new ArrayList<List<Map<String, String>>>();
            yearMap.put("month", monthsList);
            //计算一月多少天
            while (moths < month + 2) {
                List<Map<String, String>> daysList = new ArrayList<Map<String, String>>();
                //一个月多少天
                int days = 0;
                if (moths < month) {
                    days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) + 1;
                } else {
                    days = day;
                }
                for (int i = 1; i < days; i++) {
                    Map<String, String> map = new HashMap<String, String>();
                    if (i < 10) {
                        map.put("name", "0" + i + "日");
                    } else {
                        map.put("name", i + "日");
                    }
                    daysList.add(map);
                }
                monthsList.add(daysList);
                ++moths;
                calendar.add(Calendar.MONTH, 1);
            }
            ++yearDistance;
        }
        return yearsList;
    }

    /**
     * 从现在开始 往后100年
     * 显示时间三级菜单 确定获取数据 getTag() == str[]{年,月,日}
     *
     * @param context
     * @param view
     * @param listener
     * @return
     */
    public static PopupWindow showThreeDateWheelFromNow(final Context context, final View view, final OnClickListener listener) {
        final List<Map<String, Object>> list = getCalendarFromNowList();
        final List<Map<String, Object>> monthList = new ArrayList<Map<String, Object>>();
        for (int i = 1; i <= 12; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            if (i < 10) {
                map.put("name", "0" + i + "月");
            } else {
                map.put("name", i + "月");
            }
            monthList.add(map);
        }
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View wheelView = inflater.inflate(R.layout.agewheel, null);
        final PopupWindow pop = new PopupWindow(wheelView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final WheelView wheelOne = (WheelView) wheelView.findViewById(R.id.wheel_left);
        final WheelView wheelTwo = (WheelView) wheelView.findViewById(R.id.wheel_middle);
        final WheelView wheelThree = (WheelView) wheelView.findViewById(R.id.wheel_right1);
        Button wheelCancel = (Button) wheelView.findViewById(R.id.wheel_cancel_age);
        Button wheelSure = (Button) wheelView.findViewById(R.id.wheel_sure_age);
        wheelCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pop.isShowing()) pop.dismiss();
            }
        });
        wheelSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    //年
                    int indexYear = wheelOne.getCurrentItem();
                    //月
                    int indexMonth = wheelTwo.getCurrentItem();
                    //天
                    int indexDay = wheelThree.getCurrentItem();
                    Map<String, Object> map = list.get(indexYear);
                    List<List<Map<String, String>>> data = (List<List<Map<String, String>>>) map.get("month");
                    List<Map<String, String>> daysList = data.get(indexMonth);

                    String yearName = list.get(indexYear).get("name").toString();
                    String monthName = monthList.get(indexMonth).get("name").toString();
                    String dayName = daysList.get(indexDay).get("name");
                    String str[] = new String[]{yearName, monthName, dayName};
                    v.setTag(str);
                    listener.onClick(v);
                }
                if (pop.isShowing()) pop.dismiss();
            }
        });
        //年
        wheelOne.setAdapter(new ArrayWheelAdapter(list, list.size()));
        wheelOne.setCurrentItem(0);
        wheelOne.setCyclic(false);
        wheelOne.setInterpolator(new AnticipateOvershootInterpolator());
        wheelOne.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheelView, int oldValue, int newValue) {
                wheelTwo.setAdapter(new ArrayWheelAdapter(monthList, monthList.size()));
                wheelTwo.setCurrentItem(0, false);
            }
        });
        //月
        wheelTwo.setAdapter(new ArrayWheelAdapter(monthList, monthList.size()));
        wheelTwo.setCurrentItem(0);
        wheelTwo.setCyclic(false);
        wheelTwo.setInterpolator(new AnticipateOvershootInterpolator());
        wheelTwo.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheelView, int oldValue, int newValue) {
                //年
                int indexYear = wheelOne.getCurrentItem();
                //月
                int indexMonth = wheelTwo.getCurrentItem();

                Map<String, Object> map = list.get(indexYear);
                List<List<Map<String, String>>> data = (List<List<Map<String, String>>>) map.get("month");
                List<Map<String, String>> daysList = data.get(indexMonth);

                wheelThree.setAdapter(new ArrayWheelAdapter(daysList, daysList.size()));
                wheelThree.setCurrentItem(0, false);
            }
        });
        //年
        int indexYear = wheelOne.getCurrentItem();
        //月
        int indexMonth = wheelTwo.getCurrentItem();
        Map<String, Object> map = list.get(indexYear);
        List<List<Map<String, String>>> data = (List<List<Map<String, String>>>) map.get("month");
        List<Map<String, String>> daysList = data.get(indexMonth);
        wheelThree.setAdapter(new ArrayWheelAdapter(daysList, daysList.size()));
        wheelThree.setCurrentItem(0, false);
        wheelThree.setCyclic(false);
        wheelThree.setInterpolator(new AnticipateOvershootInterpolator());
        wheelThree.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheelView, int oldValue, int newValue) {
            }
        });

        pop.setFocusable(true);
        pop.setAnimationStyle(R.style.AnimationPreview);
        pop.setTouchable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setOutsideTouchable(true);
        pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        return pop;
    }
    /**
     * 显示时间三级菜单 确定获取数据 getTag() == str[]{年,月,日}
     *
     * @param context
     * @param view
     * @param listener
     * @return
     */
    public static PopupWindow showThreeDateWheel(final Context context, final View view, final OnClickListener listener) {
        final List<Map<String, Object>> list = getCalendarList();
        final List<Map<String, Object>> monthList = new ArrayList<Map<String, Object>>();
        final List<Map<String, Object>> monthList2 = new ArrayList<Map<String, Object>>();
        final List<List<Map<String, Object>>> monthsList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();


        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        for (int i = 1; i <= 12; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            if (i < 10) {
                map.put("name", "0" + i + "月");
            } else {
                map.put("name", i + "月");
            }
            monthList.add(map);
        }
        for (int i = 1; i <= month + 1; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            if (i < 10) {
                map.put("name", "0" + i + "月");
            } else {
                map.put("name", i + "月");
            }
            monthList2.add(map);
        }

        for (int i = 0; i < list.size(); i++) {
            if (i != list.size() - 1) {
                monthsList.add(monthList);
            } else {
                monthsList.add(monthList2);
            }
        }


        final LayoutInflater inflater = LayoutInflater.from(context);
        final View wheelView = inflater.inflate(R.layout.agewheel, null);
        final PopupWindow pop = new PopupWindow(wheelView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final WheelView wheelOne = (WheelView) wheelView.findViewById(R.id.wheel_left);
        final WheelView wheelTwo = (WheelView) wheelView.findViewById(R.id.wheel_middle);
        final WheelView wheelThree = (WheelView) wheelView.findViewById(R.id.wheel_right1);
        Button wheelCancel = (Button) wheelView.findViewById(R.id.wheel_cancel_age);
        Button wheelSure = (Button) wheelView.findViewById(R.id.wheel_sure_age);
        wheelCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pop.isShowing()) pop.dismiss();
            }
        });
        wheelSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    //年
                    int indexYear = wheelOne.getCurrentItem();
                    //月
                    int indexMonth = wheelTwo.getCurrentItem();
                    //天
                    int indexDay = wheelThree.getCurrentItem();

                    Map<String, Object> map = list.get(indexYear);
                    List<List<Map<String, String>>> data = (List<List<Map<String, String>>>) map.get("month");
                    List<Map<String, String>> daysList = data.get(indexMonth);

                    String yearName = list.get(indexYear).get("name").toString();
//					String monthName = monthList.get(indexMonth).get("name").toString();
                    String monthName = monthsList.get(indexYear).get(indexMonth).get("name").toString();
                    String dayName = daysList.get(indexDay).get("name");
                    String str[] = new String[]{yearName, monthName, dayName};
                    v.setTag(str);
                    listener.onClick(v);
                }
                if (pop.isShowing()) pop.dismiss();
            }
        });
        //年
        wheelOne.setAdapter(new ArrayWheelAdapter(list, list.size()));
        wheelOne.setCurrentItem(list.size() - 1);
        wheelOne.setCyclic(false);
        wheelOne.setInterpolator(new AnticipateOvershootInterpolator());
        wheelOne.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheelView, int oldValue, int newValue) {

                //年
                int indexYear = wheelOne.getCurrentItem();
                //月
                int indexMonth = wheelTwo.getCurrentItem();
                //天
                int indexDay = wheelThree.getCurrentItem();
                int curr = monthsList.get(indexYear).size();
//				wheelTwo.setAdapter(new ArrayWheelAdapter(monthList,monthList.size()));
                wheelTwo.setAdapter(new ArrayWheelAdapter(monthsList.get(indexYear), curr));

                Map<String, Object> map = list.get(indexYear);
                List<List<Map<String, String>>> data = (List<List<Map<String, String>>>) map.get("month");
                List<Map<String, String>> daysList2 = null;
                try {
                    if (curr < 12) {
                        daysList2 = data.get(curr-1);
                        wheelTwo.setCurrentItem(curr-1);
                    } else {
                        daysList2 = data.get(indexMonth);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                wheelThree.setAdapter(new ArrayWheelAdapter(daysList2, daysList2.size()));
                wheelThree.setCurrentItem(0);

            }
        });

        //年
        int indexYear = wheelOne.getCurrentItem();
        //天
        int indexDay = wheelThree.getCurrentItem();
        //月
        wheelTwo.setAdapter(new ArrayWheelAdapter(monthsList.get(indexYear), monthsList.get(indexYear).size()));
        wheelTwo.setCurrentItem(monthsList.get(indexYear).size() - 1);
        wheelTwo.setCyclic(true);
        wheelTwo.setInterpolator(new AnticipateOvershootInterpolator());
        wheelTwo.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheelView, int oldValue, int newValue) {
                //年
                int indexYear = wheelOne.getCurrentItem();
                //月
                int indexMonth = wheelTwo.getCurrentItem();

                Map<String, Object> map = list.get(indexYear);
                List<List<Map<String, String>>> data = (List<List<Map<String, String>>>) map.get("month");
                List<Map<String, String>> daysList = data.get(indexMonth);

                wheelThree.setAdapter(new ArrayWheelAdapter(daysList, daysList.size()));
//				wheelThree.setCurrentItem(0,false);
            }
        });
//		//年
//		int indexYear = wheelOne.getCurrentItem();
//		//月
        int indexMonth = wheelTwo.getCurrentItem();
        Map<String, Object> map = list.get(indexYear);
        List<List<Map<String, String>>> data = (List<List<Map<String, String>>>) map.get("month");
        List<Map<String, String>> daysList = data.get(indexMonth);
        wheelThree.setAdapter(new ArrayWheelAdapter(daysList, daysList.size()));
        wheelThree.setCurrentItem(daysList.size() - 1);
        wheelThree.setCyclic(true);
        wheelThree.setInterpolator(new AnticipateOvershootInterpolator());
        wheelThree.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheelView, int oldValue, int newValue) {
            }
        });

        pop.setFocusable(true);
        pop.setAnimationStyle(R.style.AnimationPreview);
        pop.setTouchable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setOutsideTouchable(true);
        pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        return pop;
    }


    /**
     * 显示双滚轮
     * 获取数据
     * v.getTag(R.id.wheel_one);
     * v.getTag(R.id.wheel_two);
     *
     * @param context
     * @param list      一级数据 List<Map<String, String>> list  name,code
     * @param map       二级数据 Map<String, List<Map<String, String>>> map  name code
     * @param mainView
     * @param pop
     * @param wheelView
     */
    public static PopupWindow showDoubleWheel(
            final Context context,
            final List<Map<String, String>> list,
            final Map<String, List<Map<String, String>>> map,
            final View parentView,
            final OnClickListener listener
    ) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View wheelView = inflater.inflate(R.layout.double_wheel, null);
        final PopupWindow pop = new PopupWindow(wheelView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final WheelView wheelOne = (WheelView) wheelView.findViewById(R.id.wheel_one);
        final WheelView wheelTwo = (WheelView) wheelView.findViewById(R.id.wheel_two);
        Button wheelCancel = (Button) wheelView.findViewById(R.id.wheel_cancel);
        Button wheelSure = (Button) wheelView.findViewById(R.id.wheel_sure);

        wheelCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });
        wheelSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    v.setTag(R.id.wheel_one, wheelOne.getCurrentItem());
                    v.setTag(R.id.wheel_two, wheelTwo.getCurrentItem());
                    listener.onClick(v);
                }
                pop.dismiss();
            }
        });
        wheelOne.setAdapter(new ArrayWheelAdapter(list, list.size()));
        wheelOne.setCurrentItem(0);
        wheelOne.setCyclic(false);
        wheelOne.setInterpolator(new AnticipateOvershootInterpolator());
        wheelOne.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheelView, int oldValue, int newValue) {
//				String code = list.get(newValue).get("code");
                String name = list.get(newValue).get("name");
                List<Map<String, String>> data = map.get(name);
                wheelTwo.setAdapter(new ArrayWheelAdapter(data, data.size()));
                wheelTwo.setCurrentItem(0, true);
            }
        });
//		String code = list.get(0).get("code");
        String name = list.get(0).get("name");
        List<Map<String, String>> data = map.get(name);
        wheelTwo.setAdapter(new ArrayWheelAdapter(data, data.size()));
        wheelTwo.setCyclic(false);
        wheelTwo.setInterpolator(new AnticipateOvershootInterpolator());

        pop.setFocusable(true);
        pop.setAnimationStyle(R.style.AnimationPreview);
        pop.setTouchable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setOutsideTouchable(true);
        pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
        return pop;
    }

    /**
     * 显示单滚轮
     *
     * @param context
     * @param list
     * @param map
     * @param mainView
     * @param pop
     * @param wheelView
     */
    public static PopupWindow showSingleWheel(
            final Context context,
            final List<Map<String, String>> list,
            final View parentView,
            final OnClickListener listener
    ) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View wheelView = inflater.inflate(R.layout.double_wheel, null);
        final PopupWindow pop = new PopupWindow(wheelView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final WheelView wheelOne = (WheelView) wheelView.findViewById(R.id.wheel_one);
        final WheelView wheelTwo = (WheelView) wheelView.findViewById(R.id.wheel_two);
        wheelTwo.setVisibility(View.GONE);
        Button wheelCancel = (Button) wheelView.findViewById(R.id.wheel_cancel);
        Button wheelSure = (Button) wheelView.findViewById(R.id.wheel_sure);

        wheelCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });
        wheelSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    v.setTag(R.id.wheel_one, wheelOne.getCurrentItem());
                    listener.onClick(v);
                }
                pop.dismiss();
            }
        });
        wheelOne.setAdapter(new ArrayWheelAdapter(list, list.size()));
        wheelOne.setCurrentItem(0);
        wheelOne.setCyclic(false);
        wheelOne.setInterpolator(new AnticipateOvershootInterpolator());

        pop.setFocusable(true);
        pop.setAnimationStyle(R.style.AnimationPreview);
        pop.setTouchable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setOutsideTouchable(true);
        pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
        return pop;
    }


    /**
     * 设置双滚轮
     *
     * @param context
     * @param list      第一级数据
     * @param map       第一级对应的二级数据
     * @param mainView  pop在此View上显示
     * @param pop       pop窗口
     * @param wheelView pop中显示的View
     */
    public static void setDoubleWheel(Context context,
                                      final List<Map<String, String>> list,
                                      final Map<String, List<Map<String, String>>> map,
                                      View mainView, PopupWindow pop, View wheelView) {
        limitNo = context.getString(R.string.limit_no);
        current = null;
        current1 = null;
        current2 = null;
        item = 0;
        if (list.size() == 0) {
            return;
        }

        setValHeight(context, wheelView);
        WheelView wheel = (WheelView) wheelView.findViewById(R.id.wheel);
        final WheelView wheelRight = (WheelView) wheelView.findViewById(R.id.wheel_right);
        wheelRight.setVisibility(View.VISIBLE);
        wheel.removeAllChangingListener();
        wheel.setAdapter(new ArrayWheelAdapter(list, list.size()));
        setWheel(wheel, false);
//		wheel.setVisibleItems(9);
        firstCode = list.get(0).get("code");
        //firstName = list.get(0).get("name");
        wheel.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                cate = newValue;
                firstName = list.get(newValue).get("name");
                firstCode = list.get(newValue).get("code");
                List<Map<String, String>> arr = map.get(firstName);
                wheelRight.setCurrentItem(0);
                wheelRight.setAdapter(new ArrayWheelAdapter(arr, arr.size()));
                current = firstName;
                code = firstCode;
                if (arr.size() == 0) {
                    return;
                }

                String name = arr.get(0).get("name");

                if (name != null && !limitNo.equals(name)) {
                    current = firstName + "-" + arr.get(0).get("name");
                    code = arr.get(0).get("code");
                }
                partMap = arr.get(0);
            }
        });

        wheelRight.removeAllChangingListener();
        firstName = list.get(0).get("name");
        current = firstName;
        code = firstCode;
        if (map.get(firstName).size() > 0) {
            if (map.get(firstName).get(0).get("name").equals("全部")) {
                current = firstName;
            } else {
                current = firstName + " " + map.get(firstName).get(0).get("name");
            }
            code = map.get(firstName).get(0).get("code");
            partMap = map.get(firstName).get(0);
        }

        wheelRight.setAdapter(new ArrayWheelAdapter(map.get(firstName), map.get(firstName)
                .size()));
        setWheel(wheelRight, false);
//		wheelRight.setVisibleItems(9);
        wheelRight.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                List<Map<String, String>> arr = map.get(firstName);
                if (arr.size() == 0) {
                    return;
                }

                String name = arr.get(newValue).get("name");
                current = firstName;
                code = firstCode;
                if (name != null && !limitNo.equals(name)) {
                    if (arr.get(newValue).get("name").equals(limitNo)) {
                        current = firstName;
                    } else {
                        current = firstName + " " + arr.get(newValue).get("name");
                    }
                    code = arr.get(newValue).get("code");
                }

                partMap = arr.get(newValue);
            }
        });
        if (pop != null)
            setPopeWindow(context, mainView, pop);

    }

    private static void setValHeight(Context context, View wheelView) {
        LinearLayout wheelVal = (LinearLayout) wheelView.findViewById(R.id.wheel_val);
        LayoutParams params = wheelVal.getLayoutParams();
        float den = context.getResources().getDisplayMetrics().density;
        params.height = (int) ((16 + 35) * den);
        wheelVal.setLayoutParams(params);
    }

    /**
     * 设置双滚轮用以设置关注和地点
     *
     * @param context
     * @param list      第一级数据
     * @param map       第一级对应的二级数据
     * @param mainView  pop在此View上显示
     * @param pop       pop窗口
     * @param wheelView pop中显示的View
     */
    public static void setDoubleWheelForAttention(Context context,
                                                  final ArrayList<HashMap<String, String>> list,
                                                  final HashMap<String, ArrayList<HashMap<String, String>>> map,
                                                  View mainView, PopupWindow pop, View wheelView) {
        current = null;
        current1 = list.get(0).get("name");
        current2 = null;
        item = 0;
        if (list.size() == 0) {
            return;
        }
        setValHeight(context, wheelView);
        WheelView wheel = (WheelView) wheelView.findViewById(R.id.wheel);
        final WheelView wheelRight = (WheelView) wheelView.findViewById(R.id.wheel_right);

        wheelRight.setVisibility(View.VISIBLE);
        wheel.removeAllChangingListener();
        wheel.setAdapter(new ArrayWheelAdapter(list, list.size()));
        setWheel(wheel, false);
//		wheel.setVisibleItems(9);
        wheel.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                cate = newValue;
                firstName = list.get(newValue).get("name");
                ArrayList<HashMap<String, String>> arr = map.get(firstName);
                wheelRight.setCurrentItem(0);
                wheelRight.setAdapter(new ArrayWheelAdapter(arr, arr.size()));
                current1 = firstName;
                if (arr.size() == 0) {
                    return;
                }
                code = arr.get(0).get("code");
                current = arr.get(0).get("name");
            }
        });

        wheelRight.removeAllChangingListener();
        firstName = list.get(0).get("name");
        current = map.get(firstName).get(0).get("name");
        code = map.get(firstName).get(0).get("code");
        wheelRight.setAdapter(new ArrayWheelAdapter(map.get(firstName), map.get(firstName)
                .size()));
        setWheel(wheelRight, false);
//		wheelRight.setVisibleItems(9);
        wheelRight.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                ArrayList<HashMap<String, String>> arr = map.get(firstName);
                if (arr.size() == 0) {
                    return;
                }
                current = arr.get(newValue).get("name");
                code = arr.get(newValue).get("code");
            }
        });
        setPopeWindow(context, mainView, pop);
    }

    /**
     * 设置年龄滚轮
     *
     * @param context
     * @param str1      第一级数据
     * @param str2      第二级数据
     * @param str3      第三级数据
     * @param mainView  pop在此View上显示
     * @param pop       pop窗口
     * @param wheelView pop中显示的View
     */
    public static void setThirdWheel(final Context context,
                                     final String str1[], final String str2[], final String[] mon1, final String[] mon2, final String[] mon3, final String[] mon4,
                                     View mainView, PopupWindow pop, View wheelView) {
        current1 = str1[0];
        current2 = str2[0];
        current = mon1[0];
        setValHeight(context, wheelView);
        WheelView wheel = (WheelView) wheelView.findViewById(R.id.wheel);
        final WheelView wheelmiddle = (WheelView) wheelView.findViewById(R.id.wheel_middle); //不能提交
        final WheelView wheelRight = (WheelView) wheelView.findViewById(R.id.wheel_right);

        wheelRight.setVisibility(View.VISIBLE);
        wheel.removeAllChangingListener();
        wheel.setAdapter(new ArrayWheelAdapter(str1, str1.length));
        setWheel(wheel, false);
        wheel.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                current1 = str1[newValue];
            }
        });

        wheelmiddle.removeAllChangingListener();
        wheelmiddle.setAdapter(new ArrayWheelAdapter(str2, str2.length));
        setWheel(wheelmiddle, false);
        wheelmiddle.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                current2 = str2[newValue];
                String month = current2;
                String y = current1.substring(0, 4);
                int year = Integer.parseInt(y);
                if (month.equals("02月")) {
                    if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                        wheelRight.setAdapter(new ArrayWheelAdapter(mon2, mon2.length));
                        if (ageIndex == 29 || ageIndex == 30) {
                            current = mon2[28];
                            ageIndex = 0;
                        }
                    } else {
                        wheelRight.setAdapter(new ArrayWheelAdapter(mon4, mon4.length));
                        if (ageIndex == 28 || ageIndex == 29 || ageIndex == 30) {
                            current = mon2[27];
                            ageIndex = 0;
                        }
                    }

                } else if (month.equals("01月") || month.equals("03月") || month.equals("05月") || month.equals("07月")
                        || month.equals("08月") || month.equals("10月")
                        || month.equals("12月")) {
                    wheelRight.setAdapter(new ArrayWheelAdapter(mon1, mon1.length));
                } else if (month.equals("04月") || month.equals("06月") || month.equals("09月") || month.equals("11月")) {
                    wheelRight.setAdapter(new ArrayWheelAdapter(mon3, mon3.length));
                    if (ageIndex == 30) {
                        current = mon3[29];
                        ageIndex = 0;
                    }

                }
            }
        });

        wheelRight.removeAllChangingListener();
        wheelRight.setAdapter(new ArrayWheelAdapter(mon1, mon1.length));
        setWheel(wheelRight, false);
        wheelRight.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                ageIndex = newValue;
                String month = current2;
                if (month.equals("02月")) {
                    current = mon2[newValue];
                } else if (month.equals("01月") || month.equals("03月") || month.equals("05月") || month.equals("07月")
                        || month.equals("08月") || month.equals("10月")
                        || month.equals("12月")) {
                    current = mon1[newValue];
                } else if (month.equals("04月") || month.equals("06月") || month.equals("09月") || month.equals("11月")) {
                    current = mon3[newValue];
                }
            }
        });
        setPopeWindow(context, mainView, pop);
    }

    /**
     * 获得第一级所选择的
     *
     * @return
     */
    public static int getCate() {
        return cate;
    }

    /**
     * 获取数据current1
     *
     * @return
     */
    public static String getCurrent1() {
        return current1;
    }

    /**
     * 获取数据current2
     *
     * @return
     */
    public static String getCurrent2() {
        return current2;
    }

    /**
     * 获取当前项
     *
     * @return
     */
    public static String getCurrent() {
        if (current1 != null && current2 != null) {
            return current1 + current2 + current;
        } else if (current1 != null && current2 == null) {
            return current1 + " " + current;
        } else {
            return current;
        }
    }

    public static int getItem() {
        return item;
    }

    /**
     * 获取当前项对应的code码
     *
     * @return
     */
    public static String getCode() {
        return code;
    }

    /**
     * 获取第一级对应的code码
     *
     * @return
     */
    public static String getFirstCode() {
        return firstCode;
    }

    /**
     * 获取第一级对应的内容
     *
     * @return
     */
    public static String getFirstName() {
        return firstName;
    }

    public static Map<String, String> getPartMap() {
        return partMap;
    }

    /**
     * 设置WheelView基本属性
     * @param wheel 滚轮
     * @param isCyclic 滚轮内数据是否循环显示
     */
    /**
     * @param wheel
     * @param isCyclic
     */
    public static void setWheel(WheelView wheel, boolean isCyclic) {
        wheel.setCurrentItem(0);
        wheel.setCyclic(isCyclic);
        wheel.setInterpolator(new AnticipateOvershootInterpolator());
    }

    /**
     * 初始化popeWindow
     *
     * @param context
     * @param mainView pop在此View上显示
     * @param pop
     * @param v        pop中显示的View
     */
    public static PopupWindow setPopeWindow(Context context, View mainView, PopupWindow pop) {
        pop.setFocusable(true);
        pop.setAnimationStyle(R.style.AnimationPreview);
        pop.setTouchable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setOutsideTouchable(true);
        pop.showAtLocation(mainView, Gravity.BOTTOM, 0, 0);
        return pop;
    }


    /**
     * 设置popeWindow所占屏幕比例  默认1/2
     *
     * @param part
     */
    public static void setPart(int part) {
        WheelUtils.part = part;
    }

    /**
     * 显示软键盘
     *
     * @param context
     * @param edit    输入框
     * @param sureBtn 确定按钮
     */
    public static void showInput(final Context context) {
        //View inputView = LayoutInflater.from(context).inflate(R.layout.input_edit_layout, null);
        //edit.setView(view,inputView);
        //view.setVisibility(View.GONE);

        final Dialog dia = new Dialog(context, R.style.dialog);
        View v = LayoutInflater.from(context).inflate(R.layout.input_edit_layout, null);
        dia.setContentView(v);
        Window win = dia.getWindow();
        android.view.WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.BOTTOM;
        win.setAttributes(params);
        dia.setCanceledOnTouchOutside(true);
        dia.show();
        final EditTextBox edit = (EditTextBox) dia.findViewById(R.id.input_edit);
        edit.setOnBottomListener(new OnBottomListener() {
            @Override
            public void setBottom() {

            }
        });
        //WheelUtils.showSoftInput(context, (EditTextBox)dia.findViewById(R.id.input_edit));
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                WheelUtils.showSoftInput(context, edit);
            }
        }, 100);
        //inputView.setVisibility(View.VISIBLE);
        edit.requestFocus();
    }

    /**
     * 显示软键盘
     *
     * @param context
     * @param edit
     */
    public static void showSoftInput(Context context, EditTextBox edit) {
        edit.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edit, InputMethodManager.SHOW_FORCED);
        //imm.showSoftInputFromInputMethod(edit.getWindowToken(),  InputMethodManager.SHOW_FORCED);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        // imm.toggleSoftInputFromWindow(edit.getWindowToken(), 0, 0);
    }


    /**
     * 隐藏软键盘s
     *
     * @param context
     * @param edit
     */
    public static void hideInput(Context context, IBinder windowToken) {
        InputMethodManager input = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
        input.hideSoftInputFromWindow(windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
}
