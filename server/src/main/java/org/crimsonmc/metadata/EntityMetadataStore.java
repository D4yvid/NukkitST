package org.crimsonmc.metadata;

import org.crimsonmc.entity.Entity;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class EntityMetadataStore extends MetadataStore {

    @Override
    protected String disambiguate(Metadatable entity, String metadataKey) {
        if (!(entity instanceof Entity)) {
            throw new IllegalArgumentException("Argument must be an Entity instance");
        }
        return ((Entity) entity).getId() + ":" + metadataKey;
    }
}
