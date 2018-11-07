package com.gnss.rtcm.handler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.apache.mina.core.session.IoSession;

import com.gnss.bean.GnssObservationData;
import com.gnss.bean.SatelliteData;
import com.gnss.bean.SignalData;
import com.gnss.rtcm.bean.Rtcm3Msm4SatelliteData;
import com.gnss.rtcm.bean.Rtcm3Msm4SignalData;
import com.gnss.rtcm.bean.Rtcm3MsmHeader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Rtcm32Handler extends AbsHandler {

	private GnssObservationData gnssObservationData;

	private Rtcm3MsmHeader rtcm3MsmHeader;
	private Rtcm3Msm4SatelliteData rtcm3Msm4SatelliteData;
	private Rtcm3Msm4SignalData rtcm3Msm4SignalData;

	@Override
	public void handle(IoSession session, byte[] rawBytes) {
		rtcm3MsmHeader = new Rtcm3MsmHeader();
		rtcm3MsmHeader.parseRtcm3MsmHeader(rawBytes);
		if(!rtcm3MsmHeader.isValiableRtcm3Msm()) {
			return;
		}
		
		rtcm3MsmHeader.showFormat();

		rtcm3Msm4SatelliteData = new Rtcm3Msm4SatelliteData();
		rtcm3Msm4SatelliteData.parseRtcm3Msm4SatelliteData(rawBytes,rtcm3MsmHeader);
//		log.info(rtcm3Msm4SatelliteData.getMillisecondsRoughRangeList().size() + ":" + rtcm3Msm4SatelliteData.getDotMillisecondsRoungRangeList().size());
		
		rtcm3Msm4SignalData = new Rtcm3Msm4SignalData();
		rtcm3Msm4SignalData.parseRtcm3Msm4SignalData(rawBytes, rtcm3MsmHeader, rtcm3Msm4SatelliteData);
//		log.info(rtcm3Msm4SignalData.getFinePhaseRangeList().size() + ": " + rtcm3Msm4SignalData.getFinePhaseRangeList().toString());
		
		ArrayList<Integer> satellitePrnList = rtcm3MsmHeader.getSatellitePrnList();
		int signalCount = rtcm3MsmHeader.getSignalCount();
		int satelliteCount = rtcm3MsmHeader.getSatelliteCount();
		
		gnssObservationData = new GnssObservationData();

		int valiableCellIndex = 0;
		for (int i = 0; i < satelliteCount; i++) {
			SatelliteData satelliteData = new SatelliteData();

			satelliteData.setSatelliteSystem(rtcm3MsmHeader.getSatelliteSystem());
			satelliteData.setPrn(satellitePrnList.get(i));

			for (int j = 0; j < signalCount; j++) {
				int test = i * signalCount + j;
				boolean isValid = rtcm3MsmHeader.isValilableCell(i * signalCount + j);
				if (isValid) {
					SignalData signalData = new SignalData();

					String frequencyBand = rtcm3MsmHeader.getFrequencyBand()[j];
					signalData.setFrequencyBand(frequencyBand);

					int finePseudorange = rtcm3Msm4SignalData.getFinePseudorangeList().get(valiableCellIndex);
					double speedLight = 299792458;
					double pseudorange = (speedLight / 1000)
							* (rtcm3Msm4SatelliteData.getMillisecondsRoughRangeList().get(i)
									+ rtcm3Msm4SatelliteData.getDotMillisecondsRoungRangeList().get(i) / 1024
									+ Math.pow(2, -24) * finePseudorange);
					signalData.setPseudorange(pseudorange);

					int finePhaseRange = rtcm3Msm4SignalData.getFinePhaseRangeList().get(valiableCellIndex);
					double phaseRange = (speedLight / 1000)
							* (rtcm3Msm4SatelliteData.getMillisecondsRoughRangeList().get(i)
									+ rtcm3Msm4SatelliteData.getDotMillisecondsRoungRangeList().get(i) / 1024
									+ Math.pow(2, -29) * finePhaseRange);
					signalData.setPhaseRange(phaseRange);

					int cnr = rtcm3Msm4SignalData.getSnrList().get(valiableCellIndex);
					signalData.setSnr(cnr);

					valiableCellIndex++;
					
					satelliteData.addSignalData(signalData);
				}
			}
			gnssObservationData.addSatelliteData(satelliteData);
			
		}
		
		this.test();

	}

	public void test() {
		Set<String> allSystems = gnssObservationData.getSatelliteDataMap().keySet();
		for (String system : allSystems) {
			HashMap<Integer, SatelliteData> satellites = gnssObservationData.getSatelliteDataMap().get(system);
			log.info(system + ":" + satellites.size());
			
			for (SatelliteData satelliteData : satellites.values()) {
				log.info("prn: " + satelliteData.getPrn() + "***" + satelliteData.getSignalDataMap().size());
				for (SignalData signalData : satelliteData.getAllSignalData()) {
					DecimalFormat df = new DecimalFormat("###############0.00");// 16位整数位，两小数位
					log.info("***" +signalData.getFrequencyBand() + "***" + df.format(signalData.getPseudorange()) + "***"
							+ df.format(signalData.getPhaseRange()) + "***" + df.format(signalData.getSnr()));
				}

			}
		}
	}

}
