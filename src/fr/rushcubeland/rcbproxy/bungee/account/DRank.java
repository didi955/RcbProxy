package fr.rushcubeland.rcbproxy.bungee.account;

import net.md_5.bungee.api.ProxyServer;

import java.util.UUID;

public class DRank extends AbstractData {

    private RankUnit grade;
    private long end;

    public DRank(UUID uuid) {
        this.uuid = uuid;
    }

    public void setRank(RankUnit rank){
        if(grade != null){
            for(String perm : grade.getPermissions()){
                ProxyServer.getInstance().getPlayer(uuid).setPermission(perm, false);
            }
        }
        grade = rank;
        end = -1;
        for(String perm : rank.getPermissions()){
            ProxyServer.getInstance().getPlayer(uuid).setPermission(perm, true);
        }
    }
    public void setRank(RankUnit rank, long seconds){
        if(seconds <= 0){
            setRank(rank);
        }
        else
        {
            if(grade != null){
                for(String perm : grade.getPermissions()){
                    ProxyServer.getInstance().getPlayer(uuid).setPermission(perm, false);
                }
            }
            grade = rank;
            end = seconds*1000 + System.currentTimeMillis();
            for(String perm : rank.getPermissions()){
                ProxyServer.getInstance().getPlayer(uuid).setPermission(perm, true);
            }
        }
    }

    public RankUnit getRank(){
        return grade;
    }

    public long getEnd(){
        return end;
    }

    public boolean isTemporary(){
        return end != -1;
    }

    public boolean isValid(){
        return end != -1 && end < System.currentTimeMillis();
    }
}

