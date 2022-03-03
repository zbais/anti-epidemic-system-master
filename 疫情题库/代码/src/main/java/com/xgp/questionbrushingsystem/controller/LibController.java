package com.xgp.questionbrushingsystem.controller;

import com.xgp.questionbrushingsystem.model.Error;
import com.xgp.questionbrushingsystem.model.Lib;
import com.xgp.questionbrushingsystem.model.User;
import com.xgp.questionbrushingsystem.service.LibService;
import com.xgp.questionbrushingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping("/lib")
@Controller
public class LibController extends BaseController {

    @RequestMapping("/searchLib")
    public String searchLib(String sealib,HttpSession session) {
        //1,调用方法，进行模糊查询
        List<Lib> seaLibs = null;
        if(sealib != null && !"".equals(sealib)) {
            seaLibs = libService.searchLib(sealib);
        }
        //2,存值
        session.setAttribute("seaLibs",seaLibs);

        return "redirect:/searchlib2";
    }

//    @ResponseBody
    @RequestMapping("/randLib")
    public String randLib(HttpSession session) {
        List<Lib> libs = libService.randLib(10);
        session.setAttribute("randPage",libs);
        return "redirect:/randpage";
    }

    @RequestMapping("/orderLib")
    public String orderLib(HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        Integer lastindex = user.getLastindex();
        //到时候点击提交时，更改session中的用户信息
        List<Lib> libs = libService.orderLib(lastindex,10);
        session.setAttribute("orderPage",libs);

        //查询题目总数
        int countLib = libService.countLib();
        session.setAttribute("countLib",countLib);
        return "redirect:/orderpage";
    }

    @ResponseBody
    @RequestMapping("/trueORfalse")
    public int trueORfalse(String[] list,String flag, HttpSession session) {
        //list 从前端传过来的  flag 标识是顺序还是乱序
//        System.out.println("判断程序访问了");

        //0,取出当前用户
        User user = (User) session.getAttribute("loginUser");

        if("rand".equals(flag)) {
            //1,取出session中的题目集和
            List<Lib> libs = (List) session.getAttribute("randPage");
            if(libs == null || list == null) {
                return 0;
            }
            //2，与list数组判断对错
            List<Error> errors = isTrueOrFalse(libs,list,user);
            //3,修改用户信息（总做题数和错题数）
            user.setAllquestioncount(user.getAllquestioncount() + list.length);
            user.setErrorquestioncount(user.getErrorquestioncount() + errors.size());
            userService.updateByPrimaryKeySelective(user);
            //更新session中的用户信息
            session.setAttribute("loginUser",user);
            for (Error error : errors) {
                //4，查询该用户是否有该错题（u_id l_id）
                int num = errorService.selectByTwoId(error.getuId(),error.getlId());
                //5,更新错题
                if(num == 0) {
                    errorService.insertSelective(error);
                }else {
                    errorService.updateErrorcount(error.getuId(),error.getlId());
                }
            }
            //6,将要添加的错题空间保存到session中
//            session.setAttribute("errorLibs",errors);
            //7，根据错题id查询错题的全部信息
            List<Lib> showErrors = new ArrayList<>();
            for (Error error : errors) {
                for (Lib lib : libs) {
                    if(lib.getlId().equals(error.getlId())) {
                        showErrors.add(lib);
                    }
                }
            }
            session.setAttribute("showRandErrors",showErrors);
            //7,跳转页面
            return 1;
        }

        if("order".equals(flag)) {
            //1,取出session中的题目集和
            List<Lib> libs = (List) session.getAttribute("orderPage");
            if(libs == null || list == null) {
                return 0;
            }
            //2，与list数组判断对错
            List<Error> errors = isTrueOrFalse(libs,list,user);
            //3,修改用户信息（总做题数和错题数）
            user.setAllquestioncount(user.getAllquestioncount() + list.length);
            user.setErrorquestioncount(user.getErrorquestioncount() + errors.size());
            //修改最后刷题索引
            if(list.length - errors.size() >= 6) {
                int countLib = (int) session.getAttribute("countLib");
                int newindex = user.getLastindex()+list.length;
                if(newindex <= countLib) {
                    user.setLastindex(newindex);
                }else {
                    user.setLastindex(newindex - countLib);
                }
            }
            userService.updateByPrimaryKeySelective(user);
            session.setAttribute("loginUser",user);
            for (Error error : errors) {
                //4，查询该用户是否有该错题（u_id l_id）
                int num = errorService.selectByTwoId(error.getuId(),error.getlId());
                //5,更新错题
                if(num == 0) {
                    errorService.insertSelective(error);
                }else {
                    errorService.updateErrorcount(error.getuId(),error.getlId());
                }
            }
            //6,将要添加的错题空间保存到session中
//            session.setAttribute("errorLibs",errors);
            //7，根据错题id查询错题的全部信息
            List<Lib> showErrors = new ArrayList<>();
            for (Error error : errors) {
                for (Lib lib : libs) {
                    if(lib.getlId().equals(error.getlId())) {
                        showErrors.add(lib);
                    }
                }
            }
            session.setAttribute("showOrderErrors",showErrors);
            //7,跳转页面
            return 1;
        }

        return 0;
    }

    private List<Error> isTrueOrFalse(List<Lib> libs, String[] list,User user) {
        List<Error> errors = new ArrayList<>();
        for(int i = 0;i < list.length;i++) {
            if(!libs.get(i).getAnswer().equals(list[i])) {
                Error error = new Error(user.getuId(),libs.get(i).getlId(),list[i]);
                errors.add(error);
            }
        }
        return errors;
    }
}
