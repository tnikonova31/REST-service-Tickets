package com.example.secondCertification.service;

import com.example.secondCertification.entity.User;

public interface UserService {

    /** Создание пользователя
     *
     * @param user
     * @return
     */
    boolean isCreate(User user);

    /** Авторизация пользователя
     *
     * @param user
     * @return
     */
    boolean isSignIn(User user);

    /** Получение токена пользователя
     *
     * @param user
     * @return
     */
    String getToken(User user);

    /** Выход из системы
     *
     * @param token
     * @return
     */
    boolean isLogOut(String token);

}
