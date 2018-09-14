package com.example.kadohiraharuki.myscheduler

import android.app.Application
import io.realm.Realm

class MySchedulerApplication : Application(){ //Applicationクラスを継承

   //アプリケーション実行時に処理を行う
    override fun onCreate(){
        super.onCreate()
       //Realmをinitで初期化
        Realm.init(this)
    }

}