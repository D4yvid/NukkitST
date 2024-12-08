package org.crimsonmc.block;

/**
 * Created on 2015/11/22 by xtypr. Package cn.crimsonmc.block in project crimsonmc .
 */
public class BlockPodzol extends BlockDirt {

    public BlockPodzol() {
        this(0);
    }

    public BlockPodzol(int meta) {
        super(0);
    }

    @Override
    public int getId() {
        return PODZOL;
    }

    @Override
    public String getName() {
        return "Podzol";
    }
}
