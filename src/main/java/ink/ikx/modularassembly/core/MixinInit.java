package ink.ikx.modularassembly.core;

import org.spongepowered.asm.mixin.Mixins;
import zone.rong.mixinbooter.MixinLoader;

@MixinLoader
public class MixinInit {

    public MixinInit() {
        Mixins.addConfiguration("mixins.modularassembly.json");
    }

}
