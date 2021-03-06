package com.nnxy.gjp.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dbmanager.CommomUtils;
import com.nnxy.gjp.R;
import com.nnxy.gjp.application.MyApplication;
import com.nnxy.gjp.entity.Account;
import com.nnxy.gjp.okhttp.OKManager;

import org.json.JSONException;

import java.util.Calendar;

/**
 * 该类解决账务更新与删除的问题
 */
public class UpdateOrDeleteAccountFragment extends Fragment {

    //对所有控件、对象进行声明
    private Button updateAccountBtn,deleteAccountBtn;
    private Calendar calendar;
    private int mYear,mMonth,mDay;
    private OKManager manager;
    private Bundle bundle;
    private Spinner output_LeiBie = null;
    private Spinner leiBie = null;
    private String[][] leibieData = new String[][]{{"工资", "捡钱", "金融", "其他"},
            {"购物", "吃饭", "出行", "其他"}};
    private ArrayAdapter<CharSequence> adapterArea = null;
    private EditText money,date,note;
    private Account account;
    private CommomUtils accountUtils;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MyApplication.setAPPFLAG(102);
        View view = inflater.inflate(R.layout.update_or_delete_account_,container,false );

        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MyApplication.setAPPFLAG(101);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //级联下拉框
        this.output_LeiBie =  view.findViewById(R.id.zhangwu_leibie);
        this.leiBie =  view.findViewById(R.id.leibie);
        this.output_LeiBie.setOnItemSelectedListener(new OnItemSelectedListenerImp());
        //网络框架实例化
        manager = OKManager.getInstance();
        money=view.findViewById(R.id.acc_money);
        date=view.findViewById(R.id.acc_date);
        note=view.findViewById(R.id.acc_note);
        calendar=Calendar.getInstance();
        accountUtils = new CommomUtils(getActivity());
        updateAccountBtn = view.findViewById(R.id.updateAccount_btn);
        deleteAccountBtn = view.findViewById(R.id.deleteAccount_btn);
        //bundle实例化
        bundle = getArguments();
        accountUtils = new CommomUtils(getActivity());
        money.setText(bundle.getString("money"));
        date.setText(bundle.getString("date"));
        note.setText(bundle.getString("note"));
        //判断收支类型
        if (bundle.getString("type").equals("true")){
            output_LeiBie.setSelection(0);
        }else {
            output_LeiBie.setSelection(1);
        }

        //更新账务按钮的点击事件
        updateAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = new Account();
                account.setAccId(Long.parseLong(bundle.get("accId").toString()));
                try {
                    account.setUserId(Long.parseLong(MyApplication.getUser().getString("userId")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                account.setAccMoney(Double.parseDouble(money.getText().toString()));
                account.setAccCreateDate(date.getText().toString());
                account.setOperateFlag(1l);//将操作标识符设置为1
                if (output_LeiBie.getSelectedItem().toString().equals("收入")){
                    account.setAccType(true);
                }else if (output_LeiBie.getSelectedItem().toString().equals("支出")){
                    account.setAccType(false);
                }

                account.setAccNote(note.getText().toString());
//                    account.setAccMoney(Double.parseDouble(money.getText().toString()));


                account.setAccIsDel(false);
                account.setAccStyle(leiBie.getSelectedItem().toString());
                if (money.getText().toString().trim().equals("||")){
                    Toast.makeText(getActivity(),"金额不能为空",Toast.LENGTH_LONG).show();
                }else if (date.getText().toString().trim().equals("")){
                    Toast.makeText(getActivity(),"日期不能为空",Toast.LENGTH_LONG).show();
                }else {
                    accountUtils.updateAccount(account);
                    System.out.println(account.toString());
                    Toast.makeText(getActivity(),"更新成功",Toast.LENGTH_LONG).show();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,new AllAccountFragment()).commitAllowingStateLoss();
                }
            }
        });
        //删除按钮的点击事件
        deleteAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity()).setTitle("你确定删除吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                account = new Account();
                                account.setAccId(Long.parseLong(bundle.get("accId").toString()));
                                try {
                                    account.setUserId(Long.parseLong(MyApplication.getUser().getString("userId")));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                account.setAccMoney(Double.parseDouble(money.getText().toString()));
                                account.setAccCreateDate(date.getText().toString());
                                account.setOperateFlag(1l);//将操作标识符设置为1
                                if (output_LeiBie.getSelectedItem().toString().equals("收入")){
                                    account.setAccType(true);
                                }else if (output_LeiBie.getSelectedItem().toString().equals("支出")){
                                    account.setAccType(false);
                                }

                                account.setAccNote(note.getText().toString());
//                    account.setAccMoney(Double.parseDouble(money.getText().toString()));
                                account.setAccStyle(leiBie.getSelectedItem().toString());
                                account.setAccIsDel(true);
                                accountUtils.updateAccount(account);
//                                跳转到主页面
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,new AllAccountFragment()).commitAllowingStateLoss();
                            }
                        }).setNegativeButton("取消",null).show();

            }
        });


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //日历控件
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        mYear = year;
                        mMonth = month;
                        mDay = day;
                        date.setText(new StringBuilder()
                                .append(mYear).append("-").append((mMonth+1) < 10 ? "0"+(mMonth+1):(mMonth+1)).append("-").append((mDay<10)?"0"+mDay:mDay));
                    }
                },calendar.get(Calendar.YEAR)
                        ,calendar.get(Calendar.MONTH)
                        ,calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }


    private class OnItemSelectedListenerImp implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            // 得到选择的选项
            UpdateOrDeleteAccountFragment.this.adapterArea = new ArrayAdapter<CharSequence>(
                    getActivity(), android.R.layout.simple_spinner_item,
                    UpdateOrDeleteAccountFragment.this.leibieData[position]);


            UpdateOrDeleteAccountFragment.this.adapterArea
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


            UpdateOrDeleteAccountFragment.this.leiBie.setAdapter(UpdateOrDeleteAccountFragment.this.adapterArea);

            //解决级联下拉框的问题
            int p ;
            if (bundle.getString("style").equals("工资")||bundle.getString("style").equals("购物")){
                p = 0;
            }else if (bundle.getString("style").equals("捡钱")||bundle.getString("style").equals("吃饭")){
                p = 1;
            }else if (bundle.getString("style").equals("金融")||bundle.getString("style").equals("出行")){
                p = 2;
            }else {
                p = 3;
            }
                leiBie.setSelection(p);

        }

        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }
    }
}
