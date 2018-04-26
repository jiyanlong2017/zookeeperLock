/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package com.sinosoft.microservice.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Utils - Date
 * 
 * @author 纪炎龙
 * @version 3.1
 */
public final class DateUtils {


	/**
	 * 不可实例化
	 */
	private DateUtils() {
	}

	/**
	 * 得到简单时间
	 * 
	 * @param value
	 * @return 如：xx日 xx：xx
	 */
	public static String  toSimpleTime(Date value) {
		if (value==null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(value);
		return calendar.get(Calendar.DAY_OF_MONTH)+"日"+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);		
	}
	/**
	 * 将日期设置为当天最早的时间
	 * 
	 * @param value
	 * @return 
	 */
	public static Date toDayMin(Date value) {
		if (value==null) {
			return null;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(value);
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
		return calendar.getTime();		
	}
	/**
	 * 将日期设置为当天最晚的时间
	 * 
	 * @param value
	 * @return 
	 */
	public static Date toDayMax(Date value) {
		if (value==null) {
			return null;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(value);
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
		return calendar.getTime();		
	}
	
	/**
	 * 将日期设置为当月最早的时间
	 * @param value
	 * @return
	 */
	public static Date toMonthMin(Date value) {
		if (value==null) {
			return null;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(value);
		calendar.set(Calendar.DAY_OF_MONTH,1);		
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
		return calendar.getTime();		
	}
	
	/**
	 * 将日期设置为当月最晚的时间
	 * @param value
	 * @return
	 */
	public static Date toMonthMax(Date value) {
		if (value==null) {
			return null;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(value);
		calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));		
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
		return calendar.getTime();		
	}
	
	/**
	 * 
	* @Title: getDateDiffMinute 时间差  单位：分
	* @Description: TODO
	* @param @param a
	* @param @param b
	* @param @return 
	* @return long 
	* @throws
	 */
	public static long getDateDiffMinute(Date a,Date b){
		if (a==null) {
			return 0;
		}
		if (b==null) {
			return 0;
		}
		
		try
		{
		    long diff = a.getTime() - b.getTime();
		    return  diff / (1000*60);
		}
		catch (Exception e)
		{
			return 0;
		}
	}
	/**
	 *
	 * @Title: getDateDiffMinute 时间差  单位：天
	 * @Description: TODO
	 * @param @param a
	 * @param @param b
	 * @param @return
	 * @return long
	 * @throws
	 */
	public static int getDateDiffDay(Date a,Date b){
		Calendar startDate =Calendar.getInstance();
		startDate.setTime(a);
		Calendar endDate =Calendar.getInstance();
		endDate.setTime(b);
		int day1=startDate.get(Calendar.DAY_OF_YEAR);
		int day2=endDate.get(Calendar.DAY_OF_YEAR);

		int year1=startDate.get(Calendar.YEAR);
		int year2=endDate.get(Calendar.YEAR);

		if(year1!=year2){
			int timeDistance=0;
			for(int i=year1;i<year2;i++){
				if(i%4==0 && i%100!=0 ||i%400==0){
                    timeDistance+=366;

				}else{
					timeDistance+=365;
				}

			}
			return timeDistance+(day2 -day1);
		}else {
			return day2-day1;
		}
	}

	/** 
	* 字符串转换成日期 
	* @param str 
	* @return date 
	*/ 
	public static Date StrToDate(String str,String iformat) { 	  
	   SimpleDateFormat format = new SimpleDateFormat(iformat); 
	   Date date = null; 
		   try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		} 
	   return date; 
	}


	/**
	 * 默认 yyyy-MM-dd h24:mm:ss
	 * @return
     */
	public static String getCurrentTime(){
		return format(new Date(),"yyyy-MM-dd HH:mm:ss");
	}

	public static String getCurrentTime(String format){
		return format(new Date(), format);
	}

	//日期转换成字符串 
	public static String format(Date iDate,String format) {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(iDate);
    }

	//字符串日期格式化
	public static String format(String iDate,String format) {

		SimpleDateFormat sf = new SimpleDateFormat(format);

		try {
			Date date = sf.parse(iDate);
			return format(date, format);
		} catch (ParseException e) {
			e.printStackTrace();
		}
			return null;
	}

	/**
	 * 返回中文时间差
	 * @param d
	 * @return
	 */
	public static String toChineseChar(Date d){
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		SimpleDateFormat dfs2 = new SimpleDateFormat("MM-dd");
		SimpleDateFormat dfs3 = new SimpleDateFormat("HH:mm");
        long between = 0;
        String cc="";
        String dd="";
        try {
            between = ((new Date()).getTime() - d.getTime() );// 得到两者的毫秒数
            cc=  dfs2.format(d);
            dd=  dfs3.format(d);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        long day = between / (24 * 60 * 60 * 1000);
        
        //天数补差
        if((toDayMin(new Date()).getTime() - d.getTime()) > 0){
        	day=day+1;
        }
        
        
        if(day==0){
        	long hour=(between/(60*60*1000)-day*24);
        	if(hour==0){
        		long min=((between/(60*1000))-day*24*60-hour*60);
        		if (min==0){
        			 long s=(between/1000-day*24*60*60-hour*60*60-min*60);
        			 return String.valueOf(min)+"秒前";
        		}
        		return String.valueOf(min)+"分钟前";
        	}
        	cc="今天";
        }else if(day==1){
        	cc="昨天";        	
        }else if(day==2){
        	cc="前天";        	
        }
        return cc+ " " +dd;
	}

	/**
	 * 判断两个是日期是否为1天
	 * @param
	 * @return
	 */
	public static boolean sameDate(Date d1,Date d2){
		if(null==d1||null==d2){
			return false;
		}
		Calendar cal=Calendar.getInstance();
		cal.setTime(d1);
		//cal.add(Calendar.YEAR,1);
		Calendar cal1=Calendar.getInstance();
		cal1.setTime(d2);
		return cal.getTime().equals(cal1.getTime());

	}
	/**
	 * 判断今年是否为闰年，平年
	 * @param
	 * @return
	 */
	public static boolean isLeapYear (Date d1){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String formatDate=sdf.format(d1);
		int a=Integer.valueOf(formatDate);
		if((a%4==0&&a%100!=0)||a%400==0){
			return  true;
		}else{
			return  false;
		}
	}

	/**
	 * 得到当前日期的相当日期
	 * @param i 相当几天 例： 昨天就是 -1 明天就是 1
	 * @return 返回 yyyy-MM-dd 类型
	 */
	public static String getOtherDay(Date date, int i){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, i);
		Date time = calendar.getTime();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(time);

	}

//	public static void main(String[] args){
////		System.out.print(toChineseChar(StrToDate("2015-03-27 23:37:09","yyyy-MM-dd hh:mm:ss")));
////		System.out.print(toMonthMin(new Date()));
////		System.out.print(toMonthMax(new Date()));
////		System.out.print(getCurrentTime());
////		String startDate="2017-09-26 00:00:00";
////		String startDate1="2017-10-22 00:00:00";
////		Date startDate3=StrToDate(startDate,"yyyy-MM-dd hh:mm:ss");
////		Date startDate4=StrToDate(startDate1,"yyyy-MM-dd hh:mm:ss");
////		System.out.println("时间差几天："+getDateDiffDay(startDate3, startDate4));
////		System.out.println("是否为闰年："+isLeapYear(startDate3));
//
//         int a[]={1492542,1492590,1492616,1492617,1492628,1492669,1492524};
//         if(a[0]>a[1]){
//			 System.out.println("11111111111111111111111111111");
//		 }
//         int temp=0;
//         for(int i=1;i<a.length;i++){
//         	for(int j=0;j<a.length-i;j++){
//         		if(a[j]>a[j+1]){
//         			temp=a[j];
//         			a[j]=a[j+1];
//         			a[j+1]=temp;
//				}
//			}
//		 }
//         for(int i=0;i<a.length;i++){
//			 System.out.println(a[i]);
//		 }
//	}
}