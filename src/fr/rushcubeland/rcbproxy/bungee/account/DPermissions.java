package fr.rushcubeland.rcbproxy.bungee.account;

import net.md_5.bungee.api.ProxyServer;

import java.util.ArrayList;
import java.util.UUID;

public class DPermissions extends AbstractData {

    private ArrayList<String> permissions = new ArrayList<>();

    public DPermissions(UUID uuid){
        this.uuid = uuid;
    }

    public void addPermission(String permission){
        if(!this.permissions.contains(permission)){
            this.permissions.add(permission);
            ProxyServer.getInstance().getPlayer(uuid).setPermission(permission, true);
        }
    }

    public void removePermission(String permission){
        if(this.permissions.contains(permission)){
            this.permissions.remove(permission);
            ProxyServer.getInstance().getPlayer(uuid).setPermission(permission, false);
        }
    }

    public ArrayList<String> getPermissions(){
        return this.permissions;
    }

    public boolean hasPermission(String permission){
        if(this.permissions.contains(permission)){
            return true;
        }
        return false;
    }


}
