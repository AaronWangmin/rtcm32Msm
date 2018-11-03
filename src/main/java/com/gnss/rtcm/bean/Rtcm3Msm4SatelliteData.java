package com.gnss.rtcm.bean;

import java.util.ArrayList;

import com.gnss.rtcm.common.util.BitUtil;

import lombok.Data;

@Data
public class Rtcm3Msm4SatelliteData {
	private ArrayList<Integer> millisecondsRoughRangeList;
	private ArrayList<Integer> dotMillisecondsRoungRangeList;
	
	public void addMillisecondsRoughRange(int millisecondsRoughRange) {
		millisecondsRoughRangeList.add(millisecondsRoughRange);
	}
	
	public void addDotMillisecondsRoughRange(int dotMillisecondsRoughRange) {
		dotMillisecondsRoungRangeList.add(dotMillisecondsRoughRange);
	}
	
	public int getSatelliteDataLength() {
		return millisecondsRoughRangeList.size() * (8 + 10);
	}
	
	public void parseRtcm3Msm4SatelliteData(byte[] rawBytes,Rtcm3MsmHeader rtcm3MsmHeader) {
		this.millisecondsRoughRangeList = new ArrayList<Integer>();
		this.dotMillisecondsRoungRangeList = new ArrayList<Integer>();
		
		int satelliteCount = rtcm3MsmHeader.getSatelliteCount();

		int satelliteDataStart = 193 + rtcm3MsmHeader.getCellCount();
		int dotMillisecondsRoughRangeStart = satelliteDataStart + (8 * satelliteCount);

		for (int i = 0; i < satelliteCount; i++) {
			int millisecondsRoughRange = (int) BitUtil.bytesDecodeR(rawBytes, satelliteDataStart + (i * 8), 8);
			this.addMillisecondsRoughRange(millisecondsRoughRange);

			int dotMillisecondsRoughRange = (int) BitUtil.bytesDecodeR(rawBytes,
					dotMillisecondsRoughRangeStart + (i * 10), 10);
			this.addDotMillisecondsRoughRange(dotMillisecondsRoughRange);
		}

	}
}
