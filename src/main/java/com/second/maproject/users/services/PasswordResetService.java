package com.second.maproject.users.services;

import com.second.maproject.users.models.PasswordResetToken;
import com.second.maproject.users.repository.UserRepository;
import com.second.maproject.users.models.User;
import com.second.maproject.users.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PasswordResetService {
    private final UserRepository userRepo;
    private final JavaMailSender mailSender;
    private final PasswordResetTokenRepository tokenRepository;

    @Autowired
    PasswordEncoder encoder;

    public void createPasswordResetToken(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        tokenRepository.save(myToken);
    }

    public String forgotPassword(HttpServletRequest request, String userEmail) throws MessagingException, UnsupportedEncodingException {
        User user = userRepo.findByEmail(userEmail);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid email: " + userEmail);
        }
        String token = UUID.randomUUID().toString();
        createPasswordResetToken(user, token);
        sendPasswordResetEmail(token, user);

        JSONObject response = new JSONObject();
        response.put("msg", "Click link provided in email to reset password");
        return response.toString();
    }

    private void sendPasswordResetEmail(String token, User user) throws MessagingException, UnsupportedEncodingException {

        String toAddress = user.getEmail();
        String fromAddress= "i.reportp@gmail.com";//the mail of the sender
        String senderName = "I-Report";//company name
        String subject = "Here is the link to reset your password";
        String content = "Dear [[username]], <br>"
                + "Please click the link below to reset your password: <br>"
                + "[[URL]] <br>"
                + "This link expires in 20 minutes <br>"
                + "Ignore this email if you have not made the request <br>"
                + "Thank you, <br>"
                + "I-Report";

        MimeMessage email = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(email);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[username]]", user.getUsername());
        String verifyURL ="https://ireport-web.herokuapp.com/reset-password/"+ token;

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(email);
    }

//    private String getAppURL(HttpServletRequest request) {
//        String siteURL = request.getRequestURL().toString();
//        return siteURL.replace(request.getServletPath(), "");
//    }

    public String resetPassword(String token, String password) {
        PasswordResetToken passToken = tokenRepository.findByResetToken(token);
        // validate token
        if (passToken == null) {
            JSONObject response = new JSONObject();
            response.put("msg", "Invalid token.");
            return response.toString();
        }
        if (isTokenExpired(passToken)) {
            tokenRepository.delete(passToken);
            JSONObject response = new JSONObject();
            response.put("msg", "Token invalid and expired");
            return response.toString();
        }
        PasswordResetToken resetToken = tokenRepository.findByResetToken(token);
        User user = resetToken.getUser();

        user.setPassword(encoder.encode(password));
        userRepo.save(user);

        tokenRepository.delete(resetToken);

        JSONObject response = new JSONObject();
        response.put("msg", "Your password successfully updated");
        return response.toString();
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        Calendar cal = Calendar.getInstance();

        return passToken.getExpiryDate().before(cal.getTime());
    }

}
