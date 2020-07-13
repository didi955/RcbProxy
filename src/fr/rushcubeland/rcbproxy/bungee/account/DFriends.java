package fr.rushcubeland.rcbproxy.bungee.account;

import java.util.ArrayList;
import java.util.UUID;

public class DFriends extends AbstractData {

    private final ArrayList<String> friends = new ArrayList<>();
    private final ArrayList<String> removingFriends = new ArrayList<>();
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
            if(removingFriends.contains(name)){
                removingFriends.remove(name);
            }
        }
    }

    public void removeFriend(String name){
        if(friends.contains(name)){
            friends.remove(name);
            addRemovingFriend(name);
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

    public ArrayList<String> getRemovingFriends() {
        return removingFriends;
    }

    public void addRemovingFriend(String name){
        if(!this.removingFriends.contains(name)){
            this.removingFriends.add(name);
        }
    }
}
