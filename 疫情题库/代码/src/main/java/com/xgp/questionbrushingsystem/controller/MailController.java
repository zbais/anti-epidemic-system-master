package com.xgp.questionbrushingsystem.controller;

import com.xgp.questionbrushingsystem.tool.CheckTool;
import com.xgp.questionbrushingsystem.tool.CodeText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.util.Timer;
import java.util.TimerTask;

@RestController
@RequestMapping("/mail")
public class MailController {
    @Autowired
    JavaMailSender mailSender;

//    @ResponseBody
    @RequestMapping("/send")
    public void sendEmail( String email,HttpSession session) {
        try {
            final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setFrom("2043928998@qq.com");
            System.out.println(email);
            if(email != null && CheckTool.checkEmail(email) == true) {
                message.setTo(email);
                message.setSubject("在线刷题系统");

                //验证码生成规则
                String relcode = CodeText.MyMethod1();
                message.setText("你当前使用的在线刷题系统的验证码为：" + relcode
                            + "\n" + "请在5分钟内尽快验证");

                //将验证码存过入session
                String attrName = "emailCode"+email;
                session.setAttribute(attrName,relcode);
                this.mailSender.send(mimeMessage);

                //5分钟后清除session
                this.removeAttrbute(session,attrName);
//                return "sucesss";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
//            return "error";
        }
    }

    /**
     * 设置5分钟后删除session中的验证码
     * @param session
     * @param attrName
     */
    private void removeAttrbute(final HttpSession session, final String attrName) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 删除session中存的验证码
                session.removeAttribute(attrName);
                timer.cancel();
            }
        }, 5 * 60 * 1000);
    }

}
