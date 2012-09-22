package org.eclipse.e4.paho.client.databinding;

import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class StringToMqttMessageConverter extends Converter {

	@Override
	public Object convert(Object fromObject) {
		String s = (String) fromObject;
		return new MqttMessage(s.getBytes());
	}
	
	public StringToMqttMessageConverter() {
		super(MqttMessage.class,String.class);
	}
	
	

}
