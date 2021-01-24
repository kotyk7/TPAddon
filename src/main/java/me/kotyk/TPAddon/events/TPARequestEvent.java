package me.kotyk.TPAddon.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class TPARequestEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    private Player requester;
    private Player target;
    private boolean requestToTPAHere;

    public TPARequestEvent(final Player requester, final Player target, final boolean tpaHere) {
        super();
        this.requester = requester;
        this.target = target;
        this.requestToTPAHere = tpaHere;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
