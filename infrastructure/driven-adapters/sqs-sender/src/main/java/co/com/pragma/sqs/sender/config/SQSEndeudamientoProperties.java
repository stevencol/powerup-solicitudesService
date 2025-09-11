package co.com.pragma.sqs.sender.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adapter.sqs2")
public record SQSEndeudamientoProperties(
     String region,
     String queueUrl,
     String endpoint){
}
