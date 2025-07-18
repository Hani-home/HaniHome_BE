package org.hanihome.hanihomebe.global.aop.exception;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Aspect
@Component
@Order(1)
@Profile("!test") //테스트에선 실행하지 않음.
public class ExceptionNotificationAspect {

    private final JavaMailSender mailSender;
    private final List<String> alertEmails;

    @Autowired
    public ExceptionNotificationAspect(JavaMailSender mailSender,
                                       AlertProperties props) {
        this.mailSender   = mailSender;
        this.alertEmails  = props.getEmails();
    }

    @AfterThrowing(
            pointcut = "org.hanihome.hanihomebe.global.aop.common.CommonPointCuts.inServiceLayer()",
            throwing  = "ex"
    )
    public void notifyOnException(JoinPoint jp, Throwable ex) {
        log.info("메일 전송 AOP");
        String signature = jp.getSignature().toShortString();
        String subject   = "[ALERT] Exception in " + signature;
        String body      = String.format(
                "<p><b>Method:</b> %s</p>" +
                        "<p><b>Args:</b> %s</p>" +
                        "<p><b>Exception:</b> %s</p>",
                signature,
                java.util.Arrays.toString(jp.getArgs()),
                ex.toString()
        );

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");
            // List<String> → String[]
            helper.setTo(alertEmails.toArray(new String[0]));
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
        } catch (MessagingException me) {
            org.slf4j.LoggerFactory.getLogger(this.getClass())
                    .error("[ExceptionNotification] 이메일 전송 실패", me);
        }
    }
}
