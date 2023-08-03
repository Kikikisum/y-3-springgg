package com.example.y3spring.context.event;

import com.example.y3spring.context.ApplicationEvent;
import com.example.y3spring.context.ApplicationListener;

import java.util.concurrent.Executor;

public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster{

    /**
     * 异步处理任务的自定义线程池
     */
    private Executor taskExecutor;

    @Override
    public void multicastEvent(ApplicationEvent event) {
        for (ApplicationListener<?> listener : applicationListeners) {
            //todo 这里的判断逻辑可以优化 可以先将event解析成type 再使用type去检查
            if(supportsEvent(listener,event)){
                // 查看是否有自定义的线程池
                Executor executor = getTaskExecutor();

                // 有则异步处理事件
                if(executor != null){
                    executor.execute(()->{
                        invokeListener(listener,event);
                    });

                }else {
                    // 无则同步处理事件
                    invokeListener(listener,event);
                }
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void invokeListener(ApplicationListener listener, ApplicationEvent event){
        listener.onApplicationEvent(event);
    }

    @Override
    public void addTaskExecutor(Executor executor) {
        this.taskExecutor = executor;
    }

    protected Executor getTaskExecutor(){
        return this.taskExecutor;
    }
}
