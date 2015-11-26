package com.example.androidtest;

import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import java.util.List;

/**
 * Created by Rock on 2015/6/17.
 */
public class SmsUtils {

	public static enum TelCo {
		UNICOM, CMCC, CHINANET
	}

	public static class Sms {
		public String senderNumber;
		public String receiverNumber;
		public String content;
	}

	public static boolean sendMessage(String receiverPhone, String message) {
		if (message == null)
			return false;
		try {
			SmsManager sm = SmsManager.getDefault();
			List<String> texts = sm.divideMessage(message);
			for (String text : texts) {
				sm.sendTextMessage(receiverPhone, null, message, null, null);
			}
			return true;
		} catch (Throwable ignore) {
		}
		return false;
	}

	public static Sms parseMessage(Context context, Intent intent) {
		try {
			Object[] pduses = (Object[]) intent.getExtras().get("pdus");
			StringBuilder content = new StringBuilder();
			Sms sms = new Sms();
			for (Object pdus : pduses) {
				if (!(pdus instanceof byte[]))
					continue;
				SmsMessage msg = SmsMessage.createFromPdu((byte[]) pdus);
				if (msg.getOriginatingAddress() != null)
					sms.senderNumber = msg.getOriginatingAddress();
				// Date date = new Date(msg.getTimestampMillis());
				if (msg.getMessageBody() != null)
					content.append(msg.getMessageBody());
			}
			sms.content = content.toString();
			return sms;
		} catch (Throwable ignore) {
		}
		return null;
	}

	public static String getServiceNumber(TelCo telephoneCompany) {
		if (telephoneCompany == null)
			return null;
		switch (telephoneCompany) {
		case UNICOM:
			return "10010";
		case CMCC:
			return "10086";
		case CHINANET:
			return "10000";
		}
		return null;
	}

}
