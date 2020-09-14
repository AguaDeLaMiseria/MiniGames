package me.patothebest.gamecore.cosmetics.walkparticles;

import fr.mrmicky.fastparticle.ParticleType;
import org.bukkit.inventory.ItemStack;

class TrackedWalkTrail {

    private final WalkTrail walkTrail;
    private int ticksLived;

    TrackedWalkTrail(WalkTrail walkTrail) {
        this.walkTrail = walkTrail;
    }

    ParticleType getParticleType() {
        return walkTrail.getParticleType();
    }

    ItemStack getParticleData(){
        return walkTrail.getParticleData();
    }

    int getAmount () {
        return walkTrail.getAmount();
    }

    boolean canSpawn() {
        return ticksLived++ % walkTrail.getInterval() == 0;
    }
}
