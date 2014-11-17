package com.mytest;

import com.hibernate.Event;

public class MyTestCase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		outHbmPath(Event.class);
	}

	/**
	 * 输出Hibernate持久化类映射文件的完整路径
	 */
	public static void outHbmPath(Class<?> persistentClass) {
		String mappingResourceName = persistentClass.getName()
				.replace('.', '/')
				+ ".hbm.xml";
		System.out.println(mappingResourceName);
	}

}
