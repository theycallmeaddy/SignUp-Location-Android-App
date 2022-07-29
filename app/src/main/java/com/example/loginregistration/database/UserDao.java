package com.example.loginregistration.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.loginregistration.models.User;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Query("select * from User where email = :email AND password = :password ")
    User findUser(String email, String password);

    @Query("select * from User where name = :name AND email = :email")
    User findLoc(String name, String email);

    @Query("select * from User where user_id = :id")
    User findUsrId(int id);

    @Query("SELECT COUNT(user_id) FROM User")
    int getCount();
}