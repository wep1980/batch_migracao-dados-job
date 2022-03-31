package br.com.wepdev.migracaodadosjob.step;

import br.com.wepdev.migracaodadosjob.dominio.Pessoa;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MigrarPessoasStepConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;


    @Bean
    public Step migrarPessoasStep(
            ItemReader<Pessoa> arquivoPessoaReader,
            ClassifierCompositeItemWriter<Pessoa> pessoaClassifierWrite, // Classificador para utlização do escritor correto
            FlatFileItemWriter<Pessoa> arquivoPessoasInvalidasWriter) {

        return stepBuilderFactory
                .get("migrarPessoasStep")
                .<Pessoa, Pessoa>chunk(1)
                .reader(arquivoPessoaReader)
                .writer(pessoaClassifierWrite)
                .stream(arquivoPessoasInvalidasWriter)
                .build();
    }

}
