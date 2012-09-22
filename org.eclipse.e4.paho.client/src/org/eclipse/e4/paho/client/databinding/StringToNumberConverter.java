package org.eclipse.e4.paho.client.databinding;

import org.eclipse.core.databinding.conversion.Converter;

public class StringToNumberConverter extends Converter{

	@Override
	public Integer convert(Object fromObject) {
		Integer result = null;
		try {
			result = Integer.valueOf((String) fromObject);
		} catch (NumberFormatException e) {
		}
		return result;
	}
	
	public StringToNumberConverter() {
		super(String.class, Integer.class );
	}
	
	
	

	
	
	
	
}
