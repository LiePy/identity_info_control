package com.example.identity_info_control.Activitys;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.identity_info_control.DBHelper.MyDatabaseHelper;
import com.example.identity_info_control.OnCLickLinstener.MenuOnClickListener;
import com.example.identity_info_control.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Activity_Add extends AppCompatActivity {

    private MyDatabaseHelper dbHelper = new MyDatabaseHelper(this,"identity_info.db", null, 1);

    private TextView title_text;
    private ImageView imageView;
    private EditText name,idNumber,addr,email,phone,note;
    private Button add_button;
    private String imageUrl=null,name_=null;
    private SQLiteDatabase db;
    private String info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        findAllView();//获得所有用到的实力控件

        initTitleUI();//初始化标题栏

        initView();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture();
            }
        });

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    //获取所有输入框实例
    private void findAllView(){
        name = findViewById(R.id.editText);
        idNumber = findViewById(R.id.editText2);
        addr = findViewById(R.id.editText3);
        email = findViewById(R.id.editText4);
        phone = findViewById(R.id.editText5);
        note = findViewById(R.id.editText6);
        imageView = findViewById(R.id.imageView);
        title_text = findViewById(R.id.textView_title);
        add_button = findViewById(R.id.add_button);
    }

    //初始化标题栏
    private void initTitleUI(){
        //隐藏原标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }
        //标题栏按钮监听
        LinearLayout titleLayout = findViewById(R.id.title_linearlayout);
        MenuOnClickListener mcl = new MenuOnClickListener(this, titleLayout);
        mcl.listenMenu();

        title_text.setText("添加数据");
    }

    private void initView(){
        db = dbHelper.getWritableDatabase();//若数据库已存在则打开，否则创建一个数据库

        Intent intent = getIntent();
        info = intent.getStringExtra("info");
        if(info!=null){
            initOldData(intent);
        }
    }

    //加载旧数据
    private void initOldData(Intent intent){
        title_text.setText("修改数据");
        idNumber.setSaveEnabled(false);
        idNumber.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"主键不可修改！\n若需修改请删除！",Toast.LENGTH_SHORT).show();
            }
        });

        name_ = intent.getStringExtra("name");
        String phone_ = intent.getStringExtra("phone");
        String addr_ = intent.getStringExtra("addr");
        String email_ = intent.getStringExtra("email");
        String idNumber_ = intent.getStringExtra("idnumber");
        String imageUrl = intent.getStringExtra("imageUrl");
        String note_ = intent.getStringExtra("note");

        name.setText(name_);
        phone.setText(phone_);
        addr.setText(addr_);
        email.setText(email_);
        idNumber.setText(idNumber_);
        note.setText(note_);
        if(imageUrl.equals("null")){
            imageView.setImageResource(R.drawable.img3);
        }else{
            imageView.setImageURI(Uri.parse(imageUrl));
        }
    }

    //打开本地图片选择
    private void selectPicture(){
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, 0x1);
    }

    //保存数据，若是修改，则先删除原数据，再保存新数据
    private void saveData(){
        //获取所有输入框里的文字
        String name_new = name.getText().toString();
        String phone_new = phone.getText().toString();
        String addr_new = addr.getText().toString();
        String email_new = email.getText().toString();
        String idNumber_new = idNumber.getText().toString();
        String note_new = note.getText().toString();

        if(idNumber_new.equals("")){
            Toast.makeText(getApplicationContext(),"身份证号码不能为空！",Toast.LENGTH_SHORT).show();
        }else if(name_new.equals("")){
            Toast.makeText(getApplicationContext(),"姓名不能为空！",Toast.LENGTH_SHORT).show();
        }else if(!checkIDNumber(idNumber_new)){
            Toast.makeText(getApplicationContext(),"身份证号格式错误！\n请输入真实的身份证号码！",Toast.LENGTH_SHORT).show();
        }else if(checkIDNumberExisted(idNumber_new)&&info==null){
            Toast.makeText(getApplicationContext(),"身份证号已存在！",Toast.LENGTH_SHORT).show();
        }else if(!checkEmail(email_new)){
            Toast.makeText(getApplicationContext(),"邮箱格式错误！\n请输入真实的邮箱号码！",Toast.LENGTH_SHORT).show();
        }else if(!checkPhoneNumber(phone_new)){
            Toast.makeText(getApplicationContext(),"手机号码格式错误！\n请输入真实的手机号码",Toast.LENGTH_SHORT).show();
        }else if(checkOthers(name_new+phone_new+addr_new+email_new+idNumber_new+note_new)){
            Toast.makeText(getApplicationContext(),"您的输入中不能带有英文的单引号！",Toast.LENGTH_SHORT).show();
        }else{
            try{
                if(info!=null){//如果是修改信息，则先删除原来的数据
                    db.execSQL("delete from identity where name='"+name_+"'");
                    Toast.makeText(getBaseContext(),"修改成功！",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getBaseContext(),"保存成功！",Toast.LENGTH_SHORT).show();
                }
                //提交SQL语句，保存联系人信息到contacts表
                db.execSQL("insert into identity(name,phone,address,email,idnumber,note,imageurl) values(" +
                        "'"+name_new+"','"+phone_new+"','"+addr_new+"','"+email_new+"','"+idNumber_new+"','"+note_new+"','"+imageUrl+"')");

                finish();
            }catch(Exception e){
                Toast.makeText(getBaseContext(),"保存失败！\n未知错误！",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0x1 && resultCode == RESULT_OK) {
            if (data != null) {
                imageUrl = data.getData().toString();
                imageView.setImageURI(data.getData());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //检验手机号码
    private boolean checkPhoneNumber(String phoneNumber){
        if (phoneNumber.equals("")){
            return true;
        }else{
            String regExp = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[3,5,6,7,8])" +
                    "|(18[0-9])|(19[8,9]))\\d{8}$";
            Pattern p = Pattern.compile(regExp);
            Matcher m = p.matcher(phoneNumber);
            return m.matches();
        }
    }

    //检验邮箱
    private boolean checkEmail(String email){
        if (email.equals("")){
            return true;
        }else{
            String regExp = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+" +
                    "[a-zA-Z]{2,}$";
            Pattern p = Pattern.compile(regExp);
            Matcher m = p.matcher(email);
            return m.matches();
        }
    }

    //检验身份证号码
    private boolean checkIDNumber(String idNumber){
        String regExp = "^[1-9][0-9]{5}(17|18|19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|30|31)" +
                "|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}([0-9]|x|X)$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(idNumber);
        return m.matches();
    }

    private boolean checkIDNumberExisted(String idNumber){
        Cursor cursor = db.rawQuery("select * from identity where idnumber='"+idNumber+"'", null);
        if(cursor.getCount()==0){
            return false;
        }else{return true;}
    }

    private boolean checkOthers(String str){
        String regExp = ".*'.*";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }
}
