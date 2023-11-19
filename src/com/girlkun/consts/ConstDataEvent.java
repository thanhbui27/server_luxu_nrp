package com.girlkun.consts;

import com.girlkun.utils.Util;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

// su kien 1/6
public class ConstDataEvent {

    public static final int idVoOc = 695;
    public static final int idSaoBien = 698;
    public static final int idConCua = 697;
    public static final int idMily = 666;
  public static final int idquockhanh = 891;
    public static final int idquockhanh2 = 887;
    public static final int idquockhanhsk = 1198;
    public static final int idquockhanh2sk = 1199;
    public static final int idVoOc_SK = 2160;
    public static final int idSaoBien_SK = 2161;
    public static final int idConCua_Sk = 2162;

    public static final int mayDoSuKien = 2159;
    public static final int ruong1 = 2163;

//    public static final int ruong2 = 2139;
    public static final int ruongvip1 = 737;
    public static final int ruongvip2 = 2117;
    public static final int ruongvip3 = 2142;
    public static final int trumcuoi = 2136;
    public static final int ruongloai1 = 2144;
    public static final int ruongloai2 = 2145;
    public static final int ruongloai3 = 2146;
    public static final int ruongloai4 = 2147;
    public static final int ruongloai5 = 2158;
    public static final int ruong2 = 2164;

    public static final int top1sm = 2148;
    public static final int top2sm = 2149;
    public static final int top3sm = 2150;
    public static final int top4sm = 2151;
    public static final int top510sm = 2152;
    public static final int top1s = 2153;
    public static final int top2s = 2154;
    public static final int top3s = 2155;
    public static final int top4s = 2156;
    public static final int top510s = 2157;
    public static final int slVoOcSK = 99;

    public static final int slSaoBienSK = 99;

    public static final int slConCuaSK = 99;

    public static List<Integer> listVPSK = Arrays.asList(idConCua, idVoOc, idSaoBien);
    public static int slBanhTrongNoi;

    public static int mucNuocTrongNoi;
    public static int thoiGianNauBanh = -1;

    public static int getRandomFromList() {
        int tile_saoBien = 100;
        int tile_ConCua = 100;
        int tile_VoOc = 100;
        int tile = Util.nextInt(100, 100);
        if (tile < tile_ConCua) {
            return listVPSK.get(0);
        } else if (tile < tile_VoOc) {
            return listVPSK.get(1);
        } else if (tile < tile_saoBien) {
            return listVPSK.get(2);
        } else {
            return listVPSK.get(Util.nextInt(listVPSK.size()));
        }
    }

    public static ConstDataEvent gI;

    public static ConstDataEvent gI() {
        if (gI == null) {
            gI = new ConstDataEvent();
        }
        return gI;
    }

    public static final boolean isRunningSK16 = true;


    public static long getCountdownSecondsCBSK() {
        Calendar calendar = Calendar.getInstance();
        int currentMinute = calendar.get(Calendar.MINUTE);

        // Đặt phút của thời gian hiện tại thành 15
        calendar.set(Calendar.MINUTE, 15);

        long countdownMinutes = currentMinute - 15;
        long countdownSeconds = countdownMinutes * 60;

        return countdownSeconds;
    }

    public static long getCountdownSeconds() {
        Calendar calendar = Calendar.getInstance();
        int currentMinute = calendar.get(Calendar.MINUTE);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        int targetHour;
        if (currentMinute >= 15) {
            targetHour = currentHour + 1;
        } else {
            targetHour = currentHour;
        }

        int targetMinute = 0;

        // Đặt giờ và phút của thời gian đếm ngược
        calendar.set(Calendar.HOUR_OF_DAY, targetHour);
        calendar.set(Calendar.MINUTE, targetMinute);
        calendar.set(Calendar.SECOND, 0);

        long currentTimeMillis = System.currentTimeMillis();
        long targetTimeMillis = calendar.getTimeInMillis();

        long countdownSeconds = (targetTimeMillis - currentTimeMillis) / 1000;

        return countdownSeconds;
    }

}
