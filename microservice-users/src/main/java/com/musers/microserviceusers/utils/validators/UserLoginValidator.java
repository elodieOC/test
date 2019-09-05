package com.musers.microserviceusers.utils.validators;

import com.musers.microserviceusers.dao.UserDao;
import com.musers.microserviceusers.model.User;
import com.musers.microserviceusers.utils.Encryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * <p>Class validates the User login form</p>
 */
@Component
public class UserLoginValidator implements Validator {
    @Autowired
    private UserDao userDao;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    /**
     * <p>Method adds error to bindingresult if username doesn't exist or password doesn't match username</p>
     * @param o the user
     * @param errors error values will be added to bindingresult for messages on form
     */
    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;
        String formUsername = user.getUserName();
        String formPassword = user.getPassword();
            if (formUsername.equals("")) {
                errors.rejectValue("username", "NotEmpty");
            }
            if (formPassword.equals("")) {
                errors.rejectValue("password", "NotEmpty");
            }
            if (!formUsername.equals("") && !userDao.findFirstByUserName(formUsername).isPresent()) {
                errors.rejectValue("username", "login.username.unknown");
            }
            if (!formPassword.equals("")&& userDao.findFirstByUserName(formUsername).isPresent()) {
                User toBeChecked = userDao.findByUserName(formUsername);
                System.out.println("USERTOLOGIN"+toBeChecked);
                String loginPassword = Encryption.encrypt(formPassword);
                if (!loginPassword.equals(toBeChecked.getPassword())) {
                    errors.rejectValue("password", "login.password.noMatch");
                }
            }
        }
    }
