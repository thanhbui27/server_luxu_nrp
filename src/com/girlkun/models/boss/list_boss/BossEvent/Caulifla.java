/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.girlkun.models.boss.list_boss.BossEvent;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossData;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

/**
 *
 * @author Administrator
 */
public class Caulifla extends Boss {
    
    public Caulifla() throws Exception {
        super(BossID.CAULIFLA, BossesData.CAULIFLA);
    }
     @Override
    public void reward(Player plKill) {
        if (Util.isTrue(30, 100)) {
            Service.gI().dropItemMap(this.zone, Util.caitrang2011(zone, 680, 1, this.location.x, this.location.y, plKill.id, true));        
        } 
         if (Util.isTrue(30, 100)) {
            ItemMap it =  new ItemMap(this.zone, Util.nextInt(1066, 1070), Util.nextInt(5, 15), this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        }
          plKill.pointEvent +=10;
          Service.gI().sendThongBao(plKill, " Bạn nhận được 10 điểm");
    }
    @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
       if (Util.canDoWithTime(st, 3600000)) {
           this.changeStatus(BossStatus.LEAVE_MAP);
       }
    }

    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }
    private long st;
    
}
