package com.alexeykadilnikov.utils;

import com.alexeykadilnikov.entity.User;

public class UserUtils {
    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        UserUtils.currentUser = currentUser;
    }
}
