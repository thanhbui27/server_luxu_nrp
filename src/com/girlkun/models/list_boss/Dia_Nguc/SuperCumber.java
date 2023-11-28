/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.girlkun.models.list_boss.Dia_Nguc;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;

/**
 *
 * @author bthan
 */
public class SuperCumber extends Boss {
    
    public SuperCumber() throws Exception {
        super(BossID.SUPERCUMBER, BossesData.SUPERCUMBER);
    }
     @Override
    public void reward(Player plKill) {
        if(plKill.isPet){
            ItemMap it =  new ItemMap(this.zone, Util.nextInt(1066, 1070), Util.nextInt(5, 15), this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
             Service.gI().dropItemMap(this.zone, it);
            return;
        }
        if (Util.isTrue(40, 100) || plKill.isAdmin()) {
            ItemMap it =  new ItemMap(this.zone, 2114, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.gI().dropItemMap(this.zone, it);        
        } 
         if (Util.isTrue(30, 100)) {
            ItemMap it =  new ItemMap(this.zone, Util.nextInt(1066, 1070), Util.nextInt(5, 15), this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.gI().dropItemMap(this.zone, it);
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
