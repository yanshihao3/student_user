package com.ysh.action;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.ysh.bean.Message;

import java.util.HashMap;
import java.util.List;

/**
 * @author 闫世豪
 * @date 2019/11/10 10:37 上午
 * @email whynightcode@gmail.com
 */
public class ShowController extends Controller {
    public void index() {
        String name = getPara("username");
        setAttr("username", name);
        render("show.jsp");
    }

    //查询成绩
    public void selectInfo() {

        String name = getPara("username");
        String sql = "SELECT * FROM student_grade WHERE name=? order by create_time desc";
        List<Record> grades = Db.find(sql, name);
        renderJson(grades);
    } //查询成绩

    //查询量化积分
    public void selectTeacher_score() {

        String name = getPara("username");
        String sql = "SELECT\n" +
                "\tteacher_users.personnel,\n" +
                "\tteacher_score.* ,\n" +
                "\t(work+teaching_plan+job+teaching+attendance+evaluation+exam+mentality+award+performance+bonus_point) sum\n" +
                "FROM\n" +
                "\tteacher_users,\n" +
                "\tteacher_score \n" +
                "WHERE\n" +
                "\tteacher_users.id = teacher_score.teacher_users_id \n" +
                "\tAND teacher_users.personnel = ? order by create_time desc";
        List<Record> grades = Db.find(sql, name);
        renderJson(grades);
    }

    //查询工资
    public void selectSalary() {
        Message message = new Message();
        String sql = "SELECT * FROM teacher_users WHERE personnel = ?";
        String name = getPara("username");
        String date = getPara("date");
        Record user = Db.findFirst(sql, name);

        String sql_salary = "SELECT\n" +
                "\tteacher_users.personnel,\n" +
                "\tteacher_users.card,\n" +
                "\tteacher_salary.* \n" +
                "FROM\n" +
                "\tteacher_users,\n" +
                "\tteacher_salary \n" +
                "WHERE\n" +
                "\tteacher_users.id = teacher_salary.teacher_users_id \n" +
                "\tAND teacher_users.personnel = ? \n" +
                "\tAND date_format( teacher_salary.create_time, '%Y-%m' ) = date_format( ?, '%Y-%m' ) ";

        List<Record> salarys = Db.find(sql_salary, name,date);
        double money_salary = 0;
        double money_all = 0;
        if (salarys != null) {
            for (int i = 0; i < salarys.size(); i++) {
                Record record = salarys.get(i);
                double total = total(record);
                money_salary += total;
                record.set("total", total);
            }
        }

        String sql_other = "SELECT\n" +
                "\tteacher_users.personnel,\n" +
                "\tteacher_users.card,\n" +
                "\tteacher_other.* \n" +
                "FROM\n" +
                "\tteacher_users,\n" +
                "\tteacher_other \n" +
                "WHERE\n" +
                "\tteacher_users.id = teacher_other.teacher_users_id \n" +
                "\tAND teacher_users.personnel = ? \n" +
                "\tAND date_format( teacher_other.create_time, '%Y-%m' ) = date_format( ?, '%Y-%m' )";

        List<Record> others = Db.find(sql_other, name,date);
        double money_other = 0;
        if (others != null) {
            for (Record other : others) {
                Double money = other.getDouble("money");
                if (money != null) {
                    money_other += money;
                }
            }
        }
        money_all = money_other + money_salary;
        message.setSuccess(true);
        message.setObject(salarys);
        message.setObject1(others);
        HashMap<String, Double> map = new HashMap<>();
        map.put("money_all", money_all);
        map.put("money_other", money_other);
        map.put("money_salary", money_salary);
        message.setObject2(map);
        message.setObject3(user);
        //总计
        renderJson(message);
    }

    public double total(Record record) {
        double sum = 0;
        String[] colums = {"administrative_office", "grade_money", "day_shift", "night_shift"
                , "test", "tutor", "pass_card", "read", "self_performance", "teacher_performance"
                , "monthly_test_performance", "final_performance", "heating_subsidy", "bonus"
                , "award", "help_poor", "people", "title_performance"};
        for (String colum : colums) {
            Double aDouble = record.getDouble(colum);
            if (aDouble != null) {
                sum += aDouble;
            }
        }
        return sum;
    }

}
