package org.crimsonmc.player;

import org.crimsonmc.metadata.Metadatable;
import org.crimsonmc.permission.ServerOperator;
import org.crimsonmc.server.Server;

public interface Player extends ServerOperator, Metadatable {

    /**
     * 返回这个玩家是否在线。<br> Returns if this player is online.
     *
     * @return 这个玩家是否在线。<br>If this player is online.
     * @since crimsonmc 1.0 | crimsonmc API 1.0.0
     */
    boolean isOnline();

    /**
     * 返回这个玩家的名称。<br> Returns the name of this player.
     * <p>
     * <p>如果是在线的玩家，这个函数只会返回登录名字。如果要返回显示的名字，参见{@link
     * ServerPlayer#getDisplayName}<br> Notice that this will only return its login name. If you
     * need its display name, turn to {@link ServerPlayer#getDisplayName}</p>
     *
     * @return 这个玩家的名称。<br>The name of this player.
     * @since crimsonmc 1.0 | crimsonmc API 1.0.0
     */
    String getName();

    /**
     * 返回这个玩家是否被封禁(ban)。<br> Returns if this player is banned.
     *
     * @return 这个玩家的名称。<br>The name of this player.
     * @see #setBanned
     * @since crimsonmc 1.0 | crimsonmc API 1.0.0
     */
    boolean isBanned();

    /**
     * 设置这个玩家是否被封禁(ban)。<br> Sets this player to be banned or to be pardoned.
     *
     * @param value 如果为{@code true}，封禁这个玩家。如果为{@code false}，解封这个玩家。<br> {@code
     *              true} for ban and {@code false} for pardon.
     * @see #isBanned
     * @since crimsonmc 1.0 | crimsonmc API 1.0.0
     */
    void setBanned(boolean value);

    /**
     * 返回这个玩家是否已加入白名单。<br> Returns if this player is pardoned by whitelist.
     *
     * @return 这个玩家是否已加入白名单。<br>If this player is pardoned by whitelist.
     * @see Server#isWhitelisted
     * @since crimsonmc 1.0 | crimsonmc API 1.0.0
     */
    boolean isWhitelisted();

    /**
     * 把这个玩家加入白名单，或者取消这个玩家的白名单。<br> Adds this player to the white list, or
     * removes it from the whitelist.
     *
     * @param value 如果为{@code true}，把玩家加入白名单。如果为{@code
     *              false}，取消这个玩家的白名单。<br> {@code true} for add and {@code false} for remove.
     * @see #isWhitelisted
     * @see Server#addWhitelist
     * @see Server#removeWhitelist
     * @since crimsonmc 1.0 | crimsonmc API 1.0.0
     */
    void setWhitelisted(boolean value);

    /**
     * 得到这个接口的{@code Player}对象。<br> Returns a {@code Player} object for this interface.
     *
     * @return 这个接口的 {@code Player}对象。<br>a {@code Player} object for this interface.
     * @see Server#getPlayerExact
     * @since crimsonmc 1.0 | crimsonmc API 1.0.0
     */
    ServerPlayer getPlayer();

    /**
     * 返回玩家所在的服务器。<br> Returns the server carrying this player.
     *
     * @return 玩家所在的服务器。<br>the server carrying this player.
     * @since crimsonmc 1.0 | crimsonmc API 1.0.0
     */
    Server getServer();

    /**
     * 得到这个玩家第一次游戏的时间。<br> Returns the time this player first played in this server.
     *
     * @return 这个玩家第一次游戏的时间。<br>The time this player first played in this server.
     * @since crimsonmc 1.0 | crimsonmc API 1.0.0
     */
    Long getFirstPlayed();

    /**
     * 得到这个玩家上次加入游戏的时间。<br> Returns the time this player last joined in this server.
     *
     * @return 这个玩家上次游戏的时间。<br>The time this player last joined in this server.
     * @since crimsonmc 1.0 | crimsonmc API 1.0.0
     */
    Long getLastPlayed();

    /**
     * 返回这个玩家以前是否来过服务器。<br> Returns if this player has played in this server before.
     * <p>
     * <p>如果想得到这个玩家是不是第一次玩，可以使用：<br>
     * If you want to know if this player is the first time playing in this server, you can use:<br>
     * <pre>if(!player.hasPlayerBefore()) {...}</pre></p>
     *
     * @return 这个玩家以前是不是玩过游戏。<br>If this player has played in this server before.
     * @since crimsonmc 1.0 | crimsonmc API 1.0.0
     */
    boolean hasPlayedBefore();
}
