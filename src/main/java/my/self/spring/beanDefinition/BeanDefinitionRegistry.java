package my.self.spring.beanDefinition;

public interface BeanDefinitionRegistry {
    void registerBeanDefinition
            (String beanName,AnnotateBeanDefinition beanDefinition);
}
