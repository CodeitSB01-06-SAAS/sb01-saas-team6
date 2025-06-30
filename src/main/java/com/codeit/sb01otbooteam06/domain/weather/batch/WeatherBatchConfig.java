package com.codeit.sb01otbooteam06.domain.weather.batch;

import com.codeit.sb01otbooteam06.domain.weather.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class WeatherBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;

    private final WeatherRepository weatherRepository;

}
