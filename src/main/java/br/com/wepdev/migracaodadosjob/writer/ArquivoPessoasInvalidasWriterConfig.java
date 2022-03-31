package br.com.wepdev.migracaodadosjob.writer;

import br.com.wepdev.migracaodadosjob.dominio.Pessoa;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
public class ArquivoPessoasInvalidasWriterConfig {


    @Bean
    public FlatFileItemWriter<Pessoa> arquivoPessoasInvalidasWriter(){

        return new FlatFileItemWriterBuilder<Pessoa>()
                .name("arquivoPessoasInvalidasWriter")
                .resource(new FileSystemResource("files/pessoas_invalidas.csv"))
                .delimited()
                .names("id") // Somente o Id da pessoa sera escrito para identificar a pessoa invalida
                .build();
    }
}
