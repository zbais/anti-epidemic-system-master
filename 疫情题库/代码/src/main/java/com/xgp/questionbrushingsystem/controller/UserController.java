package com.xgp.questionbrushingsystem.controller;

import com.google.code.kaptcha.Constants;
import com.xgp.questionbrushingsystem.model.User;
import com.xgp.questionbrushingsystem.service.UserService;
import com.xgp.questionbrushingsystem.tool.CheckTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

@RequestMapping("/user")
@Controller
public class UserController extends BaseController {


    /**
     * 在注册时，鼠标失去焦点，判断用户名是否以在数据库中
     * @param username      用户名
     * @return  数据库有该用户名，放回1，不允许注册。没有，放回0，允许注册
     */
//    @ResponseBody
//    @RequestMapping("/usernameisonly")
    public int usernameisonly( String username) {
        int flag = userService.selectByUsername(username);
        return flag;
    }

    /**
     * 在注册时，鼠标失去焦点，判断email是否以在数据库中
     * @param email     用户名
     * @return  数据库有该邮箱，放回1，不允许注册。没有，放回0，允许注册
     * 如果是找回密码，放回1，允许找回。没有，放回0，不允许找回
     */
//    @ResponseBody
//    @RequestMapping("/emailisonly")
    public int emailisonly(String email) {
        int flag = userService.selectByEmail(email);
        return flag;
    }

    /**
     * 用户注册方法，成功时返回1，失败放回0
     * @param user  从前端传入的注册数据封装到user对象中
     * @return
     */
    @ResponseBody
    @RequestMapping("/register")
    public String register(User user,String code,HttpSession session) {
        String email = user.getEmail();
        String username = user.getUsername();
        String password = user.getPassword();
        String phone = user.getPhone();

        System.out.println(code);

        if(email != null && username != null && password != null && phone != null && code != null) {
            String attrName = "emailCode"+email;
            String realcode = (String) session.getAttribute(attrName);

            if(code.length() < 4 || !realcode.equals(code)) {
                return "验证码不正确";
                //
            }
            if(CheckTool.checkEmail(email) == false) {
                return "邮箱格式不正确";
            }
            if(CheckTool.checkMobileNumber(phone) == false) {
                return "手机号码格式不正确";
            }
            if(emailisonly(email) == 1) {
                return "该邮箱以注册，请更换";
            }
            if(usernameisonly(username) == 1) {
                return "该用户名以存在，请更换";
            }

            int flag = userService.insertSelective(user);
            if(flag == 1) {
                return "success";
            }else {
                return "更改失败";
            }

        }else {
            return "表单信息不完全";
        }



    }

    /**
     * 根据用户名和密码查询该用户的所有信息，并修改最后登陆时间,如果该用户登陆失败，放回null
     * @param username
     * @return
     */
    @ResponseBody
    @RequestMapping("/loginMethod")
    public int loginMethod(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("code") String code,
                           HttpSession session) {

        String realcode = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
//        System.out.println(realcode);

        if(code != null && !code.isEmpty() && realcode.equals(code)) {

//        System.out.println(username);
//        System.out.println(password);
            //1,查询该用户是否存在，放回id
            int id = userService.login(username,password);

            if(id == 0) {
                //用户名密码错误
                return 0;
            }

            if(id != 0) {
                //2，如果存在，根据查询该用户所有信息
                User user = userService.selectByPrimaryKey(id);
                //3,修改最后登陆时间
                Date time = new Date();
                user.setLastlogintime(time);
                userService.updateByPrimaryKeySelective(user);
                //将用户信息存到session中
                session.setAttribute("loginUser",user);
                return 1;
            }
        }

        return 2;
    }

    /**
     * 根据主键更新用户
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping("/update")
    public int update(User user,HttpSession session) {
        if(user.getUsername() != null && user.getPassword() != null && (user.getUsername().isEmpty() || user.getPassword().isEmpty())) {
            return 0;
        }else {
            if(user.getPassword() != null && !user.getPassword().isEmpty()) {
                //如果有password,则清除session
                session.invalidate();
            }

        }

        if(user.getEmail() != null && user.getPhone() != null) {
            if(CheckTool.checkEmail(user.getEmail()) == false || CheckTool.checkMobileNumber(user.getPhone()) == false) {
                return 2;
            }
        }

        int flag = userService.updateByPrimaryKeySelective(user);
        return flag;
    }

    /**
     * 找回密码，通过邮箱修改用户名和密码
     * @param email
     * @param username
     * @param password
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateByEmail")
    public String updateByEmail(@RequestParam("email") String email,
                             @RequestParam("username") String username,
                             @RequestParam("password") String password,
                             @RequestParam("code") String code,
                             HttpSession session) {
        if(email != null && username != null && password != null && code != null) {
            String attrName = "emailCode"+email;
            String realcode = (String) session.getAttribute(attrName);

            if(code.length() < 4 || !realcode.equals(code)) {
                return "验证码不正确";
                //
            }
            if(CheckTool.checkEmail(email) == false) {
                return "邮箱格式不正确";
            }
            if(emailisonly(email) == 0) {
                return "该邮箱未注册，请注册";
            }
            if(usernameisonly(username) == 1) {
                return "该用户名以存在，请更换";
            }

            int flag = userService.updateByEmail(email,username,password);
            if(flag == 1) {
                return "success";
            }else {
                return "更改失败";
            }

        }else {
            return "表单信息不完全";
        }

    }

    @RequestMapping("clearSession")
    public String clearSession(HttpSession session) {
        session.invalidate();
        return "redirect:/login.html";
    }

}
