package com.example.kadohiraharuki.myscheduler

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import io.realm.Realm
import io.realm.kotlin.where

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {
    //lateinit修飾子でrealmはonCreateメソッドで初期化
    private lateinit var realm: Realm


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        //Realmオブジェクトを取得する
        //RealmクラスのgetDefaultInstanceメソッドでRealmクラスのインスタンスを初期化
        realm = Realm.getDefaultInstance()

        //Realmインスタンスからデータを取得するクエリを発行する
        val schedules = realm.where<Schedule>().findAll()
        //(listView二つインポート選択肢あり)、取得した全てのスケジュールをScheduleAdapterクラスのコンストラクタに渡し、リストビューに設定
        listView.adapter = ScheduleAdapter(schedules)

        fab.setOnClickListener { view ->
            //ScheduleEditActivityへの画面遷移の設定
            startActivity<ScheduleEditActivity>()
        }

            listView.setOnItemClickListener { parent, view, position, id ->
                //positionをgetItemPositionに渡せば、リスト内の指定された位置にあるデータ、つまりscheduleのインスタンスが取得できる
                val schedule = parent.getItemAtPosition(position) as Schedule
                //Scheduleインスタンスからidを取得し、インテントにschedule_idとして格納することでidをScheduleEditActivitに渡す
                startActivity<ScheduleEditActivity>("schedule_id" to schedule.id)

            }
    }




    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    //アクティビティの終了処理
    //ライフサイクル（アクティビティーが生成されてから終了されるまでの状態）であるonDestroyメソッドをオーバーライドしてcloseメソッドでRealmのインスタンスを破棄しリソースの開放
    override fun onDestroy() {
        super.onDestroy()
    realm.close()
    }
}
