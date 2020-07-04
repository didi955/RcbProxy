package fr.rushcubeland.rcbproxy.bungee.account;

import java.util.ArrayList;
import java.util.UUID;

public class DFriends extends AbstractData {

    private ArrayList<String> friends = new ArrayList<>();
    private int maxFriends;

    public DFriends(UUID uuid){
        this.uuid = uuid;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void addFriend(String name){
        if(!friends.contains(name)){
            friends.add(name);
        }
    }

    public void removeFriend(String name){
        if(friends.contains(name)){
            friends.remove(name);
        }
    }

    public boolean areFriendWith(String playerName){
        if(friends.contains(playerName)){
            return true;
        }
        return false;
    }

    public boolean hasReachedMaxFriends(){
        int a = 0;
        for(String friend : friends){
            a = a+1;
        }
        if(maxFriends <= a){
            return true;
        }
        return false;
    }

    public int getMaxFriends() {
        return maxFriends;
    }

    public void setMaxFriends(int maxFriends) {
        this.maxFriends = maxFriends;
    }
}
