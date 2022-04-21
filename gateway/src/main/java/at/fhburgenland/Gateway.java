package at.fhburgenland;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Stefan Gass, Isabella Zaby
 * @version 1.0
 * <p>
 * This class establishes an API Gateway as central access point for external applications and
 * implements a load balancer to distribute the load of accesses to different servers or instances of a program.
 * @since April 2022
 */

@SpringBootApplication
@EnableDiscoveryClient
public class Gateway {

    public static void main(String[] args) {
        SpringApplication.run(Gateway.class, args);
    }

}