package org.eclipse.e4.paho.client.databinding;

import org.eclipse.core.databinding.conversion.Converter;

public class StringToCharArrayConverter extends Converter {

	@Override
	public char[] convert(Object fromObject) {
		String s = (String) fromObject;
		if (s!=null){
			return s.toCharArray();
		}
		return null;
	}
	
	public StringToCharArrayConverter() {
		super(String.class,char[].class);
	}

}
