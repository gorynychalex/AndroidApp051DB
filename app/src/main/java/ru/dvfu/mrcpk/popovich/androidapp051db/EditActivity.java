package ru.dvfu.mrcpk.popovich.androidapp051db;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    // Объявление переменных соответствующих типов
    TextView textView;
    EditText editFirstName;
    EditText editLastName;
    EditText editPhone;
    EditText editMail;
    Button buttonEdit;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Инициализация переменных присвоением, используя метод получения объектов из идентификаторов ресурсов (с обязательным приведением типа)
        textView = (TextView) findViewById(R.id.textAboutOperation);
        editFirstName = (EditText) findViewById(R.id.editFirstname);
        editLastName = (EditText) findViewById(R.id.editLastname);
        editPhone = (EditText) findViewById(R.id.editPhone);
        editMail = (EditText) findViewById(R.id.editEmail);
        buttonEdit = (Button) findViewById(R.id.editButton);

        // Получение переданного объекта-намерения (из MainActivity)
        intent = getIntent();


        editFirstName.setText(intent.getExtras().getString(DB.TABLE_MAIN_FIRSTNAME));
        editLastName.setText(intent.getExtras().getString(DB.TABLE_MAIN_LASTNAME));
        editPhone.setText(intent.getExtras().getString(DB.TABLE_MAIN_PHONENUM));
        editMail.setText(intent.getExtras().getString(DB.TABLE_MAIN_EMAIL));
        textView.setText(intent.getExtras().getString("desc"));
        buttonEdit.setText(intent.getExtras().getString("button"));
        buttonEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        intent = new Intent();
//        intent.putExtra(DB.TABLE_MAIN_ID, intent.getExtras().getString(DB.TABLE_MAIN_ID));
        intent.putExtra(DB.TABLE_MAIN_FIRSTNAME, editFirstName.getText().toString());
        intent.putExtra(DB.TABLE_MAIN_LASTNAME, editLastName.getText().toString());
        intent.putExtra(DB.TABLE_MAIN_PHONENUM, editPhone.getText().toString());
        intent.putExtra(DB.TABLE_MAIN_EMAIL, editMail.getText().toString());
        setResult(RESULT_OK, intent);
        finish();

    }
}
