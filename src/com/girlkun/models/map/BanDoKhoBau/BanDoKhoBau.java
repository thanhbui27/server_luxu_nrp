package com.girlkun.models.map.BanDoKhoBau;

import com.girlkun.jdbc.daos.GodGK;
import com.girlkun.jdbc.daos.PlayerDAO;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.BossStatus;
import com.girlkun.models.boss.list_boss.PhoBan.TrungUyXanhLoBdkb;
import com.girlkun.models.clan.Clan;
import com.girlkun.models.map.Zone;
import com.girlkun.models.mob.Mob;
import com.girlkun.models.player.Player;
import java.util.Iterator;
import com.girlkun.services.ItemTimeService;
import com.girlkun.services.func.ChangeMapService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Khánh Đẹp Zoai
 */
@Data
public class BanDoKhoBau {

    public static final int N_PLAYER_CLAN = 0;
    public static final int AVAILABLE = 200; // số lượng bdkb trong server
    public static final int TIME_BAN_DO_KHO_BAU = 1_800_000;

    public static final int MAX_HP_MOB = 16_700_000;
    public static final int MIN_HP_MOB = 16_700_000;

    public static final int MAX_DAME_MOB = 2_000_000;
    public static final int MIN_DAME_MOB = 2_000_000;

    private int id;
    private List<Zone> zones;
    private Clan clan;

    private long lastTimeOpen;

    public int level;

    List<Integer> listMap = Arrays.asList(135, 138, 136, 137);
    private int currentIndexMap = -1;

    TrungUyXanhLoBdkb boss;

    public boolean timePickReward;

    public BanDoKhoBau(int id) {
        this.id = id;
        this.zones = new ArrayList<>();
    }

    public Zone getMapById(int mapId) {
        for (Zone zone : this.zones) {
            if (zone.map.mapId == mapId) {
                return zone;
            }
        }
        return null;
    }

    public void openBDKB(Player player, int level) {
        this.lastTimeOpen = System.currentTimeMillis();
        this.clan = player.clan;
        this.level = level;
        player.clan.banDoKhoBau = this;
        player.clan.banDoKhoBau_playerOpen = player.name;
        player.clan.banDoKhoBau_lastTimeOpen = this.lastTimeOpen;
        player.bdkb_isJoinBdkb = true;
        player.bdkb_countPerDay++;
        player.bdkb_lastTimeJoin = System.currentTimeMillis();
        ChangeMapService.gI().goToDBKB(player);
        for (Player pl : player.clan.membersInGame) {
            if (pl == null || pl.zone == null) {
                continue;
            }
            ItemTimeService.gI().sendTextBanDoKhoBau(pl);
        }

    }

    public void init() {
        Iterator var1 = this.zones.iterator();

        while(true) {
            Zone zone;
            do {
                if (!var1.hasNext()) {
                    return;
                }

                zone = (Zone)var1.next();
            } while(zone.map.mapId != (Integer)this.listMap.get(this.currentIndexMap));

            long newHpMob = (long)((this.level - 1) / 1 * 330000 + 330000);
            long newDameMob = (long)((this.level - 1) / 1 * 17000 + 3000);
            Iterator var7 = zone.mobs.iterator();

            while(var7.hasNext()) {
                Mob mob = (Mob)var7.next();
                mob.point.dame = (int)newDameMob;
                mob.point.maxHp = (int)newHpMob;
                mob.hoiSinh();
            }

            int idBoss = zone.map.mapId == 137 ? -6 : -1;
            if (idBoss != -1) {
                this.boss = (TrungUyXanhLoBdkb)BossManager.gI().createBossBdkb(idBoss, (int)(newDameMob * 4L > 2000000000L ? 2000000000L : newDameMob * 4L), (int)(newHpMob * 5L > 2000000000L ? 2000000000L : newHpMob * 5L), zone);
            }
        }
    }

    public void dispose() {
        boss.changeStatus(BossStatus.LEAVE_MAP);
        this.clan = null;
        this.boss = null;
        timePickReward = false;
        currentIndexMap = -1;
    }


}
