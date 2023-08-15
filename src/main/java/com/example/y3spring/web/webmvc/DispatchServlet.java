package com.example.y3spring.web.webmvc;

import com.example.y3spring.annotation.YRequestMapping;
import com.example.y3spring.context.ApplicationContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class DispatchServlet extends HttpServlet {

    private final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";

    private ApplicationContext context;

    private List<HandlerMapping> handlerMappings = new ArrayList<HandlerMapping>();

    private Map<HandlerMapping, HandlerAdapter> handlerAdapters = new HashMap<HandlerMapping, HandlerAdapter>();

    private List<ViewResolver> viewResolvers = new ArrayList<ViewResolver>();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            // 向页面打印错误
            resp.getWriter().println("500 Exception,Details:\r\n" + Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]", "").replaceAll(",\\s", "\r\n"));
            e.printStackTrace();
        }
    }

    // 请求分发 --> 通过反射执行相应方法 --> 对结果进行解析与渲染
    /*
        到这里init方法已经执行过，即已经创建好了
            1.处理请求的handler ---> 实质上是把把 IOC 容器管理的Bean实例进行了封装（包括代理对象的替换），并建立了映射关系
            2.请求参数适配及执行handler的Adapter
            3.对处理结果进行视图转换的 ViewResolver，及 ViewResolver内自定义的模本解析引擎
        所以这里处理请求其实只需要拿出相应组件即可
     */
    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        // 1.通过从Request中拿到URL，去匹配一个HandlerMapping
        HandlerMapping handler = getHandler(req);
        if (handler == null) {
            processDispatchResult(req, resp, new ModelAndView("404"));
            return;
        }

        // 2.获取当前handler对应的处理参数的Adapter
        HandlerAdapter handlerAdapter = getHandlerAdaptor(handler);

        // 3.Adapter真正调用处理请求的方法,返回ModelAndView（存储了页面上值，和页面模板的名称）
        // 将 request 进行处理转为 handler的参数并执行
        ModelAndView mv = handlerAdapter.handle(req, resp, handler);

        // 4.真正输出,将方法执行进行处理然后返回
        processDispatchResult(req, resp, mv);

    }

    // 将ModelAndView解析成 HTML、json、outputStream等 --> 然后解析数据 --> 最后输出给前端
    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, ModelAndView mv) throws Exception {
        // null 表示方法返回类型是void，或返回值是null
        if(mv == null) {
            return;
        }

        // 如果没有视图解析器就返回，因为无法处理ModelAndView
        if (this.viewResolvers.isEmpty()) {
            return;
        }

        // 遍历视图解析器
        for (ViewResolver viewResolver : this.viewResolvers) {
            // 通过相应解析器，返回相应页面 View
            View view = viewResolver.resolveViewName(mv.getViewName(), null);
            // View通过模板引擎（自定义的）解析后输出
            view.render(mv.getModel(), req, resp);
            return;
        }
    }

    private HandlerAdapter getHandlerAdaptor(HandlerMapping handler) {
        if (this.handlerAdapters.isEmpty()) {
            return null;
        }
        HandlerAdapter handlerAdapter = this.handlerAdapters.get(handler);
        // 判断当前handler能否被当前adaptor进行适配（即将传入参数转换成该handler的参数，并处理）
        if (handlerAdapter.supports(handler)) {
            return handlerAdapter;
        }
        return null;
    }

    public void init(ApplicationContext context) throws ServletException {
        //初始化SpringMVC九大组件
        initStrategies(context);
    }

    // 通过Request获取相应handler
    private HandlerMapping getHandler(HttpServletRequest req) {
        if (this.handlerMappings.isEmpty()) return null;

        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");

        for (HandlerMapping handler : this.handlerMappings) {
            Matcher matcher = handler.getPattern().matcher(url);
            // 如果没有匹配上就继续遍历handler
            if (!matcher.matches()) {
                continue;
            }
            return handler;
        }
        return null;
    }


    protected void initStrategies(ApplicationContext context) {
        //多文件上传的组件
        initMultipartResolver(context);
        //初始化本地语言环境
        initLocaleResolver(context);
        //初始化模板处理器
        initThemeResolver(context);
        //handlerMapping，必须实现
        initHandlerMappings(context);
        //初始化参数适配器，必须实现
        initHandlerAdapters(context);
        //初始化异常拦截器
        initHandlerExceptionResolvers(context);
        //初始化视图预处理器
        initRequestToViewNameTranslator(context);
        //初始化视图转换器，必须实现
        initViewResolvers(context);
        //参数缓存器
        initFlashMapManager(context);
    }

    private void initFlashMapManager(ApplicationContext context) {

    }

    private void initViewResolvers(ApplicationContext context) {

        // 拿到模板存放路径(layouts)
        String templateRoot = context.getConfig().getProperty("templateRoot");
        // getResource返回的是URL对象
        // getFile返回文件的绝对路径
        // 即通过相对路径找到目标后，获取到绝对路径
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();

        File templateRootDir = new File(templateRootPath);
        String[] templates = templateRootDir.list();
        // 模板引擎可以有多种，且不同的模板需要不同的Resolver去解析成不同的View（jsp，html，json。。）

        for (int i = 0; i < templates.length; i ++) {
            this.viewResolvers.add(new ViewResolver(templateRoot));
        }
    }

    private void initRequestToViewNameTranslator(ApplicationContext context) {

    }

    private void initHandlerExceptionResolvers(ApplicationContext context) {
    }

    private void initHandlerAdapters(ApplicationContext context) {
        for (HandlerMapping handlerMapping : this.handlerMappings) {
            this.handlerAdapters.put(handlerMapping, new HandlerAdapter());
        }
    }

    private void initHandlerMappings(ApplicationContext context) {
        // 一个BeanDefinition对应一个Bean,依次拿到
        String[] beanNames = context.getBeanDefinitionNames();

        try {
            for (String beanName : beanNames) {
                Object controller = context.getBean(beanName);

                Class<?> clazz = controller.getClass();

                if (!clazz.isAnnotationPresent(Controller.class)) {
                    continue;
                }

                // 获取当期Controller的共有url
                String baseUrl = "";
                if (clazz.isAnnotationPresent(YRequestMapping.class)) {
                    YRequestMapping annotation = clazz.getAnnotation(YRequestMapping.class);
                    baseUrl = annotation.value();
                }

                // 获取所有方法的处理路径
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (!method.isAnnotationPresent(YRequestMapping.class)) {
                        continue;
                    }

                    YRequestMapping annotation = method.getAnnotation(YRequestMapping.class);
                    String regex = ("/" + baseUrl + "/" + annotation.value().replaceAll("\\*", ".*")).replaceAll("/+", "/");
                    Pattern pattern = Pattern.compile(regex);
                    // 构建处理器，并加入handlerMapping
                    this.handlerMappings.add(new HandlerMapping(controller, method, pattern));
                    log.info("Mapped " + regex + "," + method);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initThemeResolver(ApplicationContext context) {

    }

    private void initLocaleResolver(ApplicationContext context) {

    }

    private void initMultipartResolver(ApplicationContext context) {

    }
}
