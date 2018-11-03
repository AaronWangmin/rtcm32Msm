package com.gnss.bean;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

//@Component
//@Scope("prototype")
@Data
@NoArgsConstructor
public class SatelliteData {
	private String satelliteSystem;	// GPS/GLONASS/BDS/GELLILEO
	
	private int prn;
	
	/**
	 * Map<String frequencyBand,SignalData signalData>
	 */
	private Map<String,SignalData> signalDataMap = new HashMap<String,SignalData>();
	
	public SatelliteData(String satelliteSystem,int prn) {
		this.satelliteSystem = satelliteSystem;
		this.prn = prn;
		this.signalDataMap = new HashMap<String,SignalData>();
	}
	
	public void addSignalData(SignalData signalData) {
		signalDataMap.put(signalData.getFrequencyBand(), signalData);
	}
	
	public SignalData getSignalData(String frequencyBand) {
		return signalDataMap.get(frequencyBand);
	}
	
	public Collection<SignalData> getAllSignalData(){
		return signalDataMap.values();
	}

}
