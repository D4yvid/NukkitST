package org.crimsonmc.level.particle;

import org.crimsonmc.block.BlockColor;
import org.crimsonmc.math.Vector3;

/**
 * Created on 2016/1/4 by xtypr. Package cn.crimsonmc.level.particle in project crimsonmc .
 */
public class InstantSpellParticle extends SpellParticle {

    protected int data;

    public InstantSpellParticle(Vector3 pos) {
        this(pos, 0);
    }

    public InstantSpellParticle(Vector3 pos, int data) {
        super(pos, data);
    }

    public InstantSpellParticle(Vector3 pos, BlockColor blockMapColor) {
        // alpha is ignored
        this(pos, blockMapColor.getRed(), blockMapColor.getGreen(), blockMapColor.getBlue());
    }

    public InstantSpellParticle(Vector3 pos, int r, int g, int b) {
        // this 0x01 is the only difference between instant spell and non-instant one
        super(pos, r, g, b, 0x01);
    }
}
