package com.ztrs.zgj.device.utils;

public class Crc16 {

    /**
     * 计算CRC16校验码
     *
     * @param bytes
     * @return
     */
    public static int getCRC(byte[] bytes,int len) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;
        if (len == 0 ) {
            len = bytes.length;
        }

        int i, j;
        for (i = 0; i < len; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        return CRC;
        //return Integer.toHexString(CRC);
    }

}
