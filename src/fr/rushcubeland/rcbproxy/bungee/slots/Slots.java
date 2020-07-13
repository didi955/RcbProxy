package fr.rushcubeland.rcbproxy.bungee.slots;

import net.md_5.bungee.api.ProxyServer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Slots {

    public static void changeSlots(int slots) throws ReflectiveOperationException {
        Class<?> configClass = ProxyServer.getInstance().getConfig().getClass();

        if (!configClass.getSuperclass().equals(Object.class)) {
            configClass = configClass.getSuperclass();
        }
        Field playerLimitField = configClass.getDeclaredField("playerLimit");
        playerLimitField.setAccessible(true);
        playerLimitField.set(ProxyServer.getInstance().getConfig(), slots);
    }

    public static void updateSlotsConfig(int slots) throws ReflectiveOperationException {
        Method setMethod = ProxyServer.getInstance().getConfigurationAdapter().getClass().getDeclaredMethod("set", String.class, Object.class);
        setMethod.setAccessible(true);
        setMethod.invoke(ProxyServer.getInstance().getConfigurationAdapter(), "player_limit", slots);
    }
}
