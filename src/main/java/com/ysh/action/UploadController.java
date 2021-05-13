package com.ysh.action;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.ysh.bean.Message;
import com.ysh.excel.ExcelSet;
import com.ysh.excel.ExcelSheet;
import com.ysh.utils.ExcelUtil;

import java.util.Date;
import java.util.List;

/**
 * @author 闫世豪
 * @date 2019/11/10 1:09 上午
 * @email whynightcode@gmail.com
 */
public class UploadController extends Controller {


    public void index() {
        render("upload.html");
    }

    //上传成绩表
    public void upload() {
        Message message = new Message();
        try {
            UploadFile file = getFile();
            if (file != null) {
                boolean excelFilename = isExcelFilename(file.getFileName());
                if (excelFilename) {
                    String type = getPara("type");
                    ExcelSet excelSet = ExcelUtil.resolveExcel(file.getFile());
                    List<ExcelSheet> sheets = excelSet.getSheets();
                    file.getFile().delete();
                    if (sheets.size() > 0) {
                        ExcelSheet excelSheet = sheets.get(0);
                        List<List<String>> content = excelSheet.getContent();
                        if (content.size() > 0) {
                            List<String> titles = content.get(0); //表头
                            for (int i = 1; i < content.size(); i++) { //每一行
                                List<String> row = content.get(i);
                                Record record = new Record();
                                for (int j = 0; j < row.size(); j++) {
                                    setValue(record, titles.get(j), row.get(j), type);
                                }
                                String s = row.get(0);
                                if (!s.equals("")) {
                                    save(record, type);
                                }
                            }
                        }
                    }

                    message.setSuccess(true);
                    message.setMsg("解析成功");
                } else {
                    message.setSuccess(false);
                    message.setMsg("上传格式出错");
                }
            } else {
                message.setSuccess(false);
                message.setMsg("未选择文件");
            }

        } catch (Exception e) {
            e.printStackTrace();
            message.setSuccess(false);
            message.setMsg("解析失败");
        }
        renderJson(message);
    }

    private void save(Record record, String type) {
        record.set("create_time", new Date());
        switch (type) {
            case "score":
                Db.save("student_grade", record);
                break;
            case "teacher":
                String sql = "SELECT * FROM teacher_users WHERE personnel = ?";
                Record teacher = Db.findFirst(sql, record.getStr("personnel"));
                if (teacher == null) {
                    Db.save("teacher_users", record);
                } else {
                    record.set("id", teacher.getLong("id"))
                            .set("update_time", new Date());
                    Db.update("teacher_users", record);
                }
                break;
            case "salary":
                Db.save("teacher_salary", record);
                break;
            case "other":
                Db.save("teacher_other", record);
                break;
            case "teacher_score":
                Db.save("teacher_score", record);
                break;
            default:
                break;
        }
    }

    private void setValue(Record record, String title, String value, String type) {
        switch (type) {
            case "score":
                setScoreValue(record, title, value);
                break;
            case "teacher":
                setTeacherValue(record, title, value);
                break;
            case "salary":
                setSalaryValue(record, title, value);
                break;
            case "other":
                setOtherValue(record, title, value);
                break;
            case "teacher_score":
                setTeacherScoreValue(record, title, value);
                break;
            default:
                break;
        }
    }

    private void setSalaryValue(Record record, String title, String value) {
        String sql = "SELECT * FROM teacher_users WHERE personnel=?";
        String key = "";
        title = title.trim();
        value = value.trim();
        switch (title) {
            case "人员":
                key = "teacher_users_id";
                Record user = Db.findFirst(sql, value);
                if (user == null) {
                    user = new Record();
                    user.set("personnel", value)
                            .set("password", "123456")
                            .set("create_time", new Date());
                    Db.save("teacher_users", user);
                }
                value = String.valueOf(user.getLong("id"));
                break;
            case "科室":
                key = "administrative_office";
                break;
            case "年级类型":
                key = "grade_type";
                break;
            case "年级金额":
                key = "grade_money";
                break;
            case "白班":
                key = "day_shift";
                break;
            case "夜班":
                key = "night_shift";
                break;
            case "小测":
                key = "test";
                break;
            case "导师制":
                key = "tutor";
                break;
            case "过卡":
                key = "pass_card";
                break;
            case "早读":
                key = "read";
                break;
            case "各年级部自筹绩效":
                key = "self_performance";
                break;
            case "教研绩效":
                key = "teacher_performance";
                break;
            case "月考绩效":
                key = "monthly_test_performance";
                break;
            case "期末绩效":
                key = "final_performance";
                break;
            case "取暖补助":
                key = "heating_subsidy";
                break;
            case "高考奖金":
                key = "bonus";
                break;
            case "教师节奖励":
                key = "award";
                break;
            case "扶贫下乡":
                key = "help_poor";
                break;
            case "三民一促":
                key = "people";
                break;
            case "教师职称绩效":
                key = "title_performance";
                break;
            default:
                break;
        }
        record.set(key, value);
    }

    /**
     * 根据不同的表头 进行不同的赋值
     *
     * @param record
     * @param title
     * @return
     */
    private void setScoreValue(Record record, String title, String value) {
        String key = "";
        title = title.trim();
        value = value.trim();
        switch (title) {
            case "级部":
                key = "grade";
                break;
            case "类别":
                key = "xtype";
                break;
            case "班级":
                key = "class";
                break;
            case "考号":
                key = "number";
                break;
            case "姓名":
                key = "name";
                break;
            case "总分":
                key = "total_score";
                break;
            case "语文":
                key = "language";
                break;
            case "数学":
                key = "mathematics";
                break;
            case "英语":
                key = "english";
                break;
            case "物理":
                key = "physics";
                break;
            case "化学":
                key = "chemistry";
                break;
            case "生物":
                key = "biology";
                break;
            case "政治":
                key = "politics";
                break;
            case "历史":
                key = "history";
                break;
            case "地理":
                key = "geography";
                break;
            case "综合":
                key = "synthesize";
                break;
            case "全体排名":
                key = "xorder";
                break;
            default:
                break;
        }
        record.set(key, value);
    }

    private void setTeacherValue(Record record, String title, String value) {
        String key = "";
        title = title.trim();
        value = value.trim();
        switch (title) {
            case "人员":
                key = "personnel";
                break;
            case "卡号":
                key = "card";
                break;
            default:
                break;
        }
        record.set("password", "123456");
        record.set(key, value);
    }

    private void setOtherValue(Record record, String title, String value) {
        String sql = "SELECT * FROM teacher_users WHERE personnel=?";
        String key = "";
        title = title.trim();
        value = value.trim();
        switch (title) {
            case "人员":
                key = "teacher_users_id";
                Record user = Db.findFirst(sql, value);
                if (user == null) {
                    user = new Record();
                    user.set("personnel", value)
                            .set("password", "123456")
                            .set("create_time", new Date());
                    Db.save("teacher_users", user);
                }
                value = String.valueOf(user.getLong("id"));
                break;
            case "类型":
                key = "xtype";
                break;
            case "金额":
                key = "money";
                break;
            case "时间":
                key = "time";
                break;
            default:
                break;
        }
        record.set(key, value);
    }

    //教师量化积分表
    private void setTeacherScoreValue(Record record, String title, String value) {
        String sql = "SELECT * FROM teacher_users WHERE personnel=?";
        String key = "";
        title = title.trim();
        value = value.trim();
        switch (title) {
            case "姓名":
                key = "teacher_users_id";
                Record user = Db.findFirst(sql, value);
                if (user == null) {
                    user = new Record();
                    user.set("personnel", value)
                            .set("password", "123456")
                            .set("create_time", new Date());
                    Db.save("teacher_users", user);
                }
                value = String.valueOf(user.getLong("id"));
                break;
            case "时间":
                key = "time";
                break;
            case "工作量积分":
                key = "work";
                break;
            case "教案":
                key = "teaching_plan";
                break;
            case "作业":
                key = "job";
                break;
            case "教研":
                key = "teaching";
                break;
            case "考勤":
                key = "attendance";
                break;
            case "评教":
                key = "evaluation";
                break;
            case "文化考试":
                key = "exam";
                break;
            case "大局意识":
                key = "mentality";
                break;
            case "管理人员奖励":
                key = "award";
                break;
            case "教学成绩":
                key = "performance";
                break;
            case "奖励加分":
                key = "bonus_point";
                break;
            default:
                break;
        }
        record.set(key, value);
    }

    /**
     * 判断Excel文件后缀名是否正确
     */
    private boolean isExcelFilename(String filename) {
        String ext = filename.substring(filename.lastIndexOf(".") + 1);
        String[] exts = {"xlsx", "xls"};
        for (String e : exts) {
            if (ext.equals(e)) {
                return true;
            }
        }
        return false;
    }

}
