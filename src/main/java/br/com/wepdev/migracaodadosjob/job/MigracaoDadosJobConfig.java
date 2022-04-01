package br.com.wepdev.migracaodadosjob.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;


@EnableBatchProcessing
@Configuration
public class MigracaoDadosJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public Job migracaoDadosJob(
            @Qualifier("migrarPessoasStep") Step migrarPessoasStep,
            @Qualifier("migrarDadosBancariosStep") Step migrarDadosBancariosStep) {

        return jobBuilderFactory
                .get("migracaoDadosJob")
                .start(stepsParalelos(migrarPessoasStep, migrarDadosBancariosStep)) // Metodo para rodar 2 steps em paralelo, os steps sao independentes
                .end() // Finaliza o fluxo apos rodar os steps em paralelo
                //.start(migrarPessoasStep) // Sao duas fontes de dados lidas, sera um step para pessoas e outro para dados bancarios. Roda primeiro esse step
                //.next(migrarDadosBancariosStep) // Roda esse step depois do primeiro
                .incrementer(new RunIdIncrementer())
                .build();
    }

    private Flow stepsParalelos(Step migrarPessoasStep, Step migrarDadosBancariosStep) {
        Flow migrarDadosBancariosFlow = new FlowBuilder<Flow>("migrarDadosBancariosFlow")
                .start(migrarDadosBancariosStep)
                .build();

        Flow stepsParalelos = new FlowBuilder<Flow>("stepsParalelosFlow")
                .start(migrarPessoasStep)
                .split(new SimpleAsyncTaskExecutor()) // Divide a tarefa ao executar os 2 steps
                .add(migrarDadosBancariosFlow)
                .build();

        return stepsParalelos;
    }

}
