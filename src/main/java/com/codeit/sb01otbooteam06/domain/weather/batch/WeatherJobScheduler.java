package com.codeit.sb01otbooteam06.domain.weather.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class WeatherJobScheduler {

    private final Job weatherChunkJob;
    private final JobLauncher jobLauncher;

    /** 3시간마다 10분 30초 지점에 실행 (KST) */
    @Scheduled(cron = "0 45 15 * * *", zone = "Asia/Seoul")
    public void run() throws Exception {
        long ts = System.currentTimeMillis();
        log.info("⏰ Launching job ts={}", ts);
        jobLauncher.run(
            weatherChunkJob,
            new JobParametersBuilder()
                .addLong("ts", System.currentTimeMillis())
                .toJobParameters());
    }
}
