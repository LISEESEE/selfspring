package my.self.spring.applicationContext;

import my.self.spring.beanDefinition.AnnotateBeanDefinition;
import my.self.spring.beanDefinition.BeanDefinitionRegistry;
import my.self.spring.beanFactory.DefaultListableBeanFactory;



public class GenericApplicationContext implements BeanDefinitionRegistry {
    private DefaultListableBeanFactory beanFactory;

    public GenericApplicationContext() {
        this.beanFactory = new DefaultListableBeanFactory();
    }

    @Override
    public void registerBeanDefinition(String beanName, AnnotateBeanDefinition beanDefinition) {
        this.beanFactory.registerBeanDefinition(beanName,beanDefinition);
    }
    protected void refresh(){
        //获取bean工厂
        DefaultListableBeanFactory beanFactory = obtainBeanFactory();
        //把appConfig路径下的所有的bean进行扫描，注册到bean工厂beanDefinitionMap（UserService和UserService1）
        invokeBeanFactoryPostProcessors(beanFactory);
        //初始化beanDefinition所代表的单例bean，放到单例bean的容器（缓存）里
        finishBeanFactoryInitialization(beanFactory);
    }
    private void invokeBeanFactoryPostProcessors(DefaultListableBeanFactory beanFactory) {
        //这部分啊，我们进行了一个简化的类，doScan方法并没有在beanFactory里
        beanFactory.doScan();
    }
    private void finishBeanFactoryInitialization(DefaultListableBeanFactory beanFactory) {
        beanFactory.preInstantiateSingleton();
    }



    private DefaultListableBeanFactory obtainBeanFactory() {
        return this.beanFactory;
    }

    public Object getBean(String beanName) {
        return this.beanFactory.getBean(beanName);
    }
}
