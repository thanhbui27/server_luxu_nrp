package com.girlkun.models.boss.list_boss.Broly;

import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.BossesData;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.player.Player;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import java.util.Random;


public class Broly extends Boss {

    public Broly() throws Exception {
        super(BossID.BROLY , BossesData.BROLY_1,BossesData.BROLY_2, BossesData.BROLY_3);
    }
     @Override
    public void reward(Player plKill) {
//      int[] itemDos = new int[]{2029};
//        int[] NRs = new int[]{2029};
//        int randomDo = new Random().nextInt(itemDos.length);
//        int randomNR = new Random().nextInt(NRs.length);
//        if (Util.isTrue(90, 100)) {
//            if (Util.isTrue(40, 50)) {
//                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 17, 1, this.location.x, this.location.y, plKill.id));
//                return;
//            }
//            Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, itemDos[randomDo], 1, this.location.x, this.location.y, plKill.id));
//        } else {
//            Service.gI().dropItemMap(this.zone, new ItemMap(zone, NRs[randomNR], 1, this.location.x, this.location.y, plKill.id));
//        }
        int random = new Random().nextInt(5,10);
       if(Util.isTrue(60, 100)){
            Service.gI().dropItemMap(this.zone, Util.manhTS(zone, 2087, random, this.location.x, this.location.y, plKill.id));
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
        if(Util.canDoWithTime(st,900000)){
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st= System.currentTimeMillis();
    }
    private long st;
    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = (int) this.nPoint.subDameInjureWithDeff(damage/2);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage/2;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }
}
