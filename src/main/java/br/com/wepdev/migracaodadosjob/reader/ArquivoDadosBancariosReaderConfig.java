package br.com.wepdev.migracaodadosjob.reader;

import br.com.wepdev.migracaodadosjob.dominio.DadosBancarios;
import br.com.wepdev.migracaodadosjob.dominio.Pessoa;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.validation.BindException;

import java.util.Date;

@Configuration
public class ArquivoDadosBancariosReaderConfig {


    @Bean
    public FlatFileItemReader<DadosBancarios> dadosBancariosReader(){

        return new FlatFileItemReaderBuilder<DadosBancarios>()
                .name("dadosBancariosReader")
                .resource(new FileSystemResource("files/dados_bancarios.csv"))
                .delimited()
                .names("pessoaId", "agencia", "conta", "banco", "id")
                .addComment("--") // No arquivo pessoas.csv existem uma linha com comentario, entao o -- sera ignorado no momento da leitura
                .targetType(DadosBancarios.class) // O mapeamento sera feiro para a classe Pessoa, como existem campos especificos como a Data sera feito o mapeador de campos para ser feito os castings necessarios
                //.fieldSetMapper(fieldSetMapper()) // Mapeador de campos
                .build();

    }

}
