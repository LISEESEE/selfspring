spring源码分析中自己写的项目
一、 想要书写简易spring源码，首先要搞明白BeanFactory，BeanDefinition，ApplicationContext之间的关系。
BeanFactory：工厂，生产bean，提供获取bean的方法 getBean方法
        生产bean的话，是不是得解析我们的注解@Service，一个bean可能是单例的，也可能是多例的
BeanDefinition：bean定义。String scope（单例singleton，多例prototype）；
                             Class clazz（Object.class）代表当前bean属于哪个class
                             他就是生产bean的原料

ApplicationContext：容器（上下文）。他要主导BeanDefinition的生成，把BeanDefinition
        "传递"（注册，beanDefinition注册，beanDefinitionRegister（方法：registerBeanDefinition））
        给BeanFactory生产bean。 getBean方法不是ApplicationContext里边的方法吗？是的，这个getBean方法
        如果你倒一倒源码的话吗，你就会发现，他是最终调用的BeanFactory的getBean