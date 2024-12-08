package org.crimsonmc.entity.data;

import org.crimsonmc.entity.Entity;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class LongEntityData extends EntityData<Long> {

    public long data;

    public LongEntityData(int id, long data) {
        super(id);
        this.data = data;
    }

    public Long getData() {
        return data;
    }

    public void setData(Long data) {
        this.data = data;
    }

    @Override
    public int getType() {
        return Entity.DATA_TYPE_LONG;
    }
}
