package com.gnss.rtcm.bean;

import java.util.ArrayList;
import java.util.Collections;

import com.gnss.rtcm.common.util.BitUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class Rtcm3MsmHeader {
	private int messageNumber;
	private int referenceStationId;
	private int epochTime;
	private int multipleMessageBit;
	private int iods;
	private int clockSteeringIndicator;
	private int externalClockIndicator;
	private int smoothingIndicator;
	private int smoothingInterval;
	private long satelliteMask;
	private long signalMask;
	private long cellMask;
	
	public boolean isValiableRtcm3Msm() {
		if (messageNumber >= 1071 && messageNumber <= 1077) return true;
		if (messageNumber >= 1081 && messageNumber <= 1087) return true;
		if (messageNumber >= 1121 && messageNumber <= 1127) return true;
		
		return false;
	}
	public String getSatelliteSystem() {
		int messageNumber = this.messageNumber;
		
		if (messageNumber >= 1071 && messageNumber <= 1077)
			return "GPS";
		if (messageNumber >= 1081 && messageNumber <= 1087)
			return "GlONASS";
		if (messageNumber >= 1091 && messageNumber <= 1097)
			return "GELLILEO";
		if (messageNumber >= 1121 && messageNumber <= 1127)
			return "BDS";

		return null;
	}
	
	
	public ArrayList<Integer> getSatellitePrnList() {
		return getSatelliteAndSingnalMaskMap(this.satelliteMask, 64);
	}
	
	public ArrayList<Integer> getSignalCodeList() {
		return getSatelliteAndSingnalMaskMap(this.signalMask, 32);
	}

	private ArrayList<Integer> getSatelliteAndSingnalMaskMap(long mask, int size) {
		ArrayList<Integer> maskValueList = new ArrayList<Integer>();
		for (int i = 0; i < size; i++, mask = mask >> 1) {
			if ((mask & 1) == 1) {
				int value = size - i;
				maskValueList.add(value);
			}
		}
		Collections.sort(maskValueList);
		return maskValueList;
	}
	
	// TODO...
	public String[] getFrequencyBand() {
		int signalCount = getSignalCount();
		
		if(signalCount == 2){
			return new String[] {"L1","L2"};
		}else if(signalCount == 3) {
			return new String[] {"L1","L2","L3"};
		}else if(signalCount == 4) {
			return new String[] {"L1","L2","L3","L5"};
		}else {
			log.error("The count of availabe signal mask: " + signalCount + " is not correct");
			return null;
		}
	}
	
	public int getSatelliteCount() {
		return getSatellitePrnList().size();
	}
	
	public int getSignalCount() {
		return getSignalCodeList().size();
	}
	
	public int getCellCount() {
		return getSatelliteCount() * getSignalCount();
	}
	
	public int getValiableCellCount() {
		return getSatelliteAndSingnalMaskMap(this.cellMask,getCellCount()).size();
	}
	
	public int getHeaderLength() {
		return 169 + getCellCount();
	}
	
	public boolean isValilableCell(int index) {
		int cellCount = getCellCount();
		return ((this.cellMask >> (cellCount-1-index)) & 1) == 1;
	}
	
	public void showFormat() {
		log.info("Rtcm3MsmHeader................................................");
		log.info(this.getSatelliteSystem());
		log.info("SatelliteMask: " + Long.toBinaryString(this.getSatelliteMask()) + ": " 
				+ "SatelliteCount: " + this.getSatelliteCount() + ": "
				+ "SatellitePrnList: " + this.getSatellitePrnList().toString());
		log.info("SignalMask: " + Long.toBinaryString(this.getSignalMask()) + ": "
				+ "SignalCount: " + this.getSignalCount() + ": "
				+ "SignalMaskCode: " + this.getSignalCodeList().toString());
		log.info("CellMask: " + Long.toBinaryString(this.getCellMask()) + ": "
				+ "CellCount: " + this.getCellCount() + ": "
				+ "ValiableCellCount: " + this.getValiableCellCount() + ": ");
	}
	
	
	
	/**
	 * parse Header of MSM
	 */
	public void parseRtcm3MsmHeader(byte[] rawBytes) {

		int messageNumber = (int) BitUtil.bytesDecodeR(rawBytes, 3 * 8, 12);
//		log.info("messageNumber: "+ messageNumber);
		this.setMessageNumber(messageNumber);
		if(!isValiableRtcm3Msm()) return;
		
		int epochTime = (int)BitUtil.bytesDecodeR(rawBytes, 48, 30);
		if (messageNumber >= 1081 && messageNumber <= 1087) {
			epochTime = (int)BitUtil.bytesDecodeR(rawBytes, 51, 27);
		}
//		if (satelliteSystem.equals("BDS")) {
//			epochTime += 14000;
//		}

		int multipleMessageBit = (int) BitUtil.bytesDecodeR(rawBytes, 78, 1);
		this.setMultipleMessageBit(multipleMessageBit);
		
		// TODO............
		boolean sendFlag = true;
		sendFlag = multipleMessageBit == 0;

		int iods = (int) BitUtil.bytesDecodeR(rawBytes, 79, 3);
		this.setIods(iods);

		int clockSteeringIndicator = (int) BitUtil.bytesDecodeR(rawBytes, 89, 2);
		this.setClockSteeringIndicator(clockSteeringIndicator);

		int externalClockIndicator = (int) BitUtil.bytesDecodeR(rawBytes, 91, 2);
		this.setExternalClockIndicator(externalClockIndicator);

		int smoothingIndicator = (int) BitUtil.bytesDecodeR(rawBytes, 93, 1);
		this.setSmoothingIndicator(smoothingIndicator);

		int smoothingInterval = (int) BitUtil.bytesDecodeR(rawBytes, 94, 3);
		this.setSmoothingInterval(smoothingInterval);

		long satelliteMask = BitUtil.bytesDecodeR(rawBytes, 97, 64);
		this.setSatelliteMask(satelliteMask);

		long signalMask = BitUtil.bytesDecodeR(rawBytes, 161, 32);
		this.setSignalMask(signalMask);

		int allCellCount = this.getCellCount();
		long cellMask = BitUtil.bytesDecodeR(rawBytes, 193, allCellCount);
		this.setCellMask(cellMask);

		// TODO...............
		this.setEpochTime(epochTime);
	}
}
