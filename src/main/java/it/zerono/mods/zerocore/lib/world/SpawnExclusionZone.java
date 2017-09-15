package it.zerono.mods.zerocore.lib.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SpawnExclusionZone {

    public interface ISpawnExclusion {

        boolean canEntitySpawn(@Nonnull EntityLiving entity, @Nonnull World world, float x, float y, float z);
    }

    public static void addZone() {

    }

    public static void removeZone() {

    }

    @SubscribeEvent
    protected void onEntitySpawn(LivingSpawnEvent.CheckSpawn event) {

        if (Event.Result.DENY == event.getResult())
            return;



    }

    private final Set<Integer> x = Sets.newTreeSet()

    private final Map<Integer, Map<Zone, ISpawnExclusion>> _exclusionZones; = Maps.newHashMap();
}
