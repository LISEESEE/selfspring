package my.self.test;

import my.self.spring.applicationContext.AnnotationConfigApplicationContext;
import my.self.test.config.AppConfig;

public class SpringTest {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(AppConfig.class);

        System.out.println(applicationContext.getBean("userService"));
        System.out.println(applicationContext.getBean("userService"));

        System.out.println(applicationContext.getBean("userService1"));
        System.out.println(applicationContext.getBean("userService1"));
    }
}
