package com.girlkun.models.boss.list_boss.BrolySuper;

import com.girlkun.consts.ConstPlayer;
import com.girlkun.models.boss.*;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.map.Zone;

import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.PetService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import java.util.Random;

public class BrolySuper extends Boss {

    private long lastUpdate = System.currentTimeMillis();
    private long timeJoinMap;
    protected Player playerAtt;
    private int timeLive = 200000000;
    public int petgender = 0;
    public Player mypett;

    @Override
    public void die(Player player) {
        super.die(player);
        int random = new Random().nextInt(5,10);
       if(Util.isTrue(60, 100)){
            Service.gI().dropItemMap(this.zone, Util.manhTS(zone, 2087, random, this.location.x, this.location.y, player.id));
        }
        if (Util.isTrue(30, 100)) {
            ItemMap it =  new ItemMap(this.zone, Util.nextInt(1066, 1070), Util.nextInt(5, 15), this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), player.id);
            Service.gI().dropItemMap(this.zone, it);
        }
//        PetService.gI().createKaminOren(player, player.pet != null, player.gender);
//        Service.gI().sendThongBao(player, "Bạn đã nhận được đệ KamiOren"); // Nhận Pet mới
//        Service.getInstance().chatJustForMe(player, null, "|1|Bạn đã nhận được Pet BrolySuper!"); // Thông báo
//        
//        // Nếu pet của boss tồn tại thì tạo một pet mới với loại tương tự và cho người chơi sở hữu
    }

    public BrolySuper() throws Exception {
        super(Util.randomBossId(), BossesData.BOSS_BROLY_SUPER);
    }

    public BrolySuper(Zone zone, int dame, int hp, int id) throws Exception {
        super(id, new BossData(
                "Super Broly", //name
                ConstPlayer.TRAI_DAT, //gender
                new short[]{294, 295, 296, 28, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
                ((50000 + dame)), //dame
                new int[]{((5000000 + hp))}, //hp
                new int[]{49}, //map join
                new int[][]{
                    {Skill.KAMEJOKO, 7, 30000},
                    {Skill.MASENKO, 7, 10000},
                    {Skill.ANTOMIC, 7, 15000},
                    {Skill.TAI_TAO_NANG_LUONG, 1, 20000},},
                new String[]{
                    "|-1|Gaaaaaa",
                    "|-2|Tới đây đi!"
                }, //text chat 1
                new String[]{"|-1|Các ngươi tới số rồi mới gặp phải ta",
                    "|-1|Gaaaaaa",
                    "|-2|Không ngờ..Hắn mạnh cỡ này sao..!!"
                }, //text chat 2
                new String[]{"|-1|Gaaaaaaaa!!!"}, //text chat 3
                60
                ,TypeAppear.ANOTHER_LEVEL
        ));
        this.zone = zone;
    }
    @Override
    public void reward(Player plKill) {
        if (plKill.pet != null) {
            return;
        }
        if (plKill.pet == null) {
            petgender = this.pet.gender;
            
            PetService.gI().createNormalPet(plKill, petgender);
        }
        this.pet.dispose();
        this.pet = null;

    }

    @Override
    public void active() {
        super.active();
        if (this.pet == null) {
            PetService.gI().createNormalPet(this,Util.nextInt(0,2));

        }
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = (int) this.nPoint.subDameInjureWithDeff(damage / 2);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
              if(damage > this.nPoint.hpMax*0.3)  
                  damage = (int) (this.nPoint.hpMax*0.3);
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.pet.dispose();
                this.pet = null;
                this.setDie(plAtt);
                die(plAtt);
               
            }
            return damage;
        } else {
            return 0;
        }

    }
}
