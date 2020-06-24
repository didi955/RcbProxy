package fr.rushcubeland.rcbproxy.bungee;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class BungeeSend {


    public static void teleport(ProxiedPlayer from, ProxiedPlayer to) {
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteArrayOut);

        try {
            out.writeUTF("Teleport");
            out.writeUTF(from.getName());
            out.writeUTF(to.getName());

            from.getServer().getInfo()
                    .sendData(RcbProxy.channel, byteArrayOut.toByteArray());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
