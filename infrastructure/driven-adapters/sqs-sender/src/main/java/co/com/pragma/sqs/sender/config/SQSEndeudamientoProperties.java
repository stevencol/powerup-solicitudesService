package co.com.pragma.sqs.sender.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws.adapter.sqs.endeudamiento")
public record SQSEndeudamientoProperties(
     String region,
     String queueUrl,
     String endpoint){
}
