package org.crimsonmc.metadata;

import org.crimsonmc.plugin.Plugin;

import java.util.List;

/**
 * author: MagicDroidX crimsonmc Project
 */
public interface Metadatable {

    void setMetadata(String metadataKey, MetadataValue newMetadataValue) throws Exception;

    List<MetadataValue> getMetadata(String metadataKey) throws Exception;

    boolean hasMetadata(String metadataKey) throws Exception;

    void removeMetadata(String metadataKey, Plugin owningPlugin) throws Exception;
}
