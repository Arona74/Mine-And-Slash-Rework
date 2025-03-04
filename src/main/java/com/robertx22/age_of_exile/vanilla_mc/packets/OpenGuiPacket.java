package com.robertx22.age_of_exile.vanilla_mc.packets;

import com.robertx22.age_of_exile.mmorpg.SlashRef;
import com.robertx22.age_of_exile.vanilla_mc.packets.proxies.OpenGuiWrapper;
import com.robertx22.library_of_exile.main.MyPacket;
import com.robertx22.library_of_exile.packets.ExilePacketContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class OpenGuiPacket extends MyPacket<OpenGuiPacket> {

    public enum GuiType {
        TALENTS,
        PICK_STATS,
        SPELLS,
        MAIN_HUB
    }

    GuiType type;

    public OpenGuiPacket() {

    }

    public OpenGuiPacket(GuiType type) {
        this.type = type;
    }

    @Override
    public void loadFromData(FriendlyByteBuf buf) {
        type = GuiType.valueOf(buf.readUtf(44));
    }

    @Override
    public void saveToData(FriendlyByteBuf buf) {
        buf.writeUtf(type.name(), 44);
    }

    @Override
    public void onReceived(ExilePacketContext ctx) {
        if (type == GuiType.MAIN_HUB) {
            OpenGuiWrapper.openMainHub();
        }

    }

    @Override
    public MyPacket<OpenGuiPacket> newInstance() {
        return new OpenGuiPacket();
    }

    @Override
    public ResourceLocation getIdentifier() {
        return new ResourceLocation(SlashRef.MODID, "opengui");
    }

}