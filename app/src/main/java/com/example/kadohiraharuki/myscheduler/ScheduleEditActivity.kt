package com.example.kadohiraharuki.myscheduler

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_schedule_edit.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ScheduleEditActivity : AppCompatActivity() {

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_edit)
        //MainActivityの場合と同様に,Realmのインスタンスを取得
        realm = Realm.getDefaultInstance()

        //MainActivityで作成したインテントに格納したschedule_idをの値を取得して変数scheduleIdに格納、取得できない場合scheduleId＝ー１
        val scheduleId = intent?.getLongExtra("schedule_id", -1L)
        if (scheduleId != -1L) {
            //scheduleIdが-1以外の場合更新
            val schedule = realm.where<Schedule>()
                    .equalTo("id", scheduleId).findFirst()
            dateEdit.setText(
                    DateFormat.format("yyy/MM/dd", schedule?.date))
            titleEdit.setText(schedule?.title)
            detailEdit.setText(schedule?.detail)
            delete.visibility = View.VISIBLE
        } else {
            delete.visibility = View.INVISIBLE
        }


        //データを削除する機能
        delete.setOnClickListener {
            realm.executeTransaction {
                realm.where<Schedule>().equalTo("id", scheduleId)
                        ?.findFirst()
                        ?.deleteFromRealm()
            }
            alert("削除しました") {
                yesButton { finish() }

            }.show()
        }



        save.setOnClickListener{
         when(scheduleId) {
             -1L -> {
                 //データベースの書き込み時にはexecuteTransactionメソッドを使う、開始、終了、キャンセル処理を自動で行ってくれる
                 //Realm.Transactionのインターフェイスを実装したクラスでは、executeメソッドをオーバーライドして実行したいデータベース処理を記述
                 realm.executeTransaction {
                     //executeメソッド内では、RealmQueryクラスのmaxメソッドで、Scheduleのidを取得
                     val maxId = realm.where<Schedule>().max("id")
                     //idの最大値+1を次に新規に登録するモデルのidとする
                     //変数maxIdはnullの可能性がある為null以外の場合はLong型に、nullの場合はLong型の0を取得し、それに＋１した値をnextIdに代入
                     val nextId = (maxId?.toLong() ?: 0L) + 1
                     //RealmインスタンスのcreateObjectメソッドを使い、データを一行追加
                     val schedule = realm.createObject<Schedule>(nextId)
                     //日付を設定する処理はtoDate（拡張関数）を定義してTextEditに入力された日付を表す文字列をDate型の値に変換してから設定
                     dateEdit.text.toString().toDate("yyy/MMM/dd")?.let {
                         schedule.date = it
                     }
                     schedule.title = titleEdit.text.toString()
                     schedule.detail = detailEdit.text.toString()
                 }
                 //Ankoを使って簡略化、追加しましたというメッセージを表示
                 alert("追加しました") {
                     yesButton { finish() }
                 }.show()
             }
             else -> {
                 realm.executeTransaction{
                     val schedule = realm.where<Schedule>()
                             .equalTo("id", scheduleId).findFirst()
                     dateEdit.text.toString().toDate("yyy/MM/dd")?.let{
                         schedule?.date = it
                     }
                     schedule?.title = titleEdit.text.toString()
                     schedule?.detail = detailEdit.text.toString()
                 }
                 alert("修正しました") {
                     yesButton { finish() }
                 }.show()
             }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    //toDate(拡張関数)を定義してTextEditに入力された日付を表す文字列をDate型の値に変換してから設定
    fun String.toDate(pattern: String = "yyyy/MM/dd HH:mm"): Date?{
        val sdFormat = try {
            SimpleDateFormat(pattern)
        }
        catch (e:IllegalArgumentException){
            null
        }
        val date = sdFormat?.let{
            try{
                it.parse(this)
            }
            catch(e: ParseException){
                null
            }
        }
        return date
    }
}
