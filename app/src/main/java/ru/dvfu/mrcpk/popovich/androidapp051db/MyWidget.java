package ru.dvfu.mrcpk.popovich.androidapp051db;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

//Класс для создания виджета (так называемый контент-провайдер)
public class MyWidget extends AppWidgetProvider {

    final String LOG = "MyWidget:Log ";

    final String ACTION_ON_CLICK = "ru.dvfu.mrcpk.androidapp051db.itemonclick";
    final static String ITEM_POSITION = "item_position";

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

    //Метод onUpdate вызывается, когда поступил запрос на обновление widgets.
    // Здесь перебираются ID-виджетов, для каждого из которых вызывается updateWidget
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);


        for(int appWidgetId : appWidgetIds){
            Log.d(LOG, "appWidgetID = " + appWidgetId);
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    //Метод для вызова методов для формирования виджета
    private void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetID) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.activity_main);

        setUpdateText(remoteViews,context,appWidgetID);
        //указание списку, что для получения адаптера нужно обращаться к сервису
        setList(remoteViews,context,appWidgetID);
        setListClick(remoteViews,context,appWidgetID);
        appWidgetManager.updateAppWidget(appWidgetID,remoteViews);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetID,R.id.listViewMain);
    }

    private void setUpdateText(RemoteViews remoteViews, Context context, int appWidgetID) {
        remoteViews.setTextViewText(R.id.tvUpdate,simpleDateFormat.format(new Date(System.currentTimeMillis())));
//        simpleDateFormat.format(new Date(System.currentTimeMillis()));
        Intent updateIntent = new Intent(context, MyWidget.class);
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,new int[]{appWidgetID});
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast(context,appWidgetID,updateIntent,0);
        remoteViews.setOnClickPendingIntent(R.id.tvUpdate,updatePendingIntent);
    }

    //указание списку, что для получения адаптера нужно обращаться к сервису MyService
    private void setList(RemoteViews remoteViews, Context context, int appWidgetID) {
        Intent intent = new Intent(context,MyService.class);

        //appWidgetID для того, чтобы: этот Intent будет передан в метод сервиса onGetViewFactory
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetID);
        Uri dataIntent = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME));
        intent.setData(dataIntent);
        remoteViews.setRemoteAdapter(R.id.listViewMain, intent);
    }

    void setListClick(RemoteViews remoteViews, Context context, int appWidgetId) {

//        Intent listClickIntent = new Intent(context, MyWidget.class);
//        listClickIntent.setAction(ACTION_ON_CLICK);
        Intent listClickIntent = new Intent(context, EditActivity.class);
//        listClickIntent.setAction("android.intent.action.MAIN");
        listClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        Log.d(LOG, " appWidgetId = " + appWidgetId);
//        PendingIntent listClickPIntent = PendingIntent.getBroadcast(context, 0,
//                listClickIntent, 0);
        PendingIntent listClickPIntent = PendingIntent.getActivity(context,appWidgetId,listClickIntent,0);
        remoteViews.setPendingIntentTemplate(R.id.listViewMain, listClickPIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equalsIgnoreCase(ACTION_ON_CLICK)) {
            int itemPos = intent.getIntExtra(ITEM_POSITION, -1);
            Log.d(LOG,"itemPos = " + itemPos);
            if (itemPos != -1) {
                Toast.makeText(context, "Clicked on item " + itemPos,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
