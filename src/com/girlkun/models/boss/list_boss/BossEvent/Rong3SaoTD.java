/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.girlkun.models.boss.list_boss.BossEvent;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

/**
 *
 * @author bthan
 */
public class Rong3SaoTD extends Boss {

    public Rong3SaoTD() throws Exception {
        super(BossID.Rong_3Sao_TD, BossesData.Rong_3Sao_TD);
    }

    @Override
    public void reward(Player plKill) {
       
        if(Util.isTrue(950, 1000)){
            ItemMap it =  new ItemMap(this.zone, Util.nextInt(16, 20), 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id);
            Service.gI().sendThongBao(plKill, " Bạn nhận được nro " + it.itemTemplate.name);
            Service.gI().dropItemMap(this.zone, it);
        }else {
            if(Util.isTrue(950, 1000)){
                ItemMap it =  new ItemMap(this.zone, 927, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id);
                Service.gI().sendThongBao(plKill, " Bạn nhận được nro băng 3s");
                Service.gI().dropItemMap(this.zone, it);
            }else{
                ItemMap it =  new ItemMap(this.zone, 2090, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id);
                Service.gI().sendThongBao(plKill, " Bạn nhận được nro vip 3s");
                Service.gI().dropItemMap(this.zone, it);
            }
        }
       
         
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


