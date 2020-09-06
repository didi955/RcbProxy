package fr.rushcubeland.commons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class APermissions implements Cloneable {

    private UUID uuid;
    private ArrayList<String> permissions = new ArrayList<>();

    public APermissions(){
    }

    public APermissions(UUID uuid, ArrayList<String> permissions) {
        this.uuid = uuid;
        this.permissions = permissions;
    }

    public ArrayList<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<String> permissions) {
        this.permissions = permissions;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void addPermission(String... permissions){
        this.permissions.addAll(Arrays.asList(permissions));
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void removePermission(String... permissions){
        this.permissions.addAll(Arrays.asList(permissions));
    }

    public APermissions clone(){
        try {

            return (APermissions) super.clone();

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
