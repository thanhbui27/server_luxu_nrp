package com.girlkun.models.npc;

//import com.girlkun.MaQuaTang.MaQuaTangManager;
import com.girlkun.consts.ConstDataEvent;
import com.girlkun.kygui.ItemKyGui;
import com.girlkun.kygui.ShopKyGuiService;
import com.girlkun.consts.ConstMap;
import com.girlkun.consts.ConstNpc;
import com.girlkun.consts.ConstPlayer;
import com.girlkun.consts.ConstTask;
import com.girlkun.consts.cn;
import com.girlkun.models.boss.Boss;
import com.girlkun.models.boss.BossData;
import com.girlkun.models.boss.BossID;
import com.girlkun.models.boss.BossManager;
import com.girlkun.models.boss.list_boss.NhanBan;
import com.girlkun.models.clan.Clan;
import com.girlkun.models.clan.ClanMember;
import com.girlkun.models.item.Item;
import com.girlkun.models.map.Map;
import com.girlkun.models.map.MapMaBu.MapMaBu;
import com.girlkun.models.map.Zone;
import com.girlkun.models.map.BanDoKhoBau.BanDoKhoBau;
import com.girlkun.models.map.BanDoKhoBau.BanDoKhoBauService;
import com.girlkun.models.map.ConDuongRanDoc.ConDuongRanDoc;
import com.girlkun.models.map.ConDuongRanDoc.ConDuongRanDocService;
import com.girlkun.models.map.NgocRongSaoDen.BlackBallWar;
import com.girlkun.models.map.DaiHoiVoThuat23.MartialCongressService;
import com.girlkun.models.map.DoanhTraiDocNhan.DoanhTrai;
import com.girlkun.models.map.DoanhTraiDocNhan.DoanhTraiService;
import com.girlkun.models.map.KhiGasHuyDiet.*;
import com.girlkun.models.map.NguHanhSon.nguhs;
import com.girlkun.database.GirlkunDB;
import com.girlkun.jdbc.daos.PlayerDAO;
import com.girlkun.models.map.DaiHoiVoThuat.DaiHoiManager;
import com.girlkun.models.matches.PVPService;
import com.girlkun.models.matches.TOP;
import com.girlkun.models.matches.pvp.DaiHoiVoThuat;
import com.girlkun.models.matches.pvp.DaiHoiVoThuatService;
import com.girlkun.models.map.Mapchichi.Map22h;
import com.girlkun.models.mob.Mob;
import com.girlkun.models.player.Inventory;
import com.girlkun.models.player.NPoint;
import com.girlkun.models.player.Player;
import com.girlkun.models.shop.ShopServiceNew;
import com.girlkun.models.skill.Skill;
import com.girlkun.server.Client;
import com.girlkun.server.Maintenance;
import com.girlkun.server.Manager;
import com.girlkun.services.*;
import com.girlkun.services.func.*;
import com.girlkun.utils.Logger;
import com.girlkun.utils.TimeUtil;
import com.girlkun.utils.Util;
import com.girlkun.models.boss.list_boss.DuongTank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.TimerTask;
import java.util.Timer;

import static com.girlkun.services.func.SummonDragon.*;
import java.sql.Connection;
import static com.girlkun.services.func.CombineServiceNew.NANG_CAP_SKH_VIP;
import com.girlkun.sukien.NauBanhServices;

public class NpcFactory {

    private static final int COST_HD = 50000000;

    private static boolean nhanVang = false;
    private static boolean nhanDeTu = false;

    //playerid - object
    public static final java.util.Map<Long, Object> PLAYERID_OBJECT = new HashMap<Long, Object>();

    private NpcFactory() {

    }

    private static Npc TrongTai(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 113) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đại hội võ thuật Siêu Hạng\n(thử nghiệm)\ndiễn ra 24/7 kể cả ngày lễ và chủ nhật\nHãy thi đấu để khẳng định đẳng cấp của mình nhé", "Top 100\nCao thủ\n(thử nghiệm)", "Hướng\ndẫn\nthêm", "Đấu ngay\n(thử nghiệm)", "Về\nĐại Hội\nVõ Thuật");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 113) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    try ( Connection con = GirlkunDB.getConnection()) {
                                    Manager.topSieuHang = Manager.realTopSieuHang(con);
                                } catch (Exception ignored) {
                                    Logger.error("Lỗi đọc top");
                                }
                                Service.gI().showListTop(player, Manager.topSieuHang, (byte) 1);
                                break;
                                case 2:
                                    List<TOP> tops = new ArrayList<>();
                                    tops.addAll(Manager.realTopSieuHang(player));
                                    Service.gI().showListTop(player, tops, (byte) 1);
                                    tops.clear();
                                    break;
                                case 3:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, -1, 432);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }
    
    public static Npc mavuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 202) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Xin chào, tôi có thể giúp gì cho cậu?", "Tây thánh địa",
                                "Từ chối");
                    } else if (this.mapId == 156) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Người muốn trở về?",
                                "Quay về", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 202) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                // đến tay thanh dia
                                ChangeMapService.gI().changeMapBySpaceShip(player, 156, -1, 360);
                            }
                        }
                    } else if (this.mapId == 156) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                // về lanh dia bang hoi
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 202, -1,
                                            432);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }


    private static Npc trungLinhThu(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 104) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đổi Trứng Linh thú cần:\b|7|X99 Hồn Linh Thú + 1 Tỷ vàng", "Đổi Trứng\nLinh thú", "Nâng Chiến Linh", "Mở chỉ số ẩn\nChiến Linh", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 104) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: {
                                    Item honLinhThu = null;
                                    try {
                                        honLinhThu = InventoryServiceNew.gI().findItemBag(player, 2029);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (honLinhThu == null || honLinhThu.quantity < 99) {
                                        this.npcChat(player, "Bạn không đủ 99 Hồn Linh thú");
                                    } else if (player.inventory.gold < 1_000_000_000) {
                                        this.npcChat(player, "Bạn không đủ 1 Tỷ vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 1_000_000_000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, honLinhThu, 99);
                                        Service.gI().sendMoney(player);
                                        Item trungLinhThu = ItemService.gI().createNewItem((short) 2028);
                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được 1 Trứng Linh thú");
                                    }
                                    break;
                                }

                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.Nang_Chien_Linh);
                                    break;
                                case 2:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_Chien_Linh);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.Nang_Chien_Linh:
                                case CombineServiceNew.MO_CHI_SO_Chien_Linh:
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    private static Npc kyGui(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                   
                    //createOtherMenu(player, 0, "Cửa hàng chúng tôi chuyên mua bán hàng hiệu, hàng độc, cảm ơn bạn đã ghé thăm.", "Hướng\ndẫn\nthêm", "Mua bán\nKý gửi", "Từ chối");
                    
                  

                }
            }

            @Override
            public void confirmMenu(Player pl, int select) {
                if (canOpenNpc(pl)) {
                    switch (select) {
                        case 0:
                            Service.gI().sendPopUpMultiLine(pl, tempId, avartar, "Cửa hàng chuyên nhận ký gửi mua bán vật phẩm\bChỉ với 5 hồng ngọc\bGiá trị ký gửi 10k-200Tr vàng hoặc 2-2k ngọc\bMột người bán, vạn người mua, mại dô, mại dô");
                            break;
                        case 1:
                            if (pl.session.version < 222) {
                                Service.gI().sendPopUpMultiLine(pl, tempId, avartar, "Bạn phải sử dụng phiên bản từ v2.2.2 trở lên mới có thể sử dụng tính năng này");
                                break;
                            }
                            ShopKyGuiService.gI().openShopKyGui(pl);
                            break;

                    }
                }
            }
        };
    }

    private static Npc poTaGe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 140) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đa vũ trụ song song \b|7|Con muốn gọi con trong đa vũ trụ \b|1|Với giá 200tr vàng không?", "Gọi Boss\nNhân bản", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 140) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: {
                                    Boss oldBossClone = BossManager.gI().getBossById(Util.createIdBossClone((int) player.id));
                                    if (oldBossClone != null) {
                                        this.npcChat(player, "Nhà ngươi hãy tiêu diệt Boss lúc trước gọi ra đã, con boss đó đang ở khu " + oldBossClone.zone.zoneId);
                                    } else if (player.inventory.gold < 200_000_000) {
                                        this.npcChat(player, "Nhà ngươi không đủ 200 Triệu vàng ");
                                    } else {
                                        List<Skill> skillList = new ArrayList<>();
                                        for (byte i = 0; i < player.playerSkill.skills.size(); i++) {
                                            Skill skill = player.playerSkill.skills.get(i);
                                            if (skill.point > 0) {
                                                skillList.add(skill);
                                            }
                                        }
                                        int[][] skillTemp = new int[skillList.size()][3];
                                        for (byte i = 0; i < skillList.size(); i++) {
                                            Skill skill = skillList.get(i);
                                            if (skill.point > 0) {
                                                skillTemp[i][0] = skill.template.id;
                                                skillTemp[i][1] = skill.point;
                                                skillTemp[i][2] = skill.coolDown;
                                            }
                                        }
                                        BossData bossDataClone = new BossData(
                                                "Nhân Bản" + player.name,
                                                player.gender,
                                                new short[]{player.getHead(), player.getBody(), player.getLeg(), player.getFlagBag(), player.idAura, player.getEffFront()},
                                                player.nPoint.dame,
                                                new int[]{player.nPoint.hpMax},
                                                new int[]{140},
                                                skillTemp,
                                                new String[]{"|-2|Boss nhân bản đã xuất hiện rồi"}, //text chat 1
                                                new String[]{"|-1|Ta sẽ chiếm lấy thân xác của ngươi hahaha!"}, //text chat 2
                                                new String[]{"|-1|Lần khác ta sẽ xử đẹp ngươi"}, //text chat 3
                                                60
                                        );

                                        try {
                                            new NhanBan(Util.createIdBossClone((int) player.id), bossDataClone, player.zone);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        //trừ vàng khi gọi boss
                                        player.inventory.gold -= 200_000_000;
                                        Service.gI().sendMoney(player);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc Tapion(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    Map22h.gI().setTimeJoinMap22h();
                }
                if (this.mapId == 19) {
                    long now = System.currentTimeMillis();
                    if (now > Map22h.TIME_OPEN_22h && now < Map22h.TIME_CLOSE_22h) {
                        this.createOtherMenu(player, ConstNpc.MENU_OPEN_MMB, "phong ấn đã bị phá vỡ, "
                                + "Xin hãy cứu lấy người dân",
                                "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                    } else {
                        this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_MMB,
                                "Ác quỷ truyền thuyết Hirudegarn đã thoát khỏi phong ấn ngàn năm nHãy giúp tôi chế ngự nó?",
                                "Hướng dẫn", "Từ chối");
                    }
                } else {

                    if (this.mapId == 212) {
                        this.createOtherMenu(player, 0,
                                "Quay Trở Về", "Thành Phố Vegeta", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (this.mapId) {
                        case 19:
                            switch (player.iDMark.getIndexMenu()) {
                                case ConstNpc.MENU_REWARD_MMB:
                                case ConstNpc.MENU_OPEN_MMB:
                                    if (select == 0) {
                                        NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_chi22h);
                                    }
                                    if (!player.getSession().actived) {
                                    }
                                    if (select == 1) {
                                        ChangeMapService.gI().changeMap(player, 212, 0, 66, 312);
                                    }
                                    break;
                                case ConstNpc.MENU_NOT_OPEN_BDW:
                                    if (select == 0) {
                                        NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_2h);
                                    }
                                    break;
                            }
                    }
                }
                if (this.mapId == 212) {
                    if (player.iDMark.getIndexMenu() == 0) { // 
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 19, -1, 600);
                                break;
                        }
                    }
                }
            }
        };
    }

    private static Npc quyLaoKame(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "|1| Sự Kiện Kho Báu Dưới Biển\n Đi Kho Báu Dưới Biển Tỉ Lệ Rơi Vật Phẩm Để Đổi", "Giải tán bang hội","Lãnh địa Bang Hội", "Kho báu dưới biển", "Tang hoa hong");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.BASE_MENU:
                            switch (select) {
                                case 0:
                                    Clan clan = player.clan;
                                    if (clan != null) {
                                        ClanMember cm = clan.getClanMember((int) player.id);
                                        if (cm != null) {
                                            if (clan.members.size() > 1) {
                                                Service.gI().sendThongBao(player, "Bang phải còn một người");
                                                break;
                                            }
                                            if (!clan.isLeader(player)) {
                                                Service.gI().sendThongBao(player, "Phải là bảng chủ");
                                                break;
                                            }
//                                        
                                            NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DISSOLUTION_CLAN, -1, "Con có chắc chắn muốn giải tán bang hội không? Ta cho con 2 lựa chọn...",
                                                    "Yes you do!", "Từ chối!");
                                        }
                                        break;
                                    }
                                    Service.gI().sendThongBao(player, "Có bang hội đâu ba!!!");
                                    break;
                                case 1 : 
                                    if (player.getSession().player.nPoint.power >= 80000000000L || player.isAdmin()) {

                                    ChangeMapService.gI().changeMapBySpaceShip(player, 202, -1,
                                            432);
                                    } else {
                                            this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                     }
                                break;
                                case 2:
                                    if (player.clan == null) {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Vào bang hội trước", "Đóng");
                                        break;
                                    }
//                                    if (player.clan.getMembers().size() < BanDoKhoBau.N_PLAYER_CLAN) {
//                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                                "Bang hội phải có ít nhất 5 thành viên mới có thể mở", "Đóng");
//                                        break;
//                                    }
//                                    if (player.clanMember.getNumDateFromJoinTimeToToday() < 1) {
//                                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                                "Bản đồ kho báu chỉ cho phép những người ở trong bang trên 1 ngày. Hẹn ngươi quay lại vào lúc khác",
//                                                "OK");
//                                        break;
//                                    }

                                    if (player.bdkb_countPerDay >= 3) {
                                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Con đã đạt giới hạn lượt đi trong ngày",
                                                "OK");
                                        break;
                                    }

                                    player.clan.banDoKhoBau_haveGone = !(System.currentTimeMillis() - player.clan.banDoKhoBau_lastTimeOpen > 300000);
                                    if (player.clan.banDoKhoBau_haveGone) {
                                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Bang hội của con đã đi Bản Đồ lúc " + TimeUtil.formatTime(player.clan.banDoKhoBau_lastTimeOpen, "HH:mm:ss") + " hôm nay. Người mở\n"
                                                + "(" + player.clan.banDoKhoBau_playerOpen + "). Hẹn con sau 5 phút nữa", "OK");
                                        break;
                                    }
                                    if (player.clan.banDoKhoBau != null) {
                                        createOtherMenu(player, ConstNpc.MENU_OPENED_DBKB,
                                                "Bang hội của con đang tham gia Bản đồ kho báu cấp độ " + player.clan.banDoKhoBau.level + "\n"
                                                + "Thời gian còn lại là "
                                                + TimeUtil.getSecondLeft(player.clan.banDoKhoBau.getLastTimeOpen(), BanDoKhoBau.TIME_BAN_DO_KHO_BAU / 1000)
                                                + " giây. Con có muốn tham gia không?",
                                                "Tham gia", "Không");
                                        break;
                                    }
                                    Input.gI().createFormChooseLevelBDKB(player);
                                    break;
                                case 3 : 
                                      createOtherMenu(player, ConstNpc.TANG_HOA,
                                                "ồ con muốn tặng hoa cho ta à \n"
                                               + "Con đang có " + player.pointEvent + " Điểm"
                                                , "Đổi x99 bông\n lấy bó hoa hồng","Đôi 10 bó hoa đỏ","Đổi x199 bông + 1 bó hoa đỏ \n lấy bó hoa vàng","Đổi 10 bó hoa vàng", "xem top sự kiện", "Hướng dẫn");
                                        break;
                            }
                            break;
                            
                        case ConstNpc.TANG_HOA : 
                            if(select == 0){
                               Item it = InventoryServiceNew.gI().findItemBag(player, 589);
                               if(it != null){
                                   if(it.quantity < 99){
                                        Service.gI().sendThongBao(player, "không đủ x99 bông hoa");
                                        return;
                                   }
                                        Item bohoahong = ItemService.gI().createNewItem((short) 2096);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, it, 99);
                                        InventoryServiceNew.gI().addItemBag(player, bohoahong);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Bạn nhận được bó hoa đỏ");
                                   
                               }else {
                                    Service.gI().sendThongBao(player, "Con không có bông hoa nào");
                               }
                            }
                            if(select == 1){
                               Item it = InventoryServiceNew.gI().findItemBag(player, 589);
                               if(it != null){
                                   if(it.quantity < 999){
                                        Service.gI().sendThongBao(player, "không đủ x999 bông hoa");
                                        return;
                                   }
                                        Item bohoahong = ItemService.gI().createNewItem((short) 2096,10);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, it, 999);
                                        
                                       InventoryServiceNew.gI().addItemBag(player, bohoahong);
                                       InventoryServiceNew.gI().sendItemBags(player);                      
                   
                                        Service.gI().sendThongBao(player, "Bạn nhận được bó hoa đỏ");
                                   
                               }else {
                                    Service.gI().sendThongBao(player, "Con không có bông hoa nào");
                               }
                            }
                            if(select == 2){
                                 Item it1 = InventoryServiceNew.gI().findItemBag(player, 589);                                 
                                 Item it2 = InventoryServiceNew.gI().findItemBag(player, 2096);
                               if(it1 != null && it2 != null){
                                   if(it1.quantity < 199 || it2.quantity < 0){
                                        Service.gI().sendThongBao(player, "không đủ x199 bông hoa");
                                        return;
                                   }
                                        Item bohoahong = ItemService.gI().createNewItem((short) 2097);                                                                              
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, it1, 199);                                        
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, it2, 1);

                                        InventoryServiceNew.gI().addItemBag(player, bohoahong);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Bạn nhận được bó hoa vàng");
                                   
                               }else {
                                    Service.gI().sendThongBao(player, "Con không có bông hoa nào hoặc bó hoa nào");
                               }
                            }
                             if(select == 3){
                                 Item it1 = InventoryServiceNew.gI().findItemBag(player, 589);                                 
                                 Item it2 = InventoryServiceNew.gI().findItemBag(player, 2096);
                               if(it1 != null && it2 != null){
                                   if(it1.quantity < 1999 || it2.quantity < 10){
                                        Service.gI().sendThongBao(player, "không đủ x1999 bông hoa hoặc không đủ 10 bông hoa hồng");
                                        return;
                                   }
                                        Item bohoahong = ItemService.gI().createNewItem((short) 2097,10); 
                                        
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, it1, 1999);                                        
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, it2, 10);
                                       
                                        InventoryServiceNew.gI().addItemBag(player, bohoahong);
                                        InventoryServiceNew.gI().sendItemBags(player);

                                                                           
                                       
                                        Service.gI().sendThongBao(player, "Bạn nhận được bó hoa vàng");
                                   
                               }else {
                                    Service.gI().sendThongBao(player, "Con không có bông hoa nào hoặc bó hoa nào");
                               }
                            }
                            if(select == 4){
                                   Service.gI().showListTop(player, Manager.tangHoa);
                            }
                             if(select == 5){
                                
                            }
                            break;
                            
                        case ConstNpc.MENU_OPENED_DBKB:
                            if (select == 0) {
                                BanDoKhoBauService.gI().joinBDKB(player);
                            }
                            break;
                        case ConstNpc.MENU_ACCEPT_GO_TO_BDKB:
                            if (select == 0) {
                                Object level = PLAYERID_OBJECT.get(player.id);
                                BanDoKhoBauService.gI().openBDKB(player, (int) level);
                            }
                            break;

                    }
                }
            }
        };
    }

    public static Npc truongLaoGuru(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc vuaVegeta(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc ongGohan_ongMoori_ongParagus(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con cố gắng theo %1 học thành tài, đừng lo lắng cho ta."
                                        .replaceAll("%1", player.gender == ConstPlayer.TRAI_DAT ? "Quy lão Kamê"
                                                : player.gender == ConstPlayer.NAMEC ? "Trưởng lão Guru" : "Vua Vegeta"),
                                "Nhận ngọc xanh", "Nhận đệ tử", "Mã quà tặng", "Hỗ trợ\nbỏ qua\nNhiệm vụ", "Danh hiệu", "Mở thành viên", "Điểm danh\nngày");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.inventory.gem == 200000) {
                                    this.npcChat(player, "Đã đạt giới hạn ngọc");
                                    break;
                                }
                                player.inventory.gem = 200000;
                                Service.gI().sendMoney(player);
                                Service.gI().sendThongBao(player, "Bạn vừa nhận được 200K ngọc xanh");
                                break;
                            case 1:
                                if (player.pet == null) {
                                    PetService.gI().createNormalPet(player);
                                    Service.gI().sendThongBao(player, "Bạn vừa nhận được đệ tử");
                                } else {
                                    this.npcChat(player, "Bạn đã có rồi");
                                }
                                break;
                         
                          
                            case 2:
                                Input.gI().createFormGiftCode(player);
                                break;
                            case 3: {
                                if (player.playerTask.taskMain.id == 11) {
                                    if (player.playerTask.taskMain.index == 0) {
                                        TaskService.gI().DoneTask(player, ConstTask.TASK_11_0);
                                    } else if (player.playerTask.taskMain.index == 1) {
                                        TaskService.gI().DoneTask(player, ConstTask.TASK_11_1);
                                    } else if (player.playerTask.taskMain.index == 2) {
                                        TaskService.gI().DoneTask(player, ConstTask.TASK_11_2);
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Ta đã giúp con hoàn thành nhiệm vụ rồi mau đi trả nhiệm vụ");
                                    }
                                } else if (player.playerTask.taskMain.id == 13) {
                                    if (player.playerTask.taskMain.index == 0) {
                                        TaskService.gI().DoneTask(player, ConstTask.TASK_13_0);
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Ta đã giúp con hoàn thành nhiệm vụ rồi mau đi trả nhiệm vụ");
                                    }
                                } else if (player.playerTask.taskMain.id == 14) {
                                    if (player.playerTask.taskMain.index == 0) {
                                        for (int i = player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count; i < 25; i++) {
                                            TaskService.gI().DoneTask(player, ConstTask.TASK_14_0);
                                        }
                                    } else if (player.playerTask.taskMain.index == 1) {
                                        for (int i = player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count; i < 25; i++) {
                                            TaskService.gI().DoneTask(player, ConstTask.TASK_14_1);
                                        }
                                    } else if (player.playerTask.taskMain.index == 2) {
                                        for (int i = player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count; i < 25; i++) {
                                            TaskService.gI().DoneTask(player, ConstTask.TASK_14_2);
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Ta đã giúp con hoàn thành nhiệm vụ rồi mau đi trả nhiệm vụ");
                                    }
                                } else if (player.playerTask.taskMain.id == 15) {
                                    if (player.playerTask.taskMain.index == 0) {
                                        for (int i = player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count; i < 50; i++) {
                                            TaskService.gI().DoneTask(player, ConstTask.TASK_15_0);
                                        }
                                    } else if (player.playerTask.taskMain.index == 1) {
                                        for (int i = player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count; i < 50; i++) {
                                            TaskService.gI().DoneTask(player, ConstTask.TASK_15_1);
                                        }
                                    } else if (player.playerTask.taskMain.index == 2) {
                                        for (int i = player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count; i < 50; i++) {
                                            TaskService.gI().DoneTask(player, ConstTask.TASK_15_2);
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Ta đã giúp con hoàn thành nhiệm vụ rồi mau đi trả nhiệm vụ");
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Nhiệm vụ hiện tại không thuộc diện hỗ trợ");
                                }
                                break;
                            }
                            case 4:
                                this.createOtherMenu(player, 888,
                                        "Đây là danh hiệu mà ngươi có"
                                        + (player.lastTimeTitle1 > 0 ? "\nDanh Hiệu Để TÔN VINH CÁC PROPLAYER" : "") + (player.lastTimeTitle2 > 0 ? "" : "") + (player.lastTimeTitle3 > 0 ? "" : "") + (player.lastTimeTitle4 > 0 ? "" : ""),
                                        (player.lastTimeTitle1 > 0 ? ("Danh hiệu \n Một: " + (player.isTitleUse == true ? "On" : "Off") + "\n" + Util.msToTime(player.lastTimeTitle1)) : "Hết hạn"),
                                        (player.lastTimeTitle2 > 0 ? ("Danh hiệu \n 2: " + (player.isTitleUse2 == true ? "On" : "Off") + "\n" + Util.msToTime(player.lastTimeTitle2)) : "Hết hạn"),
                                        (player.lastTimeTitle3 > 0 ? ("Danh hiệu \n 3: " + (player.isTitleUse3 == true ? "On" : "Off") + "\n" + Util.msToTime(player.lastTimeTitle3)) : "Hết hạn"),
                                        (player.lastTimeTitle4 > 0 ? ("Danh hiệu \n 4: " + (player.isTitleUse4 == true ? "On" : "Off") + "\n" + Util.msToTime(player.lastTimeTitle4)) : "Hết hạn"),
                                        "Từ chối");

                                break;
                              case 5:
                             if (!player.getSession().actived) {
                                if (player.getSession().coinBar >= 200000) {
                                    player.getSession().actived = true;
                                    if (PlayerDAO.subcoinBar(player, 200000)) {
                                        int tv = 670; 
                                        Item thoiVang = ItemService.gI().createNewItem((short) 457, tv);// x3
                                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "bạn nhận được " + tv+ " " + thoiVang.template.name);
                                   
                                        this.npcChat(player, "Đã mở thành viên thành công!");
                                        } else {
                                          this.npcChat(player, "Lỗi vui lòng báo admin...");
                                        }
                                    } else {
                                        this.npcChat(player, "Bạn còn thiếu " + (200000 -
                                    player.getSession().coinBar) + " để mở thành viên");
                                }
                             }else {
                                 this.npcChat(player, "Bạn đã mở thành viên rồi mà!");
                             }
                                   
                            break;   
                             case 6:
                                if (player.diemdanh == 0) {
                                    int thoivang1;
                                    if (player.vnd >= 0 && player.vnd < 500000) {
                                        thoivang1 = 15;
                                    } else if (player.vnd > 500000 && player.vnd < 1000000) {
                                        thoivang1 = 50;
                                    } else {
                                        thoivang1 = 80;
                                    }
                                    Item thoivang = ItemService.gI().createNewItem((short) 457, thoivang1);
                                    InventoryServiceNew.gI().addItemBag(player, thoivang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    player.diemdanh = 1;
                                    Service.getInstance().sendThongBao(player, "|7|Bạn vừa nhận được " + thoivang1 + " Thỏi vàng");
                                } else {
                                    this.npcChat(player, "Hôm nay đã nhận rồi mà !!!");
                                }
                                break;    
                                
                        }
                    } else if (player.iDMark.getIndexMenu() == 888) {
                        switch (select) {
                            case 0:
                                if (player.lastTimeTitle1 > 0) {
                                    Service.gI().removeTitle(player);
                                    player.isTitleUse = !player.isTitleUse;
                                    Service.gI().point(player);
                                    Service.gI().sendThongBao(player, "Đã " + (player.isTitleUse == true ? "Bật" : "Tắt") + " Danh Hiệu!");
                                    break;
                                }
                                break;
                            case 1:
                                if (player.lastTimeTitle2 > 0) {
                                    Service.gI().removeTitle(player);
                                    player.isTitleUse2 = !player.isTitleUse2;
                                    Service.gI().point(player);
                                    Service.gI().sendThongBao(player, "Đã " + (player.isTitleUse2 == true ? "Bật" : "Tắt") + " Danh Hiệu!");
                                    break;
                                }
                                break;
                            case 2:
                                if (player.lastTimeTitle3 > 0) {
                                    Service.gI().removeTitle(player);
                                    player.isTitleUse3 = !player.isTitleUse3;
                                    Service.gI().point(player);
                                    Service.gI().sendThongBao(player, "Đã " + (player.isTitleUse3 == true ? "Bật" : "Tắt") + " Danh Hiệu!");
                                    break;
                                }
                                break;
                            case 3:
                                if (player.lastTimeTitle4 > 0) {
                                    Service.gI().removeTitle(player);
                                    player.isTitleUse4 = !player.isTitleUse4;
                                    Service.gI().point(player);
                                    Service.gI().sendThongBao(player, "Đã " + (player.isTitleUse4 == true ? "Bật" : "Tắt") + " Danh Hiệu!");
                                    break;
                                }
                                break;
                        }
                    }
                }

            }

        };
    }

    public static Npc bulmaQK(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Cậu cần trang bị gì cứ đến chỗ tôi nhé", "Cửa\nhàng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0://Shop
                                if (player.gender == ConstPlayer.TRAI_DAT) {
                                    ShopServiceNew.gI().opendShop(player, "BUNMA", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Xin lỗi cưng, chị chỉ bán đồ cho người Trái Đất", "Đóng");
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc dende(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.idNRNM != -1) {
                            if (player.zone.map.mapId == 7) {
                                this.createOtherMenu(player, 1, "Ồ, ngọc rồng namếc, bạn thật là may mắn\nnếu tìm đủ 7 viên sẽ được Rồng Thiêng Namếc ban cho điều ước", "Hướng\ndẫn\nGọi Rồng", "Gọi rồng", "Từ chối");
                            }
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Anh cần trang bị gì cứ đến chỗ em nhé", "Cửa\nhàng");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0://Shop
                                if (player.gender == ConstPlayer.NAMEC) {
                                    ShopServiceNew.gI().opendShop(player, "DENDE", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Xin lỗi anh, em chỉ bán đồ cho dân tộc Namếc", "Đóng");
                                }
                                break;
                            case 1:
                                if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) {

                                    if (player.session.actived) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 250, -1, 295);
                                        break;
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở thành viên Để sử dụng");
                                    }

                                } else {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 295);
                                }
                        }
                    } else if (player.iDMark.getIndexMenu() == 1) {

                        if (player.clan == null) {
                            Service.gI().sendThongBao(player, "Không có bang hội");
                            return;
                        }
                        if (player.idNRNM != 353) {
                            Service.gI().sendThongBao(player, "Anh phải có viên ngọc rồng Namếc 1 sao");
                            return;
                        }

                        byte numChar = 0;
                        for (Player pl : player.zone.getPlayers()) {
                            if (pl.clan.id == player.clan.id && pl.id != player.id) {
                                if (pl.idNRNM != -1) {
                                    numChar++;
                                }
                            }
                        }
                        if (numChar < 6) {
                            Service.gI().sendThongBao(player, "Anh hãy tập hợp đủ 7 viên ngọc rồng nameck đi");
                            return;
                        }

                        if (player.zone.map.mapId == 7 && player.idNRNM != -1) {
                            if (player.idNRNM == 353) {
                                NgocRongNamecService.gI().tOpenNrNamec = System.currentTimeMillis() + 86400000;
                                NgocRongNamecService.gI().firstNrNamec = true;
                                NgocRongNamecService.gI().timeNrNamec = 0;
                                NgocRongNamecService.gI().doneDragonNamec();
                                NgocRongNamecService.gI().initNgocRongNamec((byte) 1);
                                NgocRongNamecService.gI().reInitNrNamec((long) 86399000);
                                SummonDragon.gI().summonNamec(player);
                            } else {
                                Service.gI().sendThongBao(player, "Anh phải có viên ngọc rồng Namếc 1 sao");
                            }
                        }
                        switch (select) {

                        }
                    }
                }
            }
        };
    }

    public static Npc appule(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi cần trang bị gì cứ đến chỗ ta nhé", "Cửa\nhàng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0://Shop
                                if (player.gender == ConstPlayer.XAYDA) {
                                    ShopServiceNew.gI().opendShop(player, "APPULE", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Về hành tinh hạ đẳng của ngươi mà mua đồ cùi nhé. Tại đây ta chỉ bán đồ cho người Xayda thôi", "Đóng");
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc drDrief(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (this.mapId == 84) {
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                pl.gender == ConstPlayer.TRAI_DAT ? "Đến\nTrái Đất" : pl.gender == ConstPlayer.NAMEC ? "Đến\nNamếc" : "Đến\nXayda");
                    } else if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                    "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                    "Đến\nNamếc", "Đến\nXayda", "Siêu thị");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 84) {
                        ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 24, -1, -1);
                    } else if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc cargo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                    "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                    "Đến\nTrái Đất", "Đến\nXayda", "Siêu thị");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc cui(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            private final int COST_FIND_BOSS = 50000000;

            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            if (this.mapId == 19) {

                                int taskId = TaskService.gI().getIdTask(pl);
                                switch (taskId) {
                                    case ConstTask.TASK_19_0:
                                        this.createOtherMenu(pl, ConstNpc.MENU_FIND_KUKU,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nKuku\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)", "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    case ConstTask.TASK_19_1:
                                        this.createOtherMenu(pl, ConstNpc.MENU_FIND_MAP_DAU_DINH,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nMập đầu đinh\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)", "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    case ConstTask.TASK_19_2:
                                        this.createOtherMenu(pl, ConstNpc.MENU_FIND_RAMBO,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nRambo\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)", "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    default:
                                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến Cold", "Đến\nNappa", "Từ chối");

                                        break;
                                }
                            } else if (this.mapId == 68) {
                                this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                        "Ngươi muốn về Thành Phố Vegeta", "Đồng ý", "Từ chối");
                            } else {
                                this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                        "Tàu vũ trụ Xayda sử dụng công nghệ mới nhất, "
                                        + "có thể đưa ngươi đi bất kỳ đâu, chỉ cần trả tiền là được.",
                                        "Đến\nTrái Đất", "Đến\nNamếc", "Siêu thị");
                            }
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 26) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                    break;
                            }
                        }
                    }
                    if (this.mapId == 19) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (player.getSession().getIdTask < ConstTask.TASK_21_4) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                    } else {
                                        //    this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                    }
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_KUKU) {
                            switch (select) {
                                case 0:
                                    Boss boss = BossManager.gI().getBossById(BossID.KUKU);
                                    if (boss != null && !boss.isDie()) {
                                        if (player.inventory.gold >= COST_FIND_BOSS) {
                                            Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId, boss.zone.zoneId);
                                            if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                                player.inventory.gold -= COST_FIND_BOSS;
                                                ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x, boss.location.y);
                                                Service.gI().sendMoney(player);
                                            } else {
                                                Service.gI().sendThongBao(player, "Khu vực đang full.");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                        }
                                        break;
                                    }
                                    Service.gI().sendThongBao(player, "Chết rồi ba...");
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_MAP_DAU_DINH) {
                            switch (select) {
                                case 0:
                                    Boss boss = BossManager.gI().getBossById(BossID.MAP_DAU_DINH);
                                    if (boss != null && !boss.isDie()) {
                                        if (player.inventory.gold >= COST_FIND_BOSS) {
                                            Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId, boss.zone.zoneId);
                                            if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                                player.inventory.gold -= COST_FIND_BOSS;
                                                ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x, boss.location.y);
                                                Service.gI().sendMoney(player);
                                            } else {
                                                Service.gI().sendThongBao(player, "Khu vực đang full.");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                        }
                                        break;
                                    }
                                    Service.gI().sendThongBao(player, "Chết rồi ba...");
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_RAMBO) {
                            switch (select) {
                                case 0:
                                    Boss boss = BossManager.gI().getBossById(BossID.RAMBO);
                                    if (boss != null && !boss.isDie()) {
                                        if (player.inventory.gold >= COST_FIND_BOSS) {
                                            Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId, boss.zone.zoneId);
                                            if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                                player.inventory.gold -= COST_FIND_BOSS;
                                                ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x, boss.location.y);
                                                Service.gI().sendMoney(player);
                                            } else {
                                                Service.gI().sendThongBao(player, "Khu vực đang full.");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                        }
                                        break;
                                    }
                                    Service.gI().sendThongBao(player, "Chết rồi ba...");
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                    break;
                            }
                        }
                    }
                    if (this.mapId == 68) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 19, -1, 1100);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc santa(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Xin chào, ta có một số vật phẩm đặt biệt cậu có muốn xem không?",
                            "Cửa hàng", "Quy đổi");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5 || this.mapId == 13 || this.mapId == 20) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop
                                    ShopServiceNew.gI().opendShop(player, "SANTA", false);
                                    break;
                                case 1:
                                    this.createOtherMenu(player, ConstNpc.QUY_DOI, "|7|Shop quy đổi đang được mở. Số tiền của bạn còn : " + player.getSession().coinBar + "\n"
                                            + "Muốn quy đổi không", "Quy đổi\n Thỏi vàng và capsual","Đổi đệ", "Quy đổi\nHồng ngọc", "không");
                                    break;

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.QUY_DOI) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, ConstNpc.QUY_DOI_THOI_VANG, "Hãy chọn mệnh giá muốn thay đổi.", "10k",
                                            "20k", "30k", "50K", "100k", "200k", "300k", "500k", "1tr");
                                    break;
                                    
                                 case 2:
                                    this.createOtherMenu(player, ConstNpc.QUY_DOI_HN,
                                            "Chọn các mệnh giá muốn quy đổi hồng ngọc\nTỉ lệ quy đổi là ví dụ: \nnếu hạn chọn mệnh giá là 10k thì sẽ nhận được 1k hồng ngọc",
                                            "Mệnh giá\n10k", "Mệnh giá\n20k", "Mệnh giá\n30k",
                                            "Mệnh giá\n50K", "Mệnh giá\n100k", "Mệnh giá\n200k",
                                            "Mệnh giá\n300k", "Mệnh giá\n500k",
                                            "Mệnh giá\n1 triệu");
                                    break;  
                                //                                   Input.gI().createFormQDTV(player);
//                                case 1:
                                    //                   Input.gI().createFormQDHN(player);
//                                    break;
                                case 1:                                 
                                    Item it = InventoryServiceNew.gI().findItemBag(player, 2087);
                                    if(it == null){
                                        Service.gI().sendThongBao(player, "Bạn không có mảnh hồn để quy đổi");
                                    }else {
                                        this.createOtherMenu(player, ConstNpc.QUY_DOI_DE_TU, "Chọn đệ tử mà bạn muốn đổi", "99 capsual : De Mabu",
                                            "499 capsual : De Pic", "999 capsual : De Berus",  "Dong");
                                        break;
                                    }
                                                                       
                               
                                    break;
                                    
                            }
                        } 
                        else if (player.iDMark.getIndexMenu() == ConstNpc.QUY_DOI_DE_TU){
                             Item it = InventoryServiceNew.gI().findItemBag(player, 2087);
                             switch (select) {
                                case 0:
                                    if(it.quantity < 99){
                                        Service.gI().sendThongBao(player, " Bạn không có đủ mảnh hồn để quy đổi");
                                 
                                    }else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, InventoryServiceNew.gI().findItemBag(player, 2087), 99);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        PetService.gI().changeMabuPet(player);
                                        Service.getInstance().chatJustForMe(player, null, "|1|Bạn đã nhận được Pet Mabu!");
                                    }           
                                    break;
                                case 1:
                                    if(it.quantity < 499){
                                        Service.gI().sendThongBao(player, " Bạn không có đủ mảnh hồn để quy đổi");
                                 
                                    }else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, InventoryServiceNew.gI().findItemBag(player, 2087), 499);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        PetService.gI().changePicPet(player);
                                        Service.getInstance().chatJustForMe(player, null, "|1|Bạn đã nhận được Pet Pic!");
                                    }           
                                    break;
                                    
                                case 2:
                                    if(it.quantity < 999){
                                        Service.gI().sendThongBao(player, " Bạn không có đủ mảnh hồn để quy đổi");
                                 
                                    }else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, InventoryServiceNew.gI().findItemBag(player, 2087), 999);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        PetService.gI().changeBerusPet(player);
                                        Service.getInstance().chatJustForMe(player, null, "|1|Bạn đã nhận được Pet Berus!");
                                    }           
                                    break;
                                case 3 : 
                                    break;
                                    
                            }                    
                             
                        }else if (player.iDMark.getIndexMenu() == ConstNpc.QUY_DOI_THOI_VANG) {
                            switch (select) {
                                case 0:
                                    int coin = 10000;
                                    int capsual = 30;
                                    int tv = 30;
                                    int dans = 3;
                                    if (player.getSession().coinBar >= coin) {
                                        PlayerDAO.subcoinBar(player, coin);
                                        Item thoiVang = ItemService.gI().createNewItem((short) 457, tv);// x3
                                        Item capsualItem = ItemService.gI().createNewItem((short) 2087, capsual);// x3
                                        Item dns = ItemService.gI().createNewItem((short) 674, dans);// x3
                                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                        InventoryServiceNew.gI().addItemBag(player, dns);                                        
                                        InventoryServiceNew.gI().addItemBag(player, capsualItem);

                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "bạn nhận được " + tv
                                                + " " + thoiVang.template.name + " và " + dans
                                                + " " + dns.template.name + "  và " + capsual + " " + capsualItem.template.name);
                                    } else {
                                        Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().coinBar + " không đủ để quy "
                                                + " đổi ");
                                    }
                                    break;
                                case 1:
                                    coin = 20000;
                                    tv = 60;
                                    capsual = 60;
                                    dans = 6;
                                    if (player.getSession().coinBar >= coin) {
                                        PlayerDAO.subcoinBar(player, coin);
                                        Item thoiVang = ItemService.gI().createNewItem((short) 457, tv);// x3
                                        Item dns = ItemService.gI().createNewItem((short) 674, dans);// x3
                                         Item capsualItem = ItemService.gI().createNewItem((short) 2087, capsual);// x3
                                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                        InventoryServiceNew.gI().addItemBag(player, dns);
                                         InventoryServiceNew.gI().addItemBag(player, capsualItem);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "bạn nhận được " + tv
                                                + " " + thoiVang.template.name + " và " + dans
                                                + " " + dns.template.name + "  và " + capsual + " " + capsualItem.template.name );
                                    } else {
                                        Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().coinBar + " không đủ để quy "
                                                + " đổi ");
                                    }
                                    break;
                                case 2:
                                    coin = 30000;
                                    tv = 90;
                                    capsual = 90;
                                    dans = 9;
                                    if (player.getSession().coinBar >= coin) {
                                        PlayerDAO.subcoinBar(player, coin);
                                        Item thoiVang = ItemService.gI().createNewItem((short) 457, tv);// x3
                                        Item dns = ItemService.gI().createNewItem((short) 674, dans);// x3
                                         Item capsualItem = ItemService.gI().createNewItem((short) 2087, capsual);// x3
                                        InventoryServiceNew.gI().addItemBag(player, thoiVang);                                    
                                        InventoryServiceNew.gI().addItemBag(player, dns);
                                         InventoryServiceNew.gI().addItemBag(player, capsualItem);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "bạn nhận được " + tv
                                                + " " + thoiVang.template.name + " và " + dans
                                                + " " + dns.template.name + "  và " + capsual + " " + capsualItem.template.name);
                                    } else {
                                        Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().coinBar + " không đủ để quy "
                                                + " đổi ");
                                    }
                                    break;
                                case 3:
                                    coin = 50000;
                                    tv = 160;
                                    capsual = 160;
                                    dans = 16;
                                    if (player.getSession().coinBar >= coin) {
                                        PlayerDAO.subcoinBar(player, coin);
                                        Item thoiVang = ItemService.gI().createNewItem((short) 457, tv);// x3
                                        Item dns = ItemService.gI().createNewItem((short) 674, dans);// x3
                                        Item capsualItem = ItemService.gI().createNewItem((short) 2087, capsual);// x3
                                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                        InventoryServiceNew.gI().addItemBag(player, dns);
                                        InventoryServiceNew.gI().addItemBag(player, capsualItem);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "bạn nhận được " + tv
                                                + " " + thoiVang.template.name + " và " + dans
                                                + " " + dns.template.name + "  và " + capsual + " " + capsualItem.template.name);
                                    } else {
                                        Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().coinBar + " không đủ để quy "
                                                + " đổi ");
                                    }
                                    break;
                                case 4:
                                    coin = 100000;
                                    tv = 330;
                                    capsual = 330;
                                    dans = 33;
                                    if (player.getSession().coinBar >= coin) {
                                        PlayerDAO.subcoinBar(player, coin);
                                        Item thoiVang = ItemService.gI().createNewItem((short) 457, tv);// x3
                                        Item dns = ItemService.gI().createNewItem((short) 674, dans);// x3
                                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                        InventoryServiceNew.gI().addItemBag(player, dns);
                                        Item capsualItem = ItemService.gI().createNewItem((short) 2087, capsual);// x3
                                        InventoryServiceNew.gI().addItemBag(player, capsualItem);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "bạn nhận được " + tv
                                                + " " + thoiVang.template.name + " và " + dans
                                                + " " + dns.template.name + "  và " + capsual + " " + capsualItem.template.name);
                                    } else {
                                        Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().coinBar + " không đủ để quy "
                                                + " đổi ");
                                    }
                                    break;
                                case 5:
                                    coin = 200000;
                                    tv = 670;
                                    capsual = 670;
                                    dans = 67;
                                    if (player.getSession().coinBar >= coin) {
                                        PlayerDAO.subcoinBar(player, coin);
                                        Item thoiVang = ItemService.gI().createNewItem((short) 457, tv);// x3
                                        Item dns = ItemService.gI().createNewItem((short) 674, dans);// x3
                                        Item capsualItem = ItemService.gI().createNewItem((short) 2087, capsual);// x3
                                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                        InventoryServiceNew.gI().addItemBag(player, capsualItem);
                                        InventoryServiceNew.gI().addItemBag(player, dns);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "bạn nhận được " + tv
                                                + " " + thoiVang.template.name + " và " + dans
                                                + " " + dns.template.name + "  và " + capsual + " " + capsualItem.template.name);
                                    } else {
                                        Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().coinBar + " không đủ để quy "
                                                + " đổi ");
                                    }
                                    break;
                                case 6:
                                    coin = 300000;
                                    tv = 1050;
                                    capsual = 1050;
                                    dans = 105;
                                    if (player.getSession().coinBar >= coin) {
                                        PlayerDAO.subcoinBar(player, coin);
                                        Item thoiVang = ItemService.gI().createNewItem((short) 457, tv);// x3
                                        Item capsualItem = ItemService.gI().createNewItem((short) 2087, capsual);// x3
                                        Item dns = ItemService.gI().createNewItem((short) 674, dans);// x3
                                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                        InventoryServiceNew.gI().addItemBag(player, dns);
                                        InventoryServiceNew.gI().addItemBag(player, capsualItem);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "bạn nhận được " + tv
                                                + " " + thoiVang.template.name + " và " + dans
                                                + " " + dns.template.name + "  và " + capsual + " " + capsualItem.template.name);
                                    } else {
                                        Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().coinBar + " không đủ để quy "
                                                + " đổi ");
                                    }
                                    break;
                                case 7:
                                    coin = 500000;
                                    tv = 1800;
                                    capsual = 1800;
                                    dans = 180;
                                    if (player.getSession().coinBar >= coin) {
                                        PlayerDAO.subcoinBar(player, coin);
                                        Item thoiVang = ItemService.gI().createNewItem((short) 457, tv);// x3
                                        Item dns = ItemService.gI().createNewItem((short) 674, dans);// x3
                                        Item capsualItem = ItemService.gI().createNewItem((short) 2087, capsual);// x3
                                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                        InventoryServiceNew.gI().addItemBag(player, dns);
                                        InventoryServiceNew.gI().addItemBag(player, capsualItem);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "bạn nhận được " + tv
                                                + " " + thoiVang.template.name + " và " + dans
                                                + " " + dns.template.name + "  và " + capsual + " " + capsualItem.template.name);
                                    } else {
                                        Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().coinBar + " không đủ để quy "
                                                + " đổi ");
                                    }
                                    break;
                                case 8:
                                    coin = 1000000;
                                    tv = 3700;
                                    capsual = 3700;
                                    dans = 370;
                                    if (player.getSession().coinBar >= coin) {
                                        PlayerDAO.subcoinBar(player, coin);
                                        Item thoiVang = ItemService.gI().createNewItem((short) 457, tv);// x3
                                        Item dns = ItemService.gI().createNewItem((short) 674, dans);// x3
                                        Item capsualItem = ItemService.gI().createNewItem((short) 2087, capsual);// x3
                                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                        InventoryServiceNew.gI().addItemBag(player, dns);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        InventoryServiceNew.gI().addItemBag(player, capsualItem);
                                        Service.gI().sendThongBao(player, "bạn nhận được " + tv
                                                + " " + thoiVang.template.name + " và " + dans
                                                + " " + dns.template.name + "  và " + capsual + " " + capsualItem.template.name);
                                    } else {
                                        Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().coinBar + " không đủ để quy "
                                                + " đổi ");
                                    }
                                    break;
                                    
                                  }
                            } else if (player.iDMark.getIndexMenu() == ConstNpc.QUY_DOI_HN) {
                              switch (select) {
                                case 0:
                                    int coin = 10000;
                                    int tv = 1000;
                                    int dans = 3;
                                    if (player.getSession().coinBar >= coin) {
                                        PlayerDAO.subcoinBar(player, coin);
                                        Item thoiVang =
                                                ItemService.gI().createNewItem((short) 861, tv);// x3
                                        Item dns =
                                                ItemService.gI().createNewItem((short) 674, dans);// x3
                                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                        InventoryServiceNew.gI().addItemBag(player, dns);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player,
                                                "bạn nhận được " + tv + " " + thoiVang.template.name
                                                        + " và " + dans + " " + dns.template.name);
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "Số tiền của bạn là " + player.getSession().coinBar
                                                        + " không đủ để quy " + " đổi ");
                                    }
                                    break;
                                case 1:
                                    coin = 20000;
                                    tv = 2000;
                                    dans = 6;
                                    if (player.getSession().coinBar >= coin) {
                                        PlayerDAO.subcoinBar(player, coin);
                                        Item thoiVang =
                                                ItemService.gI().createNewItem((short) 861, tv);// x3
                                        Item dns =
                                                ItemService.gI().createNewItem((short) 674, dans);// x3
                                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                        InventoryServiceNew.gI().addItemBag(player, dns);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player,
                                                "bạn nhận được " + tv + " " + thoiVang.template.name
                                                        + " và " + dans + " " + dns.template.name);
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "Số tiền của bạn là " + player.getSession().coinBar
                                                        + " không đủ để quy " + " đổi ");
                                    }
                                    break;
                                case 2:
                                    coin = 30000;
                                    tv = 3000;
                                    dans = 9;
                                    if (player.getSession().coinBar >= coin) {
                                        PlayerDAO.subcoinBar(player, coin);
                                        Item thoiVang =
                                                ItemService.gI().createNewItem((short) 861, tv);// x3
                                        Item dns =
                                                ItemService.gI().createNewItem((short) 674, dans);// x3
                                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                        InventoryServiceNew.gI().addItemBag(player, dns);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player,
                                                "bạn nhận được " + tv + " " + thoiVang.template.name
                                                        + " và " + dans + " " + dns.template.name);
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "Số tiền của bạn là " + player.getSession().coinBar
                                                        + " không đủ để quy " + " đổi ");
                                    }
                                    break;
                                case 3:
                                    coin = 50000;
                                    tv = 5000;
                                    dans = 16;
                                    if (player.getSession().coinBar >= coin) {
                                        PlayerDAO.subcoinBar(player, coin);
                                        Item thoiVang =
                                                ItemService.gI().createNewItem((short) 861, tv);// x3
                                        Item dns =
                                                ItemService.gI().createNewItem((short) 674, dans);// x3
                                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                        InventoryServiceNew.gI().addItemBag(player, dns);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player,
                                                "bạn nhận được " + tv + " " + thoiVang.template.name
                                                        + " và " + dans + " " + dns.template.name);
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "Số tiền của bạn là " + player.getSession().coinBar
                                                        + " không đủ để quy " + " đổi ");
                                    }
                                    break;
                                case 4:
                                    coin = 100000;
                                    tv = 10000;
                                    dans = 33;
                                    if (player.getSession().coinBar >= coin) {
                                        PlayerDAO.subcoinBar(player, coin);
                                        Item thoiVang =
                                                ItemService.gI().createNewItem((short) 861, tv);// x3
                                        Item dns =
                                                ItemService.gI().createNewItem((short) 674, dans);// x3
                                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                        InventoryServiceNew.gI().addItemBag(player, dns);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player,
                                                "bạn nhận được " + tv + " " + thoiVang.template.name
                                                        + " và " + dans + " " + dns.template.name);
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "Số tiền của bạn là " + player.getSession().coinBar
                                                        + " không đủ để quy " + " đổi ");
                                    }
                                    break;
                                case 5:
                                    coin = 200000;
                                    tv = 20000;
                                    dans = 67;
                                    if (player.getSession().coinBar >= coin) {
                                        PlayerDAO.subcoinBar(player, coin);
                                        Item thoiVang =
                                                ItemService.gI().createNewItem((short) 861, tv);// x3
                                        Item dns =
                                                ItemService.gI().createNewItem((short) 674, dans);// x3
                                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                        InventoryServiceNew.gI().addItemBag(player, dns);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player,
                                                "bạn nhận được " + tv + " " + thoiVang.template.name
                                                        + " và " + dans + " " + dns.template.name);
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "Số tiền của bạn là " + player.getSession().coinBar
                                                        + " không đủ để quy " + " đổi ");
                                    }
                                    break;
                                case 6:
                                    coin = 300000;
                                    tv = 30000;
                                    dans = 105;
                                    if (player.getSession().coinBar >= coin) {
                                        PlayerDAO.subcoinBar(player, coin);
                                        Item thoiVang =
                                                ItemService.gI().createNewItem((short) 861, tv);// x3
                                        Item dns =
                                                ItemService.gI().createNewItem((short) 674, dans);// x3
                                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                        InventoryServiceNew.gI().addItemBag(player, dns);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player,
                                                "bạn nhận được " + tv + " " + thoiVang.template.name
                                                        + " và " + dans + " " + dns.template.name);
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "Số tiền của bạn là " + player.getSession().coinBar
                                                        + " không đủ để quy " + " đổi ");
                                    }
                                    break;
                                case 7:
                                    coin = 500000;
                                    tv = 50000;
                                    dans = 180;
                                    if (player.getSession().coinBar >= coin) {
                                        PlayerDAO.subcoinBar(player, coin);
                                        Item thoiVang =
                                                ItemService.gI().createNewItem((short) 861, tv);// x3
                                        Item dns =
                                                ItemService.gI().createNewItem((short) 674, dans);// x3
                                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                        InventoryServiceNew.gI().addItemBag(player, dns);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player,
                                                "bạn nhận được " + tv + " " + thoiVang.template.name
                                                        + " và " + dans + " " + dns.template.name);
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "Số tiền của bạn là " + player.getSession().coinBar
                                                        + " không đủ để quy " + " đổi ");
                                    }
                                    break;
                                case 8:
                                    coin = 1000000;
                                    tv = 100000;
                                    dans = 370;
                                    if (player.getSession().coinBar >= coin) {
                                        PlayerDAO.subcoinBar(player, coin);
                                        Item thoiVang =
                                                ItemService.gI().createNewItem((short) 861, tv);// x3
                                        Item dns =
                                                ItemService.gI().createNewItem((short) 674, dans);// x3
                                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                        InventoryServiceNew.gI().addItemBag(player, dns);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player,
                                                "bạn nhận được " + tv + " " + thoiVang.template.name
                                                        + " và " + dans + " " + dns.template.name);
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "Số tiền của bạn là " + player.getSession().coinBar
                                                        + " không đủ để quy " + " đổi ");
                                    }
                                    break;   
//                            case 8:
//                                MaQuaTangManager.gI().checkInfomationGiftCode(player);
//                                break;
                            }

                        }
                    }
                }
            }
        };
    }

    
    public static Npc TUTIEN(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

           @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 14) {
                        this.createOtherMenu(player, 0,
                                "|2|Bạn đạt 200 tỷ sức mạnh , chuyển sinh cấp 34 , đá tu tiên có thể tu tiên \n"
                                + "cấp 1-10 99 đá \n cấp 10-20 99 đá \n cấp 20-30 99 đá \n "
                                + "\n" + "\n\nSức Mạnh: " + Util.getFormatNumber(player.nPoint.power) + "/" + "105.000.000.000 sức mạnh"
                                + "\n\n Cấp chuyển sinh : " + Util.getFormatNumber(player.capCS), "Hướng dẫn","Tu Tiên", "Xem thông tin", " từ chối");

                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 141) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                     NpcService.gI().createTutorial(player, this.avartar, "hom nay ai tu tien nao");
                                     break;
                                case 1:
                                    int percent = 100;
                                    int hongngoc = 0;
                                    int da =0;
                                    Item TUTIEN = null;
                                    TUTIEN = InventoryServiceNew.gI().findItemBag(player, 1998);
                                    if (player.capTT <= 10) {
                                        hongngoc = 99;
                                        da = 20000;
                                    }
                                    if (player.capTT >10 && player.capTT <= 20) {
                                        hongngoc = 99;
                                        da = 15000;
                                    }
                                    if (player.capTT > 20) {
                                        hongngoc = 99;
                                        da = 10000;
                                    }

                                    if (player.capTT >= 20) {
                                        percent = percent - (player.capTT) * 3;
                                    }
                                    this.createOtherMenu(player, 987,
                                            "|6|Bạn đang Tu Tiên  :" + player.capTT + " \n Cấp tiếp theo với tỉ lệ : " + (100 - player.capTT * 3) + "% \n Mức giá Tu Tiên : " + hongngoc + "Đá tu tiên, "+da+" hồng ngọc \n Bạn có muốn tu tiên ?", "Đồng ý", "Từ chối");
                                    break; // 
                                case 2:
                                    int hp = 0,
                                     dame = 0;
                                    if (player.capTT > 0) {
                                        if (player.capTT <= 10) {
                                            dame += (400) * player.capTT;
                                            hp += (4000) * player.capTT;
                                        }
                                        if (player.capTT <= 20 && player.capTT > 10) {
                                            dame += (500) * (player.capTT);
                                            hp += (5000) * (player.capTT);
                                        }
                                        if (player.capTT > 20) {
                                            dame += (600) * (player.capTT);
                                            hp += (6000) * (player.capTT);
                                        }
                                    }
                                    Service.gI().sendThongBaoOK(player, "Bạn đang cấp Tu Tiên: " + player.capTT + "\n HP : +" + hp + "\n MP : +" + hp + "\n Dame : +" + dame);

                                    break;
                                case 3:
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 987) {
                            if (player.capCS < 34) {
                                npcChat(player, "Bạn chưa đủ cấp chuyển sinh");
                                return;
                            }
                            if (player.nPoint.power < 105000000000L) {
                                npcChat(player, "Bạn chưa đủ sức mạnh tu tiẻn");
                                return;
                            } else {
                                int hongngoc = 0;
                                int da = 0;
                                Item tutien = null;
                                tutien = InventoryServiceNew.gI().findItemBag(player, 1998);
                                if (player.capTT <= 10) {
                                    hongngoc = 99;
                                    da = 20000;
                                }
                                if (player.capTT <= 20 && player.capTT > 10) {
                                    hongngoc = 99;
                                    da = 15000;
                                }
                                if (player.capTT > 20) {
                                    hongngoc = 99;
                                    da = 10000;
                                }
                                if (player.inventory.ruby < da) {
                                    npcChat(player, "Bạn chưa đủ hồng ngọc để Tu Tiên");
                                    return;
                                }
                                
                                if (tutien == null || tutien.quantity < hongngoc) {
                                    this.npcChat(player, "Bạn không đủ "+hongngoc+" đá tu tiên");
                                    return;
                                }
                                int percent = 100;
                                if (player.capTT >= 20) {
                                    percent = percent - (player.capTT) * 3;
                                }
                                player.inventory.ruby -= da;
                                InventoryServiceNew.gI().subQuantityItemsBag(player, tutien, hongngoc);
                                if (Util.nextInt(0, 100) < (percent)) {
                                    player.capTT++;
                                    npcChat(player, "Tu Tiên thành công \n cấp hiện tại :" + player.capTT);
                                    player.nPoint.power = 1500000;

                                } else {
                                    npcChat(player, "Tu tiên thất bại \n cấp hiện tại :" + player.capTT);
                                    player.nPoint.power = 103000000000L;
                                }
                                Service.gI().player(player);
                                InventoryServiceNew.gI().sendItemBags(player);
                                Service.gI().sendMoney(player);
                            }

                        }
                    }
                }
            }
        };
    }
    
    
    public static Npc thodaika(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            
            
           @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, 0,
                                "\n\n|1|Yêu cầu Sức mạnh đạt 106 tỷ"
                                + "\n\n|2|Sau khi chuyển sinh Thành công"
                                + "\n-Sức mạnh trở về 1,5 Triệu"
                                + "\n-Cấp chuyển sinh tăng 1 Cấp"
                                + "\n\n|1|Sức Mạnh: " + Util.getFormatNumber(player.nPoint.power) + "/" + "105Tỷ"
                                + "\n|3|=>Cấp Chuyển sinh càng cao sẽ được công thêm HP,KI,SD càng cao"
                                + "\n\n|7|Chuyển sinh Thất bại sẽ trừ đi Hồng ngọc và Giảm 20 Tỷ Sức mạnh"
                                + "\nMAX cấp Chuyển sinh là 30 Cấp", "Chuyển sinh", "Xem thông tin",
                                "từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    int percent = 100;
                                    int hongngoc = 0;
                                    if (player.capCS <= 10) {
                                        hongngoc = 20000;
                                    }
                                    if (player.capCS <= 20 && player.capCS > 10) {
                                        hongngoc = 15000;
                                    }
                                    if (player.capCS > 20) {
                                        hongngoc = 10000;
                                    }

                                    if (player.capCS >= 20) {
                                        percent = percent - (player.capCS) * 3;
                                    }
                                    this.createOtherMenu(player, 987,
                                            "|5|Bạn đang chuyển sinh :" + player.capCS + " \n Cấp tiếp theo với tỉ lệ : " + (100 - player.capCS * 3) + "% \n Mức giá chuyển sinh : " + hongngoc + "hồng ngọc \nBạn có muốn chuyển sinh ?", "Đồng ý", "Từ chối");
                                    break; // 
                                case 1:
                                    int hp = 0,
                                     dame = 0;
                                    if (player.capCS > 0) {
                                        if (player.capCS <= 10) {
                                            dame += (100) * player.capCS;
                                            hp += (1000) * player.capCS;
                                        }
                                        if (player.capCS <= 20 && player.capCS > 10) {
                                            dame += (200) * (player.capCS);
                                            hp += (2000) * (player.capCS);
                                        }
                                        if (player.capCS > 20) {
                                            dame += (300) * (player.capCS);
                                            hp += (3000) * (player.capCS);
                                        }
                                    }
                                    Service.gI().sendThongBaoOK(player, "Bạn đang cấp chuyển sinh: " + player.capCS + "\n HP : +" + hp + "\n MP : +" + hp + "\n Dame : +" + dame);

                                    break;
                                case 2:
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 987) {
                            if (player.nPoint.power < 105000000000L) {
                                npcChat(player, "Bạn chưa đủ khỏe để chuyển sinh");
                                return;
                            } else {
                                int hongngoc = 0;
                                if (player.capCS <= 10) {
                                    hongngoc = 20000;
                                }
                                if (player.capCS <= 20 && player.capCS > 10) {
                                    hongngoc = 15000;
                                }
                                if (player.capCS > 20) {
                                    hongngoc = 10000;
                                }
                                if (player.inventory.ruby < hongngoc) {
                                    npcChat(player, "Bạn chưa đủ hồng ngọc để chuyển sinh");
                                    return;
                                }
                                int percent = 100;
                                if (player.capCS >= 20) {
                                    percent = percent - (player.capCS) * 3;
                                }
                                player.inventory.ruby -= hongngoc;
                                if (Util.nextInt(0, 100) < (percent)) {
                                    player.capCS++;
                                    npcChat(player, "Chuyển sinh thành công \n cấp hiện tại :" + player.capCS);
                                    player.nPoint.power = 1500000;

                                } else {
                                    npcChat(player, "Chuyển sinh thất bại \n cấp hiện tại :" + player.capCS);
                                    player.nPoint.power = 103000000000L - 20000000000L;
                                }
                                Service.gI().player(player);
                                InventoryServiceNew.gI().sendItemBags(player);
                                Service.gI().sendMoney(player);

                            }

                        }
                    }
                }
            }
        };
    }       

    public static Npc uron(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    ShopServiceNew.gI().opendShop(pl, "URON", false);
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc gohannn(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) {
                        this.createOtherMenu(player, 0, "Tiến vào map\nNơi up set kích hoạt và nhiều phần quà hấp dẫn\nChỉ dành cho người chơi từ 2k đến 60 tỷ sức mạnh!", "Đến\nRừng Aurura", "Từ chối");

                    } else {
                        this.createOtherMenu(player, 0, "Ngươi muốn quay về?", "Quay về", "Từ chối");
                    }
                }

            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                    switch (select) {
                        case 0:
                            if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) {

                                if (player.session.actived) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 250, -1, 295);
                                    break;
                                } else {
                                    Service.getInstance().sendThongBao(player, "Hãy mở thành viên Để sử dụng");
                                }

                            } else {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 295);
                            }
                        case 1:

                            break;
                    }

                }
            }
        };
    }

     public static Npc noibanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (player.getSession().vnd > cn.gioihanvnd && player.getSession().totalvnd > cn.gioihantotalvnd) {
                    PlayerService.gI().banPlayer((player));
                    Service.getInstance().sendThongBao(player, "Bạn bị ban thành công");
                    return;
                }
                if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14 || this.mapId == 5) {
                    if (ConstDataEvent.thoiGianNauBanh == -1 ) {
                        this.createOtherMenu(player, ConstNpc.CB_NAU_BANH, "Nồi nấu bánh toàn server\n Đang trong thời gian lấy bánh chín, và cho nguyên liệu vào để nấu tiếp" +
                                "\nThời gian chuẩn bị để nấu tiếp còn " +((NauBanhServices.timeCBNau - System.currentTimeMillis()) / 1000) +
                                "\nToan Server đang có " + ConstDataEvent.slBanhTrongNoi + " đang chuẩn bị nấu " +
                                "\nTrong đó mày có " + getSLBanhChungTet(player) + " đang chuẩn bị nấu", "Nấu bánh chưng", "Nấu bánh tét", "Hướng dâ");

                    }
                    else if (ConstDataEvent.thoiGianNauBanh == 0 ) {
                        this.createOtherMenu(player, ConstNpc.BANH_CHIN, "Bánh đã chín, Mày có 5 phút để lấy", "Lấy ngay", "Hướng dâ");
                    }
                    else {
                        this.createOtherMenu(player, ConstNpc.NAU_BANH, "Nồi nấu bánh toàn server\n Thời gian nấu còn " + ConstDataEvent.thoiGianNauBanh / 1000 +
                                "\nSố bánh đang nấu " + ConstDataEvent.slBanhTrongNoi +
                                "\nMức nước trong nồi " + ConstDataEvent.mucNuocTrongNoi + "/" + ConstDataEvent.slBanhTrongNoi + " đang chuẩn bị nấu " +
                                "\nTrong đó có " + (player.slBanhChung + player.slBanhTet) + " bánh của bạn đang nấu\n Chơm đủ nước để nồi không bị cháy và nhận đủ số bánh nấu" +
                                "\nThêm củi lửa để tăng tốc thời gian nấu bánh", "Thêm nước nấu", "Thêm củi lữa", "Hướng dẫn");

                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.CB_NAU_BANH) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, ConstNpc.NAU_BANH_CHUNG, "Bánh chưng: 10 Lá giong, 10 Gạo nếp, 10 Đậu xanh, 10 Gióng tre, 10 Thịt lợn và 03 Nước nấu.", "Nấu", "Đóng");
                                    break;
                                case 1:
                                    this.createOtherMenu(player, ConstNpc.NAU_BANH_TET, "Bánh tết: 10 Lá chuối, 10 Gạo nếp, 10 Đậu xanh, 10 Giống tre, 10 Thịt lợn và 03 Nước nấu.", "Nấu", "Đóng");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.NAU_BANH_CHUNG) {
                            switch (select) {
                                case 0:
                                    Input.gI().createFormNauBanhChung(player);
                                    break;
                            }
                        }
                        else if (player.iDMark.getIndexMenu() == ConstNpc.NAU_BANH_TET) {
                            switch (select) {
                                case 0:
                                    Input.gI().createFormNauBanhTet(player);
                                    break;
                            }
                        }
                        else if (player.iDMark.getIndexMenu() == ConstNpc.BANH_CHIN) {
                            switch (select) {
                                case 0:
                                    if(!NauBanhServices.banhChungBanhTets.containsKey(player)){
                                        Service.gI().sendThongBao(player,"Có đâu mà nhận");
                                        return;
                                    }
                                    if(NauBanhServices.banhChungBanhTets.get(player).slBanhTet ==0  &&NauBanhServices.banhChungBanhTets.get(player).slBanhChung == 0){
                                        Service.gI().sendThongBao(player,"Có đâu mà nhận");
                                        return;
                                    }
                                    if(NauBanhServices.banhChungBanhTets.get(player).slBanhTet != 0 ){
                                        Item banhTet = ItemService.gI().createNewItem((short)1225 , NauBanhServices.banhChungBanhTets.get(player).slBanhTet);
                                        InventoryServiceNew.gI().addItemBag(player,banhTet);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player,"Bạn đã nhận được bánh tét");
                                        player.slBanhTet = 0;
                                        NauBanhServices.banhChungBanhTets.get(player).slBanhTet = 0;
                                    }
                                    if(NauBanhServices.banhChungBanhTets.get(player).slBanhChung != 0 ){
                                        Item banhChung = ItemService.gI().createNewItem((short)1224  , NauBanhServices.banhChungBanhTets.get(player).slBanhChung);
                                        InventoryServiceNew.gI().addItemBag(player,banhChung);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player,"Bạn đã nhận được bánh chưng");
                                        player.slBanhChung = 0;
                                        NauBanhServices.banhChungBanhTets.get(player).slBanhChung = 0;
                                    }
                                    break;
                            }
                        }
                        else if(player.iDMark.getIndexMenu() ==  ConstNpc.NAU_BANH){
                            switch (select){
                                case 0:
                                    if(ConstDataEvent.mucNuocTrongNoi < ConstDataEvent.slBanhTrongNoi){
                                        Item nuocNau = InventoryServiceNew.gI().findItemBag(player,1221 );
                                        if(nuocNau == null){
                                            Service.gI().sendThongBao(player,"Có nước nấu đâu cu");
                                            return;
                                        }
                                        InventoryServiceNew.gI().subQuantityItemsBag(player,nuocNau,1);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        ConstDataEvent.mucNuocTrongNoi++;
                                    }
                                    else{
                                        Service.gI().sendThongBao(player,"Đủ nước rồi cu");
                                    }
                                    return;
                                case 1:
                                    if(ConstDataEvent.thoiGianNauBanh <=0){
                                        return;
                                    }
                                    else{
                                        Item cuiLua = InventoryServiceNew.gI().findItemBag(player,1220);
                                        if(cuiLua == null){
                                            Service.gI().sendThongBao(player,"Có cui lửa đâu cú");
                                            return;
                                        }
                                        InventoryServiceNew.gI().subQuantityItemsBag(player,cuiLua,1);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        ConstDataEvent.thoiGianNauBanh -= 3000;
                                    }
                                    return;
                            }
                        }
                    }
                }
            }
        };
    }

    private static String getSLBanhChungTet(Player player) {
        if(NauBanhServices.banhChungBanhTets.containsKey(player)){
            int tongSLBanh = NauBanhServices.banhChungBanhTets.get(player).slBanhChung + NauBanhServices.banhChungBanhTets.get(player).slBanhTet;
            return String.valueOf(tongSLBanh);
        }
        return "0";
    }

    ;
    
    
    
    
    public static Npc baHatMit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
//                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                                "Ngươi tìm ta có việc gì?",
//                                "Ép sao", "Pha lê\nhóa", "Pháp sư\ntrang bị",
//                                "Nâng Cấp\nHuỷ Diệt", "Nâng Cấp SKH");
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Ép sao", "Pha lê\nhóa", "Pháp sư\ntrang bị",
                                "Nâng Cấp SKH", "Nâng Cấp\nSarigan");

                    } else if (this.mapId == 121) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Về đảo\nrùa");

                    } else {

                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Cửa hàng\nBùa", "Nâng cấp\nVật phẩm",
                                "Nâng cấp\nBông tai\nPorata", "Mở chỉ số\nBông tai\nPorata",
                                "Nhập\nNgọc Rồng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
//                                                CombineService.gI().openTabCombine(player, CombineService.EP_SAO_TRANG_BI);
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.EP_SAO_TRANG_BI);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHA_LE_HOA_TRANG_BI);
                                    break;
                                case 2:

                                    createOtherMenu(player, ConstNpc.PHAP_SU,
                                            "Ngươi tìm ta có việc gì?\n",
                                            "Hướng dẫn", "Pháp Sư Hoá\nTrang Bị", "Tẩy\nPháp Sư", "Từ Chối");
                                    break;

//                                case 3:
//                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.kh_Hd);
//                                    break;
                                case 3:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_SKH_VIPhd);
                                    break;
                                case 4:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_MAT_THAN);
                                    break;

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.EP_SAO_TRANG_BI:
                                case CombineServiceNew.PHA_LE_HOA_TRANG_BI:
                                case CombineServiceNew.CHUYEN_HOA_TRANG_BI:
                                case CombineServiceNew.PS_HOA_TRANG_BI:
                                case CombineServiceNew.NANG_CAP_MAT_THAN:
                                case CombineServiceNew.NANG_CAP_BONG_TAI_CAP3:
                                case CombineServiceNew.MO_CHI_SO_BONG_TAI_CAP3:
                                case CombineServiceNew.kh_Tl:
                                case CombineServiceNew.kh_Hd:
                                case CombineServiceNew.kh_Ts:
                                case CombineServiceNew.NANG_CAP_SKH_VIPhd:
                                case CombineServiceNew.TAY_PS_HOA_TRANG_BI:
                                    switch (select) {
                                        case 0:
                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                player.combineNew.quantities = 1;
                                            }
                                            break;
                                        case 1:
                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                player.combineNew.quantities = 10;
                                            }
                                            break;
                                        case 2:
                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                player.combineNew.quantities = 100;
                                            }
                                            break;
                                    }
                                    CombineServiceNew.gI().startCombine(player);

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.PHAP_SU) {
                            switch (select) {
                                case 0:
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_PHAP_SU_HOA);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PS_HOA_TRANG_BI);
                                    break;
                                case 2:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.TAY_PS_HOA_TRANG_BI);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.SKH) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.kh_Tl);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.kh_Hd);
                                    break;
                                case 2:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.kh_Ts);
                                    break;

                            }
                            
                     
                        }else if (player.iDMark.getIndexMenu() == ConstNpc.PHAP_SU) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.PHA_LE_HOA_TRANG_BI);
                                    break;

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DAP_DO_KICH_HOAT) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_DOI_SKH_VIP) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }
                        }

                    } else if (this.mapId == 112) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1156);
                                    break;
                            }
                        }
                    } else if (this.mapId == 42 || this.mapId == 43 || this.mapId == 44 || this.mapId == 84) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop bùa
                                    createOtherMenu(player, ConstNpc.MENU_OPTION_SHOP_BUA,
                                            "Bùa của ta rất lợi hại, nhìn ngươi yếu đuối thế này, chắc muốn mua bùa để "
                                            + "mạnh mẽ à, mua không ta bán cho, xài rồi lại thích cho mà xem.",
                                            "Bùa\n1 giờ", "Bùa\n8 giờ", "Bùa\n1 tháng", "Đóng");
                                    break;
                                case 1:

                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_VAT_PHAM);
                                    break;
                                case 2: //nâng cấp bông tai
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI);
                                    break;
                                case 3: //làm phép nhập đá
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BONG_TAI);
                                    break;
                                case 4:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NHAP_NGOC_RONG);
                                    break;

                            }

                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_SHOP_BUA) {
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1H", true);
                                    break;
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "BUA_8H", true);
                                    break;
                                case 2:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1M", true);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.NANG_CAP_VAT_PHAM:
                                case CombineServiceNew.NANG_CAP_BONG_TAI:
                                case CombineServiceNew.MO_CHI_SO_BONG_TAI:
                                case CombineServiceNew.NHAP_NGOC_RONG:
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHAN_RA_DO_THAN_LINH) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_CAP_DO_TS) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc ruongDo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    InventoryServiceNew.gI().sendItemBox(player);
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc duongtank(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (mapId == 0) {
                        nguhs.gI().setTimeJoinnguhs();
                        long now = System.currentTimeMillis();
                        if (now > nguhs.TIME_OPEN_NHS && now < nguhs.TIME_CLOSE_NHS) {
                            this.createOtherMenu(player, 0, "Ngũ Hàng Sơn x3 Tnsm\nHỗ trợ cho Ae trên 20 Tỷ SM?\n5 hồng ngọc 1 lần vào", "Éo", "OK");
                        } else {
                            this.createOtherMenu(player, 0, "Ngũ Hàng Sơn x3 Tnsm\nHỗ trợ cho Ae trên 20 Tỷ SM?\n5 hồng ngọc 1 lần vào", "Éo");
                        }
                    }
                    if (mapId == 123) {
                        this.createOtherMenu(player, 0, "Bạn Muốn Quay Trở Lại Làng Ảru?", "OK", "Từ chối");

                    }
                    if (mapId == 122) {
                        this.createOtherMenu(player, 0, "Xia xia thua phùa\b|7|Thí chủ đang có: " + player.NguHanhSonPoint + " điểm ngũ hành sơn\b|1|Thí chủ muốn đổi cải trang x4 chưởng ko?", "Âu kê", "Top Ngu Hanh Son", "No");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                    if (mapId == 0) {
                        switch (select) {
                            case 0:
                                break;
                            case 1:
                                if (player.nPoint.power < 20000000000L) {
                                    Service.getInstance().sendThongBao(player, "Sức mạnh bạn Đéo đủ để qua map!");
                                    return;
                                } else if (player.inventory.ruby < 5) {
                                    Service.getInstance().sendThongBao(player, "Phí vào là 500 hồng ngọc một lần bạn ey!\nBạn đéo đủ!");
                                    return;
                                } else {
                                    player.inventory.ruby -= 500;
                                    PlayerService.gI().sendInfoHpMpMoney(player);
                                    ChangeMapService.gI().changeMapInYard(player, 123, -1, -1);
                                }
                                break;
                        }
                    }
                    if (mapId == 123) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapInYard(player, 0, -1, 469);
                        }
                    }
                    if (mapId == 122) {
                        if (select == 0) {
                            if (player.NguHanhSonPoint >= 500) {
                                player.NguHanhSonPoint -= 500;
                                Item item = ItemService.gI().createNewItem((short) (711));
                                item.itemOptions.add(new Item.ItemOption(49, 80));
                                item.itemOptions.add(new Item.ItemOption(77, 80));
                                item.itemOptions.add(new Item.ItemOption(103, 50));
                                item.itemOptions.add(new Item.ItemOption(207, 0));
                                item.itemOptions.add(new Item.ItemOption(33, 0));
//                                      
                                InventoryServiceNew.gI().addItemBag(player, item);
                                Service.gI().sendThongBao(player, "Chúc Mừng Bạn Đổi Vật Phẩm Thành Công !");
                            } else {
                                Service.gI().sendThongBao(player, "Không đủ điểm, bạn còn " + (500 - player.pointPvp) + " điểm nữa");
                            }

                        }
                        if (select == 1) {
                            Service.gI().showListTop(player, Manager.topNHS);

                        }
                    }

                }
            }
        };
    }

    public static Npc dauThan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    player.magicTree.openMenuTree();
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    TaskService.gI().checkDoneTaskConfirmMenuNpc(player, this, (byte) select);
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MAGIC_TREE_NON_UPGRADE_LEFT_PEA:
                            if (select == 0) {
                                player.magicTree.harvestPea();
                            } else if (select == 1) {
                                if (player.magicTree.level == 10) {
                                    player.magicTree.fastRespawnPea();
                                } else {
                                    player.magicTree.showConfirmUpgradeMagicTree();
                                }
                            } else if (select == 2) {
                                player.magicTree.fastRespawnPea();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_NON_UPGRADE_FULL_PEA:
                            if (select == 0) {
                                player.magicTree.harvestPea();
                            } else if (select == 1) {
                                player.magicTree.showConfirmUpgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_CONFIRM_UPGRADE:
                            if (select == 0) {
                                player.magicTree.upgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_UPGRADE:
                            if (select == 0) {
                                player.magicTree.fastUpgradeMagicTree();
                            } else if (select == 1) {
                                player.magicTree.showConfirmUnuppgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_CONFIRM_UNUPGRADE:
                            if (select == 0) {
                                player.magicTree.unupgradeMagicTree();
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc calick(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            private final byte COUNT_CHANGE = 50;
            private int count;

            private void changeMap() {
                if (this.mapId != 102) {
                    count++;
                    if (this.count >= COUNT_CHANGE) {
                        count = 0;
                        this.map.npcs.remove(this);
                        Map map = MapService.gI().getMapForCalich();
                        this.mapId = map.mapId;
                        this.cx = Util.nextInt(100, map.mapWidth - 100);
                        this.cy = map.yPhysicInTop(this.cx, 0);
                        this.map = map;
                        this.map.npcs.add(this);
                    }
                }
            }

            @Override
            public void openBaseMenu(Player player) {
                player.iDMark.setIndexMenu(ConstNpc.BASE_MENU);
                if (TaskService.gI().getIdTask(player) < ConstTask.TASK_20_0) {
                    Service.gI().hideWaitDialog(player);
                    Service.gI().sendThongBao(player, "Không thể thực hiện");
                    return;
                }
                if (this.mapId != player.zone.map.mapId) {
                    Service.gI().sendThongBao(player, "Calích đã rời khỏi map!");
                    Service.gI().hideWaitDialog(player);
                    return;
                }

                if (this.mapId == 102) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào chú, cháu có thể giúp gì?",
                            "Kể\nChuyện", "Quay về\nQuá khứ");
                } else {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào chú, cháu có thể giúp gì?", "Kể\nChuyện", "Đi đến\nTương lai", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (this.mapId == 102) {
                    if (player.iDMark.isBaseMenu()) {
                        if (select == 0) {
                            //kể chuyện
                            NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                        } else if (select == 1) {
                            //về quá khứ
                            ChangeMapService.gI().goToQuaKhu(player);
                        }
                    }
                } else if (player.iDMark.isBaseMenu()) {
                    if (select == 0) {
                        //kể chuyện
                        NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                    } else if (select == 1) {
                        //đến tương lai
//                                    changeMap();
                        if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_20_0) {
                            ChangeMapService.gI().goToTuongLai(player);
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Không thể thực hiện");
                    }
                }
            }
        };
    }

    public static Npc jaco(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Gô Tên, Calich và Monaka đang gặp chuyện ở hành tinh Potaufeu \n Hãy đến đó ngay", "Đến \nPotaufeu");
                    } else if (this.mapId == 139) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Người muốn trở về?", "Quay về", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        if (player.getSession().player.nPoint.power >= 800000000L) {

                            ChangeMapService.gI().goToPotaufeu(player);
                        } else {
                            this.npcChat(player, "Bạn chưa đủ 800tr sức mạnh để vào!");
                        }
                    } else if (this.mapId == 139) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                //về trạm vũ trụ
                                case 0:
                                    this.npcChat(player, "Chức Năng Tạm Thời Đang Bảo Trì!");
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    //public static Npc Potage(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 149) {
//                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                                "tét", "Gọi nhân bản");
//                    }
//                }
//            }
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                   if (select == 0){
//                        BossManager.gI().createBoss(-214);
//                   }
//                }
//            }
//        };
//    }
    public static Npc npclytieunuong54(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                createOtherMenu(player, 0, "Trò chơi Chọn ai đây đang được diễn ra, nếu bạn tin tưởng mình đang tràn đầy may mắn thì có thể tham gia thử", "Thể lệ", "Chọn\nThỏi vàng", "Chẵn Lẽ");
            }

            @Override
            public void confirmMenu(Player pl, int select) {
                if (canOpenNpc(pl)) {
                    String time = ((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                    if (((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) < 0) {
                        ChonAiDay.gI().lastTimeEnd = System.currentTimeMillis() + 300000;
                    }
                    String timeCL = ((ChanLe.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                    if (((ChanLe.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) < 0) {
                        ChanLe.gI().lastTimeEnd = System.currentTimeMillis() + 300000;
                    }
                    if (pl.iDMark.getIndexMenu() == 0) {
                        if (select == 0) {
                            createOtherMenu(pl, ConstNpc.IGNORE_MENU, "Thời gian giữa các giải là 5 phút\nKhi hết giờ, hệ thống sẽ ngẫu nhiên chọn ra 1 người may mắn.\nLưu ý: Số thỏi vàng nhận được sẽ bị nhà cái lụm đi 5%!Trong quá trình diễn ra khi đặt cược nếu thoát game mọi phần đặt đều sẽ bị hủy", "Ok");
                        } else if (select == 1) {
                            createOtherMenu(pl, 1, "Tổng giải thường: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
                        }else if (select == 2) {
                                    createOtherMenu(pl, 2, "Tổng giải Chẵn: " + ChanLe.gI().Chan + " thỏi vàng,\nTổng giải Lẽ: " + ChanLe.gI().Le + " thỏi vàng,\nSố thỏi vàng đặt Chẵn: " + pl.Chan + "\nSố thỏi vàng đặt Lẽ: " + pl.Le + "\n Thời gian còn lại: " + timeCL, "Cập nhập", "Đặt cược Lẽ \n20 thỏi\nvàng", "Đặt cược Chẵn \n20 thỏi\nvàng", "Đóng");
                        }
                    } else if (pl.iDMark.getIndexMenu() == 1) {
                        if (((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "Tổng giải thường: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
                                    break;
                                case 1: {
                                    try {
                                        if (InventoryServiceNew.gI().findItemBag(pl, 457).isNotNullItem() && InventoryServiceNew.gI().findItemBag(pl, 457).quantity >= 20) {
                                            InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, 457), 20);
                                            InventoryServiceNew.gI().sendItemBags(pl);
                                            pl.goldNormar += 20;
                                            ChonAiDay.gI().goldNormar += 20;
                                            ChonAiDay.gI().addPlayerNormar(pl);
                                            createOtherMenu(pl, 1, "Tổng giải thường: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
                                        } else {
                                            Service.gI().sendThongBao(pl, "Bạn không đủ thỏi vàng");

                                        }
                                    } catch (Exception ex) {
                                        java.util.logging.Logger.getLogger(NpcFactory.class
                                                .getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                break;

                                case 2: {
                                    try {
                                        if (InventoryServiceNew.gI().findItemBag(pl, 457).isNotNullItem() && InventoryServiceNew.gI().findItemBag(pl, 457).quantity >= 200) {
                                            InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, 457), 200);
                                            InventoryServiceNew.gI().sendItemBags(pl);
                                            pl.goldVIP += 200;
                                            ChonAiDay.gI().goldVip += 200;
                                            ChonAiDay.gI().addPlayerVIP(pl);
                                            createOtherMenu(pl, 1, "Tổng giải thường: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
                                        } else {
                                            Service.gI().sendThongBao(pl, "Bạn không đủ thỏi vàng");
                                        }
                                    } catch (Exception ex) {
//                                            java.util.logging.Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                break;

                            }
                        }
                    }else if (pl.iDMark.getIndexMenu() == 2) {
                        if (((ChanLe.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 2, "Tổng giải Chẵn: " + ChanLe.gI().Chan + " thỏi vàng,\nTổng giải Lẽ: " + ChanLe.gI().Le + " thỏi vàng,\nSố thỏi vàng đặt Chẵn: " + pl.Chan + "\nSố thỏi vàng đặt Lẽ: " + pl.Le + "\n Thời gian còn lại: " + timeCL, "Cập nhập", "Đặt cược Lẽ \n20 thỏi\nvàng", "Đặt cược Chẵn \n20 thỏi\nvàng", "Đóng");
                                    break;
                                case 1: {
                                    try {
                                        if (InventoryServiceNew.gI().findItemBag(pl, 457).isNotNullItem() && InventoryServiceNew.gI().findItemBag(pl, 457).quantity >= 20) {
                                            InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, 457), 20);
                                            InventoryServiceNew.gI().sendItemBags(pl);
                                            pl.Le += 20;
                                            ChanLe.gI().Le += 20;
                                            ChanLe.gI().addPlayerChan(pl);
                                    createOtherMenu(pl, 2, "Tổng giải Chẵn: " + ChanLe.gI().Chan + " thỏi vàng,\nTổng giải Lẽ: " + ChanLe.gI().Le + " thỏi vàng,\nSố thỏi vàng đặt Chẵn: " + pl.Chan + "\nSố thỏi vàng đặt Lẽ: " + pl.Le + "\n Thời gian còn lại: " + timeCL, "Cập nhập", "Đặt cược Lẽ \n20 thỏi\nvàng", "Đặt cược Chẵn \n20 thỏi\nvàng", "Đóng");
                                        } else {
                                            Service.gI().sendThongBao(pl, "Bạn không đủ thỏi vàng");

                                        }
                                    } catch (Exception ex) {
                                        java.util.logging.Logger.getLogger(NpcFactory.class
                                                .getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                break;

                                case 2: {
                                    try {
                                        if (InventoryServiceNew.gI().findItemBag(pl, 457).isNotNullItem() && InventoryServiceNew.gI().findItemBag(pl, 457).quantity >= 20) {
                                            InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, 457), 20);
                                            InventoryServiceNew.gI().sendItemBags(pl);
                                            pl.Chan += 20;
                                            ChanLe.gI().Chan += 20;
                                            ChanLe.gI().addPlayerLe(pl);
                                    createOtherMenu(pl, 2, "Tổng giải Chẵn: " + ChanLe.gI().Chan + " thỏi vàng,\nTổng giải Lẽ: " + ChanLe.gI().Le + " thỏi vàng,\nSố thỏi vàng đặt Chẵn: " + pl.Chan + "\nSố thỏi vàng đặt Lẽ: " + pl.Le + "\n Thời gian còn lại: " + timeCL, "Cập nhập", "Đặt cược Lẽ \n20 thỏi\nvàng", "Đặt cược Chẵn \n20 thỏi\nvàng", "Đóng");
                                        } else {
                                            Service.gI().sendThongBao(pl, "Bạn không đủ thỏi vàng");
                                        }
                                    } catch (Exception ex) {
//                                            java.util.logging.Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                break;

                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc thuongDe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 45) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con muốn làm gì nào", "Đến Kaio");// "Quay số\nmay mắn");
                    }
                    if (this.mapId == 143) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "ở đây rất nguy hiểm con có muốn đi tiếp khon??",
                                "Đồng ý", "Quay Về");
                    }
                    if (this.mapId == 129) {
                        this.createOtherMenu(player, 0,
                                "Con muốn gì nào?", "Quay Về");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 143) {
                        switch (select) {
                            case 0: // quay ve
                                ChangeMapService.gI().changeMapBySpaceShip(player, 144, -1, 354);
                                break;
                        }
                    }
                }
                if (this.mapId == 129) {
                    switch (select) {
                        case 0: // quay ve
                            ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 354);
                            break;
                    }
                }
                if (this.mapId == 45) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 48, -1, 354);
                                break;
                            case 1:
                                this.createOtherMenu(player, ConstNpc.MENU_CHOOSE_LUCKY_ROUND,
                                        "Con muốn làm gì nào?", "Quay bằng\nvàng",
                                        "Rương phụ\n("
                                        + (player.inventory.itemsBoxCrackBall.size()
                                        - InventoryServiceNew.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
                                        + " món)",
                                        "Xóa hết\ntrong rương", "Đóng");
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHOOSE_LUCKY_ROUND) {
                        switch (select) {
                            case 0:
                                LuckyRound.gI().openCrackBallUI(player, LuckyRound.USING_GOLD);
                                break;
                            case 1:
                                ShopServiceNew.gI().opendShop(player, "ITEMS_LUCKY_ROUND", true);
                                break;
                            case 2:
                                NpcService.gI().createMenuConMeo(player,
                                        ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND, this.avartar,
                                        "Con có chắc muốn xóa hết vật phẩm trong rương phụ? Sau khi xóa "
                                        + "sẽ không thể khôi phục!",
                                        "Đồng ý", "Hủy bỏ");
                                break;
                        }
                    }
                }

            }
        };
    }

    public static Npc thanVuTru(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con muốn làm gì nào?", "Di chuyển");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, ConstNpc.MENU_DI_CHUYEN,
                                            "Con muốn đi đâu?", "Về\nthần điện", "Thánh địa\nKaio", "Con\nđường\nrắn độc", "Từ chối");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DI_CHUYEN) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 45, -1, 354);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                    break;
                                case 2:
                                    if (player.clan != null) {
                                        if (player.clan.ConDuongRanDoc != null) {
                                            this.createOtherMenu(player, ConstNpc.MENU_OPENED_CDRD,
                                                    "Bang hội của con đang đi con đường rắn độc cấp độ "
                                                    + player.clan.ConDuongRanDoc.level + "\nCon có muốn đi theo không?",
                                                    "Đồng ý", "Từ chối");
                                        } else {
                                            this.createOtherMenu(player, ConstNpc.MENU_OPEN_CDRD,
                                                    "Đây là Con đường rắn độc \nCác con cứ yên tâm lên đường\n"
                                                    + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                                    "Chọn\ncấp độ", "Từ chối");
                                        }
                                    } else {
                                        this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_CDRD) {
                            switch (select) {
                                case 0:
                                    if (player.isAdmin() || player.nPoint.power >= ConDuongRanDoc.POWER_CAN_GO_TO_CDRD) {
                                        ChangeMapService.gI().goToCDRD(player);
                                    } else {
                                        Service.gI().sendThongBao(player, "Không đủ sức mạnh yêu cầu");
                                    }
                                    if (player.clan.haveGoneConDuongRanDoc) {
                                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Bang hội của ngươi đã đi con đường rắn độc lúc " + TimeUtil.formatTime(player.clan.lastTimeOpenConDuongRanDoc, "HH:mm:ss") + " hôm nay. Người mở\n"
                                                + "(" + player.clan.playerOpenConDuongRanDoc + "). Hẹn ngươi quay lại vào ngày mai", "OK", "Hướng\ndẫn\nthêm");
                                        return;
                                    } else if (player.clanMember.getNumDateFromJoinTimeToToday() < 2) {
                                        Service.gI().sendThongBao(player, "Yêu cầu tham gia bang hội trên 2 ngày!");
                                    } else {
                                        this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                                + Util.numberToMoney(ConDuongRanDoc.POWER_CAN_GO_TO_CDRD));
                                    }
                                    break;

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_CDRD) {
                            switch (select) {
                                case 0:
                                    if (player.isAdmin() || player.nPoint.power >= ConDuongRanDoc.POWER_CAN_GO_TO_CDRD) {
                                        Input.gI().createFormChooseLevelCDRD(player);
                                    } else {
                                        this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                                + Util.numberToMoney(ConDuongRanDoc.POWER_CAN_GO_TO_CDRD));
                                    }
                                    break;
                            }

                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_CDRD) {
                            switch (select) {
                                case 0:
                                    ConDuongRanDocService.gI().openConDuongRanDoc(player, Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                    break;
                            }
                        }
                    }
                }
            }

        };
    }

    public static Npc kibit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Đến\nKaio", "Từ chối");
                    }
                    if (this.mapId == 114) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc osin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Đến\nKaio", "Đến\nhành tinh\nBill", "Từ chối");
                    } else if (this.mapId == 154) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Về thánh địa", "Đến\nhành tinh\nngục tù", "Từ chối");
                    } else if (this.mapId == 155) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    } else if (this.mapId == 52) {
                        try {
                            MapMaBu.gI().setTimeJoinMapMaBu();
                            if (this.mapId == 52) {
                                long now = System.currentTimeMillis();
                                if (now > MapMaBu.TIME_OPEN_MABU && now < MapMaBu.TIME_CLOSE_MABU) {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPEN_MMB, "Đại chiến Ma Bư đã mở, "
                                            + "ngươi có muốn tham gia không?",
                                            "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_MMB,
                                            "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                                }

                            }
                        } catch (Exception ex) {
                            Logger.error("Lỗi mở menu osin");
                        }

                    } else if (this.mapId >= 114 && this.mapId < 120 && this.mapId != 116) {
                        if (player.fightMabu.pointMabu >= player.fightMabu.POINT_MAX) {
                            this.createOtherMenu(player, ConstNpc.GO_UPSTAIRS_MENU, "Ta có thể giúp gì cho ngươi ?",
                                    "Lên Tầng!", "Quay về", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                    "Quay về", "Từ chối");
                        }
                    } else if (this.mapId == 120) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    } else {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                                    break;
                            }
                        }
                    } else if (this.mapId == 154) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 155, -1, 111, 792);
                                    break;
                            }
                        }
                    } else if (this.mapId == 155) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                            }
                        }
                    } else if (this.mapId == 52) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.MENU_REWARD_MMB:
                                break;
                            case ConstNpc.MENU_OPEN_MMB:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                                } else if (select == 1) {
//                                    if (!player.getSession().actived) {
//                                        Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
//                                    } else
                                    ChangeMapService.gI().changeMap(player, 114, -1, 318, 336);
                                }
                                break;
                            case ConstNpc.MENU_NOT_OPEN_BDW:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                                }
                                break;
                        }
                    } else if (this.mapId >= 114 && this.mapId < 120 && this.mapId != 116) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.GO_UPSTAIRS_MENU) {
                            if (select == 0) {
                                player.fightMabu.clear();
                                ChangeMapService.gI().changeMap(player, this.map.mapIdNextMabu((short) this.mapId), -1, this.cx, this.cy);
                            } else if (select == 1) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        } else {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        }
                    } else if (this.mapId == 120) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc docNhan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.clan == null) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chỉ tiếp các bang hội, miễn tiếp khách vãng lai", "Đóng");
                        return;
                    }
                    if (player.clan.doanhTrai_haveGone) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ta đã thả ngọc rồng ở tất cả các map,mau đi nhặt đi. Hẹn ngươi quay lại vào ngày mai", "OK");
                        return;
                    }

                    boolean flag = true;
                    for (Mob mob : player.zone.mobs) {
                        if (!mob.isDie()) {
                            flag = false;
                        }
                    }
                    for (Player boss : player.zone.getBosses()) {
                        if (!boss.isDie()) {
                            flag = false;
                        }
                    }

                    if (flag) {
                        player.clan.doanhTrai_haveGone = true;
                        player.clan.doanhTrai.setLastTimeOpen(System.currentTimeMillis() + 290_000);
                        player.clan.doanhTrai.DropNgocRong();
                        for (Player pl : player.clan.membersInGame) {
                            ItemTimeService.gI().sendTextTime(pl, (byte) 0, "Doanh trại độc nhãn sắp kết thúc : ", 300);
                        }
                        player.clan.doanhTrai.timePickDragonBall = true;
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ta đã thả ngọc rồng ở tất cả các map,mau đi nhặt đi. Hẹn ngươi quay lại vào ngày mai", "OK");
                    } else {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hãy tiêu diệt hết quái và boss trong map", "OK");
                    }

                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_JOIN_DOANH_TRAI:
                            if (select == 0) {
                                DoanhTraiService.gI().joinDoanhTrai(player);
                            } else if (select == 2) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                        case ConstNpc.IGNORE_MENU:
                            if (select == 1) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc linhCanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.clan == null) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chỉ tiếp các bang hội, miễn tiếp khách vãng lai", "Đóng");
                        return;
                    }
//                    if (player.clan.getMembers().size() < DoanhTrai.N_PLAYER_CLAN) {
//                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                "Bang hội phải có ít nhất 5 thành viên mới có thể mở", "Đóng");
//                        return;
//                    }
                    if (player.clan.doanhTrai != null) {
                        createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
                                "Bang hội của ngươi đang đánh trại độc nhãn\n"
                                + "Thời gian còn lại là "
                                + TimeUtil.getSecondLeft(player.clan.doanhTrai.getLastTimeOpen(), DoanhTrai.TIME_DOANH_TRAI / 1000)
                                + ". Ngươi có muốn tham gia không?",
                                "Tham gia", "Không", "Hướng\ndẫn\nthêm");
                        return;
                    }
//                    int nPlSameClan = 0;
//                    for (Player pl : player.zone.getPlayers()) {
//                        if (!pl.equals(player) && pl.clan != null
//                                && pl.clan.equals(player.clan) && pl.location.x >= 1285
//                                && pl.location.x <= 1645) {
//                            nPlSameClan++;
//                        }
//                    }
//                    if (nPlSameClan < DoanhTrai.N_PLAYER_MAP) {
//                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                "Ngươi phải có ít nhất " + DoanhTrai.N_PLAYER_MAP + " đồng đội cùng bang đứng gần mới có thể\nvào\n"
//                                + "tuy nhiên ta khuyên ngươi nên đi cùng với 3-4 người để khỏi chết.\n"
//                                + "Hahaha.", "OK", "Hướng\ndẫn\nthêm");
//                        return;
//                    }
//                    if (player.clanMember.getNumDateFromJoinTimeToToday() < 1) {
//                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                "Doanh trại chỉ cho phép những người ở trong bang trên 1 ngày. Hẹn ngươi quay lại vào lúc khác",
//                                "OK", "Hướng\ndẫn\nthêm");
//                        return;
//                    }

                    if (!player.clan.doanhTrai_haveGone) {
                        player.clan.doanhTrai_haveGone = (new java.sql.Date(player.clan.doanhTrai_lastTimeOpen)).getDay() == (new java.sql.Date(System.currentTimeMillis())).getDay();
                    }

                    if (player.clan.doanhTrai_haveGone) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Bang hội của ngươi đã đi trại lúc " + TimeUtil.formatTime(player.clan.doanhTrai_lastTimeOpen, "HH:mm:ss") + " hôm nay. Người mở\n"
                                + "(" + player.clan.doanhTrai_playerOpen + "). Hẹn ngươi quay lại vào ngày mai", "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
                            "Hôm nay bang hội của ngươi chưa vào trại lần nào. Ngươi có muốn vào\n"
                            + "không?\nĐể vào, ta khuyên ngươi nên có 3-4 người cùng bang đi cùng",
                            "Vào\n(miễn phí)", "Không", "Hướng\ndẫn\nthêm");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_JOIN_DOANH_TRAI:
                            if (select == 0) {
                                DoanhTraiService.gI().joinDoanhTrai(player);
                            } else if (select == 2) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                        case ConstNpc.IGNORE_MENU:
                            if (select == 1) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                    }
                }
            }
        };
    }

    private static Npc popo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.getSession().is_gift_box) {
//                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chào con, con muốn ta giúp gì nào?", "Giải tán bang hội", "Nhận quà\nđền bù");
                    } else {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Thượng Đế vừa phát hiện 1 loại khí đang âm thầm\nhủy diệt mọi mầm sống trên Trái Đất,\nnó được gọi là Destron Gas.\nTa sẽ đưa các cậu đến nơi ấy, các cậu sẵn sàng chưa?", "Thông tin chi tiết", "Top 100 bang hội", "OK", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                return;
                            case 1:
                                return;
                            case 2:
                                if (player.clan != null) {
                                    if (player.clan.KhiGaHuyDiet != null) {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPENED_KGHD,
                                                "Bang hội của con đang đi khí ga hủy diệt cấp độ "
                                                + player.clan.KhiGaHuyDiet.level + "\nCon có muốn đi theo không?",
                                                "Đồng ý", "Từ chối");
                                    } else {

                                        this.createOtherMenu(player, ConstNpc.MENU_OPEN_KGHD,
                                                "Đây là khí ga hủy diệt \nCác con cứ yên tâm lên đường\n"
                                                + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                                "Chọn\ncấp độ", "Từ chối");
                                    }
                                } else {
                                    this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                                }
                                break;
                            case 3:
                                return;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_KGHD) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= KhiGasHuyDiet.POWER_CAN_GO_TO_KGHD) {
                                    ChangeMapService.gI().goToKGHD(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(KhiGasHuyDiet.POWER_CAN_GO_TO_KGHD));
                                }
                                break;

                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_KGHD) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= KhiGasHuyDiet.POWER_CAN_GO_TO_KGHD) {
                                    Input.gI().createFormChooseLevelKGHD(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(KhiGasHuyDiet.POWER_CAN_GO_TO_KGHD));
                                }
                                break;
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_KGHD) {
                        switch (select) {
                            case 0:
                                KhiGasHuyDietService.gI().openKhiGaHuyDiet(player, Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                break;
                        }
                    }
                }
            }
        ;
    }

    ;
    }

    public static Npc quaTrung(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            private final int COST_AP_TRUNG_NHANH = 1000000000;

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == (21 + player.gender)) {
                        player.mabuEgg.sendMabuEgg();
                        if (player.mabuEgg.getSecondDone() != 0) {
                            this.createOtherMenu(player, ConstNpc.CAN_NOT_OPEN_EGG, "Burk Burk...",
                                    "Hủy bỏ\ntrứng", "Ấp nhanh\n" + Util.numberToMoney(COST_AP_TRUNG_NHANH) + " vàng", "Đóng");
                        } else {
                            this.createOtherMenu(player, ConstNpc.CAN_OPEN_EGG, "Burk Burk...", "Nở", "Hủy bỏ\ntrứng", "Đóng");
                        }
                    }
                    if (this.mapId == 154) {
                        player.billEgg.sendBillEgg();
                        if (player.billEgg.getSecondDone() != 0) {
                            this.createOtherMenu(player, ConstNpc.CAN_NOT_OPEN_EGG, "Burk Burk...",
                                    "Hủy bỏ\ntrứng", "Ấp nhanh\n" + Util.numberToMoney(COST_AP_TRUNG_NHANH) + " vàng", "Đóng");
                        } else {
                            this.createOtherMenu(player, ConstNpc.CAN_OPEN_EGG, "Burk Burk...", "Nở", "Hủy bỏ\ntrứng", "Đóng");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == (21 + player.gender)) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.CAN_NOT_OPEN_EGG:
                                if (select == 0) {
                                    this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                            "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?", "Đồng ý", "Từ chối");
                                } else if (select == 1) {
                                    if (player.inventory.gold >= COST_AP_TRUNG_NHANH) {
                                        player.inventory.gold -= COST_AP_TRUNG_NHANH;
                                        player.mabuEgg.timeDone = 0;
                                        Service.gI().sendMoney(player);
                                        player.mabuEgg.sendMabuEgg();
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "Bạn không đủ vàng để thực hiện, còn thiếu "
                                                + Util.numberToMoney((COST_AP_TRUNG_NHANH - player.inventory.gold)) + " vàng");
                                    }
                                }
                                break;
                            case ConstNpc.CAN_OPEN_EGG:
                                switch (select) {
                                    case 0:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_OPEN_EGG,
                                                "Bạn có chắc chắn cho trứng nở?\n"
                                                + "Đệ tử của bạn sẽ được thay thế bằng đệ Mabư",
                                                "Đệ mabư\nTrái Đất", "Đệ mabư\nNamếc", "Đệ mabư\nXayda", "Từ chối");
                                        break;
                                    case 1:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                                "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?", "Đồng ý", "Từ chối");
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_OPEN_EGG:
                                switch (select) {
                                    case 0:
                                        player.mabuEgg.openEgg(ConstPlayer.TRAI_DAT);
                                        break;
                                    case 1:
                                        player.mabuEgg.openEgg(ConstPlayer.NAMEC);
                                        break;
                                    case 2:
                                        player.mabuEgg.openEgg(ConstPlayer.XAYDA);
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_DESTROY_EGG:
                                if (select == 0) {
                                    player.mabuEgg.destroyEgg();
                                }
                                break;
                        }
                    }
                    if (this.mapId == 154) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.CAN_NOT_OPEN_BILL:
                                if (select == 0) {
                                    this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_BILL,
                                            "Bạn có chắc chắn muốn hủy bỏ trứng Bill?", "Đồng ý", "Từ chối");
                                } else if (select == 1) {
                                    if (player.inventory.gold >= COST_AP_TRUNG_NHANH) {
                                        player.inventory.gold -= COST_AP_TRUNG_NHANH;
                                        player.billEgg.timeDone = 0;
                                        Service.gI().sendMoney(player);
                                        player.billEgg.sendBillEgg();
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "Bạn không đủ vàng để thực hiện, còn thiếu "
                                                + Util.numberToMoney((COST_AP_TRUNG_NHANH - player.inventory.gold)) + " vàng");
                                    }
                                }
                                break;
                            case ConstNpc.CAN_OPEN_EGG:
                                switch (select) {
                                    case 0:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_OPEN_BILL,
                                                "Bạn có chắc chắn cho trứng nở?\n"
                                                + "Đệ tử của bạn sẽ được thay thế bằng đệ Bill",
                                                "Đệ Bill\nTrái Đất", "Đệ Bill\nNamếc", "Đệ Bill\nXayda", "Từ chối");
                                        break;
                                    case 1:
                                        this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_BILL,
                                                "Bạn có chắc chắn muốn hủy bỏ trứng Bill?", "Đồng ý", "Từ chối");
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_OPEN_BILL:
                                switch (select) {
                                    case 0:
                                        player.billEgg.openEgg(ConstPlayer.TRAI_DAT);
                                        break;
                                    case 1:
                                        player.billEgg.openEgg(ConstPlayer.NAMEC);
                                        break;
                                    case 2:
                                        player.billEgg.openEgg(ConstPlayer.XAYDA);
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case ConstNpc.CONFIRM_DESTROY_BILL:
                                if (select == 0) {
                                    player.billEgg.destroyEgg();
                                }
                                break;
                        }
                    }

                }
            }
        };
    }

    public static Npc quocVuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "Con muốn nâng giới hạn sức mạnh cho bản thân hay đệ tử?",
                        "Bản thân", "Đệ tử", "Từ chối");
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                    this.createOtherMenu(player, ConstNpc.OPEN_POWER_MYSEFT,
                                            "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của bản thân lên "
                                            + Util.numberToMoney(player.nPoint.getPowerNextLimit()),
                                            "Nâng\ngiới hạn\nsức mạnh",
                                            "Nâng ngay\n" + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " vàng", "Đóng");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Sức mạnh của con đã đạt tới giới hạn",
                                            "Đóng");
                                }
                                break;
                            case 1:
                                if (player.pet != null) {
                                    if (player.pet.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                        this.createOtherMenu(player, ConstNpc.OPEN_POWER_PET,
                                                "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của đệ tử lên "
                                                + Util.numberToMoney(player.pet.nPoint.getPowerNextLimit()),
                                                "Nâng ngay\n" + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " vàng", "Đóng");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Sức mạnh của đệ con đã đạt tới giới hạn",
                                                "Đóng");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Không thể thực hiện");
                                }
                                //giới hạn đệ tử
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_MYSEFT) {
                        switch (select) {
                            case 0:
                                OpenPowerService.gI().openPowerBasic(player);
                                break;
                            case 1:
                                if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                    if (OpenPowerService.gI().openPowerSpeed(player)) {
                                        player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                        Service.gI().sendMoney(player);
                                    }
                                } else {
                                    Service.gI().sendThongBao(player,
                                            "Bạn không đủ vàng để mở, còn thiếu "
                                            + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER - player.inventory.gold)) + " vàng");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_PET) {
                        if (select == 0) {
                            if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                if (OpenPowerService.gI().openPowerSpeed(player.pet)) {
                                    player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                    Service.gI().sendMoney(player);
                                }
                            } else {
                                Service.gI().sendThongBao(player,
                                        "Bạn không đủ vàng để mở, còn thiếu "
                                        + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER - player.inventory.gold)) + " vàng");
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc bulmaTL(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 102) {
                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu bé muốn mua gì nào?", "Cửa hàng", "Đóng");
                        }
                    } else if (this.mapId == 104) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Kính chào Ngài Linh thú sư!", "Cửa hàng", "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 102) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_FUTURE", true);
                            }
                        }
                    } else if (this.mapId == 104) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_LINHTHU", true);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc rongOmega(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    BlackBallWar.gI().setTime();
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        try {
                            long now = System.currentTimeMillis();
                            System.out.println("check time now : " + now + " check time ppen : " + BlackBallWar.TIME_OPEN );
                            if (now > BlackBallWar.TIME_OPEN && now < BlackBallWar.TIME_CLOSE) {
                                this.createOtherMenu(player, ConstNpc.MENU_OPEN_BDW, "Đường đến với ngọc rồng sao đen đã mở, "
                                        + "ngươi có muốn tham gia không?",
                                        "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                            } else {
                                String[] optionRewards = new String[7];
                                int index = 0;
                                for (int i = 0; i < 7; i++) {
                                    if (player.rewardBlackBall.timeOutOfDateReward[i] > System.currentTimeMillis()) {
                                        String quantily = player.rewardBlackBall.quantilyBlackBall[i] > 1 ? "x" + player.rewardBlackBall.quantilyBlackBall[i] + " " : "";
                                        optionRewards[index] = quantily + (i + 1) + " sao";
                                        index++;
                                    }
                                }
                                if (index != 0) {
                                    String[] options = new String[index + 1];
                                    for (int i = 0; i < index; i++) {
                                        options[i] = optionRewards[i];
                                    }
                                    options[options.length - 1] = "Từ chối";
                                    this.createOtherMenu(player, ConstNpc.MENU_REWARD_BDW, "Ngươi có một vài phần thưởng ngọc "
                                            + "rồng sao đen đây!",
                                            options);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_BDW,
                                            "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                                }
                            }
                        } catch (Exception ex) {
                            Logger.error("Lỗi mở menu rồng Omega");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_REWARD_BDW:
                            player.rewardBlackBall.getRewardSelect((byte) select);
                            break;
                        case ConstNpc.MENU_OPEN_BDW:
                            if (select == 0) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                            } else if (select == 1) {
//                                if (!player.getSession().actived) {
//                                    Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
//
//                                } else
                                player.iDMark.setTypeChangeMap(ConstMap.CHANGE_BLACK_BALL);
                                ChangeMapService.gI().openChangeMapTab(player);
                            }
                            break;
                        case ConstNpc.MENU_NOT_OPEN_BDW:
                            if (select == 0) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                            }
                            break;
                    }
                }
            }

        };
    }

    public static Npc rong1_to_7s(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isHoldBlackBall()) {
                        this.createOtherMenu(player, ConstNpc.MENU_PHU_HP, "Ta có thể giúp gì cho ngươi?", "Phù hộ", "Từ chối");
                    } else {
                        if (BossManager.gI().existBossOnPlayer(player)
                                || player.zone.items.stream().anyMatch(itemMap -> ItemMapService.gI().isBlackBall(itemMap.itemTemplate.id))
                                || player.zone.getPlayers().stream().anyMatch(p -> p.iDMark.isHoldBlackBall())) {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME, "Ta có thể giúp gì cho ngươi?", "Về nhà", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME, "Ta có thể giúp gì cho ngươi?", "Về nhà", "Từ chối", "Gọi BOSS");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHU_HP) {
                        if (select == 0) {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_PHU_HP,
                                    "Ta sẽ giúp ngươi tăng HP lên mức kinh hoàng, ngươi chọn đi",
                                    "x3 HP\n" + Util.numberToMoney(BlackBallWar.COST_X3) + " vàng",
                                    "x5 HP\n" + Util.numberToMoney(BlackBallWar.COST_X5) + " vàng",
                                    "x7 HP\n" + Util.numberToMoney(BlackBallWar.COST_X7) + " vàng",
                                    "Từ chối"
                            );
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_GO_HOME) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                        } else if (select == 2) {
                            BossManager.gI().callBoss(player, mapId);
                        } else if (select == 1) {
                            this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PHU_HP) {
                        if (player.effectSkin.xHPKI > 1) {
                            Service.gI().sendThongBao(player, "Bạn đã được phù hộ rồi!");
                            return;
                        }
                        switch (select) {
                            case 0:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X3);
                                break;
                            case 1:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X5);
                                break;
                            case 2:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X7);
                                break;
                            case 3:
                                this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc npcThienSu64(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 14) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 80 tỷ sức mạnh "
                            + "\n 3. chi phí vào cổng  50 triệu vàng", "Tới ngay", "Từ chối");
                }
                if (this.mapId == 7) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 80 tỷ sức mạnh "
                            + "\n 3. chi phí vào cổng  50 triệu vàng", "Tới ngay", "Từ chối");
                }
                if (this.mapId == 0) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 80 tỷ sức mạnh "
                            + "\n 3. chi phí vào cổng  50 triệu vàng", "Tới ngay", "Từ chối");
                }
                if (this.mapId == 146) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 147) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 148) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 48) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đã tìm đủ nguyên liệu cho tôi chưa?\n Tôi sẽ giúp cậu mạnh lên kha khá đấy!", "Hướng Dẫn",
                            "Đổi SKH VIP", "Từ Chối");
                }
            }

            //if (player.inventory.gold < 500000000) {
//                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
//                return;
//            }
            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu() && this.mapId == 7) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 80000000000L && player.inventory.gold > COST_HD) {
                                player.inventory.gold -= COST_HD;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 146, -1, 168);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 14) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 80000000000L && player.inventory.gold > COST_HD) {
                                player.inventory.gold -= COST_HD;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 148, -1, 168);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 0) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 80000000000L && player.inventory.gold > COST_HD) {
                                player.inventory.gold -= COST_HD;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 147, -1, 168);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 147) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 450);
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 148) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 14, -1, 450);
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 146) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 7, -1, 450);
                        }
                        if (select == 1) {
                        }

                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 48) {
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOI_SKH_VIP);
                        }
                        if (select == 1) {
                            CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_SKH_VIP);
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_DOI_SKH_VIP) {
                        if (select == 0) {
                            CombineServiceNew.gI().startCombine(player);
                        }
                    }
                }
            }

        };
    }

    public static Npc whis(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 154) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Thử đánh với ta xem nào.\nNgươi còn 1 lượt cơ mà.",
                            "Nói chuyện", "Học tuyệt kỹ", "Phân rã đồ thần linh","Đổi thăng tinh thạch", "Từ chối");

                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu() && this.mapId == 154) {
                        switch (select) {
                            case 0:
                                this.createOtherMenu(player, 5, "Ta sẽ giúp ngươi chế tạo trang bị thiên sứ", "Chế tạo", "Từ chối");
                                break;
                            case 1:
                                Item BiKiepTuyetKy = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1320);
                                if (BiKiepTuyetKy != null) {
                                    if (player.gender == 0) {
                                        this.createOtherMenu(player, 6, "|1|Ta sẽ dạy ngươi tuyệt kỹ Super kamejoko\n" + "|7|Bí kiếp tuyệt kỹ: " + BiKiepTuyetKy.quantity + "/9999\n" + "|2|Giá vàng: 10.000.000\n" + "|2|Giá ngọc: 99",
                                                "Đồng ý", "Từ chối");
                                    }
                                    if (player.gender == 1) {
                                        this.createOtherMenu(player, 6, "|1|Ta sẽ dạy ngươi tuyệt kỹ Ma phông ba\n" + "|7|Bí kiếp tuyệt kỹ: " + BiKiepTuyetKy.quantity + "/9999\n" + "|2|Giá vàng: 10.000.000\n" + "|2|Giá ngọc: 99",
                                                "Đồng ý", "Từ chối");
                                    }
                                    if (player.gender == 2) {
                                        this.createOtherMenu(player, 6, "|1|Ta sẽ dạy ngươi tuyệt kỹ "
                                                + "đíc chưởng liên hoàn\n" + "|7|Bí kiếp tuyệt kỹ: " + BiKiepTuyetKy.quantity + "/9999\n" + "|2|Giá vàng: 10.000.000\n" + "|2|Giá ngọc: 99",
                                                "Đồng ý", "Từ chối");
                                    }
                                } else {
                                    this.npcChat(player, "Hãy tìm bí kíp rồi quay lại gặp ta!");
                                }
                                break;
                            case 2:
                                 CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHAN_RA_DO_THAN_LINH);
                                break;
                            case 3:
                                this.createOtherMenu(player, 551, "Ta sẽ đổi đá ma thuật giúp ngươi", "đổi x1","đổi x10", "Từ chối");
                              
                                break;
                        }
                    } 
                    else if (player.iDMark.getIndexMenu() == 5) {
                        switch (select) {
                            // case 0:
                            //    ShopServiceNew.gI().opendShop(player, "THIEN_SU", false);
                            //   break;
                            case 0:
                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_TRANG_BI_TS);
                                break;
                        }
                        //   } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DAP_DO) {
                        //     if (select == 0) {
                        //       CombineServiceNew.gI().startCombine(player);
                    }
                    else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.PHAN_RA_DO_THAN_LINH:
                                case CombineServiceNew.NANG_CAP_DO_TS:
                                
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHAN_RA_DO_THAN_LINH) {
                            if (select == 0) {                              
                                CombineServiceNew.gI().startCombine(player);
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_CAP_DO_TS) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                         }

                        }
                        else if(player.iDMark.getIndexMenu() == 551){
                            if (select == 0) {       
                               int COST_DOI_THANG_TINH_THACH = 100000000;
                               if(player.inventory.gold < COST_DOI_THANG_TINH_THACH){
                                        Service.gI().sendThongBao(player, "không đủ vàng thức hiện");
                                        return;
                                    }
                                    Item ttt = InventoryServiceNew.gI().findItemBag(player, 2030);                                 
                                    if(ttt == null){
                                        Service.gI().sendThongBao(player,"Không  có đá ma thuật");
                                        return;
                                    }
                                    
                                    if(ttt.quantity < 10){
                                        Service.gI().sendThongBao(player,"Không đủ số lượng đá ma thuật");
                                        return;
                                    }
                                    

                                    player.inventory.gold -= COST_DOI_THANG_TINH_THACH;
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, ttt, 10);
                                    Item itemDMT = ItemService.gI().createNewItem((short) 2031);
                                    InventoryServiceNew.gI().addItemBag(player, itemDMT);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendMoney(player);
                                    Service.gI().sendThongBao(player, "Bạn nhận đc thăng tinh thạch");
                                    return;
                            } else if (select == 1){
                               int COST_DOI_THANG_TINH_THACH = 1000000000;
                               if(player.inventory.gold < COST_DOI_THANG_TINH_THACH){
                                        Service.gI().sendThongBao(player, "không đủ vàng thức hiện");
                                        return;
                                    }
                                    Item ttt = InventoryServiceNew.gI().findItemBag(player, 2030);                                 
                                     if(ttt == null){
                                        Service.gI().sendThongBao(player,"Không  có đá ma thuật");
                                        return;
                                    }
                                    
                                    if(ttt.quantity < 100){
                                        Service.gI().sendThongBao(player,"Không đủ số lượng đá ma thuật");
                                        return;
                                    }

                                    player.inventory.gold -= COST_DOI_THANG_TINH_THACH;
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, ttt, 100);
                                    Item itemDMT = ItemService.gI().createNewItem((short) 2031,10);
                                    InventoryServiceNew.gI().addItemBag(player, itemDMT);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendMoney(player);
                                    Service.gI().sendThongBao(player, "Bạn nhận đc thăng tinh thạch");
                                    return;
                            }
                       }else if (player.iDMark.getIndexMenu() == 6) {
                        switch (select) {
                            case 0:
                                Item sach = InventoryServiceNew.gI().findItemBag(player, 1320);
                                if (sach != null && sach.quantity >= 9999 && player.inventory.gold >= 10000000 && player.inventory.gem > 99 && player.nPoint.power >= 1000000000L) {

                                    if (player.gender == 2) {
                                        SkillService.gI().learSkillSpecial(player, Skill.LIEN_HOAN_CHUONG);
                                    }
                                    if (player.gender == 0) {
                                        SkillService.gI().learSkillSpecial(player, Skill.SUPER_KAME);
                                    }
                                    if (player.gender == 1) {
                                        SkillService.gI().learSkillSpecial(player, Skill.MA_PHONG_BA);
                                    }
                                    InventoryServiceNew.gI().subQuantityItem(player.inventory.itemsBag, sach, 9999);
                                    player.inventory.gold -= 10000000;
                                    player.inventory.gem -= 99;
                                    InventoryServiceNew.gI().sendItemBags(player);
                                } else if (player.nPoint.power < 1000000000L) {
                                    Service.getInstance().sendThongBao(player, "Ngươi không đủ sức mạnh để học tuyệt kỹ");
                                    return;
                                } else if (sach.quantity <= 9999) {
                                    int sosach = 9999 - sach.quantity;
                                    Service.getInstance().sendThongBao(player, "Ngươi còn thiếu " + sosach + " bí kíp nữa.\nHãy tìm đủ rồi đến gặp ta.");
                                    return;
                                } else if (player.inventory.gold <= 10000000) {
                                    Service.getInstance().sendThongBao(player, "Hãy có đủ vàng thì quay lại gặp ta.");
                                    return;
                                } else if (player.inventory.gem <= 99) {
                                    Service.getInstance().sendThongBao(player, "Hãy có đủ ngọc xanh thì quay lại gặp ta.");
                                    return;
                                }

                                break;
                        }
                    }
                }
            }

        };
    }

    public static Npc Achievement(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 47 || this.mapId == 84) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Chào bạn! \btôi có thể giúp bạn làm nhiệm vụ", "Nhiệm vụ\nhàng ngày", "Nhận ngọc\nmiễn phí", "Từ chối");
                    }
//                    if (this.mapId == 47) {
//                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                                "Xin chào, cậu muốn tôi giúp gì?", "Từ chối");
//                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 47 || this.mapId == 84) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (player.playerTask.sideTask.template != null) {
                                        String npcSay = "Nhiệm vụ hiện tại: " + player.playerTask.sideTask.getName() + " ("
                                                + player.playerTask.sideTask.getLevel() + ")"
                                                + "\nHiện tại đã hoàn thành: " + player.playerTask.sideTask.count + "/"
                                                + player.playerTask.sideTask.maxCount + " ("
                                                + player.playerTask.sideTask.getPercentProcess() + "%)\nSố nhiệm vụ còn lại trong ngày: "
                                                + player.playerTask.sideTask.leftTask + "/" + ConstTask.MAX_SIDE_TASK;
                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_PAY_SIDE_TASK,
                                                npcSay, "Trả nhiệm\nvụ", "Hủy nhiệm\nvụ");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK,
                                                "Tôi có vài nhiệm vụ theo cấp bậc, "
                                                + "sức cậu có thể làm được cái nào?",
                                                "Dễ", "Bình thường", "Khó", "Siêu khó", "Địa ngục", "Từ chối");
                                    }
                                    break;
                                case 1:
                                    player.achievement.Show();
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK) {
                            switch (select) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                    TaskService.gI().changeSideTask(player, (byte) select);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PAY_SIDE_TASK) {
                            switch (select) {
                                case 0:
                                    TaskService.gI().paySideTask(player);
                                    break;
                                case 1:
                                    TaskService.gI().removeSideTask(player);
                                    break;
                            }

                        }
                    }
                }
            }
        };
    }

    public static Npc vados(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|2|Ta Vừa Hắc Mắp Xêm Được Tóp Của Toàn Server\b|7|Người Muốn Xem Tóp Gì?",
                            "Tóp Sức Mạnh", "Top Nhiệm Vụ", "Top Nạp", "Top Sức đánh","Shop Top",  "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (this.mapId) {
                        case 5:
                            switch (player.iDMark.getIndexMenu()) {
                                case ConstNpc.BASE_MENU:
                                    if (select == 0) {
                                        Service.gI().showListTop(player, Manager.topSM);
                                        break;
                                    }
                                    if (select == 1) {
                                        Service.gI().showListTop(player, Manager.topNV);
                                        break;
                                    }
                                    if (select == 2) {
                                        Service.gI().showListTop(player, Manager.topNap);
                                        break;
                                    }
                                    if (select == 3) {
                                        Service.gI().showListTop(player, Manager.topSD);
                                        break;

                                    }
                                     if (select == 4) {
                                         ShopServiceNew.gI().opendShop(player, "SHOP_Top", false);
                                        break;

                                    }
                                    break;
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc gokuSSJ_1(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 80) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, tôi có thể giúp gì cho cậu?", "Tới hành tinh\nYardart", "Từ chối");
                    } else if (this.mapId == 131) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, tôi có thể giúp gì cho cậu?", "Quay về", "Từ chối");
                    } else {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 131) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                //đến tay thanh dia
                                ChangeMapService.gI().changeMapBySpaceShip(player, 80, -1, 870);
                            }
                        }
                    } else if (this.mapId == 80) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                //về lanh dia bang hoi
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 131, -1, 938);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }



    public static Npc khidaumoi(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 14) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Bạn muốn nâng cấp khỉ ư?", "Nâng cấp\nkhỉ", "Shop của Khỉ", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 14) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, 1,
                                            "|7|Cần Khỉ Lv1,2,3,4,5,6,7 để nâng cấp lên ct khỉ cấp cao hơn\b|2|Mỗi lần nâng cấp tiếp thì mỗi cấp cần thêm 5 đá ngũ sắc",
                                            "Nâng cấp",
                                            "Từ chối");
                                    break;
                                case 1: //shop
                                    ShopServiceNew.gI().opendShop(player, "KHI", false);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 1) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_KHI);
                                    break;
                                case 1:
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_KHI) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.NANG_CAP_KHI:
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc GhiDanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            String[] menuselect = new String[]{};

            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (this.map.mapId == 52) {
                        if (DaiHoiManager.gI().openDHVT && (System.currentTimeMillis() <= DaiHoiManager.gI().tOpenDHVT)) {
                            String nameDH = DaiHoiManager.gI().nameRoundDHVT();
                            this.createOtherMenu(pl, ConstNpc.MENU_DHVT, "Hiện đang có giải đấu " + nameDH + " bạn có muốn đăng ký không? \nSố người đã đăng ký :" + DaiHoiManager.gI().lstIDPlayers.size(), new String[]{"Giải\n" + nameDH + "\n(" + DaiHoiManager.gI().costRoundDHVT() + ")", "Từ chối", "Đại Hội\nVõ Thuật\nLần thứ\n23", "Giải siêu hạng"});
                        } else {
                            this.createOtherMenu(pl, ConstNpc.BASE_MENU, "Đã hết hạn đăng ký thi đấu, xin vui lòng chờ đến giải sau", new String[]{"Thông tin\bChi tiết", "OK", "Đại Hội\nVõ Thuật\nLần thứ\n23", "Giải siêu hạng\n(thử nghiệm)"});
                        }
                    } else if (this.mapId == 129) {
                        int goldchallenge = pl.goldChallenge;
                        if (pl.levelWoodChest == 0) {
                            menuselect = new String[]{"Thi đấu\n" + Util.numberToMoney(goldchallenge) + " vàng", "Về\nĐại Hội\nVõ Thuật"};
                        } else {
                            menuselect = new String[]{"Thi đấu\n" + Util.numberToMoney(goldchallenge) + " vàng", "Nhận thưởng\nRương cấp\n" + pl.levelWoodChest, "Về\nĐại Hội\nVõ Thuật"};
                        }
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU, "Đại hội võ thuật lần thứ 23\nDiễn ra bất kể ngày đêm,ngày nghỉ ngày lễ\nPhần thưởng vô cùng quý giá\nNhanh chóng tham gia nào", menuselect, "Từ chối");

                    } else {
                        super.openBaseMenu(pl);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.map.mapId == 52) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Lịch thi đấu trong ngày\bGiải Nhi đồng: 8,13,18h\bGiải Siêu cấp 1: 9,14,19h\bGiải Siêu cấp 2: 10,15,20h\bGiải Siêu cấp 3: 11,16,21h\bGiải Ngoại hạng: 12,17,22,23h\nGiải thưởng khi thắng mỗi vòng\bGiải Nhi đồng: 2 ngọc\bGiải Siêu cấp 1: 4 ngọc\bGiải Siêu cấp 2: 6 ngọc\bGiải Siêu cấp 3: 8 ngọc\bGiải Ngoại hạng: 10.000 vàng\bVô địch: 5 viên đá nâng cấp\nVui lòng đến đúng giờ để đăng ký thi đấu");
                                    break;
                                case 1:
                                    Service.getInstance().sendThongBaoFromAdmin(player, "Nhớ Đến Đúng Giờ nhé");
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 129, player.location.x, 360);
                                    break;
                                case 3:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 113, player.location.x, 360);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DHVT) {
                            switch (select) {
                                case 0:
//                                    if (DaiHoiService.gI().canRegisDHVT(player.nPoint.power)) {
                                    if (DaiHoiManager.gI().lstIDPlayers.size() < 256) {
                                        if (DaiHoiManager.gI().typeDHVT == (byte) 5 && player.inventory.gold >= 10000) {
                                            if (DaiHoiManager.gI().isAssignDHVT(player.id)) {
                                                Service.getInstance().sendThongBao(player, "Bạn đã đăng ký tham gia đại hội võ thuật rồi");
                                            } else {
                                                player.inventory.gold -= 10000;
                                                Service.getInstance().sendMoney(player);
                                                Service.getInstance().sendThongBao(player, "Bạn đã đăng ký thành công, nhớ có mặt tại đây trước giờ thi đấu");
                                                DaiHoiManager.gI().lstIDPlayers.add(player.id);
                                            }
                                        } else if (DaiHoiManager.gI().typeDHVT > (byte) 0 && DaiHoiManager.gI().typeDHVT < (byte) 5 && player.inventory.gem >= (int) (2 * DaiHoiManager.gI().typeDHVT)) {
                                            if (DaiHoiManager.gI().isAssignDHVT(player.id)) {
                                                Service.getInstance().sendThongBao(player, "Bạn đã đăng ký tham gia đại hội võ thuật rồi");
                                            } else {
                                                player.inventory.gem -= (int) (2 * DaiHoiManager.gI().typeDHVT);
                                                Service.getInstance().sendMoney(player);
                                                Service.getInstance().sendThongBao(player, "Bạn đã đăng ký thành công, nhớ có mặt tại đây trước giờ thi đấu");
                                                DaiHoiManager.gI().lstIDPlayers.add(player.id);
                                            }
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng ngọc để đăng ký thi đấu");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hiện tại đã đạt tới số lượng người đăng ký tối đa, xin hãy chờ đến giải sau");
                                    }

//                                    } else {
//                                        Service.getInstance().sendThongBao(player, "Bạn không đủ điều kiện tham gia giải này, hãy quay lại vào giải phù hợp");
//                                    }
                                    break;
                                case 1:
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 129, player.location.x, 360);
                                    break;
                            }
                        }
                    } else if (this.mapId == 129) {
                        int goldchallenge = player.goldChallenge;
                        if (player.levelWoodChest == 0) {
                            switch (select) {
                                case 0:
                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) {
                                        if (player.inventory.gold >= goldchallenge) {
                                            MartialCongressService.gI().startChallenge(player);
                                            player.inventory.gold -= (goldchallenge);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.goldChallenge += 2000000;
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu " + Util.numberToMoney(goldchallenge - player.inventory.gold) + " vàng");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
                                    break;
                            }
                        } else {
                            switch (select) {
                                case 0:
                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) {
                                        if (player.inventory.gold >= goldchallenge) {
                                            MartialCongressService.gI().startChallenge(player);
                                            player.inventory.gold -= (goldchallenge);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.goldChallenge += 2000000;
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu " + Util.numberToMoney(goldchallenge - player.inventory.gold) + " vàng");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
                                case 1:
                                    if (!player.receivedWoodChest) {
                                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                            Item it = ItemService.gI().createNewItem((short) 570);
                                            it.itemOptions.add(new Item.ItemOption(72, player.levelWoodChest));
                                            it.itemOptions.add(new Item.ItemOption(30, 0));
                                            it.createTime = System.currentTimeMillis();
                                            InventoryServiceNew.gI().addItemBag(player, it);
                                            InventoryServiceNew.gI().sendItemBags(player);

                                            player.receivedWoodChest = true;
                                            player.levelWoodChest = 0;
                                            Service.getInstance().sendThongBao(player, "Bạn nhận được rương gỗ");
                                        } else {
                                            this.npcChat(player, "Hành trang đã đầy");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Mỗi ngày chỉ có thể nhận rương báu 1 lần");
                                    }
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc vodai(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 44) {
                        this.createOtherMenu(player, 0,
                                "Éc éc Bạn muốn gì ở tôi :3?", "Đến Võ đài");
                    }
                    if (this.mapId == 112) {
                        this.createOtherMenu(player, 0,
                                "Bạn đang còn : " + player.pointPvp + " điểm PvP Point", "Về đảo Kame", "Đổi Cải trang sự kiên", "Top PVP");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    if (player.getSession().player.nPoint.power >= 10000000000L) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 112, -1, 495);
                                        Service.gI().changeFlag(player, Util.nextInt(8));
                                    } else {
                                        this.npcChat(player, "Bạn cần 10 tỷ sức mạnh mới có thể vào");
                                    }
                                    break; // qua vo dai
                            }
                        }
                    }

                    if (this.mapId == 112) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 319);
                                    break; // ve dao kame
                                case 1:  // 
                                    this.createOtherMenu(player, 1,
                                            "Bạn có muốn đổi 500 điểm PVP lấy \n|6|Cải trang Goku SSJ3\n với chỉ số random từ 20 > 30% \n ", "Ok", "Không");
                                    // bat menu doi item
                                    break;

                                case 2:  // 
                                    Service.gI().showListTop(player, Manager.topPVP);
                                    // mo top pvp
                                    break;

                            }
                        }
                        if (player.iDMark.getIndexMenu() == 1) { // action doi item
                            switch (select) {
                                case 0: // trade
                                    if (player.pointPvp >= 500) {
                                        player.pointPvp -= 500;
                                        Item item = ItemService.gI().createNewItem((short) (1227)); // 49
                                        item.itemOptions.add(new Item.ItemOption(49, Util.nextInt(20, 30)));
                                        item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(20, 30)));
                                        item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(20, 30)));
                                        item.itemOptions.add(new Item.ItemOption(207, 0));
                                        item.itemOptions.add(new Item.ItemOption(33, 0));
//                                      
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        Service.gI().sendThongBao(player, "Chúc Mừng Bạn Đổi Cải Trang Thành Công !");
                                    } else {
                                        Service.gI().sendThongBao(player, "Không đủ điểm bạn còn " + (500 - player.pointPvp) + " Điểm nữa");
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc monaito(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 7) {
                        this.createOtherMenu(player, 0,
                                "Chào bạn tôi sẽ đưa bạn đến hành tinh Cereal?", "Đồng ý", "Từ chối");
                    }
                    if (this.mapId == 170) {
                        this.createOtherMenu(player, 0,
                                "Ta ở đây để đưa con về", "Về Làng Mori", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 7) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 170, -1, 264);
                                    break; // den hanh tinh cereal
                            }
                        }
                    }
                    if (this.mapId == 170) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 7, -1, 432);
                                    break; // quay ve

                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc Luffy(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, 0,
                                "|7|Chào bạn tôi sẽ đưa bạn đến map sự kiện,\n Nơi đây chiếm giữ Mảnh Vỏ sò sự kiện\n Hãy Thu Thập Và Đến Đây Đổi  ?", "Map SK", "SHOPLUFFY","SHOPHAOQUANG",
                                "Từ chối");
                    }
                    if (this.mapId == 174) {
                        this.createOtherMenu(player, 0,
                                "Ta ở đây để đưa con về", "Về Kame", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 174, -1, 277);
                                    break;
                                case 1: //shop
                                    ShopServiceNew.gI().opendShop(player, "SHOPLUFFY", false);
                                    break;
                                case 2: //shop
                                    ShopServiceNew.gI().opendShop(player, "SHOPHAOQUANG", false);
                                    break;     
                            }
                        }
                    } else if (this.mapId == 174) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 432);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc thanquy(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 14) {
                        this.createOtherMenu(player, 0,
                                "|7|Chào bạn tôi sẽ đưa bạn đến map sự kiện,\n Nơi đây chiếm giữ Mảnh Vật Phẩm sự kiện\n Hãy Thu Thập Và Đến Đây Đổi  ?", "Map SK", "Shop Boss",
                                "Từ chối");
                    }
                    if (this.mapId == 195) {
                        this.createOtherMenu(player, 0,
                                "Ta ở đây để đưa con về", "Về Kame", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 14) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 195, -1, 277);
                                    break;
                                case 1: //shop
                                    ShopServiceNew.gI().opendShop(player, "SHOPTELEBOSS", false);
                                    break;
                            }
                        }
                    } else if (this.mapId == 195) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 432);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }
    
    public static Npc Berry(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 7) {
                        this.createOtherMenu(player, 0,
                                "|7|Cửa Hàng Thú Cưng Đi Theo\n Hoặc Đến Map Bóng Tối ZuZu ?", "Bóng\nTối ZuZu", "Shop Pet",
                                "Từ chối");
                    }
                    if (this.mapId == 193) {
                        this.createOtherMenu(player, 0,
                                "Quay Trở Về", "Về Kame", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 7) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 193, -1, 153);
                                    break;
                                case 1: //shop
                                    ShopServiceNew.gI().opendShop(player, "SHOPBERRY", false);
                                    break;

                            }
                        }
                    }
                    if (this.mapId == 193) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 600);
                                    break; // quay ve

                            }
                        }
                    }
                }
            }
        };
    }

     public static Npc barock(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Tôi là Barock, bạn muốn tôi giúp đỡ gì cho bạn?",
                                "Cửa hàng" , "Chân mệnh","Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (!canOpenNpc(player)) {
                    return;
                }
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0:
                            ShopServiceNew.gI().opendShop(player, "RUBY", false);
                            break;
                        case 1: //nâng cấp Chân mệnh
                                    this.createOtherMenu(player, 5701,
                                            "|7|CHÂN MỆNH"
                                            + "\n\n|5|Bạn Được Hiếu Tặng Free  Chân Mệnh cấp 1 Bú Nhanh"
                                            + "\n|3| Lưu ý: Chỉ được nhận Chân mệnh 1 lần (Hành trang chỉ tồn tại 1 Chân mệnh)"
                                            + "\nNếu đã có Chân mệnh. Ta sẽ giúp ngươi nâng cấp bậc lên với các dòng chỉ số cao hơn",
                                            "Nhận Chân mệnh", "Nâng cấp Chân mệnh");
                                    break;
                    }
                 } else if (player.iDMark.getIndexMenu() == 5701) {
                            switch (select) {
                                case 0:
                                    for (int i = 0; i < 9; i++) {
                                        Item findItemBag = InventoryServiceNew.gI().findItemBag(player, 2055 + i);
                                        Item findItemBody = InventoryServiceNew.gI().findItemBody(player, 2055 + i);
                                        if (findItemBag != null || findItemBody != null) {
                                            Service.gI().sendThongBao(player, "|7|Ngươi đã có Chân mệnh rồi mà");
                                            return;
                                        }
                                    }
                                    if (player.inventory.event >= 0) {
                                        player.inventory.event -= 0;
                                        Item chanmenh = ItemService.gI().createNewItem((short) 2055);
                                        chanmenh.itemOptions.add(new Item.ItemOption(50, 5));
                                        chanmenh.itemOptions.add(new Item.ItemOption(77, 5));
                                        chanmenh.itemOptions.add(new Item.ItemOption(103, 5));
                                        chanmenh.itemOptions.add(new Item.ItemOption(30, 1));
                                        InventoryServiceNew.gI().addItemBag(player, chanmenh);
                                        Service.getInstance().sendMoney(player);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "|1|Bạn nhận được Chân mệnh Cấp 1");
                                    } else {
                                        this.npcChat(player, "|1|Kiểm Tra Hành Trang Của Ngươi Đi");
                                    }
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_CHAN_MENH);
                                    break;
                                case 2:
                                    ShopServiceNew.gI().opendShop(player, "CHAN MENH", true);
                                    break;
                            }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.NANG_CAP_CHAN_MENH:
                                    if (select == 0) {
                                       
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }
                        
                         
                }
                else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_CAP_CHAN_MENH) {
                       if (select == 0) {
                            CombineServiceNew.gI().startCombine(player);
                    }                   
                }
            }
        };
    }

    public static Npc CHIHANG(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Tôi là Chị Hằng, Ngươi Muốn?",
                                "Cửa hàng\nTrung Thu", "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (!canOpenNpc(player)) {
                    return;
                }
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0:
                            ShopServiceNew.gI().opendShop(player, "TRUNGTHU", false);
                            break;

                    }
                }
            }
        };
    }

    public static Npc MORO(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Tôi là MORO, bạn muốn tôi giúp đỡ gì cho bạn?",
                                "Cửa hàng", "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (!canOpenNpc(player)) {
                    return;
                }
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0:
                            ShopServiceNew.gI().opendShop(player, "SHOPPET", false);
                            break;

                    }
                }
            }
        };
    }
    
    

    public static Npc granala(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {

                    if (this.mapId == 171) {
                        this.createOtherMenu(player, 0,
                                "Ngươi!\n Hãy cầm đủ 7 viên ngọc rồng \n Monaito đến đây gặp ta ta sẽ ban cho ngươi\n 1 điều ước ", "Gọi rồng", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                    if (this.mapId == 171) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    this.npcChat(player, "Chức Năng Đang Được Update!");
                                    break; // goi rong

                            }
                        }
                    }
                }
            }
        };
    }
    
    public static Npc Bill(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ngươi chỉ cần mang Mảnh Chiến Lực đến đây\n Ta sẽ giúp ngươi có được những trang bị\n xịn nhất của ta!",
                            "Shop Bill", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (this.mapId) {
                        case 48:
                            switch (player.iDMark.getIndexMenu()) {
                                case ConstNpc.BASE_MENU:
                                     if (select == 0) {
                                        ShopServiceNew.gI().opendShop(player, "BILL", true);
                                        break;
                                    }
                            }
                            break;
                    }
                }
            }
        };
    }
    
    public static Npc Thiensu(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    ShopServiceNew.gI().opendShop(player, "TORIBOT", true);
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    switch (this.mapId) {
//                        case 48:
//                            switch (player.iDMark.getIndexMenu()) {
//                                case ConstNpc.BASE_MENU:
//                                     if (select == 0) {
//                                        
//                                        break;
//                                    }
//                            }
//                            break;
//                    }
//                }
            }
        };
    }
       
//    Service.gI().showListTop(player, Manager.topNV);

    public static Npc createNPC(int mapId, int status, int cx, int cy, int tempId) {
        int avatar = Manager.NPC_TEMPLATES.get(tempId).avatar;
        try {
            switch (tempId) {
                case ConstNpc.BILL :
                    return Bill(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TORI_BOT :
                    return Thiensu(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BAROCK:
                    return barock(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.UNKOWN:
                    return vodai(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GHI_DANH:
                    return GhiDanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRUNG_LINH_THU:
                    return trungLinhThu(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.POTAGE:
                    return poTaGe(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUY_LAO_KAME:
                    return quyLaoKame(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.MR_POPO:
                    return popo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THANQUY:
                    return thanquy(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NOI_BANH:
                    return noibanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TAPION:
                    return Tapion(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THO_DAI_CA:
                    return thodaika(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TU_TIEN:
                    return TUTIEN(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRUONG_LAO_GURU:
                    return truongLaoGuru(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.VUA_VEGETA:
                    return vuaVegeta(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.ONG_GOHAN:
                case ConstNpc.ONG_MOORI:
                case ConstNpc.ONG_PARAGUS:
                    return ongGohan_ongMoori_ongParagus(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA:
                    return bulmaQK(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DENDE:
                    return dende(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.APPULE:
                    return appule(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DR_DRIEF:
                    return drDrief(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CARGO:
                    return cargo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CUI:
                    return cui(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.SANTA:
                    return santa(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.URON:
                    return uron(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BA_HAT_MIT:
                    return baHatMit(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RUONG_DO:
                    return ruongDo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DAU_THAN:
                    return dauThan(mapId, status, cx, cy, tempId, avatar);
                
                case ConstNpc.CALICK:
                    return calick(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.JACO:
                    return jaco(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRONG_TAI:
                    return TrongTai(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THUONG_DE:
                    return thuongDe(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.Granola:
                    return granala(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GIUMA_DAU_BO:
                    return mavuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CUA_HANG_KY_GUI:
                    return kyGui(mapId, status, cx, cy, tempId, avatar);

                case ConstNpc.Luffy:
                    return Luffy(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.Berry:
                    return Berry(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.MORO:
                    return MORO(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.Monaito:
                    return monaito(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.VADOS:
                    return vados(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.KHI_DAU_MOI:
                    return khidaumoi(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THAN_VU_TRU:
                    return thanVuTru(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.KIBIT:
                    return kibit(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.OSIN:
                    return osin(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CHIHANG:
                    return CHIHANG(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.LY_TIEU_NUONG:
                    return npclytieunuong54(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.LINH_CANH:
                    return linhCanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DOC_NHAN:
                    return docNhan(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUA_TRUNG:
                    return quaTrung(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUOC_VUONG:
                    return quocVuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA_TL:
                    return bulmaTL(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_OMEGA:
                    return rongOmega(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_1S:
                case ConstNpc.RONG_2S:
                case ConstNpc.RONG_3S:
                case ConstNpc.RONG_4S:
                case ConstNpc.RONG_5S:
                case ConstNpc.RONG_6S:
                case ConstNpc.RONG_7S:
                    return rong1_to_7s(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NPC_64:
                    return npcThienSu64(mapId, status, cx, cy, tempId, avatar);

                case ConstNpc.WHIS:
                    return whis(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BO_MONG:
                    return Achievement(mapId, status, cx, cy, tempId, avatar);

                case ConstNpc.GOKU_SSJ:
                    return gokuSSJ_1(mapId, status, cx, cy, tempId, avatar);

                case ConstNpc.DUONG_TANG:
                    return duongtank(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GOHAN_NHAT_NGUYET:
                    return gohannn(mapId, status, cx, cy, tempId, avatar);
                default:
                    return new Npc(mapId, status, cx, cy, tempId, avatar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                super.openBaseMenu(player);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
//                                ShopService.gI().openShopNormal(player, this, ConstNpc.SHOP_BUNMA_TL_0, 0, player.gender);
                            }
                        }
                    };

            }
        } catch (Exception e) {
            Logger.logException(NpcFactory.class,
                    e, "Lỗi load npc");
            return null;
        }
    }

    //girlbeo-mark
    public static void createNpcRongThieng() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.RONG_THIENG, -1) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.SHENRON_CONFIRM:
                        if (select == 0) {
                            SummonDragon.gI().confirmWish();
                        } else if (select == 1) {
                            SummonDragon.gI().reOpenShenronWishes(player);
                        }
                        break;
                    case ConstNpc.SHENRON_1_1:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_1 && select == SHENRON_1_STAR_WISHES_1.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_2, SHENRON_SAY, SHENRON_1_STAR_WISHES_2);
                            break;
                        }
                    case ConstNpc.SHENRON_1_2:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_2 && select == SHENRON_1_STAR_WISHES_2.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_1, SHENRON_SAY, SHENRON_1_STAR_WISHES_1);
                            break;
                        }
                    default:
                        SummonDragon.gI().showConfirmShenron(player, player.iDMark.getIndexMenu(), (byte) select);
                        break;
                }
            }
        };
    }

    public static void createNpcConMeo() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.CON_MEO, 351) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.MAKE_MATCH_PVP: {
                        if (Maintenance.isRuning) {
                            break;
                        }
                        PVPService.gI().sendInvitePVP(player, (byte) select);
                        break;
                    }
//                        else {
//                            Service.gI().sendThongBao(player, "|5|VUI LÒNG KÍCH HOẠT TÀI KHOẢN TẠI\n|7|NROGOD.COM\n|5|ĐỂ MỞ KHÓA TÍNH NĂNG");
//                            break;
//                        }
                    case ConstNpc.MAKE_FRIEND:
                        if (select == 0) {
                            Object playerId = PLAYERID_OBJECT.get(player.id);
                            if (playerId != null) {
                                FriendAndEnemyService.gI().acceptMakeFriend(player,
                                        Integer.parseInt(String.valueOf(playerId)));
                            }
                        }
                        break;
                    case ConstNpc.REVENGE:
                        if (select == 0) {
                            PVPService.gI().acceptRevenge(player);
                        }
                        break;
                    case ConstNpc.TUTORIAL_SUMMON_DRAGON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        }
                        break;
                    case ConstNpc.SUMMON_SHENRON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        } else if (select == 1) {
                            SummonDragon.gI().summonShenron(player);
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM1105:
                        if (select == 0) {
                            IntrinsicService.gI().sattd(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().satnm(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().setxd(player);
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2000:
                    case ConstNpc.MENU_OPTION_USE_ITEM2001:
                    case ConstNpc.MENU_OPTION_USE_ITEM2002:
                        try {
                        ItemService.gI().OpenSKH(player, player.iDMark.getIndexMenu(), select);
                    } catch (Exception e) {
                        Logger.error("Lỗi mở hộp quà");
                    }
                    break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2003:
                    case ConstNpc.MENU_OPTION_USE_ITEM2004:
                    case ConstNpc.MENU_OPTION_USE_ITEM2005:
                        try {
                        ItemService.gI().OpenDHD(player, player.iDMark.getIndexMenu(), select);
                    } catch (Exception e) {
                        Logger.error("Lỗi mở hộp quà");
                    }
                    case ConstNpc.MENU_OPTION_USE_ITEM2093:
                    case ConstNpc.MENU_OPTION_USE_ITEM2094:
                    case ConstNpc.MENU_OPTION_USE_ITEM2095:
                    try {
                        ItemService.gI().OpenDTL(player, player.iDMark.getIndexMenu(), select);
                    } catch (Exception e) {
                        Logger.error("Lỗi mở hộp quà");
                    }
                    break;
                    
                  
                    case ConstNpc.MENU_OPTION_USE_ITEM736:
                        try {
                        ItemService.gI().OpenDHD(player, player.iDMark.getIndexMenu(), select);
                    } catch (Exception e) {
                        Logger.error("Lỗi mở hộp quà");
                    }
                    break;
                    case ConstNpc.INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().showAllIntrinsic(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().showConfirmOpen(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().showConfirmOpenVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().open(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC_VIP:
                        if (select == 0) {
                            IntrinsicService.gI().openVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_LEAVE_CLAN:
                        if (select == 0) {
                            ClanService.gI().leaveClan(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_NHUONG_PC:
                        if (select == 0) {
                            ClanService.gI().phongPc(player, (int) PLAYERID_OBJECT.get(player.id));
                        }
                        break;
                    case ConstNpc.BAN_PLAYER:
                        if (select == 0) {
                            PlayerService.gI().banPlayer((Player) PLAYERID_OBJECT.get(player.id));
                            Service.gI().sendThongBao(player, "Ban người chơi " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                        }
                        break;

                    case ConstNpc.BUFF_PET:
                        if (select == 0) {
                            Player pl = (Player) PLAYERID_OBJECT.get(player.id);
                            if (pl.pet == null) {
                                PetService.gI().createNormalPet(pl);
                                Service.gI().sendThongBao(player, "Phát đệ tử cho " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                            }
                        }
                        break;
                    case ConstNpc.UP_TOP_ITEM:
                        if (select == 0) {
                            if (player.inventory.gem >= 50 && player.iDMark.getIdItemUpTop() != -1) {
                                ItemKyGui it = ShopKyGuiService.gI().getItemBuy(player.iDMark.getIdItemUpTop());
                                if (it == null || it.isBuy) {
                                    Service.gI().sendThongBao(player, "Vật phẩm không tồn tại hoặc đã được bán");
                                    return;
                                }
                                if (it.player_sell != player.id) {
                                    Service.gI().sendThongBao(player, "Vật phẩm không thuộc quyền sở hữu");
                                    ShopKyGuiService.gI().openShopKyGui(player);
                                    return;
                                }
                                player.inventory.gem -= 50;
                                Service.gI().sendMoney(player);
                                Service.gI().sendThongBao(player, "Thành công");
                                it.isUpTop += 1;
                                ShopKyGuiService.gI().openShopKyGui(player);
                            } else {
                                Service.gI().sendThongBao(player, "Bạn không đủ hồng ngọc");
                                player.iDMark.setIdItemUpTop(-1);
                            }
                        }
                        break;
                    case ConstNpc.MENU_ADMIN:
                        switch (select) {
                            case 0:
                                for (int i = 14; i <= 20; i++) {
                                    Item item = ItemService.gI().createNewItem((short) i);
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                }
                                InventoryServiceNew.gI().sendItemBags(player);
                                break;
                            case 1:
                                if (player.pet == null) {
                                    PetService.gI().createNormalPet(player);
                                } else {
                                    if (player.pet.typePet == 1) {
                                        PetService.gI().changePicPet(player);
                                    } else if (player.pet.typePet == 2) {
                                        PetService.gI().changeMabuPet(player);
                                    } else if (player.pet.typePet == 3) {
                                        PetService.gI().changeBerusPet(player);
                                    } else if (player.pet.typePet == 4) {
                                        PetService.gI().changeQuyPet(player);
                                    }
                                }
                                break;
                            case 2:
                                if (player.isAdmin()) {
                                    System.out.println(player.name);
//                                PlayerService.gI().baoTri();
                                    Maintenance.gI().start(15);
                                    System.out.println(player.name);
                                }
                                break;
                            case 3:
                                Input.gI().createFormFindPlayer(player);
                                break;
                            case 4:
                                this.createOtherMenu(player, ConstNpc.CALL_BOSS,
                                        "Chọn Boss?", "Full Cụm\nANDROID", "BLACK", "BROLY", "Cụm\nCell",
                                        "Cụm\nDoanh trại", "DOREMON", "FIDE", "FIDE\nBlack", "Cụm\nGINYU", "Cụm\nNAPPA", "NGỤC\nTÙ");
                                break;
                            case 5:
                                Input.gI().createFormSenditem(player);
                                break;
                            case 6:
                                Input.gI().createFormSenditem1(player);
                                break;
                            case 7:
                                Input.gI().createFormSenditem2(player);
                                break;
                            case 8:
                                BossManager.gI().showListBoss(player);
                                break;
//                          case 8:
                            //                               MaQuaTangManager.gI().checkInfomationGiftCode(player);
//                               break;
                        }
                        break;

                    case ConstNpc.QUY_DOI_HN:
                        switch (select) {
                            case 0:
                                int coin = 10000;
                                int tv = 30;
                                int dans = 3;
                                if (player.getSession().coinBar >= coin) {
                                    PlayerDAO.subcoinBar(player, coin);
                                    Item thoiVang = ItemService.gI().createNewItem((short) 457, tv);// x3
                                    Item dns = ItemService.gI().createNewItem((short) 674, dans);// x3
                                    InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                    InventoryServiceNew.gI().addItemBag(player, dns);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "bạn nhận được " + tv
                                            + " " + thoiVang.template.name + " và " + dans
                                            + " " + dns.template.name);
                                } else {
                                    Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().coinBar + " không đủ để quy "
                                            + " đổi ");
                                }
                                break;
                            case 1:
                                coin = 20000;
                                tv = 60;
                                dans = 6;
                                if (player.getSession().coinBar >= coin) {
                                    PlayerDAO.subcoinBar(player, coin);
                                    Item thoiVang = ItemService.gI().createNewItem((short) 457, tv);// x3
                                    Item dns = ItemService.gI().createNewItem((short) 674, dans);// x3
                                    InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                    InventoryServiceNew.gI().addItemBag(player, dns);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "bạn nhận được " + tv
                                            + " " + thoiVang.template.name + " và " + dans
                                            + " " + dns.template.name);
                                } else {
                                    Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().coinBar + " không đủ để quy "
                                            + " đổi ");
                                }
                                break;
                            case 2:
                                coin = 30000;
                                tv = 90;
                                dans = 9;
                                if (player.getSession().coinBar >= coin) {
                                    PlayerDAO.subcoinBar(player, coin);
                                    Item thoiVang = ItemService.gI().createNewItem((short) 457, tv);// x3
                                    Item dns = ItemService.gI().createNewItem((short) 674, dans);// x3
                                    InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                    InventoryServiceNew.gI().addItemBag(player, dns);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "bạn nhận được " + tv
                                            + " " + thoiVang.template.name + " và " + dans
                                            + " " + dns.template.name);
                                } else {
                                    Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().coinBar + " không đủ để quy "
                                            + " đổi ");
                                }
                                break;
                            case 3:
                                coin = 50000;
                                tv = 160;
                                dans = 16;
                                if (player.getSession().coinBar >= coin) {
                                    PlayerDAO.subcoinBar(player, coin);
                                    Item thoiVang = ItemService.gI().createNewItem((short) 457, tv);// x3
                                    Item dns = ItemService.gI().createNewItem((short) 674, dans);// x3
                                    InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                    InventoryServiceNew.gI().addItemBag(player, dns);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "bạn nhận được " + tv
                                            + " " + thoiVang.template.name + " và " + dans
                                            + " " + dns.template.name);
                                } else {
                                    Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().coinBar + " không đủ để quy "
                                            + " đổi ");
                                }
                                break;
                            case 4:
                                coin = 100000;
                                tv = 330;
                                dans = 33;
                                if (player.getSession().coinBar >= coin) {
                                    PlayerDAO.subcoinBar(player, coin);
                                    Item thoiVang = ItemService.gI().createNewItem((short) 457, tv);// x3
                                    Item dns = ItemService.gI().createNewItem((short) 674, dans);// x3
                                    InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                    InventoryServiceNew.gI().addItemBag(player, dns);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "bạn nhận được " + tv
                                            + " " + thoiVang.template.name + " và " + dans
                                            + " " + dns.template.name);
                                } else {
                                    Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().coinBar + " không đủ để quy "
                                            + " đổi ");
                                }
                                break;
                            case 5:
                                coin = 200000;
                                tv = 670;
                                dans = 67;
                                if (player.getSession().coinBar >= coin) {
                                    PlayerDAO.subcoinBar(player, coin);
                                    Item thoiVang = ItemService.gI().createNewItem((short) 457, tv);// x3
                                    Item dns = ItemService.gI().createNewItem((short) 674, dans);// x3
                                    InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                    InventoryServiceNew.gI().addItemBag(player, dns);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "bạn nhận được " + tv
                                            + " " + thoiVang.template.name + " và " + dans
                                            + " " + dns.template.name);
                                } else {
                                    Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().coinBar + " không đủ để quy "
                                            + " đổi ");
                                }
                                break;
                            case 6:
                                coin = 300000;
                                tv = 1050;
                                dans = 105;
                                if (player.getSession().coinBar >= coin) {
                                    PlayerDAO.subcoinBar(player, coin);
                                    Item thoiVang = ItemService.gI().createNewItem((short) 457, tv);// x3
                                    Item dns = ItemService.gI().createNewItem((short) 674, dans);// x3
                                    InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                    InventoryServiceNew.gI().addItemBag(player, dns);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "bạn nhận được " + tv
                                            + " " + thoiVang.template.name + " và " + dans
                                            + " " + dns.template.name);
                                } else {
                                    Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().coinBar + " không đủ để quy "
                                            + " đổi ");
                                }
                                break;
                            case 7:
                                coin = 500000;
                                tv = 1800;
                                dans = 180;
                                if (player.getSession().coinBar >= coin) {
                                    PlayerDAO.subcoinBar(player, coin);
                                    Item thoiVang = ItemService.gI().createNewItem((short) 457, tv);// x3
                                    Item dns = ItemService.gI().createNewItem((short) 674, dans);// x3
                                    InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                    InventoryServiceNew.gI().addItemBag(player, dns);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "bạn nhận được " + tv
                                            + " " + thoiVang.template.name + " và " + dans
                                            + " " + dns.template.name);
                                } else {
                                    Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().coinBar + " không đủ để quy "
                                            + " đổi ");
                                }
                                break;
                            case 8:
                                coin = 1000000;
                                tv = 3700;
                                dans = 370;
                                if (player.getSession().coinBar >= coin) {
                                    PlayerDAO.subcoinBar(player, coin);
                                    Item thoiVang = ItemService.gI().createNewItem((short) 457, tv);// x3
                                    Item dns = ItemService.gI().createNewItem((short) 674, dans);// x3
                                    InventoryServiceNew.gI().addItemBag(player, thoiVang);
                                    InventoryServiceNew.gI().addItemBag(player, dns);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "bạn nhận được " + tv
                                            + " " + thoiVang.template.name + " và " + dans
                                            + " " + dns.template.name);
                                } else {
                                    Service.gI().sendThongBao(player, "Số tiền của bạn là " + player.getSession().coinBar + " không đủ để quy "
                                            + " đổi ");
                                }
                                break;
//                            case 8:
//                                MaQuaTangManager.gI().checkInfomationGiftCode(player);
//                                break;
                        }
                        break;

                    case ConstNpc.CALL_BOSS:
                        switch (select) {
                            case 0:
                                BossManager.gI().createBoss(BossID.ANDROID_13);
                                BossManager.gI().createBoss(BossID.ANDROID_14);
                                BossManager.gI().createBoss(BossID.ANDROID_15);
                                BossManager.gI().createBoss(BossID.ANDROID_19);
                                BossManager.gI().createBoss(BossID.DR_KORE);
                                BossManager.gI().createBoss(BossID.KING_KONG);
                                BossManager.gI().createBoss(BossID.PIC);
                                BossManager.gI().createBoss(BossID.POC);
                                break;
                            case 1:
                                BossManager.gI().createBoss(BossID.BLACK);
                                break;
                            case 2:
                                BossManager.gI().createBoss(BossID.BOSS_BROLY_THUONG);
                                BossManager.gI().createBoss(BossID.BOSS_BROLY_SUPER);
                                BossManager.gI().createBoss(BossID.BROLY);

                                break;
                            case 3:
                                BossManager.gI().createBoss(BossID.SIEU_BO_HUNG);
                                BossManager.gI().createBoss(BossID.XEN_BO_HUNG);
//                                BossManager.gI().createBoss(BossID.XEN_CON);
                                break;
                            case 4:
                                Service.getInstance().sendThongBao(player, "Không có boss");
                                break;
                            case 5:
                                BossManager.gI().createBoss(BossID.CHAIEN);
                                BossManager.gI().createBoss(BossID.XEKO);
                                BossManager.gI().createBoss(BossID.XUKA);
                                BossManager.gI().createBoss(BossID.NOBITA);
                                BossManager.gI().createBoss(BossID.DORAEMON);
                                break;
                            case 6:
                                BossManager.gI().createBoss(BossID.FIDE);
                                break;
                            case 7:
                                BossManager.gI().createBoss(BossID.FIDE_ROBOT);
                                BossManager.gI().createBoss(BossID.VUA_COLD);
                                break;
                            case 8:
                                BossManager.gI().createBoss(BossID.SO_1);
                                BossManager.gI().createBoss(BossID.SO_2);
                                BossManager.gI().createBoss(BossID.SO_3);
                                BossManager.gI().createBoss(BossID.SO_4);
                                BossManager.gI().createBoss(BossID.TIEU_DOI_TRUONG);
                                break;
                            case 9:
                                BossManager.gI().createBoss(BossID.KUKU);
                                BossManager.gI().createBoss(BossID.MAP_DAU_DINH);
                                BossManager.gI().createBoss(BossID.RAMBO);
                                break;
                            case 10:
                                BossManager.gI().createBoss(BossID.COOLER_GOLD);
                                BossManager.gI().createBoss(BossID.CUMBER);
                                BossManager.gI().createBoss(BossID.SONGOKU_TA_AC);
                                break;
                        }
                        break;
                    case ConstNpc.menutd:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().settaiyoken(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setgenki(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setkamejoko(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;

                    case ConstNpc.menunm:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().setgodki(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setgoddam(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setsummon(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;

                    case ConstNpc.menuxd:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().setgodgalick(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setmonkey(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setgodhp(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;

                    case ConstNpc.CONFIRM_DISSOLUTION_CLAN:
                        switch (select) {
                            case 0:
                                Clan clan = player.clan;
                                clan.deleteDB(clan.id);
                                Manager.CLANS.remove(clan);
                                player.clan = null;
                                player.clanMember = null;
                                ClanService.gI().sendMyClan(player);
                                ClanService.gI().sendClanId(player);
                                Service.gI().sendThongBao(player, "Đã giải tán bang hội.");
                                break;
                        }
                        break;
                    case ConstNpc.CONFIRM_ACTIVE:
                        switch (select) {
                            case 0:
                                if (player.getSession().goldBar >= 20) {
                                    player.getSession().actived = true;
                                    if (PlayerDAO.subGoldBar(player, 20)) {
                                        Service.gI().sendThongBao(player, "Đã mở thành viên thành công!");
                                        break;
                                    } else {
                                        this.npcChat(player, "Lỗi vui lòng báo admin...");
                                    }
                                }
                                Service.gI().sendThongBao(player, "Bạn không có vàng\n Vui lòng NROSEOBI.ME để nạp thỏi vàng");
                                break;
                        }
                        break;
                    case ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND:
                        if (select == 0) {
                            for (int i = 0; i < player.inventory.itemsBoxCrackBall.size(); i++) {
                                player.inventory.itemsBoxCrackBall.set(i, ItemService.gI().createItemNull());
                            }
                            player.inventory.itemsBoxCrackBall.clear();
                            Service.gI().sendThongBao(player, "Đã xóa hết vật phẩm trong rương");
                        }
                        break;
                    case ConstNpc.MENU_FIND_PLAYER:
                        Player p = (Player) PLAYERID_OBJECT.get(player.id);
                        if (p != null) {
                            switch (select) {
                                case 0:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMapYardrat(player, p.zone, p.location.x, p.location.y);
                                    }
                                    break;
                                case 1:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMap(p, player.zone, player.location.x, player.location.y);
                                    }
                                    break;
                                case 2:
                                    Input.gI().createFormChangeName(player, p);
                                    break;
                                case 3:
                                    String[] selects = new String[]{"Đồng ý", "Hủy"};
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BAN_PLAYER, -1,
                                            "Bạn có chắc chắn muốn ban " + p.name, selects, p);
                                    break;
                                case 4:
                                    Service.gI().sendThongBao(player, "Kik người chơi " + p.name + " thành công");
                                    Client.gI().getPlayers().remove(p);
                                    Client.gI().kickSession(p.getSession());
                                    break;
                            }
                        }
                        break;
                    case ConstNpc.MENU_EVENT:
                        switch (select) {
                            case 0:
                                Service.gI().sendThongBaoOK(player, "Điểm sự kiện: " + player.inventory.event + " ngon ngon...");
                                break;
                            case 1:
                                Service.gI().showListTop(player, Manager.topSK);
                                break;
                            case 2:
                                Service.gI().sendThongBao(player, "Sự kiện đã kết thúc...");
//                                NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_GIAO_BONG, -1, "Người muốn giao bao nhiêu bông...",
//                                        "100 bông", "1000 bông", "10000 bông");
                                break;
                            case 3:
                                Service.gI().sendThongBao(player, "Sự kiện đã kết thúc...");
//                                NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DOI_THUONG_SU_KIEN, -1, "Con có thực sự muốn đổi thưởng?\nPhải giao cho ta 3000 điểm sự kiện đấy... ",
//                                        "Đồng ý", "Từ chối");
                                break;

                        }
                        break;
                    case ConstNpc.MENU_GIAO_BONG:
                        ItemService.gI().giaobong(player, (int) Util.tinhLuyThua(10, select + 2));
                        break;
                    case ConstNpc.CONFIRM_DOI_THUONG_SU_KIEN:
                        if (select == 0) {
                            ItemService.gI().openBoxVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_TELE_NAMEC:
                        if (select == 0) {
                            NgocRongNamecService.gI().teleportToNrNamec(player);
                            player.inventory.subGemAndRuby(50);
                            Service.gI().sendMoney(player);
                        }
                        break;
                }
            }
        };
    }

}
