package com.example.secondCertification.controllers;

import com.example.secondCertification.entity.User;
import com.example.secondCertification.models.UserModel;
import com.example.secondCertification.models.answers.ApiAnswer;
import com.example.secondCertification.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/users")
@Tag(name="Пользователи / клиенты", description="Выполняет все действия с пользователями")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** Создание нового пользователя
     *
     * @param user данные для создания пользователя
     * @return
     */
    @PostMapping(value = "/create")
    @Operation(
            summary = "Создание/регитрация нового пользователя"
    )
    public ResponseEntity<?> create(@RequestBody User user){
        if(user.getLogin() == null || user.getPhone() == null || user.getPassword() == null){
            return UserModel.badUserData();
        }
        return userService.isCreate(user) ?
                UserModel.createEntity("User") :
                UserModel.entityNotCreate("User");
    }

    /** Авторизация
     *
     * @param user
     * @param request
     * @return
     */
    @PostMapping("/logIn")
    @Operation(
            summary = "Авторизация пользователя",
            description="При успешной авторизации генерируется токен"
    )
    public ResponseEntity<?> login(@RequestBody User user, HttpServletRequest request) {
        if(user.getLogin() == null || user.getPassword() == null){
            return UserModel.badUserData();
        } else if (userService.isSignIn(user)) {
            HttpSession session = request.getSession();
            session.setAttribute("userToken", userService.getToken(user));
            return UserModel.userToken(userService.getToken(user));
        }
        return UserModel.userNotRegistr();
    }

    /** Выход из системы
     *
     * @param token
     * @param request
     * @return
     */
    @PostMapping("/logOut/{token}")
    @Operation(
            summary = "Выход из системы"
    )
    public ResponseEntity<?> logOut(@PathVariable(value = "token") @Parameter(description = "Токен пользователя") String token, HttpServletRequest request) {
        if (token != null){ // токен не пустой
            if (userService.isLogOut(token)){ //нашли user по токену
                request.getSession().invalidate(); // закрыли сессию
                return UserModel.userLogOut();
            }
            return UserModel.notFind("User");
        }
        return UserModel.notFind("Token");
    }
}
