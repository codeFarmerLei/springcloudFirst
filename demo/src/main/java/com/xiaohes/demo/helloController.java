package com.xiaohes.demo;


import com.xiaohes.feign.ItemClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by lei
 * @date 2019-1-14 19:13
 */
@RestController
public class helloController {

    //@Value("${xiaohes.providerName}")
    //String serviceId;
    //@Autowired
    //private DiscoveryClient discoveryClient;
    //
    //@Autowired
    //private RestTemplate restTemplate;
    //
    //@GetMapping("/hi")
    //public String hi(){
    //    List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
    //    if(instances == null || instances.isEmpty()) {
    //        System.out.println("instances is null ================");
    //        return null;
    //    }
    //    ServiceInstance serviceInstance = instances.get(0);
    //    String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort();
    //    System.out.println("请求url:"+url);
    //    String name = restTemplate.getForObject(url + "/getName", String.class);
    //    return "Hello World!"+name;
    //}

    @Autowired
    ItemClient itemClient;

    //@HystrixCommand(fallbackMethod = "error")
    @GetMapping("/hi")
    public String hiByFeign(){
        return "Hello World!"+itemClient.getName();
    }

    public String error()
    {
        return "item server error!";
    }
}
