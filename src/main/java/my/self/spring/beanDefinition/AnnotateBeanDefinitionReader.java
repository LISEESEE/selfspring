package my.self.spring.beanDefinition;

import my.self.spring.annotation.Scope;

public class AnnotateBeanDefinitionReader {
    private BeanDefinitionRegistry registry;

    public AnnotateBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    //注册我们的路径扫描 这个bean到bean工厂里
    public void register(Class<?> componentClass) {
        registerBean(componentClass);
    }

    private void registerBean(Class<?> componentClass) {
        doRegisterBean(componentClass);
    }

    private void doRegisterBean(Class<?> componentClass) {
        //把appConfig 读取一个 BeanDefinition定义
        AnnotateGeniricBeanDefinition beanDefinition =
                new AnnotateGeniricBeanDefinition();
        beanDefinition.setClazz(componentClass);
        if (componentClass.isAnnotationPresent(Scope.class)){
            String scope = componentClass.getAnnotation(Scope.class).value();
            beanDefinition.setScope(scope);
        } else{
            beanDefinition.setScope("singleton");
        }
        //beanDefinition 创建完成后，是不是得给beanFactory进行bean注册了呀？
        BeanDefinitionUtils.registerBeanDefinition(beanDefinition,this.registry);
    }
}
