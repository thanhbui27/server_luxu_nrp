/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.girlkun.models.boss.list_boss.NgucTu;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.PetService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import java.util.Random;

/**
 *
 *@Stole By MITCHIKEN ZALO 0358689793
 */
public class SongokuTaAc extends Boss {

    public SongokuTaAc() throws Exception {
        super(BossID.SONGOKU_TA_AC, BossesData.SONGOKU_TA_AC);
    }

    @Override
    public void reward(Player plKill) {
        int[] NRs = new int[]{17,16};
        int randomNR = new Random().nextInt(NRs.length);
        if (Util.isTrue(10, 100)) {
                 Service.gI().dropItemMap(this.zone, new ItemMap(zone, 2107, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
                return;
        }
        if (Util.isTrue(15, 100)) {
           
            Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 561, 1, this.location.x, this.location.y, plKill.id));
        } else if (Util.isTrue(50, 100)) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, NRs[randomNR], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        }
    }
@Override
public void leaveMap(){
    super.leaveMap();
    super.dispose();
    BossManager.gI().removeBoss(this);
}

}
