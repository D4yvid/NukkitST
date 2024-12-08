package org.crimsonmc.redstone;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.crimsonmc.block.Block;

/**
 * author: Angelic47 crimsonmc Project
 */
@Getter
@RequiredArgsConstructor
public class UpdateObject {

    private final int population;

    private final Block location;
}
