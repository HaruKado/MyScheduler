package com.example.kadohiraharuki.myscheduler

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter

class ScheduleAdapter(data: OrderedRealmCollection<Schedule>?) : RealmBaseAdapter<Schedule>(data) {//RealmBaseAdapterを継承（Scheduleを継承）

    //ViewHolderクラスでViewオブジェクトを保持
    //android.R.layout.simple_list_item_2(レイアウトXML)の内部に配置されているtext1およびtext2のIDをもつテキストビューの内容をメンバー変数として保持
    inner class ViewHolder(cell: View){
        val date = cell.findViewById<TextView>(android.R.id.text1)
        val title = cell.findViewById<TextView>(android.R.id.text2)
    }
    //getViewでリストビューのセルのデータが必要になるたびに呼び出され、表示するビューを戻り値として返す
    //position:リストビューのセルの位置を受け取る、convertView:すでに作成ずみのセルを表すビューを受け取る、parent:親のリストビューを受け取る
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
     val view: View
     val viewHolder: ViewHolder

        //convertViewがnullであった時の処理、メモリ不足に陥らせない為セルを使いまわす
        when(convertView){
            null -> {
                //LayoutInflaterのクラスメソッドであるfromを使用してインスタンスを作成
                //引数のコンテキストは、getViewに第三引数として渡される親のリストビューを使用して取得する
                //実際にXMLファイルから画面レイアウトを作成しているのがinflateメソッド
                val inflater = LayoutInflater.from(parent?.context)
                //inflateメソッドでXMLファイルからビューを作成、resource: XMLファイルのID,android.R.layout.simple_list_item_2はandroidSDKに元々用意されているレイアウトファイル
                view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false)
                //viwHolderクラスのインスタンスを生成しセル用ビューのタグの中に保持
                viewHolder = ViewHolder(view)
                view.tag = viewHolder
            }

            else -> {
                view = convertView
                //ViewHolderからtagプロパティで取り出し、viewHolderに格納
                viewHolder = view.tag as ViewHolder
            }
        }

        //adapterDataプロパティにはデータのリストが入っている、安全呼び出し演算子でnullチェックを行い、nullでない場合runする
        adapterData?.run{
            //getメソッドで何番目のデータを取得するのか指定すれば、そのデータを取得することができる
            val schedule =  get(position)
            //TextViewに日付と文字列をセット、日付はDate型で保持している為、文字列を事前に変換する
            viewHolder.date.text = DateFormat.format("yyyy/MM/dd", schedule.date)
            viewHolder.title.text = schedule.title
        }
        //viewを返す
        return view
    }
}