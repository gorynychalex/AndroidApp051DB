package ru.dvfu.mrcpk.popovich.androidapp051db;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {
    private static final int CM_EDIT_ID = 1;
    private static final int CM_DELETE_ID = 2;
    private static final int REQUEST_CODE_EDIT = 1;
    private static final int REQUEST_CODE_ADD = 2;
    ListView listView;
    DB db;
    SimpleCursorAdapter simpleCursorAdapter;
    Cursor cursor;
    AdapterView.AdapterContextMenuInfo acmi;
    Intent intent;
    ContentValues contentValues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // инициализация подключения к БД
        db = new DB(this);
        db.open();

        // получение курсора
        cursor = db.getAllData();

        // массивы сопоставления полей БД и вспомогательного макета
        String[] from = new String[] { DB.TABLE_MAIN_FIRSTNAME, DB.TABLE_MAIN_LASTNAME, DB.TABLE_MAIN_PHONENUM, DB.TABLE_MAIN_EMAIL };
        int[] to = new int[] { R.id.listItemFirstname, R.id.listItemLastname, R.id.listItemPhone, R.id.listItemMail };

        // создание адаптера и настройка и установка списка
        simpleCursorAdapter = new SimpleCursorAdapter(this,R.layout.list_items,cursor,from,to,1);
        listView=(ListView) findViewById(R.id.listViewMain);
        listView.setAdapter(simpleCursorAdapter);

        if(cursor.moveToFirst()) {
            do {
                Log.d("myLog", "Cursor = " + cursor.getPosition() + ", ID = " + cursor.getInt(0) + " , " + DB.TABLE_MAIN_FIRSTNAME+ " = " + cursor.getString(1) + " , " + DB.TABLE_MAIN_LASTNAME + " = " + cursor.getString(2));
            } while (cursor.moveToNext());
        }

//        db.close();
        registerForContextMenu(listView);
    }

//    // обработка нажатия кнопки
//    public void onButtonClick(View view) {
//        // добавляем запись
//        db.addRec("sometext " + (cursor.getCount() + 1), R.drawable.ic_launcher);
//        // обновляем курсор
//        cursor.requery();
//    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_EDIT_ID, 0, R.string.edit_record);
        menu.add(0, CM_DELETE_ID, 0, R.string.delete_record);
    }

    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case CM_EDIT_ID:
            // получаем из пункта контекстного меню данные по пункту списка
            acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                intent = new Intent(this, EditActivity.class);
                intent.putExtra("desc","Редактирование данных:");
//                intent.putExtra(DB.TABLE_MAIN_ID,acmi.id);

                Log.d("myLog", "acmi.id = " + String.valueOf(acmi.id));


                cursor = db.getElement(acmi.id);
                if(cursor.moveToFirst()){
                    do{
                        for (int i = 0; i < cursor.getColumnCount(); i++) {
                            intent.putExtra(cursor.getColumnName(i),cursor.getString(i));
                            Log.d("myLog", "Cursor column [" + cursor.getColumnName(i) + "] = " + cursor.getString(i));
                        }

                    }while (cursor.moveToNext());
                }

                startActivityForResult(intent, REQUEST_CODE_EDIT);
            return true;
            case CM_DELETE_ID:
                // получаем из пункта контекстного меню данные по пункту списка
                acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                // извлекаем id записи и удаляем соответствующую запись в БД
                db.delRec(acmi.id);
                // обновляем курсор
                cursor.requery();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,0,0,"Добавить запись");
        menu.add(0,1,0,"Выход");
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case 0:
                intent = new Intent(this, EditActivity.class);
                intent.putExtra("desc","Добавить запись:");
                intent.putExtra("button", "Добавить");
                startActivityForResult(intent, REQUEST_CODE_ADD);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onDestroy() {
        super.onDestroy();
        // закрываем подключение при выходе
        db.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) return;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_EDIT:
                    contentValues = new ContentValues();
//                    contentValues.put(DB.TABLE_MAIN_ID,data.getExtras().getString(DB.TABLE_MAIN_ID));
                    contentValues.put(DB.TABLE_MAIN_FIRSTNAME,data.getExtras().getString(DB.TABLE_MAIN_FIRSTNAME));
                    contentValues.put(DB.TABLE_MAIN_LASTNAME,data.getExtras().getString(DB.TABLE_MAIN_LASTNAME));
                    contentValues.put(DB.TABLE_MAIN_PHONENUM,data.getExtras().getString(DB.TABLE_MAIN_PHONENUM));
                    contentValues.put(DB.TABLE_MAIN_EMAIL,data.getExtras().getString(DB.TABLE_MAIN_EMAIL));
                    db.updateRec(contentValues,acmi.id);
                    simpleCursorAdapter.notifyDataSetChanged();
                    listView.setAdapter(simpleCursorAdapter);
                    break;
                case REQUEST_CODE_ADD:
                    contentValues = new ContentValues();
//                    contentValues.put(DB.TABLE_MAIN_ID,data.getExtras().getString(DB.TABLE_MAIN_ID));
                    contentValues.put(DB.TABLE_MAIN_FIRSTNAME,data.getExtras().getString(DB.TABLE_MAIN_FIRSTNAME));
                    contentValues.put(DB.TABLE_MAIN_LASTNAME,data.getExtras().getString(DB.TABLE_MAIN_LASTNAME));
                    contentValues.put(DB.TABLE_MAIN_PHONENUM,data.getExtras().getString(DB.TABLE_MAIN_PHONENUM));
                    contentValues.put(DB.TABLE_MAIN_EMAIL,data.getExtras().getString(DB.TABLE_MAIN_EMAIL));
                    db.addRec(contentValues);
                    simpleCursorAdapter.notifyDataSetChanged();
                    listView.setAdapter(simpleCursorAdapter);
                    break;
            }
        }
    }
}
