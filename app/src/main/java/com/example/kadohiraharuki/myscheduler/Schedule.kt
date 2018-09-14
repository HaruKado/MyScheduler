package com.example.kadohiraharuki.myscheduler

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

//Realmのモデルクラスは継承できるようにopen装飾子をつけておく
open class Schedule : RealmObject(){
    //@PrimaryKeyアノテーションを付加することにより、データを識別できるようにするための項目
    @PrimaryKey
    var id: Long = 0
    var date : Date = Date()
    var title: String = ""
    var detail: String = ""
}