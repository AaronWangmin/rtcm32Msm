package com.gnss.rtcm.bean;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gnss.bean.SatelliteData;
import com.gnss.bean.SignalData;

public class SatelliteDataTest {
	
	SignalData signalData = new SignalData("L1","CA",10000,111111,51.1,31);
	SignalData signalData2 = new SignalData("L2","P",20000,222222,52.2,32);

	@Test
	public void test() {
		SatelliteData satelliteData = new SatelliteData("BDS",20);
		satelliteData.addSignalData(signalData);
		satelliteData.addSignalData(signalData2);
		
		System.out.println(satelliteData);
	}

}
