package net.blockhost.anarchymod.mixin;

import net.blockhost.anarchymod.Domains;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.logging.Logger;

@Mixin(ServerList.class)
public class ServerListMixin {

    private static final Logger LOGGER = Logger.getLogger("AnarchyMod-ServerList");

    @Shadow
    @Final
    private List<ServerData> serverList;

    @Inject(method = "load", at = @At("RETURN"))
    public void afterLoad(CallbackInfo ci) {
        List<String> existingServers = new ArrayList<>(serverList.size());
        for (ServerData data : serverList) {
            existingServers.add(data.ip);
        }

        List<Domains.ServerEntry> pendingServers = Domains.getFeaturedServersToAdd(existingServers);
        if (pendingServers.isEmpty()) {
            return;
        }

        LOGGER.info(() -> {
            StringJoiner joiner = new StringJoiner(", ");
            for (Domains.ServerEntry pending : pendingServers) {
                joiner.add(pending.name() + " (" + pending.address() + ")");
            }
            return "Pending auto-added servers: " + joiner;
        });

        for (int i = pendingServers.size() - 1; i >= 0; i--) {
            Domains.ServerEntry pending = pendingServers.get(i);
            //? if <1.20.2 {
            /*serverList.add(0, new ServerData(pending.name(), pending.address(), false));
            *///? } elif <1.20.5 {
            /*serverList.add(0, new ServerData(pending.name(), pending.address(), ServerData.Type.OTHER));
            *///?} else {
            serverList.addFirst(new ServerData(pending.name(), pending.address(), ServerData.Type.OTHER));
            //?}
        }
    }
}
