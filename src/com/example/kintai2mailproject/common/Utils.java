package com.example.kintai2mailproject.common;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	/**
	 * 時間、分が１桁の場合、０を先頭につける
	 * @param i
	 * @return str
	 */
	public static String timeToString(int i){

		Integer integer = i;
		String str;
		if(integer < 10){
			str = "0" + integer.toString();
			return str;

		}
		str = integer.toString();
		return str;

	}

	/**
	 * 今日の日付をyyyy/MM/dd形式で取得
	 * @return String 日付
	 */
	public static String getStrToday(){

		Date date = new Date();

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

		String str = dateFormat.format(date);

		return str;
	}

	/**
	 * 空白値の場合trueを返却する
	 *
	 */
	public static boolean isEmpty(String str){

		if(str == "" || str == null){
			return true;
		}
		return false;
	}

	/**
	 * Nullの場合tureを返却する
	 *
	 */
	public static boolean isNull(Object obj){
		if(obj == null){
			return true;
		}

		return false;
	}

}
