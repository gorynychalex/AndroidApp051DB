package ru.dvfu.mrcpk.popovich.androidapp051db;

import android.content.Intent;
import android.widget.RemoteViewsService;

//Класс возвращает экземпляр класса MyFactory, заполняющий список
public class MyService extends RemoteViewsService{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyFactory(getApplicationContext(), intent);
    }
}
