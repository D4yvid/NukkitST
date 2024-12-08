package org.crimsonmc.cli.exception

import org.crimsonmc.cli.Option

class NoOptionValueProvidedException(
    option: Option,
) : Exception(
    "Value for option ${option.longName} not provided"
)