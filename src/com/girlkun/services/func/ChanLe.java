/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.girlkun.services.func;

import com.girlkun.models.item.Item;
import com.girlkun.models.player.Player;
import com.girlkun.services.ChatGlobalService;
import com.girlkun.services.InventoryServiceNew;
import com.girlkun.services.ItemService;
import com.girlkun.services.Service;
import com.girlkun.utils.Util;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class ChanLe implements Runnable{
    public int Chan;
    public int Le;
    public long lastTimeEnd;
    public List<Player> ChanList = new ArrayList<>();
    public List<Player> LeList = new ArrayList<>();
    private static ChanLe instance;
    
    public static ChanLe gI() {
        if (instance == null) {
            instance = new ChanLe();
        }
        return instance;
    } 
    
    public void addPlayerChan(Player pl){
        if(!ChanList.equals(pl)){
            ChanList.add(pl);
        }
    }
    
    public void addPlayerLe(Player pl){
        if(!LeList.equals(pl)){
            LeList.add(pl);
        }
    }
    
    public void removePlayerChan(Player pl){
        if(ChanList.equals(pl)){
            ChanList.remove(pl);
        }
    }
    
    public void removePlayerLe(Player pl){
        if(LeList.equals(pl)){
            LeList.remove(pl);
        }
    }
    
    @Override
    public void run() {
        while (true) {
            try{
               if(((ChanLe.gI().lastTimeEnd - System.currentTimeMillis())/1000) <= 0){
                    int random = Util.nextInt(0, 100);
                    if(random % 2 == 0){
                        List<Player> listChan = new ArrayList<>();
                        ChanLe.gI().ChanList.stream().filter(p -> p != null && p.Chan != 0).sorted(Comparator.comparing(p -> Math.ceil(((double)p.Chan/ChanLe.gI().Chan) * 100),Comparator.reverseOrder())).forEach(cl -> listChan.add(cl));
                         if(listChan.size() > 0){
                             for (Player player : listChan) {
                                    int goldC = player.Chan * 80 / 100;
                                    Service.gI().sendThongBao(player, "Chẵn win - Chúc mừng bạn đã dành chiến thắng và nhận được " + goldC +" thỏi vàng");
                                    Item it = ItemService.gI().createNewItem((short)457,goldC);
                                    InventoryServiceNew.gI().addItemBag(player, it);
                                    InventoryServiceNew.gI().sendItemBags(player);
                             }
                         }
                        Service.gI().sendThongBao(listChan, "Chẵn win nhé ae");

                        listChan.clear();
                       
                    }else {
                        List<Player> listLe = new ArrayList<>();
                        ChanLe.gI().LeList.stream().filter(p -> p != null && p.Le != 0).sorted(Comparator.comparing(p -> Math.ceil(((double)p.Le/ChanLe.gI().Le) * 100),Comparator.reverseOrder())).forEach(cl -> LeList.add(cl));
                         if(listLe.size() > 0){
                             for (Player player : listLe) {
                                    int goldC = player.Le * 80 / 100;
                                    Service.gI().sendThongBao(player, "Lẽ win - Chúc mừng bạn đã dành chiến thắng và nhận được " + goldC +" thỏi vàng");
                                    Item it = ItemService.gI().createNewItem((short)457,goldC);
                                    InventoryServiceNew.gI().addItemBag(player, it);
                                    InventoryServiceNew.gI().sendItemBags(player);
                             }
                         }
                         Service.gI().sendThongBao(listLe, "Lẽ win nhé ae");
                         listLe.clear();
                    }
                                      
                               
                    for(int i = 0 ; i < ChanLe.gI().ChanList.size();i++){
                        Player pl = ChanLe.gI().ChanList.get(i);
                        if(pl != null){
                            pl.Chan = 0;
                            pl.Le = 0;
                        }
                    }
                    for(int i = 0 ; i < ChanLe.gI().LeList.size();i++){
                        Player pl = ChanLe.gI().LeList.get(i);
                        if(pl != null){
                            pl.Chan = 0;
                            pl.Le = 0;
                        }
                    }
                    ChanLe.gI().Chan = 0;
                    ChanLe.gI().Le = 0;
                    ChanLe.gI().ChanList.clear();
                    ChanLe.gI().LeList.clear();
                    ChanLe.gI().lastTimeEnd = System.currentTimeMillis() + 300000;
                }
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
    }
}