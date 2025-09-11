package co.com.pragma.sqs.sender.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adapter.sqs1")
public record SQSSenderProperties(
     String region,
     String queueUrl,
     String endpoint){
}
