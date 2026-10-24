package design.inherent.copycatbars;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ModelEvent;

@Mod(value = "copycat_bar_extrusion", dist = Dist.CLIENT)
public final class CopycatBarExtrusion {
    public CopycatBarExtrusion(IEventBus events) {
        events.addListener((ModelEvent.BakingCompleted event) -> BarMesh.clear());
    }
}
