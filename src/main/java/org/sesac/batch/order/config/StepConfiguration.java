package org.sesac.batch.order.config;

import org.sesac.batch.order.constant.OrderState;
import org.sesac.batch.order.service.OrderDirectDBAdapter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class StepConfiguration {

    @Autowired
    private OrderDirectDBAdapter adapter;

    @Bean
    public Step changeOrderStateStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("changeOrderStateStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    adapter.updateOrderState(OrderState.CANCELED, OrderState.PENDING, 1);
                    return RepeatStatus.FINISHED;
                }, transactionManager).build();
    }

    @Bean
    public Step deleteOrderStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("deleteOrderStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    adapter.deleteOrder(OrderState.CANCELED, 1);
                    return RepeatStatus.FINISHED;
                }, transactionManager).build();
    }
}
