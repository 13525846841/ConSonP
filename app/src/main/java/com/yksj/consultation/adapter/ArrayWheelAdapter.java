package com.yksj.consultation.adapter;

import java.util.ArrayList;
import java.util.HashMap;

/*
 *  Copyright 2010 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */



/**
 * Numeric Wheel adapter.
 */
public class ArrayWheelAdapter implements WheelAdapter {
	
	
	private Object data;
	private int length;
	private int MAX_NUMBER = 5; 

	/**
	 * Constructor
	 */
	public ArrayWheelAdapter(Object data,int length) {
		this.data = data;
		this.length = length;
	}
	

	@Override
	public String getItem(int index) {
		if(data instanceof Object[]){
			Object[] temp = (Object[])data;
			return (String)temp[index%temp.length];
		}
		
		if(data instanceof ArrayList){
			ArrayList<HashMap<String, String>> list = (ArrayList<HashMap<String, String>>)data;
			if(list.size() == 0)
				return "";
			String item = list.get(index%list.size()).get("name");
			return item.length()<=MAX_NUMBER ? item : item.substring(0, MAX_NUMBER)+"...";
		}
		return null;
	}

	@Override
	public int getItemsCount() {
		return length;
	}
	
	@Override
	public int getMaximumLength() {
		return -1;
	}
}
