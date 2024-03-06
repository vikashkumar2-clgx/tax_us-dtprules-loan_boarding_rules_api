package com.corelogic.tax.tpd.taxservicingrulesapi.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BeanRefreshListener {
    @Autowired
    ApplicationContext applicationContext;
    @EventListener
    public void onRefreshScopeRefreshed(final RefreshScopeRefreshedEvent event) {
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            Class<?> beanClass = applicationContext.getBean(beanName).getClass();
            applicationContext.getBean(beanName).getClass(); // to cause refresh eagerly
        }
    }
}
