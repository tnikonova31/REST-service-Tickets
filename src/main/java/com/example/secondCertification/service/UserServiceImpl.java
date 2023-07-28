package com.example.secondCertification.service;


import com.example.secondCertification.entity.User;
import com.example.secondCertification.models.UserModel;
import com.example.secondCertification.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserServiceImpl implements UserService {
    private static final AtomicLong USER_ID = new AtomicLong();
    private static Base64.Encoder encoder = Base64.getEncoder();
    private static final String KEY = "&&&qBTmv4oXFFR2Gwjex";
    @Autowired
    UserRepository userRepository;

    /** Создание нового пользователя (регистрация)
     *
     * @param user данные для создания
     * @return true - успешно
     */
    @Override
    public boolean isCreate(User user) {
        if (UserModel.isUserDataCurrent(user.getLogin(), user.getPassword(), user.getPhone())) { //данные корректные
            if (userRepository.findUserByLogin(user.getLogin()) == null) { // если в базе нет такого логина
                if (userRepository.findUserByPhone(user.getPhone()) == null) {// в базе нет такого телефона
                    Long userId = USER_ID.incrementAndGet(); // получаем ID
                    User userFind = userRepository.findById(userId).orElse(null); // ищем по ID в БД
                    while (userFind != null) { // пока находим с таким ID
                        userId = USER_ID.incrementAndGet(); // генерируем новый ID
                        userFind = userRepository.findById(userId).orElse(null); // проверяем
                    }
                    user.setUserId(userId); //зафиксировали ID
                    user.setPassword(encoder.encodeToString((user.getPassword() + KEY).getBytes())); // закодировали пароль
                    userRepository.save(user); // сохранили
                    return true;
                }
            }
        }
        return false;
    }

    /** Авторизация пользователя
     *
     * @param user
     * @return
     */
    @Override
    public boolean isSignIn(User user) {
        User userFind = userRepository.findUserByLoginAndPassword(
                user.getLogin(), encoder.encodeToString(((user.getPassword() + KEY).getBytes())));
        if (userFind != null) {
            userFind.setToken(encoder.encodeToString((user.getLogin() + user.getPassword() + KEY).getBytes()));
            userRepository.save(userFind);
            return true;
        }
        return false;
    }

    /** Получение токена пользователя
     *
     * @param user
     * @return
     */
    @Override
    public String getToken(User user) {
        return userRepository.findUserByLogin(user.getLogin()).getToken();
    }

    /** Выход из системы
     *
     * @param token пользователя
     * @return
     */
    @Override
    public boolean isLogOut(String token) {
        User userFind = userRepository.findUserByToken(token);
        if (userFind != null){ // нашли пользователя по токену
            userFind.setToken(null); // обнулили
            userRepository.save(userFind); // сохранили
            return true;
        }
        return false;
    }
}
