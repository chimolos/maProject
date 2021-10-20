package com.second.maproject.users.services;

import com.second.maproject.users.repository.UserRepository;
import com.second.maproject.users.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EmailVerificationService {
    private final UserRepository userRepo;
    private final JavaMailSender mailSender;

    protected void sendVerificationEmail(User user) throws MessagingException, UnsupportedEncodingException {

        String toAddress = user.getEmail();
        String fromAddress= "i.reportp@gmail.com";//the mail of the sender
        String senderName = "I-Report";//company name
        String subject = "Please verify your account";
        String content = "Dear [[username]], <br>"
                + "Please click the link below to verify your account: <br>"
                + "[[URL]] <br>"
                + "Thank you, <br>"
                + "I-Report";

        MimeMessage email = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(email);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[username]]", user.getUsername());
        String verifyURL = "http://localhost:3000/verify/" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(email);
    }

    public boolean verify(String verificationCode) {
        User user = userRepo.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepo.save(user);

            return true;
        }
    }
}
