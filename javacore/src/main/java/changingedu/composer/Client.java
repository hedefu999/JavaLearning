package changingedu.composer;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

public class Client {
    @Test
    public void test5() {
        List<Student> students = Lists.newArrayList(
                new Student(123L,234L,12,"jack"),
                new Student(345L,456L,13,"lucy")
        );
        List<Teacher> teachers = Lists.newArrayList(
                new Teacher("2345","WANG"),
                new Teacher("9867","CHEN")
        );
        //两个使用Lambda的
        List<Long> idCardNos = CollectionUtils.extractFields(students, Student::getIdCardNo);
        System.out.println(idCardNos);
        List<String> teacherNames = CollectionUtils.extractFields(teachers, Teacher::getName);
        System.out.println(teacherNames);
        //正常的用法
        List<Long> stuIDs = CollectionUtils.extractFields(students, Student.STU_ID_EXTRACTOR);
        System.out.println(stuIDs);
        //错误的设计
        List<String> strings = CollectionUtils.extractFields(teachers, new Teacher("", ""));
        System.out.println(strings);
    }
}
