package com.second.maproject.users.models;

import com.second.maproject.BaseIdModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@RequiredArgsConstructor
public class PasswordResetToken extends BaseIdModel {

    private static final int EXPIRATION = 20;

    private String resetToken;

    @OneToOne(fetch = FetchType.EAGER)
    private User user;

    private Date expiryDate;

    public PasswordResetToken(String resetToken, User user) {
        this.resetToken = resetToken;
        this.user = user;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(now.getTime().getTime());
    }
}
