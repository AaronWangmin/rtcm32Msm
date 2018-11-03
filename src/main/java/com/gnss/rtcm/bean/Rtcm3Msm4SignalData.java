package com.gnss.rtcm.bean;

import java.util.ArrayList;

import com.gnss.rtcm.common.util.BitUtil;

import lombok.Data;

@Data
public class Rtcm3Msm4SignalData {
	private ArrayList<Integer> finePseudorangeList;
	private ArrayList<Integer> finePhaseRangeList;
	private ArrayList<Integer> phaseRangeLockTimeIndicatorList;
	private ArrayList<Integer> halfCycleAmbiguityIndicatorList;
	private ArrayList<Integer> snrList;
	
	public void addFinePseudorange(int finePseudorange) {
		this.addFinePseudorange(finePseudorange);
	}
	
	public void addFinePhaseRange(int finePhaseRange) {
		this.addFinePseudorange(finePhaseRange);
	}
	
	public void addPhaseRangeLockTimeIndicator(int phaseRangeLockTimeIndicator) {
		this.addFinePseudorange(phaseRangeLockTimeIndicator);
	}
	
	public void addHalfCycleAmbiguityIndicator(int halfCycleAmbiguityIndicator) {
		this.addFinePseudorange(halfCycleAmbiguityIndicator);
	}
	
	public void addSnr(int snr) {
		this.addFinePseudorange(snr);
	}
	
	public void parseRtcm3Msm4SignalData(byte[] rawBytes,Rtcm3MsmHeader rtcm3MsmHeader,Rtcm3Msm4SatelliteData rtcm3Msm4SatelliteData) {
		this.finePseudorangeList = new ArrayList<Integer>();
		this.finePhaseRangeList = new ArrayList<Integer>();
		this.phaseRangeLockTimeIndicatorList = new ArrayList<Integer>();
		this.halfCycleAmbiguityIndicatorList = new ArrayList<Integer>();
		this.snrList = new ArrayList<Integer>();
		
		int valiableCellCount = rtcm3MsmHeader.getValiableCellCount();
		int signalDataStart = 24 + rtcm3MsmHeader.getHeaderLength() + rtcm3Msm4SatelliteData.getSatelliteDataLength();
		int finePseudorangeStart = signalDataStart;
		int finePhaseRangeStart = finePseudorangeStart + (valiableCellCount * 15);
		int phaseRangeLockTimeStart = finePhaseRangeStart + (valiableCellCount * 22);
		int halfCycleAmbiguityStart = phaseRangeLockTimeStart + (valiableCellCount * 4);
		int snrStart = halfCycleAmbiguityStart + (valiableCellCount * 1);
		
		for(int i = 0; i < valiableCellCount; i++) {
			int finePseudorange = (int)BitUtil.bytesDecodeR(rawBytes,
					finePseudorangeStart + (i * 15), 15);
			finePseudorangeList.add(finePseudorange);
			
			int finePhaseRange = (int)BitUtil.bytesDecodeR(rawBytes,
					finePhaseRangeStart + (i * 22), 22);
			finePhaseRangeList.add(finePhaseRange);
			
			int phaseRangeLockTime = (int)BitUtil.bytesDecodeR(rawBytes,
					phaseRangeLockTimeStart + (i * 4), 4);
			phaseRangeLockTimeIndicatorList.add(phaseRangeLockTime);
			
			int halfCycleAmbiguity = (int)BitUtil.bytesDecodeR(rawBytes,
					halfCycleAmbiguityStart + (i * 1), 1);
			halfCycleAmbiguityIndicatorList.add(halfCycleAmbiguity);

			int snr = (int)BitUtil.bytesDecodeR(rawBytes, snrStart + (i * 6), 6);
			snrList.add(snr);
		}
	}
}
