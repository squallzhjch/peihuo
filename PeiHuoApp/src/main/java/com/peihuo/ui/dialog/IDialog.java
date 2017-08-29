package com.peihuo.ui.dialog;
/**
 * @ClassName:     Dialog.java
 * @version        V1.0
 * @Date           2015年9月17日 上午10:15:44
 * @Description:   所有弹出框的父类 
 */
public interface IDialog {

	//void result(T t);
	//public <T> void result(T t);
	//public <T> void showData(T t);
	/**
	 * 给弹出框赋值
	 * @param str
	 * @param obj
	 */
	public void setData(String str, Object obj);
	
	/**
	 * 给Diaolog传递的数据，显示时调用
	 * @param obj
	 */
	public void request(Object obj);
	
	/**
	 * 获得数据
	 * @return
	 */
	public Object getData();

}
