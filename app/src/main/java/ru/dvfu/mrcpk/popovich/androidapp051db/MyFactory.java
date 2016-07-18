package ru.dvfu.mrcpk.popovich.androidapp051db;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


//Класс для наполнения списка значениями
public class MyFactory implements RemoteViewsService.RemoteViewsFactory {

    final String LOG = "MyFactory: myLog ";

    //Объявление переменной id-виджета
    int widgetID;
    //Переменная для определения контекста
    Context context;
    //Переменная для инициализации DB
    DB db;
    //Для управления DB - query(),insert(),delete(),update(), execSQL()
    SQLiteDatabase sqLiteDatabase;
    //Переменная объекта курсора
    Cursor cursor;
    //Переменная для упаковывания данных
    ArrayList<String> arrayListFirst;
    ArrayList<String> arrayListLast;

    //Формат даты для заглавия списка
    SimpleDateFormat simpleDateFormat;

    public MyFactory(Context applicationContext, Intent intent) {
        this.context = applicationContext;

        //Передача widgetID адаптеру
        widgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
        Log.d(LOG,"widget ID = " + widgetID + " from AppWidgetManager.EXTRA_APPWIDGET_ID = " + AppWidgetManager.EXTRA_APPWIDGET_ID);
        // инициализация подключения к БД
        db = new DB(context);
        db.open();

        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    }

    //Создание адаптера (в нем создание объекта массива для данных)
    @Override
    public void onCreate() {
        arrayListFirst = new ArrayList<>();
        arrayListLast = new ArrayList<>();

    }

    //Подготовка данных для списка
    @Override
    public void onDataSetChanged() {
        arrayListFirst.clear();
//        arrayList.add("Widget ID: " + String.valueOf(widgetID));
        // Объект Cursor типа MAP-коллекция
        cursor = db.getAllData();

        if(cursor.moveToFirst()) {
            do {
                arrayListFirst.add(cursor.getString(1));
                arrayListLast.add(cursor.getString(2));
                Log.d(LOG, " cursor.getString(1): " + cursor.getString(1) + " cursor.getString(2): " + cursor.getString(2));
            } while (cursor.moveToNext());
        }
        cursor.close();

//        for (int i = 0; i < 5; i++) {
//            arrayList.add("Item" + i);
//
//        }
    }

    @Override
    public void onDestroy() {

//        db.close();
    }

    @Override
    public int getCount() {
        return arrayListFirst.size();
    }

    //Создание пунктов списка
    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.list_items);
        remoteViews.setTextViewText(R.id.listItemFirstname,arrayListFirst.get(position));
        remoteViews.setTextViewText(R.id.listItemLastname,arrayListLast.get(position));

        //ОБРАБОТКА НАЖАТИЙ КАЖДОГО ПУНКТА СПИСКА. СВЯЗАН С MyWidget (КОНТЕНТ-ПРОВАЙДЕРОМ)
        //Для каждого пункта списка создается свой интент. В него помещается позиция пункта и вызывается соотвествующий метод.
        Intent clickIntent = new Intent();
        clickIntent.putExtra(MyWidget.ITEM_POSITION, position);

        //Упаковываем данные для передачи в EditActivity
        clickIntent.putExtra(DB.TABLE_MAIN_FIRSTNAME,arrayListFirst.get(position));
        clickIntent.putExtra(DB.TABLE_MAIN_LASTNAME,arrayListLast.get(position));

        Log.d(LOG, "ITEM_POSITION = " + position);
        //Метод получает на вход ID View и Intent. Для View с полученным ID создается обработчик нажатия,
        // который вызывает PendingIntent, привязанный к списку методом setPendingIntentTemplate.
        // К нему добавляются данные на вход Intent.
        remoteViews.setOnClickFillInIntent(R.id.itemLinearLayout, clickIntent);

        //Варианты нажатий на поля по отдельности. Т.е. на каждый можно повесить свой обработчик
//        remoteViews.setOnClickFillInIntent(R.id.listItemFirstname, clickIntent);
//        remoteViews.setOnClickFillInIntent(R.id.listItemLastname, clickIntent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
