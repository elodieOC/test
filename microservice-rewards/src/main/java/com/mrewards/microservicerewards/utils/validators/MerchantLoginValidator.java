package com.mmerchants.microservicemerchants.utils.validators;

import com.mmerchants.microservicemerchants.dao.MerchantDao;
import com.mmerchants.microservicemerchants.model.Merchant;
import com.mmerchants.microservicemerchants.utils.Encryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * <p>Class validates the Reward login form</p>
 */
@Component
public class MerchantLoginValidator implements Validator {
    @Autowired
    private MerchantDao merchantDao;

    @Override
    public boolean supports(Class<?> aClass) {
        return Merchant.class.equals(aClass);
    }

    /**
     * <p>Method adds error to bindingresult if username doesn't exist or password doesn't match username</p>
     * @param o the user
     * @param errors error values will be added to bindingresult for messages on form
     */
    @Override
    public void validate(Object o, Errors errors) {
        Merchant merchant = (Merchant) o;
        String formMerchantName = merchant.getMerchantName();
        String formPassword = merchant.getPassword();
            if (formMerchantName.equals("")) {
                errors.rejectValue("formMerchantName", "NotEmpty");
            }
            if (formPassword.equals("")) {
                errors.rejectValue("password", "NotEmpty");
            }
            if (!formMerchantName.equals("") && !merchantDao.findFirstByMerchantName(formMerchantName).isPresent()) {
                errors.rejectValue("formMerchantName", "login.merchantName.unknown");
            }
            if (!formPassword.equals("")&& merchantDao.findFirstByMerchantName(formMerchantName).isPresent()) {
                Merchant toBeChecked = merchantDao.findByMerchantName(formMerchantName);
                String loginPassword = Encryption.encrypt(formPassword);
                if (!loginPassword.equals(toBeChecked.getPassword())) {
                    errors.rejectValue("password", "login.password.noMatch");
                }
            }
        }
    }
