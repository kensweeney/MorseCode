package com.ken.camel.opencv;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;


public class CamelMain extends RouteBuilder {
    private static Main main = null;
    public static void main(String[] args) {
        try {
            main = new Main();
            CamelMain routeBuilder = new CamelMain();
            main.configure().addRoutesBuilder(routeBuilder);
            //main.run(new String[] {"-trace"});
            main.run(args);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    public CamelMain() {
        // TODO Auto-generated constructor stub
    }
    @Override
    public void configure() throws Exception {
        from("file:E:/morse/image?")
        .process(new OpenCVProcessor())
        .log("${body}");
        
    }

}
