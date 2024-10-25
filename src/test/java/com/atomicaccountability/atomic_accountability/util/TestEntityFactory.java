package com.atomicaccountability.atomic_accountability.util;

import com.atomicaccountability.atomic_accountability.entity.*;
import com.atomicaccountability.atomic_accountability.enums.NotificationPref;
import com.atomicaccountability.atomic_accountability.enums.Status;

import java.time.LocalDateTime;
import java.util.UUID;

public class TestEntityFactory {

    public static User createUser() {
        User user = new User();
        user.setUsername(UUID.randomUUID().toString());
        user.setPassword("password");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail(UUID.randomUUID().toString());
        user.setNotificationPref(NotificationPref.TEXT);
        return user;
    }

    public static Habit createHabit(User user) {
        Habit habit = new Habit();
        habit.setName("Test Habit");
        habit.setDescription("Test description");
        habit.setUser(user);
        habit.setFrequency("2 days");
        return habit;
    }

    public static Reward createReward(User user, Habit habit) {
        Reward reward = new Reward();
        reward.setUser(user);
        reward.setHabit(habit);
        reward.setName("Test Reward");
        reward.setDescription("Test description");
        return reward;
    }

    public static HabitLog createHabitLog(Habit habit) {
        HabitLog habitLog = new HabitLog();
        habitLog.setHabit(habit);
        habitLog.setStatus(Status.COMPLETED);
        habitLog.setCompletionDate(LocalDateTime.now());
        return habitLog;
    }

    public static Notification createNotification(User user, Habit habit) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setHabit(habit);
        notification.setMessage("Test message");
        notification.setNotificationType(NotificationPref.EMAIL);
        return notification;
    }


}
