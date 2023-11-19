package com.girlkun.sukien;

import com.girlkun.consts.ConstDataEvent;
import com.girlkun.models.player.Player;
import com.girlkun.services.Service;

import java.util.HashMap;

public class NauBanhServices extends Thread {

    public static HashMap<Player, BanhChungBanhTet> banhChungBanhTets = new HashMap<>();
    public static long timeCBNau = 0;

    @Override
    public void run() {
        while (true) {
            try {
                if (ConstDataEvent.thoiGianNauBanh == -1) {
                    //timeCBNau = System.currentTimeMillis() + 900000;
                    timeCBNau = System.currentTimeMillis() + 60000;
                    // Nghỉ tạm 15 phút
                    Sleep(60000);
                    //ConstDataEvent.thoiGianNauBanh = 2700000;
                    ConstDataEvent.thoiGianNauBanh = 60000;
                }
                if (ConstDataEvent.thoiGianNauBanh == 0) {
                    if (ConstDataEvent.mucNuocTrongNoi < ConstDataEvent.slBanhTrongNoi) {
                        for (java.util.Map.Entry<Player, BanhChungBanhTet> entry : banhChungBanhTets.entrySet()) {

                            try {
                                Service.gI().sendThongBaoOK(entry.getKey(), "Mước nước không đủ nên bánh huỷ nha bà con");
                            } catch (Exception e) {

                            }
                            // do what you have to do here
                            // In your case, another loop.
                        }
                    } else {
                        // Nghỉ tạm 5 phút cho nó nhận bánh
                        Sleep(300000);
                    }
                    ConstDataEvent.slBanhTrongNoi = 0;
                    ConstDataEvent.mucNuocTrongNoi = 0;
                    ConstDataEvent.thoiGianNauBanh = -1;
                    banhChungBanhTets.clear();;
                }
                Sleep(1000);
                if (ConstDataEvent.thoiGianNauBanh > 0) {
                    ConstDataEvent.thoiGianNauBanh -= 1000;
                }
            } catch (Exception e) {

            }
        }
    }

    public void Sleep(long j) {
        try {
            Thread.sleep(j);
        } catch (Exception e) {

        }
    }
}
