package my.self.spring.applicationContext;

import my.self.spring.beanDefinition.AnnotateBeanDefinitionReader;
import my.self.spring.beanDefinition.BeanDefinitionRegistry;

public class AnnotationConfigApplicationContext extends GenericApplicationContext
        implements BeanDefinitionRegistry {
    private AnnotateBeanDefinitionReader reader;

    //如果有人调用这个无参构造，必须先调用父类的无参构造，父类初始化DefaultListableBeanFactory
    public AnnotationConfigApplicationContext() {
        this.reader = new AnnotateBeanDefinitionReader(this);
    }
    public AnnotationConfigApplicationContext(Class<?> componentClass) {
        //1、读componentClass 也就是我们的扫描路径 所在的类 AppConfig
        //专门读取AnnotateBeanDefinitionReader
        this();
        //2、先把这个类AppConfig 注册到bean工厂里
        //（BeanDefinition+registerBeanDefinition+FactoryBean）
        register(componentClass);
        //3、扫描这个路径，然后提取出这个路径下所有的bean，然后组册到bean工厂（单例bean的初始化）
        //refresh方法作为核心方法，需要放到父类中，让所有的子类都能使用。
        refresh();
    }
    private void register(Class<?> componentClass){
        this.reader.register(componentClass);
    }



//    @Override
//    public void registerBeanDefinition(String beanName, AnnotateBeanDefinition beanDefinition) {
//        //这段代码没删，导致没有注册进去
////        this.
//    }
}
