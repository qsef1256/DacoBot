package net.qsef1256.dacobot.service.key;

import net.dv8tion.jda.api.entities.User;

import java.util.Set;

public interface UserKey extends ManagedKey {

    Set<User> getUsers();

}
