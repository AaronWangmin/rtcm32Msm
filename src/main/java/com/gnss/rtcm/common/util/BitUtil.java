package com.gnss.rtcm.common.util;

/**
 * 
* bit数据处理公用类
* @ClassName: BitUtil 
* @Description: TODO
* @author 
* @date 2014-4-29 下午2:11:36 
*
 */
public class BitUtil {
	
	public static final int[] low=new int[]{0,1,3,7,15,31,63,127,255};
	
	public static long bytesDecode(byte[] data,int startOffset,final int lengthIn){
		long result=0;
		int  startByte=startOffset/8;
		int  startbit=startOffset%8;
		int length=lengthIn;
		while(length>0){
			if(startbit+length<8){
				result+=get(data[startByte], length, 8-startbit-length)<<(lengthIn-length);break;
			}else {
				result+=get(data[startByte], 8-startbit, startbit)<<(lengthIn-length);
				length-=8-startbit;
				startbit=0;
				startByte+=1;
			}
		}
		return result;
	}
	
	public static long bytesDecodeR(byte[] data,int startOffset,final int lengthIn){
		long result=0;
		int  startByte=startOffset/8;
		int  startbit=startOffset%8;
		int length=lengthIn;
		while(length>0){
			if(startbit+length<8){
				result+=get(data[startByte], length, 8-startbit-length);break;
			}else {
				length-=8-startbit;
				result+=get(data[startByte], 8-startbit, 0)<<length;
				startbit=0;
				startByte+=1;
			}
		}
		return result;
	}
	
	public static double bytesDouble(byte[]data,int startOffset,final int lengthIn){
		return (double)getDouble(bytesDecodeR(data, startOffset, lengthIn),lengthIn);
	}
	
	private static final long[]powValue=new long[63];
	
	static{for (int i = 0; i < powValue.length; i++) {powValue[i]=1l<<i;}}
	
	public static long bytesToDouble(byte[] data,int startOffset,int length){
		long result=bytesDecode(data, startOffset, length);
		if((result>>(length-1))==1)return result-powValue[length];
		return result;
	}
	
	public static long get(byte value,int length,int offset){return (value&low[length+offset])>>offset;}
	
	public static int getLow(byte value,int length){return  (low[length]&value);}
	
	public static int getHigh(byte value,int length){return (value&0xFF)>>(8-length);}
	
	public static final long getDouble(long value,int length){return (value>>(length-1))==0?value:value-powValue[length];}
	
	public static final long getDouble(double value,int length){return getDouble((long)value, length);}

    public static int exor(long data, int length) {
        int result = 0;
        for (int i = 0; i < length; i++, data = data >> 1)
            if ((data & 1) == 1)
                result++;
        return result;
    }
}
