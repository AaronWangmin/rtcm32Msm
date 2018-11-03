package com.gnss.rtcm.bean;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gnss.bean.GnssObservationData;
import com.gnss.bean.SatelliteData;
import com.gnss.bean.SignalData;

public class GnssObservationDataTest {
	SignalData signalData = new SignalData("L1","CA",10000,111111,51.1,31);
	SignalData signalData2 = new SignalData("L2","P",20000,222222,52.2,32);
	
	SatelliteData satelliteData = new SatelliteData("GPS", 11);
	
	SignalData signalData3 = new SignalData("B1","CA",30000,33333,53.3,33);
	SignalData signalData4 = new SignalData("B2", "P",40000,44444,54.4,34);
	
	SatelliteData satelliteData2 = new SatelliteData("BDS", 22);

	@Test
	public void test() {
		satelliteData.addSignalData(signalData);
		satelliteData.addSignalData(signalData2);
		
		satelliteData2.addSignalData(signalData3);
		satelliteData2.addSignalData(signalData4);
		
		GnssObservationData gnssObservationData = new GnssObservationData();
		gnssObservationData.addSatelliteData(satelliteData);
		System.out.println(gnssObservationData);
		
		gnssObservationData.addSatelliteData(satelliteData2);
		System.out.println(gnssObservationData);
		
	}

}
