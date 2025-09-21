package co.com.pragma.sqs.sender.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws.adapter.sqs.save.report")
public record SQSReportesProperties(
        String region,
        String queueUrl,
        String endpoint) {
}
