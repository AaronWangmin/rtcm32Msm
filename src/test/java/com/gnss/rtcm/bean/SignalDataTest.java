package com.gnss.rtcm.bean;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gnss.bean.SignalData;

public class SignalDataTest {

	@Test
	public void test() {
		SignalData signalData = new SignalData("L1","CA",10000,111111,51.1,31);
		SignalData signalData2 = new SignalData("L2","P",20000,222222,52.2,32);
		
		System.out.println(signalData);
		System.out.println(signalData2);
	}

}
