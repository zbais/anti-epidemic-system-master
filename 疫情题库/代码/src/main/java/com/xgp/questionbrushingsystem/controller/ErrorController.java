package com.xgp.questionbrushingsystem.controller;

//import com.sun.deploy.net.HttpResponse;
import com.xgp.questionbrushingsystem.model.Lib;
import com.xgp.questionbrushingsystem.model.User;
import com.xgp.questionbrushingsystem.model.tool.PageBean;
import com.xgp.questionbrushingsystem.model.tool.QuestionAllmessage;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@RequestMapping("/error")
@Controller
public class ErrorController extends BaseController {

//    @ResponseBody
    @RequestMapping("/addRemarks")
    public String addRemarks(Integer uid,Integer lid,String remarks) {
        //到数据库中修改备注
        errorService.addRemarks(uid,lid,remarks);
        return "redirect:/showranderror";
    }

    @RequestMapping("/addRemarks2")
    public String addRemarks2(Integer uid,Integer lid,String remarks) {
        //到数据库中修改备注
        errorService.addRemarks(uid,lid,remarks);
        return "redirect:/errorspace";
    }

    @RequestMapping("/randErrorLib")
    public String randLib(HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        Integer uid = user.getuId();
        List<Lib> libs = errorService.randLib(10,uid);
        if(libs.size() < 10) {
            return "redirect:/tip1";
        }
        session.setAttribute("randPage",libs);
        return "redirect:/randpage2";
    }

//    @ResponseBody
    @RequestMapping("/exportMyExcel")
    public void exportMyExcel(HttpSession session, HttpServletResponse response) throws IOException {
        //1，创建workbook【相当于xls文件】
        HSSFWorkbook workbook = new HSSFWorkbook();
        //2，创建sheet
        HSSFSheet sheet = workbook.createSheet("错题数据");
        //3,创建标题行
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("题库id");
        row.createCell(1).setCellValue("题目类型");
        row.createCell(2).setCellValue("题目详情");
        row.createCell(3).setCellValue("A选项");
        row.createCell(4).setCellValue("B选项");
        row.createCell(5).setCellValue("C选项");
        row.createCell(6).setCellValue("D选项");
        row.createCell(7).setCellValue("正确答案");
        row.createCell(8).setCellValue("难度系数");
        row.createCell(9).setCellValue("上次错答");
        row.createCell(10).setCellValue("错误次数");
        row.createCell(11).setCellValue("错题笔记");
        row.createCell(12).setCellValue("作废标记");

        //4，查询数据
        //4.1 查询当前用户，两表查询
        User user = (User) session.getAttribute("loginUser");
        List<QuestionAllmessage> twoAllMsg =  errorService.selectTwoAllMessage(user.getuId());
        for (QuestionAllmessage msg : twoAllMsg) {
            row = sheet.createRow(sheet.getLastRowNum() + 1);
            row.createCell(0).setCellValue(msg.getlId());
            row.createCell(1).setCellValue(msg.getType());
            row.createCell(2).setCellValue(msg.getQuestion());
            row.createCell(3).setCellValue(msg.getA());
            row.createCell(4).setCellValue(msg.getB());
            row.createCell(5).setCellValue(msg.getC());
            row.createCell(6).setCellValue(msg.getD());
            row.createCell(7).setCellValue(msg.getAnswer());
            row.createCell(8).setCellValue(msg.getDifficulty());
            row.createCell(9).setCellValue(msg.getLastErrorAnswer());
            row.createCell(10).setCellValue(msg.getErrorcount());
            row.createCell(11).setCellValue(msg.getRemarks());
            row.createCell(12).setCellValue(msg.getFlag());
        }

        //响应【输出流】
        //设置响应头
        String fileName = URLEncoder.encode("MyErrors.xls","utf-8");
        response.setHeader("Content-Disposition","attachment;filename=" + fileName);
        //根据文件名获取文件类型
        String contentType = session.getServletContext().getMimeType(fileName);
//        response.setContentType("application/octet-stream");
        response.setContentType(contentType);
//        System.out.println(contentType);


        OutputStream os = response.getOutputStream();
        workbook.write(os);
    }


    @RequestMapping("/exportAllExcel")
    public void exportAllExcel(HttpSession session, HttpServletResponse response) throws IOException {
        //1，创建workbook【相当于xls文件】
        HSSFWorkbook workbook = new HSSFWorkbook();
        //2，创建sheet
        HSSFSheet sheet = workbook.createSheet("错题数据");
        //3,创建标题行
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("用户名");
        row.createCell(1).setCellValue("题库id");
        row.createCell(2).setCellValue("题目类型");
        row.createCell(3).setCellValue("题目详情");
        row.createCell(4).setCellValue("A选项");
        row.createCell(5).setCellValue("B选项");
        row.createCell(6).setCellValue("C选项");
        row.createCell(7).setCellValue("D选项");
        row.createCell(8).setCellValue("正确答案");
        row.createCell(9).setCellValue("难度系数");
        row.createCell(10).setCellValue("上次错答");
        row.createCell(11).setCellValue("错误次数");
        row.createCell(12).setCellValue("错题笔记");
        row.createCell(13).setCellValue("作废标记");

        //4，查询数据
        //4.1 查询当前用户，两表查询
        User user = (User) session.getAttribute("loginUser");
        List<QuestionAllmessage> twoAllMsg =  errorService.selectAllMessage();
        for (QuestionAllmessage msg : twoAllMsg) {
            row = sheet.createRow(sheet.getLastRowNum() + 1);
            row.createCell(0).setCellValue(msg.getUsername());
            row.createCell(1).setCellValue(msg.getlId());
            row.createCell(2).setCellValue(msg.getType());
            row.createCell(3).setCellValue(msg.getQuestion());
            row.createCell(4).setCellValue(msg.getA());
            row.createCell(5).setCellValue(msg.getB());
            row.createCell(6).setCellValue(msg.getC());
            row.createCell(7).setCellValue(msg.getD());
            row.createCell(8).setCellValue(msg.getAnswer());
            row.createCell(9).setCellValue(msg.getDifficulty());
            row.createCell(10).setCellValue(msg.getLastErrorAnswer());
            row.createCell(11).setCellValue(msg.getErrorcount());
            row.createCell(12).setCellValue(msg.getRemarks());
            row.createCell(13).setCellValue(msg.getFlag());
        }

        //响应【输出流】
        //设置响应头
        String fileName = URLEncoder.encode("AllErrors.xls","utf-8");
        response.setHeader("Content-Disposition","attachment;filename=" + fileName);
        //根据文件名获取文件类型
        String contentType = session.getServletContext().getMimeType(fileName);
//        response.setContentType("application/octet-stream");
        response.setContentType(contentType);
//        System.out.println(contentType);


        OutputStream os = response.getOutputStream();
        workbook.write(os);
    }

//    @ResponseBody
    @RequestMapping("/errorsPage")
    public String errorsPage(String currentPage,Integer flag,HttpSession session) {
        //1,获取用户信息
        User user = (User) session.getAttribute("loginUser");
        Integer uid = user.getuId();
        //2,设置每页显示的条数
        String rows = "10";
        if(currentPage == null || "".equals(currentPage)) {
            currentPage = "1";
        }
        //3,调用service查询
        PageBean<QuestionAllmessage> pb = errorService.findErrorsSpaceByPage(currentPage,rows,uid,flag);
        //4,将pb存入session中
        session.setAttribute("pb",pb);
        //5,放回数据（跳转）
        if(flag == 1) {
            return "redirect:/errorspace";
        }
        if(flag == 0) {
            return "redirect:/errorsbin";
        }
        return null;
    }

    @RequestMapping("/deleteError")
    public String deleteError(Integer uid,Integer lid,String currentPage,String rows,Integer flag,HttpSession session) {
        //1,--> 0
        //0,--> 1
        //2,--> 2
        if(currentPage == null || "".equals(currentPage)) {
            currentPage = "1";
        }
        if(rows == null || "".equals(rows)) {
            rows = "10";
        }
        //1,更新作废标记
        Integer newflag = 0;
        if(flag == 1) newflag = 0;
        if(flag == 0) newflag = 1;
        if(flag == 2) newflag = 2;
        errorService.updateFlag(uid,lid,newflag);
        if(flag == 1) {
            //3,调用service查询
            PageBean<QuestionAllmessage> pb = errorService.findErrorsSpaceByPage(currentPage,rows,uid,flag);
            //4,将pb存入session中
            session.setAttribute("pb",pb);
            //5,放回数据（跳转）
            return "redirect:/errorspace";
        }
        if(flag == 0 || flag == 2) {
//            if(flag == 0) {
                //3,调用service查询
                PageBean<QuestionAllmessage> pb = errorService.findErrorsSpaceByPage(currentPage,rows,uid,0);
                //4,将pb存入session中
                session.setAttribute("pb",pb);
//            }
            return "redirect:/errorsbin";
        }
        return null;
    }
}
