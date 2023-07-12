package next.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;

import next.optional.User;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.assertj.core.api.Assertions.*;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);
    private static final int PRIVATE_NUM = 2;

    @Test
    @DisplayName("테스트1: 리플렉션을 이용해서 클래스와 메소드의 정보를 정확하게 출력해야 한다.")
    public void showClass() {
        SoftAssertions s = new SoftAssertions();
        Field[] declaredFields = Question.class.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            logger.debug("filed = {}", declaredField);
        }
        Method[] declaredMethods = Question.class.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            logger.debug("method = {}", declaredMethod);
        }
        Constructor<?>[] declaredConstructors = Question.class.getDeclaredConstructors();
        for (Constructor<?> declaredConstructor : declaredConstructors) {
            logger.debug("constructor = {}", declaredConstructor);
        }

        assertThat(declaredFields.length).isEqualTo(6);
        assertThat(declaredConstructors.length).isEqualTo(2);
        assertThat(declaredMethods.length).isEqualTo(11);

    }

    @Test
    public void constructor() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();
            logger.debug("paramer length : {}", parameterTypes.length);
            for (Class paramType : parameterTypes) {
                logger.debug("param type : {}", paramType);
            }
        }
    }

    @Test
    public void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException {
        Class<Student> clazz = Student.class;

        Field name = clazz.getDeclaredField("name");
        Field age = clazz.getDeclaredField("age");

        name.setAccessible(true);
        age.setAccessible(true);

        Student student = new Student();
        name.set(student, "김우혁");
        age.set(student, 26);

        assertThat(student.getAge()).isEqualTo(26);
        assertThat(student.getName()).isEqualTo("김우혁");
    }

    @Test
    public void createUserInstance() throws Exception {
        Class<User> clazz = User.class;

        User user = clazz.getDeclaredConstructor(String.class, Integer.class).newInstance("김우혁", 26);

        assertThat(user.getName()).isEqualTo("김우혁");
        assertThat(user.getAge()).isEqualTo(26);
    }

    @Test
    public void timeTest() throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<TimeTester> clazz = TimeTester.class;
        int count = 0;
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.isAnnotationPresent(ElapsedTime.class)) {
                long start = System.currentTimeMillis();
                declaredMethod.invoke(clazz.newInstance());
                long end = System.currentTimeMillis();
                count++;
                System.out.println(declaredMethod.getName()+" 의 실행에 걸린 시간 : " + (end - start));
            }
        }

        assertThat(count).isEqualTo(1);
    }
}
