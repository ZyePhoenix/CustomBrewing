package com.gmail.ryderzye.CustomPotionBrewing;

import org.bukkit.ChatColor;

public enum ChatConstant {
    COMMAND_ERROR(CustomBrewing.get().getConfig().getString("commands.COMMAND_ERROR")),
    COMMAND_NO_PERMISSION(CustomBrewing.get().getConfig().getString("commands.COMMAND_NO_PERMISSION")),
    COMMAND_NUMBER(CustomBrewing.get().getConfig().getString("commands.COMMAND_NUMBER")),
    NO_PLAYER(CustomBrewing.get().getConfig().getString("commands.NO_PLAYER")),
    NO_CONSOLE(CustomBrewing.get().getConfig().getString("commands.NO_CONSOLE")),
    PREFIX("");

    private String message;

    private ChatConstant(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public String getFormattedMessage(ChatColor color) {
        return PREFIX.getMessage() + color + this.message;
    }
}
